package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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

import com.vritti.sales.adapters.ItemListAdapter_customer;
import com.vritti.sales.beans.AddProductsToCart;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.OrderedItems_Fragment;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
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
import java.util.Date;

import static java.lang.String.valueOf;

/**
 * Created by sharvari on 3/4/2016.
 */
public class ProductListActivity extends AppCompatActivity {
    String product_name, product_id, product_img;
    TextView textview_product_name, textview_cart_count;
    private Context parent;
    String res = "";
    private ProgressDialog progressDialog;
    private ArrayList<Merchants_against_items> arrayList;
    private ArrayList<Merchants_against_items> arrayList_two;
    private  ArrayList<AllCatSubcatItems> arrayList_items;
    private ArrayList<Merchants_against_items> myJSONArray;
    private LinearLayout containerLayout_one, containerLayout_two, linearlayout_cart_bottom_layout;
    private LinearLayout layoutOne, layoutTwo;
    ImageView imageview_product_logo_details;
    public Merchants_against_items bean;
    public Merchants_against_items bean_addnew;
    public AllCatSubcatItems bean_item;
    private String json;
    ScrollView scrollview_cart_list1, scrollview_cart_list;
    Button button_my_cart_proceed;
    AddProductsToCart addProductsToCart;
    public static ArrayList<AddProductsToCart> addProductsToCartArrayList;
    public static String today;
    ProgressHUD progress;
    String edit_qty;

    String[] myItemId;
    ListView ordered_list, non_ordered_list;
    OrderedItems_Fragment.ProductListAdapter padapter;
    ItemListAdapter_customer iAdapter;
    Button btn_confirm, btn_cancel, btn_addtoOrderedList;
    String SubcatID,PurDigit;
    private String[] newuser;
    JSONArray jsonArray = null;
    ImageView img_delete_row;
    String ItemId,ItemName;
    float ToatlAmount;
    int qty ;
    int index =0;
    TextView total_toPay;
    float amt = 0;
    private String[] user;

    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    SearchView searchView;
    EditText edit_List;
    TextView non_orderedList;
    LinearLayout ordl1, ordl2;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activit_murchant_items);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

       /* final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setTitle(" "+"Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);*/

        parent = ProductListActivity.this;

        /*toolbar = (Toolbar)findViewById(R.id.toolbar);

        actionBarImage = (ImageView)findViewById(R.id.actionBarImage);
        toolbar.setTitle("Product Details");
        // toolbar.setTitleTextColor(0xffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);*/

       /* databaseHelper = new DatabaseHelper(com.vritti.orderbilling.customer.ProductListActivity.this,
                AnyMartDatabaseConstants.DATABASE__NAME_URL);*/

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =(SearchView) findViewById(R.id.searchview);
        searchView.setVisibility(View.VISIBLE);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        initialize();

        // String user = null;
        Bundle b = getIntent().getExtras();
        String jsonData = b.getString("user");
        SubcatID = b.getString("SubCategoryId");
        PurDigit = b.getString("PurDigit");

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES,Context.MODE_PRIVATE);

        image_URL = sharedpreferences.getString("logopath",null);


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
                     //   ab.setIcon(d);
                     //   ab.setDisplayShowHomeEnabled(true);
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


        //jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         myJSONArray = new ArrayList<Merchants_against_items>();

        //Retrieve from ItemListActivity

        //ordered items list
        GetOrderedItemsInList();

        //listview.setAdapter(new ItemListAdapter_customer(parent, arrayList));

       //Intent intent = getIntent();
       // product_id = intent.getStringExtra("ItemMasterId");
        //product_name = intent.getStringExtra("itemname");
      //  product_img = intent.getStringExtra("ItemImgPath");



       // textview_product_name.setText(product_name);
        // imageview_product_logo_details
        /*Picasso.with(parent)
                .load(product_img)
                .placeholder(R.drawable.default_img).error(R.drawable.error)
                .resize(60, 60)
                .into(imageview_product_logo_details);*/


        if (tcf.getMerchantsAgainst_Items(product_name) > 0) {
           // layoutOne.setVisibility(View.VISIBLE);
          //  containerLayout_one.setVisibility(View.VISIBLE);
          // scrollview_cart_list.setVisibility(View.VISIBLE);
           // getDataFromDataBase();
        } else {
            //getDataFromServer();
           // layoutOne.setVisibility(View.GONE);
           // containerLayout_one.setVisibility(View.GONE);
            //scrollview_cart_list.setVisibility(View.GONE);
        }

        setListeners();

        //click on confirm button

        //on long click of listitem remove product from list
