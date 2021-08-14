package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.crm.adapter.OpportunityAdapter;
import com.vritti.crm.adapter.SubTeamOpportunityAdapter;
import com.vritti.crm.adapter.TeamOpportunityAdapter;
import com.vritti.crm.bean.ApproverData;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.vritti.crm.vcrm7.OpportunityActivity.formateDateFromstring;

public class SubMemberOpportunityActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    LinearLayout lsCall_list, realcolors, laycall_type;
    String Obj, Usermasterid, Username, type, IsChatApplicable = "";
    TextView txtopportunitytype;
    String UserType;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    ArrayList<PartialCallList> partialCallLists;
    TextView
            txtfirmname, tvcall, txt_chat,
            txtcityname, txtactiondatetime;
    CommonObjectProperties commonObj;
    SimpleDateFormat dfDate;
    String FinalObj;
    ProgressBar progressbar;
    ImageView img_search_firm;
    EditText edt_search_firm;
    List<android.location.Address> address = null;
    String Address = "";
    double Lat = 0, Lng = 0;
    TextView txtaddress, txt_expvalue;
    ImageView img_appotunity_update, img_action;
    RecyclerView list_Opportunity;
    ScrollView scrolll;
    private String callId="";
    private String invoice="";
    private int Provisional_count=0;
    private AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private AlertDialog alertDialog1;
    private ArrayList<String> Approverlist1;
    ArrayList<ApproverData> approverDatas = new ArrayList<>();
    private String ApprId="";
    private Spinner spinner_approver;
    private String Reversal_amount="",reason="",Call_ID="",Invoice_NO="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_team_member_opportunity);

        context = SubMemberOpportunityActivity.this;

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        init();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);
        Intent intent = getIntent();
        Obj = intent.getStringExtra("Obj");
        Usermasterid = intent.getStringExtra("UserMasterId");
        Username = intent.getStringExtra("Username");

        type = intent.getStringExtra("Type");


        if (type.equalsIgnoreCase("A")) {
            txtopportunitytype.setText("Assigned Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        } else if (type.equalsIgnoreCase("O")) {
            txtopportunitytype.setText("Overdue Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        } else if (type.equalsIgnoreCase("T")) {
            txtopportunitytype.setText("Today's Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        } else if (type.equalsIgnoreCase("C")) {
            txtopportunitytype.setText("Collection Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCollectionDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        } else if (type.equalsIgnoreCase("H")) {
            txtopportunitytype.setText("Hot Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        } else if (type.equalsIgnoreCase("TO")) {
            txtopportunitytype.setText("Tomorrow Opportunity of " + Username);
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                });
            }
        }
       /* if (db.getCallTeamPartialcount() > 0) {
            updateList();

        } else {*/


    }

    //}

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);
        txtopportunitytype = (TextView) findViewById(R.id.txtopportunity_type);
        edt_search_firm = (EditText) findViewById(R.id.edt_search_firm);
        img_search_firm = (ImageView) findViewById(R.id.img_search_firm);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        scrolll = findViewById(R.id.scroll);

        sql = db.getWritableDatabase();
        partialCallLists = new ArrayList<PartialCallList>();

        list_Opportunity = findViewById(R.id.list_Opportunity);

        edt_search_firm.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                String Firmname = edt_search_firm.getText().toString();

                if (Firmname.length() > 1) {

                    FilterOppUpdatList(Firmname);
                }

            }
        });
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId())

        {
            case R.id.provisionalreceipt:
                //callId = partialCallLists.get(position).getCallId();
                Intent intent = new Intent(SubMemberOpportunityActivity.this, ProvisionalActivity.class);
                intent.putExtra("Call_id", callId);
                intent.putExtra("procount", Provisional_count);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.discountvoucher:
                // callId = partialCallLists.get(position).getCallId();
                getDiscountVoucherdialog(callId, invoice);
                return true;

            default:

                return false;
        }
    }

    class DownloadCommanDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0], SubMemberOpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    //  response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);

                    Log.d("TeamRes", response);

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL_PARTIAL_TEAM, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_TEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);

                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL_TEAM, null, values);
                        Log.e("data offset", "Data " + a);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("CallId")) {
                UpdatList();

            } else if (response.contains("Error")) {

            }
        }

    }


    class DownloadCollectionDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0], SubMemberOpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    //  response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);

                    Log.d("TeamRes", response);

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_TEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);

                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM, null, values);
                        Log.e("data offset", "Data " + a);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("CallId")) {
                UpdatList_COllection_call();
            } else if (response.contains("Error")) {

            }
        }

    }


   /* private void UpdatList() {
        partialCallLists.clear();
        String query="";
        if (type.equalsIgnoreCase("C")){
            query= "SELECT  FirmName,CityName,CallId," +
                    "NextActionDateTime,Mobile,CallType,CallStatus,ContactName,CurrentCallOwner,UserName" +
                    " FROM " + db.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM + "";
        }else {
            query = "SELECT  FirmName,CityName,CallId," +
                    "NextActionDateTime,Mobile,CallType,CallStatus,ContactName,CurrentCallOwner,UserName,Address,ExpectedValue,ExpectedValue,EmailId,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + "";
        }
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                // partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                // partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setCurrentCallOwner(cur.getString(cur.getColumnIndex("CurrentCallOwner")));
                partialCallList.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                if (type.equalsIgnoreCase("C")){
                    partialCallList.setAddress("");
                }else {
                    partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                }
                if (type.equalsIgnoreCase("C")){
                    partialCallList.setExpectedValue("0.0");
                }else {
                    partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                }
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           *//* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*//*
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }*/


    private void UpdatList() {
        partialCallLists.clear();
        String query = "";
       /* query = "SELECT  FirmName,CityName,CallId," +
                "NextActionDateTime,Mobile,CallType,CallStatus,ContactName,CurrentCallOwner,UserName,Address,ExpectedValue,ExpectedValue,EmailId,NextAction" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + "";*/


        query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId,Address,ExpectedValue,EmailId,NextAction" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + "";


        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
               /* String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                // partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                // partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setCurrentCallOwner(cur.getString(cur.getColumnIndex("CurrentCallOwner")));
                partialCallList.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                if (type.equalsIgnoreCase("C")){
                    partialCallList.setAddress("");
                }else {
                    partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                }
                if (type.equalsIgnoreCase("C")){
                    partialCallList.setExpectedValue("0.0");
                }else {
                    partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                }
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallLists.add(partialCallList);*/


                //  String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                String callId = cur.getString(cur.getColumnIndex("CallId"));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());

            SubTeamOpportunityAdapter opportunityAdapter = new SubTeamOpportunityAdapter(SubMemberOpportunityActivity.this, partialCallLists);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            list_Opportunity.setLayoutManager(mLayoutManager);
            list_Opportunity.setItemAnimator(new DefaultItemAnimator());
            list_Opportunity.setAdapter(opportunityAdapter);

           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
           /* lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }*/
        }

    }


    private void addView_CallList(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_calllist_partial,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
       /* txtcityterritoryname = (TextView) convertView
                .findViewById(R.id.cityterritory);*/

        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
        txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);
        txt_expvalue = (TextView) convertView.findViewById(R.id.txt_expvalue);
        img_appotunity_update = convertView.findViewById(R.id.img_appotunity_update);
        img_action = convertView.findViewById(R.id.btn_action);

        img_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String callId = partialCallLists.get(position).getCallId();
                String invoice = partialCallLists.get(position).getInvoiceNo();
                String ProspectPkSusId = partialCallLists.get(position).getPKSuspectId();
                String oppCity = partialCallLists.get(position).getCityname();
                PopupMenu popup = new PopupMenu(SubMemberOpportunityActivity.this, v);
                popup.setOnMenuItemClickListener(SubMemberOpportunityActivity.this);
                popup.inflate(R.menu.menu_opportunitycall);

                popup.show();*/

                Intent intent1 = new Intent(SubMemberOpportunityActivity.this, CallListActionActivity.class);
                intent1.putExtra("callid", partialCallLists.get(position).getCallId());
                intent1.putExtra("firmname", partialCallLists.get(position).getFirmname());
                intent1.putExtra("calltype", partialCallLists.get(position).getCallType());
                intent1.putExtra("ProspectId", partialCallLists.get(position).getPKSuspectId());
                intent1.putExtra("SourceId", partialCallLists.get(position).getSourceId());
                intent1.putExtra("table", "Call");
                intent1.putExtra("type", "Callfromcalllogs");
                intent1.putExtra("MobileCalltype", type);
                intent1.putExtra("starttime", "");
                intent1.putExtra("endtime", "");
                intent1.putExtra("duration", "");
                intent1.putExtra("rowNo", "");
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                img_appotunity_update.setImageResource(R.drawable.img_warm_call);

            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        /*txtcityname.setText(partialCallLists.get(i)
                .getCityname() + "-" + partialCallLists.get(i)
                .getProductname());*/

        txtcityname.setText(partialCallLists.get(i)
                .getCityname());
        txtactiondatetime.setText(partialCallLists.get(i)
                .getActiondatetime());
        String status = partialCallLists.get(i).getContactName();

        if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("")) {
            tvcall.setText("(No Contact Available)");
        } else {
            if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {
                tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
            } else {
                tvcall.setText("(" + partialCallLists.get(i).getContactName() + " - "
                        + partialCallLists.get(i).getMobileno() + ")");
            }

        }

        Address = partialCallLists.get(i).getAddress();
        if (!Address.equalsIgnoreCase("")) {
            txtaddress.setVisibility(View.VISIBLE);
            txtaddress.setText(Address);
        }

        Geocoder coder = new Geocoder(SubMemberOpportunityActivity.this);
        try {
            address = coder.getFromLocationName(Address, 5);
            android.location.Address location = address.get(0);
            Lat = location.getLatitude();
            Lng = location.getLongitude();

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lat == 0 & Lng == 0) {

                } else {
                    Geocoder coder = new Geocoder(SubMemberOpportunityActivity.this);
                    try {
                        Address = partialCallLists.get(position).getAddress();
                        address = coder.getFromLocationName(Address, 5);
                        android.location.Address location = address.get(0);
                        Lat = location.getLatitude();
                        Lng = location.getLongitude();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String uriMap = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                    startActivity(intent);
                }
            }
        });

        String ExpValue = partialCallLists.get(i).getExpectedValue();
        if (ExpValue.equals("0.00") || ExpValue.equals("0.0") || ExpValue.equals("0")) {

        } else {
            txt_expvalue.setVisibility(View.VISIBLE);
            txt_expvalue.setText("EV-" + "\u20B9" + ExpValue);
        }

        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cf.getCallListTeamcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_TEAM, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(context, TeamsCallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("Usermasterid", Usermasterid);
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());

                    startActivity(intent);
                    //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                    getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                   /* Intent intent = new Intent(context, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
