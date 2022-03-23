package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.crm.classes.ProgressHUD;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import com.vritti.sales.adapters.ItemListAdapter_customer;
import com.vritti.sales.adapters.listcopyAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** Created by Chetana **/

//-------------------------Customer Item List--------------------------------------------//
public class ItemListActivity extends AppCompatActivity {
    private Context parent;
    private boolean doubleBackToExitPressedOnce;
    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<AllCatSubcatItems> checkedarrayList;

    private ArrayList<Merchants_against_items> arrayList_itemdetails;
    private ArrayList<Merchants_against_items> arrayList_two;
    private ArrayList<Merchants_against_items> array_List_merchant;
    boolean isInternetPresent;
    private AllCatSubcatItems bean;
    public Merchants_against_items bean_itemdetails;
    public MyCartBean myCartBean;
    private String json, json_Item_details;
    Connectiondetector cd;
    MenuItem m, refresh;
    public String restoredText;
    public static final String MyPREFERENCES = "LoginPrefs";
    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;
    public static String Mobilenumber;
    String Subcatid;
    String SubCatName;
    String CategoryName;
    String custvendorID,CustomerID;
    ProgressHUD progress;

    private GridView listview;
    Button button_chkproceed;
    private String[] user;
    String abc;
    ListView listview_itemList;
    String product_name, product_id, product_img;
    public static String today;
    String res = "";

    NavigationView navigationView;
    TextView txtownername;
    static int year, month, day;
    public static String date = null,yesterday =null;

    JSONArray jsonArray;
    JSONObject json_order;
    public static ArrayList<MyCartBean> arrayList_bean;
    MyCartBean bean_wishlist;
    String DateToStr,yesterday_DTS, last_week_day ;
    ItemListAdapter_customer adt;
    private String PurDigit;
    private String image_URL;

    ListView listView;
    View convertView;
    listcopyAdapter ldpt;
    private AlertDialog.Builder builder;
    private AlertDialog myDialog;
    JSONArray JArrayItems;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Products");
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        Intent intent = getIntent();

        Subcatid = intent.getStringExtra("SubCategoryId");
        SubCatName = intent.getStringExtra("SubCategoryName");
        CategoryName = intent.getStringExtra("CategoryId");
        custvendorID = intent.getStringExtra("CustVendorMasterId");
        CustomerID = intent.getStringExtra("CustomerID");
        PurDigit = intent.getStringExtra("PurDigit");

        AnyMartData.SUbcatID = Subcatid;
        AnyMartData.CategoryID = CategoryName;

        if (NetworkUtils.isNetworkAvailable(parent)) {
          //  startService(new Intent(parent,OrderHistoryRefreshService.class));

        } else {
        }

