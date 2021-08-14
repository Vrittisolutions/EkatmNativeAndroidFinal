package com.vritti.sales.OrderBookingNew;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.WishListAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    private Context parent;
    ListView wishList;
    Button btndiscard;
    EditText edtsearch;
    TextView total_amount;
    LinearLayout noitems;
    ImageView imgzoomview;
    LinearLayout imgzoomview_lay;
    TextView txtbname, txtiname;
    public static ArrayList<AllCatSubcatItems> arrayList_bean;
    private ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<MyCartBean> myCartBeanArrayList;;
    AllCatSubcatItems beanwishlistitems;
    Merchants_against_items bean_wishlist;
    SQLiteDatabase sql_db;
    WishListAdapter wAdapter;
    JSONArray jsonArray;
    private DatabaseHandlers databaseHelper;
    OrderHistoryBean bean;
    private String DateToStr;
    Button btnplaceproceed;
    private String[] user;
    String Subcatid;
    private String PurDigit;
    String res = "";

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    SharedPreferences sharedpreferences;
    MyCartBean myCartBean;

    static float finalamt = 0;
    JSONArray jsonArray1;
    private float amt = 0;
    WishListAdapter adapter;
    boolean shopByMerchant = false;

    Utility ut;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    String dabasename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        getDataFromDatabase();

        setListeners();

    }

    public void init(){
        final ActionBar ab = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.mycart)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        parent = WishlistActivity.this;

        txtiname = findViewById(R.id.txtiname);
        txtbname = findViewById(R.id.txtbname);
        imgzoomview = findViewById(R.id.imgzoomview);
        imgzoomview_lay = findViewById(R.id.imgzoomview_lay);
        wishList = (ListView)findViewById(R.id.wishlist2);
        btnplaceproceed = (Button)findViewById(R.id.button_placeproceed);
        total_amount = findViewById(R.id.total_amount);
        btndiscard = findViewById(R.id.btndiscard);
        noitems = findViewById(R.id.noitems);
        edtsearch = findViewById(R.id.edtsearch);
        noitems.setVisibility(View.GONE);
        wishList.setVisibility(View.VISIBLE);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        ut = new Utility();
        tcf = new Tbuds_commonFunctions(WishlistActivity.this);
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
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.LANGUAGE = sharedpreferences.getString("Language","");
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.CatImgPath = sharedpreferences.getString("CatImgPath","");
        AnyMartData.SubCatImgPath = sharedpreferences.getString("SubCatImgPath","");
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");


        /*Picasso.with(parent).load(image_URL)//Your image link url.into(actionBarImage);*/

        try{

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
                            ab.setIcon(d);
                            ab.setDisplayShowHomeEnabled(true);
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
        }catch (Exception e){
            e.printStackTrace();
        }

        // arrayList_bean.clear();
        arrayList_bean = new ArrayList<AllCatSubcatItems>();
        myCartBeanArrayList = new ArrayList<MyCartBean>();

    }

    public void getDataFromDatabase(){

        try{
            myCartBeanArrayList.clear();

            Cursor cursor1 = sql_db.rawQuery("Select * from " + DatabaseHandlers.TABLE_CART_ITEM_new+" Order by Product_name ASC" , null);
            Log.d("test", "" + cursor1.getCount());

            if(cursor1.getCount() > 0){
                cursor1.moveToFirst();
                do{
                    String Itemname = cursor1.getString(cursor1.getColumnIndex("Product_name"));
                    String ItemID = cursor1.getString(cursor1.getColumnIndex("Product_id"));
                    float price = Float.valueOf(cursor1.getString(cursor1.getColumnIndex("price")));
                    float qnty = Float.valueOf(cursor1.getString(cursor1.getColumnIndex("qnty")));
                    float Amount = Float.valueOf(cursor1.getString(cursor1.getColumnIndex("Amount")));
                    String MerchantId = cursor1.getString(cursor1.getColumnIndex("MerchantId"));
                    String MerchantName = cursor1.getString(cursor1.getColumnIndex("MerchantName"));
                    String categoryId = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                    String categoryName = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                    String subcategoryId = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));
                    String subcategoryName = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                    float mrp = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("MRP")));
                    String ItemImgPath = cursor1.getString(cursor1.getColumnIndex("ItemImgPath"));
                    String MinOrdQty = cursor1.getString(cursor1.getColumnIndex("minqnty"));
                    String MaxOrdQty = cursor1.getString(cursor1.getColumnIndex("MaxOrdQty"));
                    String Range = cursor1.getString(cursor1.getColumnIndex("Range"));
                    String OutOfStock = cursor1.getString(cursor1.getColumnIndex("OutOfStock"));
                    String Distance = cursor1.getString(cursor1.getColumnIndex("Distance"));
                    String UOMCode = cursor1.getString(cursor1.getColumnIndex("UOMCode"));
                    String PurDigit = cursor1.getString(cursor1.getColumnIndex("UomDigit"));
                    String Brand = cursor1.getString(cursor1.getColumnIndex("Brand"));
                    String Content = cursor1.getString(cursor1.getColumnIndex("Content"));
                    String ContentUOM = cursor1.getString(cursor1.getColumnIndex("ContentUOM"));
                    String SellingUOM = cursor1.getString(cursor1.getColumnIndex("SellingUOM"));
                    String PackOfQty = cursor1.getString(cursor1.getColumnIndex("PackOfQty"));
                    String FreeAboveAmt = cursor1.getString(cursor1.getColumnIndex("FreeAboveAmt"));
                    String FreeDelyMaxDist = cursor1.getString(cursor1.getColumnIndex("FreeDelyMaxDist"));
                    String MinDelyKg = cursor1.getString(cursor1.getColumnIndex("MinDelyKg"));
                    String MinDelyKm = cursor1.getString(cursor1.getColumnIndex("MinDelyKm"));
                    String ExprDelyWithinMin = cursor1.getString(cursor1.getColumnIndex("ExprDelyWithinMin"));
                    String ExpressDelyChg = cursor1.getString(cursor1.getColumnIndex("ExpressDelyChg"));
                    String Open_slots = cursor1.getString(cursor1.getColumnIndex("Open_slots"));
                    String OpenTime1 = cursor1.getString(cursor1.getColumnIndex("OpenTime1"));
                    String OpenTime2 = cursor1.getString(cursor1.getColumnIndex("OpenTime2"));
                    String CloseTime1 = cursor1.getString(cursor1.getColumnIndex("CloseTime1"));
                    String CloseTime2 = cursor1.getString(cursor1.getColumnIndex("CloseTime2"));
                    String IsDelivery = cursor1.getString(cursor1.getColumnIndex("IsDelivery"));
                    String UPI = cursor1.getString(cursor1.getColumnIndex("UPI"));

                    Cursor curperdigit = sql_db.rawQuery("Select PurDigit  from "
                            + DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS +
                            " WHERE itemmasterid= '"+ ItemID +"'", null);
                    Log.e("cnt", String.valueOf(curperdigit.getCount()));

                    if(curperdigit.getCount() > 0){
                        curperdigit.moveToFirst();
                        do{
                            PurDigit = curperdigit.getString(curperdigit.getColumnIndex("PurDigit"));
                        }while (curperdigit.moveToNext());
                    }

                    myCartBean = new MyCartBean();
                    myCartBean.setProduct_name(Itemname);
                    myCartBean.setProduct_id(ItemID);
                    myCartBean.setQnty(qnty);
                    myCartBean.setAmount(Amount);
                    myCartBean.setPrice(price);
                    myCartBean.setMerchantName(MerchantName);
                    myCartBean.setMerchantId(MerchantId);
                    myCartBean.setCategoryId(categoryId);
                    myCartBean.setCategoryName(categoryName);
                    myCartBean.setSubCategoryName(subcategoryName);
                    myCartBean.setSubCategoryId(subcategoryId);
                    myCartBean.setPerdigit(PurDigit);
                    myCartBean.setMrp(mrp);
                    myCartBean.setItemImgPath(ItemImgPath);
                    myCartBean.setMinqnty(MinOrdQty);
                    myCartBean.setMaxOrdQty(MaxOrdQty);
                    myCartBean.setRange(Range);
                    myCartBean.setOutOfStock(OutOfStock);
                    myCartBean.setDistance(Distance);
                    myCartBean.setUOMCode(UOMCode);
                    myCartBean.setBrand(Brand);
                    myCartBean.setContent(Content);
                    myCartBean.setContentUOM(ContentUOM);
                    myCartBean.setSellingUOM(SellingUOM);
                    myCartBean.setCatImgPath(AnyMartData.CatImgPath);
                    myCartBean.setSubCatImgPath(AnyMartData.SubCatImgPath);
                    myCartBean.setBusiSegImgPath(AnyMartData.SpecImgPath);
                    myCartBean.setPackOfQty(PackOfQty);
                    myCartBean.setFreeAboveAmt(FreeAboveAmt);
                    myCartBean.setFreeDelyMaxDist(FreeDelyMaxDist);
                    myCartBean.setMinDelyKg(MinDelyKg);
                    myCartBean.setMinDelyKm(MinDelyKm);
                    myCartBean.setExprDelyWithinMin(ExprDelyWithinMin);
                    myCartBean.setExpressDelyChg(ExpressDelyChg);
                    myCartBean.setOpen_slots(Open_slots);
                    myCartBean.setOpenTime1(OpenTime1);
                    myCartBean.setOpenTime2(OpenTime2);
                    myCartBean.setCloseTime1(CloseTime1);
                    myCartBean.setCloseTime2(CloseTime2);
                    myCartBean.setIsMerchDelivery(IsDelivery);
                    myCartBean.setUPIMerch(UPI);
                    myCartBeanArrayList.add(myCartBean);

                }while (cursor1.moveToNext());

            }else {
                noitems.setVisibility(View.VISIBLE);
                wishList.setVisibility(View.GONE);
                //showNoDataInWishlist();
             //   Toast.makeText(getApplicationContext(),"No products in cart",Toast.LENGTH_SHORT).show();
            }

            if(myCartBeanArrayList.size() == 0){
                total_amount.setText("0.00 ₹");
            }

            adapter = new WishListAdapter(parent, myCartBeanArrayList);
            wishList.setAdapter(adapter);
            calculate_total_new();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setListeners(){
        imgzoomview_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgzoomview_lay.setVisibility(View.GONE);
                return false;
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try{
                    String search = edtsearch.getText().toString();
                    adapter.filter(edtsearch.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btndiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcf.clearTable(parent, DatabaseHandlers.TABLE_CART_ITEM_new);
                Toast.makeText(parent,""+getResources().getString(R.string.cart_discarded), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btnplaceproceed.setOnClickListener(new View.OnClickListener() {

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
                    Intent intent;

                   /* if(AnyMartData.AppCode.equalsIgnoreCase("SM")){
                        intent = new Intent(WishlistActivity.this, CheckoutActivity.class);
                    }else {
                        intent = new Intent(WishlistActivity.this, CheckoutActivity_Multimerchant.class);
                    }*/

                    intent = new Intent(WishlistActivity.this, CheckoutActivity_Multimerchant.class);
                    b.putString("OrderedItems", jsonArray1.toString());
                    //b.putString("SubCategoryId",Subcatid);
                    b.putString("PayableAmount", String.valueOf(finalamt));
                    b.putString("PurDigit", PurDigit);
                    b.putString("callFrom", "WishList");
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(parent, ""+getResources().getString(R.string.no_items_in_cart), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            /*case R.id.refresh:
              //  getDataFromServer();
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
            /*Intent i = new Intent(parent, com.vritti.freshmart.customer.MainActivity.class);
            startActivity(i);*/
            finish();
        }

    public void calculate_total_new(){
        try{

            finalamt =0;
            String qry = "Select Amount from "+DatabaseHandlers.TABLE_CART_ITEM_new;
            Cursor c = sql_db.rawQuery(qry,null);
            if(c.getCount()>0){
                c.moveToFirst();
                do {
                    finalamt = finalamt + Float.parseFloat(c.getString(c.getColumnIndex("Amount")));
                }while (c.moveToNext());
            }else {
                c.close();
            }

            float payableAmount = finalamt;
            double amount = Double.parseDouble(String.valueOf(finalamt));
            DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
            String formatted = formatter.format(amount);
            // total_toPay.setText(String.valueOf(amt)+" ₹");
            total_amount.setText(formatted + " ₹");
            // txtcartcount.setText(myCartBeanArrayList.size());

            if(myCartBeanArrayList.size() == 0){
                total_amount.setText("0" + " ₹");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void calculate_total(){
        finalamt =0;
        for (int k = 0; k < myCartBeanArrayList.size(); k++) {
            finalamt = finalamt + myCartBeanArrayList.get(k).getAmount();
        }
        float payableAmount = finalamt;
        double amount = Double.parseDouble(String.valueOf(payableAmount));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        total_amount.setText(formatted+" ₹");

        if(myCartBeanArrayList.size() == 0){
            total_amount.setText("0" + " ₹");
        }

        updateCart();
    }

    public void deleteItemFromCart(String itemid, String merchId){
        String q = "Select * from "+ DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'" +
                " AND MerchantId='"+merchId+"'";
        Cursor c = sql_db.rawQuery(q,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                //same item with same qty present, delete that item
                String qry = "Delete from "+DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'"+
                        " AND MerchantId='"+merchId+"'";
                Cursor c1 = sql_db.rawQuery(qry,null);
                if(c1.getCount()>0) {
                    c1.moveToFirst();
                }

                String q2 = "Select * from "+ DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'"+
                        " AND MerchantId='"+merchId+"'";
                Cursor c2 = sql_db.rawQuery(q2,null);
                if(c2.getCount()>0) {
                    c2.moveToFirst();
                }

                //   return true;
            }while (c.moveToNext());

        }else {
            // return false;
        }

        getDataFromDatabase();
    }

    public void expandImage(String imgPath, String itmname, String brandname, String content, String UOMCOde, String PackQtyOf){
        imgzoomview_lay.setVisibility(View.VISIBLE);

        if(PackQtyOf.equalsIgnoreCase("0") || PackQtyOf.equalsIgnoreCase("1") ||
                PackQtyOf.equalsIgnoreCase("0.0") || PackQtyOf.equalsIgnoreCase("1.0")){
            if(brandname.equalsIgnoreCase("") || brandname.equalsIgnoreCase("") ||
                    brandname.equalsIgnoreCase("null")){
                txtiname.setText(/*brandname+""+*/itmname+", "+content+" "+UOMCOde);
            }else {
                txtiname.setText(/*brandname+" "+*/itmname+", "+content+" "+UOMCOde);
            }
        }else {
            if(brandname.equalsIgnoreCase("") || brandname.equalsIgnoreCase("") ||
                    brandname.equalsIgnoreCase("null")){
                txtiname.setText(/*brandname+""+*/itmname+", "+getResources().getString(R.string.combo1)+" "+content+" "+UOMCOde+" x "+PackQtyOf);
            }else {
                txtiname.setText(/*brandname+" "+*/itmname+", "+getResources().getString(R.string.combo1)+" "+content+" "+UOMCOde+" x "+PackQtyOf);
            }
        }

        txtbname.setText(brandname);

        if(!imgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(imgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.SubCatImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.SubCatImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.CatImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.CatImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.SpecImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.SpecImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateCart(){
        for(int i=0; i<myCartBeanArrayList.size();i++){
            ContentValues values1 = new ContentValues();
            values1.put("qnty",myCartBeanArrayList.get(i).getQnty());
            values1.put("Amount",myCartBeanArrayList.get(i).getAmount());

            sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new,
                    values1, "Product_id=? and MerchantId=?",
                    new String[]{myCartBeanArrayList.get(i).getProduct_id(),
                            myCartBeanArrayList.get(i).getMerchantId()});
        }
    }

}
