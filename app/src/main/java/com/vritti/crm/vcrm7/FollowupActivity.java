package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.CallHistoryAdapter;
import com.vritti.crm.adapter.FollowupAdapter;
import com.vritti.crm.bean.Schedule;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.vritti.crm.vcrm7.CountryListActivity.COUNTRY;

public class FollowupActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    private String settingKey, dabasename;
    public String IsChatApplicable;
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    Toolbar toolbar;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    String UserType;
    RecyclerView callhistory_listview;
    ProgressBar center_progress;
    ArrayList<Schedule> callHistoryArrayList;
    FollowupAdapter callHistoryAdapter;
    TextView txt_firm_title;
    String callid = "", firmname = "", calltype = "", SourceId = "", FormId = "", callstatu = "", table = "", ProspectId = "";
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    private String dateString="";
    private String FromDate="",ToDate="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_call_history_activity);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        sql = db.getWritableDatabase();

        InitView();

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Followup Schedule");

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        dateString = sdf.format(date1);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        if (ut.isNet(FollowupActivity.this)) {
            center_progress.setVisibility(View.VISIBLE);
            new StartSession(FollowupActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadNextActionData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    private void InitView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        callhistory_listview = findViewById(R.id.callhistory_listview);
        center_progress = findViewById(R.id.center_progress);
        txt_firm_title = findViewById(R.id.txt_firm_title);
        callHistoryArrayList = new ArrayList<>();
    }

    public void ActionClick(String formattedDate) {
        Intent intent = new Intent();
        intent.putExtra("Name", formattedDate);
        setResult(COUNTRY, intent);
        finish();
    }

    class DownloadCallHistoryData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            center_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            callHistoryArrayList.clear();
            try {
                String url = CompanyURL+WebUrlClass.GetScheduledata+"?CurrentCallOwner="+UserMasterId+"&nextactionfromdate="+FromDate+"&nextactiontodate="+ToDate;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                    //response = response.replaceAll("\\\\\\\\/", "");
                   // response = response.substring(1, response.length() - 1);
                    JSONArray jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jcallhistory = jResults.getJSONObject(i);

                        Schedule schedule = new Schedule();
                        schedule.setTheDate(jcallhistory.getString("TheDate"));
                        schedule.setDOW(jcallhistory.getString("DOW"));
                        schedule.setVisit(jcallhistory.getString("Visit"));
                        schedule.setTelephone(jcallhistory.getString("Telephone"));
                        schedule.setEmail(jcallhistory.getString("Email"));
                        schedule.setVisitPlan(jcallhistory.getString("VisitPlan"));
                        schedule.setTravelPlan(jcallhistory.getString("TravelPlan"));
                        callHistoryArrayList.add(schedule);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            center_progress.setVisibility(View.GONE);
            if (response.equals("[]")) {
                Toast.makeText(FollowupActivity.this, "Record not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                    callHistoryAdapter = new FollowupAdapter(FollowupActivity.this, callHistoryArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    callhistory_listview.setLayoutManager(mLayoutManager);
                    callhistory_listview.setItemAnimator(new DefaultItemAnimator());
                    callhistory_listview.setAdapter(callHistoryAdapter);

                }

            }
        }
    }


    class DownloadNextActionData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            center_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            callHistoryArrayList.clear();
            try {
                String url =CompanyURL+WebUrlClass.GetFromToNextActionDate+"?NextActDateTime="+dateString;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                    //response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            center_progress.setVisibility(View.GONE);
            if (response.equals("[]")) {
                Toast.makeText(FollowupActivity.this, "Record not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response.contains("FromDate")) {

                    JSONArray jResults = null;
                    try {
                        jResults = new JSONArray(response);
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jsonObject=jResults.getJSONObject(i);
                            FromDate=jsonObject.getString("FromDate");
                            FromDate=FromDate.replaceAll("\\\\\\\\/", "");
                            ToDate=jsonObject.getString("ToDate");
                            ToDate=ToDate.replaceAll("\\\\\\\\/", "");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (ut.isNet(FollowupActivity.this)) {
                        center_progress.setVisibility(View.VISIBLE);
                        new StartSession(FollowupActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCallHistoryData().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}
