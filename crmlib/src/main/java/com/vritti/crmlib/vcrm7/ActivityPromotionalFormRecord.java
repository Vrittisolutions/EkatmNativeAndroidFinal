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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crmlib.adapter.PromotionalRecordAdapter;
import com.vritti.crmlib.bean.PromotionalracordBean;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ActivityPromotionalFormRecord extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    ListView ls_record;
    SQLiteDatabase sql;
    ProgressBar mProgress;
    ArrayList<PromotionalracordBean> recordlist;
    SearchableSpinner mSpinner;
    SharedPreferences userpreferences;

    TextView mTxtdate;
    Button mBtnProceed;
    private String PromoeterLogInId = "", Promoterdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_promotional_form_record);
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

        InitView();
        if (CheckList()) {
            UpdatePromotionalform();
        } else {
            Refresh();
        }

    }
    private Boolean CheckList() {
        String query = "SELECT * FROM " + db.TABLE_PROMOTER_REPORT+" where UserMasterId='"+PromoeterLogInId+"' and Dt='"+Promoterdate+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;

        } else {
            return false;
        }

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.drawable.vwb_logo);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        PromoeterLogInId = intent.getStringExtra("PromoterLiginId");
        Promoterdate = intent.getStringExtra("Date");


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
        recordlist = new ArrayList<PromotionalracordBean>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        ls_record = (ListView) findViewById(R.id.listviewPromoterRecord);


    }

    private void
    Refresh() {
        if (isnet()) {
            ShowProgress();
            new StartSession(ActivityPromotionalFormRecord.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetPromoterListRecord().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    HideProgres();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void UpdatePromotionalform() {
        recordlist.clear();
        String query = "SELECT * FROM "
                + db.TABLE_PROMOTER_REPORT+" where UserMasterId='"+PromoeterLogInId+"' and Dt='"+Promoterdate+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                PromotionalracordBean Promotinalbean = new PromotionalracordBean();
                Promotinalbean.setUserLoginId(cur.getString(cur.getColumnIndex("UserLoginId")));
                Promotinalbean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                Promotinalbean.setDt(cur.getString(cur.getColumnIndex("Dt")));
                Promotinalbean.setLocation(cur.getString(cur.getColumnIndex("Location")));
                Promotinalbean.setD2DChillTaste(cur.getString(cur.getColumnIndex("D2DChillTaste")));
                Promotinalbean.setSampleGiven(cur.getString(cur.getColumnIndex("SampleGiven")));
                Promotinalbean.setHChillTaste(cur.getString(cur.getColumnIndex("HChillTaste")));
                Promotinalbean.setSaleamount(cur.getString(cur.getColumnIndex("SalesAmount")));
                recordlist.add(Promotinalbean);

            } while (cur.moveToNext());
        } else {

            Toast.makeText(getApplicationContext(),"No record to display for selected date",Toast.LENGTH_LONG).show();

        }
        PromotionalRecordAdapter notificationAdapter = new PromotionalRecordAdapter
                (ActivityPromotionalFormRecord.this, recordlist);
        ls_record.setAdapter(notificationAdapter);
    }

    class GetPromoterListRecord extends AsyncTask<String, Void, String> {
        String response;
        Object res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            HideProgres();
            if (integer.contains("UserMasterId")) {

            }
            UpdatePromotionalform();
            }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetPromoterRecord + "?PromoterId=" + PromoeterLogInId + "&Dt=" + Promoterdate;

                res = ut.OpenConnection(url, getApplicationContext());
                Log.e("response data", res + "");
                response = res.toString().replaceAll("\\\\", "");
                //  response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_PROMOTER_REPORT, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROMOTER_REPORT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("D2DChillTaste")) {
                            columnValue = jorder.getString("D2D Chill Taste");
                            values.put(columnName, columnValue);
                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        }
                    long a = sql.insert(db.TABLE_PROMOTER_REPORT, null, values);
                    Log.e("promoter Table", "" + a);

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityPromotionalFormRecord.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        int id = menuItem.getItemId();
        if (id == R.id.refresh) {
            Refresh();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public void ShowProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void HideProgres() {
        mProgress.setVisibility(View.GONE);

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
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
