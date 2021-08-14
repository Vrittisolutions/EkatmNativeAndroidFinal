package com.vritti.sales.activity;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.CustomerSelectionListAdapter;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Cust_Consignee_billto_ListActivity extends AppCompatActivity {

    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    ImageView imgrefresh,imgsearch, btnnewcust, btnprospsetting;;
    ListView list_consignee;
    EditText edtsearch;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    CommonFunction cf;
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

    ArrayList<Customer> lstReference = new ArrayList<>();
    SearchableSpinner sReference;
    MySpinnerAdapter stradapter;
    CustomerSelectionListAdapter custSelectionAdapter;

    public static final int CUSTOMERDATA = 8;
    ArrayList<Customer> listCust;
    ArrayList<String> listCustString;
    boolean isCustSelected;

    String CustVendorMasterId="", CustVendorName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_customers_list_selection_);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getConsigneeCnt() > 0){
            getAvailableCustomers();
        }

        /*if(getCustomerDataCount()){

            getDataFromDatabase();

        }else {
            //get data from database
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        //GetCustomerData();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }*/
        setListeners();
    }

    public void init(){
        parent = Cust_Consignee_billto_ListActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Customers List");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        imgrefresh = (ImageView)findViewById(R.id.imgrefresh);
        imgrefresh.setVisibility(View.GONE);
        imgsearch = (ImageView)findViewById(R.id.imgsearch);
        btnnewcust = (ImageView)findViewById(R.id.btnnewcust);
        btnnewcust.setVisibility(View.GONE);
        btnprospsetting = (ImageView)findViewById(R.id.btnprospsetting);
        list_consignee = (ListView)findViewById(R.id.list_consignee);
        edtsearch = (EditText)findViewById(R.id.edtsearch);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(Cust_Consignee_billto_ListActivity.this);
        cf = new CommonFunction(Cust_Consignee_billto_ListActivity.this);
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

        customerArrayList=new ArrayList<>();

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        listCust = new ArrayList<Customer>();
        listCustString = new ArrayList<String>();

    }

    public void setListeners(){

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                //first check customer in list if not found then call API without removing previous data

                if(s.toString().equals("") || s.toString().equals(null)){

                }else {
                    if(checkInDb(s.toString())){
                        //if true then filter search
                        custSelectionAdapter.filter((edtsearch).getText().toString().trim().toLowerCase(Locale.getDefault()));
                    }else {
                        //call API
                        if(s.length() >= 3 && !isCustSelected){
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
                        }
                    }
                }

            }
        });

        /*btnnewcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Cust_Consignee_billto_ListActivity.this, AddNewEntity_Customer.class);
                startActivity(intent);

                *//*Intent intent = new Intent(CustomersListSelection_Activity.this, AddNewCustProspect.class);
                startActivity(intent);*//*
            }
        });*/

        btnprospsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cust_Consignee_billto_ListActivity.this,
                        ProspectSettingsActivity_tbuds.class);
                startActivity(intent);
            }
        });

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        GetCustomerData();
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

      /*  edtsearch.addTextChangedListener(new TextWatcher() {

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
                custSelectionAdapter.filter((edtsearch)
                        .getText().toString().trim()
                        .toLowerCase(Locale.getDefault()));
            }
        });*/

        //textView.setOnItemSelectedListener(this);
        list_consignee.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                isCustSelected = true;

                Intent intent = new Intent(Cust_Consignee_billto_ListActivity.this, SalesHeaderActivity.class);
                //intent.putExtra("CustVendorMasterId", AnyMartData.USER_ID);
                intent.putExtra("username", username);
                intent.putExtra("CustName", listCust.get(position).getCustomer_name());
                intent.putExtra("CustVendorMasterId", listCust.get(position).getCustomerId());
                //intent.putExtra("intentFrom", "CustomerSelectionScreen");
                setResult(CUSTOMERDATA,intent);
                finish();

            }
        });

        /*list_consignee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String CustomerId = lstReference.get(position).getClient_id();
                String custName = lstReference.get(position).getClient_name();
                AnyMartData.USER_ID = CustomerId;
                AnyMartData.MOBILE_CUST = lstReference.get(position).getShipToMobile();
                username = lstReference.get(position).getCustomer_name();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Mobileno", AnyMartData.MOBILE);
                editor.putString("usertype", usertype);
                editor.putString("username", username);
                editor.putString("CustVendorMasterId", AnyMartData.USER_ID);
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.putString("MobileCust", AnyMartData.MOBILE_CUST);
                editor.commit();

                Intent intent = new Intent(Cust_Consignee_billto_ListActivity.this, SalesHeaderActivity.class);
                //intent.putExtra("CustVendorMasterId", AnyMartData.USER_ID);
                intent.putExtra("username", username);
                intent.putExtra("CustName", custName);
                intent.putExtra("CustVendorMasterId", CustomerId);
                //intent.putExtra("intentFrom", "CustomerSelectionScreen");
                setResult(CUSTOMERDATA,intent);
                finish();
            }
        });*/
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void GetCustomerData(){
        new DownloadReferenceJSON().execute();
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Reference + "?LeadWise=C";

                res = ut.OpenConnection(url);
                if (res != null) {
                 /*   String a = "\\";
                    String b = "\"";
                    String c = a+b;
                    response = res.toString().replaceAll(c,"");*/
                    response = res.toString().replaceAll("\\r\\n","");
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
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
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            customerArrayList.clear();
            lstReference.clear();

            tcf.clearTable(parent, db.TABLE_CONSIGNEES);

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){
                    for(int i=0; i<=jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String CustVendorName = jsonObject.getString("CustVendorName");
                            String CustVendorMasterId = jsonObject.getString("CustVendorMasterId");
                            String Mobile = jsonObject.getString("Mobile");
                            String Email = jsonObject.getString("Email");
                            String Address = jsonObject.getString("Address");
                            String AddedBy = jsonObject.getString("AddedBy");

                            Customer cust = new Customer();
                            cust.setCustomer_name(CustVendorName);
                            cust.setClient_id(CustVendorMasterId);
                            cust.setShipToAddress(Address);
                            cust.setShipToEmail(Email);
                            cust.setShipToMobile(Mobile);
                            cust.setAddedBy(AddedBy);

                            customerArrayList.add(cust);
                            lstReference.add(cust);

                            tcf.insertCustomer(CustVendorMasterId, CustVendorName, Email, Mobile, Address,AddedBy);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {

                }
            }catch (Exception e){

            }

           /* stradapter = new MySpinnerAdapter(CustomersListSelection_Activity.this,
                    R.layout.crm_custom_spinner_txt, lstReference);*/
            custSelectionAdapter = new CustomerSelectionListAdapter(parent, lstReference);
            list_consignee.setAdapter(custSelectionAdapter);
            list_consignee.setTextFilterEnabled(true);
            // sReference.setSelection(0);
        }
    }

    class getSearchedCustomer extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getCustomerAutocomplete + "?SearchText="+params[0];

                res = ut.OpenConnection(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            if(res.toString().equalsIgnoreCase("[]")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent,"No new records available",Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                try{
                    jResults = new JSONArray(res.toString().trim());

                    listCust.clear();
                    listCustString.clear();

                    for(int i=0; i<jResults.length();i++){

                        CustVendorMasterId = jResults.get(i).toString().split(",")[1];
                        CustVendorName = jResults.get(i).toString().split(",")[0];

                    /*Customer cust = new Customer();
                    cust.setCustomer_name(jResults.get(i).toString().split(",")[0]);
                    cust.setCustomerId(jResults.get(i).toString().split(",")[1]);
                    listCust.add(cust);
                    listCustString.add(jResults.get(i).toString().split(",")[0]);*/

                        if(chkIfCustAlreadyPresent(CustVendorMasterId)){
                            //customer already present
                        }else {
                            tcf.insertCustomer(CustVendorMasterId, CustVendorName, "", "", "","");
                        }

                    }

                    getAvailableCustomers();

               /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,listCustString);
                list_consignee.setAdapter(adapter);*/

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:

        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }

    public void getAvailableCustomers(){
        if(listCust.size() > 0){
            listCust.clear();;
            listCustString.clear();
        }

        String query = "Select CustVendorMasterId,CustVendorName from  "+db.TABLE_CONSIGNEES;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                CustVendorMasterId = c.getString(c.getColumnIndex("CustVendorMasterId"));
                CustVendorName = c.getString(c.getColumnIndex("CustVendorName"));

                Customer cust = new Customer();
                cust.setCustomer_name(CustVendorName);
                cust.setCustomerId(CustVendorMasterId);
                listCust.add(cust);
                listCustString.add(CustVendorName);

            }while (c.moveToNext());

            //Collections.sort(listCustString, String.CASE_INSENSITIVE_ORDER);

            Collections.sort(listCust, new Comparator<Customer>() {
                public int compare(Customer o1, Customer o2) {
                    if (o1.getCustomer_name() == null || o2.getCustomer_name() == null)
                        return 0;
                    return o2.getCustomer_name().compareTo(o1.getCustomer_name());
                }
            });

            /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,listCustString);
            list_consignee.setAdapter(adapter);*/

            custSelectionAdapter = new CustomerSelectionListAdapter(parent,listCust);
            list_consignee.setAdapter(custSelectionAdapter);
        }
    }

    public void getDataFromDatabase(){

        String query = "Select * from  "+db.TABLE_CONSIGNEES;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                String CustVendorName = c.getString(c.getColumnIndex("CustVendorName"));
                String CustVendorMasterId = c.getString(c.getColumnIndex("CustVendorMasterId"));
                String Mobile = c.getString(c.getColumnIndex("Mobile"));
                String Email = c.getString(c.getColumnIndex("Email"));
                String Address = c.getString(c.getColumnIndex("Address"));

                Customer cust = new Customer();
                cust.setCustomer_name(CustVendorName);
                cust.setClient_id(CustVendorMasterId);
                cust.setShipToAddress(Address);
                cust.setShipToEmail(Email);
                cust.setShipToMobile(Mobile);

                customerArrayList.add(cust);
                lstReference.add(cust);

            }while (c.moveToNext());

            custSelectionAdapter = new CustomerSelectionListAdapter(parent, lstReference);
            list_consignee.setAdapter(custSelectionAdapter);
            list_consignee.setTextFilterEnabled(true);

        }else {

        }
    }

    public boolean chkIfCustAlreadyPresent(String custVendorMasterId){
        String q = "SELECT CustVendorMasterId FROM "+db.TABLE_CONSIGNEES+ " WHERE CustVendorMasterId='"+custVendorMasterId+"'";
        Cursor c = sql.rawQuery(q,null);
        if(c.getCount() == 0){
            return false;
        }else if(c.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkInDb(String texttosearch){
        String q = "SELECT * FROM "+db.TABLE_CONSIGNEES+ " WHERE CustVendorName like '%" + texttosearch + "%'";
        Cursor c = sql.rawQuery(q,null);
        if(c.getCount() == 0){
            return false;   //call API
        }else if(c.getCount() > 0){
            return true;    //filter search
        }else {
            return false;   //call API
        }
    }

    public boolean getCustomerDataCount(){
        int count = 0;

        String query = "Select * from  "+db.TABLE_CONSIGNEES;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount()>0){
            return  true;

        }else {
            return false;
        }
        //  return count;
    }

}
