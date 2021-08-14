package com.vritti.crm.vcrm7;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.chat.adapter.ChatRoomMultipleAdapterNewRecycleView;
import com.vritti.crm.adapter.CityAdapter;
import com.vritti.crm.adapter.ContactListAdapter;
import com.vritti.crm.adapter.CountryAdapter;
import com.vritti.crm.adapter.CustomAdapterContactList;
import com.vritti.crm.adapter.StateAdapter;
import com.vritti.crm.adapter.TerritoryAdapter;
import com.vritti.crm.bean.BusinessProfileBean;
import com.vritti.crm.bean.BusinessSegmentbean;
import com.vritti.crm.bean.City;
import com.vritti.crm.bean.CityMaster;
import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.EnterpriseBean;
import com.vritti.crm.bean.Firmbean;
import com.vritti.crm.bean.ProspectContact;
import com.vritti.crm.bean.ProspectsourceBean;
import com.vritti.crm.bean.SalesFamily;
import com.vritti.crm.bean.State;
import com.vritti.crm.bean.Teritorybean;
import com.vritti.crm.bean.Territory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class ProspectEnterpriseActivity2 extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;

    public static Context context;
    private long mLastClickTime = 0;

    private static final int PERMISSION_REQUEST_CONTACT = 1;
    LinearLayout ln_firmname, ln_Entity, ln_city, ln_state, ln_country, ln_territory, ln_businessSegment,
            ln_source, ln_reference, ln_Notes, ln_Contact, ln_Deptrole, ln_birthday, ln_BusinessDetails,
            ln_currency, ln_turnover, ln_Product;
    RelativeLayout hdr_Businessprofile, rl_entity;
    EditText edt_firmalise, edt_Address, edt_gstn, edt_TANNO, edt_TANNONAME, edt_website, edt_Notes,
            edt_ContactName, edt_ContactDesignation, edt_contactEmailid, edt_contactMobile, edt_contactwhatsapp, edt_ContactTele, edt_Contactskype,
            edt_Birthdate, edt_OfficeCnt, edt_EmployeeStrength, edt_turnover, edt_businessDetails;
    TextView Products;
    AutoCompleteTextView spinner_Departmentrole, atxt_firmname, spinner_territory, spinner_BusinessSegment, spinner_source,
            spinner_reference, spinner_refrenceoption, spinner_product, spinner_turnover, spinner_Entity, sConsignee;
    ImageView buttonSave_contact;

    AutoCompleteTextView spinner_country, spinner_state, spinner_city, spinner_currency;
    AppCompatCheckBox checkbox_entity;
    ImageView img_birth_calender, img_contact;
    DatePickerDialog datePickerDialog;

    LinearLayout ln_village, ln_District, ln_sex, ln_val1, ln_val2, ln_val3,
            ln_val4, ln_val5, ln_val6, ln_val7, ln_val8, ln_val9, ln_val10;
    TextView tv_village, tv_District, tv_sex, tv_val1, tv_val2, tv_val3,
            tv_val4, tv_val5, tv_val6, tv_val7, tv_val8, tv_val9, tv_val10;
    TextView tv_sourceofprospect, tv_product;
    TextInputLayout tv_city, tv_state, tv_country, tv_territory, tv_bussinesssegment, tv_currency,
            tv_source, tv_reference, tv_role;
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
    List<City> lstCity = new ArrayList<City>();
    List<String> lstTerrority = new ArrayList<String>();
    List<CityMaster> lstcitymaster = new ArrayList<>();
    ArrayList<String> Productionitems = new ArrayList<String>();
    List<String> lstBusinessSegment = new ArrayList<String>();
    public static List<String> lstProduct = new ArrayList<String>();
    List<String> lstSourceProspect = new ArrayList<String>();
    List<String> lstReferenceType = new ArrayList<String>();
    List<String> lstReference = new ArrayList<String>();
    List<String> lstEntity = new ArrayList<String>();
    List<String> lstConsignee = new ArrayList<String>();
    List<String> lstTurnover = new ArrayList<String>();
    List<State> lstState = new ArrayList<State>();
    public static List<Firmbean> lstFirm = new ArrayList<Firmbean>();
    ArrayList<String> currency = new ArrayList();
    ArrayList<Country> mList = new ArrayList<>();

    SimpleDateFormat dfDate = null;
    MySpinnerAdapter customDept;
    Dialog d_contact;
    LinearLayout ln_contact;
    ArrayList<ProspectContact> lstContact = new ArrayList<ProspectContact>();
    ContactListAdapter contactListAdapter;
    LinearLayout ln_contactlist;
    CardView card_viewdetails;
    CustomAdapterContactList customAdapterContactList;
    List<String> lstDept;
    String Stateid = "", Countryid = "", firmAlias = "", address = "", gstn = "", website = "";
    String FirmName, Territoryid = "", Cityid = "", BusDetailid = "", notes = "",
            SuSpSourceId = "0", CurrentDate = "", Consigneeid, Referenceid,
            Currencyid, PKSuspectId = "",
            mobile, Productid = "",
            Conttact_name = "", Designation = "", Contact_emailid = "", Contact_mobile = "",
            Contact_telephone = "", Contact_DateBirth = "", Contact_Depatment = "", Contact_Fax = "", GSTN = "",
            TAN_No = "", TAN_No_Name = "", Whatsapp_no = "", Birth_date = "", date_Before = "", CountryName = "", StateName = "",
            FkCountryId = "", CityName = "", FKStateId = "", FKDistrictId = "", date_Current = "";

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

    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    ArrayList<Country> countryArrayList = new ArrayList<>();
    ArrayList<State> stateArrayList = new ArrayList<>();
    ArrayList<City> cityArrayList = new ArrayList<>();
    ArrayList<String> cityarrList = new ArrayList<>();
    ArrayList<String> territoryarrList = new ArrayList<>();
    ArrayList<Territory> territoryArrayList = new ArrayList<>();
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter;
    CityAdapter cityAdapter;
    TerritoryAdapter territoryAdapter;
    private Country country;
    private State state;
    private City City;
    private Territory territory;
    String Mode = "";
    Dialog editDialog;

    public static final int COUNTRY = 2;
    public static final int STATE = 3;
    public static final int CITY = 4;
    public static final int TERRITORY = 5;
    public static final int BUSINESSSEGMENT = 5;

    boolean isCountry = false, isState = false, isCity = false, isTerritory = false,
            isBusiSegment = false, isSrcProspect = false, isRole = false, isSelProduct = false;
    private String DistrictName = "";
    private String Taluka = "";
    RecyclerView list_contactDetails;
    Button  buttonnext_prospect,buttonback;
    ImageView buttonAdd;

    ArrayList<EnterpriseBean> enterpriseBeanArrayList;
    ArrayList<ProspectContact> prospectContactArrayList;
    ArrayList<BusinessProfileBean> businessProfileBeanArrayList;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_prospect_entryv2);
        context = ProspectEnterpriseActivity2.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        context = ProspectEnterpriseActivity2.this;


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Enterprise Prospect");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_Enterprise, "");
        if (ProspectTypeID.equalsIgnoreCase("")) {
            ProspectTypeID = "1";
        }


        ut = new Utility();
        cf = new CommonFunctionCrm(ProspectEnterpriseActivity2.this);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();


        init();
        dfDate = new SimpleDateFormat("yyy/MM/dd");
        Date d = new Date();
        CurrentDate = dfDate.format(d);
        Intent intent = getIntent();
        PKSuspectId = intent.getStringExtra("PKSuspectId");
        FirmName = intent.getStringExtra("firmname");
        Mode = intent.getStringExtra("keymode");

        if (Mode != null) {
            if (Mode.equalsIgnoreCase("Edit") || Mode.equals("E")) {
                Mode = "E";
            } else {
                Mode = "A";
            }
        }

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
        }
        else {
            CustomerName = "";
            ContactName = "";
            ContactNumber = "";
            registeryID = "";
            Email = "";
            EnaquiryDetail = "";
        }

        if (PKSuspectId != null) {


            ///getproductfetchdetails();
            //getremainingdata();
           // atxt_firmname.setText(FirmName);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS}, 2909);
            }
        }


        if (CheckValidation()) {
            validationAsync();
            //    applyValidation();
        } else {
            validationAsync();
        }

        if (Mode != null) {
            if (Mode.equals("E")) {
                if(!(getIntent().getStringExtra("EnterpriseDetailsList").equals(""))){
                    enterpriseBeanArrayList.clear();
                    enterpriseBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("EnterpriseDetailsList"), EnterpriseBean.class).getEnterpriseBeanArrayList();
                }

                if(!(getIntent().getStringExtra("BusinessDetailsList").equals(""))){
                    businessProfileBeanArrayList.clear();
                    businessProfileBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("BusinessDetailsList"), BusinessProfileBean.class).getBusinessProfileBeanArrayList();
                }

                getcontactfetchdetails();

            }
            else {


                if(!(getIntent().getStringExtra("EnterpriseDetailsList").equals(""))){
                    enterpriseBeanArrayList.clear();
                    enterpriseBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("EnterpriseDetailsList"), EnterpriseBean.class).getEnterpriseBeanArrayList();
                }

                if(!(getIntent().getStringExtra("BusinessDetailsList").equals(""))){
                    businessProfileBeanArrayList.clear();
                    businessProfileBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("BusinessDetailsList"), BusinessProfileBean.class).getBusinessProfileBeanArrayList();
                }
                if(!(getIntent().getStringExtra("ContactDetailsList").equals(""))){
                    lstContact.clear();
                    lstContact = new Gson().fromJson(getIntent().getStringExtra("ContactDetailsList"), ProspectContact.class).getProspectContactArrayList();

                    ln_contactlist.setVisibility(View.VISIBLE);
                    ln_contact.setVisibility(View.GONE);
                    contactListAdapter = new ContactListAdapter(ProspectEnterpriseActivity2.this, lstContact);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    list_contactDetails.setLayoutManager(mLayoutManager);
                    list_contactDetails.setAdapter(contactListAdapter);
                }








                /*    .putExtra("StateId",Stateid)
                    .putExtra("CityId",Cityid)
                    .putExtra("TerritoryId",Territoryid)
                    .putExtra("GSTN",edt_gstn.getText().toString())
                    .putExtra("TanNo",edt_TANNO.getText().toString())
                    .putExtra("BusinessSegmentId",BusDetailid)
                    .putExtra("Website",edt_website.getText().toString())
                    .putExtra("SourceId",SuSpSourceId)
                    .putExtra("Notes",edt_Notes.getText().toString()));*/

            }
        } else {
            if(!(getIntent().getStringExtra("EnterpriseDetailsList").equals(""))){
                enterpriseBeanArrayList.clear();
                enterpriseBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("EnterpriseDetailsList"), EnterpriseBean.class).getEnterpriseBeanArrayList();
            }

            if(!(getIntent().getStringExtra("ContactDetailsList").equals(""))){
                lstContact.clear();
                lstContact = new Gson().fromJson(getIntent().getStringExtra("ContactDetailsList"), ProspectContact.class).getProspectContactArrayList();
                ln_contact.setVisibility(View.GONE);
                ln_contactlist.setVisibility(View.VISIBLE);
                contactListAdapter = new ContactListAdapter(ProspectEnterpriseActivity2.this, lstContact);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                list_contactDetails.setLayoutManager(mLayoutManager);
                list_contactDetails.setAdapter(contactListAdapter);
            }

            if(!(getIntent().getStringExtra("BusinessDetailsList").equals(""))){
                businessProfileBeanArrayList.clear();
                businessProfileBeanArrayList = new Gson().fromJson(getIntent().getStringExtra("BusinessDetailsList"), BusinessProfileBean.class).getBusinessProfileBeanArrayList();
            }



        }

        if (cf.check_BusinessSegment() > 0) {
            // setautocomplete_BusinessSegment();
        } else {
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
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
            //setautocomplete_prospect();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //new DownloadSourceofProspectJSON().execute();
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

        if (cf.getSalesFamilyProuctcount() > 0) {
//            getproduct();
        } else {
            if (ut.isNet(context)) {
                new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
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
            //getCurrency();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //  new DownloadCurrencyMasterJSON().execute();
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

        try {
            int a = lstDept.indexOf(Contact_Depatment);
            spinner_Departmentrole.setSelection(lstDept.indexOf(Contact_Depatment));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setListener();
    }

    private void setSpinner() {
        String[] countries = getResources().getStringArray(R.array.OptionYN);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, countries);

        String[] gender = getResources().getStringArray(R.array.OptionSex);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, gender);

      /*  spinner_village.setAdapter(adapter);
        spinner_sex.setAdapter(adapter1);
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
        spinner_val10.setTitle("Select Option");*/
    }

    private void init() {
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        //  Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        //toolbar_action.setLogo(R.mipmap.ic_toolbar_logo_crm);
//        toolbar_action.setTitle(R.string.app_name_toolbar_CRM);
        //setSupportActionBar(toolbar_action);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ln_Contact = (LinearLayout) findViewById(R.id.ln_Contact);
        card_viewdetails =  findViewById(R.id.card_viewdetails);
        edt_ContactName = (EditText) findViewById(R.id.edt_ContactName);
        edt_ContactDesignation = (EditText) findViewById(R.id.edt_ContactDesignation1);
        ln_Deptrole = (LinearLayout) findViewById(R.id.ln_Deptrole);
        spinner_Departmentrole = findViewById(R.id.spinner_Departmentrole);
        edt_contactEmailid = (EditText) findViewById(R.id.edt_contactEmailid);
        edt_contactMobile = (EditText) findViewById(R.id.edt_contactMobile);
        edt_contactwhatsapp = (EditText) findViewById(R.id.edt_contactwhatsapp);
        edt_ContactTele = (EditText) findViewById(R.id.edt_ContactTele);
        edt_Contactskype = (EditText) findViewById(R.id.edt_Contactskype);
        ln_birthday = (LinearLayout) findViewById(R.id.ln_birthday);
        edt_Birthdate = (EditText) findViewById(R.id.edt_Birthdate);
        buttonSave_contact = findViewById(R.id.buttonSave_contact);
        list_contactDetails = findViewById(R.id.list_contactDetails);
        ln_contactlist = findViewById(R.id.ln_contactlist);
        ln_contact = findViewById(R.id.ln_contact);

        img_birth_calender = (ImageView) findViewById(R.id.img_birth_calender);
        img_contact = (ImageView) findViewById(R.id.img_contact);

        //  ln_Product = (LinearLayout) findViewById(R.id.ln_Product);
        //spinner_product = findViewById(R.id.spinner_product);
        //spinner_product.setTitle("Sales Family");

        hdr_Businessprofile = (RelativeLayout) findViewById(R.id.hdr_Businessprofile);
        rl_entity = (RelativeLayout) findViewById(R.id.rl_entity);
        ln_BusinessDetails = (LinearLayout) findViewById(R.id.ln_BusinessDetails);
        edt_OfficeCnt = (EditText) findViewById(R.id.edt_OfficeCnt);
        edt_EmployeeStrength = (EditText) findViewById(R.id.edt_EmployeeStrength);
        ln_currency = (LinearLayout) findViewById(R.id.ln_currency);
        spinner_currency = findViewById(R.id.spinner_currency);
        ln_turnover = (LinearLayout) findViewById(R.id.ln_turnover);
        edt_turnover = (EditText) findViewById(R.id.edt_turnover);
        spinner_turnover = findViewById(R.id.spinner_turnover);
        edt_businessDetails = (EditText) findViewById(R.id.edt_businessDetails);


        // autotxtProspect.setTitle("Select Source of ProspectSelectionActivity ");


        buttonSave_prospect = (Button) findViewById(R.id.buttonSave_prospect);
        buttonClose_prospect = (Button) findViewById(R.id.buttonClose_prospect);
        buttonSaveandstartcall_prospect = (Button) findViewById(R.id.buttonSaveandstartcall_prospect);
        buttonAdd = (ImageView) findViewById(R.id.buttonAdd);
        buttonnext_prospect = (Button) findViewById(R.id.buttonnext_prospect);
        buttonback = (Button) findViewById(R.id.buttonback);

        enterpriseBeanArrayList = new ArrayList<>();
        prospectContactArrayList = new ArrayList<>();
        businessProfileBeanArrayList = new ArrayList<>();

        customAdapterContactList = new CustomAdapterContactList();

        if (EnvMasterId.equalsIgnoreCase("ssidf")) {
            ln_reference.setVisibility(View.GONE);
        }
        setSpinner();
        //TempSpinnerCall();

        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForContactPermission();
            }
        });

        buttonSave_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSaveContact()) {

                    Contact_mobile = edt_contactMobile.getText().toString();
                    Conttact_name = edt_ContactName.getText().toString();

                    addContact(Conttact_name, Contact_mobile);

                    //  lstContact.clear();
                    String cname = edt_ContactName.getText().toString();
                    String cDesignation = edt_ContactDesignation.getText().toString();
                    String cBirthdate = edt_Birthdate
                            .getText().toString();
                    String cDept = (String) spinner_Departmentrole.getText().toString();
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
                    String guid = UUID.randomUUID().toString();
                    ContentValues  contentValues = new ContentValues();
                    contentValues.put("ContactName",cname);
                    contentValues.put("Designation",cDesignation);
                    contentValues.put("ContactPersonDept",cDept);
                    contentValues.put("DateofBirth",cBirthdate);
                    contentValues.put("EmailId",cEmailId);
                    contentValues.put("Telephone",cTele);
                    contentValues.put("Mobile",cMobile);
                    contentValues.put("Fax",cFax);
                    contentValues.put("WhatsAppNo",cWhtsapp);
                    sql.insert(db.TABLE_CONTACT_DETAILS, null, contentValues);


                    if (lstContact.size() != 0) {
                        ln_contactlist.setVisibility(View.VISIBLE);
                        contactListAdapter = new ContactListAdapter(ProspectEnterpriseActivity2.this, lstContact);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        list_contactDetails.setLayoutManager(mLayoutManager);
                        list_contactDetails.setAdapter(contactListAdapter);

                        ln_contact.setVisibility(View.GONE);
                        buttonAdd.setVisibility(View.VISIBLE);
                        edt_ContactName.setText("");
                        edt_ContactDesignation.setText("");
                        spinner_Departmentrole.setText("");
                        edt_contactEmailid.setText("");
                        edt_contactMobile.setText("");
                        edt_contactwhatsapp.setText("");
                        edt_ContactTele.setText("");
                        edt_Contactskype.setText("");
                        edt_Birthdate.setText("");

                    } else {
                        ln_contactlist.setVisibility(View.GONE);
                        ln_contact.setVisibility(View.VISIBLE);
                        buttonAdd.setVisibility(View.GONE);
                    }


                    // ((LinearLayout) findViewById(R.id.llAddContact)).setVisibility(View.GONE);
                    // ((LinearLayout) findViewById(R.id.llAddContact1)).setVisibility(View.VISIBLE);
                    //   ListView listView = (ListView) findViewById(R.id.listContacts);
                    //  listView.setAdapter(new CustomAdapterContactList(context, lstContact));
                }
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lstContact.size() != 0) {
                    ln_contactlist.setVisibility(View.VISIBLE);
                } else {
                    ln_contactlist.setVisibility(View.GONE);
                }
                ln_contact.setVisibility(View.VISIBLE);
                buttonAdd.setVisibility(View.GONE);


            }
        });

        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        buttonnext_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contactDetailsList = "", enterpriseDetailsList = "",businessDetailsList="";
                if (lstContact.size() != 0) {


                    if (lstContact.size() != 0) {
                        contactDetailsList = new Gson().toJson(new ProspectContact(lstContact));
                    }
                    if (enterpriseBeanArrayList.size() != 0) {
                        enterpriseDetailsList = new Gson().toJson(new EnterpriseBean(enterpriseBeanArrayList));
                    }
                    if (businessProfileBeanArrayList.size() != 0) {
                        businessDetailsList = new Gson().toJson(new BusinessProfileBean(businessProfileBeanArrayList));
                    }

                    startActivityForResult(new Intent(ProspectEnterpriseActivity2.this, ProspectEnterpriseActivity3.class)
                            .putExtra("EnterpriseDetailsList", enterpriseDetailsList)
                            .putExtra("ContactDetailsList", contactDetailsList)
                            .putExtra("BusinessDetailsList", businessDetailsList)
                            .putExtra("keymode",Mode),1130);
                    //.putExtra("ContactList", contactDetailsList);

                } else {
                    startActivity(new Intent(ProspectEnterpriseActivity2.this, ProspectEnterpriseActivity3.class));
                }
            }
        });

      /*  checkbox_entity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });*/


    }

    private void setListener() {
       /* spinner_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        CountryListActivity.class);
                isCountry = true;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusiSegment = false;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_COUNTRY);
                intent.putExtra("Id", "PKCountryId");
                intent.putExtra("DispName", "CountryName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*StateListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = true;
                isCity = false;
                isTerritory = false;
                isBusiSegment = false;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_get_statelistdata + "?Id=" + Countryid;
                intent.putExtra("Table_Name", db.TABLE_STATE);
                intent.putExtra("Id", "PKStateId");
                intent.putExtra("DispName", "StateDesc");
                intent.putExtra("WHClauseParameter", "WHERE FKCountryId='" + Countryid + "'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*CityListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = false;
                isCity = true;
                isTerritory = false;
                isBusiSegment = false;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_getCityMaster;
                intent.putExtra("Table_Name", db.TABLE_CITY_ENTITY);
                intent.putExtra("Id", "PKCityID");
                intent.putExtra("DispName", "CityName");
                intent.putExtra("WHClauseParameter", "WHERE FKStateId='" + Stateid + "'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<City> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_territory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*TerritoryListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = true;
                isBusiSegment = false;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_get_fill_territory;
                intent.putExtra("Table_Name", db.TABLE_Teritory);
                intent.putExtra("Id", "PKTerritoryId");
                intent.putExtra("DispName", "TerritoryName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_BusinessSegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*TerritoryListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusiSegment = true;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_get_Businesssegment;
                intent.putExtra("Table_Name", db.TABLE_Business_segment);
                intent.putExtra("Id", "PKBusiSegmentID");
                intent.putExtra("DispName", "SegmentDescription");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*TerritoryListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusiSegment = false;
                isSrcProspect = true;
                isRole = false;
                isSelProduct = false;

                String url = CompanyURL + WebUrlClass.api_get_ProspectSource;
                intent.putExtra("Table_Name", db.TABLE_Prospectsource);
                intent.putExtra("Id", "PKSuspSourceId");
                intent.putExtra("DispName", "SourceName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity2.this,
                        *//*TerritoryListActivity.class*//*CountryListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusiSegment = false;
                isSrcProspect = false;
                isRole = false;
                isSelProduct = true;

                String url = CompanyURL + WebUrlClass.api_get_sales_family;
                intent.putExtra("Table_Name", db.TABLE_SALES_FAMILY_PRODUCT);
                intent.putExtra("Id", "FamilyId");
                intent.putExtra("DispName", "FamilyDesc");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });*/

      /*  spinner_Departmentrole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectEnterpriseActivity1.this,
                        *//*TerritoryListActivity.class*//*CountryListActivity.class);
                isCountry = false;isState = false; isCity = false;isTerritory = false;
                isBusiSegment=false;isSrcProspect=false;isRole=true;isSelProduct=false;

                String url = CompanyURL + WebUrlClass.api_get_sales_family;
                intent.putExtra("Table_Name", db.TABLE_SALES_FAMILY_PRODUCT);
                intent.putExtra("Id","FamilyId");
                intent.putExtra("DispName","FamilyDesc");
                intent.putExtra("WHClauseParameter","");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName",url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent,COUNTRY);
            }
        });*/

        spinner_Departmentrole.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinner_Departmentrole.showDropDown();
                return false;
            }
        });

      /*  buttonClose_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    /*    buttonSaveandstartcall_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 30000) {

                        Toast.makeText(getApplicationContext(), "You can click only after 30 sec from your first click", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();


                    JSONObject jsoncontact = new JSONObject();
                    try {
                        Conttact_name = edt_ContactName.getText().toString();
                        Designation = edt_ContactDesignation.getText().toString();
                        Contact_emailid = edt_contactEmailid.getText().toString();
                        Contact_mobile = edt_contactMobile.getText().toString();
                        Contact_telephone = edt_ContactTele.getText().toString();
                        Contact_DateBirth = edt_Birthdate.getText().toString();
                        Contact_Depatment = (String) spinner_Departmentrole.getText().toString();
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
                        if (edt_website.getText().toString().equals("")) {
                            jsonSuspMaster.put("CompanyURL", CompanyURL);
                        } else {
                            jsonSuspMaster.put("CompanyURL", edt_website.getText().toString());
                        }

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
        });*/

       /* spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_product_salesfamily = (String) spinner_product.getText().toString().trim();

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
                string_currency = spinner_currency.getText().toString().trim();
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


                *//*if (spinner_currency.getSelectedItemPosition() == 0) {
                    lstTurnover.add("Lac");
                    lstTurnover.add("Cr");
                } else if (spinner_currency.getSelectedItemPosition() == 1) {
                    lstTurnover.add("Bill");
                    lstTurnover.add("Mill");
                }*//*
                if (position == 0) {
                    lstTurnover.add("Lac");
                    lstTurnover.add("Cr");
                } else if (position == 1) {
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
        });*/

        /*spinner_refrenceoption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_refrenceoption = (String) spinner_refrenceoption.getText().toString().trim();
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
        });*/


        spinner_Departmentrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_DepartmentRole = (String) spinner_Departmentrole.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* spinner_turnover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_turnover = (String) spinner_turnover.getText().toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sConsignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_entityconsinee = (String) sConsignee.getText().toString().trim();
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
                string_Source = (String) spinner_source.getText().toString();
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
        });*/


        buttonSave_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //getId();

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 30000) {
                        Toast.makeText(getApplicationContext(), "You can click only after 30 sec from your first click", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    JSONObject jsoncontact = new JSONObject();
                    try {
                        Conttact_name = edt_ContactName.getText().toString();
                        Designation = edt_ContactDesignation.getText().toString();
                        Contact_emailid = edt_contactEmailid.getText().toString();
                        Contact_mobile = edt_contactMobile.getText().toString();
                        Contact_telephone = edt_ContactTele.getText().toString();
                        Contact_DateBirth = edt_Birthdate.getText().toString();
                        Contact_Depatment = (String) spinner_Departmentrole.getText().toString();
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
                        jsonBusinessprospect.put("BusinessDetails", edt_businessDetails.getText().toString());
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
                                    .equalsIgnoreCase("Male")) {
                                sex = "M";
                            } else if (((String) spinner_sex.getSelectedItem()).
                                    equalsIgnoreCase("Female")) {
                                sex = "F";
                            } else if (((String) spinner_sex.getSelectedItem()).equalsIgnoreCase("Others")) {
                                sex = "0";
                            } else {
                                sex = "";
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


     /*   ((TextView) findViewById(R.id.btnSaveContact1)).setOnClickListener(new View.OnClickListener() {

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

        ((LinearLayout) findViewById(R.id.llAddContact1)).setVisibility(View.GONE);

        ((TextView) findViewById(R.id.btnSaveContact)).setOnClickListener(new View.OnClickListener() {

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
                            String cDept = (String) spinner_Departmentrole.getText().toString();
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
                        else {}
                    }
                });*/


        /*spinner_territory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/


     /*   atxt_firmname.addTextChangedListener(new TextWatcher() {
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
        });*/


        /*spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
*/

        /*spinner_reference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reftypeid = "";
                string_reference = (String) spinner_reference.getText().toString();
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
        });*/
      /*  spinner_Entity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                custvid = "";
                string_entity = (String) spinner_Entity.getText().toString().trim();
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
        });*/

        /*spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });*/
      /*  spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
*/
      /*  spinner_BusinessSegment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_BusinessSegment = (String) spinner_BusinessSegment.getText().toString();
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
        });*/


      /*  edt_Birthdate.setOnClickListener(new View.OnClickListener() {

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
                               *//* String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);*//*
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
        });*/

      /*  img_birth_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);
                                edt_Birthdate.setText(Contact_DateBirth);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.show();
            }
        });*/

        edt_Birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(ProspectEnterpriseActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;
                                date_Current = mYear + "/"
                                        + String.format("%02d", (mMonth + 1))
                                        + "/" + mDay;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);

                                // String ValidDate = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Current);

                                Calendar validDate = Calendar.getInstance();
                                validDate.set(year, monthOfYear + 1, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();

                                edt_Birthdate.setText(Contact_DateBirth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });

    /*  edt_Birthdate.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {

              Calendar mcurrentDate = Calendar.getInstance();
              final int mYear = mcurrentDate.get(Calendar.YEAR);
              final int mMonth = mcurrentDate.get(Calendar.MONTH);
              final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


              // Launch Date Picker Dialog
              datePickerDialog = new DatePickerDialog(ProspectEnterpriseActivity2.this,
                      new DatePickerDialog.OnDateSetListener() {

                          @Override
                          public void onDateSet(DatePicker datePicker, int year,
                                                int monthOfYear, int dayOfMonth) {
                              // Display Selected date in textbox

                              date_Before = year + "/"
                                      + String.format("%02d", (monthOfYear + 1))
                                      + "/" + dayOfMonth;
                              date_Current = mYear + "/"
                                      + String.format("%02d", (mMonth + 1))
                                      + "/" + mDay;

                              Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);

                              // String ValidDate = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Current);

                              Calendar validDate = Calendar.getInstance();
                              validDate.set(year, monthOfYear + 1, dayOfMonth);

                              Calendar currentDate = Calendar.getInstance();

                              edt_Birthdate.setText(Contact_DateBirth);

                          }
                      }, mYear, mMonth, mDay);
              datePickerDialog.setTitle("Select date");
              datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
              datePickerDialog.show();

              return true;
          }
      });
*/

    /*    edt_Birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(ProspectEnterpriseActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;
                                date_Current = mYear + "/"
                                        + String.format("%02d", (mMonth + 1))
                                        + "/" + mDay;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);

                                // String ValidDate = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Current);

                                Calendar validDate = Calendar.getInstance();
                                validDate.set(year, monthOfYear + 1, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();

                                edt_Birthdate.setText(Contact_DateBirth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
            }
        });*/

        /*spinner_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                String countryname = spinner_country.getText().toString().trim();
                try {
                    Countryid = getPosition_Countryfromspin(mList, countryname);
                } catch (Exception e) {
                    e.printStackTrace();
                    Countryid = "";
                }
                System.out.println(item.toString());

                if (cf.getStatecount() > 0) {
                    getState();
                } else {
                    new DownloadStatelistJSON().execute(Countryid);
                }
            }
        });
*/

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
      /*  if ((atxt_firmname.getText().toString().equalsIgnoreCase("") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(" ") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(null)) && isMadatory_Firmname) {
            if (!isVisible_Firmname) {
                atxt_firmname.setVisibility(View.VISIBLE);
                ln_firmname.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, "Enter firm name", Toast.LENGTH_LONG).show();
            return false;
        } else */
        if ((edt_ContactName.getText().toString().equalsIgnoreCase("") ||
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

        //ln_border();


        email = edt_contactEmailid.getEditableText().toString().trim();
        String GSTNNo = edt_gstn.getEditableText().toString().trim();
        String email1 = edt_contactEmailid.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String GSTN = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[0-9]{1}[a-zA-Z]{1}[0-9]{1}";

        //22AAAAA1236D1Z5         "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";

        // TODO Auto-generated method stub
        if ((atxt_firmname.getText().toString().equalsIgnoreCase("") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(" ") ||
                atxt_firmname.getText().toString().equalsIgnoreCase(null)) && isMadatory_Firmname) {
            if (!isVisible_Firmname) {
                atxt_firmname.setVisibility(View.VISIBLE);
                ln_firmname.setVisibility(View.VISIBLE);
            }
            //ln_firmname.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter firm name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_firmalise.getText().toString().equalsIgnoreCase("") ||
                edt_firmalise.getText().toString().equalsIgnoreCase(" ") ||
                edt_firmalise.getText().toString().equalsIgnoreCase(null)) && isMadatory_FirmAlias) {
            if (!isVisible_FirmAlias) {
                edt_firmalise.setVisibility(View.VISIBLE);
            }
            // edt_firmalise.setBackgroundResource(R.drawable.edit_text_red);
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
            //edt_Address.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((Cityid.equalsIgnoreCase("") ||
                Cityid.equalsIgnoreCase(" ") ||
                Cityid.equalsIgnoreCase(null) ||
                Cityid.equalsIgnoreCase("Select")) && isMadatory_City) {
            if (!isVisible_City) {
                ln_city.setVisibility(View.VISIBLE);
                spinner_city.setVisibility(View.VISIBLE);
            }
            //ln_city.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select City", Toast.LENGTH_LONG).show();
            return false;
        } else if ((Stateid.equalsIgnoreCase("") ||
                Stateid.equalsIgnoreCase(" ") ||
                Stateid.equalsIgnoreCase(null) ||
                Stateid.equalsIgnoreCase("Select")) && isMadatory_State) {
            if (!isVisible_State) {
                ln_state.setVisibility(View.VISIBLE);
                spinner_state.setVisibility(View.VISIBLE);
            }
            //   ln_state.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select State", Toast.LENGTH_LONG).show();
            return false;
        } else if ((Countryid.equalsIgnoreCase("") ||
                Countryid.equalsIgnoreCase(" ") ||
                Countryid.equalsIgnoreCase(null) ||
                Countryid.equalsIgnoreCase("Select")) && isMadatory_Country) {
            if (!isVisible_Country) {
                ln_country.setVisibility(View.VISIBLE);
                spinner_country.setVisibility(View.VISIBLE);
            }
            //  ln_country.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select country", Toast.LENGTH_LONG).show();
            return false;
        } else if ((Territoryid.equalsIgnoreCase("") ||
                Territoryid.equalsIgnoreCase(" ") ||
                Territoryid.equalsIgnoreCase(null) ||
                Territoryid.equalsIgnoreCase("Select")) && isMadatory_Territory) {
            if (!isVisible_Territory) {
                ln_territory.setVisibility(View.VISIBLE);
                spinner_territory.setVisibility(View.VISIBLE);
            }
            // ln_territory.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select territory", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_gstn.getText().toString().equalsIgnoreCase("") ||
                edt_gstn.getText().toString().equalsIgnoreCase(" ") ||
                edt_gstn.getText().toString().equalsIgnoreCase(null) ||
                !(GSTNNo.matches(GSTN))) && isMadatory_GSTN) {
            if (!isVisible_GSTN) {
                edt_gstn.setVisibility(View.VISIBLE);
            }
            // edt_gstn.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please enter valid GSTN No.", Toast.LENGTH_LONG).show();
            return false;
        }

        /*else if ((edt_gstn.getText().toString().equalsIgnoreCase("") ||
                edt_gstn.getText().toString().equalsIgnoreCase(" ") ||
                edt_gstn.getText().toString().equalsIgnoreCase(null) ||
                edt_gstn.getText().toString().length() < 15) && isMadatory_GSTN) {
            if (!isVisible_GSTN) {
                edt_gstn.setVisibility(View.VISIBLE);
            }

            Toast.makeText(context, "Please enter valid GSTN No.", Toast.LENGTH_LONG).show();
            return false;
        } */
        else if ((edt_TANNO.getText().toString().equalsIgnoreCase("") ||
                edt_TANNO.getText().toString().equalsIgnoreCase(" ") ||
                edt_TANNO.getText().toString().equalsIgnoreCase(null)) && isMadatory_TANno) {
            if (!isVisible_TANno) {
                edt_TANNO.setVisibility(View.VISIBLE);
            }
            //  edt_TANNO.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please TAN No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_TANNONAME.getText().toString().equalsIgnoreCase("") ||
                edt_TANNONAME.getText().toString().equalsIgnoreCase(" ") ||
                edt_TANNONAME.getText().toString().equalsIgnoreCase(null)) && isMadatory_TANname) {
            if (!isVisible_TANname) {
                edt_TANNONAME.setVisibility(View.VISIBLE);
            }
            //  edt_TANNONAME.setBackgroundResource(R.drawable.edit_text_red);

            Toast.makeText(context, "Please enter TAN No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_BusinessSegment.equalsIgnoreCase("") ||
                string_BusinessSegment.equalsIgnoreCase(" ") ||
                string_BusinessSegment.equalsIgnoreCase(null)) && isMadatory_BusinessSegment) {
            if (!isVisible_BusinessSegment) {
                ln_businessSegment.setVisibility(View.VISIBLE);
                spinner_BusinessSegment.setVisibility(View.VISIBLE);
            }
            // ln_businessSegment.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select business segment", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_website.getText().toString().equalsIgnoreCase("") ||
                edt_website.getText().toString().equalsIgnoreCase(" ") ||
                edt_website.getText().toString().equalsIgnoreCase(null)) && isMadatory_Website) {
            if (!isVisible_Website) {
                edt_website.setVisibility(View.VISIBLE);
            }
            //edt_website.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please enter website", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_Source.equalsIgnoreCase("") ||
                string_Source.equalsIgnoreCase(" ") ||
                string_Source.equalsIgnoreCase(null)) && isMadatory_SourceofProspect) {
            if (!isVisible_SourceofProspect) {
                ln_source.setVisibility(View.VISIBLE);
                spinner_source.setVisibility(View.VISIBLE);
            }
            //ln_source.setBackgroundResource(R.drawable.edit_text_red);
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
            //  ln_reference.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select reference", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Notes.getText().toString().equalsIgnoreCase("") ||
                edt_Notes.getText().toString().equalsIgnoreCase(" ") ||
                edt_Notes.getText().toString().equalsIgnoreCase(null)) && isMadatory_Notes) {
            if (!isVisible_Notes) {
                ln_Notes.setVisibility(View.VISIBLE);
                edt_Notes.setVisibility(View.VISIBLE);
            }
            //   edt_Notes.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please enter Notes", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactName.getText().toString().equalsIgnoreCase("") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactName.getText().toString().equalsIgnoreCase(null)) && isMadatory_Contact) {
            if (!isVisible_Contact) {
                ln_Contact.setVisibility(View.VISIBLE);
                edt_ContactName.setVisibility(View.VISIBLE);
            }
            //  edt_ContactName.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactDesignation.getText().toString().equalsIgnoreCase("") ||
                edt_ContactDesignation.getText().toString().equalsIgnoreCase(" ")) && isMadatory_Designation) {
            Boolean val = isVisible_Designation;
            if (!val) {
                edt_ContactDesignation.setVisibility(View.VISIBLE);
            }
            //edt_ContactDesignation.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please enter contact designation", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_DepartmentRole.equalsIgnoreCase("") ||
                string_DepartmentRole.equalsIgnoreCase("--Select Role--") ||
                string_DepartmentRole.equalsIgnoreCase(null)) && isMadatory_Role) {
            if (!isVisible_Role) {
                ln_Deptrole.setVisibility(View.VISIBLE);
                spinner_Departmentrole.setVisibility(View.VISIBLE);
            }
            //  ln_Deptrole.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Select Department role", Toast.LENGTH_LONG).show();
            return false;
        } else if ((email1.equalsIgnoreCase("") ||
                email1.equalsIgnoreCase(" ") ||
                email1.equalsIgnoreCase(null))) {
            //  edt_contactEmailid.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter email address or valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if (!(isValidEmail(email)) && isMadatory_EmailID) {
            if (!isVisible_EmailID) {
                edt_contactEmailid.setVisibility(View.VISIBLE);
            }
            // edt_contactEmailid.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter  valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactMobile.getText().toString().equalsIgnoreCase("") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactMobile.getText().toString().equalsIgnoreCase(null) ||
                edt_contactMobile.getText().length() != 10) && isMadatory_Mobile) {
            if (!isVisible_Mobile) {
                edt_contactMobile.setVisibility(View.VISIBLE);
            }
            // edt_contactMobile.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter mobile No. or valid mobile No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_contactwhatsapp.getText().toString().equalsIgnoreCase("") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(" ") ||
                edt_contactwhatsapp.getText().toString().equalsIgnoreCase(null) ||
                edt_contactwhatsapp.getText().length() != 10) && isMadatory_WhatsappNo) {
            if (!isVisible_WhatsappNo) {
                edt_contactwhatsapp.setVisibility(View.VISIBLE);
            }
            //   edt_contactwhatsapp.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter Whatsaap No. or valid Whatsaap No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ContactTele.getText().toString().equalsIgnoreCase("") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(" ") ||
                edt_ContactTele.getText().toString().equalsIgnoreCase(null)) && isMadatory_Telephone) {
            if (!isVisible_Telephone) {
                edt_ContactTele.setVisibility(View.VISIBLE);
            }
            //  edt_ContactTele.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter Telephone", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Contactskype.getText().toString().equalsIgnoreCase("") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(" ") ||
                edt_Contactskype.getText().toString().equalsIgnoreCase(null)) && isMadatory_Skype) {
            if (!isVisible_Skype) {
                edt_Contactskype.setVisibility(View.VISIBLE);
            }
            //  edt_Contactskype.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter skype No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Birthdate.getText().toString().equalsIgnoreCase("") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(" ") ||
                edt_Birthdate.getText().toString().equalsIgnoreCase(null)) && isMadatory_Dateofbirth) {
            if (!isVisible_Dateofbirth) {
                ln_birthday.setVisibility(View.VISIBLE);
                edt_Birthdate.setVisibility(View.VISIBLE);
            }
            //   ln_birthday.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Select contact Birth date", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_product_salesfamily.equalsIgnoreCase("") ||
                string_product_salesfamily.equalsIgnoreCase(" ") ||
                string_product_salesfamily.equalsIgnoreCase(null)) && isMadatory_ProductSalesFamily) {
            if (!isVisible_ProductSalesFamily) {
                ln_Product.setVisibility(View.VISIBLE);
                spinner_product.setVisibility(View.VISIBLE);
            }
            //   ln_Product.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Please select sales family", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_OfficeCnt.getText().toString().equalsIgnoreCase("") ||
                edt_OfficeCnt.getText().toString().equalsIgnoreCase(" ") ||
                edt_OfficeCnt.getText().toString().equalsIgnoreCase(null)) && isMadatory_NoofOffices) {
            if (!isVisible_NoofOffices) {
                edt_OfficeCnt.setVisibility(View.VISIBLE);
            }
            //  edt_OfficeCnt.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter No. of offices", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_EmployeeStrength.getText().toString().equalsIgnoreCase("") ||
                edt_EmployeeStrength.getText().toString().equalsIgnoreCase(" ") ||
                edt_EmployeeStrength.getText().toString().equalsIgnoreCase(null)) && isMadatory_NoofEmployees) {
            if (!isVisible_NoofEmployees) {
                edt_EmployeeStrength.setVisibility(View.VISIBLE);
            }
            // edt_EmployeeStrength.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter No. of employees", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_currency.equalsIgnoreCase("") ||
                string_currency.equalsIgnoreCase(" ") ||
                string_currency.equalsIgnoreCase(null)) && isMadatory_Currency) {
            if (!isVisible_Currency) {
                ln_currency.setVisibility(View.VISIBLE);
                spinner_currency.setVisibility(View.VISIBLE);
            }
            // ln_currency.setBackgroundResource(R.drawable.edit_text_red);
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
            // ln_turnover.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter turnover", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_businessDetails.getText().toString().equalsIgnoreCase("") ||
                edt_businessDetails.getText().toString().equalsIgnoreCase(" ") ||
                edt_businessDetails.getText().toString().equalsIgnoreCase(null)) && isMadatory_BusinessDetails) {
            if (!isVisible_BusinessDetails) {
                edt_businessDetails.setVisibility(View.VISIBLE);
            }
            //  edt_businessDetails.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter Business Details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_village.getVisibility() == View.VISIBLE
                && (edt_village.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_village) {
            //  String a = edt_village.getTag().toString();

            if (!isVisible_village) {
                ln_village.setVisibility(View.VISIBLE);

            }
            // edt_village.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter village", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_District.getVisibility() == View.VISIBLE
                && (edt_District.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_district) {
            if (!isVisible_district) {
                ln_District.setVisibility(View.VISIBLE);

            }
            // ln_District.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter district", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_sex.getVisibility() == View.VISIBLE
                && (edt_sex.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_sex) {
            if (!isVisible_sex) {
                ln_sex.setVisibility(View.VISIBLE);

            }
            //  ln_sex.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter sex", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val1.getVisibility() == View.VISIBLE
                && (edt_val1.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val1) {
            if (!isVisible_val1) {
                ln_val1.setVisibility(View.VISIBLE);

            }
            // ln_val1.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val2.getVisibility() == View.VISIBLE
                && (edt_val2.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val2) {
            if (!isVisible_val2) {
                ln_val2.setVisibility(View.VISIBLE);

            }
            //  ln_val2.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val3.getVisibility() == View.VISIBLE
                && (edt_val3.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val3) {
            if (!isVisible_val3) {
                ln_val3.setVisibility(View.VISIBLE);

            }
            // ln_val3.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val4.getVisibility() == View.VISIBLE
                && (edt_val4.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val4) {
            if (!isVisible_val4) {
                ln_val4.setVisibility(View.VISIBLE);

            }
            // ln_val4.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val5.getVisibility() == View.VISIBLE
                && (edt_val5.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val5) {
            if (!isVisible_val5) {
                ln_val5.setVisibility(View.VISIBLE);

            }
            ln_val5.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val6.getVisibility() == View.VISIBLE
                && (edt_val6.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val6) {
            if (!isVisible_val6) {
                ln_val6.setVisibility(View.VISIBLE);

            }
            ln_val6.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val7.getVisibility() == View.VISIBLE
                && (edt_val7.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val7) {
            if (!isVisible_val7) {
                ln_val7.setVisibility(View.VISIBLE);

            }
            ln_val7.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val8.getVisibility() == View.VISIBLE
                && (edt_val8.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val8) {
            if (!isVisible_val8) {
                ln_val8.setVisibility(View.VISIBLE);

            }
            ln_val8.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val9.getVisibility() == View.VISIBLE
                && (edt_val9.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val9) {
                ln_val9.setVisibility(View.VISIBLE);

            }
            ln_val9.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val10.getVisibility() == View.VISIBLE
                && (edt_val10.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val10) {
                ln_val10.setVisibility(View.VISIBLE);

            }
            ln_val10.setBackgroundResource(R.drawable.edit_text_red);
            Toast.makeText(context, "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

//        spinner_currency.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        //  spinner_currency.setSelection(0);

        lstTurnover = new ArrayList<String>();
        lstTurnover.clear();

        /*if (spinner_currency.getSelectedItemPosition() == 0) {
            lstTurnover.add("Lac");
            lstTurnover.add("Cr");
        } else if (spinner_currency.getSelectedItemPosition() == 1) {
            lstTurnover.add("Bill");
            lstTurnover.add("Mill");
        }*/
        for (int ind = 0; ind < currency.size(); ind++) {
            if (spinner_currency.getText().toString().trim().equals(currency.get(ind).toString().trim())) {
                if (ind == 0) {
                    lstTurnover.add("Lac");
                    lstTurnover.add("Cr");
                } else if (ind == 1) {
                    lstTurnover.add("Bill");
                    lstTurnover.add("Mill");
                }
            } else {

            }
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
//        spinner_Entity.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        //  spinner_Entity.setSelection(0);


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
//        spinner_reference.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        //    spinner_reference.setSelection(0);
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

//        progressbar.setVisibility(View.VISIBLE);

    }

    private void dismissProgressDialog() {

        //progressbar.setVisibility(View.GONE);

    }

    /*  private void showProgressDialog1() {

          ProgressDialog progressDialog = new ProgressDialog(ProspectEnterpriseActivity1.this);
          progressDialog.setCancelable(true);
          if (!isFinishing()) {
              progressDialog.show();
          }
          progressDialog.setContentView(R.layout.crm_progress_lay);
          progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      }*/
    private void CreateOfflineIntend(final String url, final String parameter, final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
           /* if (PKSuspectId != null) {
                Toast.makeText(BusinessProspectusActivity.this, "ProspectSelectionActivity Update succcessfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(BusinessProspectusActivity.this, "ProspectSelectionActivity added succcessfully", Toast.LENGTH_LONG).show();
            }*/
            Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(ProspectEnterpriseActivity2.this,
                    SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            Intent intent2 = new Intent(ProspectEnterpriseActivity2.this,
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

        ProspectEnterpriseActivity2.this.runOnUiThread(new Runnable() {
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

    /*private void setautocomplete_teritory() {

        List<Teritorybean> lstdb = cf.getTeritorybean();
        lstTerrority.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstTerrority.add(lstdb.get(i).getTerritoryName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstTerrority);

        spinner_territory.setAdapter(customAdcity);
        int a = lstTerrority.indexOf(Territory_name);
        spinner_territory.setSelection(lstTerrority.indexOf(Territory_name));


    }*/

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
                /*if (cf.getCitycount() > 0) {
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
                }*/


            } else {
                //autotxtBusinessSegment.setHint(setup("Businesssegment"));
                //efName1.setHint(setup("Firmname"));
                //efAlias.setHint(setup("Firmalias"));
                /*if (cf.getCitycount() > 0) {
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
                }*/

            }


            /*if (cf.getCitycount() > 0) {
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
            }*/

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


   /* private void getCity() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstCity.add(lstdb.get(i).getCityName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstCity);
        spinner_city.setAdapter(customAdcity);
        int a = lstCity.indexOf(City_name);
        spinner_city.setSelection(lstCity.indexOf(City_name));


    }*/

    private void setautocomplete_BusinessSegment() {

        List<BusinessSegmentbean> lstdb = cf.getBusinessSegmentbean();
        lstBusinessSegment.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstBusinessSegment.add(lstdb.get(i).getSegmentDescription());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectEnterpriseActivity2.this,
                R.layout.crm_custom_spinner_txt, lstBusinessSegment);
        spinner_BusinessSegment.setAdapter(customAdcity);

        if (Mode.equalsIgnoreCase("E")) {
            if (BusDetailid != "") {
                int pos = -1;

                for (int i = 0; i < lstdb.size(); i++) {
                    if (BusDetailid.equals(lstdb.get(i).getPKBusiSegmentID())) {
                        string_BusinessSegment = lstdb.get(i).getSegmentDescription();
                        pos = i;

                    }
                }
                if (pos != -1) {
                    spinner_BusinessSegment.setText(string_BusinessSegment);
                } else {
                    spinner_BusinessSegment.setText("");
                }
            }

        }


    }

    private void setautocomplete_prospect() {

        List<ProspectsourceBean> lstdb = cf.getProspectsourceBean();
        lstSourceProspect.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstSourceProspect.add(lstdb.get(i).getSourceName());

        MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectEnterpriseActivity2.this,
                R.layout.crm_custom_spinner_txt, lstSourceProspect);
        spinner_source.setAdapter(customAdcity);

       /* try{
            int a = lstSourceProspect.indexOf(Source_prospect);
            spinner_source.setSelection(lstSourceProspect.indexOf(Source_prospect));
        }catch (Exception e){
            e.printStackTrace();
        }*/

        if (Mode.equals("E")) {
            int pos1 = -1;
            if (SuSpSourceId != "") {
                for (int i = 0; i < lstdb.size(); i++) {
                    if (SuSpSourceId.equals(lstdb.get(i).getPKSuspSourceId())) {
                        Source_prospect = lstdb.get(i).getSourceName();
                        pos1 = i;
                        break;
                    }
                }
                if (pos1 != -1) {
                    spinner_source.setText(Source_prospect);
                } else {
                    spinner_source.setText("");
                }
            }
        }
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
        try {
            if (ut.isNet(context)) {
                showProgressDialog();
                new StartSession(context, new CallbackInterface() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*class DownloadterritoryJSON extends AsyncTask<Integer, Void, Integer> {
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

    }*/

    /*class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
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

    }*/

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
                //getEntity();
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
                res = ut.OpenPostConnection(url, params[0], ProspectEnterpriseActivity2.this);
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
                Toast.makeText(context, "Prospect updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(context, "Prospect not updated successfully", Toast.LENGTH_LONG).show();
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
                res = ut.OpenPostConnection(url, params[0], ProspectEnterpriseActivity2.this);
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
                Toast.makeText(ProspectEnterpriseActivity2.this, "Prospect updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ProspectEnterpriseActivity2.this, "Prospect added successfully", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(context, CreateOpportunityActivity.class);

            intent.putExtra("SuspectID", integer);//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
            startActivity(intent);
            ProspectEnterpriseActivity2.this.finish();
            //onBackPressed();
        }

    }

    /*class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
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
         *//*   if (progressHUD1 != null && progressHUD1.isShowing()) {
                progressHUD1.dismiss();
            }*//*
            dismissProgressDialog();
            if (response.contains("PKStateId")) {
                getState();
            }

        }

    }*/

    /*class DownloadCountryListJSON extends AsyncTask<String, Void, String> {
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

    }*/

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prospect, menu);
        return true;
    }
*/
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (ut.isNet(context)) {
                showProgressDialog();
                new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dismissProgressDialog();

                        //  new DownloadCityJSON().execute();
                        // new DownloadStatelistJSON().execute();
                        //new DownloadterritoryJSON().execute();
                        //  new DownloadCountryDataJson().execute();
                        new DownloadCountryListJSON().execute();
                        // new DownloadterritoryJSON().execute();
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
        if (id == R.id.action_menu) {
            dialog = new Dialog(ProspectEnterpriseActivity2.this);
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
                        startActivity(new Intent(ProspectEnterpriseActivity2.this,
                                BusinessProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId)
                                .putExtra("keymode", "AddNew"));

                    } else {
                        Intent i = new Intent(ProspectEnterpriseActivity2.this, BusinessProspectusActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("PKSuspectId", PKSuspectId);
                        i.putExtra("custname", CustomerName);
                        i.putExtra("contactname", ContactName);
                        i.putExtra("contactnumber", ContactNumber);
                        i.putExtra("registryID", registeryID);
                        i.putExtra("email", Email);
                        i.putExtra("enquirydetail", EnaquiryDetail);
                        i.putExtra("keymode", "AddNew");
                        startActivity(i);
                    }

                    dialog.dismiss();
                }
            });
            txt_individual_prospect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (registeryID.equalsIgnoreCase("")) {
                        startActivity(new Intent(ProspectEnterpriseActivity2.this, IndividualProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId)
                                .putExtra("keymode", "AddNew"));

                    } else {
                        Intent i = new Intent(ProspectEnterpriseActivity2.this, IndividualProspectusActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("PKSuspectId", PKSuspectId);
                        i.putExtra("custname", CustomerName);
                        i.putExtra("contactname", ContactName);
                        i.putExtra("contactnumber", ContactNumber);
                        i.putExtra("registryID", registeryID);
                        i.putExtra("email", Email);
                        i.putExtra("enquirydetail", EnaquiryDetail);
                        i.putExtra("keymode", "AddNew");
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
*/
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        String mainDetailsList = "", contactDetailsList = "", businessDetailsList = "";

        if (enterpriseBeanArrayList.size() != 0) {
            mainDetailsList = new Gson().toJson(new EnterpriseBean(enterpriseBeanArrayList));
        }
        if (lstContact.size() != 0) {
            contactDetailsList = new Gson().toJson(new ProspectContact(lstContact));
        }

        if (businessProfileBeanArrayList.size() != 0) {
            businessDetailsList = new Gson().toJson(new BusinessProfileBean(businessProfileBeanArrayList));
        }

        Intent intent = new Intent(ProspectEnterpriseActivity2.this, ProspectEnterpriseActivity1.class);
        intent.putExtra("ContactList", contactDetailsList).putExtra("MainDetailsList", mainDetailsList)
                .putExtra("BusinessDetailsList", businessDetailsList);

        setResult(12, intent);
        super.onBackPressed();
        //finish();
    }

    /*private void getState() {
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity1.this,
                R.layout.crm_custom_spinner_txt, lstState);
        spinner_state.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        //  spinner_state.setSelection(0);
        spinner_state.setAdapter(customDept);
        int a = lstState.indexOf(Statename);
        spinner_state.setSelection(lstState.indexOf(Statename));

    }*/

    /*private void getCountry() {

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

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity1.this,
                R.layout.crm_custom_spinner_txt, mList);
        spinner_country.setAdapter(customDept);
    }*/

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

        try {
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
                        //      edt_firmalise.setHint(Caption);

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
                        //edt_Address.setHint(Caption);

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
                        tv_city.setHint(Caption + ":");
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
                        //spinner_country.setTag(PKFieldID);
                        tv_country.setHint(Caption + ":");

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
                        tv_state.setHint(Caption + ":");

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
                        tv_territory.setHint(Caption + ":");

                        if (isVisible.equalsIgnoreCase("1")) {
                            ln_territory.setVisibility(View.VISIBLE);
                            spinner_territory.setVisibility(View.VISIBLE);
                            isVisible_Territory = true;
                            if (cf.check_Teritory() > 0) {
                                setautocomplete_teritory();
                            } else {
                                if (ut.isNet(context)) {
                                    new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
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
                        //    edt_gstn.setHint(Caption);

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
                        //    edt_TANNO.setHint(Caption);

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
                        //    edt_TANNONAME.setHint(Caption);

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
                        tv_bussinesssegment.setHint(Caption + ":");

                        if (isVisible.equalsIgnoreCase("1")) {
                            ln_businessSegment.setVisibility(View.VISIBLE);
                            spinner_BusinessSegment.setVisibility(View.VISIBLE);
                            isVisible_BusinessSegment = true;

                            //call API
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
                        //    edt_website.setHint(Caption);

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
                        tv_source.setHint(Caption + ":");
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
                        tv_reference.setHint(Caption + ":");
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
                        //    edt_Notes.setHint(Caption);
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
                        //    edt_ContactName.setHint(Caption);

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
                        //     edt_ContactDesignation.setHint(Caption);
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
                        tv_role.setHint(Caption + ":");

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
                        //     edt_contactEmailid.setHint(Caption);

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
                        //      edt_contactMobile.setHint(Caption);
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
                        //    edt_contactwhatsapp.setHint(Caption);

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
                        //     edt_ContactTele.setHint(Caption);
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
                        //     edt_Contactskype.setHint(Caption);

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
                        //      edt_Birthdate.setHint(Caption);


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
                        //      edt_OfficeCnt.setHint(Caption);

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
                        //      edt_EmployeeStrength.setHint(Caption);

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
                        //      tv_currency.setHint(Caption);
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
                        //     edt_turnover.setHint(Caption);

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
                        //      edt_businessDetails.setHint(Caption);

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
        } catch (Exception e) {
            e.printStackTrace();
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
                Productid = cur.getString(cur.getColumnIndex("FKProductId"));

                System.out.println("Hello :" + turnover);

                edt_turnover.setText(cur.getString(cur.getColumnIndex("Turnover")));
                edt_OfficeCnt.setText(cur.getString(cur.getColumnIndex("NoOfOffices")));
                edt_EmployeeStrength.setText(cur.getString(cur.getColumnIndex("NoOfEmployees")));
                atxt_firmname.setText(cur.getString(cur.getColumnIndex("FirmName")));
                edt_Address.setText(cur.getString(cur.getColumnIndex("Address")));
                edt_firmalise.setText(cur.getString(cur.getColumnIndex("FirmAlias")));
                edt_Notes.setText(cur.getString(cur.getColumnIndex("Notes")));
                edt_website.setText(cur.getString(cur.getColumnIndex("CompanyURL")));


            } while (cur.moveToNext());

        }
    }

    private void getcontactfetchdetails() {
        String query = "SELECT * FROM " + db.TABLE_CONTACT_DETAILS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            lstContact.clear();
            cur.moveToFirst();
            do {
                Conttact_name = cur.getString(cur.getColumnIndex("ContactName"));
               // edt_ContactName.setText(Conttact_name);
                Contact_mobile = cur.getString(cur.getColumnIndex("Mobile"));
                //edt_contactMobile.setText(Contact_mobile);
                //edt_contactMobile.setText(cur.getString(cur.getColumnIndex("Mobile")));
                Contact_emailid = cur.getString(cur.getColumnIndex("EmailId"));
              //  edt_contactEmailid.setText(Contact_emailid);
                Designation = cur.getString(cur.getColumnIndex("Designation"));
              //  edt_ContactDesignation.setText(Designation);

                Contact_DateBirth = cur.getString(cur.getColumnIndex("DateofBirth"));
             if(Contact_DateBirth.contains("T")) {
                 String[] splits = Contact_DateBirth.split("T");
                 Contact_DateBirth = splits[0];
                 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                 Date newDate = null;
                 try {
                     newDate = format.parse(Contact_DateBirth);
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }

                 //Contact_DateBirth = Contact_DateBirth.substring(Contact_DateBirth.indexOf("(") + 1, Contact_DateBirth.lastIndexOf(")"));
                 SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                 Contact_DateBirth = sd.format(newDate);
             }
                Contact_Fax = cur.getString(cur.getColumnIndex("Fax"));
               // edt_Contactskype.setText(Contact_Fax);
                Contact_telephone = cur.getString(cur.getColumnIndex("Telephone"));
               // edt_ContactTele.setText(Contact_telephone);
                Contact_Depatment = cur.getString(cur.getColumnIndex("ContactPersonDept"));
                String Whtsapp = cur.getString(cur.getColumnIndex("Mobile"));

                lstContact.add(new ProspectContact(Conttact_name, Designation,
                        Contact_Depatment, Contact_DateBirth, Contact_emailid, Contact_telephone, Contact_mobile,
                        Contact_Fax, Whtsapp));
               /* lstDept=new ArrayList<>();
                int a = lstDept.indexOf(
                );
*/
            } while (cur.moveToNext());

        }

        if (lstContact.size() != 0) {
            ln_contactlist.setVisibility(View.VISIBLE);
            contactListAdapter = new ContactListAdapter(ProspectEnterpriseActivity2.this, lstContact);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            list_contactDetails.setLayoutManager(mLayoutManager);
            list_contactDetails.setAdapter(contactListAdapter);

            ln_contact.setVisibility(View.GONE);
            buttonAdd.setVisibility(View.VISIBLE);
            edt_ContactName.setText("");
            edt_ContactDesignation.setText("");
            spinner_Departmentrole.setText("");
            edt_contactEmailid.setText("");
            edt_contactMobile.setText("");
            edt_contactwhatsapp.setText("");
            edt_ContactTele.setText("");
            edt_Contactskype.setText("");
            edt_Birthdate.setText("");

        } else {
            ln_contactlist.setVisibility(View.GONE);
            ln_contact.setVisibility(View.VISIBLE);
            buttonAdd.setVisibility(View.GONE);
        }
    }

    private void getremainingdata() {
        String query = "SELECT * FROM " + db.TABLE_FILLCONTROL_DATA_FETCH;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                int pos = -1;
                int pos1 = -1;

                String cityId = (cur.getString(cur.getColumnIndex("FKCityId")));
                for (int i = 0; i < lstcitymaster.size(); i++) {
                    if (lstcitymaster.get(i).getPKCityID().equals(cityId)) {
                        pos = i;
                    }
                }

                if (pos != -1) {
                    spinner_city.setSelection(pos);
                } else {
                    spinner_city.setSelection(0);
                }

                String territoryId = (cur.getString(cur.getColumnIndex("FKTerritoryId")));
                List<Teritorybean> lstdb = cf.getTeritorybean();

                for (int i = 0; i < lstdb.size(); i++) {
                    if (lstdb.get(i).getPKTerritoryId().equals(territoryId)) {
                        pos1 = i;
                    }
                }

                if (pos1 != -1) {
                    spinner_territory.setSelection(pos1);
                } else {
                    spinner_territory.setSelection(0);
                }
                //  Source_prospect = (cur.getString(cur.getColumnIndex("SourceName")));
                /*spinner_city.setTitle(City_name);
                spinner_territory.setPrompt(Territory_name);*/
                Statename = "";

            } while (cur.moveToNext());

        }
    }

    private void getproduct() {

        List<SalesFamily> lstdb = cf.getSalesFamilyBean();
        Productionitems.clear();
        for (int i = 0; i < lstdb.size(); i++)
            Productionitems.add(lstdb.get(i).getFamilyDesc());


        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity2.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
//        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        int a = lstProduct.indexOf(Product_name);
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);

        /*try{
            spinner_product.setSelection(Productionitems.indexOf(Product_name));
        }catch (Exception e){
            e.printStackTrace();
        }*/

        if (Mode.equals("E")) {
            int productPos = -1;
            if (Productid != "") {
                for (int i = 0; i < lstdb.size(); i++) {
                    if (Productid.equals(lstdb.get(i).getFamilyId())) {
                        Product_name = lstdb.get(i).getFamilyDesc();
                        productPos = i;
                        break;
                    }
                }
                if (productPos != -1) {
                    // spinner_product.setText(Product_name);

                } else {
                    //  spinner_product.setSelection(0);
                }
            }
        }
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
        else if (requestCode == COUNTRY && resultCode == COUNTRY) {
            if (isCountry == true) {
                CountryName = data.getStringExtra("Name");
                Countryid = data.getStringExtra("ID");

                spinner_country.setText(CountryName);
            } else if (isState == true) {
                StateName = data.getStringExtra("Name");
                Stateid = data.getStringExtra("ID");

                spinner_state.setText(StateName);
            } else if (isCity == true) {
                City_name = data.getStringExtra("Name");
                Cityid = data.getStringExtra("ID");

                spinner_city.setText(City_name);
            } else if (isTerritory == true) {
                Territory_name = data.getStringExtra("Name");
                Territoryid = data.getStringExtra("ID");

                spinner_territory.setText(Territory_name);
            } else if (isBusiSegment == true) {
                //  BusDetail_Segment_name = data.getStringExtra("Name");
                BusDetailid = data.getStringExtra("ID");
                string_BusinessSegment = data.getStringExtra("Name");
                spinner_BusinessSegment.setText(string_BusinessSegment);
            } else if (isSrcProspect == true) {
                String name = data.getStringExtra("Name");
                SuSpSourceId = data.getStringExtra("ID");

                spinner_source.setText(name);
            } else if (isSelProduct == true) {
                String name = data.getStringExtra("Name");
                Productid = data.getStringExtra("ID");

                spinner_product.setText(name);
            }

        }
      else if(requestCode == 1130 && resultCode == 13 ){
            if (data != null) {

                if (!(data.getStringExtra("ContactList").equals(""))) {
                    lstContact.clear();
                    lstContact = new ArrayList<>();
                    lstContact = new Gson().fromJson(data.getStringExtra("ContactList"), ProspectContact.class)
                            .getProspectContactArrayList();

                    ln_contactlist.setVisibility(View.VISIBLE);
                    contactListAdapter = new ContactListAdapter(ProspectEnterpriseActivity2.this, lstContact);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    list_contactDetails.setLayoutManager(mLayoutManager);
                    list_contactDetails.setAdapter(contactListAdapter);


                }

                if (!(data.getStringExtra("MainDetailsList").equals(""))) {
                    enterpriseBeanArrayList.clear();
                    enterpriseBeanArrayList = new ArrayList<>();
                    enterpriseBeanArrayList = new Gson().fromJson(data.getStringExtra("MainDetailsList"), EnterpriseBean.class)
                            .getEnterpriseBeanArrayList();
                }

                if (!(data.getStringExtra("BusinessDetailsList").equals(""))) {
                    businessProfileBeanArrayList.clear();
                    businessProfileBeanArrayList = new ArrayList<>();
                    businessProfileBeanArrayList = new Gson().fromJson(data.getStringExtra("BusinessDetailsList"), BusinessProfileBean.class)
                            .getBusinessProfileBeanArrayList();
                }
            }
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
            contactNumber = contactNumber.replace(" ", "");
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
     /*     try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // error
        } catch (OperationApplicationException e) {
            // error
        }*/
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ProspectEnterpriseActivity2.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ProspectEnterpriseActivity2.this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProspectEnterpriseActivity2.this);
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
                    ActivityCompat.requestPermissions(ProspectEnterpriseActivity2.this,
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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


    /***********************************************************Temp data call***************************************************************/


    class DownloadCountryDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_countrylistdata;

            try {
                res = ut.OpenConnection(url, ProspectEnterpriseActivity2.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    //
                    // countryArrayList=new ArrayList<>();

                    countryArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        country = new Country();
                        JSONObject jorder = jResults.getJSONObject(i);
                        //String a = jorder.getString("PKCountryId");
                        country.setPKCountryId(jorder.getString("PKCountryId"));
                        country.setCountryCode(jorder.getString("CountryCode"));
                        country.setCountryName(jorder.getString("CountryName"));
                        countryArrayList.add(country);
                    }

                    Country country = new Country();
                    country.setCountryName("Select");
                    countryArrayList.add(0, country);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgressDialog();
            if (response.contains("[]")) {
                dismissProgressDialog();
                Toast.makeText(ProspectEnterpriseActivity2.this, "Data not found", Toast.LENGTH_SHORT).show();
                ArrayAdapter<Country> arrayAdapter = new ArrayAdapter<Country>(ProspectEnterpriseActivity2.this,
                        android.R.layout.simple_spinner_dropdown_item, countryArrayList);
                spinner_country.setAdapter(arrayAdapter);
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ProspectEnterpriseActivity2.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(countryArrayList);
                editor.putString("Country", json);
                editor.commit();

                //countryAdapter=new CountryAdapter(BusinessProspectusActivity.this,countryArrayList);
                ArrayAdapter<Country> arrayAdapter = new ArrayAdapter<Country>(ProspectEnterpriseActivity2.this,
                        android.R.layout.simple_spinner_dropdown_item, countryArrayList);
                spinner_country.setAdapter(arrayAdapter);



               /* //ArrayAdapter<String > arrayAdapter = new ArrayAdapter<String >(BusinessProspectusActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,countryArrayList);
                //spinner_country.setAdapter(arrayAdapter);
*/
              /*  PriorityAdapter countryadapter = new PriorityAdapter(BusinessProspectusActivity.this,
                        R.layout.crm_custom_spinner_txt, countryArrayList);
                spinner_country.setAdapter(countryadapter);*/

                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //permit_closed.setAdapter(authorizedPersonAdapter);

            }


        }
    }

    class DownloadStateDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_statelistdata + "?Id=" + Countryid;

            try {
                res = ut.OpenConnection(url, ProspectEnterpriseActivity2.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    //
                    // countryArrayList=new ArrayList<>();

                    stateArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        state = new State();
                        JSONObject jorder = jResults.getJSONObject(i);
                        //String a = jorder.getString("PKCountryId");
                        state.setPKStateId(jorder.getString("PKStateId"));
                        state.setFKCountryId(jorder.getString("FKCountryId"));
                        state.setStateDesc(jorder.getString("StateDesc"));
                        state.setStateNo(jorder.getString("StateNo"));
                        stateArrayList.add(state);
                    }
                    State state = new State();
                    state.setStateDesc("Select");
                    stateArrayList.add(0, state);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgressDialog();
            if (response.contains("[]")) {
                dismissProgressDialog();
                Toast.makeText(ProspectEnterpriseActivity2.this, "Data not found", Toast.LENGTH_SHORT).show();
                ArrayAdapter<State> stateArrayAdapter = new ArrayAdapter<State>
                        (ProspectEnterpriseActivity2.this,
                                android.R.layout.simple_spinner_dropdown_item, stateArrayList);
                spinner_state.setAdapter(stateArrayAdapter);
            } else {

                if (Mode.equals("E")) {

                    ArrayAdapter<State> stateArrayAdapter = new ArrayAdapter<State>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, stateArrayList);


                    spinner_state.setAdapter(stateArrayAdapter);

                    int stateposn = -1;

                    for (int i = 1; i < stateArrayList.size(); i++) {
                        if (Stateid.equalsIgnoreCase(stateArrayList.get(i).getPKStateId())) {
                            StateName = stateArrayList.get(i).getStateDesc();
                            stateposn = i;
                        }
                    }
                    if (stateposn != -1) {
                        spinner_state.setText(StateName);
                    } else {
                        spinner_state.setText(StateName);
                    }


                } else {
                    ArrayAdapter<State> stateArrayAdapter = new ArrayAdapter<State>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, stateArrayList);
                    spinner_state.setAdapter(stateArrayAdapter);
                }


               /* SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BusinessProspectusActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(countryArrayList);
                editor.putString("Country", json);
                editor.commit();
*/
                //countryAdapter=new CountryAdapter(BusinessProspectusActivity.this,countryArrayList);


            }


        }
    }

    class DownloadCityDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_districtlistdata + "?Id=" + Stateid;

            try {
                res = ut.OpenConnection(url, ProspectEnterpriseActivity2.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    //
                    // countryArrayList=new ArrayList<>();

                    cityArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    if (cityArrayList.size() == 0) {
                        City c = new City();
                        c.setPKDistrictId("");
                        c.setFKStateId(Stateid);
                        c.setDistrictDesc("-Select-");
                        c.setDistrictNo("");
                        cityArrayList.add(c);


                    }

                    for (int i = 0; i < jResults.length(); i++) {
                        City = new City();
                        JSONObject jorder = jResults.getJSONObject(i);
                        //String a = jorder.getString("PKCountryId");
                        City.setPKDistrictId(jorder.getString("PKDistrictId"));
                        City.setFKStateId(jorder.getString("FKStateId"));
                        City.setDistrictDesc(jorder.getString("DistrictDesc"));
                        City.setDistrictNo(jorder.getString("DistrictNo"));
                        cityArrayList.add(City);

                    }
                    City city = new City();
                    city.setDistrictDesc("Select");
                    cityArrayList.add(0, city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgressDialog();
            if (response.contains("[]")) {
                dismissProgressDialog();
                Toast.makeText(ProspectEnterpriseActivity2.this, "Data not found", Toast.LENGTH_SHORT).show();
                ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<City>
                        (ProspectEnterpriseActivity2.this,
                                android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                spinner_city.setAdapter(cityArrayAdapter);
                //spinner_city.setPrompt("Select");

            } else {

                if (Mode.equalsIgnoreCase("E")) {

                    ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<City>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                    spinner_city.setAdapter(cityArrayAdapter);
                    int statepos = -1;

                    for (int j = 1; j < cityArrayList.size(); j++) {
                        if (cityArrayList.get(j).getPKDistrictId().equalsIgnoreCase(Cityid)) {
                            DistrictName = cityArrayList.get(j).getDistrictDesc();
                            statepos = j;
                        }
                    }

                    if (statepos != -1) {
                        spinner_city.setText(DistrictName);
                    } else {
                        spinner_city.setSelection(0);
                    }

                } else {
                    ArrayAdapter<City> cityArrayAdapter = new ArrayAdapter<City>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, cityArrayList);
                    spinner_city.setAdapter(cityArrayAdapter);
                }


            }


        }
    }

    class DownloadTerritoryDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            //progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_territorylistdata + "?Id=" + Cityid;

            try {
                res = ut.OpenConnection(url, ProspectEnterpriseActivity2.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    // countryArrayList=new ArrayList<>();

                    territoryArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        territory = new Territory();
                        JSONObject jorder = jResults.getJSONObject(i);
                        //String a = jorder.getString("PKCountryId");
                        territory.setPKTalukaId(jorder.getString("PKTalukaId"));
                        territory.setFKDistrictId(jorder.getString("FKDistrictId"));
                        territory.setTalukaDesc(jorder.getString("TalukaDesc"));
                        territory.setTalukaNo(jorder.getString("TalukaNo"));
                        territoryArrayList.add(territory);
                    }
                    territoryarrList.add(String.valueOf(territoryArrayList));
                    territoryarrList.add(0, "Select");


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgressDialog();
            if (response.contains("[]")) {

                Toast.makeText(ProspectEnterpriseActivity2.this, "Data not found", Toast.LENGTH_SHORT).show();
                ArrayAdapter<String> territoryArrayAdapter1 = new ArrayAdapter<>
                        (ProspectEnterpriseActivity2.this,
                                android.R.layout.simple_spinner_dropdown_item, territoryarrList);
                spinner_territory.setAdapter(territoryArrayAdapter1);
                //spinner_territory.setTitle("Select");

            } else {
                //countryAdapter=new CountryAdapter(BusinessProspectusActivity.this,countryArrayList);
                if (Mode.equalsIgnoreCase("E")) {
                    ArrayAdapter<Territory> territoryArrayAdapter = new ArrayAdapter<Territory>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, territoryArrayList);
                    spinner_territory.setAdapter(territoryArrayAdapter);

                    int territoryposn = -1;

                    for (int i = 0; i < territoryArrayList.size(); i++) {
                        if (Territoryid.equalsIgnoreCase(territoryArrayList.get(i).getPKTalukaId())) {
                            Taluka = territoryArrayList.get(i).getTalukaDesc();
                            territoryposn = i;
                        } else {

                        }
                    }

                    if (territoryposn != -1) {
                        spinner_city.setSelection(territoryposn);
                    } else {

                        if (territoryposn == -1) {
                            Territory territory = new Territory();
                            territory.setTalukaDesc("Select");
                            territoryArrayList.add(0, territory);
                            spinner_territory.setSelection(0);
                        } else {

                        }
                    }
                } else {
                    ArrayAdapter<Territory> territoryArrayAdapter = new ArrayAdapter<Territory>
                            (ProspectEnterpriseActivity2.this,
                                    android.R.layout.simple_spinner_dropdown_item, territoryArrayList);
                    spinner_territory.setAdapter(territoryArrayAdapter);
                }




               /* //ArrayAdapter<String > arrayAdapter = new ArrayAdapter<String >(BusinessProspectusActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,countryArrayList);
                //spinner_country.setAdapter(arrayAdapter);
*/
              /*  PriorityAdapter countryadapter = new PriorityAdapter(BusinessProspectusActivity.this,
                        R.layout.crm_custom_spinner_txt, countryArrayList);
                spinner_country.setAdapter(countryadapter);*/

                //spinner_spotcheck.setAdapter(authorizedPersonAdapter);
                //permit_closed.setAdapter(authorizedPersonAdapter);

            }


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

            }
            setautocomplete_teritory();
        }

    }

    private void setautocomplete_teritory() {

        List<Teritorybean> lstdb = cf.getTeritorybean();
        lstTerrority.clear();
        for (int i = 0; i < lstdb.size(); i++) {
            lstTerrority.add(lstdb.get(i).getTerritoryName());
        }
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectEnterpriseActivity2.this,
                R.layout.crm_custom_spinner_txt, lstTerrority);

        spinner_territory.setAdapter(customAdcity);


      /*  int a = lstTerrority.indexOf(Territory_name);
        spinner_territory.setText(lstTerrority.indexOf(Territory_name));*/


        if (Mode.equalsIgnoreCase("E")) {
            int pos = -1;
            for (int i = 0; i < lstdb.size(); i++) {
                if (Territoryid.equals(lstdb.get(i).getPKTerritoryId())) {
                    Territory_name = lstdb.get(i).getTerritoryName();
                    pos = i;
                    break;
                }
            }
            if (pos != -1) {
                spinner_territory.setText(Territory_name);
            } else {
                spinner_territory.setText("");
            }

        }

    }

    public void TempSpinnerCall() {

        spinner_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList.size() > 0) {


                    string_country = spinner_country.getText().toString().trim();

                    try {
                        Countryid = getPosition_Countryfromspin(mList, string_country);

                        String query1 = "SELECT *" +
                                " FROM " + db.TABLE_STATE +
                                " WHERE FKCountryId='" + Countryid + "'";
                        Cursor cur1 = sql.rawQuery(query1, null);

                        if (cf.getStatecount() > 0) {
                            getState();

                        } else {

                            if (ut.isNet(context)) {
                                new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadStatelistJSON().execute(Countryid);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Countryid = "";
                    }

                }
            }
        });


        spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lstState.size() > 0) {
                    string_state = spinner_state.getText().toString().trim();
                    try {
                        Stateid = getPosition_StatefromSpin((ArrayList<State>) lstState, string_state);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setCityData("");

                } else {

                }
            }
        });

        spinner_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (EnvMasterId.equalsIgnoreCase("pragati")) {
                    if (lstCity.size() > 0) {
                        string_city = spinner_city.getText().toString().trim();

                        try {
                            Cityid = getPosition_DistrictfromSpin((ArrayList<City>) lstCity, string_city);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //District table
                        String query = "SELECT distinct DistrictDesc,PKDistrictId" +
                                " FROM " + db.TABLE_CITY_PROSPECT +
                                " WHERE DistrictDesc='" + string_city + "'";
                        Cursor cur = sql.rawQuery(query, null);

                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {

                                Cityid = cur.getString(cur.getColumnIndex("PKDistrictId"));

                            } while (cur.moveToNext());


                        } else {
                            Cityid = "";
                        }
                    }
                } else {
                    if (lstcitymaster.size() > 0) {
                        string_city = spinner_city.getText().toString().trim();

                        try {
                            Cityid = getPosition_CityfromSpin((ArrayList<CityMaster>) lstcitymaster, string_city);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //District table
                        String query = "SELECT distinct CityName,PKCityID" +
                                " FROM " + db.TABLE_CITY_MASTER +
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
                }
            }
        });


        spinner_territory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_territory = (String) spinner_territory.getText().toString();
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
    }


    private void getCountry() {


        mList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_COUNTRY;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                mList.add(new Country(cur.getString(cur.getColumnIndex("PKCountryId")),
                        cur.getString(cur.getColumnIndex("CountryName"))));


            } while (cur.moveToNext());

        }
        if (Mode.equalsIgnoreCase("E")) {

            ArrayAdapter<Country> countryArrayAdapter = new ArrayAdapter<Country>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, mList);
            spinner_country.setAdapter(countryArrayAdapter);


            int cntrypos = 0;
            for (int i = 0; i < mList.size(); i++) {
                if (Countryid.equalsIgnoreCase(mList.get(i).getPKCountryId())) {
                    cntrypos = i;
                    CountryName = mList.get(cntrypos).getCountryName();
                    break;

                }
            }

            if (cntrypos != -1) {
                spinner_country.setText(CountryName);
            } else {
                spinner_country.setText(CountryName);
            }


        } else {
            ArrayAdapter<Country> countryArrayAdapter = new ArrayAdapter<Country>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, mList);
            spinner_country.setAdapter(countryArrayAdapter);

            if (mList.size() > 1) {
                spinner_country.setEnabled(true);
            } else {
                spinner_country.setEnabled(false);
            }
        }


        if (cf.getStatecount() > 0) {
            getState();
        } else {
            if (ut.isNet(context)) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        if (Mode.equalsIgnoreCase("E") && !(Countryid == null || Countryid.equalsIgnoreCase(""))) {
                            new DownloadStatelistJSON().execute(Countryid);
                        } else if (mList.get(0) != null) {
                            new DownloadStatelistJSON().execute(mList.get(0).getPKCountryId());
                        }
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }

        }


    }

    private void getState() {
        lstState.clear();

        if (Countryid.equalsIgnoreCase("") || Countryid.equalsIgnoreCase(" ")
                || Countryid.equalsIgnoreCase("null")
                || Countryid.equalsIgnoreCase(null)) {
            Countryid = mList.get(0).getPKCountryId();
        } else {

        }

        /*if(lstState.size() == 0) {
            lstState.add(new State("0","-Select-"));
        }*/

        String query = "SELECT distinct PKStateId,StateDesc" +
                " FROM " + db.TABLE_STATE + " WHERE FKCountryId='" + Countryid + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lstState.add(new State(cur.getString(cur.getColumnIndex("PKStateId")),
                        cur.getString(cur.getColumnIndex("StateDesc"))));

            } while (cur.moveToNext());

            ArrayAdapter<State> statearrayadapter = new ArrayAdapter<State>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstState);
            spinner_state.setAdapter(statearrayadapter);
            //   customDept.notifyDataSetChanged();
            spinner_state.setSelection(0);

            if (lstState.size() > 1) {
                spinner_state.setEnabled(true);
            } else {
                spinner_state.setEnabled(false);
                spinner_state.setText(lstState.get(0).getStateDesc());
                Stateid = lstState.get(0).getPKStateId();
            }

        } else {
            new StartSession(ProspectEnterpriseActivity2.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadStatelistJSON().execute(Countryid);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (Mode.equalsIgnoreCase("E")) {
            ArrayAdapter<State> statearrayadapter = new ArrayAdapter<State>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstState);
            spinner_state.setAdapter(statearrayadapter);
            int statepos = -1;
            for (int i = 0; i < lstState.size(); i++) {
                if (Stateid.equalsIgnoreCase(lstState.get(i).getPKStateId())) {
                    statepos = i;
                    StateName = lstState.get(statepos).getStateDesc();
                    break;
                }
            }

            if (statepos != -1)
                spinner_state.setText(StateName);
            else
                spinner_state.setText(StateName);

            /*if (lstState.size() > 1) {
                spinner_state.setEnabled(true);
            } else {
                spinner_state.setEnabled(false);
            }*/
        } else {

            ArrayAdapter<State> statearrayadapter = new ArrayAdapter<State>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstState);
            spinner_state.setAdapter(statearrayadapter);
            //   customDept.notifyDataSetChanged();
            if (lstState.size() > 0) {
                spinner_state.setText(lstState.get(0).getStateDesc());
            }


           /* if (lstState.size() > 1) {
                spinner_state.setEnabled(true);
            } else {
                spinner_state.setEnabled(false);
            }*/
        }


        if (EnvMasterId.equalsIgnoreCase("pragati")) {
            if (cf.getprospectcitycnt() > 0) {
                getCity();
            } else {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            if (lstState.size() != 0)
                                new DownloadCityJSON().execute(lstState.get(0).getPKStateId());
                            else
                                spinner_state.setVisibility(View.GONE);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        } else {
            if (cf.getcitycnt() > 0) {
                getCitydata(false);
            } else {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            if (lstState.size() != 0)
                                new DownloadCityJSONData().execute(lstState.get(0).getPKStateId());
                            else
                                spinner_state.setVisibility(View.GONE);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        }

    }

    private void getCity() {
//sReferenceType, sReference,sEntity,sConsignee;

        lstCity.clear();
        if (Cityid.equalsIgnoreCase("") || Cityid.equalsIgnoreCase(null) ||
                Cityid.equalsIgnoreCase("null") || Cityid.equalsIgnoreCase(" ")) {
            if (lstState.size() != 0)
                Cityid = lstState.get(0).getPKStateId();

          /*  String query = "SELECT distinct PKDistrictId,DistrictDesc" +
                    " FROM " + db.TABLE_CITY_PROSPECT +
                    " WHERE FKStateId='" + Stateid + "'";

            Cursor cur = sql.rawQuery(query, null);
            //   lstReferenceType.add("Select");

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    if(lstCity.size()==0){
                        lstCity.add(new City("", "-Select-"));
                    }else {
                        lstCity.add(new City(cur.getString(cur.getColumnIndex("PKDistrictId")), cur.getString(cur.getColumnIndex("DistrictDesc"))));
                    }

                } while (cur.moveToNext());

            }*/
        } else {
            String query = "SELECT distinct PKDistrictId,DistrictDesc" +
                    " FROM " + db.TABLE_CITY_PROSPECT +
                    " WHERE FKStateId='" + Stateid + "'";

            Cursor cur = sql.rawQuery(query, null);
            //   lstReferenceType.add("Select");

            if (lstCity.size() == 0) {
                lstCity.add(new City("", "-Select-"));
            }
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    lstCity.add(new City(cur.getString(cur.getColumnIndex("PKDistrictId")), cur.getString(cur.getColumnIndex("DistrictDesc"))));

                } while (cur.moveToNext());

            }
        }


        if (Mode.equalsIgnoreCase("E")) {
            ArrayAdapter<City> cityarraStateArrayAdapter = new ArrayAdapter<City>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstCity);
            spinner_city.setAdapter(cityarraStateArrayAdapter);
            int citypos = -1;
            for (int i = 0; i < lstCity.size(); i++) {
                if (Cityid.equalsIgnoreCase(lstCity.get(i).getPKDistrictId())) {
                    citypos = i;
                    DistrictName = lstCity.get(citypos).getDistrictDesc();

                }
            }

            if (citypos != -1)
                spinner_city.setText(DistrictName);
            else
                spinner_city.setText(DistrictName);


            /*if (lstCity.size() > 1) {
                spinner_city.setEnabled(true);
            } else {
                spinner_city.setEnabled(false);
            }*/
        } else {

            ArrayAdapter<City> cityarraStateArrayAdapter = new ArrayAdapter<City>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstCity);
            spinner_city.setAdapter(cityarraStateArrayAdapter);
            //   customDept.notifyDataSetChanged();
            spinner_city.setText(lstCity.get(0).getDistrictDesc());

            /*if (lstCity.size() > 1) {
                spinner_city.setEnabled(true);
            } else {
                spinner_city.setEnabled(false);
            }*/

        }

        // center_progressbar.setVisibility(View.GONE);


    }

    private void getCitydata(boolean flag) {
//sReferenceType, sReference,sEntity,sConsignee;

        lstcitymaster.clear();
        if (Cityid.equalsIgnoreCase("") || Cityid.equalsIgnoreCase(null) ||
                Cityid.equalsIgnoreCase("null") || Cityid.equalsIgnoreCase(" ")) {
            if (lstState.size() != 0)
                Cityid = lstState.get(0).getPKStateId();

            /*String query = "SELECT distinct PKCityID,CityName" +
                    " FROM " + db.TABLE_CITY_MASTER +
                    " WHERE FKStateId='" + Stateid + "'";
            Log.i("queryStateId::", Stateid);

            Cursor cur = sql.rawQuery(query, null);
            //   lstReferenceType.add("Select");

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {

                    if(lstcitymaster.size()==0){
                        lstcitymaster.add(new CityMaster("", "-Select-"));
                    }else {
                        lstcitymaster.add(new CityMaster(cur.getString(cur.getColumnIndex("PKCityID")),
                                cur.getString(cur.getColumnIndex("CityName"))));
                    }

                } while (cur.moveToNext());

            } else {
                if (ut.isNet(context)) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCityJSONData().execute(Stateid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }*/
        } else {
            String query = "SELECT distinct PKCityID,CityName" +
                    " FROM " + db.TABLE_CITY_MASTER +
                    " WHERE FKStateId='" + Stateid + "'";
            Log.i("queryStateId::", Stateid);
            Cursor cur = sql.rawQuery(query, null);
            //   lstReferenceType.add("Select");

            if (lstcitymaster.size() == 0) {
                lstcitymaster.add(new CityMaster("", "-Select-"));
            }

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    lstcitymaster.add(new CityMaster(cur.getString(cur.getColumnIndex("PKCityID")),
                            cur.getString(cur.getColumnIndex("CityName"))));

                } while (cur.moveToNext());
                spinner_city.setVisibility(View.VISIBLE);
                ln_city.setVisibility(View.VISIBLE);
            } else {
                if (!flag) {
                    if (ut.isNet(context)) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCityJSONData().execute(Stateid);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                } else {
                    spinner_city.setVisibility(View.GONE);
                    ln_city.setVisibility(View.GONE);
                }
            }

        }

        if (Mode.equalsIgnoreCase("E")) {
            ArrayAdapter<CityMaster> cityarraStateArrayAdapter = new ArrayAdapter<CityMaster>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstcitymaster);
            spinner_city.setAdapter(cityarraStateArrayAdapter);
            int citypos = -1;
            for (int i = 0; i < lstcitymaster.size(); i++) {
                if (Cityid.equalsIgnoreCase(lstcitymaster.get(i).getPKCityID())) {
                    citypos = i;
                    CityName = lstcitymaster.get(citypos).getCityName();

                }
            }
            if (citypos != -1)
                spinner_city.setText(CityName);
            else
                spinner_city.setText(CityName);


            /*if (lstCity.size() > 1) {
                spinner_city.setEnabled(true);
            } else {
                spinner_city.setEnabled(false);
            }*/
        } else {

            ArrayAdapter<CityMaster> cityarraStateArrayAdapter = new ArrayAdapter<CityMaster>
                    (ProspectEnterpriseActivity2.this,
                            android.R.layout.simple_spinner_dropdown_item, lstcitymaster);
            spinner_city.setAdapter(cityarraStateArrayAdapter);
            //customDept.notifyDataSetChanged();spinner_city.setText(lstcitymaster.get(0).getCityName());

           /* if (lstcitymaster.size() > 1) {
                spinner_city.setEnabled(true);
            } else {
                spinner_city.setEnabled(false);
            }*/

        }


        //center_progressbar.setVisibility(View.GONE);
    }


    class DownloadCountryListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* if(center_progressbar.getVisibility() == View.VISIBLE){
                dismissProgressDialog();
            }else{*/
            showProgressDialog();
            //  }

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
                sql.delete(db.TABLE_COUNTRY, null, null);
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
       /*     if(center_progressbar.getVisibility() == View.VISIBLE){

            }else {*/
            dismissProgressDialog();
            // }
            if (response.contains("PKCountryId")) {

                getCountry();
            } else {

            }

        }

    }

    class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);
        }

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
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_STATE, null);
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

                    long a = sql.insert(db.TABLE_STATE, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //dismissProgressDialog();
            if (response.equals("") || response.equals("[]")) {

            } else {
                getState();
            }
        }

    }

    class DownloadCityJSON extends AsyncTask<String, Void, String> {
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
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getDistrict
                        + "?Id=" + Stateid;

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);


                sql.delete(db.TABLE_CITY_PROSPECT, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY_PROSPECT, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_CITY_PROSPECT, null, values);

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
            if (response.contains("PKDistrictId")) {
                // getInitiatedby();
                ln_city.setVisibility(View.VISIBLE);
                spinner_city.setVisibility(View.VISIBLE);
                getCity();
            } else {
                ln_city.setVisibility(View.GONE);
                spinner_city.setVisibility(View.GONE);

            }

        }

    }

    class DownloadCityJSONData extends AsyncTask<String, Void, String> {
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
        protected String doInBackground(String... params) {
            String citycnt = params[0];
            Log.i("stateId ::", citycnt);
            try {
                String url = CompanyURL + WebUrlClass.api_getCityMaster
                        + "?PKCityID=" + citycnt;

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                //sql.delete(db.TABLE_CITY_MASTER, null,
                //null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY_MASTER, null);
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

                    long a = sql.insert(db.TABLE_CITY_MASTER, null, values);

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
            if (response.contains("PKCityID")) {

                //   getInitiatedby();
                ln_city.setVisibility(View.VISIBLE);
                spinner_city.setVisibility(View.VISIBLE);
                getCitydata(true);
            } else {
                ln_city.setVisibility(View.GONE);
                spinner_city.setVisibility(View.GONE);
            }

        }

    }

    private String getPosition_Countryfromspin(ArrayList<Country> lst_Country,
                                               String countryName)
            throws JSONException {
        String countryId = "";
        for (Country countryBean : lst_Country) {
            if (countryBean.getCountryName().equalsIgnoreCase(countryName)) {
                countryId = countryBean.getPKCountryId();
            }
        }
        return countryId; //it wasn't found at all
    }


    private String getPosition_StatefromSpin(ArrayList<State> lst_State, String stateName) throws JSONException {

        String stateId = "";
        for (State stateBean : lst_State) {
            if (stateBean.getStateDesc().equalsIgnoreCase(stateName)) {
                stateId = stateBean.getPKStateId();
            }
        }
        return stateId;
    }

    private String getPosition_CityfromSpin(ArrayList<CityMaster> lstCity, String cityName) throws JSONException {

        String cityId = "";
        for (CityMaster citybean : lstCity) {
            if (citybean.getCityName().equalsIgnoreCase(cityName)) {
                cityId = citybean.getPKCityID();
            }
        }

        return cityId;
    }

    private String getPosition_DistrictfromSpin(ArrayList<City> lstDistrict, String districtName) throws JSONException {

        String districtId = "";
        for (City districtBean : lstDistrict) {
            if (districtBean.getDistrictDesc().equalsIgnoreCase(districtName)) {
                districtId = districtBean.getPKDistrictId();
            }
        }
        return districtId;
    }


    public void editContactDetails(final int position, ProspectContact tempValues) {

        String name = lstContact.get(position).getName();
        editDialog = new Dialog(ProspectEnterpriseActivity2.this);
        editDialog.setContentView(R.layout.crm_popupaddcontact);
        //ls_attachname = editDialog.findViewById(R.id.ls_attachname);
        final Button btnSave = ((Button) editDialog.findViewById(R.id.btnpopupSaveContact));
        final Button btnCancel = ((Button) editDialog.findViewById(R.id.btnpopupCancelContact));
        final EditText eName = ((EditText) editDialog.findViewById(R.id.eContactName));

        final EditText eDesignation = ((EditText) editDialog.findViewById(R.id.eContactDesignation));
        final EditText eBirthdate = ((EditText) editDialog.findViewById(R.id.eBirthdate));
        final Spinner sDepartmnt = ((Spinner) editDialog.findViewById(R.id.sDepartment));

        final EditText cEmailId = (EditText) editDialog.findViewById(R.id.econtactpEmailid);
        final EditText cMobilee = (EditText) editDialog.findViewById(R.id.econtactpMobile);
        final EditText cTele = (EditText) editDialog.findViewById(R.id.eContactpTele);
        final EditText cFaxe = (EditText) editDialog.findViewById(R.id.eContactpFax);
        final EditText econtactwhatsapp = (EditText) editDialog.findViewById(R.id.econtactwhatsapp);

        eName.setText(tempValues.getName());
        eDesignation.setText(tempValues.getDesignation());
        eBirthdate.setText(tempValues.getBirthdate());
        //  sDepartmnt.setText();
        cEmailId.setText(tempValues.getEmailId());
        cMobilee.setText(tempValues.getMobile());
        cTele.setText(tempValues.getTelephone());
        cFaxe.setText(tempValues.getFax());
        econtactwhatsapp.setText(tempValues.getWhtsapp());
        lstDept = new ArrayList<String>();
        lstDept.add("--Select Role--");
        lstDept.add("Decision Maker");
        lstDept.add("HR");
        lstDept.add("Marketing");
        lstDept.add("Operator");
        lstDept.add("Purchase");

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectEnterpriseActivity2.this,
                R.layout.crm_custom_spinner_txt, lstDept);
        sDepartmnt.setAdapter(customDept);
        if (tempValues.getDepartment().toString().equalsIgnoreCase("\"--Select--\"")) {
            sDepartmnt.setSelection(0);
        } else if (tempValues.getDepartment().toString().equalsIgnoreCase("Decision Maker")) {
            sDepartmnt.setSelection(1);
        } else if (tempValues.getDepartment().toString().equalsIgnoreCase("HR")) {
            sDepartmnt.setSelection(2);
        } else if (tempValues.getDepartment().toString().equalsIgnoreCase("Marketing")) {
            sDepartmnt.setSelection(3);
        } else if (tempValues.getDepartment().toString().equalsIgnoreCase("Operator")) {
            sDepartmnt.setSelection(4);
        } else if (tempValues.getDepartment().toString().equalsIgnoreCase("Purchase")) {
            sDepartmnt.setSelection(5);
        }
        eBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(ProspectEnterpriseActivity2.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                eBirthdate.setText(year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth);


                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (validateSaveContact()) {
                    String cname = eName.getText().toString();
                    String cDesignation = eDesignation.getText().toString();
                    String cBirthdate = eBirthdate.getText().toString();
                    String cDept = sDepartmnt.getSelectedItem().toString();
                    String cemailid = cEmailId.getText().toString().trim();
                    String ctele = cTele.getText().toString().trim();
                    String cMobile = cMobilee.getText().toString().trim();
                    String cFax = cFaxe.getText().toString().trim();
                    String cWhtsapp = econtactwhatsapp.getText().toString().trim();
                    lstContact.get(position).setName(cname);
                    lstContact.get(position).setBirthdate(cBirthdate);
                    lstContact.get(position).setDepartment(cDept);
                    lstContact.get(position).setDesignation(cDesignation);
                    lstContact.get(position).setEmailId(cemailid);
                    lstContact.get(position).setFax(cFax);
                    lstContact.get(position).setMobile(cMobile);
                    lstContact.get(position).setTelephone(ctele);
                    lstContact.get(position).setWhtsapp(cWhtsapp);

                    new CustomAdapterContactList().updateList(lstContact);

                    //  Log.e("lstContact ", "size : " + ProspectEnterpriseActivity2.lstContact.size());

                    //Log.e("lstContact ", "after size : " + ProspectEnterpriseActivity2.lstContact.size());
                    //notifyDataSetChanged();
                } else {
                    Toast.makeText(ProspectEnterpriseActivity2.this, "Please fill the contact details", Toast.LENGTH_SHORT).show();
                }
                editDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                editDialog.dismiss();

            }
        });

        editDialog.show();


    }

    private void ln_border() {
        ln_firmname.setBackgroundResource(R.drawable.edit_text);
        edt_firmalise.setBackgroundResource(R.drawable.edit_text);
        edt_Address.setBackgroundResource(R.drawable.edit_text);
        ln_city.setBackgroundResource(R.drawable.edit_text);
        ln_state.setBackgroundResource(R.drawable.edit_text);
        ln_country.setBackgroundResource(R.drawable.edit_text);
        ln_territory.setBackgroundResource(R.drawable.edit_text);
        edt_gstn.setBackgroundResource(R.drawable.edit_text);
        edt_TANNO.setBackgroundResource(R.drawable.edit_text);
        edt_TANNONAME.setBackgroundResource(R.drawable.edit_text);
        ln_businessSegment.setBackgroundResource(R.drawable.edit_text);
        edt_website.setBackgroundResource(R.drawable.edit_text);
        ln_source.setBackgroundResource(R.drawable.edit_text);
        ln_reference.setBackgroundResource(R.drawable.edit_text);
        edt_Notes.setBackgroundResource(R.drawable.edit_text);
        edt_ContactName.setBackgroundResource(R.drawable.edit_text);
        edt_ContactDesignation.setBackgroundResource(R.drawable.edit_text);
        ln_Deptrole.setBackgroundResource(R.drawable.edit_text);
        edt_contactEmailid.setBackgroundResource(R.drawable.edit_text);
        edt_contactMobile.setBackgroundResource(R.drawable.edit_text);
        edt_contactwhatsapp.setBackgroundResource(R.drawable.edit_text);
        edt_ContactTele.setBackgroundResource(R.drawable.edit_text);
        edt_Contactskype.setBackgroundResource(R.drawable.edit_text);
        ln_birthday.setBackgroundResource(R.drawable.edit_text);
        ln_Product.setBackgroundResource(R.drawable.edit_text);
        edt_OfficeCnt.setBackgroundResource(R.drawable.edit_text);
        edt_EmployeeStrength.setBackgroundResource(R.drawable.edit_text);
        ln_currency.setBackgroundResource(R.drawable.edit_text);
        ln_turnover.setBackgroundResource(R.drawable.edit_text);
        edt_businessDetails.setBackgroundResource(R.drawable.edit_text);
        edt_village.setBackgroundResource(R.drawable.edit_text);
        ln_District.setBackgroundResource(R.drawable.edit_text);
        ln_sex.setBackgroundResource(R.drawable.edit_text);
        ln_val1.setBackgroundResource(R.drawable.edit_text);
        ln_val2.setBackgroundResource(R.drawable.edit_text);
        ln_val3.setBackgroundResource(R.drawable.edit_text);
        ln_val4.setBackgroundResource(R.drawable.edit_text);
        ln_val5.setBackgroundResource(R.drawable.edit_text);
        ln_val6.setBackgroundResource(R.drawable.edit_text);
        ln_val7.setBackgroundResource(R.drawable.edit_text);
        ln_val8.setBackgroundResource(R.drawable.edit_text);
        ln_val9.setBackgroundResource(R.drawable.edit_text);
        ln_val10.setBackgroundResource(R.drawable.edit_text);
    }

    public void setCityData(String Stateid) {
        String query = "SELECT distinct StateDesc,PKStateId" + " FROM " + db.TABLE_STATE +
                " WHERE PKStateId='" + Stateid + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                //Stateid = cur.getString(cur.getColumnIndex("PKStateId"));
                Stateid = cur.getString(cur.getColumnIndex("PKStateId"));
                Log.i("stateId ::", Stateid);
            } while (cur.moveToNext());

            if (EnvMasterId.equalsIgnoreCase("pragati")) {
                if (cf.getprospectcitycnt() > 0) {
                    getCity();

                } else {
                    if (ut.isNet(context)) {
                        final String finalStateid = Stateid;
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCityJSON().execute(finalStateid);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            } else {
                if (cf.getcitycnt() > 0) {
                    getCitydata(false);

                } else {
                    if (ut.isNet(context)) {
                        final String finalStateid1 = Stateid;
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCityJSONData().execute(finalStateid1);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }


        } else {
            Stateid = "";
        }
    }


}






