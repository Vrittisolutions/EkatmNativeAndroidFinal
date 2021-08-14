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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.vritti.sales.adapters.TaxAdapter;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TaxClassActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;

    ProgressBar mprogress;
    ImageView imgrefresh,imgsearch;
    ListView listtax;
    EditText edtsearch;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    ArrayList<Customer> customerArrayList;
    String usertype = "";
    String username = "";

    public static final int TAXDATA = 9;
    TaxAdapter taxadpter;
    ArrayList<String> taxclass_List;
    ArrayList<TaxClassBean> taxClassArrayList;

    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;

    String TaxClassMasterId = "", TaxClassCode = "", TaxClassDesc = "", SODate = "",callFrom = "";

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

        try{
            if(cf.gettaxclscount() > 0){
                getTaxFromDatabase();
            }else {
                if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadTAXDataJSON().execute(SODate);
                        }
                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        setListeners();
    }

    public void init(){
        parent = TaxClassActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Tax Class");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        imgrefresh = (ImageView)findViewById(R.id.imgrefresh);
        imgsearch = (ImageView)findViewById(R.id.imgsearch);
        listtax = (ListView)findViewById(R.id.listtax);
        edtsearch = (EditText)findViewById(R.id.edtsearch);

        ut = new Utility();
        cf = new Tbuds_commonFunctions(TaxClassActivity.this);
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
        callFrom = intent.getStringExtra("callFrom");

        customerArrayList=new ArrayList<>();
        taxclass_List = new ArrayList<String>();
        taxClassArrayList = new ArrayList<TaxClassBean>();

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //date = String.format("%2d",day) + "/" + String.format("%02d", (month + 1)) + "/" + year;
        date = day + "/" + String.format("%02d", (month + 1)) + "/" + year;
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

                        new DownloadTAXDataJSON().execute(SODate);
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
                taxadpter.filter((edtsearch)
                        .getText().toString().trim()
                        .toLowerCase(Locale.getDefault()));
            }
        });

        listtax.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(TaxClassActivity.this, SalesChargeActivity.class);
                //intent.putExtra("CustVendorMasterId", AnyMartData.USER_ID);
                intent.putExtra("username", username);
                intent.putExtra("TaxClassDesc", taxClassArrayList.get(position).getTaxClassDesc());
                intent.putExtra("TaxClassID", taxClassArrayList.get(position).getTaxClassMasterId());
                intent.putExtra("TaxClassCode", taxClassArrayList.get(position).getTaxClassCode());
                setResult(TAXDATA,intent);
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

    //get taxclass list
    class DownloadTAXDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = "";
                if(callFrom.equalsIgnoreCase("SalesFamily") || callFrom.equalsIgnoreCase("PriceList")){
                    url = CompanyURL + WebUrlClass.api_getTaxClassList;
                }else {
                    url = CompanyURL + WebUrlClass.api_getTaxClassCode+"?SODt="+params[0];
                }

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length()-1);
                    ContentValues values = new ContentValues();
                    jResults = new JSONArray(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hideProgress();

            try{
                if(jResults != null){
                    sql.delete(db.TABLE_TAXCLASS,null,null);

                    parseTaxClassJson(jResults);
                }else {
                }
            }catch (Exception e){

            }
        }
    }

    public void parseTaxClassJson(JSONArray jResults){

        //    tcf.clearTable(parent, dbhandler.TABLE_ADD_ITEMS_COUNTERBILL);

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                TaxClassMasterId = jsonObject.getString("TaxClassMasterId");
                TaxClassCode = jsonObject.getString("TaxClassCode");
                TaxClassDesc = jsonObject.getString("TaxClassDesc");

                /*TaxClassBean tcbean = new TaxClassBean();
                tcbean.setTaxClassMasterId(TaxClassMasterId);
                tcbean.setTaxClassCode(TaxClassCode);
                tcbean.setTaxClassDesc(TaxClassDesc);
                taxClassArrayList.add(tcbean);
                taxclass_List.add(TaxClassDesc);*/

                // insert in table
                cf.insertTaxClass(TaxClassMasterId, TaxClassCode, TaxClassDesc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getTaxFromDatabase();
    }

    public void getTaxFromDatabase(){

        taxClassArrayList.clear();
        taxclass_List.clear();
        String query = "Select TaxClassMasterId,TaxClassCode,TaxClassDesc from "+ db.TABLE_TAXCLASS;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                TaxClassMasterId = c.getString(c.getColumnIndex("TaxClassMasterId"));
                TaxClassCode = c.getString(c.getColumnIndex("TaxClassCode"));
                TaxClassDesc = c.getString(c.getColumnIndex("TaxClassDesc"));

                TaxClassBean tcbean = new TaxClassBean();
                tcbean.setTaxClassMasterId(TaxClassMasterId);
                tcbean.setTaxClassCode(TaxClassCode);
                tcbean.setTaxClassDesc(TaxClassDesc);

                taxClassArrayList.add(tcbean);
                taxclass_List.add(TaxClassDesc);

            }while (c.moveToNext());
        }else {

        }

        taxadpter = new TaxAdapter(parent,taxClassArrayList);
        listtax.setAdapter(taxadpter);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item,taxclass_List);
        listtax.setAdapter(adapter);*/

    }

}
