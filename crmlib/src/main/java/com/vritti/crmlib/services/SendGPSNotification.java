package com.vritti.crmlib.services;

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
import android.widget.Toast;


import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONObject;

import java.util.ArrayList;


public class SendGPSNotification extends Service {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;


    SQLiteDatabase sql;
    SharedPreferences userpreferences;
    //private CommonClass cc;
    String link;
    public SendGPSNotification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        link = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        sql = db.getWritableDatabase();


        getRowFromDatabase();
        return super.onStartCommand(intent, flags, startId);

    }

    private void getRowFromDatabase() {
        String GPSID;
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_GPS_SEND_NOTIFICATION + " where isUploaded=?",
                new String[]{"No"});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            System.out.println("======= c= 0  fetchall ");
            stopSelf();
        } else {
            cursor.moveToFirst();
            GPSID = cursor.getString(cursor.getColumnIndex("GId"));
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

            } catch (Exception e) {
                e.printStackTrace();
            }


            String s = Object.toString();
            s = s.replace("\\\\", "");
            if (isInternetAvailable(getApplicationContext())) {
                final String finalS = s;
                final String sed = GPSID;

                new StartSession(getApplicationContext(), new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new GPSTask().execute(finalS, sed);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }

                  /*  @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }*/
                });

            } else {
                stopSelf();
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
                String url = link + WebUrlClass.api_PostGpsNot;
                res = ut.OpenPostConnection(url, params[0],getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
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
            if (res.contains("Success")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "Yes");
                sql.update(db.TABLE_GPS_SEND_NOTIFICATION, contentValues, "GId=?",
                        new String[]{gpsid});
                sql.delete(db.TABLE_GPS_SEND_NOTIFICATION, "isUploaded=?",
                        new String[]{"Yes"});
            } else {

            }
            getRowFromDatabase();
        }
    }
}
