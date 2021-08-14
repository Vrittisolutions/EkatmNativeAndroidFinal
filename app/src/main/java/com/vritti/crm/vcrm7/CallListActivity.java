package com.vritti.crm.vcrm7;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.crm.adapter.CalendarAdapter;
import com.vritti.crm.adapter.MyTeamAdapter;
import com.vritti.crm.bean.Appointment;
import com.vritti.crm.bean.BirthdayBean;
import com.vritti.crm.bean.CalendarCollection;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.bean.MyTeamBean;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.bean.ReasonBean;
import com.vritti.crm.bean.ShowContact;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.crm.classes.ProgressHUD;

import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.apache.commons.lang3.time.StopWatch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CallListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    DrawerLayout drawer_layout;
    NavigationView navigationView;
    boolean avail = false;
    static String Salesmodulevisible = "true";
    static String Salesmoduleinvisible = "false";

    View header;
    LinearLayout layout_birthday, ls_appointments, ly_team, realcolors, lay_opportunity, lay_overdue_opp,
            lay_today_opp, lay_Tommorow_opp, lay_this_week_opp, lay_collection, lay_collection_view,
            lay_overdue_collection, lay_today_overdue_collection, lay_tomorrow_overdue_collection,
            lay_week_overdue_collection, lay_callrating, lay_hot_call, lay_warm_call, lay_week_overdue_feedback, lay_today_overdue_feedback, lay_tomorrow_overdue_feedback,
            lay_overdue_feedback, lay_new_feedback, lay_new_collection, lay_feedback, lay_new_opp, lay_revived_opp;
    RelativeLayout rly_birthday, rly_notification, rly_meeting, rel_call_list, rel_calender;
    TextView layloadmore;
    Dialog dialog;
    TextView tv_birthday_cnt;
    TextView tv_username;
    TextView tv_team_mem_cnt;
    TextView tv_team_mem_cnt1;
    TextView tv_meeting_cnt;
    TextView tv_notification_cnt;
    TextView tv_opportunities;
    TextView tv_appointments_cnt;
    TextView tv_collection_cnt;
    TextView tv_callrating_cnt;
    TextView tv_overdue_opp, tv_yesterday_opp;
    TextView tv_today_opp, tv_callagain_opp, tv_revived_opp;
    TextView tv_tommorow_oppportunity;
    TextView tv_this_week_opp;
    TextView tv_overdue_collection;
    TextView tv_new_collection;

    TextView tv_today_overdue_collection;
    TextView tv_tomrrow_overdue_collection;
    TextView tv_week_overdue_collection;
    TextView tv_hot_call;
    TextView tv_warm_call;
    TextView tv_firmname;
    TextView tv_date_time;
    TextView tv_city;
    TextView tv_warm_call_value;
    TextView tv_hot_call_value;
    TextView txtHome, txtServiceReport, txt_clientmaster,txt_CRMReport;
    TextView txtExecutivePerformance;
    TextView txtProspectMaster;
    TextView txtCallLogs;
    TextView txtCRMReport;
    TextView txtEntityMaster;
    TextView txtCityMaster;
    TextView txtBusinessSegMaster;
    TextView txtTeritoryMaster;
    TextView txtReportlist;
    TextView txtTravelPlan, txtoffline, txtpromotional;
    TextView txtfirmname;
    TextView tvcall;
    TextView txtcityname;
    TextView tv_latestremark;
    TextView tv_mailid;
    TextView txtcityterritoryname;
    TextView txtactiondatetime;
    TextView txtisPartial;
    TextView txtteam;
    TextView txtEnquiryform, txtpromotinalform;
    TextView txtEnquiryFormSetting;
    TextView tv_overdue_feedback;
    TextView tv_new_feedback;

    TextView tv_today_overdue_feedback;
    TextView tv_tomrrow_overdue_feedback;
    TextView tv_week_overdue_feedback;
    TextView tv_feedback_cnt;
    TextView txt_target_achivment;
    TextView txt_provisional, txt_chat, txt_chatroom, tv_new_opp, textview_opportunity_Overdue, textview_opportunity_yesterday, textview_opportunity_today,
            textview_opportunity_callagain;

    ImageView img_contact, img_appotunity_update, img_calender;

    ListView ls_Team;
    LinearLayout lsCall_list, laycall_type, len_show_calldetails, len_contact_edit, len_contact, len_cal;
    CommonObjectProperties commonObj;
    String UserType, Token;

    String FinalObj, loadmore;
    ArrayList<BirthdayBean> lsBirthdayList;
    MyTeamAdapter teamAdapter;
    MenuItem menuItem;
    SimpleDateFormat sdf;
    ArrayList<PartialCallList> partialCallLists;
    // CallListPartialAdapter callListPartialAdapter;
    public static final String MyPREFERENCES = "LoggingPrefs";
    String time, sp_date, rStr;
    int sp_count;
    String getdate, currentTime;
    Appointment appointment;
    ArrayList<Appointment> appointmentArrayList;
    ArrayList<MyTeamBean> ls;
    ProgressBar googleProgressBar;
    String Update;
    int pos;
    int calllogPos = -1;


    //Apportunity Rating

    EditText editTextExpecteddate, expectedvalue, edt_search_firm;
    ImageView img_search_firm;
    static int year, month, day;
    public static String date = null;
    public static String today, todaysDate;

    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    ArrayList<ReasonBean> reasonBeanArrayList;
    List reason;
    Spinner spinner_reason, spinner_callstatus;
    public static String PKReasonID, callid, jsonparams, callstatus, CurrentDate;
    public static String callStatus;
    TextView RatingbuttonSave, RatingbuttonClose;
    public static String ReasonDescription;
    ProgressHUD progressHUD;
    SimpleDateFormat dfDate;
    LinearLayout len_action;
    // int position1;
    View convertView;

    ArrayAdapter<String> dataAdapter;


    //Add contact Code

    EditText edt_contact_name, edt_designation, edt_emailid, edt_mobile, edt_offfice;
    Spinner spinner_department;
    AppCompatCheckBox checkbox_primary_contact;
    TextView txt_save, txt_reset;
    String Call_Contactname, Call_Designation, Call_Department, Call_Mobile, Call_Email, Call_Office, Addcalljson, Call_Callid, Call_CallType, Call_ProspectId, Deletecalljson, Contact_id, Call_primary, Editcalljson;
    boolean Check_Value;
    ArrayList<ShowContact> showContactArrayList = new ArrayList<>();
    String show_contactname, show_designation, show_department, show_email, show_mobile, show_primary_contact;
    View showcontactconvertView;
    TextView txt_show_contactname, txt_show_contact_designation, txt_show_contact_department, txt_show_contact_mobileno, txt_show_contact_email, txt_show_contact_primary;
    Spinner txt_action;
    EditText edit_contactname, edit_designation, edit_mobile, edit_email;
    TextView txt_edit_save, txt_update_msg, txt_update;
    Spinner spinner_edit_primary, spinner_edit_department;
    String ActionPerformitem, Firmname;
    ArrayAdapter<String> ActionPerform;
    String[] ArrarlistActionPerform = {"Edit", "Delete"};
    int position;
    ImageView img_opportunity, img_appointment;
    LinearLayout len_update, len_search;
    String App_version;
    TextView app_version;
    View view_Enquiry;


    //Calender Demo

    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;
    GridView gridview;
    String ChatRoomId;
    public String IsChatApplicable;
    public String IsCollectionApplicable;
    TextView txt_enquirycount;
    LinearLayout ln_enquiry;
    ImageView img_inquiry;
    private Integer pos1;
    private String settingKey, dabasename;
    ActionBarDrawerToggle mDrawerToggle;
    SimpleDraweeView img_userpic;
    private int REQUEST_READ_PHONE_STATE = 1;
    String imeinumber = "";
    TextView txt_phncalltxt;
    TextView tv_phncall_cnt;
    LinearLayout layout_phncalls;

    //TextView resultView;
    Loader<Cursor> cursorLoader = null;

    List<android.location.Address> address;
    String Address="";
    double Lat=0,Lng=0;
    TextView txtaddress,txt_expvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(CallListActivity.this);
        setContentView(R.layout.crm_activity_calllist);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, "");
        IsCollectionApplicable = userpreferences.getString(WebUrlClass.USERINFO_ISCOLLECTION_APPLICABLE, Salesmodulevisible);


        CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();


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
        lsBirthdayList = new ArrayList<BirthdayBean>();
        partialCallLists = new ArrayList<PartialCallList>();
        InitView();

        getSupportLoaderManager().initLoader(1, null, CallListActivity.this);


        Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();
        count1 = count1 + 1;
        Log.e("Call Log Count", "" + count1);


        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        today = dfDate.format(new Date());
        sdf = new SimpleDateFormat("dd MMM yyyy");// 25 Oct 2016
        getdate = sdf.format(new Date());// 17 Apr 2014
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar cl = Calendar.getInstance();
        currentTime = dateFormat.format(cl.getTime());

        ActionPerform = new ArrayAdapter<String>(CallListActivity.this, android.R.layout.simple_spinner_dropdown_item,
                ArrarlistActionPerform);

        String query = "SELECT * FROM " + db.TABLE_OPPORTUNITY_UPDATE;
        Cursor cur = sql.rawQuery(query, null);
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String finaljson1 = (cur.getString(cur.getColumnIndex("Opportunity_update")));
                String Update1 = (cur.getString(cur.getColumnIndex("UpdateData")));


            } while (cur.moveToNext());

        } else {

        }

        if (cf.getCalendercount() > 0) {
            Calenderlist();
            // Toast.makeText(CallListActivity.this,"Calender method Called",Toast.LENGTH_SHORT).show();
        } else {

            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCalenderDetailsData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                    }
                });
            }
        }


        //Opportunities
        //Appointment
        //CheckSalesInstalled
        //Collection call
        //callrating
        //downloadbday data json
        //Meeting
        //Notification
        //Team
        //Subteam
        //Enquiry json

        int call_cnt = cf.getCallcount();
        if (cf.getCallcount() > 0) {
            CallData();
        } else {
            //getCallListData();
        }


        if (UserType.equalsIgnoreCase("")) {
            new DownloadUserTypeFromServer().execute();


         /*   if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadTeamJSON().execute();
                        new DownloadSubTeamJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }*/

        } else {
            tv_username.setText(UserName);
            /*if (cf.getCallListPartialcount() > 0) {
                UpdatList();

         *//*       if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //   Toast.makeText(CallListActivity.this,"Opportunity api call",Toast.LENGTH_SHORT).show();
                            new DownloadOpportunitiesJSON().execute();

                            //call of team json
                        }

                        @Override
                        public void callfailMethod(String msg) {

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        }
                    });

                }*//*


            }
            else {
              //  UpdateOpportunities();
                if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            // Toast.makeText(CallListActivity.this,"GetcallList data method called",Toast.LENGTH_SHORT).show();
                            getCallListData();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });


                }
                *//*if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadOpportunitiesJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        }
                    });

                }*//*
            }
*/

        }
       /* if (IsChatApplicable.equals("")) {
            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGetEnvJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }*/


        /*if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUSerImageJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(CallListActivity.this, msg);

                }
            });
        } else {
            ut.displayToast(CallListActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }*/


        SetListener();


        /*Intent myIntent = new Intent(this,
                PaidLocationFusedLocationTracker1.class);
        startService(myIntent);*/
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


    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);


        img_calender = (ImageView) findViewById(R.id.img_calender);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        app_version = (TextView) findViewById(R.id.txt_app_version);
        img_userpic = (SimpleDraweeView) findViewById(R.id.img_userpic);


        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getSupportActionBar().setTitle("Close");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getSupportActionBar().setTitle("Open");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        String currentVersion;
        try {
            currentVersion = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            currentVersion = "0";
        }
        app_version.setText(currentVersion);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_birthday_cnt = (TextView) findViewById(R.id.tv_birthday_cnt);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);

        layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
        tv_notification_cnt = (TextView) findViewById(R.id.tv_notification_cnt);
        tv_meeting_cnt = (TextView) findViewById(R.id.tv_meeting_cnt);

        ly_team = (LinearLayout) findViewById(R.id.ly_team);
        ls_Team = (ListView) findViewById(R.id.ls_Team);
        tv_team_mem_cnt = (TextView) findViewById(R.id.tv_team_mem_cnt);
        tv_team_mem_cnt1 = (TextView) findViewById(R.id.tv_team_mem_cnt1);

        tv_opportunities = (TextView) findViewById(R.id.tv_opportunities);
        tv_overdue_opp = (TextView) findViewById(R.id.tv_overdue_opp);
        tv_yesterday_opp = (TextView) findViewById(R.id.tv_yesterday_opp);
        tv_today_opp = (TextView) findViewById(R.id.tv_today_opp);
        tv_callagain_opp = (TextView) findViewById(R.id.tv_callagain_opp);
        tv_tommorow_oppportunity = (TextView) findViewById(R.id.tv_tommorow_oppportunity);
        tv_this_week_opp = (TextView) findViewById(R.id.tv_this_week_opp);
        tv_new_opp = (TextView) findViewById(R.id.tv_new_opp);
        tv_revived_opp = (TextView) findViewById(R.id.tv_revived_opp);
        // view_Enquiry =  findViewById(R.id.view_Enquiry);


        textview_opportunity_Overdue = (TextView) findViewById(R.id.opportunity_Overdue);
        textview_opportunity_yesterday = (TextView) findViewById(R.id.opportunity_yesterday);
        textview_opportunity_today = (TextView) findViewById(R.id.opportunity_today);
        textview_opportunity_callagain = (TextView) findViewById(R.id.opportunity_callagain);

        ls_appointments = (LinearLayout) findViewById(R.id.ls_appointments);
        tv_appointments_cnt = (TextView) findViewById(R.id.tv_appointments_cnt);

        tv_collection_cnt = (TextView) findViewById(R.id.tv_collection_cnt);
        tv_new_collection = (TextView) findViewById(R.id.tv_new_collection);
        tv_new_feedback = (TextView) findViewById(R.id.tv_new_feedback);

        tv_overdue_collection = (TextView) findViewById(R.id.tv_overdue_collection);
        tv_today_overdue_collection = (TextView) findViewById(R.id.tv_today_overdue_collection);
        tv_week_overdue_collection = (TextView) findViewById(R.id.tv_week_overdue_collection);
        tv_tomrrow_overdue_collection = (TextView) findViewById(R.id.tv_tomrrow_overdue_collection);

        tv_callrating_cnt = (TextView) findViewById(R.id.tv_callrating_cnt);
        tv_hot_call = (TextView) findViewById(R.id.tv_hot_call);
        tv_warm_call = (TextView) findViewById(R.id.tv_warm_call);
        tv_warm_call_value = (TextView) findViewById(R.id.tv_warm_call_value);
        tv_hot_call_value = (TextView) findViewById(R.id.tv_hot_call_value);
        appointmentArrayList = new ArrayList<Appointment>();
        ls = new ArrayList<MyTeamBean>();

        layloadmore = (TextView) findViewById(R.id.layloadmore);
        rly_birthday = (RelativeLayout) findViewById(R.id.rly_birthday);
        rly_notification = (RelativeLayout) findViewById(R.id.rly_notification);
        rel_calender = (RelativeLayout) findViewById(R.id.rel_calender);
        rly_meeting = (RelativeLayout) findViewById(R.id.rly_meeting);
        rel_call_list = (RelativeLayout) findViewById(R.id.rel_call_list);
        len_cal = (LinearLayout) findViewById(R.id.len_cal);


        txtteam = (TextView) findViewById(R.id.txtteam);


        lay_opportunity = (LinearLayout) findViewById(R.id.lay_opportunity);
        lay_overdue_opp = (LinearLayout) findViewById(R.id.lay_overdue_opp);
        lay_today_opp = (LinearLayout) findViewById(R.id.lay_today_opp);
        lay_Tommorow_opp = (LinearLayout) findViewById(R.id.lay_Tommorow_opp);
        lay_this_week_opp = (LinearLayout) findViewById(R.id.lay_this_week_opp);
        lay_collection = (LinearLayout) findViewById(R.id.lay_collection);
        lay_collection_view = (LinearLayout) findViewById(R.id.lay_collectionview);
        if (IsCollectionApplicable.equalsIgnoreCase(Salesmodulevisible)) {
            lay_collection_view.setVisibility(View.VISIBLE);
        } else {
            lay_collection_view.setVisibility(View.GONE);
        }
        lay_new_collection = (LinearLayout) findViewById(R.id.lay_new_collection);
        lay_new_feedback = (LinearLayout) findViewById(R.id.lay_new_feedback);

        lay_overdue_collection = (LinearLayout) findViewById(R.id.lay_overdue_collection);
        lay_today_overdue_collection = (LinearLayout) findViewById(R.id.lay_today_overdue_collection);
        lay_tomorrow_overdue_collection = (LinearLayout) findViewById(R.id.lay_tomorrow_overdue_collection);
        lay_week_overdue_collection = (LinearLayout) findViewById(R.id.lay_week_overdue_collection);
        lay_callrating = (LinearLayout) findViewById(R.id.lay_callrating);
        lay_hot_call = (LinearLayout) findViewById(R.id.lay_hot_call);
        lay_warm_call = (LinearLayout) findViewById(R.id.lay_warm_call);
        lay_overdue_feedback = (LinearLayout) findViewById(R.id.lay_overdue_feedback);
        lay_today_overdue_feedback = (LinearLayout) findViewById(R.id.lay_today_overdue_feedback);
        lay_tomorrow_overdue_feedback = (LinearLayout) findViewById(R.id.lay_tomorrow_overdue_feedback);
        lay_week_overdue_feedback = (LinearLayout) findViewById(R.id.lay_week_overdue_feedback);
        lay_feedback = (LinearLayout) findViewById(R.id.lay_feedback);

        lay_new_opp = (LinearLayout) findViewById(R.id.lay_new_opp);
        lay_revived_opp = (LinearLayout) findViewById(R.id.lay_revived_opp);
        tv_overdue_feedback = (TextView) findViewById(R.id.tv_overdue_feedback);
        tv_today_overdue_feedback = (TextView) findViewById(R.id.tv_today_overdue_feedback);
        tv_week_overdue_feedback = (TextView) findViewById(R.id.tv_week_overdue_feedback);
        tv_tomrrow_overdue_feedback = (TextView) findViewById(R.id.tv_tomrrow_overdue_feedback);
        tv_feedback_cnt = (TextView) findViewById(R.id.tv_feedback_cnt);
        txt_target_achivment = (TextView) findViewById(R.id.txt_target_achivment);

        googleProgressBar = (ProgressBar) findViewById(R.id.google_progress);
        edt_search_firm = (EditText) findViewById(R.id.edt_search_firm);
        img_search_firm = (ImageView) findViewById(R.id.img_search_firm);
        img_opportunity = (ImageView) findViewById(R.id.img_opportunity);
        img_appointment = (ImageView) findViewById(R.id.img_appointment);
        img_opportunity.setColorFilter(getResources().getColor(R.color.magneta), PorterDuff.Mode.SRC_ATOP);
        img_appointment.setColorFilter(getResources().getColor(R.color.appontmentcolor), PorterDuff.Mode.SRC_ATOP);

        len_update = (LinearLayout) findViewById(R.id.len_update);
        len_search = (LinearLayout) findViewById(R.id.len_search);
        txt_update_msg = (TextView) findViewById(R.id.txt_update_msg);
        txt_update = (TextView) findViewById(R.id.txt_update);

        ln_enquiry = (LinearLayout) findViewById(R.id.lay_enquiry);
        txt_enquirycount = (TextView) findViewById(R.id.tv_enquiry);
        img_inquiry = (ImageView) findViewById(R.id.img_enquiry);
        txt_phncalltxt = findViewById(R.id.txt_phncalltxt);
        tv_phncall_cnt = (TextView) findViewById(R.id.tv_phncall_cnt);
        layout_phncalls = findViewById(R.id.layout_phncalls);

        Intent intent1 = new Intent();
        intent1 = getIntent();
        App_version = intent1.getStringExtra("version");
        if (App_version == null) {
            len_update.setVisibility(View.GONE);
        } else {
            len_update.setVisibility(View.VISIBLE);
            txt_update_msg.setText(App_version);
            txt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=vcrm7.vritti.com.vcrm7"));
                    startActivity(intent);
                }
            });

        }
        //Calender Demo
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        //   cal_month_copy = (GregorianCalendar) cal_month.clone();
        //  cal_adapter = new CalendarAdapter(this, cal_month, CalendarCollection.date_collection_arr);


        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageView previous = (ImageView) findViewById(R.id.ib_prev);


        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        ImageView next = (ImageView) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
                String selectedGridDate = CalendarAdapter.day_string
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v, position);

                ((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, CallListActivity.this);
            }

        });


        if (getIntent().hasExtra("homeopp")) {
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        clickopportunitygetCallListData(UserMasterId);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


    }

    private void SetListener() {

        layout_phncalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallListActivity.this, CRM_CallLogList.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        ls_Team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {

                }


                final String clickTeamMem_Id = ls.get(position).getUserMasterId();
                String clickTeamMem_Name = ls.get(position).getUserName();


                String tv_opportunity = tv_opportunities.getText().toString();
                if (tv_opportunity.equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                } else {
                    if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                // clickopportunitygetCallListData(clickTeamMem_Id);
                                clickopportunitygetCallListData(clickTeamMem_Id);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }


            }
        });


        img_search_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Firmname = edt_search_firm.getText().toString();
                UpdatListFiter(Firmname);
            }
        });
        txtteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_team_mem_cnt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CallListActivity.this, "No Record Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, TeamMemberActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        ln_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CallListActivity.this, ActivityEnquiryList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });
         rel_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avail) {
                    avail = false;
                    rel_call_list.setVisibility(View.VISIBLE);
                    len_cal.setVisibility(View.GONE);

                    img_calender.setImageResource(R.drawable.calender_icon);
                   // OpportunityUpdatList();
                } else {
                    rel_call_list.setVisibility(View.GONE);
                    len_cal.setVisibility(View.VISIBLE);
                    img_calender.setImageResource(R.drawable.list_icon);
                    avail = true;

                }
            }
        });

         layloadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadmore = layloadmore.getText().toString();
                System.out.println("Staticloadmore" + loadmore);
                getCallListDatanext();

            }
        });


        lay_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {

                }

                String tv_opportunity = tv_opportunities.getText().toString();
                if (tv_opportunity.equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {

                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "main_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                   /* if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                clickopportunitygetCallListData(UserMasterId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }*/

                }
            }
        });

        lay_new_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_new_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "new_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });


        textview_opportunity_Overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_overdue_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "overdue_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        textview_opportunity_yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_yesterday_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "yesterday_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        textview_opportunity_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_today_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "today_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        textview_opportunity_callagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_callagain_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "callagain_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_revived_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_revived_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "revived_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });


        lay_Tommorow_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_tommorow_oppportunity.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "Tommorow_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                }
            }
        });
        lay_this_week_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_this_week_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "this_week_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_collection_cnt.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    //  Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "collection");
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "overdue_collection");
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        lay_today_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_today_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "today_overdue_collection");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "today_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_tomorrow_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_tomrrow_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "tomorrow_overdue_collection");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    /*intent.putExtra("Opportunity", "tomorrow_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }
            }
        });
        lay_week_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_week_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "week_overdue_collection");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "week_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        lay_new_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_new_collection.getText().toString().equalsIgnoreCase("0.00 T (0)") ||
                        tv_new_collection.getText().toString().equalsIgnoreCase("0/0.00 (0)")) {
                    Toast.makeText(CallListActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "newCollection");
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "new_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                }
            }
        });

        lay_hot_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_hot_call.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Call Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "hot_call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_warm_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_warm_call.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "warm_call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        lay_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_feedback_cnt.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);
                }
            }
        });

        lay_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_today_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_today_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "today_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        lay_tomorrow_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_tomrrow_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "tomorrow_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });


        lay_week_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                if (tv_week_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CallListActivity.this, "No Feedback  Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CallListActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "week_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }

    private void UpdatList() {  //,ChatRoomId,ChatCount
        partialCallLists.clear();

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,isPartial,CallType,CallStatus,ProspectId,LatestRemark,Mobile,CityName,ContactName,Product,SourceName," +
                "EmailId,FamilyDesc,Address" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL + "";
        Cursor cur = sql.rawQuery(query, null);


        if (cur.getCount() > 0) {
            lsCall_list.setVisibility(View.VISIBLE);

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                // partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                callid = cur.getString(cur.getColumnIndex("CallId"));
                partialCallList.setCallId(callid);

                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                //   partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                //  partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Telephone")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                callstatus = cur.getString(cur.getColumnIndex("CallStatus"));
                partialCallList.setCallStatus(callstatus);
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setFamilyDesc(cur.getString(cur.getColumnIndex("FamilyDesc")));
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setSource(cur.getString(cur.getColumnIndex("SourceName")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);


                }
            }
        } else {
            lsCall_list.setVisibility(View.GONE);
            //Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_SHORT).show();
        }


    }

    private void addView_CallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CallListActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final int position1 = i;

        convertView = layoutInflater.inflate(R.layout.crm_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        img_contact = (ImageView) convertView.findViewById(R.id.img_contact);
        img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        tv_mailid = (TextView) convertView.findViewById(R.id.city);//tv_source
        tv_mailid.setVisibility(View.GONE);
        TextView tv_source = (TextView) convertView.findViewById(R.id.tv_source);

        //Add contact Code
        len_contact = (LinearLayout) convertView.findViewById(R.id.len_contact);
        len_show_calldetails = (LinearLayout) convertView.findViewById(R.id.len_show_calldetails);

        edt_contact_name = (EditText) convertView.findViewById(R.id.edt_contact_name);
        edt_designation = (EditText) convertView.findViewById(R.id.edt_designation);
        spinner_department = (Spinner) convertView.findViewById(R.id.spinner_department);
        edt_emailid = (EditText) convertView.findViewById(R.id.edt_emailid);
        edt_mobile = (EditText) convertView.findViewById(R.id.edt_mobile);
        edt_offfice = (EditText) convertView.findViewById(R.id.edt_offfice);
        checkbox_primary_contact = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_primary_contact);
        txt_save = (TextView) convertView.findViewById(R.id.txt_save);
        txt_reset = (TextView) convertView.findViewById(R.id.txt_reset);

        txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
        txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);


        Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
        String mail = partialCallLists.get(i).getEmailid();
        if (!(mail.equalsIgnoreCase(""))) {// || mail == null)
            tv_mailid.setVisibility(View.VISIBLE);
            tv_mailid.setText(mail);
        } else {
            tv_mailid.setVisibility(View.GONE);
        }

        txt_chat.setVisibility(View.VISIBLE);

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#5d9cec"));
                img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            } else {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.que);

            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        txtactiondatetime.setText(partialCallLists.get(i)
                .getActiondatetime());
        String Partial = partialCallLists.get(i).getIsPartial();
        String remark = partialCallLists.get(i).getLatestRemark();
        if (remark.equalsIgnoreCase("") || remark.equalsIgnoreCase(" ") ||
                remark.equalsIgnoreCase(null) || remark.equalsIgnoreCase("null")) {
            tv_latestremark.setText("");
        } else {
            tv_latestremark.setText(" For " + partialCallLists.get(i).getLatestRemark());
        }

        //tv_latestremark.setText(" For " + partialCallLists.get(i).getLatestRemark());

        String Mobile = partialCallLists.get(i).getMobileno();
        String City = partialCallLists.get(i).getCityname();
        String Contactname = partialCallLists.get(i).getContactName();
        String Product = partialCallLists.get(i).getProduct();
        String familydesc = partialCallLists.get(i).getFamilyDesc();
        String Source = partialCallLists.get(i).getSource();
        String Concatdata = "";
        if (City.equalsIgnoreCase("") || City.equalsIgnoreCase(" ") ||
                City.equalsIgnoreCase(null) || City.equalsIgnoreCase("null")) {
            Concatdata = "-" + familydesc + "(" + Product + ")(" + Contactname + "-" + Mobile + ")";

        } else if (familydesc.equalsIgnoreCase("") || familydesc.equalsIgnoreCase(null) ||
                familydesc.equalsIgnoreCase(" ") || familydesc.equalsIgnoreCase("null")) {
            Concatdata = City + "-" + "(" + Product + ")(" + Contactname + "-" + Mobile + ")";

        } else if (Product.equalsIgnoreCase("") || Product.equalsIgnoreCase(null) ||
                Product.equalsIgnoreCase(" ") || Product.equalsIgnoreCase("null")) {
            Concatdata = City + "-" + familydesc + "(" + Contactname + "-" + Mobile + ")";

        } else if (Contactname.equalsIgnoreCase("") || Contactname.equalsIgnoreCase(null) ||
                Contactname.equalsIgnoreCase(" ") || Contactname.equalsIgnoreCase("null")) {
            Concatdata = City + "-" + familydesc + "(" + Product + ")(" + "-" + Mobile + ")";

        } else if (Mobile.equalsIgnoreCase("") || Mobile.equalsIgnoreCase(null) ||
                Mobile.equalsIgnoreCase(" ") || Mobile.equalsIgnoreCase("null")) {
            Concatdata = City + "-" + familydesc + "(" + Product + ")(" + Contactname + "-" + ")";
        } else {
            Concatdata = City + "-" + familydesc + "(" + Product + ")(" + Contactname + "-" + Mobile + ")";
        }


        tvcall.setText(Concatdata);



        Address=partialCallLists.get(position).getAddress();
        txtaddress.setText(Address);

       /* Lat=partialCallLists.get(position).getLat();
        Long=partialCallLists.get(position).getLong();
       */
        Geocoder coder = new Geocoder(CallListActivity.this);
        try {
            address = coder.getFromLocationName(Address, 5);
            android.location.Address location = address.get(0);
            Lat = location.getLatitude();
            Lng = location.getLongitude();

        }catch (Exception e){
            e.printStackTrace();
        }

        txtaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lat==0&Lng==0){

                }else {

                    try {
                        Address=partialCallLists.get(position).getAddress();
                        Geocoder coder = new Geocoder(CallListActivity.this);
                        address = coder.getFromLocationName(Address, 5);
                        android.location.Address location = address.get(0);
                        Lat = location.getLatitude();
                        Lng = location.getLongitude();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    String geoUri = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng + " (" + partialCallLists.get(i).getFirmname() + ")";
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);

                   /* String uriMap = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                    startActivity(intent);
*/
                    }
                }
            }
        });



        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position1).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(i).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(CallListActivity.this,CallDetailsHistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) );

                String callId = partialCallLists.get(i).getCallId();
                String Partial = partialCallLists.get(i).getIsPartial();


                if (Partial.equalsIgnoreCase("P")) {
                /*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                */


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                getCallListData_Full(partialCallLists.get(i).getCallId(), i);


                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(CallListActivity.this, ChatMainActivity.class));
                Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                Call_CallType = partialCallLists.get(i).getCallType();

                Intent intent = new Intent(CallListActivity.this, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        img_appotunity_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int p = (int) v.getTag();
                //  Log.i("TAG", "The index is" + p);

                Intent intent = new Intent(CallListActivity.this, CallRatingActivity.class);
                intent.putExtra("callid", partialCallLists.get(position1).getCallId());
                intent.putExtra("callstatus", partialCallLists.get(position1).getCallStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
                    UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

                    Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                    Call_CallType = partialCallLists.get(i).getCallType();
                    Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    String Assigned_By = partialCallLists.get(i).getAssignedby();

                    //ChatRoomId=partialCallLists.get(i).getChatRoomId();
                    //int  ChatCount= Integer.parseInt(partialCallLists.get(i).getChatCount());
                    Intent intent = new Intent(CallListActivity.this,
                            MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Opportunity");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(CallListActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Call_Department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_contact_name.setText("");
                edt_designation.setText("");
                edt_mobile.setText("");
                edt_emailid.setText("");
                edt_offfice.setText("");
                checkbox_primary_contact.setChecked(false);


            }
        });


        for (int position = 0; position < partialCallLists.size(); position++) {
            convertView.setTag(position);
        }

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Integer) {
                    pos = (Integer) v.getTag();
                }

                Call_Contactname = edt_contact_name.getText().toString();
                Call_Designation = edt_designation.getText().toString();
                Call_Mobile = edt_mobile.getText().toString();
                Call_Email = edt_emailid.getText().toString();
                Call_Office = edt_offfice.getText().toString();
                Call_Callid = partialCallLists.get(pos).getCallId();
                Call_CallType = partialCallLists.get(pos).getCallType();
                CallData();
                JSONObject jsoncontactadd = new JSONObject();

                try {
                    jsoncontactadd.put("CallId", Call_Callid);
                    jsoncontactadd.put("CallType", Call_CallType);
                    jsoncontactadd.put("ProspectId", Call_ProspectId);
                    jsoncontactadd.put("ContactName", Call_Contactname);
                    jsoncontactadd.put("Designation", Call_Designation);
                    jsoncontactadd.put("DeptRoleName", Call_Department);
                    jsoncontactadd.put("EmailId", Call_Email);
                    jsoncontactadd.put("TeleNo", Call_Office);
                    jsoncontactadd.put("MobileNo", Call_Mobile);
                    jsoncontactadd.put("IsPrimaryContact", Check_Value);

                    Addcalljson = jsoncontactadd.toString();

                    System.out.println("Contact list : " + jsoncontactadd.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Addcalljson = Addcalljson.toString();
                Addcalljson = Addcalljson.replaceAll("\\\\", "");

                if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostAddContactJSON().execute(Addcalljson);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }


            }
        });
        checkbox_primary_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkbox_primary_contact.isChecked()) {
                    Check_Value = Boolean.parseBoolean("1");
                } else {
                    Check_Value = Boolean.parseBoolean("0");
                }
            }
        });


        lsCall_list.addView(convertView);


    }


    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date datestop = sdf.parse(datedb);


            return_value = datedb;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("crm_dialog_action", "crm_dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(datedb);
            return_value = "Today" + tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(datedb);
            return_value = "Yesterday" + tm[0];
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(datedb);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {
            String k = datedb.substring(0, datedb.length() - 15);
            String tm[] = splitfrom(return_value);
            return k + tm[0];
        }

    }

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    public void getCallListData() {
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String currentDateandTime = dfDate.format(new Date());
            Date cdate = dfDate.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cdate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date newDate1 = calendar.getTime();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();

            jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", dfDate.format(newDate1));
            jsonObj.put("value2", dfDate.format(newDate2));
            jsonObj.put("Operator", "bet");


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("IsPartial");
            jsonObj.put("IsSet", false);
            jsonObj.put("value1", "P");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanDataURLJSON().execute();
    }



    class DownloadCommanDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, FinalObj, CallListActivity.this);
                if (res != null) {

                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    response = response.replace("\\ ", "\"");
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response.toString());
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_PARTIAL, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }
                        }
                        long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL, null, values);
                        Log.e("", "" + a);
                    }
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch
            (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //    googleProgressBar.setVisibility(View.GONE);
            dismissProgressDialog();
            if (response.contains("CallId")) {
                layloadmore.setVisibility(View.VISIBLE);
                UpdatList();


            } else if (response.contains("Error")) {
                Toast.makeText(CallListActivity.this, "Server error", Toast.LENGTH_LONG).show();

            } else {
                //  Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                layloadmore.setVisibility(View.GONE);

            }

        }

    }


    private void dismissProgressDialog() {
        if (googleProgressBar != null && googleProgressBar.isShown()) {
            googleProgressBar.setVisibility(View.GONE);
        }
    }


    public void getCallListDatanext() {
        FinalObj = "";
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        String query = "SELECT distinct " +
                "NextActionDateTime" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL + " Order by NextActionDateTime desc";
        Cursor cur = sql.rawQuery(query, null);

        String currentDateandTime, d, formatdate;
        Date newDate1 = null;
        Date newDate2 = null;
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            d = cur.getString(cur.getColumnIndex("NextActionDateTime"));//27-Feb-2017  09:00 am
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");

            try {
                Date date = sdf.parse(d);


                currentDateandTime = dfDate.format(date);
                String cdate = dfDate.format(date);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(date);
                calendar1.add(Calendar.DAY_OF_YEAR, 14);
                newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", currentDateandTime);
                jsonObj.put("value2", "");
                jsonObj.put("Operator", ">");


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            try {
                currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);


                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 14);
                newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", currentDateandTime);
                jsonObj.put("value2", "");//dfDate.format(newDate2)
                jsonObj.put("Operator", ">");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        if (isnet()) {
            new StartSession(CallListActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadLoadmoreCommanDataOpportunityURLJSON().execute(FinalObj);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });


        }

    }


    class DownloadCommanDataNextURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0], CallListActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);
                String msg = "";

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
                int count = c.getCount();

                String columnName, columnValue;
                String callid, callidvalue = "";
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("CallId")) {
                            callid = columnName;
                            callidvalue = jorder.getString("CallId");

                        }

                        String jsonDOB = jorder.getString("NextActionDateTime");
                        if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                            jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(")
                                    + 1, jsonDOB.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonDOB);
                            DOBDate = new Date(DOB_date);
                            jsonDOB = sdf.format(DOBDate);
                            values.put(columnName, jsonDOB);

                        } else if (columnName.equalsIgnoreCase("isPartial")) {

                            values.put(columnName, "P");
                        } else if (columnName.equalsIgnoreCase("Mobile")) {
                            if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                values.put(columnName, "No Contact Available");
                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }

                        } else if (columnName.equalsIgnoreCase("ContactName")) {
                            if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                values.put(columnName, "");
                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }
                    if (cf.getCallIdcount(callidvalue) > 0) {

                    } else {
                        long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL, null, values);
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
            progressDialog.dismiss();
            // dismissProgressDialog();
            // progressHUD.dismiss();
            if (response.contains("CallId")) {
                UpdatList();

            } else if (response.contains("Error")) {

            }


            Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
            int count1 = c1.getCount();
            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOpportunitiesJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
            progressDialog.dismiss();
            // dismissProgressDialog();
            //progressHUD.dismiss();
        }

    }

    class DownloadUserTypeFromServer extends AsyncTask<String, Void, String> {
        String res, response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetUserType
                        + "?usermasterid=" + URLEncoder.encode(UserMasterId, "UTF-8");


                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                // response = response.substring(1, response.length() - 1);

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_User_Type, null,
                        null);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_User_Type, null);
                int count = c.getCount();
                String columnName, columnValue;


                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("Code")) {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            UserType = columnValue;
                        } else if (columnName.equalsIgnoreCase("UserName")) {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            UserName = columnValue;
                        } else {

                        }
                    }

                    long a = sql.insert(db.TABLE_User_Type, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString(WebUrlClass.USERINFO_USER_TYPE, UserType);
            editor.commit();

            //  Toast.makeText(CallListActivity.this,"Download user from server api call",Toast.LENGTH_SHORT).show();
            getCallListData();

        }

    }


    class DownloadCallRatingJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_CallRating +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_CALL_RATING, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_RATING, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_CALL_RATING, null, values);
                        Log.e("", "" + a);

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
            if (response != null) {

                UpdateCallRating();
            }

            if (isnet()) {

                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFeedbackJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }


           /* if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        Toast.makeText(CallListActivity.this,"Bday api called",Toast.LENGTH_SHORT).show();
                        new DownloadBirthdayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }*/

           /* if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTeamDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }*/


        }

    }

    class DownloadAppointmentJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Appointment
                        + "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_APPOINTMENT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_APPOINTMENT, null, values);
                    Log.e("DATA", "" + a);

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
            if (response != null) {

                UpdateAppointment();
            }
            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        // Toast.makeText(CallListActivity.this,"Collection api called",Toast.LENGTH_SHORT).show();
                        new DownloadCheckSalesInstalled().execute();
                        // new DownloadCollectionJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        }

    }

    class DownloadOpportunitiesJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {

               String url = CompanyURL + WebUrlClass.api_Opportunities + "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

               // String url = CompanyURL + WebUrlClass.api_Opportunities + "?UserMasterId=4a23812d-1ccf-4859-ad8e-2f3bd33c5247";

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_OPPORTUNITIES, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_OPPORTUNITIES, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }
                    long a = sql.insert(db.TABLE_OPPORTUNITIES, null, values);
                    Log.e("opportunity", "" + a);
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            //progressHUD.dismiss();
            //   dismissProgressDialog();
            progressDialog.dismiss();
            if (response != null) {

                UpdateOpportunities();


            } else {
                Toast.makeText(CallListActivity.this, "Unable to fetch opportunity data", Toast.LENGTH_SHORT).show();
            }

           /* if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        // Toast.makeText(CallListActivity.this,"Appointment api called",Toast.LENGTH_SHORT).show();
                        new DownloadAppointmentJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
*/
            progressDialog.dismiss();
            // progressHUD.dismiss();
            //dismissProgressDialog();
        }

    }

    class DownloadCollectionJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Collection +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_COLLECTION, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_COLLECTION, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_COLLECTION, null, values);

                    }
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                UpdateCollection();
            }
            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //Toast.makeText(CallListActivity.this,"call Rating api called",Toast.LENGTH_SHORT).show();
                        new DownloadCallRatingJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        }

    }

    class DownloadTeamJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {

            //Toast.makeText(CallListActivity.this,"Team api call",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_TeamMembers +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_Team_Member, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Team_Member, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Team_Member, null, values);
                    Log.e("", "" + a);

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
            if (!(response == null || response.equalsIgnoreCase("[]"))) {
                getMyTeamData();
            } else {

            }

            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //  Toast.makeText(CallListActivity.this,"Enquiry api called",Toast.LENGTH_SHORT).show();
                        //  new DownloadEnquiryAssigned().execute();
                        new DownloadSubTeamJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(CallListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            UpdateOpportunities();
            UpdateFeedback();

        }

    }

    private void UpdateOpportunities() {
        String query = "SELECT * FROM " + db.TABLE_OPPORTUNITIES;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();

            String a = cur.getString(cur.getColumnIndex("Assigned"));
            String t = cur.getString(cur.getColumnIndex("Tomorrow"));

            String Overdue = cur.getString(cur.getColumnIndex("Overdue"));
            String Yesterday = cur.getString(cur.getColumnIndex("Yesterday"));

            String Today = cur.getString(cur.getColumnIndex("Today"));
            String CallAgain = cur.getString(cur.getColumnIndex("CallAgain"));
            String Revived = cur.getString(cur.getColumnIndex("Revived"));


            tv_opportunities.setText(cur.getString(cur.getColumnIndex("Assigned")));
            tv_overdue_opp.setText(Overdue);
            tv_yesterday_opp.setText(Yesterday);
            tv_today_opp.setText(Today);
            tv_callagain_opp.setText(CallAgain);
            tv_tommorow_oppportunity.setText(t);
            tv_this_week_opp.setText(cur.getString(cur.getColumnIndex("ThisWeek")));
            tv_new_opp.setText(cur.getString(cur.getColumnIndex("New")));
            tv_revived_opp.setText(Revived);
        }
        UpdateAppointment();
    }

    private void UpdateAppointment() {
        appointmentArrayList.clear();
        String query = "SELECT FirmName,FormattedAppointmentTime,AppointmentDate,CityName FROM " + db.TABLE_APPOINTMENT;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            tv_appointments_cnt.setText(cur.getCount() + "");
            cur.moveToFirst();
           /* ls_appointments=(LinearLayout) findViewById(R.id.ls_appointments);
            tv_appointments_cnt = (TextView) findViewById(R.id.tv_appointments_cnt);*/
            appointment = new Appointment();
            appointment.setFirmName(cur.getString(cur.getColumnIndex("FirmName")));
            appointment.setAppointmentDate(cur.getString(cur.getColumnIndex("AppointmentDate")));
            appointment.setFormattedAppointmentTime(cur.getString(cur.getColumnIndex("FormattedAppointmentTime")));
            appointment.setCityName(cur.getString(cur.getColumnIndex("CityName")));
            appointmentArrayList.add(appointment);
        }/* else {
            tv_appointments_cnt.setText(0);
        }*/
        ls_appointments.removeAllViews();
        if (appointmentArrayList.size() > 0) {
            for (int i = 0; i < appointmentArrayList.size(); i++) {
                addView(i);
            }
        }
        UpdateCollection();
    }


    private void addView(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CallListActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.crm_custom_appointments,
                null);


        tv_firmname = (TextView) baseView.findViewById(R.id.tv_firmname);
        tv_date_time = (TextView) baseView.findViewById(R.id.tv_date_time);
        tv_city = (TextView) baseView.findViewById(R.id.tv_city);
        tv_firmname.setText(appointmentArrayList.get(i).getFirmName());
        tv_date_time.setText(appointmentArrayList.get(i).getAppointmentDate() +
                " " + appointmentArrayList.get(i).getFormattedAppointmentTime());
        tv_city.setText(appointmentArrayList.get(i).getCityName());
        ls_appointments.addView(baseView);
    }

    private void UpdateCollection() {
        String query = "SELECT Tomorrow,TotalCollection,OverdueCollection,TodayCollection,New,TomorrowCollection,ThisWeekCollection,Assigned,Overdue,Today,ThisWeek FROM " + db.TABLE_COLLECTION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String a = cur.getString(cur.getColumnIndex("TotalCollection"));
            String b = cur.getString(cur.getColumnIndex("OverdueCollection"));
            String c = cur.getString(cur.getColumnIndex("TodayCollection"));
            String d = cur.getString(cur.getColumnIndex("ThisWeekCollection"));
            String e = cur.getString(cur.getColumnIndex("TomorrowCollection"));
            String Assign = cur.getString(cur.getColumnIndex("Assigned"));
            String New = cur.getString(cur.getColumnIndex("New"));

            String Overdue = cur.getString(cur.getColumnIndex("Overdue"));
            String Today = cur.getString(cur.getColumnIndex("Today"));
            String ThisWeek = cur.getString(cur.getColumnIndex("ThisWeek"));
            String Tomorrow = cur.getString(cur.getColumnIndex("Tomorrow"));
            int index = Assign.indexOf("/");
            int index1 = Overdue.indexOf("/");
            int index2 = Today.indexOf("/");
            int index3 = ThisWeek.indexOf("/");
            int index4 = Tomorrow.indexOf("/");
            int index5 = New.indexOf("/");
            String AssignCount = Assign.substring(0, index);
            String OverdueCount = Overdue.substring(0, index1);
            String TodayCount = Today.substring(0, index2);
            String ThisWeekCount = ThisWeek.substring(0, index3);
            String TomorrowCount = Tomorrow.substring(0, index4);
            String NewCount = New.substring(0, index5);


            tv_collection_cnt.setText(a + " " + "(" + AssignCount + ")");
            tv_new_collection.setText(New + " " + "(" + NewCount + ")");
            tv_overdue_collection.setText(b + " " + "(" + OverdueCount + ")");
            tv_today_overdue_collection.setText(c + " " + "(" + TodayCount + ")");
            tv_week_overdue_collection.setText(d + " " + "(" + ThisWeekCount + ")");
            tv_tomrrow_overdue_collection.setText(e + " " + "(" + TomorrowCount + ")");

        }
        UpdateFeedback();
        UpdateCallRating();
    }

    private void UpdateEnuiry() {
        String query = "SELECT * FROM " + db.TABLE_ENQUIRY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            int a = cur.getCount();

            txt_enquirycount.setText("" + a);
        } else {
            txt_enquirycount.setText("" + 0);
        }

    }

    private void UpdateCallRating() {
        String query = "SELECT HotCount,HotValue,WarmCount,WarmValue FROM "
                + db.TABLE_CALL_RATING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            //   tv_callrating_cnt.setText(cur.getString(cur.getColumnIndex("ThisWeekCollection")));
            int h = cur.getInt(cur.getColumnIndex("HotCount"));
            int w = cur.getInt(cur.getColumnIndex("WarmCount"));
            String hh = cur.getString(cur.getColumnIndex("HotValue"));
            String ww = cur.getString(cur.getColumnIndex("WarmValue"));
            int callrating = h + w;
           /* String t = h;
              String s = w;
              int totalrating = Integer.parseInt(t+s);*/

            tv_hot_call.setText(cur.getString(cur.getColumnIndex("HotCount")));
            tv_warm_call.setText(cur.getString(cur.getColumnIndex("WarmCount")));
            tv_warm_call_value.setText(cur.getString(cur.getColumnIndex("WarmValue")));
            tv_hot_call_value.setText(cur.getString(cur.getColumnIndex("HotValue")));
            tv_callrating_cnt.setText(String.valueOf(callrating));

        }
        /***************************************************************************************/
        //getMyTeamData();
    }

    private void UpdateMeeting() {
        String query = "SELECT * FROM " + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_meeting_cnt.setVisibility(View.VISIBLE);
            tv_meeting_cnt.setText(cur.getCount() + "");
        } else {
            tv_meeting_cnt.setText(0 + "");
        }


        UpdateNotification();
    }

    private void UpdateNotification() {
        String query = "SELECT * FROM " + db.TABLE_NOTIFICATION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_notification_cnt.setVisibility(View.VISIBLE);
            tv_notification_cnt.setText(cur.getCount() + "");
        } else {
            tv_notification_cnt.setText(0 + "");
        }

    }

    private void UpdateBirthdayList() {
        String query = "SELECT * FROM " + db.TABLE_BIRTHDAY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_birthday_cnt.setVisibility(View.VISIBLE);
        }
        tv_birthday_cnt.setText(cur.getCount() + "");
        UpdateMeeting();
    }

    private void getMyTeamData() {
        String query = "SELECT UserName," +
                "            UserMasterId," +
                "            UserLoginId," +
                "            Assigned," +
                "            Overdue FROM " + db.TABLE_Team_Member;
        Cursor cur = sql.rawQuery(query, null);
        ls = new ArrayList<MyTeamBean>();
        if (cur.getCount() > 0) {
            tv_team_mem_cnt.setText("" + cur.getCount());
            ly_team.setVisibility(View.VISIBLE);

            cur.moveToFirst();
            do {
                MyTeamBean bean = new MyTeamBean();
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setAssigned(cur.getString(cur.getColumnIndex("Assigned")));
                bean.setOverdue(cur.getString(cur.getColumnIndex("Overdue")));
                // bean.setPercent(cur.getString(cur.getColumnIndex("Percent")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                ls.add(bean);


              /*  String CREATE_Team_Member = "CREATE TABLE " + TABLE_Team_Member +
                        "(UserMasterId TEXT, UserLoginId TEXT,UserName TEXT,Assigned TEXT," +
                        " Overdue TEXT, Today TEXT ,Hot TEXT ,Collection TEXT,Tomorrow TEXT,Report TEXT)";*/
            } while (cur.moveToNext());

            teamAdapter = new MyTeamAdapter(CallListActivity.this, ls);
            ls_Team.setAdapter(teamAdapter);
            setListViewHeightBasedOnChildren(ls_Team);

            teamAdapter.notifyDataSetChanged();
            registerForContextMenu(ls_Team);
        } else {
            tv_team_mem_cnt.setText("");
        }
        UpdateBirthdayList();

    }

    public void getCallListData_Full(String cid, int position) {

        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String currentDateandTime = dfDate.format(new Date());
            Date cdate = dfDate.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cdate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date newDate1 = calendar.getTime();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();

            jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", dfDate.format(newDate1));
            jsonObj.put("value2", dfDate.format(newDate2));
            jsonObj.put("Operator", "bet");


        } catch (Exception e) {
            e.printStackTrace();
        }


        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanData_fullURLJSON().execute(position);
    }

    class DownloadCommanData_fullURLJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        int pos;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            pos1 = params[0];
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, FinalObj, CallListActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    pos = params[0];
                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            String jsonDt = jorder.getString("ExpectedCloserDate");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            }
                            if (columnName.equalsIgnoreCase("ExpectedCloserDate")) {
                                jsonDt = jsonDt.substring(jsonDt.indexOf("(") + 1, jsonDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDt);
                                DOBDate = new Date(DOB_date);
                                jsonDt = sdf.format(DOBDate);
                                values.put(columnName, jsonDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL, null, values);
                        Log.d("crm_dialog_action", "count " + a);
                    }
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    //CallData();
                    int count1 = c1.getCount();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

