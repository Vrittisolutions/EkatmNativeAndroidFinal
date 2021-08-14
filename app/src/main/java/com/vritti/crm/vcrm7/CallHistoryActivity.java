package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.CallHistoryAdapter;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallHistoryActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    private String settingKey, dabasename;
    public String IsChatApplicable;
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    Toolbar toolbar;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    String UserType;
    RecyclerView callhistory_listview;
    ProgressBar center_progress;
    ArrayList<CallHistory> callHistoryArrayList;
    CallHistoryAdapter callHistoryAdapter;
    TextView txt_firm_title;
    String callid = "", firmname = "", calltype = "", SourceId = "", FormId = "", callstatu = "", table = "", ProspectId = "";
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_call_history_activity);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);
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
        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
    /*    firmname = intent.getStringExtra("firmname");
        calltype = intent.getStringExtra("calltype");
        table = intent.getStringExtra("table");
        ProspectId = intent.getStringExtra("ProspectId");
        SourceId = intent.getStringExtra("SourceId");*/
        txt_firm_title.setText(firmname);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Opportunity History");
        if (ut.isNet(CallHistoryActivity.this)) {
            center_progress.setVisibility(View.VISIBLE);
            new StartSession(CallHistoryActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCallHistoryData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void InitView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        callhistory_listview = findViewById(R.id.callhistory_listview);
        center_progress = findViewById(R.id.center_progress);
        txt_firm_title = findViewById(R.id.txt_firm_title);
        callHistoryArrayList = new ArrayList<>();
    }

    class DownloadCallHistoryData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            center_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            callHistoryArrayList.clear();
            try {
                String url = CompanyURL + WebUrlClass.api_GetCallHistory + "?CallId=" +
                        URLEncoder.encode(callid, "UTF-8");

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                    sql.delete(db.TABLE_CALLHISTORY, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALLHISTORY, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jcallhistory = jResults.getJSONObject(i);

                        CallHistory callHistory = new CallHistory();

                        callHistory.setCallHistoryId(jcallhistory.getString("CallHistoryId"));
                        callHistory.setCallId(jcallhistory.getString("CallId"));
                        callHistory.setCurrentCallOwner(jcallhistory.getString("CurrentCallOwner"));
                        callHistory.setActionType(jcallhistory.getString("ActionType"));

                        String contact = jcallhistory.getString("Contact");

                        contact = contact.replace("rr0", "");
                        String Contact = contact;
                        System.out.println("Whoom :" + Contact);
                        callHistory.setContact(Contact);
                        callHistory.setPurpose(jcallhistory.getString("Purpose"));
                        callHistory.setNextAction(jcallhistory.getString("NextAction"));
                        // callHistory.setNextActionDateTime(jcallhistory.getString("NextActionDateTime"));
                        String jsonDOJ = jcallhistory.getString("NextActionDateTime");

                        System.out.println("Datetimecall :" + jsonDOJ);
                        jsonDOJ = jsonDOJ.substring(jsonDOJ.indexOf("(") + 1, jsonDOJ.lastIndexOf(")"));
                        long DOJ_date = Long.parseLong(jsonDOJ);
                        DOJDate = new Date(DOJ_date);
                        jsonDOJ = sdf1.format(DOJDate);
                        System.out.println("Datetimecall_1 :" + jsonDOJ);
                        callHistory.setNextActionDateTime(jsonDOJ);
                        String jsonDOB = jcallhistory.getString("ModifiedDt");
                        jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                        long DOB_date = Long.parseLong(jsonDOB);
                        DOBDate = new Date(DOB_date);
                        jsonDOB = sdf1.format(DOBDate);
                        callHistory.setModifiedDt(jsonDOB);
                        callHistory.setOutcome(jcallhistory.getString("Outcome"));
                        callHistory.setUserName(jcallhistory.getString("UserName"));
                        callHistory.setOutcomeCode(jcallhistory.getString("OutcomeCode"));
                        callHistory.setLatestRemark(jcallhistory.getString("LatestRemark"));
                        cf.AddCallHistory(callHistory);
                        callHistoryArrayList.add(callHistory);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            center_progress.setVisibility(View.GONE);
            if (response.equals("[]")) {
                Toast.makeText(CallHistoryActivity.this, "Call history not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                   /*callHistoryAdapter = new CallHistoryAdapter(CallListActionActivity.this, callHistoryArrayList);
                   callhistory_listview.setAdapter(callHistoryAdapter);
                   callhistory_listview.setVisibility(View.VISIBLE);*/

                    UpdatList();

                }

            }
        }
    }

    private void UpdatList() {
        callHistoryArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CALLHISTORY + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CallHistory callHistory = new CallHistory();
                callHistory.setCallHistoryId(cur.getString(cur.getColumnIndex("CallHistoryId")));
                callHistory.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                callHistory.setCurrentCallOwner(cur.getString(cur.getColumnIndex("CurrentCallOwner")));
                callHistory.setActionType(cur.getString(cur.getColumnIndex("ActionType")));
                callHistory.setContact(cur.getString(cur.getColumnIndex("Contact")));
                callHistory.setPurpose(cur.getString(cur.getColumnIndex("Purpose")));
                callHistory.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                callHistory.setNextActionDateTime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                callHistory.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                callHistory.setOutcome(cur.getString(cur.getColumnIndex("Outcome")));
                callHistory.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                callHistory.setOutcomeCode(cur.getString(cur.getColumnIndex("OutcomeCode")));
                callHistory.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                callHistoryArrayList.add(callHistory);
            } while (cur.moveToNext());

            callHistoryAdapter = new CallHistoryAdapter(CallHistoryActivity.this, callHistoryArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            callhistory_listview.setLayoutManager(mLayoutManager);
            callhistory_listview.setItemAnimator(new DefaultItemAnimator());
            callhistory_listview.setAdapter(callHistoryAdapter);
           /* if (callHistoryAdapter.getCount() != 0) {
                callhistory_listview.setAdapter(callHistoryAdapter);

                callhistory_listview.setVisibility(View.VISIBLE);


            } else {
                Toast.makeText(CallHistoryActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
            }*/
        } else {
            if (ut.isNet(CallHistoryActivity.this)) {
                new StartSession(CallHistoryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCallHistoryData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CallHistoryActivity.this,CRMHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}
