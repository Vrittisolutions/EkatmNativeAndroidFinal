package com.vritti.sales.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.OpenOrderListAdapter;
import com.vritti.sales.adapters.SOApprListAdapter;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class SOApproveActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    GridView listso;
    TextView txtnoordnote;
    LinearLayout txtlayout;
    ImageView imgrefresh;
    SOApprListAdapter soAdapter;
    ArrayList<OrderHistoryBean> listSO;

    public static ArrayList<OrderHistoryBean> historyBeanList;
    static OpenOrderListAdapter myOrderHistoryAdapter;

    SharedPreferences sharedpreferences;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    String CustVendorMasterId, CustomerID;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private static DatabaseHandlers db;
    static SQLiteDatabase sql_db;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "", OrderType = "";

    private static String json;
    private static String DateToStr;
    private static int newlistCount = 0;
    int posToRemove = 0;

    String sono = "", TotalOrderValue = "",LineAmt = "",ItemMasterId = "", ItemDesc = "",ConsigneeName = "", CustomerMasterId = "",
            SOHeaderId = "", OrgQty = "",Qty = "", Rate = "",SODate = "",DoAck = "",SODetailId = "", Range = "",
            DeliveryTerms = "",distance = "",UOMCode = "",mrp = "",UOMDigit = "",custMobile = "",numTomakeCall =  "",Brand = "",Content = "",
            ContentUOM = "",SellingUOM ="",PackOfQty="0",FreeAboveAmt="",FreeDelyMaxDist="",MinDelyKg="",MinDelyKm="",ExprDelyWithinMin="",ExpressDelyChg="";
    private String tax="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soapprove);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales Orders");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //AnyMartData.MerchantID = "fee4b450-174f-4bdc-84d2-ad6eca7d37fc";

        //get SO List data from API
        if(isnet()){
            new StartSession_tbuds(SOApproveActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetOpenSOList().execute();
                }
                @Override
                public void callfailMethod(String s) {
                }
            });
        }

        setListeners();

    }

    public void init(){
        parent = SOApproveActivity.this;

        txtnoordnote = findViewById(R.id.txtnoordnote);
        listso = findViewById(R.id.listso);
        txtlayout = findViewById(R.id.txtlayout);
        imgrefresh = findViewById(R.id.imgrefresh);
        mprogress = findViewById(R.id.mprogress);

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
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(SOApproveActivity.this);
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

        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        listSO = new ArrayList<OrderHistoryBean>();
        historyBeanList = new ArrayList<OrderHistoryBean>();

        /*soAdapter = new SOApprListAdapter(this,listSO);
        listso.setAdapter(soAdapter);       //testpurpose*/

    }

    public void setListeners(){

        txtlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SOApproveActivity.this,SOApproveDetailActivity.class);
                startActivity(intent);
            }
        });

        listso.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                posToRemove = position;


                String custID = listSO.get(position).getCustomerMasterId();
                String so = listSO.get(position).getSONo();
                String merchcustdist = listSO.get(position).getDistance();
                String perkmchrg = listSO.get(position).getMinDelyKm();
                String maxfreedeldist = listSO.get(position).getFreeDelyMaxDist();

                Intent intent = new Intent(SOApproveActivity.this,SOApproveDetailActivity.class);
                intent.putExtra("SoHeaderId",listSO.get(position).getSOHeaderId());
                intent.putExtra("ConsigneeName",listSO.get(position).getConsigneeName());
                intent.putExtra("ConsigneeId",custID);
                intent.putExtra("SONO",so);
                intent.putExtra("TotOrdValue",String.valueOf(listSO.get(position).getNetAmt()));
                intent.putExtra("DeliveryMode",listSO.get(position).getDeliveryTerms());
                intent.putExtra("FreeAboveAmt",listSO.get(position).getFreeAboveAmt());
                intent.putExtra("Distance",merchcustdist);
                intent.putExtra("ValPerKm",perkmchrg);
                intent.putExtra("MaxFreeDelDist",maxfreedeldist);
                intent.putExtra("Tax",listSO.get(position).getTax());
                startActivity(intent);
            }
        });

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isnet()){
                    new GetOpenSOList().execute();
                }
            }
        });
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public class GetOpenSOList extends AsyncTask<Void, Void, Void> {
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
        protected Void doInBackground(Void... params) {

            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.api_getOrderAcceptance_SOList +
                    "?AppEnvMasterId=" + EnvMasterId +
                    "&PlantId=" + PlantMasterId +
                    //"&UserMasterId=" + AnyMartData.MerchantID +
                    "&UserMasterId=" + "" +
                    "&Mobile=" + MobileNo+
                    //"&MerchantId=" + AnyMartData.MerchantID;
                    "&MerchantId=" + "";
                    //"&MerchantId=" + MerchantId;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {

            }else if (resp_orderHistory.equalsIgnoreCase("empty")) {
                imgrefresh.setVisibility(View.VISIBLE);
                mprogress.setVisibility(View.GONE);

                txtnoordnote.setVisibility(View.VISIBLE);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent, "No orders to show", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (resp_orderHistory.equalsIgnoreCase("[]")) {
                imgrefresh.setVisibility(View.VISIBLE);
                mprogress.setVisibility(View.GONE);

                try{
                    listSO.clear();
                    soAdapter = new SOApprListAdapter(SOApproveActivity.this,listSO);
                    listso.setAdapter(soAdapter);
                    soAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

                txtnoordnote.setVisibility(View.VISIBLE);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent, "No orders to show", Toast.LENGTH_LONG).show();
                    }
                });

            } else if (resp_orderHistory.equalsIgnoreCase("error")) {

                txtnoordnote.setVisibility(View.VISIBLE);

                Toast.makeText(parent, "The server is taking too long to respond OR " +
                        "something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else {
                imgrefresh.setVisibility(View.VISIBLE);
                mprogress.setVisibility(View.GONE);

                txtnoordnote.setVisibility(View.GONE);

                json = resp_orderHistory;
                parseJson_openSO(json);
            }
        }
    }

    protected void parseJson_openSO(String json) {
        tcf.clearTable(parent, DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE);
        listSO.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                OrderHistoryBean historybean = new OrderHistoryBean();
                // String a = jsonArray.getJSONObject(i).getString("status");
                String placeOrderDate = jsonArray.getJSONObject(i).getString("Doack");

                if(!placeOrderDate.equalsIgnoreCase("")){
                    SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                    //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                    SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date d1 = format.parse(placeOrderDate);
                    DateToStr = Format.format(d1);
                    System.out.println(DateToStr);
                }else {
                    DateToStr = "";
                }
                SOHeaderId = jsonArray.getJSONObject(i).getString("SoHeaderId");
                SODetailId = jsonArray.getJSONObject(i).getString("SoDetailId");
                sono = jsonArray.getJSONObject(i).getString("SoNo");
                CustomerMasterId = jsonArray.getJSONObject(i).getString("CustomerMasterId");
                ConsigneeName = jsonArray.getJSONObject(i).getString("ConsigneeName");
                //Mobile = jsonArray.getJSONObject(i).getString("Mobile");
                TotalOrderValue = jsonArray.getJSONObject(i).getString("TotalOrderValue");
                DoAck = jsonArray.getJSONObject(i).getString("Doack");
                SODate = jsonArray.getJSONObject(i).getString("SoDate");
                Qty = jsonArray.getJSONObject(i).getString("Qty");
                Rate = jsonArray.getJSONObject(i).getString("Rate");
                LineAmt = jsonArray.getJSONObject(i).getString("LineAmt");
                OrgQty = jsonArray.getJSONObject(i).getString("OrgQty");
                ItemMasterId = jsonArray.getJSONObject(i).getString("ItemMasterId");
                ItemDesc = jsonArray.getJSONObject(i).getString("ItemDesc");
                Range = jsonArray.getJSONObject(i).getString("Range");
                DeliveryTerms = jsonArray.getJSONObject(i).getString("DeliveryTerms");
                distance = jsonArray.getJSONObject(i).getString("distance");
                UOMCode = jsonArray.getJSONObject(i).getString("UOMCode");
                mrp = jsonArray.getJSONObject(i).getString("mrp");
                UOMDigit = jsonArray.getJSONObject(i).getString("UOMDigit");
                custMobile = jsonArray.getJSONObject(i).getString("Mobile");
                Brand= jsonArray.getJSONObject(i).getString("Brand");
                Content = jsonArray.getJSONObject(i).getString("Content");
                ContentUOM = jsonArray.getJSONObject(i).getString("ContentUOM");
                SellingUOM = jsonArray.getJSONObject(i).getString("SellingUOM");
                FreeAboveAmt = jsonArray.getJSONObject(i).getString("FreeAboveAmt");
                FreeDelyMaxDist = jsonArray.getJSONObject(i).getString("FreeDelyMaxDist");
                MinDelyKg = jsonArray.getJSONObject(i).getString("MinDelyKg");
                MinDelyKm = jsonArray.getJSONObject(i).getString("MinDelyKm");
                ExprDelyWithinMin = jsonArray.getJSONObject(i).getString("ExprDelyWithinMin");
                ExpressDelyChg = jsonArray.getJSONObject(i).getString("ExpressDelyChg");
                tax = jsonArray.getJSONObject(i).getString("TotTaxAmt");

                try{
                    PackOfQty = jsonArray.getJSONObject(i).getString("PackOfQty");
                }catch (Exception e){
                    e.printStackTrace();
                }

                historybean.setSOHeaderId(SOHeaderId);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setCustomerMasterId(CustomerMasterId);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(TotalOrderValue));
                historybean.setDoAck(DoAck);
                historybean.setSONo(sono);
                historybean.setRange(Range);
                historybean.setMrp(Float.parseFloat(mrp));
                historybean.setMobile(custMobile);
                historybean.setBrand(Brand);
                historybean.setContent(Content);
                historybean.setContentUOM(ContentUOM);
                historybean.setSellingUOM(SellingUOM);
                historybean.setPackOfQty(PackOfQty);
                historybean.setFreeAboveAmt(FreeAboveAmt);
                historybean.setFreeDelyMaxDist(FreeDelyMaxDist);
                historybean.setMinDelyKg(MinDelyKg);
                historybean.setMinDelyKm(MinDelyKm);
                historybean.setExprDelyWithinMin(ExprDelyWithinMin);
                historybean.setExpressDelyChg(ExpressDelyChg);
                historybean.setTax(tax);


                listSO.add(historybean);

                tcf.insert_OpenOrdersData(SOApproveActivity.this,sono,SOHeaderId,SODetailId,ConsigneeName,CustomerMasterId,ItemDesc,ItemMasterId,Qty,OrgQty,
                        Rate,LineAmt, TotalOrderValue,SODate,DoAck,Range,mrp,distance,UOMDigit,UOMCode,DeliveryTerms,custMobile,Brand,Content,
                        ContentUOM,SellingUOM,PackOfQty,FreeAboveAmt,FreeDelyMaxDist,MinDelyKg,MinDelyKm,ExprDelyWithinMin,ExpressDelyChg,tax);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getDataFromDatabase();
    }

    public String Convertdate(String date){

        try{
            SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//Feb 23 2016 12:16PM
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
        }catch (Exception e){
            e.printStackTrace();
            DateToStr = "";
        }


        return DateToStr;
    }

    /*public void getDataFromDatabase(){
        listSO.clear();

        String qry = "Select distinct sono,SOHeaderId,TotalOrderValue,SODate,DoAck,ConsigneeName,CustomerMasterId from "+ db.TABLE_MY_ORDER_ACCEPTANCE +
                " order by sono desc";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                sono = c.getString(c.getColumnIndex("sono"));
                SOHeaderId = c.getString(c.getColumnIndex("SOHeaderId"));
                TotalOrderValue = c.getString(c.getColumnIndex("TotalOrderValue"));
                SODate = c.getString(c.getColumnIndex("SODate"));
                DoAck = c.getString(c.getColumnIndex("DoAck"));
                ConsigneeName = c.getString(c.getColumnIndex("ConsigneeName"));
                CustomerMasterId = c.getString(c.getColumnIndex("CustomerMasterId"));

                OrderHistoryBean historybean = new OrderHistoryBean();
                historybean.setSOHeaderId(SOHeaderId);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(TotalOrderValue));
                historybean.setDoAck(DoAck);
                historybean.setSONo(sono);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setCustomerMasterId(CustomerMasterId);

                listSO.add(historybean);

                Set<OrderHistoryBean> set = new TreeSet<OrderHistoryBean>(new Comparator<OrderHistoryBean>() {
                    @Override
                    public int compare(OrderHistoryBean o1, OrderHistoryBean o2) {
                        String a = o1.getSONo();
                        String b = o2.getSONo();
                        Log.e("", "" + a + " " + b);
                        if (o1.getSONo().equalsIgnoreCase(o2.getSONo())) {
                            return 0;
                        }
                        return 1;
                    }
                });
                set.addAll(listSO);

            }while (c.moveToNext());

            soAdapter = new SOApprListAdapter(this,listSO);
            listso.setAdapter(soAdapter);
            soAdapter.notifyDataSetChanged();
        }
    }*/

    public void getDataFromDatabase(){
        listSO.clear();

        String qry = "Select distinct sono,SOHeaderId,TotalOrderValue," +
                "SODate,DoAck,ConsigneeName,CustomerMasterId,Mobile,DeliveryTerms,TotTaxAmt" +
                " from "+ DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE +
                " order by SODate desc";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                sono = c.getString(c.getColumnIndex("sono"));
                SOHeaderId = c.getString(c.getColumnIndex("SOHeaderId"));
                TotalOrderValue = c.getString(c.getColumnIndex("TotalOrderValue"));
                SODate = c.getString(c.getColumnIndex("SODate"));
                DoAck = c.getString(c.getColumnIndex("DoAck"));
                ConsigneeName = c.getString(c.getColumnIndex("ConsigneeName"));
                CustomerMasterId = c.getString(c.getColumnIndex("CustomerMasterId"));
                DeliveryTerms = c.getString(c.getColumnIndex("DeliveryTerms"));
                custMobile = c.getString(c.getColumnIndex("Mobile"));
                tax = c.getString(c.getColumnIndex("TotTaxAmt"));
                //Range = c.getString(c.getColumnIndex("Range"));

                String q = "Select FreeAboveAmt,FreeDelyMaxDist,MinDelyKg,MinDelyKm,distance from "+
                        DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE+" Where SOHeaderId='"+SOHeaderId+"'";
                Cursor cq = sql_db.rawQuery(q,null);
                if(cq.getCount()>0){
                    cq.moveToFirst();
                    FreeAboveAmt = cq.getString(cq.getColumnIndex("FreeAboveAmt"));
                    FreeDelyMaxDist = cq.getString(cq.getColumnIndex("FreeDelyMaxDist"));
                    MinDelyKg = cq.getString(cq.getColumnIndex("MinDelyKg"));
                    MinDelyKm = cq.getString(cq.getColumnIndex("MinDelyKm"));
                    distance = cq.getString(cq.getColumnIndex("distance"));
                }

                OrderHistoryBean historybean = new OrderHistoryBean();
                historybean.setSOHeaderId(SOHeaderId);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(TotalOrderValue));
                historybean.setDoAck(DoAck);
                historybean.setSONo(sono);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setCustomerMasterId(CustomerMasterId);
                historybean.setMobile(custMobile);
                historybean.setDeliveryTerms(DeliveryTerms);
                historybean.setFreeAboveAmt(FreeAboveAmt);
                historybean.setFreeDelyMaxDist(FreeDelyMaxDist);
                historybean.setMinDelyKm(MinDelyKm);
                historybean.setMinDelyKg(MinDelyKg);
                historybean.setDistance(distance);
                historybean.setTax(tax);

                listSO.add(historybean);

                /*Set<OrderHistoryBean> set = new TreeSet<OrderHistoryBean>(new Comparator<OrderHistoryBean>() {
                    @Override
                    public int compare(OrderHistoryBean o1, OrderHistoryBean o2) {
                        String a = o1.getSONo();
                        String b = o2.getSONo();
                        Log.e("", "" + a + " " + b);
                        if (o1.getSONo().equalsIgnoreCase(o2.getSONo())) {
                            return 0;
                        }
                        return 1;
                    }
                });

                set.addAll(listSO);

                */

                Collections.sort(listSO, new Comparator<OrderHistoryBean>() {
                    @Override
                    public int compare(OrderHistoryBean lhs, OrderHistoryBean rhs) {
                        return rhs.getDoAck().compareTo(lhs.getDoAck());
                    }
                });


            }while (c.moveToNext());

            soAdapter = new SOApprListAdapter(this,listSO);
            listso.setAdapter(soAdapter);
            soAdapter.notifyDataSetChanged();

            if(listSO.size() == 0){
           //     txtnoordnote.setVisibility(View.VISIBLE);
                listso.setVisibility(View.GONE);
            }else {
             //   txtnoordnote.setVisibility(View.GONE);
                listso.setVisibility(View.VISIBLE);
            }
        }
    }

    public void MakeCall(String mobile){
        numTomakeCall = mobile;

        try{
            if (ActivityCompat.checkSelfPermission(SOApproveActivity.this.getApplicationContext(), Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91"+mobile));
                startActivity(callIntent);
            }
            else
            {
                ActivityCompat.requestPermissions(SOApproveActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode==0 && ActivityCompat.checkSelfPermission(SOApproveActivity.this.getApplicationContext(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = "tel:+91"+numTomakeCall;
            i.setData(Uri.parse(p));
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //listSO.remove(posToRemove);
        new GetOpenSOList().execute();
    }

}
