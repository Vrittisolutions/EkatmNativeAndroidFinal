package com.vritti.crmlib.vcrm7;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.CustomAdapterContactList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class ProspectEnterpriseActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    public static Context context;


    private static final int PERMISSION_REQUEST_CONTACT = 1;
    LinearLayout ln_firmname, ln_Entity, ln_city, ln_state, ln_country, ln_territory, ln_businessSegment,
            ln_source, ln_reference, ln_Notes, ln_Contact, ln_Deptrole, ln_birthday, ln_BusinessDetails,
            ln_currency, ln_turnover, ln_Product;
    RelativeLayout hdr_Businessprofile, rl_entity;
    EditText edt_firmalise, edt_Address, edt_gstn, edt_TANNO, edt_TANNONAME, edt_website, edt_Notes,
            edt_ContactName, edt_ContactDesignation, edt_contactEmailid, edt_contactMobile, edt_contactwhatsapp, edt_ContactTele, edt_Contactskype,
            edt_Birthdate, edt_OfficeCnt, edt_EmployeeStrength, edt_turnover, edt_businessDetails;
    TextView Products;
    AutoCompleteTextView atxt_firmname;
    SearchableSpinner spinner_city, spinner_state, spinner_country, spinner_territory, spinner_BusinessSegment,
            spinner_reference, spinner_refrenceoption,
            spinner_product, spinner_Departmentrole, spinner_currency, spinner_turnover;
    SearchableSpinner spinner_Entity, sConsignee, spinner_source;
    AppCompatCheckBox checkbox_entity;
    ImageView img_birth_calender, img_contact;

    LinearLayout ln_village, ln_District, ln_sex, ln_val1, ln_val2, ln_val3,
            ln_val4, ln_val5, ln_val6, ln_val7, ln_val8, ln_val9, ln_val10;
    TextView tv_village, tv_District, tv_sex, tv_val1, tv_val2, tv_val3,
            tv_val4, tv_val5, tv_val6, tv_val7, tv_val8, tv_val9, tv_val10;

    TextView tv_city, tv_state, tv_country, tv_territory, tv_bussinesssegment, tv_currency,
            tv_source, tv_sourceofprospect, tv_reference, tv_role, tv_product;
    SearchableSpinner spinner_village, spinner_District, spinner_sex, spinner_val1, spinner_val2, spinner_val3,
            spinner_val4, spinner_val5, spinner_val6, spinner_val7, spinner_val8, spinner_val9, spinner_val10;
    EditText edt_village, edt_District, edt_sex, edt_val1, edt_val2, edt_val3,
            edt_val4, edt_val5, edt_val6, edt_val7, edt_val8, edt_val9, edt_val10;

    private static Boolean isMadatory_Firmname = false, isMadatory_FirmAlias = false, isMadatory_entity = false, isMadatory_Address = false, isMadatory_City = false,
            isMadatory_State = false, isMadatory_Country = false, isMadatory_Territory = false, isMadatory_GSTN = false, isMadatory_TANno = false,
            isMadatory_TANname = false, isMadatory_BusinessSegment = false, isMadatory_Website = false, isMadatory_SourceofProspect = false, isMadatory_Selectreference = false,
            isMadatory_Notes = false, isMadatory_Contact = false, isMadatory_Designation = false, isMadatory_Role = false,
            isMadatory_EmailID = false, isMadatory_Mobile = false, isMadatory_WhatsappNo = false, isMadatory_Telephone = false, isMadatory_Skype = false, isMadatory_Dateofbirth = false,
            isMadatory_ProductSalesFamily = false,
            isMadatory_BusinessDetails = false, isMadatory_Currency = false, isMadatory_Turnover = false, isMadatory_NoofEmployees = false, isMadatory_NoofOffices = false;
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
    private static Boolean isVisible_Firmname = true, isVisible_FirmAlias = true, isVisible_entity = true, isVisible_Address = true, isVisible_City = true,
            isVisible_State = true, isVisible_Country = true, isVisible_Territory = true, isVisible_GSTN = true, isVisible_TANno = true,
            isVisible_TANname = true, isVisible_BusinessSegment = true, isVisible_Website = true, isVisible_SourceofProspect = true, isVisible_Selectreference = true,
            isVisible_Notes = true, isVisible_Contact = true, isVisible_Designation = true, isVisible_Role = true,
            isVisible_EmailID = true, isVisible_Mobile = true, isVisible_WhatsappNo = true, isVisible_Telephone = true, isVisible_Skype = true,
            isVisible_Dateofbirth = true, isVisible_ProductSalesFamily = true,
            isVisible_BusinessDetails = true, isVisible_Currency = true, isVisible_Turnover = true, isVisible_NoofEmployees = true, isVisible_NoofOffices = true;
    private static String string_entity = "", string_entityconsinee = "", string_city = "", string_country = "", string_state = "",
            string_territory = "", string_BusinessSegment = "", string_Source = "", string_reference = "", string_refrenceoption = "",
            string_product_salesfamily = "", string_currency = "", string_turnover = "", string_DepartmentRole = "";
    private String CustomerName = "", ContactName = "", ContactNumber = "", registeryID = "", Email = "", EnaquiryDetail = "";
    Button buttonSave_prospect, buttonClose_prospect, buttonSaveandstartcall_prospect;
    List city;
    private PopupWindow pw;
    private boolean expanded;
    String[] susmaster;
    String[] contact;
    String[] prod;
    String[] FinalArray;
    String[] Customer_contact;
    List<String> lstfirm = new ArrayList<String>();
    List<String> lstCity = new ArrayList<String>();
    List<String> lstTerrority = new ArrayList<String>();
    ArrayList<String> Productionitems = new ArrayList<String>();
    List<String> lstBusinessSegment = new ArrayList<String>();
    public static List<String> lstProduct = new ArrayList<String>();
    List<String> lstSourceProspect = new ArrayList<String>();
    List<String> lstReferenceType = new ArrayList<String>();
    List<String> lstReference = new ArrayList<String>();
    List<String> lstEntity = new ArrayList<String>();
    List<String> lstConsignee = new ArrayList<String>();
    List<String> lstTurnover = new ArrayList<String>();
    List<String> lstState = new ArrayList<String>();
    public static List<Firmbean> lstFirm = new ArrayList<Firmbean>();

    SimpleDateFormat dfDate = null;
    MySpinnerAdapter customDept;
    Dialog d_contact;
    public static List<ProspectContact> lstContact = new ArrayList<ProspectContact>();
    List<String> lstDept;
    String Stateid, Countryid;
    String FirmName, Territoryid = "", Cityid = "", BusDetailid = "",
            SuSpSourceId = "0", CurrentDate = "", Consigneeid, Referenceid,
            Currencyid, PKSuspectId,
            mobile, Productid,
             Conttact_name = "", Designation = "", Contact_emailid = "", Contact_mobile = "",
            Contact_telephone = "", Contact_DateBirth = "", Contact_Depatment = "", Contact_Fax = "", GSTN = "", TAN_No = "", TAN_No_Name = "", Whatsapp_no = "", Birth_date = "", date_Before = "";

    SharedPreferences userpreferences;
    SharedPreferences Prospectpreference;
    private static String ProspectTypeID;
    SQLiteDatabase sql;
    String finaljson;
    String custvid = "", reftypeid = "";
    ProgressBar progressbar;
    public static boolean[] checkSelected;
    Dialog dialog;
    Date DOBDate = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private String Statename, City_name, Territory_name, Source_prospect, BusDetail_Segment_name, Product_name;

    String email;

    private String product;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_prospect_entry);
        context = ProspectEnterpriseActivity.this;

        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_Enterprise, "");
        if (ProspectTypeID.equalsIgnoreCase("")) {
            ProspectTypeID = "1";
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
        dfDate = new SimpleDateFormat("yyy/MM/dd");
        Date d = new Date();
        CurrentDate = dfDate.format(d);
        Intent intent = getIntent();
        PKSuspectId = intent.getStringExtra("PKSuspectId");
        FirmName = intent.getStringExtra("firmname");
        if (intent.hasExtra("registryID")) {
            CustomerName = intent.getStringExtra("custname");
            ContactName = intent.getStringExtra("contactname");
            ContactNumber = intent.getStringExtra("contactnumber");
            registeryID = intent.getStringExtra("registryID");
            Email = intent.getStringExtra("email");
            EnaquiryDetail = intent.getStringExtra("enquirydetail");
            atxt_firmname.setText(CustomerName);
            atxt_firmname.setSelection(atxt_firmname.length());
            edt_ContactName.setText(ContactName);
            edt_contactMobile.setText(ContactNumber);
        } else {
            CustomerName = "";
            ContactName = "";
            ContactNumber = "";
            registeryID = "";
            Email = "";
            EnaquiryDetail = "";
        }
        if (PKSuspectId != null) {
            getcontactfetchdetails();
            getproductfetchdetails();
            getremainingdata();
            atxt_firmname.setText(FirmName);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS}, 2909);
            }
        }
        if (CheckValidation()) {
            applyValidation();
        } else {
            validationAsync();
        }


        if (cf.getCitycount() > 0) {
            getCity();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
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
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
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
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
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
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
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
        }
        {
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
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

        if (cf.check_source() > 0) {
            setautocomplete_prospect();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
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

        if (cf.getReferencetypecount() > 0) {
            getReferencetype();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
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
        if (cf.getProuctcount() > 0) {
            getproduct();
        } else {
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
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

      /*  if (db.check_setup() > 0) {

        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }*/

        if (cf.getEntitycount() > 0) {
            getEntity();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEntityJSON().execute();
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
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
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


        lstDept = new ArrayList<String>();
        lstDept.add("--Select Role--");
        lstDept.add("Decision Maker");
        lstDept.add("HR");
        lstDept.add("Marketing");
        lstDept.add("Operator");
        lstDept.add("Purchase");

        customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstDept);
        spinner_Departmentrole.setAdapter(customDept);
        int a = lstDept.indexOf(Contact_Depatment);
        spinner_Departmentrole.setSelection(lstDept.indexOf(Contact_Depatment));
        setListener();
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

    private void init() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ln_firmname = (LinearLayout) findViewById(R.id.ln_firmname);
        atxt_firmname = (AutoCompleteTextView) findViewById(R.id.atxt_firmname);
        edt_firmalise = (EditText) findViewById(R.id.edt_firmalise);
        checkbox_entity = (AppCompatCheckBox) findViewById(R.id.checkbox_entity);
        ln_Entity = (LinearLayout) findViewById(R.id.ln_Entity);
        spinner_Entity = (SearchableSpinner) findViewById(R.id.sEntity);
        spinner_Entity.setSelection(0);
        sConsignee = (SearchableSpinner) findViewById(R.id.sConsignee);
        sConsignee.setSelection(0);
        edt_Address = (EditText) findViewById(R.id.edt_Address);
        ln_city = (LinearLayout) findViewById(R.id.ln_city);
        spinner_city = (SearchableSpinner) findViewById(R.id.spinner_City);
        ln_state = (LinearLayout) findViewById(R.id.ln_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        ln_country = (LinearLayout) findViewById(R.id.ln_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        ln_territory = (LinearLayout) findViewById(R.id.ln_territory);
        spinner_territory = (SearchableSpinner) findViewById(R.id.spinner_territory);
        spinner_city.setSelection(0);
        spinner_state.setSelection(0);
        spinner_country.setSelection(0);
        spinner_territory.setSelection(0);
        spinner_city.setTitle("Select City");
        spinner_state.setTitle("Select State");
        spinner_country.setTitle("Select Country");
        spinner_territory.setTitle("Select Territory");
        edt_gstn = (EditText) findViewById(R.id.edt_GSTN);//edt_TANNO
        edt_TANNO = (EditText) findViewById(R.id.edt_TANNO);//edt_TANNO
        edt_TANNONAME = (EditText) findViewById(R.id.edt_TANNONAME);
        ln_businessSegment = (LinearLayout) findViewById(R.id.ln_businessSegment);
        spinner_BusinessSegment = (SearchableSpinner) findViewById(R.id.spinner_BusinessSegment);
        spinner_BusinessSegment.setTitle("Select Option");
        edt_website = (EditText) findViewById(R.id.edt_website);
        ln_source = (LinearLayout) findViewById(R.id.ln_source);
        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_source);
        spinner_source.setTitle("Select Source");
        ln_reference = (LinearLayout) findViewById(R.id.ln_reference);
        spinner_reference = (SearchableSpinner) findViewById(R.id.spinner_reference);
        spinner_refrenceoption = (SearchableSpinner) findViewById(R.id.spinner_refrenceoption);
        spinner_reference.setTitle("Select Reference");
        spinner_refrenceoption.setTitle("Select Option");
        spinner_reference.setSelection(0);
        spinner_refrenceoption.setSelection(0);
        ln_Notes = (LinearLayout) findViewById(R.id.ln_Notes);
        edt_Notes = (EditText) findViewById(R.id.edt_Notes);

        ln_Contact = (LinearLayout) findViewById(R.id.ln_Contact);
        edt_ContactName = (EditText) findViewById(R.id.edt_ContactName);
        edt_ContactDesignation = (EditText) findViewById(R.id.edt_ContactDesignation1);
        ln_Deptrole = (LinearLayout) findViewById(R.id.ln_Deptrole);
        spinner_Departmentrole = (SearchableSpinner) findViewById(R.id.spinner_Departmentrole);
        edt_contactEmailid = (EditText) findViewById(R.id.edt_contactEmailid);
        edt_contactMobile = (EditText) findViewById(R.id.edt_contactMobile);
        edt_contactwhatsapp = (EditText) findViewById(R.id.edt_contactwhatsapp);
        edt_ContactTele = (EditText) findViewById(R.id.edt_ContactTele);
        edt_Contactskype = (EditText) findViewById(R.id.edt_Contactskype);
        ln_birthday = (LinearLayout) findViewById(R.id.ln_birthday);
        edt_Birthdate = (EditText) findViewById(R.id.edt_Birthdate);

        img_birth_calender = (ImageView) findViewById(R.id.img_birth_calender);
        img_contact = (ImageView) findViewById(R.id.img_contact);

        ln_Product = (LinearLayout) findViewById(R.id.ln_Product);
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);
        spinner_product.setTitle("Sales Family");

        hdr_Businessprofile = (RelativeLayout) findViewById(R.id.hdr_Businessprofile);
        rl_entity = (RelativeLayout) findViewById(R.id.rl_entity);
        ln_BusinessDetails = (LinearLayout) findViewById(R.id.ln_BusinessDetails);
        edt_OfficeCnt = (EditText) findViewById(R.id.edt_OfficeCnt);
        edt_EmployeeStrength = (EditText) findViewById(R.id.edt_EmployeeStrength);
        ln_currency = (LinearLayout) findViewById(R.id.ln_currency);
        spinner_currency = (SearchableSpinner) findViewById(R.id.spinner_currency);
        ln_turnover = (LinearLayout) findViewById(R.id.ln_turnover);
        edt_turnover = (EditText) findViewById(R.id.edt_turnover);
        spinner_turnover = (SearchableSpinner) findViewById(R.id.spinner_turnover);
        edt_businessDetails = (EditText) findViewById(R.id.edt_businessDetails);


        // autotxtProspect.setTitle("Select Source of Prospect ");


        buttonSave_prospect = (Button) findViewById(R.id.buttonSave_prospect);
        buttonClose_prospect = (Button) findViewById(R.id.buttonClose_prospect);
        buttonSaveandstartcall_prospect = (Button) findViewById(R.id.buttonSaveandstartcall_prospect);


        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_territory = (TextView) findViewById(R.id.tv_territory);
        tv_bussinesssegment = (TextView) findViewById(R.id.tv_businessSegment);

        tv_source = (TextView) findViewById(R.id.tv_source);
        tv_reference = (TextView) findViewById(R.id.tv_reference);//headProductServices
        tv_product = (TextView) findViewById(R.id.headProductServices);
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        tv_role = (TextView) findViewById(R.id.tv_role);

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

       /* ln_val6 = findViewById(R.id.ln_val6);
        tv_val6 = findViewById(R.id.tv_val6);
        spinner_val6 = findViewById(R.id.spinner_val6);
        edt_val6 = findViewById(R.id.edt_val6);

        ln_val7 = findViewById(R.id.ln_val7);
        tv_val7 = findViewById(R.id.tv_val7);
        spinner_val7 = findViewById(R.id.spinner_val7);
        edt_val7 = findViewById(R.id.edt_val7);

        ln_val8 = findViewById(R.id.ln_val8);
        tv_val8 = findViewById(R.id.tv_val8);
        spinner_val8 = findViewById(R.id.spinner_val8);
        edt_val8 = findViewById(R.id.edt_val8);

        ln_val9 = findViewById(R.id.ln_val9);
        tv_val9 = findViewById(R.id.tv_val9);
        spinner_val9 = findViewById(R.id.spinner_val9);
        edt_val9 = findViewById(R.id.edt_val9);

        ln_val10 = findViewById(R.id.ln_val10);
        tv_val10 = findViewById(R.id.tv_val10);
        spinner_val10 = findViewById(R.id.spinner_val10);
        edt_val10 = findViewById(R.id.edt_val10);*/


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

        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForContactPermission();
            }
        });

        checkbox_entity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (checkbox_entity.isChecked()) {
                    ln_Entity.setVisibility(View.VISIBLE);
                    if (cf.getEntitycount() > 0) {
                        getEntity();
                    } else {
                        if (ut.isNet(context)) {
                            showProgressDialog();
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    dismissProgressDialog();
                                    new DownloadEntityJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    dismissProgressDialog();
                                }
                            });
                        }
                    }
                } else {
                    ln_Entity.setVisibility(View.GONE);
                }
            }
        });


    }

    private void setListener() {
        buttonClose_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        buttonSaveandstartcall_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        Conttact_name = edt_ContactName.getText().toString();
                        Designation = edt_ContactDesignation.getText().toString();
                        Contact_emailid = edt_contactEmailid.getText().toString();
                        Contact_mobile = edt_contactMobile.getText().toString();
                        Contact_telephone = edt_ContactTele.getText().toString();
                        Contact_DateBirth = edt_Birthdate.getText().toString();
                        Contact_Depatment = (String) spinner_Departmentrole.getSelectedItem();
                        Contact_Fax = edt_Contactskype.getText().toString();
                        Whatsapp_no = edt_contactwhatsapp.getText().toString();
                        addContact(Conttact_name, Contact_mobile);

                        lstContact.add(new ProspectContact(Conttact_name, Designation, Contact_Depatment,
                                Contact_DateBirth, Contact_emailid, Contact_telephone, Contact_mobile, Contact_Fax, Whatsapp_no));
                        Customer_contact = new String[lstContact.size()];

                        for (int i = 0; i < lstContact.size(); i++) {
                            jsoncontact.put("ContactName", Conttact_name);
                            jsoncontact.put("Designation", Designation);
                            jsoncontact.put("EmailId", Contact_emailid);
                            jsoncontact.put("Mobile", Contact_mobile);
                            jsoncontact.put("Telephone", Contact_telephone);
                            jsoncontact.put("DateofBirth", Contact_DateBirth);
                            jsoncontact.put("ContactPersonDept", Contact_Depatment);
                            jsoncontact.put("Fax", Contact_Fax);
                            jsoncontact.put("WhatsAppNo", Whatsapp_no);
                            Customer_contact[i] = jsoncontact.toString();
                        }

                    } catch (Exception e) {

                    }
                    JSONObject jsonProduct = new JSONObject();
                    try {


                        jsonProduct.put("FKProductId", Productid);

                        product = jsonProduct.toString();


                    } catch (Exception e) {

                    }
                    susmaster = new String[5];
                    JSONObject jsonSuspMaster = new JSONObject();

                    try {
                        if (PKSuspectId != null) {
                            jsonSuspMaster.put("PKSuspectId", PKSuspectId);
                        } else {
                            jsonSuspMaster.put("PKSuspectId", null);
                        }
                        jsonSuspMaster.put("FirmName", atxt_firmname.getText().toString());
                        jsonSuspMaster.put("Address", edt_Address.getText().toString());
                        jsonSuspMaster.put("FirmAlias", edt_firmalise.getText().toString());
                        jsonSuspMaster.put("FKCityId", Cityid);
                        jsonSuspMaster.put("FKTerritoryId", Territoryid);
                        jsonSuspMaster.put("FKBusiSegmentId", BusDetailid);
                        jsonSuspMaster.put("CompanyURL", CompanyURL);
                        jsonSuspMaster.put("FKEnqSourceId", SuSpSourceId);
                        jsonSuspMaster.put("Fax", "");
                        jsonSuspMaster.put("Notes", edt_Notes.getText().toString());
                        jsonSuspMaster.put("Remark", edt_Notes.getText().toString());
                        jsonSuspMaster.put("Department", string_DepartmentRole);
                        jsonSuspMaster.put("BusinessDetails", edt_businessDetails.getText().toString());
                        jsonSuspMaster.put("CurrencyMasterId", Currencyid);
                        jsonSuspMaster.put("CurrencyDesc", string_currency);
                        jsonSuspMaster.put("Turnover", edt_turnover.getText().toString());
                        jsonSuspMaster.put("NoOfEmployees", edt_EmployeeStrength.getText().toString());
                        jsonSuspMaster.put("NoOfOffices", edt_OfficeCnt.getText().toString());
                        jsonSuspMaster.put("LeadGivenBYId", Referenceid);
                        jsonSuspMaster.put("FKConsigneeId", Consigneeid);
                        jsonSuspMaster.put("FKCustomerId", custvid);
                        jsonSuspMaster.put("EntityType", reftypeid);
                        jsonSuspMaster.put("PAT", "");
                        jsonSuspMaster.put("PBT", "");
                        jsonSuspMaster.put("Rating", "");
                        jsonSuspMaster.put("Network", "");
                        jsonSuspMaster.put("Borrowings", "");
                        jsonSuspMaster.put("GSTState", "");
                        jsonSuspMaster.put("GSTCode", edt_gstn.getText().toString());
                        jsonSuspMaster.put("TANNo", edt_TANNO.getText().toString());
                        jsonSuspMaster.put("TANNoName", edt_TANNONAME.getText().toString());
                        jsonSuspMaster.put("FKStateId", Stateid);
                        jsonSuspMaster.put("FKCountryId", Countryid);
                        jsonSuspMaster.put("ProspectType", ProspectTypeID);

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


                        jsonSuspMaster.put("val1", val1);
                        jsonSuspMaster.put("val2", val2);
                        jsonSuspMaster.put("val3", val3);
                        jsonSuspMaster.put("val4", val4);
                        jsonSuspMaster.put("val5", val5);
                        jsonSuspMaster.put("val6", val6);
                        jsonSuspMaster.put("val7", val7);
                        jsonSuspMaster.put("val8", val8);
                        jsonSuspMaster.put("val9", val9);
                        jsonSuspMaster.put("val10", val10);
                        jsonSuspMaster.put("sex", sex);
                        jsonSuspMaster.put("District", district);
                        jsonSuspMaster.put("Village", village);


                    } catch (JSONException e) {

                    }

                    JSONObject jsonData = new JSONObject();

                    try {
                        JSONArray ob = new JSONArray();
                        JSONObject j = new JSONObject(jsonSuspMaster.toString());
                        ob.put(j);
                        jsonData.put("SuspMaster", ob);

                        JSONArray obj1 = new JSONArray();
                        for (int i = 0; i < Customer_contact.length; i++) {

                            JSONObject a = new JSONObject(Customer_contact[i]);
                            obj1.put(a);
                        }
                        jsonData.put("SuspContactDetails", obj1);

                        JSONArray obj = new JSONArray();

                        JSONObject a2 = null;

                        a2 = new JSONObject(product);
                        obj.put(a2);
                        jsonData.put("SuspProdDetails", obj);

                        jsonData.put("EnquiryRegistryId", registeryID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finaljson = jsonData.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");
                    if (ut.isNet(context)) {
                        showProgressDialog();

                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostProspectUpdate_savenstartJSON().execute(finaljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_product_salesfamily = (String) spinner_product.getSelectedItem();

                String query = "SELECT distinct FamilyDesc,FamilyId" +//FamilyId,
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
                lstTurnover = new ArrayList<String>();


                if (spinner_currency.getSelectedItemPosition() == 0) {
                    lstTurnover.add("Lac");
                    lstTurnover.add("Cr");
                } else if (spinner_currency.getSelectedItemPosition() == 1) {
                    lstTurnover.add("Bill");
                    lstTurnover.add("Mill");
                }

                MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                        R.layout.crm_custom_spinner_txt, lstTurnover);
                spinner_turnover.setAdapter(customDept);


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
                        " WHERE CustVendorName='" + string_refrenceoption + "'";
                Cursor cur = sql.rawQuery(query, null);
                // lstReference.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        Referenceid = cur.getString(cur.getColumnIndex("CustVendorMasterId"));

                    } while (cur.moveToNext());

                } else {
                    Referenceid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Departmentrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_DepartmentRole = (String) spinner_Departmentrole.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_turnover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_turnover = (String) spinner_turnover.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sConsignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_entityconsinee = (String) sConsignee.getSelectedItem();
                String query = "SELECT distinct ShipToMasterId" +
                        " FROM " + db.TABLE_Consignee +
                        " WHERE ConsigneeName='" + string_entityconsinee + "'";
                Cursor cur = sql.rawQuery(query, null);
                //   lstConsignee.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        Consigneeid = cur.getString(cur.getColumnIndex("ShipToMasterId"));

                    } while (cur.moveToNext());

                } else {
                    Consigneeid = "";
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


        buttonSave_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //getId();
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        Conttact_name = edt_ContactName.getText().toString();
                        Designation = edt_ContactDesignation.getText().toString();
                        Contact_emailid = edt_contactEmailid.getText().toString();
                        Contact_mobile = edt_contactMobile.getText().toString();
                        Contact_telephone = edt_ContactTele.getText().toString();
                        Contact_DateBirth = edt_Birthdate.getText().toString();
                        Contact_Depatment = (String) spinner_Departmentrole.getSelectedItem();
                        Contact_Fax = edt_Contactskype.getText().toString();
                        Whatsapp_no = edt_contactwhatsapp.getText().toString();
                        addContact(Conttact_name, Contact_mobile);
                        lstContact.add(new ProspectContact(Conttact_name, Designation, Contact_Depatment,
                                Contact_DateBirth, Contact_emailid, Contact_telephone, Contact_mobile, Contact_Fax, Whatsapp_no));
                        Customer_contact = new String[lstContact.size()];
                        for (int i = 0; i < lstContact.size(); i++) {
                            jsoncontact.put("ContactName", Conttact_name);
                            jsoncontact.put("Designation", Designation);
                            jsoncontact.put("EmailId", Contact_emailid);
                            jsoncontact.put("Mobile", Contact_mobile);
                            jsoncontact.put("Telephone", Contact_telephone);
                            jsoncontact.put("DateofBirth", Contact_DateBirth);
                            jsoncontact.put("ContactPersonDept", Contact_Depatment);
                            jsoncontact.put("Fax", Contact_Fax);
                            jsoncontact.put("WhatsAppNo", Whatsapp_no);
                            Customer_contact[i] = jsoncontact.toString();
                        }
                    } catch (Exception e) {

                    }

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
                        jsonBusinessprospect.put("FirmName", atxt_firmname.getText().toString());
                        jsonBusinessprospect.put("Address", edt_Address.getText().toString());
                        jsonBusinessprospect.put("FirmAlias", edt_firmalise.getText().toString());
                        jsonBusinessprospect.put("FKCityId", Cityid);
                        jsonBusinessprospect.put("FKTerritoryId", Territoryid);
                        jsonBusinessprospect.put("FKBusiSegmentId", BusDetailid);
                        jsonBusinessprospect.put("CompanyURL", CompanyURL);
                        jsonBusinessprospect.put("FKEnqSourceId", SuSpSourceId);
                        jsonBusinessprospect.put("Fax", "");
                        jsonBusinessprospect.put("Notes", edt_Notes.getText().toString());
                        jsonBusinessprospect.put("Remark", edt_Notes.getText().toString());
                        jsonBusinessprospect.put("Department", string_DepartmentRole);
                        jsonBusinessprospect.put("BusinessDetails", BusDetailid);
                        jsonBusinessprospect.put("CurrencyMasterId", Currencyid);
                        jsonBusinessprospect.put("CurrencyDesc", string_currency);
                        jsonBusinessprospect.put("Turnover", edt_turnover.getText().toString());
                        jsonBusinessprospect.put("NoOfEmployees", edt_EmployeeStrength.getText().toString());
                        jsonBusinessprospect.put("NoOfOffices", edt_OfficeCnt.getText().toString());
                        jsonBusinessprospect.put("LeadGivenBYId", Referenceid);
                        jsonBusinessprospect.put("FKConsigneeId", Consigneeid);
                        jsonBusinessprospect.put("FKCustomerId", custvid);
                        jsonBusinessprospect.put("EntityType", reftypeid);
                        jsonBusinessprospect.put("PBT", "");
                        jsonBusinessprospect.put("Rating", "");
                        jsonBusinessprospect.put("Network", "");
                        jsonBusinessprospect.put("Borrowings", "");
                        jsonBusinessprospect.put("FKStateId", Stateid);
                        jsonBusinessprospect.put("FKCountryId", Countryid);
                        jsonBusinessprospect.put("GSTState", "");
                        jsonBusinessprospect.put("GSTCode", edt_gstn.getText().toString());
                        jsonBusinessprospect.put("TANNo", edt_TANNO.getText().toString());
                        jsonBusinessprospect.put("TANNoName", edt_TANNONAME.getText().toString());
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
                        for (int i = 0; i < Customer_contact.length; i++) {

                            JSONObject a = new JSONObject(Customer_contact[i]);
                            obj1.put(a);
                        }
                        jsonData.put("SuspContactDetails", obj1);


                        JSONArray obj = new JSONArray();
                        JSONObject a2 = null;

                        a2 = new JSONObject(product);
                        obj.put(a2);

                        jsonData.put("SuspProdDetails", obj);
                        jsonData.put("EnquiryRegistryId", registeryID);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    // FinalArray[0]
                    finaljson = jsonData.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");
                    //   finaljson = finaljson.replaceAll(" ", "");

                    String fName = atxt_firmname.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy HH:mm");
                    String date = sdf.format(new Date());
                    String remark1 = "Promotional form Added for firm " + fName + " on" + date;
                    String url = CompanyURL + WebUrlClass.api_Post_Prospect;

                    String op = "";
                    CreateOfflineIntend(url, finaljson, WebUrlClass.POSTFLAG, remark1, op);
                   /* if (ut.isNet(context)) {
                        showProgressDialog();
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostProspectUpdateJSON().execute(finaljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                dismissProgressDialog();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            }
                        });
                    }*/


                }
            }
        });


        ((TextView) findViewById(R.id.btnSaveContact1))
                .setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                Contact_mobile = edt_contactMobile.getText().toString();
                                Conttact_name = edt_ContactName.getText().toString();

                                addContact(Conttact_name, Contact_mobile);
                                if (validate()) {
                                    AddContactPopup();

                                }
                            }
                        });

        ((LinearLayout) findViewById(R.id.llAddContact1))
                .setVisibility(View.GONE);

        ((TextView) findViewById(R.id.btnSaveContact))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (validateSaveContact()) {
                            Contact_mobile = edt_contactMobile.getText().toString();
                            Conttact_name = edt_ContactName.getText().toString();

                            addContact(Conttact_name, Contact_mobile);

                            lstContact.clear();
                            String cname = edt_ContactName.getText().toString();
                            String cDesignation = edt_ContactDesignation.getText().toString();
                            String cBirthdate = edt_Birthdate
                                    .getText().toString();
                            String cDept = (String) spinner_Departmentrole
                                    .getSelectedItem();
                            String cEmailId = edt_contactEmailid
                                    .getText().toString();
                            String cMobile = edt_contactMobile
                                    .getText().toString();
                            String cTele = edt_ContactTele.getText().toString();
                            String cFax = edt_Contactskype
                                    .getText().toString();
                            String cWhtsapp = edt_contactwhatsapp.getText().toString();

                            lstContact.add(new ProspectContact(cname, cDesignation,
                                    cDept, cBirthdate, cEmailId, cTele, cMobile,
                                    cFax, cWhtsapp));

                            ((LinearLayout) findViewById(R.id.llAddContact))
                                    .setVisibility(View.GONE);
                            ((LinearLayout) findViewById(R.id.llAddContact1))
                                    .setVisibility(View.VISIBLE);
                            ListView listView = (ListView) findViewById(R.id.listContacts);
                            listView.setAdapter(new CustomAdapterContactList(
                                    context, lstContact));
                            //   Utility.setListViewHeightBasedOnItems(listView);

                        }
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


        atxt_firmname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    final String pass = s.toString();


                    if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
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


                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
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
        spinner_Entity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                custvid = "";
                string_entity = (String) spinner_Entity.getSelectedItem();
                String query = "SELECT distinct CustVendorMasterId,CustVendorName" +
                        " FROM " + db.TABLE_Entity +
                        " WHERE CustVendorName='" + string_entity + "'";

                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        custvid = cur.getString(cur.getColumnIndex("CustVendorMasterId"));


                    } while (cur.moveToNext());

                }
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadConsigneeJSON().execute(custvid);
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


        edt_Birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               /* String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);*/
                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);
                                edt_Birthdate.setText(Contact_DateBirth);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();

            }
        });

        img_birth_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               /* String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);*/
                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);
                                edt_Birthdate.setText(Contact_DateBirth);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

    }

    public boolean validateSaveContact() {
        String string = edt_ContactDesignation.getText().toString();
        try {
            Boolean tr = edt_ContactDesignation.getText().length() < 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        email = edt_contactEmailid.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // TODO Auto-generated method stub
        if ((atxt_firmname.getText().toString().equalsIgnoreCase("") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(" ") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(null)) && isMadatory_Firmname) {
            if (!isVisible_Firmname) {
                atxt_firmname.setVisibility(View.VISIBLE);
                ln_firmname.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter firm name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactName.getText().toString().equalsIgnoreCase("") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(null)) && isMadatory_Contact) {
            if (!isVisible_Contact) {
                ln_Contact.setVisibility(View.VISIBLE);
                edt_ContactName.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactDesignation.getText().toString().equalsIgnoreCase("") ||
                edt_ContactDesignation.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactDesignation.getText().toString().equalsIgnoreCase(null)) && isMadatory_Designation) {
            Boolean val = isVisible_Designation;
            if (!isVisible_Designation) {
                edt_ContactDesignation.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter contact designation", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_DepartmentRole.equalsIgnoreCase("") ||
                string_DepartmentRole.equalsIgnoreCase("--Select Role--") ||
                string_DepartmentRole.equalsIgnoreCase(null)) && isMadatory_Role) {
            if (!isVisible_Role) {
                ln_Deptrole.setVisibility(View.VISIBLE);
                spinner_Departmentrole.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Select Department role", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactEmailid.getText().toString().equalsIgnoreCase("") ||
                edt_contactEmailid.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactEmailid.getText().toString().equalsIgnoreCase(null) ||
                !(email.matches(emailPattern))) && isMadatory_EmailID) {
            if (!isVisible_EmailID) {
                edt_contactEmailid.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter email address or valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactMobile.getText().toString().equalsIgnoreCase("") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(null) ||
                edt_contactMobile.getText().length() != 10) && isMadatory_Mobile) {
            if (!isVisible_Mobile) {
                edt_contactMobile.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter mobile No. or valid mobile No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactwhatsapp.getText().toString().equalsIgnoreCase("") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(null) ||
                edt_contactwhatsapp.getText().length() != 10) && isMadatory_WhatsappNo) {
            if (!isVisible_WhatsappNo) {
                edt_contactwhatsapp.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter Whatsaap No. or valid Whatsaap No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactTele.getText().toString().equalsIgnoreCase("") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(null)) && isMadatory_Telephone) {
            if (!isVisible_Telephone) {
                edt_ContactTele.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter Telephone", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Contactskype.getText().toString().equalsIgnoreCase("") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(" ") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(null)) && isMadatory_Skype) {
            if (!isVisible_Skype) {
                edt_Contactskype.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter skype No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Birthdate.getText().toString().equalsIgnoreCase("") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(" ") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(null)) && isMadatory_Dateofbirth) {
            if (!isVisible_Dateofbirth) {
                ln_birthday.setVisibility(View.VISIBLE);
                edt_Birthdate.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Select contact Birth date", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    public boolean validate() {
        email = edt_contactEmailid.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // TODO Auto-generated method stub
        if ((atxt_firmname.getText().toString().equalsIgnoreCase("") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(" ") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(null)) && isMadatory_Firmname) {
            if (!isVisible_Firmname) {
                atxt_firmname.setVisibility(View.VISIBLE);
                ln_firmname.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter firm name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_firmalise.getText().toString().equalsIgnoreCase("") ||
                edt_firmalise.getText().toString().equalsIgnoreCase(" ") ||
                edt_firmalise.getText().toString().equalsIgnoreCase(null)) && isMadatory_FirmAlias) {
            if (!isVisible_FirmAlias) {
                edt_firmalise.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter firm alise", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_entity.equalsIgnoreCase("") ||
                string_entity.equalsIgnoreCase(" ") ||
                string_entity.equalsIgnoreCase(null)) && isMadatory_entity) {
            if (!isVisible_entity) {
                ln_Entity.setVisibility(View.VISIBLE);
                rl_entity.setVisibility(View.VISIBLE);
                checkbox_entity.setChecked(true);
            }
            Toast.makeText(context, "Please select entity", Toast.LENGTH_LONG).show();
            return false;
        } else if (((edt_Address.getText().toString().equalsIgnoreCase(""))
                || edt_Address.getText().toString().equalsIgnoreCase(" ")) && isMadatory_Address) {
            if (!isVisible_Address) {
                edt_Address.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter address", Toast.LENGTH_LONG).show();
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
        } else if ((edt_gstn.getText().toString().equalsIgnoreCase("") ||
                edt_gstn.getText().toString().equalsIgnoreCase(" ") ||
                edt_gstn.getText().toString().equalsIgnoreCase(null) || edt_gstn.getText().toString().length() < 15) && isMadatory_GSTN) {
            if (!isVisible_GSTN) {
                edt_gstn.setVisibility(View.VISIBLE);
            }

            Toast.makeText(context, "Please enter valid GSTN No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_TANNO.getText().toString().equalsIgnoreCase("") ||
                edt_TANNO.getText().toString().equalsIgnoreCase(" ") ||
                edt_TANNO.getText().toString().equalsIgnoreCase(null)) && isMadatory_TANno) {
            if (!isVisible_TANno) {
                edt_TANNO.setVisibility(View.VISIBLE);
            }

            Toast.makeText(context, "Please TAN No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_TANNONAME.getText().toString().equalsIgnoreCase("") ||
                edt_TANNONAME.getText().toString().equalsIgnoreCase(" ") ||
                edt_TANNONAME.getText().toString().equalsIgnoreCase(null)) && isMadatory_TANname) {
            if (!isVisible_TANname) {
                edt_TANNONAME.setVisibility(View.VISIBLE);
            }

            Toast.makeText(context, "Please enter TAN No.", Toast.LENGTH_LONG).show();
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
        } else if ((edt_website.getText().toString().equalsIgnoreCase("") ||
                edt_website.getText().toString().equalsIgnoreCase(" ") ||
                edt_website.getText().toString().equalsIgnoreCase(null)) && isMadatory_Website) {
            if (!isVisible_Website) {
                edt_website.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter website", Toast.LENGTH_LONG).show();
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
        } else if ((edt_Notes.getText().toString().equalsIgnoreCase("") ||
                edt_Notes.getText().toString().equalsIgnoreCase(" ") ||
                edt_Notes.getText().toString().equalsIgnoreCase(null)) && isMadatory_Notes) {
            if (!isVisible_Notes) {
                ln_Notes.setVisibility(View.VISIBLE);
                edt_Notes.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter Notes", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactName.getText().toString().equalsIgnoreCase("") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(null)) && isMadatory_Contact) {
            if (!isVisible_Contact) {
                ln_Contact.setVisibility(View.VISIBLE);
                edt_ContactName.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactDesignation.getText().toString().equalsIgnoreCase("") ||
                edt_ContactDesignation.getText().toString().equalsIgnoreCase(" ")) && isMadatory_Designation) {
            Boolean val = isVisible_Designation;
            if (!val) {
                edt_ContactDesignation.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Please enter contact designation", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_DepartmentRole.equalsIgnoreCase("") ||
                string_DepartmentRole.equalsIgnoreCase("--Select Role--") ||
                string_DepartmentRole.equalsIgnoreCase(null)) && isMadatory_Role) {
            if (!isVisible_Role) {
                ln_Deptrole.setVisibility(View.VISIBLE);
                spinner_Departmentrole.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Select Department role", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactEmailid.getText().toString().equalsIgnoreCase("") ||
                edt_contactEmailid.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactEmailid.getText().toString().equalsIgnoreCase(null) ||
                !(email.matches(emailPattern))) && isMadatory_EmailID) {
            if (!isVisible_EmailID) {
                edt_contactEmailid.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter email address or valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactMobile.getText().toString().equalsIgnoreCase("") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(null) ||
                edt_contactMobile.getText().length() != 10) && isMadatory_Mobile) {
            if (!isVisible_Mobile) {
                edt_contactMobile.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter mobile No. or valid mobile No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactwhatsapp.getText().toString().equalsIgnoreCase("") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(null) ||
                edt_contactwhatsapp.getText().length() != 10) && isMadatory_WhatsappNo) {
            if (!isVisible_WhatsappNo) {
                edt_contactwhatsapp.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter Whatsaap No. or valid Whatsaap No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactTele.getText().toString().equalsIgnoreCase("") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(null)) && isMadatory_Telephone) {
            if (!isVisible_Telephone) {
                edt_ContactTele.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter Telephone", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Contactskype.getText().toString().equalsIgnoreCase("") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(" ") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(null)) && isMadatory_Skype) {
            if (!isVisible_Skype) {
                edt_Contactskype.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter skype No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Birthdate.getText().toString().equalsIgnoreCase("") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(" ") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(null)) && isMadatory_Dateofbirth) {
            if (!isVisible_Dateofbirth) {
                ln_birthday.setVisibility(View.VISIBLE);
                edt_Birthdate.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Select contact Birth date", Toast.LENGTH_LONG).show();
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
        } else if ((edt_OfficeCnt.getText().toString().equalsIgnoreCase("") ||
                edt_OfficeCnt.getText().toString().equalsIgnoreCase(" ") ||
                edt_OfficeCnt.getText().toString().equalsIgnoreCase(null)) && isMadatory_NoofOffices) {
            if (!isVisible_NoofOffices) {
                edt_OfficeCnt.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter No. of offices", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_EmployeeStrength.getText().toString().equalsIgnoreCase("") ||
                edt_EmployeeStrength.getText().toString().equalsIgnoreCase(" ") ||
                edt_EmployeeStrength.getText().toString().equalsIgnoreCase(null)) && isMadatory_NoofEmployees) {
            if (!isVisible_NoofEmployees) {
                edt_EmployeeStrength.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter No. of employees", Toast.LENGTH_LONG).show();
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

        } else if ((edt_turnover.getText().toString().equalsIgnoreCase("") ||
                edt_turnover.getText().toString().equalsIgnoreCase(" ") ||
                edt_turnover.getText().toString().equalsIgnoreCase(null)) && isMadatory_Turnover) {
            if (!isVisible_Turnover) {
                ln_turnover.setVisibility(View.VISIBLE);
                spinner_turnover.setVisibility(View.VISIBLE);
                edt_turnover.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter turnover", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_businessDetails.getText().toString().equalsIgnoreCase("") ||
                edt_businessDetails.getText().toString().equalsIgnoreCase(" ") ||
                edt_businessDetails.getText().toString().equalsIgnoreCase(null)) && isMadatory_BusinessDetails) {
            if (!isVisible_BusinessDetails) {
                edt_businessDetails.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter Business Details", Toast.LENGTH_LONG).show();
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

    private void AddContactPopup() {
        d_contact = new Dialog(context);
        d_contact.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_contact.setContentView(R.layout.crm_popupaddcontact);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d_contact.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        d_contact.getWindow().setAttributes(lp);


        final Button btnSave = ((Button) d_contact
                .findViewById(R.id.btnpopupSaveContact));
        final Button btnCancel = ((Button) d_contact
                .findViewById(R.id.btnpopupCancelContact));
        final EditText eName = ((EditText) d_contact
                .findViewById(R.id.eContactName));
        final EditText eDesignation = ((EditText) d_contact
                .findViewById(R.id.eContactDesignation));
        final EditText eBirthdate = ((EditText) d_contact
                .findViewById(R.id.eBirthdate));
        final Spinner sDepartmnt = ((Spinner) d_contact
                .findViewById(R.id.sDepartment));

        final EditText cEmailId = (EditText) d_contact
                .findViewById(R.id.econtactpEmailid);
        final EditText cMobilee = (EditText) d_contact
                .findViewById(R.id.econtactpMobile);
        final EditText cTele = (EditText) d_contact
                .findViewById(R.id.eContactpTele);
        final EditText cFaxe = (EditText) d_contact
                .findViewById(R.id.eContactpFax);
        final EditText econtactwhatsapp = (EditText) d_contact.findViewById(R.id.econtactwhatsapp);

        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstDept);
        sDepartmnt.setAdapter(customDept);

        dfDate = new SimpleDateFormat("yyy/MM/dd");
        CurrentDate = dfDate.format(new Date());
        eBirthdate.setText(CurrentDate);

       /* ((TextView) d_contact.findViewById(R.id.popupHead))
                .setTypeface(typeFace);*/

        eBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                               /* String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);*/

                                eBirthdate.setText(year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String cname = eName.getText().toString();
                String cDesignation = eDesignation.getText().toString();
                String cBirthdate = eBirthdate.getText().toString();
                String cDept = sDepartmnt.getSelectedItem().toString();
                String cemailid = cEmailId.getText().toString().trim();
                String ctele = cTele.getText().toString().trim();
                String cMobile = cMobilee.getText().toString().trim();
                String cFax = cFaxe.getText().toString().trim();
                String cWhtsapp = econtactwhatsapp.getText().toString().trim();

                Log.e("lstContact ", "size : " + lstContact.size());
                lstContact.add(new ProspectContact(cname, cDesignation, cDept,
                        cBirthdate, cemailid, ctele, cMobile, cFax, cWhtsapp));
                Log.e("lstContact ", "after size : " + lstContact.size());
                ListView listView = (ListView) findViewById(R.id.listContacts);
                listView.setAdapter(new CustomAdapterContactList(
                        context, lstContact));
                //   Utility.setListViewHeightBasedOnItems(listView);


                LinearLayout layout = ((LinearLayout) findViewById(R.id.llContact));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layout
                        .getLayoutParams();
                lp.height = lstContact.size() * 100;
                d_contact.cancel();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d_contact.cancel();

            }
        });

        d_contact.show();


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


        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, currency);

        spinner_currency.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        spinner_currency.setSelection(0);


        lstTurnover = new ArrayList<String>();
        lstTurnover.clear();

        if (spinner_currency.getSelectedItemPosition() == 0) {
            lstTurnover.add("Lac");
            lstTurnover.add("Cr");
        } else if (spinner_currency.getSelectedItemPosition() == 1) {
            lstTurnover.add("Bill");
            lstTurnover.add("Mill");
        }

        MySpinnerAdapter custom = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstTurnover);
        spinner_turnover.setAdapter(custom);
    }


    private void getEntity() {
        lstEntity.clear();
        String query = "SELECT distinct CustVendorMasterId,CustVendorName" +
                " FROM " + db.TABLE_Entity;
        Cursor cur = sql.rawQuery(query, null);
        //  lstEntity.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstEntity.add(cur.getString(cur.getColumnIndex("CustVendorName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstEntity);
        spinner_Entity.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        spinner_Entity.setSelection(0);


    }

    private void getConsignee() {
        //sReferenceType, sReference,sEntity,sConsignee;
        lstConsignee.clear();
        String query = "SELECT distinct ConsigneeName,ShipToMasterId" +
                " FROM " + db.TABLE_Consignee;
        Cursor cur = sql.rawQuery(query, null);
        //   lstConsignee.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                lstConsignee.add(cur.getString(cur.getColumnIndex("ConsigneeName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstConsignee);
        sConsignee.setAdapter(customDept);
        // customDept.notifyDataSetChanged();
        sConsignee.setSelection(0);
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstReference);
        spinner_refrenceoption.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        spinner_refrenceoption.setSelection(0);
    }

    private void getReferencetype() {
        lstReferenceType.clear();
        String query = "SELECT distinct CustVendor,CustVendorCode" +
                " FROM " + db.TABLE_Referencetype;
        Cursor cur = sql.rawQuery(query, null);
        lstReferenceType.add("No Reference");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstReferenceType.add(cur.getString(cur.getColumnIndex("CustVendorCode")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstReferenceType);
        spinner_reference.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_reference.setSelection(0);
    }


    /*@SuppressLint("NewApi")
    public void ExpandNameClick(View v) {
        if (((LinearLayout) findViewById(R.id.llName)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llName)).setVisibility(View.GONE);
           *//* ((ImageView) findViewById(R.id.ExpandName))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.expand));*//*

            ((ImageView) findViewById(R.id.ExpandName)).setBackgroundResource(R.drawable.ic_add);
        } else {
            ((LinearLayout) findViewById(R.id.llName))
                    .setVisibility(View.VISIBLE);
          *//*  ((ImageView) findViewById(R.id.ExpandName))
                    .setBackground(getResources().getDrawable(
                            R.drawable.collapse));*//*
            ((ImageView) findViewById(R.id.ExpandName)).setBackgroundResource(R.drawable.ic_minus);
        }
        String fname = ((AutoCompleteTextView) findViewById(R.id.efName1))
                .getText().toString().trim();
        if (fname.isEmpty())
            ((TextView) findViewById(R.id.headName)).setText("Name & Address");
        else
            ((TextView) findViewById(R.id.headName)).setText(fname);
    }

    @SuppressLint("NewApi")
    public void ExpandBusinessDetailsClick(View v) {
        if (((LinearLayout) findViewById(R.id.llBusinessDetails))
                .getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llBusinessDetails))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandBusinessDetails))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llBusinessDetails))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandBusinessDetails))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }

    @SuppressLint("NewApi")
    public void ProspectClick(View v) {
        if (((LinearLayout) findViewById(R.id.llProspect)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llProspect))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandProspect))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llProspect))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandProspect))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }

    @SuppressLint("NewApi")
    public void EntityClick(View v) {
        if (((LinearLayout) findViewById(R.id.llEntity)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llEntity))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandEntity))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llEntity))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandEntity))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }

    @SuppressLint("NewApi")
    public void ContactClick(View v) {
        if (((LinearLayout) findViewById(R.id.llContact)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llContact))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandContact))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llContact))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandContact))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }

    @SuppressLint("NewApi")
    public void ProductClick(View v) {
        if (((LinearLayout) findViewById(R.id.llProduct)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llProduct))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandProduct))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llProduct))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandProduct))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }

    @SuppressLint("NewApi")
    public void NotesClick(View v) {
        if (((LinearLayout) findViewById(R.id.llNotes)).getVisibility() == View.VISIBLE) {
            ((LinearLayout) findViewById(R.id.llNotes))
                    .setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.ExpandNotes))
                    .setBackground(getResources()
                            .getDrawable(R.drawable.ic_add));
        } else {
            ((LinearLayout) findViewById(R.id.llNotes))
                    .setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.ExpandNotes))
                    .setBackground(getResources().getDrawable(
                            R.drawable.ic_minus));
        }
    }*/

    private void showProgressDialog() {

        progressbar.setVisibility(View.VISIBLE);

    }

    private void dismissProgressDialog() {

        progressbar.setVisibility(View.GONE);

    }

  /*  private void showProgressDialog1() {

        ProgressDialog progressDialog = new ProgressDialog(ProspectEnterpriseActivity.this);
        progressDialog.setCancelable(true);
        if (!isFinishing()) {
            progressDialog.show();
        }
        progressDialog.setContentView(R.layout.crm_progress_lay);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }*/
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
          Intent intent1 = new Intent(ProspectEnterpriseActivity.this,
                  SendOfflineData.class);
          intent1.putExtra("flag", "direct");
          startService(intent1);
          Intent intent2 = new Intent(ProspectEnterpriseActivity.this,
                  CallListActivity.class);
          intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          startActivity(intent2);
          finish();
      } else {
          Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
      }

  }




    class DownloadFirmJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error", name, id;
        String a[], b[];
        List<String> suggestions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            suggestions = new ArrayList<String>();
            try {
                String url = CompanyURL + WebUrlClass.api_get_Firm +
                        "?SearchText=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
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
                            dismissProgressDialog();
                            MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                                    R.layout.crm_custom_spinner_txt, suggestions);


                            ((AutoCompleteTextView) findViewById(R.id.efName1))
                                    .setAdapter(customAdcity);
                            customAdcity.notifyDataSetChanged();
                            ((AutoCompleteTextView) findViewById(R.id.efName1)).setThreshold(1);
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            dismissProgressDialog();
        }

    }

    private void setautocomplete() {
        lstfirm.clear();

        List<Firmbean> lstdb = cf.getFirmbean();

        for (int i = 0; i < lstdb.size(); i++)
            lstfirm.add(lstdb.get(i).getName());

        ProspectEnterpriseActivity.this.runOnUiThread(new Runnable() {
            public void run() {

                MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                        R.layout.crm_custom_spinner_txt, lstfirm);


                ((AutoCompleteTextView) findViewById(R.id.efName1))
                        .setAdapter(customAdcity);
                customAdcity.notifyDataSetChanged();
                ((AutoCompleteTextView) findViewById(R.id.efName1)).setThreshold(1);
            }
        });


    }

    private void setautocomplete_teritory() {

        List<Teritorybean> lstdb = cf.getTeritorybean();
        lstTerrority.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstTerrority.add(lstdb.get(i).getTerritoryName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstTerrority);

        spinner_territory.setAdapter(customAdcity);
        int a = lstTerrority.indexOf(Territory_name);
        spinner_territory.setSelection(lstTerrority.indexOf(Territory_name));


    }

    class DownloadDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error", name, id;
        String a[], b[];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + "/Ekatm/getresx";

            try {
                res = ut.OpenConnection(url);
                response = res.toString();
                JSONArray jResults = new JSONArray(response);
                ContentValues values = new ContentValues();

                sql.delete(db.TABLE_Setup_Prospect, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Setup_Prospect, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Setup_Prospect, null, values);

                }
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("null")) {
                // autotxtBusinessSegment.setHint("Businesssegment");
                //     efName1.setHint("Firm name");
                //   efAlias.setHint("Firm alias");
                if (cf.getCitycount() > 0) {
                    getCity();
                } else {
                    if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
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


            } else {
                //autotxtBusinessSegment.setHint(setup("Businesssegment"));
                //efName1.setHint(setup("Firmname"));
                //efAlias.setHint(setup("Firmalias"));
                if (cf.getCitycount() > 0) {
                    getCity();
                } else {
                    if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
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

            }


            if (cf.getCitycount() > 0) {
                getCity();
            } else {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
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

        }

    }

    public String setup(String vname) {
        Cursor cursor1 = sql.rawQuery("Select distinct Key,value from "
                + db.TABLE_Setup_Prospect + " where Key='" + vname + "'", null);
        Log.d("test", "" + cursor1.getCount());
        String key;
        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            key = cursor1.getString(cursor1.getColumnIndex("value"));
            return key;

        } else {
            key = vname;
        }
        return null;
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


    private void getCity() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstCity.add(lstdb.get(i).getCityName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstCity);
        spinner_city.setAdapter(customAdcity);
        int a = lstCity.indexOf(City_name);
        spinner_city.setSelection(lstCity.indexOf(City_name));


    }

    private void setautocomplete_BusinessSegment() {

        List<BusinessSegmentbean> lstdb = cf.getBusinessSegmentbean();
        lstBusinessSegment.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstBusinessSegment.add(lstdb.get(i).getSegmentDescription());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectEnterpriseActivity.this,
                R.layout.crm_custom_spinner_txt, lstBusinessSegment);
        spinner_BusinessSegment.setAdapter(customAdcity);
        int a = lstBusinessSegment.indexOf(BusDetail_Segment_name);
        spinner_BusinessSegment.setSelection(lstBusinessSegment.indexOf(BusDetail_Segment_name));


    }

    private void setautocomplete_prospect() {

        List<ProspectsourceBean> lstdb = cf.getProspectsourceBean();
        lstSourceProspect.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstSourceProspect.add(lstdb.get(i).getSourceName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectEnterpriseActivity.this,
                R.layout.crm_custom_spinner_txt, lstSourceProspect);
        spinner_source.setAdapter(customAdcity);
        int a = lstSourceProspect.indexOf(Source_prospect);
        spinner_source.setSelection(lstSourceProspect.indexOf(Source_prospect));


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
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
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
            if (response.contains("PKBusiSegmentID")) {
                setautocomplete_BusinessSegment();
            }

        }

    }

    private void validationAsync() {
        if (ut.isNet(context)) {
            showProgressDialog();
            new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
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

    class DownloadterritoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
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
                if (res != null) {
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
                                    jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                                    long DOB_date = Long.parseLong(jsonAddeddt);
                                    DOBDate = new Date(DOB_date);
                                    jsonAddeddt = sdf.format(DOBDate);
                                }
                                values.put(columnName, jsonAddeddt);

                            } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                                if (!((jsonModifiedDt.equalsIgnoreCase(null)) ||
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
                        Log.e("Territory", "");

                    }
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
            if (response.contains("PKTerritoryId")) {
                setautocomplete_teritory();
            }

        }

    }

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_city + "?PKCityID=" +
                        URLEncoder.encode("", "UTF-8") + "";

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
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

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("PKCityID")) {
                getCity();
            }

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
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_ProspectSource;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("(\r\n|\n)", "");

                    response = response.replaceAll("u0026", "&");
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            setautocomplete_prospect();


        }

    }

    class DownloadReferencetypeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_Referencetype;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
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
            if (response.contains("CustVendor")) {
                getReferencetype();
            }

        }

    }

    class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Reference +
                        "?LeadWise=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
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
                response = "error";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            dismissProgressDialog();
            if (response.contains("CustVendorMasterId")) {
                getReference();
            }

        }

    }

    class DownloadEntityJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            // progressHUD3 = ProgressHUD.show(context, " ", false, false, null);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_get_fill_entity;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Entity, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Entity, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Entity, null, values);
                        Log.e("Entry", "" + a);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            dismissProgressDialog();
            if (response.contains("CustVendorMasterId")) {
                getEntity();
            } else {
                Toast.makeText(getApplicationContext(), "Error in fetching entity", Toast.LENGTH_LONG).show();
            }

        }

    }

    class DownloadConsigneeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_fill_consignee
                        + "?ddlEntity=" + URLEncoder.encode(params[0], "UTF-8");


                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Consignee, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Consignee, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Consignee, null, values);
                        Log.e("consignee", "" + a);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
        /*    if (progressHUD4 != null && progressHUD4.isShowing()) {
                progressHUD4.dismiss();
            }*/
            dismissProgressDialog();
            if (response.contains("ShipToMasterId")) {
                getConsignee();
            }
        }

    }

    class DownloadCurrencyMasterJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {

            //   progressHUD5 = ProgressHUD.show(context, " ", false, false, null);
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getCurrencyMaster;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
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

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            /*if (progressHUD5 != null && progressHUD5.isShowing()) {
                progressHUD5.dismiss();
            }*/
            getCurrency();
            dismissProgressDialog();
            if (response.contains("error")) {

            }


        }

    }

    class DownloadProductJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Integer... params) {
            // String url = CompanyURL + WebUrlClass.api_get_Product;
            String url = CompanyURL + WebUrlClass.api_get_sales_family;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
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
            if (response.contains("FamilyId")) {
                getproduct();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to load product detail. Please refresh data again or contact to support", Toast.LENGTH_LONG).show();
            }


        }

    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            try {
                res = ut.OpenPostConnection(url, params[0]);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                Log.d("Response  :", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (PKSuspectId != null) {
                Toast.makeText(context, "Prospect Updated Successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "Prospect Added Successfully", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }

    class PostProspectUpdate_savenstartJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            try {
                res = ut.OpenPostConnection(url, params[0]);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (PKSuspectId != null) {
                Toast.makeText(ProspectEnterpriseActivity.this, "Prospect Updated Successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ProspectEnterpriseActivity.this, "Prospect Added Successfully", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(context, CreateOpportunityActivity.class);

            intent.putExtra("SuspectID", integer);//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
            startActivity(intent);
            ProspectEnterpriseActivity.this.finish();
            //onBackPressed();
        }

    }

    class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_Statelist;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
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
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
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
            if (response.contains("PKStateId")) {
                getState();
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prospect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (ut.isNet(context)) {
                showProgressDialog();
                new StartSession(ProspectEnterpriseActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dismissProgressDialog();

                        new DownloadCityJSON().execute();
                        new DownloadStatelistJSON().execute();
                        new DownloadterritoryJSON().execute();
                        new DownloadEntityJSON().execute();
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
        }
        if (id == android.R.id.home) {
            onBackPressed();

        }
        if (id == R.id.actionSetting) {
            validationAsync();

        }
        if (id == R.id.action_menu)

        {
            dialog = new Dialog(ProspectEnterpriseActivity.this);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.crm_dialog_prospect_lay);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView txt_business_prospect = (TextView) dialog.findViewById(R.id.txt_business_prospect);
            TextView txt_individual_prospect = (TextView) dialog.findViewById(R.id.txt_individual_prospect);

            txt_business_prospect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (registeryID.equalsIgnoreCase("")) {
                        startActivity(new Intent(ProspectEnterpriseActivity.this, BusinessProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(ProspectEnterpriseActivity.this, BusinessProspectusActivity.class);
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
                        startActivity(new Intent(ProspectEnterpriseActivity.this, IndividualProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(ProspectEnterpriseActivity.this, IndividualProspectusActivity.class);
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
        return super.

                onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(FragmentProspectEntry.this, CallListActivity.class);
        startActivity(intent);*/
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        ProspectEnterpriseActivity.this.finish();
    }

    private void getState() {
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity.this,
                R.layout.crm_custom_spinner_txt, lstState);
        spinner_state.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        //  spinner_state.setSelection(0);
        spinner_state.setAdapter(customDept);
        int a = lstState.indexOf(Statename);
        spinner_state.setSelection(lstState.indexOf(Statename));

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

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity.this,
                R.layout.crm_custom_spinner_txt, mList);
        spinner_country.setAdapter(customDept);
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
                if (ProspectField.equalsIgnoreCase("FirmName")) {
                    atxt_firmname.setTag(PKFieldID);
                    ln_firmname.setTag(PKFieldID);
                    atxt_firmname.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        atxt_firmname.setVisibility(View.VISIBLE);
                        ln_firmname.setVisibility(View.VISIBLE);
                        isVisible_Firmname = true;
                    } else {
                        atxt_firmname.setVisibility(View.GONE);
                        ln_firmname.setVisibility(View.GONE);
                        isVisible_Firmname = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Firmname = true;
                    } else {
                        isMadatory_Firmname = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FirmAlias")) {
                    edt_firmalise.setTag(PKFieldID);
                    edt_firmalise.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_firmalise.setVisibility(View.VISIBLE);
                        isVisible_FirmAlias = true;
                    } else {
                        edt_firmalise.setVisibility(View.GONE);
                        isVisible_FirmAlias = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_FirmAlias = true;
                    } else {
                        isMadatory_FirmAlias = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Attach Entity")) {
                    ln_Entity.setTag(PKFieldID);
                    rl_entity.setTag(PKFieldID);
                    checkbox_entity.setText(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_Entity.setVisibility(View.GONE);
                        rl_entity.setVisibility(View.VISIBLE);
                        checkbox_entity.setChecked(false);
                        isVisible_entity = true;
                    } else {
                        checkbox_entity.setChecked(false);
                        ln_Entity.setVisibility(View.GONE);
                        rl_entity.setVisibility(View.GONE);
                        isVisible_entity = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_entity = true;
                    } else {
                        isMadatory_entity = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Address")) {
                    edt_Address.setTag(PKFieldID);
                    edt_Address.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_Address.setVisibility(View.VISIBLE);
                        isVisible_Address = true;
                    } else {
                        edt_Address.setVisibility(View.GONE);
                        isVisible_Address = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Address = true;
                    } else {
                        isMadatory_Address = false;
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
                } else if (ProspectField.equalsIgnoreCase("GSTN")) {
                    edt_gstn.setTag(PKFieldID);
                    edt_gstn.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_gstn.setVisibility(View.VISIBLE);
                        isVisible_GSTN = true;
                    } else {
                        edt_gstn.setVisibility(View.GONE);
                        isVisible_GSTN = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_GSTN = true;
                    } else {
                        isMadatory_GSTN = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("TANNo")) {
                    edt_TANNO.setTag(PKFieldID);
                    edt_TANNO.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_TANNO.setVisibility(View.VISIBLE);
                        isVisible_TANno = true;
                    } else {
                        edt_TANNO.setVisibility(View.GONE);
                        isVisible_TANno = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_TANno = true;
                    } else {
                        isMadatory_TANno = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("TANNoName")) {
                    edt_TANNONAME.setTag(PKFieldID);
                    edt_TANNONAME.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_TANNONAME.setVisibility(View.VISIBLE);
                        isVisible_TANname = true;
                    } else {
                        edt_TANNONAME.setVisibility(View.GONE);
                        isVisible_TANname = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_TANname = true;
                    } else {
                        isMadatory_TANname = false;
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
                } else if (ProspectField.equalsIgnoreCase("Source of Prospect")) {
                    ln_source.setTag(PKFieldID);
                    spinner_source.setTag(PKFieldID);
                    tv_source.setText(Caption + ":");
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
                } else if (ProspectField.equalsIgnoreCase("Notes")) {
                    ln_Notes.setTag(PKFieldID);
                    edt_Notes.setTag(PKFieldID);
                    edt_Notes.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_Notes.setVisibility(View.VISIBLE);
                        edt_Notes.setVisibility(View.VISIBLE);
                        isVisible_Notes = true;
                    } else {
                        ln_Notes.setVisibility(View.GONE);
                        edt_Notes.setVisibility(View.GONE);
                        isVisible_Notes = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Notes = true;
                    } else {
                        isMadatory_Notes = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Contact")) {
                    ln_Contact.setTag(PKFieldID);
                    edt_ContactName.setTag(PKFieldID);
                    edt_ContactName.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_Contact.setVisibility(View.VISIBLE);
                        edt_ContactName.setVisibility(View.VISIBLE);
                        isVisible_Contact = true;
                    } else {
                        ln_Contact.setVisibility(View.GONE);
                        edt_ContactName.setVisibility(View.GONE);
                        isVisible_Contact = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Contact = true;
                    } else {
                        isMadatory_Contact = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Designation")) {
                    edt_ContactDesignation.setTag(PKFieldID);
                    edt_ContactDesignation.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_ContactDesignation.setVisibility(View.VISIBLE);
                        isVisible_Designation = true;
                    } else {
                        edt_ContactDesignation.setVisibility(View.GONE);
                        isVisible_Designation = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Designation = true;
                    } else {
                        isMadatory_Designation = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Role")) {
                    ln_Deptrole.setTag(PKFieldID);
                    spinner_Departmentrole.setTag(PKFieldID);
                    tv_role.setText(Caption + ":");

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_Deptrole.setVisibility(View.VISIBLE);
                        spinner_Departmentrole.setVisibility(View.VISIBLE);
                        isVisible_Role = true;
                    } else {
                        ln_Deptrole.setVisibility(View.GONE);
                        isVisible_Role = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Role = true;
                    } else {
                        isMadatory_Role = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("EmailMsgId")) {
                    edt_contactEmailid.setTag(PKFieldID);
                    edt_contactEmailid.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_contactEmailid.setVisibility(View.VISIBLE);
                        isVisible_EmailID = true;
                    } else {
                        edt_contactEmailid.setVisibility(View.GONE);
                        isVisible_EmailID = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_EmailID = true;
                    } else {
                        isMadatory_EmailID = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Mobile")) {
                    edt_contactMobile.setTag(PKFieldID);
                    edt_contactMobile.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_contactMobile.setVisibility(View.VISIBLE);
                        isVisible_Mobile = true;
                    } else {
                        edt_contactMobile.setVisibility(View.GONE);
                        isVisible_Mobile = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Mobile = true;
                    } else {
                        isMadatory_Mobile = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("WhatsApp")) {
                    edt_contactwhatsapp.setTag(PKFieldID);
                    edt_contactwhatsapp.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_contactwhatsapp.setVisibility(View.VISIBLE);
                        isVisible_WhatsappNo = true;
                    } else {
                        edt_contactwhatsapp.setVisibility(View.GONE);
                        isVisible_WhatsappNo = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_WhatsappNo = true;
                    } else {
                        isMadatory_WhatsappNo = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Telephone")) {
                    edt_ContactTele.setTag(PKFieldID);
                    edt_ContactTele.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_ContactTele.setVisibility(View.VISIBLE);
                        isVisible_Telephone = true;
                    } else {
                        edt_ContactTele.setVisibility(View.GONE);
                        isVisible_Telephone = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Telephone = true;
                    } else {
                        isMadatory_Telephone = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Skype")) {
                    edt_Contactskype.setTag(PKFieldID);
                    edt_Contactskype.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_Contactskype.setVisibility(View.VISIBLE);
                        isVisible_Skype = true;
                    } else {
                        edt_Contactskype.setVisibility(View.GONE);
                        isVisible_Skype = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Skype = true;
                    } else {
                        isMadatory_Skype = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Date of birth")) {
                    ln_birthday.setTag(PKFieldID);
                    edt_Birthdate.setTag(PKFieldID);
                    edt_Birthdate.setHint(Caption);


                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_birthday.setVisibility(View.VISIBLE);
                        edt_Birthdate.setVisibility(View.VISIBLE);
                        isVisible_Dateofbirth = true;
                    } else {
                        ln_birthday.setVisibility(View.GONE);
                        edt_Birthdate.setVisibility(View.GONE);

                        isVisible_Dateofbirth = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Dateofbirth = true;
                    } else {
                        isMadatory_Dateofbirth = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Sales Family")) {
                    ln_Product.setTag(PKFieldID);
                    spinner_product.setTag(PKFieldID);
                    tv_product.setText(Caption);

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
                } else if (ProspectField.equalsIgnoreCase("NoOfOffices")) {

                    edt_OfficeCnt.setTag(PKFieldID);
                    edt_OfficeCnt.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_OfficeCnt.setVisibility(View.VISIBLE);
                        isVisible_NoofOffices = true;
                    } else {
                        edt_OfficeCnt.setVisibility(View.GONE);
                        isVisible_NoofOffices = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_NoofOffices = true;
                    } else {
                        isMadatory_NoofOffices = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("NoOfEmployees")) {

                    edt_EmployeeStrength.setTag(PKFieldID);
                    edt_EmployeeStrength.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_EmployeeStrength.setVisibility(View.VISIBLE);
                        isVisible_NoofEmployees = true;
                    } else {
                        edt_EmployeeStrength.setVisibility(View.GONE);
                        isVisible_NoofEmployees = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_NoofEmployees = true;
                    } else {
                        isMadatory_NoofEmployees = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("CurrencyDesc")) {
                    ln_currency.setTag(PKFieldID);
                    spinner_currency.setTag(PKFieldID);
                    tv_currency.setText(Caption);
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
                } else if (ProspectField.equalsIgnoreCase("Turnover")) {
                    ln_turnover.setTag(PKFieldID);
                    spinner_turnover.setTag(PKFieldID);
                    edt_turnover.setTag(PKFieldID);
                    edt_turnover.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_turnover.setVisibility(View.VISIBLE);
                        spinner_turnover.setVisibility(View.VISIBLE);
                        edt_turnover.setVisibility(View.VISIBLE);
                        isVisible_Turnover = true;
                    } else {
                        ln_turnover.setVisibility(View.GONE);
                        spinner_turnover.setVisibility(View.VISIBLE);
                        edt_turnover.setVisibility(View.GONE);
                        isVisible_Turnover = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Turnover = true;
                    } else {
                        isMadatory_Turnover = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Business Details")) {
                    edt_businessDetails.setTag(PKFieldID);
                    edt_businessDetails.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_businessDetails.setVisibility(View.VISIBLE);
                        isVisible_BusinessDetails = true;
                    } else {
                        edt_businessDetails.setVisibility(View.GONE);
                        isVisible_BusinessDetails = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_BusinessDetails = true;
                    } else {
                        isMadatory_BusinessDetails = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Village")) {
                    ln_village.setTag(PKFieldID);
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

    private void getproductfetchdetails() {

        String query = "SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                String turnover = cur.getString(cur.getColumnIndex("Turnover"));

                System.out.println("Hello :" + turnover);

                edt_turnover.setText(cur.getString(cur.getColumnIndex("Turnover")));
                edt_OfficeCnt.setText(cur.getString(cur.getColumnIndex("NoOfOffices")));
                edt_EmployeeStrength.setText(cur.getString(cur.getColumnIndex("NoOfEmployees")));
                atxt_firmname.setText(cur.getString(cur.getColumnIndex("FirmName")));
                edt_Address.setText(cur.getString(cur.getColumnIndex("Address")));
                edt_firmalise.setText(cur.getString(cur.getColumnIndex("FirmAlias")));
                edt_Notes.setText(cur.getString(cur.getColumnIndex("Notes")));


            } while (cur.moveToNext());

        }
    }

    private void getcontactfetchdetails() {
        String query = "SELECT * FROM " + db.TABLE_CONTACT_DETAILS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Conttact_name = cur.getString(cur.getColumnIndex("ContactName"));
                edt_ContactName.setText(Conttact_name);
                Contact_mobile = cur.getString(cur.getColumnIndex("Mobile"));
                edt_contactMobile.setText(Contact_mobile);
                edt_contactMobile.setText(cur.getString(cur.getColumnIndex("Mobile")));
                Contact_emailid = cur.getString(cur.getColumnIndex("EmailId"));
                edt_contactEmailid.setText(Contact_emailid);
                Designation = cur.getString(cur.getColumnIndex("Designation"));
                edt_ContactDesignation.setText(Designation);
                Contact_DateBirth = cur.getString(cur.getColumnIndex("DateofBirth"));
                Contact_DateBirth = Contact_DateBirth.substring(Contact_DateBirth.indexOf("(") + 1, Contact_DateBirth.lastIndexOf(")"));
                long DOB_date = Long.parseLong(Contact_DateBirth);
                DOBDate = new Date(DOB_date);
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                Contact_DateBirth = sd.format(DOBDate);
                edt_Birthdate.setText(Contact_DateBirth);
                edt_contactEmailid.setText(cur.getString(cur.getColumnIndex("EmailId")));
                Contact_Fax = cur.getString(cur.getColumnIndex("Fax"));
                edt_Contactskype.setText(Contact_Fax);
                Contact_telephone = cur.getString(cur.getColumnIndex("Telephone"));
                edt_ContactTele.setText(Contact_telephone);
                Contact_Depatment = cur.getString(cur.getColumnIndex("ContactPersonDept"));
                String Whtsapp = cur.getString(cur.getColumnIndex("Mobile"));
                if (Whtsapp != null) {
                    edt_contactwhatsapp.setText(Whtsapp);
                }
               /* lstDept=new ArrayList<>();
                int a = lstDept.indexOf(
                );
*/
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
                Statename = "";

            } while (cur.moveToNext());

        }
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
        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        int a = lstProduct.indexOf(Product_name);
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        spinner_product.setSelection(Productionitems.indexOf(Product_name));
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();

        }
    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();


        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        try {
            contactNumber = contactNumber.replace("+91", "");
        } catch (Exception e) {
            e.printStackTrace();
            contactNumber = "";
        }


        edt_contactMobile.setText(contactNumber);
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        }
        edt_ContactName.setText(contactName);
        cursor.close();


    }

    private void addContact(String name, String phone) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name) // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // error
        } catch (OperationApplicationException e) {
            // error
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProspectEnterpriseActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProspectEnterpriseActivity.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProspectEnterpriseActivity.this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(ProspectEnterpriseActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);
                }
            } else {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            }
        } else {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

                } else {

                }
                return;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }
}




