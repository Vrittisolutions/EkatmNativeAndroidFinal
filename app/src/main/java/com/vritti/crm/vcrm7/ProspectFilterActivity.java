package com.vritti.crm.vcrm7;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.bean.BusinessSegmentbean;
import com.vritti.crm.bean.CityBean;
import com.vritti.crm.bean.CityMaster;
import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.LeadWiseBean;
import com.vritti.crm.bean.ListData;
import com.vritti.crm.bean.ProductBean;
import com.vritti.crm.bean.ProspectsourceBean;
import com.vritti.crm.bean.ReferenceBean;
import com.vritti.crm.bean.SalesFamily;
import com.vritti.crm.bean.State;
import com.vritti.crm.bean.Teritorybean;
import com.vritti.crm.bean.Territory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProspectFilterActivity extends AppCompatActivity {

    public static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    String prospectType = "";


    CheckBox checkBox_added_by, checkBoxcity, checkBoxcontact_name, checkBoxmobile_no, checkBoxfirmname, checkBoxProduct,
            checkBoxnewprospectsonly, checkBoxopencallsonly, checkBoxclosedcallsonly, checkBoxwithnocalls, checkBoxAddedPeriod,
            checkBoxfirmalias, checkBoxterritory, checkBoxAddressContains, checkBoxSalesFamily, checkBoxReference, checkBoxBusinesssegment,
            checkBoxsource, checkBoxcountry, checkBoxstate, checkBoxSurveyRating, checkBoxViewcustomersonly, checkBoxViewResellersOnly,
            checkBoxNotSurveyRating, checkBoxNotFuturPlanDate,
            checkBoxLastSODate, checkBOXLastSOScheduleDate, checkBOXLastShipmentDate;
    AutoCompleteTextView eAutoCity, spinner_added_date, edtcontact_name, edtmobile_no, edtalias,
            edt_territory, edtsource, edt_country, edtSurveyRating, spinner_added_by, spinner_viewReseller, edt_Reference2;

    ImageView buttonShowProspect, buttonAddProspect;
    ProgressHUD progressHUD;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    public static Context context;
    List<String> lstProduct = new ArrayList<String>();
    List<String> lstadded_by = new ArrayList<String>();
    String SelCriteria = "";
    ProgressBar progressbar;
    LinearLayout ln_viewReseller;
    String viewResellerName = "", viewResellerCode = "0";


    String newcalls, CityName;
    public static ArrayAdapter<CityBean> myAdapter;
    String City, Before_date, After_date, Added_id;
    List<String> lstCity = new ArrayList<String>();
    ArrayList<CityBean> cityBeen;
    private String Cityid;
    List<CityBean> lstdb;
    AutoCompleteTextView spinner_product;
    private String Productname;
    LinearLayout len_added_date;

    private String Month, Start_time;
    LinearLayout len_after, len_before, ln_SurveyRating, ln_SurveyNotRating, ln_bet_addedby;
    EditText edt_after_date, edt_before_date;
    ImageView img_date, img_date1;
    TextInputLayout ln_state, ln_firm_name, ln_added_by, ln_product, ln_contact_name, ln_mobile_no,
            ln_city, ln_firm_alias, ln_territory, ln_source, ln_country, ln_AddressContains, ln_SalesFamily, ln_Reference, ln_Businesssegment,
            ln_futurePlanSpinner, ln_futurePlandt, ln_lastSOSpinner, ln_lastSOEdt, ln_lastSOScSpinner, ln_lastSOScEdt, ln_lastshipDtSpinner,
            ln_lastshipDtEdt;
    LinearLayout ln_bet_Future, ln_SODt, ln_SOSCDt, ln_ShipmentDt;

    AutoCompleteTextView edtfirm, edt_state, edt_businessDetails, edt_AddressContains, edt_SalesFamily, edt_Reference,
            edt_Businesssegment;
    TextView edt_FromDt_addedby, edt_ToDt_addedby, edt_addeddate;
    ArrayList<State> stateArrayList;
    ArrayList<CityMaster> cityArrayList;
    ArrayList<Country> countryArrayList;
    ArrayList<ProductBean> saleFamilyArrayList;
    ArrayList<Teritorybean> teritorybeanArrayList;
    ArrayList<BusinessSegmentbean> businessSegmentbeanArrayList;
    ArrayList<ProspectsourceBean> prospectsourceBeanArrayList;
    ArrayList<ReferenceBean> referenceBeanArrayList;
    List<String> lstTerrority = new ArrayList<String>();
    String countryid = "", stateId = "", cityId = "", territoryId = "", sourceId = "", salesId = "", businessId = "";
    String saleFamilyName = "", cityName = "", referenceName = "", referenceId = "", reference2Name = "", reference2Id = "";
    AutoCompleteTextView spinner_FuturePlan, spinner_LastSOScheduleDate, spinner_LastSODate, spinner_LastShipmentDate;
    ImageView img_futurePlan, img_SoDate, img_SoScheduleDate, img_LastShipmentDate, img_SurveyRating;
    TextView edtLastSOScheduleDate, edt_LastShipmentDate;
    TextView edt_FromDt_Future, edt_ToDt_Future, edtFuturePlanDate, edt_FromDt_SODate, edt_ToDt_SODate, edt_FromDt_SOSCDate,
            edt_ToDt_SOSCDate, edt_FromDt_Shipment, edt_ToDt_Shipment, edtLastSODate, edtSurveyNotRating;

    String notSurveyafter = "", fromDt_Future = "", toDt_Future = "", futurePlanDate = "", fromDt_SODate = "", toDt_SODate = "", lastSODate = "",
            fromDt_SOSCDate = "", toDt_SOSCDate = "", lastSOScheduleDate = "", fromDt_Shipment = "", toDt_Shipment = "", lastShipmentDate = "",
            addeddate = "", fromDt_addedby = "", toDt_addedby = "";

    LinearLayout ln_Reference2;
    ArrayList<LeadWiseBean> leadWiseBeanArrayList;

    ImageView img_add, img_refresh, img_back;
    TextView txt_title;

    boolean isTerritory,isSource,isCountry,isState,isCity,isSalesFamily,isBusinessSegment,isAddedBy;
    public static final int COUNTRY = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_prospect_filter);
        context = ProspectFilterActivity.this;
        //onHideKeyBoard();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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


        cityBeen = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();
        teritorybeanArrayList = new ArrayList<>();
        businessSegmentbeanArrayList = new ArrayList<>();
        prospectsourceBeanArrayList = new ArrayList<>();
        referenceBeanArrayList = new ArrayList<>();
        saleFamilyArrayList = new ArrayList<>();
        leadWiseBeanArrayList = new ArrayList<>();

        init();
        setListener();
        DateListner();

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        String futurePlanDate1 = mDay + "/"
                + String.format("%02d", (mMonth + 1))
                + "/" + mYear;

        addeddate = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate1);
        fromDt_addedby = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate1);
        toDt_addedby = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate1);

        notSurveyafter = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        fromDt_Future = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        toDt_Future = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        futurePlanDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        fromDt_SODate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        toDt_SODate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        lastSODate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        fromDt_SOSCDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        toDt_SOSCDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        lastSOScheduleDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        fromDt_Shipment = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        toDt_Shipment = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        lastShipmentDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        lastShipmentDate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        // addeddate = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        //  fromDt_addedby = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;
        //  toDt_addedby = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + mDay;


        edtSurveyNotRating.setText(futurePlanDate1);
        edtFuturePlanDate.setText(futurePlanDate1);
        edtLastSODate.setText(futurePlanDate1);
        edtLastSOScheduleDate.setText(futurePlanDate1);
        edt_LastShipmentDate.setText(futurePlanDate1);
        edt_FromDt_Future.setText(futurePlanDate1);
        edt_ToDt_Future.setText(futurePlanDate1);
        edt_FromDt_SODate.setText(futurePlanDate1);
        edt_ToDt_SODate.setText(futurePlanDate1);
        edt_FromDt_SOSCDate.setText(futurePlanDate1);
        edt_ToDt_SOSCDate.setText(futurePlanDate1);
        edt_FromDt_Shipment.setText(futurePlanDate1);
        edt_ToDt_Shipment.setText(futurePlanDate1);
        edt_addeddate.setText(futurePlanDate1);
        edt_FromDt_addedby.setText(futurePlanDate1);
        edt_ToDt_addedby.setText(futurePlanDate1);


        lstdb = cf.getCitybean();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Start_time = format.format(cal.getTime());

        SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        String data = userpreferences.getString(WebUrlClass.Key_indivisual, "");

        /*if (data.equalsIgnoreCase("")) {
            new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //new DownloadProspectID().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }*/

      if (cf.getCountrycount() > 0) {
            AllMethodCall();
        } else {

          if (isnet()) {
              new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                  @Override
                  public void callMethod() {
                      new DownloadAllDropDown().execute();
                  }

                  @Override
                  public void callfailMethod(String msg) {

                  }
              });


          } else {
              Toast.makeText(ProspectFilterActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
          }
      }
        //}

        if (cf.getSalesFamilyProuctcount() > 0) {
            getproduct();
        } else {
            if (ut.isNet(context)) {
                new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
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

      /*  if (cf.check_City() > 0) {
            setautocomplete_city();
        } else {
            if (isnet()) {
                new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
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

        if (cf.getAddbycount() > 0) {
            getAddedby();
        } else {
            if (isnet()) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadAddedByJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

       /* if (cf.getProuctcount() > 0) {
            getproduct();
        } else {
            if (isnet()) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadProductJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }*/

    }

    private void DateListner() {

        edtFuturePlanDate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate1 = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                futurePlanDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate1);
                                edtFuturePlanDate.setText(futurePlanDate1);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edtLastSODate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                lastSODate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edtLastSODate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edtLastSOScheduleDate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                lastSOScheduleDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edtLastSOScheduleDate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edt_LastShipmentDate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                lastShipmentDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_LastShipmentDate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edt_FromDt_Future.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                fromDt_Future = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_FromDt_Future.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edt_ToDt_Future.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                toDt_Future = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_ToDt_Future.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edt_FromDt_SODate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate1 = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                fromDt_SODate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate1);
                                edt_FromDt_SODate.setText(futurePlanDate1);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edt_ToDt_SODate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                toDt_SODate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_ToDt_SODate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edt_FromDt_SOSCDate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                fromDt_SOSCDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_FromDt_SOSCDate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edt_ToDt_SOSCDate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                toDt_SOSCDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_ToDt_SOSCDate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });
        edt_FromDt_Shipment.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                fromDt_Shipment = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_FromDt_Shipment.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edt_FromDt_Shipment.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                fromDt_Shipment = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_FromDt_Shipment.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edt_ToDt_Shipment.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                toDt_Shipment = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edt_ToDt_Shipment.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });
        edt_FromDt_addedby.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                //fromDt_addedby = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                fromDt_addedby = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate);
                                edt_FromDt_addedby.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });
        edt_ToDt_addedby.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                toDt_addedby = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate);
                                edt_ToDt_addedby.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        edt_addeddate.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;

                                addeddate = formateDateFromstring("dd/MM/yyyy", "dd MMM yyyy", futurePlanDate);
                                edt_addeddate.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        edtSurveyNotRating.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                String futurePlanDate = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
