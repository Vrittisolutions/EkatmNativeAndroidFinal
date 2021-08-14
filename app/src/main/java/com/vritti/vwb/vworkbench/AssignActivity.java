package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AutoCompleteAssignAdapter;
import com.vritti.vwb.Adapter.CustomAdapter;
import com.vritti.vwb.Beans.ActivityEditDetails;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.Beans.FinancialYear;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.classes.CustomAssignAutoCompleteTextChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by 300151 on 10/19/2016.
 */
public class AssignActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", clientname = "", clientid = "", contactno = "",contactid="", id1="",
            CompanyURL = "",Concatdata="",testid="",ShiftKeyMasterId="";

    Utility ut;
    DatabaseHandlers db;
    SQLiteDatabase sdb;
    CommonFunction cf;
    Context context;
    SharedPreferences userpreferences;
    SharedPreferences workspaceferences;
    ArrayList<Customer> customerArrayList;
    TextView txtclientname, txtclientno;
    SimpleDateFormat sdf;

    Toolbar toolbar;
    String ScratchWorkspaceId, ScratchWorkspaceName;
    RadioGroup radio_groupPriority;
    RadioButton RaioNormal, RadioCritical, Radioimportant;
    AutoCompleteTextView edit_clientname;
    EditText ed_activity_desc, ed_hours, ed_bill_amount,edit_contactno;
    public CustomAutoCompleteView sp_issueto;
    Spinner sp_responsible, spinner_financial_year;
    Spinner sp_workspace, sp_group, sp_subgroup, sp_nature_work, spinner_all_group, spinner_clientname,spinner_contactno;
    LinearLayout ln_natureOfWork,ln_subGrp,ln_grp;
    CheckBox chk_mail,
            chk_completion,
            chk_ChkApproval;
    Button btn_fromdate, btn_endon, btn_save, btn_cancel,btn_save_contactdetails;
    TextView tv_Subactivity;
    EditText tv_Subactivity_desc;
    ProgressBar mprogress;
    static ArrayList<String> Workspace_list;
    static ArrayList<String> ActivityGroup_list;
    ArrayList<String> Subgroup_List, MainGroup_List;
    ArrayList<String> ChkUser_list, UnChkUser_list;
    ArrayList<Customer> clientarraylist;
    public  String ProjectId,moduleId,UnitId = " ";
    public static String prjMstId;
    public String IssueTomastId = "", GroupId = "";
    static int year, month, day;
    JSONObject ActivityJsonObj;
    String PriorityId = "1";/*Normal*/
    String ProjectName = "Scratch";
    String SubActivityId, SunActivityName, ActivityGroup;
    public Boolean IsSubAct = false;
    public AutoCompleteAssignAdapter autocompleteAdapter;
    public static Boolean IsProjectMember = false;
    String NatureOfWork, TypeID, Financial_Year = "",ServiceId="",UnitName="",moduleName="";
    LinearLayout mbillableamt,ln_financialyear;
    private String UserGroupName;
    SQLiteDatabase sql;
    private String ActivityDescription, Imagename, ImagePath;
    ArrayList<FinancialYear> financialYearArrayList = new ArrayList<>();
    ArrayList<ActivityEditDetails> activityDetailsArrayList = new ArrayList<>();
    CustomAdapter customAdapter;
    List<String> suggestions;
    List<Customer> clientcontactnoarraylist;
    List<String> clientcontactnoarraylist_test;
    Customer customer;
    MySpinnerAdapter customAdcity;
    JSONObject jsonMain, jObj;
    JSONArray jArray;
    boolean isContactDetailsAdded;
    private String d="";
    private String From_date="",To_date="";
    String ActivityName="",ActivityId="",Assigned_To="",Mode="";
    int flag_fromTeam;
    List<String> categories;
    String StartDate="",EndDate="";
    long timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_activitiy);


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        workspaceferences = getSharedPreferences("WORKSPACE",
                Context.MODE_PRIVATE);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
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
        ScratchWorkspaceName = userpreferences.getString("ScratchWorkspaceName", null);
        ScratchWorkspaceId = userpreferences.getString("ScratchWorkspaceId", null);

        sql = db.getWritableDatabase();


        InitView();
        /*spinner_contactno.setPrompt("Select contact person...");*/
        setListner();

        if (getIntent().hasExtra("Description") || getIntent().hasExtra("Imagename") || getIntent().hasExtra("ImagePath")) {
            Intent intent = getIntent();
            ActivityDescription = intent.getStringExtra("Description");
            Imagename = intent.getStringExtra("Imagename");
            ImagePath = intent.getStringExtra("ImagePath");
            ed_activity_desc.setText(ActivityDescription);
        }

        if (getIntent().hasExtra("Mode")) {
            Intent i = getIntent();

            ActivityName = i.getStringExtra("ActivityName");
            ActivityId = i.getStringExtra("ActivityId");//B3D6CCD6-49AE-4DD3-B299-285250AAFBF2
            Assigned_To = i.getStringExtra("Assigned_To");//ProjectId
            ProjectId = i.getStringExtra("ProjectId");
            flag_fromTeam = i.getIntExtra("Flag_fromteam", 0);
            Mode = i.getStringExtra("Mode");
            sp_responsible.setEnabled(false);
            sp_responsible.setFocusable(false);


            if(ut.isNet(context)){
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEditDataJson().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }





        if (getIntent().hasExtra("IsSubordinate")) {
            IsSubAct = true;
            Intent i = getIntent();
            //subgrp
            SubActivityId = i.getStringExtra("ActivityId");
            SunActivityName = i.getStringExtra("ActivityName");
            ed_activity_desc.setText(SunActivityName);
            tv_Subactivity.setVisibility(View.VISIBLE);
            tv_Subactivity_desc.setVisibility(View.VISIBLE);
            tv_Subactivity_desc.setText(SunActivityName);
            tv_Subactivity_desc.setSelection(tv_Subactivity_desc.length());

            ed_activity_desc.setFocusable(false);
            ed_activity_desc.setClickable(false);
        }

        if (cf.getWorkspaceList() > 0) {
            UpadateWorkspaceList();
        } else {
            refresh();
        }

        if (cf.getActivityGroupList() > 0) {
            UpdateActivityGroupList();
        } else {
            GroupList();
        }

        if (cf.getAllMember() > 0) {

        } else {

            AllMemberdatalist();
        }

        if (Constants.type == Constants.Type.Vwb) {
            mbillableamt.setVisibility(View.GONE);
            txtclientno.setVisibility(View.GONE);
            txtclientname.setVisibility(View.GONE);
            edit_clientname.setVisibility(View.GONE);
            spinner_contactno.setVisibility(View.GONE);
            ln_financialyear.setVisibility(View.GONE);
            btn_save_contactdetails.setVisibility(View.GONE);



        } else if (Constants.type == Constants.Type.PM) {
            mbillableamt.setVisibility(View.VISIBLE);

        }
        else {
            mbillableamt.setVisibility(View.VISIBLE);
        }


    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        Workspace_list = new ArrayList<String>();
        ActivityGroup_list = new ArrayList<>();
        Subgroup_List = new ArrayList<String>();
        MainGroup_List = new ArrayList<String>();
        ChkUser_list = new ArrayList<String>();
        UnChkUser_list = new ArrayList<String>();
        clientarraylist = new ArrayList<>();
        customerArrayList = new ArrayList<>();
        suggestions = new ArrayList<String>();
        clientcontactnoarraylist = new ArrayList<Customer>();
        clientcontactnoarraylist_test = new ArrayList<String>();


        txtclientno = (TextView) findViewById(R.id.txtclientno);
        txtclientname = (TextView) findViewById(R.id.txtclientname);
        radio_groupPriority = (RadioGroup) findViewById(R.id.radio_groupPriority);
        RaioNormal = (RadioButton) findViewById(R.id.radionorml);
        Radioimportant = (RadioButton) findViewById(R.id.radioimportant);
        RadioCritical = (RadioButton) findViewById(R.id.radiocritical);
        ed_activity_desc = (EditText) findViewById(R.id.ed_activity_desc);
        sp_responsible = (Spinner) findViewById(R.id.sp_responsible);
        spinner_all_group = findViewById(R.id.spinner_all_group);
        sp_workspace = (Spinner) findViewById(R.id.sp_workspace);
        sp_group = (Spinner) findViewById(R.id.sp_group);
        sp_subgroup = (Spinner) findViewById(R.id.sp_subgroup);
        sp_nature_work = (Spinner) findViewById(R.id.spinner_natureofwork);
        spinner_contactno = (Spinner)findViewById(R.id.spinner_contactno);
        ln_natureOfWork = findViewById(R.id.ln_natureOfWork);
        ln_subGrp = findViewById(R.id.ln_subGrp);
        ln_grp = findViewById(R.id.ln_grp);


        sp_issueto = (CustomAutoCompleteView) findViewById(R.id.sp_issueto);
        btn_fromdate = (Button) findViewById(R.id.btn_start_date);
        btn_endon = (Button) findViewById(R.id.btn_end_date);
        ed_hours = (EditText) findViewById(R.id.ed_hours);
        edit_clientname = (AutoCompleteTextView) findViewById(R.id.edit_clientname);
        ed_bill_amount = (EditText) findViewById(R.id.ed_billable_client);
        mbillableamt = (LinearLayout) findViewById(R.id.ln_billable_client);
        ln_financialyear = (LinearLayout)findViewById(R.id.ln_financialyear);
        tv_Subactivity = (TextView) findViewById(R.id.tv_Subactivity);
        tv_Subactivity_desc = (EditText) findViewById(R.id.ed_sub_activity_desc);
        chk_mail = (CheckBox) findViewById(R.id.mail);
        chk_mail.setChecked(true);
        chk_completion = (CheckBox) findViewById(R.id.chk_openend);
        chk_ChkApproval = (CheckBox) findViewById(R.id.chk_plan);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save_contactdetails=(Button)findViewById(R.id.btn_savecontact);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);//ed_billable_client
        spinner_financial_year = (Spinner) findViewById(R.id.spinner_financial_year);
        // SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
        btn_fromdate.setText(dff.format(new Date()));
        btn_endon.setText(dff.format(new Date()));
        ed_hours.setText("4");
        ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.Arr_AssignFrom));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_responsible.setAdapter(adapter);

        sp_issueto.addTextChangedListener(new CustomAssignAutoCompleteTextChangedListener(AssignActivity.this));
        String myObject = null;
        String[] ObjectItemData = new String[1];
        myObject = new String();
        ObjectItemData[0] = myObject;

        autocompleteAdapter = new AutoCompleteAssignAdapter(AssignActivity.this, R.layout.vwb_list_view_row_item, ObjectItemData);
        sp_issueto.setAdapter(autocompleteAdapter);

        int textLength1 = sp_issueto.getText().length();
        sp_issueto.setSelection(textLength1, textLength1);
        sp_issueto.setFocusable(true);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AssignActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Financial", "");
        Type type = new TypeToken<List<FinancialYear>>() {
        }.getType();
        financialYearArrayList = gson.fromJson(json, type);
        if (financialYearArrayList == null) {
            if (isnet()) {
                showProgress();
                new StartSession(AssignActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFinancialData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (financialYearArrayList.size() > 0) {
                customAdapter = new CustomAdapter(AssignActivity.this, financialYearArrayList);
                spinner_financial_year.setAdapter(customAdapter);
            }

        }


        spinner_financial_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Financial_Year = financialYearArrayList.get(position).getFYMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edit_clientname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                id1 = customerArrayList.get(position).getClient_id();

                new Downloaddatajsonmobile().execute(id1);

            }
        });

        /*spinner_contactno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id1 = customerArrayList.get(position).getClient_id();
                new Downloaddatajsonmobile().execute(id1);
            }
        });*/


    }

    class DownloaDownloadCustomerJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        String name;
        String id;
        String a[], b[];


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_clientname +
                        "?SearchText=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());

                if (res != null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);
                    response = response.substring(1, response.length() - 1);
                    response = response.toString().replaceAll("^\"|\"$", "");

                } else {
                    res = "NoData";
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            suggestions.clear();


            if (!res.equalsIgnoreCase(null) || !res.equalsIgnoreCase("")) {

                response = response.toString().replaceAll("^\"|\"$", "");
                response = response.replaceAll("\"", "");
                a = response.split(",");

                for (int i = 0; i < a.length; i++) {
                    if (a[i].equalsIgnoreCase("") || a[i].equalsIgnoreCase(null)) {
                       // Toast.makeText(getApplicationContext(),"NO Data",Toast.LENGTH_SHORT).show();

                    } else {
                        try {
                           String parts[]  = a[i].split("\\?"); // escape .
                             clientname = parts[0];
                             clientid = parts[1];
                             ShiftKeyMasterId = parts[2];
                        }catch(Exception e){
                            e.printStackTrace();
                            String response = "ERROR";
                        }

                   /* String data = a[i];
                    String test[]= data.split("\\?");
                    String clientname = test[0];
                    clientid = test[1];*/

                        customer = new Customer();
                        customer.setClient_name(clientname);
                        customer.setClient_id(clientid);
                        customerArrayList.add(customer);
                        suggestions.add(clientname);
                        cf.AddClient(customer);

                    }
                }

                mprogress.setVisibility(View.GONE);
                customAdcity = new MySpinnerAdapter(AssignActivity.this,
                        R.layout.vwb_custom_spinner_txt, suggestions);
                String Clientname;
                edit_clientname.setAdapter(customAdcity);
                customAdcity.notifyDataSetChanged();
                edit_clientname.setThreshold(1);
                //new  Downloaddatajsonmobile().execute();

            } else if (res.equalsIgnoreCase("error") || res.equalsIgnoreCase("NoData")) {
                Toast.makeText(getApplicationContext(), "No response", Toast.LENGTH_SHORT).show();
            }
        }


    }

    class Downloaddatajsonmobile extends AsyncTask<String, Void, String> {

        String response, status;
        String a[];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";

            url = CompanyURL + WebUrlClass.api_clientmobileno + "?CID=" + clientid;

            String res = ut.OpenConnection(url,getApplicationContext());

            if(res!=null){
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.toString().replaceAll("^\"|\"$", "");
                String data = response;

            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            clientcontactnoarraylist.clear();

            /*if(status.equalsIgnoreCase("Valid")){
                Toast.makeText(AssignActivity.this,"true response",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AssignActivity.this,"False response",Toast.LENGTH_SHORT).show();
            }*/
           /* edit_contactno.setText(contactno);*/

           if(!result.equalsIgnoreCase("") || !result.equalsIgnoreCase(null)){

               clientcontactnoarraylist_test = new ArrayList<String>();

               try {
                   JSONArray jsonArray = new JSONArray(result);

                   for(int i=0 ; i<jsonArray.length() ;i++){

                       Customer customer = new Customer();
                       JSONObject jsonObject = jsonArray.getJSONObject(i);
                      /* for(int j=0;j < ColumnName.length();j++) {*/
                           customer.setClient_id(id1);
                           customer.setMobile(jsonObject.getString("ContactPersonname"));
                           customer.setEntityContactInfoId(jsonObject.getString("EntityContactInfoId"));

                           contactno = customer.getMobile();
                           contactid = customer.getEntityContactInfoId();

                           clientcontactnoarraylist_test.add(contactno);
                           //clientcontactnoarraylist_test.add(contactno);

                       isContactDetailsAdded = cf.addcontactdetails(customer);

                       if(isContactDetailsAdded){
                           Toast.makeText(getApplicationContext(),"Data inserted",Toast.LENGTH_SHORT).show();
                       }else {
                           Toast.makeText(getApplicationContext(),"Data not inserted",Toast.LENGTH_SHORT).show();
                       }

                       }

               } catch (JSONException e) {
                   e.printStackTrace();
                   String resp = "Error";
               }
               spinner_contactno.setPrompt("select contact person");

               ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignActivity.this,
                       android.R.layout.simple_spinner_item, clientcontactnoarraylist_test);

               adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
               spinner_contactno.setAdapter(adapter);

           }else {
               //no data
               Toast.makeText(getApplicationContext(),"No contact Present",Toast.LENGTH_SHORT).show();
           }

        }
    }

    class addcontactdetails extends AsyncTask<String ,Void ,String>{
        String response;
        String status;
        Object res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            String objFinalObj = jObj.toString().replaceAll("\\\\","");
            String url = null;
             url = CompanyURL+ WebUrlClass.api_addclientdetails;
             res= ut.OpenPostConnection(url,objFinalObj,getApplicationContext());
             response = res.toString().replaceAll("\\\\","");

             return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"Record Added Successfully",Toast.LENGTH_SHORT).show();

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

    private void setListner() {
        radio_groupPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                int a = index;
                if (a == 1) {/*Important*/
                    PriorityId = "2";
                } else if (a == 2) {/*Critical*/
                    PriorityId = "3";
                } else if (a == 0) {/*Normal*/
                    PriorityId = "1";
                }
            }
        });
        btn_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AssignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                From_date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                                btn_fromdate.setText(From_date);
                                btn_endon.setText(From_date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_endon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AssignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   datePicker.setMinDate(c.getTimeInMillis());
                                To_date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;

                                if(To_date.compareTo(From_date)<0){
                                  Toast.makeText(AssignActivity.this,"End date should not be less than start date",Toast.LENGTH_SHORT).show();
                                }else{

                                    btn_endon.setText(To_date);
                                    //Toast.makeText(AssignActivity.this,"End date is equal to start date",Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        sp_responsible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ActivityGroup = sp_responsible.getSelectedItem().toString();
                if (parent.getItemAtPosition(position).toString()
                        .equalsIgnoreCase(getResources().getString(R.string.assign_act_string_from_group))) {
                    sp_issueto.setVisibility(View.GONE);
                    spinner_all_group.setVisibility(View.VISIBLE);

                    //IsProjectMember = true;
                    //  UpadateWorkspaceList();
                }
                if (parent.getItemAtPosition(position).toString().
                        equalsIgnoreCase(getResources().getString(R.string.assign_act_string_from_participants))) {
                    sp_issueto.setVisibility(View.VISIBLE);
                    spinner_all_group.setVisibility(View.GONE);
                    // UpadateWorkspaceList();
                    IsProjectMember = true;

                } else if (parent.getItemAtPosition(position).toString()
                        .equalsIgnoreCase(getResources().getString(R.string.assign_act_string_from_all))) {
                    sp_issueto.setVisibility(View.VISIBLE);
                    spinner_all_group.setVisibility(View.GONE);
                    //   UpadateWorkspaceList();
                    IsProjectMember = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_workspace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ProjectName = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = workspaceferences.edit();
                editor.putString("worksp", ProjectName);
                editor.commit();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST + " WHERE ProjectName='" + ProjectName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ProjectId = cur.getString(cur.getColumnIndex("ProjectId"));
                    prjMstId = ProjectId;
                    UpadateMainGroupList();
                    if (!isProjectmemberpresent(prjMstId)) {
                        new StartSession(getApplicationContext(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadGetChkUserlistDataJSON().execute(prjMstId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                    if (!isnatureworkpresent(prjMstId)) {
                        new StartSession(getApplicationContext(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadNAtureOfWork().execute(prjMstId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        setSpinnerNatureOfWork(prjMstId);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No Workspace Assign or Refresh Data", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String ModuleName = parent.getItemAtPosition(position).toString();
                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ModuleName='" + ModuleName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    moduleId = cur.getString(cur.getColumnIndex("PKModuleMastId"));


                    new StartSession(AssignActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadSubGroupDataJSON().execute(moduleId);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);

                        }
                    });

                    //   UpadateSubGroupList();
                }


                //new DownloadSubGroupDataJSON().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_subgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitdesc = parent.getItemAtPosition(position).toString();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE UnitDesc='" + unitdesc + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UnitId = cur.getString(cur.getColumnIndex("UnitId"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_all_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserGroupName = spinner_all_group.getSelectedItem().toString();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_ActivityGetGroupList + " WHERE UserGroupName='" + UserGroupName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    GroupId = cur.getString(cur.getColumnIndex("PKUserGroupId"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Mode.equalsIgnoreCase("Edit")){

                    String d = ed_bill_amount.getText().toString();
                    if(!(d.equalsIgnoreCase(""))){
                        EditActivityJson();
                    }else{
                        if (ed_activity_desc.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Enter activity description", Toast.LENGTH_LONG).show();
                        }if (ed_hours.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Enter efforts", Toast.LENGTH_LONG).show();
                        }else {
                            EditActivityJson();
                        }
                    }

                }else{
                    String d = ed_bill_amount.getText().toString();
                    if (!(d.equalsIgnoreCase(""))) {
                        CreateActivityJson();
                   /* } else {
                        Toast.makeText(getApplicationContext(), "Enter Billable Amount", Toast.LENGTH_LONG).show();
                    }*/
                    } else {

                        if (ed_activity_desc.getText().toString().equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(), "Enter activity description", Toast.LENGTH_LONG).show();
                        }if (ed_hours.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Enter efforts", Toast.LENGTH_LONG).show();
                        }else {
                            CreateActivityJson();
                        }

                    }
                }
            }
        });

        btn_save_contactdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AssignActivity.this,"IN Btnsave contact",Toast.LENGTH_SHORT).show();
                addcontactdetails();


            }
        });
        sp_nature_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NatureOfWork = parent.getItemAtPosition(position).toString();
                TypeID = GetActivityTypeId(NatureOfWork);
                String IntemateCompletion = isIntementCompletion(TypeID);
                String Chargavle = isChargeble(TypeID);
                String ApprovalRequired = isApprovalRequired(TypeID);
                if(Constants.type == Constants.Type.Vwb) {
                    if (Chargavle.equalsIgnoreCase("true")
                            && ApprovalRequired.equalsIgnoreCase("true")) {
                        chk_mail.setChecked(true);
                        chk_ChkApproval.setChecked(true);
                        mbillableamt.setVisibility(View.VISIBLE);
                    } else {
                        mbillableamt.setVisibility(View.GONE);
                        chk_ChkApproval.setChecked(false);
                    }
                    if (IntemateCompletion.equalsIgnoreCase("true")) {
                        chk_completion.setChecked(true);
                    } else {
                        chk_completion.setChecked(false);
                    }
                }else {
                    mbillableamt.setVisibility(View.VISIBLE);
                }
               /* if (ApprovalRequired.equalsIgnoreCase("true")) {
                    chk_ChkApproval.setChecked(true);
                } else {
                    chk_ChkApproval.setChecked(false);
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* sp_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Priority = parent.getItemAtPosition(position).toString();
                if (Priority.equalsIgnoreCase("Important")) {
                    PriorityId = "2";
                } else if (Priority.equalsIgnoreCase("Critical")) {
                    PriorityId = "1";
                } else if (Priority.equalsIgnoreCase("Normal")) {
                    PriorityId = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        edit_clientname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    final String pass = s.toString();


                    if (isnet()) {
                        new StartSession(AssignActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloaDownloadCustomerJSON().execute(pass);
                                //String tst = edit_clientname.getText().toString();
                                //Log.e("value - ", tst);
                                //get clientname and its id from table - getDatafromTable(spinnervalue)
                                //getclientdata();
                                //pass id to API and get mobile number & ShiftKeyMasterId  - call method Asyntask
                                //store it in bean class and set mobile number to textview - in postexecute
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

        spinner_contactno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spin_position, long id) {

                String spin_ans = spinner_contactno.getSelectedItem().toString();
                //String txt2 = parent.getItemAtPosition(spin_position).toString();
               // clientcontactnoarraylist_test.get(pos).setANSWER(spin_ans);

                getentitycontactinfoid(spin_ans);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Auto-generated method stub
            }
        });

    }

    private String GetActivityTypeId(String natureOfWork) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeName='" + natureOfWork + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("ActivityTypeId"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String GetActivityTypeName(String activityTypeId) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + activityTypeId + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("ActivityTypeName"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isIntementCompletion(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IntimateCompletion"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isApprovalRequired(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IsApproval"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isChargeble(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IsChargable"));
            } while (c.moveToNext());
        }

        return data;
    }


    private String getAllMembers(String name) {
        UnChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ALL_MEMBERS + " WHERE UserName='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        String returndata = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                returndata = cur.getString(cur.getColumnIndex("UserMasterId"));
            } while (cur.moveToNext());
        }

        return returndata;
    }

    private String getentitycontactinfoid(String spin_ans) {
        SQLiteDatabase sql = db.getWritableDatabase();
        String contprsname = spin_ans;
        String EntityContactInfoId = null,Contactpersonname = null, ClientId = null;

        // String query = "SELECT EntityContactInfoId FROM " + db.TABLE_CLIENTCONTACT_DETAILS + " WHERE Contactpersonname='"+contprsname+"'";

        String query = " SELECT * FROM " + db.TABLE_CLIENTCONTACT_DETAILS + " WHERE Contactpersonname='"+ contprsname +"'"  ;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                EntityContactInfoId = cur.getString(cur.getColumnIndex("EntityContactInfoId"));
                Contactpersonname = cur.getString(cur.getColumnIndex("Contactpersonname"));
                ClientId = cur.getString(cur.getColumnIndex("ClientId"));
            } while (cur.moveToNext());
        }

        return EntityContactInfoId;
    }



    private String getProjectMembers(String ProjectId, String name) {
        ChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();

        String query = "SELECT * FROM " + db.TABLE_PROJECT_MEMBERS + " WHERE prjMstId='" + ProjectId + "' AND UserName='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        String data = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("UserMasterId"));
            } while (cur.moveToNext());
        }

        return data;

    }

    private String getStatusforBilling(String Id) {
        ChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();

        String query = "SELECT * FROM " + db.TABLE_ISBILLABLE_AMOUNT + " WHERE NAtureofworkID='" + Id + "'";
        Cursor cur = sql.rawQuery(query, null);
        String data = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("IsChargable"));
            } while (cur.moveToNext());
        }

        return data;

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void CreateActivityJson() {
        // showProgress();

        String issuedtoname = sp_issueto.getText().toString();

        if (IsProjectMember) {
            IssueTomastId = getProjectMembers(prjMstId, issuedtoname);
        } else {
            IssueTomastId = getAllMembers(issuedtoname);
        }


        if ((IssueTomastId.equalsIgnoreCase("") && GroupId.equalsIgnoreCase(""))) {

            Toast.makeText(getApplicationContext(), "Enter Valid User", Toast.LENGTH_LONG).show();

        } else {
            ActivityJsonObj = new JSONObject();
            try {

                ActivityJsonObj.put("UnitId", UnitId);
                ActivityJsonObj.put("ActivityTypeId", TypeID);
                ActivityJsonObj.put("CompletionActId", " ");
                ActivityJsonObj.put("IsCompActPresent", " ");
                ActivityJsonObj.put("PriorityId", PriorityId);
                String StartDate = getyyyymmdd(btn_fromdate.getText().toString());
                String EndADte = getyyyymmdd(btn_endon.getText().toString());
                ActivityJsonObj.put("StartDate", StartDate);
                ActivityJsonObj.put("EndDate", EndADte);
                ActivityJsonObj.put("ActualStartDate", StartDate);
                ActivityJsonObj.put("ActualEndDate", EndADte);
                ActivityJsonObj.put("DueDate", EndADte);
                ActivityJsonObj.put("ExpectedComplete_Date", EndADte);
                ActivityJsonObj.put("HoursRequired", ed_hours.getText().toString());
                ActivityJsonObj.put("FYYear", Financial_Year);
                ActivityJsonObj.put("ServiceId", ServiceId);

                //ActivityJsonObj.put("sourceId", clientid);
                //ActivityJsonObj.put("TktCustReportedBy", contactid);


                if(clientname.equalsIgnoreCase("")){
                    ActivityJsonObj.put("SourceId", " ");
                    ActivityJsonObj.put("SourceType", " ");
                }else{

                    ActivityJsonObj.put("SourceId",ShiftKeyMasterId);
                    ActivityJsonObj.put("SourceType","Support");
                    ActivityJsonObj.put("TktCustReportedBy", contactid);


                }

                if (ActivityGroup.equalsIgnoreCase("Group")) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(GroupId);
                    ActivityJsonObj.put("objarr", jsonArray);
                    ActivityJsonObj.put("UsrGrp", "Y");
                } else {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(IssueTomastId);
                    ActivityJsonObj.put("objarr", jsonArray);
                    ActivityJsonObj.put("UsrGrp", "N");
                }

                if (IsSubAct) {
                    ActivityJsonObj.put("IsSubActivity", "Y");
                    ActivityJsonObj.put("ParentActId", SubActivityId);
                    ActivityJsonObj.put("ActivityName", tv_Subactivity_desc.getText().toString());
                } else {
                    ActivityJsonObj.put("IsSubActivity", "N");
                    ActivityJsonObj.put("ParentActId", " ");
                    ActivityJsonObj.put("ActivityName", ed_activity_desc.getText().toString());
                }

                /*ActivityJsonObj.put("SourceId", " ");
                ActivityJsonObj.put("SourceType", " ");*/
                ActivityJsonObj.put("MOMId", " ");
                ActivityJsonObj.put("IsUnplanned", "N");
                String s;




                if (chk_completion.isChecked()) {
                    s = "Y";
                } else {
                    s = "N";
                }
                ActivityJsonObj.put("CompletionIntimate", s);
                String app;
                if (chk_ChkApproval.isChecked()) {
                    app = "1";
                } else {
                    app = "0";
                }
                ActivityJsonObj.put("IsApproval", app);//
                int ac = 0;
                if (Constants.type == Constants.Type.CRM) {


                } else if (Constants.type == Constants.Type.Vwb) {

                    if (mbillableamt.getVisibility() == View.VISIBLE) {
                        String d = ed_bill_amount.getText().toString();
                        if(d.equalsIgnoreCase("")){
                            ActivityJsonObj.put("BillableAmt", 0);
                        }else{
                            ac = Integer.parseInt(d);
                            ActivityJsonObj.put("BillableAmt", ac);
                        }
                        //ac = Integer.parseInt(d);
                    }else{
                        ActivityJsonObj.put("BillableAmt",0);
                    }


                } else if (Constants.type == Constants.Type.PM) {
                    mbillableamt.setVisibility(View.VISIBLE);
                     d = ed_bill_amount.getText().toString();
                     if (d.equalsIgnoreCase("")){
                         ActivityJsonObj.put("BillableAmt", 0);
                         }else {

                         ac = Integer.parseInt(d);
                         ActivityJsonObj.put("BillableAmt", ac);

                     }

                }



                String a;
                if (chk_mail.isChecked()) {
                    a = "Y";
                } else {
                    a = "N";

                }
                ActivityJsonObj.put("chkIntimteByEmail", a);
                JSONArray jsonArray = new JSONArray();
                ActivityJsonObj.put("SourceIdarr", jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String FinalObj = ActivityJsonObj.toString();
            String remark = "Assign activity  " + ed_activity_desc.getText().toString() + " to " + issuedtoname;
            String url = CompanyURL + WebUrlClass.api_PostInsertAct;

            String op = "Success";
            CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

          /*  new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new AssignActivityDataJson().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                    dismissProgress();
                }
            });*/


        }


    }

    private void EditActivityJson() {
        // showProgress();

        String issuedtoname = sp_issueto.getText().toString();

        if (IsProjectMember) {
            IssueTomastId = getProjectMembers(prjMstId, issuedtoname);
        } else {
            IssueTomastId = getAllMembers(issuedtoname);
        }


        if ((IssueTomastId.equalsIgnoreCase("") && GroupId.equalsIgnoreCase(""))) {

            Toast.makeText(getApplicationContext(), "Enter Valid User", Toast.LENGTH_LONG).show();

        } else {
            ActivityJsonObj = new JSONObject();
            try {

                ActivityJsonObj.put("UnitId", UnitId);
                ActivityJsonObj.put("ActivityTypeId", TypeID);
                ActivityJsonObj.put("CompletionActId", " ");
                ActivityJsonObj.put("IsCompActPresent", " ");
                ActivityJsonObj.put("PriorityId", PriorityId);
                String StartDate = getyyyymmdd(btn_fromdate.getText().toString());
                String EndADte = getyyyymmdd(btn_endon.getText().toString());
                ActivityJsonObj.put("StartDate", StartDate);
                ActivityJsonObj.put("EndDate", EndADte);
                ActivityJsonObj.put("ActualStartDate", StartDate);
                ActivityJsonObj.put("ActualEndDate", EndADte);
                ActivityJsonObj.put("DueDate", EndADte);
                ActivityJsonObj.put("ExpectedComplete_Date", EndADte);
                ActivityJsonObj.put("HoursRequired", ed_hours.getText().toString());
                ActivityJsonObj.put("FYYear", Financial_Year);
                ActivityJsonObj.put("ServiceId", ServiceId);

                //ActivityJsonObj.put("sourceId", clientid);
                //ActivityJsonObj.put("TktCustReportedBy", contactid);


                if(clientname.equalsIgnoreCase("")){
                    ActivityJsonObj.put("SourceId", " ");
                    ActivityJsonObj.put("SourceType", " ");
                }else{

                    ActivityJsonObj.put("SourceId",ShiftKeyMasterId);
                    ActivityJsonObj.put("SourceType","Support");
                    ActivityJsonObj.put("TktCustReportedBy", contactid);


                }

                if (ActivityGroup.equalsIgnoreCase("Group")) {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(GroupId);
                    ActivityJsonObj.put("objarr", jsonArray);
                    ActivityJsonObj.put("UsrGrp", "Y");
                } else {
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(IssueTomastId);
                    ActivityJsonObj.put("objarr", jsonArray);
                    ActivityJsonObj.put("UsrGrp", "N");
                }

                if (IsSubAct) {
                    ActivityJsonObj.put("IsSubActivity", "Y");
                    ActivityJsonObj.put("ParentActId", SubActivityId);
                    ActivityJsonObj.put("ActivityName", tv_Subactivity_desc.getText().toString());
                } else {
                    ActivityJsonObj.put("IsSubActivity", "N");
                    ActivityJsonObj.put("ParentActId", " ");
                    ActivityJsonObj.put("ActivityName", ed_activity_desc.getText().toString());
                }

                /*ActivityJsonObj.put("SourceId", " ");
                ActivityJsonObj.put("SourceType", " ");*/
                ActivityJsonObj.put("MOMId", " ");
                ActivityJsonObj.put("IsUnplanned", "N");
                String s;




                if (chk_completion.isChecked()) {
                    s = "Y";
                } else {
                    s = "N";
                }
                ActivityJsonObj.put("CompletionIntimate", s);
                String app;
                if (chk_ChkApproval.isChecked()) {
                    app = "1";
                } else {
                    app = "0";
                }
                ActivityJsonObj.put("IsApproval", app);//
                int ac = 0;
                if (Constants.type == Constants.Type.CRM) {


                } else if (Constants.type == Constants.Type.Vwb) {

                    if (mbillableamt.getVisibility() == View.VISIBLE) {
                        String d = ed_bill_amount.getText().toString();
                        if(d.equalsIgnoreCase("")){
                            ActivityJsonObj.put("BillableAmt", 0);
                        }else{
                            ac = Integer.parseInt(d);
                            ActivityJsonObj.put("BillableAmt", ac);
                        }
                        //ac = Integer.parseInt(d);
                    }else{
                        ActivityJsonObj.put("BillableAmt",0);
                    }


                } else if (Constants.type == Constants.Type.PM) {
                    mbillableamt.setVisibility(View.VISIBLE);
                    d = ed_bill_amount.getText().toString();
                    if (d.equalsIgnoreCase("")){
                        ActivityJsonObj.put("BillableAmt", 0);
                    }else {

                        ac = Integer.parseInt(d);
                        ActivityJsonObj.put("BillableAmt", ac);

                    }

                }



                String a;
                if (chk_mail.isChecked()) {
                    a = "Y";
                } else {
                    a = "N";

                }
                ActivityJsonObj.put("chkIntimteByEmail", a);
                //ActivityJsonObj.put("mode","EditAct");
                ActivityJsonObj.put("ActivityId",ActivityId);
                JSONArray jsonArray = new JSONArray();
                ActivityJsonObj.put("SourceIdarr", jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String FinalObj = ActivityJsonObj.toString();
            String remark = "Assign activity  " + ed_activity_desc.getText().toString() + " to " + issuedtoname;
            String url = CompanyURL + WebUrlClass.api_PostEditAct;

            String op = "Success";
            CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

          /*  new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new AssignActivityDataJson().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                    dismissProgress();
                }
            });*/


        }


    }

    class AssignActivityDataJson extends AsyncTask<Integer, Void, String> {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            dismissProgress();
            if (val.contains("Success")) {//Success
                Toast.makeText(AssignActivity.this, "Activity assign Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                startActivity(intent);
            } else if (val.equalsIgnoreCase(WebUrlClass.Errormsg)) {
                Toast.makeText(AssignActivity.this, "Can not Assign Activity ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AssignActivity.this, "Check internet connectivity", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            Object res;
            try {
                String objFinalObj = ActivityJsonObj.toString().replaceAll("\\\\", "");
                String url = CompanyURL + WebUrlClass.api_PostInsertAct;
                res = ut.OpenPostConnection(url, objFinalObj, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                //   response = response.replaceAll("\"", "");
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }

    private void UpadateMainGroupList() {
        MainGroup_List.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ProjectId='" + prjMstId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MainGroup_List.add(cur.getString(cur.getColumnIndex("ModuleName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, MainGroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_group.setAdapter(adapter);

        } else {
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadMainGroupDataJSON().execute(prjMstId);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });

        }

    }

    private String GetGroupName(String grpID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_MAINGROUP_LIST + " Where PKModuleMastId='" + grpID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("ModuleName"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String GetSubGrpName(String subGrpId) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_SUBGROUP_LIST + " Where UnitId='" + subGrpId + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("UnitDesc"));
            } while (c.moveToNext());
        }

        return data;
    }

    private void UpadateSubGroupList() {
        Subgroup_List.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
       // String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE PKModuleMastId='" + moduleId + "'";
        String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST;

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Subgroup_List.add(cur.getString(cur.getColumnIndex("UnitDesc")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, Subgroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_subgroup.setAdapter(adapter);

        } else {

        }

    }

    private void UpadateWorkspaceList() {
        Workspace_list.clear();
        String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("ProjectName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_workspace.setAdapter(adapter);
            String data = workspaceferences.getString("worksp", "");
            if (!(data.equalsIgnoreCase(""))) {
                int a = Workspace_list.indexOf(data);
                sp_workspace.setSelection(Workspace_list.indexOf(data));
            }
        }


    }

    private void UpdateActivityGroupList() {
        String query = "SELECT * FROM " + db.TABLE_ActivityGetGroupList;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityGroup_list.add(cur.getString(cur.getColumnIndex("UserGroupName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, ActivityGroup_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_all_group.setAdapter(adapter);

        }


    }

    class DownloadWorkspaceDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Workspace_list;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_WORKSPACE_LIST, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_WORKSPACE_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    ProjectId = jorder.getString("ProjectId");
                    if (CheckifRecordPresent(db.TABLE_WORKSPACE_LIST, "ProjectId", ProjectId)) {//ProjectId
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            //   ProjectId = jorder.getString("ProjectId");
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_WORKSPACE_LIST, null, values);
                        Log.e("Added Record Count", "" + a);
                    }

                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();


            if (response.equalsIgnoreCase("[]")) {

            } else if (response.equalsIgnoreCase("ProjectId")) {
                UpadateWorkspaceList();
            } else {
                UpadateWorkspaceList();
            }

        }

    }

    class DownloadMainGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_MainGroup_list + "?projectId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MAINGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    String ModuleId = jorder.getString("PKModuleMastId");
                    if (CheckifRecordPresent(db.TABLE_MAINGROUP_LIST, "PKModuleMastId", ModuleId)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            //  moduleId = jorder.getString("PKModuleMastId");
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("ProjectId")) {
                                columnValue = params[0];
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_MAINGROUP_LIST, null, values);
                        Log.e("Added Value", "" + a);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();

            if (response.equalsIgnoreCase("[]")) {

            } else if (response.equalsIgnoreCase("PKModuleMastId")) {
                UpadateMainGroupList();
            } else {
                UpadateMainGroupList();
            }

        }

    }

    class DownloadSubGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SubGroup_list + "?moduleId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                sql.delete(db.TABLE_SUBGROUP_LIST, null,
                        null);
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUBGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    String UnID = jorder.getString("UnitId");
                    if (CheckifRecordPresent(db.TABLE_SUBGROUP_LIST, "UnitId", UnID)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("PKModuleMastId")) {
                                columnValue = params[0];
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_SUBGROUP_LIST, null, values);
                        Log.e("DAtaInsert :", " " + a);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();//"[]"

            if (response.equalsIgnoreCase("[]")) {

            } else if (response.equalsIgnoreCase("UnitId")) {
                UpadateSubGroupList();
            } else {
                UpadateSubGroupList();
            }
        }

    }

    class DownloadGroupListDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_ActivityGroup_list;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ActivityGetGroupList, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        //   ProjectId = jorder.getString("ProjectId");
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_ActivityGetGroupList, null, values);
                    Log.e("Added Record Count", "" + a);


                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                UpdateActivityGroupList();
            }
        }

    }

    class Downloadisbillable extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_ISbillable_amt + "?ActivityTypeId='" + params[0] + "'";
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ISBILLABLE_AMOUNT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        //   ProjectId = jorder.getString("ProjectId");
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("NAtureofworkID")) {
                            columnValue = params[0];
                            values.put(columnName, columnValue);
                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                    }
                    long a = sql.insert(db.TABLE_ISBILLABLE_AMOUNT, null, values);
                    Log.e("Added Record Count", "" + a);


                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                String data = getStatusforBilling(TypeID);
                if (data.equalsIgnoreCase("true")) {


                    mbillableamt.setVisibility(View.VISIBLE);
                } else {
                    if (Constants.type == Constants.Type.PM) {
                        //mbillableamt.setVisibility(View.VISIBLE);

                    } else {
                        mbillableamt.setVisibility(View.GONE);
                    }

                }
            }
        }

    }

    class DownloadGetChkUserlistDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetChkUser_list + "?prjMstId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_PROJECT_MEMBERS, null,
                        null);*/
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROJECT_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("prjMstId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_PROJECT_MEMBERS, null, values);
                    String data = a + "";


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {

            }
        }

    }

    private class DownloadNAtureOfWork extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_NatureOfWork + "?projectId=" + params[0];
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(params[0]);
            data.add(response);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> res) {
            super.onPostExecute(res);
            dismissProgress();
            String prjmasterID = res.get(0);
            String response = res.get(1);
            String Activityid = "";
            if (response.contains("ActivityTypeName")) {
                try {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    /*sql.delete(db.TABLE_NATURE_Of_WORK, null,
                            null);*/
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NATURE_Of_WORK, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {//projectId
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("ProjectId")) {
                                columnValue = prjmasterID;
                                values.put(columnName, columnValue);
                            } else {
                                columnValue = jorder.getString(columnName);
                                if (columnValue.equalsIgnoreCase("null")) {
                                    columnValue = "false";
                                }
                                values.put(columnName, columnValue);
                            }
                        }
                        long a = sql.insert(db.TABLE_NATURE_Of_WORK, null, values);
                        Log.e("", "" + a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setSpinnerNatureOfWork(prjmasterID);
            } else {
                ut.displayToast(AssignActivity.this, "Could not connect to server");
            }
        }

    }

    private List<String> getAllActivityType(String projectmstrId) {
        List<String> data = new ArrayList<String>();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From '" + db.TABLE_NATURE_Of_WORK + "' where ProjectId='" + projectmstrId + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("ActivityTypeName")));
            } while (c.moveToNext());
        }

        return data;
    }

    private void setSpinnerNatureOfWork(String projectmsterId) {
       categories = getAllActivityType(projectmsterId);


        //Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        //Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_nature_work.setAdapter(dataAdapter);
    }

    private String getyyyymmdd(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        String ghe = "";
        try {
            date = dateFormat1.parse(data);
            ghe = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return ghe;
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    private void refresh() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadWorkspaceDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void GroupList() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGroupListDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void AllMemberdatalist() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUserlistData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    private boolean isProjectmemberpresent(String projectmasterID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_PROJECT_MEMBERS + " Where prjMstId='" + projectmasterID + "'", null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPresentToLocal(String natureworkID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_ISBILLABLE_AMOUNT + " Where NAtureofworkID='" + natureworkID + "'", null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean isnatureworkpresent(String projectId) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From '" + db.TABLE_NATURE_Of_WORK + "' where ProjectId='" + projectId + "'", null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean CheckifRecordPresent(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh1:
                refresh();
                GroupList();
                if (isnet()) {
                    new StartSession(AssignActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadUserlistData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                return true;
            default:
                return false;
        }

    }

    private void CreateOfflineAssignActivity(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();

        }

    }


    class DownloadUserlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUnChkUser_list;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ALL_MEMBERS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ALL_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;
/////////////////////////////////////
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_ALL_MEMBERS, null, values);
                    String data = a + "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
        }
    }

    class DownloadEditDataJson extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String cnt = ActivityId;

            String url = CompanyURL + WebUrlClass.api_EditTask +"?ActivityId="+cnt ;

            try {
                res = ut.OpenConnection(url, AssignActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            SimpleDateFormat sff = new SimpleDateFormat("dd MMM yyyy");
            Date FDate, TDate;
            String fd = "", td = "";

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {



            } else {

                activityDetailsArrayList = new ArrayList<>();
                activityDetailsArrayList.clear();

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);

                    for(int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        // ActivityName = jsonObject.getString("");
                        ed_activity_desc.setText(jsonObject.getString("Activityname"));

                        sp_issueto.setText(Assigned_To);
                        sp_issueto.setEnabled(false);
                        ed_hours.setText(jsonObject.getString("HoursRequired"));

                        int pos = -1;
                        String ProjectName = jsonObject.getString("ProjectName");

                        sp_workspace.setSelection(Workspace_list.indexOf(jsonObject.getString("ProjectName")));

                        moduleName  = GetGroupName(jsonObject.getString("PKModuleMastId"));
                        if(moduleName.equalsIgnoreCase("false")){
                            ln_grp.setVisibility(View.GONE);
                        }else{
                            UpadateMainGroupList();
                            if(MainGroup_List.size()>1){
                                sp_group.setSelection(MainGroup_List.indexOf(moduleName));
                            }else{
                                ln_grp.setVisibility(View.GONE);
                            }
                        }


                        UnitName = GetSubGrpName(jsonObject.getString("UnitId"));
                        if(UnitName.equalsIgnoreCase("false")){
                            ln_subGrp.setVisibility(View.GONE);
                        }else{
                            UpadateSubGroupList();
                            if(Subgroup_List.size()>1){
                                sp_subgroup.setSelection(Subgroup_List.indexOf(UnitName));
                            }else{
                                ln_subGrp.setVisibility(View.GONE);

                            }
                        }

                        NatureOfWork = GetActivityTypeName(jsonObject.getString("ActivityTypeId"));
                        if(NatureOfWork.equalsIgnoreCase("false")){
                            ln_natureOfWork.setVisibility(View.GONE);

                        }else{
                            sp_nature_work.setSelection(categories.indexOf(NatureOfWork));
                        }

                        String MessageDate = jsonObject.getString("StartDate");
                        StartDate = MessageDate.substring(MessageDate.indexOf("(") + 1, MessageDate.lastIndexOf(")"));
                        long timestamp = Long.parseLong(StartDate);
                        btn_fromdate.setText(getDate(timestamp));

                        EndDate = jsonObject.getString("Enddate");
                        EndDate = EndDate.substring(EndDate.indexOf("(") + 1, EndDate.lastIndexOf(")"));
                        long timestamp1 = Long.parseLong(EndDate);
                        btn_endon.setText(getDate(timestamp1));


                        String PriorityId = jsonObject.getString("PriorityId");
                        /*Normal*/
                        if(PriorityId.equalsIgnoreCase("1")){
                            RaioNormal.setChecked(true);
                            Radioimportant.setChecked(false);
                            RadioCritical.setChecked(false);

                       /*Important*/
                        }else if(PriorityId.equalsIgnoreCase("2")){
                            RaioNormal.setChecked(false);
                            Radioimportant.setChecked(true);
                            RadioCritical.setChecked(false);

                        /*Critical*/
                        }else if(PriorityId.equalsIgnoreCase("3")){
                            Radioimportant.setChecked(false);
                            RaioNormal.setChecked(false);
                            RadioCritical.setChecked(true);

                        }else{
                            RaioNormal.setChecked(true);
                            Radioimportant.setChecked(false);
                            RadioCritical.setChecked(false);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error",e.getMessage());
                }

            }


        }
    }


    private String getDate(long timeStamp){

        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


    class DownloadFinancialData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetFyYear;

            try {
                res = ut.OpenConnection(url, AssignActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    financialYearArrayList = new ArrayList<>();
                    financialYearArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        FinancialYear financialYear = new FinancialYear();
                        JSONObject jorder = jResults.getJSONObject(i);

                        financialYear.setFYMasterId(jorder.getString("FYMasterId"));
                        financialYear.setFYCode(jorder.getString("FYCode"));
                        financialYearArrayList.add(financialYear);


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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                Toast.makeText(AssignActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AssignActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(financialYearArrayList);
                editor.putString("Financial", json);
                editor.commit();
                customAdapter = new CustomAdapter(AssignActivity.this, financialYearArrayList);
                spinner_financial_year.setAdapter(customAdapter);


            }


        }
    }

    /*private void addcontactdetails(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AssignActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_client_details, null);
        dialogBuilder.setView(dialogView);

        EditText ContactPerson = dialogView.findViewById(R.id.edit_contactperson);
        EditText mobileno = dialogView.findViewById(R.id.edit_mobno);
        EditText email = dialogView.findViewById(R.id.edit_email);

    }*/

    protected void addcontactdetails() {
        // TODO Auto-generated method stub

        final Dialog myDialog = new Dialog(AssignActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.add_client_details);
        //myDialog.setCancelable(true);
        // myDialog.getWindow().setGravity(Gravity.BOTTOM);

        final EditText contactperson = (EditText) myDialog.findViewById(R.id.edit_contactperson);
        final EditText mobileno = (EditText) myDialog.findViewById(R.id.edit_mobno);
        final EditText emailid = (EditText)myDialog.findViewById(R.id.edit_email);

        Button btn_submit = (Button) myDialog.findViewById(R.id.btn_submit);
        Button btn_cancel = (Button) myDialog.findViewById(R.id.btn_cancel);


        //declare ids for all
        //get the typed data of dialog boss
        //append it as on named == Contactperson(Name - mobile no - gmail)
        //call the save api and spass clientid,mobileno,contactperson name,email fro it.

        btn_submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                testid = id1;
                String name = contactperson.getText().toString();
                String mobile = mobileno.getText().toString();
                String email = emailid.getText().toString();

                Concatdata =  name + "-" + mobile + "-" + email;
                String data = Concatdata;

                jObj = new JSONObject();
                jArray = new JSONArray();

                try {
                    jObj.put("ContactName",name);
                    jObj.put("MobileNo",mobile);
                    jObj.put("EmailId",email);
                    jObj.put("CID",id1);
                    jArray.put(jObj);

                    jsonMain = new JSONObject();
                    jsonMain.put("ContactPerson",jArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new addcontactdetails().execute();

                /*new addcontactdetails().execute(jObj);*/


               /* cf.addcontactdetails(Concatdata);*/

                //save data to server/table

                myDialog.dismiss();
                // finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {

            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();
            Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_LONG).show();
        }
    }

}