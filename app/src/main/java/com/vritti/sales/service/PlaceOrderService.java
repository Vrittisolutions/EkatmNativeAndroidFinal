package com.vritti.sales.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
//import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import com.vritti.sales.activity.CheckoutActivity;
import com.vritti.sales.activity.MainActivity;
import com.vritti.sales.beans.PlaceOrderBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chetana
 */
public class PlaceOrderService extends Service {
    private Context parent;
    PlaceOrderBean placeOrderBean;
    List<PlaceOrderBean> placeOrderBeanList;
    JSONObject JsonMain;
    private String DateToStr, OrdRCVDt_ModifiedDt;
    String res = "";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    String dabasename;
    private String ID="";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        parent = PlaceOrderService.this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parent = PlaceOrderService.this;
        placeOrderBeanList = new ArrayList<PlaceOrderBean>();
      //  databaseHelper = new DatabaseHelper(PlaceOrderService.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(PlaceOrderService.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
       // mprogress=findViewById(R.id.toolbar_progress_App_bar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*new StartSession_tbuds(parent, new CallbackInterface() {
            @Override
            public void callMethod() {

            }
            @Override
            public void callfailMethod(String s) {
            }
        });*/

        getRowFromDatabase();

        return super.onStartCommand(intent, flags, startId);
    }

    private void getRowFromDatabase() {

        placeOrderBeanList.clear();

        DatabaseHandlers db1_new = new DatabaseHandlers(PlaceOrderService.this,dabasename);
        SQLiteDatabase db_new = db1_new.getWritableDatabase();

        JsonMain = AnyMartData.JMain;

         Cursor c1 = db_new.rawQuery("Select * from " + DatabaseHandlers.TABLE_PLACE_ORDER + " where isUploaded='No'", null);

       /* Cursor c1 = sql_db.rawQuery("Select * from " + DatabaseHandlers.TABLE_PLACE_ORDER  + " where isUploaded='No'", null);*/

        placeOrderBeanList.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                placeOrderBean = new PlaceOrderBean();
                placeOrderBean.setPid(c1.getInt(c1.getColumnIndex("Pid")));
                placeOrderBean.setUsertype(c1.getString(c1.getColumnIndex("C_V_type")));
                placeOrderBean.setExpectedDateTime(c1.getString(c1.getColumnIndex("schedule_date_time")));
                placeOrderBean.setXml1(c1.getString(c1.getColumnIndex("xml1")));
                placeOrderBean.setXml2(c1.getString(c1.getColumnIndex("xml2")));
                placeOrderBeanList.add(placeOrderBean);

            } while (c1.moveToNext());
        } else {
            db_new.close();
            c1.close();
        }

        if (NetworkUtils.isNetworkAvailable(PlaceOrderService.this)) {

           new StartSession_tbuds(PlaceOrderService.this, new CallbackInterface() {

                @Override
                public void callMethod() {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        if (placeOrderBeanList.size() > 0) {
                            JSONObject jsonMain ;
                            for (int i = 0; i < placeOrderBeanList.size(); i++) {
                                String xml1 = placeOrderBeanList.get(i).getXml1();
                                String xml2 = placeOrderBeanList.get(i).getXml2();
                                String datetime = placeOrderBeanList.get(i).getExpectedDateTime();
                                String id = String.valueOf(placeOrderBeanList.get(i).getPid());

                                jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("HeaderData", xml1);
                                    jsonMain.put("ItemData",xml2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                              //  new PlaceOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,datetime, jsonMain.toString(), id);
                                new PlaceOrder().execute(datetime, jsonMain.toString(), id);
                                Log.e("In - ","place order execution failed");
                            }
                        }

                    } else {

                       new GetSessionId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                       Log.e("log - ","session execution failed");
                    }

                }
                @Override
                public void callfailMethod(String s) {
                }
            });
        }
        //if network off then condition
    }

    //start sessions of ekatm
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    class GetSessionFromServer extends AsyncTask<Integer, Void, Integer> {
        String res, responseString;
        String res_AppEnvMasterId;
        Boolean IsSessionActivate;

        String CompanyURL_new="http://tbuds.ekatm.com";
        String EnvMasterId = "tbuds";
        String LoginId ="admin";
        String Password = "admin123";
        String PlantMasterId = "1";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url_ekatmsess = CompanyURL_new + AnyMartData.API_GETSESSIONS_EKATM + "?AppEnvMasterId=" +
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

                Log.e("Response", responseString);

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
             //callback.callMethod();
             new GetSessionId().execute();
         }else {
         }

        }
    }

    class GetSessionId extends AsyncTask<Void, Void, Void> {
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // String ses_activate_1 = AnyMartData.API_ACTION_SESSION_ACTIVATE_1;
            String ses_activate_1 = AnyMartData.MAIN_URL + AnyMartData.METHOD_SESSION_ACTIVATE_1+
                    "?Mobileno="+ AnyMartData.MOBILE+"&version="+ AnyMartData.VERSION;
            OutputStreamWriter wr = null;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                res = Utility.OpenconnectionOrferbilling(ses_activate_1, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
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

            if (!responseString.equalsIgnoreCase("error")) {
                AnyMartData.SESSION_ID = responseString;
                new GetHandle().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {

                Toast.makeText(PlaceOrderService.this,
                        "Poor Internet Connection. Please try again later.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        class GetHandle extends AsyncTask<Void, Void, Void> {
            String responseString = null;

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
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
                    res = res.replaceAll("\\\\", "");
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

                if (!responseString.equalsIgnoreCase("error")) {
                    AnyMartData.HANDLE = responseString.replaceAll(
                            "[^0-9]", "");
                    new GetSessionFinalService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {

                    Toast.makeText(PlaceOrderService.this, "Server is not responding. Please try again later",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        class GetSessionFinalService extends AsyncTask<Void, Void, Void> {
            String responseString = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //  showProgressDialog();
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
                    res = res.replaceAll("\\\\", "");
                    // res = res.replaceAll("\"", "");
                    // res = res.replaceAll(" ", "");
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

                if (!responseString.equalsIgnoreCase("error")) {
                    if (placeOrderBeanList.size() > 0) {
                        for (int i = 0; i < placeOrderBeanList.size(); i++) {
                            String xml1 = placeOrderBeanList.get(i).getXml1();
                            String xml2 = placeOrderBeanList.get(i).getXml2();
                            String datetime = placeOrderBeanList.get(i).getExpectedDateTime();
                            String id = String.valueOf(placeOrderBeanList.get(i).getPid());
                            JSONObject jsonMain = new JSONObject();
                            try {
                                jsonMain.put("HeaderData", xml1);
                                jsonMain.put("ItemData",xml2);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //AnyMartData.JMain = jsonMain;
                            //new PlaceOrder().execute(datetime, jsonMain.toString(), id);
                            new PlaceOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,datetime, jsonMain.toString(), id);
                        }
                    }
                } else {

                    Toast.makeText(PlaceOrderService.this, "Poor Internet connection. Please try again later.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    class PlaceOrder extends AsyncTask<String, Void, String> {

        String responseString = "";
        String response_generateOrder = "";
        String res ="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String dt=params[0];
            String body=params[1];
            ID = params[2];
            String url_generateOrder = "";

                try {
                    if(JsonMain.toString() != null){
                        url_generateOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_GENERATE_ORDER +
                                "?handler="+ AnyMartData.HANDLE +
                                "&sessionid="+ AnyMartData.SESSION_ID +
                                "&Scheduledate="+ URLEncoder.encode(params[0],"UTF-8") +
                                // "&Arr="+  JsonMain.toString().replaceAll("^\"|\"$", "");
                                "&Arr="+  URLEncoder.encode(JsonMain.toString(),"UTF-8");
                    }else {
                        //No data found
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            try {
                res = Utility.OpenConnection(url_generateOrder, parent);
               // res = Utility.OpenconnectionOrferbilling(url_generateOrder, parent);
               // int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "")+ "," + params[2];
                response_generateOrder = responseString.replaceAll("\\\\","");
                System.out.println("resp ="+response_generateOrder);
                Log.e("Response", response_generateOrder);

            } catch (Exception e) {
                response_generateOrder = "error";
                e.printStackTrace();
            }
            return response_generateOrder;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (response_generateOrder.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(PlaceOrderService.this)) {

                    new GetSessionId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            } else if (response_generateOrder.equalsIgnoreCase("error")) {
                Toast.makeText(PlaceOrderService.this, "Sorry! Order not Placed due to network error",
                        Toast.LENGTH_LONG).show();
            }else if(response_generateOrder.contains("ExceptionMessage")){
                /*Toast.makeText(PlaceOrderService.this, "Sorry! unable to place order due to server error.",
                        Toast.LENGTH_LONG).show();*/

                String id[] = result.split(",");

                try{
                    sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+ID+"'",
                            null);
                }catch (Exception e){
                    e.printStackTrace();
                }

              //getRowFromDatabase();

            } else {
               Toast.makeText(PlaceOrderService.this, "Order Placed Successfully", Toast.LENGTH_LONG).show();

                String id[] = result.split(",");

                try{
                    sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+id[1]+"'",
                            null);
        //            new HistoryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch (Exception e){
                    e.printStackTrace();
                }

               getRowFromDatabase();

             //  ((CheckoutActivity)parent).CallThankYouActivity(parent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("intentFrom","OrderPlaceComplete");
                        startActivity(intent);
                    }
                }, 3000);

                //finish(getApplicationContext());
            }
        }
    }

    class HistoryOrder extends AsyncTask<Void, Void, Void> {
        // ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_ORDER_HISTORY +
                    "?mobileno="+ AnyMartData.MOBILE +
                    "&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID ;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_orderHistory = responseString.replaceAll("\\\\","");

            } catch (NullPointerException e) {
                resp_orderHistory = "empty";
                e.printStackTrace();
            }catch (Exception e) {
                resp_orderHistory = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (resp_orderHistory.equalsIgnoreCase("Session Expired")) { Log.e("History","Session Expired");
            } else if (resp_orderHistory.equalsIgnoreCase("error")) { Log.e("History","error");
            } else if (resp_orderHistory.equalsIgnoreCase("empty")) {  Log.e("History","empty");
            } else {
               // parseJson(resp_orderHistory);
            }
        }

       /* protected void parseJson(String json) {
       //     tcf.clearTable(PlaceOrderService.this, DatabaseHandlers.TABLE_MY_ORDER_HISTORY);
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {

                    String placeOrderDate = jsonArray.getJSONObject(i).getString("DoAck");
                    String OrdRcvdDate = jsonArray.getJSONObject(i).getString("ModifiedDt");

                    SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                    SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date d1 = format.parse(placeOrderDate);
                    Date d2 = format.parse(OrdRcvdDate);
                    //DateToStr = toFormat.format(date);
                    DateToStr = Format.format(d1);
                    OrdRCVDt_ModifiedDt = Format.format(d2);
                    // DateToStr = format.format(d1);
                    System.out.println(DateToStr);
                    System.out.println(OrdRCVDt_ModifiedDt);

                    String a = jsonArray.getJSONObject(i).getString("status");

                    databaseHelper.addOrderHistory(jsonArray.getJSONObject(i).getString(
                            "Address"), jsonArray.getJSONObject(i).getString(
                            "City"), jsonArray.getJSONObject(i).getString(
                            "ConsigneeName"), jsonArray.getJSONObject(i).getString(
                            "CustomerMasterId"), jsonArray.getJSONObject(i).getString(
                            "ItemMasterId"), jsonArray.getJSONObject(i).getString(
                            "Mobile"), jsonArray.getJSONObject(i).getString(
                            "Qty"), jsonArray.getJSONObject(i).getString(
                            "Rate"), jsonArray.getJSONObject(i).getString(
                            "SODate"), jsonArray.getJSONObject(i).getString(
                            "SOHeaderId"), jsonArray.getJSONObject(i).getString(
                            "DODisptch"), jsonArray.getJSONObject(i).getString(
                            "DORcvd"), jsonArray.getJSONObject(i).getString(
                            "status"),jsonArray.getJSONObject(i).getString(
                            "statusname"), jsonArray.getJSONObject(i).getString(
                            "DoAck"), jsonArray.getJSONObject(i).getString(
                            "NetAmt"),jsonArray.getJSONObject(i).getString("ItemDesc"),
                            jsonArray.getJSONObject(i).getString("LineAmt"),
                            jsonArray.getJSONObject(i).getString("merchantid"),
                            jsonArray.getJSONObject(i).getString("merchantname"),
                            jsonArray.getJSONObject(i).getString("SODetailId"),
                            jsonArray.getJSONObject(i).getString("sono"),
                            jsonArray.getJSONObject(i).getString("SOScheduleId"),
                            jsonArray.getJSONObject(i).getString("ShipmentQty"),
                            jsonArray.getJSONObject(i).getString("ClientRecQty"),
                            jsonArray.getJSONObject(i).getString("AppvDt"),
                            "","",
                            jsonArray.getJSONObject(i).getString("DispatchNo"),
                            jsonArray.getJSONObject(i).getString("SalesHeaderId"),"",
                            jsonArray.getJSONObject(i).getString("DispNetAmnt"),
                            jsonArray.getJSONObject(i).getString("ShipStatus"),
                            jsonArray.getJSONObject(i).getString("ModifiedDt"),
                            DateToStr);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            new MainActivity.GetPendingOrderHistoryList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }*/
    }
}
