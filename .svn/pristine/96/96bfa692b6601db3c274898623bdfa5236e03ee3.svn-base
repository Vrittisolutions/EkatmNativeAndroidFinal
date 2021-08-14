package com.vritti.vwblib.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.classes.CommonFunction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.vritti.vwblib.R;


/**
 * Created by Admin-1 on 8/9/2017.
 */

public class PaidLocationFusedLocationTracker1 extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG1 = PaidLocationFusedLocationTracker1.class.getSimpleName();

    private static final int TWO_SECONDS = 1000 * 2;

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // default value is false but user can change it
    private String locationProvider;                                                                // source of fetched location
    private String mLastLocationUpdateTime;                                                         // fetched location time

    private Location mLastLocationFetched;                                                          // location fetched
    private Location mLocationFetched;                                                              // location fetched
    private Location networkLocation;
    private Location gpsLocation;



    // activity context


    private android.location.LocationListener locationListener;


    private int mProviderType;
    public static final int NETWORK_PROVIDER = 1;
    public static final int ALL_PROVIDERS = 0;
    public static final int GPS_PROVIDER = 2;


    public static final int LOCATION_PROVIDER_ALL_RESTICTION = 1;
    public static final int LOCATION_PROVIDER_RESTRICTION_NONE = 0;
    public static final int LOCATION_PROVIDER_GPS_ONLY_RESTICTION = 2;
    public static final int LOCATION_PROVIDER_NETWORK_ONLY_RESTICTION = 3;
    private int mForceNetworkProviders = 0;

    Context mcontext;
    // flag for gps status
    boolean isGPSEnabled = false;
    // flag fornework status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    String GpsAddedDt;
    ArrayList<String> GPSNotificationID;

    protected LocationManager locationManager;
    double latitude;//final values
    double longitude;//final values
    Location mUpdatedLocation;//final value


    // static DatabaseHandler db;
    private String mobno, LocationName, link, UserMasterID;
    //private String Currenttime = "";
    SharedPreferences userpreferences;
    SimpleDateFormat dfDate, DateNT;
    private static final String TAG = "LocationActivity";
    // The minimum distance to change Updates in meters

    //prad
    private static long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    public static long MIN_TIME_BW_UPDATES = 1000 * 60 * 15;
    private static long FASTEST_INTERVAL = 1000 * 60 * 2;


    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;


    private Handler mHandler = new Handler();
    private Timer mtimer = null;
    String IsGPSLocation;
    private String current_date, yesterday_date = "06-06-2018", offday;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "",UserName = "",
    MobileNo = "" ;

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    private void DeleteDataNotification() {

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM " + db.TABLE_GPS_SEND_NOTIFICATION, null);
        int a = cursor.getCount();
        if (a == 0) {
            sql.close();
            System.out.println("======= c= 0  fetchall ");
        } else {
            cursor.moveToFirst();
            do {
                try {
                    String GID = cursor.getString(cursor.getColumnIndex("GId"));
                    String date1 = cursor.getString(cursor.getColumnIndex("Date"));
                    Date date = DateNT.parse(date1);
                    Date now = new Date(System.currentTimeMillis());
                    long days = getDateDiff(date, now, TimeUnit.DAYS);
                    if (days > 0) {
                        sql.delete(db.TABLE_GPS_SEND_NOTIFICATION, "GId=?", new String[]{GID});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        sql.close();
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private void getLocationPlayservice() {
        locationManager = (LocationManager) mcontext
                .getSystemService(mcontext.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (mGoogleApiClient == null) {
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(this, this, this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();
        } else {
            Boolean data = mGoogleApiClient.isConnected();
            if (mGoogleApiClient.isConnected()) {
                try {

                    Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                    // Toast.makeText(getApplicationContext(), " Pradnya Lat ", Toast.LENGTH_LONG).show();
                    if (mCurrentLocation != null) {
                        double lat = mCurrentLocation.getLatitude(),
                                lon = mCurrentLocation.getLongitude();
                        mUpdatedLocation = mCurrentLocation;
                        latitude = lat;
                        longitude = lon;
                        canGetLocation = true;

                        Log.e("token ", "Lat :" + lat + " Long" + lon);
                        //   Toast.makeText(getApplicationContext(), " Pradnya Lat :" + lat + " Long" + lon, Toast.LENGTH_LONG).show();
                        GetCurrentLocation(lat, lon);

                    } else {
                        if (mUpdatedLocation != null) {
                            double lat = mUpdatedLocation.getLatitude(),
                                    lon = mUpdatedLocation.getLongitude();
                            latitude = lat;
                            longitude = lon;
                            canGetLocation = true;
                            Log.e("token ", "Lat :" + lat + " Long" + lon);
                            //   Toast.makeText(getApplicationContext(), " Pradnya Lat :" + lat + " Long" + lon, Toast.LENGTH_LONG).show();
                            GetCurrentLocation(lat, lon);
                        } else {
                            canGetLocation = false;
                           /* if (!isGPSEnabled) {
                                noLocation("GPS Location");
                            } else {
                                noLocation("GPS Location and Network Connectivity");

                            }*/
                            if (!isGPSEnabled && !isNetworkEnabled) {
                                noLocation("GPS Location and Network Connectivity");
                            } else {
                                if (!isGPSEnabled) {
                                    noLocation("GPS Location");
                                } else if (!isNetworkEnabled) {
                                    noLocation("Network Connectivity");
                                } else {
                                    noLocation("GPS Location or Network Connectivity");
                                }
                            }
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                    noLocation("Location Permission");

                }


            } else {
                createLocationRequest();
                mGoogleApiClient = new GoogleApiClient.Builder(this, this, this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();

            }
        }

    }


    private void GetCurrentLocation(final double lat, final double lang) {
        IsGPSLocation = userpreferences.getString("IsGpslocation", "");

        /*Geocoder geocoder = new Geocoder(FusedLocationTracker1.this,
                Locale.getDefault());*/
        try {
            Geocoder geocoder = new Geocoder(PaidLocationFusedLocationTracker1.this,
                    Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(lat, lang, 1);
            /*if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(", ");
                }
                sb.append(address.getLocality()).append(",");
                sb.append(address.getAdminArea()).append(",");
                sb.append(address.getCountryName()).append(".");*/

            String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            //  String country = addressList.get(0).getCountryName();
            //  String postalCode = addressList.get(0).getPostalCode();
            // String knownName = addressList.get(0).getFeatureName();
            LocationName = address + " , " + city + " , " + state;

            //Log.d("Location name:", LocationName);
            // Toast.makeText(mcontext, LocationName, Toast.LENGTH_SHORT).show();


            // LocationName = sb.toString();
            Calendar c = Calendar.getInstance();
            String Currenttime = dfDate.format(c.getTime()) + ".000";

            if (check_GPSrecords(Currenttime) > 0) {
            } else {
                cf.addGps(mobno, String.valueOf(lat),
                        String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");
                    /*if (!WebUrlClass.isRunningGPSsend) {
                        getRowFromDatabase();
                    }*/
                getRowFromDatabase();
            }

        } catch (Exception e) {

            LocationName = "Location Not Found";
            Calendar c = Calendar.getInstance();
            final String Currenttime = dfDate.format(c.getTime()) + ".000";


            if (check_GPSrecords(Currenttime) > 0) {

            } else {

                if (IsGPSLocation.equalsIgnoreCase("true")) {
                    if (isInternetAvailable(getApplicationContext())) {

                        try {
                            final String[] loc = new String[1];
                            final Thread t = new Thread() {

                                public void run() {
                                    try {
                                        String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + lat + "," + lang + "&sensor=true";
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
                                                    LocationName = zero2.getString("formatted_address");
                                                    cf.addGps(mobno, String.valueOf(lat),
                                                            String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");

                                                    getRowFromDatabase();
                                                } else {
                                                    JSONObject zero2 = Results.getJSONObject(0);
                                                    LocationName = zero2.getString("formatted_address");
                                                    cf.addGps(mobno, String.valueOf(lat),
                                                            String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");

                                                    getRowFromDatabase();
                                                }

                                            } else {
                                                cf.addGps(mobno, String.valueOf(lat),
                                                        String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");

                                                getRowFromDatabase();
                                            }
                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        cf.addGps(mobno, String.valueOf(lat),
                                                String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");

                                        getRowFromDatabase();
                                    }
                                }

                            };
                            t.start();

                            t.join();


                        } catch (Exception e1) {
                            e1.printStackTrace();
                            cf.addGps(mobno, String.valueOf(lat),
                                    String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");

                            getRowFromDatabase();
                        }

                    } else {

                        if (check_GPSrecords(Currenttime) > 0) {

                        } else {
                            cf.addGps(mobno, String.valueOf(lat),
                                    String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");
                            getRowFromDatabase();

                        }
                    }
                } else {

                    if (check_GPSrecords(Currenttime) > 0) {

                    } else {
                        cf.addGps(mobno, String.valueOf(lat),
                                String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");
                        getRowFromDatabase();

                    }
                }
            }
        }
    }


    public int check_GPSrecords(String dt) {//2018-01-15 18:12:28.000
        String countQuery = "SELECT distinct GpsAddedDt FROM  " + db.TABLE_ADD_GPSRECORDS + " WHERE GpsAddedDt='"
                + dt + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        sql.close();
        return count;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    protected void startLocationUpdates() {
        try {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            Log.d(TAG, "Location update started ..............: ");
        } catch (SecurityException e) {
            // e.printStackTrace();
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

   /* @Override
    public void onDestroy() {
        //   Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }*/


    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = this;
        Log.e("", "");
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        IsGPSLocation = userpreferences.getString("IsGpslocation", "");


        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = "";
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);

        StartTimer(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);

        if (!WebUrlClass.FlagDownloadgpsdetail) {
            new StartSession(PaidLocationFusedLocationTracker1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadRefreshTime().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                    StartTimer(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);

                }
            });

        } else {
            StartTimer(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mUpdatedLocation = location;
       /* if (mCurrentLocation == null) {
            noLocation();
        } else {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            mLastUpdateTime = java.text.DateFormat.getTimeInstance().format(new Date());
            GetCurrentLocation(latitude, longitude);
        }*/


    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
        locationManager = (LocationManager) mcontext
                .getSystemService(mcontext.LOCATION_SERVICE);
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        try {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            // Toast.makeText(getApplicationContext(), " Pradnya Lat ", Toast.LENGTH_LONG).show();
            if (mCurrentLocation != null) {
                double lat = mCurrentLocation.getLatitude(),
                        lon = mCurrentLocation.getLongitude();
                canGetLocation = true;
                mUpdatedLocation = mCurrentLocation;
                latitude = lat;
                longitude = lon;
                Log.e("token ", "Lat :" + lat + " Long" + lon);
                //   Toast.makeText(getApplicationContext(), " Pradnya Lat :" + lat + " Long" + lon, Toast.LENGTH_LONG).show();
                GetCurrentLocation(lat, lon);

            } else {
                if (mUpdatedLocation != null) {
                    double lat = mUpdatedLocation.getLatitude(),
                            lon = mUpdatedLocation.getLongitude();
                    canGetLocation = true;
                    latitude = lat;
                    longitude = lon;
                    Log.e("token ", "Lat :" + lat + " Long" + lon);
                    //   Toast.makeText(getApplicationContext(), " Pradnya Lat :" + lat + " Long" + lon, Toast.LENGTH_LONG).show();
                    GetCurrentLocation(lat, lon);
                } else {
                    canGetLocation = false;
                   /* if (!isGPSEnabled) {
                        noLocation("GPS Location");
                    } else {
                        noLocation("GPS Location and Network Connectivity");
                    }*/

                    if (!isGPSEnabled && !isNetworkEnabled) {
                        noLocation("GPS Location and Network Connectivity");
                    } else {
                        if (!isGPSEnabled) {
                            noLocation("GPS Location");
                        } else if (!isNetworkEnabled) {
                            noLocation("Network Connectivity");
                        } else {
                            noLocation("GPS Location or Network Connectivity");
                        }
                    }
                }

            }
        } catch (SecurityException e) {
            e.printStackTrace();
            noLocation("Location Permission");
            canGetLocation = false;

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "onConnectionSuspended", Toast.LENGTH_LONG)
                .show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "on Connection failed", Toast.LENGTH_LONG)
                .show();

    }

    private void noLocation(String sendmsg) {

        Log.d(TAG, "location is null ...............");
        Calendar c = Calendar.getInstance();
        String DAte = DateNT.format(c.getTime());
        String name = cf.GetUserName(UserMasterID);
        String msg = name + " has turned Off " + sendmsg + " on " + DAte;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery("Select * from " + db.TABLE_LEAVE_REPORTING_TO, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String data = cur.getString(cur.getColumnIndex("UserLoginId"));
            String data3 = cur.getString(cur.getColumnIndex("UserName"));
            String sd = cf.GetUsermaterID(data3);
            cf.addGpsNotification(UserMasterID, sd, DAte, msg);
        }
        String data = "Turn ON your " + sendmsg;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew));
        builder.setContentTitle("vWb GPS Alert");
        builder.setContentText(data);
        builder.setSound(defaultSoundUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
        builder.setSubText("Tap to turn on GPS Location");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
        String Currenttime = dfDate.format(c.getTime()) + ".000";

        if (check_GPSrecords(Currenttime) > 0) {

        } else {
            String loc = sendmsg + " is Off";
            cf.addGps(mobno, "", "", loc, Currenttime, UserMasterID, "No");
            /*if (!WebUrlClass.isRunningGPSsend) {
                getRowFromDatabase();
            }*/
            getRowFromDatabase();

        }
    }

    private void getRowFromDatabase() {
        String mobno, link, LocationName, GPSID;

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_ADD_GPSRECORDS + " where isUploaded=?",
                new String[]{"No"});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            sql.close();
            System.out.println("======= c= 0  fetchall ");
            WebUrlClass.isRunningGPSsend = false;
        } else {
            WebUrlClass.isRunningGPSsend = true;
            cursor.moveToFirst();
            GPSID = cursor.getString(cursor.getColumnIndex("GPSID"));
            JSONObject Object = new JSONObject();
            try {
                Object.put("Lat", cursor.getString(cursor.getColumnIndex("latitude")));
                Object.put("Long", cursor.getString(cursor.getColumnIndex("longitude")));
                Object.put("locationname", cursor.getString(cursor.getColumnIndex("locationName")));
                Object.put("GpsDate", cursor.getString(cursor.getColumnIndex("GpsAddedDt")));
                Object.put("usermasterId", cursor.getString(cursor.getColumnIndex("UserMasterID")));
                Object.put("AppName", WebUrlClass.AppNameChat);
                String MobileNo = cursor.getString(cursor.getColumnIndex("MobileNo"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                String locationName = cursor.getString(cursor.getColumnIndex("locationName"));
                String GpsAddedDt = cursor.getString(cursor.getColumnIndex("GpsAddedDt"));
                String UsermaterID = cursor.getString(cursor.getColumnIndex("UserMasterID"));
                // isUploaded = cursor.getString(cursor.getColumnIndex("Mobileno"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            sql.close();
            String s = Object.toString();
            s = s.replace("\\\\", "");
            if (isInternetAvailable(getApplicationContext())) {
                final String finalS = s;
                final String Id = GPSID;
                new StartSession(getApplicationContext(), new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GPSTask().execute(finalS, Id);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        //  ut.displayToast(getApplicationContext(), msg);
                        // stopSelf();
                    }
                });

            } else {
                // stopSelf();
                WebUrlClass.isRunningGPSsend = false;

            }

        }

    }

    private void getRowFromDatabaseNotification() {
        String GPSID;
        GPSNotificationID.clear();

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_GPS_SEND_NOTIFICATION + " where isUploaded=?",
                new String[]{"No"});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            sql.close();
            System.out.println("======= c= 0  fetchall ");
        } else {
            JSONArray array = new JSONArray();
            cursor.moveToFirst();
            do {
                GPSID = cursor.getString(cursor.getColumnIndex("GId"));
                GPSNotificationID.add(GPSID);
                JSONObject Object = new JSONObject();
                try {
                    Object.put("IssuedTo", cursor.getString(cursor.getColumnIndex("ToUsermatserID")));
                    Object.put("Message", cursor.getString(cursor.getColumnIndex("MSG")));
                    Object.put("App_Name", WebUrlClass.AppNameFCM);
                    String MobileNo = cursor.getString(cursor.getColumnIndex("FromUserMasterID"));
                    String latitude = cursor.getString(cursor.getColumnIndex("Date"));
                    String longitude = cursor.getString(cursor.getColumnIndex("MSG"));
                    String locationName = cursor.getString(cursor.getColumnIndex("isUploaded"));
                    String frmto = cursor.getString(cursor.getColumnIndex("ToUsermatserID"));
                    array.put(Object);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

            sql.close();
            JSONObject ObjectFinale = new JSONObject();
            try {
                ObjectFinale.put("objarr", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String s = ObjectFinale.toString();
            s = s.replace("\\\\", "");
            if (isInternetAvailable(getApplicationContext())) {
                final String finalS = s;
                new StartSession(getApplicationContext(), new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new GPSTaskNotification().execute(finalS);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {

            }

        }
    }

    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public class GPSTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = link + WebUrlClass.api_postGpsLocation;
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[1]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            String gpsid = result.get(1);
            SQLiteDatabase sql = db.getWritableDatabase();
            if (res.contains("Success")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "Yes");
                sql.update(db.TABLE_ADD_GPSRECORDS, contentValues, "GPSID=?",
                        new String[]{gpsid});
                sql.delete(db.TABLE_ADD_GPSRECORDS, "isUploaded=?",
                        new String[]{"Yes"});
                sql.close();
                getRowFromDatabase();
            } else {
                sql.close();
                getRowFromDatabase();

            }
            //  getRowFromDatabase();
        }
    }

    public class GPSTaskNotification extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String url = link + WebUrlClass.api_PostGpsNot;
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);

            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub

            SQLiteDatabase sql = db.getWritableDatabase();
            super.onPostExecute(result);
            String res = result.get(0);
            if (res.contains("Success")) {
                for (int i = 0; i < GPSNotificationID.size(); i++) {
                    String gpsid = GPSNotificationID.get(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", "Yes");
                    sql.update(db.TABLE_GPS_SEND_NOTIFICATION, contentValues, "GId=?",
                            new String[]{gpsid});
                    sql.delete(db.TABLE_GPS_SEND_NOTIFICATION, "isUploaded=?",
                            new String[]{"Yes"});
                }
            }
            sql.close();
        }
    }

    public boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }

    public void askLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void getLocationUsingAndroidAPI() {
        setLocationListner();
        captureLocation();
    }

    public void captureLocation() {

        try {
            locationManager = (LocationManager) mcontext
                    .getSystemService(mcontext.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = true;

                noLocation("GPS Location and internet Connectivity");
            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

                                Log.d("Network", "Network");
                                if (locationManager != null) {
                                    location = locationManager
                                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    if (location != null) {
                                        latitude = location.getLatitude();
                                        longitude = location.getLongitude();
                                    } else {
                                        latitude = 0.0;
                                        longitude = 0.0;
                                    }
                                }

                            }
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();

                    }
                } else if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }

                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

                if (latitude == 0.0 && longitude == 0.0) {
                    Calendar c = Calendar.getInstance();
                    String Currenttime = dfDate.format(c.getTime()) + ".000";
                    if (check_GPSrecords(Currenttime) > 0) {
                    } else {

                        cf.addGps(mobno, String.valueOf(latitude),
                                String.valueOf(longitude), "Location Not Found", Currenttime, UserMasterID, "No");
                    }
                } else {
                    GetCurrentLocation(latitude, longitude);
                }

            }
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void setLocationListner() {
        // Define a listener that responds to location updates
        locationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if (location == null) {
                    getLastKnownLocation();
                } else {
                    //     setNewLocation(getBetterLocation(location, mLocationFetched), mLocationFetched);
                    mUpdatedLocation = location;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    public Location getAccurateLocation() {

        try {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location newLocalGPS, newLocalNetwork;
            if (gpsLocation != null || networkLocation != null) {
                newLocalGPS = getBetterLocation(mLocationFetched, gpsLocation);
                newLocalNetwork = getBetterLocation(mLocationFetched, networkLocation);
                setNewLocation(getBetterLocation(newLocalGPS, newLocalNetwork), mLocationFetched);
            }
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return mLocationFetched;
    }

    protected Location getBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return location;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -TWO_SECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return location;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return location;
        } else if (isNewer && !isLessAccurate) {
            return location;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return location;
        }
        return currentBestLocation;
    }

    /**
     * Checks whether two providers are the same
     */

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public boolean isLocationAccurate(Location location) {
        if (location.hasAccuracy()) {
            return true;
        } else {
            return false;
        }
    }

    public Location getStaleLocation() {
        if (mLastLocationFetched != null) {
            return mLastLocationFetched;
        }
        /*if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }*/
        try {
            if (mProviderType == PaidLocationFusedLocationTracker1.GPS_PROVIDER) {
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (mProviderType == PaidLocationFusedLocationTracker1.NETWORK_PROVIDER) {
                return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                return getBetterLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            }
        } catch (SecurityException e) {

        }
        return mLastLocationFetched;
    }

    private void setNewLocation(Location location, Location oldLocation) {
        if (location != null) {
            mLastLocationFetched = oldLocation;
            mLocationFetched = location;
            mLastLocationUpdateTime = java.text.DateFormat.getTimeInstance().format(new Date());
            locationProvider = location.getProvider();
            // mLocationManagerInterface.locationFetched(location, mLastLocationFetched, mLastLocationUpdateTime, location.getProvider());
        }
    }

    public Location getLastKnownLocation() {
        locationProvider = LocationManager.NETWORK_PROVIDER;
        Location lastKnownLocation = null;
        // Or use LocationManager.GPS_PROVIDER
      /*  if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return lastKnownLocation;
        }*/
        try {
            lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            mUpdatedLocation = lastKnownLocation;
            return lastKnownLocation;
        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }
        return lastKnownLocation;
    }

    public void checkNetworkProviderEnable() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // buildAlertMessageTurnOnLocationProviders("Your location providers seems to be disabled, please enable it", "OK", "Cancel");
        } else if (!isGPSEnabled) {
            // buildAlertMessageTurnOnLocationProviders("Your GPS seems to be disabled, please enable it", "OK", "Cancel");
        } else if (!isNetworkEnabled) {
            // buildAlertMessageTurnOnLocationProviders("Your Network location provider seems to be disabled, please enable it", "OK", "Cancel");
        }
        // getting network status


    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    //class for timer to display toast message and run service in background
    private class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH:mm:ss
                    DateNT = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
                    mcontext = getApplicationContext();
                    GPSNotificationID = new ArrayList<String>();
                    userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                            Context.MODE_PRIVATE);
                    link = userpreferences.getString("CompanyURL", null);
                    mobno = userpreferences.getString("MobileNo", null);
                    UserMasterID = userpreferences.getString("UserMasterId", null);
                    Calendar cal = Calendar.getInstance();
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    Date date = new Date();
                    CharSequence time = DateFormat.format("EEEE", date.getTime());
                    DeleteDataNotification();
                    getRowFromDatabaseNotification();
       /* if (!WebUrlClass.isRunningGPSsend) {
            getRowFromDatabase();
        }*/
                    getRowFromDatabase();


                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);


                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    current_date = df.format(c);

                    String yesterday_date1 = userpreferences.getString("YestDate", yesterday_date);
                    offday = userpreferences.getString("OFFDAY", "");

                    if (!(time.equals("Sunday"))) {
                        if (hour > 8 && hour < 20) {
                            if (!current_date.equals(yesterday_date1)) {
                                if (isInternetAvailable(PaidLocationFusedLocationTracker1.this)) {
                                    new StartSession(PaidLocationFusedLocationTracker1.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new GetCheckIsOffDay().execute(UserMasterId);

                                        }

                                        @Override
                                        public void callfailMethod(String msg) {


                                        }
                                    });
                                }
                            }
                        }

                    }


                    if (offday.equalsIgnoreCase("false")) {
                        if (!(time.equals("Sunday"))) {
                            if (hour > 8 && hour < 20) {
                                if (isGooglePlayServicesAvailable()) {              // if googleplay services available
                                    getLocationPlayservice();                            // init obj for google play service and start fetching location
                                }/* else
                    checkNetworkProviderEnable();
                getLocationUsingAndroidAPI();*/
                                // getLocation();
                            }
                        }
                    }
                }
            });

        }

    }

    class DownloadRefreshTime extends AsyncTask<Integer, Void, String> {
        String res;
        String RefershTime, distanceAccuracy;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getdata;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                String columnName, columnValue;
                JSONObject jorder = jResults.getJSONObject(0);
                RefershTime = jorder.getString("RefreshTime");
                distanceAccuracy = jorder.getString("DistanceAccuracy");

            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("RefreshTime")) {
                WebUrlClass.FlagDownloadgpsdetail = true;
                if (!(distanceAccuracy.equalsIgnoreCase("") && RefershTime.equalsIgnoreCase(""))) {
                    int dis;
                    int time;
                    try {
                        dis = Integer.parseInt(distanceAccuracy);
                        time = Integer.parseInt(RefershTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        dis = 10;
                        time = 15;
                    }

                    MIN_DISTANCE_CHANGE_FOR_UPDATES = dis;
                    MIN_TIME_BW_UPDATES = 1000 * 60 * time;
                } else {
                    MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
                    MIN_TIME_BW_UPDATES = 1000 * 60 * 15;
                }

            } else {
                WebUrlClass.FlagDownloadgpsdetail = false;
                MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
                MIN_TIME_BW_UPDATES = 1000 * 60 * 15;
            }

            StartTimer(MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES);

        }

    }

    public void StartTimer(long TimeInterval, long Distance) {
        if (mtimer != null) {
            // mtimer.cancel();
        } else {
            mtimer = new Timer();
            // Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            mtimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, TimeInterval);
        }

    }

    class GetCheckIsOffDay extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            try {

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("YestDate", current_date);
                editor.putString("OFFDAY", res);
                offday = res;
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetCheckIsOffDay + "?UserMasterId=" + UserMasterId;

            res = ut.OpenConnection(url, getApplicationContext());


            if (res != null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

}

