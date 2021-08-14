package com.vritti.vwblib.vworkbench;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.TimeSheetLogAdapter;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.TimesheetLog;

/**
 * Created by sharvari on 28-Jun-18.
 */

public class TimeSheetLogActivity extends AppCompatActivity {


    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView listview_timesheet;

    TimesheetLog timesheetLog;
    ArrayList<TimesheetLog>timesheetLogArrayList=new ArrayList<>();
    TimeSheetLogAdapter timeSheetLogAdapter;
    SharedPreferences userpreferences;
    ProgressBar toolbar_progress_App_bar;
    String ActivityId;
    TextView txt_no_details;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_timesheet_log_lay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(" Timesheet Log");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.vworkbench);
        setSupportActionBar(toolbar);
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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        listview_timesheet=(ListView) findViewById(R.id.listview_timesheet);
        toolbar_progress_App_bar= (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        txt_no_details=(TextView) findViewById(R.id.txt_no_details);
        timesheetLogArrayList=new ArrayList<>();

        CompanyURL = userpreferences.getString("CompanyURL", null);

        Intent intent=getIntent();
        ActivityId=intent.getStringExtra("ActId");


        if (isnet()) {
            new StartSession(TimeSheetLogActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTimesheetLog().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

    }

    class DownloadTimesheetLog extends AsyncTask<String, Void, String> {

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
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetTimesheetDetailsPnl + "?activityId="+ActivityId;

            try {
                res = ut.OpenConnection(url,TimeSheetLogActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                JSONArray jResults = new JSONArray(response);
                timesheetLogArrayList.clear();
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    timesheetLog = new TimesheetLog();
                    timesheetLog.setTimeSheetId(jorder.getString("TimeSheetId"));
                    timesheetLog.setRemarks(jorder.getString("Remarks"));
                    timesheetLog.setTimeFrom(jorder.getString("TimeFrom"));
                    timesheetLog.setTimeTo(jorder.getString("TimeTo"));
                    timesheetLog.setWorkDate(jorder.getString("WorkDate"));
                    timesheetLog.setHours(jorder.getString("Hours"));
                    timesheetLog.setActivityTypeName(jorder.getString("ActivityTypeName"));
                    timesheetLog.setUserName(jorder.getString("UserName"));
                    timesheetLogArrayList.add(timesheetLog);
                }

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
                    timeSheetLogAdapter=new TimeSheetLogAdapter(TimeSheetLogActivity.this,timesheetLogArrayList);
                    if(timeSheetLogAdapter.getCount()!=0){
                        listview_timesheet.setAdapter(timeSheetLogAdapter);
                        // txt_no_record.setVisibility(View.GONE);
                        listview_timesheet.setVisibility(View.VISIBLE);

                    }else{
                        txt_no_details.setVisibility(View.VISIBLE);

                       // txt_no_record.setVisibility(View.VISIBLE);
                        //list_open.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(),"No record found",Toast.LENGTH_SHORT).show();

                    }
                }
            }

        }

    }

    void showprogress() {
        toolbar_progress_App_bar.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        toolbar_progress_App_bar.setVisibility(View.GONE);

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

}