*/
                }

            }
        });

        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String IsChatApplicable = userpreferences.getString("chatapplicable", "");
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(position).getCallType();
                    String Call_Callid = partialCallLists.get(position).getCallId();
                    String Firm_name = partialCallLists.get(position).getFirmname();
                    Intent intent = new Intent(SubMemberOpportunityActivity.this,
                            MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Opportunity");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("AssignBy", Username);
                    intent.putExtra("AssignById", Usermasterid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(SubMemberOpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }


            }
        });

        lsCall_list.addView(convertView);
    }


    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date datestop = sdf.parse(datedb);


            return_value = datedb;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("crm_dialog_action", "crm_dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(datedb);
            return_value = "Today" + tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(datedb);
            return_value = "Yesterday" + tm[0];
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(datedb);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {
            String k = datedb.substring(0, datedb.length() - 15);
            String tm[] = splitfrom(return_value);
            return k + tm[0];
        }

    }

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    private String getObj(String a, String uid, String cid) {
        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", uid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (a.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("O")) {

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("T")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("C")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 2);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("H")) {

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Hot");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        return FinalObj;
    }

    public void getCallListData_Full(String cid, final int position) {
        final String obj = getObj(type, Usermasterid, cid);

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanData_fullURLJSON().execute(
                            String.valueOf(position), obj);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    class DownloadCommanData_fullURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[1], SubMemberOpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    pos = Integer.parseInt(params[0]);
                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL_TEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_TEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            String jsonDt = jorder.getString("ExpectedCloserDate");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            }
                            if (columnName.equalsIgnoreCase("ExpectedCloserDate")) {
                                jsonDt = jsonDt.substring(jsonDt.indexOf("(") + 1, jsonDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDt);
                                DOBDate = new Date(DOB_date);
                                jsonDt = sdf.format(DOBDate);
                                values.put(columnName, jsonDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_TEAM, null, values);
                        Log.d("crm_dialog_action", "count " + a);
                    }
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_TEAM, null);
                    int count1 = c1.getCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();


            if (integer.equalsIgnoreCase("[]")) {


            } else {
                if (cf.getCallListTeamcount(partialCallLists.get(pos).getCallId()) > 0) {
                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_TEAM, cv, "CallId=?",
                            new String[]{partialCallLists.get(pos).getCallId()});


                    Intent intent = new Intent(context, TeamsCallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(pos).getCallId());
                    intent.putExtra("calltype", partialCallLists.get(pos).getCallType());
                    intent.putExtra("firmname", partialCallLists.get(pos).getFirmname());
                    intent.putExtra("Usermasterid", Usermasterid);


                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            }

        }

    }

    private void showProgressDialog() {


        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SubMemberOpportunityActivity.this.finish();
    }

    private void FilterOppUpdatList(String Firmname) {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CityName,CallId," +
                "NextActionDateTime,Mobile,CallType,CallStatus,ContactName" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                // partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                // partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }

    public void ChatClick(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {

        int i = adapterPosition;
        if (IsChatApplicable.equalsIgnoreCase("true")) {
            String Call_CallType = partialCallListArrayList.get(i).getCallType();
            String Call_Callid = partialCallListArrayList.get(i).getCallId();
            String Firm_name = partialCallListArrayList.get(i).getFirmname();
            Intent intent = new Intent(SubMemberOpportunityActivity.this, MultipleGroupActivity.class);
            intent.putExtra("callid", Call_Callid);
            intent.putExtra("call_type", "Crm_Opportunity");
            intent.putExtra("firm", Firm_name);
            intent.putExtra("projmasterId", "");
            intent.putExtra("AssignBy", UserName);
            intent.putExtra("AssignById", UserMasterId);
            //intent.putExtra("chatCount",ChatCount);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            Toast.makeText(SubMemberOpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
        }

    }

    public void OpportunityUpdate(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

        Intent intent = new Intent(SubMemberOpportunityActivity.this, CallRatingActivity.class);
        intent.putExtra("callid", partialCallListArrayList.get(position).getCallId());
        intent.putExtra("callstatus", partialCallListArrayList.get(position).getCallStatus());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void tvCallMethd(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

        if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

        } else {
            try {
                String mobile = partialCallLists.get(position).getMobileno();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mobile));
                startActivity(callIntent);

            } catch (SecurityException e) {

            }
        }
    }


    public void ActionClick(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList, View v) {
        int rowClickPos = adapterPosition;

        String callId = partialCallListArrayList.get(rowClickPos).getCallId();
        String invoice = partialCallListArrayList.get(rowClickPos).getInvoiceNo();
        String ProspectPkSusId = partialCallListArrayList.get(rowClickPos).getPKSuspectId();
        String oppCity = partialCallListArrayList.get(rowClickPos).getCityname();
        PopupMenu popup = new PopupMenu(SubMemberOpportunityActivity.this, v);
        popup.setOnMenuItemClickListener(SubMemberOpportunityActivity.this);
        popup.inflate(R.menu.menu_opportunitycall);

        popup.show();
    }


    private void UpdatList_COllection_call() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal" +
                " FROM " + db.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallList.setProvisionalCount(cur.getString(cur.getColumnIndex("ProvisionalCount")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setExpectedValue("");
                partialCallList.setBalVal(cur.getString(cur.getColumnIndex("BalVal")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                scrolll.setVisibility(View.VISIBLE);
                list_Opportunity.setVisibility(View.GONE);
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CollectionCallList(i);
                }
            }
        }

    }

    private void addView_CollectionCallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) SubMemberOpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_collection_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView.findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        TextView tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        // btn_provisional = (Button) convertView.findViewById(R.id.btn_provisional);
        ImageView btn_provisional = (ImageView) convertView.findViewById(R.id.btn_provisional);
        TextView txt_invoice_no = (TextView) convertView.findViewById(R.id.txt_invoice_no);
        TextView txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
        TextView txt_invoice_date = (TextView) convertView.findViewById(R.id.txt_invoice_date);
        // len_collection= (LinearLayout) convertView.findViewById(R.id.len_collection);
        TextView txt_Unallocateamount = (TextView) convertView.findViewById(R.id.txt_Unallocateamount);


        TextView txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);


        txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView.findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        ImageView img_provisional_count = (ImageView) convertView.findViewById(R.id.img_provisional_count);

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                //     img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                //   img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                // img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        txtactiondatetime.setText(partialCallLists.get(i).getActiondatetime());
        if (partialCallLists.get(i).getLatestRemark().equalsIgnoreCase("")
                || partialCallLists.get(i).getLatestRemark().equalsIgnoreCase(null)
                || partialCallLists.get(i).getLatestRemark().equalsIgnoreCase("null")) {
            // tv_latestremark.setText("");
        } else {
            tv_latestremark.setText(" For " +
                    partialCallLists.get(i).getLatestRemark());
        }

        String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();
        // String Product = partialCallLists.get(position).getProduct();
        final String Invoice_no = partialCallLists.get(position).getInvoiceNo();
        String Amount = partialCallLists.get(position).getAmount();
        String Invoice_date = partialCallLists.get(position).getInvoiceDt();
        String[] invoiceDate = Invoice_date.split("T");
        Invoice_date = invoiceDate[0];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Invoice_date = Invoice_date.substring(Invoice_date.indexOf("(") + 1, Invoice_date.lastIndexOf(")"));
        long timestamp = Long.parseLong(Invoice_date);
        Date date = new Date(timestamp);
        String invoice_d = sdf.format(date);
        String invoice_date = formateDateFromstring("dd MMM yyyy", "dd-MM-yyyy", invoice_d);

        String Unallocateamount = "Unallocated Cash" + " - " + partialCallLists.get(position).getUnAllocatedCash();

        String BalVal = partialCallLists.get(position).getBalVal();

        //  String Invoice_Date = Invoice_no + "\n" + invoice_date + "\n" + Amount;
        txt_invoice_no.setText(Invoice_no);
        txt_invoice_date.setText(invoice_date);
        txt_amount.setText(BalVal + " / " + Amount);

        if (partialCallLists.get(position).getUnAllocatedCash().equals("0.0")) {
            txt_Unallocateamount.setVisibility(View.GONE);
        } else {
            txt_Unallocateamount.setText(Unallocateamount);
            txt_Unallocateamount.setVisibility(View.VISIBLE);
        }

        //String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";
        String Concatdata = City + "-" + "(" + Contactname + "-" + Mobile + ")";
        tvcall.setText(Concatdata);

        Provisional_count = Integer.parseInt(partialCallLists.get(position).getProvisionalCount());

        System.out.println("ProvisionalCount" + Provisional_count);

       /* if (Provisional_count > 0) {
            img_provisional_count.setVisibility(View.VISIBLE);
        }
*/

        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(SubMemberOpportunityActivity.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Collection");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(SubMemberOpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_provisional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callId = partialCallLists.get(position).getCallId();
                invoice = partialCallLists.get(position).getInvoiceNo();
                Provisional_count = Integer.parseInt(partialCallLists.get(position).getProvisionalCount());
                PopupMenu popup = new PopupMenu(SubMemberOpportunityActivity.this, v);
                popup.setOnMenuItemClickListener(SubMemberOpportunityActivity.this);
                popup.inflate(R.menu.menu_collectioncall);

                popup.show();


            }
        });





       /* spinner_provisional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callId = partialCallLists.get(position).getCallId();
                Intent intent=new Intent(OpportunityActivity.this,ProvisionalActivity.class);
                intent.putExtra("Call_id",callId);
                intent.putExtra("procount",Provisional_count);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
*/


        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SubMemberOpportunityActivity.this, CallListActionActivity.class);
                intent.putExtra("callid", partialCallLists.get(position).getCallId());
                intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                intent.putExtra("ProspectId", partialCallLists.get(position).getPKSuspectId());
                intent.putExtra("SourceId", partialCallLists.get(position).getSourceId());
                intent.putExtra("table", "Call");
                intent.putExtra("type", "Callfromcalllogs");
                intent.putExtra("starttime", "");
                intent.putExtra("endtime", "");
                intent.putExtra("duration", "");
                intent.putExtra("rowNo", "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);



                /*  if (Partial.equalsIgnoreCase("P")) {
                 *//*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                *//*


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(OpportunityActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }

*/
               /* if (db.getCallListOppcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_OPP, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(OpportunityActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    intent.putExtra("table", "Opportunity");
                    startActivity(intent);

                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                       getCallListData_Full(partialCallLists.get(position).getCallId(), position);

                }*/
            }
        });


        lsCall_list.addView(convertView);

    }

    private void getDiscountVoucherdialog(final String call_id, final String invoice_no) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_discount_lay, null, false);
        builder = new AlertDialog.Builder(SubMemberOpportunityActivity.this);
        builder.setView(v);
        alertDialog = builder.create();

        final EditText edt_reversal_amount = (EditText) v.findViewById(R.id.edt_reversal_amount);
        final EditText edt_reason = (EditText) v.findViewById(R.id.edt_reason);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Reversal_amount = edt_reversal_amount.getText().toString();
                reason = edt_reason.getText().toString();
                Call_ID = call_id;
                Invoice_NO = invoice_no;

                getapproverdialog(Reversal_amount, reason, Call_ID, Invoice_NO);
            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }


    private void getapproverdialog(final String reversal_amount, final String reason, final String call_id, final String invoice_no) {

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_approver_lay, null, false);
        builder = new AlertDialog.Builder(SubMemberOpportunityActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();


        spinner_approver = (Spinner) v.findViewById(R.id.spinner_approver);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);

        alertDialog.dismiss();


        if (isnet()) {
            new StartSession(SubMemberOpportunityActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadApproverDetailsData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        spinner_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                approverDatas.size();
                ApprId = approverDatas.get(position).getUserid();


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isnet()) {
                    new StartSession(SubMemberOpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(ApprId, call_id, invoice_no, reversal_amount, reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(SubMemberOpportunityActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });

        alertDialog1 = builder.create();
        alertDialog1.setCancelable(false);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
            }
        });


    }
    class DownloadApproverDetailsData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_FillApprover;
                res = ut.OpenConnection(url);
                System.out.println("AdvanceProList :" + url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            Approverlist1 = new ArrayList<>();
            approverDatas = new ArrayList<>();

            try {
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jsonapproverlistdata = jResults.getJSONObject(i);

                    ApproverData approverData = new ApproverData();
                    approverData.setUserid(jsonapproverlistdata.getString("userid"));
                    approverData.setUsername(jsonapproverlistdata.getString("username"));
                    approverDatas.add(approverData);
                    String point = jsonapproverlistdata.getString("username");
                    Approverlist1.add(point);


                }
                spinner_approver.setAdapter(new ArrayAdapter<String>(SubMemberOpportunityActivity.this,
                        R.layout.crm_custom_spinner_txt,
                        Approverlist1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dismissProgressDialog();

        }

    }
    class GetDiscountVoucher extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String url;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = CompanyURL + WebUrlClass.api_GetSaveDiscount + "?CallId=" + Call_ID + "&InvoiceNo=" + Invoice_NO + "&ReversalAmount=" + URLEncoder.encode(Reversal_amount, "UTF-8") + "&Reason=" + URLEncoder.encode(reason, "UTF-8") + "&ApprId=" + ApprId + "&Type=R";  //ApprId



                try {
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);

                        System.out.println("BusinessAPI-1 :" + response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.equalsIgnoreCase("success") || response != null) {
                Toast.makeText(SubMemberOpportunityActivity.this, "Discount voucher successfully sent for approval", Toast.LENGTH_LONG).show();
                alertDialog1.dismiss();
            } else {
                Toast.makeText(SubMemberOpportunityActivity.this, "Please try again", Toast.LENGTH_LONG).show();

            }

        }

    }

}