//YYYY/MM/DD
                                notSurveyafter = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", futurePlanDate);
                                edtSurveyNotRating.setText(futurePlanDate);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


    }

    /*private void displayProduct() {
        List<ProductBean> lstdb = db.getProduct();
        lstProduct.clear();
        for (int i = 0; i < lstdb.size(); i++) {
            lstProduct.add(lstdb.get(i).getItemDesc());
            String item = lstdb.get(i).getItemDesc();
            MySpinnerAdapter adapter = new MySpinnerAdapter(ProspectFilterActivity.this,
                    R.layout.crm_custom_spinner_txt, lstProduct);
            edtProduct.setAdapter(adapter);
            edtProduct.setThreshold(1);
        }


    }*/

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

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_title = findViewById(R.id.txt_title);
        // img_add=findViewById(R.id.img_add);
        img_back = findViewById(R.id.img_back);

        txt_title.setText("Search Prospect");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkBoxfirmname = (CheckBox) findViewById(R.id.checkBoxfirmname);
        checkBoxProduct = (CheckBox) findViewById(R.id.checkBoxProduct);
        checkBoxnewprospectsonly = (CheckBox) findViewById(R.id.checkBoxnewprospectsonly);
        checkBoxopencallsonly = (CheckBox) findViewById(R.id.checkBoxopencallsonly);
        checkBoxclosedcallsonly = (CheckBox) findViewById(R.id.checkBoxclosedcallsonly);
        checkBoxwithnocalls = (CheckBox) findViewById(R.id.checkBoxwithnocalls);
        checkBoxcontact_name = (CheckBox) findViewById(R.id.checkBoxcontact_name);
        checkBoxmobile_no = (CheckBox) findViewById(R.id.checkBoxmobile_no);
        checkBoxcity = (CheckBox) findViewById(R.id.checkBoxcity);
        checkBoxAddedPeriod = (CheckBox) findViewById(R.id.checkBoxAddedPeriod);
        checkBoxterritory = (CheckBox) findViewById(R.id.checkBoxterritory);
        checkBoxfirmalias = (CheckBox) findViewById(R.id.checkBoxfirmalias);
        checkBoxsource = (CheckBox) findViewById(R.id.checkBoxsource);
        checkBoxcountry = (CheckBox) findViewById(R.id.checkBoxcountry);
        checkBoxstate = (CheckBox) findViewById(R.id.checkBoxstate);
        checkBoxSurveyRating = (CheckBox) findViewById(R.id.checkBoxSurveyRating);
        checkBoxViewcustomersonly = (CheckBox) findViewById(R.id.checkBoxViewcustomersonly);
        checkBoxViewResellersOnly = (CheckBox) findViewById(R.id.checkBoxViewResellersOnly);
        checkBoxNotFuturPlanDate = (CheckBox) findViewById(R.id.checkBoxNotFuturPlanDate);
        checkBoxNotSurveyRating = (CheckBox) findViewById(R.id.checkBoxNotSurveyRating);
        checkBoxLastSODate = (CheckBox) findViewById(R.id.checkBoxLastSODate);
        checkBOXLastSOScheduleDate = (CheckBox) findViewById(R.id.checkBOXLastSOScheduleDate);
        checkBOXLastShipmentDate = (CheckBox) findViewById(R.id.checkBOXLastShipmentDate);


        checkBoxAddressContains = (CheckBox) findViewById(R.id.checkBoxAddressContains);
        checkBoxSalesFamily = (CheckBox) findViewById(R.id.checkBoxSalesFamily);
        checkBoxReference = (CheckBox) findViewById(R.id.checkBoxReference);
        checkBoxBusinesssegment = (CheckBox) findViewById(R.id.checkBoxBusinesssegment);
        edtfirm = findViewById(R.id.edtfirm);
        edtcontact_name = findViewById(R.id.edtcontact_name);
        edtmobile_no = findViewById(R.id.edtmobile_no);
        edtalias = findViewById(R.id.edtalias);
        edt_territory = findViewById(R.id.edt_territory);
        edtsource = findViewById(R.id.edtsource);
        edt_country = findViewById(R.id.edt_country);
        edt_state = findViewById(R.id.edt_state);
        edtSurveyRating = findViewById(R.id.edtSurveyRating);
        edtSurveyNotRating = findViewById(R.id.edtSurveyNotRating);
        //edt_businessDetails=  findViewById(R.id.edt_businessDetails);
        edt_AddressContains = findViewById(R.id.edt_AddressContains);
        edt_SalesFamily = findViewById(R.id.edt_SalesFamily);
        edt_Reference = findViewById(R.id.edt_Reference);
        edt_Businesssegment = findViewById(R.id.edt_Businesssegment);
        spinner_FuturePlan = findViewById(R.id.spinner_FuturePlan);
        img_SoDate = findViewById(R.id.img_SoDate);
        img_futurePlan = findViewById(R.id.img_futurePlan);
        spinner_LastSOScheduleDate = findViewById(R.id.spinner_LastSOScheduleDate);
        img_SoScheduleDate = findViewById(R.id.img_SoScheduleDate);
        img_LastShipmentDate = findViewById(R.id.img_LastShipmentDate);
        spinner_LastSODate = findViewById(R.id.spinner_LastSODate);
        spinner_LastShipmentDate = findViewById(R.id.spinner_LastShipmentDate);
        ln_bet_Future = findViewById(R.id.ln_bet_Future);
        ln_SODt = findViewById(R.id.ln_SODt);
        ln_SOSCDt = findViewById(R.id.ln_SOSCDt);
        ln_ShipmentDt = findViewById(R.id.ln_ShipmentDt);
        edt_addeddate = findViewById(R.id.edt_addeddate);


        eAutoCity = findViewById(R.id.eAutoCity);
        buttonShowProspect = (ImageView) findViewById(R.id.buttonShowProspect);
        buttonAddProspect = (ImageView) findViewById(R.id.buttonAddProspect);
        progressbar = (ProgressBar) findViewById(R.id.progressbar_1);
        spinner_product = findViewById(R.id.spinner_product);
        ln_added_by = findViewById(R.id.ln_added_by);
        ln_firm_name = findViewById(R.id.ln_firm_name);
        ln_product = findViewById(R.id.ln_product);
        ln_contact_name = findViewById(R.id.ln_contact_name);
        ln_mobile_no = findViewById(R.id.ln_mobile_no);
        ln_city = findViewById(R.id.ln_city);
        ln_added_by = findViewById(R.id.ln_added_by);
        ln_firm_alias = findViewById(R.id.ln_firm_alias);
        ln_territory = findViewById(R.id.ln_territory);
        ln_source = findViewById(R.id.ln_source);
        ln_state = findViewById(R.id.ln_state);
        ln_country = findViewById(R.id.ln_country);

        len_added_date = (LinearLayout) findViewById(R.id.len_added_date);
        ln_AddressContains = findViewById(R.id.ln_AddressContains);

        spinner_added_date = findViewById(R.id.spinner_added_date);
        edt_FromDt_addedby = findViewById(R.id.edt_FromDt_addedby);
        edt_ToDt_addedby = findViewById(R.id.edt_ToDt_addedby);

        len_after = (LinearLayout) findViewById(R.id.len_after);
        len_before = (LinearLayout) findViewById(R.id.len_before);
        ln_SurveyRating = findViewById(R.id.ln_SurveyRating);
        ln_SurveyNotRating = findViewById(R.id.ln_SurveyNotRating);
        ln_bet_addedby = findViewById(R.id.ln_bet_addedby);
        ln_SalesFamily = findViewById(R.id.ln_SalesFamily);
        ln_Reference = findViewById(R.id.ln_Reference);
        ln_Businesssegment = findViewById(R.id.ln_Businesssegment);
        edt_after_date = (EditText) findViewById(R.id.edt_after_date);
        edt_before_date = (EditText) findViewById(R.id.edt_before_date);

        img_date = (ImageView) findViewById(R.id.img_date);
        img_date1 = (ImageView) findViewById(R.id.img_date1);

        spinner_added_by = findViewById(R.id.spinner_added_by);
        checkBox_added_by = (CheckBox) findViewById(R.id.checkBox_added_by);
        edtFuturePlanDate = findViewById(R.id.edtFuturePlanDate);
        edtLastSODate = findViewById(R.id.edtLastSODate);
        edtLastSOScheduleDate = findViewById(R.id.edtLastSOScheduleDate);
        edt_LastShipmentDate = findViewById(R.id.edt_LastShipmentDate);
        img_SurveyRating = findViewById(R.id.img_SurveyRating);
        ln_futurePlanSpinner = findViewById(R.id.ln_futurePlanSpinner);
        ln_futurePlandt = findViewById(R.id.ln_futurePlandt);
        ln_lastSOSpinner = findViewById(R.id.ln_lastSOSpinner);
        ln_lastSOEdt = findViewById(R.id.ln_lastSOEdt);
        ln_lastSOScSpinner = findViewById(R.id.ln_lastSOScSpinner);
        ln_lastSOScEdt = findViewById(R.id.ln_lastSOScEdt);
        ln_lastshipDtSpinner = findViewById(R.id.ln_lastshipDtSpinner);
        ln_lastshipDtEdt = findViewById(R.id.ln_lastshipDtEdt);
        edt_FromDt_Future = findViewById(R.id.edt_FromDt_Future);
        edt_ToDt_Future = findViewById(R.id.edt_ToDt_Future);
        edt_FromDt_SODate = findViewById(R.id.edt_FromDt_SODate);
        edt_ToDt_SODate = findViewById(R.id.edt_ToDt_SODate);
        edt_FromDt_SOSCDate = findViewById(R.id.edt_FromDt_SOSCDate);
        edt_ToDt_SOSCDate = findViewById(R.id.edt_ToDt_SOSCDate);
        edt_FromDt_Shipment = findViewById(R.id.edt_FromDt_Shipment);
        edt_ToDt_Shipment = findViewById(R.id.edt_ToDt_Shipment);
        ln_viewReseller = findViewById(R.id.ln_viewReseller);
        spinner_viewReseller = findViewById(R.id.spinner_viewReseller);
        ln_Reference2 = findViewById(R.id.ln_Reference2);
        edt_Reference2 = findViewById(R.id.edt_Reference2);

        String[] datePlan = getResources().getStringArray(R.array.CRMDateFilter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, datePlan);

        String[] viewReseller = getResources().getStringArray(R.array.ViewReseller);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, viewReseller);

        String[] addedPeriod = getResources().getStringArray(R.array.AddedPeriod);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.crm_custom_spinner_txt, addedPeriod);


        spinner_FuturePlan.setAdapter(adapter);
        spinner_LastSODate.setAdapter(adapter);
        spinner_LastSOScheduleDate.setAdapter(adapter);
        spinner_LastShipmentDate.setAdapter(adapter);

        spinner_viewReseller.setAdapter(adapter1);

        spinner_added_date.setAdapter(adapter2);


        spinner_added_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });


        spinner_viewReseller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        spinner_viewReseller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewResellerName = spinner_viewReseller.getText().toString();
                if (viewResellerName.equals("Vendor")) {
                    viewResellerCode = "1";
                } else if (viewResellerName.equals("Retail Agency")) {
                    viewResellerCode = "2";
                } else if (viewResellerName.equals("Corporate Agency")) {
                    viewResellerCode = "3";
                } else if (viewResellerName.equals("Retails Dealer")) {
                    viewResellerCode = "4";
                }

            }
        });


        spinner_FuturePlan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        spinner_FuturePlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String clickItem = spinner_FuturePlan.getText().toString();

                if (clickItem.equals("Between")) {
                    edtFuturePlanDate.setVisibility(View.GONE);
                    //img_futurePlan.setVisibility(View.GONE);
                    ln_bet_Future.setVisibility(View.VISIBLE);
                } else {
                    edtFuturePlanDate.setVisibility(View.VISIBLE);
                    //img_futurePlan.setVisibility(View.GONE);
                    ln_bet_Future.setVisibility(View.GONE);
                }
            }
        });

        spinner_LastSODate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        spinner_LastSODate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String clickItem = spinner_LastSODate.getText().toString();

                if (clickItem.equals("Between")) {
                    edtLastSODate.setVisibility(View.GONE);
                    //img_futurePlan.setVisibility(View.GONE);
                    ln_SODt.setVisibility(View.VISIBLE);
                } else {
                    edtLastSODate.setVisibility(View.VISIBLE);
                    //img_futurePlan.setVisibility(View.GONE);
                    ln_SODt.setVisibility(View.GONE);
                }
            }
        });


        spinner_LastSOScheduleDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        spinner_LastSOScheduleDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String clickItem = spinner_LastSOScheduleDate.getText().toString();

                if (clickItem.equals("Between")) {
                    edtLastSOScheduleDate.setVisibility(View.GONE);
                    ln_SOSCDt.setVisibility(View.VISIBLE);
                } else {
                    edtLastSOScheduleDate.setVisibility(View.VISIBLE);
                    ln_SOSCDt.setVisibility(View.GONE);
                }
            }
        });


        spinner_LastShipmentDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView) view).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        spinner_LastShipmentDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String clickItem = spinner_LastShipmentDate.getText().toString();

                if (clickItem.equals("Between")) {
                    edt_LastShipmentDate.setVisibility(View.GONE);
                    ln_ShipmentDt.setVisibility(View.VISIBLE);
                } else {
                    edt_LastShipmentDate.setVisibility(View.VISIBLE);
                    ln_ShipmentDt.setVisibility(View.GONE);
                }
            }
        });

     /*   spinner_added_by.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct UserName,UserLoginId" +
                        " FROM " + db.TABLE_AddBy +
                        " WHERE UserName='" + spinner_added_by.getText().toString().trim() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Added_id = cur.getString(cur.getColumnIndex("UserLoginId"));

                    } while (cur.moveToNext());

                } else {
                    Added_id = "";
                }
            }
        });*/


       /* img_date.setOnClickListener(new View.OnClickListener() {
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
                               *//* String[] monthName = {"January", "February",
                                        "March", "April", "May", "June",
                                        "July", "August", "September",
                                        "October", "November", "December"};

                                eBirthdate.setText(dayOfMonth + " "
                                        + monthName[monthOfYear] + " " + year);*//*
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                Before_date = formateDateFromstring("yyyy/MM/dd", "dd MMM yyyy", date_Before);
                                edt_before_date.setText(Before_date);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });*/
        img_date1.setOnClickListener(new View.OnClickListener() {
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
                                String date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;

                                After_date = formateDateFromstring("yyyy/MM/dd", "dd MMM yyyy", date_Before);
                                edt_after_date.setText(Before_date);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });


        spinner_added_date.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Month = spinner_added_date.getText().toString().trim();
                if (Month.equalsIgnoreCase("Last Month")) {
                    // len_before.setVisibility(View.GONE);
                    //  len_after.setVisibility(View.GONE);
                    edt_addeddate.setVisibility(View.GONE);
                    ln_bet_addedby.setVisibility(View.GONE);
                    // SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Start_time + "' AS DATE ) ) ";
                } else if (Month.equalsIgnoreCase("Before")) {
                    //  len_before.setVisibility(View.VISIBLE);
                    //  len_after.setVisibility(View.VISIBLE);
                    edt_addeddate.setVisibility(View.VISIBLE);
                    ln_bet_addedby.setVisibility(View.GONE);

                    // SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + Before_date + "' AS DATE ) ) ";
                } else if (Month.equalsIgnoreCase("After")) {
                    //  len_before.setVisibility(View.VISIBLE);
                    //  len_after.setVisibility(View.VISIBLE);
                    edt_addeddate.setVisibility(View.VISIBLE);
                    ln_bet_addedby.setVisibility(View.GONE);

                    //SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Before_date + "' AS DATE ) ) ";
                } else if (Month.equalsIgnoreCase("Between")) {
                    //  SelCriteria += " AND ( CAST(AddedDt AS DATE) BETWEEN CAST('" + Before_date + "' AS DATE ) AND CAST('" + After_date + "' AS DATE ) ) ";
                    //  len_after.setVisibility(View.VISIBLE);
                    // len_before.setVisibility(View.VISIBLE);
                    ln_bet_addedby.setVisibility(View.VISIBLE);
                    edt_addeddate.setVisibility(View.GONE);

                }

            }
        });

    }

    public boolean validate_firm() {
        // TODO Auto-generated method stub

        if ((edtfirm.getText().toString().equalsIgnoreCase("") ||
                edtfirm.getText().toString().equalsIgnoreCase(" ") ||
                edtfirm.getText().toString().equalsIgnoreCase(null)) ||
                edtfirm.getText().toString().length() < 1) {

            //  Toast.makeText(context, "Enter Firm name", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_firmAlias() {
        // TODO Auto-generated method stub

        if ((edtalias.getText().toString().equalsIgnoreCase("") ||
                edtalias.getText().toString().equalsIgnoreCase(" ") ||
                edtalias.getText().toString().equalsIgnoreCase(null)) ||
                edtalias.getText().toString().length() < 1) {

            //  Toast.makeText(context, "Enter Firm name", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_mobile_no() {
        // TODO Auto-generated method stub

        if (edtmobile_no.getText().toString().equalsIgnoreCase("") ||
                edtmobile_no.getText().toString().equalsIgnoreCase(" ") ||
                edtmobile_no.getText().toString().equalsIgnoreCase(null) /*|| edtmobile_no.getText().toString().trim().length() != 10*/) {

            Toast.makeText(ProspectFilterActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }

    /*public boolean validate_product() {
        // TODO Auto-generated method stub

        if ((edtProduct.getText().toString().equalsIgnoreCase("") ||
                edtProduct.getText().toString().equalsIgnoreCase(" ") ||
                edtProduct.getText().toString().equalsIgnoreCase(null))
                ) {
            //Toast.makeText(context, "Select product", Toast.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }*/

    private void setListener() {


        buttonAddProspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // getprospect();


                if (EnvMasterId.equalsIgnoreCase("emami") || EnvMasterId.equalsIgnoreCase("dabar")
                        || EnvMasterId.equalsIgnoreCase("dabur") || EnvMasterId.equalsIgnoreCase("unireal") ||
                        EnvMasterId.equalsIgnoreCase("pragati") || EnvMasterId.equalsIgnoreCase("Pragati") || EnvMasterId.equalsIgnoreCase("b207")) {
                    Intent intent = new Intent(context, IndividualProspectusActivity.class);
                    intent.putExtra("keymode", "AddNew");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                } else if (EnvMasterId.equalsIgnoreCase("ssidf")) {    //if b207 appenv
                    Intent intent1 = new Intent(context, BusinessProspectusActivity.class);
                    intent1.putExtra("keymode", "AddNew");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                } else {

                    SharedPreferences spa = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                    prospectType = spa.getString(WebUrlClass.Key_Default_Prospect, "");
                    Intent intent = new Intent(ProspectFilterActivity.this, ProspectSelectionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                }

                   /* if(prospectType.equals("")){
                        if (isnet()) {
                            showProgressDialog();
                            new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadDefaultProspect().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    dismissProgressDialog();
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                                            Context.MODE_PRIVATE);
                                    String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                                    //      CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                                    callProspectSelection();
                                }
                            });
                        } else {
                            // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                            SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                                    Context.MODE_PRIVATE);
                            String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                            //  CallDefaultProspectActivity(getApplicationContext(), defaultprospect);

                            callProspectSelection();
                        }
                    }else{
                        callProspectSelection();
                    }*/


            }
        });


        buttonShowProspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressDialog();

                SelCriteria = "";

                String city = Cityid;

                if (checkBoxfirmname.isChecked()) {
                    if (validate_firm()) {
                        SelCriteria += "AND FirmName like '%" + edtfirm.getText().toString() + "%'";
                    }
                }

                if (checkBoxfirmalias.isChecked()) {
                    if (validate_firmAlias()) {
                        SelCriteria += "AND FirmAlias like '%" + edtalias.getText().toString() + "%'";
                    }
                }

                if (checkBoxProduct.isChecked()) {

                    SelCriteria += "AND ItemDesc like '%" + spinner_product.getText().toString().trim() + "%'";

                }

                //contactname

                if (checkBoxcontact_name.isChecked()) {

                    SelCriteria += "AND ContactName like '%" + edtcontact_name.getText().toString() + "%'";
                }

                if (checkBoxmobile_no.isChecked()) {
                    if (validate_mobile_no()) {
                        SelCriteria += "AND Mobile like '%" + edtmobile_no.getText().toString() + "%'";
                    }
                }


                //territory

                if (checkBoxterritory.isChecked()) {
                    if (edt_territory.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select territory", Toast.LENGTH_SHORT).show();
                    } else {
                        // AND FKTerritoryId = '" + fname + "'
                        SelCriteria += "and FKTerritoryId = '" + territoryId + "'";
                    }
                }
                //source

                if (checkBoxsource.isChecked()) {
                    if (edtsource.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select source", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND FkEnqSourceId = '" + fname + "'
                        SelCriteria += "AND FkEnqSourceId = '" + sourceId + "'";
                    }
                }


                //country

                if (checkBoxcountry.isChecked()) {
                    if (edt_country.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select country", Toast.LENGTH_SHORT).show();
                    } else {
                        // and FKCountryid = '" + fname + "'
                        SelCriteria += "and FKCountryid = '" + countryid + "'";
                    }
                }

                //state
                if (checkBoxstate.isChecked()) {
                    if (edt_state.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND FKStateId = '" + fname + "'
                        SelCriteria += "and FKStateId = '" + stateId + "'";
                    }
                }


                //city
                if (checkBoxcity.isChecked()) {
                    if (eAutoCity.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select state", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND FKCityId IN(" + fname + ")
                        //    SelCriteria += "and FKCityId IN (" + cityId + ")";
                        SelCriteria += "AND FKCityId = '" + cityId + "'";
                        //AND FKCityId IN(" + fname + ")

                        // AND FKStateId = '" + fname + "'


                    }
                }

                //added by

                if (checkBox_added_by.isChecked()) {
                    SelCriteria += " AND AddedBy = '" + Added_id + "'";
//AND AddedBy = '" + fname + "'
                }
//re
               /* if (checkBoxAddedPeriod.isChecked()) {
                    if (Month.equalsIgnoreCase("Last Month")) {
                    //    len_before.setVisibility(View.GONE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + addeddate + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Before")) {
                     //   len_before.setVisibility(View.VISIBLE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + addeddate + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("After")) {
                      //  len_before.setVisibility(View.VISIBLE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Before_date + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Between")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) BETWEEN CAST('" + Before_date + "' AS DATE ) AND CAST('" + After_date + "' AS DATE ) ) ";

                    }
                }*/

                //address contains

                if (checkBoxAddressContains.isChecked()) {
                    // AND Address like '%" + fname + "%'
                    //"AND ContactName like '%" + edtcontact_name.getText().toString() + "%'"
                    //SelCriteria += " AND Address like %'" + fname +'" %";
                    SelCriteria += "AND Address like '%" + edt_AddressContains.getText().toString().trim() + "%'";
                }
                //Sales Family

                if (checkBoxSalesFamily.isChecked()) {
                    if (edt_SalesFamily.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select sales family", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND FKCityId IN(" + fname + ")
                        //AND FamilyDesc like '%" + fname + "%'
                        SelCriteria += "AND FamilyDesc like '%" + saleFamilyName + "%'";
                        // SelCriteria += "AND FamilyDesc like '" + salesId + "'";
                    }
                }

                //Reference

                if (checkBoxReference.isChecked()) {
                    //AND LeadGivenBYId = '" + fname + "'

                    if (edt_Reference.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select reference", Toast.LENGTH_SHORT).show();
                    } else {
                        SelCriteria += "AND LeadGivenBYId = '" + reference2Id + "'";
                    }

                }


                //Business Segment
                if (checkBoxBusinesssegment.isChecked()) {
                    if (edt_Businesssegment.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select business segment", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND FKCityId IN(" + fname + ")
                        //  AND FKBusiSegmentId ='" + fname + "'
                        //   SelCriteria += "AND FKBusiSegmentId = " + businessId + "";
                        SelCriteria += " AND FKBusiSegmentId = '" + businessId + "'";
                    }
                }


                // View customers only
                // AND CustVendorName like '%customer%'

                if (checkBoxViewcustomersonly.isChecked()) {
                    SelCriteria += "AND CustVendorName like '%customer%'";
                }

                //View Prospects with open calls only
                if (checkBoxopencallsonly.isChecked()) {
                    //      AND OpenCalls <> '0' and CloseCalls='0'
                    SelCriteria += "AND OpenCalls <> '0' and CloseCalls='0'";
                }


                //View Prospects with closed calls only
                if (checkBoxclosedcallsonly.isChecked()) {
                    //      AND  CloseCalls<>'0' AND OpenCalls='0'
                    SelCriteria += "AND  CloseCalls<>'0' AND OpenCalls='0'";
                }

                //View prospects with no calls
                if (checkBoxwithnocalls.isChecked()) {
                    //     AND OpenCalls='0' and CloseCalls='0'
                    SelCriteria += "AND OpenCalls='0' and CloseCalls='0'";
                }

                //View new prospects only
                if (checkBoxnewprospectsonly.isChecked()) {
                    //     AND Addeddt>GETDATE()-2
                    SelCriteria += "AND Addeddt>GETDATE()-2";
                }

                //    SelCriteria += "and FKStateId = '0";
                if (checkBoxViewResellersOnly.isChecked()) {
                    if (viewResellerName.equals("Select")) {
                        //AND IsReseller IN(2,3,4,5)
                        SelCriteria += "AND IsReseller IN(2,3,4,5)";
                    } else {
                        //AND IsReseller='" + fname + "'
                        if (viewResellerName.equals("Vendor")) {
                            SelCriteria += "AND IsReseller = '" + viewResellerCode + "'";
                        } else if (viewResellerName.equals("Retail Agency")) {
                            SelCriteria += "AND IsReseller = '" + viewResellerCode + "'";
                        } else if (viewResellerName.equals("Corporate Agency")) {
                            SelCriteria += "AND IsReseller = '" + viewResellerCode + "'";
                        } else if (viewResellerName.equals("Retails Dealer")) {
                            SelCriteria += "AND IsReseller = '" + viewResellerCode + "'";
                        }
                    }
                }


                if (checkBoxSurveyRating.isChecked()) {
                    if (edtSurveyRating.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please enter survey rating details", Toast.LENGTH_SHORT).show();
                    } else {
                        //AND LastCSSRating = '" + fname + "'
                        SelCriteria += "AND LastCSSRating = '" + edtSurveyRating.getText().toString() + "'";
                    }
                }

                if (checkBoxNotSurveyRating.isChecked()) {
                    if (edtSurveyNotRating.getText().toString() == null) {
                        Toast.makeText(ProspectFilterActivity.this, "Please select date", Toast.LENGTH_SHORT).show();
                    } else {
                        //  AND LastCSSDate >= '" + strtdt + "'
                        SelCriteria += "AND LastCSSDate >= '" + notSurveyafter + "'";
                    }
                }

                if (checkBoxNotFuturPlanDate.isChecked()) {
                    String name = spinner_FuturePlan.getText().toString();
                    if (name.equals("Between")) {
//AND FuturePlanDate  Between '" + strtdt + "' AND '" + Enddt + "'

                        SelCriteria += "AND FuturePlanDate  Between '" + fromDt_Future + "' AND '" + toDt_Future + "'";
                    } else if (name.equals("After")) {
//AND LastSODate >= '" + strtdt + "'
                        SelCriteria += "AND FuturePlanDate >= '" + futurePlanDate + "'";
                    } else if (name.equals("Before")) {
//AND LastSODate <= '" + strtdt + "'
                        SelCriteria += "AND FuturePlanDate <= '" + futurePlanDate + "'";
                    } else {
                        Toast.makeText(ProspectFilterActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                if (checkBoxLastSODate.isChecked()) {
                    String name = spinner_LastSODate.getText().toString();
                    if (name.equals("Between")) {
                        // AND LastSODate Between '" + strtdt + "' AND '" + Enddt + "'
                        SelCriteria += "AND LastSODate  Between '" + fromDt_SODate + "' AND '" + toDt_SODate + "'";
                    } else if (name.equals("After")) {
// AND LastSODate >= '" + strtdt + "'
                        SelCriteria += "AND LastSODate >= '" + lastSODate + "'";
                    } else if (name.equals("Before")) {
// AND LastSODate <= '" + strtdt + "'
                        SelCriteria += "AND LastSODate <= '" + lastSODate + "'";
                    } else {
                        Toast.makeText(ProspectFilterActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                if (checkBOXLastSOScheduleDate.isChecked()) {
                    String name = spinner_LastSOScheduleDate.getText().toString();

                  /*  AND LastSOScheduleDate Between '" + strtdt + "' AND '" + Enddt + "'
                    AND LastSOScheduleDate >= '" + strtdt + "'
                    AND LastSOScheduleDate <= '" + strtdt + "'*/

                    if (name.equals("Between")) {
                        //   AND LastSOScheduleDate Between '" + strtdt + "' AND '" + Enddt + "'
                        SelCriteria += "AND LastSOScheduleDate  Between '" + fromDt_SOSCDate + "' AND '" + toDt_SOSCDate + "'";
                    } else if (name.equals("After")) {
//  AND LastSOScheduleDate >= '" + strtdt + "'
                        SelCriteria += "AND LastSOScheduleDate >= '" + lastSOScheduleDate + "'";
                    } else if (name.equals("Before")) {
// AND LastSOScheduleDate <= '" + strtdt + "'
                        SelCriteria += "AND LastSOScheduleDate <= '" + lastSOScheduleDate + "'";
                    } else {
                        Toast.makeText(ProspectFilterActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                if (checkBOXLastShipmentDate.isChecked()) {
                    String name = spinner_LastShipmentDate.getText().toString();

                    if (name.equals("Between")) {
                        //   AND LastShipmentDate Between '" + strtdt + "' AND '" + Enddt + "'
                        SelCriteria += "AND LastShipmentDate  Between '" + fromDt_Shipment + "' AND '" + toDt_Shipment + "'";
                    } else if (name.equals("After")) {
//     AND LastShipmentDate >= '" + strtdt + "'
                        SelCriteria += "AND LastShipmentDate >= '" + lastShipmentDate + "'";
                    } else if (name.equals("Before")) {
//   AND LastShipmentDate <= '" + strtdt + "'
                        SelCriteria += "AND LastShipmentDate <= '" + lastShipmentDate + "'";
                    } else {
                        Toast.makeText(ProspectFilterActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }

                if (checkBoxAddedPeriod.isChecked()) {
                    if (Month.equalsIgnoreCase("Last Month")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + Start_time + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Before")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + addeddate + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("After")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + addeddate + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Between")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) BETWEEN CAST('" + fromDt_addedby + "' AS DATE ) AND CAST('" + toDt_addedby + "' AS DATE ) ) ";

                    }
                }


                if (SelCriteria != "") {
                    if (isnet()) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadFilterJSON().execute(SelCriteria);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }
                        });

                    } else {
                        dismissProgressDialog();
                        Toast.makeText(ProspectFilterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dismissProgressDialog();
                    Toast.makeText(getApplicationContext(), "Select Filter First..,", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBoxfirmname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_firm_name.setVisibility(View.VISIBLE);
                    edtfirm.setVisibility(View.VISIBLE);
                } else {
                    ln_firm_name.setVisibility(View.GONE);
                    edtfirm.setVisibility(View.GONE);
                }
            }
        });


        checkBoxfirmalias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_firm_alias.setVisibility(View.VISIBLE);
                    edtalias.setVisibility(View.VISIBLE);
                } else {
                    ln_firm_alias.setVisibility(View.GONE);
                    edtalias.setVisibility(View.GONE);
                }
            }
        });


        checkBoxProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    ln_product.setVisibility(View.VISIBLE);
                    spinner_product.setVisibility(View.VISIBLE);
                } else {
                    ln_product.setVisibility(View.GONE);
                    spinner_product.setVisibility(View.GONE);
                }
            }
        });

        checkBoxcontact_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_contact_name.setVisibility(View.VISIBLE);
                    edtcontact_name.setVisibility(View.VISIBLE);
                } else {
                    edtcontact_name.setVisibility(View.GONE);
                    ln_contact_name.setVisibility(View.GONE);
                }
            }
        });


        checkBoxSurveyRating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_SurveyRating.setVisibility(View.VISIBLE);
                    edtSurveyRating.setVisibility(View.VISIBLE);
                } else {
                    ln_SurveyRating.setVisibility(View.GONE);
                    edtSurveyRating.setVisibility(View.GONE);
                }
            }
        });

        checkBoxNotSurveyRating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // ln_SurveyNotRating.setVisibility(View.VISIBLE);
                    edtSurveyNotRating.setVisibility(View.VISIBLE);
                } else {
                    //ln_SurveyNotRating.setVisibility(View.GONE);
                    edtSurveyNotRating.setVisibility(View.GONE);
                }
            }
        });
        checkBoxNotFuturPlanDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    ln_futurePlanSpinner.setVisibility(View.VISIBLE);
                    edtFuturePlanDate.setVisibility(View.VISIBLE);
                    // ln_futurePlandt.setVisibility(View.VISIBLE);
                    //img_futurePlan.setVisibility(View.VISIBLE);
                } else {
                    ln_futurePlanSpinner.setVisibility(View.GONE);
                    edtFuturePlanDate.setVisibility(View.GONE);
                    // img_futurePlan.setVisibility(View.GONE);
                }
            }
        });
        checkBoxLastSODate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_lastSOSpinner.setVisibility(View.VISIBLE);
                    edtLastSODate.setVisibility(View.VISIBLE);
                    // img_SoDate.setVisibility(View.VISIBLE);
                } else {
                    ln_lastSOSpinner.setVisibility(View.GONE);
                    edtLastSODate.setVisibility(View.GONE);
                    //img_SoDate.setVisibility(View.GONE);
                }
            }
        });

        checkBOXLastSOScheduleDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_lastSOScSpinner.setVisibility(View.VISIBLE);
                    edtLastSOScheduleDate.setVisibility(View.VISIBLE);
                    // img_SoScheduleDate.setVisibility(View.VISIBLE);
                } else {
                    ln_lastSOScSpinner.setVisibility(View.GONE);
                    edtLastSOScheduleDate.setVisibility(View.GONE);
                    // img_SoScheduleDate.setVisibility(View.GONE);
                }
            }
        });

        checkBOXLastShipmentDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_lastshipDtSpinner.setVisibility(View.VISIBLE);
                    edt_LastShipmentDate.setVisibility(View.VISIBLE);
                    //img_LastShipmentDate.setVisibility(View.VISIBLE);
                } else {
                    ln_lastshipDtSpinner.setVisibility(View.GONE);
                    edt_LastShipmentDate.setVisibility(View.GONE);
                    //  img_LastShipmentDate.setVisibility(View.GONE);
                }
            }
        });


        checkBoxmobile_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtmobile_no.setVisibility(View.VISIBLE);
                    ln_mobile_no.setVisibility(View.VISIBLE);
                } else {
                    edtmobile_no.setVisibility(View.GONE);
                    ln_mobile_no.setVisibility(View.GONE);
                }
            }
        });

        checkBoxcity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    eAutoCity.setVisibility(View.VISIBLE);
                    ln_city.setVisibility(View.VISIBLE);
                } else {
                    eAutoCity.setVisibility(View.GONE);
                    ln_city.setVisibility(View.GONE);
                }
            }
        });

        checkBoxterritory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_territory.setVisibility(View.VISIBLE);
                    edt_territory.setVisibility(View.VISIBLE);
                } else {
                    ln_territory.setVisibility(View.GONE);
                    edt_territory.setVisibility(View.GONE);
                }
            }
        });


        checkBoxcountry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_country.setVisibility(View.VISIBLE);
                    edt_country.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(ProspectFilterActivity.this,
                            FilterListActivity.class);
                    isCountry = true;
                    isState = false;
                    isCity = false;
                    isTerritory = false;
                    isBusinessSegment = false;
                    isSalesFamily = false;
                    isSource = false;
                    //isSelProduct = false;

                    String url = CompanyURL + WebUrlClass.api_getCountry;
                    intent.putExtra("Table_Name", db.TABLE_COUNTRY);
                    intent.putExtra("Id", "PKCountryId");
                    intent.putExtra("DispName", "CountryName");
                    intent.putExtra("Type","Country");
                    //  intent.putExtra("WHClauseParameter", "");
                    //intent.putExtra("WHClauseParamVal","");
                    // intent.putExtra("APIName", url);
                    //intent.putExtra("APIParameters","");
                    //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                    startActivityForResult(intent, COUNTRY);
                } else {
                    ln_country.setVisibility(View.GONE);
                    edt_country.setVisibility(View.GONE);
                }
            }
        });

        checkBoxsource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_source.setVisibility(View.VISIBLE);
                    edtsource.setVisibility(View.VISIBLE);
                } else {
                    ln_source.setVisibility(View.GONE);
                    edtsource.setVisibility(View.GONE);
                }
            }
        });

        checkBoxstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_state.setVisibility(View.VISIBLE);
                    edt_state.setVisibility(View.VISIBLE);
                } else {
                    ln_state.setVisibility(View.GONE);
                    edt_state.setVisibility(View.GONE);
                }
            }
        });
        checkBox_added_by.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_added_by.setVisibility(View.VISIBLE);
                    spinner_added_by.setVisibility(View.VISIBLE);

                } else {
                    ln_added_by.setVisibility(View.GONE);
                    spinner_added_by.setVisibility(View.GONE);
                    // edt_addeddate.setVisibility(View.GONE);
                }
            }
        });


        checkBoxViewResellersOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_viewReseller.setVisibility(View.VISIBLE);
                } else {
                    ln_viewReseller.setVisibility(View.GONE);
                }
            }
        });
        /*checkBoxnewprospectsonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelCriteria = " AND Addeddt>GETDATE()-2";
                System.out.println("Newcalls :" + newcalls);
            }
        });

        checkBoxopencallsonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelCriteria = "AND OpenCalls <> '0'AND Closecalls=0";

            }
        });
        checkBoxclosedcallsonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelCriteria = "AND  CloseCalls<>'0'AND OpenCalls=0";

            }
        });

        checkBoxwithnocalls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SelCriteria = " AND OpenCalls='0' and CloseCalls='0'";
            }
        });
*/
        checkBoxAddedPeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    len_added_date.setVisibility(View.VISIBLE);
                    spinner_added_date.setVisibility(View.VISIBLE);
                    //edt_addeddate.setVisibility(View.VISIBLE);
                } else {
                    len_added_date.setVisibility(View.GONE);
                    spinner_added_date.setVisibility(View.GONE);
                    //  edt_addeddate.setVisibility(View.GONE);
                }
            }
        });
        checkBoxAddressContains.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_AddressContains.setVisibility(View.VISIBLE);
                    edt_AddressContains.setVisibility(View.VISIBLE);
                } else {
                    ln_AddressContains.setVisibility(View.GONE);
                    edt_AddressContains.setVisibility(View.GONE);
                }
            }
        });
        checkBoxSalesFamily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_SalesFamily.setVisibility(View.VISIBLE);
                    edt_SalesFamily.setVisibility(View.VISIBLE);
                } else {
                    ln_SalesFamily.setVisibility(View.GONE);
                    edt_SalesFamily.setVisibility(View.GONE);
                }
            }
        });
        checkBoxReference.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_Reference.setVisibility(View.VISIBLE);
                    edt_Reference.setVisibility(View.VISIBLE);

                } else {
                    ln_Reference.setVisibility(View.GONE);
                    edt_Reference.setVisibility(View.GONE);
                    ln_Reference2.setVisibility(View.GONE);
                }
            }
        });

        checkBoxBusinesssegment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ln_Businesssegment.setVisibility(View.VISIBLE);
                    edt_Businesssegment.setVisibility(View.VISIBLE);
                } else {
                    ln_Businesssegment.setVisibility(View.GONE);
                    edt_Businesssegment.setVisibility(View.GONE);
                }
            }
        });


        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct ItemDesc,ItemMasterId" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemDesc='" + spinner_product.getText().toString().trim() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        SelCriteria = cur.getString(cur.getColumnIndex("ItemDesc"));

                    } while (cur.moveToNext());

                } else {
                    SelCriteria = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        eAutoCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cf.check_City() > 0) {
                    setautocomplete_city();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

     /*   edt_country.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
             //   ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return false;
            }
        });*/


        edt_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        edt_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this,
                        FilterListActivity.class);
                isCountry = false;
                isState = true;
                isCity = false;
                isTerritory = false;
                isBusinessSegment = false;
                isSalesFamily = false;
                isSource = false;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_STATE);
                intent.putExtra("Id", "PKStateId");
                intent.putExtra("DispName", "StateDesc");
                intent.putExtra("Type","State");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });


     /*   edt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (countryArrayList.size() > 0) {


                    String string_country = edt_country.getText().toString().trim();

                    try {
                        countryid = getPosition_Countryfromspin(countryArrayList, string_country);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

    /*    edt_state.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stateArrayList.size() > 0) {
                    String stateName = edt_state.getText().toString();


                    try {
                        stateId = getPosition_Statefromspin(stateArrayList, stateName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

    /*    eAutoCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        eAutoCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cityArrayList.size() > 0) {
                    cityName = eAutoCity.getText().toString();


                    try {
                        cityId = getPosition_Cityfromspin(cityArrayList, cityName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

        eAutoCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this,
                        FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = true;
                isTerritory = false;
                isBusinessSegment = false;
                isSalesFamily = false;
                isSource = false;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_CITY);
                intent.putExtra("Id", "PKCityID");
                intent.putExtra("DispName", "CityName");
                intent.putExtra("Type","City");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        edt_territory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this, FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = true;
                isBusinessSegment = false;
                isSalesFamily = false;
                isSource = false;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_Teritory);
                intent.putExtra("Id", "PKTerritoryId");
                intent.putExtra("DispName", "TerritoryName");
                intent.putExtra("Type","Territory");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        spinner_added_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this, FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusinessSegment = false;
                isSalesFamily = false;
                isSource = false;
                isAddedBy = true;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_AddBy);
                intent.putExtra("Id", "UserLoginId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("Type","Added By");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

    /*    edt_territory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edt_territory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (teritorybeanArrayList.size() > 0) {
                    String territoryName = edt_territory.getText().toString();


                    try {
                        territoryId = getPosition_Terrifromspin(teritorybeanArrayList, territoryName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });*/

       /* edtsource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edtsource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (prospectsourceBeanArrayList.size() != 0) {
                    String sourceName = edtsource.getText().toString();


                    try {
                        sourceId = getPosition_Sourcefromspin(prospectsourceBeanArrayList, sourceName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });*/

        edtsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this,
                        FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusinessSegment = false;
                isSalesFamily = false;
                isSource = true;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_Prospectsource);
                intent.putExtra("Id", "PKSuspSourceId");
                intent.putExtra("DispName", "SourceName");
                intent.putExtra("Type","Source");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

        edt_SalesFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this,
                        FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusinessSegment = false;
                isSalesFamily = true;
                isSource = false;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_SALES_FAMILY_PRODUCT);
                intent.putExtra("Id", "FamilyId");
                intent.putExtra("DispName", "FamilyDesc");
                intent.putExtra("Type","Family");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

       /* edt_SalesFamily.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edt_SalesFamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // saleFamilyArrayList

                if (saleFamilyArrayList.size() != 0) {
                    saleFamilyName = edt_SalesFamily.getText().toString();


                    try {
                        salesId = getPosition_Salesfromspin(saleFamilyArrayList, saleFamilyName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/

        edt_Reference.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edt_Reference.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // saleFamilyArrayList

                if (referenceBeanArrayList.size() != 0) {
                    referenceName = edt_Reference.getText().toString();


                    try {
                        referenceId = getPosition_Referencefromspin(referenceBeanArrayList, referenceName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (referenceId != "") {
                        showProgressDialog();
                        if (ut.isNet(ProspectFilterActivity.this)) {
                            new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    ///api/SuspectMasterAPI/GetFillLeadwiseCustomer?LeadWise=C

                                    new LeadWiseCustomer().execute(referenceId);
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    dismissProgressDialog();
                                }
                            });
                        }
                    } else {
                        ln_Reference2.setVisibility(View.GONE);
                        dismissProgressDialog();
                    }
                }
            }
        });

        edt_Reference2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });

        edt_Reference2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // saleFamilyArrayList

                if (leadWiseBeanArrayList.size() != 0) {
                    reference2Name = edt_Reference2.getText().toString();


                    try {
                        reference2Id = getPosition_Reference2fromspin(leadWiseBeanArrayList, reference2Name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        /*edt_Businesssegment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AutoCompleteTextView) v).showDropDown();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
        });


        edt_Businesssegment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // saleFamilyArrayList

                if (businessSegmentbeanArrayList.size() != 0) {
                    String businId = edt_Businesssegment.getText().toString();


                    try {
                        businessId = getPosition_Businesssfromspin(businessSegmentbeanArrayList, businId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/


        edt_Businesssegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProspectFilterActivity.this, FilterListActivity.class);
                isCountry = false;
                isState = false;
                isCity = false;
                isTerritory = false;
                isBusinessSegment = true;
                isSalesFamily = false;
                isSource = false;
                //isSelProduct = false;

                // String url = CompanyURL + WebUrlClass.api_getCountry;
                intent.putExtra("Table_Name", db.TABLE_Business_segment);
                intent.putExtra("Id", "PKBusiSegmentID");
                intent.putExtra("DispName", "SegmentDescription");
                intent.putExtra("Type","Bussiness Segment");
                //intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                // intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Country> mList = new ArrayList<>()");
                startActivityForResult(intent, COUNTRY);
            }
        });

    }

    private void getprospect() {
        if(prospectType.equals("")){
            if (isnet()) {
                showProgressDialog();
                new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDefaultProspect().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                                Context.MODE_PRIVATE);
                        String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                        //      CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                        callProspectSelection();
                    }
                });
            } else {
                // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                        Context.MODE_PRIVATE);
                String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");

                callProspectSelection();
            }
        }else{
            callProspectSelection();
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

    private String getPosition_Statefromspin(ArrayList<State> lst_state,
                                             String stateName)
            throws JSONException {
        String stateId = "";
        for (State stateBean : lst_state) {
            if (stateBean.getStateDesc().equalsIgnoreCase(stateName)) {
                stateId = stateBean.getPKStateId();
            }
        }
        return stateId; //it wasn't found at all
    }

    private String getPosition_Cityfromspin(ArrayList<CityMaster> lst_city,
                                            String cityName)
            throws JSONException {
        String cityId = "";
        for (CityMaster cityMaster : lst_city) {
            if (cityMaster.getCityName().equalsIgnoreCase(cityName)) {
                cityId = cityMaster.getPKCityID();
            }
        }
        return cityId; //it wasn't found at all
    }

    private String getPosition_Terrifromspin(ArrayList<Teritorybean> lst_terri,
                                             String terriName)
            throws JSONException {
        String terriId = "";
        for (Teritorybean teritorybean : lst_terri) {
            if (teritorybean.getTerritoryName().equalsIgnoreCase(terriName)) {
                terriId = teritorybean.getPKTerritoryId();
                break;
            }
        }
        return terriId; //it wasn't found at all
    }

    private String getPosition_Sourcefromspin(ArrayList<ProspectsourceBean> lst_product, String productName1)
            throws JSONException {
        String sourceId1 = "";
        for (ProspectsourceBean productBean : lst_product) {
            if (productBean.getSourceName().equalsIgnoreCase(productName1)) {
                sourceId1 = productBean.getPKSuspSourceId();
            }
        }
        return sourceId1; //it wasn't found at all
    }

    private String getPosition_Salesfromspin(ArrayList<ProductBean> lst_product, String salesName1)
            throws JSONException {
        String salesId = "";
        for (ProductBean productBean : lst_product) {
            if (productBean.getFamilyDesc().equalsIgnoreCase(salesName1)) {
                salesId = productBean.getFamilyId();
            }
        }
        return salesId; //it wasn't found at all
    }

    private String getPosition_Referencefromspin(ArrayList<ReferenceBean> lst_product, String salesName1)
            throws JSONException {
        String salesId = "";
        for (ReferenceBean referenceBean : lst_product) {
            if (referenceBean.getCustVendorCode().equalsIgnoreCase(salesName1)) {
                salesId = referenceBean.getCustVendor();
                break;
            }
        }
        return salesId; //it wasn't found at all
    }

    private String getPosition_Reference2fromspin(ArrayList<LeadWiseBean> lst_product, String salesName1)
            throws JSONException {
        String salesId = "";
        for (LeadWiseBean leadWiseBean : lst_product) {
            if (leadWiseBean.getCustVendorName().equalsIgnoreCase(salesName1)) {
                salesId = leadWiseBean.getCustVendorMasterId();
                break;
            }
        }
        return salesId; //it wasn't found at all
    }

    private String getPosition_Businesssfromspin(ArrayList<BusinessSegmentbean> lst_product, String salesName1)
            throws JSONException {
        String businessId = "";
        for (BusinessSegmentbean productBean : lst_product) {
            if (productBean.getSegmentDescription().equalsIgnoreCase(salesName1)) {
                businessId = productBean.getPKBusiSegmentID();
            }
        }
        return businessId; //it wasn't found at all
    }


    private void showProgressDialog() {


        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {


        progressbar.setVisibility(View.GONE);

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

    class DownloadFilterJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String resp = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Prospect_filter + "?SelCriteria=" + URLEncoder.encode(params[0], "utf-8");
                res = ut.OpenConnection(url, ProspectFilterActivity.this);
                response = res.toString();
                response = response.substring(1, response.length() - 1);

                response = "{\"Activity\":\"" + response + "\n" + "\"}";

               /*url = url.replaceAll(" ","%20");
                url = url.replaceAll("'","%27");*/
                /*res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }*/
            } catch (NullPointerException e) {
                e.printStackTrace();

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
            if (integer.contains("PKSuspectId")) {
                try {
                    JSONArray jResults = null;
                    JSONObject obj = new JSONObject(response);

                    String Msgcontent = obj.getString("Activity");
                    jResults = new JSONArray(Msgcontent);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_filterdata_prospect, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_filterdata_prospect, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_filterdata_prospect, null, values);
                        Log.e("log data", "" + a);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    resp = "error";
                }
                if (resp.equals("error")) {
                    Toast.makeText(ProspectFilterActivity.this, "Please Contact admin person for the issue", Toast.LENGTH_SHORT).show();


                } else {
                    Intent intent = new Intent(ProspectFilterActivity.this, FilteredProspectListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                }

                //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            } else if (integer.contains("Search is returning so many records")) {
                Toast toast = Toast.makeText(getApplicationContext(), "Search is returning so many records..! Please refine your search..!", Toast.LENGTH_SHORT);
                toast.show();

            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "No Record Found...", Toast.LENGTH_SHORT);
                toast.show();
            }


        }

    }

    class DownloadProspectID extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_get_Prospect_ID;
                System.out.println("FilterCall" + url);

               /* url = url.replaceAll(" ","%20");
                url = url.replaceAll("'","%27");*/
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";

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
            if (integer.contains("PKProspectHdrID")) {
                try {
                    JSONObject obj = new JSONObject(response);
                    // JSONObject objLocation = obj.getJSONObject("location");
                    Log.e("RESPONSE"," CRM --> "+obj);
                    JSONArray jResults = obj.getJSONArray(obj.toString());
                    Log.e("RESPONSE"," CRM  jResults--> "+jResults);

                    JSONObject obj0 = jResults.getJSONObject(0);//Enterprise ProspectSelectionActivity
                    JSONObject obj1 = jResults.getJSONObject(1);//Small Business
                    JSONObject obj2 = jResults.getJSONObject(2);//Individual ProspectSelectionActivity


                    //JSONArray jResults = new JSONArray(response);
                    /*JSONObject obj0 = jResults.getJSONObject(0);//Enterprise ProspectSelectionActivity
                    JSONObject obj1 = jResults.getJSONObject(1);//Small Business
                    JSONObject obj2 = jResults.getJSONObject(2);//Individual ProspectSelectionActivity*/
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString(WebUrlClass.Key_Enterprise, obj0.getString("PKProspectHdrID"));
                    editor.putString(WebUrlClass.Key_Business, obj1.getString("PKProspectHdrID"));
                    editor.putString(WebUrlClass.Key_indivisual, obj2.getString("PKProspectHdrID"));
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.Key_Enterprise, "");
                editor.putString(WebUrlClass.Key_Business, "");
                editor.putString(WebUrlClass.Key_indivisual, "");
                editor.commit();

            }


        }

    }

    class DownloadDefaultProspect extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_get_Default_Prospect;
                System.out.println("FilterCall" + url);
                /* url = url.replaceAll(" ","%20");
                url = url.replaceAll("'","%27");*/
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";

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
            if (integer.contains("ModuleSetupValue")) {
                try {
                    JSONArray jsonArray = new JSONArray(integer);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String DefaultProspect = jsonObject.getString("ModuleSetupValue");
                    Log.e("" + DefaultProspect, "" + DefaultProspect);
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                  /*  SharedPreferences setDefaultVal = getSharedPreferences("Prospecttype",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = setDefaultVal.edit();
                    editor1.putString("ProspectType",DefaultProspect);*/

                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString(WebUrlClass.Key_Default_Prospect, DefaultProspect);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString(WebUrlClass.Key_Default_Prospect, "");
                    editor.commit();
                }


                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
                String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                //    CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                callProspectSelection();
            } else {
                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.Key_Default_Prospect, "");
                editor.commit();


                String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                //     CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                callProspectSelection();
            }


        }

    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
       /* Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "font/BOOKOS.TTF");
*/
        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //   view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //   view.setTypeface(font);
            return view;
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

                        long a = sql.insert(db.TABLE_CITY, null, values);

                    }

                }

            } catch (NullPointerException e) {
                e.printStackTrace();
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

    private void setautocomplete_city() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++) {
            lstCity.add(lstdb.get(i).getCityName());
            lstCity.add(lstdb.get(i).getPKCityID());


            Cityid = lstdb.get(i).getPKCityID();
            System.out.println("Ccccc :" + Cityid);
            MySpinnerAdapter customAdcity = new MySpinnerAdapter(ProspectFilterActivity.this,
                    R.layout.crm_custom_spinner_txt, lstCity);

           /* eAutoCity.setAdapter(customAdcity);

            eAutoCity.setThreshold(3);
            eAutoCity.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());*/
        }
    }
