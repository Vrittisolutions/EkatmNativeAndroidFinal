package com.vritti.sales.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Utilities;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.Sales_HomeSActivity;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderHistoryAPIClass {
    Context parent;
    String res="";
    String statusCode = "";
    Tbuds_commonFunctions tcf;
    Utility ut;
    static DatabaseHandlers db;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "",  usertype = "", username = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    static ArrayList<OrderHistoryBean> arrayList = new ArrayList<>();

    static OrderHistoryBean bean;
    private static String DateToStr;
    private static String OrdRCVDt_ModifiedDt;
    String appCallFrom="";

    public OrderHistoryAPIClass(Context context){
        this.parent = context;

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
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

        arrayList = new ArrayList<>();
    }

    public ArrayList<OrderHistoryBean> getOrderHistory(String statCode){
        statusCode = statCode;

        if(tcf.getOrdHistory()>0){
            arrayList = getDataFromDatabase();
        }else {
            arrayList = callApi();
        }

        return arrayList;
    }

    public ArrayList<OrderHistoryBean> callApi(){
        try{
            arrayList = new GetMyOrderHistoryList().execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        /*if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession_tbuds(parent, new CallbackInterface() {

                @Override
                public void callMethod() {

                    try{
                        new GetMyOrderHistoryList().execute().get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        } else {
        }*/

        return arrayList;
    }

    class GetMyOrderHistoryList extends AsyncTask<ArrayList<OrderHistoryBean>, Void, ArrayList<OrderHistoryBean>> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<OrderHistoryBean> doInBackground(ArrayList<OrderHistoryBean>... params) {
            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_ORDER_HISTORY +
                    "?mobileno="+ "" + "&statuscode="+statusCode+"&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID;

            Log.e("url",url_orderHistory);

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_orderHistory = responseString.replaceAll("\\\\","");
                resp_orderHistory = responseString.replaceAll("u0026", "&");
                System.out.println("rsep = "+resp_orderHistory);

                try{
                    if (resp_orderHistory.equalsIgnoreCase("empty")) {
                        //      txtnoordnote.setVisibility(View.VISIBLE);
                    }else if (resp_orderHistory.equalsIgnoreCase("error")) {
                        //     txtnoordnote.setVisibility(View.VISIBLE);
                    } else if(resp_orderHistory.equalsIgnoreCase("[]")){
                        //      txtnoordnote.setVisibility(View.VISIBLE);
                    } else  if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {

                    }else{
                        //    txtnoordnote.setVisibility(View.GONE);
                        String json_1 = resp_orderHistory;
                        arrayList = parseJson(json_1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (NullPointerException e) {
                resp_orderHistory = "empty";
                e.printStackTrace();
            }catch (Exception e) {
                resp_orderHistory = "error";
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<OrderHistoryBean> result) {
            super.onPostExecute(result);

           /* try{
                if (resp_orderHistory.equalsIgnoreCase("empty")) {
              //      txtnoordnote.setVisibility(View.VISIBLE);
                }else if (resp_orderHistory.equalsIgnoreCase("error")) {
               //     txtnoordnote.setVisibility(View.VISIBLE);
                } else if(resp_orderHistory.equalsIgnoreCase("[]")){
             //      txtnoordnote.setVisibility(View.VISIBLE);
                } else  if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {

                }else{
                //    txtnoordnote.setVisibility(View.GONE);
                    String json_1 = resp_orderHistory;
                    parseJson(json_1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }
    }

    protected ArrayList<OrderHistoryBean> parseJson(String json) {
        tcf.clearTable_OrdHistory(parent, AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY,statusCode);
        arrayList.clear();

        Cursor c = sql.rawQuery("SELECT * FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY,null);
        Log.e("cnt", String.valueOf(c.getCount()));

        if(c.getCount()>0){
            String cnt = String.valueOf(c.getCount());
        }

        String PackOfQty = "0",UPIMerch="",PaymentStatus = "",PaymentMode="",TransactionId="",AmountStatus="",TransactionDate="",
                merchant_Mobile="",MerchAddress="",FreeAboveAmt="",FreeDelyMaxDist="",MinDelyKg="",MinDelyKm="",
                ExprDelyWithinMin="",ExpressDelyChg="",IsDelivery="", MerchLattitude="",MerchLongitude="";

        try {

            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject_so = new JSONObject();

                String SalesHeaderId = jsonArray.getJSONObject(i).getString("SalesHeaderId");
                String ShipStatus = jsonArray.getJSONObject(i).getString("ShipStatus");
                String OrdRcvdDate = jsonArray.getJSONObject(i).getString("ModifiedDt");
                String DODisptch = jsonArray.getJSONObject(i).getString("DODisptch");

                String PrefDelFrmTime = "", PrefDelToTime = "";
                try{
                    PrefDelFrmTime = jsonArray.getJSONObject(i).getString("PrefDelFrmTime");
                    PrefDelToTime = jsonArray.getJSONObject(i).getString("PrefDelToTime");
                }catch (Exception e){
                    e.printStackTrace();
                    PrefDelFrmTime = "";
                    PrefDelToTime = "";
                }

                String DispNetAmnt = jsonArray.getJSONObject(i).getString("DispNetAmnt");
                String DispatchNo = jsonArray.getJSONObject(i).getString("DispatchNo");
                String sono = jsonArray.getJSONObject(i).getString("sono");
                String statusname = jsonArray.getJSONObject(i).getString("statusname");
                String SODate = jsonArray.getJSONObject(i).getString("SODate");
                String status = jsonArray.getJSONObject(i).getString("status");
                String a = jsonArray.getJSONObject(i).getString("status");
                String CustomerMasterId = jsonArray.getJSONObject(i).getString("CustomerMasterId");
                String ShipToMasterId = jsonArray.getJSONObject(i).getString("ShipToMasterId");
                String Destination = jsonArray.getJSONObject(i).getString("Destination").trim();
                String ConsigneeName = jsonArray.getJSONObject(i).getString("ConsigneeName");
                String Address = jsonArray.getJSONObject(i).getString("Address");
                String City = jsonArray.getJSONObject(i).getString("City");
                String State = jsonArray.getJSONObject(i).getString("State");
                String Country = jsonArray.getJSONObject(i).getString("Country");
                String Mobile = jsonArray.getJSONObject(i).getString("Mobile");
                String DObkd = jsonArray.getJSONObject(i).getString("DObkd");
                String placeOrderDate = jsonArray.getJSONObject(i).getString("DoAck");
                String DORcvd = jsonArray.getJSONObject(i).getString("DORcvd");
                String DOrej = jsonArray.getJSONObject(i).getString("DOrej");
                String AppvDt = jsonArray.getJSONObject(i).getString("AppvDt");
                String SODetailId = jsonArray.getJSONObject(i).getString("SODetailId");
                String SOHeaderId = jsonArray.getJSONObject(i).getString("SOHeaderId");
                // String Dispatchdt = jsonArray.getJSONObject(i).getString("Dispatchdt");
                String NetAmt = jsonArray.getJSONObject(i).getString("NetAmt");

                String OrgQty = jsonArray.getJSONObject(i).getString("OrgQty");
                String DeliveryTerms = jsonArray.getJSONObject(i).getString("DeliveryTerms");
                String minordqty = jsonArray.getJSONObject(i).getString("minordqty");
                String maxordqty = jsonArray.getJSONObject(i).getString("maxordqty");
                String distance = jsonArray.getJSONObject(i).getString("distance");
                String UOMCode = jsonArray.getJSONObject(i).getString("UOMCode");
                String outofstock = jsonArray.getJSONObject(i).getString("outofstock");
                String mrp = jsonArray.getJSONObject(i).getString("MRP");
                String sellingrate = jsonArray.getJSONObject(i).getString("SellingRate");
                String range = jsonArray.getJSONObject(i).getString("range");
                String UOMDigit = jsonArray.getJSONObject(i).getString("UOMDigit");
                String Brand = jsonArray.getJSONObject(i).getString("Brand");
                String Content = jsonArray.getJSONObject(i).getString("Content");
                String ContentUOM = jsonArray.getJSONObject(i).getString("ContentUOM");
                String SellingUOM = jsonArray.getJSONObject(i).getString("SellingUOM");

                try{
                    PackOfQty = jsonArray.getJSONObject(i).getString("PackOfQty");
                    UPIMerch = jsonArray.getJSONObject(i).getString("UPI");
                    MerchAddress = jsonArray.getJSONObject(i).getString("MerchAddress");
                    merchant_Mobile = jsonArray.getJSONObject(i).getString("merchant_Mobile");
                    PaymentStatus = jsonArray.getJSONObject(i).getString("PaymentStatus");
                    PaymentMode = jsonArray.getJSONObject(i).getString("PaymentMode");
                    TransactionId = jsonArray.getJSONObject(i).getString("TransactionId");
                    AmountStatus = jsonArray.getJSONObject(i).getString("AmountStatus");
                    TransactionDate = jsonArray.getJSONObject(i).getString("TransactionDate");
                    FreeAboveAmt = jsonArray.getJSONObject(i).getString("FreeAboveAmt");
                    FreeDelyMaxDist = jsonArray.getJSONObject(i).getString("FreeDelyMaxDist");
                    MinDelyKg = jsonArray.getJSONObject(i).getString("MinDelyKg");
                    MinDelyKm = jsonArray.getJSONObject(i).getString("MinDelyKm");
                    ExprDelyWithinMin = jsonArray.getJSONObject(i).getString("ExprDelyWithinMin");
                    ExpressDelyChg = jsonArray.getJSONObject(i).getString("ExpressDelyChg");
                    IsDelivery = jsonArray.getJSONObject(i).getString("IsDelivery");
                    /*"MerchLattitude":"18.514278018846596","MerchLongitude":"73.82978461682796",
                    "Lattitude":"17.6941255","Longitude":"73.9816249"*/
                    MerchLattitude = jsonArray.getJSONObject(i).getString("MerchLattitude");
                    MerchLongitude = jsonArray.getJSONObject(i).getString("MerchLongitude");

                }catch (Exception e){
                    e.printStackTrace();
                }

                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                //SimpleDateFormat Format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = format.parse(placeOrderDate);

                if(OrdRcvdDate.equalsIgnoreCase("null")){

                }else {
                    if(!OrdRcvdDate.equalsIgnoreCase("")){
                        Date d2 = format.parse(OrdRcvdDate);
                        OrdRCVDt_ModifiedDt = Format.format(d2);
                    }
                }
                //DateToStr = toFormat.format(date);
                DateToStr = Format.format(d1);

                // DateToStr = format.format(d1);
                System.out.println(DateToStr);
                System.out.println(OrdRCVDt_ModifiedDt);

                jsonObject_so.put("Address", Address);
                jsonObject_so.put("City", City);
                jsonObject_so.put("ConsigneeName", ConsigneeName);
                jsonObject_so.put("Country", Country);
                jsonObject_so.put("State", State);
                jsonObject_so.put("AppvDt", AppvDt);
                jsonObject_so.put("DODisptch", DODisptch);
                jsonObject_so.put("DORcvd", DORcvd);
                jsonObject_so.put("DOrej", DOrej);
                jsonObject_so.put("SODate", SODate);
                jsonObject_so.put("DObkd", DObkd);
                jsonObject_so.put("DoAck", placeOrderDate);
                //  jsonObject_so.put("Dispatchdt", Dispatchdt);
                jsonObject_so.put("Destination", Destination);
                jsonObject_so.put("DispatchNo", DispatchNo);
                jsonObject_so.put("DispNetAmnt", DispNetAmnt);
                jsonObject_so.put("Mobile", Mobile);
                jsonObject_so.put("NetAmt", NetAmt);
                jsonObject_so.put("PrefDelFrmTime", PrefDelFrmTime);
                jsonObject_so.put("PrefDelToTime", PrefDelToTime);

                jsonObject_so.put("SOHeaderId", SOHeaderId);
                jsonObject_so.put("SalesHeaderId", SalesHeaderId);
                jsonObject_so.put("ShipToMasterId", ShipToMasterId);
                jsonObject_so.put("CustomerMasterId", CustomerMasterId);
                jsonObject_so.put("sono", sono);
                jsonObject_so.put("status", status);
                jsonObject_so.put("statusname", statusname);
                jsonObject_so.put("ShipStatus",ShipStatus);
                jsonObject_so.put("ModifiedDt",OrdRCVDt_ModifiedDt);
                jsonObject_so.put("DateToStr",DateToStr);
                jsonObject_so.put("ShipmentQty",DateToStr);
                jsonObject_so.put("ClientRecQty",DateToStr);

                tcf.addOrderHistory(jsonArray.getJSONObject(i).getString(
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
                        "NetAmt"), jsonArray.getJSONObject(i).getString(
                        "ItemDesc"),jsonArray.getJSONObject(i).getString(
                        "LineAmt"),jsonArray.getJSONObject(i).getString(
                        "merchantid"),jsonArray.getJSONObject(i).getString(
                        "merchantname"),jsonArray.getJSONObject(i).getString(
                        "SODetailId"),jsonArray.getJSONObject(i).getString(
                        "sono"),jsonArray.getJSONObject(i).getString(
                        "SOScheduleId"),jsonArray.getJSONObject(i).getString(
                        "ShipmentQty"),jsonArray.getJSONObject(i).getString(
                        "ClientRecQty"),jsonArray.getJSONObject(i).getString(
                        "AppvDt"),UOMDigit,jsonArray.getJSONObject(i).getString("DOrej"),
                        jsonArray.getJSONObject(i).getString("DispatchNo"),
                        jsonArray.getJSONObject(i).getString("SalesHeaderId"),"",
                        jsonArray.getJSONObject(i).getString("DispNetAmnt"),
                        jsonArray.getJSONObject(i).getString("ShipStatus"),
                        jsonArray.getJSONObject(i).getString("ModifiedDt"),
                        DateToStr, PrefDelFrmTime,PrefDelToTime,"","",
                        OrgQty,DeliveryTerms,minordqty, maxordqty,distance,UOMCode,outofstock,mrp,sellingrate,range,
                        Brand,Content,ContentUOM,SellingUOM,PackOfQty,UPIMerch,PaymentStatus,PaymentMode,TransactionId,AmountStatus,
                        TransactionDate,MerchAddress,merchant_Mobile,FreeAboveAmt,FreeDelyMaxDist,MinDelyKg,MinDelyKm,ExprDelyWithinMin,
                        ExpressDelyChg,IsDelivery,MerchLattitude,MerchLongitude);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        arrayList = getDataFromDatabase();

        return arrayList;

    }

    public ArrayList<OrderHistoryBean> getDataFromDatabase() {
        arrayList.clear();
        //SQLiteDatabase sql = databaseHelper.getWritableDatabase();
        Cursor c;

        if(appCallFrom.equals("C")){
            c = sql.rawQuery(
                    "SELECT DISTINCT SOHeaderId, sono, ConsigneeName  FROM " +
                            AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE Mobile ='"
                            + AnyMartData.MOBILE + "' AND status='"+statusCode+"' ORDER BY sono desc ",
                    null);
        }else if(appCallFrom.equals("V")){
            c = sql.rawQuery(
                    "SELECT DISTINCT SOHeaderId, sono, ConsigneeName  FROM " +
                            AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE merchant_Mobile ='"
                            + AnyMartData.MOBILE + "' AND status='"+statusCode+"' ORDER BY sono desc ",
                    null);
        }else {
            c = sql.rawQuery(
                    "SELECT DISTINCT SOHeaderId, sono, ConsigneeName  FROM " +
                            AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE status='"+statusCode+"' ORDER BY sono desc", null);
        }

        /*AND status='10'*/

        int ordercnt = 0;
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String orderid = c.getString(c.getColumnIndex("SOHeaderId"));
                ordercnt = ordercnt + 1;
                bean = new OrderHistoryBean();

                bean.setSOHeaderId/*(Integer.parseInt*/(orderid); //String.valueOf(ordercnt)
                bean.setSONo(c.getString(c.getColumnIndex("sono")));
                bean.setConsigneeName(c.getString(c.getColumnIndex("ConsigneeName")));

                SQLiteDatabase sql1 = db.getWritableDatabase();

                Cursor c1 = sql1.rawQuery(
                        "SELECT distinct * FROM "
                                + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " WHERE SOHeaderId ='"
                                + orderid + "' ORDER BY sono desc ", null);

                float amt = 0;
                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        //   float amtofitem = c1.getFloat(c1.getColumnIndex("Rate"));
                        String o_date = c1.getString(c1.getColumnIndex("SODate"));
                        //   amt = amt + amtofitem;
                        bean.setSODate(o_date);
                        bean.setNetAmt(Float.parseFloat(c1.getString(c1.getColumnIndex("NetAmt"))));
                        bean.setDoAck(c1.getString(c1.getColumnIndex("DoAck")));
                        bean.setDODisptch(c1.getString(c1.getColumnIndex("DODisptch")));
                        bean.setDORcvd(c1.getString(c1.getColumnIndex("OrdRcvdDate")));
                        bean.setDOApprvd(c1.getString(c1.getColumnIndex("AppvDt")));
                        bean.setStatus(c1.getString(c1.getColumnIndex("status")));
                        bean.setStatusname(c1.getString(c1.getColumnIndex("statusname")));
                        bean.setDispatchNo(c1.getString(c1.getColumnIndex("DispatchNo")));
                        bean.setDispNetAmnt(Float.parseFloat(c1.getString(c1.getColumnIndex("DispNetAmnt"))));
                        bean.setSalesHeaderId(c1.getString(c1.getColumnIndex("SalesHeaderId")));
                        bean.setDOrej(c1.getString(c1.getColumnIndex("DOrej")));
                        bean.setShipstatus(c1.getString(c1.getColumnIndex("ShipStatus")));
                        bean.setAddress(c1.getString(c1.getColumnIndex("Address")));
                        bean.setMerchantname(c1.getString(c1.getColumnIndex("merchantname")));
                        bean.setOrgQty(c1.getString(c1.getColumnIndex("OrgQty")));
                        bean.setDeliveryTerms(c1.getString(c1.getColumnIndex("DeliveryTerms")));
                        bean.setMinordqty(c1.getString(c1.getColumnIndex("minordqty")));
                        bean.setMaxordqty(c1.getString(c1.getColumnIndex("maxordqty")));
                        bean.setDistance(c1.getString(c1.getColumnIndex("distance")));
                        bean.setUOMCode(c1.getString(c1.getColumnIndex("UOMCode")));
                        bean.setOutofstock(c1.getString(c1.getColumnIndex("outofstock")));
                        bean.setMrp(Float.parseFloat(c1.getString(c1.getColumnIndex("mrp"))));
                        bean.setSellingrate(c1.getString(c1.getColumnIndex("sellingrate")));
                        bean.setRange(c1.getString(c1.getColumnIndex("range")));
                        bean.setUOMDigit(c1.getString(c1.getColumnIndex("PurDigit")));
                        bean.setBrand(c1.getString(c1.getColumnIndex("Brand")));
                        bean.setContent(c1.getString(c1.getColumnIndex("Content")));
                        bean.setContentUOM(c1.getString(c1.getColumnIndex("ContentUOM")));
                        bean.setSellingUOM(c1.getString(c1.getColumnIndex("SellingUOM")));
                        bean.setPackOfQty(c1.getString(c1.getColumnIndex("PackOfQty")));
                        bean.setUPIMerch(c1.getString(c1.getColumnIndex("UPIMerch")));
                        bean.setMerchAddress(c1.getString(c1.getColumnIndex("MerchAddress")));
                        bean.setMerchant_Mobile(c1.getString(c1.getColumnIndex("merchant_Mobile")));
                        bean.setMerchantid(c1.getString(c1.getColumnIndex("merchantid")));

                        try{
                            bean.setPaymentStatus(c1.getString(c1.getColumnIndex("PaymentStatus")));
                            bean.setPaymentMode(c1.getString(c1.getColumnIndex("PaymentMode")));
                            bean.setTransactionId(c1.getString(c1.getColumnIndex("TransactionId")));
                            bean.setAmountStatus(c1.getString(c1.getColumnIndex("AmountStatus")));
                            bean.setTransactionDate(c1.getString(c1.getColumnIndex("TransactionDate")));
                            bean.setFreeAboveAmt(c1.getString(c1.getColumnIndex("FreeAboveAmt")));
                            bean.setFreeDelyMaxDist(c1.getString(c1.getColumnIndex("FreeDelyMaxDist")));
                            bean.setMinDelyKg(c1.getString(c1.getColumnIndex("MinDelyKg")));
                            bean.setMinDelyKm(c1.getString(c1.getColumnIndex("MinDelyKm")));
                            bean.setExprDelyWithinMin(c1.getString(c1.getColumnIndex("ExprDelyWithinMin")));
                            bean.setExpressDelyChg(c1.getString(c1.getColumnIndex("ExpressDelyChg")));
                            bean.setIsDelivery(c1.getString(c1.getColumnIndex("IsDelivery")));
                            bean.setMerchLatitude(c1.getString(c1.getColumnIndex("MerchLattitude")));
                            bean.setMerchLongitude(c1.getString(c1.getColumnIndex("MerchLongitude")));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } while (c1.moveToNext());
                }
                arrayList.add(bean);
            } while(c.moveToNext());
        } else {

        }

        return arrayList;

    }

}
