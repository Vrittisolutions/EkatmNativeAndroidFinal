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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CityBean;
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

public class IndividualProspectusActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    Dialog dialog;
    AlertDialog alertDialog;
    EditText editTextDate, edit_anni_date, edt_spouse_name, edt_notes, edt_postalcode, edt_middle_name, edt_first_name, edt_last_name;
    TextView edt_Products;
    Button buttonSave_prospect, buttonSaveandstartcall_prospect, buttonClose_prospect;
    ImageView img_birth_date, img_anniversary;
    private Calendar calendar;
    private DateFormat dateFormat;
    static int year, month, day;
    Date result;
    String date;
    public static boolean[] checkSelected;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    List<String> lstState = new ArrayList<String>();
    ArrayList<String> Productionitems = new ArrayList<String>();
    List<String> lstTerrority = new ArrayList<String>();
    List<String> lstCity = new ArrayList<String>();
    List<String> lstReferenceType = new ArrayList<String>();
    List<String> lstReference = new ArrayList<>();
    List<String> lstSourceProspect = new ArrayList<String>();
    public List<String> lstProduct = new ArrayList<String>();
    private String reftypeid;
    LinearLayout len_spouse_name;
    SharedPreferences Prospectpreference;

    private static String ProspectTypeID;
    AppCompatRadioButton Radio_button_married, radio_male, radio_female, radio_transgender, radionutton_single;
    ProgressBar progressbar_business, proress_reference;
    private String FirmName = "", FirmAlias, Remark, Address,
            CustSegment, CurrentDate = "", Consigneeid,
            empNo, NoFOFFice, TurnOverid, UserLoginId, Emailid, telph,
            mobile, fax, IsLead, ddllead, ddlCustNm;
    String[] susmaster;
    private String finaljson;
    private String Birthdate = "", Anniverssary = "", PKSuspectId, Product_name;
    private String City_name, Territory_name, Source_prospect;

    private String Territoryid = "", Cityid = "", BusDetailid = "",
            SuSpSourceId = "0", Referenceid = "", Currencyid = "", Productid = "",
            Stateid = "", Countryid = "";

    LinearLayout ln_gender, ln_date, ln_marriddstatus, ln_anniversary, ln_qualification, ln_city, ln_country, ln_state, ln_territory, ln_businessSegment,
            ln_source, ln_reference, ln_Product;
    TextView tv_gender, tv_birthdate, tv_marriedstatus, tv_qualification, tv_city, tv_state, tv_country, tv_territory, tv_salesfamily, tv_sourceofprospect, tv_reference;
    EditText edt_website;
    EditText edt_address, edt_mobile, edt_telephone, edt_email, edt_Experience;

    LinearLayout ln_village, ln_District, ln_sex, ln_val1, ln_val2, ln_val3,
            ln_val4, ln_val5, ln_val6, ln_val7, ln_val8, ln_val9, ln_val10;
    TextView tv_village, tv_District, tv_sex, tv_val1, tv_val2, tv_val3,
            tv_val4, tv_val5, tv_val6, tv_val7, tv_val8, tv_val9, tv_val10;

    SearchableSpinner spinner_city, spinner_state, spinner_country, spinner_territory, spinner_BusinessSegment, spinner_source,
            spinner_product, spinner_qualification, sReferenceType, sReference;

    SearchableSpinner spinner_village, spinner_District, spinner_sex, spinner_val1, spinner_val2, spinner_val3,
            spinner_val4, spinner_val5, spinner_val6, spinner_val7, spinner_val8, spinner_val9, spinner_val10;
    EditText edt_village, edt_District, edt_sex, edt_val1, edt_val2, edt_val3,
            edt_val4, edt_val5, edt_val6, edt_val7, edt_val8, edt_val9, edt_val10;

    private static Boolean isMadatory_first_name = true, isMadatory_middle_name = false, isMadatory_last_name = false,
            isMadatory_EmailID = false, isMadatory_Mobile = false, isMadatory_Telephone = false, isMadatory_gender = false,
            isMadatory_birthdate = false, isMadatory_marriedstatus = false, isMadatory_Experience = true,
            isMadatory_qualification = false,
            isMadatory_Address = false, isMadatory_City = false, isMadatory_Postal = false,
            isMadatory_State = false, isMadatory_Country = false, isMadatory_Territory = false,
            isMadatory_SourceofProspect = false, isMadatory_ProductSalesFamily = false,
            isMadatory_Selectreference = false, isMadatory_notes = true;


    private static Boolean isVisible_first_name = true, isVisible_middle_name = false, isVisible_last_name = true,
            isVisible_EmailID = true,
            isVisible_Mobile = true, isVisible_Telephone = true, isVisible_gender = true, isVisible_birthdate = true,
            isVisible_marriedstatus = true, isVisible_Experience = true, isVisible_qualification = true,
            isVisible_Address = true, isVisible_City = true, isVisible_postal = true,
            isVisible_State = true, isVisible_Country = true, isVisible_Territory = true,
            isVisible_SourceofProspect = true,
            isVisible_Selectreference = true,

    isVisible_ProductSalesFamily = true,
            isVisible_notes = true;

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

    private String CustomerName = "", ContactName = "", ContactNumber = "", registeryID = "", Email = "", EnaquiryDetail = "";


    @Override
    protected void onCreate(@org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_individual_lay);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        sql = db.getWritableDatabase();
        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_indivisual, "");
        if (ProspectTypeID.equalsIgnoreCase("")) {
            ProspectTypeID = "3";
        }
        init();
        /*birthdateupdate();
        anniversaryupdate();
*/
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
          /*  efName1.setText(CustomerName);
            efName1.setSelection(efName1.length());*/
            edt_first_name.setText(ContactName);
            edt_first_name.setSelection(edt_first_name.length());
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
        if (cf.check_City() > 0) {
            setautocomplete_city();
        } else {
            if (isnet()) {
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
        if (cf.getProuctcount() > 0) {
            getproduct();
        } else {
            if (isnet()) {
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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
            if (isnet()) {
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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


    }

    private void init() {
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

        edt_first_name = (EditText) findViewById(R.id.edt_first_name);
        edt_middle_name = (EditText) findViewById(R.id.edt_middle_name);
        edt_last_name = (EditText) findViewById(R.id.edt_last_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mobile = (EditText) findViewById(R.id.edt_monbile);
        edt_telephone = (EditText) findViewById(R.id.edt_telephone);

        ln_gender = (LinearLayout) findViewById(R.id.ln_gender);
        tv_gender = (TextView) findViewById(R.id.txt_gender);

        tv_marriedstatus = (TextView) findViewById(R.id.tv_marriedstatus);
        tv_birthdate = (TextView) findViewById(R.id.txtDate);
        tv_qualification = (TextView) findViewById(R.id.tv_qualification);

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_territory = (TextView) findViewById(R.id.tv_territory);

        tv_sourceofprospect = (TextView) findViewById(R.id.tv_sourceofprospect);
        tv_salesfamily = (TextView) findViewById(R.id.edt_Products);
        tv_reference = (TextView) findViewById(R.id.tv_reference);


        radio_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radio_female = (AppCompatRadioButton) findViewById(R.id.radio_female);
        radio_transgender = (AppCompatRadioButton) findViewById(R.id.radio_transgender);

        ln_date = (LinearLayout) findViewById(R.id.lay_Date);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        img_birth_date = (ImageView) findViewById(R.id.img_birth_date);

        ln_marriddstatus = (LinearLayout) findViewById(R.id.ln_marriddstatus);
        Radio_button_married = (AppCompatRadioButton) findViewById(R.id.radionutton_married);
        radionutton_single = (AppCompatRadioButton) findViewById(R.id.radionutton_single);
        len_spouse_name = (LinearLayout) findViewById(R.id.len_spouse_name);
        edt_spouse_name = (EditText) findViewById(R.id.edt_spouse_name);
        ln_anniversary = (LinearLayout) findViewById(R.id.lay_anniversary);
        edit_anni_date = (EditText) findViewById(R.id.edit_anni_date);
        img_anniversary = (ImageView) findViewById(R.id.img_anniversary);

        edt_Experience = (EditText) findViewById(R.id.edt_Experience);

        ln_qualification = (LinearLayout) findViewById(R.id.ln_qualification);
        spinner_qualification = (SearchableSpinner) findViewById(R.id.spinner_qualification);
        edt_address = (EditText) findViewById(R.id.edt_address);

        ln_city = (LinearLayout) findViewById(R.id.ln_city);
        spinner_city = (SearchableSpinner) findViewById(R.id.eAutoCity);
        spinner_city.setTitle("Select City");

        edt_postalcode = (EditText) findViewById(R.id.edt_postalcode);

        ln_state = (LinearLayout) findViewById(R.id.ln_state);
        spinner_state = (SearchableSpinner) findViewById(R.id.spinner_state);
        spinner_state.setTitle("Select State");

        ln_country = (LinearLayout) findViewById(R.id.ln_country);
        spinner_country = (SearchableSpinner) findViewById(R.id.spinner_country);
        spinner_country.setTitle("Select Country");

        ln_territory = (LinearLayout) findViewById(R.id.ln_territory);
        spinner_territory = (SearchableSpinner) findViewById(R.id.eAutoTerritory);
        spinner_territory.setTitle("Select Territory ");

        ln_Product = (LinearLayout) findViewById(R.id.ln_Product);
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);
        spinner_product.setTitle("Sales Family");

        ln_source = (LinearLayout) findViewById(R.id.ln_sourceofprospect);
        spinner_source = (SearchableSpinner) findViewById(R.id.autotxtProspect);
        spinner_source.setTitle("Select Source of Prospect ");


        ln_reference = (LinearLayout) findViewById(R.id.ln_reference);
        sReferenceType = (SearchableSpinner) findViewById(R.id.sReferenceType);
        sReference = (SearchableSpinner) findViewById(R.id.sReference);

        edt_notes = (EditText) findViewById(R.id.edt_notes);


        progressbar_business = (ProgressBar) findViewById(R.id.progressbar_business);
        proress_reference = (ProgressBar) findViewById(R.id.proress_reference);
        buttonSave_prospect = (Button) findViewById(R.id.buttonSave_prospect);
        buttonSaveandstartcall_prospect = (Button) findViewById(R.id.buttonSaveandstartcall_prospect);
        buttonClose_prospect = (Button) findViewById(R.id.buttonClose_prospect);


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


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(IndividualProspectusActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextDate.setText(date);
                                Birthdate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);


                            }
                        }, year, month, day);

                /*Calendar minDate = Calendar.getInstance();
                minDate.set(year, month, day);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());*/
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        img_birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(IndividualProspectusActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextDate.setText(date);
                                Birthdate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });

        edit_anni_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(IndividualProspectusActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                edit_anni_date.setText(date);
                                Anniverssary = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        img_anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(IndividualProspectusActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                edit_anni_date.setText(date);
                                Anniverssary = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);


                            }
                        }, year, month, day);


                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });


        spinner_qualification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_qualification = (String) spinner_qualification.getSelectedItem();
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
        sReferenceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reftypeid = "";
                string_reference = (String) sReferenceType.getSelectedItem();
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
                    new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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

        sReference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_refrenceoption = (String) sReference.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Radio_button_married.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                marristatus = Radio_button_married.getText().toString();
                if (Radio_button_married.isChecked()) {
                    len_spouse_name.setVisibility(View.VISIBLE);
                } else {
                    len_spouse_name.setVisibility(View.GONE);
                }
            }
        });

        radionutton_single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                marristatus = radionutton_single.getText().toString();
                if (len_spouse_name.getVisibility() == View.VISIBLE) {
                    len_spouse_name.setVisibility(View.GONE);
                }
            }
        });
        radio_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = radio_male.getText().toString();
                string_gender = gender;
            }
        });
        radio_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = radio_male.getText().toString();
                string_gender = gender;

            }
        });

        radio_transgender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = radio_transgender.getText().toString();
                string_gender = gender;

            }
        });

        buttonSave_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    System.out.println("Birthdate :" + Birthdate);
                    System.out.println("Birthdate :" + Anniverssary);

                    if (Birthdate == null || Birthdate.equalsIgnoreCase("")) {
                        Birthdate = "1900/01/01";
                    }

                    if (Anniverssary == null || Anniverssary.equalsIgnoreCase("")) {
                        Anniverssary = "1900/01/01";
                    }


                    FirmName = edt_first_name.getText().toString() + " " + edt_middle_name.getText().toString() + " " + edt_last_name.getText().toString();

                    spouse_name = edt_spouse_name.getText().toString();
               /* getId();
             //  getId();*/
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        jsoncontact.put("ContactName", FirmName);
                        jsoncontact.put("Designation", "");
                        jsoncontact.put("EmailId", edt_email.getText().toString());
                        jsoncontact.put("Mobile", edt_mobile.getText().toString());
                        jsoncontact.put("Telephone", edt_telephone.getText().toString());
                        jsoncontact.put("DateofBirth", Birthdate);
                        jsoncontact.put("ContactPersonDept", "");
                        jsoncontact.put("Fax", "");
                        jsoncontact.put("AnniversaryDate", Anniverssary);
                        jsoncontact.put("Gender", gender);
                        jsoncontact.put("MaritalStatus", marristatus);
                        jsoncontact.put("SpouseName", spouse_name);
                        jsoncontact.put("WhatsAppNo", edt_mobile.getText().toString());

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
                        jsonBusinessprospect.put("FirmName", FirmName);
                        jsonBusinessprospect.put("Address", edt_address.getText().toString());
                        jsonBusinessprospect.put("FirmAlias", "");
                        jsonBusinessprospect.put("FKCityId", Cityid);
                        jsonBusinessprospect.put("FKTerritoryId", Territoryid);
                        jsonBusinessprospect.put("FKBusiSegmentId", "");
                        jsonBusinessprospect.put("CompanyURL", CompanyURL);
                        jsonBusinessprospect.put("FKEnqSourceId", SuSpSourceId);
                        jsonBusinessprospect.put("Fax", "");
                        jsonBusinessprospect.put("Notes", edt_notes.getText().toString());
                        jsonBusinessprospect.put("Remark", edt_notes.getText().toString());
                        jsonBusinessprospect.put("Department", "");
                        jsonBusinessprospect.put("BusinessDetails", "");
                        jsonBusinessprospect.put("CurrencyMasterId", "");
                        jsonBusinessprospect.put("CurrencyDesc", "");
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
                        jsonBusinessprospect.put("ProspectType", ProspectTypeID);//"Individual"
                        jsonBusinessprospect.put("Qualification: ", string_qualification);
                        jsonBusinessprospect.put("Experience: ", edt_Experience.getText().toString());


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

                    String fName = edt_first_name.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy HH:mm");
                    String date = sdf.format(new Date());
                    String remark1 = "Promotional form Added for firm " + fName + " on" + date;
                    String url = CompanyURL + WebUrlClass.api_Post_Prospect;

                    String op = "";
                    CreateOfflineIntend(url, finaljson, WebUrlClass.POSTFLAG, remark1, op);

                   /* if (isnet()) {
                        new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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


        buttonSaveandstartcall_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {


                    if (Birthdate.equalsIgnoreCase("")) {
                        Birthdate = "1900/01/01";
                    }
                    if (Anniverssary.equalsIgnoreCase("")) {
                        Anniverssary = "1900/01/01";
                    }


                    FirmName = edt_first_name.getText().toString() + " " + edt_middle_name.getText().toString() + " " + edt_last_name.getText().toString();

                    spouse_name = edt_spouse_name.getText().toString();
                    JSONObject jsoncontact = new JSONObject();
                    try {
                        jsoncontact.put("ContactName", FirmName);
                        jsoncontact.put("Designation", "");
                        jsoncontact.put("EmailId", edt_email.getText().toString());
                        jsoncontact.put("Mobile", edt_mobile.getText().toString());
                        jsoncontact.put("Telephone", edt_telephone.getText().toString());
                        jsoncontact.put("DateofBirth", Birthdate);
                        jsoncontact.put("ContactPersonDept", "");
                        jsoncontact.put("Fax", "");
                        jsoncontact.put("AnniversaryDate", Anniverssary);
                        jsoncontact.put("Gender", gender);
                        jsoncontact.put("MaritalStatus", marristatus);
                        jsoncontact.put("SpouseName", spouse_name);
                        jsoncontact.put("WhatsAppNo", edt_mobile.getText().toString());

                        contact = jsoncontact.toString();

                        System.out.println("Contact list : " + jsoncontact.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
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
                        jsonBusinessprospect.put("FirmName", FirmName);
                        jsonBusinessprospect.put("Address", edt_address.getText().toString());
                        jsonBusinessprospect.put("FirmAlias", "");
                        jsonBusinessprospect.put("FKCityId", Cityid);
                        jsonBusinessprospect.put("FKTerritoryId", Territoryid);
                        jsonBusinessprospect.put("FKBusiSegmentId", "");
                        jsonBusinessprospect.put("CompanyURL", CompanyURL);
                        jsonBusinessprospect.put("FKEnqSourceId", SuSpSourceId);
                        jsonBusinessprospect.put("Fax", "");
                        jsonBusinessprospect.put("Notes", edt_notes.getText().toString());
                        jsonBusinessprospect.put("Remark", "");
                        jsonBusinessprospect.put("Department", "");
                        jsonBusinessprospect.put("BusinessDetails", "");
                        jsonBusinessprospect.put("CurrencyMasterId", "");
                        jsonBusinessprospect.put("CurrencyDesc", "");
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
                        jsonBusinessprospect.put("Qualification: ", string_qualification);
                        jsonBusinessprospect.put("Experience: ", edt_Experience.getText().toString());

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
                        new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(IndividualProspectusActivity.this,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            startService(intent1);
            Intent intent2 = new Intent(IndividualProspectusActivity.this,
                    CallListActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
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
                if (ProspectField.equalsIgnoreCase("FirstName")) {
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
                } else if (ProspectField.equalsIgnoreCase("Mobile")) {
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
                } else if (ProspectField.equalsIgnoreCase("HomeNo")) {
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
                } else if (ProspectField.equalsIgnoreCase("Gender")) {
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
                } else if (ProspectField.equalsIgnoreCase("BirthDate ")) {
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
                } else if (ProspectField.equalsIgnoreCase("Experience")) {
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
                } else if (ProspectField.equalsIgnoreCase("FKCityId")) {
                    ln_city.setTag(PKFieldID);
                    tv_city.setText(Caption);

                    if (isVisible.equalsIgnoreCase("1")) {
                        ln_city.setVisibility(View.VISIBLE);
                        isVisible_City = true;
                    } else {
                        ln_city.setVisibility(View.GONE);
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
                        edt_postalcode.setVisibility(View.GONE);
                        isVisible_postal = false;
                    }
                    if (IsMandatory.equalsIgnoreCase("1")) {
                        isMadatory_Postal = true;
                    } else {
                        isMadatory_Postal = false;
                    }
                } else if (ProspectField.equalsIgnoreCase("FKStateId")) {
                    ln_state.setTag(PKFieldID);
                    spinner_state.setTag(PKFieldID);
                    tv_state.setText(Caption);

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
                } else if (ProspectField.equalsIgnoreCase("FKCountryId")) {
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
                } else if (ProspectField.equalsIgnoreCase("FKTerritoryId")) {
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
                } else if (ProspectField.equalsIgnoreCase("Sales Family")) {
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
                } else if (ProspectField.equalsIgnoreCase("Source of Prospect")) {
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
                } else if (ProspectField.equalsIgnoreCase("SuspectReferenceId")) {
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
                } else if (ProspectField.equalsIgnoreCase("Notes")) {
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
            new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, mList);
        spinner_country.setAdapter(customDept);
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
                if (!isFinishing()) {
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

    private void birthdateupdate() {
        editTextDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void anniversaryupdate() {
        edit_anni_date.setText(dateFormat.format(calendar.getTime()));
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

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        birthdateupdate();
        anniversaryupdate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IndividualProspectusActivity.this.finish();
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstState);

        if (customDept.getCount() != 0) {
            spinner_state.setAdapter(customDept);
        } else {
            Toast.makeText(IndividualProspectusActivity.this, "Data not Found", Toast.LENGTH_SHORT).show();
        }
        //   customDept.notifyDataSetChanged();
        spinner_state.setSelection(0);
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
                if (!isFinishing()) {
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
                        Log.e("data", "" + a);

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
            if (response.contains("FamilyId")) {
                getproduct();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to load product detail. Please refresh data again or contact to support", Toast.LENGTH_LONG).show();
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
                setautocomplete_teritory();
            }

        }

    }

    class DownloadReferencetypeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);

//            proress_reference.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_get_Referencetype;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
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

            //proress_reference.setVisibility(View.GONE);
            if (response.contains("")) {

            }

            getReferencetype();
        }

    }


    private void setautocomplete_teritory() {

        List<Teritorybean> lstdb = cf.getTeritorybean();
        lstTerrority.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstTerrority.add(lstdb.get(i).getTerritoryName());
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstTerrority);

        spinner_territory.setAdapter(customAdcity);
        int a = lstTerrority.indexOf(Territory_name);
        spinner_territory.setSelection(lstTerrority.indexOf(Territory_name));


    }

    private void setautocomplete_city() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstCity.add(lstdb.get(i).getCityName());
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstCity);

        spinner_city.setAdapter(customAdcity);
        int a = lstCity.indexOf(City_name);
        spinner_city.setSelection(lstCity.indexOf(City_name));

        //  eAutoCity.setThreshold(1);


    }

    private void getReferencetype() {
//
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
        MySpinnerAdapter customDept = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstReferenceType);
        sReferenceType.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        sReferenceType.setSelection(0);
    }

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
                if (res != null) {
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            proress_reference.setVisibility(View.GONE);
            getReference();
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstReference);
        sReference.setAdapter(customDept);
        //  customDept.notifyDataSetChanged();
        sReference.setSelection(0);
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

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
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
            try {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=" + URLEncoder.encode("", "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
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
                        Log.e("log :", "" + a);

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

                //   getInitiatedby();
                setautocomplete_city();
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
                if (res != null) {
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
                        Log.e("", "");

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
            setautocomplete_prospect();


        }

    }

    private void setautocomplete_prospect() {

        List<ProspectsourceBean> lstdb = cf.getProspectsourceBean();
        lstSourceProspect.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstSourceProspect.add(lstdb.get(i).getSourceName());


        MySpinnerAdapter customAdcity = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, lstSourceProspect);
        spinner_source.setAdapter(customAdcity);
        int a = lstSourceProspect.indexOf(Source_prospect);
        spinner_source.setSelection(lstSourceProspect.indexOf(Source_prospect));


    }

    private void getproduct() {
//sReferenceType, sReference,sEntity,sConsignee;


        Productionitems.clear();
        String query = "SELECT distinct FamilyId,FamilyDesc" +
                " FROM " + db.TABLE_SALES_FAMILY_PRODUCT;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                Productionitems.add(cur.getString(cur.getColumnIndex("FamilyDesc")));
                // Productionitems.add(cur.getString(cur.getColumnIndex("ItemMasterId")));
            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(IndividualProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        int a = lstProduct.indexOf(Product_name);
        spinner_product.setSelection(Productionitems.indexOf(Product_name));


    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(IndividualProspectusActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Prospect;
            System.out.println("InBusinessAPIURL-1 :" + finaljson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    System.out.println("BusinessAPI-1 :" + response);
                }
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
                Toast.makeText(IndividualProspectusActivity.this, "Prospect updated successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(IndividualProspectusActivity.this, "Prospect added successfully", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    class PostProspectUpdate_savenstartJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(IndividualProspectusActivity.this);
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
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("SaveBusiness :" + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            Toast.makeText(IndividualProspectusActivity.this, "Prospect added succcessfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(IndividualProspectusActivity.this,
                    CreateOpportunityActivity.class);
            intent.putExtra("SuspectID", integer);//"cab7944e-d227-479e-91e4-c7b84d9e26b7"
            startActivity(intent);
            IndividualProspectusActivity.this.finish();
            //onBackPressed();
        }

    }

    public static boolean validatePastDate(Context mContext, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day > currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (month > currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (year > currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void getcontactfetchdetails() {

        String query = "SELECT * FROM " + db.TABLE_CONTACT_DETAILS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                edt_mobile.setText(cur.getString(cur.getColumnIndex("Mobile")));
                edt_email.setText(cur.getString(cur.getColumnIndex("EmailId")));
                edt_telephone.setText(cur.getString(cur.getColumnIndex("Telephone")));
                String Birthdate1 = cur.getString(cur.getColumnIndex("DateofBirth"));
                Birthdate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", Birthdate1);
                editTextDate.setText(Birthdate);


            } while (cur.moveToNext());

        }
    }

    private void getproductfetchdetails() {

        String query = "SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {


                edt_first_name.setText(cur.getString(cur.getColumnIndex("FirmName")));
                edt_address.setText(cur.getString(cur.getColumnIndex("Address")));
                // Product_name=cur.getString(cur.getColumnIndex("ItemDesc"));

                //    edt_Products.setText(Product_name);


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


            } while (cur.moveToNext());

        }
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
                new StartSession(IndividualProspectusActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dismissProgressDialog();
                        new DownloadCityJSON().execute();
                        new DownloadStatelistJSON().execute();
                        new DownloadterritoryJSON().execute();
                        new DownloadSourceofProspectJSON().execute();
                        new DownloadProductJSON().execute();
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
        if (id == R.id.action_menu)

        {
            dialog = new Dialog(IndividualProspectusActivity.this);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.crm_dialog_prospectindividual_lay);

            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView txt_business_prospect = (TextView) dialog.findViewById(R.id.txt_business_prospect);
            TextView txt_individual_prospect = (TextView) dialog.findViewById(R.id.txt_enterprise_prospect);

            txt_business_prospect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (registeryID.equalsIgnoreCase("")) {
                        startActivity(new Intent(IndividualProspectusActivity.this, BusinessProspectusActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(IndividualProspectusActivity.this, BusinessProspectusActivity.class);
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
                        startActivity(new Intent(IndividualProspectusActivity.this, ProspectEnterpriseActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                .putExtra("PKSuspectId", PKSuspectId));
                    } else {
                        Intent i = new Intent(IndividualProspectusActivity.this, ProspectEnterpriseActivity.class);
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

    public boolean validate() {
        String email = edt_email.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // TODO Auto-generated method stub
        if ((edt_first_name.getText().toString().equalsIgnoreCase("") ||
                edt_first_name.getText().toString().equalsIgnoreCase(" ") ||
                edt_first_name.getText().toString().equalsIgnoreCase(null)) && isMadatory_first_name) {
            if (!isVisible_first_name) {
                edt_first_name.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter first name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_middle_name.getText().toString().equalsIgnoreCase("") ||
                edt_middle_name.getText().toString().equalsIgnoreCase(" ") ||
                edt_middle_name.getText().toString().equalsIgnoreCase(null)) && isMadatory_middle_name) {
            if (!isVisible_middle_name) {
                edt_middle_name.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter middle name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_last_name.getText().toString().equalsIgnoreCase("") ||
                edt_last_name.getText().toString().equalsIgnoreCase(" ") ||
                edt_last_name.getText().toString().equalsIgnoreCase(null)) && isMadatory_last_name) {
            if (!isVisible_last_name) {
                edt_last_name.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter last name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_email.getText().toString().equalsIgnoreCase("") ||
                edt_email.getText().toString().equalsIgnoreCase(" ") ||
                edt_email.getText().toString().equalsIgnoreCase(null) ||
                !(email.matches(emailPattern))) && isMadatory_EmailID) {
            if (!isVisible_EmailID) {
                edt_email.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter email or valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_mobile.getText().toString().equalsIgnoreCase("") ||
                edt_mobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_mobile.getText().toString().equalsIgnoreCase(null) || edt_mobile.getText().length() != 10)
                && isMadatory_Mobile) {
            if (!isVisible_Mobile) {
                edt_mobile.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter mobile or valid mobile", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_telephone.getText().toString().equalsIgnoreCase("")
                || edt_telephone.getText().toString().equalsIgnoreCase(" ") ||
                edt_telephone.getText().toString().equalsIgnoreCase(null)) && isMadatory_Telephone) {
            if (!isVisible_Telephone) {
                edt_telephone.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter Telephone", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_gender.equalsIgnoreCase("") ||
                string_gender.equalsIgnoreCase(" ") ||
                string_gender.equalsIgnoreCase(null)) && isMadatory_gender) {
            if (!isVisible_gender) {
                ln_gender.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select gender", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextDate.getText().toString().equalsIgnoreCase("")
                || editTextDate.getText().toString().equalsIgnoreCase(" ") ||
                editTextDate.getText().toString().equalsIgnoreCase(null)) && isMadatory_birthdate) {
            if (!isVisible_birthdate) {
                ln_date.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Select Birthdate", Toast.LENGTH_LONG).show();
            return false;
        } else if ((marristatus.equalsIgnoreCase("") ||
                marristatus.equalsIgnoreCase(" ") ||
                marristatus.equalsIgnoreCase(null)) && isMadatory_marriedstatus) {
            if (!isVisible_marriedstatus) {
                ln_marriddstatus.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Please select married Status", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_Experience.getText().toString().equalsIgnoreCase("")
                || edt_Experience.getText().toString().equalsIgnoreCase(" ") ||
                edt_Experience.getText().toString().equalsIgnoreCase(null)) && isMadatory_Experience) {
            if (!isVisible_Experience) {
                edt_Experience.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter Experience", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_qualification.equalsIgnoreCase("") ||
                string_qualification.equalsIgnoreCase(" ") ||
                string_qualification.equalsIgnoreCase(null)) && isMadatory_qualification) {
            if (!isVisible_qualification) {
                ln_qualification.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select qualification", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_address.getText().toString().equalsIgnoreCase("")
                || edt_address.getText().toString().equalsIgnoreCase(" ") ||
                edt_address.getText().toString().equalsIgnoreCase(null)) && isMadatory_Address) {
            if (!isVisible_Address) {
                edt_address.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter Address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_city.equalsIgnoreCase("") ||
                string_city.equalsIgnoreCase(" ") ||
                string_city.equalsIgnoreCase(null)) && isMadatory_City) {
            if (!isVisible_City) {
                ln_city.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select city", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_postalcode.getText().toString().equalsIgnoreCase("")
                || edt_postalcode.getText().toString().equalsIgnoreCase(" ") ||
                edt_postalcode.getText().toString().equalsIgnoreCase(null)) && isMadatory_Postal) {
            if (!isVisible_postal) {
                edt_postalcode.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter postal code", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_state.equalsIgnoreCase("") ||
                string_state.equalsIgnoreCase(" ") ||
                string_state.equalsIgnoreCase(null)) && isMadatory_State) {
            if (!isVisible_State) {
                ln_state.setVisibility(View.VISIBLE);
                spinner_state.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select state", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_country.equalsIgnoreCase("") ||
                string_country.equalsIgnoreCase(" ") ||
                string_country.equalsIgnoreCase(null)) && isMadatory_Country) {
            if (!isVisible_Country) {
                ln_country.setVisibility(View.VISIBLE);
                spinner_country.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select country", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_territory.equalsIgnoreCase("") ||
                string_territory.equalsIgnoreCase(" ") ||
                string_territory.equalsIgnoreCase(null)) && isMadatory_Territory) {
            if (!isVisible_Territory) {
                ln_territory.setVisibility(View.VISIBLE);
                spinner_territory.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select territory", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_product_salesfamily.equalsIgnoreCase("") ||
                string_product_salesfamily.equalsIgnoreCase(" ") ||
                string_product_salesfamily.equalsIgnoreCase(null)) && isMadatory_ProductSalesFamily) {
            if (!isVisible_ProductSalesFamily) {
                ln_Product.setVisibility(View.VISIBLE);
                spinner_product.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select sales family", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_Source.equalsIgnoreCase("") ||
                string_Source.equalsIgnoreCase(" ") ||
                string_Source.equalsIgnoreCase(null)) && isMadatory_SourceofProspect) {
            if (!isVisible_SourceofProspect) {
                ln_source.setVisibility(View.VISIBLE);
                spinner_source.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select source of prospect", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_reference.equalsIgnoreCase("") ||
                string_reference.equalsIgnoreCase(" ") ||
                string_reference.equalsIgnoreCase(null)) && isMadatory_Selectreference) {
            if (!isVisible_Selectreference) {
                ln_reference.setVisibility(View.VISIBLE);
                sReferenceType.setVisibility(View.VISIBLE);
                sReference.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Please select reference", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_notes.getText().toString().equalsIgnoreCase("")
                || edt_notes.getText().toString().equalsIgnoreCase(" ") ||
                edt_notes.getText().toString().equalsIgnoreCase(null)) && isMadatory_notes) {
            if (!isVisible_notes) {
                edt_notes.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), "Enter notes", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_village.getVisibility() == View.VISIBLE
                && (edt_village.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_village) {
            //  String a = edt_village.getTag().toString();

            if (!isVisible_village) {
                ln_village.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter village", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_District.getVisibility() == View.VISIBLE
                && (edt_District.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_district) {
            if (!isVisible_district) {
                ln_District.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter district", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_sex.getVisibility() == View.VISIBLE
                && (edt_sex.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_sex) {
            if (!isVisible_sex) {
                ln_sex.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter sex", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val1.getVisibility() == View.VISIBLE
                && (edt_val1.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val1) {
            if (!isVisible_val1) {
                ln_val1.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val2.getVisibility() == View.VISIBLE
                && (edt_val2.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val2) {
            if (!isVisible_val2) {
                ln_val2.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val3.getVisibility() == View.VISIBLE
                && (edt_val3.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val3) {
            if (!isVisible_val3) {
                ln_val3.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val4.getVisibility() == View.VISIBLE
                && (edt_val4.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val4) {
            if (!isVisible_val4) {
                ln_val4.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val5.getVisibility() == View.VISIBLE
                && (edt_val5.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val5) {
            if (!isVisible_val5) {
                ln_val5.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val6.getVisibility() == View.VISIBLE
                && (edt_val6.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val6) {
            if (!isVisible_val6) {
                ln_val6.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val7.getVisibility() == View.VISIBLE
                && (edt_val7.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val7) {
            if (!isVisible_val7) {
                ln_val7.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val8.getVisibility() == View.VISIBLE
                && (edt_val8.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val8) {
            if (!isVisible_val8) {
                ln_val8.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val9.getVisibility() == View.VISIBLE
                && (edt_val9.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val9) {
                ln_val9.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_val10.getVisibility() == View.VISIBLE
                && (edt_val10.getText().toString()).equalsIgnoreCase(""))
                && isMadatory_val9) {
            if (!isVisible_val10) {
                ln_val10.setVisibility(View.VISIBLE);

            }
            Toast.makeText(getApplicationContext(), "Enter all details", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

}
