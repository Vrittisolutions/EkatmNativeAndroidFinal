package com.vritti.crm.vcrm7;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.crm.adapter.CallLogDetailsAdapter;
import com.vritti.crm.bean.CalendarCollection;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.CallReceiverIntentService;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class CRM_CallLogList extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    String UserType, Token;
    RecyclerView list_calllogs;
    public String IsChatApplicable;
    public String IsCollectionApplicable;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    static String Salesmodulevisible = "true";
    static String Salesmoduleinvisible = "false";
    private String settingKey, dabasename;
    String contactedMobNo = "", usermasterid = "", callType = "", startTime = "", endTime = "", duration = "", rowNo = "",
            callingType = "";
    int editRowPos;
    String FinalJSOnObject = "", UsernameRecord = "";
    TextView txt_type_status, txt_statusdetails;
    LinearLayout ln_button;
    Button btn_opporunity, btn_collection, btn_save, btn_cancel;
    LinearLayout ln_txtvalue, ln_spinner, ln_savebutton;

    CallLogDetailsAdapter callLogDetailsAdapter;
    ArrayList<CallLogsDetails> callLogsDetailsArrayList;
    Spinner spinner_calltype;
    ArrayList<PartialCallList> partialCallLists;
    ProgressBar progressbar_1;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    private String ContactName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_calllog_list);


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, "");
        IsCollectionApplicable = userpreferences.getString(WebUrlClass.USERINFO_ISCOLLECTION_APPLICABLE, Salesmodulevisible);

        //CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();


        /*StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .detectLeakedSqlLiteObjects()
                .penaltyDeath()
                .penaltyLog()
                .build();
        StrictMode.setVmPolicy(policy);*/


        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();
        InitView();
        SetListener();








       /* Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();
        count1 = count1 + 1;
        Log.e("Call Log Count", "" + count1);*/

       /* sql.delete(db.TABLE_CALL_LOG, null,
                null);*/
      /*  db.addCallLogsDetails(UserMasterId, UserName, "9657711698", "2018-10-09 21:51:58.000",
                "2018-10-09 22:09:22.000", "00:10:51", "Incoming", count1,"");
        db.addCallLogsDetails(UserMasterId, UserName, "7057411246", "2018-10-09 21:51:58.000",

                "2018-10-09 22:09:22.000", "00:10:51", "Incoming", count1+1,"");*/



        if (getIntent() != null) {
            // contactedMobNo = getIntent().getStringExtra("MobileNumber");
            // String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  Mobile ='" + contactedMobNo + "'";
            getDatafromLocalDatabase();


        }




    }

    private void getDatafromLocalDatabase() {
       callLogsDetailsArrayList.clear();

        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG +" Order by StartTime DESC limit 100", null);
        int count = c.getCount();

        if (count > 0) {
            c.moveToFirst();
            do {
                CallLogsDetails callLogsDetails = new CallLogsDetails();

                callLogsDetails.setUserMasterId(c.getString(c.getColumnIndex("UserMasterId")));
                callLogsDetails.setUserName(c.getString(c.getColumnIndex("UserMasterName")));
                callLogsDetails.setNumber(c.getString(c.getColumnIndex("MobileNo")));
                callLogsDetails.setStartTime(c.getString(c.getColumnIndex("StartTime")));
                callLogsDetails.setEndTime(c.getString(c.getColumnIndex("EndTime")));
                callLogsDetails.setDuration(c.getString(c.getColumnIndex("Duration")));
                callLogsDetails.setCallType(c.getString(c.getColumnIndex("MobileCallType")));
                callLogsDetails.setRowNo(c.getString(c.getColumnIndex("RowNo")));
                callLogsDetails.setContactPersonName(c.getString(c.getColumnIndex("ContactPersonName")));
                callLogsDetails.setCustomerName(c.getString(c.getColumnIndex("CustomerName")));
                String mobile = c.getString(c.getColumnIndex("MobileNo"));
                String Contactname=getContactName(CRM_CallLogList.this,mobile);
                callLogsDetails.setUsername(Contactname);

                String query1 = "SELECT FirmName FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  Mobile ='" + mobile + "'";
                Cursor c1 = sql.rawQuery(query1, null);
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        callLogsDetails.setFirmname(c1.getString(c1.getColumnIndex("FirmName")));
                    } while (c1.moveToNext()) ;

                }

                if(callLogsDetails.getCallType().equalsIgnoreCase("incoming") ||
                        callLogsDetails.getCallType().equalsIgnoreCase("outgoing") ||
                        callLogsDetails.getCallType().equalsIgnoreCase("Opportunity/collection")
                ||callLogsDetails.getCallType().equalsIgnoreCase("Opportunity")) {
                    callLogsDetailsArrayList.add(callLogsDetails);
                }
            } while (c.moveToNext());

            for(int i=0;i<callLogsDetailsArrayList.size();i++){
                if(callLogsDetailsArrayList.get(i).getCallType().equalsIgnoreCase("incoming") ||
                        callLogsDetailsArrayList.get(i).getCallType().equalsIgnoreCase("outgoing")){
                    String no = callLogsDetailsArrayList.get(i).getNumber();

                    String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  Mobile ='" + no + "'";
                    Cursor cur1 = sql.rawQuery(query1, null);
                    String query2 = "SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  Mobile ='" + no + "'";
                    Cursor cur2 = sql.rawQuery(query2, null);

                    String query3 = "SELECT * FROM " + db.TABLE_CALL_LOG + " WHERE  MobileNo ='" + no + "'";
                    Cursor cur3 = sql.rawQuery(query3, null);


                    if(cur1.getCount() > 0 && cur2.getCount() > 0){
                        CallLogsDetails callLogsDetails = new CallLogsDetails();
                        callLogsDetails.setCallType("Opportunity/Collection");
                        callLogsDetails.setUserMasterId(callLogsDetailsArrayList.get(i).getUserMasterId());
                        callLogsDetails.setUserName(callLogsDetailsArrayList.get(i).getUserName());
                        callLogsDetails.setNumber(callLogsDetailsArrayList.get(i).getNumber());
                        callLogsDetails.setStartTime(callLogsDetailsArrayList.get(i).getStartTime());
                        callLogsDetails.setEndTime(callLogsDetailsArrayList.get(i).getEndTime());
                        callLogsDetails.setDuration(callLogsDetailsArrayList.get(i).getDuration());
                        callLogsDetails.setRowNo(callLogsDetailsArrayList.get(i).getRowNo());
                        callLogsDetails.setContactPersonName(callLogsDetailsArrayList.get(i).getContactPersonName());
                        callLogsDetails.setCustomerName(callLogsDetailsArrayList.get(i).getCustomerName());
                        callLogsDetails.setFirmname(callLogsDetailsArrayList.get(i).getFirmname());
                        String Contactname=getContactName(CRM_CallLogList.this,no);
                        callLogsDetails.setUsername(Contactname);

                        callLogsDetailsArrayList.remove(i);
                        callLogsDetailsArrayList.add(i,callLogsDetails);
                    }
                    else if(cur1.getCount() > 0){
                        CallLogsDetails callLogsDetails = new CallLogsDetails();
                        callLogsDetails.setCallType("Opportunity");
                        callLogsDetails.setUserMasterId(callLogsDetailsArrayList.get(i).getUserMasterId());
                        callLogsDetails.setUserName(callLogsDetailsArrayList.get(i).getUserName());
                        callLogsDetails.setNumber(callLogsDetailsArrayList.get(i).getNumber());
                        callLogsDetails.setStartTime(callLogsDetailsArrayList.get(i).getStartTime());
                        callLogsDetails.setEndTime(callLogsDetailsArrayList.get(i).getEndTime());
                        callLogsDetails.setDuration(callLogsDetailsArrayList.get(i).getDuration());
                        callLogsDetails.setRowNo(callLogsDetailsArrayList.get(i).getRowNo());
                        callLogsDetails.setContactPersonName(callLogsDetailsArrayList.get(i).getContactPersonName());
                        callLogsDetails.setCustomerName(callLogsDetailsArrayList.get(i).getCustomerName());
                        callLogsDetails.setFirmname(callLogsDetailsArrayList.get(i).getFirmname());
                        String Contactname=getContactName(CRM_CallLogList.this,no);
                        callLogsDetails.setUsername(Contactname);
                        callLogsDetailsArrayList.remove(i);
                        callLogsDetailsArrayList.add(i,callLogsDetails);
                    }
                    else if(cur2.getCount() > 0){
                        CallLogsDetails callLogsDetails = new CallLogsDetails();
                        callLogsDetails.setCallType("collection");
                        callLogsDetails.setUserMasterId(callLogsDetailsArrayList.get(i).getUserMasterId());
                        callLogsDetails.setUserName(callLogsDetailsArrayList.get(i).getUserName());
                        callLogsDetails.setNumber(callLogsDetailsArrayList.get(i).getNumber());
                        callLogsDetails.setStartTime(callLogsDetailsArrayList.get(i).getStartTime());
                        callLogsDetails.setEndTime(callLogsDetailsArrayList.get(i).getEndTime());
                        callLogsDetails.setDuration(callLogsDetailsArrayList.get(i).getDuration());
                        callLogsDetails.setRowNo(callLogsDetailsArrayList.get(i).getRowNo());
                        callLogsDetails.setContactPersonName(callLogsDetailsArrayList.get(i).getContactPersonName());
                        callLogsDetails.setCustomerName(callLogsDetailsArrayList.get(i).getCustomerName());
                        callLogsDetails.setFirmname(callLogsDetailsArrayList.get(i).getFirmname());
                        String Contactname=getContactName(CRM_CallLogList.this,no);
                        callLogsDetails.setUsername(Contactname);
                        callLogsDetailsArrayList.remove(i);
                        callLogsDetailsArrayList.add(i,callLogsDetails);
                    }
                    else  {

                    }
                }

            }
            callLogDetailsAdapter = new CallLogDetailsAdapter(CRM_CallLogList.this, callLogsDetailsArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            list_calllogs.setLayoutManager(mLayoutManager);
            list_calllogs.setItemAnimator(new DefaultItemAnimator());
            list_calllogs.setAdapter(callLogDetailsAdapter);
        }

    }

    private void SetListener() {

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        list_calllogs = findViewById(R.id.list_calllogs);
        progressbar_1 = findViewById(R.id.progressbar_1);

        callLogsDetailsArrayList = new ArrayList<>();
        partialCallLists = new ArrayList<>();

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

      /*  img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/
        txt_title.setText("Unlinked Phone Calls");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void rowClick(final int adapterPosition) {
        editRowPos = adapterPosition;
        contactedMobNo = callLogsDetailsArrayList.get(adapterPosition).getNumber();
        usermasterid = callLogsDetailsArrayList.get(adapterPosition).getUserMasterId();
        callType = callLogsDetailsArrayList.get(adapterPosition).getCallType();
        startTime = callLogsDetailsArrayList.get(adapterPosition).getStartTime();
        endTime = callLogsDetailsArrayList.get(adapterPosition).getEndTime();
        duration = callLogsDetailsArrayList.get(adapterPosition).getDuration();
        rowNo = callLogsDetailsArrayList.get(adapterPosition).getRowNo();
        ContactName = callLogsDetailsArrayList.get(adapterPosition).getUsername();
        final String uniqueID = UUID.randomUUID().toString();

        String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  Mobile ='" + contactedMobNo + "'";
        Cursor cur1 = sql.rawQuery(query1, null);
        int cnt1 = cur1.getCount();

        String query = "SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + " WHERE  Mobile ='" + contactedMobNo + "'";
        Cursor cur = sql.rawQuery(query, null);
        int cnt = cur.getCount();
        Log.e("Table Crm opportunity", "" + cnt);

        String query2 = "SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  Mobile ='" + contactedMobNo + "'";
        Cursor cur2 = sql.rawQuery(query2, null);
        int cnt2 = cur2.getCount();


        Log.e("Table Crm opportunity", "" + cnt);




        if (cur1.getCount() > 0 && cur2.getCount() > 0) {
            if(callType.equalsIgnoreCase("opportunity")){
                if (cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
                    callid = cur1.getString(cur1.getColumnIndex("CallId"));
                    firmname = cur1.getString(cur1.getColumnIndex("FirmName"));
                    mobile = cur1.getString(cur1.getColumnIndex("Mobile"));
                    callstatus = cur1.getString(cur1.getColumnIndex("CallStatus"));

                    calltype = cur1.getString(cur1.getColumnIndex("CallType"));
                    contactname = cur1.getString(cur1.getColumnIndex("ContactName"));
                    ProspectId = cur1.getString(cur1.getColumnIndex("ProspectId"));
                    ProspectId = cur1.getString(cur1.getColumnIndex("Email"));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserMasterId", UserMasterId);
                        jsonObject.put("MobileNo", contactedMobNo);
                        jsonObject.put("StartTime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                        jsonObject.put("EndTime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                        jsonObject.put("Duration", callLogsDetailsArrayList.get(adapterPosition).getDuration());
                        jsonObject.put("MobileCallType", calltype);
                        jsonObject.put("CallingType", "1");
                        jsonObject.put("CallType", "Opportunity");
                        jsonObject.put("CallId", callid);
                        jsonObject.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Incomingcall Record has been added of Number " + contactedMobNo + " on " + date;
                    String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);
       /*     String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + contactedMobNo +
                    "&StartTime=" + startTime + "&EndTime=" + endTime + "&Duration=" + duration + "&MobileCallType=Outgoing";*/

                    //Personal call //Call id  - kutalya call la attach ahe //category - Opportunity,personal,spam,crm -
                    // url = url.replace(" ", "%20");


                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Opportunity");
                    cv.put("ContactPersonName", contactname);
                    cv.put("CustomerName", firmname);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});


                    String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());




                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "opportunitycall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime",  callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    intent.putExtra("endtime", callLogsDetailsArrayList.get(editRowPos).getEndTime());
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }
            }
            else if(callType.equalsIgnoreCase("collection")){
                if (cur2.getCount() > 0) {
                    cur2.moveToFirst();
                    String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
                    callid = cur2.getString(cur2.getColumnIndex("CallId"));
                    firmname = cur2.getString(cur2.getColumnIndex("FirmName"));
                    mobile = cur2.getString(cur2.getColumnIndex("Mobile"));
                    callstatus = cur2.getString(cur2.getColumnIndex("CallStatus"));

                    calltype = cur2.getString(cur2.getColumnIndex("CallType"));
                    contactname = cur2.getString(cur2.getColumnIndex("ContactName"));
                    ProspectId = cur2.getString(cur2.getColumnIndex("ProspectId"));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserMasterId", UserMasterId);
                        jsonObject.put("MobileNo", contactedMobNo);
                        jsonObject.put("StartTime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                        jsonObject.put("EndTime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                        jsonObject.put("Duration", callLogsDetailsArrayList.get(adapterPosition).getDuration());
                        jsonObject.put("MobileCallType", calltype);
                        jsonObject.put("CallingType", "2");
                        jsonObject.put("CallType", "Collection");
                        jsonObject.put("CallId", callid);
                        jsonObject.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Incomingcall Record has been added of Number " + contactedMobNo + " on " + date;
                    String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);
       /*     String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + contactedMobNo +
                    "&StartTime=" + startTime + "&EndTime=" + endTime + "&Duration=" + duration + "&MobileCallType=Outgoing";*/

                    //Personal call //Call id  - kutalya call la attach ahe //category - Opportunity,personal,spam,crm -
                    // url = url.replace(" ", "%20");

                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Collection");
                    cv.put("ContactPersonName", contactname);
                    cv.put("CustomerName", firmname);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});

                    String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());



                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "Collectioncall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime", sTime);
                    intent.putExtra("endtime", eTime);
                    intent.putExtra("currentdate", callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


          /*  Intent intent = new Intent(CRM_CallLogList.this, OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
            intent.putExtra("endtime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
            intent.putExtra("initiatedby", "Customer");
            intent.putExtra("Callnature", "IN");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);*/
                }
            }

            else {
                ///direct to new activity
              /*  Intent intent = new Intent(CRM_CallLogList.this,CallUpdateActivity.class);
                intent.putExtra("MobileNo",contactedMobNo);
                startActivity(intent);*/
                if (ut.isNet(context)) {
                    progressbar_1.setVisibility(View.VISIBLE);
                    new StartSession(CRM_CallLogList.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetUserMobileCallRecord().execute(contactedMobNo);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }


        //Individual
        else{
            if(callType.equalsIgnoreCase("incoming") || callType.equalsIgnoreCase("outgoing") ||
                    callType.equalsIgnoreCase("Opportunity/collection")){
                if (ut.isNet(context)) {
                    new StartSession(CRM_CallLogList.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetUserMobileCallRecord().execute(contactedMobNo);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }else{
                if (cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
                    callid = cur1.getString(cur1.getColumnIndex("CallId"));
                    firmname = cur1.getString(cur1.getColumnIndex("FirmName"));
                    mobile = cur1.getString(cur1.getColumnIndex("Mobile"));
                    callstatus = cur1.getString(cur1.getColumnIndex("CallStatus"));

                    calltype = cur1.getString(cur1.getColumnIndex("CallType"));
                    contactname = cur1.getString(cur1.getColumnIndex("ContactName"));
                    ProspectId = cur1.getString(cur1.getColumnIndex("ProspectId"));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserMasterId", UserMasterId);
                        jsonObject.put("MobileNo", contactedMobNo);
                        jsonObject.put("StartTime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                        jsonObject.put("EndTime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                        jsonObject.put("Duration", callLogsDetailsArrayList.get(adapterPosition).getDuration());
                        jsonObject.put("MobileCallType", calltype);
                        jsonObject.put("CallingType", "2");
                        jsonObject.put("CallType", "Collection");
                        jsonObject.put("CallId", callid);
                        jsonObject.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Incomingcall Record has been added of Number " + contactedMobNo + " on " + date;
                    String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);

       /*     String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + contactedMobNo +
                    "&StartTime=" + startTime + "&EndTime=" + endTime + "&Duration=" + duration + "&MobileCallType=Outgoing";*/

                    //Personal call //Call id  - kutalya call la attach ahe //category - Opportunity,personal,spam,crm -
                    // url = url.replace(" ", "%20");

                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Opportunity");
                    cv.put("ContactPersonName", contactname);
                    cv.put("CustomerName", firmname);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});



                    String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());


                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "opportunitycall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime",  callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    intent.putExtra("endtime", callLogsDetailsArrayList.get(editRowPos).getEndTime());
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

          /*  Intent intent = new Intent(CRM_CallLogList.this, OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
            intent.putExtra("endtime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
            intent.putExtra("initiatedby", "Customer");
            intent.putExtra("Callnature", "IN");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);*/
                }
                else if (cur2.getCount() > 0) {
                    cur2.moveToFirst();
                    String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
                    callid = cur2.getString(cur2.getColumnIndex("CallId"));
                    firmname = cur2.getString(cur2.getColumnIndex("FirmName"));
                    mobile = cur2.getString(cur2.getColumnIndex("Mobile"));
                    callstatus = cur2.getString(cur2.getColumnIndex("CallStatus"));

                    calltype = cur2.getString(cur2.getColumnIndex("CallType"));
                    contactname = cur2.getString(cur2.getColumnIndex("ContactName"));
                    ProspectId = cur2.getString(cur2.getColumnIndex("ProspectId"));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserMasterId", UserMasterId);
                        jsonObject.put("MobileNo", contactedMobNo);
                        jsonObject.put("StartTime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                        jsonObject.put("EndTime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                        jsonObject.put("Duration", callLogsDetailsArrayList.get(adapterPosition).getDuration());
                        jsonObject.put("MobileCallType", calltype);
                        jsonObject.put("CallingType", "2");
                        jsonObject.put("CallType", "Collection");
                        jsonObject.put("CallId", callid);
                        jsonObject.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Incomingcall Record has been added of Number " + contactedMobNo + " on " + date;
                    String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);
       /*     String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + contactedMobNo +
                    "&StartTime=" + startTime + "&EndTime=" + endTime + "&Duration=" + duration + "&MobileCallType=Outgoing";*/

                    //Personal call //Call id  - kutalya call la attach ahe //category - Opportunity,personal,spam,crm -
                    // url = url.replace(" ", "%20");

                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Collection");
                    cv.put("ContactPersonName", contactname);
                    cv.put("CustomerName", firmname);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});

                   /* String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            startTime);
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            endTime);

*/

                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "Collectioncall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                    intent.putExtra("endtime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                    intent.putExtra("duration", callLogsDetailsArrayList.get(adapterPosition).getDuration());
                    intent.putExtra("MobileCallType", calltype);
                    intent.putExtra("CallingType", "2");
                    intent.putExtra("CallType","Opportunity");
                    intent.putExtra("CallId", callid);
                    intent.putExtra("NewId", uniqueID);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
                else {

                    if (ut.isNet(context)) {
                        new StartSession(CRM_CallLogList.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetUserMobileCallRecord().execute(contactedMobNo);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CRM_CallLogList.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                    // call new API
                    //respoense true
                    /*----Store dtata in call log table*/
            /*if -Oppotunity - Store data in TABLE_CRM_OPPOTUNITY_CALL_FILTER & TABLE_CRM_CALL_PARTIAL
            call Existiing API - Offline data store
            GO TO OpportunityUpdateActviity*/
            /*else if - Collection - Store data  in TABLE_CRM_CALL_PARTIAL
             call Existing API - Offline data store
             GO TO OpportunityUpdateActviity*/
                    /*else ALert Diaog Open */

                    //response false

                    /*******************************/


         /*   AlertDialog.Builder builder = new AlertDialog.Builder(CRM_CallLogList.this);
            LayoutInflater inflater = getLayoutInflater().from(CRM_CallLogList.this);
            final View dialogView = inflater.inflate(R.layout.calllogs_askfor_calltype, null);
            spinner_calltype = dialogView.findViewById(R.id.spinner_calltype);
            btn_cancel = dialogView.findViewById(R.id.btn_cancel);
            btn_save = dialogView.findViewById(R.id.btn_save);
            builder.setView(dialogView);

            builder.setCancelable(false);
            final AlertDialog b = builder.create();
            b.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            String[] calltype1 = getResources().getStringArray(R.array.Calltype);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.vwb_custom_spinner_txt, calltype1);
            spinner_calltype.setAdapter(adapter);


            spinner_calltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    callType = spinner_calltype.getSelectedItem().toString();
                    if(callType.equals("Colleague")){
                        callingType = "3";
                    }else if(callType.equals("Spam")){
                        callingType = "4";
                    }else if(callType.equals("Personal")){
                        callingType = "5";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callType.equals("Select Option")) {
                        Toast.makeText(CRM_CallLogList.this, "Please select call type", Toast.LENGTH_SHORT).show();
                    } else {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("UserMasterId", UserMasterId);
                            jsonObject.put("MobileNo", contactedMobNo);
                            jsonObject.put("StartTime", callLogsDetailsArrayList.get(adapterPosition).getStartTime());
                            jsonObject.put("EndTime", callLogsDetailsArrayList.get(adapterPosition).getEndTime());
                            jsonObject.put("Duration",callLogsDetailsArrayList.get(adapterPosition).getDuration());
                            jsonObject.put("MobileCallType", callType);
                            jsonObject.put("CallingType", callingType);
                            jsonObject.put("CallType", "");
                            jsonObject.put("CallId", "");
                            jsonObject.put("NewId",uniqueID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        FinalJSOnObject = jsonObject.toString();

                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String date = sdf1.format(new Date());
                        String remark1 = "Incomingcall Record has been added of Number " + contactedMobNo + " on " + date;
                        String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                        String op = "true";
                        CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);

                *//*   String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId +
                                "&MobileNo=" + contactedMobNo + "&StartTime=" + startTime + "&EndTime=" + endTime +
                                "&Duration=" + duration + "&MobileCallType=" + callType;
                        url = url.replace(" ", "%20");*//*

                        //CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.GETFlAG, "Call Update", op, CRM_CallLogList.this);

                        //sql.execSQL("UPDATE " + db.TABLE_CALL_LOG + " SET MobileCallType =" + callType + " WHERE RowNo =" + rowNo);
                        ContentValues cv = new ContentValues();
                        cv.put("MobileCallType", callType);
                        sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{rowNo});
                        Toast.makeText(CRM_CallLogList.this, "Call type is updated", Toast.LENGTH_SHORT).show();
                        b.dismiss();
                        getDatafromLocalDatabase();
                    }
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callType = "";
                    b.dismiss();
                }
            });
            b.setCanceledOnTouchOutside(false);
            b.show();*/

                    /*******************************/

                }
            }

        }



    }

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op, Context context) {

        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            //Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(context,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            startService(intent1);
            //CallReceiverIntentService.enqueueWork(context, intent1);

        } else {
            // Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private class GetUserMobileCallRecord extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";
        JSONArray opportunityJsonArray = null;
        JSONArray collectionJsonArray = null;
        JSONArray colleagueJsonArray = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar_1.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            String MobileNo = params[0];
           /* String url = CompanyURL + WebUrlClass.api_GetUserMobileCallRecord +
                    "?MobileNo=" + contactedMobNo+"&UserMasterId="+UserMasterId;
*/
            String url = CompanyURL + WebUrlClass.api_GetUserMobileCallRecord +
                    "?MobileNo=" + contactedMobNo+"&UserMasterId="+UserMasterId;

            res = ut.OpenConnection(url);
            if (res != null) {
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            }

               /* Oppotunity - Store data in TABLE_CRM_OPPOTUNITY_CALL_FILTER & TABLE_CRM_CALL_PARTIAL
                call Existiing API - Offline data store
                GO TO OpportunityUpdateActviity*/


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressbar_1.setVisibility(View.GONE);
            final ContentValues contentValues = new ContentValues();

            try {
                JSONObject jsonObject = new JSONObject(response);
                opportunityJsonArray = jsonObject.getJSONArray("Opportunity");
                collectionJsonArray = jsonObject.getJSONArray("Collection");
                colleagueJsonArray = jsonObject.getJSONArray("Collegaue");
                Log.i("Opportunity", String.valueOf(opportunityJsonArray));
                Log.i("Collection", String.valueOf(collectionJsonArray));
                Log.i("Colleague", String.valueOf(colleagueJsonArray));

                if (colleagueJsonArray.length() != 0) {
                    for (int i = 0; i < colleagueJsonArray.length(); i++) {
                        JSONObject jsonObject1 = colleagueJsonArray.getJSONObject(i);
                        UsernameRecord = jsonObject1.getString("UserName");

                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            if (opportunityJsonArray.length() != 0 && collectionJsonArray.length() != 0) {
                String colleaguejson="";
                String datasheetList = new Gson().toJson(new CallLogsDetails(callLogsDetailsArrayList));
                String opportunityjson = String.valueOf(opportunityJsonArray);
                if(colleagueJsonArray.length() != 0){
                 colleaguejson = String.valueOf(colleagueJsonArray);}
                String collectionjson = String.valueOf(collectionJsonArray);
                Intent intent = new Intent(CRM_CallLogList.this, CallUpdateActivity.class);
                intent.putExtra("list",datasheetList);
                intent.putExtra("opportunityjson",opportunityjson);
                intent.putExtra("colleaguejson",colleaguejson);
                intent.putExtra("collectionjson",collectionjson);
                intent.putExtra("position",editRowPos);
                intent.putExtra("IsBoth","fromOppCollec");
                intent.putExtra("contactname",ContactName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
            else {
                if (opportunityJsonArray.length() != 0) {

                    //Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY, null);
                    int count = c.getCount();
                    String callid = "", callType = "", FirmName = "", prospectId = "", userNameRecord = "";
                    String columnName, columnValue;
                    try {
                        for (int i = 0; i < opportunityJsonArray.length(); i++) {
                            JSONObject jorder = opportunityJsonArray.getJSONObject(i);
                            for (int j = 0; j < c.getColumnCount(); j++) {

                                columnName = c.getColumnName(j);
                                String jsonDOB = jorder.getString("NextActionDateTime");
                                callid = jorder.getString("CallId");
                                callType = jorder.getString("CallType");
                                FirmName = jorder.getString("FirmName");
                                prospectId = jorder.getString("ProspectId");
                                userNameRecord = jorder.getString("ContactName");


                                if (columnName.equalsIgnoreCase("isPartial")) {

                                    contentValues.put(columnName, "P");
                                }
                                else if (columnName.equalsIgnoreCase("Mobile")) {
                                    if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                        contentValues.put(columnName, "No Contact Available");
                                    } else {
                                        columnValue = jorder.getString(columnName);
                                        contentValues.put(columnName, columnValue);
                                    }

                                } else if (columnName.equalsIgnoreCase("ContactName")) {
                                    if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                        contentValues.put(columnName, "");
                                    } else {
                                        columnValue = jorder.getString(columnName);
                                        contentValues.put(columnName, columnValue);
                                    }

                                }  else if (columnName.equalsIgnoreCase("Address")) {
                                    contentValues.put("Address", "");

                                } else if (columnName.equalsIgnoreCase("lat")) {
                                    contentValues.put("lat", "");

                                } else if (columnName.equalsIgnoreCase("Long")) {
                                    contentValues.put("Long", "");

                                }else if (columnName.equalsIgnoreCase("NextMilestone")) {
                                    contentValues.put("NextMilestone", "");

                                }else {
                                    columnValue = jorder.getString(columnName);
                                    contentValues.put(columnName, columnValue);
                                }
                            }
                            //long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL, null, contentValues);
                            long a = sql.insert(db.TABLE_CRM_CALL_OPPORTUNITY, null, contentValues);
                            Log.e("", "" + a);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObject1 = new JSONObject();
                    String uniqueID = UUID.randomUUID().toString();
                    try {
                        jsonObject1.put("UserMasterId", UserMasterId);
                        jsonObject1.put("MobileNo", contactedMobNo);
                        jsonObject1.put("StartTime", callLogsDetailsArrayList.get(editRowPos).getStartTime());
                        jsonObject1.put("EndTime", callLogsDetailsArrayList.get(editRowPos).getEndTime());
                        jsonObject1.put("Duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                        jsonObject1.put("MobileCallType", "Opportunity");
                        jsonObject1.put("CallingType", "1");
                        jsonObject1.put("CallType", "Opportunity");
                        jsonObject1.put("CallId", callid);
                        jsonObject1.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject1.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Call has been added of Number " + contactedMobNo + " on " + date;
                    String url1 = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url1, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);

                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Opportunity");
                    cv.put("ContactPersonName", userNameRecord);
                    cv.put("CustomerName", FirmName);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});

                    String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());

                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "opportunitycall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime",  callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    intent.putExtra("endtime", callLogsDetailsArrayList.get(editRowPos).getEndTime());
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                    /*String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());


                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "opportunitycall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime", sTime);
                    intent.putExtra("endtime", eTime);
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
*/


                }
                else if (collectionJsonArray.length() != 0) {
                    String callid = "", userNameRecord = "", firmName = "", calltype = "", prospectid = "";
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    try {
                        for (int i = 0; i < collectionJsonArray.length(); i++) {
                            JSONObject jorder = collectionJsonArray.getJSONObject(0);
                            for (int j = 0; j < c.getColumnCount(); j++) {

                                columnName = c.getColumnName(j);
                                String jsonDOB = jorder.getString("NextActionDateTime");
                                callid = jorder.getString("CallId");
                                userNameRecord = jorder.getString("ContactName");
                                firmName = jorder.getString("FirmName");
                                calltype = jorder.getString("CallType");
                                prospectid = jorder.getString("ProspectId");
                                if (columnName.equalsIgnoreCase("isPartial")) {

                                    contentValues.put(columnName, "P");
                                } else if (columnName.equalsIgnoreCase("Mobile")) {
                                    if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                        contentValues.put(columnName, "No Contact Available");
                                    } else {
                                        columnValue = jorder.getString(columnName);
                                        contentValues.put(columnName, columnValue);
                                    }

                                } else if (columnName.equalsIgnoreCase("ContactName")) {
                                    if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                        contentValues.put(columnName, "");
                                    } else {
                                        columnValue = jorder.getString(columnName);
                                        contentValues.put(columnName, columnValue);
                                    }

                                }
                                else if (columnName.equalsIgnoreCase("NextMilestone")) {
                                    contentValues.put("NextMilestone", "");

                                }else {
                                    columnValue = jorder.getString(columnName);
                                    contentValues.put(columnName, columnValue);
                                }


                            }
                            long a = sql.insert(db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null, contentValues);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject1 = new JSONObject();
                    String uniqueID = UUID.randomUUID().toString();
                    try {
                        jsonObject1.put("UserMasterId", UserMasterId);
                        jsonObject1.put("MobileNo", contactedMobNo);
                        jsonObject1.put("StartTime", callLogsDetailsArrayList.get(editRowPos).getStartTime());
                        jsonObject1.put("EndTime", callLogsDetailsArrayList.get(editRowPos).getEndTime());
                        jsonObject1.put("Duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                        jsonObject1.put("MobileCallType", "Collection");
                        jsonObject1.put("CallingType", "2");
                        jsonObject1.put("CallType", "Collection");
                        jsonObject1.put("CallId", callid);
                        jsonObject1.put("NewId", uniqueID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FinalJSOnObject = jsonObject1.toString();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String date = sdf1.format(new Date());
                    String remark1 = "Call has been added of Number " + contactedMobNo + " on " + date;
                    String url1 = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                    String op = "true";
                    CreateOfflineIntend(url1, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);

                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", "Collection");
                    cv.put("ContactPersonName", userNameRecord);
                    cv.put("CustomerName", firmName);
                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});


                    /*String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(editRowPos).getEndTime());


                    Intent intent = new Intent(CRM_CallLogList.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "Collectioncall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime", sTime);
                    intent.putExtra("endtime", eTime);
                    intent.putExtra("duration", callLogsDetailsArrayList.get(editRowPos).getDuration());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
*/

                }
                else {
                    String colleaguejson="";
                    String datasheetList = new Gson().toJson(new CallLogsDetails(callLogsDetailsArrayList));
                    String opportunityjson = String.valueOf(opportunityJsonArray);
                    if(colleagueJsonArray.length() != 0){
                        colleaguejson = String.valueOf(colleagueJsonArray);}
                    String collectionjson = String.valueOf(collectionJsonArray);
                    Intent intent = new Intent(CRM_CallLogList.this, CallUpdateActivity.class);
                    intent.putExtra("list",datasheetList);
                    intent.putExtra("opportunityjson",opportunityjson);
                    intent.putExtra("colleaguejson",colleaguejson);
                    intent.putExtra("collectionjson",collectionjson);
                    intent.putExtra("position",editRowPos);
                    intent.putExtra("IsBoth","fromIndi");
                    intent.putExtra("contactname",ContactName);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }
            }

        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDatafromLocalDatabase();
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if(hours > 12){
                hours = hours - 12;
                timeSet = "AM";
            }else if(hours == 12) {
                timeSet ="AM";
            }else{
                timeSet = "PM";
            }

        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

    }


    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

}
