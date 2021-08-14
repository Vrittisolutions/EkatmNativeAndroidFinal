package com.vritti.sales.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.data.Factory;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.CartListAdapter;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartStringConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chetana
 */

public class CartActivity extends AppCompatActivity {
    private Context parent;
    private ProgressDialog progressDialog;
    private String json;
    ProgressHUD progress;
    Button button_checkout;
    TextView textview_cart_total_amount;
    MyCartBean myCartBean;
    public static ArrayList<MyCartBean> myCartBeanArrayList;
    private ArrayList<Merchants_against_items> listdisplay;
    LinearLayout linearlayout_cart_container;
    LinearLayout linearlayout_cart_list;
    float paybaleAmt = 0;
    String M_name = "", Merchant_name;
    TextView textview_total_amount;
    TextView textview_delivery_charges_amt, textview_delivery_charges;
    TextView textview_discount;
    public static String today;
    LinearLayout volumefreelayout;
    float cntAmt = 0;
    TextView textview_volume_free_name;
    float flag = 0;
    boolean isData = false;
    int getCount = 0;
    int M_count = 0;
    ListView listDisplay;
    JSONArray jsonArray = null;
    String SubcatID, PurDigit;
    JSONArray jsonArray1;
    private float amt = 0;
    String res = "";

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    SharedPreferences sharedpreferences;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_cart);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       /* final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setTitle(" "+"My Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //actionBarImage = (ImageView)findViewById(R.id.actionBarImage);
        toolbar.setTitle("My Cart");
        // toolbar.setTitleTextColor(0xffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Utilities.darkenStatusBar(this, R.color.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

      //  databaseHelper = new DatabaseHelper(CartActivity.this,AnyMartDatabaseConstants.DATABASE__NAME_URL);

        initialize();

        /*Picasso.with(parent)
                .load(image_URL)//Your image link url
                .into(actionBarImage);*/

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
                    //    ab.setIcon(d);
                    //    ab.setDisplayShowHomeEnabled(true);
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


        Bundle b = getIntent().getExtras();
            String jsonData = b.getString("OrderedItems");
            SubcatID = b.getString("SubCategoryId");
            final String payableAmount = b.getString(String.valueOf("PayableAmount"));

        PurDigit = b.getString("PurDigit");

            try {
                jsonArray = new JSONArray(jsonData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myCartBeanArrayList = new ArrayList<MyCartBean>();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jOBJ = new JSONObject();
                myCartBean = new MyCartBean();

                try {

                    myCartBean.setProduct_name(this.jsonArray.getJSONObject(i)
                            .getString("ItemName"));
                    myCartBean.setProduct_id(this.jsonArray.getJSONObject(i).getString("ItemId"));
                    //myItemId[i]= this.jsonArray.getJSONObject(i).getString("ItemId");
                    myCartBean.setQnty(Float.valueOf(this.jsonArray.getJSONObject(i).getString("Qty")));
                    myCartBean.setAmount(Float.parseFloat(this.jsonArray.getJSONObject(i).getString("TotalAmount")));

                    myCartBean.setPrice(Float.parseFloat(this.jsonArray.getJSONObject(i).getString("itemmrp")));
                    myCartBean.setMerchantName(this.jsonArray.getJSONObject(i).getString("custVendorname"));
                    myCartBean.setMerchantId(this.jsonArray.getJSONObject(i).getString("CustVendorMasterId"));
                    myCartBean.setPerdigit(PurDigit);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myCartBeanArrayList.add(myCartBean);
            }
            linearlayout_cart_container.setVisibility(View.VISIBLE);

            listDisplay.setAdapter(new CartListAdapter(parent, myCartBeanArrayList));
        double amount = Double.parseDouble(payableAmount);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
            textview_cart_total_amount.setText(formatted + " ₹");

        //ordered_list.setAdapter(new ProductListAdapter(parent,myCartBeanArrayList));

        if (NetworkUtils.isNetworkAvailable(parent)) {
            if (tcf.getcartvolumediscountcount() > 0 || tcf.getcartvolumediscountcount() == 0) {
            //    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
            //    SQLiteDatabase db = db1.getWritableDatabase();
                sql_db.execSQL("DROP TABLE IF EXISTS "
                        + DatabaseHandlers.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                sql_db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
              //  getDataFromServer();
                linearlayout_cart_container.setVisibility(View.VISIBLE);
            }

        } else {
            if (tcf.getcartvolumediscountcount() > 0 || tcf.getcartvolumediscountcount() == 0) {
            //    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
            //    SQLiteDatabase db = db1.getWritableDatabase();
                sql_db.execSQL("DROP TABLE IF EXISTS "
                        + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                sql_db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
               // getDataFromDataBase();
                linearlayout_cart_container.setVisibility(View.VISIBLE);
            }
        }

        button_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;

                 jsonArray1 = new JSONArray();
                if (myCartBeanArrayList.size() > 0) {

                    for ( i = 0; i < myCartBeanArrayList.size(); i++) {

                        String ItemName = myCartBeanArrayList.get(i).getProduct_name();
                        String ItemId = myCartBeanArrayList.get(i).getProduct_id();
                        float qty = myCartBeanArrayList.get(i).getQnty();
                        float ToatlAmount = myCartBeanArrayList.get(i).getAmount();
                        float Rate = Float.parseFloat(myCartBeanArrayList.get(i).getPrice());
                        String Merchant_Name = myCartBeanArrayList.get(i).getMerchantName();
                        String Merchant_id = myCartBeanArrayList.get(i).getMerchantId();
                        String perDigit = myCartBeanArrayList.get(i).getPerdigit();

                        JSONObject jsonObject = new JSONObject();

                        try {

                            jsonObject.put("ItemName",ItemName);
                            jsonObject.put("ItemId",ItemId);
                            jsonObject.put("Qty",qty);
                            jsonObject.put("TotalAmount",ToatlAmount);
                            jsonObject.put("itemmrp",Rate);
                            jsonObject.put("custVendorname",Merchant_Name);
                            jsonObject.put("CustVendorMasterId",Merchant_id);
                            jsonObject.put("PurDigit", perDigit);
                            //price
                            jsonArray1.put(jsonObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Bundle b = new Bundle();
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    b.putString("OrderedItems", jsonArray1.toString());
                    b.putString("SubCategoryId",SubcatID);
                    b.putString("PayableAmount",String.valueOf(amt));
                    b.putString("PurDigit", PurDigit);
                    intent.putExtras(b);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                //    overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

///////////////////////////////////////////////////////////////////////////////////////////////

    public void calculate_total(){
        amt =0;
        for (int k = 0; k < myCartBeanArrayList.size(); k++) {

            amt = amt + myCartBeanArrayList.get(k).getAmount();
        }
        float payableAmount = amt;
        double amount = Double.parseDouble(String.valueOf(payableAmount));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_cart_total_amount.setText(formatted+" ₹");
    }


    private void initialize() {

        parent = CartActivity.this;

        progressDialog = new ProgressDialog(parent);
        myCartBeanArrayList = new ArrayList<MyCartBean>();
        linearlayout_cart_container = (LinearLayout) findViewById(R.id.linearlayout_cart_container);
        button_checkout = (Button) findViewById(R.id.button_checkout);
        textview_cart_total_amount = (TextView) findViewById(R.id.textview_cart_total_amount);
        listDisplay = (ListView)findViewById(R.id.listDisplay);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CartActivity.this);
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

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES,Context.MODE_PRIVATE);

        image_URL = sharedpreferences.getString("logopath",null);

    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        myCartBeanArrayList.clear();
     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
     //   SQLiteDatabase db = db1.getWritableDatabase();
        Cursor c1 = sql_db.rawQuery("Select distinct MerchantId from "
                + databaseHelper.TABLE_CART_ITEM, null);

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                String id = c1.getString(c1.getColumnIndex("MerchantId"));
               /* String id1 = c1.getString(c1.getColumnIndex("Product_id"));
                String id2 = c1.getString(c1.getColumnIndex("Product_name"));
                String id3 = c1.getString(c1.getColumnIndex("price"));
                String id4 = c1.getString(c1.getColumnIndex("qnty"));*/

                float amt = 0;
                float finalminvalue = 0;
                Cursor cr = sql_db.rawQuery("Select Amount from "
                        + databaseHelper.TABLE_CART_ITEM +
                        "  where MerchantId ='" + id + "'", null);
                if (cr.getCount() > 0) {
                    cr.moveToFirst();
                    do {
                        //amt = amt + Float.parseFloat(cr.getString(cr.getColumnIndex("Amount")));
                    } while (cr.moveToNext());
                }

                Cursor cr1 = sql_db.rawQuery("Select minvalue from "
                        + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT +
                        "  where MerchantId ='" + id + "' order by minvalue", null);
                if (cr1.getCount() > 0) {
                    cr1.moveToFirst();
                    do {
                        float minvalue = cr1.getFloat(cr1.getColumnIndex("minvalue"));
                        if (minvalue <= amt) {
                            finalminvalue = minvalue;
                        }

                    } while (cr1.moveToNext());
                }

                Cursor c = sql_db.rawQuery("Select distinct tab1.MerchantId,tab1.MerchantName,tab1.qnty,tab1.minqnty," +
                        "tab1.offers,tab1.price,tab1.Product_name,tab1.Amount,tab1.Product_id," +
                        "tab1.Freeitemqty , tab1.Freeitemname,tab1.validfrom , tab1.validto,tab1.ItemImgPath," +
                        "tab2.minvalue,tab2.netrate,tab2.freeitemid,tab2.freeitem_name,tab2.freeitemqnty," +
                        "tab2.validfrom,tab2.validto,tab2.CouponId,tab2.discount from "
                        + databaseHelper.TABLE_CART_ITEM + " tab1 inner join  "
                        + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT +
                        " tab2  on tab1.Product_id = tab2.FKVendorProductmasterId where tab1.MerchantId ='" + id + "' " /*+
                       " and  tab2.minvalue=" + finalminvalue*/, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {

                        myCartBean = new MyCartBean();
                        myCartBean.setMerchantId(c.getString(c.getColumnIndex("MerchantId")));
                        myCartBean.setMerchantName(c.getString(c.getColumnIndex("MerchantName")));
                        myCartBean.setQnty(Float.valueOf(c.getString(c.getColumnIndex("qnty"))));
                        myCartBean.setMinqnty(c.getString(c.getColumnIndex("minqnty")));
                        myCartBean.setOffers(c.getString(c.getColumnIndex("offers")));
                        myCartBean.setPrice(Float.valueOf(c.getString(c.getColumnIndex("price"))));
                        myCartBean.setProduct_name(c.getString(c.getColumnIndex("Product_name")));
                        myCartBean.setAmount(Float.valueOf(c.getString(c.getColumnIndex("Amount"))));
                        myCartBean.setProduct_id(c.getString(c.getColumnIndex("Product_id")));
                        myCartBean.setFree_item_name_trade(c.getString(c.getColumnIndex("Freeitemname")));
                        myCartBean.setFree_item_qnty_trade(c.getString(c.getColumnIndex("Freeitemqty")));
                        myCartBean.setValidfrom_trade(c.getString(c.getColumnIndex("validfrom")));
                        myCartBean.setValidto_trade(c.getString(c.getColumnIndex("validto")));
                        myCartBean.setItemImgPath(c.getString(c.getColumnIndex("ItemImgPath")));
                        myCartBean.setMinvalue(c.getInt(c.getColumnIndex("minvalue")));
                        myCartBean.setNetrate(c.getString(c.getColumnIndex("netrate")));
                        myCartBean.setFreeitemid(c.getString(c.getColumnIndex("freeitemid")));
                        myCartBean.setFreeitemname(c.getString(c.getColumnIndex("freeitem_name")));
                        myCartBean.setFreeitemqnty(c.getInt(c.getColumnIndex("freeitemqnty")));
                        myCartBean.setValidfrom(c.getString(c.getColumnIndex("validfrom")));
                        myCartBean.setValidto(c.getString(c.getColumnIndex("validto")));
                        myCartBean.setCouponId(c.getString(c.getColumnIndex("CouponId")));
                        myCartBean.setDiscount(c.getString(c.getColumnIndex("discount")));

                        myCartBeanArrayList.add(myCartBean);

                    } while (c.moveToNext());
                } else {
                    Cursor c11 = sql_db.rawQuery("Select distinct MerchantId,MerchantName,qnty,minqnty,offers,price," +
                            "Product_name,Amount,Product_id , Freeitemqty , Freeitemname, validfrom,validto,ItemImgPath from "
                            + databaseHelper.TABLE_CART_ITEM +
                            "  where MerchantId ='" + id + "'", null);
                    if (c11.getCount() > 0) {
                        c11.moveToFirst();
                        do {
                            String MerchantId = c11.getString(c11.getColumnIndex("MerchantId"));
                            String MerchantName = c11.getString(c11.getColumnIndex("MerchantName"));
                            String qnty = c11.getString(c11.getColumnIndex("qnty"));
                            String offers = c11.getString(c11.getColumnIndex("offers"));
                            String price = c11.getString(c11.getColumnIndex("price"));
                            String Product_name = c11.getString(c11.getColumnIndex("Product_name"));
                            String Amount = c11.getString(c11.getColumnIndex("Amount"));
                            String Product_id = c11.getString(c11.getColumnIndex("Product_id"));

                            myCartBean = new MyCartBean();
                            myCartBean.setMerchantId(MerchantId);
                            myCartBean.setMerchantName(MerchantName);
                            myCartBean.setQnty(Float.valueOf(qnty));
                            myCartBean.setMinqnty(c11.getString(c11.getColumnIndex("minqnty")));
                            myCartBean.setOffers(offers);
                            myCartBean.setPrice(Float.valueOf(price));
                            myCartBean.setProduct_name(Product_name);
                            myCartBean.setAmount(Float.valueOf(Amount));
                            myCartBean.setProduct_id(Product_id);
                            myCartBean.setFree_item_name_trade(c11.getString(c11.getColumnIndex("Freeitemname")));
                            myCartBean.setFree_item_qnty_trade(c11.getString(c11.getColumnIndex("Freeitemqty")));
                            myCartBean.setValidfrom_trade(c11.getString(c11.getColumnIndex("validfrom")));
                            myCartBean.setValidto_trade(c11.getString(c11.getColumnIndex("validto")));
                            myCartBean.setItemImgPath(c11.getString(c11.getColumnIndex("ItemImgPath")));
                            myCartBean.setMinvalue(0);
                            myCartBean.setNetrate("0");
                            myCartBean.setFreeitemid("0");
                            myCartBean.setFreeitemname("0");
                            myCartBean.setFreeitemqnty(0);
                            myCartBean.setValidfrom("0");
                            myCartBean.setValidto("0");
                            myCartBean.setCouponId("0");
                            myCartBean.setDiscount("0 ₹");
                            myCartBeanArrayList.add(myCartBean);
                        } while (c11.moveToNext());

                    }
                }
                if (myCartBeanArrayList.size() == 0) {
                    Cursor c11 = sql_db.rawQuery("Select distinct MerchantId,MerchantName,qnty,minqnty,offers,price," +
                            "Product_name,Amount,Product_id , Freeitemqty , Freeitemname, validfrom,validto,ItemImgPath from "
                            + databaseHelper.TABLE_CART_ITEM +
                            "  where MerchantId ='" + id + "'", null);
                    if (c11.getCount() > 0) {
                        c11.moveToFirst();
                        do {
                            String MerchantId = c11.getString(c11.getColumnIndex("MerchantId"));
                            String MerchantName = c11.getString(c11.getColumnIndex("MerchantName"));
                            String qnty = c11.getString(c11.getColumnIndex("qnty"));
                            String offers = c11.getString(c11.getColumnIndex("offers"));
                            String price = c11.getString(c11.getColumnIndex("price"));
                            String Product_name = c11.getString(c11.getColumnIndex("Product_name"));
                            String Amount = c11.getString(c11.getColumnIndex("Amount"));
                            String Product_id = c11.getString(c11.getColumnIndex("Product_id"));


                            myCartBean = new MyCartBean();
                            myCartBean.setMerchantId(MerchantId);
                            myCartBean.setMerchantName(MerchantName);
                            myCartBean.setQnty(Float.valueOf(qnty));
                            myCartBean.setMinqnty(c11.getString(c11.getColumnIndex("minqnty")));
                            myCartBean.setOffers(offers);
                            myCartBean.setPrice(Float.valueOf(price));
                            myCartBean.setProduct_name(Product_name);
                            myCartBean.setAmount(Float.valueOf(Amount));
                            myCartBean.setProduct_id(Product_id);
                            myCartBean.setFree_item_name_trade(c11.getString(c11.getColumnIndex("Freeitemname")));
                            myCartBean.setFree_item_qnty_trade(c11.getString(c11.getColumnIndex("Freeitemqty")));
                            myCartBean.setValidfrom_trade(c11.getString(c11.getColumnIndex("validfrom")));
                            myCartBean.setValidto_trade(c11.getString(c11.getColumnIndex("validto")));
                            myCartBean.setItemImgPath(c11.getString(c11.getColumnIndex("ItemImgPath")));
                            myCartBean.setMinvalue(0);
                            myCartBean.setNetrate("0");
                            myCartBean.setFreeitemid("0");
                            myCartBean.setFreeitemname("0");
                            myCartBean.setFreeitemqnty(0);
                            myCartBean.setValidfrom("0");
                            myCartBean.setValidto("0");
                            myCartBean.setCouponId("0");
                            myCartBean.setDiscount("0 ₹");
                            myCartBeanArrayList.add(myCartBean);
                        } while (c11.moveToNext());
                    }
                }
            } while (c1.moveToNext());
        }
       // linearlayout_cart_container.removeAllViews();
       // showlist_new();
    }


    public String get_date(String d) {
        String toDate;
        try {

            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");//Feb 23 2016 12:16PM

            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            String date = d;
            try {
                date1 = toFormat.parse(date);
            } catch (Exception e) {
                Log.e("Date", "" + e);
            }
            toDate = toFormat.format(date1);
        } catch (Exception e) {
            toDate = "";
        }
        return toDate;

    }

    public static boolean compare_date(String fromdate, String todate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");

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

    private void getDataFromServer() {

        myCartBeanArrayList.clear();

        if (NetworkUtils.isNetworkAvailable(parent)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                String vid = "";
              //  DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
              //  SQLiteDatabase db = db1.getWritableDatabase();
                Cursor c1 = sql_db.rawQuery("Select distinct MerchantId from "
                        + databaseHelper.TABLE_CART_ITEM, null);

                if (c1.getCount() > 0) {
                    c1.moveToFirst();
                    do {
                        vid = c1.getString(c1.getColumnIndex("MerchantId"));//"3";//
                        //new GetvendorsOnVolumeDiscount().execute(vid);
                    } while (c1.moveToNext());
                } else {

                }


            } else {
                new StartSession_tbuds(parent, new CallbackInterface() {

                    @Override
                    public void callMethod() {

                        String vid = "";
                     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
                     //   SQLiteDatabase db = db1.getWritableDatabase();
                        Cursor c1 = sql_db.rawQuery("Select distinct MerchantId from "
                                + databaseHelper.TABLE_CART_ITEM, null);

                        if (c1.getCount() > 0) {
                            c1.moveToFirst();
                            do {
                                vid = c1.getString(c1.getColumnIndex("MerchantId"));//"3";//
                                isData = false;
                               // new GetvendorsOnVolumeDiscount().execute(vid, String.valueOf(c1.getCount()));

                            } while (c1.moveToNext());

                        } else {

                        }

                         /*else {

                            if (databaseHelper.getcartvolumediscountcount() > 0 || databaseHelper.getcartvolumediscountcount() == 0) {
                                DatabaseHelper db1 = new DatabaseHelper(parent);
                                SQLiteDatabase db = db1.getWritableDatabase();
                                db.execSQL("DROP TABLE IF EXISTS "
                                        + AnyMartDatabaseConstants.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                                db.execSQL(AnyMartDatabaseConstants.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
                                getDataFromDataBase();
                                linearlayout_cart_container.setVisibility(View.VISIBLE);
                            }

                        }*/
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
/*
            Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
*/
            Toast.makeText(parent, "Poor Internet connection. Please try again later.", Toast.LENGTH_LONG).show();

            myCartBeanArrayList.clear();
            linearlayout_cart_list.removeAllViews();
            linearlayout_cart_container.removeAllViews();
            if (tcf.getcartvolumediscountcount() > 0 || tcf.getcartvolumediscountcount() == 0) {
            //    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
            //    SQLiteDatabase db = db1.getWritableDatabase();
                sql_db.execSQL("DROP TABLE IF EXISTS " + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                sql_db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
                getDataFromDataBase();
                linearlayout_cart_container.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetvendorsOnVolumeDiscount extends AsyncTask<String, Void, String> {
        String responseString = null;
        String response_CartItems = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog.setMessage("Loading...");
            progressDialog.show();*/
            progress = ProgressHUD.show(parent,
                    "Loading Volume disc...", false, true, null);
        }

        @Override
        protected String doInBackground(String... params) {
            String url_cartActivity = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_VOLUME_DISCOUNT +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&VendorId="+ params[0];

            String cartActivity = url_cartActivity;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_cartActivity, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL url_ItemAddInCart = new URL(url_cartActivity);
                URL url = url_ItemAddInCart;
                urlConnection = url_ItemAddInCart.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_ItemsInCart = new StringBuffer();
                String line;
                StringBuilder str_build_getdetails = new StringBuilder();
                while((line = bufferedReader.readLine())!= null){
                    stringBuff_ItemsInCart = stringBuff_ItemsInCart.append(line);
                }

                responseString = stringBuff_ItemsInCart.toString().replaceAll("^\"|\"$", "");*/
                response_CartItems = responseString.replaceAll("\\\\","");

                System.out.println("resp ="+response_CartItems);

            } catch (Exception e) {
                response_CartItems = "error";
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (response_CartItems.equalsIgnoreCase("Session Expired")) {
                // progress.dismiss();
                getDataFromServer();
            } else if (response_CartItems.equalsIgnoreCase("error")) {
                //  progress.dismiss();
                /*Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();*/

                myCartBeanArrayList.clear();
                // linearlayout_cart_list.removeAllViews();
                linearlayout_cart_container.removeAllViews();
                if (tcf.getcartvolumediscountcount() > 0 || tcf.getcartvolumediscountcount() == 0) {
               //     DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
               //     SQLiteDatabase db = db1.getWritableDatabase();
                    sql_db.execSQL("DROP TABLE IF EXISTS "
                            + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                    sql_db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
                    getDataFromDataBase();
                    linearlayout_cart_container.setVisibility(View.VISIBLE);
                }


            } else if (response_CartItems.equalsIgnoreCase("No data found")) {
                //  progress.dismiss();

                json = response_CartItems;
                parseJson(json);
            } else {
                //  progress.dismiss();
                json = response_CartItems;
                parseJson(json);

            }
        }
    }

    protected void parseJson(String json) {

        try {
            if (json.equalsIgnoreCase("No data found")) {
                isData = true;
                getCount = getCount + 1;
                tcf.addCartItemsVolumeDiscount("0", "0", 0, "0", "0", "0", "0.00", "0", "0", "0", "0","0","0");
            } else {

                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {

                    String fromdate = get_date(jsonArray.getJSONObject(i).getString("ValidFrom"));
                    String todate = get_date(jsonArray.getJSONObject(i).getString("ValidTo"));
                    if (compare_date(fromdate, todate) == true &&
                            (!(String.valueOf(jsonArray.getJSONObject(i)
                                    .getString("Minvalue"))).equalsIgnoreCase(""))) {

                        String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                .getString("Freeitemid"));

                        String discount = "";

                        if (!(jsonArray.getJSONObject(i)
                                .getString("DiscrateMRP").equalsIgnoreCase(" "))
                                || (!(jsonArray.getJSONObject(i)
                                .getString("Discratepercent").equalsIgnoreCase(" ")))) {

                            if (!(jsonArray.getJSONObject(i)
                                    .getString("DiscrateMRP").equalsIgnoreCase(" "))) {

                                discount = jsonArray.getJSONObject(i)
                                        .getString("DiscrateMRP") + " ₹";

                            } else if (!(jsonArray.getJSONObject(i)
                                    .getString("Discratepercent").equalsIgnoreCase(" "))) {

                                discount = jsonArray.getJSONObject(i)
                                        .getString("Discratepercent") + " %";

                            }
                        } else {
                            discount = "0 ₹";
                        }
                        String freeqnty;
                        String fff = jsonArray.getJSONObject(i)
                                .getString("Freeitemqty");
                        if (!(jsonArray.getJSONObject(i)
                                .getString("Freeitemqty").equalsIgnoreCase(" ")) &&
                                !(jsonArray.getJSONObject(i)
                                        .getString("Freeitemqty").equalsIgnoreCase(""))) {

                            String a = jsonArray.getJSONObject(i)
                                    .getString("Freeitemqty");
                            freeqnty = (a);

                        } else {
                            freeqnty = "0.00";
                        }


                        tcf.addCartItemsVolumeDiscount(
                                jsonArray.getJSONObject(i).getString("FKVendorId"),
                                jsonArray.getJSONObject(i).getString("VendorItemname"),
                                jsonArray.getJSONObject(i).getInt("Minvalue"),
                                jsonArray.getJSONObject(i).getString("NetRate"),
                                jsonArray.getJSONObject(i).getString("Freeitemid"), freeitemname,freeqnty,
                                jsonArray.getJSONObject(i).getString("ValidFrom"),
                                jsonArray.getJSONObject(i).getString("ValidTo"),
                                jsonArray.getJSONObject(i).getString("CouponId"), discount,
                                jsonArray.getJSONObject(i).getString("FKVendorItemMasterId"),
                                jsonArray.getJSONObject(i).getString("FKVendorProductmasterId")
                        );

                    } else {
                    }
                }
                isData = true;
                getCount = getCount + 1;
            }

         //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
         //   SQLiteDatabase db = db1.getWritableDatabase();
            Cursor c1 = sql_db.rawQuery("Select distinct MerchantId from "
                    + databaseHelper.TABLE_CART_ITEM, null);
            int count = 0;
            if (c1.getCount() > 0) {

                count = c1.getCount();
            }

            if (isData == true && getCount == count) {
                if (tcf.getCartItems() > 0) {
                    linearlayout_cart_container.setVisibility(View.VISIBLE);
                    getDataFromDataBase();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showlist_new() {
        if (tcf.getMerchantCount() > 0) {
            //   for (int i = 0; i < databaseHelper.getMerchantCount(); i++) {//
            cntAmt = 0;
            int i = 0;
            String Murchant_name = "";
         //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
         //   SQLiteDatabase db = db1.getWritableDatabase();
            Cursor c1 = sql_db.rawQuery("Select distinct MerchantName from "
                    + databaseHelper.TABLE_CART_ITEM + " ", null);

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                do {
                    cntAmt = 0;
                    M_count = c1.getCount();
                    Murchant_name ="R.K.Foods";
                    //Murchant_name =c1.getString(c1.getColumnIndex("MerchantName"));
//myCartBeanArrayList  databaseHelper.getMerchantName(Murchant_name)
                    for (int k = 0; k < myCartBeanArrayList.size(); k++) {
                        addView_new(k, Murchant_name);
                    }
                } while (c1.moveToNext());
            } else {
            }
        }
    }

    private void addView_new(int i, String M_name_1) {

        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_cart_row,
                null);
        Merchant_name = "";
        TextView textview_merchant_name = (TextView) baseView
                .findViewById(R.id.textview_merchant_name);
        textview_discount = (TextView) baseView
                .findViewById(R.id.textview_discount);
        textview_total_amount = (TextView) baseView
                .findViewById(R.id.textview_total_amount);
        textview_delivery_charges_amt = (TextView) baseView
                .findViewById(R.id.textview_delivery_charges_amt);
        textview_volume_free_name = (TextView) baseView
                .findViewById(R.id.textview_volume_free_name);
        volumefreelayout = (LinearLayout) baseView
                .findViewById(R.id.volumefreelayout);
        textview_delivery_charges = (TextView) baseView
                .findViewById(R.id.textview_delivery_charges);

        /*ImageView imageview_cart_remove = (ImageView) baseView
                .findViewById(R.id.imageview_cart_remove);*/

        linearlayout_cart_list = (LinearLayout) baseView.findViewById(R.id.linearlayout_cart_list);

        // imageview_cart_remove.setTag(linearlayout_cart_container.getChildCount());

        textview_merchant_name.setText(myCartBeanArrayList.get(i).getMerchantName());

       // M_name = myCartBeanArrayList.get(i).getMerchantName();
        M_name = "R.K.Foods";

        float cntAmt1 = 0;

        if (M_name_1.equalsIgnoreCase(M_name)) {
            if (!(i == 0)) {
                if (M_name_1.equalsIgnoreCase(myCartBeanArrayList.get(i - 1).getMerchantName())) {
                    for (int k = 0; k < i; k++) {
                        if (M_name_1.equalsIgnoreCase(myCartBeanArrayList.get(i - 1).getMerchantName())) {
                            flag = 0;
                        }
                    }
                } else {

                    String discount = "0";

                    if (myCartBeanArrayList.size() > 0) {
                        discount = String.valueOf(myCartBeanArrayList.get(i).getMinvalue());
                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                            cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(j).getAmount()));
                        }
                    }

                    String splits[] = myCartBeanArrayList.get(i).getDiscount().split(" ");
                    String disamt = splits[0];//10
                    String distype = splits[1];

                    if (distype.equalsIgnoreCase("₹")) {

                        if (Integer.parseInt(discount) <= cntAmt1) {

                            textview_discount.setText(disamt + " ");
                            cntAmt1 = cntAmt1 - Integer.parseInt(disamt);

                            for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }

                        } else {
                            textview_discount.setText("No ");
                            for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }

                        }
                    } else if (distype.equalsIgnoreCase("%")) {
                        if (Integer.parseInt(discount) <= cntAmt1) {
                            float dis_amt = (Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount()))
                                    * Float.parseFloat(disamt)
                                    / 100);
                            textview_discount.setText(dis_amt + " ");
                            cntAmt1 = cntAmt1 - dis_amt;

                            for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }

                        } else {
                            textview_discount.setText("No ");
                            for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }
                        }
                    }

                    if (!(myCartBeanArrayList.get(i).getFreeitemqnty() == 0)) {
                        volumefreelayout.setVisibility(View.VISIBLE);
                        textview_volume_free_name.setText("Get " + myCartBeanArrayList.get(i).getFreeitemqnty() + " "
                                + myCartBeanArrayList.get(i).getFreeitemname()
                                + " free with above purchase");
                    } else {
                        volumefreelayout.setVisibility(View.GONE);
                    }

                    Merchant_name = M_name_1;
                    ShowTextValue(M_name_1);
                }
            } else {

                String discount = "0";

                if (myCartBeanArrayList.size() > 0) {
                    discount = String.valueOf(myCartBeanArrayList.get(i).getMinvalue());
                    for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                        cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(j).getAmount()));
                    }
                }

                String splits[] = myCartBeanArrayList.get(i).getDiscount().split(" ");
                String disamt = splits[0];//10
                String distype = splits[1];

                if (distype.equalsIgnoreCase("₹")) {

                    if (Integer.parseInt(discount) <= cntAmt1) {

                        textview_discount.setText(disamt + " ");
                        cntAmt1 = cntAmt1 - Float.parseFloat(disamt);

                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    } else {
                        textview_discount.setText("No ");
                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    }
                } else if (distype.equalsIgnoreCase("%")) {
                    if (Integer.parseInt(discount) <= cntAmt1) {
                        float dis_amt = (Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount())) * Float.parseFloat(disamt)
                                / 100);
                        textview_discount.setText(dis_amt + " ");
                        cntAmt1 = cntAmt1 - dis_amt;

                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    } else {
                        textview_discount.setText("No ");
                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    }
                }

                if (!(myCartBeanArrayList.get(i).getFreeitemqnty() == 0)) {
                    volumefreelayout.setVisibility(View.VISIBLE);
                    textview_volume_free_name.setText("Get " + myCartBeanArrayList.get(i).getFreeitemqnty() + " "
                            + myCartBeanArrayList.get(i).getFreeitemname()
                            + " free with above purchase");
                } else {
                    volumefreelayout.setVisibility(View.GONE);
                }

                Merchant_name = M_name_1;
                ShowTextValue(M_name_1);
            }

        } else {
            flag = 0;
        }

        if (flag > 5) {
            linearlayout_cart_container.addView(baseView);
        }
    }

    private void ShowTextValue(String M) {
        cntAmt = 0;
        paybaleAmt = 0;
        String discount = "0";
        flag = 0;

        String disamt = "";
        String distype = "";


        for (int k = 0; k < myCartBeanArrayList.size(); k++) {

            if (M.equalsIgnoreCase(myCartBeanArrayList.get(k).getMerchantName())) {
                String c1 = String.valueOf(myCartBeanArrayList.get(k).getAmount());
                cntAmt = cntAmt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(k).getAmount()));
                String splits[] = myCartBeanArrayList.get(k).getDiscount().split(" ");
                disamt = splits[0];//10
                distype = splits[1];
            }
        }

        float amt = 0;
        for (int k = 0; k < myCartBeanArrayList.size(); k++) {

            amt = amt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(k).getAmount()));
            discount = String.valueOf(myCartBeanArrayList.get(k).getMinvalue());

        }
        if (distype.equalsIgnoreCase("₹")) {
            if (Integer.parseInt(discount) <= cntAmt) {
                cntAmt = cntAmt - Float.parseFloat(disamt);
                textview_discount.setText(disamt + " " + distype);
            }
        } else if (distype.equalsIgnoreCase("%")) {
            if (Integer.parseInt(discount) <= cntAmt) {
                float dis_amt = ((cntAmt) * Float.parseFloat(disamt)
                        / 100);
                cntAmt = cntAmt - dis_amt;
                textview_discount.setText(disamt + " " + distype);
            }
        }
        if (cntAmt >= 200) {
            textview_delivery_charges_amt.setText("Free ");
            textview_delivery_charges.setText("Delivery");
        } else {
            textview_delivery_charges_amt.setText("0 ₹ ");
            textview_delivery_charges.setText("Delivery charges");
            cntAmt = cntAmt + 0;
        }

        flag = cntAmt;
        paybaleAmt = (paybaleAmt + amt) - Float.parseFloat(disamt);
        textview_total_amount.setText(" ");

        double amount = Double.parseDouble(String.valueOf(cntAmt));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount.setText("Total " + formatted + " ₹");

        double amount1 = Double.parseDouble(String.valueOf(paybaleAmt));
       // DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted1 = formatter.format(amount1);
        textview_cart_total_amount.setText(formatted1 + " ₹");
    }

    private void addView_list(int j, final String mname) {
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_cart_item_list,
                null);
        final int list_pos = j;
        TextView textview_cart_product_name = (TextView) baseView
                .findViewById(R.id.textview_cart_product_name);
        TextView textview_cart_offer_amount = (TextView) baseView
                .findViewById(R.id.textview_cart_offer_amount);
        final TextView textview_cart_product_amount = (TextView) baseView
                .findViewById(R.id.textview_cart_product_amount);
        final EditText edittext_cart_product_qty = (EditText) baseView
                .findViewById(R.id.edittext_cart_product_qty);
        ImageView img_delete_row = (ImageView) baseView
                .findViewById(R.id.img_delete_row);
        final TextView textview_cart_free_name = (TextView) baseView
                .findViewById(R.id.textview_cart_free_name);
        final LinearLayout itemfreelayout = (LinearLayout) baseView.findViewById(R.id.itemfreelayout);
        if (mname.equalsIgnoreCase(myCartBeanArrayList.get(j).getMerchantName())) {

            edittext_cart_product_qty.setText(String.valueOf(myCartBeanArrayList.get(j)
                    .getQnty()));
            edittext_cart_product_qty.setTag(linearlayout_cart_list.getChildCount());

            int textLength = edittext_cart_product_qty.getText().length();
            edittext_cart_product_qty.setSelection(textLength, textLength);

            int getfreeitem = 0;
            double offer = 0;
            String discount = myCartBeanArrayList.get(j).getOffers();//10%
            String splits[] = discount.split(" ");
            /*String disamt = splits[0];//10*/
            double disamt = Double.parseDouble(splits[0]);//10
            String distype = splits[1];
            String a = edittext_cart_product_qty.getText().toString().trim();
            String b = String.valueOf(myCartBeanArrayList.get(j).getQnty());
            float get_qnty;
            if (Integer.parseInt(myCartBeanArrayList.get(j).getMinqnty()) == 0) {
                get_qnty = 0;
            }else {
                get_qnty =
                        Integer.parseInt(edittext_cart_product_qty.getText().toString().trim()) /
                                Integer.parseInt(myCartBeanArrayList.get(j).getMinqnty());
            }

            if (!(Integer.parseInt(myCartBeanArrayList.get(j).getFree_item_qnty_trade()) == 0)) {
                itemfreelayout.setVisibility(View.VISIBLE);
                if (Integer.parseInt(myCartBeanArrayList.get(j).getMinqnty()) == 0) {
                    getfreeitem =0;
                }else {
                    getfreeitem = (Integer.parseInt(edittext_cart_product_qty.getText().toString().trim())
                            * Integer.parseInt(myCartBeanArrayList.get(j).getFree_item_qnty_trade())) /
                            Integer.parseInt(myCartBeanArrayList.get(j).getMinqnty());
                }
                textview_cart_free_name.setText("Get " + getfreeitem + " " +
                        myCartBeanArrayList.get(j).getFree_item_name_trade()
                        + " free with " + edittext_cart_product_qty.getText().toString().trim() +
                        " " + myCartBeanArrayList.get(j).getProduct_name());
            }

            if (distype.equalsIgnoreCase("₹")) {
                offer = (Double.parseDouble(String.valueOf(disamt))) * get_qnty;
            } else if (distype.equalsIgnoreCase("%")) {

                offer = (Float.parseFloat(myCartBeanArrayList.get(j).getPrice()) * Double.parseDouble(String.valueOf(disamt)) / 100)
                        * get_qnty; //0.3
            }
            float subtotal = Integer.parseInt(edittext_cart_product_qty.getText().toString().trim())
                    * Float.parseFloat(myCartBeanArrayList.get(j).getPrice());


            textview_cart_product_name.setText(myCartBeanArrayList.get(j).getProduct_name());
            textview_cart_offer_amount.setText(myCartBeanArrayList.get(j).getOffers());
            textview_cart_product_amount.setText((subtotal - offer) + " ");//₹
            edittext_cart_product_qty.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    int pos = 0;
                    float offer = 0;
                    cntAmt = 0;
                    int getfreeitem = 0;
                    if (M_count == 1) {
                        pos = Integer.parseInt(edittext_cart_product_qty.getTag().toString());
                    } else {
                        pos = Integer.parseInt(edittext_cart_product_qty.getTag().toString()) + list_pos;
                    }

                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0))) {

                        textview_cart_product_amount.setText("0 ₹");
                    } else {

                        String discount = myCartBeanArrayList.get(pos).getOffers();//10%
                        String splits[] = discount.split(" ");
                        String disamt = splits[0];//10
                        String distype = splits[1];

                        int get_qnty = Integer.parseInt(edittext_cart_product_qty.getText().toString().trim()) /
                                Integer.parseInt(myCartBeanArrayList.get(pos).getMinqnty());
                        int a = Integer.parseInt(myCartBeanArrayList.get(pos).getFree_item_qnty_trade());
                        int b = Integer.parseInt(myCartBeanArrayList.get(pos).getMinqnty());

                        if (!(Integer.parseInt(myCartBeanArrayList.get(pos).getFree_item_qnty_trade()) == 0)) {
                            itemfreelayout.setVisibility(View.VISIBLE);
                            getfreeitem = (Integer.parseInt(edittext_cart_product_qty.getText().toString().trim())
                                    * Integer.parseInt(myCartBeanArrayList.get(pos).getFree_item_qnty_trade())) /
                                    Integer.parseInt(myCartBeanArrayList.get(pos).getMinqnty());

                            textview_cart_free_name.setText("Get " + getfreeitem +
                                    " " + myCartBeanArrayList.get(pos).getFree_item_name_trade()
                                    + " free with " + edittext_cart_product_qty.getText().toString().trim() +
                                    " " + myCartBeanArrayList.get(pos).getProduct_name());
                        }


                        if (distype.equalsIgnoreCase("₹")) {
                            offer = (Integer.parseInt(disamt)) * get_qnty;
                        } else if (distype.equalsIgnoreCase("%")) {

                            offer = (Float.parseFloat(myCartBeanArrayList.get(pos).getPrice()) * Float.parseFloat(disamt) / 100)
                                    * get_qnty; //0.3
                        }
                        float subtotal = Integer.parseInt(edittext_cart_product_qty.getText().toString().trim())
                                * Float.parseFloat(myCartBeanArrayList.get(pos).getPrice());

                        myCartBeanArrayList.get(pos).setAmount(Float.valueOf("" + (subtotal - offer)));
                        float amt = 0, cntAmt_1 = 0;
                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                            if (mname.equalsIgnoreCase(myCartBeanArrayList.get(j).getMerchantName())) {
                                String c1 = String.valueOf(myCartBeanArrayList.get(j).getAmount());
                                cntAmt_1 = cntAmt_1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(j).getAmount()));
                            }
                        }
                      /*  getDataForVolume(pos, cntAmt_1, Integer.parseInt(myCartBeanArrayList.get(pos).getQnty()),
                                Integer.parseInt(s.toString().trim()), mname);*/
                        myCartBeanArrayList.get(pos).setQnty(Float.valueOf((s.toString())));

                   //     DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
                   //     SQLiteDatabase db = db1.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("qnty", "" + myCartBeanArrayList.get(pos).getQnty());
                        contentValues.put("Amount", "" + myCartBeanArrayList.get(pos).getAmount());
                        contentValues.put("minqnty", "" + myCartBeanArrayList.get(pos).getMinqnty());
                        contentValues.put("offers", "" + myCartBeanArrayList.get(pos).getOffers());
                        contentValues.put("price", "" + myCartBeanArrayList.get(pos).getPrice());
                        contentValues.put("Freeitemname", "" + myCartBeanArrayList.get(pos).getFree_item_name_trade());
                        contentValues.put("Freeitemqty", "" + myCartBeanArrayList.get(pos).getFree_item_qnty_trade());
                        contentValues.put("validfrom", "" + myCartBeanArrayList.get(pos).getValidfrom_trade());
                        contentValues.put("validto", "" + myCartBeanArrayList.get(pos).getValidto_trade());

                        long q = sql_db.update(databaseHelper.TABLE_CART_ITEM, contentValues,
                                "MerchantId=? and Product_id=?",
                                new String[]{myCartBeanArrayList.get(pos).getMerchantId(),
                                        myCartBeanArrayList.get(pos).getProduct_id()});
                        tcf.getCartcount();
                        textview_cart_product_amount.setText(myCartBeanArrayList.get(pos).getAmount() + " ");//₹

                        String discount_vol = String.valueOf(myCartBeanArrayList.get(pos).getMinvalue());

                        paybaleAmt = 0;

                        Float c = myCartBeanArrayList.get(pos).getAmount();

                        linearlayout_cart_container.removeAllViews();
                        getDataFromDataBase();
                    }
                }
            });

            img_delete_row.setTag(linearlayout_cart_list.getChildCount());
            img_delete_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(parent)
                            .setTitle(AnyMartStringConstants.APP_TITLE)
                            .setMessage(
                                    AnyMartStringConstants.MSG_MY_CART_REMOVE)
                          // .setIcon(R.drawable.cart)
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {

                                            int index = Integer.parseInt(v.getTag().toString().trim());

                                        //    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
                                         //   SQLiteDatabase db = db1.getWritableDatabase();
                                            //   Log.d("test", "id" + myCartBeanArrayList.get(index).getMerchantId());
                                            String a = myCartBeanArrayList.get(index).getMerchantId();
                                            sql_db.delete(databaseHelper.TABLE_CART_ITEM, "MerchantId='" +myCartBeanArrayList.get(index).getMerchantId()+"'" +
                                                            " and Product_name='"+myCartBeanArrayList.get(index).getProduct_name()+"'",null);

                                            ((LinearLayout) baseView.getParent())
                                                    .removeView(baseView);
                                            myCartBeanArrayList
                                                    .remove(index);

                                            if (myCartBeanArrayList
                                                    .size() == 0) {

                                                Toast.makeText(
                                                        parent,
                                                        AnyMartStringConstants.MSG_NO_ITEM_IN_CART,
                                                        Toast.LENGTH_LONG).show();
                                                onBackPressed();
                                            } else {

                                                myCartBeanArrayList.clear();
                                                linearlayout_cart_container.removeAllViews();
                                                linearlayout_cart_list.removeAllViews();
                                                getDataFromDataBase();
                                            }
                                        }
                                    }).setNegativeButton(android.R.string.no, null)
                            .show();
                }
            });
            String discount_vol = String.valueOf(myCartBeanArrayList.get(j).getMinvalue());
            myCartBeanArrayList.get(j).setQnty(Float.valueOf(edittext_cart_product_qty.getText().toString().trim()));
            myCartBeanArrayList.get(j).setAmount(Float.valueOf("" + (subtotal - offer)));

            Float c = myCartBeanArrayList.get(j).getAmount();

            linearlayout_cart_list.addView(baseView);
        } else {

        }
    }

    private void showTotal() {
        cntAmt = 0;
        paybaleAmt = 0;
        float offer = 0;
        int pos = 0;
        for (int index = 0; index < myCartBeanArrayList.size(); index++) {

            String discount = myCartBeanArrayList.get(index).getOffers();
            String splits[] = discount.split(" ");
            String disamt = splits[0];
            String distype = splits[1];

            String b = String.valueOf(myCartBeanArrayList.get(index).getQnty());
            int get_qnty = Integer.parseInt(String.valueOf(myCartBeanArrayList.get(index).getQnty())) /
                    Integer.parseInt(myCartBeanArrayList.get(index).getMinqnty());
            if (distype.equalsIgnoreCase("₹")) {
                offer = (Integer.parseInt(disamt)) * get_qnty;
            } else if (distype.equalsIgnoreCase("%")) {

                offer = (Float.parseFloat(myCartBeanArrayList.get(index).getPrice()) * Float.parseFloat(disamt) / 100)
                        * get_qnty;
            }
            float subtotal = get_qnty * Float.parseFloat(myCartBeanArrayList.get(index).getPrice());


            Float c = myCartBeanArrayList.get(index).getAmount();

            // cntAmt = cntAmt + Float.parseFloat(myCartBeanArrayList.get(index).getAmount());
            pos = index;
        }
        String discount_vol = String.valueOf(myCartBeanArrayList.get(pos).getMinvalue());

        for (int k = 0; k < myCartBeanArrayList.size(); k++) {

            if (M_name.equalsIgnoreCase(myCartBeanArrayList.get(k).getMerchantName())) {
                Float c1 = myCartBeanArrayList.get(k).getAmount();
                cntAmt = cntAmt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(k).getAmount()));
            }
        }

        float amt = 0;
        for (int k = 0; k < myCartBeanArrayList.size(); k++) {

            amt = amt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(k).getAmount()));

        }
        if (cntAmt >= 200) {
            textview_delivery_charges_amt.setText("Free ");
            textview_delivery_charges.setText("Delivery");
        } else {
            textview_delivery_charges_amt.setText("0 ₹ ");
            textview_delivery_charges.setText("Delivery charges");
            cntAmt = cntAmt + 0;
        }

        paybaleAmt = paybaleAmt + amt;
        double amount = Double.parseDouble(String.valueOf(cntAmt));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount.setText("Total " + formatted + " ₹");

        double amount1 = Double.parseDouble(String.valueOf(paybaleAmt));
        //DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted1 = formatter.format(amount1);
        textview_cart_total_amount.setText(formatted1 + " ₹");

    }

    private void getDataForVolume(int i, float amt, int oldqnty, int newqnty, String Mname) {
     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
      //  SQLiteDatabase db = db1.getWritableDatabase();
        Cursor c1 = sql_db.rawQuery("Select distinct MerchantId from "
                + databaseHelper.TABLE_CART_ITEM, null);

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                String id = c1.getString(c1.getColumnIndex("MerchantId"));

                Cursor c = sql_db.rawQuery("Select distinct " +
                        "minvalue,netrate,freeitemid,freeitem_name,freeitemqnty," +
                        "validfrom,validto,CouponId,discount from "
                        + databaseHelper.TABLE_CART_ITEM_VOLUME_DISCOUNT +
                        "  where MerchantId ='" + id + "' order by minvalue ", null);

               /* and minvalue <="
                        + myCartBeanArrayList.get(i).getMinvalue() + "*/
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {

                        int minvalue = c.getInt(c.getColumnIndex("minvalue"));
                        //  if (newqnty > oldqnty) {

                        if (amt >= Float.parseFloat(c.getString(c.getColumnIndex("minvalue"))))

                        {
                            myCartBeanArrayList.get(i).setMinvalue(minvalue);
                            myCartBeanArrayList.get(i).setNetrate(c.getString(c.getColumnIndex("netrate")));
                            myCartBeanArrayList.get(i).setFreeitemid(c.getString(c.getColumnIndex("freeitemid")));
                            myCartBeanArrayList.get(i).setFreeitemname(c.getString(c.getColumnIndex("freeitem_name")));
                            myCartBeanArrayList.get(i).setFreeitemqnty(c.getInt(c.getColumnIndex("freeitemqnty")));
                            myCartBeanArrayList.get(i).setValidfrom(c.getString(c.getColumnIndex("validfrom")));
                            myCartBeanArrayList.get(i).setValidto(c.getString(c.getColumnIndex("validto")));
                            myCartBeanArrayList.get(i).setCouponId(c.getString(c.getColumnIndex("CouponId")));
                            myCartBeanArrayList.get(i).setDiscount(c.getString(c.getColumnIndex("discount")));
                        }

                    } while (c.moveToNext());

                } else {

                }


            } while (c1.moveToNext());
        }

        String discount = "0";
        float cntAmt1 = 0;
        if (myCartBeanArrayList.size() > 0) {
            discount = String.valueOf(myCartBeanArrayList.get(i).getMinvalue());
            for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount()));
            }
        }

        String splits[] = myCartBeanArrayList.get(i).getDiscount().split(" ");
        String disamt = splits[0];
        String distype = splits[1];

        if (distype.equalsIgnoreCase("₹")) {

            if (Integer.parseInt(discount) <= cntAmt1) {

                textview_discount.setText(disamt + " ");
                cntAmt = cntAmt - Integer.parseInt(disamt);

                for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                    cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount()));
                }

            } else {
                textview_discount.setText("No ");
            }
        } else if (distype.equalsIgnoreCase("%")) {
            if (Integer.parseInt(discount) <= cntAmt1) {
                float dis_amt = (Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount())) * Float.parseFloat(disamt)
                        / 100);
                textview_discount.setText(dis_amt + " ");
                cntAmt = cntAmt - dis_amt;

                for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                    cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(i).getAmount()));

                }

            } else {
                textview_discount.setText("No ");
            }
        }
        if (!(myCartBeanArrayList.get(i).getFreeitemqnty() == 0)) {
            volumefreelayout.setVisibility(View.VISIBLE);
            textview_volume_free_name.setText("Get " + myCartBeanArrayList.get(i).getFreeitemqnty() + " "
                    + myCartBeanArrayList.get(i).getFreeitemname()
                    + " free with above purchace");
        } else {
            volumefreelayout.setVisibility(View.GONE);
        }
      /*  Merchant_name = Mname;
        ShowTextValue();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

           /* case R.id.refresh_cart:

                //after getting data from server remove comment
               *//* myCartBeanArrayList.clear();

                paybaleAmt = 0;
                cntAmt = 0;
                if (databaseHelper.getcartvolumediscountcount() > 0 || databaseHelper.getcartvolumediscountcount() == 0) {
                    DatabaseHelper db1 = new DatabaseHelper(parent);
                    SQLiteDatabase db = db1.getWritableDatabase();
                    db.execSQL("DROP TABLE IF EXISTS "
                            + AnyMartDatabaseConstants.TABLE_CART_ITEM_VOLUME_DISCOUNT);
                    db.execSQL(AnyMartDatabaseConstants.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
                    getDataFromServer();
                    linearlayout_cart_container.setVisibility(View.VISIBLE);
                }*//*

                return true;*/
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  linearlayout_cart_container.removeAllViews();
        /*Intent i = new Intent(parent, com.vritti.orderbilling.customer.ProductListActivity.class);

        startActivity(i);
        finish();*/

        int i =0;
        jsonArray1 = new JSONArray();
        if(myCartBeanArrayList.size()>0) {
            for (i = 0; i < myCartBeanArrayList.size(); i++) {

                String ItemName = myCartBeanArrayList.get(i).getProduct_name();
                String ItemId = myCartBeanArrayList.get(i).getProduct_id();
                float qty = myCartBeanArrayList.get(i).getQnty();
                float ToatlAmount = myCartBeanArrayList.get(i).getAmount();
                float Rate = Float.parseFloat(myCartBeanArrayList.get(i).getPrice());
                String Merchant_Name = myCartBeanArrayList.get(i).getMerchantName();
                String Merchant_id = myCartBeanArrayList.get(i).getMerchantId();
                String perDigit = myCartBeanArrayList.get(i).getPerdigit();

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("ItemName", ItemName);
                    jsonObject.put("ItemId", ItemId);
                    jsonObject.put("Qty", qty);
                    jsonObject.put("TotalAmount", ToatlAmount);
                    jsonObject.put("itemmrp", Rate);
                    jsonObject.put("custVendorname", Merchant_Name);
                    jsonObject.put("CustVendorMasterId", Merchant_id);
                    jsonObject.putOpt("PurDigit", PurDigit);
                    //price
                    jsonArray1.put(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

            //Intent intent1 = new Intent(CartActivity.this, ProductListActivity.class);
         Intent intent1 = new Intent(CartActivity.this, ProductList_TabActivity.class);
            Bundle b = new Bundle();
            //b.putStringArray("user", user);
            b.putString("user", jsonArray1.toString());
            b.putString("SubCategoryId", SubcatID);
            b.putString("PurDigit",PurDigit);
            //Toast.makeText(getApplicationContext(),ItemName+" added to Ordered list",Toast.LENGTH_SHORT).show();
            intent1.putExtras(b);

            startActivity(intent1);
          //  overridePendingTransition(R.anim.enter_slide_in_up, R.anim.enter_slide_out_up);
            finish();

    }

    private String getFreeItemname(String id) {
       // DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
       // SQLiteDatabase db = db1.getWritableDatabase();
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
}
