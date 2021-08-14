package com.vritti.vwblib.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.classes.CommonFunction;



import java.util.ArrayList;

import com.vritti.vwblib.R;

public class SendOfflineData extends Service {
    String MobileNo;
    String latitude;
    String longitude;
    String locationName;
    String GpsAddedDt, UsermaterID;
    String isUploaded;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    public String mobno, link, LocationName, RecordID;

    SharedPreferences userpreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context context = this;
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        link = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        getRowFromDatabase();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getRowFromDatabase() {
        Object obj = null;
        int methodtype = 0;
        String Url = null, remark, date, is, output = null;

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?",
                new String[]{WebUrlClass.FlagisUploadedFalse});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            System.out.println("======= c= 0  fetchall ");
            stopSelf();
        } else {
            cursor.moveToFirst();
            try {
                RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                Url = cursor.getString(cursor.getColumnIndex("linkurl"));
                obj = cursor.getString(cursor.getColumnIndex("parameter"));
                methodtype = cursor.getInt(cursor.getColumnIndex("methodtype"));
                remark = cursor.getString(cursor.getColumnIndex("remark"));//output
                date = cursor.getString(cursor.getColumnIndex("AddedDt"));
                output = cursor.getString(cursor.getColumnIndex("output"));
                is = cursor.getString(cursor.getColumnIndex("isUploaded"));

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (methodtype == WebUrlClass.POSTFLAG) {
                String s = obj.toString();
                s = s.replace("\\\\", "");
                if (isInternetAvailable(getApplicationContext())) {
                    final String finalS = s;
                    final String url = Url;
                    final String Output = output;
                    new StartSession(getApplicationContext(), new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new SendTask().execute(url, finalS, Output, RecordID);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            //      ut.displayToast(getApplicationContext(), msg);
                            stopSelf();
                        }
                    });

                } else {
                    stopSelf();
                }
            } else if (methodtype == WebUrlClass.GETFlAG) {

                if (isInternetAvailable(getApplicationContext())) {

                    final String url = Url;
                    final String Output = output;
                    final String finalRecordID = RecordID;
                    new StartSession(getApplicationContext(), new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new SendTaskGet().execute(url, "", Output, finalRecordID);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }


        }
    }

    private void SendNotification(String recid) {
        String remark = "";
        String RecordID = "";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE recordID=?",
                new String[]{recid});
        int a = cursor.getCount();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            try {
                RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                remark = cursor.getString(cursor.getColumnIndex("remark"));//output
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String data = "An Error has accured while sending action " + remark + " to server";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       /* Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/
        //builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew));
        builder.setContentTitle("vWb action Alert");
        builder.setContentText(data);
        builder.setSound(defaultSoundUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
        builder.setSubText("Please check your action status in vwb_settings>>offline Data");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
    }

    private void SendNotificatioMsg(String recid, String msg) {
        String remark = "";
        String RecordID = "";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE recordID=?",
                new String[]{recid});
        int a = cursor.getCount();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            try {
                RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                remark = cursor.getString(cursor.getColumnIndex("remark"));//output
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String data = "Action " + remark + " is not performed as " + msg;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       /* Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/
        //builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.vlogonew));
        builder.setContentTitle("vWb action Alert");
        builder.setContentText(data);
        builder.setSound(defaultSoundUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
        builder.setSubText("Please check your action status in vwb_settings>>offline Data");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        notificationManager.notify(1, builder.build());
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


    public class SendTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenPostConnection(url, params[1], getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");

                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            data.add(params[3]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            SQLiteDatabase sql = db.getWritableDatabase();
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});

                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    SendNotificatioMsg(recid, stdres);
                }
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                SendNotification(recid);
            }
            sql.close();
            getRowFromDatabase();
        }
    }

    public class SendTaskGet extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                //response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            data.add(params[3]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            SQLiteDatabase sql = db.getWritableDatabase();
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});

               /* sql.delete(db.TABLE_DATA_OFFLINE, "isUploaded=?",
                        new String[]{ WebUrlClass.FlagisUploadedTrue});*/
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    SendNotificatioMsg(recid, stdres);
                }

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                SendNotification(recid);
                //  Toast.makeText(getApplicationContext(),"Record not send",Toast.LENGTH_LONG).show();

            }
            sql.close();
            getRowFromDatabase();

        }
    }
}
