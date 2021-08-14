package com.vritti.crmlib.vcrm7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.services.GPSTracker;
import com.vritti.crmlib.services.PaidLocationFusedLocationTracker1;
import com.vritti.crmlib.services.SendGPSNotification;
import com.vritti.crmlib.services.SendOfflineDatabg;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;


import static com.vritti.crmlib.R.id.txt_submit;

public class ActivityLogIn extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    Button btnLogin;
    Boolean IsSessionActivate = false, IsValidUser = false;
    Spinner edEnv, edPlant;
    SharedPreferences userpreferences;
    String UserType, Token;
    private ProgressDialog progressDialog;
    EditText edLoginId, edPassword, edmob;
    LinearLayout lay_main;
    SQLiteDatabase sql;
    private CoordinatorLayout coordinatorLayout;
    GPSTracker gps;
    public static double latitude, longitude;
    private String package1, currentVersion;
    private String version;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    String part1, part2, OTPvalue;
    public static EditText edt_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_main);
        InitView();
        edLoginId.setEnabled(false);
        edPassword.setEnabled(false);
        edmob.setEnabled(false);
        getCurrentLocation();
        btnLogin.setVisibility(View.GONE);
        setListner();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
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
        UserType = userpreferences.getString("UserType", null);
        Token = userpreferences.getString("Token", null);
        sql = db.getWritableDatabase();
        //  registerInBackground();
        if (isnet()) {
            new DownloadGetEnvJSON().execute();

        } else {
            lay_main.setVisibility(View.GONE);
            callSnackbar("No internet connection. Please try again later.");
        }

       /* try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/



        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();


        StrictMode.setThreadPolicy(policy);

        String newVersion = null;

        try {
            currentVersion  = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            newVersion = Jsoup
                    .connect(
                            "https://play.google.com/store/apps/details?id="
                                    + "vcrm7.vritti.com.vcrm7" + "&hl=en")
                    .timeout(30000)
                    .userAgent(
                            "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get()
                    .select("div[itemprop=softwareVersion]").first()
                    .ownText();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (newVersion != null && !newVersion.isEmpty()) {
            if (Float.valueOf(currentVersion) < Float.valueOf(newVersion)) {
                Toast.makeText(ActivityLogIn.this,"New Update Available",Toast.LENGTH_SHORT).show();
            }
        }
*/


    }

    private void setListner() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edLoginId.getText().toString().equalsIgnoreCase("")
                        && !edPassword.getText().toString().equalsIgnoreCase("")
                        && !edmob.getText().toString().equalsIgnoreCase("")) {

                    LoginId = edLoginId.getText().toString();
                    Password = edPassword.getText().toString();
                    MobileNo = edmob.getText().toString();
                    new DownloadIsValidUser().execute();

                } else {
                    Toast.makeText(ActivityLogIn.this, "Please fill all data", Toast.LENGTH_LONG).show();
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

                edLoginId.setEnabled(true);
                edPassword.setEnabled(true);
                edmob.setEnabled(true);
                edLoginId.setFocusable(true);
                edPassword.setFocusable(true);
                edmob.setFocusable(true);
                btnLogin.setVisibility(View.VISIBLE);
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

                if (isnet()) {
                    new DownloadPlantsJSON().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edmob.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!edLoginId.getText().toString().equalsIgnoreCase("")
                            && !edPassword.getText().toString().equalsIgnoreCase("")
                            && !edmob.getText().toString().equalsIgnoreCase("")) {
                        LoginId = edLoginId.getText().toString();
                        Password = edPassword.getText().toString();
                        MobileNo = edmob.getText().toString();
                      /*  SharedPreferences.Editor editor = userpreferences.edit();
                        editor.putString("LoginId", LoginId);
                        editor.putString("Password", Password);
                        editor.putString("MobileNo", MobileNo);
                        editor.commit();*/
                        new DownloadIsValidUser().execute();
                    } else {
                        Toast.makeText(ActivityLogIn.this, "Please fill all data", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });


    }


    protected void regservicenonGPS() {
        Intent myIntent = new Intent(this,
                PaidLocationFusedLocationTracker1.class);
        startService(myIntent);
       /* int itime;
        itime = 15;
        long aTime = 1000 * 60 * itime;
        Intent igpsalarm = new Intent(ActivityLogIn.this, FusedLocationTracker.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(
                ActivityLogIn.this, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), aTime, piHeartBeatService);*/
    }

    protected void regservicenonGPSNotfn() {
       /* Intent myIntent = new Intent(this,
                AlarmManagerBroadcastReceiverGPS.class);
        sendBroadcast(myIntent);*/
        int itime;
        itime = 15;
        long aTime = 1000 * 60 * itime;
        Intent igpsalarm = new Intent(ActivityLogIn.this, SendGPSNotification.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(
                ActivityLogIn.this, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), aTime, piHeartBeatService);
    }


    private void InitView() {
        edEnv = (Spinner) findViewById(R.id.edEnv);
        edPlant = (Spinner) findViewById(R.id.edPlant);
        ((Spinner) findViewById(R.id.edEnv)).setSelection(0);
        ((Spinner) findViewById(R.id.edPlant)).setSelection(0);
        edLoginId = (EditText) findViewById(R.id.edLoginId);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edmob = (EditText) findViewById(R.id.edmob);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        lay_main = (LinearLayout) findViewById(R.id.lay_main);
        Intent intent = getIntent();
        version = intent.getStringExtra("version");
    }

    public void callSnackbar(String sb) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "" + sb,
                        Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isnet()) {
                            new DownloadGetEnvJSON().execute();

                        } else {
                            callSnackbar("No internet connection. Please try again later.");
                        }
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();


    }

    public void callSnackbar1(String sb) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "" + sb,
                        Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ActivityLogIn.this, CompanyURLActivity.class);
                        startActivity(intent);
                        ActivityLogIn.this.finish();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();


    }

    class DownloadIsValidUser extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetIsValidUser + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");

                System.out.println("Login URL :");

