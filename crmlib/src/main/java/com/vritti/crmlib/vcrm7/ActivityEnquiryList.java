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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.EnquiryAdapter;
import com.vritti.crmlib.bean.EnquiryBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ActivityEnquiryList extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView ls_enquiry;
    SQLiteDatabase sql;
    ArrayList<EnquiryBean> EnqList;

    SharedPreferences userpreferences;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_enquiry_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        sql = db.getWritableDatabase();
        EnqList = new ArrayList<EnquiryBean>();
        ls_enquiry = (ListView) findViewById(R.id.ls_enquiry);
        mProgress = (ProgressBar) findViewById(R.id.progressbar_enquiry);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        if (checkDb()) {
            UpdateEnquiryList();
        } else {
            refresh();
        }


        ls_enquiry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String name = EnqList.get(position).getCustomerName();

                Intent intent = new Intent(ActivityEnquiryList.this, ActvityEnquiryDetails.class);
                intent.putExtra("EnquiryDetailExtra", EnqList.get(position));
                startActivity(intent);
                finish();

            }
        });
    }


    private void UpdateEnquiryList() {
        EnqList.clear();
        String query = "SELECT * FROM "
                + db.TABLE_ENQUIRY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                EnquiryBean notificationBean = new EnquiryBean();
                notificationBean.setEnquiryRegistryId(cur.getString(cur.getColumnIndex("EnquiryRegistryId")));
                notificationBean.setEnquiryDate(cur.getString(cur.getColumnIndex("EnquiryDate")));
                notificationBean.setRegistryById(cur.getString(cur.getColumnIndex("RegistryById")));
                notificationBean.setAssignedToId(cur.getString(cur.getColumnIndex("AssignedToId")));
                notificationBean.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                notificationBean.setContactNumber(cur.getString(cur.getColumnIndex("ContactNumber")));
                notificationBean.setEmail(cur.getString(cur.getColumnIndex("Email")));
                notificationBean.setEnquiryDetails(cur.getString(cur.getColumnIndex("EnquiryDetails")));
                notificationBean.setActionTaken(cur.getString(cur.getColumnIndex("ActionTaken")));
                notificationBean.setReasonForCancellation(cur.getString(cur.getColumnIndex("ReasonForCancellation")));
                notificationBean.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                notificationBean.setAddedBy(cur.getString(cur.getColumnIndex("AddedBy")));
                notificationBean.setAddeddt(cur.getString(cur.getColumnIndex("Addeddt")));
                notificationBean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                notificationBean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                notificationBean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                notificationBean.setCustomerName(cur.getString(cur.getColumnIndex("CustomerName")));
                EnqList.add(notificationBean);

            } while (cur.moveToNext());
        } else {

        }
        EnquiryAdapter notificationAdapter = new EnquiryAdapter
                (ActivityEnquiryList.this, EnqList);
        ls_enquiry.setAdapter(notificationAdapter);
    }

    private Boolean checkDb() {
        String query = "SELECT * FROM "
                + db.TABLE_ENQUIRY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityEnquiryList.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_enquiry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        if (menuItem.getItemId() == R.id.refresh) {
            refresh();
            return true;
        } else {
            return (super.onOptionsItemSelected(menuItem));
        }


    }


    class DownloadEnquiryAssigned extends AsyncTask<String, Void, String> {
        String response = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Enuiry;
            try {

                response = ut.OpenConnection(url, getApplicationContext());
                response = response.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ENQUIRY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ENQUIRY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }
                    long a = sql.insert(db.TABLE_ENQUIRY, null, values);
                    Log.e("Enuiry Table", "" + a);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            HideProgress();

            if (response.contains("EnquiryRegistryId")) {
                UpdateEnquiryList();

            } else if (response.equalsIgnoreCase("[]")) {
                UpdateEnquiryList();
            } else {
                Toast.makeText(getApplicationContext(), "can not fetch Enquiry data", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void refresh() {
        if (isnet()) {
            new StartSession(ActivityEnquiryList.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    ShowProgress();
                    new DownloadEnquiryAssigned().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(ActivityEnquiryList.this, msg, Toast.LENGTH_LONG).show();
                    HideProgress();
                }
            });

        }
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

    private void ShowProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void HideProgress() {
        mProgress.setVisibility(View.GONE);

    }

}
