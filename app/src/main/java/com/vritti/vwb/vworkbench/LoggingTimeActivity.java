package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.ForegroundService;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.ekatm.services.SendTimeSheet;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.EnoSamplingHomePage;
import com.vritti.vwb.classes.CommonFunction;

import static com.vritti.ekatm.services.DownloadJobService.FASTEST_INTERVAL;
import static com.vritti.ekatm.services.DownloadJobService.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.vritti.ekatm.services.DownloadJobService.MIN_TIME_BW_UPDATES;

public class LoggingTimeActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    public static String str_attaindenceVerification = "0";
    public static String ActivityId="", ProjectId;
    String Flag;
    String IsCrmUser;

    static String today, FormatStDt, FormatEndDt, actid, Starttime = "", ActivityName;
    SQLiteDatabase sql;
    String link;
    public static SharedPreferences AtendanceSheredPreferance, userpreferences;
    String sp_date;
    int sp_count;
    GPSTracker gps;
    public static double latitude = 0.0, longitude = 0.0;
    public static String LocationName = "";
    String result = "";
    Geocoder geocoder;
    List<Address> yourAddresses;
    Button btnCancel;
    TextView btnSE;
    TextView txtActName, txtclientlocation, txtMyLocation, txtVerifyLocation,
            txtactstarttime;
    View l3, l4;
    LinearLayout llv, lc;
    static SimpleDateFormat dfDate;
    String YourCityName = "";
    String resp;
    static String client_latitude = "";
    static String client_longitude = "";
    static String client_shiptomasterid, client_customerid, client_consigneename,
            client_address, client_city, client_phone, client_mobile;

    String currentTime;
    public static String client_GeoLocation = "";
    ImageView imggps,img_client_gps;
    TextView MakemyLocation, mtextClientGeoloc;
    LinearLayout mlinClientGeoLoc;
    Toolbar toolbar;
    ProgressBar mProgrss;
    Button btnMarkAttaindence;
    TextView tvAttaindenceCount;
    private static int FlagGeoLoc;

    static String IsDelayedActivityAllowed = "";

    Button btn_ENO_startSurvey;

    // Location
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    // flag fornework status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    ProgressDialog pd;
    TextView startTxtMessage;

    String Remark = "";
    private AlertDialog b;
    Switch swichBtn;
    RelativeLayout logOutTime;
    TextView LogOutTimeTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_start_time_logging_fragment);

        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES, Context.MODE_PRIVATE);
        actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        Starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
        sp_count = AtendanceSheredPreferance.getInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 0);

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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);
        init();
        setListerners();
        Bundle extras = getIntent().getExtras();
        ActivityId = extras.getString("ActivityId");
        ActivityName=extras.getString("ActivityName");
        Flag = extras.getString("Flag");
        /*if (Flag==null) {
            Flag=getIntent().getStringExtra("f");
        }*/
        getData(ActivityId, Flag);

        if (EnvMasterId.contains("eno")) {
            if (compare_date_1(FormatStDt)) {
                SharedPreferences.Editor editor = AtendanceSheredPreferance
                        .edit();
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, ActivityId);
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, currentTime);
                editor.commit();
            } else {
                SharedPreferences.Editor editor = AtendanceSheredPreferance
                        .edit();
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                editor.commit();

            }
        } else {

        }
        Starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);

        if (EnvMasterId.contains("eno")) {
            btnSE.setVisibility(View.GONE);
            btn_ENO_startSurvey.setVisibility(View.VISIBLE);
        } else {
            btnSE.setVisibility(View.VISIBLE);
            btn_ENO_startSurvey.setVisibility(View.GONE);
        }
        if (Flag.equalsIgnoreCase("End")) {
            btnSE.setText("Click here when you stop the working ");
            // startTxtMessage.setText("Click here when you stop the working :");
        } else {
            btnSE.setText("Click here when you start the working ");
        }

        String lastDate = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, null);
        Date c1 = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c1);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c1);
        if (lastDate != null) {
            if (!lastDate.equals(formattedDate)) {
                tvAttaindenceCount.setText("0" + "");
                SharedPreferences.Editor editor = AtendanceSheredPreferance
                        .edit();
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, today);
                editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 0);
                editor.commit();
                AppCommon.getInstance(LoggingTimeActivity.this).setStartBioTime("");
                btnMarkAttaindence.setText("Mark your In time");
                logOutTime.setVisibility(View.GONE);
                AppCommon.getInstance(this).setLogOutTime(null);
                swichBtn.setChecked(false);
            } else {
                tvAttaindenceCount.setText(sp_count + "");
                if (sp_count != 0) {
                    btnMarkAttaindence.setText("Mark your Out time  \n In time : " + AppCommon.getInstance(LoggingTimeActivity.this).getBioLocation());
                }
                if(AppCommon.getInstance(this).getLogOutTime() != null){
                    logOutTime.setVisibility(View.VISIBLE);
                    LogOutTimeTxt.setText("Now you logout time is: "+AppCommon.getInstance(this).getLogOutTime());
                    swichBtn.setChecked(true);
                }else {
                    logOutTime.setVisibility(View.GONE);
                    swichBtn.setChecked(false);
                }
            }
        } else {
            tvAttaindenceCount.setText("0" + "");
            SharedPreferences.Editor editor = AtendanceSheredPreferance
                    .edit();
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, today);
            editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 0);
            editor.commit();
            AppCommon.getInstance(LoggingTimeActivity.this).setStartBioTime("");
            btnMarkAttaindence.setText("Mark your In time");
        }


        String Starttime = AtendanceSheredPreferance.getString(
                WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
        if (Starttime != null) {
            txtactstarttime.setText("You started working on this activity at : " + Starttime);
            txtactstarttime.setVisibility(View.VISIBLE);
        } else {
            txtactstarttime.setVisibility(View.GONE);
        }


        txtActName.setText(ActivityName);
        getCurrentLocationNew();
        imggps.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showProgress();
                getCurrentLocationNew();
                if (!(client_latitude.equalsIgnoreCase("") && client_longitude.equalsIgnoreCase(""))) {
                    getDistance(latitude, longitude,
                            Double.parseDouble(client_latitude),
                            Double.parseDouble(client_longitude));
                } else {

                }
                dismissProgress();
            }
        });

        llv.setVisibility(View.GONE);
        lc.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        mlinClientGeoLoc.setVisibility(View.GONE);

        //setListerners();

        if (isnet()) {
            showProgress();
            new StartSession(LoggingTimeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    FlagGeoLoc = 1;
                    new GetClientDetails().execute(ActivityId);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });

        } else {
            ut.displayToast(getApplicationContext(), "No Internet Connection");
        }
        MakemyLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FlagGeoLoc = 0;
                if (isnet()) {
                    showProgress();
                    new StartSession(LoggingTimeActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            if (!(result.equalsIgnoreCase("") && result.equalsIgnoreCase("Location not Found"))) {
                                JSONObject object = getobj();
                                new PostClientDetails().execute(object.toString().replaceAll("\\\\", ""));
                            } else {
                                Toast.makeText(getApplicationContext(), "Refresh Your Location", Toast.LENGTH_LONG);
                            }


                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });

                } else {
                    ut.displayToast(getApplicationContext(), "No Internet Connection");
                }

            }
        });
    }

    private JSONObject getobj() {
        JSONObject object = new JSONObject();
        try {

            object.put("ShipToMasterId", URLEncoder.encode(client_shiptomasterid, "UTF-8"));
            object.put("Latitude", URLEncoder.encode(latitude + "", "UTF-8"));
            object.put("Longitude", URLEncoder.encode(longitude + "", "UTF-8"));
            object.put("City", URLEncoder.encode(YourCityName, "UTF-8"));
            object.put("Address", URLEncoder.encode(result, "UTF-8"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return object;
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Context context = this;
        locationManager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude(),
                        lon = mCurrentLocation.getLongitude();
                canGetLocation = true;
                latitude = lat;
                longitude = lon;
                Log.e("token ", "Lat :" + lat + " Long" + lon);
                GetCurrentLocation(lat, lon);

            } else {

                canGetLocation = false;
                   /* if (!isGPSEnabled) {
                        noLocation("GPS Location");
                    } else {
                        noLocation("GPS Location and Network Connectivity");
                    }*/

                if (!isGPSEnabled && !isNetworkEnabled) {
                    //  noLocation("GPS Location and Network Connectivity");
                } else {
                    if (!isGPSEnabled) {
                        //  noLocation("GPS Location");
                    } else if (!isNetworkEnabled) {
                        // noLocation("Network Connectivity");
                    } else {
                        //  noLocation("GPS Location or Network Connectivity");
                    }
                }


            }
        } catch (SecurityException e) {
            e.printStackTrace();
            //noLocation("Location Permission");
            canGetLocation = false;

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class GetClientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
           /* url = CompanyURL
                    + "/vwb/webservice/Activitywebservice.asmx/getClientDetails?ActivityId="
                    + ActivityId;
                          resp = httpGet(url);
            if (resp.contains("</string>")) {
                resp = resp.substring(resp.indexOf(">") + 1);
                resp = resp.substring(resp.indexOf(">") + 1);
                resp = resp.substring(0, resp.indexOf("<"));
            } else {
                resp = "No";
            }*/

            String url;
            Object res;
            String response;
            try {
                url = CompanyURL
                        + "/api/BiometricAPI/getClientDetails?ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8");

                // resp = httpGet(url);
                resp = ut.OpenConnection(url, getApplicationContext());
                response = resp.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        protected void onPostExecute(String result) {
            dismissProgress();
            if (result.equalsIgnoreCase("Error")) {
                ut.displayToast(LoggingTimeActivity.this, "Server down...Please try  after some time.");
            } else if (result.equalsIgnoreCase("No")) {
                llv.setVisibility(View.GONE);
                lc.setVisibility(View.GONE);
                l4.setVisibility(View.GONE);
                mlinClientGeoLoc.setVisibility(View.GONE);

                ut.displayToast(LoggingTimeActivity.this, "No unit id found");
            } else if (result.equalsIgnoreCase("No unit id found")) {
                ut.displayToast(LoggingTimeActivity.this, "No unit id found");
            } else if (result.equalsIgnoreCase("Client is not attached to this activity")) {
                txtVerifyLocation.setVisibility(View.GONE);
                llv.setVisibility(View.VISIBLE);
                lc.setVisibility(View.GONE);
                l4.setVisibility(View.VISIBLE);
               /* if (EnvMasterId.equalsIgnoreCase(EnvMasterId)) {
                    btnSE.setVisibility(View.GONE);

                } else {
                    btnSE.setVisibility(View.VISIBLE);

                }*/
               /* if (Flag.equalsIgnoreCase("End")) {
                    txtVerifyLocation.setText("Click END button to finish your activity");

                } else {
                    txtVerifyLocation.setText("Click START button to start your activity");

                }*/


                ut.displayToast(LoggingTimeActivity.this, "Client is not attached to this activity");
            } else if (result.contains("Shiptomasterid")) {
                try {
                    JSONObject jResults = new JSONObject(result);
                    // {"Shiptomasterid":"6827","Customerid":"5700","Consigneename":"Ekta Trading Co","Address":"Shop 707 Kanda batata market market yard Gultekdi pune","City":"Pune","Phone":"","Mobile":"9881944086","Latitude":"","Longitude":""}
                    client_shiptomasterid = jResults.getString("Shiptomasterid");
                    client_customerid = jResults.getString("Customerid");
                    client_consigneename = jResults.getString("Consigneename");
                    String data = jResults.getString("Address");
                    String a = "";
                    try {
                        a = URLDecoder.decode(data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        a = data;
                    }
                    client_address = a;
                    client_city = jResults.getString("City");
                    client_phone = jResults.getString("Phone");
                    client_mobile = jResults.getString("Mobile");
                    client_latitude = jResults.getString("Latitude");
                    client_longitude = jResults.getString("Longitude");
                    client_GeoLocation = jResults.getString("GeoLocation");
                    if (!(client_latitude.equalsIgnoreCase("") || client_longitude.equalsIgnoreCase(""))) {
                        if (!(client_phone.equalsIgnoreCase(""))) {
                            txtclientlocation.setText(client_consigneename + "\n"
                                    + client_address + " ," + client_city + ", "
                                    + client_phone);
                        } else {
                            txtclientlocation.setText(client_consigneename + "\n"
                                    + client_address + " ,\n" + client_city + ".");
                        }
                        if (FlagGeoLoc == 1) {
                            mlinClientGeoLoc.setVisibility(View.VISIBLE);
                            mtextClientGeoloc.setText(client_GeoLocation);
                        } else {
                            mlinClientGeoLoc.setVisibility(View.GONE);
                            mtextClientGeoloc.setText(client_GeoLocation);
                        }

                        llv.setVisibility(View.VISIBLE);
                        lc.setVisibility(View.VISIBLE);
                        l4.setVisibility(View.VISIBLE);


                        getDistance(latitude, longitude,
                                Double.parseDouble(client_latitude),
                                Double.parseDouble(client_longitude));
                    } else {
                        ut.displayToast(LoggingTimeActivity.this, "Latitude and Longitude are not Present for the Client ");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

               /* // resp="1 $ 1 $ VRITTI SOLUTIONS LTD. PUNE $ 4, Continental Park, Opp Karve Statue,Karve Road, Kothrud $ Pune$020-25393367 $ 9561068582 $ 18.5073985 $ 73.80765040000006";
                // resp = resp.replaceAll(" ", "");
                String data[] = result.split("\\$");
                client_shiptomasterid = data[0];
                client_customerid = data[1];
                client_consigneename = data[2];
                client_address = data[3];
                client_city = data[4];
                client_phone = data[5];
                client_mobile = data[6];
                client_latitude = data[7];
                client_longitude = data[8];
                txtclientlocation.setText(client_consigneename + "\n"
                        + client_address + " ,\n" + client_city + ", "
                        + client_phone);*/
            }

        }
    }

    public class PostClientDetails extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String url;
            Object res;
            String response;
            try {
                url = CompanyURL
                        + "/api/BiometricAPI/POSTUpdateShipToLongLat";
                res = ut.OpenPostConnection(url, arg0[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        protected void onPostExecute(String result) {
            dismissProgress();
            if (result.contains("true")) {
                new GetClientDetails().execute(ActivityId);

            } else if (result.contains("false")) {

            } else if (result.contains("Error")) {

            } else {
            }
        }
    }

    public static String httpGet(String urlString) throws IOException {
        URL url = new URL(urlString.replaceAll(" ", "%20"));

        //	URL url = new URL("http://vritti.vworkbench.com/webservice/ActivityWebservice.asmx/GetreportingGps?MobileNo=9890156056");
        Log.d("test", "url" + url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        Log.d("test", "conn" + conn);
        //conn.connect();
        int resCode = conn.getResponseCode();
        // Check for successful response code or throw error
        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
            //return "0";
        }

        // Buffer the result into a string
        BufferedReader buffrd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buffrd.readLine()) != null) {
            sb.append(line);
        }

        buffrd.close();

        conn.disconnect();
        return sb.toString();
    }


    private void setListerners() {
        btnMarkAttaindence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAttendancedialog();

            }
        });

        btnSE.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /*String Starttime = AtendanceSheredPreferance
                        .getString("Starttime", null);*/
                if (Flag.equalsIgnoreCase("Start")) {
                    if (compare_date_1(FormatStDt)) {
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, ActivityId);
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, currentTime);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                        editor.commit();

                    }
                    Intent serviceIntent = new Intent(LoggingTimeActivity.this, ForegroundService.class);
                    serviceIntent.putExtra("inputExtra", ActivityName);
                    serviceIntent.putExtra("id", ActivityId);
                    serviceIntent.putExtra("f", Flag);
                    serviceIntent.putExtra("time",currentTime);
                    serviceIntent.putExtra("module","VWB");
                    serviceIntent.putExtra("Opportunity","Activity");

                    ContextCompat.startForegroundService(LoggingTimeActivity.this, serviceIntent);
                    onBackPressed();
                    /*Intent i = new Intent(LoggingTimeActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                    startActivity(i);
                    finish();*/
                } else {
                    Date now = new Date(); // java.util.Date, NOT java.sql.Date
                    // or
                    // java.sql.Timestamp!

                    String format2 = new SimpleDateFormat("dd-MMM-yy ")
                            .format(now);
                    //  AppCommon.getInstance(LoggingTimeActivity.this).setStartBioTime("");

                    if (IsDelayedActivityAllowed.equalsIgnoreCase("")) {
                        String PromiseDate = "1";
                        String finaloutcome = "1";
                        String ReasonTransfer = "1";
                        String UserMasterId1 = "1";
                        Intent theintent = new Intent(LoggingTimeActivity.this,
                                SendTimeSheet.class);
                        String isComp = String.valueOf("true");
                        theintent.putExtra("mob", MobileNo);
                        theintent.putExtra("url", CompanyURL);
                        theintent.putExtra("curDate", format2);
                        theintent.putExtra("fromTime", Starttime);
                        theintent.putExtra("totime", currentTime);
                        theintent.putExtra("desc", ActivityName);
                        theintent.putExtra("actid", ActivityId);
                        theintent.putExtra("pid", ProjectId);
                        theintent.putExtra("isc", isComp);
                        theintent.putExtra("PromiseDate", PromiseDate);
                        theintent.putExtra("finaloutcome", finaloutcome);
                        theintent.putExtra("ReasonTransfer", ReasonTransfer);
                        theintent.putExtra("transfertoid", UserMasterId1);
                        startService(theintent);
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                        editor.commit();
                        /*Intent i = new Intent(LoggingTimeActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                        startActivity(i);
                        finish();*/
                        onBackPressed();

                    } else {
                        if (compare_date(FormatEndDt)) {

                            String PromiseDate = "1";
                            String finaloutcome = "1";
                            String ReasonTransfer = "1";
                            String UserMasterId1 = "1";
                            Intent theintent = new Intent(
                                    LoggingTimeActivity.this,
                                    SendTimeSheet.class);
                            String isComp = String.valueOf("true");
                            theintent.putExtra("mob", MobileNo);
                            theintent.putExtra("url", CompanyURL);
                            theintent.putExtra("curDate", format2);
                            theintent.putExtra("fromTime", Starttime);
                            theintent.putExtra("totime", currentTime);
                            theintent.putExtra("desc", ActivityName);
                            theintent.putExtra("actid", ActivityId);
                            theintent.putExtra("pid", ProjectId);
                            theintent.putExtra("isc", isComp);
                            theintent.putExtra("PromiseDate", PromiseDate);
                            theintent.putExtra("finaloutcome", finaloutcome);
                            theintent
                                    .putExtra("ReasonTransfer", ReasonTransfer);
                            theintent.putExtra("transfertoid", UserMasterId1);
                            startService(theintent);

                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                            editor.commit();

                            /*Intent i = new Intent(LoggingTimeActivity.this, ActivityMain.class);
                            startActivity(i);
                            finish();*/
                            onBackPressed();
                        } else {

                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                            editor.commit();
                            showCustomMessageDialog(
                                    "You can not fill this activity as activity is overdue.",
                                    "Alert!!", LoggingTimeActivity.this);
                        }
                    }
                    Intent serviceIntent = new Intent(LoggingTimeActivity.this, ForegroundService.class);
                    stopService(serviceIntent);
                }

            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LoggingTimeActivity.this.finish();
            }
        });

        btn_ENO_startSurvey.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoggingTimeActivity.this, EnoSamplingHomePage.class).putExtra("activityId", ActivityId));

               /* Intent i = new Intent(LoggingTimeActivity.this, EnoSamplingScreen1.class);
                startActivity(i);*/


               /* if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {


                           // new DownloadAuthenticate().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(LoggingTimeActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }*/


            }
        });
    }

    public static void showCustomMessageDialog(String message, String heading,
                                               final Context parent) {

        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.vwb_dialog_message_dialog);

        myDialog.setCancelable(true);

        TextView textview_dialog_message_text = (TextView) myDialog
                .findViewById(R.id.textview_message_dialog_message);
        if (heading != null) {
            myDialog.setTitle(heading);
        }
        textview_dialog_message_text.setText(message);

        Button btn = (Button) myDialog
                .findViewById(R.id.gotobtndialoginfosmall);
        btn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                ((Activity) parent).finish();
            }
        });

        myDialog.show();
    }

    class BiometricGpsAttendance extends AsyncTask<String, Void, String> {
        Object res = null;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
            //  showProgresHud();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url;
                //http://vritti.ekatm.com/vwb/webservice/Activitywebservice.asmx/BiometricGpsAttendance?mobileno=9892305584&count=1
                String xml = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String Time = sdf.format(new Date());
//                url = CompanyURL
//                        + "/api/BiometricAPI/BiometricGpsAttendance?UserMasterId="
//                        + UserMasterId + "&time=''&Count=" + params[0] + "&Latitude=" + latitude + "&Longitude=" + longitude + "&LocationName=" + LocationName + "&IsVerified=" + str_attaindenceVerification;

                url = CompanyURL
                        + "/api/BiometricAPI/BiometricGpsAttendance?UserMasterId="
                        + UserMasterId + "&time=''&Count=" + params[0] + "&Latitude=" + latitude + "&Longitude=" + longitude + "&LocationName=" + LocationName + "&IsVerified=" + str_attaindenceVerification + "&Remark=" + URLEncoder.encode(Remark, "UTF-8");

              /*  http://a207.ekatm.com//api/BiometricAPI/BiometricGpsAttendance?
                // UserMasterId=1&time=&Count=1&Latitude=11.000&Longitude=12.678&LocationName=kothrud&IsVerified=Y*/
                url = url.replaceAll(" ", "%20");
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }

            return response;
        }

        protected void onPostExecute(String result) {
            pd.dismiss();
            //dismissProgressDialog();

            if (result.contains("Success")) {
                ut.displayToast(getApplicationContext(), "Data Send to server");
                sp_date = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, null);
                sp_count = AtendanceSheredPreferance.getInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 0);
                b.dismiss();
                if (sp_date != null) {
                    if (sp_date.equalsIgnoreCase(today)) {
                        if (sp_count > 0) {
                            int i = sp_count + 1;
                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, i);
                            editor.commit();
                        } else {
                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 1);
                            editor.commit();
                        }
                    } else {
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, today);
                        editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 1);
                        editor.commit();
                    }
                } else {
                    SharedPreferences.Editor editor = AtendanceSheredPreferance
                            .edit();
                    editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, today);
                    editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 1);
                    editor.commit();
                }

                sp_date = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, null);
                sp_count = AtendanceSheredPreferance.getInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 0);


                //String lastDate = AppCommon.getInstance(LoggingTimeActivity.this).getCurrentDate();
                String lastDate = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, null);
                Date c1 = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c1);
                SimpleDateFormat df1 = new SimpleDateFormat("HH");
                String currentHr = df1.format(c1);
                SimpleDateFormat df2 = new SimpleDateFormat("mm");
                String currentMin = df2.format(c1);
                String abc = currentHr + ":" + currentMin;
                if (sp_count == 1)
                    AppCommon.getInstance(LoggingTimeActivity.this).setStartBioTime(abc);
                if (btnMarkAttaindence.getText().toString().trim().contains("in")) {
                    btnMarkAttaindence.setText("Mark your Out time  \n In time : " + currentHr + ":" + currentMin);
                } else {
                    if (sp_count == 0)
                        btnMarkAttaindence.setText("Mark your In time");
                    else
                        btnMarkAttaindence.setText("Mark your Out time  \n In time : " + AppCommon.getInstance(LoggingTimeActivity.this).getBioLocation());
                }
                int total_min = Integer.parseInt(currentHr) * 60;
                total_min = Integer.parseInt(currentMin) + total_min;
                float timeValue = (float) total_min / 60;
                AppCommon.getInstance(LoggingTimeActivity.this).setWorkingHour(String.format("%.02f", timeValue));
                AppCommon.getInstance(context).setStartLocation(true);
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                String formattedDate = df.format(c1);
                if (!lastDate.equals(formattedDate)) {
                    tvAttaindenceCount.setText("0" + "");
                    SharedPreferences.Editor editor = AtendanceSheredPreferance
                            .edit();
                    editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_DATE_KEY, today);
                    editor.putInt(WebUrlClass.ATTENDANCE_PREFERENCES_COUNT_KEY, 1);
                    editor.commit();
                } else
                    tvAttaindenceCount.setText(sp_count + "");
            } else {

                ut.displayToast(getApplicationContext(), "Failed to Send Data");
            }

        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);//toolbar_progress_logging
        mProgrss = (ProgressBar) findViewById(R.id.toolbar_progress_logging);
        sql = db.getWritableDatabase();
        btnSE = findViewById(R.id.btnSE);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtActName = (TextView) findViewById(R.id.txtActName);
        txtclientlocation = (TextView) findViewById(R.id.txtclientlocation);
        txtMyLocation = (TextView) findViewById(R.id.txtMyLocation);
        txtVerifyLocation = (TextView) findViewById(R.id.txtVerifyLocation);
        txtactstarttime = (TextView) findViewById(R.id.txtactstarttime);
        MakemyLocation = (TextView) findViewById(R.id.makemylocation);
        mtextClientGeoloc = (TextView) findViewById(R.id.txtclientlocationgeo);
        mlinClientGeoLoc = (LinearLayout) findViewById(R.id.cl_geo);
        btnMarkAttaindence = (Button) findViewById(R.id.btnMarkAttaindence);
        tvAttaindenceCount = (TextView) findViewById(R.id.tv_attaindencecount);
        btn_ENO_startSurvey = (Button) findViewById(R.id.btn_startEnoActivity);
        startTxtMessage = findViewById(R.id.startTxtMessage);
        l3 = (View) findViewById(R.id.l3);
        l4 = (View) findViewById(R.id.l4);
        llv = (LinearLayout) findViewById(R.id.llv);
        lc = (LinearLayout) findViewById(R.id.lc);
        dfDate = new SimpleDateFormat("dd MMM yyyy");// 25 Oct 2016
        today = dfDate.format(new Date());// 17 Apr 2014
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar c = Calendar.getInstance();
        System.out.println(dateFormat.format(c.getTime()));
        currentTime = dateFormat.format(c.getTime());
        imggps = (ImageView) findViewById(R.id.imggps);
        swichBtn = findViewById(R.id.swichBtn);
        logOutTime = findViewById(R.id.logOutTime);
        LogOutTimeTxt = findViewById(R.id.LogOutTimeTxt);
        img_client_gps = findViewById(R.id.img_client_gps);

        img_client_gps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriMap = "http://maps.google.com/maps?q=loc:" + client_latitude + "," + client_longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap ));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        });

    }

    private void getCurrentLocationNew() {

        if (isGooglePlayServicesAvailable()) {
            getLocationPlayservice();
        }


    }

    private void getLocationPlayservice() {
        Context context = this;
        locationManager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (mGoogleApiClient == null) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, (GoogleApiClient.OnConnectionFailedListener) this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
            Boolean data = mGoogleApiClient.isConnected();
            if (data) {
                if (mGoogleApiClient.isConnected()) {
                    try {

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                        if (mCurrentLocation != null) {
                            double lat = mCurrentLocation.getLatitude(),
                                    lon = mCurrentLocation.getLongitude();
                            latitude = lat;
                            longitude = lon;
                            canGetLocation = true;

                            Log.e("token ", "Lat :" + lat + " Long" + lon);
                            GetCurrentLocation(lat, lon);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Boolean data = mGoogleApiClient.isConnected();
            if (mGoogleApiClient.isConnected()) {
                try {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    if (mCurrentLocation != null) {
                        double lat = mCurrentLocation.getLatitude(),
                                lon = mCurrentLocation.getLongitude();
                        latitude = lat;
                        longitude = lon;
                        canGetLocation = true;

                        Log.e("token ", "Lat :" + lat + " Long" + lon);
                        GetCurrentLocation(lat, lon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void swichClick(View v) {
        if (swichBtn.isChecked()) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            Date date = null;
            try {
                date = sdf.parse("20:00");
            } catch (ParseException e) {
            }
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    if (hourOfDay >= 20) {
                        logOutTime.setVisibility(View.VISIBLE);
                        String minVal = "00";
                        if(minute < 10){
                            minVal = "0"+minute;
                        }else {
                            minVal = String.valueOf(minute);
                        }
                        //LogOutTimeTxt.setText("Now you logout time is: " + (12 - hourOfDay) + ":" + minVal +" pm");
                        LogOutTimeTxt.setText("Now your logout time is : " + hourOfDay + ":" + minVal );
                        AppCommon.getInstance(LoggingTimeActivity.this).setLogOutTime(String.valueOf(hourOfDay) + ":" + String.valueOf(hourOfDay));
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggingTimeActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle(LoggingTimeActivity.this.getResources().getString(R.string.app_name));
                        builder.setMessage("Please select time between  8:00 pm to 11:59 pm");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                swichBtn.setChecked(false);
                            }
                        });
                        builder.show();


                    }
                }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }else {
            logOutTime.setVisibility(View.GONE);
            AppCommon.getInstance(this).setLogOutTime(null);
        }
    }


    private void GetCurrentLocation(double lat, double lon) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());

            List<Address> addressList = geocoder.getFromLocation(lat,
                    lon, 1);
            //  System.out.println(latitude + " lat " + longitude + " long");
            if (addressList != null && addressList.size() > 0) {
                Address address1 = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address1.getMaxAddressLineIndex(); i++) {
                    if (i == 0) {
                        sb.append(address1.getAddressLine(0));
                    } else {
                        sb.append("," + address1.getAddressLine(i));
                    }

                }
                  /*  String data = address.getAdminArea();
                    YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
                    imggps.setVisibility(View.VISIBLE);*/

                String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postalCode = addressList.get(0).getPostalCode();
                // String knownName = addressList.get(0).getFeatureName();
                LocationName = address + " , " + city + " , " + state + "," + country + " . " + postalCode;
                //LocationName = result;
                txtMyLocation.setText(LocationName);
                // btnSE.setVisibility(View.GONE);
                imggps.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            result = "Location not Found";
            Log.e("test", "Unable connect to Geocoder", e);
            if (isnet()) {

                try {
                    final String[] loc = new String[1];
                    final Thread t = new Thread() {

                        public void run() {
                            try {
                                String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + latitude + "," + longitude + "&sensor=true";
                                Object res = ut.OpenConnection(url, getApplicationContext());
                                if (res == null) {

                                } else {
                                    String response = res.toString();
                                    JSONObject jsonObj = null;
                                    jsonObj = new JSONObject(response);
                                    String Status1 = jsonObj.getString("status");
                                    if (Status1.equalsIgnoreCase("OK")) {
                                        JSONArray Results = jsonObj.getJSONArray("results");
                                        int cnt = Results.length();
                                        if (cnt > 1) {
                                            JSONObject zero2 = Results.getJSONObject(1);
                                            result = zero2.getString("formatted_address");

                                        } else {
                                            JSONObject zero2 = Results.getJSONObject(0);
                                            result = zero2.getString("formatted_address");

                                        }

                                    } else {

                                    }
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();

                            }
                        }

                    };
                    t.start();

                    t.join();


                } catch (Exception e1) {
                    e1.printStackTrace();

                }


            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
            LocationName = result;
            txtMyLocation.setText(result);
            //   btnSE.setVisibility(View.GONE);
            imggps.setVisibility(View.VISIBLE);

        }
        // LocationName = result;
        // txtMyLocation.setText(result);
        //    btnSE.setVisibility(View.GONE);
        imggps.setVisibility(View.VISIBLE);

    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    private void getDistance(Double mylat, Double mylang, Double cllat,
                             Double cllang) {
        Location selected_location = new Location("locationA");
        selected_location.setLatitude(mylat);
        selected_location.setLongitude(mylang);

        Location near_locations = new Location("locationA");
        near_locations.setLatitude(cllat);
        near_locations.setLongitude(cllang);

        double distance = selected_location.distanceTo(near_locations);
        // txtVerifyLocation.setText(String.format("%.2f", distance));
        if (distance > 500.0) {
            str_attaindenceVerification = WebUrlClass.FlagIsVerifyAttaindence_no;

            MakemyLocation.setVisibility(View.VISIBLE);
            if (distance < 1000.0) {
                txtVerifyLocation.setText("You are " + distance + " m away from client location");
                // btnSE.setVisibility(View.GONE);
            } else {
                distance = distance / 1000.0;
                int integer = (int) distance;
                double decimal = (10 * distance - 10 * integer) / 10;
                txtVerifyLocation.setText("You are " + integer + " km away from client location");
                // btnSE.setVisibility(View.GONE);
            }

        } else {
            str_attaindenceVerification = WebUrlClass.FlagIsVerifyAttaindence_yes;
            txtVerifyLocation.setText("You are at client location : " + String.valueOf(distance) + "m");
            txtVerifyLocation.setVisibility(View.VISIBLE);
            //  btnSE.setVisibility(View.VISIBLE);
           /* if (EnvMasterId.equalsIgnoreCase(EnvMasterId)) {
                btnSE.setVisibility(View.GONE);

            } else {
                btnSE.setVisibility(View.VISIBLE);

            }*/ // this code commited by azhar by with permission nilesh sir
            MakemyLocation.setVisibility(View.GONE);

        }

    }

    private void getData(String ActivityId, String Flag) {


        Cursor c = sql
                .rawQuery(
                        "SELECT distinct ProjectId,ActivityName,FormatStDt,FormatEndDt,StartDate,EndDate"
                                + " FROM " + db.TABLE_ACTIVITYMASTER_PAGING
                                + " where ActivityId='" + ActivityId + "'",
                        null);
        if (c.getCount() == 0) {


        } else if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ActivityName = c.getString(c.getColumnIndex("ActivityName"));
                ProjectId = c.getString(c.getColumnIndex("ProjectId"));
                /*
                 * FormatStDt = c.getString(c.getColumnIndex("FormatStDt"));
                 * FormatEndDt = c.getString(c.getColumnIndex("FormatEndDt"));
                 */
               /* IsDelayedActivityAllowed = c.getString(c
                        .getColumnIndex("IsDelayedActivityAllowed"));*/
                FormatStDt = c.getString(c.getColumnIndex("StartDate"));
                FormatEndDt = c.getString(c.getColumnIndex("EndDate"));

            } while (c.moveToNext());

        }
    }

    public static boolean compare_date(String todate) {
        boolean b = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        todate = todate.substring(todate.indexOf("(") + 1, todate.lastIndexOf(")"));
        long timestamp = Long.parseLong(todate);
        Date date = new Date(timestamp);
        String Todaydate = sdf.format(date);


        try {
            if ((dfDate.parse(today).before(dfDate.parse(Todaydate)) || dfDate
                    .parse(today).equals(dfDate.parse(Todaydate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static boolean compare_date_1(String Fromdate) {
        boolean b = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        Fromdate = Fromdate.substring(Fromdate.indexOf("(") + 1, Fromdate.lastIndexOf(")"));
        long timestamp = Long.parseLong(Fromdate);
         Date date = new Date(timestamp);
         String fromdate = sdf.format(date);

        try {
            if ((dfDate.parse(today).after(dfDate.parse(fromdate)) || dfDate
                    .parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private void showProgress() {
        mProgrss.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mProgrss.setVisibility(View.GONE);
    }

    class DownloadAuthenticate extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String AppName = "";
            AppName = SetAppName.AppNameFCM;
            String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_GetOTPServer + "?MobNo=" + MobileNo + "&UserLoginId=" + LoginId + "&AppName=" + AppName;
            //UserLoginId=300207&AppName
            try {
                res = ut.OpenConnection(url, getApplicationContext());
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
            if (res.contains("#Success")) {
                String data[] = res.split("#");
                final String OPT = data[0];

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoggingTimeActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.vwb_otp_lay, null);
                dialogBuilder.setView(dialogView);

                // set the custom dialog components - text, image and button
                final EditText textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
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
                                Intent i = new Intent(LoggingTimeActivity.this, EnoSamplingScreen1.class);
                                startActivity(i);
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

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {

                                    new DownloadAuthenticate().execute();

                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        } else {
                            Toast.makeText(LoggingTimeActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }


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

    public void CreateAttendancedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoggingTimeActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.vwb_dialog_attendance, null);
        dialogBuilder.setView(dialogView);
        /*final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_dialog_reschedule);
        dialog.setTitle("Select New End Date");*/
        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        final EditText edt_remark = (EditText) dialogView.findViewById(R.id.edt_remark);


        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Remark = edt_remark.getText().toString();

                new StartSession(LoggingTimeActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        pd = new ProgressDialog(LoggingTimeActivity.this);
                        pd.setMessage("Please wait...");
                        pd.show();
                        new BiometricGpsAttendance().execute(String
                                .valueOf(sp_count));
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });

            }
        });


        b = dialogBuilder.create();
        b.show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(LoggingTimeActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();*/
    }
}
