package com.vritti.sales.OrderBookingNew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CategoryAdapter;
import com.vritti.sales.OrderBookingNew.Adapter.SubcategoryAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.util.ArrayList;

public class SubCategorySelectionActivity extends AppCompatActivity {
    Context parent;
    GridView listview;
    LinearLayout txtnoordnote;
    RelativeLayout layyellow;

    public static ArrayList<AllCatSubcatItems> arrayList;

    Utility ut;
    static DatabaseHandlers databaseHelper;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String  Catid_1, custvendorID,PurDigit,CustomerID,CatImgPath = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    String dabasename="";
    String cat_name="", cat_id="",Catid_name="";
    String SubCatCount="";
    String selectedCatid="",selectedCatName="";
    CategoryAdapter cadt;
    String res="";
    private String json;

    SubcategoryAdapter subadt;
    boolean isSingleSubCat=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_selection);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        Intent intent = getIntent();
        Catid_name = intent.getStringExtra("CategoryName");
        Catid_1 = intent.getStringExtra("Category_Id");
        //custvendorID = intent.getStringExtra("CustVendorMasterId");
        CustomerID = intent.getStringExtra("CustomerID");
        SubCatCount = intent.getStringExtra("SubCatCount");
        CatImgPath = intent.getStringExtra("CatImgPath");
        custvendorID = CustomerID;

        getDataFromServer();

        setListeners();

    }

    public void init(){
        parent = SubCategorySelectionActivity.this;

        listview = findViewById(R.id.listview_home_subcategory_list);
        txtnoordnote =  findViewById(R.id.txtnoordnote);
        layyellow =  findViewById(R.id.layyellow);

        try{
            sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

            ut = new Utility();
            tcf = new Tbuds_commonFunctions(SubCategorySelectionActivity.this);
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

        arrayList = new ArrayList<AllCatSubcatItems>();
    }

    public void setListeners(){
        layyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcf.getCartItems1() > 0){
                    Intent intent = new Intent(SubCategorySelectionActivity.this,WishlistActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(SubCategorySelectionActivity.this,getResources().getString(R.string.no_items_in_cart),Toast.LENGTH_SHORT).show();
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = arrayList.get(position).getCategoryName();

                // Initialize a new color drawable array
                int mGridViewBGColor = Color.parseColor("#87b2d3");
                ColorDrawable[] colors = {
                        new ColorDrawable(mGridViewBGColor), // Animation starting color
                        new ColorDrawable(mGridViewBGColor) // Animation ending color
                };

                // Initialize a new transition drawable instance
                TransitionDrawable transitionDrawable = new TransitionDrawable(colors);

                // Set the clicked item background
                view.setBackground(transitionDrawable);

                // Finally, Run the item background color animation
                // This is the grid view item click effect
                transitionDrawable.startTransition(100); // 600 Milliseconds

                //setSelectedpos = position +1;
                //AnyMartData.selectedCategoryName = arrayList.get(position).getCategoryName();
                AnyMartData.selectedCategoryName = arrayList.get(position).getSubCategoryName();

                AnyMartData.CatImgPath = arrayList.get(position).getCatImgPath();
                AnyMartData.SubCatImgPath = arrayList.get(position).getSubCatImgPath();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.putString("SubCatImgPath", AnyMartData.SubCatImgPath);
                editor.commit();

                //if(AnyMartData.AppCode.equalsIgnoreCase("MM")){
                Intent intent = new Intent(parent, ItemlistActivityNewBooking.class);
                intent.putExtra("SubCategoryId", arrayList.get(position).getSubCategoryId());
                intent.putExtra("SubCategoryName", arrayList.get(position).getSubCategoryName());
                intent.putExtra("CategoryName", arrayList.get(position).getCategoryName());
                //intent.putExtra("CustVendorMasterId",custvendorID);
                intent.putExtra("CustomerID",AnyMartData.USER_ID);
                //intent.putExtra("PurDigit",PurDigit);
                intent.putExtra("CallType","SubCategorySearch");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession_tbuds(parent, new CallbackInterface() {

                @Override
                public void callMethod() {

                    new GetMerchFamilyMaster().execute();
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        } else {
            //   Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
            //  callSnackbar();
        }
    }

    public class GetMerchFamilyMaster extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_list = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            //old familymasterdata all families
            String url_getCategory_List = "";
            if(AnyMartData.SHOPBYMODE.equalsIgnoreCase("ShopBySpeciality")){
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=&type=SubCategory&BusSegmentId="+AnyMartData.selected_BSEGMENTID+"&CategoryId="+Catid_1;
            }else {
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=" + AnyMartData.selected_MERCHID+
                        "&type=SubCategory&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId="+Catid_1;
            }

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

                /*responseString = stringBuff_getItems.toString().replaceAll("^\"|\"$", "");*/
                response_list = responseString.replaceAll("\\\\", "");
                System.out.println("resp =" + response_list);

            } catch (Exception e) {
                response_list = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            try{
                if (response_list.equalsIgnoreCase("Session Expired")) {

                } else if (response_list.equalsIgnoreCase("error")) {
                    Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                    txtnoordnote.setVisibility(View.VISIBLE);
          /*          txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+Catid_name+" "+
                            getResources().getString(R.string.soon));*/
                } else if (response_list.contains("sesEndTime")) {
                    txtnoordnote.setVisibility(View.VISIBLE);
                  /*  txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+Catid_name+" "+
                            getResources().getString(R.string.soon));*/
                }else {
                    txtnoordnote.setVisibility(View.GONE);
                    json = response_list;
                    parseJson_MerchFamilyMaster(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_MerchFamilyMaster(String json) {

        tcf.clearTable(parent, DatabaseHandlers.TABLE_FAMILY_MASTERDATA);

        arrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String SubCatImgPath = "";
                SubCatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");
                AnyMartData.SubCatImgPath = SubCatImgPath;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.putString("SubCatImgPath", AnyMartData.SubCatImgPath);
                editor.commit();

                if(SubCatImgPath.equalsIgnoreCase("") || SubCatImgPath.equalsIgnoreCase(null)){
                    SubCatImgPath = "";
                }else {
                    SubCatImgPath = CompanyURL+"/images/"+SubCatImgPath;
                }

                tcf.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        "",AnyMartData.CatImgPath, SubCatImgPath,
                        SubCatCount, jsonArray.getJSONObject(i).getString("ItemCount"));
            }

            getDataFromDataBase();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

        int subcatcount = 0;
        int itemcount = 0;
        AllCatSubcatItems bean;

        Cursor cursor = sql.rawQuery("Select distinct SubCategoryName,SubCategoryId,SubCatImgPath,ItemCount from "
                + DatabaseHandlers.TABLE_FAMILY_MASTERDATA + " where CategoryName='"
                + Catid_name + "' ORDER BY SubCategoryName ASC", null);
        Log.d("test", "" + cursor.getCount());
        if(cursor.getCount() == 1){
            cursor.moveToFirst();

            String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
            String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));
            String SubCatImgPath = cursor.getString(cursor.getColumnIndex("SubCatImgPath"));
            itemcount = cursor.getInt(cursor.getColumnIndex("ItemCount"));
            String CategoryName = Catid_name;

            //setdata to listview
            bean = new AllCatSubcatItems();
            bean.setSubCategoryName(subcat_name);
            bean.setSubCategoryId(subcat_id);
            bean.setItemcount(itemcount);
            bean.setSubCatImgPath(SubCatImgPath);
            bean.setCatImgPath(AnyMartData.CatImgPath);
            bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

            //bean.setItemcount(itemcount);
            arrayList.add(bean);
            subadt = new SubcategoryAdapter(parent, arrayList);
            listview.setAdapter(subadt);

            AnyMartData.SubCatImgPath = SubCatImgPath;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("CatImgPath", AnyMartData.CatImgPath);
            editor.putString("SubCatImgPath", AnyMartData.SubCatImgPath);
            editor.commit();

            Intent intent = new Intent(parent, ItemlistActivityNewBooking.class);
            intent.putExtra("SubCategoryId", subcat_id);
            intent.putExtra("SubCategoryName",subcat_name );
            intent.putExtra("CategoryName",Catid_name);
            intent.putExtra("CustomerID",AnyMartData.USER_ID);
            intent.putExtra("CallType","SubCategorySearch");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }else if (cursor.getCount() > 1) {
            cursor.moveToFirst();
            try {
                do {
                    subcatcount = cursor.getCount();
                    // String cat_name = cursor.getString(cursor.getColumnIndex("CategoryName"));
                    /// String cat_id = cursor.getString(cursor.getColumnIndex("CategoryId"));
                    String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                    String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));
                    String SubCatImgPath = cursor.getString(cursor.getColumnIndex("SubCatImgPath"));
                    itemcount = cursor.getInt(cursor.getColumnIndex("ItemCount"));

                    bean = new AllCatSubcatItems();
                    bean.setSubCategoryName(subcat_name);
                    bean.setSubCategoryId(subcat_id);
                    bean.setItemcount(itemcount);
                    bean.setSubCatImgPath(SubCatImgPath);
                    bean.setCatImgPath(AnyMartData.CatImgPath);
                    bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

                    arrayList.add(bean);
                }
                while (cursor.moveToNext());
                //      cursor.close();

            } finally {

            }
        }

        subadt = new SubcategoryAdapter(parent, arrayList);
        listview.setAdapter(subadt);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            subadt = new SubcategoryAdapter(parent, arrayList);
            listview.setAdapter(subadt);
            subadt.notifyDataSetChanged();

          /*  cartItmCnt = databaseHelper.getCartItems();
            textview_cart.setText(String.valueOf(cartItmCnt));*/

        }catch (Exception e){
            e.printStackTrace();
        }

        //listview.setSelection(setSelectedpos);

        if(isSingleSubCat == true){
            finish();
        }
    }

}
