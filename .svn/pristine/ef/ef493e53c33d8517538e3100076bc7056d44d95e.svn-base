package com.vritti.crmlib.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AdapterCustmerDetail;
import com.vritti.crmlib.bean.CustomerDetailBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;import com.vritti.databaselib.data.DatabaseHandlers;import com.vritti.databaselib.other.Utility;import com.vritti.databaselib.other.WebUrlClass;import com.vritti.sessionlib.CallbackInterface;import com.vritti.sessionlib.StartSession;

public class ActivityServiceReportDetail extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SQLiteDatabase sql;
    private Toolbar toolbar;
    SharedPreferences userpreferences;
    ProgressBar progressBar;
    static String Custmername, CustmerId, TypeFlag;
    ListView listcustrptdetail;
    ArrayList<CustomerDetailBean> Custlist;
    AdapterCustmerDetail adapterCustmerDetail;
    TextView txt_cust_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_service_report_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle(" CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

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
        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserMasterId = userpreferences.getString("UserMasterId", "");
        UserName = userpreferences.getString("UserName", "");
        CompanyURL = userpreferences.getString("CompanyURL", null);
        Custlist = new ArrayList<CustomerDetailBean>();
        Intent intent = getIntent();
        Custmername = intent.getStringExtra("CustName");
        CustmerId = intent.getStringExtra("CustVenderMasterId");
        TypeFlag = intent.getStringExtra("TypeFlag");
        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmer);
        listcustrptdetail = (ListView) findViewById(R.id.listview_custdetail);
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_cust_name.setText(Custmername);

        if (isTicketDetailCount(CustmerId)) {
            updateCustList();

        } else {
            if (isnet()) {
                new StartSession(ActivityServiceReportDetail.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChatUSerDataJSON().execute("0", CustmerId, TypeFlag);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetTicketDetail + "?RowIndexStart=" + URLEncoder.encode(params[0], "UTF-8") + "&CustId=" +
                        URLEncoder.encode(params[1], "UTF-8") + "&Flag=" + URLEncoder.encode(params[2], "UTF-8");
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (response.equalsIgnoreCase("error")) {
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                try {
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_COUNT_DETAIL, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("ConsigneeName");
                        // if (CheckifRecordPresent(db.TABLE_TICKET_CUSTOMER_LIST, "CustVendorMasterId", Id)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("rowcnt")) {
                                columnValue = jorder.getString("row");
                            } else if (columnName.equalsIgnoreCase("TicketType")) {
                                columnValue = TypeFlag;
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_TICKET_COUNT_DETAIL, null, values);
                        Log.e("cnt", "" + a);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateCustList();
            } else {
                Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_LONG).show();
            }
            //Please Customize Your Search...
            hideProgress();
        }

    }

    private void updateCustList() {
        Custlist.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_DETAIL + " Where CustomerId='" + CustmerId
                + "' And TicketType='" + TypeFlag + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CustomerDetailBean cust = new CustomerDetailBean();
                cust.setRow(cur.getString(cur.getColumnIndex("rowcnt")));
                cust.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                cust.setCustomerId(cur.getString(cur.getColumnIndex("CustomerId")));
                cust.setCity(cur.getString(cur.getColumnIndex("City")));
                cust.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                cust.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                cust.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                cust.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                cust.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                cust.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                cust.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                cust.setIssuedTo(cur.getString(cur.getColumnIndex("IssuedTo")));
                cust.setStatus(cur.getString(cur.getColumnIndex("Status")));
                cust.setActualStartDate(cur.getString(cur.getColumnIndex("ActualStartDate")));
                cust.setActualEndDate(cur.getString(cur.getColumnIndex("ActualEndDate")));
                cust.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                cust.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                cust.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                cust.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));//TicketType
                cust.setTicketType(cur.getString(cur.getColumnIndex("TicketType")));//TicketType
                Custlist.add(cust);
            } while (cur.moveToNext());
            adapterCustmerDetail = new AdapterCustmerDetail(ActivityServiceReportDetail.this, Custlist);
            listcustrptdetail.setAdapter(adapterCustmerDetail);
        }
    }

    private boolean isTicketDetailCount(String CustId) {

        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_DETAIL + " WHERE CustomerId='" + CustId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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
    public void onBackPressed() {
        super.onBackPressed();
        ActivityServiceReportDetail.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        if (id == R.id.refresh) {


            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
