package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pixplicity.htmlcompat.HtmlCompat;
import com.vritti.crm.adapter.CRMDayEndReportAdapter;
import com.vritti.crm.bean.CRMDayEndReportBean;
import com.vritti.crm.bean.CRMDayEndReportDetailsBean;
import com.vritti.crm.bean.CalendarCollection;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

public class CRMDayEndReport extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    String UserType, Token;
    RecyclerView list_calllogs;
    public String IsChatApplicable;
    public String IsCollectionApplicable;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    static String Salesmodulevisible = "true";
    static String Salesmoduleinvisible = "false";
    private String settingKey, dabasename;
    ImageView img_pickupdate, img_share;
    Button btn_showReport;
    TextView txt_pickupdate;
    String date = "", selectedDate = "", selectedDate1 = "";
    LinearLayout ln_summary;
    ProgressBar progress_bar;
    Date result;
    int year, month, day;
    ArrayList<CRMDayEndReportBean> crmDayEndReportBeanArrayList;
    ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList;
    TextView txt_total, txt_overdue, txt_newProspect, txt_newOpp, txt_orderRec1, txt_orderRec2, txt_orderLost1, txt_orderLost2, txt_collection1, txt_collection2,
            txt_overdueColl1, txt_overdueColl2, txt_collOverdue1, txt_collOverdue2, txt_collOverdue3, txt_collOverdue4, txt_visit, txt_visitTime,
            txt_telephn, txt_telephnTime, txt_email, txt_emailTime;
    TextView txt_overdueColl30, txt_overdueColl45, txt_overdueColl90, txt_overdueCollabove;
    TextView txt_overdueCollAmt30, txt_overdueCollAmt45, txt_overdueCollAmt90, txt_overdueCollAmtabove;
    CRMDayEndReportAdapter crmDayEndReportAdapter;
    RecyclerView list_reportDetails;
    String totalTime = "";
    ImageView img_whatsapp;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_day_end_report_activity_v2);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, "");
        IsCollectionApplicable = userpreferences.getString(WebUrlClass.USERINFO_ISCOLLECTION_APPLICABLE, Salesmodulevisible);


        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();

        initView();
        setListner();


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        date = day + "/"
                + String.format("%02d", (month + 1))
                + "/" + year;
        selectedDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);
        selectedDate1 = formateDateFromstring("dd/MM/yyyy", "EEEE yyyy/MM/dd", date);
        txt_pickupdate.setText(date);
        selectedDate = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", date);

        if (getIntent().hasExtra("user")){
            UserMasterId=getIntent().getStringExtra("user");
            UserName=getIntent().getStringExtra("name");
            showReport();
            txt_title.setText(UserName);

        }else {
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
            showReport();
            txt_title.setText("CRM Report");

        }



    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

      /*  img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_pickupdate = findViewById(R.id.img_pickupdate);
        txt_pickupdate = findViewById(R.id.txt_pickupdate);
       // btn_showReport = findViewById(R.id.btn_showReport);
        ln_summary = findViewById(R.id.ln_summary);
        txt_total = findViewById(R.id.txt_total);
        txt_overdue = findViewById(R.id.txt_overdue);
        txt_newProspect = findViewById(R.id.txt_newProspect);
        txt_newOpp = findViewById(R.id.txt_newOpp);
        txt_orderRec1 = findViewById(R.id.txt_orderRec1);
        txt_orderRec2 = findViewById(R.id.txt_orderRec2);
        txt_orderLost1 = findViewById(R.id.txt_orderLost1);
        txt_orderLost2 = findViewById(R.id.txt_orderLost2);
        txt_collection1 = findViewById(R.id.txt_collection1);
        txt_collection2 = findViewById(R.id.txt_collection2);
        txt_overdueColl1 = findViewById(R.id.txt_overdueColl1);
        txt_overdueColl2 = findViewById(R.id.txt_overdueColl2);
        txt_collOverdue1 = findViewById(R.id.txt_collOverdue1);
        txt_collOverdue2 = findViewById(R.id.txt_collOverdue2);
        txt_collOverdue3 = findViewById(R.id.txt_collOverdue3);
        txt_collOverdue4 = findViewById(R.id.txt_collOverdue4);
        txt_visit = findViewById(R.id.txt_visit);
        txt_visitTime = findViewById(R.id.txt_visitTime);
        txt_telephn = findViewById(R.id.txt_telephn);
        txt_telephnTime = findViewById(R.id.txt_telephnTime);
        txt_email = findViewById(R.id.txt_email);
        txt_emailTime = findViewById(R.id.txt_emailTime);
        list_reportDetails = findViewById(R.id.list_reportDetails);
        img_share = findViewById(R.id.img_share);
        progress_bar = findViewById(R.id.progressbar);
        txt_overdueCollabove = findViewById(R.id.txt_overdueCollabove);
        txt_overdueColl30 = findViewById(R.id.txt_overdueColl30);
        txt_overdueColl45 = findViewById(R.id.txt_overdueColl45);
        txt_overdueColl90 = findViewById(R.id.txt_overdueColl90);
        txt_overdueCollAmtabove = findViewById(R.id.txt_overdueCollAmtabove);
        txt_overdueCollAmt30 = findViewById(R.id.txt_overdueCollAmt30);
        txt_overdueCollAmt45 = findViewById(R.id.txt_overdueCollAmt45);
        txt_overdueCollAmt90 = findViewById(R.id.txt_overdueCollAmt90);
        img_whatsapp = findViewById(R.id.img_whatsapp);

        crmDayEndReportBeanArrayList = new ArrayList<>();
        crmDayEndReportDetailsBeanArrayList = new ArrayList<>();

    }

    private void setListner() {
       /* txt_visit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // Here's the key code
                    txt_visit.setTextIsSelectable(true);
                    PhoneUtils.showKeyBoard(SendAlbumActivity.this,editText);
                }
            }
        });*/


        txt_pickupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                selectedDate = "";
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(CRMDayEndReport.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                txt_pickupdate.setText(date);
                                selectedDate = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", date);

                                if (getIntent().hasExtra("user")){
                                    UserMasterId=getIntent().getStringExtra("user");
                                    UserName=getIntent().getStringExtra("name");
                                    showReport();
                                }else {
                                    UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
                                    UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
                                    showReport();
                                }


                            }
                        }, year, month, day);

                /*Calendar minDate = Calendar.getInstance();
                minDate.set(year, month, day);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());*/
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });

        /*btn_showReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //    final String date = txt_pickupdate.getText().toString().trim();


                if (!(selectedDate == null || selectedDate.equals(""))) {

                    if (ut.isNet(CRMDayEndReport.this)) {
                        new StartSession(CRMDayEndReport.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DayEndReportDetails().execute(selectedDate);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(CRMDayEndReport.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CRMDayEndReport.this, "Please select date", Toast.LENGTH_SHORT).show();
                }


            }
        });
*/
        img_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String details = "", details1 = "";
                StringBuilder fullString1 = new StringBuilder();
                if (crmDayEndReportDetailsBeanArrayList.size() != 0) {
                    String prospectName = "-", person = "-", followupType = "-", outCome = "-", reason = "-", time = "-", duration = "-",
                            callPurposeDesc = "-",historynotes="";

                    for (int j = 0; j < crmDayEndReportDetailsBeanArrayList.size(); j++) {
                        prospectName = crmDayEndReportDetailsBeanArrayList.get(j).getFirmname().trim();
                        person = crmDayEndReportDetailsBeanArrayList.get(j).getContactName().trim();
                        followupType = crmDayEndReportDetailsBeanArrayList.get(j).getCallPurposeDesc().trim();
                        outCome = crmDayEndReportDetailsBeanArrayList.get(j).getOutcome().trim();
                        reason = crmDayEndReportDetailsBeanArrayList.get(j).getReasonDescription().trim();
                        time = crmDayEndReportDetailsBeanArrayList.get(j).getSchTime().trim();


                        try{
                            String[] split1;
                            String[] splitTime = new String[0];
                            if(time.contains("am") || time.contains("pm")||time.contains("AM") || time.contains("PM")){
                                split1 = time.split(" ");
                                splitTime = split1[0].split(":");
                            }else{
                                splitTime = time.split(":");
                            }

                            // int h  = Integer.parseInt(splitTime[0]);
                            if (!time.equals("")) {
                                time = updateTime(Integer.parseInt(splitTime[0].trim()), Integer.parseInt(splitTime[1].trim())).trim();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        // int h  = Integer.parseInt(splitTime[0]);
                        duration = crmDayEndReportDetailsBeanArrayList.get(j).getTotalHoursSpent().trim();
                        callPurposeDesc = crmDayEndReportDetailsBeanArrayList.get(j).getCallPurposeDesc().trim();
                        historynotes = crmDayEndReportDetailsBeanArrayList.get(j).getHistorynotes().trim();


                        if (prospectName.equals(null) || prospectName.equals("null") || prospectName.equals("")) {
                            prospectName = "-";
                        }
                        if (person.equals(null) || person.equals("null") || person.equals("")) {
                            person = "-";
                        }
                        if (followupType.equals(null) || followupType.equals("null") || followupType.equals("")) {
                            followupType = "";
                        }
                        if (outCome.equals(null) || outCome.equals("null") || outCome.equals("")) {
                            outCome = "-";
                        }
                        if (reason.equals(null) || reason.equals("null") || reason.equals("")) {
                            reason = "-";
                        }
                        if (time.equals(null) || time.equals("null") || time.equals("")) {
                            time = "-";
                        }
                        if (duration.equals(null) || duration.equals("null") || duration.equals("")) {
                            duration = "-";
                        }

                        String company = "\n" + "      Vritti Solutions Ltd      " + "\n\n";


                        Spanned prospect = HtmlCompat.fromHtml(context, "<b>" + prospectName + "</b>", 0);
                        // Toast.makeText(CRMDayEndReport.this,prospect,Toast.LENGTH_LONG).show();
                        String reasonappend = "";
                        if (outCome.equalsIgnoreCase("Call Again") ||
                                outCome.equalsIgnoreCase("Call Close without Order") ||
                                outCome.equalsIgnoreCase("Order Received") ||
                                outCome.equalsIgnoreCase("Reschedule") ||
                                outCome.equalsIgnoreCase("Order Lost") ||
                                outCome.equalsIgnoreCase("Demo Reschedule") ||
                                outCome.equalsIgnoreCase("Transfer To BOE") ||
                                outCome.equalsIgnoreCase("Customer will Call")) {
                            reasonappend = " because " + reason;
                        } else {
                            reasonappend = ".";
                        }

                        /*if (reason.equals("-")) {
                            if (historynotes.equalsIgnoreCase("null")||historynotes.equalsIgnoreCase("")||historynotes==null){
                                details1 = "Outcome was " + outCome + reasonappend + "\n";
                            }else {
                                details1 = "Outcome was " + outCome + reasonappend + "\n" + "Reason: " + historynotes;
                            }
                        } else {
                            if (historynotes.equalsIgnoreCase("null")||historynotes.equalsIgnoreCase("")||historynotes==null){
                                details1 = "Outcome was " + outCome + reasonappend + "\n";
                            }else {
                                details1 = "Outcome was " + outCome + reasonappend + "\n" + "Reason: " + historynotes;
                            }
                        }*/

                        if (reason.equals("-")) {
                            if (historynotes.equalsIgnoreCase("null")||historynotes.equalsIgnoreCase("")||historynotes==null) {

                                details1 = "\n" + "*" + prospect + "*" + " *" + followupType + "* " + "at " + "*" + time + "*" + " for duration of " + "*" + duration + "*" + " with " + "*" +
                                        person + "*" + " . Outcome is "
                                        + " *" + outCome + "* " + reasonappend + "\n";
                            }else {
                                details1 = "\n" + "*" + prospect + "*" + " *" + followupType + "* " + "at " + "*" + time + "*" + " for duration of " + "*" + duration + "*" + " with " + "*" +
                                        person + "*" + " . Outcome is "
                                        + " *" + outCome + "* " + reasonappend + "\n"+"Notes: " + historynotes+"\n";
                            }
                        } else {
                           /* details1 = "\n" + "*" + prospect + "*" + " *" + followupType + "* " + "at " + "*" + time + "*" + " for duration of " + "*" + duration + "*" + " with " + "*" +
                                    person + "*" + " . Outcome is "
                                    + " *" + outCome + "* " + reasonappend + "\n"+"Reason: " + historynotes+"\n";*/

                            if (historynotes.equalsIgnoreCase("null")||historynotes.equalsIgnoreCase("")||historynotes==null) {

                                details1 = "\n" + "*" + prospect + "*" + " *" + followupType + "* " + "at " + "*" + time + "*" + " for duration of " + "*" + duration + "*" + " with " + "*" +
                                        person + "*" + " . Outcome is "
                                        + " *" + outCome + "* " + reasonappend + "\n";
                            }else {
                                details1 = "\n" + "*" + prospect + "*" + " *" + followupType + "* " + "at " + "*" + time + "*" + " for duration of " + "*" + duration + "*" + " with " + "*" +
                                        person + "*" + " . Outcome is "
                                        + " *" + outCome + "* " + reasonappend + "\n"+"Notes: " + historynotes+"\n";
                            }


                        }

                        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                        SpannableStringBuilder sb1 = new SpannableStringBuilder(details1);
                        int start = details1.indexOf(details1);
                        int end = start + details1.length();
                        sb1.setSpan(boldStyle, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        fullString1.append(details1);

                    }


                    // details1 = new Gson().toJson(new CRMDayEndReportDetailsBean(details));
                    //  details1.replaceAll("\\{", "");
                    //   details1.replaceAll("\\}", "");
                }

                String dayDt = formateDateFromstring("yyyy-MM-dd", "EEEE, dd-MMM-yyyy", selectedDate);


                Calendar calendar = new GregorianCalendar(2021, 01, 18); // Note that Month value is 0-based. e.g., 0 for January.
                int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                final SpannableString out0 = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out0.setSpan(boldSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              /*  Collection: 10/19.90 L
                Overdue Collection: 10/19.00 L
                0-30: 0/0.00 L
                31-45: 0/0.00 L
                46-90: 0/0 .00 L
                91+: 0/0.00 L*/
/*
                SpannableString ss = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 21, 29,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

                String habitnumber = "<b>" + UserName + "</b> ";
                //  tv.setText(Html.fromHtml(habitnumber ));

                String msg = "*" + UserName + "*" + "\n" + "*" + dayDt + "* \n" + "\n\n" + "*Total*: " + txt_total.getText().toString() + "\n" +
                        "*Overdue*: " + txt_overdue.getText().toString() + "\n\n" +
                        "*New Prospect*: " + txt_newProspect.getText().toString() + "\n" +
                        "*New Opportunities*: " + txt_newOpp.getText().toString() + "\n\n" +
                        "*Order Received*: " + txt_orderRec1.getText().toString() + "" + txt_orderRec2.getText().toString() + "\n" +
                        "*Order Lost*: " + txt_orderLost1.getText().toString() + "" + txt_orderLost2.getText().toString() + "\n\n" +
                        "*Collection*: " + txt_collection1.getText().toString() + "" + txt_collection2.getText().toString() + "\n" +
                        "*Overdue Collection*: " + txt_overdueColl1.getText().toString() + "" + txt_overdueColl2.getText().toString() + "\n" +
                        "*0-30*: " + txt_overdueColl30.getText().toString() + "" + txt_overdueCollAmt30.getText().toString() + "\n" +
                        "*30-45*: " + txt_overdueColl45.getText().toString() + "" + txt_overdueCollAmt45.getText().toString() +/*" L" +*/ "\n" +
                        "*45-90*: " + txt_overdueColl90.getText().toString() + "" + txt_overdueCollAmt90.getText().toString() +/*" L" + */"\n" +
                        "*90+*: " + txt_overdueCollabove.getText().toString() + "" + txt_overdueCollAmtabove.getText().toString() +/*" L" +*/ "\n\n" +
                        "*Visit*: " + txt_visit.getText().toString() + " " + txt_visitTime.getText().toString() + "\n" +
                        "*Telephone*: " + txt_telephn.getText().toString() + "- " + txt_telephnTime.getText().toString() + "\n" +
                        "*Email*: " + txt_email.getText().toString() + "" + txt_emailTime.getText().toString() + "\n" +
                        "*Total Time*: " + totalTime + "\n\n" +
                        fullString1;


                whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);
                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(CRMDayEndReport.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if (ut.isNet(CRMDayEndReport.this)) {
                    Toast.makeText(CRMDayEndReport.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {*/

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); /*image/*,*/
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String msgDate = "CRM Day End Report " + selectedDate;
                i.putExtra(Intent.EXTRA_SUBJECT, msgDate);
                String details = "", details1 = "";
                StringBuilder fullString1 = new StringBuilder();
                if (crmDayEndReportDetailsBeanArrayList.size() != 0) {
                    String prospectName = "-", person = "-", followupType = "-", outCome = "-", reason = "-", time = "-", duration = "-",
                            callPurposeDesc = "-", historyNotes = "";

                    for (int j = 0; j < crmDayEndReportDetailsBeanArrayList.size(); j++) {
                        prospectName = crmDayEndReportDetailsBeanArrayList.get(j).getFirmname().trim();
                        person = crmDayEndReportDetailsBeanArrayList.get(j).getContactName().trim();
                        followupType = crmDayEndReportDetailsBeanArrayList.get(j).getCallPurposeDesc().trim();
                        outCome = crmDayEndReportDetailsBeanArrayList.get(j).getOutcome().trim();
                        reason = crmDayEndReportDetailsBeanArrayList.get(j).getReasonDescription().trim();
                        time = crmDayEndReportDetailsBeanArrayList.get(j).getSchTime().trim();

                        try{
                            String[] split1;
                            String[] splitTime = new String[0];
                            if(time.contains("am") || time.contains("pm")||time.contains("AM") || time.contains("PM")){
                                split1 = time.split(" ");
                                splitTime = split1[0].split(":");
                            }else{
                                splitTime = time.split(":");
                            }

                            // int h  = Integer.parseInt(splitTime[0]);
                            if (!time.equals("")) {
                                time = updateTime(Integer.parseInt(splitTime[0].trim()), Integer.parseInt(splitTime[1].trim())).trim();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        duration = crmDayEndReportDetailsBeanArrayList.get(j).getTotalHoursSpent().trim();
                        callPurposeDesc = crmDayEndReportDetailsBeanArrayList.get(j).getCallPurposeDesc().trim();
                        historyNotes = crmDayEndReportDetailsBeanArrayList.get(j).getHistorynotes().trim();

                        if (prospectName.equals(null) || prospectName.equals("null") || prospectName.equals("")) {
                            prospectName = "-";
                        }
                        if (person.equals(null) || person.equals("null") || person.equals("")) {
                            person = "-";
                        }
                        if (followupType.equals(null) || followupType.equals("null") || followupType.equals("")) {
                            followupType = "";
                        }
                        if (outCome.equals(null) || outCome.equals("null") || outCome.equals("")) {
                            outCome = "-";
                        }
                        if (reason.equals(null) || reason.equals("null") || reason.equals("")) {
                            reason = "-";
                        }
                        if (time.equals(null) || time.equals("null") || time.equals("")) {
                            time = "-";
                        }
                        if (duration.equals(null) || duration.equals("null") || duration.equals("")) {
                            duration = "-";
                        }

                        String company = "\n" + "      Vritti Solutions Ltd      " + "\n\n";


                        Spanned prospect = HtmlCompat.fromHtml(context, "<b>" + prospectName + "</b>", 0);

/*SARHAAN PETROLEUMTelephone by at 9:00 AM for duration of 03:30 with Riaz Mulani .Outcome is Call Again because of
'history notes'  and reason is -
Client busy*/
                        String reasonappend = "";

                        if (outCome.equalsIgnoreCase("Call Again") ||
                                outCome.equalsIgnoreCase("Call Close without Order") ||
                                outCome.equalsIgnoreCase("Order Received") ||
                                outCome.equalsIgnoreCase("Reschedule") ||
                                outCome.equalsIgnoreCase("Order Lost") ||
                                outCome.equalsIgnoreCase("Demo Reschedule") ||
                                outCome.equalsIgnoreCase("Transfer To BOE") ||
                                outCome.equalsIgnoreCase("Customer will Call")) {
                            reasonappend = " because " + reason;
                        } else {
                            reasonappend = ".";
                        }


                        if (reason.equals("-")) {
                            if (historyNotes.equalsIgnoreCase("null")||historyNotes.equalsIgnoreCase("")||historyNotes==null) {

                                details1 = "\n" + prospect + " " + followupType + " " + "at " + time + " for duration of " + duration + " with " + person + " . Outcome is "
                                        + outCome + reasonappend + "\n";
                            }else {
                                details1 = "\n" + prospect + " " + followupType + " " + "at " + time + " for duration of " + duration + " with " + person + " . Outcome is "
                                        + outCome + reasonappend + "\n" + "Notes: " + historyNotes + "\n";
                            }
                        } else {
                           /* details1 = "\n" + prospect + " " +followupType + " "+"at " + time + " for duration of " + duration + " with " + person + " . Outcome is "
                                    + outCome + reasonappend + "\n"+"Reason: " + historyNotes+"\n";

*/

                            if (historyNotes.equalsIgnoreCase("null")||historyNotes.equalsIgnoreCase("")||historyNotes==null) {

                                details1 = "\n" + prospect + " " + followupType + " " + "at " + time + " for duration of " + duration + " with " + person + " . Outcome is "
                                        + outCome + reasonappend + "\n";
                            }else {
                                details1 = "\n" + prospect + " " + followupType + " " + "at " + time + " for duration of " + duration + " with " + person + " . Outcome is "
                                        + outCome + reasonappend + "\n" + "Notes: " + historyNotes + "\n";
                            }

                        }

                        StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
                        SpannableStringBuilder sb1 = new SpannableStringBuilder(details1);
                        int start = details1.indexOf(details1);
                        int end = start + details1.length();
                        sb1.setSpan(boldStyle, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

                        fullString1.append(details1);

                    }


                    // details1 = new Gson().toJson(new CRMDayEndReportDetailsBean(details));
                    //  details1.replaceAll("\\{", "");
                    //   details1.replaceAll("\\}", "");
                }

                String dayDt = formateDateFromstring("yyyy-MM-dd", "EEEE, dd-MMM-yyyy", selectedDate);


                Calendar calendar = new GregorianCalendar(2021, 01, 18); // Note that Month value is 0-based. e.g., 0 for January.
                int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                final SpannableString out0 = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out0.setSpan(boldSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
              /*  Collection: 10/19.90 L
                Overdue Collection: 10/19.00 L
                0-30: 0/0.00 L
                31-45: 0/0.00 L
                46-90: 0/0 .00 L
                91+: 0/0.00 L*/
/*
                SpannableString ss = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 21, 29,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

                String habitnumber = "<b>" + UserName + "</b> ";
                //  tv.setText(Html.fromHtml(habitnumber ));

                String msg = UserName + "\n" + dayDt + "\n" + "\n\n" + "Total: " + txt_total.getText().toString() + "\n" +
                        "Overdue: " + txt_overdue.getText().toString() + "\n\n" +
                        "New Prospect: " + txt_newProspect.getText().toString() + "\n" +
                        "New Opportunities: " + txt_newOpp.getText().toString() + "\n\n" +
                        "Order Received: " + txt_orderRec1.getText().toString() + "" + txt_orderRec2.getText().toString() + "\n" +
                        "Order Lost: " + txt_orderLost1.getText().toString() + "" + txt_orderLost2.getText().toString() + "\n\n" +
                        "Collection: " + txt_collection1.getText().toString() + "" + txt_collection2.getText().toString() + "\n" +
                        "Overdue Collection: " + txt_overdueColl1.getText().toString() + "" + txt_overdueColl2.getText().toString() + "\n" +
                        "0-30: " + txt_overdueColl30.getText().toString() + "" + txt_overdueCollAmt30.getText().toString() + "\n" +
                        "30-45: " + txt_overdueColl45.getText().toString() + "" + txt_overdueCollAmt45.getText().toString() +/*" L" +*/ "\n" +
                        "45-90: " + txt_overdueColl90.getText().toString() + "" + txt_overdueCollAmt90.getText().toString() +/*" L" + */"\n" +
                        "90+: " + txt_overdueCollabove.getText().toString() + "" + txt_overdueCollAmtabove.getText().toString() +/*" L" +*/ "\n\n" +
                        "Visit: " + txt_visit.getText().toString() + " " + txt_visitTime.getText().toString() + "\n" +
                        "Telephone: " + txt_telephn.getText().toString() + "- " + txt_telephnTime.getText().toString() + "\n" +
                        "Email: " + txt_email.getText().toString() + "" + txt_emailTime.getText().toString() + "\n" +
                        "Total Time: " + totalTime + "\n\n" +
                        fullString1;

                String msg1 = "*" + UserName + "*" + "\n" + "*" + dayDt + "* \n" + "\n\n" + "*Total*: " + txt_total.getText().toString() + "\n" +
                        "*Overdue*: " + txt_overdue.getText().toString() + "\n\n" +
                        "*New Prospect*: " + txt_newProspect.getText().toString() + "\n" +
                        "*New Opportunities*: " + txt_newOpp.getText().toString() + "\n\n" +
                        "Order Received: " + txt_orderRec1.getText().toString() + "" + txt_orderRec2.getText().toString() + "\n" +
                        "Order Lost: " + txt_orderLost1.getText().toString() + "" + txt_orderLost2.getText().toString() + "\n\n" +
                        "Collection: " + txt_collection1.getText().toString() + "" + txt_collection2.getText().toString() + " L" + "\n" +
                        "Overdue Collection: " + txt_overdueColl1.getText().toString() + "" + txt_overdueColl2.getText().toString() + " L" + "\n" +
                        "0-30: " + txt_overdueColl30.getText().toString() + "" + txt_overdueCollAmt30.getText().toString() + "\n" +
                        "30-45: " + txt_overdueColl45.getText().toString() + "" + txt_overdueCollAmt45.getText().toString() +/*" L" +*/ "\n" +
                        "45-90: " + txt_overdueColl90.getText().toString() + "" + txt_overdueCollAmt90.getText().toString() +/*" L" + */"\n" +
                        "+91: " + txt_overdueCollabove.getText().toString() + "" + txt_overdueCollAmtabove.getText().toString() +/*" L" +*/ "\n\n" +
                        "Visit: " + txt_visit.getText().toString() + "" + txt_visitTime.getText().toString() + "\n" +
                        "Telephone: " + txt_telephn.getText().toString() + "" + txt_telephnTime.getText().toString() + "\n" +
                        "Email: " + txt_email.getText().toString() + "" + txt_emailTime.getText().toString() + "\n\n" +
                        fullString1;

                //  String url1 = "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.orderbilling'>https://play.google.com/store/apps/details?id=com.vritti.orderbilling</a>";

                i.putExtra(Intent.EXTRA_TEXT, msg);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(i, "Choose one to Share link!"));

            }
            // }
        });
    }

    private void showReport() {
        if (!(selectedDate == null || selectedDate.equals(""))) {
            ln_summary.setVisibility(View.GONE);

            if (ut.isNet(CRMDayEndReport.this)) {
                new StartSession(CRMDayEndReport.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DayEndReportDetails().execute(selectedDate);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(CRMDayEndReport.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(CRMDayEndReport.this, "Please select date", Toast.LENGTH_SHORT).show();
        }


    }


    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if (hours > 12) {
                hours = hours - 12;
                timeSet = "AM";
            } else if (hours == 12) {
                timeSet = "AM";
            } else {
                timeSet = "PM";
            }

        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
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


    private class DayEndReportDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";
        String date1 = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            date1 = params[0];
            /*2020-02-05*/

            String url = CompanyURL + WebUrlClass.api_POSTCrmDayReport + "?PickDate=" + date1+"&UID="+UserMasterId;
           // String url = CompanyURL + WebUrlClass.api_POSTCrmDayReport + "?PickDate=" + date1+"&UID=6977a444-c205-4348-8a01-90843192d883";
            res = ut.OpenConnection(url, CRMDayEndReport.this);

            try {
                if (res != null) {
                    // response = res.toString().replaceAll(rplc,"");
                    response = res.toString().replaceAll("\\\\", "");
                    // response = res.toString().replaceAll("rn","");
                    //     response = response.substring(1, response.length() - 1);
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
            progress_bar.setVisibility(View.GONE);
            if (!(response.equals("error") || response.contains("error") || response.equals(""))) {
                ln_summary.setVisibility(View.VISIBLE);
                img_share.setVisibility(View.VISIBLE);
                img_whatsapp.setVisibility(View.VISIBLE);
                try {
                    JSONObject mainJsonObject = new JSONObject(response);

                    JSONArray summaryCountJsonArr = mainJsonObject.getJSONArray("CRMList1");
                    int summCnt = summaryCountJsonArr.length();
                    crmDayEndReportBeanArrayList.clear();

                    for (int i = 0; i < summaryCountJsonArr.length(); i++) {
                        JSONObject countSummaryJSONObj = summaryCountJsonArr.getJSONObject(i);

                        CRMDayEndReportBean crmDayEndReportBean = new CRMDayEndReportBean();

                        crmDayEndReportBean.setTotal(countSummaryJSONObj.getString("Total"));
                        crmDayEndReportBean.setOverDue(countSummaryJSONObj.getString("OverDue"));
                        crmDayEndReportBean.setNewProspect(countSummaryJSONObj.getString("NewProspect"));
                        crmDayEndReportBean.setNewOpportunities(countSummaryJSONObj.getString("NewOpportunities"));
                        crmDayEndReportBean.setOrderReceivedCount(countSummaryJSONObj.getString("OrderReceivedCount"));
                        crmDayEndReportBean.setOrderReceivedValue(countSummaryJSONObj.getString("OrderReceivedValue"));
                        crmDayEndReportBean.setOrderLostCount(countSummaryJSONObj.getString("OrderLostCount"));
                        crmDayEndReportBean.setOrderPOValue(countSummaryJSONObj.getString("OrderPOValue"));
                        crmDayEndReportBean.setCollectionCount(countSummaryJSONObj.getString("CollectionCount"));
                        crmDayEndReportBean.setCollectionAmount(countSummaryJSONObj.getString("CollectionAmount"));
                        crmDayEndReportBean.setCollectionCount(countSummaryJSONObj.getString("OverdueCollectionCount"));
                        crmDayEndReportBean.setCollectionAmount(countSummaryJSONObj.getString("OverdueCollectionAmount"));

                        crmDayEndReportBean.setOverdueCollectionCountfor30days(countSummaryJSONObj.getString("OverdueCollectionCountfor30days"));
                        crmDayEndReportBean.setOverdueCollectionCountfor45Days(countSummaryJSONObj.getString("OverdueCollectionCountfor45days"));
                        crmDayEndReportBean.setOverdueCollectionCountfor90days(countSummaryJSONObj.getString("OverdueCollectionCountfor90days"));
                        crmDayEndReportBean.setOverdueCollectionCountfor90abvdays(countSummaryJSONObj.getString("OverdueCollectionCountabove90days"));

                        crmDayEndReportBean.setOverdueCollectionAmountfor30days(countSummaryJSONObj.getString("OverdueCollectionamountfor30days"));
                        crmDayEndReportBean.setOverdueCollectionAmountfor45days(countSummaryJSONObj.getString("OverdueCollectionAmountfor45days"));
                        crmDayEndReportBean.setOverdueCollectionAmountfor90days(countSummaryJSONObj.getString("OverdueCollectionAmountfor90days"));
                        crmDayEndReportBean.setOverdueCollectionAmountfor90abvdays(countSummaryJSONObj.getString("OverdueCollectionAmountabove90days"));


                        crmDayEndReportBeanArrayList.add(crmDayEndReportBean);

                        if (countSummaryJSONObj.get("Total") == null || countSummaryJSONObj.getString("Total").equals("null")) {
                            txt_total.setText("-");
                        } else {
                            txt_total.setText(countSummaryJSONObj.getString("Total"));
                        }
                        if (countSummaryJSONObj.get("OverDue") == null || countSummaryJSONObj.getString("OverDue").equals("null")) {
                            txt_overdue.setText("-");
                        } else {
                            txt_overdue.setText(countSummaryJSONObj.getString("OverDue"));
                        }
                        if (countSummaryJSONObj.get("NewProspect") == null || countSummaryJSONObj.getString("NewProspect").equals("null")) {
                            txt_newProspect.setText("-");
                        } else {
                            txt_newProspect.setText(countSummaryJSONObj.getString("NewProspect"));
                        }
                        if (countSummaryJSONObj.get("NewOpportunities") == null || countSummaryJSONObj.getString("NewOpportunities").equals("null")) {
                            txt_newOpp.setText("-");
                        } else {
                            txt_newOpp.setText(countSummaryJSONObj.getString("NewOpportunities"));
                        }

                        if (countSummaryJSONObj.get("OrderReceivedCount") == null || countSummaryJSONObj.getString("OrderReceivedCount").equals("null")) {
                            txt_orderRec1.setText("-");
                        } else {
                            txt_orderRec1.setText(countSummaryJSONObj.getString("OrderReceivedCount"));
                        }
                        if (countSummaryJSONObj.get("OrderReceivedValue") == null ||
                                countSummaryJSONObj.getString("OrderReceivedValue").equals("null") ||
                                countSummaryJSONObj.getString("OrderReceivedValue").equals("0")) {
                            txt_orderRec2.setText("-");
                        } else {
                            txt_orderRec2.setText("/ " + countSummaryJSONObj.getString("OrderReceivedValue") + " L");
                        }
                        if (countSummaryJSONObj.get("OrderLostCount") == null || countSummaryJSONObj.getString("OrderLostCount").equals("null")) {
                            txt_orderLost1.setText("-");
                        } else {
                            txt_orderLost1.setText(countSummaryJSONObj.getString("OrderLostCount"));
                        }
                        if (countSummaryJSONObj.get("OrderPOValue") == null ||
                                countSummaryJSONObj.getString("OrderPOValue").equals("null")) {
                            txt_orderLost2.setText("-");
                        } else {
                            txt_orderLost2.setText("/ " + countSummaryJSONObj.getString("OrderPOValue") + " L");
                        }
                        if (countSummaryJSONObj.get("CollectionCount") == null || countSummaryJSONObj.getString("CollectionCount").equals("null")) {
                            txt_collection1.setText("-");
                        } else {
                            txt_collection1.setText(countSummaryJSONObj.getString("CollectionCount"));
                        }
                        if (countSummaryJSONObj.get("CollectionAmount") == null || countSummaryJSONObj.getString("CollectionAmount").equals("null")) {
                            txt_collection2.setText("-");
                        } else {
                            txt_collection2.setText("/ " + countSummaryJSONObj.getString("CollectionAmount") + " L");
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionCount") == null || countSummaryJSONObj.getString("OverdueCollectionCount").equals("null")) {
                            txt_overdueColl1.setText("-");
                        } else {
                            txt_overdueColl1.setText(countSummaryJSONObj.getString("OverdueCollectionCount"));
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionAmount") == null || countSummaryJSONObj.getString("OverdueCollectionAmount").equals("null")) {
                            txt_overdueColl2.setText("-");
                        } else {
                            txt_overdueColl2.setText("/ " + countSummaryJSONObj.getString("OverdueCollectionAmount") + " L");
                        }
                         /*  "OverdueCollectionCountfor30days": 1,
                                "OverdueCollectionamountfor30days": "0.012",
                                "OverdueCollectionCountfor45days": 1,
                                "OverdueCollectionAmountfor45days": "0.012",
                                "OverdueCollectionCountfor90days": 3,
                                "OverdueCollectionAmountfor90days": "0.681",
                                "OverdueCollectionCountabove90days": 5,
                                "OverdueCollectionAmountabove90days": "1.203"*/

                        if (countSummaryJSONObj.get("OverdueCollectionCountfor30days") == null || countSummaryJSONObj.getString("OverdueCollectionCountfor30days").equals("null")||countSummaryJSONObj.getString("OverdueCollectionCountfor30days").equals("0")) {
                            txt_overdueColl30.setText("-");
                        } else {
                            txt_overdueColl30.setText(countSummaryJSONObj.getString("OverdueCollectionCountfor30days"));
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionamountfor30days") == null || countSummaryJSONObj.getString("OverdueCollectionamountfor30days").equals("null")||countSummaryJSONObj.getString("OverdueCollectionCountfor30days").equals("0.000")) {
                            txt_overdueCollAmt30.setText("-");
                        } else {
                            txt_overdueCollAmt30.setText("/ " + countSummaryJSONObj.getString("OverdueCollectionamountfor30days") + " L");
                        }

                        if (countSummaryJSONObj.get("OverdueCollectionCountfor45days") == null || countSummaryJSONObj.getString("OverdueCollectionCountfor45days").equals("null")) {
                            txt_overdueColl45.setText("-");
                        } else {
                            txt_overdueColl45.setText(countSummaryJSONObj.getString("OverdueCollectionCountfor45days"));
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionAmountfor45days") == null || countSummaryJSONObj.getString("OverdueCollectionAmountfor45days").equals("null")) {
                            txt_overdueCollAmt45.setText("-");
                        } else {
                            txt_overdueCollAmt45.setText("/ " + countSummaryJSONObj.getString("OverdueCollectionAmountfor45days") + " L");
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionCountfor90days") == null || countSummaryJSONObj.getString("OverdueCollectionCountfor90days").equals("null")) {
                            txt_overdueColl90.setText("-");
                        } else {
                            txt_overdueColl90.setText(countSummaryJSONObj.getString("OverdueCollectionCountfor90days"));
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionAmountfor90days") == null || countSummaryJSONObj.getString("OverdueCollectionAmountfor90days").equals("null")) {
                            txt_overdueCollAmt90.setText("-");
                        } else {
                            txt_overdueCollAmt90.setText("/ " + countSummaryJSONObj.getString("OverdueCollectionAmountfor90days") + " L");
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionCountabove90days") == null || countSummaryJSONObj.getString("OverdueCollectionCountabove90days").equals("null")) {
                            txt_overdueCollabove.setText("-");
                        } else {
                            txt_overdueCollabove.setText(countSummaryJSONObj.getString("OverdueCollectionCountabove90days"));
                        }
                        if (countSummaryJSONObj.get("OverdueCollectionAmountabove90days") == null || countSummaryJSONObj.getString("OverdueCollectionAmountabove90days").equals("null")) {
                            txt_overdueCollAmtabove.setText("-");
                        } else {
                            txt_overdueCollAmtabove.setText("/ " + countSummaryJSONObj.getString("OverdueCollectionAmountabove90days") + " L");
                        }


                    }

                    JSONArray summaryCountJsonArr1 = mainJsonObject.getJSONArray("CRMList2");
                    int summCnt1 = summaryCountJsonArr1.length();

                    for (int i = 0; i < summaryCountJsonArr1.length(); i++) {
                        JSONObject countJSONObj = summaryCountJsonArr1.getJSONObject(i);

                        String visitTimeVal = countJSONObj.get("CountForVisit").toString().trim();

                        if (visitTimeVal == null || visitTimeVal.equals("null") || visitTimeVal.equals("")) {
                            txt_visit.setText("-");
                        } else {
                            txt_visit.setText(countJSONObj.getString("CountForVisit"));
                        }
                        String visit = countJSONObj.get("MMVisit").toString().trim();
                        if (visit == null || visit.equals("null") || visit.equals("")) {
                            txt_visitTime.setText("-");
                        } else {
                            txt_visitTime.setText("- "+countJSONObj.getString("MMVisit"));
                        }

                        String teleVal = countJSONObj.get("MMTele").toString().trim();
                        if (teleVal == null || teleVal.equals("null") || teleVal.equals("")) {
                            txt_telephnTime.setText("-");
                        } else {
                            txt_telephnTime.setText(countJSONObj.getString("MMTele"));
                        }

                        String telephnVal = countJSONObj.get("Countfortele").toString().trim();
                        if (telephnVal == null || telephnVal.equals("null") || telephnVal.equals("")) {
                            txt_telephn.setText("-");
                        } else {
                            txt_telephn.setText(countJSONObj.getString("Countfortele"));
                        }

                        String emailVal = countJSONObj.get("CountforEmail").toString().trim();
                        if (emailVal == null || emailVal.equals("null") || emailVal.equals("")) {
                            txt_email.setText("-");
                        } else {
                            txt_email.setText(countJSONObj.getString("CountforEmail"));
                        }

                        String emailTimeVal = countJSONObj.get("MMEmail").toString().trim();
                        if (emailTimeVal == null || emailTimeVal.equals("null") || emailTimeVal.equals("")) {
                            txt_emailTime.setText("-");
                        } else {
                            txt_emailTime.setText(countJSONObj.getString("MMEmail"));
                        }

                        totalTime = countJSONObj.getString("TotalTime");
                        if (totalTime == null || totalTime.equals("null") || totalTime.equals("0")) {
                            totalTime = "-";
                        }

                    }


                    JSONArray summaryDetailsJsonArr = mainJsonObject.getJSONArray("CRMList3");
                    int summCnt2 = summaryDetailsJsonArr.length();
                    crmDayEndReportDetailsBeanArrayList.clear();
                    for (int i = 0; i < summaryDetailsJsonArr.length(); i++) {
                        JSONObject jsonObject = summaryDetailsJsonArr.getJSONObject(i);

                        CRMDayEndReportDetailsBean crmDayEndReportDetailsBean = new CRMDayEndReportDetailsBean();


                        crmDayEndReportDetailsBean.setFirmname(jsonObject.getString("firmname"));
                        String totalHrSpent = jsonObject.getString("TotalHoursSpent").trim();
                        if (totalHrSpent.equals("0") || totalHrSpent.equals("00 :")) {
                            totalHrSpent = "00:00";
                        }
                        crmDayEndReportDetailsBean.setTotalHoursSpent(totalHrSpent);
                        crmDayEndReportDetailsBean.setSchTime(jsonObject.getString("SchTime"));
                        crmDayEndReportDetailsBean.setCallPurposeDesc(jsonObject.getString("CallPurposeDesc"));
                        crmDayEndReportDetailsBean.setHistorynotes(jsonObject.getString("Historynotes"));
                        crmDayEndReportDetailsBean.setOutcome(jsonObject.getString("Outcome"));
                        crmDayEndReportDetailsBean.setReasonDescription(jsonObject.getString("ReasonDescription"));
                        crmDayEndReportDetailsBean.setCallPurposeDesc1(jsonObject.getString("CallPurposeDesc"));
                        crmDayEndReportDetailsBean.setContactName(jsonObject.getString("ContactName"));
                        crmDayEndReportDetailsBean.setHistorynotes(jsonObject.getString("Historynotes"));
                        crmDayEndReportDetailsBean.setInitiatedBy(Integer.parseInt(jsonObject.getString("InitiatedBy")));

                        crmDayEndReportDetailsBeanArrayList.add(crmDayEndReportDetailsBean);

                    }
                    if (crmDayEndReportDetailsBeanArrayList.size() != 0) {
                      /*  if (!(crmDayEndReportDetailsBeanArrayList.get(0).getCountforVisit() == null ||
                                crmDayEndReportDetailsBeanArrayList.get(0).getCountforVisit().equals("null"))) {
                            txt_visit.setText(crmDayEndReportDetailsBeanArrayList.get(0).getCountforVisit());
                        } else {
                            txt_visit.setText("0");
                        }*/

                        list_reportDetails.setVisibility(View.VISIBLE);
                        crmDayEndReportAdapter = new CRMDayEndReportAdapter(CRMDayEndReport.this, crmDayEndReportDetailsBeanArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        list_reportDetails.setLayoutManager(mLayoutManager);
                        list_reportDetails.setItemAnimator(new DefaultItemAnimator());
                        list_reportDetails.setAdapter(crmDayEndReportAdapter);
                    } else {
                        list_reportDetails.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(CRMDayEndReport.this, "No Record present", Toast.LENGTH_SHORT).show();
                ln_summary.setVisibility(View.GONE);
                img_share.setVisibility(View.GONE);
                img_whatsapp.setVisibility(View.GONE);
            }
        }
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}
