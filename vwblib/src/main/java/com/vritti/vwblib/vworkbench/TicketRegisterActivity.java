package com.vritti.vwblib.vworkbench;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.CustomerAdapter;
import com.vritti.vwblib.Adapter.ProductAdapter;
import com.vritti.vwblib.Adapter.ProductListAdapter;
import com.vritti.vwblib.Beans.CommonData;
import com.vritti.vwblib.Beans.Customer;
import com.vritti.vwblib.Beans.ReportedBy;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;


/**
 * Created by sharvari on 17-Apr-18.
 */

public class TicketRegisterActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    AutoCompleteTextView edt_search_name,edt_ticket_reportedby;
    ArrayList<Customer> customerArrayList;
    ArrayList<ReportedBy> reportedByArrayList;
    CustomerAdapter customerAdapter;
    SharedPreferences userpreferences;
    String  Customer_name;
    TextView txt_customer_name,txt_book_ticket;
    ProgressBar proress_reference;
    public static ProgressBar mprogress;
     JSONObject jsonObject;
    String FinalObject,ShiptToMasterId,ProductId,ShipMasterId,SubgroupUnit,productName;
    Spinner spinner_product_list,spinner_main_group,spinner_ticket_nature,spinner_ticket_category,spinner_ticket_assign,spinner_sub_group;;
    ArrayList<CommonData>ProductList;
    ArrayList<CommonData>TicketCategoryList;
    ArrayList<CommonData>SubGroupList;

    ArrayList<CommonData>AssignList;
    ArrayList<CommonData>MainGroupList;
    ArrayList<CommonData>TicketNature;
    ProductAdapter productAdapter;
    ProductListAdapter productListAdapter;
    ImageView img_time;


    String[] time = {"08:00 AM", "08:05 AM", "08:10 AM", "08:15 AM", "08:20 AM", "08:25 AM", "08:30 AM", "08:35 AM", "08:40 AM", "08:45 AM", "08:50 AM", "08:55 AM",
            "09:00 AM", "09:05 AM", "09:10 AM", "09:15 AM", "09:20 AM", "09:25 AM", "09:30 AM", "09:35 AM", "09:40 AM", "09:45 AM", "09:50 AM", "09:55 AM",
            "10:00 AM", "10:05 AM", "10:10 AM", "10:15 AM", "10:20 AM", "10:25 AM", "10:30 AM", "10:35 AM", "10:40 AM", "10:45 AM", "10:50 AM", "10:55 AM",
            "11:00 AM", "11:05 AM", "11:10 AM", "11:15 AM", "11:20 AM", "11:25 AM", "11:30 AM", "11:35 AM", "11:40 AM", "11:45 AM", "11:50 AM", "11:55 AM",
            "12:00 PM", "12:05 PM", "12:10 PM", "12:15 PM", "12:20 PM", "12:25 PM", "12:30 PM", "12:35 PM", "12:40 PM", "12:45 PM", "12:50 PM", "12:55 PM",
            "1:00 PM", "1:05 PM", "1:10 PM", "1:15 PM", "1:20 PM", "1:25 PM", "1:30 PM", "1:35 PM", "1:40 PM", "1:45 PM", "1:50 PM", "1:55 PM",
            "2:00 PM", "2:05 PM", "2:10 PM", "2:15 PM", "2:20 PM", "2:25 PM", "2:30 PM", "2:35 PM", "2:40 PM", "2:45 PM", "2:50 PM", "2:55 PM",
            "3:00 PM", "3:05 PM", "3:10 PM", "3:15 PM", "3:20 PM", "3:25 PM", "3:30 PM", "3:35 PM", "3:40 PM", "12:45 PM", "3:50 PM", "3:55 PM",
            "4:00 PM", "4:05 PM", "4:10 PM", "4:15 PM", "4:20 PM", "4:25 PM", "4:30 PM", "4:35 PM", "4:40 PM", "4:45 PM", "4:50 PM", "4:55 PM",
            "5:00 PM", "5:05 PM", "5:10 PM", "5:15 PM", "5:20 PM", "5:25 PM", "5:30 PM", "5:35 PM", "5:40 PM", "5:45 PM", "5:50 PM", "5:55 PM",
            "6:00 PM",


    };
    ArrayAdapter<String> spinner_timedisplay;
     int i;
    private String Mobile,Email,Name,TicketId,EmailId;
    EditText edt_time,edt_ticket_mobno,edt_ticket_email,edit_promise_date,edt_ticket_description,edt_ticket_solution;
    TextView txt_priority_level,txt_resolution_time,user_mobile_no,txt_send_request;
    String ReportedConcatname,Username,ProjectMasterId,PKModuleMastId,PKWarrantyRegister,ReportedName,ActivityTypeId;
    private String TicketNo,EnityId;
    LinearLayout len_sub_group,len_mobile;
    private String date;
    ImageView img_promise_date;
    private String Firstcustomer_name,Time,ResolutionTime,PriorityLevel,PriorityId;
    private String TicketUpdate;
    private String Solution="";
    private String EndDate,Current_date;
    TextView txt_enddate;
    private SimpleDateFormat dateFormatdate;
    private String  Promise_date;
    private SimpleDateFormat simpleDateFormat;
    int check = 0;
    private String parsedDate;
    LinearLayout linear_product,linear_desc,len_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_ticket_register_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  Ticket Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.vworkbench);
        setSupportActionBar(toolbar);
        edt_search_name = (AutoCompleteTextView) findViewById(R.id.edt_search_name);
        edt_ticket_reportedby = (AutoCompleteTextView) findViewById(R.id.edt_ticket_reportedby);
        edt_ticket_email = (EditText) findViewById(R.id.edt_ticket_email);
        edt_ticket_mobno = (EditText) findViewById(R.id.edt_ticket_mobno);
        edt_ticket_solution = (EditText) findViewById(R.id.edt_ticket_solution);
        edt_time = (EditText) findViewById(R.id.edt_time);

        img_time = (ImageView) findViewById(R.id.img_time);
        spinner_main_group = (Spinner) findViewById(R.id.spinner_main_group);
        spinner_ticket_nature = (Spinner) findViewById(R.id.spinner_ticket_nature);
        spinner_product_list = (Spinner) findViewById(R.id.spinner_product_list);
        spinner_ticket_category = (Spinner) findViewById(R.id.spinner_ticket_category);
        spinner_ticket_assign = (Spinner) findViewById(R.id.spinner_ticket_assign);
        spinner_sub_group=(Spinner) findViewById(R.id.spinner_sub_group);
        len_sub_group=(LinearLayout)findViewById(R.id.len_sub_group);
        len_mobile=(LinearLayout)findViewById(R.id.len_mobile);
        user_mobile_no=(TextView) findViewById(R.id.user_mobile_no);
        edit_promise_date=(EditText) findViewById(R.id.editTexteffertdetaildateset);
        edt_ticket_description=(EditText) findViewById(R.id.edt_ticket_description);
        img_promise_date=(ImageView)findViewById(R.id.buttoneffertdetaildateselect);

        txt_resolution_time = (TextView) findViewById(R.id.txt_resolution_time);
        txt_priority_level = (TextView) findViewById(R.id.txt_priority_level);
        txt_send_request = (TextView) findViewById(R.id.txt_send_request);
        txt_enddate = (TextView) findViewById(R.id.txt_enddate);

        linear_product=(LinearLayout) findViewById(R.id.linear_product);
        linear_desc=(LinearLayout) findViewById(R.id.linear_desc);
        len_button=(LinearLayout) findViewById(R.id.len_button);


        customerArrayList=new ArrayList<>();
        reportedByArrayList=new ArrayList<>();
        ProductList=new ArrayList<>();
        MainGroupList=new ArrayList<>();
        TicketNature=new ArrayList<>();
        TicketCategoryList=new ArrayList<>();
        AssignList=new ArrayList<>();
        SubGroupList=new ArrayList<>();


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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        txt_customer_name=(TextView) findViewById(R.id.txt_customer_name);
        txt_book_ticket=(TextView) findViewById(R.id.txt_book_ticket);
        proress_reference=(ProgressBar) findViewById(R.id.proress_reference);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

        dateFormatdate = new SimpleDateFormat("dd/MM/yyyy");
        Current_date=dateFormatdate.format(new Date());
        edit_promise_date.setText(Current_date);
        SimpleDateFormat dateFormatdate1 = new SimpleDateFormat("yyyy-MM-dd");
        date=dateFormatdate1.format(new Date());

        Calendar calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm aa");

        String time = simpleDateFormat.format(calander.getTime());
        edt_time.setText(time);


        if (isnet()) {
            new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetTicketNo().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

        /*spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    Time = parent.getSelectedItem().toString();
                      edt_time.setText(Time);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        img_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                int time = c.get(Calendar.AM_PM);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(TicketRegisterActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String Time=hourOfDay + ":" + minute;
                                DateFormat inputFormat = new SimpleDateFormat("hh:mm");
                                try {
                                    Date date = inputFormat.parse(Time);
                                    String time = simpleDateFormat.format(date);
                                    edt_time.setText(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }


        });
        txt_book_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_search_name.equals("")||edt_search_name==null){
                    Toast.makeText(TicketRegisterActivity.this,"Please select customer name",Toast.LENGTH_SHORT).show();
                }else {
                    txt_customer_name.setVisibility(View.VISIBLE);
                    Customer_name = edt_search_name.getText().toString();
                    txt_customer_name.setText(Customer_name);
                    StringTokenizer tokens = new StringTokenizer(Customer_name, "(");
                    Firstcustomer_name = tokens.nextToken();// this will contain "Fruit"


                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("CustName",Firstcustomer_name);
                        jsonObject.put("FirmName","");
                        jsonObject.put("CustPerson","");
                        jsonObject.put("MobileNo","");
                        jsonObject.put("OfficeNo","");
                        jsonObject.put("EmailId","");
                        jsonObject.put("LicNo","");
                        jsonObject.put("CustAdd","");
                        jsonObject.put("State","");
                        jsonObject.put("Dist","");
                        jsonObject.put("Taluka","");
                        jsonObject.put("SerialNo","");


                        FinalObject = jsonObject.toString();

                        if (isnet()) {
                            new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostPostSearchCustJSON().execute(FinalObject);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        spinner_product_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (ProductList.get(position).getIsExpired().equals("0")) {
                    linear_desc.setVisibility(View.VISIBLE);
                    len_button.setVisibility(View.VISIBLE);

                    ProductId = ProductList.get(position).getItemMasterId();
                    productName = ProductList.get(position).getItemDesc();
                    String answer = productName.substring(productName.indexOf("(") + 1, productName.indexOf(")"));

                    txt_enddate.setVisibility(View.VISIBLE);
                    txt_enddate.setText("AMC" + answer);

                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadMainGroup().execute(ShipMasterId, ProductId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    }
                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetWarrentyDetails().execute(ShipMasterId, ProductId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    }
                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadTicketNature().execute(ProductId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    }
                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadTicketCategoryList().execute(ProductId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    }
                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadAssignList().execute(ProductId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    }
                }else {
                    linear_desc.setVisibility(View.GONE);
                    len_button.setVisibility(View.GONE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_ticket_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TicketId=TicketCategoryList.get(position).getItemMasterId();
                if (isnet()) {
                    new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPriorityDetailsList().execute(TicketId);
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

        spinner_main_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PKModuleMastId=MainGroupList.get(position).getItemMasterId();
                if (isnet()) {
                    new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetSubgroupList().execute(PKModuleMastId);
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

       spinner_sub_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               SubgroupUnit=SubGroupList.get(position).getItemMasterId();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });




        spinner_ticket_assign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                UserMasterId=AssignList.get(position).getItemMasterId();
                Username=AssignList.get(position).getItemDesc();
                if (isnet()) {
                    new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetMobileDetails().execute(UserMasterId);
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

        edt_search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3)  {
                    final String pass = s.toString();


                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloaDownloadCustomerJSON().execute(pass);
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
        edt_ticket_reportedby.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    final String pass = s.toString();


                    if (isnet()) {
                        new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadReportedByJSON().execute(pass);
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
        edt_ticket_reportedby.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {

                ReportedName = (String) parent.getItemAtPosition(position);
                 //String i = data1.getCustomer_name();
               // ReportedName =reportedByArrayList.get(position).getCustomer_name();

                String data[] = ReportedName.split(",");
                Name=data[0];
                if (Name.equals("")) {
                    edt_ticket_reportedby.setText("");
                } else {
                    edt_ticket_reportedby.setText(Name);
                }

                Mobile=data[1];
                if (Mobile.equals("")) {
                    edt_ticket_mobno.setText("");

                } else {
                    edt_ticket_mobno.setText(Mobile);

                }
                Email=data[2];
                if (Email.equals("")) {
                    edt_ticket_email.setText("");

                } else {
                    edt_ticket_email.setText(Email);

                }

                EnityId=data[3];
                if (EnityId.equals("")){
                    EnityId="";
                }
                ReportedConcatname=Name+" "+Mobile+"  "+Email;


/*
                StringTokenizer st = new StringTokenizer(ReportedName, ",");
                try {
                    Name = st.nextToken();

                }catch(Exception e) {
                    Name="";
                    edt_ticket_reportedby.setText(Name);

                }
                try {
                    Mobile = st.nextToken();
                    if (Mobile.equals("")) {
                        edt_ticket_mobno.setText("");

                    } else {
                        edt_ticket_mobno.setText(Mobile);

                    }
                }catch(Exception e) {
                    Mobile="";
                    edt_ticket_mobno.setText(Mobile);
                }

                try {
                    Email = st.nextToken();
                    if (Email.equals("")) {
                        edt_ticket_email.setText("");

                    } else {
                        edt_ticket_email.setText(Email);

                    }
                }catch(Exception e) {
                    Email="";
                    edt_ticket_email.setText(Email);

                }
                try{
                    EnityId = st.nextToken();
                    if (EnityId.equals("")){
                        EnityId="";
                    }

                }catch(Exception e) {
                    EnityId="";
                }*/



                //TODO Do something with the selected text
            }
        });
        edit_promise_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketRegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                String date1 = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;

                                edit_promise_date.setText(date1);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        img_promise_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketRegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                String date1 = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                                edit_promise_date.setText(date1);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        txt_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ticketdescription = edt_ticket_description.getText().toString();
                Time =edt_time.getText().toString();
                Promise_date= edit_promise_date.getText().toString();
                String ConcatTime = date + " " + Time + ":00";
                Solution = edt_ticket_solution.getText().toString();
                Name=edt_ticket_reportedby.getText().toString();
                Email=edt_ticket_email.getText().toString();
                Mobile=edt_ticket_mobno.getText().toString();

                Date initDate = null;
                try {
                    initDate = new SimpleDateFormat("dd/MM/yyyy").parse(Promise_date);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    parsedDate = formatter.format(initDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (Validation()){
                    JSONObject JsonObject = new JSONObject();
                    try {
                        JsonObject.put("TicketNo", TicketNo);
                        JsonObject.put("txtTktDesc", Ticketdescription);
                        JsonObject.put("txtReportedBy", ReportedConcatname);
                        JsonObject.put("txtMobNo", Mobile);
                        JsonObject.put("txtEmail", Email);
                        JsonObject.put("ProjectMasterId", ProjectMasterId);
                        JsonObject.put("selModule", PKModuleMastId);
                        JsonObject.put("selUnit", SubgroupUnit);
                        JsonObject.put("selNatureOfTkt", ActivityTypeId);
                        JsonObject.put("selTktCategory", TicketId);
                        JsonObject.put("lblResolTime", ResolutionTime);
                        JsonObject.put("lblhours", "day");
                        JsonObject.put("lblPriorityLevel", PriorityId);
                        JsonObject.put("txtSol", Solution);
                        JsonObject.put("ddlAuto", UserMasterId);
                        JsonObject.put("IssuedToName", Username);
                        JsonObject.put("dtFrom", parsedDate);
                        JsonObject.put("lblMobNo", MobileNo);
                        JsonObject.put("cid", ShipMasterId);
                        JsonObject.put("pid", ProductId);
                        JsonObject.put("wid", PKWarrantyRegister);


                        JSONArray jsonArray = new JSONArray();
                        JsonObject.put("MatDtls", (Object) jsonArray);


                        JSONObject jsonObject1 = new JSONObject();

                        jsonObject1.put("EnityId", EnityId);
                        jsonObject1.put("Mode", "A");
                        jsonObject1.put("ReportedBy", Name);
                        jsonObject1.put("MobileNo", Mobile);
                        jsonObject1.put("Email", Email);

                        //  String  Enitity = jsonObject1.toString().replaceAll("\\\\", "");
//                    String C = jsonObject1.toString().replaceAll("\\\\", "");
//                    Enitity = Enitity.replaceAll("\\\\\\\\/", "");
//                    Enitity=Enitity.toString().replaceAll("\"","");
//                    Enitity = Enitity.toString().replaceAll("^\"|\"$", "");


                        JsonObject.put("obj", jsonObject1);


                        JsonObject.put("type", "SupportCallAssignment");
                        JsonObject.put("to", EmailId);
                        JsonObject.put("consigneeName", Firstcustomer_name);
                        JsonObject.put("productName", productName);
                        JsonObject.put("StartDate", date);
                        JsonObject.put("TimeReminder", ConcatTime);
                        JsonObject.put("segType", "Section");
                        JsonObject.put("RouteCustTcktNo", "");
                        JsonObject.put("IsFromRoute", "");
                        JsonObject.put("PromiseDate",edit_promise_date.getText().toString());
                        JsonObject.put("ServiceChargetoCust", "0");

                        TicketUpdate = JsonObject.toString();
                        TicketUpdate = TicketUpdate.replaceAll("\\\\", "");
                        // TicketUpdate = JsonObject.toString().replaceAll("\\\\", "");


                        if (isnet()) {
                            new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostTicketRegisterJSON().execute(TicketUpdate);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        }


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });

    }
    class DownloaDownloadCustomerJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error", name, id;
        String a[], b[];
        List<String> suggestions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proress_reference.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            suggestions = new ArrayList<String>();
            try {
                String url = CompanyURL + WebUrlClass.api_GetAutoCompleteReported +
                        "?txtval=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                if (res != null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);
                    response = response.substring(1, response.length() - 1);
                    a = response.split("\"");
                     suggestions.clear();
                    for (int i = 0; i < a.length; i++) {
                        if (a[i].equalsIgnoreCase(",")) {

                        } else {
                            name=a[i];
                            //b = a[i].split(",");
                           // name = b[0];
                            Customer customer = new Customer();
                            customer.setCustomer_name(name);
                            customerArrayList.add(customer);
                            suggestions.add(name);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //  dismissProgressDialog();
                                MySpinnerAdapter customAdcity = new MySpinnerAdapter(TicketRegisterActivity.this,
                                        R.layout.vwb_custom_spinner_txt, suggestions);
                                edt_search_name.setAdapter(customAdcity);
                                customAdcity.notifyDataSetChanged();
                                edt_search_name.setThreshold(1);
                            }
                        });
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

            proress_reference.setVisibility(View.GONE);
        }

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
    class PostPostSearchCustJSON extends AsyncTask<String, Void, String> {
        String response, res;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostSearchCust;
            try {
                res = String.valueOf(ut.OpenPostConnection(url, params[0], TicketRegisterActivity.this));
                res = res.toString().replaceAll("\\\\", "");
                res = res.replaceAll("\\\\\\\\/", "");
                res = res.substring(1, res.length() - 1);
                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (res == null) {
                Toast.makeText(TicketRegisterActivity.this, "Server error", Toast.LENGTH_SHORT).show();

            } else {
                if (res.contains("ShipToMasterId")) {
                    try {
                        JSONArray jResults = new JSONArray(res);
                        for (i = 0; i < jResults.length(); i++) {
                                JSONObject jproductobject = jResults.getJSONObject(i);
                                ShiptToMasterId = jproductobject.getString("ShipToMasterId");
                             final String  ShiptToMasterId1 = jproductobject.getString("ShipToMasterId");
                                if (isnet()) {
                                    new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new DownloadGetActiveContract().execute(ShiptToMasterId1);
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {

                                        }


                                    });

                                }



                        }




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


    }
    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);

    }
    class DownloadProductList extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                ProductList.clear();

                for (int i = 0; i < jResults.length(); i++) {
                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        String name=jSection.getString("ItemDesc");
                        String enddate=jSection.getString("EndDate");
                        String ExpDate=jSection.getString("ExpDate");
                        String IsExpired=jSection.getString("IsExpired");
                        String StartDate=jSection.getString("StartDate");
                        if (IsExpired.equals("0")){
                            EndDate=name+" ( "+"End Date -" + enddate+")";
                            commonData.setItemDesc(EndDate);
                        }else {
                            EndDate=name+" ( "+"End Date -" + StartDate+")"+" ( "+"End Date -" + enddate+")"+" ( "+"Exp Date -" + ExpDate+")";
                            commonData.setItemDesc(EndDate);
                            //commonData.setStartDate(StartDate);

                        }
                        commonData.setItemMasterId(jSection.getString("ItemMasterId"));
                        commonData.setIsExpired(IsExpired);

                       // txt_enddate.setText("" ( "+"End Date -" + enddate+")"End Date-" + EndDate);

                        ProductList.add(commonData);

                    }

                    productListAdapter=new ProductListAdapter(TicketRegisterActivity.this,ProductList);
                    spinner_product_list.setAdapter(productListAdapter);
                    linear_product.setVisibility(View.VISIBLE);

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetProductList + "?cid="+params[0]+"&IsWalkin=N";

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return "";
        }
    }
    class DownloadMainGroup extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                MainGroupList.clear();
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        commonData.setItemDesc(jSection.getString("ModuleName"));
                        commonData.setItemMasterId(jSection.getString("PKModuleMastId"));
                        ProjectMasterId=jSection.getString("ProjectId");
                        MainGroupList.add(commonData);

                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,MainGroupList);
                    spinner_main_group.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetModule + "?cid="+ShiptToMasterId+"&pid="+ProductId;

            res = ut.OpenConnection(url,getApplicationContext());
            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return "";
        }
    }
    class DownloadTicketNature extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                TicketNature.clear();
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        commonData.setItemDesc(jSection.getString("ActivityTypeName"));
                        ActivityTypeId=jSection.getString("ActivityTypeId");
                        commonData.setItemMasterId(ActivityTypeId);
                        TicketNature.add(commonData);

                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,TicketNature);
                    spinner_ticket_nature.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetNatureOfTkt + "?pid="+ProductId;

            res = ut.OpenConnection(url,getApplicationContext());
            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class DownloadGetActiveContract extends AsyncTask<String, Void, String> {
        String res,ShipToId;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (integer.contains("ConsigneeName")) {
                try {
                        if (isnet()) {
                            new StartSession(TicketRegisterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadProductList().execute(ShipToId);
                                    ShipMasterId=ShipToId;
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }



        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetActiveContract + "?sourceId="+params[0];
            ShipToId=params[0];
            Log.e("URL :",url);

            res = ut.OpenConnection(url,getApplicationContext());
            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

    class DownloadReportedByJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error", name, id;
        String a[], b[];
        List<String> suggestions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proress_reference.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            suggestions = new ArrayList<String>();
            try {
                String url = CompanyURL + WebUrlClass.api_GetAutoCompleteReported +
                        "?SearchText=" + URLEncoder.encode(params[0], "UTF-8")+"&cid="+ShipMasterId;

                res = ut.OpenConnection(url,getApplicationContext());
                if (res != null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);
                    response = response.substring(1, response.length() - 1);
                    a = response.split("\"");
                    suggestions.clear();
                    for (int i = 0; i < a.length; i++) {
                        if (a[i].equalsIgnoreCase(",")) {

                        } else {
                            name=a[i];
                            //b = a[i].split(",");
                            // name = b[0];
                            ReportedBy reportedBy = new ReportedBy();
                            reportedBy.setCustomer_name(name);
                            reportedByArrayList.add(reportedBy);
                            suggestions.add(name);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //  dismissProgressDialog();
                                MySpinnerAdapter customAdcity = new MySpinnerAdapter(TicketRegisterActivity.this,
                                        R.layout.vwb_custom_spinner_txt, suggestions);
                                edt_ticket_reportedby.setAdapter(customAdcity);
                                customAdcity.notifyDataSetChanged();
                                edt_ticket_reportedby.setThreshold(1);
                            }
                        });
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

            proress_reference.setVisibility(View.GONE);
        }

    }

    class DownloadTicketCategoryList extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                TicketCategoryList.clear();
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        commonData.setItemDesc(jSection.getString("Category"));
                        commonData.setItemMasterId(jSection.getString("PKProblemCategoryMaster"));
                        TicketCategoryList.add(commonData);

                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,TicketCategoryList);
                    spinner_ticket_category.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetTktCategory + "?pid="+params[0];

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class DownloadAssignList extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                AssignList.clear();
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        commonData.setItemDesc(jSection.getString("UserName"));
                        commonData.setItemMasterId(jSection.getString("UserMasterId"));
                        AssignList.add(commonData);

                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,AssignList);
                    spinner_ticket_assign.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetAssignToAuto + "?pid="+params[0];

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class GetPriorityDetailsList extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        CommonData commonData = new CommonData();
                        JSONObject jSection = jResults.getJSONObject(i);
                        ResolutionTime=jSection.getString("ResolutionTimeDays");
                        txt_resolution_time.setText(ResolutionTime);
                        PriorityLevel =jSection.getString("Priority");
                        txt_priority_level.setText(PriorityLevel);
                        PriorityId=jSection.getString("PriorityId");


                        Calendar c = Calendar.getInstance(); // Get Calendar Instance
                        c.setTime(dateFormatdate.parse(Current_date));

                        c.add(Calendar.DATE, Integer.parseInt(ResolutionTime));  // add 45 days
                        dateFormatdate = new SimpleDateFormat("dd/MM/yyyy");

                        Date resultdate = new Date(c.getTimeInMillis());   // Get new time
                        Promise_date = dateFormatdate.format(resultdate);
                        System.out.println("String date:"+Promise_date);
                        edit_promise_date.setText(Promise_date);

                        //PriorityId=jSection.getString("PriorityId");



                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,AssignList);
                    spinner_ticket_assign.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetTktCategoryDtls + "?categoryId="+params[0];

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class GetTicketNo extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            hidprogress();
            if (integer!=null){

                TicketNo=integer;

            }



        }


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetAutoCode;

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class GetSubgroupList extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;
        JSONObject jSection;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                SubGroupList.clear();
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 1) {
                        len_sub_group.setVisibility(View.VISIBLE);
                        CommonData commonData = new CommonData();
                        jSection= jResults.getJSONObject(i);
                        commonData.setItemDesc(jSection.getString("UnitDesc"));
                        commonData.setItemMasterId(jSection.getString("UnitId"));
                        SubGroupList.add(commonData);

                    }else {
                        len_sub_group.setVisibility(View.GONE);
                        jSection= jResults.getJSONObject(i);
                        SubgroupUnit=jSection.getString("UnitId");

                    }
                    if (jResults.length() > 1) {
                        productAdapter = new ProductAdapter(TicketRegisterActivity.this, SubGroupList);
                        spinner_sub_group.setAdapter(productAdapter);
                    }else {

                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetUnit + "?Moduleid="+params[0];

            res = ut.OpenConnection(url,getApplicationContext());

            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }
    class GetWarrentyDetails extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (integer.equals("[]")|| integer.equals("")) {
                Toast.makeText(TicketRegisterActivity.this,"Your warranty period is expired, unable to book ticket...",Toast.LENGTH_SHORT).show();
            }else {
                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int i = 0; i < jResults.length(); i++) {

                        if (jResults.length() > 0) {
                            JSONObject jSection = jResults.getJSONObject(i);

                            PKWarrantyRegister = jSection.getString("PKWarrantyRegister");

                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetWarrantyDtls + "?cid="+ShipMasterId+"&pid="+ProductId;

            res = ut.OpenConnection(url,getApplicationContext());
            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

    class GetMobileDetails extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {
                JSONArray jResults = new JSONArray(res);
                for (int i = 0; i < jResults.length(); i++) {

                    if (jResults.length() > 0) {
                        JSONObject jSection = jResults.getJSONObject(i);

                        MobileNo=jSection.getString("Mobile");
                        user_mobile_no.setText(MobileNo);
                        EmailId=jSection.getString("Email");

                    }

                    productAdapter=new ProductAdapter(TicketRegisterActivity.this,MainGroupList);
                    spinner_main_group.setAdapter(productAdapter);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_GetMobileNo + "?UserMasterId="+UserMasterId;

            res = ut.OpenConnection(url,getApplicationContext());
            if (res!=null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();
                res = res.substring(1, res.length() - 1);
            }
            return res;
        }
    }

    class PostTicketRegisterJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostInsertTkt;
            try {
                res = ut.OpenPostConnection(url, params[0],TicketRegisterActivity.this);
                response = res.toString();
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
            hidprogress();
            if (res!=null){
                Toast.makeText(TicketRegisterActivity.this,"Ticket register successfully",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(TicketRegisterActivity.this,ActivityMain.class));
                finish();
            }else {
                Toast.makeText(TicketRegisterActivity.this,"Please try again",Toast.LENGTH_SHORT).show();

            }



        }

    }
    private Boolean Validation() {

      if (!(edt_ticket_description.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter ticket description", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!(edt_ticket_reportedby.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Please select reported by", Toast.LENGTH_LONG).show();
            return false;
        }

     /* else if (!(edt_ticket_email.getText().toString().length() > 0)) {
          Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
          return false;
      }*/
      else if (!(edt_ticket_mobno.getText().toString().length() > 0)) {
          Toast.makeText(getApplicationContext(), "Please enter mobile no", Toast.LENGTH_LONG).show();
          return false;
      }
        /*else if (!(edt_ticket_solution.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter solution ", Toast.LENGTH_LONG).show();
            return false;
        }*/else {
            return true;
        }
    }

}