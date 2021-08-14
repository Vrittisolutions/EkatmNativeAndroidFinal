package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.bean.CityBean;
import com.vritti.crm.vcrm7.ProspectEnterpriseActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.data.Factory;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.vritti.databaselib.data.Factory.TbudsFactory.CREATE_TABLE_CITIES;
import static com.vritti.databaselib.data.Factory.TbudsFactory.CREATE_TABLE_STATES;

public class AddNewCustProspect extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    Button btnsavecust, btncancelcust;

    List<String> lstCity = new ArrayList<String>();
    List<String> lstState = new ArrayList<String>();
    ArrayList<String> StateList;
    ArrayList<String> CityList;

    private String Statename = "", City_name = "";
    public JSONArray jrresult;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static DatabaseHandlers dbhandler;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String usertype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql_db;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    AppCompatRadioButton Radio_button_married, radio_male, radio_female, radio_transgender, radionutton_single;
    ImageView img_birth_date, img_anniversary;

    LinearLayout layname,laymob, ln_gender, ln_date, ln_marriddstatus, ln_anniversary, ln_qualification, ln_city, ln_country, ln_state, ln_territory,
            ln_businessSegment,ln_source, ln_reference, ln_Product, ln_village, ln_District, ln_sex,len_spouse_name, ln_val1, ln_val2, ln_val3,
            ln_val4, ln_val5, ln_val6, ln_val7, ln_val8, ln_val9, ln_val10;

    TextView tv_gender, tv_birthdate, tv_marriedstatus, tv_qualification, tv_city, tv_state, tv_country, tv_territory, tv_salesfamily,
            tv_sourceofprospect, tv_reference, tv_village, tv_District, tv_sex, tv_val1, tv_val2, tv_val3,
            tv_val4, tv_val5, tv_val6, tv_val7, tv_val8, tv_val9, tv_val10;

    EditText edt_website, edt_name, edt_address, edt_mobile, edt_telephone, edt_email, edt_Experience, editTextDate, edit_anni_date,
            edt_spouse_name, edt_notes, edt_postalcode, edt_middle_name, edt_first_name, edt_last_name,
            edt_village, edt_District, edt_sex, edt_val1, edt_val2, edt_val3,edt_val4, edt_val5, edt_val6,
            edt_val7, edt_val8, edt_val9, edt_val10;

    SearchableSpinner /*spinner_city, spinner_state,*/ spinner_country, spinner_territory, spinner_BusinessSegment, spinner_source,
            spinner_product, spinner_qualification, sReferenceType, sReference, spinner_village, spinner_District, spinner_sex,
            spinner_val1, spinner_val2, spinner_val3,spinner_val4, spinner_val5, spinner_val6, spinner_val7, spinner_val8,
            spinner_val9, spinner_val10;
    
    AutoCompleteTextView spinner_state, spinner_city;

    private static Boolean isMadatory_first_name = true, isMadatory_middle_name = false, isMadatory_last_name = false,
            isMadatory_EmailID = false, isMadatory_Mobile = false, isMadatory_Telephone = false, isMadatory_gender = false,
            isMadatory_birthdate = false, isMadatory_marriedstatus = false,
            isMadatory_Address = false, isMadatory_City = false, isMadatory_Postal = false,
            isMadatory_State = false, isMadatory_Country = false, isMadatory_Territory = false,
            isMadatory_SourceofProspect = false,
            isMadatory_Selectreference = false, isMadatory_notes = true, isMadatory_ProductSalesFamily = false, isMadatory_Experience = false,
            isMadatory_qualification = false;


    private static Boolean isVisible_first_name = true, isVisible_middle_name = false, isVisible_last_name = true,
            isVisible_EmailID = true,
            isVisible_Mobile = true, isVisible_Telephone = true, isVisible_gender = true, isVisible_birthdate = true,
            isVisible_marriedstatus = true,
            isVisible_Address = true, isVisible_City = true, isVisible_postal = true,
            isVisible_State = true, isVisible_Country = true, isVisible_Territory = true,
            isVisible_SourceofProspect = true, isVisible_Selectreference = true,
            isVisible_notes = true, isVisible_ProductSalesFamily = false, isVisible_Experience = false, isVisible_qualification = false;

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

    private String contact, gender, marristatus = "Single", spouse_name = "", product;

    private static String string_city = "", string_country = "", string_state = "",
            string_territory = "", string_qualification = "", string_Source = "", string_reference = "",
            string_product_salesfamily = "", string_refrenceoption = "", string_gender = "Male";

    String KEY_INDIVIDUAL = "Individual Prospect";  //3
    String KEY_SMALL_BUSINESS = "Small Business";   //2
    String KEY_ENTERPRISE = "Enterprise Prospect";  //1
    String HdrID_INDIVIDUAL = "3";  //3
    String HdrID_SMALL_BUSINESS = "2";   //2
    String HdrID_ENTERPRISE = "1";  //1

    private String spin_val1_state, spin_val2_city;
    String Selected_stateID, Selected_cityID;
    String fullName = "", eMail = "", mobileNo = "", address = "", city = "", state = "", postalCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_add_new_cust_prospect);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Customer");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        applyValidation();

       new GetStates().execute();

        setListener();

    }

    public void init(){
        parent = AddNewCustProspect.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Add new customer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);

        btnsavecust = findViewById(R.id.btnsavecust);
        btncancelcust = findViewById(R.id.btncancelcust);

      //  edt_first_name = (EditText) findViewById(R.id.edt_first_name);
      //  edt_middle_name = (EditText) findViewById(R.id.edt_middle_name);
     //   edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_name = (EditText)findViewById(R.id.edt_name);
        edt_name.setVisibility(View.VISIBLE);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_email.setVisibility(View.VISIBLE);

        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_mobile.setVisibility(View.VISIBLE);

        edt_telephone = (EditText) findViewById(R.id.edt_telephone);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        edt_Experience = (EditText) findViewById(R.id.edt_Experience);
        edit_anni_date = (EditText) findViewById(R.id.edit_anni_date);
        edt_spouse_name = (EditText) findViewById(R.id.edt_spouse_name);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_address.setVisibility(View.VISIBLE);

        edt_postalcode = (EditText) findViewById(R.id.edt_postalcode);
        edt_postalcode.setVisibility(View.VISIBLE);

        edt_notes = (EditText) findViewById(R.id.edt_notes);
        edt_village = (EditText) findViewById(R.id.edt_village);
        edt_District = (EditText) findViewById(R.id.edt_District);
        edt_sex = (EditText) findViewById(R.id.edt_sex);
        edt_val1 = (EditText) findViewById(R.id.edt_val1);
        edt_val2 = (EditText) findViewById(R.id.edt_val2);
        edt_val3 = (EditText) findViewById(R.id.edt_val3);
        edt_val4 = (EditText) findViewById(R.id.edt_val4);
        edt_val5 = (EditText) findViewById(R.id.edt_val5);
        edt_val6 = (EditText) findViewById(R.id.edt_val6);
        edt_val7 = (EditText) findViewById(R.id.edt_val7);
        edt_val8 = (EditText) findViewById(R.id.edt_val8);
        edt_val9 = (EditText) findViewById(R.id.edt_val9);
        edt_val10 = (EditText) findViewById(R.id.edt_val10);

        tv_gender = (TextView) findViewById(R.id.txt_gender);
        tv_marriedstatus = (TextView) findViewById(R.id.tv_marriedstatus);
        tv_birthdate = (TextView) findViewById(R.id.txtDate);
        tv_qualification = (TextView) findViewById(R.id.tv_qualification);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setVisibility(View.GONE);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_state.setVisibility(View.GONE);

        tv_territory = (TextView) findViewById(R.id.tv_territory);
        tv_sourceofprospect = (TextView) findViewById(R.id.tv_sourceofprospect);
        tv_salesfamily = (TextView) findViewById(R.id.edt_Products);
        tv_reference = (TextView) findViewById(R.id.tv_reference);
        tv_village = (TextView) findViewById(R.id.tv_village);
        tv_District = (TextView) findViewById(R.id.tv_District);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        tv_val1 = (TextView) findViewById(R.id.tv_val1);
        tv_val2 = (TextView) findViewById(R.id.tv_val2);
        tv_val3 = (TextView) findViewById(R.id.tv_val3);
        tv_val4 = (TextView) findViewById(R.id.tv_val4);
        tv_val5 = (TextView) findViewById(R.id.tv_val5);
        tv_val6 = (TextView) findViewById(R.id.tv_val6);
        tv_val7 = (TextView) findViewById(R.id.tv_val7);
        tv_val8 = (TextView) findViewById(R.id.tv_val8);
        tv_val9 = (TextView) findViewById(R.id.tv_val9);
        tv_val10 = (TextView) findViewById(R.id.tv_val10);

        radio_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radio_female = (AppCompatRadioButton) findViewById(R.id.radio_female);
        radio_transgender = (AppCompatRadioButton) findViewById(R.id.radio_transgender);
        Radio_button_married = (AppCompatRadioButton) findViewById(R.id.radionutton_married);
        radionutton_single = (AppCompatRadioButton) findViewById(R.id.radionutton_single);

        img_birth_date = (ImageView) findViewById(R.id.img_birth_date);
        img_anniversary = (ImageView) findViewById(R.id.img_anniversary);

        layname = (LinearLayout)findViewById(R.id.layname);
        layname.setVisibility(View.VISIBLE);

        ln_marriddstatus = (LinearLayout) findViewById(R.id.ln_marriddstatus);
        len_spouse_name = (LinearLayout) findViewById(R.id.len_spouse_name);
        ln_anniversary = (LinearLayout) findViewById(R.id.lay_anniversary);
        ln_qualification = (LinearLayout) findViewById(R.id.ln_qualification);
        layname = (LinearLayout) findViewById(R.id.layname);
        laymob = (LinearLayout) findViewById(R.id.laymob);
        ln_gender = (LinearLayout) findViewById(R.id.ln_gender);
        ln_date = (LinearLayout) findViewById(R.id.lay_Date);
        ln_city = (LinearLayout) findViewById(R.id.ln_city);
        ln_city.setVisibility(View.VISIBLE);

        ln_state = (LinearLayout) findViewById(R.id.ln_state);
        ln_state.setVisibility(View.VISIBLE);

        ln_country = (LinearLayout) findViewById(R.id.ln_country);
        ln_territory = (LinearLayout) findViewById(R.id.ln_territory);
        ln_Product = (LinearLayout) findViewById(R.id.ln_Product);
        ln_source = (LinearLayout) findViewById(R.id.ln_sourceofprospect);
        ln_reference = (LinearLayout) findViewById(R.id.ln_reference);
        ln_village = (LinearLayout) findViewById(R.id.ln_village);
        ln_District = (LinearLayout) findViewById(R.id.ln_District);
        ln_sex = (LinearLayout) findViewById(R.id.ln_sex);
        ln_val1 = (LinearLayout) findViewById(R.id.ln_val1);
        ln_val2 = (LinearLayout) findViewById(R.id.ln_val2);
        ln_val3 = (LinearLayout) findViewById(R.id.ln_val3);
        ln_val5 = (LinearLayout) findViewById(R.id.ln_val5);
        ln_val4 = (LinearLayout) findViewById(R.id.ln_val4);
        ln_val6 = (LinearLayout) findViewById(R.id.ln_val6);
        ln_val7 = (LinearLayout) findViewById(R.id.ln_val7);
        ln_val8 = (LinearLayout) findViewById(R.id.ln_val8);
        ln_val9 = (LinearLayout) findViewById(R.id.ln_val9);
        ln_val10 = (LinearLayout) findViewById(R.id.ln_val10);

        spinner_qualification = (SearchableSpinner) findViewById(R.id.spinner_qualification);
        spinner_city = findViewById(R.id.eAutoCity);
        //spinner_city.setTitle("Select City");
        spinner_state = findViewById(R.id.spinner_state);
        //spinner_state.setTitle("Select State");
        
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle("Select Country");
        spinner_territory = (SearchableSpinner) findViewById(R.id.eAutoTerritory);
        spinner_territory.setTitle("Select Territory ");
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);
        spinner_product.setTitle("Sales Family");
        spinner_source = (SearchableSpinner) findViewById(R.id.autotxtProspect);
        spinner_source.setTitle("Select Source of Prospect ");
        sReferenceType = (SearchableSpinner) findViewById(R.id.sReferenceType);
        sReference = (SearchableSpinner) findViewById(R.id.sReference);
        spinner_village = (SearchableSpinner) findViewById(R.id.spinner_village);
        spinner_District = (SearchableSpinner) findViewById(R.id.spinner_District);
        spinner_sex = (SearchableSpinner) findViewById(R.id.spinner_sex);
        spinner_val1 = (SearchableSpinner) findViewById(R.id.spinner_val1);
        spinner_val2 = (SearchableSpinner) findViewById(R.id.spinner_val2);
        spinner_val3 = (SearchableSpinner) findViewById(R.id.spinner_val3);
        spinner_val4 = (SearchableSpinner) findViewById(R.id.spinner_val4);
        spinner_val5 = (SearchableSpinner) findViewById(R.id.spinner_val5);
        spinner_val6 = (SearchableSpinner) findViewById(R.id.spinner_val6);
        spinner_val7 = (SearchableSpinner) findViewById(R.id.spinner_val7);
        spinner_val8 = (SearchableSpinner) findViewById(R.id.spinner_val8);
        spinner_val9 = (SearchableSpinner) findViewById(R.id.spinner_val9);
        spinner_val10 = (SearchableSpinner) findViewById(R.id.spinner_val10);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(AddNewCustProspect.this);
        cf = new Tbuds_commonFunctions(AddNewCustProspect.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(parent, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        StateList = new ArrayList<String>();
        CityList = new ArrayList<String>();
    }

    public void setListener(){

        btncancelcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnsavecust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullName = edt_name.getText().toString().trim();
                eMail = edt_email.getText().toString().trim();
                mobileNo = edt_mobile.getText().toString().trim();
                address = edt_address.getText().toString().trim();
                city = spinner_city.getText().toString().trim();
                state = spinner_state.getText().toString().trim();
                postalCode = edt_postalcode.getText().toString().trim();

               if(validate()){

                    new StartSession_tbuds(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new RegisterUser().execute();
                        }
                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Please fill data correctly.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btncancelcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Boolean applyValidation() {
        String query = "SELECT *" +
                " FROM " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " WHERE FKProspectHdrID='" + HdrID_INDIVIDUAL + "'";
        Cursor cur = sql_db.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String PKFieldID = cur.getString(cur.getColumnIndex("PKFieldID"));
                String ProspectHdrID = cur.getString(cur.getColumnIndex("FKProspectHdrID"));
                String ProspectField = cur.getString(cur.getColumnIndex("ProspectField"));
                String isVisible = cur.getString(cur.getColumnIndex("IsVisible"));
                if(isVisible.equalsIgnoreCase("true")){
                    isVisible = "1";
                }else {
                    isVisible = "0";
                }
                String IsMandatory = cur.getString(cur.getColumnIndex("IsMandatory"));
                if(IsMandatory.equalsIgnoreCase("true")){
                    IsMandatory = "1";
                }else {
                    IsMandatory = "0";
                }
                String Caption = cur.getString(cur.getColumnIndex("Caption"));
                String Section = cur.getString(cur.getColumnIndex("Section"));
                String FieldType = cur.getString(cur.getColumnIndex("FieldType"));
                String PKUserId = cur.getString(cur.getColumnIndex("PKUserId"));

              /*  if (ProspectField.equalsIgnoreCase("FirstName")) {
                    edt_first_name.setTag(PKFieldID);
                    edt_first_name.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_first_name.setVisibility(View.VISIBLE);
                        isVisible_first_name = true;
                    } else {
                        edt_first_name.setVisibility(View.GONE);
                        isVisible_first_name = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_first_name = true;
                    } else {
                        isMadatory_first_name = true;
                    }
                } else if (ProspectField.equalsIgnoreCase("MiddleName")) {
                    edt_middle_name.setTag(PKFieldID);
                    edt_middle_name.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_middle_name.setVisibility(View.VISIBLE);
                        isVisible_middle_name = true;
                    } else {
                        edt_middle_name.setVisibility(View.GONE);
                        isVisible_middle_name = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_middle_name = true;
                    } else {
                        isMadatory_middle_name = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("LastName")) {
                    edt_last_name.setTag(PKFieldID);
                    edt_last_name.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_last_name.setVisibility(View.VISIBLE);
                        isVisible_last_name = true;
                    } else {
                        edt_last_name.setVisibility(View.GONE);
                        isVisible_last_name = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_last_name = true;
                    } else {
                        isMadatory_last_name = false;
                    }
                } else*/
                if (ProspectField.equalsIgnoreCase("Name")) {
                    edt_name.setTag(PKFieldID);
                    edt_name.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        layname.setVisibility(View.VISIBLE);
                       // edt_name.setVisibility(View.VISIBLE);
                        isVisible_last_name = true;
                    } else {
                        layname.setVisibility(View.VISIBLE);
                       // layname.setVisibility(View.GONE);
                       // edt_name.setVisibility(View.GONE);
                        isVisible_last_name = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_last_name = true;
                    } else {
                        isMadatory_last_name = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Email ID")) {
                    edt_email.setTag(PKFieldID);
                    edt_email.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_email.setVisibility(View.VISIBLE);
                        isVisible_EmailID = true;
                    } else {
                        edt_email.setVisibility(View.VISIBLE);
                        //edt_email.setVisibility(View.GONE);
                        isVisible_EmailID = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_EmailID = true;
                    } else {
                        isMadatory_EmailID = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Mobile")) {
                    edt_mobile.setTag(PKFieldID);
                    edt_mobile.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        laymob.setVisibility(View.VISIBLE);
                       // edt_mobile.setVisibility(View.VISIBLE);
                        isVisible_Mobile = true;
                    } else {
                        laymob.setVisibility(View.VISIBLE);
                       // laymob.setVisibility(View.GONE);
                       // edt_mobile.setVisibility(View.GONE);
                        isVisible_Mobile = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Mobile = true;
                    } else {
                        isMadatory_Mobile = false;
                    }
                } else/* if (ProspectField.equalsIgnoreCase("HomeNo")) {
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
                } else*/ if (ProspectField.equalsIgnoreCase("Gender")) {
                    ln_gender.setTag(PKFieldID);
                    tv_gender.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_gender.setVisibility(View.VISIBLE);
                        isVisible_gender = true;
                    } else {
                        ln_gender.setVisibility(View.GONE);
                        isVisible_gender = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_gender = true;
                    } else {
                        isMadatory_gender = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Birth Date")) {
                    ln_date.setTag(PKFieldID);
                    tv_birthdate.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_date.setVisibility(View.VISIBLE);
                        isVisible_birthdate = true;
                    } else {
                        ln_date.setVisibility(View.GONE);
                        isVisible_birthdate = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_birthdate = true;
                    } else {
                        isMadatory_birthdate = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Marital Status")) {
                    ln_marriddstatus.setTag(PKFieldID);
                    tv_marriedstatus.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_marriddstatus.setVisibility(View.VISIBLE);
                        isVisible_marriedstatus = true;
                    } else {
                        ln_marriddstatus.setVisibility(View.GONE);
                        isVisible_marriedstatus = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_marriedstatus = true;
                    } else {
                        isMadatory_marriedstatus = false;
                    }
                } else /*if (ProspectField.equalsIgnoreCase("Experience")) {
                    edt_Experience.setTag(PKFieldID);
                    edt_Experience.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_Experience.setVisibility(View.VISIBLE);
                        isVisible_Experience = true;
                    } else {
                        edt_Experience.setVisibility(View.GONE);
                        isVisible_Experience = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Experience = true;
                    } else {
                        isMadatory_Experience = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Qualification")) {
                    ln_qualification.setTag(PKFieldID);
                    tv_qualification.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_qualification.setVisibility(View.VISIBLE);
                        isVisible_qualification = true;
                    } else {
                        ln_qualification.setVisibility(View.GONE);
                        isVisible_qualification = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_qualification = true;
                    } else {
                        isMadatory_qualification = false;
                    }
                } else */if (ProspectField.equalsIgnoreCase("Address")) {
                    try{
                        edt_address.setTag(PKFieldID);
                        edt_address.setHint(Caption);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_address.setVisibility(View.VISIBLE);
                        isVisible_Address = true;
                    } else {
                        edt_address.setVisibility(View.VISIBLE);
                       // edt_address.setVisibility(View.GONE);
                        isVisible_Address = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Address = true;
                    } else {
                        isMadatory_Address = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("City")) { /*FKCityId*/
                    ln_city.setTag(PKFieldID);
                    tv_city.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_city.setVisibility(View.VISIBLE);
                        isVisible_City = true;
                    } else {
                        ln_city.setVisibility(View.VISIBLE);
                      //  ln_city.setVisibility(View.GONE);
                        isVisible_City = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_City = true;
                    } else {
                        isMadatory_City = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Postal Code")) {
                    edt_postalcode.setTag(PKFieldID);
                    edt_postalcode.setHint(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_postalcode.setVisibility(View.VISIBLE);
                        isVisible_postal = true;
                    } else {
                        edt_postalcode.setVisibility(View.VISIBLE);
                      //  edt_postalcode.setVisibility(View.GONE);
                        isVisible_postal = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Postal = true;
                    } else {
                        isMadatory_Postal = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("State")) { /*FKStateId*/
                    ln_state.setTag(PKFieldID);
                    spinner_state.setTag(PKFieldID);
                    tv_state.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_state.setVisibility(View.VISIBLE);
                        spinner_state.setVisibility(View.VISIBLE);
                        isVisible_State = true;
                    } else {
                        ln_state.setVisibility(View.VISIBLE);
                        spinner_state.setVisibility(View.VISIBLE);
                        /*ln_state.setVisibility(View.GONE);
                        spinner_state.setVisibility(View.GONE);*/
                        isVisible_State = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_State = true;
                    } else {
                        isMadatory_State = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("Country")) {/*FKCountryId*/
                    ln_country.setTag(PKFieldID);
                    spinner_country.setTag(PKFieldID);
                    tv_country.setText(Caption);

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
                } else if (ProspectField.equalsIgnoreCase("Territory")) {/*FKTerritoryId*/
                    ln_territory.setTag(PKFieldID);
                    spinner_territory.setTag(PKFieldID);
                    tv_territory.setText(Caption);

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
                } else /*if (ProspectField.equalsIgnoreCase("Sales Family")) {
                    ln_Product.setTag(PKFieldID);
                    spinner_product.setTag(PKFieldID);
                    tv_salesfamily.setText(Caption);
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
                } else*/ if (ProspectField.equalsIgnoreCase("Source")) {
                    ln_source.setTag(PKFieldID);
                    spinner_source.setTag(PKFieldID);
                    tv_sourceofprospect.setText(Caption);
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
                } else if (ProspectField.equalsIgnoreCase("Reference")) {
                    ln_reference.setTag(PKFieldID);
                    sReferenceType.setTag(PKFieldID);
                    sReference.setTag(PKFieldID);
                    tv_reference.setText(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_reference.setVisibility(View.VISIBLE);
                        sReferenceType.setVisibility(View.VISIBLE);
                        sReference.setVisibility(View.VISIBLE);
                        isVisible_Selectreference = true;
                    } else {
                        ln_reference.setVisibility(View.GONE);
                        sReferenceType.setVisibility(View.GONE);
                        sReference.setVisibility(View.GONE);
                        isVisible_Selectreference = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Selectreference = true;
                    } else {
                        isMadatory_Selectreference = false;
                    }
                } else /*if (ProspectField.equalsIgnoreCase("Notes")) {
                    edt_notes.setTag(PKFieldID);
                    edt_notes.setHint(Caption);
                    if (isVisible.equalsIgnoreCase("1")) {
                        edt_notes.setVisibility(View.VISIBLE);
                        isVisible_notes = true;
                    } else {
                        edt_notes.setVisibility(View.GONE);
                        isVisible_notes = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_notes = true;
                    } else {
                        isMadatory_notes = false;
                    }
                } else*/ if (ProspectField.equalsIgnoreCase("Village")) {
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

                } else if (ProspectField.equalsIgnoreCase("Sex")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val1")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val2")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val3")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val4")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val5")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val6")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val7")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val8")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val9")) {
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

                } else if (ProspectField.equalsIgnoreCase("Val10")) {
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

    //GetStates
    private class GetStates extends AsyncTask<Void, Void, JSONArray> {
        String responseString = "", res = "";
        String respState = "";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            /*Toast.makeText(MainActivity.this,
                    "Json data is downloading..",Toast.LENGTH_SHORT).show();*/
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            String url_States = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_STATESLIST;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                res = Utility.OpenconnectionOrferbilling(url_States, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                respState = responseString.toString().replaceAll("\\\\", "");
                String rs = respState;
                Log.e("Response", respState);

                sql_db.execSQL("DROP TABLE IF EXISTS " + dbhandler.TABLE_STATES_SALES);
                sql_db.execSQL(CREATE_TABLE_STATES);
                jrresult = new JSONArray(respState);

                StateList.clear();
                for(int i=0; i<jrresult.length();++i){
                    JSONObject jsonObject = jrresult.getJSONObject(i);
                    String stateId = jsonObject.getString("PKStateId");
                    String statename = jsonObject.getString("StateDesc");

                    if(!statename.equalsIgnoreCase("") && statename !=null){
                        StateList.add(statename);
                    }

                    tcf.addStates(stateId,statename);
                }

            } catch (Exception e) {
                respState = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                    android.R.layout.simple_spinner_dropdown_item,StateList);

            spinner_state.setThreshold(1);
            spinner_state.setAdapter(adapter);

            //StateSpinner.setPrompt("Select State");
            spinner_state.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                   spinner_state.showDropDown();
                    return false;
                }
            });

            spinner_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Object item = adapterView.getItemAtPosition(position);
                    spin_val1_state = spinner_state.getText().toString();
                    try{
                        Selected_stateID = getPositionofStatefromspin(jrresult,spin_val1_state);
                        Log.e("StateID", Selected_stateID);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    System.out.println(item.toString());

                    new GetCities().execute();
                }
            });
        }
    }

    //GetCities
    private class GetCities extends AsyncTask<Void, Void, JSONArray> {
        String responseString = "", res = "";
        String respCity = "";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            /*Toast.makeText(MainActivity.this,
                    "Json data is downloading..",Toast.LENGTH_SHORT).show();*/
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            String url_Cities = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_CITYLIST +
                    "?Stateid="+Selected_stateID;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                res = Utility.OpenconnectionOrferbilling(url_Cities, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");

                respCity = responseString.toString().replaceAll("\\\\", "");
                String rs = respCity;
                Log.e("Response", respCity);

                sql_db.execSQL("DROP TABLE IF EXISTS " + dbhandler.TABLE_CITY_SALES);
                sql_db.execSQL(CREATE_TABLE_CITIES);
                JSONArray jrresult = new JSONArray(respCity);
                CityList.clear();
                for (int i = 0; i < jrresult.length(); ++i) {
                    JSONObject jsonObject = jrresult.getJSONObject(i);
                    String CityId = jsonObject.getString("PKCityID");
                    String CityName = jsonObject.getString("CityName");

                    CityList.add(CityName);
                   tcf.addCities(CityId,CityName, Selected_stateID);
                }

            } catch (Exception e) {
                respCity = "No Data";
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result){
            super.onPostExecute(result);

            if (respCity.contains("No Data")){
                spinner_city.setAdapter(null);
            }else{

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                        android.R.layout.simple_spinner_dropdown_item,CityList);

                spinner_city.setThreshold(1);
                spinner_city.setAdapter(adapter);

                spinner_city.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        spinner_city.showDropDown();
                        return false;
                    }
                });

                spinner_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Object item = adapterView.getItemAtPosition(position);
                        spin_val2_city = spinner_city.getText().toString();
                        try{
                            Selected_cityID = getPositionofCityfromspin(jrresult,spin_val2_city);
                            Log.e("CityID", Selected_cityID);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        System.out.println(item.toString());
                    }
                });
            }
        }
    }

    private String getPositionofStatefromspin(JSONArray jsonArray, String State) throws JSONException {
        String StateId =null;
        for(int index = 0; index < jsonArray.length(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if(jsonObject.getString("StateDesc").equals(State)) {
                StateId = jsonObject.getString("PKStateId");
                Log.e("StateID", StateId);
                //this is the index of the JSONObject you want
            }
        }
        return StateId; //it wasn't found at all
    }

    private String getPositionofCityfromspin(JSONArray jsonArray, String City) throws JSONException {
        String CityId =null;
        for(int index = 0; index < jsonArray.length(); index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            if(jsonObject.getString("CityName").equals(City)) {
                CityId = jsonObject.getString("PKCityID");
                Log.e("CityID", CityId);
                //this is the index of the JSONObject you want
            }
        }
        return CityId; //it wasn't found at all
    }

    class RegisterUser extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "", res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Authenticating User...");
            progressDialog.show();*/
            /*progress = ProgressHUD.show(parent,
                    "Authenticating User...", false, true, null);*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_authentication = AnyMartData.MAIN_URL + AnyMartData.METHOD_USER_REGISTRATION +
                    "?handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID +
                    "&CustVendorType="+ usertype +
                    "&CustVendorName="+ fullName +
                    "&PermanentAddress="+ address +
                    "&City="+ city +
                    "&Pin="+ postalCode +
                    "&MobileNo="+ mobileNo +
                    "&Email="+ eMail +
                    "&State="+ state +
                    "&DeviceRegIdAndroid="+ "" +
                    "&GpsLocationAddress="+ "" +
                    "&Latitude="+""+
                    "&Longitude="+""+
                    "&CurrentAddress="+""+
                    "&OfficeAddress="+"";

            //   url_authentication = url_authentication.replaceAll(" ","%20");

            String auth = url_authentication;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                res = Utility.OpenconnectionOrferbilling(url_authentication, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                res = responseString;
                Log.e("RegResponse", responseString);


            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

           // progress.dismiss();

            if (responseString.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession_tbuds(parent, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new RegisterUser().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }

            } else if (!responseString.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "Customer added successfully", Toast.LENGTH_LONG)
                        .show();
                finish();

            } else {
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    public boolean validate() {
        eMail = edt_email.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        int mobLength = mobileNo.length();
        int postcodelength = postalCode.length();
        // TODO Auto-generated method stub

        if ((fullName.equalsIgnoreCase("") ||
                fullName.equalsIgnoreCase(" ") ||
                fullName.equalsIgnoreCase(null))) {

            edt_name.setError("Please enter fullname");
            Toast.makeText(parent, "Please enter fullname", Toast.LENGTH_LONG).show();

            return false;
        } else if ((eMail.equalsIgnoreCase("") ||
                eMail.equalsIgnoreCase(" ") ||
                eMail.equalsIgnoreCase(null)) ) {

            edt_email.setError("Enter valid email id");
            Toast.makeText(parent, "Please enter email id", Toast.LENGTH_LONG).show();

            return false;
        } else if (((address.equalsIgnoreCase(""))
                || address.equalsIgnoreCase(" "))) {

            edt_address.setError("Please enter address");
            Toast.makeText(parent, "Please enter address", Toast.LENGTH_LONG).show();

            return false;
        } else if ((city.equalsIgnoreCase("") ||
                city.equalsIgnoreCase(" ") ||
                city.equalsIgnoreCase(null))) {

            Toast.makeText(parent, "Please select City", Toast.LENGTH_LONG).show();
            return false;
        } else if ((state.equalsIgnoreCase("") ||
                state.equalsIgnoreCase(" ") ||
                state.equalsIgnoreCase(null))) {

            Toast.makeText(parent, "Please select state", Toast.LENGTH_LONG).show();
            return false;
        } else if ((mobileNo.equalsIgnoreCase("") ||
                mobileNo.equalsIgnoreCase(" ") ||
                mobileNo.equalsIgnoreCase(null) ||
                mobLength != 10)) {

            edt_mobile.setError("Enter valid mobile no.");
            Toast.makeText(parent, "Please enter valid mobile no.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((postalCode.equalsIgnoreCase("") ||
                postalCode.equalsIgnoreCase(" ") ||
                postalCode.equalsIgnoreCase(null)) ||
                postcodelength != 6) {

            edt_postalcode.setError("Enter valid postal code");
            Toast.makeText(parent, "Please enter postalcode", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