/*
        ordered_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub
                //showNewPrompt(index);
                return true;
            }
        });
*/


        if (tcf.getCartItems() > 0) {
           // textview_cart_count.setVisibility(View.VISIBLE);
            //textview_cart_count.setText(databaseHelper.getCartItems() + "");
        } else {
           // textview_cart_count.setVisibility(View.INVISIBLE);
        }
    }

    private void initialize() {
        progressDialog = new ProgressDialog(parent);
       // textview_product_name = (TextView) findViewById(R.id.textview_product_name_details);
        arrayList = new ArrayList<Merchants_against_items>();
        arrayList_two = new ArrayList<Merchants_against_items>();
        arrayList_items = new ArrayList<AllCatSubcatItems>();
       // final ArrayList<Merchants_against_items> myJSONArray= new ArrayList<Merchants_against_items>();

       // containerLayout_one = (LinearLayout) findViewById(R.id.containerLayout_one);
        //containerLayout_two = (LinearLayout) findViewById(R.id.containerLayout_two);
       // layoutOne = (LinearLayout) findViewById(R.id.layoutOne);
       // layoutTwo = (LinearLayout) findViewById(R.id.layoutTwo);
        //textview_cart_count = (TextView) findViewById(R.id.textview_cart_count);
        addProductsToCartArrayList = new ArrayList<AddProductsToCart>();
        //button_my_cart_proceed = (Button) findViewById(R.id.button_my_cart_proceed);
        //button_my_cart_proceed.setText("SHOP MORE");
        //scrollview_cart_list1 = (ScrollView) findViewById(R.id.scrollview_cart_list1);
       // scrollview_cart_list = (ScrollView) findViewById(R.id.scrollview_cart_list);
      // imageview_product_logo_details = (ImageView) findViewById(R.id.imageview_product_logo_details);
        linearlayout_cart_bottom_layout = (LinearLayout) findViewById(R.id.linearlayout_cart_bottom_layout);
        /*ImageView img_delete_row = (ImageView) baseView
                .findViewById(R.id.img_delete_row);*/
        img_delete_row = (ImageView)findViewById(R.id.img_delete_row_1);
        ordered_list = (ListView)findViewById(R.id.listView_ordered);
        non_ordered_list = (ListView)findViewById(R.id.listView_nonordered) ;
        non_ordered_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        non_ordered_list.setStackFromBottom(true);

        total_toPay = (TextView)findViewById(R.id.total_topay);
        btn_confirm = (Button)findViewById(R.id.button_product_add_to_cart);
        btn_cancel = (Button)findViewById(R.id.btncancel);
        btn_addtoOrderedList = (Button)findViewById(R.id.addtocart);

        non_orderedList = (TextView)findViewById(R.id.non_orderedList);
        ordl1 = (LinearLayout)findViewById(R.id.ordl1);
        ordl1.setVisibility(View.VISIBLE);
        ordl2 = (LinearLayout)findViewById(R.id.ordl2);
        ordl2.setVisibility(View.VISIBLE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ProductListActivity.this);
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

    private  void setListeners(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                non_orderedList.setVisibility(View.GONE);
                ordl1.setVisibility(View.VISIBLE);
                // filter recycler view when query submitted
                // adt.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                non_orderedList.setVisibility(View.GONE);
                ordl1.setVisibility(View.VISIBLE);
                // filter recycler view when text is changed
                //adt.getFilter().filter(query);
                iAdapter.filter(query);
                return true;
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                int i=0;

                //add items in cart
             //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
             //   SQLiteDatabase db = db1.getWritableDatabase();
                sql_db.execSQL("DROP TABLE IF EXISTS "
                        + databaseHelper.TABLE_CART_ITEM);
                sql_db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM);

                myJSONArray = ((OrderedItems_Fragment.ProductListAdapter) ordered_list.getAdapter()).getMerchantAgainstItemsList();


                for(index =0; index < myJSONArray.size(); index++) {

                    tcf.addCartItems(myJSONArray.get(index).getMerchant_id(),
                            myJSONArray.get(index).getMerchant_name(),
                            valueOf(myJSONArray.get(index).getQnty()),
                            valueOf(myJSONArray.get(index).getMinqnty()),
                            myJSONArray.get(index).getOffers(),
                            valueOf(myJSONArray.get(index).getPrice()),
                            myJSONArray.get(index).getProduct_name(),
                            valueOf(myJSONArray.get(index).getAmount()),
                            myJSONArray.get(index).getProductid(),
                            myJSONArray.get(index).getFreeitemid(),
                            valueOf(myJSONArray.get(index).getFreeitemqty()),
                            myJSONArray.get(index).getFreeitemname(),
                            myJSONArray.get(index).getValidfrom(),
                            myJSONArray.get(index).getValidto(),
                            "" );
                }

                Cursor cursor1 = sql_db.rawQuery("Select * from "
                        + databaseHelper.TABLE_CART_ITEM , null);
                Log.d("test", "" + cursor1.getCount());

                if (cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                }


                JSONArray jsonArray1 = new JSONArray();

                if (tcf.getCartItems() > 0) {

                    for ( i = 0; i < myJSONArray.size(); i++) {

                        String  ItemName = myJSONArray.get(i).getProduct_name();
                        String ItemId = myJSONArray.get(i).getProductid();
                        float qty = myJSONArray.get(i).getQnty();
                        float ToatlAmount = myJSONArray.get(i).getAmount();
                        float Rate = myJSONArray.get(i).getPrice();
                        String Merchant_Name = myJSONArray.get(i).getMerchant_name();
                        String Merchant_id = myJSONArray.get(i).getMerchant_id();
                        String perDigit = myJSONArray.get(i).getPerDigit();
                        perDigit = PurDigit;

                        JSONObject jsonObject = new JSONObject();

                        try {

                            jsonObject.put("ItemName", ItemName);
                            jsonObject.put("ItemId", ItemId);
                            jsonObject.put("Qty", qty);
                            jsonObject.put("TotalAmount", ToatlAmount);
                            jsonObject.put("itemmrp", Rate);
                            jsonObject.put("custVendorname", Merchant_Name);
                            jsonObject.put("CustVendorMasterId", Merchant_id);
                            jsonObject.putOpt("PurDigit", perDigit);
                            //price
                            jsonArray1.put(jsonObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(ProductListActivity.this, CartActivity.class);
                    Bundle b = new Bundle();
                    b.putString("OrderedItems", jsonArray1.toString());
                    b.putString("SubCategoryId",SubcatID);
                    b.putString("PayableAmount", String.valueOf(amt));
                    b.putString("PurDigit",PurDigit);
                    intent1.putExtras(b);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent1 = new Intent(ProductListActivity.this, MainActivity.class);
            //    startActivity(intent1);
                finish();
            }
        });

        //onClick of AddToOrderList add item in Ordered items list
        btn_addtoOrderedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                non_orderedList.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                ordl1.setVisibility(View.VISIBLE);

                String ItemId = null;
                String ItemName = null;
                String Merchant_id = null;
                String Merchant_Name = null;
                float Rate;
                float ToatlAmount,qty;
                int i=0;

                arrayList_items = ((ItemListAdapter_customer) non_ordered_list.getAdapter()).getAllCatSubcatItemsList();
                if (arrayList_items.size() > 0) {

                    JSONArray jsonArray1_new = new JSONArray();
                    AllCatSubcatItems items = arrayList_items.get(i);

                    if (arrayList_items.size() > 0) {

                        newuser = new String[arrayList_items.size()];

                        for (i = 0; i < arrayList_items.size(); i++) {

                            ItemName = arrayList_items.get(i).getItemName();
                            ItemId = arrayList_items.get(i).getItemMasterId();
                            qty = arrayList_items.get(i).getEdtQty();
                            ToatlAmount = arrayList_items.get(i).getTotalAmount();
                            Rate = arrayList_items.get(i).getPrice();
                            Merchant_Name = arrayList_items.get(i).getMerchant_name();
                            Merchant_id = arrayList_items.get(i).getMerchant_id();

                            JSONObject jsonObject = new JSONObject();

                            try {

                                jsonObject.put("ItemName", ItemName);
                                jsonObject.put("ItemId", ItemId);
                                jsonObject.put("Qty", qty);
                                jsonObject.put("TotalAmount", ToatlAmount);
                                jsonObject.put("itemmrp",Rate);
                                jsonObject.put("custVendorname",Merchant_Name);
                                jsonObject.put("CustVendorMasterId",Merchant_id);

                                //price
                                jsonArray.put(jsonObject);

                                newuser[i] = jsonObject.toString();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ////////////Add to ordered items list
                        System.out.println(myJSONArray);
                        GetOrderedItemsInList();
                    }
                }
            }
        });


        //Click on cart
        /*linearlayout_cart_bottom_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (databaseHelper.getCartItems() > 0) {
                    Intent intent1 = new Intent(ProductListActivity.this, CartActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        //shop more
     /*   button_my_cart_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (databaseHelper.getCartItems() > 0) {
                    Intent intent1 = new Intent(ProductListActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart", Toast.LENGTH_LONG).show();
                }
            }
        });*/

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //calculate total of all products
    public void calculate_total(){
        amt =0;
        for (int k = 0; k < myJSONArray.size(); k++) {

            amt = amt + myJSONArray.get(k).getAmount();
        }
        float payableAmount = amt;
        total_toPay.setText(String.valueOf(amt)+" ₹");
    }

//Dialogue Box
    public void showNewPrompt(final int ind) {
        // TODO Auto-generated method stub
        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_message);
        myDialog.setCancelable(true);
        // myDialog.getWindow().setGravity(Gravity.BOTTOM);
      //  myDialog.setTitle("Complete Activity");

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        quest.setText(Html.fromHtml("Do you really want to remove <b><i> "+myJSONArray.get(ind).getProduct_name()+"</i></b> from Ordered list?"));

        Button btnyes = (Button) myDialog
                .findViewById(R.id.btn_yes);
        btnyes.setText("YES");
        btnyes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            public void onClick(View v) {
                // TODO Auto-generated method stub
                jsonArray.remove(ind);
                System.out.println(valueOf(jsonArray));
                Toast.makeText(parent,myJSONArray.get(ind).getProduct_name()+" removed from Ordered List",Toast.LENGTH_SHORT).show();
                GetOrderedItemsInList();
                myDialog.dismiss();
                // finish();
            }
        });

        Button btnno = (Button) myDialog
                .findViewById(R.id.btn_no);
        btnno.setText("NO");
        btnno.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                myDialog.dismiss();
                // finish();
            }
        });

        myDialog.show();

    }

