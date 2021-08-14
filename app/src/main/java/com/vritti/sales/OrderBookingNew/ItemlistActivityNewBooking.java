package com.vritti.sales.OrderBookingNew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CategoryAdapter;
import com.vritti.sales.OrderBookingNew.Adapter.ItemListAdapter_customer_new;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.String.valueOf;

public class ItemlistActivityNewBooking extends AppCompatActivity {
    Context parent;
    TextView quest;
    TextView textview_cart;
    RelativeLayout layyellow;
    ImageView imgzoomview;
    LinearLayout imgzoomview_lay;
    TextView txtbname, txtiname;
    ListView listview_itemList;
    Button button_chkproceed,button_continue;

    ItemListAdapter_customer_new adt;

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
    String cat_name, cat_id,PurDigit,CatImgPath = "";
    int SubCatCount = 0;
    String selectedCatid="",selectedCatName="";
    CategoryAdapter cadt;
    String res="";

    String LangCode = "eng";
    JSONObject jsonMain, jsonObj;
    JSONArray jsonArray_;
    JSONArray jsonArray;
    String Subcatid,SubCatName,CategoryName,custvendorID,CustomerID,CallType,ItemKey;
    String json="";

    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<AllCatSubcatItems> checkedarrayList;
    public static ArrayList<MyCartBean> arrayList_bean;
    private ArrayList<Merchants_against_items> arrayList_two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist_new_booking);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        callAPI("");

        setListeners();

    }

    public void init(){
        parent = ItemlistActivityNewBooking.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        final ActionBar ab = getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.prod)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textview_cart =  findViewById(R.id.textview_cart);
        layyellow =  findViewById(R.id.layyellow);
        txtiname = findViewById(R.id.txtiname);
        txtbname = findViewById(R.id.txtbname);
        imgzoomview = findViewById(R.id.imgzoomview);
        imgzoomview_lay = findViewById(R.id.imgzoomview_lay);
        // listview = (GridView) findViewById(R.id.listview_item_list);
        listview_itemList = findViewById(R.id.listview_item_list);
        button_chkproceed = findViewById(R.id.button_chkproceed);
        button_continue = findViewById(R.id.button_continue);

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList_bean = new ArrayList<MyCartBean>();
        arrayList_two = new ArrayList<Merchants_against_items>();
        checkedarrayList = new ArrayList<AllCatSubcatItems>();

        try{
            sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

            ut = new Utility();
            tcf = new Tbuds_commonFunctions(ItemlistActivityNewBooking.this);
            String settingKey = ut.getSharedPreference_SettingKey(parent);
            dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            databaseHelper = new DatabaseHandlers(parent, dabasename);
            sql = databaseHelper.getWritableDatabase();
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
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = getIntent();
        CallType = intent.getStringExtra("CallType");
        Subcatid = intent.getStringExtra("SubCategoryId");
        SubCatName = intent.getStringExtra("SubCategoryName");
        CategoryName = intent.getStringExtra("CategoryName");
        CustomerID = intent.getStringExtra("CustomerID");
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+SubCatName+"</small>"));

    }

    public void setListeners(){

        layyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcf.getCartItems1() > 0){
                    Intent intent = new Intent(ItemlistActivityNewBooking.this,WishlistActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ItemlistActivityNewBooking.this,getResources().getString(R.string.no_items_in_cart),Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgzoomview_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgzoomview_lay.setVisibility(View.GONE);
                return false;
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(0);

                finish();
            }
        });

        button_chkproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addItemToCart(0)){
                    Intent intent = new Intent(parent, CartActivity_MultiMerchant.class);
                    intent.putExtra("cType","Normal");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Toast.makeText(parent,""+getResources().getString(R.string.no_items_in_cart),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void callAPI(String DefDist){
        try{
            jsonMain = new JSONObject();

            JSONObject jsonObj = new JSONObject();

            jsonObj.put("FamilyId",Subcatid);
            jsonObj.put("ItemId","");
            jsonObj.put("LangCd",LangCode);
            jsonObj.put("MerchId",AnyMartData.MerchantID);
            jsonObj.put("DefDistance","");
            jsonObj.put("LookInArea1","");
            jsonObj.put("LookInArea2","");
            jsonObj.put("Brand","");
            jsonObj.put("ItemName","");
            jsonObj.put("Rate","");
            jsonObj.put("Range","");
            jsonObj.put("InStock","");
            jsonObj.put("Shipto",AnyMartData.SHIPTOMASTERID);
            jsonObj.put("AppCode","SM");

            jsonArray = new JSONArray();
            jsonArray.put(jsonObj);

            jsonMain.put("Parameter",jsonArray);

            //call API to post data
            if (NetworkUtils.isNetworkAvailable(parent)) {

                new StartSession_tbuds(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new GetSMItemList().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class GetSMItemList extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "",res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*
            try{
                progress = ProgressHUD.show(parent,
                        ""+getResources().getString(R.string.sel_list_loaded), false, true, null);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        @Override
        protected String doInBackground(String... params) {

            String urlgetdata = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_PRODUCT_MULTISELLER;

            try {
                res = String.valueOf(Utility.OpenPostConnection(urlgetdata,
                        jsonMain.toString().replaceAll("^\"|\"$",""),ItemlistActivityNewBooking.this));
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return response_list;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (response_list.equalsIgnoreCase("No Data")) {
                //Toast.makeText(parent, ""+parent.getResources().getString(R.string.noitems), Toast.LENGTH_LONG).show();
                //  finish();
            }else if (response_list.equalsIgnoreCase("error")) {
                Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
            } else {
                json = response_list;
                parseJson_SM(json);
            }
        }
    }

    public void parseJson_SM(String json){

        deleteitem();

        try{

            arrayList.clear();

            JSONArray jsonArray = new JSONArray(json);

            String MerchId= "",MerchName = "",OutOfStock = "",MerchItemCode = "",ProductPhotoId = "",BaseRate = "",
                    SGSTAmt = "", CGSTAmt = "",MinOrdQty = "",MaxOrdQty = "",SubCategoryId = "",SubCategoryName = "",CategoryId = "",
                    CategoryName = "",ItemDescLang = "", Range = "", ItemImgPath = "", ItemName = "",ItemMasterId = "",
                    Distance = "",Uomdigit="",UOMCode="",Brand = "",Content = "",ContentUOM= "",SellingUOM = "",PackOfQty="0",
                    FreeAboveAmt="",FreeDelyMaxDist="",MinDelyKg="",MinDelyKm="",ExprDelyWithinMin="",ExpressDelyChg="",Open_slots="",
                    OpenTime1="",CloseTime1="",OpenTime2="",CloseTime2="",IsDelivery="",UPI="";
            Float SellingRate= 0.0f,MRP = 0.0f;

            for(int i=0; i<jsonArray.length();i++){
                MerchId = jsonArray.getJSONObject(i).getString("MerchId");
                MerchName = jsonArray.getJSONObject(i).getString("CustVendorName");
                OutOfStock = jsonArray.getJSONObject(i).getString("OutOfStock");
                MerchItemCode = jsonArray.getJSONObject(i).getString("MerchItemCode");
                ItemImgPath = jsonArray.getJSONObject(i).getString("ItemImgPath");
                ItemName = jsonArray.getJSONObject(i).getString("ItemName");
                ItemMasterId = jsonArray.getJSONObject(i).getString("ItemMasterId");
                MRP = Float.valueOf(jsonArray.getJSONObject(i).getString("MRP"));
                SellingRate = Float.valueOf(jsonArray.getJSONObject(i).getString("SellingRate"));
                BaseRate = jsonArray.getJSONObject(i).getString("BaseRate");
                SGSTAmt = jsonArray.getJSONObject(i).getString("SGSTAmt");
                CGSTAmt = jsonArray.getJSONObject(i).getString("CGSTAmt");
                MinOrdQty = jsonArray.getJSONObject(i).getString("MinOrdQty");
                MaxOrdQty = jsonArray.getJSONObject(i).getString("MaxOrdQty");
                SubCategoryId = jsonArray.getJSONObject(i).getString("SubCategoryId");
                SubCategoryName = jsonArray.getJSONObject(i).getString("SubCategoryName");
                CategoryId = jsonArray.getJSONObject(i).getString("CategoryId");
                CategoryName = jsonArray.getJSONObject(i).getString("CategoryName");
                ItemDescLang = jsonArray.getJSONObject(i).getString("ItemDescLang");
                Range = jsonArray.getJSONObject(i).getString("Range");

                if(Range.equalsIgnoreCase("0")){
                    Range = "false";
                }else if(Range.equalsIgnoreCase("1")){
                    Range = "true";
                }

                Distance = jsonArray.getJSONObject(i).getString("distance");
                Uomdigit = jsonArray.getJSONObject(i).getString("Uomdigit");
                UOMCode = jsonArray.getJSONObject(i).getString("UOMCode");
                Brand = jsonArray.getJSONObject(i).getString("Brand");
                Content = jsonArray.getJSONObject(i).getString("Content");
                ContentUOM = jsonArray.getJSONObject(i).getString("ContentUOM");
                SellingUOM = jsonArray.getJSONObject(i).getString("SellingUOM");

                try{
                    PackOfQty = jsonArray.getJSONObject(i).getString("PackOfQty");
                    FreeAboveAmt= jsonArray.getJSONObject(i).getString("FreeAboveAmt");
                    FreeDelyMaxDist = jsonArray.getJSONObject(i).getString("FreeDelyMaxDist");
                    MinDelyKg = jsonArray.getJSONObject(i).getString("MinDelyKg");
                    MinDelyKm = jsonArray.getJSONObject(i).getString("MinDelyKm");
                    ExprDelyWithinMin = jsonArray.getJSONObject(i).getString("ExprDelyWithinMin");
                    ExpressDelyChg = jsonArray.getJSONObject(i).getString("ExpressDelyChg");
                    Open_slots = jsonArray.getJSONObject(i).getString("Open_slots");
                    OpenTime1 = jsonArray.getJSONObject(i).getString("OpenTime1");
                    OpenTime2 = jsonArray.getJSONObject(i).getString("OpenTime2");
                    CloseTime1 = jsonArray.getJSONObject(i).getString("CloseTime1");
                    CloseTime2 = jsonArray.getJSONObject(i).getString("CloseTime2");
                    IsDelivery = jsonArray.getJSONObject(i).getString("IsDelivery");
                    UPI = jsonArray.getJSONObject(i).getString("UPI");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(ItemDescLang.equalsIgnoreCase("") || ItemDescLang.equalsIgnoreCase(null)){
                    ItemDescLang = ItemName;
                }

                String itmPhotoPath = "";
                if(ItemImgPath.equalsIgnoreCase("") || ItemImgPath.equalsIgnoreCase(null)){
                    ItemImgPath = "";
                }else {
                    ItemImgPath = CompanyURL+"/images/"+ItemImgPath;
                }

                AllCatSubcatItems itemBean = new AllCatSubcatItems();
                itemBean.setItemMasterId(ItemMasterId);
                //itemBean.setItemName(itemName);
                itemBean.setItemName(ItemDescLang);
                itemBean.setMerchant_id(MerchId);
                itemBean.setMerchant_name(MerchName);
                itemBean.setOutOfStock(OutOfStock);
                itemBean.setMerchItemCode(MerchItemCode);
                itemBean.setItemImgPath(ItemImgPath);
                itemBean.setMrp(MRP);
                itemBean.setPrice(SellingRate);
                itemBean.setBaseRate(BaseRate);
                itemBean.setCGSTAmt(SGSTAmt);
                itemBean.setSGSTAmt(CGSTAmt);
                itemBean.setMinOrdQty(MinOrdQty);
                itemBean.setMaxOrdQty(MaxOrdQty);
                itemBean.setSubCategoryId(SubCategoryId);
                itemBean.setSubCategoryName(SubCategoryName);
                itemBean.setCategoryId(CategoryId);
                itemBean.setCategoryName(CategoryName);
                itemBean.setRange(Range);
                itemBean.setPerDigit(Uomdigit);
                itemBean.setUOMcode(UOMCode);
                itemBean.setDistance(Distance);
                itemBean.setBrand(Brand);
                itemBean.setContent(Content);
                itemBean.setContentUOM(ContentUOM);
                itemBean.setSellingUOM(SellingUOM);
                itemBean.setCatImgPath(AnyMartData.CatImgPath);
                itemBean.setSubCatImgPath(AnyMartData.SubCatImgPath);
                itemBean.setBusiSegImgPath(AnyMartData.SpecImgPath);
                itemBean.setPackOfQty(PackOfQty);
                itemBean.setFreeAboveAmt(FreeAboveAmt);
                itemBean.setFreeDelyMaxDist(FreeDelyMaxDist);
                itemBean.setMinDelyKg(MinDelyKg);
                itemBean.setMinDelyKm(MinDelyKm);
                itemBean.setOpen_slots(Open_slots);
                itemBean.setOpenTime1(OpenTime1);
                itemBean.setOpenTime2(OpenTime2);
                itemBean.setCloseTime1(CloseTime1);
                itemBean.setCloseTime2(CloseTime2);
                itemBean.setExprDelyWithinMin(ExprDelyWithinMin);
                itemBean.setExpressDelyChg(ExpressDelyChg);
                itemBean.setIsMerchDelivery(IsDelivery);
                itemBean.setUPIMerch(UPI);

                tcf.addAllCatSubcatItems_new(CategoryId,CategoryName,SubCategoryId,SubCategoryName,ItemMasterId,ItemName,
                        ItemImgPath, String.valueOf(SellingRate),
                        /*jsonArray.getJSONObject(i).getString("custVendorname")*/MerchName,
                        /*jsonArray.getJSONObject(i).getString("TypeFixedPercent")*/"0",
                        "","","", String.valueOf(MRP),"","",MinOrdQty,
                        "","", Uomdigit,MerchId,"", String.valueOf(SellingRate),UOMCode,
                        Range,OutOfStock,MinOrdQty,MaxOrdQty,Distance,Brand,Content,ContentUOM,SellingUOM,
                        AnyMartData.CatImgPath, AnyMartData.SubCatImgPath,PackOfQty,FreeAboveAmt,FreeDelyMaxDist,MinDelyKg,MinDelyKm,
                        ExprDelyWithinMin,ExpressDelyChg,Open_slots,OpenTime1,OpenTime2,CloseTime1,CloseTime2,IsDelivery,UPI);

                arrayList.add(itemBean);

                Collections.sort(arrayList, new Comparator<AllCatSubcatItems>() {
                    @Override
                    public int compare(AllCatSubcatItems lhs, AllCatSubcatItems rhs) {
                        // return rhs.getItemName().compareTo(lhs.getItemName());
                        //return Float.compare(lhs.getMrp(), rhs.getMrp()); // try it now
                        Float lrate = Float.valueOf(lhs.getBaseRate());
                        Float rrate = Float.valueOf(rhs.getBaseRate());
                        return Float.compare(lrate, rrate); // try it now
                       /* return (lhs.getItemName() + lhs.getBaseRate() + lhs.getMrp())
                                .compareTo(rhs.getItemName() +  rhs.getBaseRate() + rhs.getMrp());*/
                    }
                });
            }

            adt = new ItemListAdapter_customer_new(parent,arrayList);
            listview_itemList.setAdapter(adt);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteitem(){
        ArrayList<String> listItm = new ArrayList<>();

        String qry = "Select * from "+DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS_new+" WHERE SubCategoryId='" + Subcatid + "'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                listItm.add(c.getString(c.getColumnIndex("itemmasterid")));
            }while (c.moveToNext());
        }

        String pId = "(";

        for(int j=0; j<listItm.size();j++){
            if(j!=0){
                pId = pId+",";
            }
            pId = pId + "'" + listItm.get(j).toString() +"'";
        }
        pId = pId + ")";

        int subcatcount = 0;
        int itemcount = 0;

        Cursor cursor1 = sql.rawQuery("delete from "
                + DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS_new + " where SubCategoryId='"+Subcatid +"' and" +
                " itemmasterid IN "+pId, null);
        Log.d("test", "" + cursor1.getCount());
    }

    public void expandImage(String imgPath, String itmname, String brandname,String content,String UOMCOde,String PackQtyOf){
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

    public boolean addItemToCart(int index1){
        Toast.makeText(parent,""+getResources().getString(R.string.itm_added),Toast.LENGTH_LONG).show();

        try{

            //arrayList = ((ItemListAdapter_customer) listview_itemList.getAdapter()).getAllCatSubcatItemsList();
            arrayList = ((ItemListAdapter_customer_new) listview_itemList.getAdapter()).getAllCatSubcatItemsList();

            if(arrayList.size() > 0){
                //add selected rows data to list
                for(int index=0; index<arrayList.size();index++){

                    checkItemPresent(arrayList.get(index).getItemMasterId(),arrayList.get(index).getMerchant_id());

                    String ItemImgPath = "";
                    String qimg = "Select ItemImgPath from "+DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS_new+
                            " WHERE itemmasterid='"+arrayList.get(index).getItemMasterId()+"'";
                    Cursor c_qimg = sql.rawQuery(qimg,null);
                    if(c_qimg.getCount()>0){
                        c_qimg.moveToFirst();
                        ItemImgPath = c_qimg.getString(c_qimg.getColumnIndex("ItemImgPath"));
                    }

                    tcf.addCartItems_new(arrayList.get(index).getMerchant_id(),
                            arrayList.get(index).getMerchant_name(),
                            valueOf(arrayList.get(index).getEdtQty()),
                            valueOf(arrayList.get(index).getMinOrdQty()),
                            arrayList.get(index).getOffers(),
                            valueOf(arrayList.get(index).getPrice()),
                            arrayList.get(index).getItemName(),
                            valueOf(arrayList.get(index).getTotalAmount()),
                            arrayList.get(index).getItemMasterId(),
                            arrayList.get(index).getFreeitemid(),
                            valueOf(arrayList.get(index).getFreeitemqty()),
                            arrayList.get(index).getFreeitemname(),
                            arrayList.get(index).getValidfrom(),
                            arrayList.get(index).getValidto(),
                            ItemImgPath,arrayList.get(index).getCategoryId(),
                            arrayList.get(index).getCategoryName(),
                            arrayList.get(index).getSubCategoryId(),
                            arrayList.get(index).getSubCategoryName(),
                            String.valueOf(arrayList.get(index).getMrp()),
                            arrayList.get(index).getMaxOrdQty(),
                            arrayList.get(index).getRange(),
                            arrayList.get(index).getOutOfStock(),
                            arrayList.get(index).getDistance(),
                            arrayList.get(index).getPerDigit(),
                            arrayList.get(index).getUOMcode(),
                            arrayList.get(index).getBrand(),
                            arrayList.get(index).getContent(),
                            arrayList.get(index).getContentUOM(),
                            arrayList.get(index).getSellingUOM(),
                            arrayList.get(index).getPackOfQty(),
                            arrayList.get(index).getFreeAboveAmt(),arrayList.get(index).getFreeDelyMaxDist(),
                            arrayList.get(index).getMinDelyKg(),arrayList.get(index).getMinDelyKm(),
                            arrayList.get(index).getExprDelyWithinMin(), arrayList.get(index).getExpressDelyChg(),
                            arrayList.get(index).getOpen_slots(), arrayList.get(index).getOpenTime1(),
                            arrayList.get(index).getOpenTime2(),arrayList.get(index).getCloseTime1(),
                            arrayList.get(index).getCloseTime2(),arrayList.get(index).getIsMerchDelivery(),
                            arrayList.get(index).getUPIMerch());
                }

            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void checkItemPresent(String itemid, String merchId){
        String q = "Select * from "+ DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'" +
                " AND MerchantId='"+merchId+"'";
        Cursor c = sql.rawQuery(q,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                //same item with same qty present, delete that item
                String qry = "Delete from "+DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'"+
                        " AND MerchantId='"+merchId+"'";
                Cursor c1 = sql.rawQuery(qry,null);
                if(c1.getCount()>0) {
                    c1.moveToFirst();
                }

                String q2 = "Select * from "+ DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE Product_id ='"+itemid+"'"+
                        " AND MerchantId='"+merchId+"'";
                Cursor c2 = sql.rawQuery(q2,null);
                if(c2.getCount()>0) {
                    c2.moveToFirst();
                }

                //   return true;
            }while (c.moveToNext());

        }else {
            // return false;
        }
    }

}
