package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import com.vritti.sales.adapters.CategoryAdapter;
import com.vritti.sales.adapters.OpenOrderListAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.beans.URL_Company_Domain;
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
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TreeSet;

//-------------------------------------Customer Main Activity------------------------------------------//
public class MainActivity extends AppCompatActivity /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private static Context parent;
    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<OrderHistoryBean> historyBeanList;
    static ArrayList<OrderHistoryBean> newList = null;
    List<OrderHistoryBean> al;
    public static ArrayList<MyCartBean> arrayList_bean;
    private int backpressCount = 0;
    private AllCatSubcatItems bean;
    private static String json;
    private ListView listview;
    private long back_pressed = 0;
    private static DatabaseHandlers databaseHelper;
    static ProgressHUD progress;
    NavigationView navigationView;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    LinearLayout lay_category;

    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;
    public static String Mobilenumber;
    TextView txtusername, txtmobileno, txtownername;
    ToggleButton account_view_icon_button;
    String message, ataaleliurl;
    ArrayList<URL_Company_Domain> URL_list;
    //ArrayList<String> URL_list;
    private URL_Company_Domain bean_url;
   // private DatabaseHelper_URLStore db_URLStore;

  //  URL_ListAdapter urlAdapter;
    ListView navheader_accountslist;
    String CustVendorMasterId, CustomerID;
    String res = "";

    //viewpager code
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    int[] imgflag;
    Button btnimage, btnimg;
    final Handler handler = new Handler();
    public Timer swipeTimer;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    public ImageView iv;
    int i = 0;

    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private ImageSwitcher imageSwitcher;
    private int animationCounter = 1;
    private Handler imageSwitcherHandler;
    LinearLayout recentorderlistlayout, startshopping;
    TextView txt_showmore, noorder;
    static ListView listview_recent_ordered_list;
    ImageView imageview_goto;
    String cat_name, cat_id, custvendorID, PurDigit,subcat_name, subcat_id ;
    //WishListAdapter wAdapter;
    //RecentOrderedListAdapter RAdapter;
    MyCartBean myCartBean;
    static OpenOrderListAdapter myOrderHistoryAdapter;
    private static int newlistCount = 0;
    static TextView pending_ordrcnt;
    String CopmanyURL;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    Connectiondetector cd;
    private String jimageview_mainon_1, jsonRate;
    private ArrayList<Merchants_against_items> merchants_against_itemsArrayList;
    private Merchants_against_items merchants_against_items;
    public static String today;
   // DatabaseHandler dbHandler;
    private static String DateToStr;
    private String DateToString;
    private String DateToString_ack;
    private static int index = 0;
    private Uri imageUri;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        //Image slider one after another
        /*iv = (ImageView) findViewById(R.id.imageview_main);
        i = 0;
        t.start();*/

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Open Orders");

        //actionBarImage = (ImageView)findViewById(R.id.actionBarImage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        Intent intent = getIntent();
        intentFrom = intent.getStringExtra("intentFrom");

        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            txtownername.setVisibility(View.VISIBLE);
        }

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

        getSupportActionBar().setTitle(restoredusername);

        merchants_against_itemsArrayList = new ArrayList<Merchants_against_items>();

        if (restoredText != null) {
            Mobilenumber = restoredText;
            AnyMartData.MOBILE = restoredText;
        }
        if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            /*txtownername.setText("Owner : " + restoredownername);
            txtusername.setText("User : " + restoredusername + " (" + restoredText + ")");*/
            txtmobileno.setVisibility(View.GONE);
        } else if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {

        }

        if (tcf.getAllCatSubcatItemCount(getParent()) > 0) {

            getDataFromServer();
        //    getDataFromDataBase();

        } else {
            //add merchant directly in database
            if (tcf.getMerchantsAgainstItems() > 0) {
                //getDataFromServer_addMerchant();
                Toast.makeText(parent, "Merchent is present", Toast.LENGTH_SHORT).show();
            } else {
                getDataFromServer_addMerchant();
            }

            //get data of category
            getDataFromServer();
        }

        setListeners();
    }

    /////////////////////////////////addmerchant database code ///////////////////////////////

    public static class GetPendingOrderHistoryList extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Pending Orders...");
            progressDialog.show();*/
            /*progress = ProgressHUD.show(parent,
                    "Loading Pending Orders...", false, true, null);*/
        }

        @Override
        protected Void doInBackground(Void... params) {
           /* String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_PENDING_ORDER_HISTORY +
                    "?mobileno=" + AnyMartData.MOBILE +
                    "&handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID +
                    "&index=" + index;*/

            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.METHOD_PENDING_ORDERS_SHIPMENTS +
                    "?mobileno=" + AnyMartData.MOBILE +
                    "&handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID +
                    "&index=" + index;

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
            //progress.dismiss();

            if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
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
            } else if (resp_orderHistory.equalsIgnoreCase("empty")) {
               // noorder.setVisibility(View.VISIBLE);
                Toast.makeText(parent, "No Order Yet..! Please Place Some Order..", Toast.LENGTH_LONG)
                        .show();
            } else if (resp_orderHistory.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "The server is taking too long to respond OR " +
                        "something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else {
                json = resp_orderHistory;
                parseJson_pendingorder(json);
            }
        }
    }

    protected static void parseJson_pendingorder(String json) {
        tcf.clearTable(parent, databaseHelper.TABLE_MY_ORDER_HISTORY);

        /*Utilities.clearTable(parent,
                AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY_HEADERDATA);*/

        historyBeanList.clear();
        //  categoryNameList.clear();
        //noorder.setVisibility(View.GONE);

        sql_db = databaseHelper.getWritableDatabase();

        String Address = null, City = null, ConsigneeName = null, CustomerMasterId = null, ItemMasterId = null,
                Mobile = null, Qty = null, Rate = null, SODate = null, SOHeaderId = null, DODisptch = null,
                DORcvd = null, DoAck = null, status = null, sono = null, NetAmt = null, LineAmt = null, ItemDesc = null,
                merchantid = null, merchantname = null, SODetailId = null, statusname = null, ShipToMasterId = null;

        // JSONArray JArrayItems = new JSONArray();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {

                OrderHistoryBean historybean = new OrderHistoryBean();
                String a = jsonArray.getJSONObject(i).getString("status");
                String placeOrderDate = jsonArray.getJSONObject(i).getString("DoAck");

                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = format.parse(placeOrderDate);
                //DateToStr = toFormat.format(date);
                DateToStr = Format.format(d1);
                // DateToStr = format.format(d1);
                System.out.println(DateToStr);

                Address = jsonArray.getJSONObject(i).getString("Address");
                City = jsonArray.getJSONObject(i).getString("City");
                ConsigneeName = jsonArray.getJSONObject(i).getString("ConsigneeName");
                CustomerMasterId = jsonArray.getJSONObject(i).getString("CustomerMasterId");
                ItemMasterId = jsonArray.getJSONObject(i).getString("ItemMasterId");
                Mobile = jsonArray.getJSONObject(i).getString("Mobile");
                Qty = jsonArray.getJSONObject(i).getString("Qty");
                Rate = jsonArray.getJSONObject(i).getString("Rate");
                SODate = jsonArray.getJSONObject(i).getString("SODate");
                SOHeaderId = jsonArray.getJSONObject(i).getString("SOHeaderId");
                DODisptch = jsonArray.getJSONObject(i).getString("DODisptch");
                DORcvd = jsonArray.getJSONObject(i).getString("DORcvd");
                status = jsonArray.getJSONObject(i).getString("status");
                DoAck = jsonArray.getJSONObject(i).getString("DoAck");
                NetAmt = jsonArray.getJSONObject(i).getString("NetAmt");
                ItemDesc = jsonArray.getJSONObject(i).getString("ItemDesc");
                LineAmt = jsonArray.getJSONObject(i).getString("LineAmt");
                merchantid = jsonArray.getJSONObject(i).getString("merchantid");
                merchantname = jsonArray.getJSONObject(i).getString("merchantname");
                SODetailId = jsonArray.getJSONObject(i).getString("SODetailId");
                sono = jsonArray.getJSONObject(i).getString("sono");
                statusname = jsonArray.getJSONObject(i).getString("statusname");
                ShipToMasterId = jsonArray.getJSONObject(i).getString("ShipToMasterId");

                historybean.setSOHeaderId(SOHeaderId);
                historybean.setConsigneeName(ConsigneeName);
                historybean.setSODate(SODate);
                historybean.setNetAmt(Float.parseFloat(NetAmt));
                historybean.setDoAck(DoAck);
                //historybean.setDoAck(DateToStr);
                historybean.setStatus(status);
                historybean.setStatusname(statusname);
                historybean.setSONo(sono);
                historybean.setAddress(Address);
                historybean.setShipToMasterId(ShipToMasterId);

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

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // getDataFromDatabase_pendingorders();
    }

    private void getDataFromServer_addMerchant() {
        if (NetworkUtils.isNetworkAvailable(parent)) {

                new StartSession_tbuds(MainActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetMerchants().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
           // }
        } else {
            Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    class GetMerchants extends AsyncTask<Void, Void, Void> {
        String responseString = "";
        String resp_addMerchant = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Get Merchants...");
            progressDialog.show();*/
            /*progress = ProgressHUD.show(MainActivity.this,
                    "Get Merchants...", false, true, null);*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_AddMerchant = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_MERCHANT +
                    "?sessionid=" + AnyMartData.SESSION_ID +
                    "&handler=" + AnyMartData.HANDLE +
                    //"&type=" + usertype + "&city=" + AnyMartData.CITY;
                    "&type=" + "V" + "&city=" + AnyMartData.CITY;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_AddMerchant, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_addMerchant = responseString.replaceAll("\\\\", "");

                System.out.println("resp =" + resp_addMerchant);
            } catch (Exception e) {
                resp_addMerchant = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (resp_addMerchant.equalsIgnoreCase("Session Expired")) {
                progress.dismiss();
                if (cd.isConnectingToInternet()) {
                    new StartSession_tbuds(MainActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetMerchants().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (resp_addMerchant.equalsIgnoreCase("error")) {
                progress.dismiss();
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else {
            //    progress.dismiss();
                jsonRate = resp_addMerchant;
                parseJson_addmerchant(jsonRate);
                //parseJson(JSON);
            }
        }
    }

    protected void parseJson_addmerchant(String json) {
        merchants_against_itemsArrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String fromdate = get_date(jsonArray.getJSONObject(i).getString("validfrom"));
                String todate = get_date(jsonArray.getJSONObject(i).getString("validto"));
                String discount = "";
                String MerchantName = jsonArray.getJSONObject(i).getString("MrchtName");
                String MerchantAddress = jsonArray.getJSONObject(i).getString("MerchantAddress");
                String MerchantEmail = jsonArray.getJSONObject(i).getString("MerchantEmail");
                String MerchantMobile = jsonArray.getJSONObject(i).getString("MerchantMobile");

                if (!(jsonArray.getJSONObject(i)
                        .getString("discratemrp").equalsIgnoreCase("NA"))
                        || (!(jsonArray.getJSONObject(i)
                        .getString("discratepercent").equalsIgnoreCase("NA")))) {

                    if (!(jsonArray.getJSONObject(i)
                            .getString("discratemrp").equalsIgnoreCase("NA"))) {

                        discount = jsonArray.getJSONObject(i)
                                .getString("discratemrp") + " ₹";

                    } else if (!(jsonArray.getJSONObject(i)
                            .getString("discratepercent").equalsIgnoreCase("NA"))) {

                        discount = jsonArray.getJSONObject(i)
                                .getString("discratepercent") + " %";
                    }
                } else {
                    discount = "0 ₹";
                }

                if (jsonArray.getJSONObject(i)
                        .getString("validfrom").equalsIgnoreCase("NA") &&
                        jsonArray.getJSONObject(i)
                                .getString("validto").equalsIgnoreCase("NA")
                        && (!(String.valueOf(jsonArray.getJSONObject(i)
                        .getString("MRP"))).equalsIgnoreCase("NA"))) {

                    merchants_against_items = new Merchants_against_items();
                    merchants_against_items.setMerchant_name_two(MerchantName);
                    merchants_against_items.setMerchnat_address(MerchantAddress);
                    merchants_against_items.setMerchnat_email(MerchantEmail);
                    merchants_against_items.setMerchnat_mobile(MerchantMobile);

                    merchants_against_items.setMerchant_name(jsonArray.getJSONObject(i)
                            .getString("custvendorname"));
                    merchants_against_items.setQnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setMinqnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setOffers(discount);
                    merchants_against_items.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                            .getString("MRP")));

                    merchants_against_items.setMerchant_id(jsonArray.getJSONObject(i)
                            .getString("fkvendorid"));
                    if (jsonArray.getJSONObject(i)
                            .getString("freeitemid").equalsIgnoreCase("NA")) {
                        merchants_against_items.setFreeitemid("");
                        merchants_against_items.setFreeitemqty(0);
                        merchants_against_items.setFreeitemname("");
                    } else {
                        merchants_against_items.setFreeitemid(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemqty(jsonArray.getJSONObject(i)
                                .getInt("freeitemqty"));
                        String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemname(freeitemname);

                    }
                    merchants_against_items.setValidfrom(fromdate);

                    merchants_against_items.setValidto(todate);
                    merchants_against_items.setProduct_name(jsonArray.getJSONObject(i)
                            .getString("vendoritemname"));

                    tcf.addMerchants(merchants_against_items.getMerchant_id(),
                            merchants_against_items.getMerchant_name(),
                            merchants_against_items.getQnty(), merchants_against_items.getMinqnty(),
                            merchants_against_items.getOffers(),
                            merchants_against_items.getPrice(),
                            merchants_against_items.getProduct_name(), merchants_against_items.getFreeitemid(),
                            merchants_against_items.getFreeitemqty(), merchants_against_items.getFreeitemname(),
                            merchants_against_items.getValidfrom(), merchants_against_items.getValidto(),
                            merchants_against_items.getMerchant_name_two(), merchants_against_items.getMerchnat_address(),
                            merchants_against_items.getMerchnat_email(), merchants_against_items.getMerchnat_mobile());

                    merchants_against_itemsArrayList.add(merchants_against_items);

                } else if (compare_date(fromdate, todate) == true &&
                        (!(String.valueOf(Float.parseFloat(jsonArray.getJSONObject(i)
                                .getString("MRP")))).equalsIgnoreCase("NA"))) {


                    merchants_against_items = new Merchants_against_items();

                    merchants_against_items.setMerchant_name_two(MerchantName);
                    merchants_against_items.setMerchnat_address(MerchantAddress);
                    merchants_against_items.setMerchnat_email(MerchantEmail);
                    merchants_against_items.setMerchnat_mobile(MerchantMobile);

                    merchants_against_items.setMerchant_name(jsonArray.getJSONObject(i)
                            .getString("custvendorname"));


                    merchants_against_items.setQnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));
                    merchants_against_items.setMinqnty(jsonArray.getJSONObject(i)
                            .getInt("minqty"));


                    merchants_against_items.setOffers(discount);
                    merchants_against_items.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                            .getString("MRP")));

                    merchants_against_items.setMerchant_id(jsonArray.getJSONObject(i)
                            .getString("fkvendorid"));
                    if (jsonArray.getJSONObject(i)
                            .getString("freeitemid").equalsIgnoreCase("NA")) {
                        merchants_against_items.setFreeitemid("");
                        merchants_against_items.setFreeitemqty(0);
                        merchants_against_items.setFreeitemname("");
                    } else {
                        merchants_against_items.setFreeitemid(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemqty(jsonArray.getJSONObject(i)
                                .getInt("freeitemqty"));
                        String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                .getString("freeitemid"));
                        merchants_against_items.setFreeitemname(freeitemname);

                    }
                    merchants_against_items.setValidfrom(fromdate);

                    merchants_against_items.setValidto(todate);
                    merchants_against_items.setProduct_name(jsonArray.getJSONObject(i)
                            .getString("vendoritemname"));

                    tcf.addMerchants(merchants_against_items.getMerchant_id(),
                            merchants_against_items.getMerchant_name(),
                            merchants_against_items.getQnty(),
                            merchants_against_items.getMinqnty(),
                            merchants_against_items.getOffers(),
                            merchants_against_items.getPrice(),
                            merchants_against_items.getProduct_name(),
                            merchants_against_items.getFreeitemid(),
                            merchants_against_items.getFreeitemqty(),
                            merchants_against_items.getFreeitemname(),
                            merchants_against_items.getValidfrom(),
                            merchants_against_items.getValidto(),
                            merchants_against_items.getMerchant_name_two(),
                            merchants_against_items.getMerchnat_address(),
                            merchants_against_items.getMerchnat_email(),
                            merchants_against_items.getMerchnat_mobile());

                    merchants_against_itemsArrayList.add(merchants_against_items);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //getDataFromDataBase_addmerchant();
    }

    public String get_date(String d) {

        String finalDate;
        if (!(d.equals("") || d == null)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

            Date myDate = null;
            try {
                myDate = dateFormat.parse(d);
                //myDate = readFormat.parse(d);
                System.out.println("..........value of my date after conv"+ myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd yyyy");
            finalDate = timeFormat.format(myDate);

           /* String formattedDate = "";
            if( myDate != null ) {
                finalDate = writeFormat.format( myDate );
            }

            System.out.println(finalDate);*/

        } else {
            finalDate = "";
        }

        return finalDate;
    }

    public static boolean compare_date(String fromdate, String todate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("MMM dd yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate))) &&
                    (dfDate.parse(today).before(dfDate.parse(todate)) ||
                            dfDate.parse(today).equals(dfDate.parse(todate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private String getFreeItemname(String id) {
      //  DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
      //  SQLiteDatabase db = db1.getWritableDatabase();
        String que = "Select  ItemName  from "
                + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS +
                " where ItemMasterId='" + id + "'";
        String itemname;
        Cursor c = sql_db.rawQuery(que, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            itemname = c.getString(c.getColumnIndex("ItemName"));
        } else {
            itemname = "";
        }

        return itemname;
    }

    private void initialize() {
        parent = MainActivity.this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout_main);

        listview = (ListView) findViewById(R.id.listview_home_category_list);
        pending_ordrcnt = (TextView) findViewById(R.id.pending_ordrcnt);
        pending_ordrcnt.setText(String.valueOf(index));

        lay_category = (LinearLayout)findViewById(R.id.lay_category);
        lay_category.setVisibility(View.VISIBLE);
        recentorderlistlayout = (LinearLayout) findViewById(R.id.recentorderlistlayout);
        recentorderlistlayout.setVisibility(View.GONE);

        listview_recent_ordered_list = (ListView) findViewById(R.id.listview_recent_ordered_list);

        startshopping = (LinearLayout) findViewById(R.id.startshopping);
        txt_showmore = (TextView) findViewById(R.id.txt_showmore);
        imageview_goto = (ImageView) findViewById(R.id.imageview_goto);

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList_bean = new ArrayList<MyCartBean>();
        historyBeanList = new ArrayList<OrderHistoryBean>();

        URL_list = new ArrayList<URL_Company_Domain>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(MainActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);

    }

    private void getUrlListFromDataBase() {

        // TODO Auto-generated method stub
        URL_list.clear();

       /* databaseHelper = new DatabaseHelper(parent,
                AnyMartDatabaseConstants.DATABASE__NAME_URL);*/

        // sql_db= databaseHelper.getWritableDatabase();

    /*    db_URLStore = new DatabaseHelper_URLStore(parent);
        sql_db = db_URLStore.getWritableDatabase();*/

       /* Cursor c1 = sql_db.rawQuery("Select Url from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c1));
        if(c1.getCount()>0){
            c1.moveToFirst();
            do{
                String urlname = c1.getString(c1.getColumnIndex("Url"));
            }while (c1.moveToNext());
            //url found
        }else {
            //no url found
        }*/

   /*     Cursor c = sql_db.rawQuery("Select distinct Url, DBName, CustVendorMasterId from "
                + AnyMartDatabaseConstants.TABLE_URL_COMPANYDOMAIN, null);
        Log.e("cnt", String.valueOf(c));
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                bean_url = new URL_Company_Domain();
                //bean.setCompanyId(c.getString(c.getColumnIndex("MerchantName")));
                String urlname = c.getString(c.getColumnIndex("Url"));
                String DB_name = c.getString(c.getColumnIndex("DBName"));
                String CustVendorMasterId = c.getString(c.getColumnIndex("CustVendorMasterId"));

                bean_url.setUrlname(c.getString(c.getColumnIndex("Url")));
                bean_url.setDBName(c.getString(c.getColumnIndex("DBName")));
                bean_url.setCustVendorMasterId(c.getString(c.getColumnIndex("CustVendorMasterId")));
                URL_list.add(bean_url);
                //URL_list.add(urlname);
            } while (c.moveToNext());
        } else {

        }

        urlAdapter = new URL_ListAdapter(parent, URL_list);
        navheader_accountslist.setAdapter(urlAdapter);*/
        //navheader_accountslist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListViewHeightBasedOnItems(navheader_accountslist);

       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, URL_list);
        navheader_accountslist.setAdapter(adapter);*/
    }

    private void getDataFromServer() {

        if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession_tbuds(parent, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetCategoryList().execute();
                }
                @Override
                public void callfailMethod(String s) {
                }
            });

        } else {
               Toast.makeText(parent, "No internet connection available...", Toast.LENGTH_LONG).show();
           // callSnackbar();
        }
    }

    public void callSnackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No iternet connection. Please try again later.", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new GetCategoryList().execute();
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson(String json) {
        tcf.clearTable(parent,databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS);
        arrayList.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                //String PricelistId = jsonArray.getJSONObject(i).getString("PricelistId");
                String PricelistId = jsonArray.getJSONObject(i).getString("PricelistHdrId");
                String PricelistRate = jsonArray.getJSONObject(i).getString("PricelistRate");
                String MRP = jsonArray.getJSONObject(i).getString("MRP");

                String storeMRP;
                if(PricelistId.equalsIgnoreCase("")){
                    storeMRP = MRP;
                }else {

                    if(PricelistRate.equalsIgnoreCase("0") ||
                            PricelistRate.equalsIgnoreCase("0.0")){
                        storeMRP = MRP;
                    }else {
                        storeMRP = PricelistRate;
                    }
                }

                tcf.addAllCatSubcatItems(jsonArray.getJSONObject(i).getString(
                        "CategoryId"),
                        jsonArray.getJSONObject(i)
                                .getString("CategoryName"),
                        jsonArray.getJSONObject(i)
                                .getString("SubCategoryId"),
                        jsonArray.getJSONObject(i)
                                .getString("SubCategoryName"),
                        jsonArray.getJSONObject(i)
                                .getString("itemmasterid"), jsonArray.getJSONObject(i)
                                .getString("itemnaame"), "http://test1.ekatm.com/menshirts.jpg",
                        storeMRP,
                        jsonArray.getJSONObject(i).getString("custVendorname"),
                       /* jsonArray.getJSONObject(i).getString("TypeFixedPercent")*/"0",
                        jsonArray.getJSONObject(i).getString("validfrom"),
                        jsonArray.getJSONObject(i).getString("validto"),
                        jsonArray.getJSONObject(i).getString("DisRate"),
                        jsonArray.getJSONObject(i).getString("NetRate"),
                        jsonArray.getJSONObject(i).getString("Freeitemid"),
                        jsonArray.getJSONObject(i).getString("Freeitemqty"),
                        jsonArray.getJSONObject(i).getString("Minqty"),
                        jsonArray.getJSONObject(i).getString("Discratepercent"),
                        jsonArray.getJSONObject(i).getString("DiscrateMRP"),
                        jsonArray.getJSONObject(i).getString("PurDigit"),
                        jsonArray.getJSONObject(i).getString("CustVendorMasterId"),
                        jsonArray.getJSONObject(i).getString("PricelistHdrId"),
                        jsonArray.getJSONObject(i).getString("PricelistRate"));
            }
            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

        String que_ = "Select distinct CategoryId, CategoryName, CustVendorMasterId, PurDigit  from getAllCatSubItem";
        Cursor c_ = sql_db.rawQuery(que_, null);
        Log.d("test", "" + c_.getCount());
        if (c_.getCount() == 1) {
            c_.moveToFirst();
        }

        String que = "Select distinct CategoryId, CategoryName, CustVendorMasterId, PurDigit  from "
                + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS;
        Cursor c = sql_db.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() == 1) {
            c.moveToFirst();

            int subcatcount = 0, itemcount = 0;

            cat_name = c.getString(c.getColumnIndex("CategoryName"));
            cat_id = c.getString(c.getColumnIndex("CategoryId"));

            bean = new AllCatSubcatItems();
            bean.setCategoryId(cat_id);
            bean.setCategoryName(cat_name);

            Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                    + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + "" +
                    " where CategoryName='" + cat_name + "'", null);
            Log.d("test", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                try {
                    do {

                        subcatcount = cursor.getCount();
                        String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                        String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                        bean.setSubCategoryName(subcat_name);
                        bean.setSubCategoryId(subcat_id);
                        bean.setSubcatcount(subcatcount);
                        Cursor cursor1 = sql_db.rawQuery("Select distinct itemmasterid,ItemName,ItemImgPath" +
                                " from " + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS +
                                " where SubCategoryName='" + subcat_name + "' and CategoryName='"
                                + cat_id + "'", null);
                        Log.d("test", "" + cursor1.getCount());
                        if (cursor1.getCount() > 0) {
                            cursor1.moveToFirst();
                            try {
                                do {

                                    itemcount = cursor1.getCount();

                                    bean.setItemMasterId(cursor1.getString(cursor1
                                            .getColumnIndex("itemmasterid")));
                                    bean.setItemName(cursor1.getString(cursor1
                                            .getColumnIndex("ItemName")));

                                    bean.setItemImgPath(cursor1.getString(cursor1
                                            .getColumnIndex("ItemImgPath")));
                                    bean.setItemcount(itemcount);
                                    //  arrayList.add(bean);

                                } while (cursor1.moveToNext());
                            } finally {
                                cursor1.close();
                            }
                        }
                    }
                    while (cursor.moveToNext());
                    cursor.close();

                } finally {

                }
            }
            arrayList.add(bean);
            listview.setAdapter(new CategoryAdapter(parent, arrayList));

           // getSupportActionBar().setTitle("Category");

            lay_category.setVisibility(View.VISIBLE);
            //listview.setVisibility(View.GONE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);

            /////////////////////////////////////////////////////////////////////////////////////

                new StartSession_tbuds(parent, new CallbackInterface() {

                    @Override
                    public void callMethod() {

                        new GetPendingOrderHistoryList().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });

            cat_name = c.getString(c.getColumnIndex("CategoryName"));
            cat_id = c.getString(c.getColumnIndex("CategoryId"));
            custvendorID = c.getString(c.getColumnIndex("CustVendorMasterId"));
            PurDigit = c.getString(c.getColumnIndex("PurDigit"));

            /*if(intentFrom.equalsIgnoreCase("ShipmentComplete")){

            }else {*/
                Intent intent = new Intent(parent, SubCategoryActivity.class);
                intent.putExtra("CategoryId", cat_name);
                intent.putExtra("Category_Id", cat_id);
                intent.putExtra("CustVendorMasterId", custvendorID);
                intent.putExtra("CustomerID",CustomerID);
                intent.putExtra("PurDigit",PurDigit);
                startActivity(intent);
        //    }

        } else if (c.getCount() > 1) {
            c.moveToFirst();

            lay_category.setVisibility(View.VISIBLE);
            //listview.setVisibility(View.VISIBLE);
            recentorderlistlayout.setVisibility(View.GONE);
            listview_recent_ordered_list.setVisibility(View.GONE);
            startshopping.setVisibility(View.GONE);

            try {
                do {
                    int subcatcount = 0;
                    int itemcount = 0;
                     cat_name = c.getString(c.getColumnIndex("CategoryName"));
                     cat_id = c.getString(c.getColumnIndex("CategoryId"));

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);

                    Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                            + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + "" +
                            " where CategoryName='" + cat_name + "'", null);
                    Log.d("test", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        try {
                            do {

                                subcatcount = cursor.getCount();
                                subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                                subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                                bean.setSubCategoryName(subcat_name);
                                bean.setSubCategoryId(subcat_id);
                                bean.setSubcatcount(subcatcount);
                                Cursor cursor1 = sql_db.rawQuery("Select distinct itemMasterId,ItemName,ItemImgPath" +
                                        " from " + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS +
                                        " where SubCategoryName='" + subcat_name + "' and CategoryName='"
                                        + cat_id + "'", null);
                                Log.d("test", "" + cursor1.getCount());
                                if (cursor1.getCount() > 0) {
                                    cursor1.moveToFirst();
                                    try {
                                        do {
                                            itemcount = cursor1.getCount();
                                            Log.e("itemcnt - ", String.valueOf(itemcount));

                                            bean.setItemMasterId(cursor1.getString(cursor1.getColumnIndex("itemmasterid")));
                                            bean.setItemName(cursor1.getString(cursor1
                                                    .getColumnIndex("ItemName")));

                                            bean.setItemImgPath(cursor1.getString(cursor1
                                                    .getColumnIndex("ItemImgPath")));
                                            bean.setItemcount(itemcount);

                                        } while (cursor1.moveToNext());
                                    } finally {
                                        cursor1.close();
                                    }
                                }
                            }
                            while (cursor.moveToNext());
                            cursor.close();

                        } finally {

                        }
                    }
                    arrayList.add(bean);
                } while (c.moveToNext());

                listview.setAdapter(new CategoryAdapter(parent, arrayList));

            } finally {

            }
        }
    }

    private void setListeners() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        arrayList.get(position).getCategoryName();
                Intent intent = new Intent(parent, SubCategoryActivity.class);
                intent.putExtra("CategoryId", arrayList.get(position).getCategoryName());
                intent.putExtra("Category_Id", arrayList.get(position).getCategoryId());
                intent.putExtra("CustVendorMasterId", custvendorID);
                intent.putExtra("CustomerID",CustomerID);
                intent.putExtra("PurDigit",PurDigit);
                startActivity(intent);
               // finish();
            }
        });

        listview_recent_ordered_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, final int position, long id) {

                String SOHeaderID = newList.get(position).getSOHeaderId();
                String status = newList.get(position).getStatus();
                String Consignee = newList.get(position).getConsigneeName();
                String TotalAmt = String.valueOf(newList.get(position).getNetAmt());
                String sono = newList.get(position).getSONo();
                String address = newList.get(position).getAddress();
                String ShipToMasterId = newList.get(position).getShipToMasterId();
                String deliveryAddress = newList.get(position).getAddress();
                String deliveryDate = newList.get(position).getSODate();

                Intent intent_go = new Intent(parent, ShipmentEntryActivity.class);
                intent_go.putExtra("SOHeaderID", SOHeaderID);
                intent_go.putExtra("TotalAmt",TotalAmt);
                intent_go.putExtra("SONO",sono);
                intent_go.putExtra("Consignee",Consignee);
                intent_go.putExtra("OrdType",AnyMartData.Order_Type);
                intent_go.putExtra("ShipToMasterId",ShipToMasterId);
                intent_go.putExtra("DeliveryAddress",deliveryAddress);
                intent_go.putExtra("DeliveryDate",deliveryDate);
                startActivity(intent_go);
                finish();

            }
        });

        txt_showmore.setOnClickListener(new OnClickListener() {
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

        startshopping.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_goto = new Intent(parent, SubCategoryActivity.class);
                intent_goto.putExtra("CategoryId", cat_name);
                intent_goto.putExtra("Category_Id", cat_id);
                intent_goto.putExtra("CustVendorMasterId", custvendorID);
                intent_goto.putExtra("CustomerID",CustomerID);
                intent_goto.putExtra("PurDigit",PurDigit);
              //  intent_goto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_goto);
               // finish();
            }
        });
    }

    public class GetCategoryList extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Categories...");
            progressDialog.show();*/

          //  progress = ProgressHUD.show(parent, "Loading Categories...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_PRODUCT_CATEGORY +
                    "?handler=" + AnyMartData.HANDLE +
                    "&sessionid=" + AnyMartData.SESSION_ID +
                    "&custvenmasterid=" + CustomerID;

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                response_list = responseString.replaceAll("\\\\", "");

                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //  progressDialog.dismiss();
         //   progress.dismiss();

            if (response_list.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession_tbuds(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetCategoryList().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (response_list.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your " +
                        "iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
            } else {
                json = response_list;
                parseJson(json);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Utilities.setCartImage(parent, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tbuds_main_refresh, menu);
        //View m = menu.findItem(R.id.search).getActionView();
        menu.removeItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //  SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        //searchView.setSearchableInfo(info);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.search:
                break;

           /* case R.id.cart1:
                if (databaseHelper.getCartItems() > 0) {
                    Intent intent1 = new Intent(com.vritti.orderbilling.customer.MainActivity.this, WishlistActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart", Toast.LENGTH_LONG).show();
                }
                break;*/

            case R.id.refresh:

          //      startService(new Intent(parent, OrderHistoryRefreshService.class));

                if (NetworkUtils.isNetworkAvailable(parent)) {

                 new StartSession_tbuds(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetCategoryList().execute();
                            }

                            @Override
                            public void callfailMethod(String s) {

                            }
                        });
                } else {
                       Toast.makeText(parent, "No internet connection available...", Toast.LENGTH_LONG).show();
                    //callSnackbar();
                }

                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }

    protected void mySearch(String query) {

        for (AllCatSubcatItems s : arrayList) {
            if (s.getCategoryName().contains(query)) {
                arrayList.add(s);
            }
        }
    }

    private void shareIt() {
//sharing implementation here
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Any Mart");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "\n Now order products with Any Mart application, " +
                "\n \t click here to visit \n\n https://play.google.com/store/apps/details?id=com.vritti.orderbilling");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(this, Sales_HomeSActivity.class));
        finish();
    }
}
