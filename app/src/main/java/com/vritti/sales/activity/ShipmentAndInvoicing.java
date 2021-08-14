package com.vritti.sales.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.OpenOrderListAdapter;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

public class ShipmentAndInvoicing extends AppCompatActivity {
    private static Context parent;
    Toolbar toolbar;
    static ProgressBar mprogress;
    static GridView listview_recent_ordered_list;
    static TextView pending_ordrcnt,txt_showmore,txtnoordnote;
    static ImageView imgrefresh;

    public static ArrayList<OrderHistoryBean> historyBeanList;
    static ArrayList<OrderHistoryBean> newList = null;
    static OpenOrderListAdapter myOrderHistoryAdapter;

    SharedPreferences sharedpreferences;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    String CustVendorMasterId, CustomerID;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private static DatabaseHandlers db;
    static SQLiteDatabase sql_db;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "", OrderType = "", numTomakeCall = "";

    private static String json;
    private static int index = 0;
    private static String DateToStr;
    private static int newlistCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_shipment_and_invoicing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Shipment & Invoicing");
        toolbar.setTitle("Packaging & Invoicing");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //AnyMartData.MerchantID = "fee4b450-174f-4bdc-84d2-ad6eca7d37fc";

        new StartSession_tbuds(parent, new CallbackInterface() {

            @Override
            public void callMethod() {

                new GetPendingOrderHistoryList().execute();
            }

            @Override
            public void callfailMethod(String s) {

            }
        });

