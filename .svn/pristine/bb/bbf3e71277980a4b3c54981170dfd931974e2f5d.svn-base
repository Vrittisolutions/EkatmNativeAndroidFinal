package com.vritti.sales.service;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.CounterBilling.CounterBillingMainActivity;
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.MarchantItemBean;
import com.vritti.sales.beans.MarchantPOBean;
import com.vritti.sales.beans.MyOrederBean;
import com.vritti.sales.beans.PlaceOrderBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by 300151 on 6/3/2016.
 */
public class MarchantService extends Service {
    private Context parent;
    String res = "";
    List<MyOrederBean> OrderBeanList;
    String SoHeaderId, resp, SOdate, rejSoHeaderId, rejSoDetailId, userid;
    Boolean isInternetPresent = false;
    Connectiondetector cd;
    private StringBuilder sb;
    SharedPreferences sharedpreferences;
    String xml, xml_runi;
    String fromdate, toDate, itemcode, item_name, item_mrp, result, item_qnty, discA, discP, freeitemid, freeitem_qnty, Minvalue;
    PlaceOrderBean placeOrderBean;
    List<PlaceOrderBean> placeOrderBeanList;
    private String json;

    JSONArray jsonArray_merchantsb, jsonArray_merchantsb_runi;
    JSONObject jsonObject_merchantsb, jsonObject_merchantsb_runi;
    public static ArrayList<MarchantPOBean> AddMRP_PO_listBeanArrayList;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @Override
    public IBinder onBind(Intent intent) {
        //  databaseHelper = new DatabaseHelper(getApplicationContext());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parent = MarchantService.this;

      //  databaseHelper = new DatabaseHelper(MarchantService.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);
      //  dbHandler = new DatabaseHandler(getApplicationContext());

      //  sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        userid = sharedpreferences.getString("userid", null);
        OrderBeanList = new ArrayList<MyOrederBean>();
        placeOrderBeanList = new ArrayList<PlaceOrderBean>();
        AnyMartData.AddMRP_PO_listBeanArrayList = new ArrayList<MarchantPOBean>();
        sb = new StringBuilder();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(MarchantService.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);
       // mprogress=findViewById(R.id.toolbar_progress_App_bar);

        cd = new Connectiondetector(MarchantService.this);
        isInternetPresent = cd.isConnectingToInternet();
        new GetSessionId().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    class GetSessionId extends AsyncTask<Void, Void, Void> {
        String responseString = "";

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
               // res = res.replaceAll("\"", "");
               // res = res.replaceAll(" ", "");
               responseString = res.toString().replaceAll("^\"|\"$", "");
               Log.e("Response", responseString);
               /*URL url_lgn_session_1 = new URL(ses_activate_1);
               URL url = url_lgn_session_1;
               urlConnection = url_lgn_session_1.openConnection();
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
               //bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               StringBuffer stringBuff_ses1 = new StringBuffer();
               String line;
               StringBuilder s_lgn_session_1 = new StringBuilder();
               while((line = bufferedReader.readLine())!= null){
                   stringBuff_ses1 = stringBuff_ses1.append(line);
               }
               responseString = stringBuff_ses1.toString().replaceAll("^\"|\"$", "");*/
               //  responseString = responseString.replaceAll("^\"|\"$", "");
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
                AnyMartData.SESSION_ID = responseString;
                new GetHandle().execute();
            } else {

                //progressDialog.dismiss();
                Toast.makeText(MarchantService.this, "Session Id receive error",
                        Toast.LENGTH_SHORT).show();

            }
        }

        class GetHandle extends AsyncTask<Void, Void, Void> {
            String responseString = "";

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
                //  showProgressDialog();
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
                    // res = res.replaceAll("\"", "");
                    // res = res.replaceAll(" ", "");
                    responseString = res.toString().replaceAll("^\"|\"$", "");
                    Log.e("Response", responseString);

                   /* URL url_lgn_session_2 = new URL(ses_activate_2);
                    URL url = url_lgn_session_2;
                    urlConnection = url_lgn_session_2.openConnection();

                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "JSON");
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                    //bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer stringBuff_ses2 = new StringBuffer();
                    String line;
                    StringBuilder s_lgn_ses2 = new StringBuilder();
                    while((line = bufferedReader.readLine())!=null){
                        stringBuff_ses2 = stringBuff_ses2.append(line);
                    }

                    responseString = stringBuff_ses2.toString().replaceAll("^\"|\"$", "");*/
                    String rs = responseString;
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
                //  dismissProgressDialog();
                if (!responseString.equalsIgnoreCase("error")) {
                    AnyMartData.HANDLE = responseString.replaceAll(
                            "[^0-9]", "");
                    new GetSessionFinalService().execute();
                } else {

                    //progressDialog.dismiss();
                    Toast.makeText(MarchantService.this, "Handler receive error",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }

        class GetSessionFinalService extends AsyncTask<Void, Void, Void> {
            String responseString = "";

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

                   /* URL url_lgn_session_3 = new URL(ses_activate_3);

                    URL url = url_lgn_session_3;
                    urlConnection = url_lgn_session_3.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "JSON");
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                    StringBuffer stringBuff_ses3 = new StringBuffer();
                    String line;
                    StringBuilder s_lgn_ses_3 = new StringBuilder();
                    while((line = bufferedReader.readLine())!=null){
                        stringBuff_ses3 = stringBuff_ses3.append(line);
                    }
                    responseString = stringBuff_ses3.toString().replaceAll("^\"|\"$", "");*/
                    String rs2 = responseString;
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
                    confirmOrderData();
                    RejectedOrderData();
                    getTradeDiscount();
                    getVolumeDiscount();
                    try {
                        getAddMRP();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SendBillDetails();
                    getAddMRP_runi();
                  // new GetProductList().execute();
                    new GetMRPFromServer().execute();
                //    new GetMarchantPOFromServer().execute();
                } else {

                    Toast.makeText(MarchantService.this, "Server Error..", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void SendBillDetails() {

        placeOrderBeanList.clear();
     //   DatabaseHandler db1 = new DatabaseHandler(MarchantService.this);
     //   SQLiteDatabase db = db1.getWritableDatabase();
        Cursor c1 = sql_db.rawQuery("Select * from "
                + dbhandler.TABLE_BILL_DETAILS
                + " where isUploaded=?", new String[]{"No"});

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                placeOrderBean = new PlaceOrderBean();
                placeOrderBean.setPid(c1.getInt(c1.getColumnIndex("Pid")));
                placeOrderBean.setXml1(c1.getString(c1.getColumnIndex("xml1")));
                placeOrderBean.setXml2(c1.getString(c1.getColumnIndex("xml2")));
                placeOrderBeanList.add(placeOrderBean);
            } while (c1.moveToNext());
        } else {
         //   db.close();
          //  c1.close();
        }

        if (NetworkUtils.isNetworkAvailable(MarchantService.this)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                if (placeOrderBeanList.size() > 0) {
                    for (int i = 0; i < placeOrderBeanList.size(); i++) {
                        String xml1 = placeOrderBeanList.get(i).getXml1();
                        String xml2 = placeOrderBeanList.get(i).getXml2();

                        String id = String.valueOf(placeOrderBeanList.get(i).getPid());
                        new PlaceOrder().execute(xml1, xml2, id);
                    }
                }

            } else {
                new GetSessionId().execute();
            }
        }
    }

    class PlaceOrder extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;
        String resp_placeOrder = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

       @Override
       protected String doInBackground(String... params) {
           String url_placeOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_BILL_DETAILS +
                   "?handler="+ AnyMartData.HANDLE +
                   "&sessionid="+ AnyMartData.SESSION_ID +
                   "&xml1="+ params[0]+
                   "&xml2="+params[1];
           /*url_generateOrder =AnyMartData.MAIN_URL + AnyMartData.METHOD_GENERATE_ORDER +
                        "?handler="+AnyMartData.HANDLE +
                        "&sessionid="+AnyMartData.SESSION_ID +
                        "&Scheduledate="+ URLEncoder.encode(params[0],"UTF-8") +
                        "&Arr="+  URLEncoder.encode(JsonMain.toString(),"UTF-8");*/
           String url = url_placeOrder;
           URLConnection urlConnection = null;
           BufferedReader bufferedReader = null;

           try {
               res = Utility.OpenconnectionOrferbilling(url_placeOrder, parent);
               int a = res.getBytes().length;
               res = res.replaceAll("\\\\", "");
               // res = res.replaceAll("\"", "");
               // res = res.replaceAll(" ", "");
               responseString = res.toString().replaceAll("^\"|\"$", "");
               Log.e("Response", responseString);
              /* URL urlPlaceOrder = new URL(url_placeOrder);

               URL placeOrder = urlPlaceOrder;
               urlConnection = urlPlaceOrder.openConnection();
               urlConnection.setRequestProperty("Content-Type", "application/json");
               urlConnection.setRequestProperty("Accept", "JSON");
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

               StringBuffer stringBuff_placeOrder = new StringBuffer();
               String line;
               StringBuilder str_build_placeOrder = new StringBuilder();
               while((line = bufferedReader.readLine())!=null){
                   stringBuff_placeOrder = stringBuff_placeOrder.append(line);
               }
               responseString = stringBuff_placeOrder.toString().replaceAll("^\"|\"$", "");*/
               resp_placeOrder = responseString.replaceAll("\\\\","");

               Log.e("Response", resp_placeOrder);

           } catch (Exception e) {
               resp_placeOrder = "error";
               e.printStackTrace();
           }
           return resp_placeOrder;
       }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);


