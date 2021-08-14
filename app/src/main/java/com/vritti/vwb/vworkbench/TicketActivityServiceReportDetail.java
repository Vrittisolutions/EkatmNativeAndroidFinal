package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.CustomerDetailBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.TicketCountAdapterCustmerDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class TicketActivityServiceReportDetail extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SQLiteDatabase sql;
    private Toolbar toolbar;
    SharedPreferences userpreferences;
    ProgressBar progressBar,progressbar_custmer1;
    static String Custmername, CustmerId, TypeFlag,month,year;
    RecyclerView listcustrptdetail;
    ArrayList<CustomerDetailBean> Custlist;
    TicketCountAdapterCustmerDetail adapterCustmerDetail;
    TextView txt_cust_name;
    static Boolean isRecordPresent = true;
    static Boolean idFromload = false;
    private int rowcnt=0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_service_report_detail1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_support);
        toolbar.setTitle(R.string.app_name_toolbar_service);
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
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        Custlist = new ArrayList<CustomerDetailBean>();
        intent = getIntent();
        Custmername = intent.getStringExtra("CustName");
        CustmerId = intent.getStringExtra("CustVenderMasterId");
        TypeFlag = intent.getStringExtra("TypeFlag");

        month=intent.getStringExtra("month");
        year=intent.getStringExtra("year");


        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmer);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmer1);
        listcustrptdetail = (RecyclerView) findViewById(R.id.recycler_view);
        listcustrptdetail.setLayoutManager(new LinearLayoutManager(this));
        txt_cust_name = (TextView) findViewById(R.id.txt_cust_name);
        txt_cust_name.setText(Custmername);

        adapterCustmerDetail = new TicketCountAdapterCustmerDetail(listcustrptdetail, Custlist, TicketActivityServiceReportDetail.this);
        listcustrptdetail.setAdapter(adapterCustmerDetail);


        if (month!=null){
            if (isnet()) {
                new StartSession(TicketActivityServiceReportDetail.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadMonthWiseReportDetailJSON().execute(CustmerId,year,month);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else {
            refreshCustList(CustmerId, TypeFlag);

        }






       /* if (!(adapterCustmerDetail == null)) {
            adapterCustmerDetail.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (isRecordPresent) {
                        Custlist.add(null);
                        adapterCustmerDetail.notifyItemInserted(Custlist.size() - 1);
                        idFromload = true;
                        //String rowcnt = getRowCount(CustmerId, TypeFlag);
                        refreshCustList(CustmerId, TypeFlag);

                    } else {
                        Toast.makeText(TicketActivityServiceReportDetail.this, "No more records to load", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }*/
    }

    private void refreshCustList(final String custmerId, final String typeFlag) {
        if (isnet()) {
            new StartSession(TicketActivityServiceReportDetail.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadChatUSerDataJSON().execute(custmerId, typeFlag);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void updatePagenation(int pagPos) {
        CustmerId = intent.getStringExtra("CustVenderMasterId");
        TypeFlag = intent.getStringExtra("TypeFlag");

        month=intent.getStringExtra("month");
        year=intent.getStringExtra("year");
        if (month!=null){
            rowcnt=pagPos;

            if (isnet()) {
                new StartSession(TicketActivityServiceReportDetail.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadMonthWiseReportDetailJSON().execute(CustmerId,year,month);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }else {
            refreshCustList(CustmerId, TypeFlag);
            rowcnt=pagPos;
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
            CusID = params[0];
            flagloc = params[1];
            try {
                url = CompanyURL + WebUrlClass.api_getTicketDataForAndroid + "?RowIndexStart=" + rowcnt + "&ShipToMasterId=" +
                        URLEncoder.encode(params[0], "UTF-8") + "&Flag=" + URLEncoder.encode(params[1], "UTF-8");
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


            if (response.equalsIgnoreCase("error")) {
                isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                isRecordPresent = true;

                try {
                    JSONArray jResults = new JSONArray(response);

                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("ConsigneeName");

                        CustomerDetailBean cust = new CustomerDetailBean();
                        cust.setRow(jorder.getString("row"));
                        cust.setConsigneeName(jorder.getString("ConsigneeName"));
                        cust.setCustomerId(jorder.getString("CustomerId"));
                        cust.setCity(jorder.getString("City"));
                        cust.setActivityId(jorder.getString("ActivityId"));
                        cust.setActivityName(jorder.getString("ActivityName"));
                        cust.setUserMasterId(jorder.getString("UserMasterId"));
                        cust.setStartDate(jorder.getString("StartDate"));
                        cust.setEndDate(jorder.getString("EndDate"));
                        cust.setModifiedDt(jorder.getString("ModifiedDt"));
                        cust.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                        cust.setIssuedTo(jorder.getString("IssuedTo"));
                        cust.setStatus(jorder.getString("Status"));
                        cust.setActualStartDate(jorder.getString("ActualStartDate"));
                        cust.setActualEndDate(jorder.getString("ActualEndDate"));
                        cust.setSourceId(jorder.getString("SourceId"));
                        cust.setSourceType(jorder.getString("SourceType"));
                        cust.setIsApproved(jorder.getString("IsApproved"));
                        cust.setAddedDt(jorder.getString("AddedDt"));//TicketType
                        //cust.setTicketType(jorder.getString("TicketType"));
                        cust.setShipToMasterid(jorder.getString("ShipToMasterid"));
                        cust.setTicket_No(jorder.getString("Activitycode"));
                        //TicketType
                        Custlist.add(cust);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
               adapterCustmerDetail.updateList(Custlist , rowcnt);
                adapterCustmerDetail.notifyDataSetChanged();

                //updateCustList(CusID, flagloc);
            } else if (response.contains("No Record Found")) {

                Toast.makeText(TicketActivityServiceReportDetail.this, "No record found", Toast.LENGTH_SHORT).show();


                //isRecordPresent = false;
            } else {
               // isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_LONG).show();
            }
            //Please Customize Your Search...
            hideProgress();
        }

    }



    class DownloadMonthWiseReportDetailJSON extends AsyncTask<String, Void, String> {
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
            CusID = params[0];
            String year=params[1];
            String month = params[2];
            try {
                url = CompanyURL + WebUrlClass.api_MonthwiseTikcetDetailsForAndriod + "?RowIndexStart=" + rowcnt + "&ShipToMasterId=" +CusID+ "&Year=" + year+ "&Month=" + month;
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


            if (response.equalsIgnoreCase("error")) {
                isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                isRecordPresent = true;

                try {
                    JSONArray jResults = new JSONArray(response);

                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("ConsigneeName");

                        CustomerDetailBean cust = new CustomerDetailBean();
                        cust.setRow(jorder.getString("row"));
                        cust.setConsigneeName(jorder.getString("ConsigneeName"));
                        cust.setCustomerId(jorder.getString("CustomerId"));
                        cust.setCity(jorder.getString("City"));
                        cust.setActivityId(jorder.getString("ActivityId"));
                        cust.setActivityName(jorder.getString("ActivityName"));
                        cust.setUserMasterId(jorder.getString("UserMasterId"));
                        cust.setStartDate(jorder.getString("StartDate"));
                        cust.setEndDate(jorder.getString("EndDate"));
                        cust.setModifiedDt(jorder.getString("ModifiedDt"));
                        cust.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                        cust.setIssuedTo(jorder.getString("IssuedTo"));
                        cust.setStatus(jorder.getString("Status"));
                        cust.setActualStartDate(jorder.getString("ActualStartDate"));
                        cust.setActualEndDate(jorder.getString("ActualEndDate"));
                        cust.setSourceId(jorder.getString("SourceId"));
                        cust.setSourceType(jorder.getString("SourceType"));
                        cust.setIsApproved(jorder.getString("IsApproved"));
                        cust.setAddedDt(jorder.getString("AddedDt"));//TicketType
                        //cust.setTicketType(jorder.getString("TicketType"));
                        cust.setShipToMasterid(jorder.getString("ShipToMasterid"));
                        cust.setTicket_No(jorder.getString("Activitycode"));
                        //TicketType
                        Custlist.add(cust);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapterCustmerDetail.updateList(Custlist , rowcnt);
                adapterCustmerDetail.notifyDataSetChanged();

                //updateCustList(CusID, flagloc);
            } else if (response.contains("No Record Found")) {

                Toast.makeText(TicketActivityServiceReportDetail.this, "No record found", Toast.LENGTH_SHORT).show();


                //isRecordPresent = false;
            } else {
                // isRecordPresent = false;
                Toast.makeText(getApplicationContext(), "Server error...", Toast.LENGTH_LONG).show();
            }
            //Please Customize Your Search...
            hideProgress();
        }

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
        TicketActivityServiceReportDetail.this.finish();
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
            //idFromload = false;
            //sql.delete(db.TABLE_TICKET_COUNT_DETAIL, null, null);
           // String rowcnt = getRowCount(CustmerId, TypeFlag);
            refreshCustList(CustmerId, TypeFlag);
            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
