package com.vritti.sessionlib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;


import com.vritti.databaselib.data.DatabaseHandlers;

import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import java.net.URLEncoder;
public class StartSession {
    private Context parent;
    Boolean IsSessionActivate = false;
    String CompanyURL, EnvMasterId = "", LoginId ="", Password = "", PlantMasterId = "";
    SharedPreferences userpreferences;
    private CallbackInterface callback;
    Boolean IsValidUser = false;
    public ProgressDialog progressDialog, progressDialog1, progressDialog2;
    Utility ut;
    DatabaseHandlers db;

    public StartSession(Context context, CallbackInterface callback) {
        parent = context;
        this.callback = callback;

        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);

        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        System.out.println("Envmasterid :"+EnvMasterId);
        System.out.println("Envmasterid-1 :"+LoginId);
        System.out.println("Envmasterid-2 :"+PlantMasterId);
         // new DownloadGetEnvJSON().execute();
        //    new GetSessionFromServer().execute();

           new DownloadUserMasterIdFromServer().execute();

    }


    class DownloadUserMasterIdFromServer extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetUserMasterIdAndroid;

            try {
                res = Utility.OpenConnection(url,parent);
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("UserMasterI")) {//UserMasterId##317250de-e70b-4a1c-9b6d-e41bc3fb824a
                //[{"UserMasterID":"317250de-e70b-4a1c-9b6d-e41bc3fb824a","UserName":"Pradnya Ingale","LoginID":"300169"}]
                callback.callMethod();
            } else {
                new GetSessionFromServer().execute();
            }

        }

    }

    class GetSessionFromServer extends AsyncTask<String , Void, String> {
        String res;
        Boolean IsSessionActivate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetSessions + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8")
                        + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                        + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8")
                        + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsSessionActivate = Boolean.parseBoolean(res);

            } catch (Exception e) {
                e.printStackTrace();
                IsSessionActivate = false;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            // dismissProgressDialog();
            if (IsSessionActivate) {

                callback.callMethod();

            } else {

                callback.callfailMethod("The server is taking too long to respond or something " +
                        "is wrong with your internet connection. Please try again later");
            }

        }
    }





    class DownloadIsValidUser extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetIsValidUser + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8") + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8") + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8");
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                // res = res.replaceAll("\\\\\"", "");
                res = res.toString().replaceAll("\\\\", "");
                //  res = res.replaceAll("\"", "");
                //  res = res.replaceAll(" ", "");
                IsValidUser = Boolean.parseBoolean(res);


            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("true")) {
                new GetSessionFromServer().execute();
            } else if (s.contains("Invalid Password And Plant")) {
                //dismissProgressDialog();
                // callback.callfailMethod("Invalid Password And Plant");
                callback.callfailMethod("Can not Start Session");

            } else if (s.contains("Invalid Password")) {
                // dismissProgressDialog();
                // callback.callfailMethod("Invalid Password");
                callback.callfailMethod("Can not Start Session");

            } else if (s.contains("You are not valid user for selected plant")) {
                // dismissProgressDialog();
                //  callback.callfailMethod("You are not valid user for selected plant");
                callback.callfailMethod("Can not Start Session");

            } else {
                //  dismissProgressDialog();
                //  callback.callfailMethod("User Not Valid");
                callback.callfailMethod("Can not Start Session");

            }
        }

    }

    class DownloadPlantsJSON extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getPlants + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&PlantId=";
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.Errormsg;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("PlantMasterId")) {

                new DownloadIsValidUser().execute();
            } else {
                //dismissProgressDialog();
                // callback.callfailMethod("Failed to get Plants");
                callback.callfailMethod("Can not Start Session");

            }
        }

    }

    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;

            try {
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");

            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.Errormsg;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("AppEnvMasterId")) {
                new DownloadPlantsJSON().execute();

            } else {
                //dismissProgressDialog();
                // callback.callfailMethod("Failed to get Environment");
                callback.callfailMethod("Can not Start Session");
            }
        }
    }



}
