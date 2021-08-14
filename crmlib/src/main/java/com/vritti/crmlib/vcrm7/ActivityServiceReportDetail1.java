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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


import com.vritti.crmlib.Interface.OnLoadMoreListener;
import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AdapterCustmerDetail1;
import com.vritti.crmlib.bean.CustomerDetailBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class ActivityServiceReportDetail1 extends AppCompatActivity {
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
    RecyclerView listcustrptdetail;
    ArrayList<CustomerDetailBean> Custlist;
    AdapterCustmerDetail1 adapterCustmerDetail;
    TextView txt_cust_name;
    static Boolean isRecordPresent = true;
    static Boolean idFromload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_service_report_detail1);
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
        listcustrptdetail = (RecyclerView) findViewById(R.id.recycler_view);
        listcustrptdetail.setLayoutManager(new LinearLayoutManager(this));
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_cust_name.setText(Custmername);

        if (isTicketDetailCount(CustmerId, TypeFlag)) {
            idFromload = false;
            updateCustList(CustmerId, TypeFlag);

        } else {
            idFromload = false;
            String rowcnt = getRowCount(CustmerId, TypeFlag);
            refreshCustList(rowcnt, CustmerId, TypeFlag);
        }
        if (!(adapterCustmerDetail == null)) {
            adapterCustmerDetail.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (isRecordPresent) {
                        Custlist.add(null);
                        adapterCustmerDetail.notifyItemInserted(Custlist.size() - 1);
                        idFromload = true;
                        String rowcnt = getRowCount(CustmerId, TypeFlag);
                        refreshCustList(rowcnt, CustmerId, TypeFlag);

                    } else {
                        Toast.makeText(ActivityServiceReportDetail1.this, "No more records to load", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    private void refreshCustList(final String rowcnt, final String custmerId, final String typeFlag) {
        if (isnet()) {
            new StartSession(ActivityServiceReportDetail1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatUSerDataJSON().execute(rowcnt, custmerId, typeFlag);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    class DownloadChatUSerDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        String CusID = "";
        String flagloc = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            CusID = params[1];
            flagloc = params[2];
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
//"No Record Found..."

            if (idFromload) {
                Custlist.remove(Custlist.size() - 1);
                adapterCustmerDetail.notifyItemRemoved(Custlist.size());
                adapterCustmerDetail.notifyDataSetChanged();
                adapterCustmerDetail.setLoaded();
            }
            if (response.equalsIgnoreCase("error")) {
                isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                isRecordPresent = true;

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
                        //}
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                updateCustList(CusID, flagloc);
            } else if (response.contains("No Record Found")) {

                if (idFromload) {
                    Toast.makeText(ActivityServiceReportDetail1.this, "No more records to load", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityServiceReportDetail1.this, "No record found", Toast.LENGTH_SHORT).show();
                }

                isRecordPresent = false;
            } else {
                isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_LONG).show();
            }
            //Please Customize Your Search...
            hideProgress();
        }

    }

    private void updateCustList(String custid, String typeFlag) {
        Custlist.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_DETAIL + " Where CustomerId='" + custid
                + "' And TicketType='" + typeFlag + "'";
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
            adapterCustmerDetail = new AdapterCustmerDetail1(listcustrptdetail, Custlist, ActivityServiceReportDetail1.this);
            listcustrptdetail.setAdapter(adapterCustmerDetail);

        }
    }

    private boolean isTicketDetailCount(String CustId, String flag) {

        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_DETAIL + " WHERE CustomerId='" + CustId + "' AND TicketType='" + flag + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    private String getRowCount(String CustId, String flag) {
        String cnt = "0";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_DETAIL + " WHERE CustomerId='" + CustId + "' AND TicketType='" + flag + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToLast();

            cnt = cur.getString(cur.getColumnIndex("rowcnt"));
        } else {
            cnt = "0";
        }
        return cnt;
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
        ActivityServiceReportDetail1.this.finish();
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
            idFromload = false;
            sql.delete(db.TABLE_TICKET_COUNT_DETAIL, null, null);
            String rowcnt = getRowCount(CustmerId, TypeFlag);
            refreshCustList(rowcnt, CustmerId, TypeFlag);
            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
