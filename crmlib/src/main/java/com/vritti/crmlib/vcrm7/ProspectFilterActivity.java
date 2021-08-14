package com.vritti.crmlib.vcrm7;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CityBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class ProspectFilterActivity extends AppCompatActivity {

    public static   String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    CheckBox checkBox_added_by, checkBoxcity, checkBoxcontact_name, checkBoxmobile_no, checkBoxfirmname, checkBoxProduct, checkBoxnewprospectsonly, checkBoxopencallsonly, checkBoxclosedcallsonly, checkBoxwithnocalls, checkBoxAddedPeriod;
    EditText edtfirm, edtcontact_name, edtmobile_no;
    MultiAutoCompleteTextView eAutoCity;
    Button buttonShowProspect, buttonAddProspect;
    ProgressHUD progressHUD;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    public static Context context;
    List<String> lstProduct = new ArrayList<String>();
    List<String> lstadded_by = new ArrayList<String>();
    String SelCriteria = "";
    ProgressBar progressbar;
    String newcalls, CityName;
    public static ArrayAdapter<CityBean> myAdapter;
    String City, Before_date, After_date, Added_id;
    List<String> lstCity = new ArrayList<String>();
    ArrayList<CityBean> cityBeen;
    private String Cityid;
    List<CityBean> lstdb;
    SearchableSpinner spinner_product, spinner_added_by;
    private String Productname;
    LinearLayout len_added_date;
    Spinner spinner_added_date;
    private String Month, Start_time;
    LinearLayout len_after, len_before;
    EditText edt_after_date, edt_before_date;
    ImageView img_date, img_date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_prospect_filter);
        context = ProspectFilterActivity.this;

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


        cityBeen = new ArrayList<>();

        init();
        setListener();
        lstdb = cf.getCitybean();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Start_time = format.format(cal.getTime());

        SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        String data = userpreferences.getString(WebUrlClass.Key_indivisual, "");
        if (data.equalsIgnoreCase("")) {
            new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadProspectID().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
        if (cf.check_City() > 0) {
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
        }


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

        if (cf.getProuctcount() > 0) {
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
        }

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
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        edtfirm = (EditText) findViewById(R.id.edtfirm);
        edtcontact_name = (EditText) findViewById(R.id.edtcontact_name);
        edtmobile_no = (EditText) findViewById(R.id.edtmobile_no);


        eAutoCity = (MultiAutoCompleteTextView) findViewById(R.id.eAutoCity);
        buttonShowProspect = (Button) findViewById(R.id.buttonShowProspect);
        buttonAddProspect = (Button) findViewById(R.id.buttonAddProspect);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);


        len_added_date = (LinearLayout) findViewById(R.id.len_added_date);
        spinner_added_date = (Spinner) findViewById(R.id.spinner_added_date);

        len_after = (LinearLayout) findViewById(R.id.len_after);
        len_before = (LinearLayout) findViewById(R.id.len_before);
        edt_after_date = (EditText) findViewById(R.id.edt_after_date);
        edt_before_date = (EditText) findViewById(R.id.edt_before_date);

        img_date = (ImageView) findViewById(R.id.img_date);
        img_date1 = (ImageView) findViewById(R.id.img_date1);

        spinner_added_by = (SearchableSpinner) findViewById(R.id.spinner_added_by);
        checkBox_added_by = (CheckBox) findViewById(R.id.checkBox_added_by);


        spinner_added_by.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct UserName,UserLoginId" +
                        " FROM " + db.TABLE_AddBy +
                        " WHERE UserName='" + spinner_added_by.getSelectedItem().toString() + "'";
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        img_date.setOnClickListener(new View.OnClickListener() {
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

                                Before_date = formateDateFromstring("yyyy/MM/dd", "dd MMM yyyy", date_Before);
                                edt_before_date.setText(Before_date);

                                // eBirthdate.setText(dayOfMonth+"-"+monthOfYear+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });
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


        eAutoCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Cityid = lstdb.get(position).getPKCityID();
                City = eAutoCity.getText().toString();
                System.out.println("City :" + Cityid);
               /* String query = "";
                query = "";
                String s = eAutoCity.getText().toString().trim();
                        query += "SELECT * FROM " + db.TABLE_CITY;
                query += " WHERE CityName= '"+s+ "'";


                Cursor cur3 = sql.rawQuery(query, null);
                int count=cur3.getCount();
                if (cur3.getCount() > 0) {

                    cur3.moveToFirst();

                    do {

                        Cityid = cur3.getString(cur3.getColumnIndex("PKCityID"));
                        System.out.println("CityId :"+Cityid);


                    } while (cur3.moveToNext());
                } else {
                    Cityid = "";
                }*/


            }
        });
        spinner_added_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                         @Override
                                                         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                             Month = spinner_added_date.getSelectedItem().toString();
                                                             if (Month.equalsIgnoreCase("Last Month")) {
                                                                 len_before.setVisibility(View.GONE);
                                                                 len_after.setVisibility(View.GONE);

                                                                 SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Start_time + "' AS DATE ) ) ";
                                                             } else if (Month.equalsIgnoreCase("Before")) {
                                                                 len_before.setVisibility(View.VISIBLE);
                                                                 len_after.setVisibility(View.VISIBLE);

                                                                 SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + Before_date + "' AS DATE ) ) ";
                                                             } else if (Month.equalsIgnoreCase("After")) {
                                                                 len_before.setVisibility(View.VISIBLE);
                                                                 len_after.setVisibility(View.VISIBLE);
                                                                 SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Before_date + "' AS DATE ) ) ";
                                                             } else if (Month.equalsIgnoreCase("Between")) {
                                                                 SelCriteria += " AND ( CAST(AddedDt AS DATE) BETWEEN CAST('" + Before_date + "' AS DATE ) AND CAST('" + After_date + "' AS DATE ) ) ";
                                                                 len_after.setVisibility(View.VISIBLE);
                                                                 len_before.setVisibility(View.VISIBLE);

                                                             }


                                                         }

                                                         @Override
                                                         public void onNothingSelected(AdapterView<?> adapterView) {
                                                         }
                                                     }
        );
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

    public boolean validate_mobile_no() {
        // TODO Auto-generated method stub

        if (edtmobile_no.getText().toString().equalsIgnoreCase("") ||
                edtmobile_no.getText().toString().equalsIgnoreCase(" ") ||
                edtmobile_no.getText().toString().equalsIgnoreCase(null) || edtmobile_no.getText().toString().trim().length() != 10) {

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

                if (isnet()) {
                    new StartSession(ProspectFilterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadDefaultProspect().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                                    Context.MODE_PRIVATE);
                            String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                            CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                            Context.MODE_PRIVATE);
                    String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                    CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
                }
            }
        });


        buttonShowProspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);*/

                SelCriteria = "";


                String city = Cityid;
               /* if (checkBoxnewprospectsonly.isChecked()){
                    SelCriteria=checkBoxnewprospectsonly.getText().toString();
                    System.out.println("Newcalls :"+SelCriteria);

                }*/
               /* if (checkBoxfirmname.isChecked() == true ||checkBoxnewprospectsonly.isChecked()==true ||checkBoxnewprospectsonly.isChecked()==true||checkBoxopencallsonly.isChecked()==true||checkBoxclosedcallsonly.isChecked()==true ||checkBoxwithnocalls.isChecked()==true) {
                    if (validate_firm()) {
                        SelCriteria += "AND FirmName like '%" + edtfirm.getText().toString() + "AND Addeddt>GETDATE()-2"+"AND OpenCalls <> '0'AND Closecalls=0"+"AND CloseCalls<>'0'AND OpenCalls=0"+ "AND OpenCalls='0' and CloseCalls='0'";
                    }
                } else {

                }

                if (checkBoxProduct.isChecked() == true||checkBoxfirmname.isChecked() == true ||checkBoxnewprospectsonly.isChecked()==true ||checkBoxnewprospectsonly.isChecked()==true||checkBoxopencallsonly.isChecked()==true||checkBoxclosedcallsonly.isChecked()==true ||checkBoxwithnocalls.isChecked()==true) {
                    if (validate_product()) {//SelCriteria += "AND ItemDesc like '%" + fname + "%'";

                        SelCriteria += "AND ItemDesc like '%" + edtProduct.getText().toString() +"AND Addeddt>GETDATE()-2"+"AND OpenCalls <> '0'AND Closecalls=0"+"AND CloseCalls<>'0'AND OpenCalls=0"+"AND OpenCalls='0' and CloseCalls='0'";
                    }
                }
*/

               /* if (checkBoxopencallsonly.isChecked()==true){
                    SelCriteria="AND OpenCalls <> '0'AND Closecalls=0" + edtfirm.getText().toString();
                    System.out.println("OpenEdit :"+SelCriteria);
                }*/


                if (checkBoxfirmname.isChecked() == true) {
                    if (validate_firm()) {
                        SelCriteria += "AND FirmName like '%" + edtfirm.getText().toString() + "%'";
                    }
                }

                if (checkBoxProduct.isChecked() == true) {

                    SelCriteria += "AND ItemDesc like '%" + spinner_product.getSelectedItem().toString() + "%'";

                }

                if (checkBoxmobile_no.isChecked() == true) {
                    if (validate_mobile_no()) {
                        SelCriteria += "AND Mobile like '%" + edtmobile_no.getText().toString() + "%'";
                    }
                }

                if (checkBoxcontact_name.isChecked() == true) {

                    SelCriteria += "AND ContactName like '%" + edtcontact_name.getText().toString() + "%'";
                }

                if (checkBoxcity.isChecked() == true) {
                    SelCriteria += "AND FKCityId IN(" + Cityid + ")";

                    /*for (int i = 0; i < City.length(); i++) {
                        if(i==0)
                            SelCriteria +="AND FKCityId IN(" + Cityid + ")";
                        else
                            SelCriteria +=",'"+Cityid+"'";
                    }
*/
                }

                if (checkBoxnewprospectsonly.isChecked() == true) {
                    SelCriteria += "AND Addeddt>GETDATE()-2 AND FirmName like '%" + edtfirm.getText().toString() + "%'";
                }

                if (checkBoxopencallsonly.isChecked() == true) {
                    SelCriteria += "AND OpenCalls <> '0'AND Closecalls=0 AND FirmName like '%" + edtfirm.getText().toString() + "%'";

                    //SelCriteria="AND OpenCalls <> '0'AND Closecalls=0";

                }


                if (checkBoxAddedPeriod.isChecked() == true) {
                    if (Month.equalsIgnoreCase("Last Month")) {
                        len_before.setVisibility(View.GONE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Start_time + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Before")) {
                        len_before.setVisibility(View.VISIBLE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) <= CAST('" + Before_date + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("After")) {
                        len_before.setVisibility(View.VISIBLE);
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) >= CAST('" + Before_date + "' AS DATE ) ) ";
                    } else if (Month.equalsIgnoreCase("Between")) {
                        SelCriteria += " AND ( CAST(AddedDt AS DATE) BETWEEN CAST('" + Before_date + "' AS DATE ) AND CAST('" + After_date + "' AS DATE ) ) ";

                    }
                }
                if (checkBoxclosedcallsonly.isChecked() == true) {

                    SelCriteria += "AND CloseCalls<>'0'AND OpenCalls=0 AND FirmName like '%" + edtfirm.getText().toString() + "%'";

//                    SelCriteria= "AND  CloseCalls<>'0'AND OpenCalls=0";
                }

                if (checkBoxwithnocalls.isChecked() == true) {
                    SelCriteria += "AND OpenCalls='0' and CloseCalls='0'";
                }
                if (checkBox_added_by.isChecked() == true) {
                    SelCriteria += " AND AddedBy = '" + Added_id + "'";

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

                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Select Filter First..,", Toast.LENGTH_SHORT);
                }

            }
        });

        checkBoxfirmname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edtfirm.setVisibility(View.VISIBLE);
                } else {
                    edtfirm.setVisibility(View.GONE);
                }
            }
        });
        checkBoxProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    spinner_product.setVisibility(View.VISIBLE);
                } else {
                    spinner_product.setVisibility(View.GONE);
                }
            }
        });

        checkBoxcontact_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edtcontact_name.setVisibility(View.VISIBLE);
                } else {
                    edtcontact_name.setVisibility(View.GONE);
                }
            }
        });


        checkBoxmobile_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    edtmobile_no.setVisibility(View.VISIBLE);
                } else {
                    edtmobile_no.setVisibility(View.GONE);
                }
            }
        });

        checkBoxcity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    eAutoCity.setVisibility(View.VISIBLE);
                } else {
                    eAutoCity.setVisibility(View.GONE);
                }
            }
        });
        checkBox_added_by.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    spinner_added_by.setVisibility(View.VISIBLE);
                } else {
                    spinner_added_by.setVisibility(View.GONE);
                }
            }
        });
        checkBoxnewprospectsonly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        checkBoxAddedPeriod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    len_added_date.setVisibility(View.VISIBLE);
                } else {

                    len_added_date.setVisibility(View.GONE);
                }
            }
        });



       /* edtProduct.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);


                    SelCriteria = "";
                    if (checkBoxfirmname.isChecked() == true) {
                        if (validate_firm()) {
                            SelCriteria += "AND FirmName like '%" + edtfirm.getText().toString() + "%'";
                        }
                    } else {

                    }

                    if (checkBoxProduct.isChecked() == true) {
                        if (validate_product()) {//SelCriteria += "AND ItemDesc like '%" + fname + "%'";

                            SelCriteria += "AND ItemDesc like '%" + edtProduct.getText().toString() + "%'";
                        }
                    } else {

                    }

                    if (checkBoxnewprospectsonly.isChecked()==true){
                        SelCriteria="AND Addeddt>GETDATE()-2 AND FirmName like '%" + edtfirm.getText().toString()+"%'";
                    }

                    if (checkBoxopencallsonly.isChecked()==true){
                        SelCriteria="AND OpenCalls <> '0'AND Closecalls=0 AND FirmName like '%" + edtfirm.getText().toString()+"%'";

                        //SelCriteria="AND OpenCalls <> '0'AND Closecalls=0";

                    }



                    if (checkBoxclosedcallsonly.isChecked()==true){

                        SelCriteria="AND CloseCalls<>'0'AND OpenCalls=0 AND FirmName like '%" + edtfirm.getText().toString()+"%'";

//                    SelCriteria= "AND  CloseCalls<>'0'AND OpenCalls=0";
                    }


                    if (checkBoxwithnocalls.isChecked()==true){
                        SelCriteria=" AND OpenCalls='0' and CloseCalls='0' AND FirmName like '%" + edtfirm.getText().toString()+"%'";
                    }


                    else {

                    }


                    if (checkBoxProduct.isChecked() == true) {
                        if (validate_product()) {//SelCriteria += "AND ItemDesc like '%" + fname + "%'";

                            SelCriteria += "AND ItemDesc like '%" + edtProduct.getText().toString() + "%'";
                        }
                    } else {

                    }

                    if (checkBoxmobile_no.isChecked()==true) {
                        if (validate_mobile_no()) {
                            SelCriteria += "AND Mobile like '%" + edtmobile_no.getText().toString() + "%'";
                        }
                    }

                    if (checkBoxcontact_name.isChecked()==true){

                        SelCriteria += "AND ContactName like '%" + edtcontact_name.getText().toString() + "%'";
                    }

                    if (checkBoxcity.isChecked()==true){

                        for (int i = 0; i < City.length(); i++) {
                            if(i==0)
                                SelCriteria +="AND FKCityId IN(" + Cityid + ")";
                            else
                                SelCriteria +=",'"+Cityid+"'";
                        }

                    }


                    if (SelCriteria != "") {
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadFilterJSON().execute(SelCriteria);
                                }
                            });

                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Select Filter First..,", Toast.LENGTH_SHORT);
                    }

                }
                return false;
            }
        });
*/

        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct ItemDesc,ItemMasterId" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemDesc='" + spinner_product.getSelectedItem().toString() + "'";
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

            super.onPreExecute();
          /*  progressDialog = new ProgressDialog(ProspectFilterActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/

            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Product;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Product, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_Product, null, values);
                        Log.e("", "");

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
                getproduct();
            }


        }

    }

    class DownloadFilterJSON extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_get_Prospect_filter + "?SelCriteria=" + URLEncoder.encode(params[0], "utf-8");

                System.out.println("FilterCall" + url);

               /* url = url.replaceAll(" ","%20");
                url = url.replaceAll("'","%27");*/
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
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
                    jResults = new JSONArray(response);
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
                }
                Intent intent = new Intent(ProspectFilterActivity.this, FilteredProspectListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
                    JSONArray jResults = null;
                    jResults = new JSONArray(response);
                    JSONObject obj0 = jResults.getJSONObject(0);//Enterprise Prospect
                    JSONObject obj1 = jResults.getJSONObject(1);//Small Business
                    JSONObject obj2 = jResults.getJSONObject(2);//Individual Prospect
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
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString(WebUrlClass.Key_Default_Prospect, DefaultProspect);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                    SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString(WebUrlClass.Key_Default_Prospect, "");
                    editor.commit();
                }


                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                        Context.MODE_PRIVATE);
                String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
            } else {
                SharedPreferences userpreferences = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.Key_Default_Prospect, "");
                editor.commit();


                String defaultprospect = userpreferences.getString(WebUrlClass.Key_Default_Prospect, "");
                CallDefaultProspectActivity(getApplicationContext(), defaultprospect);
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

            eAutoCity.setAdapter(customAdcity);

            eAutoCity.setThreshold(3);
            eAutoCity.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        }
    }

    private void getproduct() {


        lstProduct.clear();
        String query = "SELECT distinct ItemMasterId,ItemDesc" +
                " FROM " + db.TABLE_Product;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstProduct.add(cur.getString(cur.getColumnIndex("ItemDesc")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstProduct, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(ProspectFilterActivity.this,
                R.layout.crm_custom_spinner_txt, lstProduct);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(lstProduct, String.CASE_INSENSITIVE_ORDER);
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
        ProspectFilterActivity.this.finish();
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

    public void CallDefaultProspectActivity(Context context, String defaultprospect) {

        if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_enterprise)) {
            Intent intent = new Intent(context, ProspectEnterpriseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_business)) {
            Intent intent = new Intent(context, BusinessProspectusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            startActivity(intent);
        } else if (defaultprospect.equalsIgnoreCase(WebUrlClass.Key_Default_individual)) {
            Intent intent = new Intent(context, IndividualProspectusActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            startActivity(intent);
        } else {
            Intent intent = new Intent(context, ProspectEnterpriseActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            startActivity(intent);
        }
    }

}

