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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CategoryAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.MyCartBean;
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
import java.util.ArrayList;

public class CategorySelectionActivity extends AppCompatActivity {
    Context parent;
    GridView listview;
    TextView txtlaunchsoon;
    LinearLayout txtnoordnote;

    public static ArrayList<AllCatSubcatItems> arrayList;
    public static ArrayList<AllCatSubcatItems> arrayList_subcat;
    public static ArrayList<OrderHistoryBean> historyBeanList;
    static ArrayList<OrderHistoryBean> newList = null;
    public static ArrayList<OrderHistoryBean> historyBeanList_new;
    public static ArrayList<MyCartBean> arrayList_bean;
    private int backpressCount = 0;
    private AllCatSubcatItems bean;
    private static String json;
    private long back_pressed = 0;
    private static DatabaseHandlers databaseHelper;

    Utility ut;
    static DatabaseHandlers db;
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
    boolean alreadysentcall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        getDataFromServer();

        int count = 0;
        count = tcf.getCartItems1();

        setListeners();
    }

    private void initialize() {
        parent = CategorySelectionActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.home)+"</small>"));

        txtlaunchsoon = findViewById(R.id.txtlaunchsoon);
        txtnoordnote = findViewById(R.id.txtnoordnote);
        listview = findViewById(R.id.listview_home_category_list);

        try{
            sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

            ut = new Utility();
            tcf = new Tbuds_commonFunctions(CategorySelectionActivity.this);
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
        }catch (Exception e){
            e.printStackTrace();
        }

        arrayList = new ArrayList<AllCatSubcatItems>();
        arrayList_subcat = new ArrayList<AllCatSubcatItems>();
        arrayList_bean = new ArrayList<MyCartBean>();
        historyBeanList = new ArrayList<OrderHistoryBean>();

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

    }

    private void setListeners() {

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
                transitionDrawable.startTransition(100); //600 Milliseconds

                AnyMartData.CatImgPath = arrayList.get(position).getCatImgPath();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();

                if(arrayList.get(position).getSubcatcount() == 1){
                    //call API and open itemlist directly
                    selectedCatid = arrayList.get(position).getCategoryId();
                    selectedCatName = arrayList.get(position).getCategoryName();
                    SubCatCount = arrayList.get(position).getSubcatcount();

                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new GetMerchFamilyMaster_subcategory().execute();
                    }
                }else if(arrayList.get(position).getSubcatcount() > 1){

                    AnyMartData.selectedCategoryName = arrayList.get(position).getCategoryName();
                    Intent intent = new Intent(parent, SubCategorySelectionActivity.class);
                    intent.putExtra("CategoryName", arrayList.get(position).getCategoryName());
                    intent.putExtra("Category_Id", arrayList.get(position).getCategoryId());
                    intent.putExtra("CustomerID",AnyMartData.USER_ID);
                    intent.putExtra("SubCatCount",String.valueOf(SubCatCount));
                    intent.putExtra("CatImgPath",CatImgPath);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }

    private void getDataFromServer() {

        if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession_tbuds(parent, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetMerchFamilyMaster().execute();    //merchant familymaster
                    }
                @Override
                public void callfailMethod(String s) {
                }
            });

        } else {
            Toast.makeText(parent, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
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
                        "&MerchantId=&type=Category&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId=";
            }else {
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=" + AnyMartData.selected_MERCHID+
                        "&type=Category&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId=";
            }

            String getcatSubItems = url_getCategory_List;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {

                res = Utility.OpenconnectionOrferbilling(url_getCategory_List, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
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
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.equalsIgnoreCase("[]")) {
                    txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.contains("sesEndTime")) {
                    txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+AnyMartData.selected_BSEGMENTDESC +" "+
                            getResources().getString(R.string.soon));
                } else {
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

        // Utilities.clearTable(parent, AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS);
        tcf.clearTable(parent, DatabaseHandlers.TABLE_FAMILY_MASTERDATA);
        arrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String catImgPath =  jsonArray.getJSONObject(i).getString("CatImgPath");
                AnyMartData.CatImgPath = catImgPath;
                /*SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();*/

                if(catImgPath.equalsIgnoreCase("") || catImgPath.equalsIgnoreCase(null)){
                    catImgPath = "";
                }else {
                    catImgPath = CompanyURL+"/images/"+catImgPath;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.commit();

                tcf.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        "","",
                        "",catImgPath,"",
                        jsonArray.getJSONObject(i).getString("SubCategoryCount"),"");

            }

            getDataFromDataBase();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        arrayList.clear();

       // sql = databaseHelper.getWritableDatabase();

        String que = "Select distinct CategoryId, CategoryName,CatImgPath,SubCatCount from "
                + DatabaseHandlers.TABLE_FAMILY_MASTERDATA +" ORDER BY CategoryName ASC";
        Cursor c = sql.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() == 1) {
            c.moveToFirst();

            /////////////////////////////////////////////////////////////////////////////////////
                cat_name = c.getString(c.getColumnIndex("CategoryName"));
                cat_id = c.getString(c.getColumnIndex("CategoryId"));
                CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
                SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

                bean = new AllCatSubcatItems();
                bean.setCategoryId(cat_id);
                bean.setCategoryName(cat_name);
                bean.setCatImgPath(CatImgPath);
                bean.setSubcatcount(SubCatCount);
                bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

                arrayList.add(bean);

                //call API and open itemlist directly
                selectedCatid = cat_id;
                selectedCatName = cat_name;
                SubCatCount = SubCatCount;

                if(alreadysentcall == false){
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        new GetMerchFamilyMaster_subcategory().execute();
                    }
                }else {

                }

        } else if (c.getCount() > 1) {
            c.moveToFirst();

            try {
                do {
                    int subcatcount = 0;
                    int itemcount = 0;
                    String cat_name = c.getString(c.getColumnIndex("CategoryName"));
                    String cat_id = c.getString(c.getColumnIndex("CategoryId"));
                    CatImgPath = c.getString(c.getColumnIndex("CatImgPath"));
                    SubCatCount = c.getInt(c.getColumnIndex("SubCatCount"));

                    bean = new AllCatSubcatItems();
                    bean.setCategoryId(cat_id);
                    bean.setCategoryName(cat_name);
                    bean.setCatImgPath(CatImgPath);
                    bean.setSubcatcount(SubCatCount);
                    bean.setBusiSegImgPath(AnyMartData.SpecImgPath);

                    arrayList.add(bean);

                } while (c.moveToNext());
            } finally {

            }
        }

        cadt = new CategoryAdapter(parent,arrayList);
        listview.setAdapter(cadt);
        setGridViewHeightBasedOnChildren(listview,3);
    }

    public class GetMerchFamilyMaster_subcategory extends AsyncTask<String, Void, String> {
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
                        "&MerchantId=&type=SubCategory&BusSegmentId="+AnyMartData.selected_BSEGMENTID+"&CategoryId="+selectedCatid;
            }else {
                url_getCategory_List = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_FAMILY_MASTER_MERCHANT +
                        "?handler=" + AnyMartData.HANDLE +
                        "&sessionid=" + AnyMartData.SESSION_ID+
                        "&MerchantId=" + AnyMartData.selected_MERCHID+
                        "&type=SubCategory&BusSegmentId=" + AnyMartData.selected_BSEGMENTID+"&CategoryId="+selectedCatid;
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
                    // txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+selectedCatName+" "+
                            getResources().getString(R.string.soon));
                } else if (response_list.contains("sesEndTime")) {
                    //   txtnoordnote.setVisibility(View.VISIBLE);
                    txtlaunchsoon.setText(getResources().getString(R.string.thanks_for_interes)+" "+selectedCatName+" "+
                            getResources().getString(R.string.soon));
                }else {
                    //   txtnoordnote.setVisibility(View.GONE);
                    json = response_list;
                    parseJson_MerchFamilyMaster_subcategory(json);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void parseJson_MerchFamilyMaster_subcategory(String json) {
        tcf.clearTable(parent, DatabaseHandlers.TABLE_FAMILY_MASTERDATA);

        arrayList_subcat.clear();
        String SubCategoryId="",SubCategoryName="",CategoryName="";
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                String SubCatImgPath = "";
                SubCatImgPath =  jsonArray.getJSONObject(i).getString("SubCatImgPath");
                AnyMartData.SubCatImgPath = SubCatImgPath;


                if(SubCatImgPath.equalsIgnoreCase("") || SubCatImgPath.equalsIgnoreCase(null)){
                    SubCatImgPath = "";
                }else {
                    SubCatImgPath = CompanyURL+"/images/"+SubCatImgPath;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatImgPath", AnyMartData.CatImgPath);
                editor.putString("SubCatImgPath", SubCatImgPath);
                editor.commit();

                tcf.addFamilyMaster(jsonArray.getJSONObject(i).getString("CategoryId"),
                        jsonArray.getJSONObject(i).getString("CategoryName"),
                        jsonArray.getJSONObject(i).getString("SubCategoryId"),
                        jsonArray.getJSONObject(i).getString("SubCategoryName"),
                        "",AnyMartData.CatImgPath, SubCatImgPath,
                        String.valueOf(SubCatCount), jsonArray.getJSONObject(i).getString("ItemCount"));

                SubCategoryId=jsonArray.getJSONObject(i).getString("SubCategoryId");
                SubCategoryName=jsonArray.getJSONObject(i).getString("SubCategoryName");
                CategoryName=jsonArray.getJSONObject(i).getString("CategoryName");

            }

            alreadysentcall = true;

            //to avoid screen transition
            Intent intent = new Intent(CategorySelectionActivity.this, ItemlistActivityNewBooking.class);
            intent.putExtra("SubCategoryId", SubCategoryId);
            intent.putExtra("SubCategoryName", SubCategoryName);
            intent.putExtra("CategoryName", CategoryName);
            intent.putExtra("CustomerID",AnyMartData.USER_ID);
            intent.putExtra("CallType","SubCategorySearch");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
           //finish();

        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        /*Intent intent = new Intent(CategorySelectionActivity.this, ItemlistActivityNewBooking.class);
        intent.putExtra("SubCategoryId", SubCategoryId);
        intent.putExtra("SubCategoryName", SubCategoryName);
        intent.putExtra("CategoryName", CategoryName);
        intent.putExtra("CustomerID",AnyMartData.USER_ID);
        intent.putExtra("CallType","SubCategorySearch");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/

    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        try{
            ListAdapter listAdapter = gridView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int items = listAdapter.getCount();
            int rows = 0;

            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();

            float x = 1;
            if( items > columns ){
                x = items/columns;
                rows = (int) (x+1);
                totalHeight *= rows;
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
