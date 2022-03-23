package com.vritti.sales.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.ItemCodeDescAdapter;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ItemCodeDescActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;

    ImageView imgrefresh,imgsearch;
    ListView listitem;
    EditText edtsearch;

    Utility ut;
    static DatabaseHandlers db;
    CommonFunction cf;
    static Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    ArrayList<CounterbillingBean> itemsDataList;
    ArrayList<String> itemdesc_List;
    ArrayList<String> itemcode_List;
    String usertype = "";
    String username = "";

    public static final int ITEMCODEDESC = 12;
    ItemCodeDescAdapter itemAdapter;

    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;

    String SODate = "", key = "";
    String ItemPlantId ="",ItemCode = "", ItemDesc = "", ItemMRP = "", TAXClass = "", TaxAmount = "", DiscAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getCounterBillItemcount() > 0){
            getDataFromdataBase();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadItemsDataJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        setListeners();
    }

    public void init(){
        parent = ItemCodeDescActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Item List");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        imgrefresh = (ImageView)findViewById(R.id.imgrefresh);
        imgsearch = (ImageView)findViewById(R.id.imgsearch);
        listitem = (ListView)findViewById(R.id.listtax);
        edtsearch = (EditText)findViewById(R.id.edtsearch);

        ut = new Utility();
        cf = new CommonFunction(ItemCodeDescActivity.this);
        tcf = new Tbuds_commonFunctions(ItemCodeDescActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        Intent intent = getIntent();
        key = intent.getStringExtra("callFor");

        itemsDataList = new ArrayList<CounterbillingBean>();
        itemdesc_List = new ArrayList<String>();
        itemcode_List = new ArrayList<String>();

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        date = String.format("%2d",day) + "/" + String.format("%02d", (month + 1)) + "/" + year;
        SODate = date;


    }

    public void setListeners(){

        /*edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                *//*if(s.length() >= 3 && !isCustSelected){
                    if (isnet()) {
                        new StartSession(parent, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new getSearchedCustomer().execute(s.toString());
                            }
                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }else if(s ==  null || s.equals("")) {
                    Toast.makeText(parent,"Minimum 3 characters required",Toast.LENGTH_SHORT).show();
                    isCustSelected = false;
                }*//*

            }
        });*/

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadItemsDataJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtsearch.getVisibility() == View.VISIBLE) {
                    edtsearch.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } else if ((edtsearch).getVisibility() == View.GONE) {
                    edtsearch.setVisibility(View.VISIBLE);
                    EditText textView = edtsearch;
                    textView.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(textView, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    itemAdapter.filter_code((edtsearch).getText().toString().trim().toLowerCase(Locale.getDefault()),key);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                Intent intent = new Intent(ItemCodeDescActivity.this, SalesChargeActivity.class);
                //intent.putExtra("CustVendorMasterId", AnyMartData.USER_ID);
                intent.putExtra("username", username);
                intent.putExtra("ItemCode", itemsDataList.get(position).getItemCode());
                intent.putExtra("ItemDesc", itemsDataList.get(position).getItemDesc());
                intent.putExtra("ItemMasterId", itemsDataList.get(position).getItemPlantId());
                setResult(ITEMCODEDESC,intent);
                finish();

            }
        });

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    class DownloadItemsDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(parent,"Downloading data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetItemsListForCounterBilling;

                Log.e("ItemCodeDescURL"," --> "+url);

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    ContentValues values = new ContentValues();
                    jResults = new JSONArray(response);
                }

                Log.e("ItemCodeDescURL"," -1-> "+url);

            } catch (Exception e) {
                Log.e("ItemCodeDescURL"," Exception --> "+e);
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.GONE);

            try{

                if(jResults != null){
                    parseJson(jResults);
                }else {
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void parseJson(JSONArray jResults){

        itemsDataList.clear();
        itemdesc_List.clear();
        itemcode_List.clear();

        tcf.clearTable(parent, db.TABLE_ADD_ITEMS_COUNTERBILL);

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                ItemPlantId = jsonObject.getString("ItemPlantId");
                ItemCode = jsonObject.getString("ItemCode");
                ItemDesc = jsonObject.getString("ItemDesc");

                CounterbillingBean cbean = new CounterbillingBean();
                cbean.setItemPlantId(ItemPlantId);
                cbean.setItemCode(ItemCode);
                cbean.setItemDesc(ItemDesc);

                itemsDataList.add(cbean);
                itemdesc_List.add(ItemDesc);
                itemcode_List.add(ItemCode);

                /*String query = "Select PricelistRate, itemmasterid from "+ db.TABLE_ALL_CAT_SUBCAT_ITEMS +
                        " WHERE itemmasterid='"+ItemPlantId*//*ItemCode*//*+"'";
                Cursor c = sql.rawQuery(query,null);

                if(c.getCount() != 0){
                    c.moveToFirst();
                    do{
                        ItemMRP = c.getString(c.getColumnIndex("PricelistRate"));
                    }while (c.moveToNext());
                }else {
                    ItemMRP = "00.00";
                }*/

                if(ItemCode.equalsIgnoreCase("") || ItemDesc.equalsIgnoreCase("")){

                }else {
                    tcf.insertItemsData_CounterBilling(ItemPlantId, ItemCode, ItemDesc, "0", "", "", "");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getDataFromdataBase();

    }

    public void getDataFromdataBase(){
        itemsDataList.clear();
        itemdesc_List.clear();
        itemcode_List.clear();

        String query = "Select ItemPlantId,ItemCode,ItemDesc,ItemMRP from "+ db.TABLE_ADD_ITEMS_COUNTERBILL;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                ItemPlantId = c.getString(c.getColumnIndex("ItemPlantId"));
                ItemCode = c.getString(c.getColumnIndex("ItemCode"));
                ItemDesc = c.getString(c.getColumnIndex("ItemDesc"));
                ItemMRP = c.getString(c.getColumnIndex("ItemMRP"));

                CounterbillingBean cbean = new CounterbillingBean();
                cbean.setItemPlantId(ItemPlantId);
                cbean.setItemCode(ItemCode);
                cbean.setItemDesc(ItemDesc);

                itemsDataList.add(cbean);
                itemdesc_List.add(ItemDesc);
                itemcode_List.add(ItemCode);

            }while (c.moveToNext());

            itemAdapter = new ItemCodeDescAdapter(parent,itemsDataList,key);
            listitem.setAdapter(itemAdapter);

            /*if(key.equalsIgnoreCase("ItemCode")){
                itemAdapter = new ItemCodeDescAdapter(parent,itemsDataList,key);
                listitem.setAdapter(itemAdapter);
            }else if(key.equalsIgnoreCase("ItemDesc")){
                itemAdapter = new ItemCodeDescAdapter(parent,itemsDataList,key);
                listitem.setAdapter(itemAdapter);
            }*/

          /*  if(key.equalsIgnoreCase("ItemCode")){
                ArrayAdapter<String> itmDescAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1,itemcode_List);
                listitem.setAdapter(itmDescAdapter);
            }else if(key.equalsIgnoreCase("ItemDesc")){
                ArrayAdapter<String> itmDescAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1,itemdesc_List);
                listitem.setAdapter(itmDescAdapter);
            }*/

        }else {

        }

    }

}
