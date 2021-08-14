package com.vritti.sales.OrderBookingNew;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
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
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CartList_multimerchantAdapter;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity_MultiMerchant extends AppCompatActivity {
    private Context parent;
    private Button btn_delivery,button_shopmore;
    ListView listcheckout;
    EditText edtsearch;
    ImageView imgzoomview;
    LinearLayout imgzoomview_lay;
    TextView txtbname, txtiname;
    TextView textview_cart,textview_total_amount_pay;
    SQLiteDatabase sql_db;

    CartList_multimerchantAdapter adapter;

    ArrayList<MyCartBean> tempList;
    public static ArrayList<MyCartBean> myCartBeanArrayList;
    MyCartBean myCartBean;

    private float amt = 0;
    String cType = "";

    Utility ut;
    static DatabaseHandlers databaseHelper;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    String dabasename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__multi_merchant);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        getCartItems();

        setListeners();

    }

    public void init(){
        parent = CartActivity_MultiMerchant.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.mycart)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textview_total_amount_pay = findViewById(R.id.textview_total_amount_pay);
        textview_cart = findViewById(R.id.textview_cart);
        btn_delivery =  findViewById(R.id.btn_delivery);
        button_shopmore =  findViewById(R.id.button_shopmore);
        listcheckout = findViewById(R.id.listcheckout);
        edtsearch = findViewById(R.id.edtsearch);
        txtiname = findViewById(R.id.txtiname);
        txtbname = findViewById(R.id.txtbname);
        imgzoomview = findViewById(R.id.imgzoomview);
        imgzoomview_lay = findViewById(R.id.imgzoomview_lay);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CartActivity_MultiMerchant.this);
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


        myCartBeanArrayList = new ArrayList<MyCartBean>();
        tempList = new ArrayList<MyCartBean>();

        Intent intent = getIntent();
        cType = intent.getStringExtra("cType");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

        button_shopmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;

                JSONArray jsonArray1 = new JSONArray();
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
                        String ItemImgPath = myCartBeanArrayList.get(i).getItemImgPath();

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
                            jsonObject.put("ItemImgPath", ItemImgPath);
                            //price
                            jsonArray1.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Bundle b = new Bundle();
                    Intent intent = new Intent(CartActivity_MultiMerchant.this, CheckoutActivity_Multimerchant.class);
                    b.putString("OrderedItems", jsonArray1.toString());
                    b.putString("PayableAmount", String.valueOf(amt));
                    b.putString("PurDigit", "");
                    b.putString("callFrom", "CartActivity");
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(parent, ""+getResources().getString(R.string.no_prod_to_checkout), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getCartItems(){

        if(myCartBeanArrayList.size() > 0){
            myCartBeanArrayList.clear();
        }

        String qry = "Select * from " + DatabaseHandlers.TABLE_CART_ITEM_new+" Order by Product_name ASC";
        Cursor cursor1 = sql_db.rawQuery(qry , null);
        Log.d("test", "" + cursor1.getCount());

        JSONArray jsonArray1 = new JSONArray();

        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            do{
                String ItemName = cursor1.getString(cursor1.getColumnIndex("Product_name"));
                String ItemId = cursor1.getString(cursor1.getColumnIndex("Product_id"));
                float qty = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("qnty")));
                float ToatlAmount = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("Amount")));
                float price = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("price")));
                String Merchant_Name = cursor1.getString(cursor1.getColumnIndex("MerchantName"));
                String Merchant_id = cursor1.getString(cursor1.getColumnIndex("MerchantId"));
               // String perDigit = PurDigit;
                String catid = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                String catname = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                String subcatid = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));
                String subcatname = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                float mrp = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("MRP")));
                String ItemImgPath = cursor1.getString(cursor1.getColumnIndex("ItemImgPath"));
                String MinOrdQty = cursor1.getString(cursor1.getColumnIndex("minqnty"));
                String MaxOrdQty = cursor1.getString(cursor1.getColumnIndex("MaxOrdQty"));
                String Range = cursor1.getString(cursor1.getColumnIndex("Range"));
                String OutOfStock = cursor1.getString(cursor1.getColumnIndex("OutOfStock"));
                String Distance = cursor1.getString(cursor1.getColumnIndex("Distance"));
                String UomDigit = cursor1.getString(cursor1.getColumnIndex("UomDigit"));
                String UOMCode = cursor1.getString(cursor1.getColumnIndex("UOMCode"));
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

                 myCartBean = new MyCartBean();

                try {
                    myCartBean.setProduct_name(ItemName);
                    myCartBean.setProduct_id(ItemId);
                    myCartBean.setQnty(qty);
                    myCartBean.setAmount(ToatlAmount);
                    myCartBean.setPrice(price);
                    myCartBean.setMerchantName(Merchant_Name);
                    myCartBean.setMerchantId(Merchant_id);
                    myCartBean.setCategoryId(catid);
                    myCartBean.setCategoryName(catname);
                    myCartBean.setSubCategoryName(subcatname);
                    myCartBean.setSubCategoryId(subcatid);
                    myCartBean.setPerdigit(UomDigit);
                    myCartBean.setMrp(mrp);
                    myCartBean.setItemImgPath(ItemImgPath);
                    myCartBean.setMinqnty(MinOrdQty);
                    myCartBean.setMaxOrdQty(MaxOrdQty);
                    myCartBean.setRange(Range);
                    myCartBean.setOutOfStock(OutOfStock);
                    myCartBean.setDistance(Distance);
                    myCartBean.setPerdigit(UomDigit);
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

                } catch (Exception e) {
                    e.printStackTrace();
                }

                myCartBeanArrayList.add(myCartBean);

            }while (cursor1.moveToNext());

        }

        if(myCartBeanArrayList.size() == 0){
            textview_total_amount_pay.setText("0.00 ₹");
            textview_cart.setText("0");
        }

        adapter = new CartList_multimerchantAdapter(parent, myCartBeanArrayList);
        listcheckout.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        calculate_total();
        textview_cart.setText(String.valueOf(cursor1.getCount()));

    }

    public void calculate_total(){
        amt =0;
        for (int k = 0; k < myCartBeanArrayList.size(); k++) {
            amt = amt + myCartBeanArrayList.get(k).getAmount();
        }
        float payableAmount = amt;
        double amount = Double.parseDouble(String.valueOf(payableAmount));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount_pay.setText(formatted+" ₹");

        if(cType.equalsIgnoreCase("Repeat")){
            updateCart();
        }else {
            updateCart();
        }
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

        getCartItems();
    }

    public void updateList(ArrayList<MyCartBean> arraylist) {
        tempList.clear();
        tempList.addAll(arraylist);
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
