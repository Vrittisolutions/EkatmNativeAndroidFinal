package com.vritti.sales.utils_tbuds;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sales.activity.ShipmentEntryActivity;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.net.URLConnection;
import java.net.URLEncoder;
import com.vritti.databaselib.other.Utility;

public class StartSession_tbuds {
    private Context parent;
    private CallbackInterface callback;
    public ProgressDialog progressDialog, progressDialog1, progressDialog2;
    ProgressHUD progress;
    private String res;
    Boolean IsSessionActivate = false;

    /*String CompanyURL="http://tbuds.ekatm.com";
    String EnvMasterId = "tbuds";
    String LoginId ="admin";
    String Password = "admin123";
    String PlantMasterId = "1";*/

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    public StartSession_tbuds(Context context, CallbackInterface callback) {
        parent = context;
        this.callback = callback;

        ut = new Utility();
        //cf = new Tbuds_commonFunctions(this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        //userpreferences = context.getSharedPreferences(AnyMartData.USERINFO, Context.MODE_PRIVATE);
        System.out.println("Envmasterid :"+EnvMasterId);
        System.out.println("Envmasterid-1 :"+LoginId);
        System.out.println("Envmasterid-2 :"+PlantMasterId);

        new GetSessionFromServer().execute();
        //new GetSessionId().executOpenConnectione();
    }

    private void showProgressDialog() {
        /*if (progressDialog == null) {
            progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();*/

         if (progress == null) {

          //   progress = ProgressHUD.show(parent, "Loading ...", false, true, null);
        }
       // progressDialog.show();
    }

    private void dismissProgressDialog() {
        if ((progress != null) && progress.isShowing()) {
            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

  /*  @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
*/

  //start sessions of ekatm
  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  class GetSessionFromServer extends AsyncTask<Integer, Void, Integer> {
      String res, responseString;
      String res_AppEnvMasterId;
      Boolean IsSessionActivate;

      @Override
      protected void onPreExecute() {
          super.onPreExecute();
      }

      @Override
      protected Integer doInBackground(Integer... params) {
          try {
              String url_ekatmsess = CompanyURL + AnyMartData.API_GETSESSIONS_EKATM + "?AppEnvMasterId=" +
                      URLEncoder.encode(EnvMasterId, "UTF-8")
                      + "&UserLoginId=" + URLEncoder.encode(LoginId, "UTF-8")
                      + "&UserPwd=" + URLEncoder.encode(Password, "UTF-8")
                      + "&PlantId=" + URLEncoder.encode(PlantMasterId, "UTF-8");

              res = Utility.OpenConnection_ekatm(url_ekatmsess,parent);
              int a = res.getBytes().length;
              res = res.replaceAll("\\\\\"", "");
              res = res.replaceAll("\"", "");
              res = res.replaceAll(" ", "");
              IsSessionActivate = Boolean.parseBoolean(res);

          } catch (Exception e) {
              e.printStackTrace();
              IsSessionActivate = false;
          }
          return null;
      }

      @Override
      protected void onPostExecute(Integer integer) {
          super.onPostExecute(integer);
          // dismissProgressDialog();

         if(IsSessionActivate){
             callback.callMethod();
             new GetSessionId().execute();
         }else {
             callback.callfailMethod("The server is taking too long to respond or something " +
                     "is wrong with your internet connection. Please try again later");

             new GetSessionFromServer().execute();
         }

      }
  }

    class GetSessionId extends AsyncTask<Void, Void, Void> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String ses_activate_1 = AnyMartData.MAIN_URL + AnyMartData.METHOD_SESSION_ACTIVATE_1+
                    "?Mobileno="+ AnyMartData.MOBILE + "&version="+ AnyMartData.VERSION;
            OutputStreamWriter wr = null;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                res = Utility.OpenconnectionOrferbilling(ses_activate_1, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           // dismissProgressDialog();
            if (!responseString.equalsIgnoreCase("error")) {
            //    Toast.makeText(parent,"Sesson1 Activated",Toast.LENGTH_SHORT).show();
                AnyMartData.SESSION_ID = responseString;
                new GetHandle().execute();
            } else {
                dismissProgressDialog();
                //progressDialog.dismiss();
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        class GetHandle extends AsyncTask<Void, Void, Void> {
            String responseString = null;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
               // showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
               // String ses_activate_2 = AnyMartData.API_ACTION_SESSION_ACTIVATE_2;
                String ses_activate_2 = AnyMartData.MAIN_URL + AnyMartData.METHOD_SESSION_ACTIVATE_2 +
                        "?Mobileno="+ AnyMartData.MOBILE+"&SessionId="+ AnyMartData.SESSION_ID;
                String ses2 = ses_activate_2;
                URLConnection urlConnection = null;
                BufferedReader bufferedReader = null;
                try{
                    res = Utility.OpenconnectionOrferbilling(ses_activate_2, parent);
                    int a = res.getBytes().length;
                    res = res.replaceAll("\\\\\"", "");
                    res = res.replaceAll("\"", "");
                    res = res.replaceAll(" ", "");
                    responseString = res.toString().replaceAll("^\"|\"$", "");
                    Log.e("Response", responseString);

                } catch (Exception e) {
                    responseString = "error";
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);
                dismissProgressDialog();
                if (!responseString.equalsIgnoreCase("error")) {
                    AnyMartData.HANDLE = responseString.replaceAll(
                            "[^0-9]", "");
                    new GetSessionFinalService().execute();
                } else {
                  //  dismissProgressDialog();
                    //progressDialog.dismiss();
                    Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }

        class GetSessionFinalService extends AsyncTask<Void, Void, Void> {
            String responseString = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
             //   showProgressDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {

                String ses_activate_3 = AnyMartData.MAIN_URL + AnyMartData.METHOD_SESSION_ACTIVATE_3 +
                        "?SessionId="+ AnyMartData.SESSION_ID+
                        "&Handle="+ AnyMartData.HANDLE+
                        "&strSessionTime="+ AnyMartData.SESSION_TIME_OUT+
                        "&Instance="+ AnyMartData.INSTANCE;

                URLConnection urlConnection = null;
                BufferedReader bufferedReader = null;

                try {
                    res = Utility.OpenconnectionOrferbilling(ses_activate_3, parent);
                    int a = res.getBytes().length;
                    res = res.replaceAll("\\\\\"", "");
                    res = res.replaceAll("\"", "");
                    res = res.replaceAll(" ", "");
                    responseString = res.toString().replaceAll("^\"|\"$", "");
                    Log.e("Response", responseString);

                } catch (Exception e) {
                    responseString = "error";
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                super.onPostExecute(result);
                dismissProgressDialog();

                if (!responseString.equalsIgnoreCase("error")) {
                    callback.callMethod();
                } else {
                   // dismissProgressDialog();
                    Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
}
