package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.CRMDayEndReportAdapter;
import com.vritti.crm.bean.CRMDayEndReportBean;
import com.vritti.crm.bean.CRMDayEndReportDetailsBean;
import com.vritti.crm.bean.City;
import com.vritti.crm.bean.Target;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

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

public class TargetAchievmentReport extends AppCompatActivity {
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
    ArrayList<Target> targetArrayList;
    TextView txt_target, txt_achievment, txt_achievment_percent, txt_backlog,txt_claim,txt_incentive,
            today_orderbooking,today_collection,
            txt_overdue_call,txt_cold_call,txt_hot_call,txt_order_till_date,
            txt_order_value,txt_collection,txt_over_collection,txt_order_no,
            txt_tot_outstanding,txt_claim_date,txt_today_opp,txt_plan,txt_team_value,txt_team_exp_order_no;
    ImageView img_whatsapp;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    ScrollView scroll;
    private ProgressDialog progressDialog;
    public double totalTargets,AchievementAsOnDates,backlogs,OrderValues,CollectionAmounts,
            OrderValueAchievementAsOnDat,Collectionamount,AchievementinPerCent,ClaimAount,Incentive,ClaimDate,Outstanding,TeamOrderValues;
    private String TotalOverdueCount="",TotalHOTOverdueCount="",TotalColdOverdueCount="",
            OrderCountAchievementAsOnDate="",TotalCollectionoverdue="",TodayOpp="";
    private String TodaysExpOrderNo="",TeamTodaysExpOrderNo;
    private String CityName="",Cityname="";
    private String[] user5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_day_end_report_acitvity_v1);

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
        progressDialog = new ProgressDialog(TargetAchievmentReport.this);

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
            txt_title.setText("Target and Achievement");
        }




    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);




        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txt_pickupdate = findViewById(R.id.txt_pickupdate);
        txt_target=findViewById(R.id.txt_target);
        txt_achievment=findViewById(R.id.txt_achievment);
        txt_achievment_percent=findViewById(R.id.txt_achievment_percent);
        txt_backlog=findViewById(R.id.txt_backlog);
        today_orderbooking=findViewById(R.id.today_orderbooking);
        today_collection=findViewById(R.id.today_collection);
        txt_overdue_call=findViewById(R.id.txt_overdue_call);
        txt_cold_call=findViewById(R.id.txt_cold_call);
        txt_hot_call=findViewById(R.id.txt_hot_call);
        txt_order_till_date=findViewById(R.id.txt_order_till_date);
        txt_order_value=findViewById(R.id.txt_order_value);
        txt_collection=findViewById(R.id.txt_collection);
        txt_incentive=findViewById(R.id.txt_incentive);
        txt_claim=findViewById(R.id.txt_claim);
        txt_over_collection=findViewById(R.id.txt_over_collection);
        txt_order_no=findViewById(R.id.txt_order_no);
        txt_tot_outstanding=findViewById(R.id.txt_tot_outstanding);
        txt_claim_date=findViewById(R.id.txt_claim_date);
        txt_today_opp=findViewById(R.id.txt_today_opp);
        txt_plan=findViewById(R.id.txt_plan);
        txt_team_value=findViewById(R.id.txt_team_value);
        txt_team_exp_order_no=findViewById(R.id.txt_team_exp_order_no);
        img_whatsapp = findViewById(R.id.img_whatsapp);
        img_share = findViewById(R.id.img_share);
        targetArrayList=new ArrayList<>();
        progress_bar = findViewById(R.id.progressbar);
        scroll = findViewById(R.id.scroll);





    }

    private void setListner() {

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TargetAchievmentReport.this,
                        new DatePickerDialog.OnDateSetListener() {

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

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();

            }
        });


        img_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                StringBuilder fullString1 = new StringBuilder();

                String dayDt = formateDateFromstring("yyyy-MM-dd", "EEEE, dd-MMM-yyyy", selectedDate);


                Calendar calendar = new GregorianCalendar(2021, 01, 18); // Note that Month value is 0-based. e.g., 0 for January.
                int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                final SpannableString out0 = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out0.setSpan(boldSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                String msg = "*" + UserName + "*" + "\n" + "*" + dayDt + "* \n" +"*Reporting Place*: " + Cityname + "\n\n"
                        +"*Total Target*: " + formatLakh(totalTargets) + "\n" +
                        "*Achievement Value*: " + formatLakh(AchievementAsOnDates) + "&" + AchievementinPerCent + "%"+"\n" +
                        "*Backlog*: " + formatLakh(backlogs) + "\n\n" +
                        "*Today's Opportunity*: " + TodayOpp + "\n" +
                        "*Today's Expected Order No.*: " + TodaysExpOrderNo + "\n" +
                        "*Expected Order Value*: " + formatLakh(OrderValues) + "\n" +
                        "*Expected Collection*: " + formatLakh(CollectionAmounts) + "\n\n" +
                        "*Team Expected Order No.*: " + TeamTodaysExpOrderNo + "\n" +
                        "*Team Expected Value*: " + formatLakh(TeamOrderValues) + "\n\n" +
                        "*Total Overdue Opportunity*: " + TotalOverdueCount + "\n" +
                        "*Total Overdue Collection Opportunity*: " + TotalCollectionoverdue + "\n" +
                        "*Total Overdue Hot Opportunity*: " + TotalHOTOverdueCount + "\n\n" +
                        "*Order Count Till Date*: " + OrderCountAchievementAsOnDate + "\n" +
                        "*Order Value*: " + formatLakh(OrderValueAchievementAsOnDat) + "\n" +
                        "*Collection*: " + formatLakh(Collectionamount) + "\n"+
                        "*Total Outstanding*: " + formatLakh(Outstanding) + "\n"+
                        "*Earned Incentive*: " + formatLakh(Incentive) + "\n"+
                        "*Month Claim*: " + formatLakh(ClaimDate) + "\n" +
                        "*Total Claim Till Date :*: " + formatLakh(ClaimAount)
                        ;
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, msg);


                try {
                    context.startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(TargetAchievmentReport.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); /*image/*,*/
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String msgDate = "Target and Achievement Report " + selectedDate;
                i.putExtra(Intent.EXTRA_SUBJECT, msgDate);
                String details = "", details1 = "";
                StringBuilder fullString1 = new StringBuilder();

                String dayDt = formateDateFromstring("yyyy-MM-dd", "EEEE, dd-MMM-yyyy", selectedDate);


                Calendar calendar = new GregorianCalendar(2021, 01, 18); // Note that Month value is 0-based. e.g., 0 for January.
                int reslut = calendar.get(Calendar.DAY_OF_WEEK);
                final SpannableString out0 = new SpannableString(UserName);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out0.setSpan(boldSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);



                String msg = "*" + UserName + "*" + "\n" + "*" + dayDt + "* \n" +"*Reporting Place*: " + Cityname + "\n\n"
                        +"*Total Target*: " + formatLakh(totalTargets) + "\n" +
                        "*Achievement Value*: " + formatLakh(AchievementAsOnDates) + "&" + AchievementinPerCent + "%"+"\n" +
                        "*Backlog*: " + formatLakh(backlogs) + "\n\n" +
                        "*Today's Opportunity*: " + TodayOpp + "\n" +
                        "*Today's Expected Order No.*: " + TodaysExpOrderNo + "\n" +
                        "*Expected Order Value*: " + formatLakh(OrderValues) + "\n" +
                        "*Expected Collection*: " + formatLakh(CollectionAmounts) + "\n\n" +
                        "*Team Expected Order No.*: " + TeamTodaysExpOrderNo + "\n" +
                        "*Team Expected Value*: " + formatLakh(TeamOrderValues) + "\n\n" +
                        "*Total Overdue Opportunity*: " + TotalOverdueCount + "\n" +
                        "*Total Overdue Collection Opportunity*: " + TotalCollectionoverdue + "\n" +
                        "*Total Overdue Hot Opportunity*: " + TotalHOTOverdueCount + "\n\n" +
                        "*Order Count Till Date*: " + OrderCountAchievementAsOnDate + "\n" +
                        "*Order Value*: " + formatLakh(OrderValueAchievementAsOnDat) + "\n" +
                        "*Collection*: " + formatLakh(Collectionamount) + "\n"+
                        "*Total Outstanding*: " + formatLakh(Outstanding) + "\n"+
                        "*Earned Incentive*: " + formatLakh(Incentive) + "\n"+
                        "*Month Claim*: " + formatLakh(ClaimDate) + "\n" +
                        "*Total Claim Till Date :*: " + formatLakh(ClaimAount)
                        ;
                i.putExtra(Intent.EXTRA_TEXT, msg);
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(i, "Choose one to Share link!"));
            }
        });

    }

    private void showReport() {
        if (!(selectedDate == null || selectedDate.equals(""))) {

            if (ut.isNet(TargetAchievmentReport.this)) {
                progressDialog.setCancelable(true);
                if (!isFinishing()) {
                    progressDialog.show();
                }
                progressDialog.setContentView(R.layout.crm_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                new StartSession(TargetAchievmentReport.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DayEndReportDetails().execute(selectedDate);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(TargetAchievmentReport.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(TargetAchievmentReport.this, "Please select date", Toast.LENGTH_SHORT).show();
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
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            date1 = params[0];
            /*2020-02-05*/

            String url = CompanyURL + WebUrlClass.GetCallDetails + "?UserMasterId=" + UserMasterId + "&Date=" + selectedDate;
            // String url = CompanyURL + WebUrlClass.GetCallDetails + "?UserMasterId=fe66846d-6656-41b1-b3e0-2b2d20d71592&Date=" + selectedDate;
            res = ut.OpenConnection(url, TargetAchievmentReport.this);

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
            progressDialog.dismiss();
            if (!(response.equals("error") || response.contains("error") || response.equals(""))) {
                try {
                    JSONObject mainJsonObject = new JSONObject(response);
                    scroll.setVisibility(View.VISIBLE);
                    JSONArray TotalTarget = mainJsonObject.getJSONArray("TotalTarget");
                    if (TotalTarget.length() > 0) {
                        for (int i = 0; i < TotalTarget.length(); i++) {
                            JSONObject totaltarget = TotalTarget.getJSONObject(i);
                            String totalTarget=totaltarget.getString("TotalTarget");
                            if (totalTarget.length()>5){
                                totalTargets = Double.parseDouble(totalTarget);
                                double value = totalTargets/100000;
                                totalTargets=value;
                                txt_target.setText(String.format("%.2f", totalTargets));
                                // txt_target.setText(formatLakh(totalTargets));
                            }else {
                                totalTargets = Double.parseDouble(totalTarget);
                                txt_target.setText(String.format("%.2f", totalTargets));
                            }
                        }

                    }
                    JSONArray TotalAchievement = mainJsonObject.getJSONArray("TotalAchievement");
                    if (TotalAchievement.length() > 0) {
                        for (int i = 0; i < TotalAchievement.length(); i++) {
                            JSONObject Totalachievement = TotalAchievement.getJSONObject(i);
                            String AchievementAsOnDate=Totalachievement.getString("AchievementAsOnDate");
                            if (AchievementAsOnDate.length()>5){
                                AchievementAsOnDates=Double.parseDouble(AchievementAsOnDate);
                                AchievementAsOnDates =AchievementAsOnDates/100000;
                                txt_achievment.setText(String.format("%.2f", AchievementAsOnDates));
                            }else {
                                AchievementAsOnDates=Double.parseDouble(AchievementAsOnDate);
                                txt_achievment.setText(String.format("%.2f", AchievementAsOnDates));
                            }

                           /* String AchievementInPer=Totalachievement.getString("AchievementInPer");
                            double AchievementInPercent=Double.parseDouble(AchievementInPer);
                            txt_achievment_percent.setText(String.format("%.2f", AchievementInPercent));*/

                            //txt_achievment_percent.setText(AchievementInPer);
                        }

                    }
                    JSONArray Backlog = mainJsonObject.getJSONArray("Backlog");
                    if (Backlog.length() > 0) {
                        for (int i = 0; i < Backlog.length(); i++) {
                            JSONObject Totalachievement = Backlog.getJSONObject(i);
                            String backlog=Totalachievement.getString("Backlog");
                            if (backlog.length()>5){
                                backlogs=Double.parseDouble(backlog);
                                backlogs = backlogs/100000;
                                txt_backlog.setText(String.format("%.2f", backlogs));
                                // txt_backlog.setText(formatLakh(backlogs));
                            }else {
                                backlogs=Double.parseDouble(backlog);
                                txt_backlog.setText(String.format("%.2f", backlogs));
                            }
                        }
                    }

                    JSONArray TodaysExpectedOrderNo = mainJsonObject.getJSONArray("TodaysExpectedOrderNo");
                    if (TodaysExpectedOrderNo.length() > 0) {
                        for (int i = 0; i < TodaysExpectedOrderNo.length(); i++) {
                            JSONObject Totalachievement = TodaysExpectedOrderNo.getJSONObject(i);
                            TodaysExpOrderNo=Totalachievement.getString("TodaysExpOrderNo");
                            txt_order_no.setText(TodaysExpOrderNo);

                        }
                    }

                    JSONArray OrderBooking = mainJsonObject.getJSONArray("ExpectedOrderValue");
                    if (OrderBooking.length() > 0) {
                        for (int i = 0; i < OrderBooking.length(); i++) {
                            JSONObject Totalachievement = OrderBooking.getJSONObject(i);
                            String OrderValue=Totalachievement.getString("OrderValue");
                            if (OrderValue.length()>5){
                                OrderValues=Double.parseDouble(OrderValue);
                                double value = OrderValues/100000;
                                OrderValues=value;
                                today_orderbooking.setText(String.format("%.2f", OrderValues));
                                // today_orderbooking.setText(formatLakh(OrderValues));
                            }else {
                                OrderValues=Double.parseDouble(OrderValue);
                                today_orderbooking.setText(String.format("%.2f", OrderValues));
                            }
                        }
                    }
                    JSONArray TodaysCollection = mainJsonObject.getJSONArray("TodaysExpectedCollectionAmt");
                    if (TodaysCollection.length() > 0) {
                        for (int i = 0; i < TodaysCollection.length(); i++) {
                            JSONObject Totalachievement = TodaysCollection.getJSONObject(i);
                            String CollectionAmount=Totalachievement.getString("CollectionAmount");

                            if (CollectionAmount.length()>5){
                                CollectionAmounts=Double.parseDouble(CollectionAmount);
                                double value = CollectionAmounts/100000;
                                CollectionAmounts=value;
                                today_collection.setText(String.format("%.2f", CollectionAmounts));
                                // today_collection.setText(formatLakh(CollectionAmounts));
                            }else {
                                CollectionAmounts=Double.parseDouble(CollectionAmount);
                                today_collection.setText(String.format("%.2f", CollectionAmounts));
                            }


                        }
                    }
                    JSONArray TotalOverdue = mainJsonObject.getJSONArray("TotalOppoOverdue");
                    if (TotalOverdue.length() > 0) {
                        for (int i = 0; i < TotalOverdue.length(); i++) {
                            JSONObject Totalachievement = TotalOverdue.getJSONObject(i);
                            TotalOverdueCount=Totalachievement.getString("OverDueCount");
                            txt_overdue_call.setText(TotalOverdueCount);
                        }
                    }
                    JSONArray TotalHotOverdue = mainJsonObject.getJSONArray("TotalOppoHotOverdue");
                    if (TotalHotOverdue.length() > 0) {
                        for (int i = 0; i < TotalHotOverdue.length(); i++) {
                            JSONObject Totalachievement = TotalHotOverdue.getJSONObject(i);
                            TotalHOTOverdueCount=Totalachievement.getString("OverDueCount");
                            txt_hot_call.setText(TotalHOTOverdueCount);
                        }
                    }
                    JSONArray TotalColdOverdue = mainJsonObject.getJSONArray("TotalOppoColdOverdue");
                    if (TotalColdOverdue.length() > 0) {
                        for (int i = 0; i < TotalColdOverdue.length(); i++) {
                            JSONObject Totalachievement = TotalColdOverdue.getJSONObject(i);
                            TotalColdOverdueCount=Totalachievement.getString("OverDueCount");
                            txt_cold_call.setText(TotalColdOverdueCount);
                        }
                    }

                    JSONArray TotalCollectionOverdue = mainJsonObject.getJSONArray("TotalCollectionOverdue");
                    if (TotalCollectionOverdue.length() > 0) {
                        for (int i = 0; i < TotalCollectionOverdue.length(); i++) {
                            JSONObject Totalachievement = TotalCollectionOverdue.getJSONObject(i);
                            TotalCollectionoverdue=Totalachievement.getString("TotalCollectionOverdue");
                            txt_over_collection.setText(TotalCollectionoverdue);

                        }
                    }

                    JSONArray OrderCount = mainJsonObject.getJSONArray("OrderCount");
                    if (OrderCount.length() > 0) {
                        for (int i = 0; i < OrderCount.length(); i++) {
                            JSONObject Totalachievement = OrderCount.getJSONObject(i);
                            OrderCountAchievementAsOnDate=Totalachievement.getString("AchievementAsOnDate");
                            txt_order_till_date.setText(OrderCountAchievementAsOnDate);
                        }
                    }
                    JSONArray OrderValue = mainJsonObject.getJSONArray("OrderValue");
                    if (OrderValue.length() > 0) {
                        for (int i = 0; i < OrderValue.length(); i++) {
                            JSONObject Totalachievement = OrderValue.getJSONObject(i);
                            String OrderAchievementAsOnDate=Totalachievement.getString("AchievementAsOnDate");
                            if (OrderAchievementAsOnDate.length()>5){
                                OrderValueAchievementAsOnDat=Double.parseDouble(OrderAchievementAsOnDate);
                                double value = OrderValueAchievementAsOnDat/100000;
                                OrderValueAchievementAsOnDat=value;
                                txt_order_value.setText(String.format("%.2f", OrderValueAchievementAsOnDat));
                                // txt_order_value.setText(formatLakh(OrderValueAchievementAsOnDat));
                            }else {
                                OrderValueAchievementAsOnDat=Double.parseDouble(OrderAchievementAsOnDate);
                                txt_order_value.setText(String.format("%.2f", OrderValueAchievementAsOnDat));
                            }


                        }
                    }
                    JSONArray CollectionAmt = mainJsonObject.getJSONArray("CollectionAmt");
                    if (CollectionAmt.length() > 0) {
                        for (int i = 0; i < CollectionAmt.length(); i++) {
                            JSONObject Totalachievement = CollectionAmt.getJSONObject(i);
                            String CollectionAmount=Totalachievement.getString("CollectionAmount");

                            if (CollectionAmount.length()>5){
                                Collectionamount=Double.parseDouble(CollectionAmount);
                                double value = Collectionamount/100000;
                                Collectionamount=value;
                                txt_collection.setText(String.format("%.2f", Collectionamount));
                                //txt_collection.setText(formatLakh(Collectionamount));
                            }else {
                                Collectionamount=Double.parseDouble(CollectionAmount);
                                txt_collection.setText(String.format("%.2f", Collectionamount));
                            }
                        }
                    }
                    JSONArray AchievementinPer = mainJsonObject.getJSONArray("AchievementinPer");
                    if (AchievementinPer.length() > 0) {
                        for (int i = 0; i < AchievementinPer.length(); i++) {
                            JSONObject Totalachievement = AchievementinPer.getJSONObject(i);
                            String AchievementinPerent=Totalachievement.getString("AchievementinPer");
                            AchievementinPerCent=Double.parseDouble(AchievementinPerent);
                            txt_achievment_percent.setText(String.valueOf(AchievementinPerCent));

                        }
                    }
                    JSONArray ClaimAmount = mainJsonObject.getJSONArray("ClaimAmount");
                    if (ClaimAmount.length() > 0) {
                        for (int i = 0; i < ClaimAmount.length(); i++) {
                            JSONObject Totalachievement = ClaimAmount.getJSONObject(i);
                            String CAmount=Totalachievement.getString("ClaimAmt");

                            if (CAmount.length()>5){
                                ClaimAount=Double.parseDouble(CAmount);
                                txt_claim_date.setText(formatLakh(ClaimAount));
                            }else {
                                ClaimAount=Double.parseDouble(CAmount);
                                txt_claim_date.setText(String.format("%.2f", ClaimAount));
                            }
                        }
                    }
                    JSONArray Incentivee = mainJsonObject.getJSONArray("Incentive");
                    if (Incentivee.length() > 0) {
                        for (int i = 0; i < Incentivee.length(); i++) {
                            JSONObject Totalachievement = Incentivee.getJSONObject(i);
                            String incentive=Totalachievement.getString("Incentive");

                            if (incentive.length()>5){
                                Incentive=Double.parseDouble(incentive);
                                txt_incentive.setText(formatLakh(Incentive));
                            }else {
                                Incentive=Double.parseDouble(incentive);
                                txt_incentive.setText(String.format("%.2f", Incentive));
                            }
                        }
                    }

                    JSONArray TotalOutstanding  = mainJsonObject.getJSONArray("TotalOutstanding");
                    if (TotalOutstanding .length() > 0) {
                        for (int i = 0; i < TotalOutstanding .length(); i++) {
                            JSONObject Totalachievement = TotalOutstanding .getJSONObject(i);
                            String out=Totalachievement.getString("OutstandingAmount");

                            if (out.length()>5){
                                Outstanding=Double.parseDouble(out);
                                double value = Outstanding/100000;
                                Outstanding=value;
                                txt_tot_outstanding.setText(String.format("%.2f", Outstanding));
                                // txt_tot_outstanding.setText(formatLakh(Outstanding));
                            }else {
                                Outstanding=Double.parseDouble(out);
                                txt_tot_outstanding.setText(String.format("%.2f", Outstanding));
                            }
                        }
                    }

                    JSONArray todaysOpp1 = mainJsonObject.getJSONArray("TodaysOpp");
                    if (todaysOpp1.length() > 0) {
                        for (int i = 0; i < todaysOpp1 .length(); i++) {
                            JSONObject Totalachievement = todaysOpp1.getJSONObject(i);
                            TodayOpp=Totalachievement.getString("OppCount");
                            txt_today_opp.setText(TodayOpp);
                        }
                    }

                    JSONArray MonClaimAmt = mainJsonObject.getJSONArray("MonClaimAmt");
                    if (MonClaimAmt.length() > 0) {
                        for (int i = 0; i < MonClaimAmt.length(); i++) {
                            JSONObject Totalachievement = MonClaimAmt.getJSONObject(i);
                            String out=Totalachievement.getString("ClaimAmt");
                            if (out.length()>5){
                                ClaimDate=Double.parseDouble(out);
                                txt_claim.setText(formatLakh(ClaimDate));
                            }else {
                                ClaimDate=Double.parseDouble(out);
                                txt_claim.setText(String.format("%.2f", ClaimDate));
                            }
                        }
                    }

                    JSONArray TravelPlan= mainJsonObject.getJSONArray("TravelPlan");
                    ArrayList<String>TravelArrayList=new ArrayList<>();
                    if (TravelPlan.length() > 0) {
                        for (int i = 0; i < TravelPlan.length(); i++) {
                            JSONObject Totalachievement = TravelPlan.getJSONObject(i);
                            if (TravelPlan.length()>1){
                                CityName=Totalachievement.getString("CityName");
                                TravelArrayList.add(CityName);
                                if (TravelArrayList.size() > 0) {
                                    if (TravelArrayList.size() > 0) {
                                        user5 = new String[TravelArrayList.size()];
                                        for (int j = 0; j < TravelArrayList.size(); j++) {
                                            String travelname = TravelArrayList.get(j).toString();
                                            user5[j] = travelname.toString();
                                            Cityname = TextUtils.join(",", user5);

                                        }
                                    }

                                }
                                txt_plan.setText(Cityname);
                            }else {
                                Cityname=Totalachievement.getString("CityName");
                                txt_plan.setText(Cityname);
                            }

                        }
                    }

                    JSONArray TeamTodaysExpectedOrderNo  = mainJsonObject.getJSONArray("TeamTodaysExpectedOrderNo");
                    if (TeamTodaysExpectedOrderNo.length() > 0) {
                        for (int i = 0; i < TeamTodaysExpectedOrderNo.length(); i++) {
                            JSONObject Totalachievement = TeamTodaysExpectedOrderNo.getJSONObject(i);
                            TeamTodaysExpOrderNo=Totalachievement.getString("TodaysExpOrderNo");
                            txt_team_exp_order_no.setText(TeamTodaysExpOrderNo);

                        }
                    }


                    JSONArray TeamExpectedOrderValue = mainJsonObject.getJSONArray("TeamExpectedOrderValue");
                    if (TeamExpectedOrderValue.length() > 0) {
                        for (int i = 0; i < TeamExpectedOrderValue.length(); i++) {
                            JSONObject Totalachievement = TeamExpectedOrderValue.getJSONObject(i);
                            String TeamOrderValue=Totalachievement.getString("OrderValue");
                            if (TeamOrderValue.length()>5){
                                TeamOrderValues=Double.parseDouble(TeamOrderValue);
                                double value = TeamOrderValues/100000;
                                TeamOrderValues=value;
                                txt_team_value.setText(String.format("%.2f", TeamOrderValues));
                                // today_orderbooking.setText(formatLakh(OrderValues));
                            }else {
                                TeamOrderValues=Double.parseDouble(TeamOrderValue);
                                txt_team_value.setText(String.format("%.2f", TeamOrderValues));
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
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


    private static String formatLakh(double d) {
        String s = String.format(Locale.UK, "%1.2f", Math.abs(d));
        s = s.replaceAll("(.+)(...\\...)", "$1,$2");
        while (s.matches("\\d{3,},.+")) {
            s = s.replaceAll("(\\d+)(\\d{2},.+)", "$1,$2");
        }
        return d < 0 ? ("-" + s) : s;
    }
}