        setListeners();
    }

    public void init(){
        parent = ShipmentAndInvoicing.this;

        toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Packaging & Invoicing");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = findViewById(R.id.toolbar_progress_Assgnwork);
        imgrefresh = findViewById(R.id.imgrefresh);

        txtnoordnote = findViewById(R.id.txtnoordnote);
        txt_showmore = findViewById(R.id.txt_showmore);
        pending_ordrcnt = findViewById(R.id.pending_ordrcnt);
        pending_ordrcnt.setText(String.valueOf(index));
        listview_recent_ordered_list = findViewById(R.id.listview_recent_ordered_list);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        restoredText = sharedpreferences.getString("Mobileno", null);
        //restoredownername = sharedpreferences.getString("OwnerName", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO",null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO",null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId",null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId",null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId", null);
        AnyMartData.LANGUAGE = sharedpreferences.getString("Language","");
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
        AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.CITY = sharedpreferences.getString("City","");
        AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.STATE = sharedpreferences.getString("State","");
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ShipmentAndInvoicing.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql_db = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //logged in's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        historyBeanList = new ArrayList<OrderHistoryBean>();

    }

    public void setListeners(){

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new StartSession_tbuds(parent, new CallbackInterface() {

                    @Override
                    public void callMethod() {

                        new GetPendingOrderHistoryList().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        });

        listview_recent_ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, final int position, long id) {

                String SOHeaderID = newList.get(position).getSOHeaderId();
                String status = newList.get(position).getStatus();
                String Consignee = newList.get(position).getConsigneeName();
                String ConsigneeID = newList.get(position).getCustomerMasterId();
                String TotalAmt = String.valueOf(newList.get(position).getNetAmt());
                String sono = newList.get(position).getSONo();
                String address = newList.get(position).getAddress();
                String ShipToMasterId = newList.get(position).getShipToMasterId();
                String deliveryAddress = newList.get(position).getAddress();
                String deliveryDate = newList.get(position).getSODate();
                String prfDelFrmTime = newList.get(position).getPrfDelFrmTime();
                String prfDelToTime = newList.get(position).getPrfDelToTime();
                String CustomerMasterId = newList.get(position).getCustomerMasterId();

                Intent intent_go = new Intent(parent, ShipmentEntryActivity.class);
                intent_go.putExtra("SOHeaderID", SOHeaderID);
                intent_go.putExtra("TotalAmt",TotalAmt);
                intent_go.putExtra("SONO",sono);
                intent_go.putExtra("Consignee",Consignee);
                intent_go.putExtra("ShipToMasterId",ShipToMasterId);
                intent_go.putExtra("DeliveryAddress",deliveryAddress);
                intent_go.putExtra("DeliveryDate",deliveryDate);
                intent_go.putExtra("ConsigneeID",ConsigneeID);
                intent_go.putExtra("prfDelFrmTime",prfDelFrmTime);
                intent_go.putExtra("prfDelToTime",prfDelToTime);
                intent_go.putExtra("CustomerMasterId",CustomerMasterId);

                startActivity(intent_go);
                finish();
            }
        });

        txt_showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                index = historyBeanList.size() + 10;
                // Toast.makeText(parent,"index = "+index,Toast.LENGTH_SHORT ).show();

                if ((AnyMartData.SESSION_ID != null)
                        && (AnyMartData.HANDLE != null)) {
                    new GetPendingOrderHistoryList().execute();
                } else {
                    new StartSession_tbuds(parent, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new GetPendingOrderHistoryList().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            }
        });
    }

    public class GetPendingOrderHistoryList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imgrefresh.setVisibility(View.GONE);
            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_PENDING_ORDERS_SHIPMENTS +
                    "?mobileno=" + AnyMartData.MOBILE +
                    "&handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID +
                    "&index=" + index+
                    //"&MerchantId=" +AnyMartData.MerchantID;
                    "&MerchantId=" +"";

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_orderHistory = responseString.replaceAll("\\\\", "");
                System.out.println("rsep = " + resp_orderHistory);

            } catch (NullPointerException e) {
                resp_orderHistory = "empty";
                e.printStackTrace();
            } catch (Exception e) {
                resp_orderHistory = "error";
                e.printStackTrace();
            }
            return resp_orderHistory;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            imgrefresh.setVisibility(View.VISIBLE);
            mprogress.setVisibility(View.GONE);

            try{
                if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {

                } else if (resp_orderHistory.equalsIgnoreCase("empty")) {

                } else if (resp_orderHistory.equalsIgnoreCase("error")) {

                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG)
                            .show();
                } else {
                    json = resp_orderHistory;
                    parseJson_pendingorder(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected void parseJson_pendingorder(String json) {
        tcf.clearTable(parent, DatabaseHandlers.TABLE_MY_ORDER_HISTORY);

        historyBeanList.clear();

        String Address = "", City = "", ConsigneeName = "", CustomerMasterId = "", ItemMasterId = null,
                Mobile = "", Qty = "", Rate = "", SODate = "", SOHeaderId = "", DODisptch = "",
                DORcvd = "", DoAck = "", status = "", sono = "", NetAmt = "", LineAmt = "", ItemDesc = "",
                merchantid = "", merchantname = "", SODetailId = "", statusname = "", ShipToMasterId = "",
                prefDelFromTime = "", prefDelToTime = "",DeliveryMode = "",PaymentStatus="",PaymentMode="",TransactionId="",
                AmountStatus="",TransactionDate="", custLat ="", custLng="", UOMCode="", Content="",Brand="";

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                OrderHistoryBean historybean = new OrderHistoryBean();
                String a = jsonArray.getJSONObject(i).getString("status");
                String placeOrderDate = "";
                try{
                    placeOrderDate = jsonArray.getJSONObject(i).getString("DoAck");
                }catch (Exception e){
                    e.printStackTrace();
                    placeOrderDate = "";
                }

                if(placeOrderDate.contains("/Date")){
                    placeOrderDate = placeOrderDate.replace("/Date(","");
                    placeOrderDate = placeOrderDate.replace(")/","");
                    Long timestamp = Long.valueOf(placeOrderDate);
                    Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
                    cal1.setTimeInMillis(timestamp);
                    String date1 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal1).toString();

                    SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                    try{
                        Date d1 = format.parse(date1);
                        DateToStr = Format.format(d1);
                    }catch (Exception e){
                        e.printStackTrace();
                        DateToStr = "";
                    }

                }else {
                    if(!placeOrderDate.equalsIgnoreCase("") || !placeOrderDate.equalsIgnoreCase("null")){
                        SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                        try{
                            Date d1 = format.parse(placeOrderDate);
                            DateToStr = Format.format(d1);
                            System.out.println(DateToStr);
                        }catch (Exception e){
                            e.printStackTrace();
                            DateToStr = "";
                        }
                    }else {
                        DateToStr = "";
                    }
                }

                sono = jsonArray.getJSONObject(i).getString("sono");
                Address = jsonArray.getJSONObject(i).getString("Address");
                City = jsonArray.getJSONObject(i).getString("City");
                ConsigneeName = jsonArray.getJSONObject(i).getString("ConsigneeName");
                CustomerMasterId = jsonArray.getJSONObject(i).getString("CustomerMasterId");
                ItemMasterId = jsonArray.getJSONObject(i).getString("ItemMasterId");
                Mobile = jsonArray.getJSONObject(i).getString("Mobile");
                Qty = jsonArray.getJSONObject(i).getString("Qty");
                Rate = jsonArray.getJSONObject(i).getString("Rate");
                SOHeaderId = jsonArray.getJSONObject(i).getString("SOHeaderId");
                status = jsonArray.getJSONObject(i).getString("status");

                try{
                    SODate = jsonArray.getJSONObject(i).getString("SODate");
                    DoAck = jsonArray.getJSONObject(i).getString("DoAck");
                    DODisptch = jsonArray.getJSONObject(i).getString("DODisptch");
                    DORcvd = jsonArray.getJSONObject(i).getString("DORcvd");
                    try{
                        TransactionDate = jsonArray.getJSONObject(i).getString("TransactionDate");
                    }catch (Exception e){
                        e.printStackTrace();
                        TransactionDate="";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    SODate = "";
                    DoAck = "";
                    DODisptch = "";
                    DORcvd = "";
                    TransactionDate="";
                }

                NetAmt = jsonArray.getJSONObject(i).getString("NetAmt");
                ItemDesc = jsonArray.getJSONObject(i).getString("ItemDesc").trim();
                LineAmt = jsonArray.getJSONObject(i).getString("LineAmt");
                merchantid = jsonArray.getJSONObject(i).getString("merchantid");
                merchantname = jsonArray.getJSONObject(i).getString("merchantname");
                SODetailId = jsonArray.getJSONObject(i).getString("SODetailId");
                statusname = jsonArray.getJSONObject(i).getString("statusname");
                ShipToMasterId = jsonArray.getJSONObject(i).getString("ShipToMasterId");
                prefDelFromTime = jsonArray.getJSONObject(i).getString("PrefDelFrmTime");
                prefDelToTime = jsonArray.getJSONObject(i).getString("PrefDelToTime");

                try{
                    DeliveryMode = jsonArray.getJSONObject(i).getString("DeliveryTerms");
                    PaymentStatus = jsonArray.getJSONObject(i).getString("PaymentStatus");
                    TransactionId = jsonArray.getJSONObject(i).getString("TransactionId");
                    PaymentMode = jsonArray.getJSONObject(i).getString("PaymentStatus");
                    AmountStatus = jsonArray.getJSONObject(i).getString("AmountStatus");
                    custLat = jsonArray.getJSONObject(i).getString("Latitude");
                    custLng = jsonArray.getJSONObject(i).getString("Longitude");
                    UOMCode = jsonArray.getJSONObject(i).getString("UOMCode");
                    Brand = jsonArray.getJSONObject(i).getString("Brand");
                    Content = jsonArray.getJSONObject(i).getString("Content");
                }catch (Exception e){
                    e.printStackTrace();
                }

                historybean.setSOHeaderId(SOHeaderId);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setCustomerMasterId(CustomerMasterId);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(NetAmt));
                historybean.setDoAck(DoAck);
                //historybean.setDoAck(DateToStr);
                historybean.setStatus(status);
                historybean.setStatusname(statusname);
                historybean.setSONo(sono);
                historybean.setAddress(Address);
                historybean.setShipToMasterId(ShipToMasterId);
                historybean.setPrfDelFrmTime(prefDelFromTime);
                historybean.setPrfDelToTime(prefDelToTime);
                historybean.setMobile(Mobile);
                historybean.setDeliveryTerms(DeliveryMode);
                historybean.setPaymentStatus(PaymentStatus);
                historybean.setPaymentMode(PaymentMode);
                historybean.setTransactionDate(TransactionDate);
                historybean.setTransactionId(TransactionId);
                historybean.setAmountStatus(AmountStatus);
                historybean.setItemDesc(ItemDesc);
                historybean.setQty(Float.parseFloat(Qty));
                historybean.setRate(Float.parseFloat(Rate));
                historybean.setCustLat(custLat);
                historybean.setCustLng(custLng);
                historybean.setUOMCode(UOMCode);
                historybean.setContent(Content);
                historybean.setBrand(Brand);

                //for Vendor purpose for pending shipments
                if(status.equalsIgnoreCase("10")){

                }else {
                    historyBeanList.add(historybean);
                }
            }

            int i = historyBeanList.size();
            Log.e("", "" + i);

            System.out.println(historyBeanList);
            Set<OrderHistoryBean> set = new TreeSet<OrderHistoryBean>(new Comparator<OrderHistoryBean>() {
                @Override
                public int compare(OrderHistoryBean o1, OrderHistoryBean o2) {
                    String a = o1.getSOHeaderId();
                    String b = o2.getSOHeaderId();
                    Log.e("", "" + a + " " + b);
                    if (o1.getSOHeaderId().equalsIgnoreCase(o2.getSOHeaderId())) {
                        return 0;
                    }
                    return 1;
                }
            });
            set.addAll(historyBeanList);
            System.out.println(historyBeanList);

            newList = new ArrayList<OrderHistoryBean>(set);

            System.out.println(newList);

            //historyBeanList = new ArrayList<OrderHistoryBean>(set);
            System.out.println(historyBeanList);
            int i2 = historyBeanList.size();
            Log.e("", "" + i2);

            myOrderHistoryAdapter = new OpenOrderListAdapter(parent, newList);
            listview_recent_ordered_list.setAdapter(myOrderHistoryAdapter);

            if(!(newlistCount==0)){
                int cntdisp = newList.size() - newlistCount;
                //listview_recent_ordered_list.setSelection((newList.size())-(newList.size()-newlistCount));
                listview_recent_ordered_list.setSelection(newList.size() - cntdisp);
            }

            newlistCount = newList.size();

            //listview_recent_ordered_list.smoothScrollToPosition(listview_recent_ordered_list.getCount());
            pending_ordrcnt.setText(String.valueOf(listview_recent_ordered_list.getCount()));
            //   pending_ordrcnt.setText(String.valueOf(newList.size()));

            if(newList.size() == 0){
                txtnoordnote.setVisibility(View.VISIBLE);
            }else {
                txtnoordnote.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void MakeCall(String mobile){
        numTomakeCall = mobile;

        try{

            if (ActivityCompat.checkSelfPermission(ShipmentAndInvoicing.this.getApplicationContext(), Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91"+mobile));
                startActivity(callIntent);
            }
            else
            {
                ActivityCompat.requestPermissions(ShipmentAndInvoicing.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String Convertdate(String date){
        //SimpleDateFormat Format = new SimpleDateFormat("dd-MM-yyyy");//Feb 23 2016 12:16PM
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat Format = new SimpleDateFormat("EEE dd MMM yyyy");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date d1 = null;
        try {
            d1 = format.parse(date);
            //DateToStr = toFormat.format(date);
            DateToStr = Format.format(d1);
            // DateToStr = format.format(d1);
            System.out.println(DateToStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateToStr;
    }

    public void share(String sono, String amt, String status, String custName, String custMob, String custAddr,
                      String custLat, String custLng, String sohdrId, String delDate, String frmTime, String toTime){

        String payment = "", delTime = "";
        if(status.equalsIgnoreCase("42")){
            payment = "Paid";
        }else {
            payment = "Unpaid";
        }

        delTime= Convertdate(delDate) +", "+frmTime +" "+getResources().getString(R.string.to)+" "+ toTime;

        ArrayList<String> itemlist = new ArrayList<String>();
        itemlist.clear();

        for(int i=0; i<historyBeanList.size();i++){
            if(sohdrId.equalsIgnoreCase(historyBeanList.get(i).getSOHeaderId())){
                //add to list string
                String itmname = historyBeanList.get(i).getItemDesc();
                String content = historyBeanList.get(i).getContent();
                String UOMCode = historyBeanList.get(i).getUOMCode();
                String Brand = historyBeanList.get(i).getBrand();
                String Rate = String.format("%.2f",historyBeanList.get(i).getRate());
                String Qty = String.valueOf(historyBeanList.get(i).getQty());
                String mrp = String.valueOf(historyBeanList.get(i).getMrp());
                String range = historyBeanList.get(i).getRange();

                if(content.contains(".0")){
                    content = content.replace(".0","");
                }else {
                    content = content;
                }

                if(Qty.contains(".0")){
                    Qty = Qty.replace(".0","");
                }else {
                    Qty = Qty;
                }

                String product="";
                product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+Rate;
                /*if(range.equalsIgnoreCase("True")){
                    product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+mrp;
                }else {
                    product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+Rate;
                }*/

                itemlist.add(product);
            }else {
                //do not add ignore
            }
        }

        try {
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain"); /*image/*,*/
            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan" /*AnyMartData.MODULE + "App"*/);
            //String msg = "\n Let me recommend you Any Dukaan application\n\n";

            //content to send
            String msg = getResources().getString(R.string.greetings);
            msg += "\n"+getResources().getString(R.string.ordno)+" - "+sono;
            msg += "\n"+getResources().getString(R.string.ordamt)+" - "+amt+" ₹";
            msg += "\n"+getResources().getString(R.string.status)+" - "+payment;
            msg += "\n\n"+getResources().getString(R.string.custdtl);
            msg += "\n"+getResources().getString(R.string.name)+" - "+custName;
            msg += "\n"+getResources().getString(R.string.mobile)+" - "+custMob;
            msg += "\n"+getResources().getString(R.string.address)+" - "+custAddr;
            //String mapaddr = "http://maps.google.com/maps?q=loc:" + "18.4668454"+ "," + "73.7812046";
            String mapaddr = "http://maps.google.com/maps?q=loc:" + custLat+ "," + custLat;
            msg += "\n"+mapaddr;
            msg += "\n"+getResources().getString(R.string.expdelon)+" "+delTime;
            msg += "\n\n"+getResources().getString(R.string.prod)+" : ";
            for (int k=0; k<itemlist.size(); k++){
                msg += "\n"+itemlist.get(k).toString().trim();
            }

            String url1= "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";
            //Uri linkurl = Uri.parse(url1);
            Uri imageUri = Uri.parse("android.resource://" + parent.getPackageName()+ "/drawable/" + "anydukan_2");
            i.putExtra(Intent.EXTRA_TEXT, msg+"\n\n"+getResources().getString(R.string.clickhere)+"\n"+ Html.fromHtml(url1));
            //i.putExtra(Intent.EXTRA_STREAM, imageUri);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i, getResources().getString(R.string.choseone)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
