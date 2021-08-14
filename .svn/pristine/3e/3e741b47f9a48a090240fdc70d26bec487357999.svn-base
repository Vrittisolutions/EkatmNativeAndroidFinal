package com.vritti.vwblib.Services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.commonObjectProperties;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;

public class BackgroundService extends Service {
    SQLiteDatabase sql;
    SharedPreferences userpreferences;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="";
    String ProjectId, moduleId;
    public static Boolean Activity_AssignByMe = false;
    String FinalObj;
    commonObjectProperties commonObj;
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;

    public BackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Context  context = this;
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
        sql = db.getWritableDatabase();
        getActicityData();
        return super.onStartCommand(intent, flags, startId);

    }
    public void getActicityData() {
        //  showProgressDialog();
        Activity_AssignByMe = false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;

        try {

            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "('13','14','25')");


        } catch (Exception e) {
            e.printStackTrace();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new StartSession(BackgroundService.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadCommanDataURLJSON().execute();
            }

            @Override
            public void callfailMethod(String msg) {

            }
        });

    }

    class DownloadCommanDataURLJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_PostloadWorkData;
            try {
                res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());

                //response = java.net.URLDecoder.decode(response, "UTF-8");
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("\\\\\"", "");
              /*  response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\"", "");
               // response = response.replaceAll(" ", "");

                response = URLEncoder.encode("UTF-8",response);
              //  String result = java.net.URLDecoder.decode(response, "UTF-8");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_ACTIVITYMASTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        if(columnName.equalsIgnoreCase("ActivityName")){
                            columnValue = jorder.getString(columnName);
                            columnValue = URLDecoder.decode(columnValue,"UTF-8");
                        }
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_ACTIVITYMASTER, null, values);
                    Log.e("", "" + a);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

        }
        }

    }