/*    private void getproduct1() {

        List<SalesFamily> lstdb = cf.getSalesFamilyBean();
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
        BusinessProspectusActivity.MySpinnerAdapter customDept = new BusinessProspectusActivity.MySpinnerAdapter(BusinessProspectusActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        int a = lstProduct.indexOf(Product_name);
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        spinner_product.setSelection(Productionitems.indexOf(Product_name));

        if (Mode.equals("E")) {
            int productPos = -1;
            if (Productid != "") {
                for (int i = 0; i < lstdb.size(); i++) {
                    if (Productid.equals(lstdb.get(i).getFamilyId())) {
                        productPos = i;
                        break;
                    }
                }
                if (productPos != -1) {
                    spinner_product.setSelection(productPos);
                } else {
                    spinner_product.setSelection(0);
                }
            }
        }

    }*/


    private void getproduct() {
        saleFamilyArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_SALES_FAMILY_PRODUCT;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                saleFamilyArrayList.add(new ProductBean(cur.getString(cur.getColumnIndex("FamilyId")),
                        cur.getString(cur.getColumnIndex("FamilyDesc"))));


            } while (cur.moveToNext());

        }

        ArrayAdapter<ProductBean> countryArrayAdapter = new ArrayAdapter<ProductBean>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, saleFamilyArrayList);
        edt_SalesFamily.setAdapter(countryArrayAdapter);


        //   int a =  lstProduct.indexOf(Product_name);
        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        //spinner_product.setSelection(Productionitems.indexOf(Product_name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

    }

    class DownloadAddedByJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetFillAddedby;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_AddBy, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_AddBy, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_AddBy, null, values);

                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response != null) {
                getAddedby();
            }


        }

    }

    private void getAddedby() {
        lstadded_by.clear();
        String query = "SELECT distinct UserLoginId,UserName" +
                " FROM " + db.TABLE_AddBy;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstadded_by.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstadded_by, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectFilterActivity.this,
                R.layout.crm_custom_spinner_txt, lstadded_by);
        spinner_added_by.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(lstadded_by, String.CASE_INSENSITIVE_ORDER);
        //   int a =  lstProduct.indexOf(Product_name);
        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        //spinner_product.setSelection(Productionitems.indexOf(Product_name));
    }


    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    public void callProspectSelection() {
        Intent intent = new Intent(ProspectFilterActivity.this, ProspectSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
    }

    public void CallDefaultProspectActivity(Context context, String defaultprospect) {

        if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_enterprise)) {
            Intent intent = new Intent(context, ProspectEnterpriseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("keymode", "AddNew");
            context.startActivity(intent);
        } else if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_business)) {
            Intent intent = new Intent(context, BusinessProspectusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("keymode", "AddNew");
            context.startActivity(intent);
            startActivity(intent);
        } else if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_individual)) {
            Intent intent = new Intent(context, IndividualProspectusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("keymode", "AddNew");
            context.startActivity(intent);
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, ProspectEnterpriseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("keymode", "AddNew");
            context.startActivity(intent);
            startActivity(intent);
        }
    }

    private class DownloadAllDropDown extends AsyncTask<String, Void, String> {
        String res = "", response = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_bindAllDropDowns;


            res = ut.OpenConnection(url);
            try {
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                } else {
                    response = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!response.equals("error")) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(response);
                    /*

                     */
/************************COUNTRY************************************/

                    JSONArray counrtyJSONArray = jsonObjectMain.getJSONArray("Country");

                    Log.e("ADDEDDATE--> "," counrtyJSONArray--> "+counrtyJSONArray);

                    sql.delete(db.TABLE_COUNTRY, null,
                            null);
                    Cursor countryC = sql.rawQuery("SELECT * FROM " + db.TABLE_COUNTRY, null);
                    int countCountry = countryC.getCount();
                    String columnNameCountry, columnValueCountry = "";
                    ContentValues countryvalues = new ContentValues();
                    for (int i = 0; i < counrtyJSONArray.length(); i++) {
                        JSONObject jorder = counrtyJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < countryC.getColumnCount(); j++) {
                                columnNameCountry = countryC.getColumnName(j);
                                String jsonAddeddt = jorder.getString("AddedDt");
                                String jsonModifiedDt = jorder.getString("ModifiedDt");
                                if (columnNameCountry.equalsIgnoreCase("AddedDt")) {
                                    Log.e("ADDEDDATE--> "," --> "+jsonAddeddt);
                                    //if(jsonAddeddt.contains("(")){
                                        jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                                //    }
                                /*else{
                                        jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                                    }*/

                                    long DOB_date = Long.parseLong(jsonAddeddt);
                                    DOBDate = new Date(DOB_date);
                                    jsonAddeddt = sdf.format(DOBDate);
                                    countryvalues.put(columnNameCountry, jsonAddeddt);

                                } else if (columnNameCountry.equalsIgnoreCase("ModifiedDt")) {
                                    jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                            + 1, jsonModifiedDt.lastIndexOf(")"));
                                    long DOB_date = Long.parseLong(jsonModifiedDt);
                                    DOBDate = new Date(DOB_date);
                                    jsonModifiedDt = sdf.format(DOBDate);
                                    countryvalues.put(columnNameCountry, jsonModifiedDt);

                                } else


/*{"PKCountryId":"2a8b16ce-6c86-4321-be87-396a0bff8d96","CountryCode":"Dubai","CountryName":"Dubai",
                                "AddedBy":"400097","AddedDt":"2021-01-28T20:44:32","ModifyBy":"400097",
                                "ModifiedDt":"2021-01-28T20:44:32","IsDeleted":"N","MobileNoDigits":15,"Zone":null}*/

                                    if (columnNameCountry.equalsIgnoreCase("MobileNoDigits") || columnNameCountry.equalsIgnoreCase("Zone")) {

                                    } else {
                                        columnValueCountry = jorder.getString(columnNameCountry);
                                        countryvalues.put(columnNameCountry, columnValueCountry);
                                    }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long a = sql.insert(db.TABLE_COUNTRY, null, countryvalues);
                        Log.i("Country", String.valueOf(a));

                    }


