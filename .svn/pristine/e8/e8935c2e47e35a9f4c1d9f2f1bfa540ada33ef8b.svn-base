package com.vritti.vwblib.vworkbench;


import android.annotation.SuppressLint;
import android.app.AlarmManager;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.BackgroundService;
import com.vritti.vwblib.Services.PaidLocationFusedLocationTracker1;
import com.vritti.vwblib.classes.CommonFunction;


public class ActivityLogIn extends AppCompatActivity {
   // DownloadAuthenticate();

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    public static Context context;
    Button btnLogin;
    Boolean IsSessionActivate, IsValidUser;
    Spinner edEnv, edPlant;
    SharedPreferences userpreferences;
    String  IsCrmUser;
    private ProgressDialog progressDialog;
    EditText edLoginId, edPassword, edmob;
    SQLiteDatabase sql;
    ProgressBar mprogress;
    public static EditText textotp;
    public static Intent igpsalarm;
    String App_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_main);
        InitView();
        setListner();
        Intent intent=getIntent();
        App_version=intent.getStringExtra("version");
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


        sql = db.getWritableDatabase();
        if (ut.isNet(getApplicationContext())) {
            new DownloadGetEnvJSON().execute();
        } else {
            ut.displayToast(getApplicationContext(), "No Internet Connetion");
        }

    }

    private void setListner() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()) {
                    LoginId = edLoginId.getText().toString();
                    Password = edPassword.getText().toString();
                    MobileNo = edmob.getText().toString();
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("LoginId", LoginId);
                    editor.putString("Password", Password);
                    editor.putString("MobileNo", MobileNo);
                    editor.commit();
                    new DownloadIsValidUser().execute();
                }
            }
        });

        edPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String PlantName = edPlant.getSelectedItem().toString();
                String que = "SELECT PlantMasterId FROM " + db.TABLE_PLANTMASTER + " WHERE PlantName='" + PlantName + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    PlantMasterId = cur.getString(cur.getColumnIndex("PlantMasterId"));
                }
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("PlantMasterId", PlantMasterId);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edEnv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EnvMasterId = edEnv.getSelectedItem().toString();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("EnvMasterId", EnvMasterId);
                editor.commit();
                if (ut.isNet(getApplicationContext())) {
                    new DownloadPlantsJSON().execute();
                } else {
                    ut.displayToast(getApplicationContext(), "No Internet Connetion");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void InitView() {

        edEnv = (Spinner) findViewById(R.id.edEnv);
        edPlant = (Spinner) findViewById(R.id.edPlant);
        ((Spinner) findViewById(R.id.edEnv)).setSelection(0);
        ((Spinner) findViewById(R.id.edPlant)).setSelection(0);
        edLoginId = (EditText) findViewById(R.id.edLoginId);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        mprogress = (ProgressBar) findViewById(R.id.progresslog);
        edmob = (EditText) findViewById(R.id.edmob);

    }


    @SuppressLint("NewApi")
    public static void regservicenonGPS(Context mcontext) {
        /* int itime;
         itime = 15;
         long aTime = 1000 * 60 * itime;
         Intent igpsalarm = new Intent(mcontext, FusedLocationTracker.class);
         PendingIntent piHeartBeatService = PendingIntent.getService(
                 mcontext, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
         AlarmManager alarmManager = (AlarmManager) mcontext
                 .getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(piHeartBeatService);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                 System.currentTimeMillis(), aTime, piHeartBeatService);*/


            Intent service = new Intent(mcontext, PaidLocationFusedLocationTracker1.class);
            mcontext.startService(service);



    }


    private Boolean checkEditText() {
        if (EnvMasterId.equalsIgnoreCase("Environment ID")) {
            ut.displayToast(getApplicationContext(), "Please Select Environment ID");
            return false;
        } else if (PlantMasterId.equalsIgnoreCase("Plant ID")) {
            ut.displayToast(getApplicationContext(), "Please Select Plant ID");
            return false;
        } else if (!(edLoginId.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter User ID");
            return false;
        } else if (!(edPassword.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter Password");
            return false;
        } else if (!(edmob.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter Register Mobile No.");
            return false;
        }else if (!(edmob.getText().toString().length() > 9)) {
            ut.displayToast(getApplicationContext(), "Please Enter valid Mobile No.");
            return false;
        }

        return true;
    }

    class DownloadIsValidUser extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //  showProgressDialog();

            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... params) {

            String response;
            try {
                String url = CompanyURL + WebUrlClass.api_GetIsValidUser + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId, "UTF-8") + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8") + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                //IsValidUser = Boolean.parseBoolean(res);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  progressDialog.dismiss();
            // dismissProgressDialog();
            mprogress.setVisibility(View.INVISIBLE);
            if (integer.contains("true")) {
                new GetSessionFromServer().execute();
            } else if (integer.contains("Invalid Password And Plant")) {
                ut.displayToast(getApplicationContext(), "Invalid Password And Plant");
            } else if (integer.contains("Invalid Password")) {
                ut.displayToast(getApplicationContext(), "Invalid Password");
            } else if (integer.contains("You are not valid user for selected plant")) {
                ut.displayToast(getApplicationContext(), "You are not valid user for selected plant");
            } else {
                ut.displayToast(getApplicationContext(), "Not Valid User");
            }
        }

    }

    class GetSessionFromServer extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //showProgressDialog();

            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {

//  URLEncoder.encode(EnvMasterId, "UTF-8")
            try {
                String url = CompanyURL + WebUrlClass.api_GetSessions + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8") + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   progressDialog.dismiss();
            // dismissProgressDialog();
            mprogress.setVisibility(View.INVISIBLE);
            if (IsSessionActivate) {
           // new DownloadAuthenticate().execute();
           new DownloadUserMasterIdFromServer().execute();

            } else {
                ut.displayToast(getApplicationContext(), "The server is taking too long to respond OR something " +
                        "is wrong with your internet connection. Please try again later");
            }

        }

    }

    private JSONObject getJobject() {
        JSONObject data = null;
        try {
            data = new JSONObject();
            String Token = userpreferences.getString("Token", null);

            data.put("App_Name", WebUrlClass.AppNameFCM);
            data.put("UserMasterId", UserMasterId);
            data.put("DeviceId", Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return data;
    }

    class UploadFCMDetail extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.FCMurl;
            try {
                res = ut.OpenPostConnection(url, params[0],getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            new DownloadIfCRMUserJson().execute();

            if (integer.equalsIgnoreCase("Fail")) {

            } else if (integer.equalsIgnoreCase("Error")) {
                ut.displayToast(getApplicationContext(), "FCM Registration Failed");

            } else if (integer.equalsIgnoreCase("Success")) {
                // ut.displayToast(getApplicationContext(), "FCM Registration Failed");
            }
        }
    }

    class DownloadIfCRMUserJson extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_getIfCRMUser + "?UserMstrId=" + URLEncoder.encode(UserMasterId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ut.displayToast(getApplicationContext(), "Unsupported Encoding Exception occurred");
            }

            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsCrmUser = res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("IsCrmUser", IsCrmUser);
            editor.commit();
            new DownloadLeavereportingTo().execute();
        }
    }


    class DownloadUserMasterIdFromServer extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetUserMasterId;

            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
              /*  JSONArray jResults = new JSONArray(res);
                for (int index = 0; index < jResults.length(); index++) {

                    JSONObject jorder = jResults.getJSONObject(index);*/
                UserMasterId = res;
                /*}*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            /*if (res.contains("UserMasterId")) {*/
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("UserMasterId", UserMasterId);
            editor.commit();
            JSONObject Jobj = getJobject();
            String jobject = Jobj.toString().replaceAll("\\\\", "");
            new UploadFCMDetail().execute(jobject);
        }
    }

    class DownloadAuthenticate extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetOTPServer + "?MobNo=" + MobileNo + "&UserLoginId=" + LoginId + "&AppName=" + WebUrlClass.AppName;
            //UserLoginId=300207&AppName
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }


        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            mprogress.setVisibility(View.GONE);
            if (res.contains("#Success")) {
                String data[] = res.split("#");
                final String OPT = data[0];

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityLogIn.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.vwb_otp_lay, null);
                dialogBuilder.setView(dialogView);

                // set the custom dialog components - text, image and button
                textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
                Button button = (Button) dialogView.findViewById(R.id.txt_submit);
                Button txt_resend_otp = (Button) dialogView.findViewById(R.id.txt_resend_otp);
               // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
                dialogBuilder.setCancelable(false);
                final AlertDialog b = dialogBuilder.create();
                b.show();
                // if button is clicked, close the custom dialog
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String entrotp = textotp.getText().toString().trim();
                        if (!(entrotp.equals(""))) {
                            if (entrotp.equalsIgnoreCase(OPT)) {
                                b.dismiss();
                                //Toast.makeText(getApplicationContext(), "OTP s", Toast.LENGTH_LONG).show();
                                new DownloadUserMasterIdFromServer().execute();

                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid OTP!!! try again", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_LONG).show();
                        }
                    }
                });