//ordered items list
    public void GetOrderedItemsInList(){
        // final ArrayList<Merchants_against_items> myJSONArray = new ArrayList<Merchants_against_items>();
        String usernameToFind;

        myItemId = null;
        myItemId = new String[jsonArray.length()];
        myJSONArray.clear();
        for (int i = 0; i< jsonArray.length(); i++) {

            // if(arrayList_items.size() == 0){
                JSONObject jOBJ = new JSONObject();
                bean = new Merchants_against_items();

                try {
                        myItemId[i] = this.jsonArray.getJSONObject(i).getString("ItemId");
                        bean.setProduct_name(this.jsonArray.getJSONObject(i)
                                .getString("ItemName"));
                        bean.setProductid(this.jsonArray.getJSONObject(i).getString("ItemId"));

                        bean.setQnty(Float.parseFloat(this.jsonArray.getJSONObject(i).getString("Qty")));
                        bean.setAmount(Float.parseFloat(this.jsonArray.getJSONObject(i).getString("TotalAmount")));

                    bean.setPrice(Float.parseFloat(this.jsonArray.getJSONObject(i).getString("itemmrp")));
                    bean.setMerchant_name(this.jsonArray.getJSONObject(i).getString("custVendorname"));
                    bean.setMerchant_id(this.jsonArray.getJSONObject(i).getString("CustVendorMasterId"));
                    bean.setPerDigit(PurDigit);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                myJSONArray.add(bean);
        }

        calculate_total();  //calculate total amount of all products

        ordered_list.setAdapter(new OrderedItems_Fragment.ProductListAdapter(parent,myJSONArray));
       // Toast.makeText(parent,"Ordered List should not be empty",Toast.LENGTH_SHORT).show();

        //non ordered items list
        getDataFromDataBase_itemList();
    }

//non ordered items list
    private void getDataFromDataBase_itemList() {
        // TODO Auto-generated method stub
        arrayList_items.clear();
        String pId = "(";
        for(int j=0; j<myItemId.length;j++){
            if(j!=0){
                pId = pId+",";
            }
            pId = pId + "'" + myItemId[j] +"'";
        }
        pId = pId + ")";

     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
     //   SQLiteDatabase db = db1.getWritableDatabase();

        int subcatcount = 0;
        int itemcount = 0;

        /*Cursor cursor1 = db.rawQuery("Select distinct CategoryId, CategoryName,SubCategoryName,SubCategoryId," +
                "itemmasterid,ItemName, ItemImgPath,itemMRP, CustVendorMasterId,custVendorname  from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + SubcatID + "' and" +
                " itemmasterid NOT IN "+pId , null);*/

        Cursor cursor1 = sql_db.rawQuery("Select * from "
                + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + SubcatID + "' and" +
                " itemmasterid NOT IN "+pId + " ORDER BY ItemName ASC ", null);
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

                    //add price and all details too

                    bean_item = new AllCatSubcatItems();
                    bean_item.setCategoryId(cat_id);
                    bean_item.setCategoryName(cat_name);
                    bean_item.setSubCategoryName(subcat_name);
                    bean_item.setSubCategoryId(subcat_id);
                    bean_item.setItemMasterId(cursor1.getString(cursor1
                            .getColumnIndex("itemmasterid")));
                    bean_item.setPrice(Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("itemMRP"))));
                    bean_item.setItemName(cursor1.getString(cursor1
                            .getColumnIndex("ItemName")));
                    bean_item.setSubcatcount(subcatcount);
                    bean_item.setItemcount(itemcount);
                    bean_item.setItemImgPath(cursor1.getString(cursor1
                            .getColumnIndex("ItemImgPath")));
                    bean_item.setMerchant_id(cursor1.getString(cursor1
                            .getColumnIndex("CustVendorMasterId")));
                    bean_item.setMerchant_name(cursor1.getString(cursor1
                            .getColumnIndex("custVendorname")));
                    bean_item.setFreeitemid(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemid")));
                   /* bean.setFreeitemname(cursor1.getString(cursor1
                            .getColumnIndex("freeitemname")));*/
                    bean_item.setFreeitemqty(Float.parseFloat(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemqty"))));
                    bean_item.setValidfrom(cursor1.getString(cursor1
                            .getColumnIndex("validfrom")));
                    bean_item.setValidto(cursor1.getString(cursor1
                            .getColumnIndex("validto")));
                    bean_item.setPerDigit(cursor1.getString(cursor1
                            .getColumnIndex("PurDigit")));

                    arrayList_items.add(bean_item);

                } while (cursor1.moveToNext());
            } finally {
                cursor1.close();
            }
        }

       // listview_itemList.setAdapter(new ItemListAdapter_customer(parent, arrayList));
        iAdapter = new ItemListAdapter_customer(parent,arrayList_items);
        //non_ordered_list.setAdapter(new ItemListAdapter_customer(parent,arrayList_items));
        non_ordered_list.setAdapter(iAdapter);

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

    //    DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
    //    SQLiteDatabase db = db1.getWritableDatabase();

        Cursor c = sql_db.rawQuery("Select * from "
                + databaseHelper.TABLE_MERCHANT_AGAINST_ITEM +
                " where Product_name='" + product_name + "'" + "ORDER BY Product_name ASC", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String fromdate = get_date(c.getString(c.getColumnIndex("validfrom")));
                String todate = get_date(c.getString(c.getColumnIndex("validto")));

                if (compare_date(fromdate, todate) == true) {

                    bean = new Merchants_against_items();
                    bean.setMerchant_name(c.getString(c.getColumnIndex("MerchantName")));
                    bean.setQnty(c.getInt(c.getColumnIndex("qnty")));
                    bean.setMinqnty(c.getInt(c.getColumnIndex("minqnty")));
                    bean.setOffers(c.getString(c.getColumnIndex("offers")));
                    bean.setPrice(c.getFloat(c.getColumnIndex("price")));
                    bean.setProduct_name(c.getString(c.getColumnIndex("Product_name")));
                    bean.setMerchant_id(c.getString(c.getColumnIndex("MerchantId")));
                    bean.setFreeitemqty(c.getInt(c.getColumnIndex("Freeitemqty")));
                    bean.setFreeitemname(c.getString(c.getColumnIndex("Freeitemname")));
                    bean.setValidfrom(fromdate);
                    bean.setValidto(todate);
                    arrayList.add(bean);
                }
            } while (c.moveToNext());
            if (arrayList.size() > 0) {
               // textview_cart_count.setVisibility(View.VISIBLE);
               // containerLayout_one.setVisibility(View.VISIBLE);
               // scrollview_cart_list.setVisibility(View.VISIBLE);
                //edit_qty = "fromDB";
                showlist();
            }
        } else {
            //layoutOne.setVisibility(View.GONE);
           // containerLayout_one.setVisibility(View.GONE);
            //scrollview_cart_list.setVisibility(View.GONE);
        }
        getDataFromServer();
    }

    private void showlist() {
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
            //    addView(i);
            }
        } else {

        }
    }

   /* private void addView(int i) {
        final int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_murchant_details,
                null);
        *//*TextView textview_product_sellers = (TextView) baseView
                .findViewById(R.id.textview_product_sellers);*//*

        *//*final TextView textview_freeitem = (TextView) baseView
                .findViewById(R.id.textview_freeitem);

        TextView textview_product_offers = (TextView) baseView
                .findViewById(R.id.textview_product_offers);*//*
        final TextView textview_product_rate = (TextView) baseView
                .findViewById(R.id.textview_product_rate);
        final TextView textview_product_subtotal = (TextView) baseView
                .findViewById(R.id.textview_product_subtotal);


        final EditText edit_product_qty = (EditText) baseView
                .findViewById(R.id.edit_product_qty);

            edit_product_qty.setFocusable(true);

        Button imageviewAddProduct = (Button) baseView.findViewById(R.id.button_product_add_to_cart);
        edit_product_qty.setTag(containerLayout_one.getChildCount());
        edit_product_qty.setText(Float.toString(arrayList.get(i).getQnty()));
        int textLength = edit_product_qty.getText().length();
        edit_product_qty.setSelection(textLength, textLength);
        edit_product_qty.addTextChangedListener(new TextWatcher() {

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

                float offer = 0;
                int getfreeitem = 0;
                int pos = Integer
                        .parseInt(edit_product_qty.getTag().toString());
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {

                    textview_product_rate.setText("0 ₹");
                    textview_product_subtotal.setText("0 ₹");
                } else {

                    String discount = arrayList.get(pos).getOffers();//10%
                    String splits[] = discount.split(" ");
                    String disamt = splits[0];//10
                    String distype = splits[1];
           *//*3  *//*
                int d = arrayList.get(pos).getMinqnty();
               int get_qnty = Integer.parseInt(edit_product_qty.getText().toString().trim()) / arrayList.get(pos).getMinqnty();

               if (arrayList.get(pos).getFreeitemqty() > 0) {
                   getfreeitem = (Integer.parseInt(edit_product_qty.getText().toString().trim()) *
                           arrayList.get(pos).getFreeitemqty()) / arrayList.get(pos).getMinqnty();
                   *//*textview_freeitem.setText("Get " + getfreeitem + " " + arrayList.get(pos).getFreeitemname()
                           + " free");*//*
               }
               if (distype.equalsIgnoreCase("₹")) {
                   offer = (Float.parseFloat(disamt)) * get_qnty;
               } else if (distype.equalsIgnoreCase("%")) {

                   offer = ((arrayList.get(pos).getPrice()) * Float.parseFloat(disamt) / 100) * get_qnty; //0.3
               }


               Log.d("test", "amt" + arrayList.get(pos).getAmount());
               Log.d("test", "amtttt" + Float.parseFloat(s.toString())
                       * (arrayList.get(pos)
                       .getPrice()));
               textview_product_rate.setText(
                       (arrayList.get(pos).getPrice()) + " ₹");
               float subtotal = Float.parseFloat(edit_product_qty.getText().toString().trim())
                       * (arrayList.get(pos).getPrice());
               textview_product_subtotal.setText((subtotal - offer) + " ");//₹


               arrayList.get(pos).setQnty(Integer.parseInt(s.toString()));
               //  arrayList.get(pos).setFreeitemqty(getfreeitem);
               arrayList.get(pos).setAmount((subtotal - offer));

                }
            }
        });
        float offer = 0;
        float subtotal = 0;
        int getfreeitem = 0;
        float get_qnty = 0 ;
        String discount = arrayList.get(i).getOffers();//10%
        String splits[] = discount.split(" ");
        String disamt = splits[0];//10
        String distype = splits[1];
           *//*3  *//*
        if(arrayList.get(i).getMinqnty()==0){
            get_qnty = 0;
        }else {
            get_qnty = arrayList.get(i).getQnty() / arrayList.get(i).getMinqnty();
        }

        if ((arrayList.get(i).getFreeitemqty() > 0) && (arrayList.get(pos).getMinqnty()!=0) ) {
            *//*getfreeitem = (Integer.parseInt(edit_product_qty.getText().toString().trim()) *
                    arrayList.get(pos).getFreeitemqty()) / arrayList.get(pos).getMinqnty();
            textview_freeitem.setText("Get " + getfreeitem + " " + arrayList.get(i).getFreeitemname() + " free");*//*
        }

        *//*if (distype.equalsIgnoreCase("₹")) {
            offer = (Integer.parseInt(disamt)) * get_qnty;
        }*//*
        else if (distype.equalsIgnoreCase("%")) {

            offer = ((arrayList.get(i).getPrice()) * Float.parseFloat(disamt) / 100) * get_qnty; //0.3
        }
        *//*textview_product_sellers.setText(arrayList.get(i).getMerchant_name());
        textview_product_offers.setText(arrayList.get(i).getOffers());
        textview_product_rate.setText(
                (arrayList.get(i).getPrice()) + " ₹");*//*

        subtotal = Float.parseFloat(edit_product_qty.getText().toString().trim())
                * (arrayList.get(i).getPrice());
        textview_product_subtotal.setText((subtotal - offer) + " ");//₹


        String qty = edit_product_qty.getText().toString().trim();
        arrayList.get(i).setQnty(Integer.parseInt(qty));
        arrayList.get(i).setAmount((subtotal - offer));
        //    arrayList.get(i).setFreeitemqty(getfreeitem);
        imageviewAddProduct.setTag(containerLayout_two.getChildCount());

        imageviewAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                int index = Integer.parseInt(v.getTag()
                        .toString().trim());

                if (databaseHelper.getCartItems_chkrecords(arrayList.get(index).getMerchant_id(),
                        product_name, product_id) > 0) {

                    SQLiteDatabase db = databaseHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();

                    cv.put("qnty", arrayList.get(index).getQnty());
                    cv.put("minqnty", arrayList.get(index).getMinqnty());
                    cv.put("offers", arrayList.get(index).getOffers());
                    cv.put("price", arrayList.get(index).getPrice());
                    cv.put("Amount", valueOf(arrayList.get(index).getAmount()));
                    cv.put("Product_id", product_id);
                    cv.put("Freeitemid", arrayList.get(index).getFreeitemid());
                    cv.put("Freeitemqty", valueOf(arrayList.get(index).getFreeitemqty()));
                    cv.put("Freeitemname", arrayList.get(index).getFreeitemname());
                    cv.put("validfrom", arrayList.get(index).getValidfrom());
                    cv.put("validto", arrayList.get(index).getValidto());

                    db.update(AnyMartDatabaseConstants.TABLE_CART_ITEM, cv,
                            "MerchantId=? and Product_name=?",
                            new String[]{arrayList.get(index).getMerchant_id(),
                                    product_name});
                    Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();
                    addProductsToCartArrayList.add(new AddProductsToCart(product_id, product_name,
                            arrayList.get(index).getMerchant_name(), arrayList.get(index).getOffers(),
                            arrayList.get(index).getPrice(), arrayList.get(index).getQnty(),
                            arrayList.get(index).getMinqnty(),
                            arrayList.get(index).getMerchant_id(),
                            arrayList.get(index).getFreeitemid(),
                            arrayList.get(index).getFreeitemname(),
                            arrayList.get(index).getFreeitemqty(),
                            arrayList.get(index).getValidfrom(),
                            arrayList.get(index).getValidto()));
                    int c = Integer.parseInt(v.getTag().toString().trim());

                    databaseHelper.addCartItems(arrayList.get(index).getMerchant_id(),
                            arrayList.get(index).getMerchant_name(),
                            valueOf(arrayList.get(index).getQnty()),
                            valueOf(arrayList.get(index).getMinqnty()),
                            arrayList.get(index).getOffers(), valueOf(arrayList.get(index).getPrice()),
                            product_name,
                            valueOf(arrayList.get(index).getAmount()), product_id,
                            arrayList.get(index).getFreeitemid(),
                            valueOf(arrayList.get(index).getFreeitemqty()),
                            arrayList.get(index).getFreeitemname(),
                            arrayList.get(index).getValidfrom(),
                            arrayList.get(index).getValidto(), product_img);

                    if (databaseHelper.getCartItems() > 0) {
                       *//* textview_cart_count.setVisibility(View.VISIBLE);
                        textview_cart_count.setText(databaseHelper.getCartItems() + "");*//*
                    } else {
                       // textview_cart_count.setVisibility(View.INVISIBLE);
                    }

                   *//* ((LinearLayout) baseView.getParent())
                            .removeView(baseView);
                    arrayList
                            .remove(index);*//*
                }
            }
        });

        if (databaseHelper.getCartItems() > 0) {
            *//*textview_cart_count.setVisibility(View.VISIBLE);
            textview_cart_count.setText(databaseHelper.getCartItems() + "");*//*
        } else {
            //textview_cart_count.setVisibility(View.INVISIBLE);
        }


        //containerLayout_one.addView(baseView);
    }*/

    private void getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable(parent)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetvendorsOnProductname().execute();
            } else {
                new StartSession_tbuds(parent, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetvendorsOnProductname().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
            Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
          //  layoutTwo.setVisibility(View.GONE);
            //containerLayout_two.setVisibility(View.GONE);
            //scrollview_cart_list1.setVisibility(View.GONE);
        }
    }

    protected void parseJson(String json) {

        arrayList_two.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                String minqnty = "0";
                int MRP = 0;
                for (int i = 0; i < jsonArray.length(); i++) {

                    if ((jsonArray.getJSONObject(i)
                            .getString("Minqty").equalsIgnoreCase("0"))) {
                        minqnty = "1";
                    } else if ((jsonArray.getJSONObject(i)
                            .getString("Minqty").equalsIgnoreCase("NA"))) {
                        minqnty = "1";
                    } else {
                        minqnty = jsonArray.getJSONObject(i)
                                .getString("Minqty");
                    }

                    if ((jsonArray.getJSONObject(i)
                            .getString("MRP").equalsIgnoreCase("NA"))) {
                        MRP = 0;
                    }
        Boolean a = (minqnty.length() > 2);
                    Boolean b = (jsonArray.getJSONObject(i).getString("itemname").contains("?"));

                    if (!(minqnty.length() > 2)  &&  !( jsonArray.getJSONObject(i)
                            .getString("itemname").contains("?"))){
                        String fromdate = get_date(jsonArray.getJSONObject(i).getString("validfrom"));
                        String todate = get_date(jsonArray.getJSONObject(i).getString("validto"));
                       /* jsonArray.getJSONObject(i)
                                .getString("validfrom").equalsIgnoreCase("NA") &&
                                jsonArray.getJSONObject(i).getString("validto").equalsIgnoreCase("NA")*/

                            if (jsonArray.getJSONObject(i)
                                    .getString("MRP").equalsIgnoreCase("0")
                                    && (!(valueOf(//jsonArray.getJSONObject(i).getInt("itemmrp"))).equalsIgnoreCase("NA"))
                                    jsonArray.getJSONObject(i).getString("itemmrp"))).equalsIgnoreCase("NA"))
                            ) {

                                String discount = "";

                                if (!(jsonArray.getJSONObject(i)
                                        .getString("DiscrateMRP").equalsIgnoreCase("NA"))
                                        || (!(jsonArray.getJSONObject(i)
                                        .getString("Discratepercent").equalsIgnoreCase("NA")))) {

                                    if (!(jsonArray.getJSONObject(i)
                                            .getString("DiscrateMRP").equalsIgnoreCase("NA"))) {

                                        discount = jsonArray.getJSONObject(i)
                                                .getString("DiscrateMRP") + " ₹";

                                    } else if (!(jsonArray.getJSONObject(i)
                                            .getString("Discratepercent").equalsIgnoreCase("NA"))) {

                                        discount = jsonArray.getJSONObject(i)
                                                .getString("Discratepercent") + " %";

                                    }
                                } else {
                                    discount = "0 ₹";
                                }
                                bean = new Merchants_against_items();
                                bean.setMerchant_name(jsonArray.getJSONObject(i)
                                        .getString("custVendorname"));
                                bean.setQnty(Integer.parseInt(minqnty));
                                bean.setMinqnty(Integer.parseInt(minqnty));
                                bean.setOffers(discount);
                                bean.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                                        .getString("itemmrp")));
                                bean.setPerDigit(PurDigit);

                                bean.setMerchant_id(jsonArray.getJSONObject(i)
                                        .getString("CustVendorMasterId"));
                                if (jsonArray.getJSONObject(i)
                                        .getString("Freeitemid").equalsIgnoreCase("NA")) {
                                    bean.setFreeitemid("");
                                    bean.setFreeitemqty(0);
                                    bean.setFreeitemname("");
                                } else {
                                    bean.setFreeitemid(jsonArray.getJSONObject(i)
                                            .getString("Freeitemid"));
                                    bean.setFreeitemqty(jsonArray.getJSONObject(i)
                                            .getInt("Freeitemqty"));
                                    String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                            .getString("Freeitemid"));
                                    bean.setFreeitemname(freeitemname);

                            }
                            bean.setValidfrom(jsonArray.getJSONObject(i)
                                    .getString("validfrom"));

                                bean.setValidto(jsonArray.getJSONObject(i)
                                        .getString("validto"));

                                arrayList_two.add(bean);


                            } else if (compare_date(fromdate, todate) == true &&
                                    (!(valueOf(jsonArray.getJSONObject(i)
                                            .getString("MRP"))).equalsIgnoreCase("NA"))) {
                                String discount = "";


                                if (!(jsonArray.getJSONObject(i)
                                        .getString("DiscrateMRP").equalsIgnoreCase("NA"))
                                        || (!(jsonArray.getJSONObject(i)
                                        .getString("Discratepercent").equalsIgnoreCase("NA")))) {

                                    if (!(jsonArray.getJSONObject(i)
                                            .getString("DiscrateMRP").equalsIgnoreCase("NA"))) {

                                        discount = jsonArray.getJSONObject(i)
                                                .getString("DiscrateMRP") + " ₹";

                                    } else if (!(jsonArray.getJSONObject(i)
                                            .getString("Discratepercent").equalsIgnoreCase("NA"))) {

                                        discount = jsonArray.getJSONObject(i)
                                                .getString("Discratepercent") + " %";

                                    }
                                } else {
                                    discount = "0 ₹";
                                }

                                bean = new Merchants_against_items();
                                bean.setMerchant_name(jsonArray.getJSONObject(i)
                                        .getString("custVendorname"));
                                bean.setQnty(1);//jsonArray.getJSONObject(i).getInt("Minqty")
                                bean.setMinqnty(Integer.parseInt(minqnty));
                                bean.setOffers(discount);
                                bean.setPrice(Float.parseFloat(jsonArray.getJSONObject(i)
                                        .getString("MRP")));

                                bean.setPerDigit(PurDigit);
                                bean.setMerchant_id(jsonArray.getJSONObject(i)
                                        .getString("CustVendorMasterId"));
                                if (jsonArray.getJSONObject(i)
                                        .getString("Freeitemid").equalsIgnoreCase("NA")) {
                                    bean.setFreeitemid("");
                                    bean.setFreeitemqty(0);
                                    bean.setFreeitemname("");
                                } else {
                                    bean.setFreeitemid(jsonArray.getJSONObject(i)
                                            .getString("Freeitemid"));
                                    bean.setFreeitemqty(jsonArray.getJSONObject(i)
                                            .getInt("Freeitemqty"));
                                    String freeitemname = getFreeItemname(jsonArray.getJSONObject(i)
                                            .getString("Freeitemid"));
                                    bean.setFreeitemname(freeitemname);

                                }
                                bean.setValidfrom(jsonArray.getJSONObject(i)
                                        .getString("validfrom"));

                                bean.setValidto(jsonArray.getJSONObject(i)
                                        .getString("validto"));

                                arrayList_two.add(bean);


                            } else {
                            }

                        /*} else {
                            String a = "sss";
                        }*/
                    }
                }
            } else {
                bean = new Merchants_against_items();
                bean.setMerchant_name("No seller found");
                bean.setQnty(0);
                bean.setMinqnty(0);
                bean.setOffers("0");
                bean.setPrice(0);
                bean.setMerchant_id("0");
                bean.setFreeitemid("0");
                bean.setFreeitemqty(0);
                bean.setFreeitemname("0");
                arrayList_two.add(bean);
            }

            if (arrayList_two.size() == 0) {
                bean = new Merchants_against_items();
                bean.setMerchant_name("No seller found");
                bean.setQnty(0);
                bean.setMinqnty(0);
                bean.setOffers("0");
                bean.setPrice(0);
                bean.setMerchant_id("");
                bean.setFreeitemid("0");
                bean.setFreeitemqty(0);
                bean.setFreeitemname("0");

                arrayList_two.add(bean);
            }

            showlist_new();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getFreeItemname(String id) {
     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
     //   SQLiteDatabase db = db1.getWritableDatabase();
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

    private void showlist_new() {
        if (arrayList_two.size() > 0) {
            for (int i = 0; i < arrayList_two.size(); i++) {
                //addView_new(i);
            }
        } else {

        }
    }

    /*private void addView_new(int i) {
        // int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_murchant_details,
                null);

       *//* TextView textview_product_sellers = (TextView) baseView
                .findViewById(R.id.textview_product_sellers);*//*
        *//*final TextView textview_freeitem = (TextView) baseView
                .findViewById(R.id.textview_freeitem);
        TextView textview_product_offers = (TextView) baseView
                .findViewById(R.id.textview_product_offers);
        final TextView textview_product_rate = (TextView) baseView
                .findViewById(R.id.textview_product_rate);
                *//*

        final TextView textview_product_subtotal = (TextView) baseView
                .findViewById(R.id.textview_product_subtotal);
        final EditText edit_product_qty = (EditText) baseView
                .findViewById(R.id.edit_product_qty);

        edit_product_qty.setFocusable(true);


        Button imageviewAddProduct = (Button) baseView.findViewById(R.id.button_product_add_to_cart);

        if (arrayList_two.get(i).getMerchant_name().equalsIgnoreCase("No seller found")) {
            //textview_product_sellers.setText(arrayList_two.get(i).getMerchant_name());
            edit_product_qty.setVisibility(View.GONE);
            *//*textview_freeitem.setVisibility(View.GONE);
            textview_product_offers.setVisibility(View.GONE);*//*
           // textview_product_rate.setVisibility(View.GONE);
            textview_product_subtotal.setVisibility(View.GONE);
            edit_product_qty.setVisibility(View.GONE);
           // imageviewAddProduct.setVisibility(View.GONE);

        } else {
            edit_product_qty.setTag(containerLayout_two.getChildCount());

            int a = containerLayout_two.getChildCount();

            edit_product_qty.setText(Float
                    .toString(arrayList_two.get(i)
                            .getQnty()));
            int textLength = edit_product_qty.getText().length();
            edit_product_qty.setSelection(textLength, textLength);

            edit_product_qty.addTextChangedListener(new TextWatcher() {

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
                    int pos = Integer
                            .parseInt(edit_product_qty.getTag().toString());
                    float offer = 0;
                    int getfreeitem = 0;
                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0))) {

                     //   textview_product_rate.setText("0 ₹");
                        textview_product_subtotal.setText("0 ₹");
                    } else {
                        String discount = arrayList_two.get(pos).getOffers();//10%
                        String splits[] = discount.split(" ");
                        String disamt = splits[0];//10
                        String distype = splits[1];
           *//*3  *//*
                        int get_qnty = Integer.parseInt(edit_product_qty.getText().toString().trim()) / arrayList_two.get(pos).getMinqnty();

                        if (arrayList_two.get(pos).getFreeitemqty() > 0) {
                            getfreeitem = (Integer.parseInt(edit_product_qty.getText().toString().trim())
                                    * arrayList_two.get(pos).getFreeitemqty()) /
                                    arrayList_two.get(pos).getMinqnty();
                           // textview_freeitem.setText("Get " + getfreeitem + " " + arrayList_two.get(pos).getFreeitemname() + " free");
                        }
                        if (distype.equalsIgnoreCase("₹")) {
                            offer = (Integer.parseInt(disamt)) * get_qnty;
                        } else if (distype.equalsIgnoreCase("%")) {

                            offer = ((arrayList_two.get(pos).getPrice()) * Float.parseFloat(disamt) / 100) * get_qnty; //0.3
                        }


                       *//* textview_product_rate.setText(
                                (arrayList_two.get(pos).getPrice()) + " ₹");*//*
                        float subtotal = Float.parseFloat(edit_product_qty.getText().toString().trim())
                                * (arrayList_two.get(pos).getPrice());
                        textview_product_subtotal.setText((subtotal - offer) + " ");//₹

                        arrayList_two.get(pos).setQnty(Integer.parseInt(s.toString()));
                        //  arrayList_two.get(pos).setFreeitemqty(getfreeitem);
                        arrayList_two.get(pos).setAmount((subtotal - offer));

                    }

                }
            });
            float offer = 0;
            float getfreeitem = 0;
            String discount = arrayList_two.get(i).getOffers();//10%
            String splits[] = discount.split(" ");
            String disamt = splits[0];//10
            String distype = splits[1];
           *//*3  *//*
            float get_qnty = arrayList_two.get(i).getQnty() / arrayList_two.get(i).getMinqnty();
            if (arrayList_two.get(i).getFreeitemqty() > 0) {
                getfreeitem = //arrayList_two.get(i).getQnty() / arrayList_two.get(i).getFreeitemqty();

                        (arrayList_two.get(i).getQnty() *
                                arrayList_two.get(i).getFreeitemqty()) / arrayList_two.get(i).getMinqnty();

               // textview_freeitem.setText("Get " + getfreeitem + " " + arrayList_two.get(i).getFreeitemname() + " free");
            }

            if (distype.equalsIgnoreCase("₹")) {

                offer = (Integer.parseInt(disamt)) * get_qnty;
            } else if (distype.equalsIgnoreCase("%")) {

                offer = ((arrayList_two.get(i).getPrice()) * Float.parseFloat(disamt) / 100) * get_qnty; //0.3
            }
            *//*textview_product_sellers.setText(arrayList_two.get(i).getMerchant_name());
            //textview_product_offers.setText(arrayList_two.get(i).getOffers());
            textview_product_rate.setText(
                    (arrayList_two.get(i).getPrice()) + " ₹");*//*

            float subtotal = Float.parseFloat(edit_product_qty.getText().toString().trim())
                    * (arrayList_two.get(i).getPrice());
            textview_product_subtotal.setText((subtotal - offer) + "");// ₹

            String qty = edit_product_qty.getText().toString().trim();
            arrayList_two.get(i).setQnty(Integer.parseInt(qty));
            arrayList_two.get(i).setAmount((subtotal - offer));
            //      arrayList_two.get(i).setFreeitemqty(getfreeitem);
            imageviewAddProduct.setTag(containerLayout_two.getChildCount());

            int b = containerLayout_two.getChildCount();
            imageviewAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int index = Integer.parseInt(v.getTag()
                            .toString().trim());

                    if (tcf.getCartItems_chkrecords(arrayList_two.get(index).getMerchant_id(),
                            product_name, product_id) > 0) {
                        //Toast.makeText(parent, "Item already added in cart", Toast.LENGTH_LONG).show();
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        ContentValues cv = new ContentValues();

                        cv.put("qnty", arrayList_two.get(index).getQnty());
                        cv.put("minqnty", arrayList_two.get(index).getMinqnty());
                        cv.put("offers", arrayList_two.get(index).getOffers());
                        cv.put("price", arrayList_two.get(index).getPrice());
                        cv.put("Amount", valueOf(arrayList_two.get(index).getAmount()));
                        cv.put("Freeitemid", arrayList_two.get(index).getFreeitemid());
                        cv.put("Freeitemqty", valueOf(arrayList_two.get(index).getFreeitemqty()));
                        cv.put("Freeitemname", arrayList_two.get(index).getFreeitemname());
                        cv.put("validfrom", arrayList_two.get(index).getValidfrom());
                        cv.put("validto", arrayList_two.get(index).getValidto());

                        sql_db.update(DatabaseHandlers.TABLE_CART_ITEM, cv,
                                "MerchantId=? and Product_id=?",
                                new String[]{arrayList_two.get(index).getMerchant_id(),
                                        product_id});
                        Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();
                        addProductsToCartArrayList.add(new AddProductsToCart(product_id, product_name,
                                        arrayList_two.get(index).getMerchant_name(),
                                        arrayList_two.get(index).getOffers(),
                                        arrayList_two.get(index).getPrice(),
                                        arrayList_two.get(index).getQnty(),
                                        arrayList_two.get(index).getMinqnty(),
                                        arrayList_two.get(index).getMerchant_id(),
                                        arrayList_two.get(index).getFreeitemid(),
                                        arrayList_two.get(index).getFreeitemname(),
                                        arrayList_two.get(index).getFreeitemqty(),
                                        arrayList_two.get(index).getValidfrom(),
                                        arrayList_two.get(index).getValidto())
                        );
                        int c = Integer.parseInt(v.getTag().toString().trim());
                        tcf.addCartItems(arrayList_two.get(index).getMerchant_id(),
                                arrayList_two.get(index).getMerchant_name(),
                                valueOf(arrayList_two.get(index).getQnty()),
                                valueOf(arrayList_two.get(index).getMinqnty()),
                                arrayList_two.get(index).getOffers(),
                                valueOf(arrayList_two.get(index).getPrice()),
                                product_name, valueOf(arrayList_two.get(index).getAmount()), product_id,
                                arrayList_two.get(index).getFreeitemid(),
                                valueOf(arrayList_two.get(index).getFreeitemqty()),
                                arrayList_two.get(index).getFreeitemname(),
                                arrayList_two.get(index).getValidfrom(),
                                arrayList_two.get(index).getValidto(), product_img);


                        if (tcf.getCartItems() > 0) {
                            *//*textview_cart_count.setVisibility(View.VISIBLE);
                            textview_cart_count.setText(databaseHelper.getCartItems() + "");*//*
                        } else {
                           // textview_cart_count.setVisibility(View.INVISIBLE);
                        }

                    }

                }
            });
        }
        // showTotal();

       // containerLayout_two.addView(baseView);
    }*/

    class GetvendorsOnProductname extends AsyncTask<Void, Void, Void> {
        String responseString = "";
        String response_productDetails = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progressDialog.setMessage("Fetching Merchants...");
            progressDialog.show();*/

            progress = ProgressHUD.show(parent,
                    "Fetching Merchants...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_ProductListDetails = AnyMartData.MAIN_URL +
                    AnyMartData.METHOD_GET_VENDOR_ON_PRODUCTNAME +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&itemasterid="+ product_id +
                    "&city="+ AnyMartData.CITY ;

            String url1 = url_ProductListDetails;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_ProductListDetails, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                /*URL url_getProductDetails = new URL(url_ProductListDetails);
                URL url = url_getProductDetails;
                urlConnection = url_getProductDetails.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_getProductdetails = new StringBuffer();
                String line;
                StringBuilder str_build_getdetails = new StringBuilder();
                while((line = bufferedReader.readLine())!= null){
                    stringBuff_getProductdetails = stringBuff_getProductdetails.append(line);
                }

                responseString = stringBuff_getProductdetails.toString().replaceAll("^\"|\"$", "");*/
                response_productDetails = responseString.replaceAll("\\\\","");

                System.out.println("resp ="+response_productDetails);
            } catch (Exception e) {
                response_productDetails = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (response_productDetails.equalsIgnoreCase("Session Expired")) {

                /*layoutTwo.setVisibility(View.GONE);
                containerLayout_two.setVisibility(View.GONE);
                scrollview_cart_list1.setVisibility(View.GONE);*/
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession_tbuds(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetvendorsOnProductname().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });

                }
            } else if (response_productDetails.equalsIgnoreCase("error")) {

                Toast.makeText(parent, "The server is taking too long to respond OR something is " +
                        "wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
                /*layoutTwo.setVisibility(View.GONE);
                containerLayout_two.setVisibility(View.GONE);
                scrollview_cart_list1.setVisibility(View.GONE);*/
            } else {

               // layoutTwo.setVisibility(View.VISIBLE);
                //containerLayout_two.setVisibility(View.VISIBLE);
                //containerLayout_two.removeAllViews();
               // scrollview_cart_list1.setVisibility(View.VISIBLE);

                if (tcf.getMerchantsAgainst_Items(product_name) > 0) {
                    /*layoutOne.setVisibility(View.VISIBLE);
                    containerLayout_one.setVisibility(View.VISIBLE);
                    scrollview_cart_list.setVisibility(View.VISIBLE);*/
                } else {
                    /*layoutOne.setVisibility(View.GONE);
                    containerLayout_one.setVisibility(View.GONE);
                    scrollview_cart_list.setVisibility(View.GONE);*/
                }
                json = response_productDetails;
                parseJson(json);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            /*case R.id.refresh:
               // getDataFromServer();
               // GetOrderedItemsInList();
                break;*/
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

       // Intent i = new Intent(parent, MainActivity.class);
       // startActivity(i);
        finish();
    }

    public String get_date(String d) {
        String toDate;
        try {

            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy hh:mm a");//Feb 23 2016 12:16PM

            SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd yyyy");
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
}

