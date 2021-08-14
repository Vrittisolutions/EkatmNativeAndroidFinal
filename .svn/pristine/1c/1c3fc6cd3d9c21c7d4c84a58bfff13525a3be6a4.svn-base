package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.vritti.vwb.Adapter.LeaveSummaryAdapter;
import com.vritti.vwb.Beans.LeaveBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;


public class LeaveSummary extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    static Context context;
    static MenuItem progressBar;
    ListView ls_LeaveSummary;
    LinearLayout ls_Team;
    public static SharedPreferences userpreferences;
    ArrayList<LeaveBean> lsSummaryList;
    LeaveSummaryAdapter leaveSummaryAdapter;
    Toolbar toolbar;
    ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_leave_summary);
        initView();
        setListner();
        context = LeaveSummary.this;

        ut = new Utility();
        cf = new CommonFunction(context);
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
        lsSummaryList = new ArrayList<LeaveBean>();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        if (checkSummaryList()) {
            updateSummaryList();
        } else {
            if (ut.isNet(getApplicationContext())) {
                showProgress();
                new StartSession(LeaveSummary.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new DownloadGetLeaveSummary().execute();
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

        ls_LeaveSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String availableLeave = lsSummaryList.get(position).getBalance();
                String Leavecode = lsSummaryList.get(position).getLeaveCode();
                Float a = 0.0f;
                try {
                    a = Float.parseFloat(availableLeave);
                } catch (Exception Ex) {
                    Ex.printStackTrace();
                    a = 0.0f;
                }
                if (a > 0 || Leavecode.equals("OnDuty")) {
                    Intent intent = new Intent(LeaveSummary.this, ApplyLeaveMainActivity.class);
                    intent.putExtra("LeaveCode", lsSummaryList.get(position).getLeaveCode());
                    intent.putExtra("balance",lsSummaryList.get(position).getBalance());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "You have Consumed All " + lsSummaryList.get(position).getLeaveCode(), Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    private void initView() {
        ls_LeaveSummary = (ListView) findViewById(R.id.ls_lvSummary);//toolbar_progress_summry
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_summry);
    }

    private void setListner() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);

        setSupportActionBar(toolbar);
    }

    private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    private void DismissProgress() {
        mprogress.setVisibility(View.GONE);

    }


    private void updateSummaryList() {
        lsSummaryList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_LEAVE_SUMMARY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                LeaveBean bean = new LeaveBean();
                bean.setLeaveCode(cur.getString(cur.getColumnIndex("LeaveCode")));
                bean.setOpenBal(cur.getString(cur.getColumnIndex("OpenBal")));
                bean.setCredit(cur.getString(cur.getColumnIndex("Credit")));
                bean.setConsumed(cur.getString(cur.getColumnIndex("Consumed")));
                bean.setBalance(cur.getString(cur.getColumnIndex("Balance")));
                bean.setOpenBalPer(cur.getString(cur.getColumnIndex("OpenBalPer")));
                bean.setConsumedPer(cur.getString(cur.getColumnIndex("ConsumedPer")));
                bean.setCreditPer(cur.getString(cur.getColumnIndex("CreditPer")));
                bean.setBalancePer(cur.getString(cur.getColumnIndex("BalancePer")));
                lsSummaryList.add(bean);
            } while (cur.moveToNext());
        }
        leaveSummaryAdapter = new LeaveSummaryAdapter(LeaveSummary.this, lsSummaryList);
        ls_LeaveSummary.setAdapter(leaveSummaryAdapter);
    }

    private Boolean checkSummaryList() {
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_LEAVE_SUMMARY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }


    class DownloadGetLeaveSummary extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getLeaveSummary;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                SQLiteDatabase sql = db.getWritableDatabase();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_LEAVE_SUMMARY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LEAVE_SUMMARY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_LEAVE_SUMMARY, null, values);
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
            if (integer.contains("LeaveCode")) {
                updateSummaryList();
            } else {
                Toast.makeText(getApplicationContext(), "Could not connect to the server", Toast.LENGTH_LONG).show();
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave_summary, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lvrc) {
            Intent intent = new Intent(getApplicationContext(), LeaveRecords.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.refresh) {

            if (ut.isNet(getApplicationContext())) {
                showProgress();
                new StartSession(LeaveSummary.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new DownloadGetLeaveSummary().execute();
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
            return true;

        } else {
                return super.onOptionsItemSelected(item);
        }
    }
}
