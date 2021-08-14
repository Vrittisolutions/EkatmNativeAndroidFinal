package com.vritti.sales.activity;

import android.annotation.SuppressLint;
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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.CityBean;
import com.vritti.sales.beans.CountryBean;
import com.vritti.sales.beans.DistrictBean;
import com.vritti.sales.beans.EntityType_Currency_PriceList;
import com.vritti.sales.beans.StateBean;
import com.vritti.sales.beans.TalukaBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddNewEntity_Customer extends AppCompatActivity {

    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;

    LinearLayout lay_district, lay_taluka, lay_city;
    AutoCompleteTextView edt_name, edt_entitytype, edt_entitycode, edt_mainaddress, edt_country, edt_state, edt_district,
            edt_taluka, edt_city, edt_pincode, edt_mobile, edt_email, edt_landline, edt_currency, edt_website, edt_condtls_name, edt_designation,
            edt_condtls_mobile, edt_condtls_email, sel_influence, sel_pricelist;
    CheckBox chkdefault, chkekatm_access, chkcontract_required;
    Button btnsave;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static DatabaseHandlers dbhandler;
    static Tbuds_commonFunctions cf;
    CommonFunctionCrm crmf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String usertype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql_db;
    SharedPreferences sharedpreferences;

    String Countryid = "", Stateid = "", Districtid = "", Talukaid = "", Cityid = "", SelPriceListId = "", Currencyid = "", Entityid = "",
            ContactDetails = "", ShipToData = "", entityMasterID = "", entityClass = "", entityCode = "";
    String[] influenceLevel = {"High","Medium","Low"};
    JSONObject jMain = null;
    String finalData = "";

    String KEY_JAPAN = "JPY";
    String KEY_IND_RUPEES = "INR";
    String KEY_FK = "BNK027";
    String KEY_USD = "USD";

    String KEY_ENT_CLASS_CUSTOMER = "1";     //Customer = 1, Supplier = 2, subcontractor = 3, onlyfinance = 4
    String KEY_ENT_CLASS_SUPPLIER = "2";
    String KEY_ENT_CLASS_SBCONTRACTOR = "3";
    String KEY_ENT_CLASS_OFINANCE = "4";

    boolean isDefault = false, isEkatmAccess = false, isContractRequired = false;
    String isDEFAULT = "", isEKATMACCESS = "", isCONTRACTREQ = "";

    ArrayList<CountryBean> mList = new ArrayList<>();
    ArrayList<StateBean> lstState = new ArrayList<>();
    ArrayList<DistrictBean> lstDistrict= new ArrayList<>();
    ArrayList<TalukaBean> lstTaluka= new ArrayList<>();
    ArrayList<CityBean> lstCity= new ArrayList<>();
    ArrayList<EntityType_Currency_PriceList> lstEntity= new ArrayList<>();
    ArrayList<EntityType_Currency_PriceList> lstCurrency= new ArrayList<>();
    ArrayList<EntityType_Currency_PriceList> lstPriceList= new ArrayList<>();
    ArrayList<String> lstinfluence= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_add_new_entity__customer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Customer");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //get data from APIs - EntityType, Country, State, City, District, Taluka, Currency, SalesPricelist
        //get Static data influencelevel, is default
        //get API for EntityCode

        new getClientCode().execute();
        //get API to add entity

        if (cf.getEntitycount() > 0) {
           // new DownloadEntityTypeJSON().execute();   //test purpose
            getEntityType();

        } else {
            if (isnet()) {
                new StartSession(AddNewEntity_Customer.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEntityTypeJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (crmf.getCountrycount() > 0) {
            getCountry();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCountryListJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (crmf.getCurrencycount() > 0) {
            getCurrency();

        } else {
            if (isnet()) {
                new StartSession(AddNewEntity_Customer.this, new CallbackInterface() {
                    @Override
                    public void callMethod() { new DownloadCurrencyMasterJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (cf.getSalesPriceListcount() > 0) {
            getSalesPriceList();

        } else {
            if (isnet()) {
                new StartSession(AddNewEntity_Customer.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadPriceListJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        setListeners();
    }

    public void init() {
        parent = AddNewEntity_Customer.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Add new customer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);

        lay_district = (TextInputLayout)findViewById(R.id.lay_district);
        lay_district.setBackgroundColor(Color.parseColor("#ededed"));
        lay_taluka = (TextInputLayout)findViewById(R.id.lay_taluka);
        lay_taluka.setBackgroundColor(Color.parseColor("#ededed"));
        lay_city = (TextInputLayout)findViewById(R.id.lay_city);
       // lay_city.setBackgroundColor(Color.parseColor("#ededed"));

        edt_name = (AutoCompleteTextView) findViewById(R.id.edt_name);
        edt_entitytype = (AutoCompleteTextView) findViewById(R.id.edt_entitytype);
        edt_entitycode = (AutoCompleteTextView) findViewById(R.id.edt_entitycode);
        edt_entitycode.setEnabled(false);
        edt_mainaddress = (AutoCompleteTextView) findViewById(R.id.edt_mainaddress);
        edt_country = (AutoCompleteTextView) findViewById(R.id.edt_country);
        edt_country.setEnabled(false);
        edt_state = (AutoCompleteTextView) findViewById(R.id.edt_state);
        edt_state.setEnabled(false);
        edt_district = (AutoCompleteTextView) findViewById(R.id.edt_district);
        edt_district.setEnabled(false);
        edt_taluka = (AutoCompleteTextView) findViewById(R.id.edt_taluka);
        edt_taluka.setEnabled(false);
        edt_city = (AutoCompleteTextView) findViewById(R.id.edt_city);
       // edt_city.setEnabled(false);
        edt_pincode = (AutoCompleteTextView) findViewById(R.id.edt_pincode);
        edt_mobile = (AutoCompleteTextView) findViewById(R.id.edt_mobile);
        edt_landline = (AutoCompleteTextView) findViewById(R.id.edt_landline);
        edt_email = (AutoCompleteTextView) findViewById(R.id.edt_email);
        edt_currency = (AutoCompleteTextView) findViewById(R.id.edt_currency);
        edt_website = (AutoCompleteTextView) findViewById(R.id.edt_website);
        edt_condtls_name = (AutoCompleteTextView) findViewById(R.id.edt_condtls_name); //contact details
        edt_designation = (AutoCompleteTextView) findViewById(R.id.edt_designation);
        edt_condtls_mobile = (AutoCompleteTextView) findViewById(R.id.edt_condtls_mobile);
        edt_condtls_email = (AutoCompleteTextView) findViewById(R.id.edt_condtls_email);
        sel_influence = (AutoCompleteTextView) findViewById(R.id.sel_influence);
        sel_pricelist = (AutoCompleteTextView) findViewById(R.id.sel_pricelist);
        chkdefault = (CheckBox) findViewById(R.id.chkdefault);
        chkekatm_access = (CheckBox) findViewById(R.id.chkekatm_access);
        chkcontract_required = (CheckBox) findViewById(R.id.chkcontract_required);
        btnsave = (Button) findViewById(R.id.btnsave_entity);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(AddNewEntity_Customer.this);
        cf = new Tbuds_commonFunctions(AddNewEntity_Customer.this);
        crmf = new CommonFunctionCrm(AddNewEntity_Customer.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(parent, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        lstinfluence.clear();

        for(int i=0; i<influenceLevel.length; i++){
            lstinfluence.add(influenceLevel[i]);
        }

        ArrayAdapter<String> influence_adapt = new ArrayAdapter<String>(AddNewEntity_Customer.this,
                R.layout.crm_custom_spinner_txt, lstinfluence);

        sel_influence.setAdapter(influence_adapt);
        sel_influence.setSelection(0);
        sel_influence.setThreshold(1);

    }

    public void setListeners() {

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jMain = getDataToSaveEntityPassAPI();
                finalData = jMain.toString().replaceAll("\\\\", "");
                finalData = finalData.replaceAll("^\"+ \"+$","");
                String a = "\"[";
                finalData = finalData.replace(a,"[");
                String b = "]\"";
                finalData = finalData.replace(b,"]");

                new PostAddNewEntityAPI().execute();
            }
        });

        edt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String countryname = edt_country.getText().toString().trim();
                try{
                    Countryid = getPosition_Countryfromspin(mList, countryname);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(item.toString());

                if(cf.getStatecount_ENTITY()>0) {
                    getState();
                }else {
                    new DownloadStatelistJSON().execute(Countryid);
                }
            }
        });

        edt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                edt_district.setText("");
                edt_taluka.setText("");
                edt_city.setText("");

                Districtid = ""; Talukaid = "";

                Object item = adapterView.getItemAtPosition(position);
                String statename = edt_state.getText().toString().trim();
                try{
                    Stateid = getPosition_Statefromspin(lstState, statename);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(item.toString());

                if (cf.getDistrictCount_ENTITY() > 0) {
                    getDistrict();
                } else {
                    new DownloadDistrictJSON().execute(Stateid);
                }


                new DownloadCityJSONData().execute(Stateid);
            }
        });

        edt_district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                edt_taluka.setText("");
                Talukaid = "";
                Object item = adapterView.getItemAtPosition(position);
                String districtname = edt_district.getText().toString().trim();
                try{
                    Districtid = getPosition_Districtfromspin(lstDistrict, districtname);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(item.toString());

                if (cf.getTalukaCount_ENTITY() > 0) {
                    getTaluka();
                } else {
                    new DownloadTalukaDataJson().execute(Districtid);
                }
            }
        });

        edt_taluka.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String talukaname = edt_taluka.getText().toString().trim();
                try{
                    Talukaid = getPosition_Talukafromspin(lstTaluka, talukaname);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println(item.toString());

                /*if (cf.getCityCount_ENTITY() > 0) {
                    //getCitydata(Talukaid);
                    //getCitydata(Stateid);
                } else {
                    new DownloadCityJSONData().execute(Talukaid);
                }*/
            }
        });

        sel_pricelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String pricelistname = sel_pricelist.getText().toString().trim();
                try {
                    SelPriceListId = getPosition_Salespricelistfromspin(lstPriceList,pricelistname);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        edt_currency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String currencyname = edt_currency.getText().toString().trim();
                try {
                    Currencyid = getPosition_Currencyfromspin(lstCurrency,currencyname);

                    if(currencyname.equals("Japan")){
                        edt_currency.setText(KEY_JAPAN);
                    }else if(currencyname.equals("indian rupee")){
                        edt_currency.setText(KEY_IND_RUPEES);
                    }else if(currencyname.equals("fk")){
                        edt_currency.setText(KEY_FK);
                    }else if(currencyname.equals("US Dollar")){
                        edt_currency.setText(KEY_USD);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        edt_entitytype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String entityname = edt_entitytype.getText().toString().trim();
                try {
                    Entityid = getPosition_Entityfromspin(lstEntity,entityname);

                    if(entityname.equals("Customer")){
                        entityClass = KEY_ENT_CLASS_CUSTOMER;
                        entityMasterID = KEY_ENT_CLASS_CUSTOMER;
                    }else if(entityname.equals("Supplier")){
                        entityClass = KEY_ENT_CLASS_SUPPLIER;
                        entityMasterID = KEY_ENT_CLASS_SUPPLIER;
                    }else if(entityname.equals("SubContractor")){
                        entityClass = KEY_ENT_CLASS_SBCONTRACTOR;
                        entityMasterID = KEY_ENT_CLASS_SBCONTRACTOR;
                    }else if(entityname.equals("Only Finance")){
                        entityClass = KEY_ENT_CLASS_OFINANCE;
                        entityMasterID = KEY_ENT_CLASS_OFINANCE;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        chkdefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDefault = chkdefault.isChecked();

                if(isDefault){
                    isDEFAULT = "Y";
                }else {
                    isDEFAULT = "N";
                }
            }
        });

        chkekatm_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEkatmAccess = chkdefault.isChecked();

                if(isEkatmAccess){
                    isEKATMACCESS = "Y";
                }else {
                    isEKATMACCESS = "N";
                }
            }
        });

        chkcontract_required.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isContractRequired = chkdefault.isChecked();

                if(isContractRequired){
                    isCONTRACTREQ = "Y";
                }else {
                    isCONTRACTREQ = "N";
                }
            }
        });
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

    class DownloadCountryListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            /*String url = CompanyURL + WebUrlClass.api_get_countrylist;*/
            String url = CompanyURL + WebUrlClass.api_getCountry;

            try {
                res = ut.OpenConnection(url);

                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql_db.delete(dbhandler.TABLE_COUNTRY, null, null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_COUNTRY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql_db.insert(dbhandler.TABLE_COUNTRY, null, values);
                    Log.e("country", "" + a);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //mprogress.setVisibility(View.GONE);

            if (response.contains("PKCountryId")) {
                getCountry();
            } else if(response.equalsIgnoreCase("[]") || response == ""  || response == null){
            }
        }
    }

    private void getCountry() {
        //  //mprogress.setVisibility(View.VISIBLE);
        mList.clear();
        String query = "SELECT distinct PKCountryId, CountryName FROM " + dbhandler.TABLE_COUNTRY;
        Cursor cur = sql_db.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                mList.add(new CountryBean(cur.getString(cur.getColumnIndex("PKCountryId")),
                        cur.getString(cur.getColumnIndex("CountryName"))));

            } while (cur.moveToNext());

        }

        ArrayAdapter<CountryBean> countryArrayAdapter = new ArrayAdapter<CountryBean>
                (AddNewEntity_Customer.this, android.R.layout.simple_spinner_dropdown_item, mList);
        edt_country.setAdapter(countryArrayAdapter);
        edt_country.setThreshold(0);

        if (mList.size() > 1) {
            edt_country.setEnabled(true);
        } else if (mList.size() == 1) {
            edt_country.setEnabled(false);
            edt_country.setText(mList.get(0).getCountryName());
            Countryid = mList.get(0).getPKCountryId();
        }

        if(cf.getStatecount_ENTITY()>0) {
               getState();
         }else {
             if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        if (mList.get(0) != null)
                            new DownloadStatelistJSON().execute(Countryid);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
    }
}

    class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mprogress.setVisibility(View.VISIBLE);

        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String counId = params[0];
            String url = CompanyURL + WebUrlClass.api_get_statelistdata + "?Id=" + counId;
            // String url = CompanyURL + WebUrlClass.api_get_Statelist;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                //sql.delete(db.TABLE_STATE, null, null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_STATE_ENTITY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (j == 0)
                            Stateid = columnValue;
                    }

                    long a = sql_db.insert(dbhandler.TABLE_STATE_ENTITY, null, values);

                }

            } catch (Exception e) {
                //mprogress.setVisibility(View.GONE);

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //dismissProgressDialog();
            //mprogress.setVisibility(View.GONE);
            if (response.equals("") || response.equals("[]")) {
                Toast.makeText(parent,"No State Found",Toast.LENGTH_SHORT).show();
            } else {
                getState();

                new DownloadCityJSONData().execute(Stateid);
            }
        }
    }

    private void getState() {

        lstState.clear();
        if (Countryid.equalsIgnoreCase("") || Countryid.equalsIgnoreCase(" ")
                || Countryid.equalsIgnoreCase("null") || Countryid.equalsIgnoreCase(null)) {

            Countryid = mList.get(0).getPKCountryId();

        }else {

        }
            String query = "SELECT distinct PKStateId,StateDesc" +
                    " FROM " + dbhandler.TABLE_STATE_ENTITY + " WHERE FKCountryId='" + Countryid + "'";

            Cursor cur = sql_db.rawQuery(query, null);
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {

                    lstState.add(new StateBean(cur.getString(cur.getColumnIndex("PKStateId")),
                            cur.getString(cur.getColumnIndex("StateDesc"))
                    ));

                } while (cur.moveToNext());

                ArrayAdapter<StateBean> statearrayadapter = new ArrayAdapter<StateBean>
                        (AddNewEntity_Customer.this,
                                android.R.layout.simple_spinner_dropdown_item, lstState);
                edt_state.setAdapter(statearrayadapter);
                //   customDept.notifyDataSetChanged();
                edt_state.setSelection(0);

                if (lstState.size() > 1) {
                    edt_state.setEnabled(true);
                } else {
                    edt_state.setEnabled(false);
                    edt_state.setText(lstState.get(0).getStateDesc());
                    Stateid = lstState.get(0).getPKStateId();
                }

            } else {
                /*if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadStatelistJSON().execute(Countryid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }*/
            }

        if (cf.getDistrictCount_ENTITY() > 0) {
            getDistrict();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        if (lstState.size() != 0){
                            new DownloadDistrictJSON().execute(Stateid);
                        }else{
                            //edt_state.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
    }

    class DownloadDistrictJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Stateid = params[0];
                String url = CompanyURL + WebUrlClass.api_getDistrict
                        + "?Id=" + Stateid;

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql_db.delete(dbhandler.TABLE_DISTRICT, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_DISTRICT, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql_db.insert(dbhandler.TABLE_DISTRICT, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  dismissProgressDialog();

            //mprogress.setVisibility(View.GONE);

            if (response.contains("PKDistrictId")) {
                // getInitiatedby();
                getDistrict();
                lay_district.setBackgroundColor(Color.parseColor("#ffffff"));

            } else if (response.equalsIgnoreCase("[]")){
                lay_district.setBackgroundColor(Color.parseColor("#ededed"));
                Toast.makeText(parent,"No district found",Toast.LENGTH_SHORT).show();

                edt_district.setEnabled(false);

            }
        }

    }

    private void getDistrict() {

        lstDistrict.clear();
       /* if (Stateid.equalsIgnoreCase("") || Stateid.equalsIgnoreCase(null) ||
                Stateid.equalsIgnoreCase("null") || Stateid.equalsIgnoreCase(" ")) {

            if(lstState.size() != 0)
                Stateid = lstState.get(0).getPKStateId();

        }else {

        }*/
            String query = "SELECT distinct PKDistrictId,DistrictDesc" +
                    " FROM " + dbhandler.TABLE_DISTRICT +
                    " WHERE FKStateId='" + Stateid + "'";

            Cursor cur = sql_db.rawQuery(query, null);
            //   lstReferenceType.add("Select");

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {

                lstDistrict.add(new DistrictBean(cur.getString(cur.getColumnIndex("PKDistrictId")),
                            cur.getString(cur.getColumnIndex("DistrictDesc"))));

                } while (cur.moveToNext());

                ArrayAdapter<DistrictBean> cityarraStateArrayAdapter = new ArrayAdapter<DistrictBean>
                        (this, android.R.layout.simple_spinner_dropdown_item, lstDistrict);
                edt_district.setAdapter(cityarraStateArrayAdapter);
                //   customDept.notifyDataSetChanged();
                edt_district.setSelection(0);

                if (lstDistrict.size() > 1) {
                    edt_district.setEnabled(true);
                } else {
                    edt_district.setEnabled(false);
                    edt_district.setText(lstDistrict.get(0).getDistrictDesc());
                    Districtid = lstDistrict.get(0).getPKDistrictId();
                }

            }else {
              //  Toast.makeText(parent,"No District Found",Toast.LENGTH_SHORT).show();
                lay_district.setBackgroundColor(Color.parseColor("#ededed"));
                //lay_city.setBackgroundColor(Color.parseColor("#ededed"));
                lay_taluka.setBackgroundColor(Color.parseColor("#ededed"));

                if(isnet()){
                    new StartSession(this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadDistrictJSON().execute(Stateid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }

        if (cf.getTalukaCount_ENTITY() > 0) {
            getTaluka();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        if (lstState.size() != 0){
                            new DownloadTalukaDataJson().execute(Districtid);
                        }else{

                        }
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
    }

    class DownloadTalukaDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String Districtid = params[0];

            String url = CompanyURL + WebUrlClass.api_getTaluka + "?Id=" + Districtid;

            try {
                res = ut.OpenConnection(url, AddNewEntity_Customer.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();

                    sql_db.delete(dbhandler.TABLE_TALUKA_ENTITY, null,
                            null);
                    Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_TALUKA_ENTITY, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    lstTaluka.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql_db.insert(dbhandler.TABLE_TALUKA_ENTITY, null, values);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            //mprogress.setVisibility(View.GONE);

            if (response.contains("[]") || response.equalsIgnoreCase("")) {

                Toast.makeText(AddNewEntity_Customer.this, "No taluka found", Toast.LENGTH_SHORT).show();
                edt_taluka.setEnabled(false);
                lay_taluka.setBackgroundColor(Color.parseColor("#ededed"));

            } else {
               lay_taluka.setBackgroundColor(Color.parseColor("#ffffff"));
               getTaluka();

            }

        }
    }

    private void getTaluka() {

        lstTaluka.clear();
        if (Districtid.equalsIgnoreCase("") || Districtid.equalsIgnoreCase(null) ||
                Districtid.equalsIgnoreCase("null") || Districtid.equalsIgnoreCase(" ")) {

            if(lstDistrict.size() != 0)
                Districtid = lstDistrict.get(0).getPKDistrictId();

        }else {

        }

        String query = "SELECT distinct PKTalukaId,TalukaDesc" +
                " FROM " + dbhandler.TABLE_TALUKA_ENTITY +
                " WHERE FKDistrictId='" + Districtid + "'";

        Cursor cur = sql_db.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                lstTaluka.add(new TalukaBean(cur.getString(cur.getColumnIndex("PKTalukaId")),
                        cur.getString(cur.getColumnIndex("TalukaDesc"))));

            } while (cur.moveToNext());

            ArrayAdapter<TalukaBean> talukaAdapter = new ArrayAdapter<TalukaBean>
                    (this, android.R.layout.simple_spinner_dropdown_item, lstTaluka);
            edt_taluka.setAdapter(talukaAdapter);
            edt_taluka.setSelection(0);

            if (lstTaluka.size() > 1) {
                edt_taluka.setEnabled(true);
            } else if(lstTaluka.size() == 0) {
                edt_taluka.setText("");
                edt_taluka.setEnabled(false);
            }else{
                edt_taluka.setEnabled(false);
                edt_taluka.setText(lstTaluka.get(0).getTalukaDesc());
                Talukaid = lstTaluka.get(0).getPKTalukaId();
            }

        }else {
            lay_taluka.setBackgroundColor(Color.parseColor("#ededed"));

            if(isnet()){
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTalukaDataJson().execute(Districtid);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

    }

    class DownloadCityJSONData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          // //mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String citycnt = params[0];
            Log.i("stateId ::" , citycnt);
            try {
                String url = CompanyURL + WebUrlClass.api_getCityMaster;    //+ "?Id=" + Stateid;

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();

                sql_db.delete(dbhandler.TABLE_CITY_ENTITY, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_CITY_ENTITY, null);
                int count = c.getCount();
                String columnName, columnValue;

                lstTaluka.clear();
                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql_db.insert(dbhandler.TABLE_CITY_ENTITY, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            ////mprogress.setVisibility(View.GONE);
            if (response.contains("PKCityID")) {
                lay_city.setBackgroundColor(Color.parseColor("#ffffff"));
                getCitydata(Stateid);
            } else if(response.equalsIgnoreCase("[]")){
               // edt_city.setEnabled(false);
               // lay_city.setBackgroundColor(Color.parseColor("#ededed"));
                Toast.makeText(parent,"No City Found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCitydata(String stateid) {
        ////mprogress.setVisibility(View.GONE);

        lstCity.clear();

        Stateid = stateid;
            String query = "SELECT distinct PKCityID,CityName FROM " + dbhandler.TABLE_CITY_ENTITY +
                    " WHERE FKStateId='" + Stateid + "'";

            Cursor cur = sql_db.rawQuery(query, null);

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {

                    lstCity.add(new CityBean(cur.getString(cur.getColumnIndex("PKCityID")),
                            cur.getString(cur.getColumnIndex("CityName"))));

                } while (cur.moveToNext());

                ArrayAdapter<CityBean> cityarraStateArrayAdapter = new ArrayAdapter<CityBean>
                        (AddNewEntity_Customer.this,
                                android.R.layout.simple_spinner_dropdown_item, lstCity);
                edt_city.setAdapter(cityarraStateArrayAdapter);
                edt_city.setSelection(0);

                if (lstCity.size() > 1) {
                    edt_city.setEnabled(true);
                } else {
                   // edt_city.setEnabled(false);
                    edt_city.setText(lstCity.get(0).getCityName());
                    Cityid = lstCity.get(0).getPKCityID();
                }

                lay_city.setBackgroundColor(Color.parseColor("#ffffff"));

            } else {
                if (isnet()) {
                  //  lay_city.setBackgroundColor(Color.parseColor("#ededed"));
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCityJSONData().execute(Stateid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
    }

    class DownloadCurrencyMasterJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {

            //   progressHUD5 = ProgressHUD.show(context, " ", false, false, null);
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
            //          showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getCurrencyMaster;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql_db.delete(dbhandler.TABLE_CurrencyMaster, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_CurrencyMaster, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql_db.insert(dbhandler.TABLE_CurrencyMaster, null, values);
                    Log.e("", "" + a);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            mprogress.setVisibility(View.GONE);
            if (response.contains("CurrencyMasterId")) {
                getCurrency();
            }
        }

    }

    private void getCurrency() {

        ArrayList<String> currency = new ArrayList();
        currency.clear();
        String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                " FROM " + dbhandler.TABLE_CurrencyMaster;
        Cursor cur = sql_db.rawQuery(query, null);
        // currency.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                EntityType_Currency_PriceList currencyBean = new EntityType_Currency_PriceList();
                String currDesc = cur.getString(cur.getColumnIndex("CurrDesc"));
                currencyBean.setCurrDesc(currDesc);
                currencyBean.setCurrencyMasterId(cur.getString(cur.getColumnIndex("CurrencyMasterId")));

                lstCurrency.add(currencyBean);

                currency.add(currDesc);

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> customDept = new ArrayAdapter<String>(AddNewEntity_Customer.this,
                R.layout.crm_custom_spinner_txt, currency);

        edt_currency.setAdapter(customDept);
        edt_currency.setSelection(0);
        edt_currency.setThreshold(0);

        if(currency.size() == 1){
            edt_currency.setEnabled(false);
            edt_currency.setThreshold(0);
            //edt_currency.setText(lstCurrency.get(0).getCurrDesc());

            Currencyid = lstCurrency.get(0).getCurrencyMasterId();

            if(lstCurrency.get(0).getCurrDesc().equals("Japan")){
                edt_currency.setText(KEY_JAPAN);
            }else if(lstCurrency.get(0).getCurrDesc().equals("Indian Rupee")){
                edt_currency.setText(KEY_IND_RUPEES);
            }else if(lstCurrency.get(0).getCurrDesc().equals("fk")){
                edt_currency.setText(KEY_FK);
            }else if(lstCurrency.get(0).getCurrDesc().equals("US Dollar")){
                edt_currency.setText(KEY_USD);
            }

        }else if(currency.size() > 1){
            edt_currency.setEnabled(true);
        }

    }

    class DownloadEntityTypeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getEntityType+"?CType="+usertype;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replace("\\\\r\\\\n","");
                response = response.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql_db.delete(dbhandler.TABLE_ENTITY_TYPE, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_ENTITY_TYPE, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql_db.insert(dbhandler.TABLE_ENTITY_TYPE, null, values);
                    Log.e("", "" + a);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            mprogress.setVisibility(View.GONE);
            if (response.contains("EntityTypeMasterId")) {
                getEntityType();
            }
        }
    }

    private void getEntityType() {

        ArrayList<String> entity = new ArrayList();
        entity.clear();
        String query = "SELECT distinct EntityType, Entity, EntityTypeMasterId, EntityTypeCode" +
                " FROM " + dbhandler.TABLE_ENTITY_TYPE;
        Cursor cur = sql_db.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                EntityType_Currency_PriceList entityBean = new EntityType_Currency_PriceList();
                String entityDesc = cur.getString(cur.getColumnIndex("EntityType"));
                entityBean.setEntityType(entityDesc);
                entityBean.setEntity(cur.getString(cur.getColumnIndex("Entity")));
                entityBean.setEntityTypeMasterId(cur.getString(cur.getColumnIndex("EntityTypeMasterId")));
                entityBean.setEntityTypeCode(cur.getString(cur.getColumnIndex("EntityTypeCode")));

                lstEntity.add(entityBean);

                entity.add(entityDesc);
            } while (cur.moveToNext());
        }

        ArrayAdapter<String> customDept = new ArrayAdapter<String>(AddNewEntity_Customer.this,
                R.layout.crm_custom_spinner_txt, entity);

        edt_entitytype.setAdapter(customDept);
        edt_entitytype.setSelection(0);

        if(entity.size() == 1){
            edt_entitytype.setEnabled(false);
            edt_entitytype.setThreshold(0);
            edt_entitytype.setText(lstEntity.get(0).getEntityType());

            Entityid = lstEntity.get(0).getEntityTypeMasterId();

            if(lstEntity.get(0).getEntityType().equals("Customer")){
                entityClass = KEY_ENT_CLASS_CUSTOMER;
                entityMasterID = KEY_ENT_CLASS_CUSTOMER;
            }else if(lstEntity.get(0).getEntityType().equals("Supplier")){
                entityClass = KEY_ENT_CLASS_SUPPLIER;
                entityMasterID = KEY_ENT_CLASS_SUPPLIER;
            }else if(lstEntity.get(0).getEntityType().equals("SubContractor")){
                entityClass = KEY_ENT_CLASS_SBCONTRACTOR;
                entityMasterID = KEY_ENT_CLASS_SBCONTRACTOR;
            }else if(lstEntity.get(0).getEntityType().equals("Only Finance")){
                entityClass = KEY_ENT_CLASS_OFINANCE;
                entityMasterID = KEY_ENT_CLASS_OFINANCE;
            }

        }else if(entity.size() > 1){
            edt_entitytype.setEnabled(true);
        }

    }

    class DownloadPriceListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getSalesPriceList;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql_db.delete(dbhandler.TABLE_SALES_PRICELIST, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_SALES_PRICELIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql_db.insert(dbhandler.TABLE_SALES_PRICELIST, null, values);
                    Log.e("", "" + a);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            mprogress.setVisibility(View.GONE);
            if (response.contains("PriceListHdrID")) {
                getSalesPriceList();
            }
        }

    }

    class getClientCode extends AsyncTask<String, Void, String> {
        String res;
        //List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL+WebUrlClass.api_CustomerCode+"?type="+usertype;

            res = ut.OpenConnection(url,AddNewEntity_Customer.this);

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            mprogress.setVisibility(View.GONE);
            if (integer!=null){

                entityCode = integer;
                edt_entitycode.setText(entityCode);

            }
        }
    }

    private void getSalesPriceList() {

        ArrayList<String> pricelist = new ArrayList();
        pricelist.clear();
        String query = "SELECT distinct PriceLstDesc, PriceListHdrID" +
                " FROM " + dbhandler.TABLE_SALES_PRICELIST;
        Cursor cur = sql_db.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                EntityType_Currency_PriceList pricelistBean = new EntityType_Currency_PriceList();
                String priceDesc = cur.getString(cur.getColumnIndex("PriceLstDesc"));
                pricelistBean.setPriceLstDesc(priceDesc);
                pricelistBean.setPriceListHdrID(cur.getString(cur.getColumnIndex("PriceListHdrID")));

                lstPriceList.add(pricelistBean);

                pricelist.add(priceDesc);

            } while (cur.moveToNext());
        }

        ArrayAdapter<String> customDept = new ArrayAdapter<String>(AddNewEntity_Customer.this,
                R.layout.crm_custom_spinner_txt, pricelist);

        sel_pricelist.setAdapter(customDept);
        sel_pricelist.setSelection(0);

        if(pricelist.size() == 1){
            sel_pricelist.setEnabled(false);
            sel_pricelist.setThreshold(0);
            sel_pricelist.setText(lstPriceList.get(0).getPriceLstDesc());

            String pricelistname = sel_pricelist.getText().toString().trim();
            try {
                SelPriceListId = getPosition_Salespricelistfromspin(lstPriceList,pricelistname);
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(pricelist.size() > 1) {
            sel_pricelist.setEnabled(true);
            sel_pricelist.setText(lstPriceList.get(0).getPriceLstDesc());
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

    private String getPosition_Countryfromspin(ArrayList<CountryBean> lst_country, String country) throws JSONException {
        String Country_Id =null;
        for(CountryBean countryBean : lst_country) {
            if(countryBean.getCountryName().equalsIgnoreCase(country)){
                Country_Id = countryBean.getPKCountryId();
            }
        }
        return Country_Id; //it wasn't found at all
    }

    private String getPosition_Statefromspin(ArrayList<StateBean> lst_State, String State) throws JSONException {
        String StateId =null;
        for(StateBean stateBean : lst_State) {
            if(stateBean.getStateDesc().equalsIgnoreCase(State)){
                StateId = stateBean.getPKStateId();
            }
        }
        return StateId; //it wasn't found at all
    }

    private String getPosition_Districtfromspin(ArrayList<DistrictBean> lst_District, String District) throws JSONException {
        String DistrictId =null;
        for(DistrictBean districtBean : lst_District) {
            if(districtBean.getDistrictDesc().equalsIgnoreCase(District)){
                DistrictId = districtBean.getPKDistrictId();
            }
        }
        return DistrictId; //it wasn't found at all
    }

    private String getPosition_Talukafromspin(ArrayList<TalukaBean> lst_District, String Taluka) throws JSONException {
        String TalukaId =null;
        for(TalukaBean talukaBean : lst_District) {
           /* if(talukaBean.getDistrictDesc().equalsIgnoreCase(District)){
                TalukaId = talukaBean.getPKDistrictId();
            }*/
        }
        return TalukaId; //it wasn't found at all
    }

    private String getPosition_Cityfromspin(ArrayList<CityBean> lst_District, String City) throws JSONException {
        String CityId =null;
        for(CityBean cityBean : lst_District) {
            if(cityBean.getCityName().equalsIgnoreCase(City)){
                CityId = cityBean.getPKCityID();
            }
        }
        return CityId; //it wasn't found at all
    }

    private String getPosition_Salespricelistfromspin(ArrayList<EntityType_Currency_PriceList> lst_pricelist,
                                                      String PriceListName)
            throws JSONException {
        String PriceListId =null;
        for(EntityType_Currency_PriceList pricelistBean : lst_pricelist) {
            if(pricelistBean.getPriceLstDesc().equalsIgnoreCase(PriceListName)){
                PriceListId = pricelistBean.getPriceListHdrID();
            }
        }
        return PriceListId; //it wasn't found at all
    }

    private String getPosition_Currencyfromspin(ArrayList<EntityType_Currency_PriceList> lst_currency,
                                                String CurrencyName) throws JSONException {
        String CurrencyID =null;
        for(EntityType_Currency_PriceList currencyBean : lst_currency) {
            if(currencyBean.getCurrDesc().equalsIgnoreCase(CurrencyName)){
                CurrencyID = currencyBean.getCurrencyMasterId();
            }
        }
        return CurrencyID; //it wasn't found at all
    }

    private String getPosition_Entityfromspin(ArrayList<EntityType_Currency_PriceList> lst_entity,
                                                String EntityName) throws JSONException {
        String entityTypeMasterID =null;
        for(EntityType_Currency_PriceList entityBean : lst_entity) {
            if(entityBean.getEntityType().equalsIgnoreCase(EntityName)){
                entityTypeMasterID = entityBean.getEntityTypeMasterId();
            }
        }
        return entityTypeMasterID; //it wasn't found at all
    }

    public JSONObject getContactDetailsData(){

        JSONObject ContactDetails_JSON = null;

        String ContPerName = "", Designation = "", DIN = "", EmailId = "", InfluentialLevel = "", IsDefault = "",
                EntityContactInfoId = "",ContactNo = "", IsVWBLoginAllowed = "", CD_LoginId = "", CD_Password = "";

        ContPerName = edt_condtls_name.getText().toString().trim();
        Designation = edt_designation.getText().toString().trim();
        EmailId = edt_condtls_email.getText().toString().trim();
        InfluentialLevel = sel_influence.getText().toString().trim();
        IsDefault = isDEFAULT;
        ContactNo = edt_condtls_mobile.getText().toString().trim();
        CD_LoginId = edt_email.getText().toString().trim();

        JSONObject jObj_contactDetails = new JSONObject();
        try {
            jObj_contactDetails.put("ContPerName",ContPerName);
            jObj_contactDetails.put("Designation",Designation);
            jObj_contactDetails.put("DIN",DIN);
            jObj_contactDetails.put("EmailId",EmailId);
            jObj_contactDetails.put("InfluentialLevel",InfluentialLevel);
            jObj_contactDetails.put("IsDefault",IsDefault);
            jObj_contactDetails.put("EntityContactInfoId",EntityContactInfoId);
            jObj_contactDetails.put("ContactNo",ContactNo);
            jObj_contactDetails.put("IsVWBLoginAllowed",IsVWBLoginAllowed);
            jObj_contactDetails.put("LoginId",CD_LoginId);
            jObj_contactDetails.put("Password",CD_Password);

            ContactDetails_JSON = jObj_contactDetails;

        } catch (JSONException e) {
            e.printStackTrace();
            ContactDetails_JSON = null;
        }

        return ContactDetails_JSON;

    }

    public JSONObject getShipToDetailsData(){

        JSONObject shiptoDetails_JSON = null;
        String ConsigneeName = "",ContactPerson = "",Address = "", City = "", Country = "",State = "", CountryName = "", StateName = "",
                Phone = "",Mobile = "", Fax = "",ShipToMasterId = "",Distance = "",EmailId = "",RouteMasterId= "",CityName= "",GSTState= "",
                GSTCode = "",TAN_GSTIN_Number = "",TANNo = "", TANNoName = "",PAN = "",LOCAAdhar = "",LOCPFNo = "", LOCESIC = "",
                GeoLocation = "",isBlocked= "",Rating = "",Latitude = "",Longitude = "";

        /*ConsigneeName = edt_name.getText().toString().trim();
        ContactPerson = edt_name.getText().toString().trim();
        Address = edt_mainaddress.getText().toString().trim();
        City = Cityid;
        Country = "1";
        State = Stateid;
        CountryName = edt_country.getText().toString().trim();
        StateName = edt_state.getText().toString().trim();
        Mobile = edt_mobile.getText().toString();
        ShipToMasterId = "";
        EmailId = edt_email.getText().toString().trim();
        CityName = edt_city.getText().toString().trim();
        Latitude = "";
        Longitude = "";
        TANNo = "";
        TAN_GSTIN_Number = "";*/

        JSONObject jObj_shipto = new JSONObject();
        try {
            jObj_shipto.put("ConsigneeName",ConsigneeName);
            jObj_shipto.put("ContactPerson",ContactPerson);
            jObj_shipto.put("Address",Address);
            jObj_shipto.put("City",City);
            jObj_shipto.put("Country",Country);
            jObj_shipto.put("State",State);
            jObj_shipto.put("CountryName",CountryName);
            jObj_shipto.put("StateName",StateName);
            jObj_shipto.put("Phone",Phone);
            jObj_shipto.put("Mobile",Mobile);
            jObj_shipto.put("Fax",Fax);
            jObj_shipto.put("ShipToMasterId",ShipToMasterId);
            jObj_shipto.put("Distance",Distance);
            jObj_shipto.put("EmailId",EmailId);
            jObj_shipto.put("RouteMasterId",RouteMasterId);
            jObj_shipto.put("CityName",CityName);
            jObj_shipto.put("StateName",StateName);
            jObj_shipto.put("CountryName",CountryName);
            jObj_shipto.put("GSTState",GSTState);
            jObj_shipto.put("GSTCode",GSTCode);
            jObj_shipto.put("TAN_GSTIN_Number",TAN_GSTIN_Number);
            jObj_shipto.put("TANNo",TANNo);
            jObj_shipto.put("TANNoName",TANNoName);
            jObj_shipto.put("PAN",PAN);
            jObj_shipto.put("LOCAAdhar",LOCAAdhar);
            jObj_shipto.put("LOCPFNo",LOCPFNo);
            jObj_shipto.put("LOCESIC",LOCESIC);
            jObj_shipto.put("GeoLocation",GeoLocation);
            jObj_shipto.put("isBlocked",isBlocked);
            jObj_shipto.put("Rating",Rating);
            jObj_shipto.put("Latitude",Latitude);
            jObj_shipto.put("Longitude",Longitude);

            shiptoDetails_JSON = jObj_shipto;

        } catch (JSONException e) {
            e.printStackTrace();
            shiptoDetails_JSON = null;
        }

        return shiptoDetails_JSON;
    }

    public JSONObject getDataToSaveEntityPassAPI(){

        JSONObject entity_JSON = null;

        String InsCountry1 = "", InsSeg1 = "", LendCountry1 = "", LendSeg1 = "", CallId = "", CustVendorCode = "", RegistrationFormNo = "",
                CustVendorName = "", ContactTitle = "", ContactName = "", Address = "", ShortName = "", City = "", Phone = "", Mobile = "",
                Email = "", Pin = "", State = "", Country = "", District = "", Taluka = "",FKTerritoryId="", Website = "", PriceListId = "", vendorCode = "",
                ENRect = "", ENInv = "", ENPndPO = "",EnterpriseType = "", CustVendor = "", IsWLForCRMRef = "", Active = "", IsActive = "",
                CountryId = "", ENGRN = "", ENPymt = "", CreditLimit = "", CreditDays = "", Currency = "", PaymentTerms = "", DeliveryTerms = "",
                TaxClass = "", SlCatId = "",ResellerName = "",CreditTerms = "", PayeeName = "", BankName = "", Branch = "", BankAddress = "",
                AccountNo = "", AccountType = "", IFSCode = "", RemittanceInstruction = "", EvaluationDt = "", ValidFrom = "",ValidTo = "",
                SystemUserId = "", IsApproved = "", IsContractReqd = "",CurrencyMasterID = "", EntityGroupMasterId = "", EntityClass = "",
                CustVendorType = "", Typeofservices = "", ServClId = "", PANNO = "", GSTNO = "", TAN_GSTIN_Number = "", TANNO = "",CSTNo = "",
                MSTNo = "", ECCNo = "", ServiceTaxNo = "", ExDivi = "", ExRange = "", CAT = "", AccountId = "", EsicNo = "", AadharNo = "",
                PFNo = "", CIN = "", EntityRestDate = "", ClientDetails = "", ShipToDetails = "", SalesFamily = "", LenderDetails = "",
                InsuranceDetails = "", BankPayeeName = "", ExpertiseDetails = "",Latitude = "", Longitude = "",VendorMasterID = "",
                TenorYear = "", IndIndemnity = "", CustVendorMasterId = "", RecId = "";

        RecId = UUID.randomUUID().toString();
        CustVendorMasterId = UUID.randomUUID().toString();

        CustVendorName = edt_name.getText().toString().trim();
        ContactTitle = edt_name.getText().toString().trim();
        ContactName = edt_name.getText().toString().trim();
        Address = edt_mainaddress.getText().toString().trim();
        ShortName = edt_name.getText().toString().trim();
        City = edt_city.getText().toString().trim();
        Phone = edt_landline.getText().toString().trim();
        Mobile = edt_mobile.getText().toString().trim();
        Email = edt_email.getText().toString().trim();
        Pin = edt_pincode.getText().toString().trim();
        State = Stateid;
        Country = Countryid;
        District = Districtid;
        Taluka = Talukaid;
        Website = edt_website.getText().toString().trim();
        PriceListId = SelPriceListId.trim();
        CountryId = Countryid;
        Currency = CurrencyMasterID;

        String[] eval_validat_from_to_date = getCurrentDate().split(",");
        EvaluationDt = eval_validat_from_to_date[0];
        ValidFrom = eval_validat_from_to_date[1];
        ValidTo = eval_validat_from_to_date[2];

        IsContractReqd = isCONTRACTREQ;
        CurrencyMasterID = Currencyid;
        EntityGroupMasterId = entityMasterID;
        EntityClass = entityClass;
        CustVendorType = Entityid;

        JSONArray jsonArray_client = new JSONArray();
        try {
            jsonArray_client.put(getContactDetailsData());
            ContactDetails = jsonArray_client.toString().replaceAll("\\\\", "");
            //ContactDetails = ContactDetails.replaceAll("^\"|\"+$","");
            //ContactDetails = ContactDetails.substring(1, ContactDetails.length()-1);
        }catch (Exception e){
            e.printStackTrace();
        }
        ClientDetails = ContactDetails;

        ShipToDetails = String.valueOf(new JSONArray());
        LenderDetails = String.valueOf(new JSONArray());
        InsuranceDetails = String.valueOf(new JSONArray());
        BankPayeeName = String.valueOf(new JSONArray());
        ExpertiseDetails = String.valueOf(new JSONArray());

        JSONObject jObj_entity = new JSONObject();

        try {
            jObj_entity.put("RecId",RecId);
            jObj_entity.put("CustVendorMasterId",CustVendorMasterId);
            jObj_entity.put("InsCountry1",InsCountry1);
            jObj_entity.put("InsSeg1",InsSeg1);
            jObj_entity.put("LendCountry1",LendCountry1);
            jObj_entity.put("LendSeg1",LendSeg1);
            jObj_entity.put("CallId",CallId);
            jObj_entity.put("CustVendorCode",CustVendorCode);
            jObj_entity.put("RegistrationFormNo",RegistrationFormNo);
            jObj_entity.put("CustVendorName",CustVendorName);
            jObj_entity.put("ContactTitle",ContactTitle);
            jObj_entity.put("ContactName",ContactName);
            jObj_entity.put("Address",Address);
            jObj_entity.put("ShortName",ShortName);
            jObj_entity.put("City",City);
            jObj_entity.put("Phone",Phone);
            jObj_entity.put("Mobile",Mobile);
            jObj_entity.put("Email",Email);
            jObj_entity.put("Pin",Pin);
            jObj_entity.put("State",State);
            jObj_entity.put("Country",Country);
            jObj_entity.put("District",District);
            jObj_entity.put("Taluka",Taluka);
            jObj_entity.put("FKTerritoryId",FKTerritoryId);
            jObj_entity.put("Website",Website);
            jObj_entity.put("PriceListId",PriceListId);
            jObj_entity.put("vendorCode",vendorCode);

            jObj_entity.put("ENRect","Y");
            jObj_entity.put("ENInv","Y");
            jObj_entity.put("ENPndPO",ENPndPO);
            jObj_entity.put("EnterpriseType",EnterpriseType);
            jObj_entity.put("CustVendor","C");       //C

            jObj_entity.put("IsWLForCRMRef","Y");     //Y
            jObj_entity.put("Active","true");
            jObj_entity.put("IsActive",IsActive);
            jObj_entity.put("CountryId",CountryId);
            jObj_entity.put("ENGRN",ENGRN);
            jObj_entity.put("ENPymt",ENPymt);
            jObj_entity.put("CreditLimit",CreditLimit);
            jObj_entity.put("CreditDays",CreditDays);
            jObj_entity.put("Currency",Currency);       //pass currency id
            jObj_entity.put("PaymentTerms",PaymentTerms);
            jObj_entity.put("DeliveryTerms",DeliveryTerms);
            jObj_entity.put("TaxClass",TaxClass);
            jObj_entity.put("SlCatId",SlCatId);
            jObj_entity.put("ResellerName",ResellerName);
            jObj_entity.put("CreditTerms",CreditTerms);
            jObj_entity.put("PayeeName",PayeeName);
            jObj_entity.put("BankName",BankName);
            jObj_entity.put("Branch",Branch);
            jObj_entity.put("BankAddress",BankAddress);
            jObj_entity.put("AccountNo",AccountNo);
            jObj_entity.put("AccountType",AccountType);
            jObj_entity.put("IFSCode",IFSCode);
            jObj_entity.put("RemittanceInstruction",RemittanceInstruction);

            jObj_entity.put("EvaluationDt",EvaluationDt);
            jObj_entity.put("ValidFrom",ValidFrom);
            jObj_entity.put("ValidTo",ValidTo);

            jObj_entity.put("SystemUserId",SystemUserId);
            jObj_entity.put("IsApproved",IsApproved);
            jObj_entity.put("IsContractReqd",IsContractReqd);
            /*jObj_entity.put("Currency",Currency);       //
            jObj_entity.put("PaymentTerms",PaymentTerms);
            jObj_entity.put("DeliveryTerms",DeliveryTerms);*/
            jObj_entity.put("CurrencyMasterID",CurrencyMasterID);   //pass currencymasterId

            jObj_entity.put("EntityGroupMasterId",EntityGroupMasterId); //Customer = 1, Supplier = 2, subcontractor = 3, only finance = 4
            jObj_entity.put("EntityClass",EntityClass);         //Customer = 1, Supplier = 2, subcontractor = 3, only finance = 4
            jObj_entity.put("CustVendorType",CustVendorType);       //pass EntitytypeMasterId

            jObj_entity.put("Typeofservices",Typeofservices);
            jObj_entity.put("ServClId",ServClId);
            jObj_entity.put("PANNO",PANNO);
            jObj_entity.put("GSTNO",GSTNO);
            jObj_entity.put("TAN_GSTIN_Number",TAN_GSTIN_Number);
            jObj_entity.put("TANNO",TANNO);
            jObj_entity.put("CSTNo",CSTNo);
            jObj_entity.put("MSTNo",MSTNo);
            jObj_entity.put("ECCNo",ECCNo);
            jObj_entity.put("ServiceTaxNo",ServiceTaxNo);
            jObj_entity.put("ExDivi",ExDivi);
            jObj_entity.put("ExRange",ExRange);
            jObj_entity.put("CAT",CAT);
            jObj_entity.put("AccountId",AccountId);

            jObj_entity.put("EsicNo",EsicNo);
            jObj_entity.put("AadharNo",AadharNo);
            jObj_entity.put("PFNo",PFNo);
            jObj_entity.put("CIN",CIN);
            jObj_entity.put("ValidFrom",ValidFrom);
            jObj_entity.put("ValidTo",ValidTo);
            jObj_entity.put("EvaluationDt",EvaluationDt);
            jObj_entity.put("EntityRestDate",EntityRestDate);
            jObj_entity.put("ClientDetails",/*ClientDetails*/jsonArray_client.toString());
            jObj_entity.put("ShipToDetails",ShipToDetails);
            jObj_entity.put("SalesFamily",SalesFamily);
            jObj_entity.put("LenderDetails",LenderDetails);
            jObj_entity.put("InsuranceDetails",InsuranceDetails);
            jObj_entity.put("BankPayeeName",BankPayeeName);
            jObj_entity.put("ExpertiseDetails",ExpertiseDetails);
            jObj_entity.put("Latitude",Latitude);
            jObj_entity.put("Longitude",Longitude);
            jObj_entity.put("VendorMasterID",VendorMasterID);

            // for blends
            jObj_entity.put("TenorYear",TenorYear);
            jObj_entity.put("IndIndemnity","N");

            entity_JSON = jObj_entity;

        } catch (JSONException e) {
            e.printStackTrace();
            entity_JSON = null;
        }
        return entity_JSON;
    }

    class PostAddNewEntityAPI extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;
        String so_no = "", ItemCode = "", ItemDesc = "", AddedDt = "", scheduledate = "", ScheduleQty = "", ShipmentQty = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_addNewEntityPost;

                res = ut.OpenPostConnection(url,finalData,parent);
                response = res.toString().replaceAll("\\\\", "");
                response = response.toString().replaceAll("^\"+ \"+$","");

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
              mprogress.setVisibility(View.GONE);

            try{
                if(!response.equalsIgnoreCase(null) || !response.equalsIgnoreCase("Error")){
                    Toast.makeText(parent,"Customer data added successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }else if(response.equalsIgnoreCase("Error")){
                    Toast.makeText(parent,"Sorry, server error! failed to add customer data.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public String getCurrentDate(){

        String evalDate = "", validFromdate = "", validToDate = "";

        Calendar cal_valfrom = Calendar.getInstance();
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat(" , hh:mm a");    //Thu Jul 18 17:33:55 GMT+05:30 2019
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        Date d1= null;
        String validFrom = String.valueOf(cal_valfrom.getTime());
        try {
            d1 = sdf.parse(validFrom);
            validFrom = Format.format(d1);
            System.out.println(validFrom);

        } catch (ParseException e) {
            e.printStackTrace();
            validFrom = "";
        }

        Calendar cal_valto = Calendar.getInstance();
        cal_valto.add(Calendar.MONTH, 1);
        String dt_valto = String.valueOf(cal_valto.getTime());
        try {
            d1 = sdf.parse(dt_valto);
            dt_valto = Format.format(d1);
            System.out.println(dt_valto);

        } catch (ParseException e) {
            e.printStackTrace();
            dt_valto = "";
        }

        Calendar cal_evaldt = Calendar.getInstance();
        cal_evaldt.add(Calendar.YEAR, 1);
        String dt_evaldt = String.valueOf(cal_evaldt.getTime());
        try {
            d1 = sdf.parse(dt_evaldt);
            dt_evaldt = Format.format(d1);
            System.out.println(dt_evaldt);

        } catch (ParseException e) {
            e.printStackTrace();
            dt_evaldt = "";
        }

        String eval_from_to = dt_evaldt + "," + validFrom + "," + dt_valto;

        return eval_from_to;

    }

}