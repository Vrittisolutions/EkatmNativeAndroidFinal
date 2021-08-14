package com.vritti.ekatm.services;

import android.app.NotificationManager;
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
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.FileUtils;
import com.vritti.vwb.ImageWithLocation.FinalObjectForENO;
import com.vritti.vwb.ImageWithLocation.ListObjectForEno;
import com.vritti.vwb.ImageWithLocation.SamplePojoClass;
import com.vritti.vwb.ImageWithLocation.Sample_List_Object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class SendOfflineData extends Service {
    String MobileNo;
    String latitude;
    String longitude;
    String locationName;
    String GpsAddedDt, UsermaterID;
    String isUploaded;
    public String mobno, link, LocationName;
    // DatabaseHandler db;
    // SQLiteDatabase sql;
    SharedPreferences userpreferences;
    private Timer mtimer = null;
    private Handler mHandler = new Handler();

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    Context context;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        if (intent == null) {
            getRowFromDatabase();

        } else {
            getRowFromDatabase();

            if (intent.hasExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY)) {
            } else {
                long MIN_TIME_BW_UPDATES = 1000 * 60 * 5;

                //  StartTimer(MIN_TIME_BW_UPDATES, 0);
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private void getRowFromDatabase() {
        Object obj = null;
        int methodtype = 0;
        String RecordID = "";
        String Filename = "";

        String Url = null, remark, date, is, output = null, filepath = "", filename = "";
        final SQLiteDatabase sql = db.getWritableDatabase();
       /* Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?",
                new String[]{WebUrlClass.FlagisUploadedFalse});*/

        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=? AND AttemptCount<=3",
                new String[]{WebUrlClass.FlagisUploadedFalse});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            System.out.println("======= c= 0  fetchall ");
            stopSelf();
        } else {
            cursor.moveToFirst();
            do {
                try {

                    RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                    Url = cursor.getString(cursor.getColumnIndex("linkurl"));// for attachment it is path

                    methodtype = cursor.getInt(cursor.getColumnIndex("methodtype"));
                    if (methodtype == WebUrlClass.ATTACHMENTFlAG) {
                        Filename = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                    } else {
                        obj = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                    }

                    filepath = cursor.getString(cursor.getColumnIndex("AttachmentPath"));
                    filename = cursor.getString(cursor.getColumnIndex("AttachmentFileName"));
                    output = cursor.getString(cursor.getColumnIndex("output"));//for attachment it is ActivityID
                    is = cursor.getString(cursor.getColumnIndex("isUploaded"));
                    remark = cursor.getString(cursor.getColumnIndex("remark"));
                    date = cursor.getString(cursor.getColumnIndex("AddedDt"));


                    if(is.equals(WebUrlClass.FlagisUploadedFalse)) {
                        if (methodtype == WebUrlClass.POSTFLAG) {
                            String s = obj.toString();
                            s = s.replace("\\\\", "");
                            if (isInternetAvailable(getApplicationContext())) {
                                final String finalS = s;
                                final String url = Url;
                                final String Output = output;
                                final String finalRecordID1 = RecordID;
                                final String finalFilepath = filepath;
                                final String finalFilename = filename;
                                new StartSession(getApplicationContext(), new CallbackInterface() {
                                    @Override
                                    public void callMethod() {

                                        new SendTask().execute(url, finalS, Output, finalRecordID1, finalFilepath, finalFilename);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        //      cc.displayToast(getApplicationContext(), msg);
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

                        } else if (methodtype == WebUrlClass.ATTACHMENTFlAG) {
                            if (isInternetAvailable(getApplicationContext())) {
                                new PostUploadImageMethod().execute(Url, Filename, output, RecordID);
                            }

                        } else if (methodtype == 3) {
                            if (isInternetAvailable(getApplicationContext())) {
                                final String finalUrl = Url;
                                final String finalFilename1 = Filename;
                                final String finalOutput = output;
                                final String finalRecordID2 = RecordID;

                                new StartSession(context, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        Log.i("imageNameCall:", finalFilename1);
                                        Cursor c = sql.rawQuery(
                                                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE linkurl=?",
                                                new String[]{finalUrl});
                                        int a = c.getCount();
                                        String localVar = null;
                                        if ((c.getCount() > 0)) {
                                            c.moveToFirst();
                                            localVar = c.getString(c.getColumnIndex("isUploaded"));
                                        }
                                        if(localVar != null && (!localVar.equals("send")) &&(!localVar.equals(WebUrlClass.FlagisUploadedTrue)))
                                        new PostUploadImageMethodProspect().execute(finalUrl, finalFilename1, finalOutput, finalRecordID2);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });



                                //callRerofitApi(Url, Filename, output, RecordID);
                            }
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
            /**/
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
        String data = "An Error has occured while sending action " + remark + " to server";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       /* Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/
        //builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
        builder.setContentTitle("Action alert");
        builder.setContentText(data);
        builder.setSound(defaultSoundUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
        builder.setSubText("Check action status in setting>>offline Data");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(code, builder.build());
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
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
        builder.setContentTitle("Action alert");
        builder.setContentText(data);
        builder.setSound(defaultSoundUri);
        builder.setSubText("Check action status in Menu>>setting>>offline data");
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Will display the notification in the notification bar
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(code, builder.build());
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
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\", "");

                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            data.add(params[3]);
            data.add(params[4]);
            data.add(params[5]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            String filepath = result.get(3);
            String filename = result.get(4);
            SQLiteDatabase sql = db.getWritableDatabase();


            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    getRowFromDatabase();

                } else if (res.length() >= 36) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    getRowFromDatabase();

                    if (isInternetAvailable(getApplicationContext())) {
                        new PostUploadImageMethod().execute(filepath, filename, res, "");
                    }
                } else {
                    Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                    int i = 0;
                    if ((c.getCount() > 0)) {
                        c.moveToFirst();
                        i = c.getInt(c.getColumnIndex("AttemptCount"));
                        i = i + 1;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
                    contentValues.put("AttemptCount", i);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});


                    SendNotificatioMsg(recid, stdres);
                }
            } else {

                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                SendNotification(recid);
            }
            sql.close();

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
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\", "");
                // response = response.replaceAll("u0026", "&");
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

            if (res.equals("10")){
                Toast.makeText(context,"You have already filled the time slot !",Toast.LENGTH_SHORT).show();
            }
            if (res.contains("Biometric punch")){
                Toast.makeText(context,"You can not fill time sheet before Biometric punch !",Toast.LENGTH_SHORT).show();
            }
            if (res.contains("From time")){
                Toast.makeText(context,"From time should not be greater than to time !",Toast.LENGTH_SHORT).show();
            }
            if (res.contains("current time")){
                Toast.makeText(context,"You can not fill time sheet greater than current time !",Toast.LENGTH_SHORT).show();
            }

            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    getRowFromDatabase();
               /* sql.delete(db.TABLE_DATA_OFFLINE, "isUploaded=?",
                        new String[]{ WebUrlClass.FlagisUploadedTrue});*/
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    //SendNotificatioMsg(recid, res);
                }

            } else {
                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                SendNotification(recid);
                //  Toast.makeText(getApplicationContext(),"Record not send",Toast.LENGTH_LONG).show();

            }


        }
    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, ArrayList<String>> {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonimage = new JSONObject();
        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(urls[0]);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_FileUploadProspect;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //conn.setRequestProperty("uploaded_file", urls[0]);
                conn.setRequestProperty("UploadedImage", urls[0]);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                /*dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + urls[0] + "\"" + lineEnd); */
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + urls[0] + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {


                    try {
                        jsonimage.put("File", urls[1]);
                        jsonArray.put(jsonimage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    if (serverResponseMessage.contains("Error")) {


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(urls[0]);
            Data.add(urls[1]);
            Data.add(urls[2]);
            Data.add(urls[3]);

            return Data;

        }

        protected void onPostExecute(ArrayList<String> feed) {
            String path = feed.get(0);
            String Imagefilename = feed.get(1);
            String ActivityId = feed.get(2);
            String recid = feed.get(3);

            String Vendordata = "";
            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isInternetAvailable(getApplicationContext())) {
                    new SaveAttachment().execute(Vendordata, recid);

                  /*  if (recid.equalsIgnoreCase("")) {
                        new SaveAttachmentProspect().execute(Vendordata, recid);

                    } else {
                        new SaveAttachment().execute(Vendordata, recid);

                    }*/
                } else {
                    //  Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }

        }
    }

    public class PostUploadImageMethodProspect extends AsyncTask<String, Void, ArrayList<String>> {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonimage = new JSONObject();
        private Exception exception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected ArrayList<String> doInBackground(String... urls) {

            try {
                Log.i("imageNameTry:", urls[0]);
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "send");
                SQLiteDatabase sql = db.getWritableDatabase();
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "linkurl=?",
                        new String[]{ urls[0]});
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                //   String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = FileUtils.getFile(context, Uri.fromFile(new File(urls[0])));
              //  String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + EnvMasterId;
                String ActivityID = urls[2];//AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + EnvMasterId+"&ActivityId="+ActivityID;
                //String upLoadServerUri = "http://b207.ekatm.com/api/UploadFilesAPI/UploadFileForAndroid?AppEnvMasterId=eno";
                //String upLoadServerUri = "http://b207.ekatm.com" + WebUrlClass.api_FileUploadProspect;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("UploadedImage", urls[0]);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"UploadedImage\";filename=\""
                        + sourceFile.getName() + "\"" + lineEnd);
                // dos.writeBytes("Content-Disposition: form-data; name=\"UploadedImage\"; filename=\""+urls[0]+"\"\r\nContent-Type: image/jpeg\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {


                    try {
                        Log.i("imageNameDone:", urls[0]);
                        jsonimage.put("File", urls[0]);
                        jsonArray.put(jsonimage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        Log.i("imageNameError:", urls[0]);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                                new String[]{ urls[0]});

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(urls[0]);
            Data.add(urls[1]);
            Data.add(urls[2]);
            Data.add(urls[3]);

            return Data;

        }

        protected void onPostExecute(ArrayList<String> feed) {
            String path = feed.get(0);
            String Imagefilename = feed.get(1);
            String ActivityId = feed.get(2);
            String recid = feed.get(3);

            String Vendordata = "";
            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isInternetAvailable(getApplicationContext())) {

                   checkAllImageUpload(path.substring(path.lastIndexOf("/")).replace("/", ""), ActivityId);


                } else {
                    //  Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }

        }
    }




    private void checkAllImageUpload(String imagefilename, String activityId) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.update(db.TABLE_DATA_OFFLINE, contentValues, "parameter=?",
                new String[]{imagefilename});
        //getRowFromDatabase();

        ListObjectForEno listObjectForEno = new Gson().fromJson(AppCommon.getInstance(context).getFilnalListSampl(), ListObjectForEno.class);
        if (listObjectForEno != null) {
            ArrayList<FinalObjectForENO> finalObjectForENOList = listObjectForEno.getFinalObjectForENOArrayList();
            if (finalObjectForENOList != null && finalObjectForENOList.size() != 0) {

       /* Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?",
                new String[]{WebUrlClass.FlagisUploadedFalse});*/

                Cursor cursor = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where output='" + activityId + "'", null);
                int a = cursor.getCount();
                if (cursor.getCount() == 0) {
                    System.out.println("======= c= 0  fetchall ");
                    stopSelf();
                } else {
                    cursor.moveToFirst();
                    boolean flag;
                    do {
                        try {

                            String is = cursor.getString(cursor.getColumnIndex("isUploaded"));
                            if (!is.equals(WebUrlClass.FlagisUploadedTrue)) {
                                flag = false;
                                break;
                            } else {
                                flag = true;
                            }

                        } catch (Exception e) {
                            flag = false;
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                    // call all Image Done
                    if (flag) {
                        Log.i("image ::", "allDone");
                        new SendEnoSampleObjectPos().execute(activityId);
                    }

                }
            } else {
                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where output='" + activityId + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "output=?",
                        new String[]{activityId});
                //SendNotification(integer);

                getRowFromDatabase();
            }
        }
    }

    class SaveAttachment extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            SQLiteDatabase sql = db.getWritableDatabase();
            if (!(integer.equalsIgnoreCase(""))) {
                if ((response != null) && (!response.equalsIgnoreCase(WebUrlClass.setError))) {
                    if (response.equalsIgnoreCase("true")) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                        sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                                new String[]{integer});
                        getRowFromDatabase();
                    } else {
                        Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + integer + "'", null);
                        int i = 0;
                        if ((c.getCount() > 0)) {
                            c.moveToFirst();
                            i = c.getInt(c.getColumnIndex("AttemptCount"));
                            i = i + 1;
                        }
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
                        contentValues.put("AttemptCount", i);
                        sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                                new String[]{integer});
                        getRowFromDatabase();

                    }
                } else {

                    Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + integer + "'", null);
                    int i = 0;
                    if ((c.getCount() > 0)) {
                        c.moveToFirst();
                        i = c.getInt(c.getColumnIndex("AttemptCount"));
                        i = i + 1;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                    contentValues.put("AttemptCount", i);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{integer});
                    //SendNotification(integer);

                    getRowFromDatabase();

                }
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0], context);
                response = res.toString();
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return params[1];
        }
    }

    class SaveAttachmentProspect extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (!(integer.equalsIgnoreCase(""))) {

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachmentProspect;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0], context);
                response = res.toString();
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return params[1];
        }
    }

    public void StartTimer(long TimeInterval, long Distance) {
        if (mtimer != null) {
            //   mtimer.cancel();
           /* try {
                mtimer.wait(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        } else {

            mtimer = new Timer();
            mtimer.purge();
            // Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
            mtimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, TimeInterval);
        }

    }


    private class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {


                    getRowFromDatabase();


                }
            });

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private class SendEnoSampleObjectPos extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        String activityId;

        @Override
        protected String doInBackground(String... strings) {
            String activityId = strings[0];
            String url = null;
            url = CompanyURL + WebUrlClass.api_postSurvayData;
            ListObjectForEno listObjectForEno = new Gson().fromJson(AppCommon.getInstance(context).getFilnalListSampl(), ListObjectForEno.class);
            this.activityId = activityId;
            if (listObjectForEno != null) {
                ArrayList<FinalObjectForENO> finalObjectForENOArrayList = listObjectForEno.getFinalObjectForENOArrayList();
                if (finalObjectForENOArrayList.size() != 0) {
                    for (FinalObjectForENO finalObjectForENO : finalObjectForENOArrayList) {
                        if (finalObjectForENO.getActivityObject().getActivityId().equals(activityId)) {
                            // find activity id

                            try {
                                res = Utility.OpenPostConnection(url, new Gson().toJson(finalObjectForENO), context);
                                response = res.toString();
                                response = response.replaceAll("\\\\", "");
                                response = response.substring(1, response.length() - 1);
                                return activityId;
                            } catch (Exception e) {
                                e.printStackTrace();
                                response = WebUrlClass.setError;
                            }

                        } else {
                            // not find activity
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    // remove to
                    Sample_List_Object sample_list_object = new Gson().fromJson(AppCommon.getInstance(context).getEnoSampelList(), Sample_List_Object.class);
                    ArrayList<SamplePojoClass> samplePojoClassArrayList = sample_list_object.getSamplePojoClassArrayList();
                    ArrayList<SamplePojoClass> tempSampleClassArray = new ArrayList<>();

                    for (int i = 0; i < samplePojoClassArrayList.size(); i++) {
                        samplePojoClassArrayList.remove(i);
                    }
                    if (samplePojoClassArrayList.size() != 0) {
                        String listObj = new Gson().toJson(new Sample_List_Object(samplePojoClassArrayList));
                        AppCommon.getInstance(context).setEnoSampelList(listObj);
                    } else {
                        AppCommon.getInstance(context).setEnoSampelList(null);
                    }
               /* samplePojoClassArrayList.clear();
                samplePojoClassArrayList = tempSampleClassArray;*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}