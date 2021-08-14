package com.vritti.crm.vcrm7;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.vritti.crm.adapter.CalendarAdapter;
import com.vritti.crm.adapter.MyTeamAdapter;
import com.vritti.crm.bean.Appointment;
import com.vritti.crm.bean.BirthdayBean;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.bean.MyTeamBean;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.bean.ReasonBean;
import com.vritti.crm.bean.ShowContact;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import static com.vritti.crm.vcrm7.CallListActivity.setListViewHeightBasedOnChildren;

public class CRMHomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;


    View header;
    LinearLayout layout_birthday, ls_appointments, ly_team, realcolors, lay_opportunity, lay_overdue_opp,
            lay_today_opp, lay_Tommorow_opp, lay_this_week_opp, lay_collection, lay_collection_view,
            lay_overdue_collection, lay_today_overdue_collection, lay_tomorrow_overdue_collection,
            lay_week_overdue_collection, lay_callrating, lay_hot_call, lay_warm_call, lay_week_overdue_feedback, lay_today_overdue_feedback, lay_tomorrow_overdue_feedback,
            lay_overdue_feedback, lay_new_feedback, lay_new_collection, lay_feedback, lay_new_opp, lay_revived_opp, lay_yesterday_opp;
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
    TextView txtHome, txtServiceReport, txt_clientmaster, txt_CRMReport;
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
    Loader<Cursor> cursorLoader;

    List<android.location.Address> address;
    String Address = "";
    double Lat = 0, Lng = 0;
    TextView txtaddress;
    static String Salesmodulevisible = "true";
    ImageView img_refresh, img_home;
    private ProgressDialog progressDialog;
    private String Overdue = "", Today = "", ThisWeek = "", Tomorrow = "", New = "";
    TextView tv_;
    LinearLayout lay_link;
    ImageView img_back;
    TextView txt_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(CRMHomeActivity.this);
        setContentView(R.layout.crm_home_menu_lay);

        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("");
        setSupportActionBar(toolbar_action);
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

       // sendDualSimSMSOption();

        getSupportLoaderManager().initLoader(1, null, CRMHomeActivity.this);


        Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();
        count1 = count1 + 1;
        Log.e("Call Log Count", "" + count1);

        if (isnet()) {
            progressDialog = new ProgressDialog(CRMHomeActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadOpportunitiesJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }
            });

        }

        if (isnet()) {
            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadTeamJSON().execute();
                    new DownloadSubTeamJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUSerImageJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(CRMHomeActivity.this, msg);

                }
            });
        } else {
            ut.displayToast(CRMHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }

        init();


    }

    private void init() {


        app_version = (TextView) findViewById(R.id.txt_app_version);
        img_userpic = (SimpleDraweeView) findViewById(R.id.img_userpic);

        String currentVersion;
        try {
            currentVersion = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            currentVersion = "0";
        }
        app_version.setText(currentVersion);
        // tv_username = (TextView) findViewById(R.id.tv_username);
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
        //tv_username.setText(R.string.app_name);

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
        txtteam = (TextView) findViewById(R.id.txtteam);


        lay_opportunity = (LinearLayout) findViewById(R.id.lay_opportunity);
        lay_overdue_opp = (LinearLayout) findViewById(R.id.lay_overdue_opp);
        lay_today_opp = (LinearLayout) findViewById(R.id.lay_today_opp);
        lay_Tommorow_opp = (LinearLayout) findViewById(R.id.lay_Tommorow_opp);
        lay_this_week_opp = (LinearLayout) findViewById(R.id.lay_this_week_opp);
        lay_collection = (LinearLayout) findViewById(R.id.lay_collection);
        lay_collection_view = (LinearLayout) findViewById(R.id.lay_collectionview);
        lay_yesterday_opp = (LinearLayout) findViewById(R.id.lay_yesterday_opp);
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

        img_opportunity = (ImageView) findViewById(R.id.img_opportunity);
        img_appointment = (ImageView) findViewById(R.id.img_appointment);
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
        img_refresh = findViewById(R.id.img_refresh);
        img_home = findViewById(R.id.img_home);
        tv_ = findViewById(R.id.tv_);
        lay_link = findViewById(R.id.lay_link);

        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        txt_title.setText("CRM");


        SetListener();


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


            if (a.equals("0")) {
                lay_new_opp.setVisibility(View.GONE);
            } else {
                lay_new_opp.setVisibility(View.VISIBLE);
                tv_opportunities.setText(a);
            }
            if (Overdue.equals("0")) {
                lay_overdue_opp.setVisibility(View.GONE);
            } else {
                lay_overdue_opp.setVisibility(View.VISIBLE);
                tv_overdue_opp.setText(Overdue);
            }
            if (Yesterday.equals("0")) {
                lay_yesterday_opp.setVisibility(View.GONE);
            } else {
                lay_yesterday_opp.setVisibility(View.VISIBLE);
                tv_yesterday_opp.setText(Yesterday);
            }
            if (Today.equals("0")) {
                lay_today_opp.setVisibility(View.GONE);
            } else {
                lay_today_opp.setVisibility(View.VISIBLE);
                tv_today_opp.setText(Today);
            }

            tv_callagain_opp.setText(CallAgain);

            if (t.equals("0")) {
                lay_Tommorow_opp.setVisibility(View.GONE);
            } else {
                lay_Tommorow_opp.setVisibility(View.VISIBLE);
                tv_tommorow_oppportunity.setText(t);
            }

            String This_Week = cur.getString(cur.getColumnIndex("ThisWeek"));

            if (This_Week.equals("0")) {
                lay_this_week_opp.setVisibility(View.GONE);
            } else {
                lay_this_week_opp.setVisibility(View.VISIBLE);
                tv_this_week_opp.setText(This_Week);
            }

            String New = cur.getString(cur.getColumnIndex("New"));

            if (New.equals("0")) {
                lay_new_opp.setVisibility(View.GONE);
            } else {
                lay_new_opp.setVisibility(View.VISIBLE);
                tv_new_opp.setText(New);
            }
            if (Revived.equals("0")) {
                lay_revived_opp.setVisibility(View.GONE);
            } else {
                lay_revived_opp.setVisibility(View.VISIBLE);
                tv_revived_opp.setText(Revived);
            }
        }
    }

    class DownloadOpportunitiesJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


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

            progressDialog.dismiss();

            if (response != null) {

                UpdateOpportunities();
                if (isnet()) {
                    new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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


            } else {
                Toast.makeText(CRMHomeActivity.this, "Unable to fetch opportunity data", Toast.LENGTH_SHORT).show();
            }


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

    private void UpdateCollection() {
        String query = "SELECT Tomorrow,TotalCollection,OverdueCollection,TodayCollection,New,TomorrowCollection,ThisWeekCollection,NewCollection,Assigned,Overdue,Today,ThisWeek FROM " + db.TABLE_COLLECTION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String a = cur.getString(cur.getColumnIndex("TotalCollection"));
            String b = cur.getString(cur.getColumnIndex("OverdueCollection"));
            String c = cur.getString(cur.getColumnIndex("TodayCollection"));
            String d = cur.getString(cur.getColumnIndex("ThisWeekCollection"));
            String e = cur.getString(cur.getColumnIndex("TomorrowCollection"));
            String Nc = cur.getString(cur.getColumnIndex("NewCollection"));
            String Assign = cur.getString(cur.getColumnIndex("Assigned"));
            New = cur.getString(cur.getColumnIndex("New"));

            Overdue = cur.getString(cur.getColumnIndex("Overdue"));
            Today = cur.getString(cur.getColumnIndex("Today"));
            ThisWeek = cur.getString(cur.getColumnIndex("ThisWeek"));
            Tomorrow = cur.getString(cur.getColumnIndex("Tomorrow"));
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


            if (NewCount.equals("0")) {
                lay_new_collection.setVisibility(View.GONE);
            } else {
                lay_new_collection.setVisibility(View.VISIBLE);
                tv_new_collection.setText(Nc + " " + "(" + NewCount + ")");
            }

            if (OverdueCount.equals("0")) {
                lay_overdue_collection.setVisibility(View.GONE);
            } else {
                lay_overdue_collection.setVisibility(View.VISIBLE);
                tv_overdue_collection.setText(b + " " + "(" + OverdueCount + ")");
            }

            if (TodayCount.equals("0")) {
                lay_today_overdue_collection.setVisibility(View.GONE);
            } else {
                lay_today_overdue_collection.setVisibility(View.VISIBLE);
                tv_today_overdue_collection.setText(c + " " + "(" + TodayCount + ")");
            }
            if (ThisWeekCount.equals("0")) {
                lay_week_overdue_collection.setVisibility(View.GONE);
            } else {
                lay_week_overdue_collection.setVisibility(View.VISIBLE);
                tv_week_overdue_collection.setText(d + " " + "(" + ThisWeekCount + ")");
            }

            if (TomorrowCount.equals("0")) {
                lay_tomorrow_overdue_collection.setVisibility(View.GONE);
            } else {
                lay_tomorrow_overdue_collection.setVisibility(View.VISIBLE);
                tv_tomrrow_overdue_collection.setText(e + " " + "(" + TomorrowCount + ")");
            }


            tv_collection_cnt.setText(a + " " + "(" + AssignCount + ")");


        }

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

           // tv_hot_call.setText(cur.getString(cur.getColumnIndex("HotCount")));

         //   tv_warm_call.setText(cur.getString(cur.getColumnIndex("WarmCount")));
           // tv_warm_call_value.setText(cur.getString(cur.getColumnIndex("WarmValue")));

            String WarmCount=cur.getString(cur.getColumnIndex("WarmCount"));
            String WarmValue=cur.getString(cur.getColumnIndex("WarmValue"));

            if (WarmCount.equals("0")){
                lay_warm_call.setVisibility(View.GONE);
            }else {
                lay_warm_call.setVisibility(View.VISIBLE);
                tv_warm_call_value.setText(WarmValue + " " + "(" + WarmCount + ")");
            }

            String HotCount=cur.getString(cur.getColumnIndex("HotCount"));
            String HotValue=cur.getString(cur.getColumnIndex("HotValue"));

            //tv_hot_call_value.setText(cur.getString(cur.getColumnIndex("HotValue")));
            if (HotCount.equals("0")){
                lay_hot_call.setVisibility(View.GONE);
            }else {
                lay_hot_call.setVisibility(View.VISIBLE);
                tv_hot_call_value.setText(HotValue + " " + "(" + HotCount + ")");
            }

            tv_callrating_cnt.setText(String.valueOf(callrating));

        }
        /***************************************************************************************/
        //getMyTeamData();
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

            teamAdapter = new MyTeamAdapter(CRMHomeActivity.this, ls);
            ls_Team.setAdapter(teamAdapter);
