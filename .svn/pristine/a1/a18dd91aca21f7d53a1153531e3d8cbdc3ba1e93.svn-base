package com.vritti.vwblib.Services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Beans.GPSMyLocationBean;
import com.vritti.vwblib.classes.CommonFunction;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sharvari on 06-Mar-18.
 */

public class GpsDataLocalSendSevice extends Service {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    ProgressBar mProgress;
    SharedPreferences userpreferences;
    private String mindATE;
    TextView mtxtsetname, mtxt_no_record;
    Button reload;
    ListView mList;
    ArrayList<GPSMyLocationBean> GPSRecods;
    private SimpleDateFormat dateFormat;
    private Calendar cal_L, cal_H;
    String Date_L;
    String Date_H;
    private Date date;
    private String mobno, LocationName, link, UserMasterID;
    String  UserName, MobileNo;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this;
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        updateList();
        if(isInternetAvailable(getApplicationContext())){
            getRowFromDatabase();
        }
    }
    private void updateList() {
        GPSRecods.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_ADD_GPSRECORDS, null);
        int a = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                GPSMyLocationBean bean = new GPSMyLocationBean();
                bean.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                bean.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                bean.setGPSID(cur.getString(cur.getColumnIndex("GPSID")));
                bean.setFKUserMasterId(cur.getString(cur.getColumnIndex("UserMasterID")));
                String s = cur.getString(cur.getColumnIndex("GpsAddedDt"));
                // s = getDate(s);
                bean.setAddedDT(s);
                bean.setLocationName(cur.getString(cur.getColumnIndex("locationName")));
                bean.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                GPSRecods.add(bean);
            } while (cur.moveToNext());

           /* mList.setAdapter(new GPSRecordsAdapter(GpsDataLocalSendSevice.this,
                    GPSRecods));*/
            sql.close();
        }
    }
    private void getRowFromDatabase() {
        String GPSID;

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_ADD_GPSRECORDS + " where isUploaded=?",
                new String[]{"No"});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            sql.close();
            System.out.println("======= c= 0  fetchall ");
        } else {
            cursor.moveToFirst();
            GPSID = cursor.getString(cursor.getColumnIndex("GPSID"));
            JSONObject Object = new JSONObject();
            try {
                Object.put("Lat", cursor.getString(cursor.getColumnIndex("latitude")));
                Object.put("Long", cursor.getString(cursor.getColumnIndex("longitude")));
                Object.put("locationname", cursor.getString(cursor.getColumnIndex("locationName")));
                Object.put("GpsDate", cursor.getString(cursor.getColumnIndex("GpsAddedDt")));
                Object.put("usermasterId", cursor.getString(cursor.getColumnIndex("UserMasterID")));
                Object.put("AppName",WebUrlClass.AppNameChat);

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
                String url = CompanyURL + WebUrlClass.api_postGpsLocation;
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
           /* sql.close();
            getRowFromDatabase();*/
        }
    }
}
