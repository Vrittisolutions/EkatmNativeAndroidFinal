package com.vritti.vwblib.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.LeaveRecordAdapter;
import com.vritti.vwblib.Beans.LeaveRecordBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;


public class LeaveRecords extends AppCompatActivity {

    public static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;

    static Context context;
    static MenuItem progressBar;
    ListView ls_LeaveRecord;
    LinearLayout ls_Team;
    public static SharedPreferences AtendanceSheredPreferance;
    public static SharedPreferences userpreferences;
    ArrayList<LeaveRecordBean> lsRecordList;
    LeaveRecordAdapter leaverecAdapter;
    Toolbar toolbar;
    public static ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_leave_records);
        initView();
        setListner();

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        lsRecordList = new ArrayList<LeaveRecordBean>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        if (checkRecordList()) {
            updateRecordList();
        } else {
            refresh();
        }


    }

    private void initView() {
        ls_LeaveRecord = (ListView) findViewById(R.id.ls_lvrecods);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_lvrecord);
    }

    private void setListner() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    private void updateRecordList() {
        lsRecordList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_LEAVE_RECORDS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                LeaveRecordBean bean = new LeaveRecordBean();
                bean.setMLId(cur.getString(cur.getColumnIndex("MLId")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setApprovedBy(cur.getString(cur.getColumnIndex("ApprovedBy")));
                bean.setReason(cur.getString(cur.getColumnIndex("Reason")));
                bean.setAddress(cur.getString(cur.getColumnIndex("Address")));
                bean.setContact(cur.getString(cur.getColumnIndex("Contact")));
                bean.setLeaveType(cur.getString(cur.getColumnIndex("LeaveType")));
                bean.setApprovedDt(cur.getString(cur.getColumnIndex("ApprovedDt")));
                bean.setLeaveCount(cur.getString(cur.getColumnIndex("LeaveCount")));
                bean.setHalfLeaveOption(cur.getString(cur.getColumnIndex("HalfLeaveOption")));
                bean.setHalfLeaveOptionTo(cur.getString(cur.getColumnIndex("HalfLeaveOptionTo")));
                bean.setApproverRemark(cur.getString(cur.getColumnIndex("ApproverRemark")));
                lsRecordList.add(bean);
            } while (cur.moveToNext());
        }
        leaverecAdapter = new LeaveRecordAdapter(LeaveRecords.this, lsRecordList);
        ls_LeaveRecord.setAdapter(leaverecAdapter);
    }

    private Boolean checkRecordList() {
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_LEAVE_RECORDS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);
    }

    private void DismissProgress() {
        mprogress.setVisibility(View.GONE);
    }

    private void refresh() {
        if (ut.isNet(getApplicationContext())) {
            showProgress();
            new StartSession(LeaveRecords.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetLeaveRecords().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    DismissProgress();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadGetLeaveRecords extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_Records;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                SQLiteDatabase sql = db.getWritableDatabase();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_LEAVE_RECORDS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LEAVE_RECORDS, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_LEAVE_RECORDS, null, values);
                    String s = a + "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            DismissProgress();
            if (integer.contains("MLId")) {
                updateRecordList();
            } else {
                Toast.makeText(getApplicationContext(), "Could not connect to the server", Toast.LENGTH_LONG).show();
            }


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave_records, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ref) {

            if (ut.isNet(getApplicationContext())) {
                refresh();
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
            return true;
        } else {

            return super.onOptionsItemSelected(item);
        }
    }
}
