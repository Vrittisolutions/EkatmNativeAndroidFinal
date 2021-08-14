package com.vritti.crmlib.vcrm7;


import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.BusinessSegmentbean;
import com.vritti.crmlib.bean.CityBean;
import com.vritti.crmlib.bean.Firmbean;
import com.vritti.crmlib.bean.ProspectContact;
import com.vritti.crmlib.bean.ProspectsourceBean;
import com.vritti.crmlib.bean.Teritorybean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.services.SendOfflineData;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

/**
 * Created by sharvari on 02-Jun-17.
 */

public class BusinessProspectusActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    public  static Context context;

    private String Territoryid = "", Cityid = "", BusDetailid = "",
            SuSpSourceId = "0",  Referenceid = "", Currencyid = "", Productid = "",
            Stateid = "", Countryid = "";

    AutoCompleteTextView efName1;
    LinearLayout ln_efName1, ln_city, ln_state, ln_country, ln_territory, ln_businessSegment,
            ln_source, ln_reference, ln_currency, ln_Product;
    LinearLayout ln_village, ln_District, ln_sex, ln_val1, ln_val2, ln_val3,
            ln_val4, ln_val5, ln_val6, ln_val7, ln_val8, ln_val9, ln_val10;
    TextView tv_village, tv_District, tv_sex, tv_val1, tv_val2, tv_val3,
            tv_val4, tv_val5, tv_val6, tv_val7, tv_val8, tv_val9, tv_val10;

    TextView tv_city, tv_state, tv_country, tv_territory, tv_bussinesssegment, tv_currency,
            tv_product, tv_sourceofprospect, tv_reference;
    Dialog dialog;

    SearchableSpinner spinner_village, spinner_District, spinner_sex, spinner_val1, spinner_val2, spinner_val3,
            spinner_val4, spinner_val5, spinner_val6, spinner_val7, spinner_val8, spinner_val9, spinner_val10;
    EditText edt_village, edt_District, edt_sex, edt_val1, edt_val2, edt_val3,
            edt_val4, edt_val5, edt_val6, edt_val7, edt_val8, edt_val9, edt_val10;
    EditText edt_website;
    EditText edt_contact_name_person, edt_address, edt_mobile, edt_telephone, edt_email, edt_whatsaap;

    SearchableSpinner spinner_city, spinner_state, spinner_country, spinner_territory, spinner_BusinessSegment, spinner_source,
            spinner_reference, spinner_refrenceoption,
            spinner_product, spinner_currency;

    private static Boolean isMadatory_prospectname = false, isMadatory_Address = false, isMadatory_City = false,
            isMadatory_State = false, isMadatory_Country = false, isMadatory_Territory = false,
            isMadatory_BusinessSegment = false, isMadatory_Website = false, isMadatory_SourceofProspect = false,
            isMadatory_Selectreference = false, isMadatory_Contact = false, isMadatory_EmailID = false,
            isMadatory_Mobile = false, isMadatory_WhatsappNo = false, isMadatory_Telephone = false,
            isMadatory_ProductSalesFamily = false,
            isMadatory_Currency = false;

    private static Boolean isMadatory_village = false, isMadatory_district = false, isMadatory_sex = false,
            isMadatory_val1 = false, isMadatory_val2 = false, isMadatory_val3 = false,
            isMadatory_val4 = false, isMadatory_val5 = false, isMadatory_val6 = false,
            isMadatory_val7 = false, isMadatory_val8 = false, isMadatory_val9 = false,
            isMadatory_val10 = false;

    private static Boolean isVisible_village = true, isVisible_district = true, isVisible_sex = true,
            isVisible_val1 = true, isVisible_val2 = true, isVisible_val3 = true,
            isVisible_val4 = true, isVisible_val5 = true, isVisible_val6 = true,
            isVisible_val7 = true, isVisible_val8 = true, isVisible_val9 = true,
            isVisible_val10 = true;


    private static Boolean isVisible_prospectname = true, isVisible_Address = true, isVisible_City = true,
            isVisible_State = true, isVisible_Country = true, isVisible_Territory = true,
            isVisible_BusinessSegment = true, isVisible_Website = true, isVisible_SourceofProspect = true,
            isVisible_Selectreference = true, isVisible_Contact = true,
            isVisible_EmailID = true, isVisible_Mobile = true, isVisible_WhatsappNo = true, isVisible_Telephone = true,
            isVisible_ProductSalesFamily = true,
            isVisible_Currency = true;
    private static String string_city = "", string_country = "", string_state = "",
            string_territory = "", string_BusinessSegment = "", string_Source = "", string_reference = "",
            string_product_salesfamily = "", string_currency = "", string_refrenceoption = "";

    Button buttonSave_prospect, buttonClose_prospect, buttonSaveandstartcall_prospect;
    //SharedPreferences userpreferences;
    SharedPreferences Prospectpreference;
    private static String ProspectTypeID;

    SQLiteDatabase sql;
    ProgressBar progress_bar;
    String custvid = "", reftypeid = "", finaljson;
    private PopupWindow pw;
    public static List<Firmbean> lstFirm = new ArrayList<Firmbean>();
    List<String> lstCity = new ArrayList<String>();
    List<String> lstTerrority = new ArrayList<String>();
    List<String> lstSourceProspect = new ArrayList<String>();
    List<String> lstBusinessSegment = new ArrayList<String>();
    List<String> lstReferenceType = new ArrayList<String>();
    List<String> lstState = new ArrayList<String>();
    List<String> lstReference = new ArrayList<String>();
    List<String> lstTurnover = new ArrayList<String>();
    ArrayList<String> Productionitems = new ArrayList<String>();
    ProgressBar progressbar_business, proress_reference;
    public static boolean[] checkSelected;
    AlertDialog alertDialog;
    String[] prod;
    public List<String> lstProduct = new ArrayList<String>();
    private String contact, product;
    int i = 0;
    String[] susmaster;
    String PKSuspectId;
    public static List<ProspectContact> lstContact = new ArrayList<ProspectContact>();
    private String Contact_Depatment;
    private String Product_name;
    private String City_name, Territory_name, Source_prospect, BusDetail_Segment_name, Tan_name;
    private String CustomerName = "", ContactName = "", ContactNumber = "", registeryID = "", Email = "", EnaquiryDetail = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_business_lay);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = BusinessProspectusActivity.this;

        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_Business, "");
        if (ProspectTypeID.equalsIgnoreCase("")) {
            ProspectTypeID = "2";
        }


        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        init();
        Intent intent = getIntent();
        PKSuspectId = intent.getStringExtra("PKSuspectId");
        if (PKSuspectId != null) {
            getcontactfetchdetails();
            getproductfetchdetails();
            getremainingdata();
        }

        if (intent.hasExtra("registryID")) {
            CustomerName = intent.getStringExtra("custname");
            ContactName = intent.getStringExtra("contactname");
            ContactNumber = intent.getStringExtra("contactnumber");
            registeryID = intent.getStringExtra("registryID");
            Email = intent.getStringExtra("email");
            EnaquiryDetail = intent.getStringExtra("enquirydetail");
            efName1.setText(CustomerName);
            efName1.setSelection(efName1.length());

            edt_contact_name_person.setText(ContactName);
            edt_contact_name_person.setSelection(edt_contact_name_person.length());
            edt_mobile.setText(ContactNumber);
            edt_mobile.setSelection(edt_mobile.length());
            edt_email.setText(Email);
            edt_email.setSelection(edt_email.length());

        } else {
            CustomerName = "";
            ContactName = "";
            ContactNumber = "";
            registeryID = "";
            Email = "";
            EnaquiryDetail = "";
        }
        if (CheckValidation()) {
            applyValidation();
        } else {
            validationAsync();
        }
        if (cf.getReferencetypecount() > 0) {
            getReferencetype();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadReferencetypeJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (cf.check_City() > 0) {
            getCity();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCityJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (cf.getStatecount() > 0) {
            getState();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadStatelistJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (cf.getCountrycount() > 0) {
            getCountry();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
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

        if (cf.check_Teritory() > 0) {
            setautocomplete_teritory();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadterritoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }


        if (cf.check_BusinessSegment() > 0) {
            setautocomplete_BusinessSegment();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadBusinesssegmentJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (cf.getCurrencycount() > 0) {
            getCurrency();


        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCurrencyMasterJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (cf.getProuctcount() > 0) {
            getproduct();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadProductJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (cf.check_source() > 0) {
            setautocomplete_prospect();
        } else {
            if (isnet()) {
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadSourceofProspectJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


    }

    private void init() {
        ln_efName1 = (LinearLayout) findViewById(R.id.ln_efName1);
        efName1 = (AutoCompleteTextView) findViewById(R.id.efName1);
        edt_contact_name_person = (EditText) findViewById(R.id.edt_contact_name_person);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_whatsaap = (EditText) findViewById(R.id.edt_whatsaap);
        edt_telephone = (EditText) findViewById(R.id.edt_telephone);
        ln_city = (LinearLayout) findViewById(R.id.ln_city);
        spinner_city = (SearchableSpinner) findViewById(R.id.eAutoCity);
        ln_state = (LinearLayout) findViewById(R.id.ln_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        ln_country = (LinearLayout) findViewById(R.id.ln_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        ln_territory = (LinearLayout) findViewById(R.id.ln_country);
        spinner_territory = (SearchableSpinner) findViewById(R.id.eAutoTerritory);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_website = (EditText) findViewById(R.id.edt_website);
        ln_businessSegment = (LinearLayout) findViewById(R.id.ln_businessSegment);
        spinner_BusinessSegment = (SearchableSpinner) findViewById(R.id.autotxtBusinessSegment);
        ln_currency = (LinearLayout) findViewById(R.id.ln_currency);
        spinner_currency = (SearchableSpinner) findViewById(R.id.sCurrency);

        ln_Product = (LinearLayout) findViewById(R.id.ln_Product);
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);
        ln_source = (LinearLayout) findViewById(R.id.ln_sourceofprospect);
        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_sourceofprospect);

        ln_reference = (LinearLayout) findViewById(R.id.ln_reference);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progressbar_business = (ProgressBar) findViewById(R.id.progressbar_business);
        proress_reference = (ProgressBar) findViewById(R.id.proress_reference);
        spinner_reference = (SearchableSpinner) findViewById(R.id.sReferenceType);
        spinner_refrenceoption = (SearchableSpinner) findViewById(R.id.sReference);

        buttonSave_prospect = (Button) findViewById(R.id.buttonSave_prospect);
        buttonClose_prospect = (Button) findViewById(R.id.buttonClose_prospect);
        buttonSaveandstartcall_prospect = (Button) findViewById(R.id.buttonSaveandstartcall_prospect);

        spinner_reference.setSelection(0);
        spinner_refrenceoption.setSelection(0);
        spinner_state.setSelection(0);
        spinner_product.setSelection(0);

        spinner_state.setTitle("Select State");
        spinner_product.setTitle("Sales Family");
        spinner_country.setTitle("Select Country");
        spinner_city.setTitle("Select City");
        spinner_territory.setTitle("Select Territory ");
        spinner_source.setTitle("Select Source of Prospect ");
        spinner_BusinessSegment.setTitle("Select Option");


        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_territory = (TextView) findViewById(R.id.tv_territory);
        tv_bussinesssegment = (TextView) findViewById(R.id.tv_businessSegment);
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        tv_product = (TextView) findViewById(R.id.tv_product);
        tv_sourceofprospect = (TextView) findViewById(R.id.tv_sourceofprospect);
        tv_reference = (TextView) findViewById(R.id.tv_reference);


        ln_village = (LinearLayout) findViewById(R.id.ln_village);
        tv_village = (TextView) findViewById(R.id.tv_village);
        spinner_village = (SearchableSpinner) findViewById(R.id.spinner_village);
        edt_village = (EditText) findViewById(R.id.edt_village);

        ln_District = (LinearLayout) findViewById(R.id.ln_District);
        tv_District = (TextView) findViewById(R.id.tv_District);
        spinner_District = (SearchableSpinner) findViewById(R.id.spinner_District);
        edt_District = (EditText) findViewById(R.id.edt_District);

        ln_sex = (LinearLayout) findViewById(R.id.ln_sex);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        spinner_sex = (SearchableSpinner) findViewById(R.id.spinner_sex);
        edt_sex = (EditText) findViewById(R.id.edt_sex);

        ln_val1 = (LinearLayout) findViewById(R.id.ln_val1);
        tv_val1 = (TextView) findViewById(R.id.tv_val1);
        spinner_val1 = (SearchableSpinner) findViewById(R.id.spinner_val1);
        edt_val1 = (EditText) findViewById(R.id.edt_val1);

        ln_val2 = (LinearLayout) findViewById(R.id.ln_val2);
        tv_val2 = (TextView) findViewById(R.id.tv_val2);
        spinner_val2 = (SearchableSpinner) findViewById(R.id.spinner_val2);
        edt_val2 = (EditText) findViewById(R.id.edt_val2);


        ln_val3 = (LinearLayout) findViewById(R.id.ln_val3);
        tv_val3 = (TextView) findViewById(R.id.tv_val3);
        spinner_val3 = (SearchableSpinner) findViewById(R.id.spinner_val3);
        edt_val3 = (EditText) findViewById(R.id.edt_val3);

        ln_val4 = (LinearLayout) findViewById(R.id.ln_val4);
        tv_val4 = (TextView) findViewById(R.id.tv_val4);
        spinner_val4 = (SearchableSpinner) findViewById(R.id.spinner_val4);
        edt_val4 = (EditText) findViewById(R.id.edt_val4);

        ln_val5 = (LinearLayout) findViewById(R.id.ln_val5);
        tv_val5 = (TextView) findViewById(R.id.tv_val5);
        spinner_val5 = (SearchableSpinner) findViewById(R.id.spinner_val5);
        edt_val5 = (EditText) findViewById(R.id.edt_val5);

        ln_val6 = (LinearLayout) findViewById(R.id.ln_val6);
        tv_val6 = (TextView) findViewById(R.id.tv_val6);
        spinner_val6 = (SearchableSpinner) findViewById(R.id.spinner_val6);
        edt_val6 = (EditText) findViewById(R.id.edt_val6);

        ln_val7 = (LinearLayout) findViewById(R.id.ln_val7);
        tv_val7 = (TextView) findViewById(R.id.tv_val7);
        spinner_val7 = (SearchableSpinner) findViewById(R.id.spinner_val7);
        edt_val7 = (EditText) findViewById(R.id.edt_val7);

        ln_val8 = (LinearLayout) findViewById(R.id.ln_val8);
        tv_val8 = (TextView) findViewById(R.id.tv_val8);
        spinner_val8 = (SearchableSpinner) findViewById(R.id.spinner_val8);
        edt_val8 = (EditText) findViewById(R.id.edt_val8);

        ln_val9 = (LinearLayout) findViewById(R.id.ln_val9);
        tv_val9 = (TextView) findViewById(R.id.tv_val9);
        spinner_val9 = (SearchableSpinner) findViewById(R.id.spinner_val9);
        edt_val9 = (EditText) findViewById(R.id.edt_val9);

        ln_val10 = (LinearLayout) findViewById(R.id.ln_val10);
        tv_val10 = (TextView) findViewById(R.id.tv_val10);
        spinner_val10 = (SearchableSpinner) findViewById(R.id.spinner_val10);
        edt_val10 = (EditText) findViewById(R.id.edt_val10);

        setSpinner();


        edt_contact_name_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (efName1.getText().toString().length() < 6) {
                    efName1.setError("Please enter minimum 6 character");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        efName1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    final String pass = s.toString();


                    if (isnet()) {
                        new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadFirmJSON().execute(pass);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {

                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_currency = (String) spinner_currency.getSelectedItem();
                String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                        " FROM " + db.TABLE_CurrencyMaster +
                        " WHERE CurrDesc='" + string_currency + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Currencyid = cur.getString(cur.getColumnIndex("CurrencyMasterId"));

                    } while (cur.moveToNext());

                } else {
                    Currencyid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_product_salesfamily = (String) spinner_product.getSelectedItem();
                String query = "SELECT distinct FamilyDesc,FamilyId" +
                        " FROM " + db.TABLE_SALES_FAMILY_PRODUCT +
                        " WHERE FamilyDesc='" + string_product_salesfamily + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Productid = cur.getString(cur.getColumnIndex("FamilyId"));

                    } while (cur.moveToNext());

                } else {
                    Productid = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_state = (String) spinner_state.getSelectedItem();
                String query = "SELECT distinct StateDesc,PKStateId" +
                        " FROM " + db.TABLE_STATE +
                        " WHERE StateDesc='" + string_state + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Stateid = cur.getString(cur.getColumnIndex("PKStateId"));

                    } while (cur.moveToNext());

                } else {
                    Stateid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_country = (String) spinner_country.getSelectedItem();
                String query = "SELECT *" +
                        " FROM " + db.TABLE_COUNTRY +
                        " WHERE CountryName='" + string_country + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        Countryid = cur.getString(cur.getColumnIndex("PKCountryId"));
                    } while (cur.moveToNext());
                } else {
                    Countryid = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_territory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_territory = (String) spinner_territory.getSelectedItem();
                String query = "SELECT distinct TerritoryName,PKTerritoryId" +
                        " FROM " + db.TABLE_Teritory +
                        " WHERE TerritoryName='" + string_territory + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Territoryid = cur.getString(cur.getColumnIndex("PKTerritoryId"));

                    } while (cur.moveToNext());

                } else {
                    Territoryid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_reference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reftypeid = "";
                string_reference = (String) spinner_reference.getSelectedItem();
                String query = "SELECT distinct CustVendor,CustVendorCode" +
                        " FROM " + db.TABLE_Referencetype +
                        " WHERE CustVendorCode='" + string_reference + "'";
                Cursor cur = sql.rawQuery(query, null);
                //  lstEntity.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        reftypeid = cur.getString(cur.getColumnIndex("CustVendor"));


                    } while (cur.moveToNext());

                }


                if (isnet()) {
                    new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReferenceJSON().execute(reftypeid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_refrenceoption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_refrenceoption = (String) spinner_refrenceoption.getSelectedItem();
                String query = "SELECT distinct CustVendorMasterId,CustVendorName" +
                        " FROM " + db.TABLE_Reference +
                        " WHERE CustVendorName='" + spinner_refrenceoption.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                // lstReference.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        Referenceid = cur.getString(cur.getColumnIndex("CustVendorMasterId"));
                        System.out.println("Reference :" + Referenceid);
                    } while (cur.moveToNext());

                } else {
                    Referenceid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_city = (String) spinner_city.getSelectedItem();
                String query = "SELECT distinct CityName,PKCityID" +
                        " FROM " + db.TABLE_CITY +
                        " WHERE CityName='" + string_city + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Cityid = cur.getString(cur.getColumnIndex("PKCityID"));

                    } while (cur.moveToNext());

                } else {
                    Cityid = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_Source = (String) spinner_source.getSelectedItem();
                String query = "SELECT distinct SourceName,PKSuspSourceId" +
                        " FROM " + db.TABLE_Prospectsource +
                        " WHERE SourceName='" + string_Source + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        SuSpSourceId = cur.getString(cur.getColumnIndex("PKSuspSourceId"));

                    } while (cur.moveToNext());

                } else {
                    SuSpSourceId = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_BusinessSegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_BusinessSegment = (String) spinner_BusinessSegment.getSelectedItem();
                String query = "SELECT distinct SegmentDescription,PKBusiSegmentID" +
                        " FROM " + db.TABLE_Business_segment +
                        " WHERE SegmentDescription='" + string_BusinessSegment + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        BusDetailid = cur.getString(cur.getColumnIndex("PKBusiSegmentID"));

                    } while (cur.moveToNext());

                } else {
                    BusDetailid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonSaveandstartcall_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        jsoncontact.put("ContactName", edt_contact_name_person.getText().toString());
                        jsoncontact.put("Designation", "");
                        jsoncontact.put("EmailId", edt_email.getText().toString());
                        jsoncontact.put("Mobile", edt_mobile.getText().toString());
                        jsoncontact.put("Telephone", edt_telephone.getText().toString());
                        jsoncontact.put("DateofBirth", "1900/01/01");
                        jsoncontact.put("ContactPersonDept", "");
                        jsoncontact.put("Fax", "");
                        jsoncontact.put("AnniversaryDate", "1900/01/01");
                        jsoncontact.put("WhatsAppNo", edt_whatsaap.getText().toString());

                        contact = jsoncontact.toString();

                        System.out.println("Contact list : " + jsoncontact.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                   /* Product details-

                            Product[k] = { FKProductId: v.FKProductId };*/
                    JSONObject jsonProduct = new JSONObject();
                    try {


                        jsonProduct.put("FKProductId", Productid);

                        product = jsonProduct.toString();


                    } catch (Exception e) {

                    }

                    susmaster = new String[5];
                    JSONObject jsonBusinessprospect = new JSONObject();

                    try {
                        if (PKSuspectId != null) {
                            jsonBusinessprospect.put("PKSuspectId", PKSuspectId);
                        } else {
                            jsonBusinessprospect.put("PKSuspectId", null);
                        }
                        jsonBusinessprospect.put("FirmName", efName1.getText().toString());
                        jsonBusinessprospect.put("Address", edt_address.getText().toString());
                        jsonBusinessprospect.put("FirmAlias", "");
                        jsonBusinessprospect.put("FKCityId", Cityid);
                        jsonBusinessprospect.put("FKTerritoryId", Territoryid);
                        jsonBusinessprospect.put("FKBusiSegmentId", BusDetailid);
                        jsonBusinessprospect.put("CompanyURL", CompanyURL);
                        jsonBusinessprospect.put("FKEnqSourceId", SuSpSourceId);
                        jsonBusinessprospect.put("Fax", "");
                        jsonBusinessprospect.put("Notes", "");
                        jsonBusinessprospect.put("Remark", "");
                        jsonBusinessprospect.put("Department", "");
                        jsonBusinessprospect.put("BusinessDetails", BusDetailid);
                        jsonBusinessprospect.put("CurrencyMasterId", Currencyid);
                        jsonBusinessprospect.put("CurrencyDesc", string_currency);
                        jsonBusinessprospect.put("Turnover", "");
                        jsonBusinessprospect.put("NoOfEmployees", "");
                        jsonBusinessprospect.put("NoOfOffices", "");
                        jsonBusinessprospect.put("LeadGivenBYId", Referenceid);
                        jsonBusinessprospect.put("FKConsigneeId", "");
                        jsonBusinessprospect.put("FKCustomerId", "");
                        jsonBusinessprospect.put("EntityType", reftypeid);
                        jsonBusinessprospect.put("PBT", "");
                        jsonBusinessprospect.put("Rating", "");
                        jsonBusinessprospect.put("Network", "");
                        jsonBusinessprospect.put("Borrowings", "");
                        jsonBusinessprospect.put("FKStateId", Stateid);
                        jsonBusinessprospect.put("FKCountryId", Countryid);
                        jsonBusinessprospect.put("GSTState", "");
                        jsonBusinessprospect.put("GSTCode", "");
                        jsonBusinessprospect.put("TANNo", "");
                        jsonBusinessprospect.put("TANNoName", "");
                        jsonBusinessprospect.put("ProspectType", ProspectTypeID);//URLEncoder.encode("Small Business","UTF-8")


                        String sex = "", district = "", village = "",
                                val1 = "", val2 = "", val3 = "", val4 = "",
                                val5 = "", val6 = "", val7 = "", val8 = "", val9 = "",
                                val10 = "";

                        if (spinner_village.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_village.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                village = "Y";
                            } else {
                                village = "N";
                            }
                        } else {
                            village = edt_village.getText().toString();
                        }

                        if (spinner_District.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_District.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                district = "Y";
                            } else {
                                district = "N";
                            }
                        } else {
                            district = edt_District.getText().toString();
                        }
                        if (spinner_sex.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_sex.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                sex = "Y";
                            } else {
                                sex = "N";
                            }
                        } else {
                            sex = edt_sex.getText().toString();
                        }
                        if (spinner_val1.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val1.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val1 = "Y";
                            } else {
                                val1 = "N";
                            }
                        } else {
                            val1 = edt_val1.getText().toString();
                        }

                        if (spinner_val2.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val2.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val2 = "Y";
                            } else {
                                val2 = "N";
                            }
                        } else {
                            val2 = edt_val2.getText().toString();
                        }
                        if (spinner_val3.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val3.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val3 = "Y";
                            } else {
                                val3 = "N";
                            }
                        } else {
                            val3 = edt_val3.getText().toString();
                        }
                        if (spinner_val4.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val4.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val4 = "Y";
                            } else {
                                val4 = "N";
                            }
                        } else {
                            val4 = edt_val4.getText().toString();
                        }
                        if (spinner_val5.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val5.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val5 = "Y";
                            } else {
                                val5 = "N";
                            }
                        } else {
                            val5 = edt_val5.getText().toString();
                        }
                        if (spinner_val6.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val6.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val6 = "Y";
                            } else {
                                val6 = "N";
                            }
                        } else {
                            val6 = edt_val6.getText().toString();
                        }
                        if (spinner_val7.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val7.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val7 = "Y";
                            } else {
                                val7 = "N";
                            }
                        } else {
                            val7 = edt_val7.getText().toString();
                        }
                        if (spinner_val8.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val8.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val8 = "Y";
                            } else {
                                val8 = "N";
                            }
                        } else {
                            val8 = edt_val8.getText().toString();
                        }
                        if (spinner_val9.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val9.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val9 = "Y";
                            } else {
                                val9 = "N";
                            }
                        } else {
                            val9 = edt_val9.getText().toString();
                        }
                        if (spinner_val10.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val10.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val10 = "Y";
                            } else {
                                val10 = "N";
                            }
                        } else {
                            val9 = edt_val10.getText().toString();
                        }


                        jsonBusinessprospect.put("val1", val1);
                        jsonBusinessprospect.put("val2", val2);
                        jsonBusinessprospect.put("val3", val3);
                        jsonBusinessprospect.put("val4", val4);
                        jsonBusinessprospect.put("val5", val5);
                        jsonBusinessprospect.put("val6", val6);
                        jsonBusinessprospect.put("val7", val7);
                        jsonBusinessprospect.put("val8", val8);
                        jsonBusinessprospect.put("val9", val9);
                        jsonBusinessprospect.put("val10", val10);
                        jsonBusinessprospect.put("sex", sex);
                        jsonBusinessprospect.put("District", district);
                        jsonBusinessprospect.put("Village", village);


                    } catch (JSONException e) {

                    }

                    JSONObject jsonData = new JSONObject();

                    try {

                        JSONArray ob = new JSONArray();
                        //  for (int i = 0; i < contact.length; i++) {
                        JSONObject j = new JSONObject(jsonBusinessprospect.toString());
                        System.out.println("ArrayBusiness : " + jsonBusinessprospect.toString());
                        ob.put(j);
                        //    }

                        jsonData.put("SuspMaster", ob);

                        JSONArray obj1 = new JSONArray();
                        JSONObject a = null;

                        a = new JSONObject(contact);
                        obj1.put(a);

                        jsonData.put("SuspContactDetails", obj1);

                        JSONArray obj = new JSONArray();
                        JSONObject a2 = null;

                        a2 = new JSONObject(product);
                        obj.put(a2);

                        jsonData.put("SuspProdDetails", obj);
                        jsonData.put("EnquiryRegistryId", registeryID);

                    } catch (JSONException e) {

                    }

                    // FinalArray[0]
                    finaljson = jsonData.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");
                    if (isnet()) {
                        new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostProspectUpdate_savenstartJSON().execute(finaljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
            }
        });


        buttonSave_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    // getId();
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        jsoncontact.put("ContactName", edt_contact_name_person.getText().toString());
                        jsoncontact.put("Designation", "");
                        jsoncontact.put("EmailId", edt_email.getText().toString());
                        jsoncontact.put("Mobile", edt_mobile.getText().toString());
                        jsoncontact.put("Telephone", edt_telephone.getText().toString());
                        jsoncontact.put("DateofBirth", "1900/01/01");
                        jsoncontact.put("ContactPersonDept", "");
                        jsoncontact.put("Fax", "");
                        jsoncontact.put("AnniversaryDate", "1900/01/01");
                        jsoncontact.put("WhatsAppNo", edt_whatsaap.getText().toString());

                        contact = jsoncontact.toString();

                        System.out.println("Contact list : " + jsoncontact.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                   /* Product details-

                            Product[k] = { FKProductId: v.FKProductId };*/
                    JSONObject jsonProduct = new JSONObject();
                    try {


                        jsonProduct.put("FKProductId", Productid);

                        product = jsonProduct.toString();


                    } catch (Exception e) {

                    }

                    susmaster = new String[5];
                    JSONObject jsonBusinessprospect = new JSONObject();

                    try {
                        if (PKSuspectId != null) {
                            jsonBusinessprospect.put("PKSuspectId", PKSuspectId);
                        } else {
                            jsonBusinessprospect.put("PKSuspectId", null);
                        }
                        jsonBusinessprospect.put("FirmName", efName1.getText().toString());
                        jsonBusinessprospect.put("Address", edt_address.getText().toString());
                        jsonBusinessprospect.put("FirmAlias", "");
                        jsonBusinessprospect.put("FKCityId", Cityid);
                        jsonBusinessprospect.put("FKTerritoryId", Territoryid);
                        jsonBusinessprospect.put("FKBusiSegmentId", BusDetailid);
                        jsonBusinessprospect.put("CompanyURL", CompanyURL);
                        jsonBusinessprospect.put("FKEnqSourceId", SuSpSourceId);
                        jsonBusinessprospect.put("Fax", "");
                        jsonBusinessprospect.put("Notes", "");
                        jsonBusinessprospect.put("Remark", "");
                        jsonBusinessprospect.put("Department", "");
                        jsonBusinessprospect.put("BusinessDetails", BusDetailid);
                        jsonBusinessprospect.put("CurrencyMasterId", Currencyid);
                        jsonBusinessprospect.put("CurrencyDesc", string_currency);
                        jsonBusinessprospect.put("Turnover", "");
                        jsonBusinessprospect.put("NoOfEmployees", "");
                        jsonBusinessprospect.put("NoOfOffices", "");
                        jsonBusinessprospect.put("LeadGivenBYId", Referenceid);
                        jsonBusinessprospect.put("FKConsigneeId", "");
                        jsonBusinessprospect.put("FKCustomerId", "");
                        jsonBusinessprospect.put("EntityType", reftypeid);
                        jsonBusinessprospect.put("PBT", "");
                        jsonBusinessprospect.put("Rating", "");
                        jsonBusinessprospect.put("Network", "");
                        jsonBusinessprospect.put("Borrowings", "");
                        jsonBusinessprospect.put("FKStateId", Stateid);
                        jsonBusinessprospect.put("GSTState", "");
                        jsonBusinessprospect.put("GSTCode", "");
                        jsonBusinessprospect.put("TANNo", "");
                        jsonBusinessprospect.put("TANNoName", "");
                        jsonBusinessprospect.put("FKCountryId", Countryid);
                        jsonBusinessprospect.put("ProspectType", ProspectTypeID);


                        String sex = "", district = "", village = "",
                                val1 = "", val2 = "", val3 = "", val4 = "",
                                val5 = "", val6 = "", val7 = "", val8 = "", val9 = "",
                                val10 = "";

                        if (spinner_village.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_village.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                village = "Y";
                            } else {
                                village = "N";
                            }
                        } else {
                            village = edt_village.getText().toString();
                        }

                        if (spinner_District.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_District.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                district = "Y";
                            } else {
                                district = "N";
                            }
                        } else {
                            district = edt_District.getText().toString();
                        }
                        if (spinner_sex.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_sex.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                sex = "Y";
                            } else {
                                sex = "N";
                            }
                        } else {
                            sex = edt_sex.getText().toString();
                        }
                        if (spinner_val1.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val1.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val1 = "Y";
                            } else {
                                val1 = "N";
                            }
                        } else {
                            val1 = edt_val1.getText().toString();
                        }

                        if (spinner_val2.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val2.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val2 = "Y";
                            } else {
                                val2 = "N";
                            }
                        } else {
                            val2 = edt_val2.getText().toString();
                        }
                        if (spinner_val3.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val3.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val3 = "Y";
                            } else {
                                val3 = "N";
                            }
                        } else {
                            val3 = edt_val3.getText().toString();
                        }
                        if (spinner_val4.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val4.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val4 = "Y";
                            } else {
                                val4 = "N";
                            }
                        } else {
                            val4 = edt_val4.getText().toString();
                        }
                        if (spinner_val5.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val5.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val5 = "Y";
                            } else {
                                val5 = "N";
                            }
                        } else {
                            val5 = edt_val5.getText().toString();
                        }
                        if (spinner_val6.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val6.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val6 = "Y";
                            } else {
                                val6 = "N";
                            }
                        } else {
                            val6 = edt_val6.getText().toString();
                        }
                        if (spinner_val7.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val7.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val7 = "Y";
                            } else {
                                val7 = "N";
                            }
                        } else {
                            val7 = edt_val7.getText().toString();
                        }
                        if (spinner_val8.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val8.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val8 = "Y";
                            } else {
                                val8 = "N";
                            }
                        } else {
                            val8 = edt_val8.getText().toString();
                        }
                        if (spinner_val9.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val9.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val9 = "Y";
                            } else {
                                val9 = "N";
                            }
                        } else {
                            val9 = edt_val9.getText().toString();
                        }
                        if (spinner_val10.getVisibility() == View.VISIBLE) {
                            if (((String) spinner_val10.getSelectedItem())
                                    .equalsIgnoreCase("Yes")) {
                                val10 = "Y";
                            } else {
                                val10 = "N";
                            }
                        } else {
                            val9 = edt_val10.getText().toString();
                        }


                        jsonBusinessprospect.put("val1", val1);
                        jsonBusinessprospect.put("val2", val2);
                        jsonBusinessprospect.put("val3", val3);
                        jsonBusinessprospect.put("val4", val4);
                        jsonBusinessprospect.put("val5", val5);
                        jsonBusinessprospect.put("val6", val6);
                        jsonBusinessprospect.put("val7", val7);
                        jsonBusinessprospect.put("val8", val8);
                        jsonBusinessprospect.put("val9", val9);
                        jsonBusinessprospect.put("val10", val10);
                        jsonBusinessprospect.put("sex", sex);
                        jsonBusinessprospect.put("District", district);
                        jsonBusinessprospect.put("Village", village);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonData = new JSONObject();

                    try {

                        JSONArray ob = new JSONArray();
                        //  for (int i = 0; i < contact.length; i++) {
                        JSONObject j = new JSONObject(jsonBusinessprospect.toString());
                        System.out.println("ArrayBusiness : " + jsonBusinessprospect.toString());
                        ob.put(j);
                        //    }

                        jsonData.put("SuspMaster", ob);

                        JSONArray obj1 = new JSONArray();
                        JSONObject a = null;

                        a = new JSONObject(contact);
                        obj1.put(a);

                        jsonData.put("SuspContactDetails", obj1);

                        JSONArray obj = new JSONArray();
                        JSONObject a2 = null;

                        a2 = new JSONObject(product);
                        obj.put(a2);

                        jsonData.put("SuspProdDetails", obj);
                        jsonData.put("EnquiryRegistryId", registeryID);

                    } catch (JSONException e) {

                    }

                    // FinalArray[0]
                    finaljson = jsonData.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");
                    //   finaljson = finaljson.replaceAll(" ", "");


                    String fName = efName1.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy HH:mm");
                    String date = sdf.format(new Date());
                    String remark1 = "Promotional form Added for firm " + fName + " on" + date;
                    String url = CompanyURL + WebUrlClass.api_Post_Prospect;

                    String op = "";
                    CreateOfflineIntend(url, finaljson, WebUrlClass.POSTFLAG, remark1, op);
                  /*  if (isnet()) {
                        new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostProspectUpdateJSON().execute(finaljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }*/

                }
            }
        });


    }

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
           /* if (PKSuspectId != null) {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect Update succcessfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect added succcessfully", Toast.LENGTH_LONG).show();
            }*/
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(BusinessProspectusActivity.this,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            startService(intent1);
            Intent intent2 = new Intent(BusinessProspectusActivity.this,
                    CallListActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BusinessProspectusActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            System.out.println("BusinessAPIURL-1 :" + finaljson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("BusinessAPI-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();

            if (PKSuspectId != null) {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect Update succcessfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect added succcessfully", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }


    private void setautocomplete_BusinessSegment() {

        List<BusinessSegmentbean> lstdb = cf.getBusinessSegmentbean();
        lstBusinessSegment.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstBusinessSegment.add(lstdb.get(i).getSegmentDescription());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstBusinessSegment);
        spinner_BusinessSegment.setAdapter(customAdcity);
        int a = lstBusinessSegment.indexOf(BusDetail_Segment_name);
        spinner_BusinessSegment.setSelection(lstBusinessSegment.indexOf(BusDetail_Segment_name));


    }


    private void getCity() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstCity.add(lstdb.get(i).getCityName());
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstCity);

        spinner_city.setAdapter(customAdcity);
        int a = lstCity.indexOf(City_name);
        spinner_city.setSelection(lstCity.indexOf(City_name));


    }

    private void setautocomplete_teritory() {


        List<Teritorybean> lstdb = cf.getTeritorybean();
        lstTerrority.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstTerrority.add(lstdb.get(i).getTerritoryName());
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstTerrority);

        spinner_territory.setAdapter(customAdcity);
        int a = lstTerrority.indexOf(Territory_name);
        spinner_territory.setSelection(lstTerrority.indexOf(Territory_name));


    }


    private void setautocomplete_prospect() {

        List<ProspectsourceBean> lstdb = cf.getProspectsourceBean();
        lstSourceProspect.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstSourceProspect.add(lstdb.get(i).getSourceName());


        MySpinnerAdapter customAdcity = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstSourceProspect);
        spinner_source.setAdapter(customAdcity);
        int a = lstSourceProspect.indexOf(Source_prospect);
        spinner_source.setSelection(lstSourceProspect.indexOf(Source_prospect));


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BusinessProspectusActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuprospectsetting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.actionSetting) {
            validationAsync();

        } else if (id == R.id.refresh) {
            if (isnet()) {
                showProgressDialog();
                new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dismissProgressDialog();

                        new DownloadCityJSON().execute();
                        new DownloadStatelistJSON().execute();
                        new DownloadterritoryJSON().execute();
                        new DownloadSourceofProspectJSON().execute();
                        new DownloadProductJSON().execute();
                        new DownloadCurrencyMasterJSON().execute();
                        new DownloadBusinesssegmentJSON().execute();
                        new DownloadReferencetypeJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgressDialog();
                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_menu)

        {
            dialog = new Dialog(BusinessProspectusActivity.this);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.crm_dialog_prospectbussiness_lay);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView txt_enterprise_prospect = (TextView) dialog.findViewById(R.id.txt_enterprise_prospect);
            TextView txt_individual_prospect = (TextView) dialog.findViewById(R.id.txt_individual_prospect);

            txt_enterprise_prospect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (registeryID.equalsIgnoreCase("")) {
                        startActivity(new Intent(BusinessProspectusActivity.this, ProspectEnterpriseActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(BusinessProspectusActivity.this, ProspectEnterpriseActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("PKSuspectId", PKSuspectId);
                        i.putExtra("custname", CustomerName);
                        i.putExtra("contactname", ContactName);
                        i.putExtra("contactnumber", ContactNumber);
                        i.putExtra("registryID", registeryID);
                        i.putExtra("email", Email);
                        i.putExtra("enquirydetail", EnaquiryDetail);
                        startActivity(i);
                    }

                    dialog.dismiss();
                }
            });
            txt_individual_prospect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (registeryID.equalsIgnoreCase("")) {
                        startActivity(new Intent(BusinessProspectusActivity.this, IndividualProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(BusinessProspectusActivity.this, IndividualProspectusActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("PKSuspectId", PKSuspectId);
                        i.putExtra("custname", CustomerName);
                        i.putExtra("contactname", ContactName);
                        i.putExtra("contactnumber", ContactNumber);
                        i.putExtra("registryID", registeryID);
                        i.putExtra("email", Email);
                        i.putExtra("enquirydetail", EnaquiryDetail);
                        startActivity(i);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
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

    class DownloadFirmJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response, name, id;
        String a[], b[];
        List<String> suggestions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            suggestions = new ArrayList<String>();
            try {
                String url = CompanyURL + WebUrlClass.api_get_Firm +
                        "?SearchText=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString();
                response = response.substring(1, response.length() - 1);
                response = response.substring(1, response.length() - 1);
                a = response.split("\"");
                sql.delete(db.TABLE_Firm, null,
                        null);
                for (int i = 0; i < a.length; i++) {
                    if (a[i].equalsIgnoreCase(",")) {

                    } else {
                        b = a[i].split(",");
                        name = b[0];
                        id = b[1];
                        Firmbean firmbean = new Firmbean();
                        firmbean.setName(name);
                        firmbean.setId(id);
                        lstFirm.add(firmbean);
                        cf.addfirm(id, name);
                        suggestions.add(name);
                        Log.d("test", "" + b[0] + "," + b[1]);
                    }
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        //   progressbar.setVisibility(View.GONE);
                        MySpinnerAdapter customAdcity = new MySpinnerAdapter(BusinessProspectusActivity.this,
                                R.layout.crm_custom_spinner_txt, suggestions);


                        ((AutoCompleteTextView) findViewById(R.id.efName1))
                                .setAdapter(customAdcity);
                        customAdcity.notifyDataSetChanged();
                        ((AutoCompleteTextView) findViewById(R.id.efName1)).setThreshold(1);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progress_bar.setVisibility(View.GONE);
           /* if (progressHUD6 != null && progressHUD6.isShowing()) {
                progressHUD6.dismiss();
            }*/
            // setautocomplete();


        }

    }

    //Prospectus ReferenceType

    class DownloadReferencetypeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);

            proress_reference.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_Referencetype;

            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_Referencetype, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Referencetype, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_Referencetype, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
         /*   if (progressHUD1 != null && progressHUD1.isShowing()) {
                progressHUD1.dismiss();
            }*/
            //  dismissProgressDialog();

            proress_reference.setVisibility(View.GONE);
            if (response.contains("")) {

            }

            getReferencetype();
        }

    }


    //Prospect Reference Code

    class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            proress_reference.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Reference +
                        "?LeadWise=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_Reference, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Reference, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_Reference, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
           /* if (progressHUD2 != null && progressHUD2.isShowing()) {
                progressHUD2.dismiss();
            }*/
            //     dismissProgressDialog();
           /* if (response.contains("")) {

            }*/
            proress_reference.setVisibility(View.GONE);
            getReference();
        }

    }

    class DownloadSourceofProspectJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response, name, id;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_ProspectSource;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("(\r\n|\n)", "");

                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_Prospectsource, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Prospectsource, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        String jsonAddeddt = jorder.getString("AddedDt");
                        String jsonModifiedDt = jorder.getString("ModifiedDt");
                        if (columnName.equalsIgnoreCase("AddedDt")) {
                            jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonAddeddt);
                            DOBDate = new Date(DOB_date);
                            jsonAddeddt = sdf.format(DOBDate);
                            values.put(columnName, jsonAddeddt);

                        } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                            jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                    + 1, jsonModifiedDt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonModifiedDt);
                            DOBDate = new Date(DOB_date);
                            jsonModifiedDt = sdf.format(DOBDate);
                            values.put(columnName, jsonModifiedDt);

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_Prospectsource, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            setautocomplete_prospect();


        }

    }

    class DownloadterritoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_fill_territory;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_Teritory, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Teritory, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        String jsonAddeddt = jorder.getString("AddedDt");
                        String jsonModifiedDt = jorder.getString("ModifiedDt");
                        if (columnName.equalsIgnoreCase("AddedDt")) {
                            if (!(jsonAddeddt.equalsIgnoreCase(null) ||
                                    (jsonAddeddt.equalsIgnoreCase("null")))) {

                                jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1,
                                        jsonAddeddt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonAddeddt);
                                DOBDate = new Date(DOB_date);
                                jsonAddeddt = sdf.format(DOBDate);
                            }
                            values.put(columnName, jsonAddeddt);

                        } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                            if (!(jsonModifiedDt.equalsIgnoreCase(null) ||
                                    (jsonModifiedDt.equalsIgnoreCase("null")))) {
                                jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                        + 1, jsonModifiedDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonModifiedDt);
                                DOBDate = new Date(DOB_date);
                                jsonModifiedDt = sdf.format(DOBDate);
                            }
                            values.put(columnName, jsonModifiedDt);

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_Teritory, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("")) {
                setautocomplete_teritory();
            }

        }

    }


    class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_Statelist;

            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_STATE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_STATE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_STATE, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
         /*   if (progressHUD1 != null && progressHUD1.isShowing()) {
                progressHUD1.dismiss();
            }*/
            dismissProgressDialog();
            if (response.contains("")) {

            }
            getState();
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

    private void getReference() {
        //sReferenceType, sReference,sEntity,sConsignee;
        lstReference.clear();
        String query = "SELECT distinct CustVendorMasterId,CustVendorName" +
                " FROM " + db.TABLE_Reference;
        Cursor cur = sql.rawQuery(query, null);
        // lstReference.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstReference.add(cur.getString(cur.getColumnIndex("CustVendorName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstReference);
        spinner_refrenceoption.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        spinner_refrenceoption.setSelection(0);
    }

    private Boolean CheckValidation() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_PROSPECT_VALIDATIONS + " WHERE FKProspectHdrID='" + ProspectTypeID + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }


    }

    private void setSpinner() {
        String[] countries = getResources().getStringArray(R.array.OptionYN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, countries);
        spinner_village.setAdapter(adapter);
        spinner_sex.setAdapter(adapter);
        spinner_District.setAdapter(adapter);
        spinner_val1.setAdapter(adapter);
        spinner_val2.setAdapter(adapter);
        spinner_val3.setAdapter(adapter);
        spinner_val4.setAdapter(adapter);
        spinner_val5.setAdapter(adapter);
        spinner_val6.setAdapter(adapter);
        spinner_val7.setAdapter(adapter);
        spinner_val8.setAdapter(adapter);
        spinner_val9.setAdapter(adapter);
        spinner_val10.setAdapter(adapter);

        spinner_village.setTitle("Select Option");
        spinner_sex.setTitle("Select Option");
        spinner_District.setTitle("Select Option");
        spinner_val1.setTitle("Select Option");
        spinner_val2.setTitle("Select Option");
        spinner_val3.setTitle("Select Option");
        spinner_val4.setTitle("Select Option");
        spinner_val5.setTitle("Select Option");
        spinner_val6.setTitle("Select Option");
        spinner_val7.setTitle("Select Option");
        spinner_val8.setTitle("Select Option");
        spinner_val9.setTitle("Select Option");
        spinner_val10.setTitle("Select Option");
    }

    private Boolean applyValidation() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_PROSPECT_VALIDATIONS + " WHERE FKProspectHdrID='" + ProspectTypeID + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String PKFieldID = cur.getString(cur.getColumnIndex("PKFieldID"));
                String ProspectHdrID = cur.getString(cur.getColumnIndex("FKProspectHdrID"));
                String ProspectField = cur.getString(cur.getColumnIndex("ProspectField"));
                String isVisible = cur.getString(cur.getColumnIndex("IsVisible"));
                String IsMandatory = cur.getString(cur.getColumnIndex("IsMandatory"));
                String Caption = cur.getString(cur.getColumnIndex("Caption"));
                String Section = cur.getString(cur.getColumnIndex("Section"));
                String FieldType = cur.getString(cur.getColumnIndex("FieldType"));

                if (ProspectField.equalsIgnoreCase("Prospect Name")) {
                    efName1.setTag(PKFieldID);
                    efName1.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_efName1.setVisibility(View.VISIBLE);
                        efName1.setVisibility(View.VISIBLE);
                        isVisible_prospectname = true;
                    } else {
                        ln_efName1.setVisibility(View.GONE);
                        efName1.setVisibility(View.GONE);
                        isVisible_prospectname = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_prospectname = true;
                    } else {
                        isMadatory_prospectname = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Contact Name")) {
                    edt_contact_name_person.setTag(PKFieldID);
                    edt_contact_name_person.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_contact_name_person.setVisibility(View.VISIBLE);
                        isVisible_Contact = true;
                    } else {
                        edt_contact_name_person.setVisibility(View.GONE);
                        isVisible_Contact = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Contact = true;
                    } else {
                        isMadatory_Contact = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Address")) {
                    edt_address.setTag(PKFieldID);
                    edt_address.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_address.setVisibility(View.VISIBLE);
                        isVisible_Address = true;
                    } else {
                        edt_address.setVisibility(View.GONE);
                        isVisible_Address = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Address = true;
                    } else {
                        isMadatory_Address = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("MobileNo")) {
                    edt_mobile.setTag(PKFieldID);
                    edt_mobile.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_mobile.setVisibility(View.VISIBLE);
                        isVisible_Mobile = true;
                    } else {
                        edt_mobile.setVisibility(View.GONE);
                        isVisible_Mobile = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Mobile = true;
                    } else {
                        isMadatory_Mobile = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("WhatsappNo")) {
                    edt_whatsaap.setTag(PKFieldID);
                    edt_whatsaap.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_whatsaap.setVisibility(View.VISIBLE);
                        isVisible_WhatsappNo = true;
                    } else {
                        edt_whatsaap.setVisibility(View.GONE);
                        isVisible_WhatsappNo = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_WhatsappNo = true;
                    } else {
                        isMadatory_WhatsappNo = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("TelephoneNo")) {
                    edt_telephone.setTag(PKFieldID);
                    edt_telephone.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_telephone.setVisibility(View.VISIBLE);
                        isVisible_Telephone = true;
                    } else {
                        edt_telephone.setVisibility(View.GONE);
                        isVisible_Telephone = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Telephone = true;
                    } else {
                        isMadatory_Telephone = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FKCityId")) {
                    ln_city.setTag(PKFieldID);
                    spinner_city.setTag(PKFieldID);
                    tv_city.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_city.setVisibility(View.VISIBLE);
                        spinner_city.setVisibility(View.VISIBLE);
                        isVisible_City = true;
                    } else {
                        ln_city.setVisibility(View.GONE);
                        spinner_city.setVisibility(View.GONE);
                        isVisible_City = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_City = true;
                    } else {
                        isMadatory_City = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FKCountryId")) {
                    ln_country.setTag(PKFieldID);
                    spinner_country.setTag(PKFieldID);
                    tv_country.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_country.setVisibility(View.VISIBLE);
                        spinner_country.setVisibility(View.VISIBLE);
                        isVisible_Country = true;
                    } else {
                        ln_country.setVisibility(View.GONE);
                        spinner_country.setVisibility(View.GONE);
                        isVisible_Country = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Country = true;
                    } else {
                        isMadatory_Country = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FKStateId")) {
                    ln_state.setTag(PKFieldID);
                    spinner_state.setTag(PKFieldID);
                    tv_state.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_state.setVisibility(View.VISIBLE);
                        spinner_state.setVisibility(View.VISIBLE);
                        isVisible_State = true;
                    } else {
                        ln_state.setVisibility(View.GONE);
                        spinner_state.setVisibility(View.GONE);
                        isVisible_State = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_State = true;
                    } else {
                        isMadatory_State = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FKTerritoryId")) {
                    ln_territory.setTag(PKFieldID);
                    spinner_territory.setTag(PKFieldID);
                    tv_territory.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_territory.setVisibility(View.VISIBLE);
                        spinner_territory.setVisibility(View.VISIBLE);
                        isVisible_Territory = true;
                    } else {
                        ln_territory.setVisibility(View.GONE);
                        spinner_territory.setVisibility(View.GONE);
                        isVisible_Territory = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Territory = true;
                    } else {
                        isMadatory_Territory = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("EmailMsgId")) {
                    edt_email.setTag(PKFieldID);
                    edt_email.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_email.setVisibility(View.VISIBLE);
                        isVisible_EmailID = true;
                    } else {
                        edt_email.setVisibility(View.GONE);
                        isVisible_EmailID = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_EmailID = true;
                    } else {
                        isMadatory_EmailID = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Website")) {
                    edt_website.setTag(PKFieldID);
                    edt_website.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_website.setVisibility(View.VISIBLE);
                        isVisible_Website = true;
                    } else {
                        edt_website.setVisibility(View.GONE);

                        isVisible_Website = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Website = true;
                    } else {
                        isMadatory_Website = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Business Segment")) {
                    ln_businessSegment.setTag(PKFieldID);
                    spinner_BusinessSegment.setTag(PKFieldID);
                    tv_bussinesssegment.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_businessSegment.setVisibility(View.VISIBLE);
                        spinner_BusinessSegment.setVisibility(View.VISIBLE);
                        isVisible_BusinessSegment = true;
                    } else {
                        ln_businessSegment.setVisibility(View.GONE);
                        spinner_BusinessSegment.setVisibility(View.GONE);

                        isVisible_BusinessSegment = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_BusinessSegment = true;
                    } else {
                        isMadatory_BusinessSegment = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("CurrencyDesc")) {
                    ln_currency.setTag(PKFieldID);
                    spinner_currency.setTag(PKFieldID);
                    tv_currency.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_currency.setVisibility(View.VISIBLE);
                        spinner_currency.setVisibility(View.VISIBLE);
                        isVisible_Currency = true;
                    } else {
                        ln_currency.setVisibility(View.GONE);
                        spinner_currency.setVisibility(View.GONE);

                        isVisible_Currency = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Currency = true;
                    } else {
                        isMadatory_Currency = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Sales Family")) {
                    ln_Product.setTag(PKFieldID);
                    spinner_product.setTag(PKFieldID);
                    tv_product.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_Product.setVisibility(View.VISIBLE);
                        spinner_product.setVisibility(View.VISIBLE);
                        isVisible_ProductSalesFamily = true;
                    } else {
                        ln_Product.setVisibility(View.GONE);
                        spinner_product.setVisibility(View.GONE);

                        isVisible_ProductSalesFamily = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_ProductSalesFamily = true;
                    } else {
                        isMadatory_ProductSalesFamily = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Source of Prospect")) {
                    ln_source.setTag(PKFieldID);
                    spinner_source.setTag(PKFieldID);
                    tv_sourceofprospect.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_source.setVisibility(View.VISIBLE);
                        spinner_source.setVisibility(View.VISIBLE);
                        isVisible_SourceofProspect = true;
                    } else {
                        ln_source.setVisibility(View.GONE);
                        spinner_source.setVisibility(View.GONE);
                        isVisible_SourceofProspect = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_SourceofProspect = true;
                    } else {
                        isMadatory_SourceofProspect = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Select reference")) {
                    ln_reference.setTag(PKFieldID);
                    spinner_reference.setTag(PKFieldID);
                    spinner_refrenceoption.setTag(PKFieldID);
                    tv_reference.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_reference.setVisibility(View.VISIBLE);
                        spinner_reference.setVisibility(View.VISIBLE);
                        spinner_refrenceoption.setVisibility(View.VISIBLE);
                        isVisible_Selectreference = true;
                    } else {
                        ln_reference.setVisibility(View.GONE);
                        spinner_reference.setVisibility(View.GONE);
                        spinner_refrenceoption.setVisibility(View.GONE);
                        isVisible_Selectreference = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Selectreference = true;
                    } else {
                        isMadatory_Selectreference = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Village")) {
                    ln_village.setTag(Caption);
                    tv_village.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_village.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_village.setVisibility(View.VISIBLE);
                            edt_village.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("")
                                || FieldType.equalsIgnoreCase("String")) {
                            edt_village.setVisibility(View.VISIBLE);
                            spinner_village.setVisibility(View.GONE);

                        }
                        isVisible_village = true;
                    } else {
                        ln_village.setVisibility(View.GONE);

                        isVisible_village = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_village = true;
                    } else {
                        isMadatory_village = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("District")) {
                    ln_District.setTag(PKFieldID);
                    tv_District.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_District.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_District.setVisibility(View.VISIBLE);
                            edt_District.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_District.setVisibility(View.VISIBLE);
                            spinner_District.setVisibility(View.GONE);

                        }
                        isVisible_district = true;
                    } else {
                        ln_District.setVisibility(View.GONE);

                        isVisible_district = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_district = true;
                    } else {
                        isMadatory_district = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("sex")) {
                    ln_sex.setTag(PKFieldID);
                    tv_sex.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_sex.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_sex.setVisibility(View.VISIBLE);
                            edt_sex.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_sex.setVisibility(View.VISIBLE);
                            spinner_sex.setVisibility(View.GONE);

                        }
                        isVisible_sex = true;
                    } else {
                        ln_sex.setVisibility(View.GONE);

                        isVisible_sex = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_sex = true;
                    } else {
                        isMadatory_sex = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val1")) {
                    ln_val1.setTag(PKFieldID);
                    tv_val1.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val1.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val1.setVisibility(View.VISIBLE);
                            edt_val1.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val1.setVisibility(View.VISIBLE);
                            spinner_val1.setVisibility(View.GONE);

                        }
                        isVisible_val1 = true;
                    } else {
                        ln_val1.setVisibility(View.GONE);

                        isVisible_val1 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val1 = true;
                    } else {
                        isMadatory_val1 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val2")) {
                    ln_val2.setTag(PKFieldID);
                    tv_val2.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val2.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val2.setVisibility(View.VISIBLE);
                            edt_val2.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val2.setVisibility(View.VISIBLE);
                            spinner_val2.setVisibility(View.GONE);

                        }
                        isVisible_val2 = true;
                    } else {
                        ln_val2.setVisibility(View.GONE);

                        isVisible_val2 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val2 = true;
                    } else {
                        isMadatory_val2 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val3")) {
                    ln_val3.setTag(PKFieldID);
                    tv_val3.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val3.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val3.setVisibility(View.VISIBLE);
                            edt_val3.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val3.setVisibility(View.VISIBLE);
                            spinner_val3.setVisibility(View.GONE);

                        }
                        isVisible_val3 = true;
                    } else {
                        ln_val3.setVisibility(View.GONE);

                        isVisible_val3 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val3 = true;
                    } else {
                        isMadatory_val3 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val4")) {
                    ln_val4.setTag(PKFieldID);
                    tv_val4.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val4.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val4.setVisibility(View.VISIBLE);
                            edt_val4.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val4.setVisibility(View.VISIBLE);
                            spinner_val4.setVisibility(View.GONE);

                        }
                        isVisible_val4 = true;
                    } else {
                        ln_val4.setVisibility(View.GONE);

                        isVisible_val4 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val4 = true;
                    } else {
                        isMadatory_val4 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val5")) {
                    ln_val5.setTag(PKFieldID);
                    tv_val5.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val5.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val5.setVisibility(View.VISIBLE);
                            edt_val5.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val5.setVisibility(View.VISIBLE);
                            spinner_val5.setVisibility(View.GONE);

                        }
                        isVisible_val5 = true;
                    } else {
                        ln_val5.setVisibility(View.GONE);

                        isVisible_val5 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val5 = true;
                    } else {
                        isMadatory_val5 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val6")) {
                    ln_val6.setTag(PKFieldID);
                    tv_val6.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val6.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val6.setVisibility(View.VISIBLE);
                            edt_val6.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val6.setVisibility(View.VISIBLE);
                            spinner_val6.setVisibility(View.GONE);

                        }
                        isVisible_val6 = true;
                    } else {
                        ln_val6.setVisibility(View.GONE);

                        isVisible_val6 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val6 = true;
                    } else {
                        isMadatory_val6 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val7")) {
                    ln_val7.setTag(PKFieldID);
                    tv_val7.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val7.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val7.setVisibility(View.VISIBLE);
                            edt_val7.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val7.setVisibility(View.VISIBLE);
                            spinner_val7.setVisibility(View.GONE);

                        }
                        isVisible_val7 = true;
                    } else {
                        ln_val7.setVisibility(View.GONE);

                        isVisible_val7 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val7 = true;
                    } else {
                        isMadatory_val7 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val8")) {
                    ln_val8.setTag(PKFieldID);
                    tv_val8.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val8.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val8.setVisibility(View.VISIBLE);
                            edt_val8.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val8.setVisibility(View.VISIBLE);
                            spinner_val8.setVisibility(View.GONE);

                        }
                        isVisible_val8 = true;
                    } else {
                        ln_val8.setVisibility(View.GONE);

                        isVisible_val8 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val8 = true;
                    } else {
                        isMadatory_val8 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val9")) {
                    ln_val9.setTag(PKFieldID);
                    tv_val9.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val9.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val9.setVisibility(View.VISIBLE);
                            edt_val9.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val9.setVisibility(View.VISIBLE);
                            spinner_val9.setVisibility(View.GONE);

                        }
                        isVisible_val9 = true;
                    } else {
                        ln_val9.setVisibility(View.GONE);

                        isVisible_val9 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val9 = true;
                    } else {
                        isMadatory_val9 = false;
                    }

                } else if (ProspectField.equalsIgnoreCase("val10")) {
                    ln_val10.setTag(PKFieldID);
                    tv_val10.setText(Caption + ":");
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_val10.setVisibility(View.VISIBLE);
                        if (FieldType.equalsIgnoreCase("Y/N")) {
                            spinner_val10.setVisibility(View.VISIBLE);
                            edt_val10.setVisibility(View.GONE);

                        } else if (FieldType.equalsIgnoreCase("") || FieldType.equalsIgnoreCase("String")) {
                            edt_val10.setVisibility(View.VISIBLE);
                            spinner_val10.setVisibility(View.GONE);

                        }
                        isVisible_val10 = true;
                    } else {
                        ln_val10.setVisibility(View.GONE);

                        isVisible_val10 = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_val10 = true;
                    } else {
                        isMadatory_val10 = false;
                    }

                }

            } while (cur.moveToNext());

            return true;
        } else {
            return false;
        }


    }


    private void validationAsync() {
        if (isnet()) {
            showProgressDialog();
            new StartSession(BusinessProspectusActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadValidationJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
        }
    }

    private void getReferencetype() {
//sReferenceType, sReference,sEntity,sConsignee;
        lstReferenceType.clear();
        String query = "SELECT distinct CustVendor,CustVendorCode" +
                " FROM " + db.TABLE_Referencetype;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstReferenceType.add(cur.getString(cur.getColumnIndex("CustVendorCode")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstReferenceType);
        spinner_reference.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_reference.setSelection(0);
    }


    private void getState() {
//sReferenceType, sReference,sEntity,sConsignee;
        lstState.clear();
        String query = "SELECT distinct PKStateId,StateDesc" +
                " FROM " + db.TABLE_STATE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstState.add(cur.getString(cur.getColumnIndex("StateDesc")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstState);
        spinner_state.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_state.setSelection(0);
    }


    class DownloadCurrencyMasterJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {

            //   progressHUD5 = ProgressHUD.show(context, " ", false, false, null);
            super.onPreExecute();
            proress_reference.setVisibility(View.VISIBLE);
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
                sql.delete(db.TABLE_CurrencyMaster, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CurrencyMaster, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_CurrencyMaster, null, values);
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
            /*if (progressHUD5 != null && progressHUD5.isShowing()) {
                progressHUD5.dismiss();
            }*/
//            dismissProgressDialog();

            proress_reference.setVisibility(View.GONE);
            if (response.contains("Error")) {

            }


        }

    }

    class DownloadValidationJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();


        }


        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Prospect_validations + "?Id=" + ProspectTypeID;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_PROSPECT_VALIDATIONS, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROSPECT_VALIDATIONS, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnValue == null ||
                                    columnValue.equalsIgnoreCase("")
                                    || columnValue.equalsIgnoreCase(null)
                                    || columnValue.equalsIgnoreCase("null")) {
                                columnValue = "";
                            }
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_PROSPECT_VALIDATIONS, null, values);
                        Log.e("ProspectValidat", "" + a);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("PKFieldID")) {
                applyValidation();
            } else {
                Toast.makeText(getApplicationContext(), "Due to server error or slow" +
                        " internet connetion can not apply validations", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void getCurrency() {

        ArrayList<String> currency = new ArrayList();
        currency.clear();
        String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                " FROM " + db.TABLE_CurrencyMaster;
        Cursor cur = sql.rawQuery(query, null);
        // currency.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                currency.add(cur.getString(cur.getColumnIndex("CurrDesc")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, currency);

        spinner_currency.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        spinner_currency.setSelection(0);


        /*lstTurnover = new ArrayList<String>();
        lstTurnover.clear();
*/
        /*if (sCurrency.getSelectedItemPosition() == 0) {
            lstTurnover.add("Lac");
            lstTurnover.add("Cr");
        } else if (sCurrency.getSelectedItemPosition() == 1) {
            lstTurnover.add("Bill");
            lstTurnover.add("Mill");
        }*/

        /*MySpinnerAdapter custom = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstTurnover);
        sTurnover.setAdapter(custom);

*/
    }

    public boolean validate() {
        String email = edt_email.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // TODO Auto-generated method stub
        if ((efName1.getText().toString().equalsIgnoreCase("") ||
                efName1.getText().toString().equalsIgnoreCase(" ") ||
                efName1.getText().toString().equalsIgnoreCase(null)) && isMadatory_prospectname) {
            if (!isVisible_prospectname) {
                ln_efName1.setVisibility(View.VISIBLE);
                efName1.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter prospect name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contact_name_person.getText().toString().equalsIgnoreCase("") ||
                edt_contact_name_person.getText().toString().equalsIgnoreCase(" ") ||
                edt_contact_name_person.getText().toString().equalsIgnoreCase(null)) && isMadatory_Contact) {
            if (!isVisible_Contact) {
                edt_contact_name_person.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_address.getText().toString().equalsIgnoreCase("") ||
                edt_address.getText().toString().equalsIgnoreCase(" ") ||
                edt_address.getText().toString().equalsIgnoreCase(null)) && isMadatory_Address) {
            if (!isVisible_Address) {
                edt_address.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_mobile.getText().toString().equalsIgnoreCase("") ||
                edt_mobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_mobile.getText().toString().equalsIgnoreCase(null) ||
                edt_mobile.getText().length() != 10) && isMadatory_Mobile) {
            if (!isVisible_Mobile) {
                edt_mobile.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter mobile or valid mobile no.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_whatsaap.getText().toString().equalsIgnoreCase("") ||
                edt_whatsaap.getText().toString().equalsIgnoreCase(" ") ||
                edt_whatsaap.getText().toString().equalsIgnoreCase(null) ||
                edt_whatsaap.getText().length() != 10) && isMadatory_WhatsappNo) {
            if (!isVisible_WhatsappNo) {
                edt_whatsaap.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter whatsaap No. or valid whatsaap No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_telephone.getText().toString().equalsIgnoreCase("") ||
                edt_telephone.getText().toString().equalsIgnoreCase(" ") ||
                edt_telephone.getText().toString().equalsIgnoreCase(null)) && isMadatory_Telephone) {
            if (!isVisible_Telephone) {
                edt_telephone.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter Telephone", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_city.equalsIgnoreCase("") ||
                string_city.equalsIgnoreCase(" ") ||
                string_city.equalsIgnoreCase(null)) && isMadatory_City) {
            if (!isVisible_City) {
                ln_city.setVisibility(View.VISIBLE);
                spinner_city.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select City", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_state.equalsIgnoreCase("") ||
                string_state.equalsIgnoreCase(" ") ||
                string_state.equalsIgnoreCase(null)) && isMadatory_State) {
            if (!isVisible_State) {
                ln_state.setVisibility(View.VISIBLE);
                spinner_state.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select State", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_country.equalsIgnoreCase("") ||
                string_country.equalsIgnoreCase(" ") ||
                string_country.equalsIgnoreCase(null)) && isMadatory_Country) {
            if (!isVisible_Country) {
                ln_country.setVisibility(View.VISIBLE);
                spinner_country.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select country", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_territory.equalsIgnoreCase("") ||
                string_territory.equalsIgnoreCase(" ") ||
                string_territory.equalsIgnoreCase(null)) && isMadatory_Territory) {
            if (!isVisible_Territory) {
                ln_territory.setVisibility(View.VISIBLE);
                spinner_territory.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select territory", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_email.getText().toString().equalsIgnoreCase("") ||
                edt_email.getText().toString().equalsIgnoreCase(" ") ||
                edt_email.getText().toString().equalsIgnoreCase(null) ||
                !(email.matches(emailPattern))) && isMadatory_EmailID) {
            if (!isVisible_EmailID) {
                edt_email.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter email address or valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_website.getText().toString().equalsIgnoreCase("") ||
                edt_website.getText().toString().equalsIgnoreCase(" ") ||
                edt_website.getText().toString().equalsIgnoreCase(null)) && isMadatory_Website) {
            if (!isVisible_Website) {
                edt_website.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter website", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_BusinessSegment.equalsIgnoreCase("") ||
                string_BusinessSegment.equalsIgnoreCase(" ") ||
                string_BusinessSegment.equalsIgnoreCase(null)) && isMadatory_BusinessSegment) {
            if (!isVisible_BusinessSegment) {
                ln_businessSegment.setVisibility(View.VISIBLE);
                spinner_BusinessSegment.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select business segment", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_currency.equalsIgnoreCase("") ||
                string_currency.equalsIgnoreCase(" ") ||
                string_currency.equalsIgnoreCase(null)) && isMadatory_Currency) {
            if (!isVisible_Currency) {
                ln_currency.setVisibility(View.VISIBLE);
                spinner_currency.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select currency", Toast.LENGTH_LONG).show();
            return false;

        } else if ((string_product_salesfamily.equalsIgnoreCase("") ||
                string_product_salesfamily.equalsIgnoreCase(" ") ||
                string_product_salesfamily.equalsIgnoreCase(null)) && isMadatory_ProductSalesFamily) {
            if (!isVisible_ProductSalesFamily) {
                ln_Product.setVisibility(View.VISIBLE);
                spinner_product.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select sales family", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_Source.equalsIgnoreCase("") ||
                string_Source.equalsIgnoreCase(" ") ||
                string_Source.equalsIgnoreCase(null)) && isMadatory_SourceofProspect) {
            if (!isVisible_SourceofProspect) {
                ln_source.setVisibility(View.VISIBLE);
                spinner_source.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select source of prospect", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_reference.equalsIgnoreCase("") ||
                string_reference.equalsIgnoreCase(" ") ||
                string_reference.equalsIgnoreCase(null)) && isMadatory_Selectreference) {
            if (!isVisible_Selectreference) {
                ln_reference.setVisibility(View.VISIBLE);
                spinner_reference.setVisibility(View.VISIBLE);
                spinner_refrenceoption.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please select reference", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_village.getVisibility() == View.VISIBLE
                && (edt_village.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_village) {
            //  String a = edt_village.getTag().toString();

            if (!isVisible_village) {
                ln_village.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter village", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_District.getVisibility() == View.VISIBLE
                && (edt_District.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_district) {
            if (!isVisible_district) {
                ln_District.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter district", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_sex.getVisibility() == View.VISIBLE
                && (edt_sex.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_sex) {
            if (!isVisible_sex) {
                ln_sex.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter sex", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val1.getVisibility() == View.VISIBLE
                && (edt_val1.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val1) {
            if (!isVisible_val1) {
                ln_val1.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val2.getVisibility() == View.VISIBLE
                && (edt_val2.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val2) {
            if (!isVisible_val2) {
                ln_val2.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val3.getVisibility() == View.VISIBLE
                && (edt_val3.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val3) {
            if (!isVisible_val3) {
                ln_val3.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val4.getVisibility() == View.VISIBLE
                && (edt_val4.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val4) {
            if (!isVisible_val4) {
                ln_val4.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val5.getVisibility() == View.VISIBLE
                && (edt_val5.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val5) {
            if (!isVisible_val5) {
                ln_val5.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val6.getVisibility() == View.VISIBLE
                && (edt_val6.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val6) {
            if (!isVisible_val6) {
                ln_val6.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val7.getVisibility() == View.VISIBLE
                && (edt_val7.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val7) {
            if (!isVisible_val7) {
                ln_val7.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val8.getVisibility() == View.VISIBLE
                && (edt_val8.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val8) {
            if (!isVisible_val8) {
                ln_val8.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val9.getVisibility() == View.VISIBLE
                && (edt_val9.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val9) {
                ln_val9.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val10.getVisibility() == View.VISIBLE
                && (edt_val10.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val10) {
                ln_val10.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    class PostProspectUpdate_savenstartJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BusinessProspectusActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            try {
                res = ut.OpenPostConnection(url, params[0]);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("SaveBusiness :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (PKSuspectId != null) {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(BusinessProspectusActivity.this, "Prospect added successfully", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(BusinessProspectusActivity.this, CreateOpportunityActivity.class);
            intent.putExtra("SuspectID", integer);//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
            startActivity(intent);
            BusinessProspectusActivity.this.finish();
            //onBackPressed();
        }

    }

    class DownloadProductJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {

            //  progressHUD6 = ProgressHUD.show(context, " ", false, false, null);
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            //String url = CompanyURL + WebUrlClass.api_get_Product;
            String url = CompanyURL + WebUrlClass.api_get_sales_family;

            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_SALES_FAMILY_PRODUCT, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SALES_FAMILY_PRODUCT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("ControllerId")) {
                            columnValue = "";
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SALES_FAMILY_PRODUCT, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
           /* if (progressHUD6 != null && progressHUD6.isShowing()) {
                progressHUD6.dismiss();
            }*/
            dismissProgressDialog();
            if (response.contains("FamilyId")) {
                getproduct();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to load product detail. Please refresh data again or contact to support", Toast.LENGTH_LONG).show();
            }


        }

    }

    private void getCountry() {

        ArrayList<String> mList = new ArrayList<String>();
        mList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_COUNTRY;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                mList.add(cur.getString(cur.getColumnIndex("CountryName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, mList);
        spinner_country.setAdapter(customDept);
    }

    private void getproduct() {


        Productionitems.clear();
        String query = "SELECT distinct FamilyId,FamilyDesc" +
                " FROM " + db.TABLE_SALES_FAMILY_PRODUCT;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Productionitems.add(cur.getString(cur.getColumnIndex("FamilyDesc")));

            } while (cur.moveToNext());

        }
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        int a = lstProduct.indexOf(Product_name);
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        spinner_product.setSelection(Productionitems.indexOf(Product_name));
    }

    class DownloadBusinesssegmentJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Businesssegment;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_Business_segment, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Business_segment, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_Business_segment, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("")) {
                setautocomplete_BusinessSegment();
            }

        }

    }

    class DownloadCountryListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_countrylist;

            try {
                res = ut.OpenConnection(url);

                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_COUNTRY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_COUNTRY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_COUNTRY, null, values);
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
            dismissProgressDialog();
            if (response.contains("PKCountryId")) {
                getCountry();
            }

        }

    }

    private void showProgressDialog() {
        progressbar_business.setVisibility(View.VISIBLE);

        //progressHUD = ProgressHUD.show(OpportunityUpdateActivity.this, "" + txt, false, false, null);
    }


    private void dismissProgressDialog() {


        progressbar_business.setVisibility(View.GONE);


    }

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=" + URLEncoder.encode("", "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_CITY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        String jsonAddeddt = jorder.getString("AddedDt");
                        String jsonModifiedDt = jorder.getString("ModifiedDt");
                        if (columnName.equalsIgnoreCase("AddedDt")) {
                            if (!(jsonAddeddt.equalsIgnoreCase(null) ||
                                    (jsonAddeddt.equalsIgnoreCase("null")))) {
                                jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonAddeddt);
                                DOBDate = new Date(DOB_date);
                                jsonAddeddt = sdf.format(DOBDate);
                            }
                            values.put(columnName, jsonAddeddt);

                        } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                            if (!(jsonModifiedDt.equalsIgnoreCase(null) ||
                                    (jsonModifiedDt.equalsIgnoreCase("null")))) {
                                jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                        + 1, jsonModifiedDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonModifiedDt);
                                DOBDate = new Date(DOB_date);
                                jsonModifiedDt = sdf.format(DOBDate);
                            }
                            values.put(columnName, jsonModifiedDt);

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_CITY, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //  dismissProgressDialog();
            if (response.contains("")) {

                //   getInitiatedby();
                getCity();
            }

        }

    }


    private void getcontactfetchdetails() {

        String query = "SELECT * FROM " + db.TABLE_CONTACT_DETAILS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                edt_contact_name_person.setText(cur.getString(cur.getColumnIndex("ContactName")));
                edt_mobile.setText(cur.getString(cur.getColumnIndex("Mobile")));
                edt_email.setText(cur.getString(cur.getColumnIndex("EmailId")));
                edt_telephone.setText(cur.getString(cur.getColumnIndex("Telephone")));
                BusDetail_Segment_name = (cur.getString(cur.getColumnIndex("ContactPersonDept")));

            } while (cur.moveToNext());

        }
    }

    private void getproductfetchdetails() {

        String query = "SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {


                efName1.setText(cur.getString(cur.getColumnIndex("FirmName")));
                edt_address.setText(cur.getString(cur.getColumnIndex("Address")));
                //  Product_name = cur.getString(cur.getColumnIndex("ItemDesc"));
                edt_website.setText(cur.getString(cur.getColumnIndex("CompanyURL")));

            } while (cur.moveToNext());

        }
    }

    private void getremainingdata() {

        String query = "SELECT * FROM " + db.TABLE_FILLCONTROL_DATA_FETCH;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                City_name = (cur.getString(cur.getColumnIndex("CityName")));
                Territory_name = (cur.getString(cur.getColumnIndex("TerritoryName")));
                Source_prospect = (cur.getString(cur.getColumnIndex("SourceName")));
                Product_name = cur.getString(cur.getColumnIndex("ItemDesc"));
            } while (cur.moveToNext());

        }
    }
}
