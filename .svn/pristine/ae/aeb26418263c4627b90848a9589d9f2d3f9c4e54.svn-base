package com.vritti.vwblib.vworkbench;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.GPSTracker;
import com.vritti.vwblib.Services.SendTimeSheet;
import com.vritti.vwblib.classes.CommonFunction;

public class LoggingTimeActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    String ActivityId, ActivityName, Flag;
    String  IsCrmUser;
    String result = "";
    static String today, FormatStDt, FormatEndDt, actid, Starttime;
    String Mobileno;
    SQLiteDatabase sql;
    String link;
    String ProjectId;
    SharedPreferences AtendanceSheredPreferance, userpreferences;
    GPSTracker gps;
    public static double latitude, longitude;
    Geocoder geocoder;
    List<Address> yourAddresses;
    Button btnSE, btnCancel;
    TextView txtActName, txtclientlocation, txtMyLocation, txtVerifyLocation,
            txtactstarttime;
    View l3, l4;
    LinearLayout llv, lc;
    static SimpleDateFormat dfDate;
    String YourCityName = "";
    String resp;
    String client_latitude = "";
    String client_longitude = "";
    String client_shiptomasterid, client_customerid, client_consigneename,
            client_address, client_city, client_phone, client_mobile;
    String IsDelayedActivityAllowed = "";
    String currentTime, client_GeoLocation;
    ImageView imggps;
    TextView MakemyLocation, mtextClientGeoloc;
    LinearLayout mlinClientGeoLoc;
    Toolbar toolbar;
    ProgressBar mProgrss;
    private static int FlagGeoLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_start_time_logging_fragment);

        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATENDANCE_PREFERENCES, Context.MODE_PRIVATE);
        actid = AtendanceSheredPreferance.getString("actid", null);
        Starttime = AtendanceSheredPreferance.getString("Starttime", null);

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

        init();
        setListerners();
        Bundle extras = getIntent().getExtras();
        ActivityId = extras.getString("ActivityId");
        Flag = extras.getString("Flag");
        if (Flag.equalsIgnoreCase("End")) {
            btnSE.setText("End");
        } else {
            btnSE.setText("Start");
        }

        String Starttime = ActivityMain.AtendanceSheredPreferance.getString(
                "Starttime", null);
        if (Starttime != null) {
            txtactstarttime.setText("Activity Start Time : " + Starttime);
        } else {
            txtactstarttime.setVisibility(View.GONE);
        }

        getData(ActivityId, Flag);
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
                                    + client_address + " ,\n" + client_city + ", "
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
                        editor.putString("actid", ActivityId);
                        editor.putString("Starttime", currentTime);
                        editor.commit();
                    } else {
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString("actid", null);
                        editor.putString("Starttime", null);
                        editor.commit();

                    }
                    Intent i = new Intent(LoggingTimeActivity.this, ActivityMain.class);
                    startActivity(i);
                    finish();
                } else {
                    Date now = new Date(); // java.util.Date, NOT java.sql.Date
                    // or
                    // java.sql.Timestamp!

                    String format2 = new SimpleDateFormat("dd-MMM-yy ")
                            .format(now);

                    if (IsDelayedActivityAllowed.equalsIgnoreCase("Y")) {
                        String PromiseDate = "1";
                        String finaloutcome = "1";
                        String ReasonTransfer = "1";
                        String UserMasterId = "1";
                        Intent theintent = new Intent(LoggingTimeActivity.this,
                                SendTimeSheet.class);
                        String isComp = String.valueOf("true");
                        theintent.putExtra("mob", Mobileno);
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
                        theintent.putExtra("transfertoid", UserMasterId);
                        startService(theintent);
                        SharedPreferences.Editor editor = AtendanceSheredPreferance
                                .edit();
                        editor.putString("actid", null);
                        editor.putString("Starttime", null);
                        editor.commit();
                        Intent i = new Intent(LoggingTimeActivity.this, ActivityMain.class);
                        startActivity(i);
                        finish();

                    } else {
                        if (compare_date(FormatEndDt)) {

                            String PromiseDate = "1";
                            String finaloutcome = "1";
                            String ReasonTransfer = "1";
                            String UserMasterId = "1";
                            Intent theintent = new Intent(
                                    LoggingTimeActivity.this,
                                    SendTimeSheet.class);
                            String isComp = String.valueOf("true");
                            theintent.putExtra("mob", Mobileno);
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
                            theintent.putExtra("transfertoid", UserMasterId);
                            startService(theintent);

                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putString("actid", null);
                            editor.putString("Starttime", null);
                            editor.commit();

                            Intent i = new Intent(LoggingTimeActivity.this, ActivityMain.class);
                            startActivity(i);
                            finish();
                        } else {

                            SharedPreferences.Editor editor = AtendanceSheredPreferance
                                    .edit();
                            editor.putString("actid", null);
                            editor.putString("Starttime", null);
                            editor.commit();
                            showCustomMessageDialog(
                                    "You can not fill this activity as activity is overdue.",
                                    "Alert!!", LoggingTimeActivity.this);
                        }
                    }
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

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);//toolbar_progress_logging
        mProgrss = (ProgressBar) findViewById(R.id.toolbar_progress_logging);
        sql = db.getWritableDatabase();
        btnSE = (Button) findViewById(R.id.btnSE);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtActName = (TextView) findViewById(R.id.txtActName);
        txtclientlocation = (TextView) findViewById(R.id.txtclientlocation);
        txtMyLocation = (TextView) findViewById(R.id.txtMyLocation);
        txtVerifyLocation = (TextView) findViewById(R.id.txtVerifyLocation);
        txtactstarttime = (TextView) findViewById(R.id.txtactstarttime);
        MakemyLocation = (TextView) findViewById(R.id.makemylocation);
        mtextClientGeoloc = (TextView) findViewById(R.id.txtclientlocationgeo);
        mlinClientGeoLoc = (LinearLayout) findViewById(R.id.cl_geo);
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
    }

  /* private void getCurrentLocation() {


        gps = new GPSTracker(LoggingTimeActivity.this);

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
                    YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
                    imggps.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                result = "Location not Found";
                txtMyLocation.setText(result);
                btnSE.setVisibility(View.GONE);
                imggps.setVisibility(View.VISIBLE);
                Log.e("test", "Unable connect to Geocoder", e);
            }

        } else {

            gps.showSettingsAlert();
        }

    }*/

    private void getCurrentLocationNew() {


        gps = new GPSTracker(LoggingTimeActivity.this);

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
                    YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
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

                    txtMyLocation.setText(result);
                    btnSE.setVisibility(View.GONE);
                    imggps.setVisibility(View.VISIBLE);

                } else {

                }
                txtMyLocation.setText(result);
                btnSE.setVisibility(View.GONE);
                imggps.setVisibility(View.VISIBLE);
            }

        } else {

            gps.showSettingsAlert();
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
            MakemyLocation.setVisibility(View.VISIBLE);
            if (distance < 1000.0) {
                txtVerifyLocation.setText("You are " + distance + " m away from client location");
                btnSE.setVisibility(View.GONE);
            } else {
                distance = distance / 1000.0;
                int integer = (int) distance;
                double decimal = (10 * distance - 10 * integer) / 10;
                txtVerifyLocation.setText("You are " + integer + " km away from client location");
                btnSE.setVisibility(View.GONE);
            }

        } else {
            txtVerifyLocation.setText("You are at client location");
            btnSE.setVisibility(View.VISIBLE);
            MakemyLocation.setVisibility(View.GONE);

        }

    }

    private void getData(String ActivityId, String Flag) {


        Cursor c = sql
                .rawQuery(
                        "SELECT distinct ProjectId,ActivityName,FormatStDt,FormatEndDt,StartDt,EndDt,IsDelayedActivityAllowed "
                                + " FROM " + db.TABLE_ACTIVITYMASTER
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
                IsDelayedActivityAllowed = c.getString(c
                        .getColumnIndex("IsDelayedActivityAllowed"));
                FormatStDt = c.getString(c.getColumnIndex("StartDt"));
                FormatEndDt = c.getString(c.getColumnIndex("EndDt"));

            } while (c.moveToNext());

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
                    .parse(today).equals(dfDate.parse(Fromdate))))

            {
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
}