            if (resp_placeOrder.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(MarchantService.this)) {
                    new GetSessionId().execute();
                }
            } else if (resp_placeOrder.equalsIgnoreCase("error")) {
                /*Toast.makeText(MarchantService.this, "The server is taking too long to respond OR something" +
                        " is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();*/

                Toast.makeText(MarchantService.this, "Internet connection is slow.", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(MarchantService.this, "Order Placed Successfully",
                        Toast.LENGTH_LONG).show();
                String id[] = result.split(",");
            //    DatabaseHandler db1 = new DatabaseHandler(MarchantService.this);
            //    SQLiteDatabase db = db1.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("isUploaded", "Yes");
                sql_db.update(dbhandler.TABLE_BILL_DETAILS, values, "Pid=?",
                        new String[]{id[1]});
                SendBillDetails();

            }
        }
    }

    private void confirmOrderData() {
        OrderBeanList.clear();
    //    DatabaseHelper db1 = new DatabaseHelper(MarchantService.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);
     //   SQLiteDatabase db = db1.getWritableDatabase();
        String que = "SELECT DISTINCT SoHeaderId,SODate FROM " + dbhandler.TABLE_MY_ORDER +
                " WHERE OrderStatus='Confirm' AND isUploaded='N'";
        Cursor cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                SoHeaderId = cur.getString(cur.getColumnIndex("SoHeaderId"));
                SOdate = cur.getString(cur.getColumnIndex("SODate"));

                if (isInternetPresent) {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        new AsyncTaskConfirmOrder().execute();
                    } else {
                        new GetSessionId().execute();
                        while ((AnyMartData.SESSION_ID == null)
                                || (AnyMartData.HANDLE == null)) {

                        }
                        new AsyncTaskConfirmOrder().execute();
                    }
                }
            } while (cur.moveToNext());
        }
    }

    private void RejectedOrderData() {
        OrderBeanList.clear();
      //  DatabaseHelper db1 = new DatabaseHelper(MarchantService.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);
      //  SQLiteDatabase db = db1.getWritableDatabase();
        String que = "SELECT DISTINCT SoHeaderId,sodetailid FROM " + dbhandler.TABLE_MY_ORDER +
                " WHERE OrderStatus='Rejected' AND isUploaded='N'";
        Cursor cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                rejSoHeaderId = cur.getString(cur.getColumnIndex("SoHeaderId"));
                rejSoDetailId = cur.getString(cur.getColumnIndex("sodetailid"));

                if (isInternetPresent) {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        new AsyncTaskRejectedOrder().execute();
                    } else {
                       /* new StartSession(MarchantService.this, new com.vritti.orderbilling.interfaces.CallbackInterface() {

                            @Override
                            public void callMethod() {
                                new AsyncTaskRejectedOrder().execute();
                            }
                        });*/
                        new GetSessionId().execute();
                        while ((AnyMartData.SESSION_ID == null)
                                || (AnyMartData.HANDLE == null)) {

                        }
                        new AsyncTaskRejectedOrder().execute();
                    }

                }
            } while (cur.moveToNext());
        }
    }

    public void getTradeDiscount() {
     //   SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cur;
        String que = "SELECT * FROM " + dbhandler.TABLE_TRADE_DISCOUNT + " WHERE IsUploaded='N'";
        cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                fromdate = cur.getString(cur.getColumnIndex("validfrom"));
                toDate = cur.getString(cur.getColumnIndex("validto"));
                freeitem_qnty = cur.getString(cur.getColumnIndex("Freeitemqty"));
                freeitemid = cur.getString(cur.getColumnIndex("Freeitemid"));
                item_mrp = cur.getString(cur.getColumnIndex("MRP"));
                item_name = cur.getString(cur.getColumnIndex("VendorItemname"));
                item_qnty = cur.getString(cur.getColumnIndex("Minqty"));
                itemcode = cur.getString(cur.getColumnIndex("FKVendorItemMasterId"));
                discP = cur.getString(cur.getColumnIndex("Discratepercent"));
                discA = cur.getString(cur.getColumnIndex("DiscrateMRP"));
                result = cur.getString(cur.getColumnIndex("NetRate"));
                if (isInternetPresent) {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        new TradeDiscount().execute();
                    } else {
                        /*new StartSession(MarchantService.this, new com.vritti.orderbilling.interfaces.CallbackInterface() {

                            @Override
                            public void callMethod() {
                                new TradeDiscount().execute();
                            }
                        });*/
                        new GetSessionId().execute();
                        while ((AnyMartData.SESSION_ID == null)
                                || (AnyMartData.HANDLE == null)) {

                        }
                        new TradeDiscount().execute();
                    }
                }
            } while ((cur.moveToNext()));
        }
    }

    public void getVolumeDiscount() {
     //   SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cur;
        String que = "SELECT * FROM " + dbhandler.TABLE_VOLUME_DISCOUNT + " WHERE IsUploaded='N'";
        cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                fromdate = cur.getString(cur.getColumnIndex("ValidFrom"));
                toDate = cur.getString(cur.getColumnIndex("Validto"));
                freeitem_qnty = cur.getString(cur.getColumnIndex("Freeitemqty"));
                freeitemid = cur.getString(cur.getColumnIndex("Freeitemid"));
                item_qnty = cur.getString(cur.getColumnIndex("Minqty"));
                itemcode = cur.getString(cur.getColumnIndex("FKVendorItemMasterId"));
                discP = cur.getString(cur.getColumnIndex("Discratepercent"));
                discA = cur.getString(cur.getColumnIndex("disrateMRP"));
                result = cur.getString(cur.getColumnIndex("NetRate"));

                if (isInternetPresent) {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        new VolumeDiscount().execute();
                    } else {
                        /*new StartSession(MarchantService.this, new com.vritti.orderbilling.interfaces.CallbackInterface() {

                            @Override
                            public void callMethod() {
                                new VolumeDiscount().execute();
                            }
                        });*/
                        new GetSessionId().execute();
                        while ((AnyMartData.SESSION_ID == null)
                                || (AnyMartData.HANDLE == null)) {

                        }
                        new VolumeDiscount().execute();
                    }

                }
            } while (cur.moveToNext());
        }
    }

    class VolumeDiscount extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;
        String resp_voldisc = null;

        @Override
        protected Void doInBackground(Void... params) {
            String url_volumeDiscount = AnyMartData.MAIN_URL +
                    AnyMartData.METHOD_INSERT_VOLUME_DISCOUNT +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&FKVendorProductmasterId="+ "" +
                    "&FKVendorId="+userid +
                    "&FKVendorItemMasterId="+ "" +
                    "&VendorItemname="+ "" +
                    "&TypeFixedPercent="+ "" +
                    "&MRP="+ "" +
                    "&DisRate="+ "" +
                    "&NetRate="+ result +
                    "&CouponId="+ "" +
                    "&Freeitemid="+ freeitemid +
                    "&Freeitemqty="+ freeitem_qnty +
                    "&Minqty="+ item_qnty +
                    "&ValidFrom="+ fromdate +
                    "&Validto="+ toDate +
                    "&Discratepercent="+ discP +
                    "&DiscrateMRP="+""+
                    "&Minvalue="+Minvalue +
                    "&discratepercent="+""+
                    "&disrateMRP="+discA+
                    "&CouponCode="+""+
                    "&DisratePercentCoupon="+""+
                    "&DisrateMRPCoupon="+""+
                    "&ValidFromCoupon="+""+
                    "&ValidToCoupon="+""+
                    "&DisTypeCoupon="+""+
                    "&Couponqty="+""+
                    "&Couponitemasterid="+"";

            String url= url_volumeDiscount;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_volumeDiscount, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL urlVolDiscount = new URL(url_volumeDiscount);

                urlConnection = urlVolDiscount.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_volDisc = new StringBuffer();
                String line;
                StringBuilder str_build_placeOrder = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_volDisc = stringBuff_volDisc.append(line);
                }
                responseString = stringBuff_volDisc.toString().replaceAll("^\"|\"$", "");*/
                resp_voldisc = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_voldisc);

            } catch (Exception e) {
                resp_voldisc = "error";
                e.printStackTrace();
            }
            return null;
        }
    }

    class TradeDiscount extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;
        String resp_tradeDisc = null;

        @Override
        protected Void doInBackground(Void... params) {
            String url_tradeDiscount = AnyMartData.MAIN_URL +
                    AnyMartData.METHOD_INSERT_TRADE_DISCOUNT +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&FKVendorProductmasterId="+ "" +
                    "&FKVendorId="+userid +
                    "&FKVendorItemMasterId="+ itemcode +
                    "&VendorItemname="+ item_name +
                    "&TypeFixedPercent="+ "" +
                    "&MRP="+ item_mrp +
                    "&DisRate="+ "0" +
                    "&NetRate="+ result +
                    "&CouponId="+ "0" +
                    "&Freeitemid="+ freeitemid +
                    "&Freeitemqty="+ freeitem_qnty +
                    "&Minqty="+ item_qnty +
                    "&ValidFrom="+ fromdate +
                    "&Validto="+ toDate +
                    "&Discratepercent="+ discP +
                    "&DiscrateMRP="+ discA +
                    "&Minvalue="+"0";

            String url = url_tradeDiscount;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_tradeDiscount, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL urlTradeDiscount = new URL(url_tradeDiscount);

                urlConnection = urlTradeDiscount.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_tradeDisc = new StringBuffer();
                String line;
                StringBuilder str_build_placeOrder = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_tradeDisc = stringBuff_tradeDisc.append(line);
                }
                responseString = stringBuff_tradeDisc.toString().replaceAll("^\"|\"$", "");*/
                resp_tradeDisc = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_tradeDisc);

            } catch (Exception e) {
                resp_tradeDisc = "error";
                e.printStackTrace();
            }
            return null;
        }
    }

    public void getAddMRP_runi() {
    //    SQLiteDatabase db = dbHandler.getWritableDatabase();

        sb.setLength(0);
        jsonArray_merchantsb_runi = new JSONArray();

        /*String que = "SELECT * FROM " + dbHandler.TABLE_SEND_MRP + " WHERE isuploaded='No'";
        cur = db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String xml = cur.getString(cur.getColumnIndex("xml"));
                String userid = cur.getString(cur.getColumnIndex("userid"));

                new SendMRPToServer_runi().execute(xml, userid);

           } while (cur.moveToNext());
        }*/

        ///////////////////////////////////////////////////////////

        String que_runi = "SELECT * FROM " + dbhandler.TABLE_ITEM_MRP_Runi +
                " WHERE IsUploaded='N'";
        Cursor cur;

        Cursor cur_runi = sql_db.rawQuery(que_runi,null);
            if(cur_runi.getCount()>0){
            cur_runi.moveToFirst();
            do{

                try {
                    JSONObject json_runi = new JSONObject();

                    json_runi.put("Itemid",cur_runi.getString(cur_runi.getColumnIndex("Itemid")));
                    json_runi.put("Itemname",cur_runi.getString(cur_runi.getColumnIndex("Itemname")));
                    json_runi.put("OMrpV",cur_runi.getString(cur_runi.getColumnIndex("OMrpV")));

                    //String pmrp = cur_runi.getString(cur_runi.getColumnIndex("PurchaseMRP"));

                   // json_runi.put("PurchaseMRP", "0");

                    json_runi.put("PurchaseMRP",cur_runi.getString(cur_runi.getColumnIndex("PurchaseMRP")));
                    json_runi.put("NMrpV",cur_runi.getString(cur_runi.getColumnIndex("NMrpV")));
                    json_runi.put("QtyV",cur_runi.getString(cur_runi.getColumnIndex("QtyV")));
                    //json_runi.put("PurchaseUnit",cur_runi.getString(cur_runi.getColumnIndex("PurchaseUnit")));
                    json_runi.put("PurchaseUnit"," kg");
                    json_runi.put("UnitV",cur_runi.getString(cur_runi.getColumnIndex("UnitV")));
                   // json_runi.put("pkpurchaseid",cur_runi.getString(cur_runi.getColumnIndex("pkpurchaseid")));
                    json_runi.put("PurchaseQty","0");
                    json_runi.put("pkpurchaseid","0");

                    jsonArray_merchantsb_runi.put(json_runi);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cur_runi.moveToNext());
            cur_runi.close();
        }

        xml = jsonArray_merchantsb_runi.toString();
        try {
            jsonObject_merchantsb_runi= new JSONObject();
            jsonObject_merchantsb_runi.put("HeaderData","" );
            jsonObject_merchantsb_runi.put("ItemData", jsonArray_merchantsb_runi);
            AnyMartData.JMain = jsonObject_merchantsb_runi;
        }catch( JSONException e){
            e.printStackTrace();
        }

       // String xml = cur_runi.getString(cur_runi.getColumnIndex("xml"));
      //  String userid = cur_runi.getString(cur_runi.getColumnIndex("userid"));

        new SendMRPToServer_runi().execute(xml, userid);
        ///////////////////////////////////////////////////////////
    }

    public void getAddMRP() throws JSONException {
    //    SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cur;
        sb.setLength(0);
        //sb.append("<Header>");
        jsonArray_merchantsb = new JSONArray();

        String que = "SELECT * FROM " + dbhandler.TABLE_ITEM_MRP + " WHERE IsUploaded='N'";
        cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                /*sb.append("<Table>");
                sb.append("<categoryid>" + cur.getString(cur.getColumnIndex("categoryid")) + "</categoryid>");
                sb.append("<subcategoryid>" + cur.getString(cur.getColumnIndex("Subcategoryid")) + "</subcategoryid>");
                sb.append("<categoryname>" + cur.getString(cur.getColumnIndex("categoryname")) + "</categoryname>");
                sb.append("<subcategoryname>" + cur.getString(cur.getColumnIndex("subcategoryname")) + "</subcategoryname>");
                sb.append("<itemmasterid>" + cur.getString(cur.getColumnIndex("itemmasterid")) + "</itemmasterid>");
                sb.append("<itemname>" + cur.getString(cur.getColumnIndex("itemname")) + "</itemname>");
                sb.append("<ItemMRP>" + cur.getString(cur.getColumnIndex("ItemMRP")) + "</ItemMRP>");

                sb.append("</Table>");*/

                JSONObject json_sb_merchant = new JSONObject();

                try {
                    json_sb_merchant.put("categoryid",cur.getString(cur.getColumnIndex("categoryid")));
                    json_sb_merchant.put("subcategoryid",cur.getString(cur.getColumnIndex("Subcategoryid")));
                    json_sb_merchant.put("categoryname",cur.getString(cur.getColumnIndex("categoryname")));
                    json_sb_merchant.put("subcategoryname",cur.getString(cur.getColumnIndex("subcategoryname")));
                    json_sb_merchant.put("itemmasterid",cur.getString(cur.getColumnIndex("itemmasterid")));
                    json_sb_merchant.put("itemname",cur.getString(cur.getColumnIndex("itemname")));
                    json_sb_merchant.put("ItemMRP",cur.getString(cur.getColumnIndex("ItemMRP")));
                    jsonArray_merchantsb.put(json_sb_merchant);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } while (cur.moveToNext());
        }
        //sb.append("</Header>");
        //xml = sb.toString();
        xml = jsonArray_merchantsb.toString();
        try {
            jsonObject_merchantsb= new JSONObject();
            jsonObject_merchantsb.put("HeaderData","" );
            jsonObject_merchantsb.put("ItemData", jsonArray_merchantsb);
            AnyMartData.JMain = jsonObject_merchantsb;
        }catch( JSONException e){
            e.printStackTrace();
        }

        if (isInternetPresent) {

            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new SendMRPToServer().execute();
            } else {
                /*new StartSession(MarchantService.this, new com.vritti.orderbilling.interfaces.CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new SendMRPToServer().execute();
                    }
                });*/
                new GetSessionId().execute();
                while ((AnyMartData.SESSION_ID == null)
                        || (AnyMartData.HANDLE == null)) {

                }
                new SendMRPToServer().execute();
            }

        }
    }

    class AsyncTaskConfirmOrder extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            setConfirmOrder();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           /* if (resp.equalsIgnoreCase("Done")) {*/
         //   SQLiteDatabase sql = databaseHelper.getWritableDatabase();
            String que = "UPDATE " + dbhandler.TABLE_MY_ORDER + " SET  isUploaded='Y' WHERE SoHeaderId='" + SoHeaderId + "'";
            Cursor cur = sql_db.rawQuery(que, null);
            int c = cur.getCount();
        }
        /*}*/

    }

    class SendMRPToServer extends AsyncTask<Void, Void, Void> {
        String responseString = "";
        String resp_sendMRP = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressBar();
            //showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_sendMRPToServer = AnyMartData.MAIN_URL + AnyMartData.METHOD_INSERT_ITEM_MRP_VENDOR+
                    "?sessionid="+ AnyMartData.SESSION_ID +
                    "&handler="+ AnyMartData.HANDLE +
                    //"&xml="+jsonObject_merchantsb.toString()+"&vendorid="+userid;
            "&xml="+URLEncoder.encode(jsonObject_merchantsb.toString())+"&vendorid="+userid;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_sendMRPToServer, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
               /* URL urlTradeDiscount = new URL(url_sendMRPToServer);

                urlConnection = urlTradeDiscount.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_sndMRP = new StringBuffer();
                String line;
                StringBuilder str_build_sndMRP = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_sndMRP = stringBuff_sndMRP.append(line);
                }
                responseString = stringBuff_sndMRP.toString().replaceAll("^\"|\"$", "");*/
                resp_sendMRP = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_sendMRP);
            } catch (Exception e) {
                resp_sendMRP = "error";
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //     dismissProgressDialog();
            if (resp_sendMRP.equalsIgnoreCase("Inserted Items")) {
                Toast.makeText(getApplicationContext(), "Added successfully..", Toast.LENGTH_LONG).show();
            //   SQLiteDatabase sql = dbHandler.getWritableDatabase();
                //ParseXML();
                json = resp_sendMRP;
                parseJson_sendMRP(json);

            } else {
                Toast.makeText(getApplicationContext(), "Error has occured..", Toast.LENGTH_LONG).show();
            }
          /*  Intent intent = new Intent(AddMRPMainActivity.this, MainActivity.class);
            startActivity(intent);
            finish();*/
        }
    }

    protected void parseJson_sendMRP(String json) {
    //   SQLiteDatabase db = dbHandler.getWritableDatabase();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setAmt(jsonArray.getJSONObject(i).getString(
                        "subcategoryid"));
                bean.setItemId(jsonArray.getJSONObject(i).getString(
                        "itemmasterid"));
                bean.setItemName(jsonArray.getJSONObject(i).getString(
                        "Subcategoryid"));
                bean.setMRP(jsonArray.getJSONObject(i).getString(
                        "IsUploaded"));
                /*bean.setIsUploaded(jsonArray.getJSONObject(i).getString(
                        "IsUploaded"));*/

                tcf.addMarchantPO(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ParseXML() {
        try {
            ContentValues values = new ContentValues();
            NodeList nl = getnode(xml, "Table");
            String SubCatId = null, itemmasterid = null;
            String columnName, columnValue, CalName = null;
        //    SQLiteDatabase db = dbHandler.getWritableDatabase();
            Cursor cur;
            String que = "SELECT * FROM " + dbhandler.TABLE_ITEM_MRP;
            cur = sql_db.rawQuery(que, null);
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                for (int j = 0; j < cur.getColumnCount(); j++) {

                    columnName = cur.getColumnName(j);
                    CalName = columnName;
                    if (columnName.equalsIgnoreCase("Subcategoryid")) {
                        CalName = "subcategoryid";
                    }
                    columnValue = getValue(e, CalName);
                    if (columnName.equalsIgnoreCase("itemmasterid")) {
                        itemmasterid = columnValue;
                    }//subcategoryid
                    else if (columnName.equalsIgnoreCase("Subcategoryid")) {
                        SubCatId = columnValue;
                    } else if (columnName.equalsIgnoreCase("IsUploaded")) {
                        columnValue = "Y";
                    }
                    values.put(columnName, columnValue);
                }

                sql_db.update(dbhandler.TABLE_ITEM_MRP,
                        values, "Subcategoryid=? and itemmasterid=?",
                        new String[]{SubCatId, itemmasterid});
            }
        } catch (Exception e) {

        }
    }


    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public NodeList getnode(String xml, String Tag) {


        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);


        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        NodeList nl = doc.getElementsByTagName(Tag);
        Log.e("get node", " nl: " + nl);
        Log.e("get node", " nl len: " + nl.getLength());
        return nl;
    }

    class AsyncTaskRejectedOrder extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            getRejectedOrder();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            /*if (resp.equalsIgnoreCase("Done")) {

            }*/
         //   SQLiteDatabase sql = databaseHelper.getWritableDatabase();
            String que = "UPDATE " + dbhandler.TABLE_MY_ORDER + " SET  isUploaded='Y' WHERE SoHeaderId='" + SoHeaderId + "'";
            Cursor cur = sql_db.rawQuery(que, null);
            int c = cur.getCount();
        }

    }

    /*public void getRejectedOrder() {
        // TODO Auto-generated method stub
        try {

            SoapObject request = new SoapObject(AnyMartKukadiAgencyData.NAMESPACE,
                    AnyMartKukadiAgencyData.METHOD_REJECT_ORDER);
            PropertyInfo propInfo = new PropertyInfo();
            propInfo.type = PropertyInfo.STRING_CLASS;
            // adding parameters
            request.addProperty("SOHeaderId", rejSoHeaderId);
            request.addProperty("sodetailid", rejSoDetailId);
            request.addProperty("handler", AnyMartData.HANDLE);
            request.addProperty("sessionid", AnyMartData.SESSION_ID);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    AnyMartKukadiAgencyData.URL);
            androidHttpTransport.call(
                    AnyMartKukadiAgencyData.SOAP_ACTION_REJECT_ORDER, envelope);

            SoapObject response = (SoapObject) envelope.bodyIn;
            resp = response.getProperty(0).toString();

            System.out.println("responce from service -->>" + resp);
            Log.d("test", "resp :=" + resp);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
*/
    public void getRejectedOrder() {
        String responseString = null;
        String resp_rejectOrder = null;

        String url_rejectedOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_REJECT_ORDER +
                "?SOHeaderId="+rejSoHeaderId + "&sodetailid="+rejSoDetailId +
                "&handler="+ AnyMartData.HANDLE + "&sessionid="+ AnyMartData.SESSION_ID;

        URLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        // TODO Auto-generated method stub
        try {

            URL urlRejectedOrder = new URL(url_rejectedOrder);
            urlConnection = urlRejectedOrder.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "JSON");
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

            StringBuffer stringBuff_rejectOrder = new StringBuffer();
            String line;
            StringBuilder str_build_rejectOrder = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                stringBuff_rejectOrder = stringBuff_rejectOrder.append(line);
            }
            responseString = stringBuff_rejectOrder.toString().replaceAll("^\"|\"$", "");
            resp_rejectOrder = responseString.replaceAll("\\\\","");

            System.out.println("responce from service -->>" + resp_rejectOrder);
            Log.d("test", "resp :=" + resp_rejectOrder);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getConfirmDate(String d) {
        String toDate = null;
        Date input = null;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy hh:mmaa");
            input = formatter.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat toFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        String output = toFormat.format(input);
        String date = d;


        return output;

    }

   /* public void setConfirmOrder() {
        // TODO Auto-generated method stub
        try {
            String df = getConfirmDate(SOdate);
            DatabaseHandler dbhandler = new DatabaseHandler(MarchantService.this);
            SoapObject request = new SoapObject(AnyMartKukadiAgencyData.NAMESPACE,
                    AnyMartKukadiAgencyData.METHOD_CONFIRM_ORDER);
            PropertyInfo propInfo = new PropertyInfo();
            propInfo.type = PropertyInfo.STRING_CLASS;
            // adding parameters
            request.addProperty("SOHeaderId", SoHeaderId);
            request.addProperty("handler", AnyMartData.HANDLE);
            request.addProperty("sessionid", AnyMartData.SESSION_ID);
            request.addProperty("date", df + ":00.000");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    AnyMartKukadiAgencyData.URL);
            androidHttpTransport.call(
                    AnyMartKukadiAgencyData.SOAP_ACTION_CONFIRM_ORDER, envelope);

            SoapObject response = (SoapObject) envelope.bodyIn;
            resp = response.getProperty(0).toString();

            System.out.println("responce from service -->>" + resp);
            Log.d("test", "resp :=" + resp);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }*/

    public void setConfirmOrder() {
        String responseString = null;
        String resp_cnfrmOrder = null;
        String df = getConfirmDate(SOdate);
     //   DatabaseHandler dbhandler = new DatabaseHandler(MarchantService.this);

        String url_vendor_ConfirmOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_CONFIRM_ORDER +
                "?SOHeaderId="+SoHeaderId + "&handler="+ AnyMartData.HANDLE +
                "&sessionid="+ AnyMartData.SESSION_ID + "&date="+df+":00.000";

        URLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        // TODO Auto-generated method stub
        try {
            res = Utility.OpenconnectionOrferbilling(url_vendor_ConfirmOrder, parent);
            int a = res.getBytes().length;
            res = res.replaceAll("\\\\", "");
            // res = res.replaceAll("\"", "");
            // res = res.replaceAll(" ", "");
            responseString = res.toString().replaceAll("^\"|\"$", "");
            Log.e("Response", responseString);

           /* URL urlConfirmOrder = new URL(url_vendor_ConfirmOrder);
            urlConnection = urlConfirmOrder.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "JSON");
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

            StringBuffer stringBuff_cnfrmOrder = new StringBuffer();
            String line;
            StringBuilder str_build_cnfrmOrder = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                stringBuff_cnfrmOrder = stringBuff_cnfrmOrder.append(line);
            }
            responseString = stringBuff_cnfrmOrder.toString().replaceAll("^\"|\"$", "");*/
            resp_cnfrmOrder = responseString.replaceAll("\\\\","");

            Log.e("Response", resp_cnfrmOrder);

            // DatabaseHandler dbhandler = new DatabaseHandler(MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class SendMRPToServer_runi extends AsyncTask<String, Void, String> {
        String responseString = null;
        String resp_SendMRPToServer_runi = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressBar();
            //showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url_sendMRPToserver_runi = AnyMartData.MAIN_URL +
                    AnyMartData.METHOD_INSERT_ITEM_MRP_RUNI_VENDOR +
                    "?sessionid="+ AnyMartData.SESSION_ID +
                    "&handler="+ AnyMartData.HANDLE +
                    "&xml="+URLEncoder.encode(jsonObject_merchantsb_runi.toString())+ "&vendorid="+params[1];
                    //"&xml="+jsonObject_merchantsb_runi.toString().replaceAll("\\\\","")+ "&vendorid="+params[1];
                    //"&xml="+jsonObject_merchantsb_runi.toString().replaceAll("\\\\","") + "&vendorid="+params[1];
            //"&xml="+ URLEncoder.encode(jsonObject_merchantsb_runi.toString()) + "&vendorid="+params[1];

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_sendMRPToserver_runi, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL urlsendMRPToserverRuni = new URL(url_sendMRPToserver_runi);
                urlConnection = urlsendMRPToserverRuni.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_sendMRPToServer = new StringBuffer();
                String line;
                StringBuilder str_build_sendMRPToServer = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_sendMRPToServer = stringBuff_sendMRPToServer.append(line);
                }
                responseString = stringBuff_sendMRPToServer.toString().replaceAll("^\"|\"$", "");*/
                resp_SendMRPToServer_runi = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_SendMRPToServer_runi);

            } catch (Exception e) {
                resp_SendMRPToServer_runi = "error";
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dismissProgressDialog();
            if (resp_SendMRPToServer_runi.equalsIgnoreCase("Inserted Items")) {
                Toast.makeText(getApplicationContext(), "Added successfully..", Toast.LENGTH_LONG).show();

                json = resp_SendMRPToServer_runi;
               // ParseXML_runi(json);
                parseJson_sendMRP_runi(json);

            } else {
                Toast.makeText(getApplicationContext(), "Error has occured..", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void parseJson_sendMRP_runi(String json) {
     //   SQLiteDatabase db = dbHandler.getWritableDatabase();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setItemId(jsonArray.getJSONObject(i).getString("Itemid"));
                bean.setItemName(jsonArray.getJSONObject(i).getString("Itemname"));
                bean.setPurchaseID(jsonArray.getJSONObject(i).getString("pkpurchaseid"));
                bean.setMRP(jsonArray.getJSONObject(i).getString("NMrpV"));
                bean.setOldMRP(jsonArray.getJSONObject(i).getString("OMrpV"));
                bean.setPOUnit(jsonArray.getJSONObject(i).getString("PurchaseUnit"));
                bean.setPOMRP(jsonArray.getJSONObject(i).getString("PurchaseMRP"));
                bean.setQty(jsonArray.getJSONObject(i).getString("QtyV"));
                bean.setUnit(jsonArray.getJSONObject(i).getString("UnitV"));
                bean.setIsUploaded(jsonArray.getJSONObject(i).getString(
                        "IsUploaded"));

                tcf.addMarchantPO(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void ParseXML_runi() {

        try {

            ContentValues values = new ContentValues();
            NodeList nl = getnode(xml, "Table");
            String SubCatId = null, itemmasterid = null;
            String columnName, columnValue, CalName = null, MRP = null, ItemName = null;

            Cursor cur;
            String que = "SELECT * FROM " + dbHandler.TABLE_ITEM_MRP_Runi;
            SQLiteDatabase db1 = dbHandler.getWritableDatabase();
            cur = db1.rawQuery(que, null);
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                for (int j = 0; j < cur.getColumnCount(); j++) {

                    columnName = cur.getColumnName(j);
                    CalName = columnName;
                    if (columnName.equalsIgnoreCase("Subcategoryid")) {
                        CalName = "subcategoryid";
                    }
                    columnValue = getValue(e, CalName);
                    if (columnName.equalsIgnoreCase("itemmasterid")) {
                        itemmasterid = columnValue;
                    }//subcategoryid
                    else if (columnName.equalsIgnoreCase("Subcategoryid")) {
                        SubCatId = columnValue;
                    } else if (columnName.equalsIgnoreCase("IsUploaded")) {
                        columnValue = "Y";
                    } else if (columnName.equalsIgnoreCase("ItemMRP")) {
                        MRP = columnValue;
                    } else if (columnName.equalsIgnoreCase("itemname")) {
                        ItemName = columnValue;
                    }
                    values.put(columnName, columnValue);


                }


              *//*  ContentValues values1 = new ContentValues();
                values1.put("Product_name", ItemName);
                values1.put("price", MRP);
                String que1 = "SELECT * FROM " + dbHandler.TABLE_PRODUCT_CB +
                        " WHERE Product_id='" + itemmasterid + "'";
                Cursor cur1 = db1.rawQuery(que1, null);
                if (cur1.getCount() > 0) {
                    db1.update(dbHandler.TABLE_PRODUCT_CB,
                            values1, "Product_id=?",
                            new String[]{itemmasterid});

                } else {
                    MarchantItemBean bean = new MarchantItemBean();
                    bean.setPrice(MRP);
                    bean.setItemname(ItemName);
                    bean.setFKitemmasterid(itemmasterid);
                    dbHandler.addMarchantItemCB(bean);

                }
*//*

                db1.update(dbHandler.TABLE_ITEM_MRP_Runi,
                        values, "Subcategoryid=? and itemmasterid=?",
                        new String[]{SubCatId, itemmasterid});

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }*/

    protected void ParseXML_runi(String json) {
        Cursor cur;
        //String Itemid = null, Itemname = null;
        String que = "SELECT * FROM " + dbhandler.TABLE_ITEM_MRP_Runi;
    //    SQLiteDatabase db1 = dbHandler.getWritableDatabase();
        cur = sql_db.rawQuery(que, null);

        tcf.deleteItemsMRP_Runi();
       // additemBeanArrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setItemName(jsonArray.getJSONObject(i).getString(
                        "Itemname"));
                bean.setItemId(jsonArray.getJSONObject(i).getString(
                        "Itemid"));
                bean.setOldMRP(jsonArray.getJSONObject(i).getString(
                        "OMrpV"));
                bean.setPOMRP(jsonArray.getJSONObject(i).getString(
                        "PurchaseMRP"));
                bean.setMRP(jsonArray.getJSONObject(i).getString(
                        "NMrpV"));
                bean.setQty(jsonArray.getJSONObject(i).getString(
                        "QtyV"));
                bean.setPOUnit(jsonArray.getJSONObject(i).getString(
                        "PurchaseUnit"));
                bean.setUnit(jsonArray.getJSONObject(i).getString(
                        "UnitV"));
                bean.setPurchaseID(jsonArray.getJSONObject(i).getString(
                        "pkpurchaseid"));

                tcf.addMarchantPO(bean);

          /*  dbHandler.addItemMRP_Runi(jsonArray.getJSONObject(i).getString("Itemname"),
                    jsonArray.getJSONObject(i).getString("Itemid"),
                    jsonArray.getJSONObject(i).getString("OMrpV"),
                    jsonArray.getJSONObject(i).getString("PurchaseMRP"),
                    jsonArray.getJSONObject(i).getString("NMrpV"),
                    jsonArray.getJSONObject(i).getString("QtyV"),
                    jsonArray.getJSONObject(i).getString("PurchaseUnit"),
                    jsonArray.getJSONObject(i).getString("UnitV"),
                    jsonArray.getJSONObject(i).getString("pkpurchaseid"));*/

                /*db1.update(dbHandler.TABLE_ITEM_MRP_Runi,
                        values, "Subcategoryid=? and itemmasterid=?",
                        new String[]{SubCatId, itemmasterid});*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class GetProductList extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_GetProductList = null;

        @Override
        protected Void doInBackground(Void... params) {
            String url_getProductList = AnyMartData.MAIN_URL + AnyMartData.METHOD_ITEMS_FOR_VENDOR +
                    "?vendorid="+userid + "&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID ;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                res = Utility.OpenconnectionOrferbilling(url_getProductList, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL urlGetProductList = new URL(url_getProductList);
                urlConnection = urlGetProductList.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_GetProductList = new StringBuffer();
                String line;
                StringBuilder str_build_getProductList = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_GetProductList = stringBuff_GetProductList.append(line);
                }
                responseString = stringBuff_GetProductList.toString().replaceAll("^\"|\"$", "");*/
                resp_GetProductList = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_GetProductList);
            } catch (Exception e) {
                resp_GetProductList = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (resp_GetProductList.equalsIgnoreCase("Session Expired")) {
                if (cd.isConnectingToInternet()) {
                    new StartSession_tbuds(MarchantService.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetProductList().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (resp_GetProductList.equalsIgnoreCase("error")) {

            } else {
                json = resp_GetProductList;
                tcf.deleteMarchantItems();
                parseJson(json);
            }
        }
    }

    class GetMRPFromServer extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_GetMRPFromServer = null;

       @Override
       protected Void doInBackground(Void... params) {
           String url_GetMRPFromServer = AnyMartData.MAIN_URL + AnyMartData.METHOD_ITEMS_MRP_Runi +
                   "?handler="+ AnyMartData.HANDLE + "&sessionid="+ AnyMartData.SESSION_ID +
                   "&vendorid="+userid;

           URLConnection urlConnection = null;
           BufferedReader bufferedReader = null;

           try {
               res = Utility.OpenconnectionOrferbilling(url_GetMRPFromServer, parent);
               int a = res.getBytes().length;
               res = res.replaceAll("\\\\", "");
               // res = res.replaceAll("\"", "");
               // res = res.replaceAll(" ", "");
               responseString = res.toString().replaceAll("^\"|\"$", "");
               Log.e("Response", responseString);
               /*URL urlGetMRPFromServer = new URL(url_GetMRPFromServer);
               urlConnection = urlGetMRPFromServer.openConnection();
               urlConnection.setRequestProperty("Content-Type", "application/json");
               urlConnection.setRequestProperty("Accept", "JSON");
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

               StringBuffer stringBuff_GetMRPFromServer = new StringBuffer();
               String line;
               StringBuilder str_build_GetMRPFromServer = new StringBuilder();
               while((line = bufferedReader.readLine())!=null){
                   stringBuff_GetMRPFromServer = stringBuff_GetMRPFromServer.append(line);
               }
               responseString = stringBuff_GetMRPFromServer.toString().replaceAll("^\"|\"$", "");*/
               resp_GetMRPFromServer = responseString.replaceAll("\\\\","");

               Log.e("Response", resp_GetMRPFromServer);
           } catch (Exception e) {
               resp_GetMRPFromServer = "error";
               e.printStackTrace();
           }
           return null;
       }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (responseString.equalsIgnoreCase("Session Expired")) {
                if (cd.isConnectingToInternet()) {
                    new StartSession_tbuds(MarchantService.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetMRPFromServer().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (responseString.equalsIgnoreCase("error")) {

            } else {
                json = resp_GetMRPFromServer;
                tcf.deleteItemsMRP_Runi();
                parseMRPJson(json);

            }
        }
    }

    class GetMarchantPOFromServer extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_GetMrchntPO = null;

       @Override
       protected Void doInBackground(Void... params) {
           String url_getMercntPOfromServer = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_MARCHANT_PO +
                   "?handler="+ AnyMartData.HANDLE + "&sessionid="+ AnyMartData.SESSION_ID +
                   "&vendorid="+userid;

           URLConnection urlConnection = null;
           BufferedReader bufferedReader = null;

           try {
               res = Utility.OpenconnectionOrferbilling(url_getMercntPOfromServer, parent);
               int a = res.getBytes().length;
               res = res.replaceAll("\\\\", "");
               // res = res.replaceAll("\"", "");
               // res = res.replaceAll(" ", "");
               responseString = res.toString().replaceAll("^\"|\"$", "");
               Log.e("Response", responseString);
               /*URL urlGetMrchntPO = new URL(url_getMercntPOfromServer);
               urlConnection = urlGetMrchntPO.openConnection();
               urlConnection.setRequestProperty("Content-Type", "application/json");
               urlConnection.setRequestProperty("Accept", "JSON");
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

               StringBuffer stringBuff_GetMrchntPO = new StringBuffer();
               String line;
               StringBuilder str_build_GetMrchntPO = new StringBuilder();
               while((line = bufferedReader.readLine())!=null){
                   stringBuff_GetMrchntPO = stringBuff_GetMrchntPO.append(line);
               }
               responseString = stringBuff_GetMrchntPO.toString().replaceAll("^\"|\"$", "");*/
               resp_GetMrchntPO = responseString.replaceAll("\\\\","");

               Log.e("Response", resp_GetMrchntPO);

           } catch (Exception e) {
               resp_GetMrchntPO = "error";
               e.printStackTrace();
           }
           return null;
       }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (responseString.equalsIgnoreCase("Session Expired")) {
                if (cd.isConnectingToInternet()) {
                    new StartSession_tbuds(MarchantService.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetMarchantPOFromServer().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (responseString.equalsIgnoreCase("error")) {

            } else {
                json = resp_GetMrchntPO;
                tcf.deletePO();
                parsePOJson(json);

            }
        }
    }


    protected void parseJson(String json) {
        tcf.deleteMarchantItems();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantItemBean bean = new MarchantItemBean();

                bean.setItemname(jsonArray.getJSONObject(i).getString(
                        "itemname"));
                bean.setCategoryname(jsonArray.getJSONObject(i).getString(
                        "categoryname"));
                bean.setSubcategoryname(jsonArray.getJSONObject(i).getString(
                        "subcategoryname"));
                bean.setFKCategoryId(jsonArray.getJSONObject(i).getString(
                        "FKCategoryId"));
                bean.setFKsubcategoryid(jsonArray.getJSONObject(i).getString(
                        "FKsubcategoryid"));
                bean.setFKitemmasterid(jsonArray.getJSONObject(i).getString(
                        "FKitemmasterid"));
                bean.setPKVendoritemRelation(jsonArray.getJSONObject(i).getString(
                        "PKVendoritemRelation"));
                bean.setVendorid(jsonArray.getJSONObject(i).getString(
                        "vendorid"));


                tcf.addMarchantItem(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* if (cd.isConnectingToInternet()) {
            new GetMRPFromServer().execute();
        }*/
    }

    protected void parsePOJson(String json) {
      //  SQLiteDatabase db = dbHandler.getWritableDatabase();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setAmt(jsonArray.getJSONObject(i).getString(
                        "Amt"));
                bean.setItemId(jsonArray.getJSONObject(i).getString(
                        "ItemId"));
                bean.setItemName(jsonArray.getJSONObject(i).getString(
                        "Item_Name"));
                bean.setMRP(jsonArray.getJSONObject(i).getString(
                        "MRP"));
                bean.setPurchaseID(jsonArray.getJSONObject(i).getString(
                        "purchaseID"));
                bean.setShopName(jsonArray.getJSONObject(i).getString(
                        "ShopName"));
                bean.setQty(jsonArray.getJSONObject(i).getString(
                        "Qty"));
                bean.setPOUnit(jsonArray.getJSONObject(i).getString(
                        "Unit"));
                bean.setTotAmt(jsonArray.getJSONObject(i).getString(
                        "TotAmt"));
                bean.setVendorid(jsonArray.getJSONObject(i).getString(
                        "vendorid"));

                tcf.addMarchantPO(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void parseMRPJson(String json) {
    //    SQLiteDatabase db = dbHandler.getWritableDatabase();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setItemId(jsonArray.getJSONObject(i).getString(
                        "Itemid"));
                bean.setItemName(jsonArray.getJSONObject(i).getString(
                        "Itemname"));
                bean.setOldMRP(jsonArray.getJSONObject(i).getString(
                        "NMrpV"));
                bean.setQty(jsonArray.getJSONObject(i).getString(
                        "QtyV"));
                bean.setUnit(jsonArray.getJSONObject(i).getString(
                        "UnitV"));
                bean.setPOMRP(jsonArray.getJSONObject(i).getString("PurchaseMRP"));
                bean.setPOUnit(jsonArray.getJSONObject(i).getString("PurchaseUnit"));
                bean.setOldMRP(jsonArray.getJSONObject(i).getString("OMrpV"));
                bean.setPurchaseID(jsonArray.getJSONObject(i).getString("pkpurchaseid"));

                bean.setIsUploaded("Y");
                tcf.addItemMRP_Runi(bean);

                if (tcf.getProductCount(jsonArray.getJSONObject(i).getString("Itemid")) > 0) {

                    ContentValues values1 = new ContentValues();

                    values1.put("price", jsonArray.getJSONObject(i).getString(
                            "NMrpV"));
                    values1.put("Product_name", jsonArray.getJSONObject(i).getString(
                            "Itemname"));
                    values1.put("qnty", jsonArray.getJSONObject(i).getString(
                            "QtyV"));
                    values1.put("unit", jsonArray.getJSONObject(i).getString(
                            "UnitV"));

                    sql_db.update(dbhandler.TABLE_PRODUCT_CB,
                            values1, "Product_id=?",
                            new String[]{jsonArray.getJSONObject(i).getString(
                                    "Itemid")});
                } else {

                    MarchantItemBean bean2 = new MarchantItemBean();
                    bean2.setPrice(jsonArray.getJSONObject(i).getString(
                            "NMrpV"));
                    bean2.setItemname(jsonArray.getJSONObject(i).getString(
                            "Itemname"));
                    bean2.setFKitemmasterid(jsonArray.getJSONObject(i).getString(
                            "Itemid"));
                    bean2.setUnit(jsonArray.getJSONObject(i).getString(
                            "UnitV"));
                    bean2.setQty(jsonArray.getJSONObject(i).getString(
                            "QtyV"));
                    tcf.addMarchantItemCB(bean2);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // db.close();
        }
    }
}