/****************************STATE***************************************/


                    JSONArray stateJSONArray = jsonObjectMain.getJSONArray("State");

                    sql.delete(db.TABLE_STATE, null,
                            null);
                    Cursor stateC = sql.rawQuery("SELECT * FROM " + db.TABLE_STATE, null);
                    int countState = countryC.getCount();
                    String columnNameState, columnValueState = "";
                    ContentValues stateValues = new ContentValues();
                    for (int i = 0; i < stateJSONArray.length(); i++) {
                        JSONObject jorder = stateJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < stateC.getColumnCount(); j++) {
                                columnNameState = stateC.getColumnName(j);
/*{"PKStateId":"f8be385c-b22f-47f2-9424-bd8d95bae8ab","StateNo":"","StateDesc":"ANDHRA PRADESH","FKCountryId":"1",
                             "IsDeleted":"N","AddedBy":"200046","AddedDt":"2013-04-24T11:36:41","ModifiedBy":"200046","ModifiedDt":"2013-04-24T11:36:41",
                             "StateCodeNo":"37","IsUT":"0"}*/


                                if (columnNameState.equalsIgnoreCase("StateCodeNo") || columnNameState.equalsIgnoreCase("IsUT")) {

                                } else {
                                    columnValueState = jorder.getString(columnNameState);
                                    stateValues.put(columnNameState, columnValueState);

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long a1 = sql.insert(db.TABLE_STATE, null, stateValues);
                        Log.i("State", String.valueOf(a1));

                    }


/***********************CITY***********************************/


                    JSONArray cityJSONArray = jsonObjectMain.getJSONArray("City");

                    sql.delete(db.TABLE_CITY, null,
                            null);
                    Cursor cityC = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY, null);
                    int countCity = cityC.getCount();
                    String columnNameCity, columnValueCity = "";
                    ContentValues cityvalues = new ContentValues();
                    for (int i = 0; i < cityJSONArray.length(); i++) {
                        JSONObject jorder = cityJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < cityC.getColumnCount(); j++) {
                                columnNameCity = cityC.getColumnName(j);


                                if (columnNameCity.equalsIgnoreCase("PKCityID") || columnNameCity.equalsIgnoreCase("CityName")) {
                                    columnValueCity = jorder.getString(columnNameCity);
                                    cityvalues.put(columnNameCity, columnValueCity);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long a2 = sql.insert(db.TABLE_CITY, null, cityvalues);
                        Log.i("City", String.valueOf(a2));

                    }


/**********************territory****************************/

                    JSONArray territoryJSONArray = jsonObjectMain.getJSONArray("Territory");

                    sql.delete(db.TABLE_Teritory, null,
                            null);
                    Cursor TerriC = sql.rawQuery("SELECT * FROM " + db.TABLE_Teritory, null);
                    int countTerri = TerriC.getCount();
                    String columnNameTerri, columnValueTerri = "";
                    ContentValues terrivalues = new ContentValues();

                    for (int i = 0; i < territoryJSONArray.length(); i++) {
                        try {
                            JSONObject jorder = territoryJSONArray.getJSONObject(i);
                            for (int j = 0; j < TerriC.getColumnCount(); j++) {
                                columnNameTerri = TerriC.getColumnName(j);
                             /*   "PKTerritoryId":
                                "5b01f81a-f369-4ea8-b3ae-e8a45ba4f6d3", "TerritoryCode":
                                "vita1", "TerritoryName":"vita1", "FKParentTerritoryId":"0",
                                        "AddedBy":"Admin", "AddedDt":
                                "2021-03-11T12:25:45", "ModifiedBy":"Admin", "ModifiedDt":
                                "2021-03-11T12:25:45", "IsDeleted":"N", "ManagerId":"0"*/


                                //     if (columnNameTerri.equalsIgnoreCase("PKCityID") || columnNameTerri.equalsIgnoreCase("CityName"))
                                columnValueTerri = jorder.getString(columnNameTerri);
                                terrivalues.put(columnNameTerri, columnValueTerri);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        long a3 = sql.insert(db.TABLE_Teritory, null, terrivalues);
                        Log.i("Territory", String.valueOf(a3));

                    }


                    /*   *//**********************Business Segment**********************/
                    JSONArray businessSegJSONArray = jsonObjectMain.getJSONArray("BusiSegment");
                    /*     TABLE_Business_segment*/


                    sql.delete(db.TABLE_Business_segment, null,
                            null);
                    Cursor busiC = sql.rawQuery("SELECT * FROM " + db.TABLE_Business_segment, null);
                    int countBusin = busiC.getCount();
                    String columnNameBusin, columnValueBusin = "";
                    ContentValues busivalues = new ContentValues();
                    for (int i = 0; i < businessSegJSONArray.length(); i++) {
                        JSONObject jorder = businessSegJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < busiC.getColumnCount(); j++) {
                                columnNameBusin = busiC.getColumnName(j);

                                columnValueBusin = jorder.getString(columnNameBusin);
                                busivalues.put(columnNameBusin, columnValueBusin);


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        long a3 = sql.insert(db.TABLE_Business_segment, null, busivalues);

                        Log.d("BusinessSegment", String.valueOf(a3));

                    }


                    /*

                     */
/****************************Source of prospect******************************/

                    JSONArray srcOfProspectJSONArray = jsonObjectMain.getJSONArray("SrcOfProsp");
                    sql.delete(db.TABLE_Prospectsource, null,
                            null);
                    Cursor sourceC = sql.rawQuery("SELECT * FROM " + db.TABLE_Prospectsource, null);
                    int countSource = sourceC.getCount();
                    String columnNameSource, columnValueSource = "";
                    ContentValues sourcevalues = new ContentValues();
                    for (int i = 0; i < srcOfProspectJSONArray.length(); i++) {
                        JSONObject jorder = srcOfProspectJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < sourceC.getColumnCount(); j++) {
                                columnNameSource = sourceC.getColumnName(j);

                                columnValueSource = jorder.getString(columnNameSource);
                                sourcevalues.put(columnNameSource, columnValueSource);


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long a5 = sql.insert(db.TABLE_Prospectsource, null, sourcevalues);
                        Log.i("Source of Prospect", String.valueOf(a5));

                    }

                    /*

                     *//*************************LEAD******************************/

                    JSONArray leadJSONArray = jsonObjectMain.getJSONArray("Lead");
                    sql.delete(db.TABLE_Referencetype, null,
                            null);
                    Cursor leadC = sql.rawQuery("SELECT * FROM " + db.TABLE_Referencetype, null);
                    int countLead = leadC.getCount();
                    String columnNameLead, columnValueLead = "";
                    ContentValues leadvalues = new ContentValues();
                    for (int i = 0; i < leadJSONArray.length(); i++) {
                        JSONObject jorder = leadJSONArray.getJSONObject(i);
                        try {
                            for (int j = 0; j < leadC.getColumnCount(); j++) {
                                columnNameLead = leadC.getColumnName(j);
                                // if (columnNameLead.equalsIgnoreCase("CustVendor") || columnNameLead.equalsIgnoreCase("CustVendorCode")) {
                                columnValueLead = jorder.getString(columnNameLead);
                                leadvalues.put(columnNameLead, columnValueLead);
                                // }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        long a6 = sql.insert(db.TABLE_Referencetype, null, leadvalues);
                        Log.i("Lead", String.valueOf(a6));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            AllMethodCall();
        }
    }

    private void AllMethodCall() {
        getCountry();
    }

    private void getCountry() {

        countryArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_COUNTRY;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                countryArrayList.add(new Country(cur.getString(cur.getColumnIndex("PKCountryId")),
                        cur.getString(cur.getColumnIndex("CountryName"))));


            } while (cur.moveToNext());

        }

        ArrayAdapter<Country> countryArrayAdapter = new ArrayAdapter<Country>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, countryArrayList);
        edt_country.setAdapter(countryArrayAdapter);

        getState();
    }

    private void getState() {

        stateArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_STATE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                stateArrayList.add(new State(cur.getString(cur.getColumnIndex("PKStateId")),
                        cur.getString(cur.getColumnIndex("StateDesc"))));


            } while (cur.moveToNext());

        }

        ArrayAdapter<State> countryArrayAdapter = new ArrayAdapter<State>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, stateArrayList);
        edt_state.setAdapter(countryArrayAdapter);

        getCity();
    }

    private void getCity() {

        cityArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_CITY;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                cityArrayList.add(new CityMaster(cur.getString(cur.getColumnIndex("PKCityID")),
                        cur.getString(cur.getColumnIndex("CityName"))));


            } while (cur.moveToNext());

        }

        ArrayAdapter<CityMaster> countryArrayAdapter = new ArrayAdapter<CityMaster>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, cityArrayList);
        eAutoCity.setAdapter(countryArrayAdapter);

        getTerritory();


    }

    private void getTerritory() {

        // teritorybeanArrayList = cf.getTeritorybean();
        teritorybeanArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_Teritory;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                Teritorybean bean = new Teritorybean();
                bean.setPKTerritoryId(cur.getString(cur.getColumnIndex("PKTerritoryId")));
                bean.setTerritoryName(cur.getString(cur.getColumnIndex("TerritoryName")));

                teritorybeanArrayList.add(bean);
            } while (cur.moveToNext());

        }

        ArrayAdapter<Teritorybean> countryArrayAdapter = new ArrayAdapter<Teritorybean>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, teritorybeanArrayList);
        edt_territory.setAdapter(countryArrayAdapter);


        getBusinessSegment();

    }

    private void getBusinessSegment() {

        businessSegmentbeanArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_Business_segment;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                BusinessSegmentbean bean = new BusinessSegmentbean();
                bean.setPKBusiSegmentID(cur.getString(cur.getColumnIndex("PKBusiSegmentID")));
                bean.setSegmentDescription(cur.getString(cur.getColumnIndex("SegmentDescription")));

                businessSegmentbeanArrayList.add(bean);
            } while (cur.moveToNext());

        }

        ArrayAdapter<BusinessSegmentbean> countryArrayAdapter = new ArrayAdapter<BusinessSegmentbean>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, businessSegmentbeanArrayList);
        // edt_Businesssegment.showDropDown();
        edt_Businesssegment.setAdapter(countryArrayAdapter);


        getSourceOfProspect();

    }

    private void getSourceOfProspect() {

        prospectsourceBeanArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_Prospectsource;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                ProspectsourceBean bean = new ProspectsourceBean();
                bean.setSourceName(cur.getString(cur.getColumnIndex("SourceName")));
                bean.setPKSuspSourceId(cur.getString(cur.getColumnIndex("PKSuspSourceId")));

                prospectsourceBeanArrayList.add(bean);
            } while (cur.moveToNext());

        }

        ArrayAdapter<ProspectsourceBean> countryArrayAdapter = new ArrayAdapter<ProspectsourceBean>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, prospectsourceBeanArrayList);
        edtsource.setAdapter(countryArrayAdapter);


        getReference();
    }

    private void getReference() {
        /*  TABLE_Referencetype  */


        referenceBeanArrayList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_Referencetype;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                ReferenceBean bean = new ReferenceBean();
                bean.setCustVendor(cur.getString(cur.getColumnIndex("CustVendor")));
                bean.setCustVendorCode(cur.getString(cur.getColumnIndex("CustVendorCode")));

                referenceBeanArrayList.add(bean);
            } while (cur.moveToNext());

        }

        ArrayAdapter<ReferenceBean> countryArrayAdapter = new ArrayAdapter<ReferenceBean>
                (ProspectFilterActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, referenceBeanArrayList);
        edt_Reference.setAdapter(countryArrayAdapter);

    }

    public void onHideKeyBoard() {
        InputMethodManager imn = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        imn.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    private class LeadWiseCustomer extends AsyncTask<String, Void, String> {
        String res = "", response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = null;
            //    api/SuspectMasterAPI/GetFillLeadwiseCustomer?LeadWise=C
            url = CompanyURL + WebUrlClass.api_get_Reference + "?LeadWise=" + strings[0];
            try {
                res = ut.OpenConnection(url, ProspectFilterActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);

                    response = "{\"Activity\":\"" + response + "\n" + "\"}";
                } else {
                    response = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (!response.equals("error")) {
                try {
                    leadWiseBeanArrayList.clear();
                    ln_Reference2.setVisibility(View.VISIBLE);
                    JSONArray jResults = null;
                    JSONObject obj = new JSONObject(response);

                    String Msgcontent = obj.getString("Activity");
                    jResults = new JSONArray(Msgcontent);
                    ContentValues values = new ContentValues();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        LeadWiseBean leadWiseBean = new LeadWiseBean();
                        leadWiseBean.setCustVendorMasterId(jorder.getString("CustVendorMasterId"));
                        leadWiseBean.setCustVendorName(jorder.getString("CustVendorName"));
                        leadWiseBean.setMobile(jorder.getString("Mobile"));
                        leadWiseBean.setEmail(jorder.getString("Email"));
                        leadWiseBean.setAddress(jorder.getString("Address"));

                        leadWiseBeanArrayList.add(leadWiseBean);
                    }
                    /*"CustVendorMasterId": "5d047b9e-af98-405f-8aae-6ab1f58a9530",
	"CustVendorName": "(Chatrapati Shivaji Maharaj Chasak Kabadi Competition) Add Zone Brandcom",
	"Mobile": "9970411800",
	"Email": "Addzonebrandcom@gmail.com",
	"Address": "Near Mahaluxmi Garden, Balikasram Road, Savedi, Ahmednagar"*/

                    ArrayAdapter<LeadWiseBean> countryArrayAdapter = new ArrayAdapter<LeadWiseBean>
                            (ProspectFilterActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, leadWiseBeanArrayList);
                    edt_Reference2.setAdapter(countryArrayAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTRY && resultCode == COUNTRY) {
            if (isCountry == true) {
                countryid = data.getStringExtra("ID");
                String countryName = data.getStringExtra("Name");
                //Toast.makeText(ProspectFilterActivity.this, countryName, Toast.LENGTH_SHORT).show();
                edt_country.setText(countryName);
            }else if(isState == true){
                stateId = data.getStringExtra("ID");
                String stateName = data.getStringExtra("Name");
                edt_state.setText(stateName);
            }else if(isCity == true){
                cityId = data.getStringExtra("ID");
                String cityName1 = data.getStringExtra("Name");
                eAutoCity.setText(cityName1);
            }else if(isTerritory == true){
                territoryId = data.getStringExtra("ID");
                String territoryName = data.getStringExtra("Name");
                edt_territory.setText(territoryName);
            }else if(isSource == true){
                sourceId = data.getStringExtra("ID");
                String sourceName = data.getStringExtra("Name");
                edtsource.setText(sourceName);
            }else if(isSalesFamily == true){
                salesId = data.getStringExtra("ID");
                saleFamilyName = data.getStringExtra("Name");
                edt_SalesFamily.setText(saleFamilyName);
            }else if(isBusinessSegment == true){
                businessId = data.getStringExtra("ID");
                String businessName = data.getStringExtra("Name");
                edt_Businesssegment.setText(businessName);
            }else if(isAddedBy == true){
                Added_id = data.getStringExtra("ID");
                String name =  data.getStringExtra("Name");
                spinner_added_by.setText(name);
            }

        }
    }


}