//            setListViewHeightBasedOnChildren(ls_Team);

            int itemcount=teamAdapter.getCount();
            ViewGroup.LayoutParams params = ls_Team.getLayoutParams();
            params.height =(itemcount*80);
            ls_Team.setLayoutParams(params);
            ls_Team.requestLayout();
            teamAdapter.notifyDataSetChanged();
           // registerForContextMenu(ls_Team);
        } else {
            tv_team_mem_cnt.setText("");
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


            String Overdue = cur.getString(cur.getColumnIndex("Overdue"));
            if (Overdue.equals("0")) {
                lay_overdue_feedback.setVisibility(View.GONE);
            } else {
                lay_overdue_feedback.setVisibility(View.VISIBLE);
                tv_overdue_feedback.setText(Overdue);
            }

            String New = cur.getString(cur.getColumnIndex("New"));
            if (New.equals("0")) {
                lay_new_feedback.setVisibility(View.GONE);
            } else {
                lay_new_feedback.setVisibility(View.VISIBLE);
                tv_new_feedback.setText(New);
            }
            String Today = cur.getString(cur.getColumnIndex("Today"));
            if (Today.equals("0")) {
                lay_today_overdue_feedback.setVisibility(View.GONE);
            } else {
                lay_today_overdue_feedback.setVisibility(View.VISIBLE);
                tv_today_overdue_feedback.setText(Today);
            }

            String Tomorrow = cur.getString(cur.getColumnIndex("Tomorrow"));
            if (Tomorrow.equals("0")) {
                lay_tomorrow_overdue_feedback.setVisibility(View.GONE);
            } else {
                lay_tomorrow_overdue_feedback.setVisibility(View.VISIBLE);
                tv_tomrrow_overdue_feedback.setText(Tomorrow);
            }

            String ThisWeek = cur.getString(cur.getColumnIndex("ThisWeek"));
            if (ThisWeek.equals("0")) {
                lay_week_overdue_feedback.setVisibility(View.GONE);
            } else {
                lay_week_overdue_feedback.setVisibility(View.VISIBLE);
                tv_week_overdue_feedback.setText(ThisWeek);
            }

        } else {


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
                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                        new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                        new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                //   Toast.makeText(CRMHomeActivity.this, "Success", Toast.LENGTH_SHORT).show();

            } else if (response.equalsIgnoreCase("[]")) {
                txt_enquirycount.setText("" + 0);
            } else {
                Toast.makeText(getApplicationContext(), "can not fetch Enquiry data", Toast.LENGTH_LONG).show();

            }
        }
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
                res = ut.OpenConnection(url, CRMHomeActivity.this);
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
                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                Toast.makeText(CRMHomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            UpdateOpportunities();
            UpdateFeedback();

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

                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFeedbackJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }


        }

    }

    private void SetListener() {

        lay_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRMHomeActivity.this, CRM_CallLogList.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

       /* ls_Team.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                final String clickTeamMem_Id = ls.get(position).getUserMasterId();
                String clickTeamMem_Name = ls.get(position).getUserName();


                String tv_opportunity = tv_opportunities.getText().toString();
                if (tv_opportunity.equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                } else {
                    if (isnet()) {
                        new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                // clickopportunitygetCallListData(clickTeamMem_Id);

                                obj = getObj("A", TeamMemberBeanArrayList.get(i).getUserMasterId());

                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }


            }
        });
*/


        txtteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_team_mem_cnt.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(CRMHomeActivity.this, "No Record Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, TeamMemberActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }


            }
        });

        ln_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRMHomeActivity.this, ActivityEnquiryList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        lay_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tv_opportunity = tv_opportunities.getText().toString();

                if (tv_opportunities.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "new");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "main_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }

                    /*Intent intent = new Intent(CRMHomeActivity.this, CallListActivity.class);
                    intent.putExtra("homeopp","1");
                    startActivity(intent);*/


            }
        });

        lay_new_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tv_new_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                 /*   Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "new");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "new_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });


        textview_opportunity_Overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tv_overdue_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "oppoverdue");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "overdue_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        textview_opportunity_yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (tv_yesterday_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "opp_yes");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "yesterday_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        textview_opportunity_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_today_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "opp_today");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "today_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        textview_opportunity_callagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_callagain_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "callagain_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_revived_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_revived_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    startActivity(intent);*/
                } else {
                   /* Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "revived_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "revived_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });


        lay_Tommorow_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_tommorow_oppportunity.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "opp_tomorow");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "Tommorow_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_this_week_opp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_this_week_opp.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "opp_week");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "this_week_opp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_collection_cnt.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                   /* //  Toast.makeText(CallListActivity.this, "No Opportunity Found", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("from", "collection");
                    startActivity(intent);
                    *//*Intent intent = new Intent(CallListActivity.this,CreateNewOppActivity.class);
                    intent.putExtra("Opportunity_type","Collection");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "overdue_collection");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });

        lay_today_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (tv_today_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                  /*  Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "today_overdue_collection");
                    startActivity(intent);*/
                    /*Intent intent = new Intent(CallListActivity.this,CreateNewOppActivity.class);
                    intent.putExtra("Opportunity_type","Collection");
                    startActivity(intent);*/
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "today_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_tomorrow_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_tomrrow_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "tomorrow_overdue_collection");
                    startActivity(intent);
*/

                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "tomorrow_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_week_overdue_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_week_overdue_collection.getText().toString().equalsIgnoreCase("0.00 T (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "week_overdue_collection");
                    startActivity(intent);*/

                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "week_overdue_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        lay_new_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_new_collection.getText().toString().equalsIgnoreCase("0.00 T (0)") ||
                        tv_new_collection.getText().toString().equalsIgnoreCase("0/0.00 (0)")) {
                    Toast.makeText(CRMHomeActivity.this, "No Collection Found", Toast.LENGTH_LONG).show();
                    /*Intent intent = new Intent(CRMHomeActivity.this, CreateNewOppActivity.class);
                    intent.putExtra("newopp", "newCollection");
                    startActivity(intent);*/

                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "new_collection");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });

        lay_hot_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "hot_call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        lay_warm_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "warm_call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        lay_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_feedback_cnt.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });

        lay_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_today_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_today_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "today_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_tomorrow_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_tomrrow_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Feedback Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "tomorrow_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });


        lay_week_overdue_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_week_overdue_feedback.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(CRMHomeActivity.this, "No Feedback  Found", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CRMHomeActivity.this, OpportunityActivity_V1.class);
                    intent.putExtra("Opportunity", "week_overdue_feedback");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });


        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callrefreshapi();

            }


        });
        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CRMHomeActivity.this, CRMRightMenuActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }


        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void callrefreshapi() {


        if (isnet()) {
            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadOpportunitiesJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                }
            });

        }

        if (isnet()) {

            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadFeedbackJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

        }


        if (isnet()) {
            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadTeamJSON().execute();
                    new DownloadSubTeamJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (isnet()) {
            new StartSession(CRMHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadEnquiryAssigned().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (IsCollectionApplicable.equalsIgnoreCase(Salesmodulevisible)) {

            if (isnet()) {
                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
                new StartSession(CRMHomeActivity.this, new CallbackInterface() {
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
    }

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
                tv_.setText(String.valueOf(stringArrayList.size()));
            } else {
            }
        }


        //resultView.setText(res);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

}
