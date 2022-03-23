package com.vritti.ekatm.services;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.EnoSampleSubmitClass;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnoJobService extends JobService {

    public String mobno, link, LocationName;

    private Handler mHandler = new Handler();

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    Context context;
    FirebaseJobDispatcher firebaseJobDispatcher = EnoSampleSubmitClass.dispatcherNew;
    SQLiteDatabase sql;

    @Override
    public boolean onStartJob(JobParameters job) {
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


            getRowFromDatabase();


        return false;
    }

    private void getRowFromDatabase() {
        Object obj = null;
        int methodtype = 0;
        String RecordID = "";
        String Filename = "";

        String Url = null, remark, date, is, output = null, filepath = "", filename = "";
        sql  = db.getWritableDatabase();
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
                         if (methodtype == 3) {
                            if (isInternetAvailable(getApplicationContext())) {
                                final String finalUrl = Url;
                                final String finalFilename1 = Filename;
                                final String finalOutput = output;
                                final String finalRecordID2 = RecordID;
                                //startSesstionApi(finalUrl, finalFilename1, finalOutput, finalRecordID2);
                                //callRerofitApi(finalUrl, finalFilename1, finalOutput, finalRecordID2);
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
                                        final String finalLocalVar = localVar;

                                        if (finalLocalVar != null && (!finalLocalVar.equals("send")) && (!finalLocalVar.equals(WebUrlClass.FlagisUploadedTrue)))
                                            new PostUploadImageMethodProspect().execute(finalUrl, finalFilename1, finalOutput, finalRecordID2);


                                    }@Override
                                    public void callfailMethod(String msg) {
                                        ut.displayToast(context, msg);
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

    /*private void callRerofitApi(String url, String filename, final String output, String recordID) {
        ServiceGenerator.changeApiBaseUrl(CompanyURL);
        //ServiceGenerator.changeApiBaseUrl("http://192.168.1.221");
        final AppService scalarService = ServiceGenerator.createService(AppService.class);
        MultipartBody.Part imageUrl = null;

        File file1 = FileUtils.getFile(context , Uri.fromFile(new File(url)));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
        // RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(Uri.fromFile(new File(url)))), file1);
        imageUrl = MultipartBody.Part.createFormData("UploadedImage", file1.getName(), requestFile);

        Call call;
        call = scalarService.startNewSesstionSesstion(EnvMasterId, LoginId, Password, PlantMasterId);
        final MultipartBody.Part finalImageUrl = imageUrl;
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                call = scalarService.REGISTRATION_RESPONSE_CALL(output, EnvMasterId ,finalImageUrl );
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("resuslt :", String.valueOf(response));
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.i("resuslt :", String.valueOf(t.getMessage()));
                    }
                });
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("resuslt :", String.valueOf(t.getMessage()));
            }
        });

    }*/

    private void startSesstionApi(String finalUrl, String finalFilename1, String finalOutput, String finalRecordID2) {

        new GetSessionFromServer().execute(finalUrl, finalFilename1, finalOutput, finalRecordID2);

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

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }


    public class PostUploadImageMethodProspect extends AsyncTask<String, Void, ArrayList<String>> {
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

                Log.i("imageNameTry:", urls[0]);
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "send");
                SQLiteDatabase sql = db.getWritableDatabase();
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "linkurl=?",
                        new String[]{ urls[0]});

                File sourceFile = FileUtils.getFile(context, Uri.fromFile(new File(urls[0])));
                String ActivityID = urls[2];//AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + EnvMasterId +"&ActivityId="+ ActivityID;
               // FileInputStream fileInputStream = new FileInputStream(sourceFile);
                Object res = null;
                String response = null;
                 response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri , sourceFile));
                if (response!= null && (!response.equals(""))) {
                    try {
                        Log.i("imageNameDone:", urls[0]);
                        jsonimage.put("File", urls[0]);
                        jsonArray.put(jsonimage);
                        Log.i("imageNameError:", urls[0]);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                                new String[]{ urls[0]});

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                        Log.i("imageNameError:", urls[0]);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                                new String[]{ urls[0]});
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
       /* contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.update(db.TABLE_DATA_OFFLINE, contentValues, "parameter=?",
                new String[]{imagefilename});*/

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
                    if(firebaseJobDispatcher != null){
                        SendNotificatioMsg();
                       // firebaseJobDispatcher.cancelAll();
                       // EnoSampleSubmitClass.myJobNew = null;
                    }
               /* samplePojoClassArrayList.clear();
                samplePojoClassArrayList = tempSampleClassArray;*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void SendNotificatioMsg() {

            String data = "Your Images is uploaded";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
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
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Will display the notification in the notification bar
            Random random = new Random();
            int code = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(code, builder.build());
        }



    }

    class GetSessionFromServer extends AsyncTask<String, Void, List<String>> {
        String res;
        Boolean IsSessionActivate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetSessions + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8")
                        + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");
                res = Utility.OpenConnection(url,context);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
                IsSessionActivate = false;
            }
            List<String> result = new ArrayList<String>();
            result.add(params[0]);
            result.add(params[1]);
            result.add(params[2]);
            result.add(params[3]);

            return result;
        }

        @Override
        protected void onPostExecute(List<String> integer) {
            super.onPostExecute(integer);
            String finalUrl = integer.get(0);
             String finalFilename1 = integer.get(1);
             String finalOutput = integer.get(2);
             String finalRecordID2 = integer.get(3);
            // dismissProgressDialog();
            if (IsSessionActivate) {
                Log.i("imageNameCall:", integer.get(0));
                Cursor c = sql.rawQuery(
                        "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE linkurl=?",
                        new String[]{finalUrl});
                int a = c.getCount();
                String localVar = null;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    localVar = c.getString(c.getColumnIndex("isUploaded"));
                }
                final String finalLocalVar = localVar;
                if(finalLocalVar != null && (!finalLocalVar.equals("send")) &&(!finalLocalVar.equals(WebUrlClass.FlagisUploadedTrue)))
                    new PostUploadImageMethodProspect().execute(finalUrl, finalFilename1, finalOutput, finalRecordID2);

                //callback.callMethod();
            } else {
               // callback.callfailMethod("The server is taking too long to respond or something " +
                     //   "is wrong with your internet connection. Please try again later");
            }

        }
    }

}
