package com.vritti.crmlib.services;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;


public class GPSTracker extends Service implements LocationListener {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    Context mcontext;
    // flag for gps status
    boolean isGPSEnabled = false;
    // flag fornework status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    protected LocationManager locationManager;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    private String mobno, LocationName, link, UserMasterID;

    SharedPreferences userpreferences;
    SimpleDateFormat dfDate, DateNT;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//yyyy-MM-dd HH:mm:ss
        DateNT = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");

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

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        Date date = new Date();
        CharSequence time = DateFormat.format("EEEE", date.getTime());
        if (!(time.equals("Sunday"))) {
            if (hour > 9 && hour < 20) {
                getLocation();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public GPSTracker(Context Context) {
        // TODO Auto-generated constructor stub
        this.mcontext = Context;
        getLocation();
    }

    public GPSTracker() {
        // TODO Auto-generated constructor stub
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) mcontext
                    .getSystemService(mcontext.LOCATION_SERVICE);
           /* LocationRequest request = new LocationRequest();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Calendar c = Calendar.getInstance();
                String time = DateNT.format(c.getTime());
                String name = cf.GetUserName(UserMasterID);
                String msg = name + " has turned Off GPS Location  and Internet connectivity on " + time;
                SQLiteDatabase sql = db.getWritableDatabase();

                cf.addGpsNotification(UserMasterID, name, time, msg);
                /*Cursor cur = sql.rawQuery("Select * from " + db.TABLE_LEAVE_REPORTING_TO, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    String data = cur.getString(cur.getColumnIndex("UserLoginId"));
                    String data3 = cur.getString(cur.getColumnIndex("UserName"));
                    String sd = db.GetUsermaterID(data3);
                    db.addGpsNotification(UserMasterID, sd, time, msg);
                }*/


                String data = "Turn ON your GPS location and Internet Connectivity";
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
                // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.vlogonew));
                builder.setContentTitle("CRM GPS Alert");
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
                    cf.addGps(mobno, "", "",
                            "Network and GPS are not Enabled", Currenttime, "", "No");
                    startService(new Intent(GPSTracker.this, SendGPSData.class));
                }


            } else {
                this.canGetLocation = true;
                if (isGPSEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else if (isNetworkEnabled) {
                                locationManager.requestLocationUpdates(
                                        LocationManager.NETWORK_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
                        startService(new Intent(GPSTracker.this, SendGPSData.class));
                    }
                } else {
                    GetCurrentLocation(latitude, longitude);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int check_GPSrecords(String dt) {
        String countQuery = "SELECT distinct GpsAddedDt FROM  '" + db.TABLE_ADD_GPSRECORDS + "' WHERE GpsAddedDt='"
                + dt + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private void GetCurrentLocation(double lat, double lang) {
        try {
            Geocoder geocoder = new Geocoder(GPSTracker.this,
                    Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(lat, lang, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(", ");
                }
                sb.append(address.getLocality()).append(",");
                sb.append(address.getAdminArea()).append(",");
                sb.append(address.getCountryName()).append(".");
                LocationName = sb.toString();
                Calendar c = Calendar.getInstance();
                String Currenttime = dfDate.format(c.getTime()) + ".000";
                if (check_GPSrecords(Currenttime) > 0) {
                } else {
                    cf.addGps(mobno, String.valueOf(lat),
                            String.valueOf(lang), LocationName, Currenttime, UserMasterID, "No");
                    startService(new Intent(GPSTracker.this, SendGPSData.class));
                }

            }
        } catch (Exception e) {
            Calendar c = Calendar.getInstance();
            String Currenttime = dfDate.format(c.getTime()) + ".000";
            if (check_GPSrecords(Currenttime) > 0) {
            } else {
                cf.addGps(mobno, String.valueOf(lat),
                        String.valueOf(lang), "Location not Found", Currenttime, UserMasterID, "No");
                startService(new Intent(GPSTracker.this, SendGPSData.class));
            }
        }
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

    public void showSettingsAlert() {


        
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //   Toast.makeText(getApplicationContext(), "onLocationChanged " + location.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //  Toast.makeText(getApplicationContext(), "onStatusChanged " + provider.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        //  Toast.makeText(getApplicationContext(), "onProviderEnabled " + provider.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Toast.makeText(getApplicationContext(), "onProviderDisabled " + provider.toString(), Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}