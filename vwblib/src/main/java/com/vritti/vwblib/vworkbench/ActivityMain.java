package com.vritti.vwblib.vworkbench;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.ActivityListMainAdapter;
import com.vritti.vwblib.Adapter.MyTeamAdapter;
import com.vritti.vwblib.Adapter.MyworkspaceAdapter;
import com.vritti.vwblib.Beans.ActivityBean;
import com.vritti.vwblib.Beans.BirthdayBean;
import com.vritti.vwblib.Beans.MyTeamBean;
import com.vritti.vwblib.Beans.MyWorkspaceBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.DownloadWorkspaceData;
import com.vritti.vwblib.Services.GpsDataLocalSendSevice;
import com.vritti.vwblib.chat.OpenChatroomActivity;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.commonObjectProperties;
import com.vritti.vwblib.receiver.MyFirebaseInstanceIDService;

/**
 * Created by 300151 on 9/30/2016.
 */
public class ActivityMain extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    public static SharedPreferences AtendanceSheredPreferance;
    static Context mContext;
    static MenuItem progressBar;
    ListView lsactivity_list, ls_Myworkspace;
    LinearLayout ls_Team;
    String FinalObj;
    String actid, time, starttime, sp_date;
    int sp_count;
    String getdate, currentTime;
    commonObjectProperties commonObj;
    ArrayList<ActivityBean> lsActivityList;
    ArrayList<BirthdayBean> lsBirthdayList;
    SQLiteDatabase sql;
    ActivityListMainAdapter activityListadapter;
    public static SharedPreferences userpreferences;
    private ProgressDialog progressDialog;
    Boolean IsSessionActivate;
    DrawerLayout drawer_layout;
    LinearLayout lay_ticket, lay_not_acted, lay_overdue, lay_today, lay_critical, lay_assign_by_me, lay_unapprove;
    TextView tv_activity_status, tv_birthday_cnt, tv_username, tv_meeting_cnt, tv_notification_cnt;
    TextView tv_assign_by_me, tv_critical, tv_today, tv_overdue, tv_notacted, tv_ticket, tv_workspacecnt, tv_workcnt, tv_team_mem_cnt, tv_unapprove;
    List<MyWorkspaceBean> lsmyworkspace;
    MyworkspaceAdapter mMenuAdapter;
    MyTeamAdapter teamAdapter;
    NavigationView navigationView;
    View header;
    LinearLayout layout_birthday, layout_meeting, layout_notification, lay_Myworkspace, lay_my_team, lay_mywork, lay_workspacewise_Act;
    MenuItem management, assign_activity;
    SharedPreferences timesheetpreferences;
    String ValidBackDate, ScratchWorkspaceId, ScratchWorkspaceName;
    ArrayList<MyTeamBean> lsMyteam;
    ActionBarDrawerToggle mDrawerToggle;


    Toolbar toolbar;
    SimpleDateFormat dfDate;
    public static Boolean Activity_AssignByMe = false;
    public static Boolean Activity_Unapprove = false;
    ProgressBar mprogress;
    Boolean FlagiSRefresh = true;
    String msg_version, App_Version_No;
    LinearLayout len_update;
    TextView txt_update_msg, txt_update, txt_app_version;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(ActivityMain.class.getName());

    private boolean running = false;
    Location location;
    String IsChatApplicable, IsGPSLocation;
    public static boolean isInFront;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_list_acivity);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        ActivityMain.this.registerReceiver(mMyBroadcastReceiver, new IntentFilter("assignscreen"));

        InitView();
        setListner();
        mContext = ActivityMain.this;


        lsActivityList = new ArrayList<ActivityBean>();
        lsBirthdayList = new ArrayList<BirthdayBean>();
        lsmyworkspace = new ArrayList<MyWorkspaceBean>();
        Intent intent = getIntent();
        msg_version = intent.getStringExtra("version");
        App_Version_No = intent.getStringExtra("app_version");

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);
        dfDate = new SimpleDateFormat("dd MMM yyyy");// 25 Oct 2016
        getdate = dfDate.format(new Date());// 17 Apr 2014
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar cl = Calendar.getInstance();
        currentTime = dateFormat.format(cl.getTime());
        // startServiceGPS();
        actid = AtendanceSheredPreferance.getString("ActivityId", null);
        starttime = AtendanceSheredPreferance.getString("Starttime", null);
        timesheetpreferences = getSharedPreferences(WebUrlClass.TIMESHEETINFO, Context.MODE_PRIVATE);

        ValidBackDate = timesheetpreferences.getString("ValidBackDate", null);
        IsChatApplicable = userpreferences.getString("chatapplicable", "");
        IsGPSLocation = userpreferences.getString("IsGpslocation", "");

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
        UserName = "";
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);

        sql = db.getWritableDatabase();
        ShortcutBadger.with(getApplicationContext()).remove();
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("count");
        editor.commit();


        /*Format dateFormat1 = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat1).toLocalizedPattern();
*/
        if (IsChatApplicable.equals("") & IsGPSLocation.equals("")) {
            if (isnet()) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGetEnvJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }

        }


        if (UserName != null) {
            tv_username.setText(UserName);
        }
        //  registerInBackground();


        if (cf.getWorkspacecount() > 0) {
            getMyWorkOnly();
            getMyWork();
        } else {

            if (isnet()) {
                showProgresHud();
                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadMyWorkDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(getApplicationContext(), "No Internet connection");
                //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
            }

        }


        if (cf.getActivityMasterCount() > 0) {
            UpdatList();
            getWorkspaceData();


        } else if (isnet()) {
            showProgresHud();
            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    Intent intent = new Intent(ActivityMain.this, DownloadWorkspaceData.class);
                    startService(intent);
                    new DownloadIsAllowedTimesheet().execute();
                    new DownloadMyWorkDataJSON().execute();
                    getActicityData();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    hideProgresHud();
                }
            });
        } else {
            ut.displayToast(getApplicationContext(), "No Internet connection");
            //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
        }

        if (msg_version == null || msg_version.equalsIgnoreCase("")) {
            len_update.setVisibility(View.GONE);
        } else {
            len_update.setVisibility(View.VISIBLE);
            txt_update_msg.setText(msg_version);
            txt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=vworkbench7.vritti.com.vworkbench7"));
                    startActivity(intent);
                }
            });

        }

        if (App_Version_No == null) {
            txt_app_version.setText("");

        } else {
            txt_app_version.setText("V." + App_Version_No);

        }
        int checkPermission = ContextCompat.checkSelfPermission(ActivityMain.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityMain.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }

        startService(new Intent(ActivityMain.this, GpsDataLocalSendSevice.class));
    }


    public void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                try {
                    String token = MyFirebaseInstanceIDService.Token();
                    Log.d("test", "token: " + token);
                } catch (Exception ex) {

                    Log.d("test", "Error: ");
                }
                return null;
            }

            @Override
            protected void onPostExecute(String msg) {
                // Toast.makeText(getApplicationContext(),
                // "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                // .show();
                Log.i("test", "Registered with GCM Server" + msg);
            }
        }.execute(null, null, null);
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle("vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Activity_main);
        lsMyteam = new ArrayList<MyTeamBean>();
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lay_mywork = (LinearLayout) findViewById(R.id.lay_mywork);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        lay_Myworkspace = (LinearLayout) findViewById(R.id.lay_Myworkspace);
        lay_workspacewise_Act = (LinearLayout) findViewById(R.id.lay_workspacewise_Act);
        tv_username = (TextView) findViewById(R.id.tv_username);
        //   tv_activity_status = (TextView) findViewById(R.id.tv_activity_status);
        tv_birthday_cnt = (TextView) findViewById(R.id.tv_birthday_cnt);
        tv_meeting_cnt = (TextView) findViewById(R.id.tv_meeting_cnt);
        tv_notification_cnt = (TextView) findViewById(R.id.tv_notification_cnt);
        lsactivity_list = (ListView) findViewById(R.id.lsactivity_list);
        layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
        layout_notification = (LinearLayout) findViewById(R.id.layout_notification);
        layout_meeting = (LinearLayout) findViewById(R.id.layout_meeting);
        lay_my_team = (LinearLayout) findViewById(R.id.lay_my_team);
        //ls_Myworkspace = (ListView) findViewById(R.id.ls_Myworkspace);
        // tv_new = (TextView) findViewById(R.id.tv_new);
        tv_critical = (TextView) findViewById(R.id.tv_critical);
        tv_notacted = (TextView) findViewById(R.id.tv_notacted);
        tv_overdue = (TextView) findViewById(R.id.tv_overdue);
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_ticket = (TextView) findViewById(R.id.tv_ticket);
        tv_workspacecnt = (TextView) findViewById(R.id.tv_workspacecnt);
        tv_assign_by_me = (TextView) findViewById(R.id.tv_assign_by_me);
        tv_workcnt = (TextView) findViewById(R.id.tv_workcnt);
        ls_Team = (LinearLayout) findViewById(R.id.ls_Team);
        tv_team_mem_cnt = (TextView) findViewById(R.id.tv_team_mem_cnt);
        tv_unapprove = (TextView) findViewById(R.id.tv_unapprove);

        lay_critical = (LinearLayout) findViewById(R.id.lay_critical);
        lay_assign_by_me = (LinearLayout) findViewById(R.id.lay_assign_by_me);
        lay_unapprove = (LinearLayout) findViewById(R.id.lay_unapprove);
        lay_not_acted = (LinearLayout) findViewById(R.id.lay_not_acted);
        lay_overdue = (LinearLayout) findViewById(R.id.lay_overdue);
        lay_today = (LinearLayout) findViewById(R.id.lay_today);
        lay_ticket = (LinearLayout) findViewById(R.id.lay_ticket);
        len_update = (LinearLayout) findViewById(R.id.len_update);
        txt_update_msg = (TextView) findViewById(R.id.txt_update_msg);
        txt_update = (TextView) findViewById(R.id.txt_update);
        txt_app_version = (TextView) findViewById(R.id.txt_app_version);


        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


       /* int index = lsactivity_list.getLastVisiblePosition();
        View v = lsactivity_list.getChildAt(0);
        int bottom = (v == null) ? 0 : (v.getTop() - lsactivity_list.getPaddingBottom());
        lsactivity_list.setSelectionFromTop(index, bottom);*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setListner() {
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
        lay_workspacewise_Act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, WorkspacewiseActCntActivity.class);
                startActivity(intent);
                finish();
            }
        });


        lay_my_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, MyTeamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                drawer_layout.closeDrawers();
            }
        });
        drawer_layout.setDrawerListener(mDrawerToggle);
        layout_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, BirthdayListAcyivity.class);
                startActivity(intent);


            }
        });
        layout_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, MeetingActivity.class);
                startActivity(intent);

            }
        });
        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, NotificationActivity.class);
                startActivity(intent);

            }
        });
        lay_mywork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdatList();
                drawer_layout.closeDrawers();
            }
        });
        lsactivity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Sourcetype = lsActivityList.get(position).getSourceType();
                if (ActivityMain.Activity_AssignByMe && Sourcetype.equalsIgnoreCase("DocAppr")) {

                } else {

                    String RRF = lsActivityList.get(position).getActivityName();
                    if (RRF.contains("RRF")) {
                        Toast.makeText(ActivityMain.this, "This functionality not available in mobile application", Toast.LENGTH_SHORT).show();
                    } else {
                        String assign = String.valueOf(ActivityMain.Activity_AssignByMe);
                        String Unapprove = String.valueOf(ActivityMain.Activity_Unapprove);

                        ActivityBean bean = lsActivityList.get(position);
                        ArrayList<ActivityBean> activityBeen = new ArrayList<ActivityBean>();
                        activityBeen.add(bean);
                        intent = new Intent(ActivityMain.this, ActivityDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("actbean", bean);
                        intent.putExtras(bundle);
                        if (Unapprove.equalsIgnoreCase("true")) {
                            intent.putExtra("unapprove", Unapprove);

                        } else {
                            intent.putExtra("unapprove", "");
                        }
                        if (assign.equalsIgnoreCase("true")) {
                            intent.putExtra("checkassign", assign);
                        } else {
                            intent.putExtra("checkassign", "");
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }

                }

            }
        });
        lsactivity_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                sp_date = AtendanceSheredPreferance.getString("sp_date", null);
                sp_count = AtendanceSheredPreferance.getInt("sp_count", 0);

                if (sp_date != null) {

                    if (sp_date.equalsIgnoreCase(getdate)) {
                        if (sp_count > 0) {
                            int i = sp_count + 1;
                            SharedPreferences.Editor editor = ActivityMain.AtendanceSheredPreferance
                                    .edit();
                            editor.putInt("sp_count", i);
                            editor.commit();
                        } else {
                            SharedPreferences.Editor editor = ActivityMain.AtendanceSheredPreferance
                                    .edit();
                            editor.putInt("sp_count", 1);
                            editor.commit();
                        }
                    } else {
                        SharedPreferences.Editor editor = ActivityMain.AtendanceSheredPreferance
                                .edit();
                        editor.putString("sp_date", getdate);
                        editor.putInt("sp_count", 1);
                        editor.commit();
                    }
                } else {
                    SharedPreferences.Editor editor = ActivityMain.AtendanceSheredPreferance
                            .edit();
                    editor.putString("sp_date", getdate);
                    editor.putInt("sp_count", 1);
                    editor.commit();
                }

                sp_date = AtendanceSheredPreferance.getString("sp_date", null);
                sp_count = AtendanceSheredPreferance.getInt("sp_count", 0);

                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new BiometricGpsAttendance().execute(String
                                .valueOf(sp_count));
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });


                String s = lsActivityList.get(position).getActivityName();
                String actid = ActivityMain.AtendanceSheredPreferance
                        .getString("actid", null);
                String Starttime = ActivityMain.AtendanceSheredPreferance
                        .getString("Starttime", null);
                //  String id = lsActivityList.get(position).getActid();

                if (actid != null) {

                    if (!(actid.equalsIgnoreCase(lsActivityList
                            .get(position).getActivityId()))) {
               /* Utilities.showCustomMessageDialog(
                        "One Activity is already started...",
                        "Alert!!", parent);*/
                    } else if (Starttime != null) {
                        Intent myIntent = new Intent();
                        myIntent.setClass(ActivityMain.this, LoggingTimeActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myIntent.putExtra("ActivityId",
                                lsActivityList.get(position).getActivityId());
                        myIntent.putExtra("ActivityName", lsActivityList
                                .get(position).getActivityName());
                        myIntent.putExtra("Flag", "End");
                        startActivity(myIntent);
                    }
                } else {
                    if (Starttime != null) {
                        Intent myIntent = new Intent();
                        myIntent.setClass(ActivityMain.this, LoggingTimeActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myIntent.putExtra("ActivityId",
                                lsActivityList.get(position).getActivityId());
                        myIntent.putExtra("ActivityName", lsActivityList
                                .get(position).getActivityName());
                        myIntent.putExtra("Flag", "End");
                        startActivity(myIntent);

                    } else {
                        Intent myIntent = new Intent();
                        myIntent.setClass(ActivityMain.this, LoggingTimeActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myIntent.putExtra("ActivityId",
                                lsActivityList.get(position).getActivityId());
                        myIntent.putExtra("ActivityName", lsActivityList
                                .get(position).getActivityName());
                        myIntent.putExtra("Flag", "Start");
                        startActivity(myIntent);
                    }
                }

                return true;
            }
        });

        lay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_ticket.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getTicketActivity();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_critical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_critical.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getCriticalActivity();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_not_acted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_notacted.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getNotActedActivity();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_overdue.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getOverdueActivity();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_today.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getTodayActivity();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_assign_by_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_assign_by_me.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getAssignByMeActicityData();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_unapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_unapprove.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getunapproveActicityData();
                    drawer_layout.closeDrawers();

                }
            }
        });
    }

    class BiometricGpsAttendance extends AsyncTask<String, Void, String> {
        Object res = null;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
            showProgresHud();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                String url;
                //http://vritti.ekatm.com/vwb/webservice/Activitywebservice.asmx/BiometricGpsAttendance?mobileno=9892305584&count=1
                String xml = "";
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String Time = sdf.format(new Date());
                url = CompanyURL
                        + "/api/BiometricAPI/BiometricGpsAttendance?UserMasterId="
                        + UserMasterId + "&time=''&Count=" + params[0];
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }

            return response;
        }

        protected void onPostExecute(String result) {
            //dismissProgressDialog();
            if (result.contains("Success")) {
                ut.displayToast(getApplicationContext(), "Data Send to server");

            } else {

                ut.displayToast(getApplicationContext(), "Failed to Send Data");
            }

        }
    }

    public static String httpGet(String urlString) throws IOException {
        URL url = new URL(urlString.replaceAll(" ", "%20"));

        //	URL url = new URL("http://vritti.vworkbench.com/webservice/ActivityWebservice.asmx/GetreportingGps?MobileNo=9890156056");
        Log.d("test", "url" + url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        Log.d("test", "conn" + conn);
        //conn.connect();
        int resCode = conn.getResponseCode();
        // Check for successful response code or throw error
        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
            //return "0";
        }

        // Buffer the result into a string
        BufferedReader buffrd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buffrd.readLine()) != null) {
            sb.append(line);
        }

        buffrd.close();

        conn.disconnect();
        return sb.toString();
    }

    private void getTicketActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER + " WHERE SourceType='Support'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                //bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                lsActivityList.add(bean);
            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void getCriticalActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER + " WHERE PriorityIndex='1'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                //bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                lsActivityList.add(bean);

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void getNotActedActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER + " WHERE TotalHoursBooked='0'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                ActivityBean bean = new ActivityBean();
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                //bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));


                lsActivityList.add(bean);

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void getOverdueActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER;
        Cursor cur = sql.rawQuery(query, null);
        Date EndDate = null, Todaydate = null;
        String Enddate, todaydate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String jsonEDate = cur.getString(cur.getColumnIndex("EndDate"));
                jsonEDate = jsonEDate.substring(jsonEDate.indexOf("(") + 1, jsonEDate.lastIndexOf(")"));
                // jsonEDate = jsonEDate.substring(jsonEDate.indexOf("(") + 1, jsonEDate.lastIndexOf(")"));
                long Etime = Long.parseLong(jsonEDate);
                EndDate = new Date(Etime);
                Todaydate = new Date();
                todaydate = sdf.format(Todaydate);
                Enddate = sdf.format(EndDate);
                try {
                    EndDate = sdf.parse(Enddate);
                    Todaydate = sdf.parse(todaydate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (EndDate.before(Todaydate) && !EndDate.equals(Todaydate)) {
                    ActivityBean bean = new ActivityBean();
                    bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                    bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                    bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                    bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                    bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                    bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                    bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                    bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                    bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                    bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                    bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                    bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                    bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                    bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                    bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                    bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                    bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                    bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                    bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                    bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                    bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                    bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                    // bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                    //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                    bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                    bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                    bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                    bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                    bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                    bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                    bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                    bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                    bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                    bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                    bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                    bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                    bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                    bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                    bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                    bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                    bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                    bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                    bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                    bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                    bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                    bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                    bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                    bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                    bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                    bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                    bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                    bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                    bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                    bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                    bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                    bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                    bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                    bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                    bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                    bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                    bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                    bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                    bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                    bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                    bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                    bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                    bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                    bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                    bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                    bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                    bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                    bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                    bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                    bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                    bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                    lsActivityList.add(bean);
                }

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void getTodayActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER;
        Cursor cur = sql.rawQuery(query, null);
        Date EndDate = null, Todaydate = null;
        String Enddate, todaydate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String jsonEDate = cur.getString(cur.getColumnIndex("EndDate"));
                jsonEDate = jsonEDate.substring(jsonEDate.indexOf("(") + 1, jsonEDate.lastIndexOf(")"));
                long Etime = Long.parseLong(jsonEDate);
                EndDate = new Date(Etime);
                Todaydate = new Date();
                todaydate = sdf.format(Todaydate);
                Enddate = sdf.format(EndDate);
                try {
                    EndDate = sdf.parse(Enddate);
                    Todaydate = sdf.parse(todaydate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (EndDate.equals(Todaydate)) {
                    ActivityBean bean = new ActivityBean();
                    bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                    bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                    bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                    bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                    bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                    bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                    bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                    bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                    bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                    bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                    bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                    bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                    bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                    bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                    bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                    bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                    bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                    bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                    bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                    bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                    bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                    bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                    // bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                    //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                    bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                    bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                    bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                    bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                    bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                    bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                    bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                    bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                    bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                    bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                    bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                    bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                    bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                    bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                    bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                    bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                    bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                    bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                    bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                    bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                    bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                    bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                    bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                    bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                    bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                    bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                    bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                    bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                    bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                    bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                    bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                    bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                    bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                    bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                    bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                    bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                    bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                    bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                    bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                    bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                    bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                    bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                    bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                    bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                    bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                    bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                    bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                    bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                    bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                    bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                    bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                    lsActivityList.add(bean);
                }

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.refresh) {


            if (isnet()) {
                showProgresHud();
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        FlagiSRefresh = false;
                        getActicityData();
                        startService(new Intent(ActivityMain.this, GpsDataLocalSendSevice.class));

                           /* String url ="http://vritti.ekatm.com/api/LoginAPI/GetEnvis";
                            JSONParser jParser = new JSONParser();
                            JSONObject json = jParser.getJSONFromUrl(url);*/

                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        hideProgresHud();

                    }
                });
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
            return true;
        } else if (id == R.id.refreshall) {
            if (isnet()) {
                showProgresHud();
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        FlagiSRefresh = true;

                        getActicityData();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
            return true;
        } else if (id == R.id.empinfo) {

            Intent i = new Intent(ActivityMain.this, ActivityEmployeeInformation.class);
            startActivity(i);
            return true;
        } else if (id == R.id.management) {

                Intent i1 = new Intent(ActivityMain.this, ActivityGPSLocalData.class);
                startActivity(i1);
                return true;
        } else if (id == R.id.assign_activity) {

                Intent intent = new Intent(ActivityMain.this, AssignActivity.class);
                startActivity(intent);
                return true;
        } else if (id == R.id.apply_claim) {

                Intent intent1 = new Intent(ActivityMain.this, ClaimMainActivity.class);
                startActivity(intent1);
                return true;
        } else if (id == R.id.view_gps) {

                Intent intent2 = new Intent(ActivityMain.this, GPSTeamList.class);
                startActivity(intent2);
                return true;
        } else if (id == R.id.settings) {

                Intent intent3 = new Intent(ActivityMain.this, ActivitySetting.class);
                startActivity(intent3);
                return true;
        } else if (id == R.id.Open_grp) {

                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    Intent intent5 = new Intent(ActivityMain.this, OpenChatroomActivity.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent5);
                } else {
                    Toast.makeText(getApplicationContext(), "Chat module is not installed", Toast.LENGTH_SHORT).show();
                }
                return true;
        } else if (id == R.id.claim_pay_noti) {

                Intent intent6 = new Intent(ActivityMain.this, ClaimNotificationActivity.class);
                startActivity(intent6);
                return true;
        } else if (id == R.id.apply_leave) {

                Intent intent4 = new Intent(ActivityMain.this, LeaveSummary.class);//LeaveSummary
                startActivity(intent4);
                return true;
        } else if (id == R.id.advance_request) {

                Intent advanceintent = new Intent(ActivityMain.this, EmployeeAdvanceRequestActivity.class);//LeaveSummary
                startActivity(advanceintent);
                return true;
        } else if (id == R.id.ticket_register) {
                Intent ticket = new Intent(ActivityMain.this, TicketRegisterActivity.class);//LeaveSummary
                startActivity(ticket);
                return true;
        } else{

            return super.onOptionsItemSelected(item);
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        management = menu.findItem(R.id.management);
        assign_activity = menu.findItem(R.id.assign_activity);
        // progressBar = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }


    private void getWorkspaceData() {
        lsmyworkspace.clear();
        String query = "SELECT * FROM " + db.TABLE_MYWORKSPACE;
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<MyWorkspaceBean> ls = new ArrayList<MyWorkspaceBean>();
        if (cur.getCount() > 0) {
            tv_workspacecnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyWorkspaceBean bean = new MyWorkspaceBean();

                double number = cur.getDouble(cur.getColumnIndex("OnTimePerc"));

                int integer = (int) number;
                double decimal = (10 * number - 10 * integer) / 10;
                bean.setOnTime(integer + " %");
                bean.setOpenActivities(cur.getString(cur.getColumnIndex("OpenActivities")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                lsmyworkspace.add(bean);
            } while (cur.moveToNext());
        }
        if (lsmyworkspace.size() > 0) {
            lay_Myworkspace.removeAllViews();
            if (lsmyworkspace.size() > 0 && lsmyworkspace.size() < 5) {
                for (int k = 0; k < lsmyworkspace.size(); k++) {
                    addView_new(k);
                }
            } else if (lsmyworkspace.size() >= 5) {
                for (int k = 0; k < 5; k++) {
                    addView_new(k);
                }
            }
        }
        getMyWork();
    }

    private void getWorkspaceDataOnly() {
        lsmyworkspace.clear();
        String query = "SELECT * FROM " + db.TABLE_MYWORKSPACE;
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<MyWorkspaceBean> ls = new ArrayList<MyWorkspaceBean>();
        if (cur.getCount() > 0) {
            tv_workspacecnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyWorkspaceBean bean = new MyWorkspaceBean();
                double number = cur.getDouble(cur.getColumnIndex("OnTimePerc"));
                int integer = (int) number;
                double decimal = (10 * number - 10 * integer) / 10;
                bean.setOnTime(integer + " %");
                bean.setOpenActivities(cur.getString(cur.getColumnIndex("OpenActivities")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                lsmyworkspace.add(bean);
            } while (cur.moveToNext());
        }
        if (lsmyworkspace.size() > 0) {
            lay_Myworkspace.removeAllViews();
            if (lsmyworkspace.size() > 0 && lsmyworkspace.size() < 5) {
                for (int k = 0; k < lsmyworkspace.size(); k++) {
                    addView_new(k);
                }
            } else if (lsmyworkspace.size() >= 5) {
                for (int k = 0; k < 5; k++) {
                    addView_new(k);
                }
            }
        }
    }

    private void getMyTeamData() {
        lsMyteam.clear();
        String query = "SELECT * FROM " + db.TABLE_MYTEAM;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            lay_my_team.setVisibility(View.VISIBLE);
            tv_team_mem_cnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyTeamBean bean = new MyTeamBean();
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setTotalOverdueActivities(cur.getString(cur.getColumnIndex("TotalOverdueActivities")));
                bean.setTotalCount(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                lsMyteam.add(bean);
            } while (cur.moveToNext());
            if (lsMyteam.size() > 0) {
                ls_Team.removeAllViews();
                if (lsMyteam.size() < 5) {
                    for (int k = 0; k < lsMyteam.size(); k++) {
                        add_viewTeam(k);
                    }
                } else {
                    for (int k = 0; k < 5; k++) {
                        add_viewTeam(k);
                    }
                }
            }
        }


    }

    private void getMyWork() {
        String query = "SELECT * FROM " + db.TABLE_MYWORK;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_critical.setText(cur.getString(cur.getColumnIndex("Critical")));
            tv_notacted.setText(cur.getString(cur.getColumnIndex("NotActed")));
            tv_overdue.setText(cur.getString(cur.getColumnIndex("Overdue")));
            tv_ticket.setText(cur.getString(cur.getColumnIndex("Tickets")));
            tv_today.setText(cur.getString(cur.getColumnIndex("Today")));
            tv_workcnt.setText(cur.getString(cur.getColumnIndex("TotalCount")));
            tv_assign_by_me.setText(cur.getString(cur.getColumnIndex("AssByCount")));
            tv_unapprove.setText(cur.getString(cur.getColumnIndex("UnApproved")));
        }
        UpdateBirthdayList();
    }

    private void getMyWorkOnly() {
        String query = "SELECT * FROM " + db.TABLE_MYWORK;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_critical.setText(cur.getString(cur.getColumnIndex("Critical")));
            tv_notacted.setText(cur.getString(cur.getColumnIndex("NotActed")));
            tv_overdue.setText(cur.getString(cur.getColumnIndex("Overdue")));
            tv_ticket.setText(cur.getString(cur.getColumnIndex("Tickets")));
            tv_today.setText(cur.getString(cur.getColumnIndex("Today")));
            tv_workcnt.setText(cur.getString(cur.getColumnIndex("TotalCount")));
            tv_assign_by_me.setText(cur.getString(cur.getColumnIndex("AssByCount")));
            tv_unapprove.setText(cur.getString(cur.getColumnIndex("UnApproved")));

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
        }
        return false;
    }

    public void getActicityData() {
        //  showProgressDialog();
        Activity_AssignByMe = false;
        Activity_Unapprove=false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;

        try {

            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("Operator", "<>");
            jsonObj.put("value1", "('15','12')");
            // jsonObj.put("value1", "('13','14','25')");


            jsonObj = jsoncommonObj.getJSONObject("ParentActId");
            jsonObj.put("IsSet", true);


        } catch (Exception e) {
            e.printStackTrace();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        new DownloadCommanDataURLJSON().execute();
    }

    public void getunapproveActicityData() {
        Activity_Unapprove=true;
        Activity_AssignByMe = false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;
        try {
            jsonObj = jsoncommonObj.getJSONObject("assignBy");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "('12')");

            jsonObj = jsoncommonObj.getJSONObject("Await");
            jsonObj.put("IsSet", true);



            jsonObj = jsoncommonObj.getJSONObject("SourceType");
            jsonObj.put("IsSet", true);
            jsonObj.put("Operator", "<>");
            jsonObj.put("value1", "Support");

            jsonObj = jsoncommonObj.getJSONObject("ParentActId");
            jsonObj.put("IsSet", true);


        } catch (Exception e) {
            e.printStackTrace();
            //dismissProgressDialog();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        new DownloadCommanDataURLJSON().execute();
    }

    public void getAssignByMeActicityData() {
        Activity_AssignByMe = true;
        Activity_Unapprove=false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;
        try {
            jsonObj = jsoncommonObj.getJSONObject("assignBy");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "('13')");


        } catch (Exception e) {
            e.printStackTrace();
            //dismissProgressDialog();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        new DownloadCommanDataURLJSON().execute();
    }



    private void UpdateBirthdayList() {
        String query = "SELECT * FROM " + db.TABLE_BIRTHDAY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            tv_birthday_cnt.setVisibility(View.VISIBLE);
        }
        tv_birthday_cnt.setText(cur.getCount() + "");
        UpdateMeeting();
    }

    private void UpdateBirthdayListonly() {
        String query = "SELECT * FROM " + db.TABLE_BIRTHDAY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            tv_birthday_cnt.setVisibility(View.VISIBLE);
        }
        tv_birthday_cnt.setText(cur.getCount() + "");
    }

    private void UpdatList() {
        lsActivityList.clear();
        String query = "SELECT * FROM '" + db.TABLE_ACTIVITYMASTER + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));


                bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
              //  bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));//SubActCount
                //bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                lsActivityList.add(bean);
            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);
            activityListadapter.notifyDataSetChanged();
        }
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

        getMyTeamData();

    }

    private void UpdateNotificationOnly() {
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

    private void UpdateMeetingOnly() {
        String query = "SELECT * FROM " + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_meeting_cnt.setVisibility(View.VISIBLE);
            tv_meeting_cnt.setText(cur.getCount() + "");
        } else {
            tv_meeting_cnt.setText(0 + "");
        }


    }

    class DownloadUserNameDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_UserName;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\\\\\\\\"", "");

                    response = response.replaceAll("\\\\", "");
                    response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                    String UserName = response.substring(0, response.indexOf("|"));
                    tv_username.setText(response.substring(0, response.indexOf("|")));
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("UserName", UserName);
                    editor.commit();
                } else {
                    //   ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                //ut.displayToast(getApplicationContext(), "Server error...");
            }
            new DownloadValidBackDateDataJSON().execute();

        }

    }

    class DownloadValidBackDateDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_valid_backdate_entry;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ValidBackDate = response;
              /*  JSONObject jobj = new JSONObject(response);
                for (int i = 0; i < jobj.length(); i++) {
                    ScratchWorkspaceId = jobj.getString("ProjectId");
                    ScratchWorkspaceName = jobj.getString("ProjectName");
                }*/

            } catch (Exception e) {
                e.printStackTrace();
                response = null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            hideProgresHud();
            if (res != null) {
                SharedPreferences.Editor editor = timesheetpreferences.edit();
                editor.putString("ValidBackDate", ValidBackDate);
                editor.commit();
            } else {
                //   ut.displayToast(getApplicationContext(), "Server error...");
            }

            getWorkspaceData();
        }

    }


    class DownloadBirthdayDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Birthday;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                sql.delete(db.TABLE_BIRTHDAY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BIRTHDAY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    BirthdayBean bean = new BirthdayBean();
                    bean.setDtDay(jorder.getString("DtDay"));
                    bean.setEmail(jorder.getString("Email"));
                    bean.setImagePath(jorder.getString("ImagePath"));
                    bean.setUserLoginId(jorder.getString("UserLoginId"));
                    bean.setUserMasterID(jorder.getString("UserMasterID"));
                    bean.setUserName(jorder.getString("UserName"));
                    bean.setTitle(jorder.getString("Title"));
                    bean.setMobile(jorder.getString("Mobile"));
                    String jsonDOJ = jorder.getString("DOJ");
                    jsonDOJ = jsonDOJ.substring(jsonDOJ.indexOf("(") + 1, jsonDOJ.lastIndexOf(")"));
                    long DOJ_date = Long.parseLong(jsonDOJ);
                    DOJDate = new Date(DOJ_date);
                    jsonDOJ = sdf.format(DOJDate);
                    bean.setDOJ(jsonDOJ);
                    String jsonDOB = jorder.getString("DOB");
                    jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                    long DOB_date = Long.parseLong(jsonDOB);
                    DOBDate = new Date(DOB_date);
                    jsonDOB = sdf.format(DOBDate);
                    bean.setDOB(jsonDOB);
                    cf.AddBirthday(bean);
                    lsBirthdayList.add(bean);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {

                } else {
                    //    ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                ///   ut.displayToast(getApplicationContext(), "Server error...");
            }

            UpdateBirthdayListonly();
            new DownloadMeetingDataJSON().execute();

        }

    }

    /*class DownloadNotificationDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Notification;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MYWORK, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYWORK, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MYWORK, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                new DownloadTeamDataJSON().execute();
            }
        }

    }*/
    class DownloadNotificationDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Notification;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
              /*  response =      "[\n" +
                      "    {\n" +
                      "        \"PKNotifDtlsId\": \"CA28A96B-33E5-43AC-8E82-67391FFD4944\",\n" +
                      "        \"NotifText\": \"Dear all,This is inform to all that, we had started M. S. Office training session from 13th Feb in 2 batches, but we have received bad response from all the dept. Only 21 no. employees who have attended training session. It is a mandatory for all employees to attend training session.You are hereby requested to all HOD to nominate their participant and send list to HR.As per last cross function meeting, it has been decided to conduct M. S. Office training session all over branches and finish it mid of March.All branch head and coordinator to conduct training session and send your your feedback earliest\",\n" +
                      "        \"NotifTitle\": \"M. S Office Training\",\n" +
                      "        \"NotifContent\": \" \",\n" +
                      "        \"NotifShortContent\": \"\",\n" +
                      "        \"NotificationTypeId\": \"335d4c79-3892-4198-822e-dfb2e1b768f5\",\n" +
                      "        \"TypeName\": \"Suggestion\",\n" +
                      "        \"UserName\": \"Parag Pande\",\n" +
                      "        \"AddedDt\": \"02 Mar 2017 10:08\",\n" +
                      "        \"DisplayFromDate\": \"02/03/2017 09:54:00\",\n" +
                      "        \"DisplayToDate\": \"31/03/2017 09:03:00\"\n" +
                      "    },\n" +
                      "    {\n" +
                      "        \"PKNotifDtlsId\": \"ED51F45C-F9E7-4863-AF9A-AC3E3B86BC39\",\n" +
                      "        \"NotifText\": \"Dear All,We take this opportunity  in extending a warm welcome to Mr. Rajagopala Bhat designated as Operations Manager at  Karnataka  who has joined us from  15th  Feb,2017.He may be reached on email id : r.bhat@vritti.co.inHe shall be immediately reporting to Mr. Jeevan PawarWe look forward to your support and cooperation to Mr.Bhat in his current assignments and wish him an exciting u0026 long term career with all of us at VRITTI Family !!! Thanks u0026 Regards, HR TeamVritti Solutions Limited \",\n" +
                      "        \"NotifTitle\": \"Welcome Note-Mr.Rajagopala Bhat\",\n" +
                      "        \"NotifContent\": \"\",\n" +
                      "        \"NotifShortContent\": \"\",\n" +
                      "        \"NotificationTypeId\": \"d73cdde2-1b2b-4024-9e77-aca6a611ac1f\",\n" +
                      "        \"TypeName\": \"Events    \",\n" +
                      "        \"UserName\": \"Dhiraj Naik\",\n" +
                      "        \"AddedDt\": \"21 Feb 2017 13:27\",\n" +
                      "        \"DisplayFromDate\": \"21/02/2017 13:27:00\",\n" +
                      "        \"DisplayToDate\": \"21/03/2017 13:27:00\"\n" +
                      "    },\n" +
                      "    {\n" +
                      "        \"PKNotifDtlsId\": \"5B9CBE8E-6A01-4BA8-997E-29FA6071D7C1\",\n" +
                      "        \"NotifText\": \"Dear all,As per Tax projection, all are requested to send their investment documents (Xerox copy/ proof) to HR before end of this month. This is very important for Tax deduction.\",\n" +
                      "        \"NotifTitle\": \"IT Deceleration Documents\",\n" +
                      "        \"NotifContent\": \"\",\n" +
                      "        \"NotifShortContent\": \"\",\n" +
                      "        \"NotificationTypeId\": \"1c3cc6cf-ba2f-46ad-a68f-0b04e6e72368\",\n" +
                      "        \"TypeName\": \"Memo      \",\n" +
                      "        \"UserName\": \"Parag Pande\",\n" +
                      "        \"AddedDt\": \"20 Feb 2017 12:40\",\n" +
                      "        \"DisplayFromDate\": \"20/02/2017 12:23:00\",\n" +
                      "        \"DisplayToDate\": \"20/03/2017 12:23:00\"\n" +
                      "    }\n" +
                      "]";*/

                try {
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_NOTIFICATION, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NOTIFICATION, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }
                        long a = sql.insert(db.TABLE_NOTIFICATION, null, values);
                    }
                } catch (JSONException e) {

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            UpdateNotificationOnly();
            if (UserName == null || ValidBackDate == null) {
                new DownloadUserNameDataJSON().execute();
            } else {
                hideProgresHud();
                getWorkspaceData();
            }
        }

    }

    class DownloadMeetingDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Meetings;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MEETING, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MEETING, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MEETING, null, values);
                    Log.e("sqlinsert :", "" + a);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    ///   ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                // ut.displayToast(getApplicationContext(), "Server error...");
            }
            UpdateMeetingOnly();
            new DownloadNotificationDataJSON().execute();

        }

    }

    /*private void showProgressDialog() {
        progressHUD = ProgressHUD.show(ActivityMain.this, "", false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }*/

    class DownloadTeamDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_MyTeam;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                try {


                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_MYTEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYTEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_MYTEAM, null, values);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {

                } else {
                    //   ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                //  ut.displayToast(getApplicationContext(), "Server error...");
            }

            getMyTeamData();

            new DownloadBirthdayDataJSON().execute();

        }

    }

    class DownloadMyWorkspaceDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_MyWorkspace;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
              *//*  res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MYWORKSPACE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYWORKSPACE, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_MYWORKSPACE, null, values);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    // ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                //  ut.displayToast(getApplicationContext(), "Server error...");
            }
            getWorkspaceDataOnly();
            new DownloadMyWorkDataJSON().execute();
        }

    }

    class DownloadMyWorkDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Mywork;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                //  response = response.replaceAll("\\\\\"\\\\", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MYWORK, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYWORK, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_MYWORK, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    //  ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                //  ut.displayToast(getApplicationContext(), "Server error...");
            }
            getMyWorkOnly();
            getMyWork();
            new DownloadTeamDataJSON().execute();
        }

    }

    public void addView_new(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) ActivityMain.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_custom_myworkspace, null);

        final TextView tv_Workspacename, tv_Openactivity, tv_onTime;
        final LinearLayout ln_workSpace;
        ln_workSpace = (LinearLayout) baseView.findViewById(R.id.workview);
        tv_Workspacename = (TextView) baseView.findViewById(R.id.tv_Workspacename);
        tv_Openactivity = (TextView) baseView.findViewById(R.id.tv_Openactivity);
        tv_onTime = (TextView) baseView.findViewById(R.id.tv_onTime);


        tv_Workspacename.setText(lsmyworkspace.get(i).getProjectName());
        tv_Openactivity.setText(lsmyworkspace.get(i).getOpenActivities());
        tv_onTime.setText(lsmyworkspace.get(i).getOnTime());

        ln_workSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String que = "SELECT ProjectId FROM " + db.TABLE_MYWORKSPACE + " WHERE ProjectName='" + lsmyworkspace.get(i).getProjectName() + "'";
                Cursor cur = sql.rawQuery(que, null);
                String ProjectId = null;
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ProjectId = cur.getString(cur.getColumnIndex("ProjectId"));
                }
                Intent intent = new Intent(ActivityMain.this, WorkspacewiseActDetailActivity.class);
                intent.putExtra("ProjectId", ProjectId);
                intent.putExtra("ProjectName", lsmyworkspace.get(i).getProjectName());
                startActivity(intent);
                finish();
            }
        });
        lay_Myworkspace.addView(baseView);

    }


    public void add_viewTeam(int i) {
        LayoutInflater layoutInflater = (LayoutInflater) ActivityMain.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_custom_myteam,
                null);
        TextView tv_memusername, tv_overdue, tv_total;
        LinearLayout team_member;

        tv_memusername = (TextView) baseView.findViewById(R.id.tv_memusername);
        tv_overdue = (TextView) baseView.findViewById(R.id.tv_overdue);
        tv_total = (TextView) baseView.findViewById(R.id.tv_total);
        team_member = (LinearLayout) baseView.findViewById(R.id.teammember);
        tv_memusername.setText(lsMyteam.get(i).getUserName());
        tv_total.setText("/" + lsMyteam.get(i).getTotalCount());
        tv_overdue.setText(lsMyteam.get(i).getTotalOverdueActivities());
        final int pos = i;
        team_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyTeamMemberActivity.class);
                intent.putExtra("UsermasterID", lsMyteam.get(pos).getUserMasterId());
                intent.putExtra("Username", lsMyteam.get(pos).getUserName());
                startActivity(intent);
                finish();
            }
        });
        ls_Team.addView(baseView);
    }

    class DownloadCommanDataURLJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_PostloadWorkData;
            try {
                res = ut.OpenPostConnection(url, FinalObj, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_ACTIVITYMASTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        if (columnName.equalsIgnoreCase("ActivityName")) {
                            columnValue = jorder.getString(columnName);
                            columnValue = URLDecoder.decode(columnValue, "UTF-8");
                        }
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_ACTIVITYMASTER, null, values);
                    Log.e("", "" + a);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            hideProgresHud();
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    ut.displayToast(getApplicationContext(), "Could not connect to server");
                }
            } else {
                ut.displayToast(getApplicationContext(), "Could Not Connect to server");
            }

            UpdatList();
            if (FlagiSRefresh) {
                showProgresHud();
                new DownloadMyWorkspaceDataJSON().execute();
            }
        }

    }


    class DownloadLeavereportingTo extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_ReportingTo;


            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.toString().replaceAll("\\\\\\\\\\\"", "");
                res = res.replaceAll("\\\\", "");
                res = res.replaceAll("u0026", "&");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_LEAVE_REPORTING_TO, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LEAVE_REPORTING_TO, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }
                    long a = sql.insert(db.TABLE_LEAVE_REPORTING_TO, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);


        }

    }

    class DownloadIsAllowedTimesheet extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_check_isSlotAllowed + "&UserMstrId=" + UserMasterId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.toString().replaceAll("\\\\\\\\\\\"", "");
                res = res.replaceAll("\\\\", "");
                res = res.replaceAll("u0026", "&");
                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            SharedPreferences.Editor editor = timesheetpreferences.edit();
            editor.putString("IsTimeslotBooked", integer);
            editor.commit();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgresHud();
        isInFront = false;

    }

    private void showProgresHud() {
        mprogress.setVisibility(View.VISIBLE);
      /*  if (progressBar != null) {
            progressBar.setVisible(true);
        }*/
    }

    private void hideProgresHud() {
        mprogress.setVisibility(View.GONE);
        /* if (progressBar != null) {
            progressBar.setVisible(false);
        }*/
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this, Manifest.permission.ACCESS_FINE_LOCATION)) {



        } else {

            ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

                    // Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();
                    requestPermission();
                }
                break;
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
                res = ut.OpenConnection(url.trim(), getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);

                if (res.contains("AppEnvMasterId")) {

                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        IsChatApplicable = jorder.getString("IsChatApplicable");
                        if (jorder.has("IsGPSLocation")) {
                            IsGPSLocation = jorder.getString("IsGPSLocation");
                        }
                        SharedPreferences.Editor editor = userpreferences.edit();
                        editor.putString("chatapplicable", IsChatApplicable);
                        editor.putString("IsGpslocation", IsGPSLocation);
                        editor.commit();
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


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
        ShortcutBadger.with(getApplicationContext()).remove();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("count");
        editor.commit();
        if (cf.getActivityMasterCount() > 0)
        {
            UpdatList();
            getWorkspaceData();

        }
    }


    BroadcastReceiver mMyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (cf.getActivityMasterCount() > 0)
            {
                UpdatList();
                getWorkspaceData();

            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityMain.this.unregisterReceiver(mMyBroadcastReceiver);
    }

}

