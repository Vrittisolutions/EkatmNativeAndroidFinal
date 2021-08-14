package com.vritti.vwb.vworkbench;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;



import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.WorkspacewiseActCntAdapter;
import com.vritti.vwb.Beans.WorkspacewiseActCntBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 2/8/2017.
 */
public class WorkspacewiseActCntActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    private ProgressBar mProgress;
    ListView ls_work_act_cnt;
    SharedPreferences userpreferences;
    String IsCrmUser;
    SQLiteDatabase sql;
    List<WorkspacewiseActCntBean> ls_WorkspacewiseActCnt;
    WorkspacewiseActCntAdapter adapter;
    //MenuItem refresh;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_workspacewise_act_cnt);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);



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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        InitView();
        setListners();

        if (getWorkspacewiseActCnt() > 0) {
            showWorkspacewiseActcnt();
        } else {
            if (isnet()) {
                new StartSession(WorkspacewiseActCntActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGetWorkspacewiseActCnt().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //  refresh = menu.findItem(R.id.refresh1);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh1) {
            if (isnet()) {
                new StartSession(WorkspacewiseActCntActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGetWorkspacewiseActCnt().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);

                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {


        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        ls_work_act_cnt = (ListView) findViewById(R.id.ls_work_act_cnt);
        ls_WorkspacewiseActCnt = new ArrayList<WorkspacewiseActCntBean>();
    }

    private void setListners() {

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void showWorkspacewiseActcnt() {
        ls_WorkspacewiseActCnt.clear();
        String que = "SELECT * FROM " + db.TABLE_WORKSPACEWISE_ACT_CNT;
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                WorkspacewiseActCntBean bean = new WorkspacewiseActCntBean();
                bean.setProjectId(cur.getString(cur.getColumnIndex("ProjectId")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setAwaitingActivities(cur.getString(cur.getColumnIndex("AwaitingActivities")));
                bean.setComplete(cur.getString(cur.getColumnIndex("Complete")));
                bean.setCriticalActivity(cur.getString(cur.getColumnIndex("CriticalActivity")));
                bean.setOPENAct(cur.getString(cur.getColumnIndex("OpenAct")));
                bean.setTotalAssigned(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setTotalOverdue(cur.getString(cur.getColumnIndex("TotalOverdue")));
                ls_WorkspacewiseActCnt.add(bean);
            } while (cur.moveToNext());
            adapter = new WorkspacewiseActCntAdapter(WorkspacewiseActCntActivity.this, ls_WorkspacewiseActCnt);
            ls_work_act_cnt.setAdapter(adapter);
        }
    }

    private int getWorkspacewiseActCnt() {
        String que = "SELECT * FROM " + db.TABLE_WORKSPACEWISE_ACT_CNT;
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            return cur.getCount();
        } else {
            return 0;
        }
    }

    class DownloadGetWorkspacewiseActCnt extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (res.contains("ProjectId")) {
                dismissProgressDialog();
                showWorkspacewiseActcnt();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getWorkspacewiseActCnt1 + "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                String response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
              //  response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_WORKSPACEWISE_ACT_CNT, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_WORKSPACEWISE_ACT_CNT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_WORKSPACEWISE_ACT_CNT, null, values);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),ActivityMain.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
