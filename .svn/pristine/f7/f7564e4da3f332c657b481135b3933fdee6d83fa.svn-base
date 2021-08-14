package com.vritti.vwb.vworkbench;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by sharvari on 29-Jun-18.
 */

public class ActivityTrailActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    String  ActivityId;
    SharedPreferences userpreferences;
    ProgressBar toolbar_progress_App_bar;
    TextView txt_issueto,txt_assign,txt_activity_type,txt_workspace,txt_maingroup,
            txt_subgroup,txt_estimate,txt_booked_hr,txt_remark,txt_start_date,txt_end_date,
            txt_assign_date,txt_priority,txt_status,txt_activity_trail;
    Date initDate;
    LinearLayout len_activity_trail,len_trail_desc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_trail_lay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        toolbar_progress_App_bar= (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

        txt_issueto=(TextView) findViewById(R.id.txt_issueto);
        txt_assign=(TextView) findViewById(R.id.txt_assign);
        txt_activity_type=(TextView) findViewById(R.id.txt_activity_type);
        txt_workspace=(TextView) findViewById(R.id.txt_workspace);
        txt_maingroup=(TextView) findViewById(R.id.txt_maingroup);
        txt_subgroup=(TextView) findViewById(R.id.txt_subgroup);
        txt_estimate=(TextView) findViewById(R.id.txt_estimate);
        txt_booked_hr=(TextView) findViewById(R.id.txt_booked_hr);
        txt_remark=(TextView) findViewById(R.id.txt_remark);
        txt_start_date=(TextView) findViewById(R.id.txt_start_date);
        txt_end_date=(TextView) findViewById(R.id.txt_end_date);
        txt_assign_date=(TextView) findViewById(R.id.txt_assign_date);
        txt_priority=(TextView) findViewById(R.id.txt_priority);
        txt_status=(TextView) findViewById(R.id.txt_status);
        txt_activity_trail=(TextView) findViewById(R.id.txt_activity_trail);

        len_activity_trail=(LinearLayout) findViewById(R.id.len_activity_trail);
        len_trail_desc=(LinearLayout) findViewById(R.id.len_trail_desc);



        Intent intent = getIntent();
        ActivityId = intent.getStringExtra("ActId");


        if (isnet()) {
            new StartSession(ActivityTrailActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTrailData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }
        if (isnet()) {
            new StartSession(ActivityTrailActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadActTraiDetailsJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

    }

    class DownloadTrailData extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;
        JSONArray jResults;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();


        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetActDetailsPnl + "?activityId=" + ActivityId;

            try {
                res = ut.OpenConnection(url, ActivityTrailActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
               // response = response.substring(1, response.length() - 1);
                jResults = new JSONArray(response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.equalsIgnoreCase("[]")) {
                //txt_chatroom_add.setVisibility(View.VISIBLE);

            } else {
                if (response != null) {
                    for (int i = 0; i < jResults.length(); i++) {
                        try {
                            JSONObject jorder = jResults.getJSONObject(i);

                            txt_issueto.setText("Issue to : " + jorder.getString("UserName"));
                            txt_assign.setText("Assigned by : " + jorder.getString("Assigned_By"));
                            txt_activity_type.setText("Activity Type : " + jorder.getString("ActivityTypeName"));
                            txt_workspace.setText(getResources().getString(R.string.Trail_workspace) + jorder.getString("ProjectName"));
                            txt_maingroup.setText(getResources().getString(R.string.Trail_maingroup)+ jorder.getString("ModuleName"));
                            txt_subgroup.setText(getResources().getString(R.string.Trail_subgroup)+ jorder.getString("UnitDesc"));

                            String StartDate=jorder.getString("StartDate");
                            String[] arr = StartDate.split("T");

                            StartDate=arr[0];
                            try {
                                initDate = new SimpleDateFormat("yyyy-MM-dd").parse(StartDate);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                StartDate = formatter.format(initDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            txt_start_date.setText("Start Date : " + StartDate);

                            String EndDate=jorder.getString("EndDate");
                             arr = EndDate.split("T");

                            EndDate=arr[0];
                            try {
                                initDate = new SimpleDateFormat("yyyy-MM-dd").parse(EndDate);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                EndDate = formatter.format(initDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            txt_end_date.setText("End Date : " + EndDate);

                            String AddedDt=jorder.getString("AddedDt");
                            arr = AddedDt.split("T");

                            AddedDt=arr[0];
                            try {
                                initDate = new SimpleDateFormat("yyyy-MM-dd").parse(AddedDt);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                AddedDt = formatter.format(initDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            txt_assign_date.setText("Assigned : " + AddedDt);
                            txt_estimate.setText("Estimated hr : " + jorder.getString("HoursRequired"));
                            txt_booked_hr.setText("Booked hr  : " + jorder.getString("TotalHoursBooked"));
                            txt_remark.setText("Remark  : " + jorder.getString("Remarks"));
                            txt_status.setText("Status : " + jorder.getString("Status"));
                            txt_priority.setText("Priority : " + jorder.getString("PriorityName"));

                            String Username=jorder.getString("UserName");
                            String Assigned_By=jorder.getString("Assigned_By");


                           // txt_activity_trail.setText("Activity assigned to " + Username + " by " + Assigned_By+ " on "+AddedDt);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                } else {

                }
            }
        }

    }


    private boolean isnet(){
        // TODO Auto-generated method stub
        Context context=this.getApplicationContext();
        ConnectivityManager cm=(ConnectivityManager)context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        if(netInfo!=null&&netInfo.isConnectedOrConnecting()){
        return true;
        }else{
        Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();
        return false;
        }

    }
    public void showprogress() {
        toolbar_progress_App_bar.setVisibility(View.VISIBLE);

    }

    public void hidprogress() {
        toolbar_progress_App_bar.setVisibility(View.GONE);

    }
    class DownloadActTraiDetailsJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.GetActTraiDetailsAndroid + "?activityId=" + ActivityId;

            try {
                res = ut.OpenConnection(url, ActivityTrailActivity.this);
                response = res.toString();
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
            // progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                JSONArray jResults = null;
                try {

                    len_activity_trail.removeAllViews();
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        len_trail_desc.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        String TrailMessage=jsonObject.getString("TrailMessage");

                        TextView txt_activity_trail;
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.vwb_trail_details_item_lay, len_activity_trail, false);
                        LinearLayout mlinitem = (LinearLayout) view.findViewById(R.id.len_trail);
                        txt_activity_trail = (TextView) view.findViewById(R.id.txt_activity_trail);


                        txt_activity_trail.setText(TrailMessage);




                        //Assigned To
                        len_activity_trail.addView(mlinitem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