//            progressHUD.dismiss();
            progressDialog.dismiss();

            //dismissProgressDialog();


            if (response.equalsIgnoreCase("[]")) {

                if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            getCallListData();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });


                }
            } else {
                String Partial = partialCallLists.get(pos1).getIsPartial();

                /*if (db.getCallListcount(partialCallLists.get(pos).getCallId()) > 0) {
                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL, cv, "CallId=?",
                            new String[]{partialCallLists.get(pos).getCallId()});*/


                if (Partial.equalsIgnoreCase("P")) {

                    Intent intent = new Intent(CallListActivity.this,

                            CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(pos1).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(pos1).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(pos1).getCallType());
                    intent.putExtra("table", "Call");
                    intent.putExtra("ProspectId", partialCallLists.get(pos1).getPKSuspectId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            }
            //   googleProgressBar.setVisibility(View.GONE);
            // progressHUD.dismiss();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calllist, menu);
        return true;
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.action_menu);

        return super.onPrepareOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_menu) {
            dialog = new Dialog(CallListActivity.this);
            Window window = dialog.getWindow();
            dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
            if (Constants.type == Constants.Type.Vwb) {
                dialog.setContentView(R.layout.crm_lay_right_menu_new);
            } else {
                dialog.setContentView(R.layout.crm_lay_menu);

            }
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.TOP | Gravity.RIGHT;

            wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            txtHome = (TextView) dialog.findViewById(R.id.txtHome);
            txtTravelPlan = (TextView) dialog.findViewById(R.id.txtTravelPlan);
            txtoffline = (TextView) dialog.findViewById(R.id.txtofflinedata);//txtpromotional
            txtpromotional = (TextView) dialog.findViewById(R.id.txtpromotional);//txtpromotional

            // txtExecutivePerformance = (TextView) dialog.findViewById(R.id.txtExecutivePerformance);
            txtProspectMaster = (TextView) dialog.findViewById(R.id.txtProspectMaster);
            txtCallLogs = (TextView) dialog.findViewById(R.id.txtCallLogs);
            //  txtCRMReport = (TextView) dialog.findViewById(R.id.txt_CRMReport);
            //txt_clientmaster =(TextView ) dialog.findViewById(R.id.txt_clientmaster);
            // txtEntityMaster = (TextView) dialog.findViewById(R.id.txtEntityMaster);
            // txtCityMaster = (TextView) dialog.findViewById(R.id.txtCityMaster);
            //  txtBusinessSegMaster = (TextView) dialog.findViewById(R.id.txtBusinessSegMaster);
            //  txtTeritoryMaster = (TextView) dialog.findViewById(R.id.txtTeritoryMaster);
            //  txtReportlist = (TextView) dialog.findViewById(R.id.txtReportlist);
            txtEnquiryform = (TextView) dialog.findViewById(R.id.txtEnquiryform);
            txtpromotinalform = (TextView) dialog.findViewById(R.id.txtPromotinalForm);
            txt_provisional = (TextView) dialog.findViewById(R.id.txt_provisional);
            txtEnquiryFormSetting = (TextView) dialog.findViewById(R.id.txtEnquiryFormSetting);
            txt_chatroom = (TextView) dialog.findViewById(R.id.txt_chatroom);
            txtServiceReport = (TextView) dialog.findViewById(R.id.txt_service_report);
            //   txtEntityMaster = (TextView) dialog.findViewById(R.id.txt_entitymaster);
            txt_CRMReport = (TextView) dialog.findViewById(R.id.txt_CRMReport);

            txtHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, CallListActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            });


        /*    txtEntityMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, EntityMasterSelectionMainActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            });

            txtCRMReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, CRMDayEndReport.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            });*/

          /*  txt_clientmaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this,ClientMasterFilterActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            });
*/

            txtTravelPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, TravelPlanShowFormActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtProspectMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, ProspectFilterActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtCallLogs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, CRM_CallLogList.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtEnquiryform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, AddEnquiry.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
            txtpromotinalform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, PromotionalFormActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
            txt_provisional.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, AdvanceProvisionalReceiptDisplayActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
            txtEnquiryFormSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, PromotionalFormSettingActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });


            txt_chatroom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IsChatApplicable.equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        Intent intent = new Intent(CallListActivity.this, OpenChatroomActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CallListActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                    }


                }
            });
            txtoffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CallListActivity.this, ActivityOfflineData.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            txtpromotional.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CallListActivity.this, ActivityPromotionalFormselection.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            txt_CRMReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, CRMDayEndReport.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtServiceReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CallListActivity.this, ActivityServiceReportCustomerList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            dialog.show();

            return true;

        } else if (id == R.id.action_refresh) {
            if (isnet()) {

                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadCalenderDetailsData().execute();
                        clickopportunitygetCallListData(UserMasterId);

                        // new DownloadOpportunitiesJSON().execute();
                       // getCallListData();

                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }
                });


            }

        }

        return super.onOptionsItemSelected(item);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    class PostAddContactJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTAddContact;
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], CallListActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("BusinessAPI-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();


            Toast.makeText(CallListActivity.this, "Contact added successfully", Toast.LENGTH_LONG).show();
            len_contact.setVisibility(View.GONE);
        }

    }

    private void CallData() {
        String query = "SELECT ProspectId "
                + " FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            //   tv_callrating_cnt.setText(cur.getString(cur.getColumnIndex("ThisWeekCollection")));
            Call_ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));


        }

    }


    class DownloadContactCommanData_fullURLJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        int pos;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, FinalObj, CallListActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    pos = params[0];
                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            String jsonDt = jorder.getString("ExpectedCloserDate");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            }
                            if (columnName.equalsIgnoreCase("ExpectedCloserDate")) {
                                jsonDt = jsonDt.substring(jsonDt.indexOf("(") + 1, jsonDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDt);
                                DOBDate = new Date(DOB_date);
                                jsonDt = sdf.format(DOBDate);
                                values.put(columnName, jsonDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL, null, values);
                        Log.d("crm_dialog_action", "count " + a);
                    }
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    //  CallData();
                    int count1 = c1.getCount();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

