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
import android.support.annotation.Nullable;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.classes.CommonFunction;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by 300151 on 10/25/2016.
 */
public class DownloadDataService extends Service {

    SQLiteDatabase sql;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;

    String ProjectId, moduleId;

    @Nullable
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
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        new DownloadWorkspaceDataJSON().execute();
        new DownloadGetUnChkUserlistDataJSON().execute();

        return super.onStartCommand(intent, flags, startId);

    }

    private void getScratchMainGroup(String ProjId) {
        ProjectId = ProjId;
        new DownloadScratchMainGroupDataJSON().execute(ProjectId);
    }

    private void getScratchSubMainGroup(String module_Id) {
        moduleId = module_Id;
        new DownloadScratchSubGroupDataJSON().execute(moduleId);
    }


    private void getMainGroup(String ProjId) {
        ProjectId = ProjId;
        new DownloadMainGroupDataJSON().execute(ProjectId);
        new DownloadGetChkUserlistDataJSON().execute(ProjectId);
        //  getSubMainGroup();
    }

    private void getSubMainGroup(String module_Id) {
        moduleId = module_Id;
        new DownloadSubGroupDataJSON().execute(moduleId);
    }

    class DownloadWorkspaceDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Workspace_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_WORKSPACE_LIST, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_WORKSPACE_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        ProjectId = jorder.getString("ProjectId");
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_WORKSPACE_LIST, null, values);
                    getMainGroup(ProjectId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {

            }
        }

    }

    class DownloadGetChkUserlistDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetChkUser_list + "?prjMstId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_WORKSPACE_LIST, null,
                        null);*/
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROJECT_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("prjMstId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_PROJECT_MEMBERS, null, values);
                    String data = a + "";


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {

            }
        }

    }

    class DownloadGetUnChkUserlistDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetUnChkUser_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ALL_MEMBERS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ALL_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_ALL_MEMBERS, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                String que = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST + " WHERE ProjectName='Scratch'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {

                } else {
                    new DownloadScratchWorkspaceJSON().execute();
                }
            }
        }


    }

    class DownloadMainGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_MainGroup_list + "?projectId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_MAINGROUP_LIST, null,
                        null);*/
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MAINGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {

                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        moduleId = jorder.getString("PKModuleMastId");
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("ProjectId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MAINGROUP_LIST, null, values);
                    getSubMainGroup(moduleId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                //  getSubMainGroup();
            }
        }

    }

    class DownloadSubGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SubGroup_list + "?moduleId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUBGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("PKModuleMastId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SUBGROUP_LIST, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {

            }
        }

    }

    class DownloadScratchMainGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_MainGroup_list + "?projectId=" + URLEncoder.encode(params[2], "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MAINGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {

                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        moduleId = jorder.getString("PKModuleMastId");
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("ProjectId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MAINGROUP_LIST, null, values);
                    getScratchSubMainGroup(moduleId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                //  getSubMainGroup();
            }
        }

    }

    class DownloadScratchSubGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SubGroup_list + "?moduleId=" + URLEncoder.encode(params[0], "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUBGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("PKModuleMastId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SUBGROUP_LIST, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {

            }
        }

    }

    class DownloadScratchWorkspaceJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Scrach_workspace;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_WORKSPACE_LIST, null);
                int count = c.getCount();
                String columnName, columnValue = "";

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        ProjectId = jorder.getString("ProjectId");
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("ProjectName")) {
                            columnValue = jorder.getString(columnName);
                        } else if (columnName.equalsIgnoreCase("ProjectId")) {
                            columnValue = jorder.getString(columnName);
                        } else {
                            columnValue = " ";
                        }

                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_WORKSPACE_LIST, null, values);
                    getScratchMainGroup(ProjectId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {


            }
        }

    }
}
