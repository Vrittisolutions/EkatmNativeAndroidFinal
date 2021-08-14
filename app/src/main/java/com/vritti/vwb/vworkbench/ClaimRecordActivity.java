package com.vritti.vwb.vworkbench;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ClaimRecordAdapter;
import com.vritti.vwb.Beans.ClaimRecordObjectForDate;
import com.vritti.vwb.Beans.ClaimSummayBean;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClaimRecordActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;
    SharedPreferences userpreferences;
    ArrayList<ClaimSummayBean> summayBeanArrayList;
    int deletPos =-1;

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    @BindView(R.id.recycleView)
    RecyclerView mRecycler;

    @BindView(R.id.toolbar_progress_App_bar)
    ProgressBar mProgress;
    ClaimRecordAdapter claimRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vwb_claim_record);
        ButterKnife.bind(this);

        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);

        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        summayBeanArrayList = new ArrayList<>();
        claimRecordAdapter = new ClaimRecordAdapter(this,  summayBeanArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(claimRecordAdapter);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();


        if(ut.isNet(context)){
            refresh();
        }else{
            if (checkSummaryList()) {
                updateClaimSummary();
            } else {
                refresh();
            }
        }



    }

    @OnClick(R.id.txt_add_claim)
    void claimAdd() {
        Intent intent1 = new Intent(this, ClaimNewActivity.class);
        startActivity(intent1);
    }

    public void editClaim(int adapterPosition, boolean isDelete) {
        if(isDelete)
        {
            deleteClaimApi(adapterPosition);


        }else {
            Intent intent = new Intent(this , ClaimNewActivity.class);
            intent.putExtra("editObject" , new Gson().toJson(summayBeanArrayList.get(adapterPosition)));
            startActivity(intent);

        }
    }

    private void deleteClaimApi(int adapterPosition) {
        deletPos = adapterPosition;
        final String claimHeaderId = summayBeanArrayList.get(adapterPosition).getClaimHeaderId();
        new StartSession(ClaimRecordActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DeleteClaimDelatils().execute(claimHeaderId);
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(ClaimRecordActivity.this, msg);
            }
        });

    }


    class GetClaimDetail extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            url = CompanyURL + WebUrlClass.api_GetClaimSummary + "?claimCode=&Workspace=&Status=&UserMstrId=" + UserMasterId;
            try {
                res = ut.OpenConnection(url, ClaimRecordActivity.this);
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";

                sql.delete(db.TABLE_CLAIM_SUMMARY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CLAIM_SUMMARY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_CLAIM_SUMMARY, null, values);
                    Log.e("Claim Summary", "" + a);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.setError;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hidprogress();

            if (res.contains("ClaimHeaderId")) {
                updateClaimSummary();
            } else if (res.equalsIgnoreCase("[]")) {
                Toast.makeText(ClaimRecordActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ClaimRecordActivity.this, "Could not fetch data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateClaimSummary() {
        if(summayBeanArrayList != null)
        summayBeanArrayList.clear();
        //LiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_CLAIM_SUMMARY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                ClaimSummayBean bean = new ClaimSummayBean();
                bean.setClaimHeaderId(cur.getString(cur.getColumnIndex("ClaimHeaderId")));
                bean.setApplicant(cur.getString(cur.getColumnIndex("Applicant")));
                bean.setClaimCode(cur.getString(cur.getColumnIndex("ClaimCode")));
                bean.setFormatedDate(cur.getString(cur.getColumnIndex("FormatedDate")));
                bean.setDate(cur.getString(cur.getColumnIndex("Date")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setPlantid(cur.getString(cur.getColumnIndex("plantid")));
                bean.setTotal(cur.getString(cur.getColumnIndex("Total")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setEMPID(cur.getString(cur.getColumnIndex("EMPID")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setPaidAmount(cur.getString(cur.getColumnIndex("PaidAmount")));
                bean.setBalanceAmount(cur.getString(cur.getColumnIndex("BalanceAmount")));
               // bean.setBalanceAmount(cur.getString(cur.getColumnIndex("Date")));
                summayBeanArrayList.add(bean);
            } while (cur.moveToNext());
        }

        claimRecordAdapter.notifyDataSetChanged();
        if(summayBeanArrayList.size()!=0){
            ArrayList<String> dateList = new ArrayList<>();
           for (ClaimSummayBean claimSummayBean : summayBeanArrayList){
               dateList.add(claimSummayBean.getFormatedDate());
           }
            AppCommon.getInstance(this).setClaimDateList(new Gson().toJson(new ClaimRecordObjectForDate(dateList)));
        }
        // leaveSummaryAdapter = new LeaveSummaryAdapter(ClaimRecordActivity.this, summayBeanArrayList);
        // ls_LeaveSummary.setAdapter(leaveSummaryAdapter);
    }

    private Boolean checkSummaryList() {
     //   SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_CLAIM_SUMMARY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }


    void refresh() {
        if (ut.isNet(getApplicationContext())) {
            showprogress();
            new StartSession(ClaimRecordActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetClaimDetail().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    hidprogress();
                    Toast.makeText(ClaimRecordActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ClaimRecordActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    void showprogress() {
        mProgress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mProgress.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.refresh1) {

            refresh();
            return true;
        } else if (id == android.R.id.home) {

            onBackPressed();
            return true;
        } else {

            return false;
        }
    }

    private class DeleteClaimDelatils  extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();


        }
        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebAPIUrl.api_GetRefreshChatRoom + "?ApplicationCode="+WebAPIUrl.AppNameFCM;
            String claimId = params[0];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("ClaimHeaderId" ,claimId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = CompanyURL + WebUrlClass.deleteClaimRecord ;

            try {
                res = ut.OpenPostConnection(url,jsonObject.toString(),ClaimRecordActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);




            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.equalsIgnoreCase("[]")){
                //txt_chatroom_add.setVisibility(View.VISIBLE);

            }else {
                if (response != null) {
                   if(response.equals("Success")){
                       summayBeanArrayList.remove(deletPos);
                       claimRecordAdapter.notifyItemRemoved(deletPos);

                    }
                }else{

                }
            }
        }

    }
}
