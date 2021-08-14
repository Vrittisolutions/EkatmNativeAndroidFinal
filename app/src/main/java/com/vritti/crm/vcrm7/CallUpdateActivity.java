package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.crm.bean.CalendarCollection;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.expensemanagement.AddExpenseActivity_Next;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

public class CallUpdateActivity extends AppCompatActivity {
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


    ArrayList<CallLogsDetails> callLogsDetailsArrayList = new ArrayList<>();
    int pos;
    String contactedMobNo = "", FinalJSOnObject = "", callType = "", Date = "";
    TextView txt_type_status, txt_statusdetails;
  //  LinearLayout ln_savebutton;
    TextView  btn_opporunity, btn_collection;
    TextInputEditText spinner_calltype;
    String from = "";
    JSONArray opportunityJsonArray = null;
    JSONArray collectionJsonArray = null;
    JSONArray colleagueJsonArray = null;
    String callingType = "", UsernameRecord = "";
    ImageView img_add,img_refresh,img_back,img_nextaction;
    TextView txt_title,txt_durationdetails,txt_add_existing,txt_create_opp,txt_personal,txt_spam,txt_collegue,txt_add_collection_call;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calllogs_askfor_calltype_v2);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, "");
        IsCollectionApplicable = userpreferences.getString(WebUrlClass.USERINFO_ISCOLLECTION_APPLICABLE, Salesmodulevisible);

        CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();


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

        initView();
        setListner();

        if (getIntent() != null) {
            callLogsDetailsArrayList = new Gson().fromJson(getIntent().getStringExtra("list"), CallLogsDetails.class).
                    getCallLogsDetailsArrayList();
            String jsonArray = getIntent().getStringExtra("opportunityjson");
            if (jsonArray != null) {
                try {
                    opportunityJsonArray = new JSONArray(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String jsonArray1 = getIntent().getStringExtra("collectionjson");
            if (jsonArray1 != null) {
                try {
                    collectionJsonArray = new JSONArray(jsonArray1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String jsonArray2 = getIntent().getStringExtra("colleaguejson");
            if (jsonArray2 != null) {
                try {
                    colleagueJsonArray = new JSONArray(jsonArray2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            pos = getIntent().getIntExtra("position", 0);
            contactedMobNo = callLogsDetailsArrayList.get(pos).getNumber();
            from = getIntent().getStringExtra("IsBoth");
            Date = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "dd-MMM",
                    callLogsDetailsArrayList.get(pos).getStartTime());
            callType = callLogsDetailsArrayList.get(pos).getCallType();


            String Date = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "dd-MMM",
                    callLogsDetailsArrayList.get(pos).getStartTime());
            String time = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm",
                    callLogsDetailsArrayList.get(pos).getStartTime());
            String[] startTime = time.split(":");
            int hours = Integer.parseInt(startTime[0]);
            int minutes = Integer.parseInt(startTime[1]);

            String StartTime = updateTime(hours, minutes);


            if (callType.equalsIgnoreCase("outgoing")) {
                //txt_type_status.setText("");
                txt_type_status.setText(contactedMobNo);
                txt_durationdetails.setText(Date + " " + StartTime);
            } else {
                txt_type_status.setText(contactedMobNo);
                txt_durationdetails.setText(Date + " " + StartTime);
            }

            String CallType=callLogsDetailsArrayList.get(pos).getCallType();
            if(CallType.equalsIgnoreCase("Incoming")){
                img_nextaction.setImageResource(R.drawable.colection);
            } if(CallType.equalsIgnoreCase("Outgoing")){
                img_nextaction.setImageResource(R.drawable.notlinked);

            }


            if (from.equalsIgnoreCase("fromOppCollec")) {

            } else {
                if(colleagueJsonArray != null){
                    if (colleagueJsonArray.length() != 0) {
                        for (int i = 0; i < colleagueJsonArray.length(); i++) {
                            JSONObject jsonObject1 = null;
                            try {
                                jsonObject1 = colleagueJsonArray.getJSONObject(i);
                                UsernameRecord = jsonObject1.getString("UserName");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        ContentValues cv = new ContentValues();
                        cv.put("MobileCallType", "Colleague");
                        cv.put("ContactPersonName", UsernameRecord);
                        sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{callLogsDetailsArrayList.get(pos).getRowNo()});
                    }
                }



                if (callLogsDetailsArrayList.get(pos).getCallType().equalsIgnoreCase("Personal")) {
                    callingType = "5";
                    txt_personal.setTextColor(getResources().getColor(R.color.red));
                  //  spinner_calltype.setText("personal");
                } else if (callLogsDetailsArrayList.get(pos).getCallType().equalsIgnoreCase("Spam")) {
                    callingType = "4";
                    txt_spam.setTextColor(getResources().getColor(R.color.red));
                    //  spinner_calltype.setText("spam");
                } else if (callLogsDetailsArrayList.get(pos).getCallType().equalsIgnoreCase("Colleague")) {
                    callingType = "3";
                    txt_collegue.setTextColor(getResources().getColor(R.color.red));
                    //spinner_calltype.setText("Colleague");
                } else {
                    callType = "";
                   // spinner_calltype.setText("");
                }

            }


        }


    }

    private void setListner() {

        btn_opporunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_opporunity.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btn_collection.setBackground(getResources().getDrawable(R.drawable.button_grey));
                //  Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY, null);
                int count = c.getCount();
                String callid = "", callType = "", FirmName = "", prospectId = "", userNameRecord = "";
                String columnName, columnValue;
                ContentValues contentValues = new ContentValues();
                if (opportunityJsonArray != null) {
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
                                }  else if (columnName.equalsIgnoreCase("Mobile")) {
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
                                else if (columnName.equalsIgnoreCase("Address")) {
                                    contentValues.put("Address", "");

                                } else if (columnName.equalsIgnoreCase("lat")) {
                                    contentValues.put("lat", "");

                                } else if (columnName.equalsIgnoreCase("Long")) {
                                    contentValues.put("Long", "");

                                }else if (columnName.equalsIgnoreCase("NextMilestone")) {
                                    contentValues.put("NextMilestone", "");

                                }
                                else {
                                    columnValue = jorder.getString(columnName);
                                    contentValues.put(columnName, columnValue);
                                }
                            }
                            // long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL, null, contentValues);
                            long a = sql.insert(db.TABLE_CRM_CALL_OPPORTUNITY, null, contentValues);
                            Log.e("", "" + a);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(pos).getStartTime());
                    String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                            callLogsDetailsArrayList.get(pos).getEndTime());
                    Intent intent = new Intent(CallUpdateActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "opportunitycall");
                    intent.putExtra("MobileNo", contactedMobNo);
                    intent.putExtra("starttime", sTime);
                    intent.putExtra("endtime",eTime);
                    intent.putExtra("duration", callLogsDetailsArrayList.get(pos).getDuration());
                    intent.putExtra("Rowno",callLogsDetailsArrayList.get(pos).getRowNo());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    JSONObject jsonObject1 = new JSONObject();
                    String uniqueID = UUID.randomUUID().toString();
                    try {
                        jsonObject1.put("UserMasterId", UserMasterId);
                        jsonObject1.put("MobileNo", contactedMobNo);
                        jsonObject1.put("StartTime", callLogsDetailsArrayList.get(pos).getStartTime());
                        jsonObject1.put("EndTime", callLogsDetailsArrayList.get(pos).getEndTime());
                        jsonObject1.put("Duration", callLogsDetailsArrayList.get(pos).getDuration());
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
                    // cv.put("MobileCallType", "Opportunity");
                    cv.put("ContactPersonName", userNameRecord);
                    cv.put("CustomerName", FirmName);
                    sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{callLogsDetailsArrayList.get(pos).getRowNo()});

                    /*Intent intent = new Intent(CallUpdateActivity.this,
                            CreateOpportunityActivity.class);
                    intent.putExtra("SuspectID", );//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
                    startActivity(intent);
                    IndividualProspectusActivity.this.finish();
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/
                }


            }
        });

        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_collection.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btn_opporunity.setBackground(getResources().getDrawable(R.drawable.button_grey));
                String callid = "", userNameRecord = "", firmName = "", calltype = "", prospectid = "";
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null);
                int count = c.getCount();
                ContentValues contentValues = new ContentValues();
                String columnName, columnValue;
                if (collectionJsonArray != null) {
                    try {
                        for (int i = 0; i < collectionJsonArray.length(); i++) {
                            JSONObject jorder = collectionJsonArray.getJSONObject(i);
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
                }


                JSONObject jsonObject1 = new JSONObject();
                String uniqueID = UUID.randomUUID().toString();
                try {
                    jsonObject1.put("UserMasterId", UserMasterId);
                    jsonObject1.put("MobileNo", contactedMobNo);
                    jsonObject1.put("StartTime", callLogsDetailsArrayList.get(pos).getStartTime());
                    jsonObject1.put("EndTime", callLogsDetailsArrayList.get(pos).getEndTime());
                    jsonObject1.put("Duration", callLogsDetailsArrayList.get(pos).getDuration());
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
               // cv.put("MobileCallType", "Collection");
                cv.put("ContactPersonName", userNameRecord);
                cv.put("CustomerName", firmName);
                sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{callLogsDetailsArrayList.get(pos).getRowNo()});


                String sTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                        callLogsDetailsArrayList.get(pos).getStartTime());
                String eTime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm:ss",
                        callLogsDetailsArrayList.get(pos).getEndTime());


                Intent intent = new Intent(CallUpdateActivity.this, OpportunityActivity_V1.class);
                intent.putExtra("Opportunity", "collectioncall");
                intent.putExtra("MobileNo", contactedMobNo);
                intent.putExtra("starttime", sTime);
                intent.putExtra("endtime",eTime);
                intent.putExtra("duration", callLogsDetailsArrayList.get(pos).getDuration());
                intent.putExtra("Rowno",callLogsDetailsArrayList.get(pos).getRowNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();
                if (callType.equals("Select Option")) {
                    Toast.makeText(CallUpdateActivity.this, "Please select call type", Toast.LENGTH_SHORT).show();
                } else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("UserMasterId", UserMasterId);
                        jsonObject.put("MobileNo", contactedMobNo);
                        jsonObject.put("StartTime", callLogsDetailsArrayList.get(pos).getStartTime());
                        jsonObject.put("EndTime", callLogsDetailsArrayList.get(pos).getEndTime());
                        jsonObject.put("Duration", callLogsDetailsArrayList.get(pos).getDuration());
                        jsonObject.put("MobileCallType", callType);
                        jsonObject.put("CallingType", callingType);
                        jsonObject.put("CallType", "");
                        jsonObject.put("CallId", "");
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
                   // CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);

                    //CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.GETFlAG, "Call Update", op, CRM_CallLogList.this);
                    //sql.execSQL("UPDATE " + db.TABLE_CALL_LOG + " SET MobileCallType =" + callType + " WHERE RowNo =" + rowNo);




                    ContentValues cv = new ContentValues();
                    cv.put("MobileCallType", callType);

                    sql.update(db.TABLE_CALL_LOG, cv, "MobileNo=?", new String[]{contactedMobNo});
                    int uri = context.getContentResolver().update(Uri.parse("content://com.example.contentproviderexample.MyProvider1/cte"), cv,"MobileNo=?",
                            new String[]{contactedMobNo});
                    /*sql.update(db.TABLE_CALL_LOG, cv,
                            "RowNo=?", new String[]{callLogsDetailsArrayList.get(pos).getRowNo()
                    });*/
                    sql.update(db.TABLE_CALL_LOG, cv,
                            "MobileNo=?", new String[]{contactedMobNo});



                    Toast.makeText(CallUpdateActivity.this, "Call type is updated", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

        txt_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingType = "5";
                callType=txt_personal.getText().toString();
                txt_personal.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_spam.setBackground(getResources().getDrawable(R.drawable.white));
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.white));
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.white));
            }
        });

        txt_spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingType = "4";
                callType=txt_spam.getText().toString();
                txt_spam.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_personal.setBackground(getResources().getDrawable(R.drawable.white));
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.white));
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.white));
            }
        });
        txt_collegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callingType = "3";
                callType=txt_collegue.getText().toString();
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_personal.setBackground(getResources().getDrawable(R.drawable.white));
                txt_spam.setBackground(getResources().getDrawable(R.drawable.white));
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.white));

            }
        });
        txt_create_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.white));
                txt_personal.setBackground(getResources().getDrawable(R.drawable.white));
                txt_spam.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.white));
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.white));
                startActivity(new Intent(CallUpdateActivity.this,ProspectSelectionActivity.class).
                        putExtra("MobileNo", contactedMobNo).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        txt_add_existing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.white));
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.white));
                txt_personal.setBackground(getResources().getDrawable(R.drawable.white));
                txt_spam.setBackground(getResources().getDrawable(R.drawable.white));
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.white));
                Intent intent = new Intent(CallUpdateActivity.this, OpportunityActivity_V1.class);
                intent.putExtra("Opportunity", "main_opp");
                intent.putExtra("logupdate", "2");
                intent.putExtra("callmob", contactedMobNo);
                intent.putExtra("starttime", callLogsDetailsArrayList.get(pos).getStartTime());
                intent.putExtra("endtime",callLogsDetailsArrayList.get(pos).getEndTime());
                intent.putExtra("duration", callLogsDetailsArrayList.get(pos).getDuration());
                intent.putExtra("Rowno",callLogsDetailsArrayList.get(pos).getRowNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });
        txt_add_collection_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_add_collection_call.setBackground(getResources().getDrawable(R.drawable.button_back_pressed));
                txt_add_existing.setBackground(getResources().getDrawable(R.drawable.white));
                txt_collegue.setBackground(getResources().getDrawable(R.drawable.white));
                txt_personal.setBackground(getResources().getDrawable(R.drawable.white));
                txt_spam.setBackground(getResources().getDrawable(R.drawable.white));
                txt_create_opp.setBackground(getResources().getDrawable(R.drawable.white));
                Intent intent = new Intent(CallUpdateActivity.this, OpportunityActivity_V1.class);
                intent.putExtra("Opportunity", "collection");
                intent.putExtra("logupdate", "2");
                intent.putExtra("callmob", contactedMobNo);
                intent.putExtra("starttime", callLogsDetailsArrayList.get(pos).getStartTime());
                intent.putExtra("endtime",callLogsDetailsArrayList.get(pos).getEndTime());
                intent.putExtra("duration", callLogsDetailsArrayList.get(pos).getDuration());
                intent.putExtra("Rowno",callLogsDetailsArrayList.get(pos).getRowNo());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

       /* spinner_calltype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallUpdateActivity.this,
                        CommonListActivity.class);
                intent.putExtra("option", "update");
                startActivityForResult(intent, COUNTRY);
            }
        });*/



    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));

        txt_title.setText("Call Update");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_type_status = findViewById(R.id.txt_type_status);
        btn_collection = findViewById(R.id.btn_collection);
        btn_opporunity = findViewById(R.id.btn_opporunity);
        txt_statusdetails = findViewById(R.id.txt_statusdetails);
    //    spinner_calltype = findViewById(R.id.spinner_calltype);
        img_nextaction = findViewById(R.id.img_nextaction);
        txt_durationdetails = findViewById(R.id.txt_durationdetails);
        txt_add_existing = findViewById(R.id.txt_add_existing);
        txt_create_opp = findViewById(R.id.txt_create_opp);
        txt_personal = findViewById(R.id.txt_personal);
        txt_spam = findViewById(R.id.txt_spam);
        txt_collegue = findViewById(R.id.txt_collegue);
        txt_add_collection_call = findViewById(R.id.txt_add_collection_call);
       /* String[] calltype1 = getResources().getStringArray(R.array.Calltype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CallUpdateActivity.this, R.layout.vwb_custom_spinner_txt, calltype1);
        spinner_calltype.setAdapter(adapter);
*/



    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if (hours > 12) {
                hours = hours - 12;
                timeSet = "AM";
            } else if (hours == 12) {
                timeSet = "AM";
            } else {
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == COUNTRY && resultCode == COUNTRY) {
                callType = data.getStringExtra("Name");
                spinner_calltype.setText(callType);
                if (callType.equals("Colleague")) {
                    callingType = "3";
                } else if (callType.equals("Spam")) {
                    callingType = "4";
                } else if (callType.equals("Personal")) {
                    callingType = "5";
                }

            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