//            progressHUD.dismiss();

//            dismissProgressDialog();

            progressDialog.dismiss();


           /* if (response.equalsIgnoreCase("[]")) {

                if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            getCallListData();
                        }
                    });


                }
            }*/ /*else {
                if (db.getCallListcount(partialCallLists.get(pos).getCallId()) > 0) {
                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL, cv, "CallId=?",
                            new String[]{partialCallLists.get(pos).getCallId()});


                    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(pos).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(pos).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(pos).getCallType());
                    intent.putExtra("table", "Call");

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
           */
            //    googleProgressBar.setVisibility(View.GONE);
            // progressHUD.dismiss();
        }

    }

    class POSTdeleteContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTdeleteContact;
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], CallListActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("BusinessAPI-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();


            Toast.makeText(CallListActivity.this, "Contact deleted successfully", Toast.LENGTH_LONG).show();
        }

    }

    class POSTEditSaveContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveEditContact;
            System.out.println("BusinessAPIURL-1 :" + Editcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], CallListActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("BusinessAPI-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();


            Toast.makeText(CallListActivity.this, "Contact update successfully", Toast.LENGTH_LONG).show();
            len_contact.setVisibility(View.GONE);
        }

    }


    class DownloadFeedbackJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //   showProgressDialog();*/
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Feedback
                        + "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Feedback, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Feedback, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                    }
                    long a = sql.insert(db.TABLE_Feedback, null, values);
                    Log.e("feedback", "" + a);
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

            // progressDialog.dismiss();
            if (response != null) {
                UpdateFeedback();

            } else {
                //Toast.makeText(CallListActivity.this,"No feedback count",Toast.LENGTH_SHORT).show();
            }

            if (ut.isNet(CallListActivity.this)) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTeamJSON().execute();
                        // new DownloadSubTeamJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(CallListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            // progressDialog.dismiss();
        }

    }

    private void UpdateFeedback() {
        String query = "SELECT  * FROM " + db.TABLE_Feedback;
        Cursor cur = sql.rawQuery(query, null);
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();

            String a = cur.getString(cur.getColumnIndex("Overdue"));
            tv_feedback_cnt.setText(cur.getString(cur.getColumnIndex("Assigned")));
            tv_overdue_feedback.setText(cur.getString(cur.getColumnIndex("Overdue")));
            tv_new_feedback.setText(cur.getString(cur.getColumnIndex("New")));
            tv_today_overdue_feedback.setText(cur.getString(cur.getColumnIndex("Today")));
            tv_tomrrow_overdue_feedback.setText(cur.getString(cur.getColumnIndex("Tomorrow")));
            tv_week_overdue_feedback.setText(cur.getString(cur.getColumnIndex("ThisWeek")));

        } else {

            // Toast.makeText(CallListActivity.this,"No Feedback count",Toast.LENGTH_SHORT).show();

            //  Toast.makeText(CallListActivity.this,"No Feedback count",Toast.LENGTH_SHORT).show();

            //  Toast.makeText(CallListActivity.this,"No Feedback count",Toast.LENGTH_SHORT).show();

        }

    }

    private void UpdatListFiter(String Firmname) {
        partialCallLists.clear();

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,isPartial,CallType,CallStatus,ProspectId,LatestRemark,Mobile,CityName,ContactName,Product" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                // partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                callid = cur.getString(cur.getColumnIndex("CallId"));
                partialCallList.setCallId(callid);

                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                //   partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                //  partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Telephone")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                callstatus = cur.getString(cur.getColumnIndex("CallStatus"));
                partialCallList.setCallStatus(callstatus);
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);


                }
            }
        }


    }

    public void clickopportunitygetCallListData(String userMasterId) {
        UserMasterId = userMasterId;
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;

        loadmore = null;
        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String currentDateandTime = dfDate.format(new Date());
            Date cdate = dfDate.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cdate);
            calendar.add(Calendar.DAY_OF_YEAR, 30);
            Date newDate1 = calendar.getTime();

            /*Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();*/

            jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", dfDate.format(newDate1));
            jsonObj.put("value2", "");
            jsonObj.put("Operator", "<");


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("IsPartial");
            jsonObj.put("IsSet", false);
            jsonObj.put("value1", "P");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        System.out.println("FinalObj" + FinalObj);
        new DownloadCommanDataOpportunityURLJSON().execute();
    }


    class DownloadCommanDataOpportunityURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        ProgressDialog progressDialog;
        StopWatch watch;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //       showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                 watch = new StopWatch();

                res = ut.OpenPostConnection(url, FinalObj, CallListActivity.this);
                if (res != null) {

                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                    //response = response.replaceAll("^\"|\"$", "");

                    Log.d("Response",response);

                    ContentValues values = new ContentValues();


                    JSONArray jResults = new JSONArray(response.toString());
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    sql.delete(db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null, values);
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
            //googleProgressBar.setVisibility(View.GONE);
            progressDialog.dismiss();
            //dismissProgressDialog();
            if (response.contains("CallId")) {
                Log.d("Opp Total",String.format(watch.toString()));

                OpportunityUpdatList();
                rel_call_list.setVisibility(View.VISIBLE);
                len_cal.setVisibility(View.GONE);
                len_search.setVisibility(View.VISIBLE);
                //now layloadmore.setVisibility(View.VISIBLE);
                layloadmore.setVisibility(View.VISIBLE);

            } else if (response.contains("Error")) {

            } else {
                Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                layloadmore.setVisibility(View.GONE);
                // layloadmore.setVisibility(View.VISIBLE);
            }
         /*   Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
            int count1 = c1.getCount();*/


           /* if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOpportunitiesJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }*/


            //progressHUD.dismiss();
            //googleProgressBar.setVisibility(View.GONE);

        }


    }


    private void OpportunityUpdatList() {//,ChatRoomId,ChatCount
        partialCallLists.clear();

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,isPartial,CallType,CallStatus,ProspectId,LatestRemark,Mobile,CityName,ContactName,Product,Address,ExpectedValue" +
                " FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + "";
        Cursor cur = sql.rawQuery(query, null);


        if (cur.getCount() > 0) {


            lsCall_list.setVisibility(View.VISIBLE);
            cur.moveToFirst();
            do {
/*
                if(cur.getCount()> 20) {*/
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                // partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                callid = cur.getString(cur.getColumnIndex("CallId"));
                partialCallList.setCallId(callid);

                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                //   partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                //  partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Telephone")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                callstatus = cur.getString(cur.getColumnIndex("CallStatus"));
                partialCallList.setCallStatus(callstatus);
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                //partialCallList.setChatRoomId(cur.getString(cur.getColumnIndex("ChatRoomId")));
                // partialCallList.setChatCount(cur.getString(cur.getColumnIndex("ChatCount")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                partialCallLists.add(partialCallList);
                /*}else{

                }*/

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_OpportunityCallList(i);

                }
            } else {
                Intent intent = new Intent(CallListActivity.this, CreateNewOppActivity.class);
                startActivity(intent);

            }
        }


    }

    private void addView_OpportunityCallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CallListActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final int position1 = i;
        convertView = layoutInflater.inflate(R.layout.crm_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView.findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView.findViewById(R.id.tvcall);
        img_contact = (ImageView) convertView.findViewById(R.id.img_contact);
        img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);


        //Add contact Code
        len_contact = (LinearLayout) convertView.findViewById(R.id.len_contact);
        len_show_calldetails = (LinearLayout) convertView.findViewById(R.id.len_show_calldetails);

        edt_contact_name = (EditText) convertView.findViewById(R.id.edt_contact_name);
        edt_designation = (EditText) convertView.findViewById(R.id.edt_designation);
        spinner_department = (Spinner) convertView.findViewById(R.id.spinner_department);
        edt_emailid = (EditText) convertView.findViewById(R.id.edt_emailid);
        edt_mobile = (EditText) convertView.findViewById(R.id.edt_mobile);
        edt_offfice = (EditText) convertView.findViewById(R.id.edt_offfice);
        checkbox_primary_contact = (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_primary_contact);
        txt_save = (TextView) convertView.findViewById(R.id.txt_save);
        txt_reset = (TextView) convertView.findViewById(R.id.txt_reset);

        txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
        txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);
        txt_expvalue = (TextView) convertView.findViewById(R.id.txt_expvalue);


        Call_ProspectId = partialCallLists.get(i).getPKSuspectId();


       /* if (CompanyURL.equalsIgnoreCase("http://vritti.ekatm.com")){
            txt_chat.setVisibility(View.VISIBLE);
        }else {
            txt_chat.setVisibility(View.GONE);
        }*/
        txt_chat.setVisibility(View.VISIBLE);


        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            } else {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.que);
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());


        //tvcall.setText("For " + partialCallLists.get(i).getLatestRemark());
        //  txtcityname.setText("No Contact Available");

        String time = partialCallLists.get(i)
                .getActiondatetime();
        System.out.println("CallTime :" + time);
        txtactiondatetime.setText(partialCallLists.get(i)
                .getActiondatetime());
        String Partial = partialCallLists.get(i).getIsPartial();
        String remark = partialCallLists.get(i).getLatestRemark();
        if (remark.equalsIgnoreCase("") || remark.equalsIgnoreCase(" ") ||
                remark.equalsIgnoreCase(null) || remark.equalsIgnoreCase("null")) {
            tv_latestremark.setText("");
        } else {
            tv_latestremark.setText(" For " + partialCallLists.get(i).getLatestRemark());
        }


        String Mobile = partialCallLists.get(i).getMobileno();
        String City = partialCallLists.get(i).getCityname();
        String Contactname = partialCallLists.get(i).getContactName();
        String Product = partialCallLists.get(i).getProduct();
        String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";

        tvcall.setText(Concatdata);


        //   String status = partialCallLists.get(i).getContactName();

        /*if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("")) {
            tv_city.setText("(" + partialCallLists.get(i).getMobileno() + ")");
        } else {
            if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {
                tv_city.setText("(" + partialCallLists.get(i).getMobileno() + ")");
            } else {
                tv_city.setText("(" + partialCallLists.get(i).getContactName() + " - "
                        + partialCallLists.get(i).getMobileno() + ")");
            }

        }
*/


        Address=partialCallLists.get(i).getAddress();
        txtaddress.setText(Address);

        Geocoder coder = new Geocoder(CallListActivity.this);
        try {
            address = coder.getFromLocationName(Address, 5);
            android.location.Address location = address.get(0);
            Lat = location.getLatitude();
            Lng = location.getLongitude();

        }catch (Exception e){
            e.printStackTrace();
        }

        String ExpValue=partialCallLists.get(i).getExpectedValue();
        if (ExpValue.equals("0.00")||ExpValue.equals("0.0")||ExpValue.equals("0")){

        }else {
            txt_expvalue.setVisibility(View.VISIBLE);
            txt_expvalue.setText("EV-"+"\u20B9"+ExpValue);
        }

        txtaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lat==0&Lng==0){

                }else {

                    try {
                        Address=partialCallLists.get(position).getAddress();
                        Geocoder coder = new Geocoder(CallListActivity.this);
                        address = coder.getFromLocationName(Address, 5);
                        android.location.Address location = address.get(0);
                        Lat = location.getLatitude();
                        Lng = location.getLongitude();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    String geoUri = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng + " (" + partialCallLists.get(i).getFirmname() + ")";
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);

                   /* String uriMap = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                    startActivity(intent);
*/
                    }
                }
            }
        });


        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position1).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(i).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String callId = partialCallLists.get(i).getCallId();
                String Partial = partialCallLists.get(i).getIsPartial();

                if (Partial.equalsIgnoreCase("P")) {

                    if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                System.out.println("load" + loadmore);
                                if (loadmore == null) {
                                    getCallListOpportunityData_Full(partialCallLists.get(i).getCallId(), i);

                                } else {

                                    getloadmoreCallListOpportunityData_Full(partialCallLists.get(i).getCallId(), i);
                                }
                            }


                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }

            }
        });


        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(CallListActivity.this, ChatMainActivity.class));
                Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                Call_CallType = partialCallLists.get(i).getCallType();

                Intent intent = new Intent(CallListActivity.this, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                    Call_CallType = partialCallLists.get(i).getCallType();
                    Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(CallListActivity.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Opportunity");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(CallListActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }


            }
        });


        img_appotunity_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CallListActivity.this, CallRatingActivity.class);
                intent.putExtra("callid", partialCallLists.get(position1).getCallId());
                intent.putExtra("callstatus", partialCallLists.get(position1).getCallStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Call_Department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_contact_name.setText("");
                edt_designation.setText("");
                edt_mobile.setText("");
                edt_emailid.setText("");
                edt_offfice.setText("");
                checkbox_primary_contact.setChecked(false);


            }
        });


        for (int position = 0; position < partialCallLists.size(); position++) {
            convertView.setTag(position);
        }

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof Integer) {
                    pos = (Integer) v.getTag();
                }

                Call_Contactname = edt_contact_name.getText().toString();
                Call_Designation = edt_designation.getText().toString();
                Call_Mobile = edt_mobile.getText().toString();
                Call_Email = edt_emailid.getText().toString();
                Call_Office = edt_offfice.getText().toString();
                Call_Callid = partialCallLists.get(pos).getCallId();
                Call_CallType = partialCallLists.get(pos).getCallType();
                CallData();
                JSONObject jsoncontactadd = new JSONObject();

                try {
                    jsoncontactadd.put("CallId", Call_Callid);
                    jsoncontactadd.put("CallType", Call_CallType);
                    jsoncontactadd.put("ProspectId", Call_ProspectId);
                    jsoncontactadd.put("ContactName", Call_Contactname);
                    jsoncontactadd.put("Designation", Call_Designation);
                    jsoncontactadd.put("DeptRoleName", Call_Department);
                    jsoncontactadd.put("EmailId", Call_Email);
                    jsoncontactadd.put("TeleNo", Call_Office);
                    jsoncontactadd.put("MobileNo", Call_Mobile);
                    jsoncontactadd.put("IsPrimaryContact", Check_Value);

                    Addcalljson = jsoncontactadd.toString();

                    System.out.println("Contact list : " + jsoncontactadd.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Addcalljson = Addcalljson.toString();
                Addcalljson = Addcalljson.replaceAll("\\\\", "");

                if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostAddContactJSON().execute(Addcalljson);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }


            }
        });
        checkbox_primary_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkbox_primary_contact.isChecked()) {
                    Check_Value = Boolean.parseBoolean("1");
                } else {
                    Check_Value = Boolean.parseBoolean("0");
                }
            }
        });


        lsCall_list.addView(convertView);


    }

    public void getCallListOpportunityData_Full(String cid, int position) {

        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String currentDateandTime = dfDate.format(new Date());
            Date cdate = dfDate.parse(currentDateandTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(cdate);
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate1 = calendar.getTime();

           /* Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();*/

            jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", dfDate.format(newDate1));
            jsonObj.put("value2", "");
            jsonObj.put("Operator", "<");


        } catch (Exception e) {
            e.printStackTrace();
        }


        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanData_fullURLJSON().execute(position);
    }


    //Calender Demo


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    class DownloadCalenderDetailsData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        ProgressDialog progressDialog;
        StopWatch stopWatch;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setIndeterminate(true);
            ;
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_GetCallCount + "?UserMasterId=" + UserMasterId;

                System.out.println("URLCALLHISTORY :" + url);
                stopWatch=new StopWatch();
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    System.out.println("URLCALLHISTORYresponse :" + response);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();
            //  dismissProgressDialog();
            try {
                Log.d("Cal Total",String.format(stopWatch.toString()));
                JSONArray jResults = null;
                jResults = new JSONArray(response);
                ContentValues values = new ContentValues();

                sql.delete(db.TABLE_Calender, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Calender, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Calender, null, values);
                    Log.e("log data", "" + a);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Calenderlist();


        }

    }

    public void getloadmoreCallListOpportunityData_Full(String cid, int position) {

        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String currentDateandTime = dfDate.format(new Date());
            Date cdate = dfDate.parse(currentDateandTime);
            currentDateandTime = dfDate.format(cdate);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 14);
            // newDate2 = calendar1.getTime();


            jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", currentDateandTime);
            jsonObj.put("value2", "");
            jsonObj.put("Operator", ">");


        } catch (Exception e) {
            e.printStackTrace();
        }


        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanData_fullURLJSON().execute(position);
    }

    private void Calenderlist() {
        CalendarCollection.date_collection_arr.clear();
        String query = "SELECT * FROM " + db.TABLE_Calender;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CalendarCollection calendarCollection = new CalendarCollection();
                calendarCollection.setNextActionDateTime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                calendarCollection.setCountCollection(cur.getString(cur.getColumnIndex("CountCollection")));
                calendarCollection.setCountFeedBack(cur.getString(cur.getColumnIndex("CountFeedBack")));
                calendarCollection.setCountSales(cur.getString(cur.getColumnIndex("CountSales")));
                CalendarCollection.date_collection_arr.add(calendarCollection);
                CalendarCollection.date_collection_arr.add(calendarCollection);
            } while (cur.moveToNext());

            cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
            cal_month_copy = (GregorianCalendar) cal_month.clone();
            cal_adapter = new CalendarAdapter(CallListActivity.this, cal_month, CalendarCollection.date_collection_arr);
            gridview.setAdapter(cal_adapter);
            len_cal.setVisibility(View.VISIBLE);
            rel_call_list.setVisibility(View.GONE);
            len_search.setVisibility(View.GONE);

        }


    }

    class DownloadLoadmoreCommanDataOpportunityURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CallListActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            // showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, FinalObj, CallListActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();


                    JSONArray jResults = new JSONArray(response.toString());
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    sql.delete(db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_OPPOTUNITY_CALL_FILTER, null, values);
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

            progressDialog.dismiss();

            if (response.contains("CallId")) {
                OpportunityUpdatList();
                rel_call_list.setVisibility(View.VISIBLE);
                len_cal.setVisibility(View.GONE);
                len_search.setVisibility(View.VISIBLE);
                layloadmore.setVisibility(View.VISIBLE);

            } else if (response.contains("Error")) {

            } else {
                Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                layloadmore.setVisibility(View.GONE);
                //layloadmore.setVisibility(View.VISIBLE);
            }
         /*   Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL, null);
            int count1 = c1.getCount();*/
            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOpportunitiesJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }


        }


    }

    class DownloadSubTeamJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            //Toast.makeText(CallListActivity.this,"Subteam api called",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getMySubTeam +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);

                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (response.equalsIgnoreCase("0")) {
                tv_team_mem_cnt1.setText("0");
            } else {
                if (response.equalsIgnoreCase("error") || response.contains("Message")) {
                    tv_team_mem_cnt1.setText("0");
                } else {

                    tv_team_mem_cnt1.setText(response);
                }

            }

            if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadEnquiryAssigned().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

    }

    class DownloadGetEnvJSON extends AsyncTask<String, Void, String> {
        String res = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;

            try {
                res = ut.OpenConnection(url.trim());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);


                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    IsChatApplicable = jorder.getString("IsChatApplicable");
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("chatapplicable", IsChatApplicable);
                    editor.commit();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


        }
    }

    class DownloadCheckSalesInstalled extends AsyncTask<String, Void, String> {
        String res = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_CheckSalesInstalled;

            try {
                res = ut.OpenConnection(url.trim());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
//true
            if (res != null) {
                String val = res;
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.USERINFO_ISCOLLECTION_APPLICABLE, val);
                editor.commit();

                if (IsCollectionApplicable.equalsIgnoreCase(Salesmodulevisible)) {

                    if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //  Toast.makeText(CallListActivity.this,"Collection api called",Toast.LENGTH_SHORT).show();
                                new DownloadCollectionJSON().execute();

                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    }


                    lay_collection_view.setVisibility(View.VISIBLE);
                } else {
                    if (isnet()) {
                        new StartSession(CallListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //  Toast.makeText(CallListActivity.this,"call Rating api called",Toast.LENGTH_SHORT).show();
                                new DownloadCallRatingJSON().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    }

                    lay_collection_view.setVisibility(View.GONE);

                }
            }
        }
    }

    class DownloadEnquiryAssigned extends AsyncTask<String, Void, String> {
        String response = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Enuiry;
            try {

                response = ut.OpenConnection(url);
                response = response.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ENQUIRY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ENQUIRY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }
                    long a = sql.insert(db.TABLE_ENQUIRY, null, values);
                    Log.e("Enuiry Table", "" + a);

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
            if (response.contains("EnquiryRegistryId")) {
                UpdateEnuiry();
                Toast.makeText(CallListActivity.this, "Success", Toast.LENGTH_SHORT).show();

            } else if (response.equalsIgnoreCase("[]")) {
                txt_enquirycount.setText("" + 0);
            } else {
                Toast.makeText(getApplicationContext(), "can not fetch Enquiry data", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {


        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    class DownloadUSerImageJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // showProgresHud();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetUserProfile;

            try {
                res = ut.OpenConnection(url, CallListActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  hideProgresHud();
            if (response.equalsIgnoreCase("[]")) {

            } else {
                try {
                    JSONArray jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        String ImagePath = jsonObject.getString("ImagePath");
                        String ImagePath1 = CompanyURL + "/" + ImagePath;
                        /*Glide.with(ActivityMain.this)
                                .load(ImagePath1)
                                .into(img_userpic);*/
                        img_userpic.setImageURI(ImagePath1);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        cursorLoader = new Loader<>(this);
        cursorLoader = new CursorLoader(this, Uri.parse("content://com.example.contentproviderexample.MyProvider1/cte"), null,
                null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null) {
            layout_phncalls.setVisibility(View.GONE);
            tv_phncall_cnt.setVisibility(View.GONE);
        } else {
            ArrayList<CallLogsDetails> callLogsDetailsArrayList = new ArrayList<>();


            sql.delete(db.TABLE_CALL_LOG, null, null);

            cursor.moveToFirst();
            StringBuilder res = new StringBuilder();

            while (!cursor.isAfterLast()) {
                CallLogsDetails callLogsDetails = new CallLogsDetails();
                String count1 = cursor.getString(cursor.getColumnIndex("RowNo"));
                callLogsDetails.setRowNo(cursor.getString(cursor.getColumnIndex("RowNo")));
                String usermasterId = cursor.getString(cursor.getColumnIndex("UserMasterId"));
                callLogsDetails.setUserMasterId(cursor.getString(cursor.getColumnIndex("UserMasterId")));
                callLogsDetails.setUserName(cursor.getString(cursor.getColumnIndex("UserMasterName")));
                String number = cursor.getString(cursor.getColumnIndex("MobileNo"));
                callLogsDetails.setNumber(cursor.getString(cursor.getColumnIndex("MobileNo")));
                String Start1 = cursor.getString(cursor.getColumnIndex("StartTime"));
                callLogsDetails.setStartTime(cursor.getString(cursor.getColumnIndex("StartTime")));
                String End1 = cursor.getString(cursor.getColumnIndex("EndTime"));
                callLogsDetails.setEndTime(cursor.getString(cursor.getColumnIndex("EndTime")));
                String duration = cursor.getString(cursor.getColumnIndex("Duration"));
                callLogsDetails.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                String callType = cursor.getString(cursor.getColumnIndex("MobileCallType"));
                callLogsDetails.setCallType(cursor.getString(cursor.getColumnIndex("MobileCallType")));
                callLogsDetails.setContactPersonName(cursor.getString(cursor.getColumnIndex("ContactPersonName")));

                if (usermasterId.equals(UserMasterId)) {
                    if (callLogsDetails.getCallType().equalsIgnoreCase("incoming") ||
                            callLogsDetails.getCallType().equalsIgnoreCase("outgoing") ||
                            callLogsDetails.getCallType().equalsIgnoreCase("Opportunity/collection")) {

                        callLogsDetailsArrayList.add(callLogsDetails);
                        db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, callType, Integer.parseInt(count1), "");

                    }
                }

                cursor.moveToNext();


            }


            ArrayList<String> stringArrayList = new ArrayList<>();
            if (callLogsDetailsArrayList.size() > 0) {
                for (int i = 0; i < callLogsDetailsArrayList.size(); i++) {
                    if (callLogsDetailsArrayList.get(i).getCallType().equalsIgnoreCase("Opportunity/collection") ||
                            callLogsDetailsArrayList.get(i).getCallType().equalsIgnoreCase("incoming") ||
                            callLogsDetailsArrayList.get(i).getCallType().equalsIgnoreCase("outgoing")) {
                        stringArrayList.add(callLogsDetailsArrayList.get(i).getCallType());
                    }
                }
            }
            if (stringArrayList.size() != 0) {
                layout_phncalls.setVisibility(View.VISIBLE);
                tv_phncall_cnt.setVisibility(View.VISIBLE);
                tv_phncall_cnt.setText(String.valueOf(stringArrayList.size()));
            } else {
                layout_phncalls.setVisibility(View.GONE);
                tv_phncall_cnt.setVisibility(View.GONE);
            }
        }


        //resultView.setText(res);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

