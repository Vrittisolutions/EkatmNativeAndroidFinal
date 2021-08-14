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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AdapterCustmerList;
import com.vritti.crmlib.bean.CustomerListBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;import com.vritti.databaselib.data.DatabaseHandlers;import com.vritti.databaselib.other.Utility;import com.vritti.databaselib.other.WebUrlClass;import com.vritti.sessionlib.CallbackInterface;import com.vritti.sessionlib.StartSession;

public class ActivityServiceReportCustomerList extends AppCompatActivity {
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
    ListView listservicerpt;
    EditText edt_search_cust;
    Button img_add_cust;
    ArrayList<CustomerListBean> Custlist;
    AdapterCustmerList adapterCustmerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_service_report);
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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        sql = db.getWritableDatabase();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserMasterId = userpreferences.getString("UserMasterId", "");
        UserName = userpreferences.getString("UserName", "");
        CompanyURL = userpreferences.getString("CompanyURL", null);
        Custlist = new ArrayList<CustomerListBean>();
        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmer);
        listservicerpt = (ListView) findViewById(R.id.listview_cust);
        edt_search_cust = (EditText) findViewById(R.id.searchcust);
        img_add_cust = (Button) findViewById(R.id.img_add_cust);

        if (isCustList()) {
            updateCustList();
        }
        edt_search_cust.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Custlist.clear();
                String username = edt_search_cust.getText().toString();
                if (!(adapterCustmerList == null)) {
                    Custlist = adapterCustmerList.filter(s.toString().trim());
                }
                if (!(Custlist.size() > 0)) {
                    if (username.length() > 3) {
                        final String charset = username;
                        if (isnet()) {
                            new StartSession(ActivityServiceReportCustomerList.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadChatUSerDataJSON().execute(charset);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter atleast three letters to customize your search", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        listservicerpt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String custname = Custlist.get(position).getCustVendorName();
                String custvenderid = Custlist.get(position).getCustVendorMasterId();
                Intent i = new Intent(getApplicationContext(), ActivityServiceReport.class);
                i.putExtra("CustName", custname);
                i.putExtra("CustVenderMasterId", custvenderid);
                startActivity(i);
            }
        });
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
                url = CompanyURL + WebUrlClass.api_GetCustList + "?CharSet=" + URLEncoder.encode(params[0], "UTF-8");
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
            } else if (response.contains("CustVendorMasterId")) {
                try {
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_CUSTOMER_LIST, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("CustVendorMasterId");
                        if (CheckifRecordPresent(db.TABLE_TICKET_CUSTOMER_LIST, "CustVendorMasterId", Id)) {
                            for (int j = 0; j < c.getColumnCount(); j++) {
                                columnName = c.getColumnName(j);
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }
                            long a = sql.insert(db.TABLE_TICKET_CUSTOMER_LIST, null, values);
                            Log.e("cnt", "" + a);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateCustList();
            } else if (response.contains("Please Customize Your Search...")) {
                Toast.makeText(getApplicationContext(), "Please Customize Your Search...", Toast.LENGTH_LONG).show();

            }
            //Please Customize Your Search...
            hideProgress();
        }

    }

    private void updateCustList() {
        Custlist.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_CUSTOMER_LIST + " ORDER BY CustVendorName ASC";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CustomerListBean cust = new CustomerListBean();
                cust.setCustVendorMasterId(cur.getString(cur.getColumnIndex("CustVendorMasterId")));
                cust.setCustVendorCode(cur.getString(cur.getColumnIndex("CustVendorCode")));
                cust.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                cust.setCustVendorName(cur.getString(cur.getColumnIndex("CustVendorName")));
                cust.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                Custlist.add(cust);
            } while (cur.moveToNext());
            adapterCustmerList = new AdapterCustmerList(ActivityServiceReportCustomerList.this, Custlist);
            listservicerpt.setAdapter(adapterCustmerList);
        }
    }

    private boolean isCustList() {

        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_CUSTOMER_LIST;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    private Boolean CheckifRecordPresent(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {
            return false;
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
        ActivityServiceReportCustomerList.this.finish();
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
            updateCustList();
            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
