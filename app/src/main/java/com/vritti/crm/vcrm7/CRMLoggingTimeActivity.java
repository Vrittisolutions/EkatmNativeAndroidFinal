package com.vritti.crm.vcrm7;

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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vritti.chat.activity.UserListDisplayActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.ekatm.services.SendTimeSheet;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.EnoSamplingHomePage;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.EnoSamplingScreen1;

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

import static com.vritti.ekatm.services.DownloadJobService.FASTEST_INTERVAL;
import static com.vritti.ekatm.services.DownloadJobService.MIN_DISTANCE_CHANGE_FOR_UPDATES;
import static com.vritti.ekatm.services.DownloadJobService.MIN_TIME_BW_UPDATES;

public class CRMLoggingTimeActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    public static String str_attaindenceVerification = "0";
    public static String ActivityId, ProjectId;
    String Flag;
    String IsCrmUser;

    static String today, FormatStDt, FormatEndDt, actid, Starttime = "", ActivityName;
    SQLiteDatabase sql;
    String link;
    public SharedPreferences AtendanceSheredPreferance, userpreferences;
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
    ImageView imggps;
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
        pd = new ProgressDialog(CRMLoggingTimeActivity.this);

        Starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);


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
                AppCommon.getInstance(CRMLoggingTimeActivity.this).setStartBioTime("");
                btnMarkAttaindence.setText("Mark your In time");
                logOutTime.setVisibility(View.GONE);
                AppCommon.getInstance(this).setLogOutTime(null);
                swichBtn.setChecked(false);
            } else {
                tvAttaindenceCount.setText(sp_count + "");
                if (sp_count != 0) {
                    btnMarkAttaindence.setText("Mark your Out time  \n In time : " + AppCommon.getInstance(CRMLoggingTimeActivity.this).getBioLocation());
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
            AppCommon.getInstance(CRMLoggingTimeActivity.this).setStartBioTime("");
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
                dismissProgress();
            }
        });

        llv.setVisibility(View.GONE);
        lc.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        mlinClientGeoLoc.setVisibility(View.GONE);

        //setListerners();



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


    private void setListerners() {
        btnMarkAttaindence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAttendancedialog();

            }
        });


        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CRMLoggingTimeActivity.this.finish();
            }
        });


    }



    class BiometricGpsAttendance extends AsyncTask<String, Void, String> {
        Object res = null;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setCancelable(false);
            if (!isFinishing()) {
                pd.show();
            }
            pd.setContentView(R.layout.vwb_progress_lay);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                    AppCommon.getInstance(CRMLoggingTimeActivity.this).setStartBioTime(abc);
                if (btnMarkAttaindence.getText().toString().trim().contains("in")) {
                    btnMarkAttaindence.setText("Mark your Out time  \n In time : " + currentHr + ":" + currentMin);
                } else {
                    if (sp_count == 0)
                        btnMarkAttaindence.setText("Mark your In time");
                    else
                        btnMarkAttaindence.setText("Mark your Out time  \n In time : " + AppCommon.getInstance(CRMLoggingTimeActivity.this).getBioLocation());
                }
                int total_min = Integer.parseInt(currentHr) * 60;
                total_min = Integer.parseInt(currentMin) + total_min;
                float timeValue = (float) total_min / 60;
                AppCommon.getInstance(CRMLoggingTimeActivity.this).setWorkingHour(String.format("%.02f", timeValue));
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
        txtActName.setVisibility(View.GONE);
        btnSE.setVisibility(View.GONE);

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
                        AppCommon.getInstance(CRMLoggingTimeActivity.this).setLogOutTime(String.valueOf(hourOfDay) + ":" + String.valueOf(hourOfDay));
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CRMLoggingTimeActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle(CRMLoggingTimeActivity.this.getResources().getString(R.string.app_name));
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




    public static boolean compare_date(String todate) {
        boolean b = false;

        try {
            if ((dfDate.parse(today).before(dfDate.parse(todate)) || dfDate
                    .parse(today).equals(dfDate.parse(todate)))) {
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

        try {
            if ((dfDate.parse(today).after(dfDate.parse(Fromdate)) || dfDate
                    .parse(today).equals(dfDate.parse(Fromdate)))) {
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



    public void CreateAttendancedialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CRMLoggingTimeActivity.this);
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
                                          if (isnet()) {
                                              pd.setCancelable(false);
                                              if (!isFinishing()) {
                                                  pd.show();
                                              }
                                              pd.setContentView(R.layout.vwb_progress_lay);
                                              pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                              new StartSession(CRMLoggingTimeActivity.this, new CallbackInterface() {
                                                  @Override
                                                  public void callMethod() {

                                                      new BiometricGpsAttendance().execute(String
                                                              .valueOf(sp_count));
                                                  }

                                                  @Override
                                                  public void callfailMethod(String msg) {
                                                      ut.displayToast(getApplicationContext(), msg);
                                                  }
                                              });
                                          }

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