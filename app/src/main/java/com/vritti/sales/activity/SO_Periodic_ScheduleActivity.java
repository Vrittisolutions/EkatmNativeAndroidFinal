package com.vritti.sales.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.ScheduleListAdapter;
import com.vritti.sales.beans.ScheduleListBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SO_Periodic_ScheduleActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    LinearLayout lay_so_schedule, layout_periodic;

    /*so schedule*/
    ImageView imgdatestart, img_delschedule, img_doordate;
    AutoCompleteTextView edt_qty;
    EditText txt_startdate, txt_del_date, txt_doordate;
    Button btnadd_schedule, btncancel,btnperiodic;
    ListView list_schedules;
    AppCompatCheckBox chkperiodic;
    LinearLayout layhdr;
    ImageView imgdelete;

    /*periodic*/
    ImageView imgdatestart2,img_enddate,img_end_dateby;
    EditText txt_startdate2,txt_enddate, txt_endeby;
    Button btndaily, btnweekly, btnmonthly, btnyearly, btn_save_rec,btn_cancel;
    LinearLayout lay_daily, lay_weekly, lay_monthly,lay_yearly, laydateby,lay_y1,lay_y2,lay_m1,lay_m2;
    AppCompatRadioButton radbtn_aftrevry, radbtn_day_monthly, radbtn_the_monthly, ractbtn_year_on, radbtn_the_yearly,
            radbtn_noend,radbtn_endafter,radbtn_endby;
    EditText edt_daycnt_daily, edt_weekcnt, edt_monthcnt_1, edt_monthcnt_2,edt_monthcnt_3,edt_yearcnt_1,edtcnt_endaftr;
    AppCompatCheckBox chksun,chkmon,chktue,chkwed,chkthu,chkfri,chksat;
    Spinner spin_firstlast, spin_day, spin_months, spin_firstlast_year, spin_day_year,spin_month_year;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    public static final int SOSCHEDULE = 1;
    public static final int PERIODIC = 2;

    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;
    private SharedPreferences sharedPrefs;
    String StartDate = "", EndDate = "";
    String selectedRecPtrn = "";

    String callFrom = "";
    String[] first_last = {"First","Second","Third","Fourth","Last"};
    String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday","Day"};
    String[] months = {"January","February","March","April","May","June","July","August","September","Octomber","November","December"};

    String ScheduleDate = "", ExVendorDate = "", BalQty = "", SoDate = "", FinalDeliverDate = "", ItemProcessId = "", ItemProcessCode = "";
    String RecStartDate = "",PeriodicEndDate="",RecEndDate="",ItemSrNo="",ItemSize="",RecurDaysCount="",RecurWeeksCount="",srno="",
            IsSunday="",IsMonday="",IsTuesday="",IsWednesday="",IsFriday="",IsThursday="",IsSaturday="",EveryMonthCount="",
            MonthlyDayNo="",MonthlyMonth="",MonthlyWeek="",MonthlyDay="",YearlyMonthName="",YearlyWeek="",YearlyDay="",YearlyMonth="",
            TypeOfPeriod="",RecurYearCount="",IsNoEndDate="",IsProRata="",ProFigure="",Occurrences="";

    JSONObject jobj_schedule, jobj_periodic;
    JSONArray jArray,jArray_periodic;
    String finalOBJ = "";

    ArrayList<ScheduleListBean> listSchedules;
    ScheduleListAdapter schAdapter;

    int editPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so__periodic__schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        Intent intent = getIntent();
        callFrom = intent.getStringExtra("callFrom");
        SoDate = intent.getStringExtra("SoDate");

        if(callFrom.equalsIgnoreCase("SOSchedule")){
            layout_periodic.setVisibility(View.GONE);
            lay_so_schedule.setVisibility(View.VISIBLE);
        }else if(callFrom.equalsIgnoreCase("Periodic")){
            layout_periodic.setVisibility(View.VISIBLE);
            lay_so_schedule.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat f1 = new SimpleDateFormat("EEE, dd MMM yyyy");
        SimpleDateFormat f2 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date newDate = null;
        String date = "", date2 = "";
        int weekOfMonth, dayOfWeek;

        try {
            newDate = f2.parse(StartDate);
            date = f1.format(newDate);
            date2 = dateFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar=Calendar.getInstance();
        String demoDate = date2;
        try{
            calendar.setTime(dateFormat.parse(demoDate));
            weekOfMonth = Calendar.WEEK_OF_MONTH;
            dayOfWeek = Calendar.DAY_OF_WEEK;
        }catch(Exception e){
            e.printStackTrace();
        }

        setListeners();
    }

    private void init() {
        parent = SO_Periodic_ScheduleActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("SO Schedule");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        lay_so_schedule = findViewById(R.id.lay_so_schedule);
        layout_periodic = findViewById(R.id.layout_periodic);
        layout_periodic.setVisibility(View.GONE);

        /*so schedule*/
        imgdatestart = findViewById(R.id.imgdatestart);
        img_delschedule = findViewById(R.id.img_delschedule);
        img_doordate = findViewById(R.id.img_doordate);
        edt_qty = findViewById(R.id.edt_qty);
        txt_startdate = findViewById(R.id.txt_startdate);
        txt_del_date = findViewById(R.id.txt_del_date);
        txt_doordate = findViewById(R.id.txt_doordate);
        btnadd_schedule = findViewById(R.id.btnadd_schedule);
        btncancel = findViewById(R.id.btncancel);
        btnperiodic = findViewById(R.id.btnperiodic);
        list_schedules = findViewById(R.id.list_schedules);
        chkperiodic = findViewById(R.id.chkperiodic);
        layhdr= findViewById(R.id.layhdr);
        layhdr.setVisibility(View.GONE);
        imgdelete = findViewById(R.id.imgdelete);

        /*periodic*/
        lay_y1 = findViewById(R.id.lay_y1);
        lay_y2 = findViewById(R.id.lay_y2);
        lay_m1 = findViewById(R.id.lay_m1);
        lay_m2 = findViewById(R.id.lay_m2);
        imgdatestart2 = findViewById(R.id.imgdatestart2);
        img_enddate = findViewById(R.id.img_enddate);
        img_end_dateby = findViewById(R.id.img_end_dateby);
        txt_startdate2 = findViewById(R.id.txt_startdate2);
        txt_enddate = findViewById(R.id.txt_enddate);
        txt_endeby = findViewById(R.id.txt_endeby);
        btndaily = findViewById(R.id.btndaily);
        btnweekly = findViewById(R.id.btnweekly);
        btnmonthly = findViewById(R.id.btnmonthly);
        btnyearly = findViewById(R.id.btnyearly);
        btn_save_rec = findViewById(R.id.btn_save_rec);
        btn_cancel = findViewById(R.id.btn_cancel);
        lay_daily = findViewById(R.id.lay_daily);
        lay_weekly = findViewById(R.id.lay_weekly);
        lay_monthly = findViewById(R.id.lay_monthly);
        lay_yearly = findViewById(R.id.lay_yearly);
        laydateby = findViewById(R.id.laydateby);
        radbtn_aftrevry = findViewById(R.id.radbtn_aftrevry);
        radbtn_day_monthly = findViewById(R.id.radbtn_day_monthly);
        radbtn_the_monthly = findViewById(R.id.radbtn_the_monthly);
        ractbtn_year_on = findViewById(R.id.ractbtn_year_on);
        radbtn_the_yearly = findViewById(R.id.radbtn_the_yearly);
        radbtn_noend = findViewById(R.id.radbtn_noend);
        radbtn_endafter = findViewById(R.id.radbtn_endafter);
        radbtn_endby = findViewById(R.id.radbtn_endby);
        edt_daycnt_daily = findViewById(R.id.edt_daycnt_daily);
        edt_daycnt_daily.setSelection(edt_daycnt_daily.getText().toString().length());
        edt_weekcnt = findViewById(R.id.edt_weekcnt);
        edt_weekcnt.setSelection(edt_weekcnt.getText().toString().length());
        edt_monthcnt_1 = findViewById(R.id.edt_monthcnt_1);
        edt_monthcnt_1.setSelection(edt_monthcnt_1.getText().toString().length());
        edt_monthcnt_2 = findViewById(R.id.edt_monthcnt_2);
        edt_monthcnt_2.setSelection(edt_monthcnt_2.getText().toString().length());
        edt_monthcnt_3 = findViewById(R.id.edt_monthcnt_3);
        edt_monthcnt_3.setSelection(edt_monthcnt_3.getText().toString().length());
        edt_yearcnt_1 = findViewById(R.id.edt_yearcnt_1);
        edt_yearcnt_1.setSelection(edt_yearcnt_1.getText().toString().length());
        edtcnt_endaftr = findViewById(R.id.edtcnt_endaftr);
        edtcnt_endaftr.setSelection(edtcnt_endaftr.getText().toString().length());
        chksun = findViewById(R.id.chksun);
        chkmon = findViewById(R.id.chkmon);
        chktue = findViewById(R.id.chktue);
        chkwed = findViewById(R.id.chkwed);
        chkthu = findViewById(R.id.chkthu);
        chkfri = findViewById(R.id.chkfri);
        chksat = findViewById(R.id.chksat);
        spin_firstlast = findViewById(R.id.spin_firstlast);
        spin_day = findViewById(R.id.spin_day);
        spin_months = findViewById(R.id.spin_months);
        spin_firstlast_year = findViewById(R.id.spin_firstlast_year);
        spin_day_year = findViewById(R.id.spin_day_year);
        spin_month_year = findViewById(R.id.spin_month_year);

        ut = new Utility();
        cf = new CommonFunction(SO_Periodic_ScheduleActivity.this);
        tcf = new Tbuds_commonFunctions(SO_Periodic_ScheduleActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SO_Periodic_ScheduleActivity.this);
        String dabasename = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SO_Periodic_ScheduleActivity.this, dabasename);
        CompanyURL = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SO_Periodic_ScheduleActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SO_Periodic_ScheduleActivity.this);
        StartDate = sharedPrefs.getString("StartDate", "");
        EndDate = sharedPrefs.getString("EndDate", "");

        txt_startdate.setText(StartDate);
        txt_del_date.setText(EndDate);
        txt_doordate.setText(EndDate);
        txt_startdate2.setText(StartDate);
        txt_enddate.setText(EndDate);

        listSchedules = new ArrayList<ScheduleListBean>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.myspinnerstyle,first_last);
        spin_firstlast.setAdapter(adapter);
        spin_firstlast_year.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.myspinnerstyle,days);
        spin_day.setAdapter(adapter1);
        spin_day_year.setAdapter(adapter1);

        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,months);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.myspinnerstyle,months);
        spin_months.setAdapter(adapter2);
        spin_month_year.setAdapter(adapter2);

    }

    private void setListeners() {
        btnadd_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_qty.getText().toString().equalsIgnoreCase("") || edt_qty.getText().toString().equals(null)){
                    Toast.makeText(parent,"Enter quantity",Toast.LENGTH_SHORT).show();
                }else {

                    int schId = 0;

                    if(listSchedules.size() == 0){
                        layhdr.setVisibility(View.VISIBLE);

                        ScheduleListBean schBean = new ScheduleListBean(txt_startdate.getText().toString(),txt_enddate.getText().toString(),
                                txt_doordate.getText().toString(),edt_qty.getText().toString(),chkperiodic.isChecked());
                        schBean.setSchId(schId+1);
                        listSchedules.add(schBean);

                        schAdapter = new ScheduleListAdapter(parent,listSchedules);
                        list_schedules.setAdapter(schAdapter);
                    }else {
                        layhdr.setVisibility(View.VISIBLE);

                        boolean ispresent = false;
                        for(int i=0; i<listSchedules.size(); i++){
                            if(txt_startdate.getText().toString().equalsIgnoreCase(listSchedules.get(i).getStartDate())){
                                Toast.makeText(parent,"Duplicate start date.",Toast.LENGTH_SHORT).show();
                                ispresent = true;
                            }
                        }

                        if(!ispresent){
                            schId = schId+1;
                            ScheduleListBean schBean = new ScheduleListBean(txt_startdate.getText().toString(),txt_enddate.getText().toString(),
                                    txt_doordate.getText().toString(),edt_qty.getText().toString(),chkperiodic.isChecked());
                            schBean.setSchId(schId);
                            listSchedules.add(schBean);

                            schAdapter = new ScheduleListAdapter(parent,listSchedules);
                            list_schedules.setAdapter(schAdapter);

                            edt_qty.setText("");
                        }

                    }

                     /*jArray = createJSON();
                finalOBJ = jArray.toString();

                Intent intent = new Intent(SO_Periodic_ScheduleActivity.this, AddDirectItemActivity.class);
                intent.putExtra("jSchedule",finalOBJ);
                setResult(SOSCHEDULE,intent);
                finish();*/
                }

            }
        });

        list_schedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open scheduled in edit mode
                txt_startdate.setText(listSchedules.get(position).getStartDate());
                txt_enddate.setText(listSchedules.get(position).getEndDate());
                txt_doordate.setText(listSchedules.get(position).getCustDoorDate());
                edt_qty.setText(listSchedules.get(position).getQty());

                editPos = position;

                imgdelete.setVisibility(View.VISIBLE);

            }
        });

        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSchedules.remove(editPos);
                schAdapter.notifyDataSetChanged();

                Toast.makeText(parent,"Row deleted successfully",Toast.LENGTH_SHORT).show();

                edt_qty.setText("");
                txt_startdate.setText(StartDate);
                txt_enddate.setText(EndDate);
                txt_doordate.setText(EndDate);
                imgdelete.setVisibility(View.GONE);
            }
        });

        btn_save_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get scheduledetails, listschedule data also
                jArray = createJSON();

                /*Intent intent = new Intent(SO_Periodic_ScheduleActivity.this, AddDirectItemActivity.class);
                intent.putExtra("jSchedule",finalOBJ);
                setResult(SOSCHEDULE,intent);
                finish();*/
                jArray_periodic = createJSON_periodic();

                Intent intent = new Intent(SO_Periodic_ScheduleActivity.this, AddDirectItemActivity.class);
                intent.putExtra("jPeriodic",jArray_periodic.toString());
                intent.putExtra("jSchedule",jArray.toString());
                intent.putExtra("RecPtrn",selectedRecPtrn);
                //setResult(PERIODIC,intent);
                setResult(SOSCHEDULE,intent);
                finish();
            }
        });

        chkperiodic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layout_periodic.setVisibility(View.VISIBLE);
                }else {
                    layout_periodic.setVisibility(View.GONE);
                }
            }
        });

        btndaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRecPtrn = "D";

                btndaily.setAlpha((float)1);
                btnweekly.setAlpha((float)0.5);
                btnmonthly.setAlpha((float)0.5);
                btnyearly.setAlpha((float)0.5);

                radbtn_aftrevry.setChecked(true);
                lay_daily.setBackgroundColor(getResources().getColor(R.color.radback_color));
                radbtn_day_monthly.setChecked(false);
                radbtn_the_monthly.setChecked(false);
                radbtn_the_yearly.setChecked(false);
                ractbtn_year_on.setChecked(false);

                lay_daily.setVisibility(View.VISIBLE);
                lay_weekly.setVisibility(View.GONE);
                lay_monthly.setVisibility(View.GONE);
                lay_yearly.setVisibility(View.GONE);

                edt_daycnt_daily.requestFocus();

            }
        });

        btnweekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRecPtrn = "W";

                btndaily.setAlpha((float)0.5);
                btnweekly.setAlpha((float)1);
                btnmonthly.setAlpha((float)0.5);
                btnyearly.setAlpha((float)0.5);

                lay_weekly.setBackgroundColor(getResources().getColor(R.color.radback_color));
                radbtn_aftrevry.setChecked(false);
                radbtn_day_monthly.setChecked(false);
                radbtn_the_monthly.setChecked(false);
                radbtn_the_yearly.setChecked(false);
                ractbtn_year_on.setChecked(false);

                lay_daily.setVisibility(View.GONE);
                lay_weekly.setVisibility(View.VISIBLE);
                lay_monthly.setVisibility(View.GONE);
                lay_yearly.setVisibility(View.GONE);

                edt_weekcnt.requestFocus();
            }
        });

        btnmonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRecPtrn = "M";

                btndaily.setAlpha((float)0.5);
                btnweekly.setAlpha((float)0.5);
                btnmonthly.setAlpha((float)1);
                btnyearly.setAlpha((float)0.5);

                radbtn_day_monthly.setChecked(true);
                lay_m1.setBackgroundColor(getResources().getColor(R.color.radback_color));
                lay_m2.setBackgroundColor(Color.parseColor("#FFFFFF"));

                radbtn_aftrevry.setChecked(false);
                radbtn_the_monthly.setChecked(false);
                radbtn_the_yearly.setChecked(false);
                ractbtn_year_on.setChecked(false);

                lay_daily.setVisibility(View.GONE);
                lay_weekly.setVisibility(View.GONE);
                lay_monthly.setVisibility(View.VISIBLE);
                lay_yearly.setVisibility(View.GONE);

                edt_monthcnt_1.requestFocus();
            }
        });

        btnyearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedRecPtrn = "Y";

                btndaily.setAlpha((float)0.5);
                btnweekly.setAlpha((float)0.5);
                btnmonthly.setAlpha((float)0.5);
                btnyearly.setAlpha((float)1);

                ractbtn_year_on.setChecked(true);
                lay_y1.setBackgroundColor(getResources().getColor(R.color.radback_color));
                lay_y2.setBackgroundColor(Color.parseColor("#FFFFFF"));

                radbtn_aftrevry.setChecked(false);
                radbtn_day_monthly.setChecked(false);
                radbtn_the_monthly.setChecked(false);
                radbtn_the_yearly.setChecked(false);

                lay_daily.setVisibility(View.GONE);
                lay_weekly.setVisibility(View.GONE);
                lay_monthly.setVisibility(View.GONE);
                lay_yearly.setVisibility(View.VISIBLE);

                edt_yearcnt_1.requestFocus();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnperiodic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_periodic.setVisibility(View.VISIBLE);
            }
        });

        radbtn_noend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_noend.setChecked(true);
                    radbtn_endafter.setChecked(false);
                    radbtn_endby.setChecked(false);

                    edtcnt_endaftr.setEnabled(false);
                    laydateby.setEnabled(false);
                    img_end_dateby.setEnabled(false);

                }else {
                }
            }
        });

        radbtn_endafter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_endafter.setChecked(true);
                    radbtn_noend.setChecked(false);
                    radbtn_endby.setChecked(false);

                    edtcnt_endaftr.requestFocus();
                    edtcnt_endaftr.setEnabled(true);
                    laydateby.setEnabled(false);
                    img_end_dateby.setEnabled(false);

                }else {
                }
            }
        });

        radbtn_endby.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_endby.setChecked(true);
                    radbtn_endafter.setChecked(false);
                    radbtn_noend.setChecked(false);

                    edtcnt_endaftr.setEnabled(false);
                    laydateby.setEnabled(true);
                    img_end_dateby.setEnabled(true);

                }else {
                }
            }
        });

        radbtn_day_monthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_day_monthly.setChecked(true);
                    radbtn_the_monthly.setChecked(false);

                    edt_monthcnt_1.setEnabled(true);
                    edt_monthcnt_2.setEnabled(true);
                    spin_firstlast.setEnabled(false);
                    spin_day.setEnabled(false);
                    edt_monthcnt_3.setEnabled(false);

                    lay_m1.setBackgroundColor(getResources().getColor(R.color.radback_color));
                    lay_m2.setBackgroundColor(Color.parseColor("#FFFFFF"));


                }else {
                }
            }
        });

        radbtn_the_monthly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_the_monthly.setChecked(true);
                    radbtn_day_monthly.setChecked(false);

                    edt_monthcnt_1.setEnabled(false);
                    edt_monthcnt_2.setEnabled(false);
                    spin_firstlast.setEnabled(true);
                    spin_day.setEnabled(true);
                    edt_monthcnt_3.setEnabled(true);

                    lay_m2.setBackgroundColor(getResources().getColor(R.color.radback_color));
                    lay_m1.setBackgroundColor(Color.parseColor("#FFFFFF"));


                }else {
                }
            }
        });

        ractbtn_year_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ractbtn_year_on.setChecked(true);
                    radbtn_the_yearly.setChecked(false);

                    lay_y1.setBackgroundColor(getResources().getColor(R.color.radback_color));
                    lay_y2.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    edt_yearcnt_1.setEnabled(true);
                    spin_months.setEnabled(true);
                    spin_firstlast_year.setEnabled(false);
                    spin_day_year.setEnabled(false);
                    spin_month_year.setEnabled(false);

                }else {
                }
            }
        });

        radbtn_the_yearly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radbtn_the_yearly.setChecked(true);
                    ractbtn_year_on.setChecked(false);

                    edt_yearcnt_1.setEnabled(false);
                    spin_months.setEnabled(false);
                    spin_firstlast_year.setEnabled(true);
                    spin_day_year.setEnabled(true);
                    spin_month_year.setEnabled(true);

                    lay_y2.setBackgroundColor(getResources().getColor(R.color.radback_color));
                    lay_y1.setBackgroundColor(Color.parseColor("#FFFFFF"));

                }else {
                }
            }
        });

        imgdatestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_startdate.setText(date);
                                } else {
                                    txt_startdate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_delschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_del_date.setText(date);
                                } else {
                                    txt_del_date.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_doordate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_doordate.setText(date);
                                } else {
                                    txt_doordate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        imgdatestart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_startdate2.setText(date);
                                } else {
                                    txt_startdate2.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_enddate.setText(date);
                                } else {
                                    txt_enddate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_end_dateby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    txt_endeby.setText(date);
                                } else {
                                    txt_endeby.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });


    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).before(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public JSONArray createJSON(){
        UUID uuid = UUID.randomUUID();
        String scheduleID = uuid.toString();

        ScheduleDate = txt_del_date.getText().toString();
        FinalDeliverDate = txt_doordate.getText().toString();
        ExVendorDate = SoDate;
        BalQty = "0";

        try {
            jArray = new JSONArray();
            jobj_schedule = new JSONObject();

            jobj_schedule.put("SrNo","");
            jobj_schedule.put("ItemSrNo","");
            jobj_schedule.put("ScheduleId",scheduleID); //guid
            jobj_schedule.put("ScheduleDate",ScheduleDate);
            jobj_schedule.put("ExVendorDate",ExVendorDate);
            jobj_schedule.put("Qty",edt_qty.getText().toString());
            jobj_schedule.put("BalQty",BalQty);
            jobj_schedule.put("SoDate",SoDate);
            jobj_schedule.put("FinalDeliverDate",FinalDeliverDate);
            jobj_schedule.put("ItemProcessId",ItemProcessId);
            jobj_schedule.put("ItemProcessCode",ItemProcessCode);
            jobj_schedule.put("Action","");

            jArray.put(jobj_schedule);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }

    public JSONArray createJSON_periodic(){

        UUID uuid = UUID.randomUUID();
        String scheduleID = uuid.toString();

        try {
            jArray_periodic = new JSONArray();
            jobj_periodic = new JSONObject();

            if(chkperiodic.isChecked()){

                RecStartDate = StartDate;
                RecEndDate = EndDate;

                if(selectedRecPtrn.equalsIgnoreCase("D")){
                    RecurDaysCount = edt_daycnt_daily.getText().toString();

                    RecurWeeksCount=""; IsSunday = ""; IsMonday=""; IsTuesday=""; IsWednesday=""; IsThursday=""; IsFriday=""; IsSaturday="";
                    EveryMonthCount =""; MonthlyDayNo=""; MonthlyMonth=""; MonthlyWeek=""; MonthlyDay ="";
                    RecurYearCount = ""; YearlyMonthName = ""; YearlyWeek = ""; YearlyDay = ""; YearlyMonth = "";

                }else if(selectedRecPtrn.equalsIgnoreCase("W")){
                    RecurDaysCount = "";EveryMonthCount =""; MonthlyDayNo=""; MonthlyMonth=""; MonthlyWeek=""; MonthlyDay ="";
                    RecurYearCount = ""; YearlyMonthName = ""; YearlyWeek = ""; YearlyDay = ""; YearlyMonth = "";

                    RecurWeeksCount = edt_weekcnt.getText().toString();
                    IsSunday = String.valueOf(chksun.isChecked());  IsMonday = String.valueOf(chkmon.isChecked());
                    IsTuesday=String.valueOf(chktue.isChecked()); IsWednesday=String.valueOf(chkwed.isChecked());
                    IsThursday=String.valueOf(chkthu.isChecked()); IsFriday=String.valueOf(chkfri.isChecked());
                    IsSaturday=String.valueOf(chksat.isChecked());

                }else if(selectedRecPtrn.equalsIgnoreCase("M")){
                    RecurDaysCount = "";RecurWeeksCount=""; IsSunday = ""; IsMonday=""; IsTuesday=""; IsWednesday=""; IsThursday="";
                    IsFriday=""; IsSaturday="";RecurYearCount = ""; YearlyMonthName = ""; YearlyWeek = ""; YearlyDay = "";
                    YearlyMonth = "";

                    if(radbtn_day_monthly.isChecked()){
                        MonthlyMonth=""; MonthlyWeek=""; MonthlyDay ="";

                        EveryMonthCount = edt_monthcnt_2.getText().toString();
                        MonthlyDayNo = edt_monthcnt_1.getText().toString();

                    }else if(radbtn_the_monthly.isChecked()){
                        EveryMonthCount =""; MonthlyDayNo="";

                        MonthlyMonth = edt_monthcnt_3.getText().toString();
                        MonthlyWeek = spin_firstlast.getSelectedItem().toString();
                        MonthlyDay = spin_day.getSelectedItem().toString();
                    }

                }else if(selectedRecPtrn.equalsIgnoreCase("Y")){
                    RecurDaysCount = "";EveryMonthCount =""; MonthlyDayNo=""; MonthlyMonth=""; MonthlyWeek=""; MonthlyDay ="";
                    RecurWeeksCount=""; IsSunday = ""; IsMonday=""; IsTuesday=""; IsWednesday=""; IsThursday=""; IsFriday=""; IsSaturday="";

                    if(ractbtn_year_on.isChecked()){
                        RecurYearCount = edt_yearcnt_1.getText().toString();
                        YearlyMonthName = spin_months.getSelectedItem().toString();

                        YearlyWeek = ""; YearlyDay = ""; YearlyMonth = "";
                    }else if(radbtn_the_yearly.isChecked()){
                        RecurYearCount = ""; YearlyMonthName = "";

                        YearlyWeek = spin_firstlast_year.getSelectedItem().toString();
                        YearlyDay = spin_day_year.getSelectedItem().toString();
                        YearlyMonth = spin_month_year.getSelectedItem().toString();
                    }
                }

                if(radbtn_noend.isChecked()){
                    IsNoEndDate = "";Occurrences = "";PeriodicEndDate = "";
                }else if(radbtn_endafter.isChecked()){
                    IsNoEndDate = "";PeriodicEndDate = "";
                    Occurrences = edtcnt_endaftr.getText().toString();
                }else if(radbtn_endby.isChecked()){
                    IsNoEndDate = "";Occurrences = "";
                    PeriodicEndDate = txt_endeby.getText().toString();
                    //PeriodicEndDate = EndDate;
                }

            }else {
                RecStartDate = "";
                RecEndDate = "";
                IsNoEndDate = "";Occurrences = "";PeriodicEndDate = "";RecurDaysCount = "";
                RecurWeeksCount=""; IsSunday = ""; IsMonday=""; IsTuesday=""; IsWednesday=""; IsThursday=""; IsFriday=""; IsSaturday="";
                EveryMonthCount =""; MonthlyDayNo=""; MonthlyMonth=""; MonthlyWeek=""; MonthlyDay ="";
                RecurYearCount = ""; YearlyMonthName = ""; YearlyWeek = ""; YearlyDay = ""; YearlyMonth = "";
            }

            jobj_periodic.put("RecStartDate",RecStartDate);
            jobj_periodic.put("PeriodicEndDate",PeriodicEndDate);
            jobj_periodic.put("RecEndDate",RecEndDate);
            jobj_periodic.put("ItemSrNo",ItemSrNo);
            jobj_periodic.put("ItemSize",ItemSize);
            jobj_periodic.put("RecurDaysCount",RecurDaysCount);
            jobj_periodic.put("RecurWeeksCount",RecurWeeksCount);
            jobj_periodic.put("srno",srno);
            jobj_periodic.put("IsSunday",IsSunday);
            jobj_periodic.put("IsMonday",IsMonday);
            jobj_periodic.put("IsTuesday",IsTuesday);
            jobj_periodic.put("IsWednesday",IsWednesday);
            jobj_periodic.put("IsThursday",IsThursday);
            jobj_periodic.put("IsFriday",IsFriday);
            jobj_periodic.put("IsSaturday",IsSaturday);
            jobj_periodic.put("EveryMonthCount",EveryMonthCount);
            jobj_periodic.put("MonthlyDayNo",MonthlyDayNo);
            jobj_periodic.put("MonthlyMonth",MonthlyMonth);
            jobj_periodic.put("MonthlyWeek",MonthlyWeek);
            jobj_periodic.put("MonthlyDay",MonthlyDay);
            jobj_periodic.put("YearlyMonthName",YearlyMonthName);
            jobj_periodic.put("YearlyWeek",YearlyWeek);
            jobj_periodic.put("YearlyDay",YearlyDay);
            jobj_periodic.put("YearlyMonth",YearlyMonth);
            jobj_periodic.put("TypeOfPeriod",TypeOfPeriod);
            jobj_periodic.put("IsNoEndDate",IsNoEndDate);
            jobj_periodic.put("RecurYearCount",RecurYearCount);
            jobj_periodic.put("Occurrences",Occurrences);

            jArray_periodic.put(jobj_periodic);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray_periodic;
    }

}