        Picasso.with(parent)
                .load(image_URL)//Your image link url
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        //BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                        BitmapDrawable d = new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(bitmap, 110, 50, true));
                      //  ab.setIcon(d);
                      //  ab.setDisplayShowHomeEnabled(true);
                        //ab.setDisplayHomeAsUpEnabled(true);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }
                });

        if (restoredText != null) {
            Mobilenumber = restoredText;
            AnyMartData.MOBILE = restoredText;
        }

        if (tcf.getAllCatSubcatItemCount(getParent()) > 0) {
            getDataFromDataBase();
        } else {
            getDataFromServer();
        }
        setListeners();

       /* listview_itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                Addtowishlist();
                return true;
            }
        });*/
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("user", "");
    }

   /* private void hideItem() {
        //navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.location).setVisible(false);
        nav_Menu.findItem(R.id.myinfo).setVisible(false);
        nav_Menu.findItem(R.id.notification).setVisible(false);
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
           // Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }

    protected void mySearch(String query) {

        for (AllCatSubcatItems s : arrayList) {
            if (s.getItemName().contains(query)) {
                arrayList.add(s);
            }
        }
        ItemListAdapter_customer adpt = new ItemListAdapter_customer(parent, arrayList);
        listview.setAdapter(adpt);
    }

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /////////////////
        switch (id) {
            case android.R.id.home:
              //  onBackPressed();
                finish();
                return true;

            case  R.id.action_yesterdayorder:
                //showYesterdayOrder();
                showYesterdayOrder_2();
                return true;

            case  R.id.action_lastsamedayorder:
                //showLastSameDayOrder();
                showLastSameDayOrder_2();
                return true;

            case  R.id.action_selectdate:
               // showNewPrompt();
                showNewPrompt_2();
                return true;

            case R.id.refresh:

                if (NetworkUtils.isNetworkAvailable(parent)) {
                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        new GetCategoryList().execute();
                    } else {
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
                }else {
                    //   Toast.makeText(parent, "No internet..", Toast.LENGTH_LONG).show();
                    callSnackbar();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

        //////////////////////////////////////////

    }*/

    //*********************************************************************************************//

    //dialog box if no order is placed on selected date
    public void showNoOrderPlaced() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_ok);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Sorry! No order placed on "+ DateToStr +" date");

        final Button SelectDate = (Button) myDialog
                .findViewById(R.id.btn_selectdate_ok);
        SelectDate.setVisibility(View.GONE);

        final Button btnok = (Button) myDialog
                .findViewById(R.id.copy_btnok);
        btnok.setText("Ok");
        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
            }

        });
        myDialog.show();
    }

    //*************************************************************************************************//

    //copy order by date and time wise
    //yesterday copy orderwise
    public void showYesterdayOrder_2() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_select_date);
        myDialog.setCancelable(true);

        //databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);

        final SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        final LinearLayout layoutdialog = (LinearLayout)myDialog.findViewById(R.id.layoutdialog);
        layoutdialog.setVisibility(View.VISIBLE);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        // quest.setText("Copy yesterday's ("+yesterday_date+") order");

        final Button Yesterday_Date = (Button) myDialog
                .findViewById(R.id.btn_selectdate);
        //  Yesterday_Date.setText("Yesterday");

        final LinearLayout layoutlist = (LinearLayout)myDialog.findViewById(R.id.layoutlist);
        layoutlist.setVisibility(View.GONE);

        final ListView lstview = (ListView) myDialog.findViewById(R.id.sonumbers_list);

        final Date yesterday_date = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        //SimpleDateFormat sdformat = new SimpleDateFormat("MMM dd yyyy hh:mm a");//Feb 23 2016 12:16PM
        SimpleDateFormat sdformat = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
        SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateToStr = sdformat.format(yesterday_date);
        yesterday_DTS = toFormat.format(yesterday_date);
        yesterday_DTS = sdformat.format(yesterday_date);
        //  DateToStr = sdformat.format(yesterday_DTS);
        System.out.println(DateToStr);

        quest.setText("Copy yesterday's order"); /*("+ DateToStr +")*/
        Yesterday_Date.setText(yesterday_DTS);
        Yesterday_Date.setEnabled(false);

        final Button btnok = (Button) myDialog
                .findViewById(R.id.btnok_copy);
        btnok.setText("Proceed");
        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // MergeRepeatedItems();
                //display selected date orderlist
                //  JSONArray JArrayItems = new JSONArray();

                Cursor c_perdgt = sql_db.rawQuery(
                        "SELECT PurDigit FROM " + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS
                        ,null);
                Log.e("ORDER COUNT",c_perdgt.getCount()+"");
                if(c_perdgt.getCount() > 0){
                    c_perdgt.moveToFirst();
                    PurDigit = c_perdgt.getString(c_perdgt.getColumnIndex("PurDigit"));

                }while (c_perdgt.moveToNext());

                Cursor new_c = sql_db.rawQuery(
                        "SELECT * FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        //+ " WHERE Mobile ='"
                        //+ AnyMartData.MOBILE + "' ORDER BY DoAck desc ",
                        ,null);
                //int ordercnt = 0;
                Log.e("ORDER COUNT",new_c.getCount()+"");

                new_c.moveToFirst();

                String date_new = DateToStr;
                arrayList.clear();

                Cursor c_orderHistory_1 = sql_db.rawQuery("SELECT DISTINCT sono, DoAck FROM " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY +
                        " where placeOrderDate like '" + date_new + "%' Order by sono ASC", null);
                Log.d("test", "" + c_orderHistory_1.getCount());

                if (c_orderHistory_1.getCount() > 0) {
                    c_orderHistory_1.moveToFirst();
                    // JArrayItems = new JSONArray();
                    // int itemcount = c_orderHistory.getCount();

                    do {
                        String sono = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("sono"));
                        String DoAck = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("DoAck"));

                        String order_date = dateconvert(DoAck);

                        bean = new AllCatSubcatItems();
                        bean.setSono(sono);
                        bean.setDoAck(order_date);

                        arrayList.add(bean);

            }while (c_orderHistory_1.moveToNext());

                    layoutdialog.setVisibility(View.GONE);
                    layoutlist.setVisibility(View.VISIBLE);
                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    lstview.setAdapter(ldpt);

                   /* myDialog.setContentView(convertView);

                    listView.setVisibility(View.VISIBLE);

                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    listView.setAdapter(ldpt);*/

        } else {
            myDialog.dismiss();
            showNoOrderPlaced();
            Toast.makeText(parent, "No order placed on " + DateToStr + " date", Toast.LENGTH_LONG).show();
        }
            }
        });

        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {

                String sono_pos = arrayList.get(position).getSono();
                Toast.makeText(parent,"clicked order no "+sono_pos ,Toast.LENGTH_SHORT).show();

                //get data of selected sono only

                Cursor c_products = sql_db.rawQuery("select * from " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " where sono ='" + sono_pos + "'",null);
                Log.d("sono", "" + c_products.getCount());

                if(c_products.getCount()>0){
                    c_products.moveToFirst();
                    JArrayItems = new JSONArray();
                    int itemcount = c_products.getCount();

                    do {
                        JSONObject jOBJ = new JSONObject();

                        try {

                            boolean dup= false;
                            int dupindex = 0;

                            String itemName = c_products.getString(c_products.getColumnIndex("ItemDesc"));
                            String itemID = c_products.getString(c_products.getColumnIndex("ItemMasterId"));
                            Float Qty = Float.valueOf(c_products.getString(c_products.getColumnIndex("Qty")));
                            Float Amount = Float.valueOf(c_products.getString(c_products.getColumnIndex("LineAmt")));
                            Float Rate = Float.valueOf(c_products.getString(c_products.getColumnIndex("Rate")));
                            String Merchant_Name = c_products.getString(c_products.getColumnIndex("merchantname"));
                            String Merchant_id = c_products.getString(c_products.getColumnIndex("merchantid"));
                            String sono = c_products.getString(c_products.getColumnIndex("sono"));
                            String DoAck = c_products.getString(c_products.getColumnIndex("DoAck"));

                            String order_date = dateconvert(DoAck);

                            for (int j = 0; j< JArrayItems.length() ; j++){
                                String id2 = JArrayItems.getJSONObject(j).getString("ItemId");
                                if(id2.equals(itemID)){
                                    dup = true;
                                    dupindex =j;
                                    Qty = Qty+ Float.valueOf(JArrayItems.getJSONObject(j).getString("Qty"));
                                    Amount = Amount+Float.valueOf(JArrayItems.getJSONObject(j).getString("TotalAmount"));
                                }else {
                                    dup = false;

                                }
                            }
                            //pass jsonArray to onclick listener
                            if(dup==true){
                                JSONObject itemupdate = JArrayItems.getJSONObject(dupindex);
                                itemupdate.put("ItemName",itemName);
                                itemupdate.put("ItemId",itemID);
                                itemupdate.put("Qty",Qty);
                                itemupdate.put("TotalAmount",Amount);
                                itemupdate.put("itemmrp",Rate);
                                itemupdate.put("custVendorname",Merchant_Name);
                                itemupdate.put("CustVendorMasterId",Merchant_id);
                            }else{
                                jOBJ.put("ItemName",itemName);
                                jOBJ.put("ItemId",itemID);
                                jOBJ.put("Qty",Qty);
                                jOBJ.put("TotalAmount",Amount);
                                jOBJ.put("itemmrp",Rate);
                                jOBJ.put("custVendorname",Merchant_Name);
                                jOBJ.put("CustVendorMasterId",Merchant_id);

                                JArrayItems.put(jOBJ);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }while (c_products.moveToNext());

                }else {
                    //no products
                }

                myDialog.dismiss();

                Intent intent1 = new Intent(ItemListActivity.this, ProductList_TabActivity.class);
                Bundle b = new Bundle();
                b.putString("user", JArrayItems.toString());
                b.putString("SubCategoryId",Subcatid);
                b.putString("PurDigit", PurDigit);
                intent1.putExtras(b);
                startActivity(intent1);
        //        overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
               // finish();

                //myDialog.dismiss();
            }
        });

       /*myDialog = builder.create();
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myDialog.show();*/

        myDialog.show();
        Window window = myDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //last week's order copy
    public void showLastSameDayOrder_2() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_last_week_copy_order);
        myDialog.setCancelable(true);

        //databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        final SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        final LinearLayout layoutdialog = (LinearLayout)myDialog.findViewById(R.id.layoutdialog);
        layoutdialog.setVisibility(View.VISIBLE);

        final TextView txttoday = (TextView)myDialog.findViewById(R.id.textMsg_today);
        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);

        // quest.setText("Copy yesterday's ("+yesterday_date+") order");

        final Button Lastweek_sameday = (Button) myDialog
                .findViewById(R.id.lastweek_sameday);

        final LinearLayout layoutlist = (LinearLayout)myDialog.findViewById(R.id.layoutlist);
        layoutlist.setVisibility(View.GONE);

        final ListView lstview = (ListView) myDialog.findViewById(R.id.sonumbers_list);
        //  Yesterday_Date.setText("Yesterday");

        Date today_date = new Date();
        final Date last_week_date = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24*7));
        //SimpleDateFormat sdformat = new SimpleDateFormat("MMM dd yyyy hh:mm a");  //Feb 23 2016 12:16PM
        SimpleDateFormat sdformat = new SimpleDateFormat("MMM dd yyyy");    //Feb 23 2016 12:16PM
        SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");    //
        String todaydate = toFormat.format(today_date);
        DateToStr = sdformat.format(last_week_date);
        last_week_day = toFormat.format(last_week_date);
        last_week_day = sdformat.format(last_week_date);
        String dayOfTheWeek = sdf.format(last_week_date);
        System.out.println(DateToStr);

        txttoday.setText("Today is "+ dayOfTheWeek + ": "+todaydate);
        /*quest.setText("Copy last week's "+ dayOfTheWeek + "(" + DateToStr + ")" +" order");*/
        quest.setText("Copy last "+ dayOfTheWeek + " order");/*"(" + DateToStr + ")" +*/
        Lastweek_sameday.setText(last_week_day);
        Lastweek_sameday.setEnabled(false);

        final Button btnok = (Button) myDialog
                .findViewById(R.id.btnok_copy);
        btnok.setText("Proceed");
        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // MergeRepeatedItems();
                //display selected date orderlist
                //  JSONArray JArrayItems = new JSONArray();

                Cursor c_perdgt = sql_db.rawQuery(
                        "SELECT PurDigit FROM " + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS
                        ,null);
                Log.e("ORDER COUNT",c_perdgt.getCount()+"");
                if(c_perdgt.getCount() > 0){
                    c_perdgt.moveToFirst();
                    PurDigit = c_perdgt.getString(c_perdgt.getColumnIndex("PurDigit"));

                }while (c_perdgt.moveToNext());

                Cursor new_c = sql_db.rawQuery(
                        "SELECT * FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        //+ " WHERE Mobile ='"
                        //+ AnyMartData.MOBILE + "' ORDER BY DoAck desc ",
                        ,null);
                //int ordercnt = 0;
                Log.e("ORDER COUNT",new_c.getCount()+"");

                new_c.moveToFirst();

                String date_new = DateToStr;
                arrayList.clear();

                Cursor c_orderHistory_1 = sql_db.rawQuery("SELECT DISTINCT sono, DoAck FROM " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY +
                        " where placeOrderDate like '" + date_new + "%' Order by sono ASC", null);
                Log.d("test", "" + c_orderHistory_1.getCount());

                if (c_orderHistory_1.getCount() > 0) {
                    c_orderHistory_1.moveToFirst();
                    // JArrayItems = new JSONArray();
                    // int itemcount = c_orderHistory.getCount();

                    do {
                        String sono = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("sono"));
                        String DoAck = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("DoAck"));

                        String order_date = dateconvert(DoAck);

                        bean = new AllCatSubcatItems();
                        bean.setSono(sono);
                        bean.setDoAck(order_date);

                        arrayList.add(bean);

                    }while (c_orderHistory_1.moveToNext());

                    layoutdialog.setVisibility(View.GONE);
                    layoutlist.setVisibility(View.VISIBLE);
                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    lstview.setAdapter(ldpt);

                    /*myDialog.setContentView(convertView);

                    listView.setVisibility(View.VISIBLE);

                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    listView.setAdapter(ldpt);*/

                } else {
                    myDialog.dismiss();
                    showNoOrderPlaced();
                    Toast.makeText(parent, "No order placed on " + DateToStr + " date", Toast.LENGTH_LONG).show();
                }
            }
        });

        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {

                String sono_pos = arrayList.get(position).getSono();
                Toast.makeText(parent,"clicked order no "+sono_pos ,Toast.LENGTH_SHORT).show();

                //get data of selected sono only

                Cursor c_products = sql_db.rawQuery("select * from " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " where sono ='" + sono_pos + "'",null);
                Log.d("sono", "" + c_products.getCount());

                if(c_products.getCount()>0){
                    c_products.moveToFirst();
                    JArrayItems = new JSONArray();
                    int itemcount = c_products.getCount();

                    do {
                        JSONObject jOBJ = new JSONObject();

                        try {

                            boolean dup= false;
                            int dupindex = 0;

                            String itemName = c_products.getString(c_products.getColumnIndex("ItemDesc"));
                            String itemID = c_products.getString(c_products.getColumnIndex("ItemMasterId"));
                            Float Qty = Float.valueOf(c_products.getString(c_products.getColumnIndex("Qty")));
                            Float Amount = Float.valueOf(c_products.getString(c_products.getColumnIndex("LineAmt")));
                            Float Rate = Float.valueOf(c_products.getString(c_products.getColumnIndex("Rate")));
                            String Merchant_Name = c_products.getString(c_products.getColumnIndex("merchantname"));
                            String Merchant_id = c_products.getString(c_products.getColumnIndex("merchantid"));
                            String sono = c_products.getString(c_products.getColumnIndex("sono"));
                            String DoAck = c_products.getString(c_products.getColumnIndex("DoAck"));

                            String order_date = dateconvert(DoAck);

                            for (int j = 0; j< JArrayItems.length() ; j++){
                                String id2 = JArrayItems.getJSONObject(j).getString("ItemId");
                                if(id2.equals(itemID)){
                                    dup = true;
                                    dupindex =j;
                                    Qty = Qty+ Float.valueOf(JArrayItems.getJSONObject(j).getString("Qty"));
                                    Amount = Amount+Float.valueOf(JArrayItems.getJSONObject(j).getString("TotalAmount"));
                                }else {
                                    dup = false;

                                    /*bean = new AllCatSubcatItems();
                                    bean.setSono(sono);
                                    bean.setDoAck(order_date);

                                    arrayList.add(bean);*/

                                }
                            }
                            //pass jsonArray to onclick listener
                            if(dup==true){
                                JSONObject itemupdate = JArrayItems.getJSONObject(dupindex);
                                itemupdate.put("ItemName",itemName);
                                itemupdate.put("ItemId",itemID);
                                itemupdate.put("Qty",Qty);
                                itemupdate.put("TotalAmount",Amount);
                                itemupdate.put("itemmrp",Rate);
                                itemupdate.put("custVendorname",Merchant_Name);
                                itemupdate.put("CustVendorMasterId",Merchant_id);
                            }else{
                                jOBJ.put("ItemName",itemName);
                                jOBJ.put("ItemId",itemID);
                                jOBJ.put("Qty",Qty);
                                jOBJ.put("TotalAmount",Amount);
                                jOBJ.put("itemmrp",Rate);
                                jOBJ.put("custVendorname",Merchant_Name);
                                jOBJ.put("CustVendorMasterId",Merchant_id);

                                JArrayItems.put(jOBJ);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }while (c_products.moveToNext());

                }else {
                    //no products
                }

                myDialog.dismiss();

                Intent intent1 = new Intent(ItemListActivity.this, ProductList_TabActivity.class);
                Bundle b = new Bundle();
                b.putString("user", JArrayItems.toString());
                b.putString("SubCategoryId",Subcatid);
                b.putString("PurDigit", PurDigit);
                intent1.putExtras(b);

                startActivity(intent1);
           //     overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                finish();

                //myDialog.dismiss();
            }
        });

       /*myDialog = builder.create();
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myDialog.show();*/

        myDialog.show();
        Window window = myDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //show order for selected date
    public void showNewPrompt_2() {

        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_datetimecopyorder);
        myDialog.setCancelable(true);

        //databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        final SQLiteDatabase sql_db = databaseHelper.getWritableDatabase();

        final LinearLayout layoutdialog = (LinearLayout)myDialog.findViewById(R.id.layoutdialog);
        layoutdialog.setVisibility(View.VISIBLE);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Copy order from selected date");

        final Button SelectDate = (Button) myDialog
                .findViewById(R.id.btn_selectdate);
        SelectDate.setText("Select Date");

        final LinearLayout layoutlist = (LinearLayout)myDialog.findViewById(R.id.layoutlist);
        layoutlist.setVisibility(View.GONE);

        final ListView lstview = (ListView) myDialog.findViewById(R.id.sonumbers_list);

        SelectDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //pick a date from calender
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ItemListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;

                                try {
                                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                                SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date d1 = null;
                                    d1 = toFormat.parse(date.toString());
                                //DateToStr = toFormat.format(date);
                                DateToStr = Format.format(d1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if (compare_date(date) == true) {

                                        // DateToStr = format.format(d1);
                                        System.out.println(DateToStr);

                                        SelectDate.setText(date);
                                        SelectDate.setText(DateToStr);

                                } else {
                                    SelectDate.setText(date);
                                    SelectDate.setText(DateToStr);

                                    Toast.makeText(parent,
                                            "You cannot select a day greater than today!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        final Button btnok = (Button) myDialog
                .findViewById(R.id.btnok_copy);
        btnok.setText("Proceed");
        btnok.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //MergeRepeatedItems();
                //display selected date orderlist
                //JSONArray JArrayItems = new JSONArray();

                Cursor c_perdgt = sql_db.rawQuery(
                        "SELECT PurDigit FROM " + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS
                        , null);
                Log.e("ORDER COUNT", c_perdgt.getCount() + "");
                if (c_perdgt.getCount() > 0) {
                    c_perdgt.moveToFirst();
                    PurDigit = c_perdgt.getString(c_perdgt.getColumnIndex("PurDigit"));

                }
                while (c_perdgt.moveToNext()) ;

                Cursor new_c = sql_db.rawQuery(
                        "SELECT * FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY
                        //+ " WHERE Mobile ='"
                        //+ AnyMartData.MOBILE + "' ORDER BY DoAck desc ",
                        , null);
                //int ordercnt = 0;
                Log.e("ORDER COUNT", new_c.getCount() + "");

                new_c.moveToFirst();

                String date_new = DateToStr;
                arrayList.clear();

                Cursor c_orderHistory_1 = sql_db.rawQuery("SELECT DISTINCT sono, DoAck FROM " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY +
                        " where placeOrderDate like '" + date_new + "%' Order by sono ASC", null);
                Log.d("test", "" + c_orderHistory_1.getCount());

                if (c_orderHistory_1.getCount() > 0) {
                    c_orderHistory_1.moveToFirst();
                    // JArrayItems = new JSONArray();
                    // int itemcount = c_orderHistory.getCount();

                    do {
                        String sono = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("sono"));
                        String DoAck = c_orderHistory_1.getString(c_orderHistory_1.getColumnIndex("DoAck"));

                        String order_date = dateconvert(DoAck);

                        bean = new AllCatSubcatItems();
                        bean.setSono(sono);
                        bean.setDoAck(order_date);

                        arrayList.add(bean);

                    }while (c_orderHistory_1.moveToNext());

                    layoutdialog.setVisibility(View.GONE);
                    layoutlist.setVisibility(View.VISIBLE);
                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    lstview.setAdapter(ldpt);

                   /* myDialog.setContentView(convertView);

                    listView.setVisibility(View.VISIBLE);

                    listcopyAdapter ldpt = new listcopyAdapter(parent, arrayList);
                    listView.setAdapter(ldpt);*/

                } else {
                    myDialog.dismiss();
                    showNoOrderPlaced();
                    Toast.makeText(parent, "No order placed on " + DateToStr + " date", Toast.LENGTH_LONG).show();
                }
            }
        });

       lstview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {

                String sono_pos = arrayList.get(position).getSono();
                Toast.makeText(parent,"clicked order no "+sono_pos ,Toast.LENGTH_SHORT).show();

                //get data of selected sono only

                Cursor c_products = sql_db.rawQuery("select * from " +
                        AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY + " where sono ='" + sono_pos + "'",null);
                Log.d("sono", "" + c_products.getCount());

                if(c_products.getCount()>0){
                    c_products.moveToFirst();
                    JArrayItems = new JSONArray();
                    int itemcount = c_products.getCount();

                   do {
                        JSONObject jOBJ = new JSONObject();

                        try {

                            boolean dup= false;
                            int dupindex = 0;

                            String itemName = c_products.getString(c_products.getColumnIndex("ItemDesc"));
                            String itemID = c_products.getString(c_products.getColumnIndex("ItemMasterId"));
                            Float Qty = Float.valueOf(c_products.getString(c_products.getColumnIndex("Qty")));
                            Float Amount = Float.valueOf(c_products.getString(c_products.getColumnIndex("LineAmt")));
                            Float Rate = Float.valueOf(c_products.getString(c_products.getColumnIndex("Rate")));
                            String Merchant_Name = c_products.getString(c_products.getColumnIndex("merchantname"));
                            String Merchant_id = c_products.getString(c_products.getColumnIndex("merchantid"));
                            String sono = c_products.getString(c_products.getColumnIndex("sono"));
                            String DoAck = c_products.getString(c_products.getColumnIndex("DoAck"));

                            String order_date = dateconvert(DoAck);

                            for (int j = 0; j< JArrayItems.length() ; j++){
                                String id2 = JArrayItems.getJSONObject(j).getString("ItemId");
                                if(id2.equals(itemID)){
                                    dup = true;
                                    dupindex =j;
                                    Qty = Qty+ Float.valueOf(JArrayItems.getJSONObject(j).getString("Qty"));
                                    Amount = Amount+Float.valueOf(JArrayItems.getJSONObject(j).getString("TotalAmount"));
                                }else {
                                    dup = false;

                                }
                            }
                            //pass jsonArray to onclick listener
                            if(dup==true){
                                JSONObject itemupdate = JArrayItems.getJSONObject(dupindex);
                                itemupdate.put("ItemName",itemName);
                                itemupdate.put("ItemId",itemID);
                                itemupdate.put("Qty",Qty);
                                itemupdate.put("TotalAmount",Amount);
                                itemupdate.put("itemmrp",Rate);
                                itemupdate.put("custVendorname",Merchant_Name);
                                itemupdate.put("CustVendorMasterId",Merchant_id);
                            }else{
                                jOBJ.put("ItemName",itemName);
                                jOBJ.put("ItemId",itemID);
                                jOBJ.put("Qty",Qty);
                                jOBJ.put("TotalAmount",Amount);
                                jOBJ.put("itemmrp",Rate);
                                jOBJ.put("custVendorname",Merchant_Name);
                                jOBJ.put("CustVendorMasterId",Merchant_id);

                                JArrayItems.put(jOBJ);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }while (c_products.moveToNext());

                }else {
                    //no products
                }

                myDialog.dismiss();

                  Intent intent1 = new Intent(ItemListActivity.this, ProductList_TabActivity.class);
                    Bundle b = new Bundle();
                    b.putString("user", JArrayItems.toString());
                    b.putString("SubCategoryId",Subcatid);
                    b.putString("PurDigit", PurDigit);
                    intent1.putExtras(b);

                    startActivity(intent1);
               //     overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                    finish();

               //myDialog.dismiss();
            }
        });

       /*myDialog = builder.create();
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        myDialog.show();*/

        myDialog.show();
        Window window = myDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //**************************************************************************************************//

        public String dateconvert(String Date_to_convert){

        SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy hh:mm a");//Feb 23 2016 12:16PM
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date d1 = null;

        try {
            //d1 = format.parse(DoAck);
            d1 = format.parse(Date_to_convert);
            //DateToStr = toFormat.format(date);
            DateToStr = Format.format(d1);
            // DateToStr = format.format(d1);
            System.out.println(DateToStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateToStr;
    }

    //dialog box to select date

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        m = menu.findItem(R.id.miActionProgress);
        refresh = menu.findItem(R.id.refresh);
        return super.onPrepareOptionsMenu(menu);
    }*/

    public void showProgressBar() {
        // Show progress item
        refresh.setVisible(false);
        m.setVisible(true);

    }

    public void hideProgressBar() {
        // Hide progress item
        m.setVisible(false);
        refresh.setVisible(true);

    }

    private void initialize() {
        parent = ItemListActivity.this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ItemListActivity.this);
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

       // listview = (GridView) findViewById(R.id.listview_item_list);
        listview_itemList = (ListView)findViewById(R.id.listview_item_list);
        button_chkproceed = findViewById(R.id.button_chkproceed);

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList_bean = new ArrayList<MyCartBean>();
        arrayList_two = new ArrayList<Merchants_against_items>();
        checkedarrayList = new ArrayList<AllCatSubcatItems>();

        LayoutInflater inflater = getLayoutInflater();
        /*final View*/ convertView = (View) inflater.inflate(R.layout.tbuds_custom, null);

       /* final ListView*/ listView = (ListView)convertView.findViewById(R.id.sonumbers_list);

        cd = new Connectiondetector(ItemListActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        //    databaseHelper = new DatabaseHelper(ItemListActivity.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        image_URL = sharedpreferences.getString("logopath",null);

        //on click of button proceed

        button_chkproceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String ItemId = null;
                String ItemName = null;
                String Merchant_id = null;
                String Merchant_Name = null;
                PurDigit = null;
                float Rate;
                float ToatlAmount;
                float qty ;
                int i=0;

                arrayList = ((ItemListAdapter_customer) listview_itemList.getAdapter()).getAllCatSubcatItemsList();
                if (arrayList.size() > 0) {

                    JSONArray jsonArray1 = new JSONArray();
                    AllCatSubcatItems items = arrayList.get(i);

                    if (arrayList.size() > 0) {

                        user = new String[arrayList.size()];

                        for ( i = 0; i < arrayList.size(); i++) {

                            ItemName = arrayList.get(i).getItemName();
                            ItemId = arrayList.get(i).getItemMasterId();
                            qty = arrayList.get(i).getEdtQty();
                            ToatlAmount = arrayList.get(i).getTotalAmount();
                            Rate = arrayList.get(i).getPrice();
                            Merchant_Name = arrayList.get(i).getMerchant_name();
                            Merchant_id = arrayList.get(i).getMerchant_id();
                            PurDigit = arrayList.get(i).getPerDigit();

                            JSONObject jsonObject = new JSONObject();

                            try {

                                jsonObject.put("ItemName",ItemName);
                                jsonObject.put("ItemId",ItemId);
                                jsonObject.put("Qty",qty);
                                jsonObject.put("TotalAmount",ToatlAmount);
                                jsonObject.put("itemmrp",Rate);
                                jsonObject.put("custVendorname",Merchant_Name);
                                jsonObject.put("CustVendorMasterId",Merchant_id);
                                jsonObject.put("PurDigit", PurDigit);
                                //price
                                jsonArray1.put(jsonObject);

                                user[i] = jsonObject.toString();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //user[i] = jsonObject.toString();
                           //user[i] = jsonArray1.toString();
                        }

                        //Intent intent1 = new Intent(ItemListActivity.this, ProductListActivity.class);
                        Intent intent1 = new Intent(ItemListActivity.this, ProductList_TabActivity.class);
                        Bundle b = new Bundle();
                        //b.putStringArray("user", user);
                        b.putString("user", jsonArray1.toString());
                        b.putString("SubCategoryId",Subcatid);
                        b.putString("PurDigit",PurDigit);
                        //Toast.makeText(getApplicationContext(),ItemName+" added to Ordered list",Toast.LENGTH_SHORT).show();
                        intent1.putExtras(b);
                      //  intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent1);
                     //   overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                        finish();
                    }
           }else{
                    Toast.makeText(parent,"Cant't proceed, Please fill quantity",Toast.LENGTH_SHORT).show();
                }
        }
        });
    }

    private void getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable(parent)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetCategoryList().execute();
            } else {
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
        } else {
              //Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
            callSnackbar();
        }
    }

    public void callSnackbar() {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No iternet connection. Please try again later.", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
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

                PurDigit = jsonArray.getJSONObject(i).getString("PurDigit");

                tcf.addAllCatSubcatItems(jsonArray.getJSONObject(i).getString(
                        "CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i)
                                .getString("SubCategoryId"),
                        jsonArray.getJSONObject(i)
                                .getString("SubCategoryName"),
                        jsonArray.getJSONObject(i)
                                .getString("itemmasterid"), jsonArray.getJSONObject(i)
                                .getString("itemnaame"), "http://test1.ekatm.com/menshirts.jpg",
                        storeMRP,
                        jsonArray.getJSONObject(i).getString("custVendorname"),
                        /*jsonArray.getJSONObject(i).getString("TypeFixedPercent")*/"0",
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
            //new GetvendorsOnProductname().execute();
            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

    /*    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase db = db1.getWritableDatabase();*/

        int subcatcount = 0;
        int itemcount = 0;

       /* Cursor cursor1 = db.rawQuery("Select distinct CategoryId, CategoryName,SubCategoryName,SubCategoryId," +
                "itemmasterid,ItemName,ItemImgPath, itemMRP,  from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + Subcatid + "'", null);*/

        Cursor cursor1 = sql_db.rawQuery("Select * from "
                + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + Subcatid + "'" + " ORDER BY ItemName ASC", null);

        Log.d("test", "" + cursor1.getCount());
        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            try {
                do {
                    itemcount = cursor1.getCount();
                    String cat_name = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                    String cat_id = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                    String subcat_name = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                    String subcat_id = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));
                    String itemMRP = cursor1.getString(cursor1.getColumnIndex("itemMRP"));
                   // String itemMRP = cursor1.getString(cursor1.getColumnIndex("itemmrp"));
                    String itemmasterID = cursor1.getString(cursor1.getColumnIndex("itemmasterid"));

                    //add price and all details too

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);
                    bean.setSubCategoryName(subcat_name);
                    bean.setSubCategoryId(subcat_id);
                    bean.setItemMasterId(itemmasterID);
                    bean.setItemName(cursor1.getString(cursor1
                            .getColumnIndex("ItemName")));
                    bean.setPrice(Float.parseFloat(itemMRP));
                    bean.setSubcatcount(subcatcount);
                    bean.setItemcount(itemcount);
                    bean.setItemImgPath(cursor1.getString(cursor1
                            .getColumnIndex("ItemImgPath")));
                    bean.setMerchant_id(cursor1.getString(cursor1
                            .getColumnIndex("CustVendorMasterId")));
                    bean.setMerchant_name(cursor1.getString(cursor1
                            .getColumnIndex("custVendorname")));
                    bean.setFreeitemid(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemid")));
                   /* bean.setFreeitemname(cursor1.getString(cursor1
                            .getColumnIndex("freeitemname")));*/
                    bean.setFreeitemqty(Float.parseFloat(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemqty"))));
                    bean.setValidfrom(cursor1.getString(cursor1
                            .getColumnIndex("validfrom")));
                    bean.setValidto(cursor1.getString(cursor1
                            .getColumnIndex("validto")));
                    bean.setPerDigit(cursor1.getString(cursor1
                            .getColumnIndex("PurDigit")));

                    arrayList.add(bean);

                } while (cursor1.moveToNext());
            } finally {
                cursor1.close();
            }
        }

        //listview.setAdapter(new ItemListAdapter_customer(parent, arrayList));
         adt =   new ItemListAdapter_customer(parent, arrayList);
        listview_itemList.setAdapter(adt);

        /*//multiselection gridview
        listview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        listview.setMultiChoiceModeListener(new MultiChoiceModeListener());*/
    }

    private void setListeners() {
/*
        listview_itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        arrayList.get(position).getCategoryName();
                Intent intent = new Intent(parent, ProductListActivity.class);
                intent.putExtra("ItemMasterId", arrayList.get(position).getItemMasterId());
                intent.putExtra("itemname", arrayList.get(position).getItemName());
                intent.putExtra("ItemImgPath", arrayList.get(position).getItemImgPath());
                startActivity(intent);
                overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                finish();
            }
        });
*/
    }

    public class GetCategoryList extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_itemlist = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Categories...");
            progressDialog.show();*/
            progress = ProgressHUD.show(parent,
                    "Loading Item List...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_itemList = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_PRODUCT_CATEGORY +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&custvenmasterid=" + CustomerID;

            String urlItem = url_itemList;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_itemList, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                response_itemlist = responseString.replaceAll("\\\\","");

                System.out.println("resp ="+response_itemlist);
            } catch (Exception e) {
                response_itemlist = "error";
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();

            if (response_itemlist.equalsIgnoreCase("Session Expired")) {
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
            } else if (response_itemlist.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else {
                json = response_itemlist;
                parseJson(json);
            }
        }
    }

    /////////////////getVendorsOnProductName//////////////////////////////

    //dialog box on long click of itemlist

    /*public void Addtowishlist() {
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_wishlist);
        myDialog.setCancelable(true);

        //myDialog.getWindow().setGravity(Gravity.BOTTOM);

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText("Add to wishlist");

        final Button btnyes = (Button) myDialog
                .findViewById(R.id.addtowishlist);
        btnyes.setText("YES");
        btnyes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               //code to add in wishlist
            }
        });

        final Button btnNo = (Button) myDialog
                .findViewById(R.id.cancelbtn);
        btnNo.setText("No");
        btnNo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               //dismiss dialog
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }*/

    //************************************************************************************//

    public class GetOrderDetail extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_getorderdetails = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Categories...");
            progressDialog.show();*/
            progress = ProgressHUD.show(parent,
                    "Loading Item List...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_getOrderDetails = AnyMartData.MAIN_URL +
                    AnyMartData.METHOD_GET_ORDER_DETAILS_TO_COPY_ORDER +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&SODate="+ DateToStr +
                    "&mobileno="+ AnyMartData.MOBILE ;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_getOrderDetails, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL urlGetOrderDetails = new URL(url_getOrderDetails);
                urlConnection = urlGetOrderDetails.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
               // bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_getOrderDetails = new StringBuffer();
                String line;
                StringBuilder str_build_getOrderDetails = new StringBuilder();
                while((line = bufferedReader.readLine())!= null){
                    stringBuff_getOrderDetails = stringBuff_getOrderDetails.append(line);
                }

                responseString = stringBuff_getOrderDetails.toString().replaceAll("^\"|\"$", "");*/
                response_getorderdetails = responseString.replaceAll("\\\\","");

                System.out.println("resp ="+response_getorderdetails);
            } catch (Exception e) {
                response_getorderdetails = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();

            if (response_getorderdetails.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession_tbuds(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetOrderDetail().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (response_getorderdetails.equalsIgnoreCase("error")) {

            } else {
               json = response_getorderdetails;
               parseJson_getOrderDetails(json);
            }
        }
    }

    protected void parseJson_getOrderDetails(String json) {
        tcf.clearTable(parent,databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS);
        arrayList.clear();
        JSONArray JArrayItems = new JSONArray();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i< jsonArray.length(); i++) {

                JSONObject jOBJ = new JSONObject();

                try {
                    boolean dup= false;
                    int dupindex = 0;

                    String itemName = jsonArray.getJSONObject(i).getString("ItemDesc");
                    String itemID = jsonArray.getJSONObject(i).getString("ItemMasterId");
                    Float Qty = Float.valueOf(jsonArray.getJSONObject(i).getString("Qty"));
                    Float Amount = Float.valueOf(jsonArray.getJSONObject(i).getString("LineAmt"));
                    Float Rate = Float.valueOf(jsonArray.getJSONObject(i).getString("Rate"));
                    String Merchant_Name = jsonArray.getJSONObject(i).getString("merchantname");
                    String Merchant_id = jsonArray.getJSONObject(i).getString("merchantid");


                    for (int j = 0; j<JArrayItems.length() ; j++){
                        String id2 = JArrayItems.getJSONObject(j).getString("ItemId");
                        if(id2.equals(itemID)){
                            dup = true;
                            dupindex =j;
                            Qty = Qty+ Float.valueOf(JArrayItems.getJSONObject(j).getString("Qty"));
                            Amount = Amount+Float.valueOf(JArrayItems.getJSONObject(j).getString("TotalAmount"));
                        }
                    }

                    if(dup==true){
                        JSONObject itemupdate = JArrayItems.getJSONObject(dupindex);
                        itemupdate.put("ItemName",itemName);
                        itemupdate.put("ItemId",itemID);
                        itemupdate.put("Qty",Qty);
                        itemupdate.put("TotalAmount",Amount);
                        itemupdate.put("itemmrp",Rate);
                        itemupdate.put("custVendorname",Merchant_Name);
                        itemupdate.put("CustVendorMasterId",Merchant_id);
                    }else{

                        jOBJ.put("ItemName",itemName);
                        jOBJ.put("ItemId",itemID);
                        jOBJ.put("Qty",Qty);
                        jOBJ.put("TotalAmount",Amount);
                        jOBJ.put("itemmrp",Rate);
                        jOBJ.put("custVendorname",Merchant_Name);
                        jOBJ.put("CustVendorMasterId",Merchant_id);

                        JArrayItems.put(jOBJ);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Intent intent1 = new Intent(ItemListActivity.this, ProductList_TabActivity.class);
            Bundle b = new Bundle();
            b.putString("user", JArrayItems.toString());
            b.putString("SubCategoryId",Subcatid);
            b.putString("PurDigit",PurDigit);
            intent1.putExtras(b);

            startActivity(intent1);
         //   overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
            //getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        // Utilities.setCartImage(parent, false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
               // adt.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                // filter recycler view when text is changed
                //adt.getFilter().filter(query);
                adt.filter(query);
                return true;
            }
        });
        return true;

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("TAG", "onQueryTextSubmit ");
                arrayList.clear();

                DatabaseHelper db1 = new DatabaseHelper(parent);
                SQLiteDatabase db = db1.getWritableDatabase();

                int subcatcount = 0;
                int itemcount = 0;

                Cursor cursor1 = db.rawQuery("Select * from "
                        + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where ItemName LIKE  '%" +s+ "%' and  SubCategoryId='" + Subcatid + "'", null);
                Log.d("test", "" + cursor1.getCount());
                if (cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                    try {
                        do {
                            itemcount = cursor1.getCount();
                            String cat_name = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                            String cat_id = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                            String subcat_name = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                            String subcat_id = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));
                            String itemMRP = cursor1.getString(cursor1.getColumnIndex("itemMRP"));

                            bean = new AllCatSubcatItems();
                            bean.setCategoryId(cat_id);
                            bean.setCategoryName(cat_name);
                            bean.setSubCategoryName(subcat_name);
                            bean.setSubCategoryId(subcat_id);
                            bean.setItemMasterId(cursor1.getString(cursor1
                                    .getColumnIndex("itemmasterid")));
                            bean.setItemName(cursor1.getString(cursor1
                                    .getColumnIndex("ItemName")));
                            bean.setSubcatcount(subcatcount);
                            bean.setItemcount(itemcount);
                            bean.setItemImgPath(cursor1.getString(cursor1
                                    .getColumnIndex("ItemImgPath")));
                            bean.setPrice(Float.parseFloat(itemMRP));
                            bean.setMerchant_id(cursor1.getString(cursor1
                                    .getColumnIndex("CustVendorMasterId")));
                            bean.setMerchant_name(cursor1.getString(cursor1
                                    .getColumnIndex("custVendorname")));
                            bean.setFreeitemid(cursor1.getString(cursor1
                                    .getColumnIndex("Freeitemid")));
                   *//* bean.setFreeitemname(cursor1.getString(cursor1
                            .getColumnIndex("freeitemname")));*//*
                            bean.setFreeitemqty(Float.parseFloat(cursor1.getString(cursor1
                                    .getColumnIndex("Freeitemqty"))));
                            bean.setValidfrom(cursor1.getString(cursor1
                                    .getColumnIndex("validfrom")));
                            bean.setValidto(cursor1.getString(cursor1
                                    .getColumnIndex("validto")));
                            bean.setPerDigit(cursor1.getString(cursor1
                                    .getColumnIndex("PurDigit")));
                            arrayList.add(bean);

                        } while (cursor1.moveToNext());
                    } finally {
                        cursor1.close();
                    }
                }

               // listview.setAdapter(new ItemListAdapter_customer(parent, arrayList));
                listview_itemList.setAdapter(new ItemListAdapter_customer(parent, arrayList));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("TAG", "onQueryTextChange ");
                arrayList.clear();
                DatabaseHelper db1 = new DatabaseHelper(parent);
                SQLiteDatabase db = db1.getWritableDatabase();

                int subcatcount = 0;
                int itemcount = 0;

                Cursor cursor1 = db.rawQuery("Select * from "
                        + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where  ItemName LIKE  '%" +s+ "%' and SubCategoryId='" + Subcatid + "'", null);
                Log.d("test", "" + cursor1.getCount());
                if (cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                    try {
                        do {
                            itemcount = cursor1.getCount();
                            String cat_name = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                            String cat_id = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                            String subcat_name = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                            String subcat_id = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));
                            String itemMRP = cursor1.getString(cursor1.getColumnIndex("itemMRP"));

                            bean = new AllCatSubcatItems();
                            bean.setCategoryId(cat_id);
                            bean.setCategoryName(cat_name);
                            bean.setSubCategoryName(subcat_name);
                            bean.setSubCategoryId(subcat_id);
                            bean.setItemMasterId(cursor1.getString(cursor1
                                    .getColumnIndex("itemmasterid")));
                            bean.setItemName(cursor1.getString(cursor1
                                    .getColumnIndex("ItemName")));
                            bean.setSubcatcount(subcatcount);
                            bean.setItemcount(itemcount);
                            bean.setItemImgPath(cursor1.getString(cursor1
                                    .getColumnIndex("ItemImgPath")));
                            bean.setPrice(Float.parseFloat(itemMRP));
                            bean.setMerchant_id(cursor1.getString(cursor1
                                    .getColumnIndex("CustVendorMasterId")));
                            bean.setMerchant_name(cursor1.getString(cursor1
                                    .getColumnIndex("custVendorname")));
                            bean.setFreeitemid(cursor1.getString(cursor1
                                    .getColumnIndex("Freeitemid")));
                   *//* bean.setFreeitemname(cursor1.getString(cursor1
                            .getColumnIndex("freeitemname")));*//*
                            bean.setFreeitemqty(Float.parseFloat(cursor1.getString(cursor1
                                    .getColumnIndex("Freeitemqty"))));
                            bean.setValidfrom(cursor1.getString(cursor1
                                    .getColumnIndex("validfrom")));
                            bean.setValidto(cursor1.getString(cursor1
                                    .getColumnIndex("validto")));
                            bean.setPerDigit(cursor1.getString(cursor1
                                    .getColumnIndex("PurDigit")));
                            arrayList.add(bean);

                        } while (cursor1.moveToNext());
                    } finally {
                        cursor1.close();
                    }
                    listview_itemList.setAdapter(new ItemListAdapter_customer(parent, arrayList));
                }

               // listview.setAdapter(new ItemListAdapter_customer(parent, arrayList));

                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

      //  Intent i = new Intent(parent, com.vritti.orderbilling.customer.MainActivity.class);
       // startActivity(i);
        finish();

        // }
    }

}