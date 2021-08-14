package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.SubcategoryAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Connectiondetector;
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

/**
 * Created by Chetana
 */
public class SubCategoryActivity extends AppCompatActivity {
    private Context parent;
    String res = "";
    public static ArrayList<AllCatSubcatItems> arrayList;
    boolean isInternetPresent;
    private AllCatSubcatItems bean;
    private String json;
    private ListView listview;
    Connectiondetector cd;
    MenuItem m, refresh;
    public String restoredText;
    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;
    public static String Mobilenumber;
    String Catid, subcat_id, subcat_name;
    String  Catid_1, custvendorID,PurDigit,CustomerID;
    ProgressHUD progress;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_subcategory);

       toolbar = (Toolbar)findViewById(R.id.toolbar);
        //actionBarImage = (ImageView)findViewById(R.id.actionBarImage);
        toolbar.setTitle("Sub-Category");
        // toolbar.setTitleTextColor(0xffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Utilities.darkenStatusBar(this, R.color.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        cd = new Connectiondetector(SubCategoryActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
      //  databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        restoredText = sharedpreferences.getString("Mobileno", null);
        if (restoredText != null) {
          //  Mobilenumber = restoredText;
         //   AnyMartData.MOBILE = restoredText;
        }

        Intent intent = getIntent();
        Catid = intent.getStringExtra("CategoryId");
        Catid_1 = intent.getStringExtra("Category_Id");
        custvendorID = intent.getStringExtra("CustVendorMasterId");
        CustomerID = intent.getStringExtra("CustomerID");
        PurDigit = intent.getStringExtra("PurDigit");
        Toast.makeText(getApplicationContext(),"There is Single Category =  "+Catid+"is presnet in database",Toast.LENGTH_LONG);

        if (tcf.getAllCatSubcatItemCount(getParent()) > 0) {
            getDataFromDataBase();
        } else {
            getDataFromServer();
        }

        setListeners();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //Toast.makeText(getApplicationContext(), "Search Reult..." + query, Toast.LENGTH_LONG).show();
            mySearch(query);
        }
    }

    protected void mySearch(String query) {

        for (AllCatSubcatItems s : arrayList) {
            if (s.getSubCategoryName().contains(query)) {
                arrayList.add(s);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                //onBackPressed();
                finish();
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

              /*  if (isInternetPresent) {
                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {

                        new GetCategoryList().execute();
                    } else {
                        new StartSession_tbuds(SubCategoryActivity.this, new com.vritti.orderbilling.interfaces.CallbackInterface() {

                            @Override
                            public void callMethod() {
                                new GetCategoryList().execute();
                            }
                        });
                    }
                }*/
                return true;
            default:
               break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        m = menu.findItem(R.id.miActionProgress);
        refresh = menu.findItem(R.id.refresh);
        return super.onPrepareOptionsMenu(menu);
    }

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

    @SuppressLint("WrongViewCast")
    private void initialize() {
        parent = SubCategoryActivity.this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        listview = (ListView) findViewById(R.id.listview_home_subcategory_list);

        arrayList = new ArrayList<AllCatSubcatItems>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(SubCategoryActivity.this);
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
        mprogress = findViewById(R.id.tbuds_tbar_prgrs_bar);
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
            //   Toast.makeText(parent, "No internet..", Toast.LENGTH_LONG).show();
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
                                .getString("itemnaame"),"http://test1.ekatm.com/menshirts.jpg",
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
        Cursor cursor = sql_db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + " where CategoryName='"
                + Catid + "'", null);
        Log.d("test", "" + cursor.getCount());
        if(cursor.getCount() == 1){
            cursor.moveToFirst();

            subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
            subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));
            String CategoryName = Catid;

            bean = new AllCatSubcatItems();
            //   bean.setCategoryId(cat_id);
            //   bean.setCategoryName(cat_name);
            bean.setSubCategoryName(subcat_name);
            bean.setSubCategoryId(subcat_id);
            bean.setSubcatcount(subcatcount);

            Cursor cursor1 = sql_db.rawQuery("Select distinct itemMasterId,ItemName,ItemImgPath from "
                    + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + "" +
                    " where SubCategoryName='" + subcat_name + "' and CategoryName='"+Catid+"'", null);
            Log.d("test", "" + cursor1.getCount());
            if (cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                try {
                    do {
                        itemcount = cursor1.getCount();

                    } while (cursor1.moveToNext());
                } finally {
                    cursor1.close();
                }
            }

            bean.setItemcount(itemcount);
            arrayList.add(bean);

            listview.setAdapter(new SubcategoryAdapter(parent, arrayList));

            Log.e("ItemListActivity","call list");

            Intent intent = new Intent(parent, ItemListActivity.class);
            intent.putExtra("SubCategoryId", subcat_id);
            intent.putExtra("SubCategoryName",subcat_name );
            intent.putExtra("CategoryId",Catid);
            intent.putExtra("CustVendorMasterId",custvendorID);
            intent.putExtra("CustomerID",CustomerID);
            startActivity(intent);
            finish();

        }else if (cursor.getCount() > 1) {
            cursor.moveToFirst();
            try {
                do {

                    subcatcount = cursor.getCount();
                    // String cat_name = cursor.getString(cursor.getColumnIndex("CategoryName"));
                    /// String cat_id = cursor.getString(cursor.getColumnIndex("CategoryId"));
                    subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                    subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));

                    bean = new AllCatSubcatItems();
                    //   bean.setCategoryId(cat_id);
                    //   bean.setCategoryName(cat_name);
                    bean.setSubCategoryName(subcat_name);
                    bean.setSubCategoryId(subcat_id);
                    bean.setSubcatcount(subcatcount);

                    Cursor cursor1 = sql_db.rawQuery("Select distinct itemMasterId,ItemName,ItemImgPath from "
                            + databaseHelper.TABLE_ALL_CAT_SUBCAT_ITEMS + "" +
                            " where SubCategoryName='" + subcat_name + "' and CategoryName='"+Catid+"'", null);
                    Log.d("test", "" + cursor1.getCount());
                    if (cursor1.getCount() > 0) {
                        cursor1.moveToFirst();
                        try {
                            do {

                                itemcount = cursor1.getCount();

                            } while (cursor1.moveToNext());
                        } finally {
                            cursor1.close();
                        }
                    }

                    bean.setItemcount(itemcount);
                    arrayList.add(bean);
                }while (cursor.moveToNext());
                cursor.close();

                listview.setAdapter(new SubcategoryAdapter(parent, arrayList));

            } finally {

            }
        }

       // listview.setAdapter(new SubcategoryAdapter(parent, arrayList));

    }

    private void setListeners() {

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                AnyMartData.selectedCategoryName =
                        arrayList.get(position).getCategoryName();
                // startActivity(new Intent(parent, ItemListActivity.class));
                Log.e("ItemListActivity","call 1");

                Intent intent = new Intent(parent, ItemListActivity.class);
                intent.putExtra("SubCategoryId", arrayList.get(position).getSubCategoryId());
                intent.putExtra("SubCategoryName", arrayList.get(position).getCategoryName());
                intent.putExtra("PurDigit",PurDigit);
             //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
       //         overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
               finish();
            }
        });
    }

    public class GetCategoryList extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String response_subcategory = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading Sub-Categories...");
            progressDialog.show();*/
           // progress = ProgressHUD.show(parent,"Loading SubCategories...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_subcategory = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_PRODUCT_CATEGORY +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&custvenmasterid=" + CustomerID;

           // "http://h207.ekatm.com/api/OrderBillingAPI/getAllItemsRuni?handler=&sessionid=&custvenmasterid="

            String getcatSubItems = url_subcategory;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_subcategory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                response_subcategory = responseString.replaceAll("\\\\","");

                System.out.println("resp ="+response_subcategory);

            } catch (Exception e) {
                response_subcategory = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
         //   progressDialog.dismiss();

            if (response_subcategory.equalsIgnoreCase("Session Expired")) {
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
            } else if (response_subcategory.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                json = response_subcategory;
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
    public void onBackPressed() {
        super.onBackPressed();
       finish();
        // }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.refresh, menu);
        //View m = menu.findItem(R.id.search).getActionView();
        menu.removeItem(R.id.search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        //searchView.setSearchableInfo(info);
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("TAG", "onQueryTextSubmit ");
                arrayList.clear();

                DatabaseHelper db1 = new DatabaseHelper(SubCategoryActivity.this);
                SQLiteDatabase db = db1.getReadableDatabase();
                int subcatcount = 0;
                int itemcount = 0;
                Cursor cursor = db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                        + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where CategoryName='"
                        + Catid + "'", null);
                Log.d("test", "" + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    try {
                        do {

                            subcatcount = cursor.getCount();
                            // String cat_name = cursor.getString(cursor.getColumnIndex("CategoryName"));
                            /// String cat_id = cursor.getString(cursor.getColumnIndex("CategoryId"));
                            String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                            String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));


                            bean = new AllCatSubcatItems();
                            //   bean.setCategoryId(cat_id);
                            //   bean.setCategoryName(cat_name);
                            bean.setSubCategoryName(subcat_name);
                            bean.setSubCategoryId(subcat_id);
                            bean.setSubcatcount(subcatcount);

                            String selectQuery =  "Select distinct itemMasterId,ItemName,ItemImgPath from "
                                    + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS +
                                    " WHERE SubCategoryName='" + subcat_name + "' and CategoryName='"+Catid
                                    +"' and VehicleNumber LIKE  '%" +s+ "%' ";

                            Cursor cursor1 = db.rawQuery(selectQuery , null);
                            Log.d("test", "" + cursor1.getCount());
                            if (cursor1==null){
                                Toast.makeText(SubCategoryActivity.this,"No records found!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SubCategoryActivity.this, cursor1.getCount() + " records found!",Toast.LENGTH_LONG).show();
                            }
                            if (cursor1.getCount() > 0) {
                                cursor1.moveToFirst();
                                try {
                                    do {

                                        itemcount = cursor1.getCount();

                                    } while (cursor1.moveToNext());
                                } finally {
                                    cursor1.close();
                                }
                            }
                            bean.setItemcount(itemcount);
                            arrayList.add(bean);
                        } while (cursor.moveToNext());
                    } finally {
                        cursor.close();
                    }
                }
                 *//*vehicleAdapter = new VehicleAdapter(GetVehicleNoActivity.this, arrayList);
                listview_vehicles.setAdapter(vehicleAdapter);*//*
                listview.setAdapter(new SubcategoryAdapter(parent, arrayList));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("TAG", "onQueryTextChange ");
                arrayList.clear();


                DatabaseHelper db1 = new DatabaseHelper(SubCategoryActivity.this);
                SQLiteDatabase db = db1.getReadableDatabase();
                int subcatcount = 0;
                int itemcount = 0;
                Cursor cursor = db.rawQuery("Select distinct SubCategoryName,SubCategoryId from "
                        + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where CategoryName='"
                        + Catid + "'", null);
                Log.d("test", "" + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    try {
                        do {

                            subcatcount = cursor.getCount();
                            // String cat_name = cursor.getString(cursor.getColumnIndex("CategoryName"));
                            /// String cat_id = cursor.getString(cursor.getColumnIndex("CategoryId"));
                            String subcat_name = cursor.getString(cursor.getColumnIndex("SubCategoryName"));
                            String subcat_id = cursor.getString(cursor.getColumnIndex("SubCategoryId"));


                            bean = new AllCatSubcatItems();
                            //   bean.setCategoryId(cat_id);
                            //   bean.setCategoryName(cat_name);
                            bean.setSubCategoryName(subcat_name);
                            bean.setSubCategoryId(subcat_id);
                            bean.setSubcatcount(subcatcount);

                            String selectQuery =  "Select distinct itemMasterId,ItemName,ItemImgPath from "
                                    + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS +
                                    " WHERE SubCategoryName='" + subcat_name + "' and CategoryName='"+Catid
                                    +"' and VehicleNumber LIKE  '%" +s+ "%' ";

                            Cursor cursor1 = db.rawQuery(selectQuery , null);
                            Log.d("test", "" + cursor1.getCount());
                            if (cursor1==null){
                                Toast.makeText(SubCategoryActivity.this,"No records found!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SubCategoryActivity.this, cursor1.getCount() + " records found!",Toast.LENGTH_LONG).show();
                            }
                            if (cursor1.getCount() > 0) {
                                cursor1.moveToFirst();
                                try {
                                    do {

                                        itemcount = cursor1.getCount();

                                    } while (cursor1.moveToNext());
                                } finally {
                                    cursor1.close();
                                }
                            }
                            bean.setItemcount(itemcount);
                            arrayList.add(bean);
                        } while (cursor.moveToNext());
                    } finally {
                        cursor.close();
                    }
                }
                 *//*vehicleAdapter = new VehicleAdapter(GetVehicleNoActivity.this, arrayList);
                listview_vehicles.setAdapter(vehicleAdapter);*//*
                listview.setAdapter(new SubcategoryAdapter(parent, arrayList));
                return false;
            }

        });*/
        return super.onCreateOptionsMenu(menu);
    }

}
