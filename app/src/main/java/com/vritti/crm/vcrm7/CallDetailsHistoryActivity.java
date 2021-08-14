package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.CallHistoryAdapter;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharvari on 25-May-17.
 */

public class CallDetailsHistoryActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView callhistory_listview;
    SharedPreferences userpreferences;
    ArrayList<CallHistory> callHistoryArrayList;
    CallHistoryAdapter callHistoryAdapter;
    ProgressBar progressbar;
    SQLiteDatabase sql;
    private String callid, firmname, table, tablename = "";
    TextView txtcall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_call_lay_history);
        InitView();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        callHistoryArrayList = new ArrayList<>();
        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
        firmname = intent.getStringExtra("firmname");
        table = intent.getStringExtra("table");
        // callstatus= intent.getStringExtra("callstatus");
        txtcall.setText(firmname);
        if (table.equalsIgnoreCase("Call")) {
            tablename = db.TABLE_CRM_CALL;
        } else if (table.equalsIgnoreCase("Opportunity")) {
            tablename = db.TABLE_CRM_CALL_OPP;
        }
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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();


        if (cf.getCallhistorycount() > 0) {
            UpdatList();
        } else {

            if (isnet()) {
                new StartSession(CallDetailsHistoryActivity.this, new CallbackInterface() {
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

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //  toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtcall = (TextView) findViewById(R.id.txtcall);
        callhistory_listview = (ListView) findViewById(R.id.callhistory_listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);


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

    class DownloadCallHistoryData extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM hh:mm");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {

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
                        jsonDOB = sdf.format(DOBDate);
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
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            dismissProgressDialog();
            if (response != null) {
                callHistoryAdapter = new CallHistoryAdapter(CallDetailsHistoryActivity.this, callHistoryArrayList);
                //callhistory_listview.setAdapter(callHistoryAdapter);
            }


            dismissProgressDialog();
        }

    }

    private void showProgressDialog() {

        progressbar.setVisibility(View.VISIBLE);
        // progressHUD = ProgressHUD.show(context, "", false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            //progressHUD.dismiss();
            progressbar.setVisibility(View.GONE);
        }
    }


    private void UpdatList() {
        callHistoryArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CALLHISTORY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CallHistory callHistory = new CallHistory();
                callHistory.setCallHistoryId(cur.getString(cur.getColumnIndex("CallHistoryId")));//CallHistoryId
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

            callHistoryAdapter = new CallHistoryAdapter(CallDetailsHistoryActivity.this, callHistoryArrayList);
            //  callhistory_listview.setAdapter(callHistoryAdapter);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CallDetailsHistoryActivity.this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();


        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