//URLEncoder.encode(EnvMasterId, "UTF-8")

                res = ut.OpenConnection(url);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                IsValidUser = Boolean.parseBoolean(res);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (IsValidUser) {
                progressDialog.dismiss();
                new GetSessionFromServer().execute();
            } else {
                progressDialog.dismiss();
                //   Msg(integer);
                Toast.makeText(ActivityLogIn.this, "" + integer, Toast.LENGTH_LONG).show();
            }
        }

    }

    class GetSessionFromServer extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetSessions
                        + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8") +
                        "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");


                res = ut.OpenConnection(url);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (IsSessionActivate) {
                progressDialog.dismiss();
                if (isnet()) {
                    new GETIsValidUserMobile().execute();
                }

              /*  if (UserMasterId != null && UserType != null) {
                    Toast.makeText(ActivityLogIn.this, "Login Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ActivityLogIn.this, CallListActivity.class);
                    startActivity(intent);
                    ActivityLogIn.this.finish();

                } else {
                    if (isnet()) {
                        new DownloadUserMasterIdFromServer().execute();
                    }
                }
*/
            } else {


            }

        }

    }

    class GETIsValidUserMobile extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GETIsValidUserMobile
                        + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&MobileNo=" + URLEncoder.encode(MobileNo, "UTF-8");


                res = ut.OpenConnection(url);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (integer.equalsIgnoreCase("Invalid")) {
                Toast.makeText(ActivityLogIn.this, integer + " Mobile No.", Toast.LENGTH_LONG).show();
            } else if (integer.equalsIgnoreCase("Valid")) {
                //   new OTPGenerationASync().execute();
                new DownloadUserMasterIdFromServer().execute();
            }

        }

    }


    class POSTRegId extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            Object res;
            String response;

            String url = CompanyURL + WebUrlClass.api_RegId_post;

            try {
                res = ut.OpenPostConnection(url, params[0]);
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

            if (integer.equalsIgnoreCase("Fail")) {
                //Toast.makeText(getApplicationContext(), "FCM Registration Failed", Toast.LENGTH_SHORT).show();
            } else if (integer.equalsIgnoreCase("Error")) {
                Toast.makeText(getApplicationContext(), "FCM Registration Failed", Toast.LENGTH_SHORT).show();
            } else if (integer.equalsIgnoreCase("Success")) {

            }
            if (isnet()) {
                new DownloadUserTypeFromServer().execute();
            }
        }

    }

    class DownloadUserMasterIdFromServer extends AsyncTask<String, Void, String> {
        String res, response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUserMasterId;

            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                UserMasterId = response;
                /*}*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("UserMasterId", UserMasterId);
            editor.commit();
            String Token = userpreferences.getString("Token", null);

            JSONObject Jobj = getJobject();
            String jobject = Jobj.toString().replaceAll("\\\\", "");
            if (isnet()) {
                new POSTRegId().execute(jobject);
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

    class DownloadUserTypeFromServer extends AsyncTask<String, Void, String> {
        String res, response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetUserType
                        + "?usermasterid=" + URLEncoder.encode(UserMasterId, "UTF-8");


                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                // response = response.substring(1, response.length() - 1);

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_User_Type, null,
                        null);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_User_Type, null);
                int count = c.getCount();
                String columnName, columnValue;


                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("Code")) {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            UserType = columnValue;
                        } else if (columnName.equalsIgnoreCase("UserName")) {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            UserName = columnValue;
                        } else {

                        }
                    }

                    long a = sql.insert(db.TABLE_User_Type, null, values);
                    Log.e("Application table user ", "" + a);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //   progressDialog.dismiss();
            /*if (res.contains("UserMasterId")) {*/
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("UserType", UserType);
            editor.putString("UserName", UserName);
            editor.putString("LoginId", LoginId);
            editor.putString("Password", Password);
            editor.putString("MobileNo", MobileNo);
            editor.commit();
            if (isnet()) {
                new DownloadLeavereportingTo().execute();


            }
            int i;
            ArrayList<String> packagenameArrayList = new ArrayList<String>();
            List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(0);
            for (i = 0; i < packageInfoList.size(); i++) {
                PackageInfo packageInfo = packageInfoList.get(i);
                String packagename = packageInfo.packageName;
                packagenameArrayList.add(packagename);
            }
            if (packagenameArrayList.contains("vworkbench7.vritti.com.vworkbench7")) {

            } else {
                regservicenonGPS();
            }
            Intent intent1 = new Intent(ActivityLogIn.this,
                    SendOfflineDatabg.class);
            startService(intent1);
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ActivityLogIn.this, CallListActivity.class);
            intent.putExtra("version", version);
            startActivity(intent);
            ActivityLogIn.this.finish();
            progressDialog.dismiss();
        }

    }

    class DownloadPlantsJSON extends AsyncTask<String, Void, String> {
        String res;
        List plantsName = new ArrayList();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
           /* String url = CompanyURL + WebUrlClass.api_getPlants + "?AppEnvMasterId=" +
                    EnvMasterId;*/
            try {
                String url = CompanyURL + WebUrlClass.api_getPlants + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&PlantId=" + URLEncoder.encode("", "UTF-8") + "";
                res = ut.OpenConnection(url);
                res = res.replaceAll("\\\\", "");
                //   res = res.replaceAll("\"", "");
                //  res = res.replaceAll(" ", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);
                sql.delete(db.TABLE_PLANTMASTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PLANTMASTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                ContentValues values = new ContentValues();

                plantsName.clear();
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (columnName.equalsIgnoreCase("PlantMasterId")) {

                        } else {
                            // Msg(jorder.getString("PlantName"))
                            plantsName.add(jorder.getString("PlantName"));
                        }


                    }

                    long a = sql.insert(db.TABLE_PLANTMASTER, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  if (res.contains("PlantMasterId")) {
            progressDialog.dismiss();
            Collections.sort(plantsName, String.CASE_INSENSITIVE_ORDER);
            if (plantsName.size() > 0) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, plantsName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edPlant.setAdapter(dataAdapter);
            } else {
                edPlant.setVisibility(View.GONE);
                callSnackbar("No Plants found");
            }

            // } else {

            // }

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

    class DownloadGetEnvJSON extends AsyncTask<String, Void, String> {
        String res = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;

            try {
                res = ut.OpenConnection(url.trim());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);


                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    EnvName.add(jorder.getString("AppEnvMasterId"));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();

            //  if (res.contains("AppEnvMasterId")) {
            if (EnvName.size() > 0) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, EnvName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edEnv.setAdapter(dataAdapter);


            } else {
                lay_main.setVisibility(View.GONE);
                edEnv.setVisibility(View.GONE);
                edPlant.setVisibility(View.GONE);
                callSnackbar1("Please Enter Correct URL OR Check internet connection");
            }

           /* } else {

            }*/
        }
    }

    public String Msg(String s) {
        StringBuilder out = new StringBuilder(s);
        Pattern p = Pattern.compile("[A-Z]");
        Matcher m = p.matcher(s);
        int extraFeed = 0;
        while (m.find()) {
            if (m.start() != 0) {
                out = out.insert(m.start() + extraFeed, " ");
                extraFeed++;
            }
        }
        System.out.println(out);
        return out.toString();
    }

    private void getCurrentLocation() {


        gps = new GPSTracker(ActivityLogIn.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(),
                        Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocation(latitude,
                        longitude, 1);
                //  System.out.println(latitude + " lat " + longitude + " long");
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        if (i == 0) {
                            sb.append(address.getAddressLine(0));
                        } else {
                            sb.append("," + address.getAddressLine(i));
                        }

                    }
                    String data = address.getAdminArea();
                   /* YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
                    imggps.setVisibility(View.VISIBLE);*/
                }
            } catch (IOException e) {
                String result = "Location not Found";
               /* txtMyLocation.setText(result);
                btnSE.setVisibility(View.GONE);
                imggps.setVisibility(View.VISIBLE);*/
                Log.e("test", "Unable connect to Geocoder", e);
            }

        } else {

            gps.showSettingsAlert();
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
                res = ut.OpenConnection(url);
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

        }

    }

    class OTPGenerationASync extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String Appname = "CRM";

            try {
                String url = CompanyURL + WebUrlClass.api_GenerateOTPAPI
                        + "?MobNo=" + URLEncoder.encode(MobileNo, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&AppName=" + URLEncoder.encode(Appname, "UTF-8");


                res = ut.OpenConnection(url);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            String[] parts = integer.split("\\#"); // escape .

            if (integer.equals("UserIdandPasswordnotfoundinERPModuleSetUp")) {
                Toast.makeText(ActivityLogIn.this, "UserId and Password not found in ERPModuleSetUp", Toast.LENGTH_LONG).show();
            } else if (integer.equals("ErrorwhilesendingOTP")) {
                Toast.makeText(ActivityLogIn.this, "Error occured while sending OTP", Toast.LENGTH_LONG).show();
            } else {
                part1 = parts[0];
                Log.d("OTP", part1);
                part2 = parts[1];
                if (part2.equals("Success")) {
                    part1 = parts[0];
                    getdialog();

                }
            }
            progressDialog.dismiss();
        }

    }

    private void getdialog() {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_otp_lay, null, false);
        builder = new AlertDialog.Builder(ActivityLogIn.this);
        builder.setView(v);
        alertDialog = builder.create();

        edt_otp = (EditText) v.findViewById(R.id.edt_otp);
        final Button btn_submit = (Button) v.findViewById(txt_submit);
        final Button txt_resend = (Button) v.findViewById(R.id.txt_resend);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OTPvalue = edt_otp.getText().toString();

                if (OTPvalue.equals(part1)) {
                    if (isnet()) {
                        new DownloadUserMasterIdFromServer().execute();
                    }
                } else {
                    Toast.makeText(ActivityLogIn.this, "Enter valid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OTPGenerationASync().execute();

            }
        });

        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* if(alertDialog.isShowing()&&alertDialog!=null){
            alertDialog.cancel();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


