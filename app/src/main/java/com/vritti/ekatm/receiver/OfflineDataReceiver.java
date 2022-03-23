package com.vritti.ekatm.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.classes.CommonFunction;


import org.json.JSONObject;

import java.util.ArrayList;

public class OfflineDataReceiver extends BroadcastReceiver {
    static Context mContext;
    private String mobno, LocationName, link, UserMasterID;
    String CompanyURL, EnvMasterId = "", LoginId = "", Password = "", PlantMasterId = "", UserMasterId="",UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            mContext = context;
            db = new DatabaseHandlers(context);
            if (getCount()) {
                ut = new Utility();
                String settingKey = ut.getSharedPreference_SettingKey(context);
                String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
                db = new DatabaseHandlers(context, dabasename);
                link = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
                EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
                LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
                Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
                PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
                mobno = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);
                UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
                UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

                if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                    ConnectivityManager cm = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        Intent intent1 = new Intent(context, SendOfflineData.class);
                        context.startService(intent1);
                    } else {
                        Log.e("netInfo", "Connected");
                    }
                } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager cm = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        Log.e("netInfo", "Connected");
                        Intent intent1 = new Intent(context, SendOfflineData.class);
                        context.startService(intent1);

                        //    getRowFromDatabase();
                    } else {
                        Log.e("netInfo", "disConnected");
                    }
                } else {
                    ConnectivityManager cm = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                        Log.e("netInfo", "Connected");
                        Intent intent1 = new Intent(context, SendOfflineData.class);
                        context.startService(intent1);

//                getRowFromDatabase();

                    } else {
                        Log.e("netInfo", "disConnected");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getRowFromDatabase() {
        try {

            String GPSID;
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
                    Object.put("AppName", SetAppName.AppNameFCM);

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

               // sql.close();
                String s = Object.toString();
                s = s.replace("\\\\", "");
                if (ut.isNet(mContext)) {
                    final String finalS = s;
                    final String Id = GPSID;
                    new StartSession(mContext, new CallbackInterface() {
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class GPSTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = link + WebUrlClass.api_postGpsLocation;
                res = ut.OpenPostConnection(url, params[0], mContext);
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


            SQLiteDatabase sql = db.getWritableDatabase();
            if (res.contains("Success")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "Yes");
                sql.update(db.TABLE_ADD_GPSRECORDS, contentValues, "GPSID=?",
                        new String[]{gpsid});
                sql.delete(db.TABLE_ADD_GPSRECORDS, "isUploaded=?",
                        new String[]{"Yes"});
               // sql.close();
                getRowFromDatabase();
            } else {
               // sql.close();
                getRowFromDatabase();
            }

        }
    }

    private boolean getCount() {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOGIN_SETTING, null);
        int cnt = c.getCount();
        if (cnt > 0) {
            return true;
        } else {
            return false;
        }
    }


}