/*
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
*/

                txt_resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MobileNo = edmob.getText().toString();
                        LoginId = edLoginId.getText().toString();

                        new DownloadAuthenticate().execute();

                    }
                });

            } else if (res.contains("User Not Found")) {
                Toast.makeText(getApplicationContext(), "Please Enter Register Mobile Number", Toast.LENGTH_LONG).show();
            } else if (res.contains("UserId and Password not found in ERPModuleSetUp")) {
                Toast.makeText(getApplicationContext(), "OTP service is not registered ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "temporarily unavailable service!!! Please try after some time..", Toast.LENGTH_LONG).show();
            }

        }
    }

    class DownloadPlantsJSON extends AsyncTask<String, String, String> {
        String res;
        List plantsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //  showProgressDialog();
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getPlants + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&PlantId=";
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                //  res = res.replaceAll("\\\"", "");
                //  res = res.replaceAll("", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);
                sql.delete(db.TABLE_PLANTMASTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PLANTMASTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                ContentValues values = new ContentValues();
                plantsName = new ArrayList();
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (columnName.equalsIgnoreCase("PlantMasterId")) {

                        } else {
                            plantsName.add(jorder.getString("PlantName"));
                        }
                    }
                    long a = sql.insert(db.TABLE_PLANTMASTER, null, values);
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
            //progressDialog.dismiss();
            // dismissProgressDialog();
            mprogress.setVisibility(View.INVISIBLE);
            if (integer.contains("PlantMasterId")) {
                Collections.sort(plantsName);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, plantsName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edPlant.setAdapter(dataAdapter);
            } else {
                ut.displayToast(getApplicationContext(), "Couldn't Connect to the Server");
            }
        }

    }

    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, Integer> {
        String res;
        List<String> EnvName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            //showProgressDialog();
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            //  dismissProgressDialog();
            mprogress.setVisibility(View.INVISIBLE);

            if (res.contains("AppEnvMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(res);
                    EnvName = new ArrayList<String>();
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        EnvName.add(jorder.getString("AppEnvMasterId"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, EnvName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edEnv.setAdapter(dataAdapter);
            } else {
                ut.displayToast(getApplicationContext(), "Error in Parsing EnvMaster ID");
            }
        }
    }

    class DownloadLeavereportingTo extends AsyncTask<String, Void, String> {

        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_ReportingTo;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_LEAVE_REPORTING_TO, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LEAVE_REPORTING_TO, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_LEAVE_REPORTING_TO, null, values);
                    String data = a + "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            new DownloadUserlistData().execute();
        }

    }

    class DownloadUserlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUnChkUser_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ALL_MEMBERS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ALL_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_ALL_MEMBERS, null, values);
                    String data = a + "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            mprogress.setVisibility(View.GONE);
            cf.RefreshDefault();
            regservicenonGPS(getApplicationContext());
            backgroundRefresh();
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ActivityLogIn.this,ActivityMain.class);
           // intent.putExtra("version",App_version);
            startActivity(intent);
            finish();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, ActivityLogIn.this, 0).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActivityLogIn.this, CompanyURLActivity.class);
        startActivity(intent);
        finish();
    }

    void backgroundRefresh() {
        int itime = 60;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_SETTING, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                itime = c.getInt(c.getColumnIndex("SettingValue"));
            } while (c.moveToNext());
        }
        long aTime = 1000 * 60 * itime;
        Intent igpsalarm = new Intent(getApplicationContext(), BackgroundService.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(
                getApplicationContext(), 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), aTime, piHeartBeatService);
    }


}

