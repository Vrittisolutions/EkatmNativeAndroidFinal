package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.Dialog;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vritti.DliveryModule.DeliveryDetailPage;
import com.vritti.MilkModule.MilkRunLocationListActivity;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.chat.bean.DefaultUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.activity.ActivityLogIn;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ActivityListMainAdapter;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;
import com.vritti.vwb.Adapter.MyTeamAdapter;
import com.vritti.vwb.Adapter.MyworkspaceAdapter;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.Beans.BirthdayBean;
import com.vritti.vwb.Beans.MyTeamBean;
import com.vritti.vwb.Beans.MyWorkspaceBean;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.DownloadWorkspaceData;
import com.vritti.ekatm.services.GpsDataLocalSendSevice;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.FileUtils;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.classes.commonObjectProperties;

/**
 * Created by 300151 on 9/30/2016.
 */
public class ActivityMain extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", Designation = "", SourceId = "";

    int rowStart = 0, rowEnd = 9;
    String reQuery = "Y", activityType = "";
    static String settingKey = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    //CommonObject commonObgj;
    commonObjectProperties commonObjectProperties;

    public static SharedPreferences AtendanceSheredPreferance;
    public static SharedPreferences userpreferences;

    static Context mContext;
    static MenuItem progressBar;
    Parcelable state;
    RecyclerView lsactivity_list;//, ls_Myworkspace;
    SwipeRefreshLayout mSwipeRefreshLayout;

    LinearLayout ls_Team;
    String FinalObj;
    String actid, time, starttime, sp_date;
    int sp_count;
    String getdate, currentTime;
    commonObjectProperties commonObj;
    ArrayList<ActivityBean> lsActivityList;
    ArrayList<ActivityBean> filterTempList;
    ArrayList<BirthdayBean> lsBirthdayList;
    SQLiteDatabase sql;
    ActivityListMainAdapter activityListadapter;
    ActivityListMainAdapter_New activityListadapterNew;

    private ProgressDialog progressDialog;
    Boolean IsSessionActivate;
    DrawerLayout drawer_layout;
    LinearLayout lay_ticket, lay_not_acted, lay_overdue, lay_today, lay_critical, lay_assign_by_me, lay_unapprove, lay_unplanned;
    TextView tv_activity_status, tv_birthday_cnt, tv_username, tv_meeting_cnt, tv_notification_cnt;
    TextView tv_assign_by_me, tv_critical, tv_today, tv_overdue, tv_notacted, tv_ticket, tv_workspacecnt, tv_workcnt, tv_team_mem_cnt,
            tv_subteam_mem_cnt, tv_unapprove, tv_plusSign, tv_unplanned;
    List<MyWorkspaceBean> lsmyworkspace;
    MyworkspaceAdapter mMenuAdapter;
    MyTeamAdapter teamAdapter;
    NavigationView navigationView;
    View header;
    LinearLayout layout_birthday, layout_meeting, layout_notification, lay_Myworkspace, lay_my_team, lay_mywork, lay_workspacewise_Act;
    MenuItem management, assign_activity;
    ArrayList<MyTeamBean> lsMyteam;
    ActionBarDrawerToggle mDrawerToggle;


    Toolbar toolbar;
    LinearLayout ln_worklist;
    SimpleDateFormat dfDate;
    public static Boolean Activity_AssignByMe = false;
    public static Boolean Activity_Unapprove = false;
    ProgressBar mprogress;
    Boolean FlagiSRefresh = true;
    LinearLayout len_update;
    TextView txt_update_msg, txt_update, txt_app_version;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(ActivityMain.class.getName());

    private boolean running = false;
    Location location;
    String IsChatApplicable, IsGPSLocation;
    public static boolean isInFront;
    private Intent intent;
    ArrayList<DefaultUser> defaultUserArrayList;

    /////////////
    int backToposition = -1;
    int startpos = -1;
    SimpleDraweeView img_userpic;
    private int APP_REQUEST_CODE = 4478;
    File file;
    private static int RESULT_LOAD_IMG = 1;

    private static final int RESULT_CAPTURE_IMG = 2;
    private Uri outPutfileUri;
    Bitmap uploadFileBitMap;
    private String current_date, yesterday_date = "06-06-2018", offday;
    String res = "0";
    EditText edt_search;
    String ActivityName;
    TextView txt_title, txt_title1;
    private ImageView img_notification, img_birthday,img_att;
    TextView txt_loadMore;
    int searchClickCount = 0;
    ProgressBar progress_bar;
    //ImageView img_chat;
    //LinearLayout len_progressbar;
    boolean firstTime = false;
    TextView txt_team_title;
    String callFrom="";
    ImageView img_add,img_refresh,img_back;
    TextView textview_title;

    String Activity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_list_acivity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ActivityMain.this.registerReceiver(mMyBroadcastReceiver, new IntentFilter("assignscreen"));
        lsActivityList = new ArrayList<ActivityBean>();
        filterTempList = new ArrayList<ActivityBean>();

        InitView();
        setListner();

        mContext = ActivityMain.this;
        lsBirthdayList = new ArrayList<BirthdayBean>();
        lsmyworkspace = new ArrayList<MyWorkspaceBean>();
        defaultUserArrayList = new ArrayList<>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);
        dfDate = new SimpleDateFormat("dd MMM yyyy");// 25 Oct 2016
        getdate = dfDate.format(new Date());// 17 Apr 2014
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar cl = Calendar.getInstance();
        currentTime = dateFormat.format(cl.getTime());
        actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);

        context = ActivityMain.this;
        ut = new Utility();
        cf = new CommonFunction(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        Designation = ut.getValue(context, WebUrlClass.GET_Designation, settingKey);


        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        IsGPSLocation = ut.getValue(context, WebUrlClass.GET_ISGPSLOCATION_KEY, settingKey);


        if (getIntent().hasExtra("activty")) {
            Activity = getIntent().getStringExtra("activty");
        }

        defaultUserArrayList.clear();
        /*if(ut.IsChangePassword(context)){
            startActivity(new Intent(this , ActivityLogIn.class));
            finishAffinity();
        }*/

        lsactivity_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    String reQuery = "N";
                    loadNextActivity(reQuery);
                }else {

                }
            }
        });


         if (Activity.equals("act_work")) {
             Activity_AssignByMe = false;
             Activity_Unapprove = false;
             if (ut.isNet(context)) {
                 new StartSession(ActivityMain.this, new CallbackInterface() {
                     @Override
                     public void callMethod() {
                         new DownloadWorkloadActivity().execute(UserMasterId, "");
                     }

                     @Override
                     public void callfailMethod(String msg) {
                         Toast.makeText(ActivityMain.this, "Unable to Parse Paging Json", Toast.LENGTH_SHORT).show();
                     }
                 });
             } else {
                 Toast.makeText(ActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
             }
         }


        if (Constants.type == Constants.Type.Sahara) {

            if (Constants.type == Constants.Type.Sahara && Designation.equalsIgnoreCase("school")) {

                // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                // Remove hamburger
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                // Show back button
                // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                layout_notification.setVisibility(View.VISIBLE);
                layout_birthday.setVisibility(View.VISIBLE);
                img_notification.setBackground(getResources().getDrawable(R.drawable.white_home));
                img_birthday.setBackground(getResources().getDrawable(R.drawable.sahara_ic_search));
                img_notification.setColorFilter(ContextCompat.getColor(ActivityMain.this, R.color.white));
                img_birthday.setColorFilter(ContextCompat.getColor(ActivityMain.this, R.color.white));
                layout_birthday.setVisibility(View.GONE);
                ln_worklist.setBackground(getResources().getDrawable(R.drawable.sahara_gradient));
                toolbar.setVisibility(View.VISIBLE);
                //  toolbar.setTitle(UserName + "\n" + "UDISE Number :" + LoginId);
                txt_title.setTextColor(Color.parseColor("#FFFFFF"));
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                txt_title.setText(UserName + "\n" + "UDISE Number :" + LoginId);

            } else if (Constants.type == Constants.Type.Sahara) {
                txt_team_title.setText("Reportee");
                lay_not_acted.setVisibility(View.GONE);
                lay_ticket.setVisibility(View.GONE);
                lay_critical.setVisibility(View.GONE);
                lay_unapprove.setVisibility(View.GONE);
                tv_subteam_mem_cnt.setVisibility(View.INVISIBLE);
                tv_plusSign.setVisibility(View.INVISIBLE);

                layout_meeting.setVisibility(View.VISIBLE);
                layout_birthday.setVisibility(View.VISIBLE);
                img_birthday.setBackground(getResources().getDrawable(R.drawable.sahara_ic_search));
                img_notification.setBackground(getResources().getDrawable(R.drawable.white_home));
                img_notification.setColorFilter(ContextCompat.getColor(ActivityMain.this, R.color.white));
                //  ln_worklist1.setVisibility(View.GONE);
                // txt_title1.setText("Work List");
                ln_worklist.setBackground(getResources().getDrawable(R.drawable.sahara_gradient));
                //ln_worklist1.setBackground(getResources().getDrawable(R.color.colorPrimary));
                txt_title.setText(UserName);
            } else {
                img_notification.setBackground(getResources().getDrawable(R.drawable.notification_icon));
            }
        } else {
            img_notification.setBackground(getResources().getDrawable(R.drawable.notification_icon));

        }

        if (UserName != "") {
            tv_username.setText(UserName);
        }
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        current_date = df.format(c);
        String yesterday_date1 = userpreferences.getString("Yesterday", yesterday_date);
        offday = userpreferences.getString("Birthday", "0");
        if (!current_date.equals(yesterday_date1)) {
            if (isnet()) {
                showProgresHud();
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadBirthdayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(ActivityMain.this, msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(ActivityMain.this, "No Internet connection");
            }
        } else {
            if (offday.equalsIgnoreCase("1")) {
                UpdateBirthdayListonly();
            } else {
                if (isnet()) {
                    showProgresHud();
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadBirthdayDataJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(ActivityMain.this, msg);
                            hideProgresHud();
                        }
                    });
                } else {
                    ut.displayToast(ActivityMain.this, "No Internet connection");
                    //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                }
            }
        }

        new downloadworkspacecnt().execute();

        ChatMessageCount();
        /*int notiCount = AppCommon.getInstance(this).getNotificationCount();
        String Msgcount = String.valueOf(notiCount);
        if (notiCount != 0) {
            // tv_meeting_cnt.setVisibility(View.VISIBLE);
            startActivity(new Intent(this, OpenChatroomActivity.class));
        }*/


        if (cf.getWorkspacecount() > 0) {
            getMyWorkOnly();
            getMyWork();
            getMyTeamData();
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
                        ut.displayToast(ActivityMain.this, msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(ActivityMain.this, "No Internet connection");
                //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
            }

        }


        if (cf.getActivityMasterCount_Paging() > 0) {
            updateList_Paging();
        } else {
            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, "");
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(ActivityMain.this, "Unable to Parse Paging Json", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

        if (cf.getMYWORKSPACE() > 0) {
            //updateList();
            getWorkspaceData();


        } else if (isnet()) {
            showProgresHud();
            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    Intent intent = new Intent(ActivityMain.this, DownloadWorkspaceData.class);
                    startService(intent);
                    new DownloadIsAllowedTimesheet().execute();
                    //   new DownloadMyWorkDataJSON().execute();
                    new DownloadMyWorkspaceDataJSON().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(ActivityMain.this, msg);
                    hideProgresHud();
                }
            });
        } else {
            ut.displayToast(ActivityMain.this, "No Internet connection");
        }

        String userImageUri = AppCommon.getInstance(context).getImageUrl();

        if (userImageUri != null)
            img_userpic.setController(AppCommon.getDraweeController(img_userpic, userImageUri, 300));
        if (isnet()) {
            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUSerImageJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(ActivityMain.this, msg);
                    //hideProgresHud();
                }
            });
        } else {
            ut.displayToast(ActivityMain.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }


        if (WebUrlClass.APP_NEW_VERSION != "" && !WebUrlClass.APP_NEW_VERSION.isEmpty()) {
            if (value(WebUrlClass.APP_CURRENT_VERSION) < value(WebUrlClass.APP_NEW_VERSION) || value(WebUrlClass.APP_CURRENT_VERSION) > value(WebUrlClass.APP_NEW_VERSION)) {
                final String Update_Message = "A new version " + WebUrlClass.APP_NEW_VERSION + " of application is now available on playstore";

                len_update.setVisibility(View.VISIBLE);
                txt_update_msg.setText(Update_Message);

                txt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Constants.type == Constants.Type.Vwb) {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=vworkbench7.vritti.com.vworkbench7"));
                            startActivity(intent);
                        } else if (Constants.type == Constants.Type.PM) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=id=practice.vritti.com"));
                            startActivity(intent);

                        }else if (Constants.type == Constants.Type.NepalPM) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=id=nepalpractice.vritti.com"));
                            startActivity(intent);

                        } else if (Constants.type == Constants.Type.Delivery) {  // delivery application
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=id=vworkbench7.vritti.com.delivery"));
                            startActivity(intent);
                        }
                    }
                });

            } else {
                len_update.setVisibility(View.GONE);
            }
        } else {
            len_update.setVisibility(View.GONE);
        }

        try {
            WebUrlClass.APP_CURRENT_VERSION = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (WebUrlClass.APP_CURRENT_VERSION == "") {
            txt_app_version.setText("");

        } else {
            txt_app_version.setText("V." + WebUrlClass.APP_CURRENT_VERSION);

        }
        int checkPermission = ContextCompat.checkSelfPermission(ActivityMain.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityMain.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }


    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);

        /*
        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/
        txt_title.setText("Work List");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ln_worklist = findViewById(R.id.ln_worklist);
        mprogress = (ProgressBar) findViewById(R.id.progress_bar);
        lsMyteam = new ArrayList<MyTeamBean>();
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lay_mywork = (LinearLayout) findViewById(R.id.lay_mywork);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        lay_Myworkspace = (LinearLayout) findViewById(R.id.lay_Myworkspace);
        lay_workspacewise_Act = (LinearLayout) findViewById(R.id.lay_workspacewise_Act);
        tv_username = (TextView) findViewById(R.id.tv_username);
        img_userpic = findViewById(R.id.img_userpic);
        //   tv_activity_status = (TextView) findViewById(R.id.tv_activity_status);
        tv_birthday_cnt = (TextView) findViewById(R.id.tv_birthday_cnt);
        tv_meeting_cnt = (TextView) findViewById(R.id.tv_meeting_cnt);
        tv_notification_cnt = (TextView) findViewById(R.id.tv_notification_cnt);
        lsactivity_list = findViewById(R.id.lsactivity_list);
        layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
        layout_notification = (LinearLayout) findViewById(R.id.layout_notification);
        layout_meeting = (LinearLayout) findViewById(R.id.layout_meeting);
        lay_my_team = (LinearLayout) findViewById(R.id.lay_my_team);
        lay_critical = (LinearLayout) findViewById(R.id.lay_critical);
        lay_assign_by_me = (LinearLayout) findViewById(R.id.lay_assign_by_me);
        lay_unapprove = (LinearLayout) findViewById(R.id.lay_unapprove);
        lay_not_acted = (LinearLayout) findViewById(R.id.lay_not_acted);
        lay_overdue = (LinearLayout) findViewById(R.id.lay_overdue);
        lay_today = (LinearLayout) findViewById(R.id.lay_today);
        lay_ticket = (LinearLayout) findViewById(R.id.lay_ticket);
        //ls_Myworkspace = (ListView) findViewById(R.id.ls_Myworkspace);
        // tv_new = (TextView) findViewById(R.id.tv_new);
        tv_critical = (TextView) findViewById(R.id.tv_critical);
        tv_notacted = (TextView) findViewById(R.id.tv_notacted);
        tv_overdue = (TextView) findViewById(R.id.tv_overdue);
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_ticket = (TextView) findViewById(R.id.tv_ticket);
        //  tv_unplanned = (TextView) findViewById(R.id.tv_unplanned);
        tv_workspacecnt = (TextView) findViewById(R.id.tv_workspacecnt);
        tv_assign_by_me = (TextView) findViewById(R.id.tv_assign_by_me);
        tv_workcnt = (TextView) findViewById(R.id.tv_workcnt);
        ls_Team = (LinearLayout) findViewById(R.id.ls_Team);
        tv_team_mem_cnt = (TextView) findViewById(R.id.tv_team_mem_cnt);
        tv_subteam_mem_cnt = (TextView) findViewById(R.id.tv_subteam_mem_cnt);
        tv_plusSign = (TextView) findViewById(R.id.tv_plusSign);
        tv_unapprove = (TextView) findViewById(R.id.tv_unapprove);

        txt_title = findViewById(R.id.txt_title);
        len_update = (LinearLayout) findViewById(R.id.len_update);
        txt_update_msg = (TextView) findViewById(R.id.txt_update_msg);
        txt_update = (TextView) findViewById(R.id.txt_update);
        txt_app_version = (TextView) findViewById(R.id.txt_app_version);
        edt_search = (EditText) findViewById(R.id.edt_search);
        img_notification = (ImageView) findViewById(R.id.img_notification);
        img_birthday = (ImageView) findViewById(R.id.img_birthday);
        //  img_att = (ImageView) findViewById(R.id.img_att);


        edt_search = (EditText) findViewById(R.id.edt_search);
        img_notification = (ImageView) findViewById(R.id.img_notification);
        img_birthday = (ImageView) findViewById(R.id.img_birthday);
        progress_bar = findViewById(R.id.progress_bar);
        //txt_team_title = findViewById(R.id.txt_team_title);
        //   sendBackForwardBean = new SendBackForwardBean();


        activityListadapterNew = new ActivityListMainAdapter_New(this, lsActivityList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lsactivity_list.setLayoutManager(mLayoutManager);
        lsactivity_list.setItemAnimator(new DefaultItemAnimator());
        lsactivity_list.setAdapter(activityListadapterNew);


        // SwipeRefreshLayout
        // mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    /*    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lsActivityList.clear();
                rowEnd = 9;
                rowStart = 0;
                reQuery = "Y";

                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //   new DownloadBirthdayDataJSON().execute();
                            //    new DownloadSubTeamDataJSON().execute();
//                            sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, null, null);

                            new DownloadTeamDataJSON().execute();
                            new DownloadWorkloadActivity().execute(UserMasterId, "");
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            ut.displayToast(ActivityMain.this, msg);
                        }
                    });
                } else {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        ut.displayToast(ActivityMain.this, "No Internet connection");
                    }
                }


                if (isnet()) {
                    showProgresHud();
                    new StartSession(ActivityMain.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            FlagiSRefresh = false;
                            //getActicityData();
                            startService(new Intent(ActivityMain.this, GpsDataLocalSendSevice.class));

                           *//* String url ="http://vritti.ekatm.com/api/LoginAPI/GetEnvis";
                            JSONParser jParser = new JSONParser();
                            JSONObject json = jParser.getJSONFromUrl(url);*//*


                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }

                        @Override
                        public void callfailMethod(String msg) {


                            ut.displayToast(ActivityMain.this, msg);

                        }
                    });
                } else {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    ut.displayToast(ActivityMain.this, "No Internet Connection");
                }

            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);*/




        if (Constants.type == Constants.Type.MilkRun) {
            layout_notification.setVisibility(View.GONE);
            layout_birthday.setVisibility(View.GONE);
        } else if (Constants.type == Constants.Type.Sahara) {
            layout_notification.setVisibility(View.VISIBLE);
            layout_birthday.setVisibility(View.GONE);
        } else {
            layout_notification.setVisibility(View.VISIBLE);
            layout_birthday.setVisibility(View.VISIBLE);
        }

        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub

                String text1 = edt_search.getText().toString().toLowerCase(Locale.getDefault());

                filter(text1);

                //chatUserlistAdapter.filter(username);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

                //String name = edt_search_chatroomname.getText().toString();

               /* if (name.length() > 0) {
                    getchatroomsearch(name);
                }
                else {
                  //  Toast.makeText(getApplicationContext(), "Please enter atleast two charactor to customize your search", Toast.LENGTH_LONG).show();

                }
*/


            }
        });

        try{
            Intent intent = getIntent();
            callFrom = intent.getStringExtra("callFrom");

        }catch (Exception e){
            e.printStackTrace();
            callFrom = "";
        }


    }

    private void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lsActivityList.clear();
        if (charText.length() == 0) {
            lsActivityList.addAll(filterTempList);
        } else {
            for (ActivityBean wp : filterTempList) {
                if (wp.getActivityName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lsActivityList.add(wp);
                }
            }
        }
        activityListadapterNew.notifyDataSetChanged();
    }

    private void ChatMessageCount() {

       /* String query = "SELECT * FROM " + db.TABLE_CHAT_CHATROOM_GROUP_LIST ;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();                           // chatUser.setChatMessage("");

                String Msgcount = cur.getString(cur.getColumnIndex("Count"));
                tv_meeting_cnt.setVisibility(View.VISIBLE);
                String  Message = Msgcount;
                tv_meeting_cnt.setText(Message);

        } else {
          //  tv_meeting_cnt.setText(0 + "");
        }*/

       /* int notiCount = AppCommon.getInstance(this).getNotificationCount();
        String Msgcount = String.valueOf(notiCount);
        if (notiCount != 0) {
            tv_meeting_cnt.setVisibility(View.VISIBLE);
        }
        else
            tv_meeting_cnt.setVisibility(View.GONE);
        tv_meeting_cnt.setText(Msgcount);*/

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

                if (Constants.type == Constants.Type.Sahara) {
                    if (searchClickCount == 0) {
                        edt_search.setVisibility(View.VISIBLE);

                        edt_search.requestFocus();
                        edt_search.setFocusableInTouchMode(true);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edt_search, InputMethodManager.SHOW_FORCED);
                        searchClickCount = 1;

                    } else if (searchClickCount == 1) {
                        edt_search.setVisibility(View.GONE);
                        AppCommon.getInstance(ActivityMain.this).onHideKeyBoard(ActivityMain.this);
                        searchClickCount = 0;

                    }

                } else {
                    Intent intent = new Intent(ActivityMain.this, BirthdayListAcyivity.class);
                    startActivity(intent);
                }


            }
        });


        layout_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                   /* Intent intent5 = new Intent(ActivityMain.this, OpenChatroomActivity.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent5);*/
                    AppCommon.getInstance(ActivityMain.this).setChatPostion(0);
                } else {
                    Toast.makeText(ActivityMain.this, "Chat module is not installed", Toast.LENGTH_SHORT).show();
                }
                /*Intent intent = new Intent(ActivityMain.this, MeetingActivity.class);
                startActivity(intent);*/

            }
        });
        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ActivityMain.this, NotificationActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(ActivityMain.this, ActvityNotificationTypeNameActivity.class);
                startActivity(intent);

            }
        });
        lay_mywork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                updateList_Paging();
                drawer_layout.closeDrawers();
            }
        });

        lay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                if (tv_ticket.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getTicketActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });

   /*     lay_unplanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_unplanned.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getUnplannedActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });*/


        lay_critical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                if (tv_critical.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getCriticalActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_not_acted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                if (tv_notacted.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getNotActedActivity();
                    //getNotActedActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                if (tv_overdue.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getOverdueActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AssignByMe=false;
                Activity_Unapprove=false;
                if (tv_today.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    // getTodayActivity();
                    getTodayActivity_Paging();
                    drawer_layout.closeDrawers();
                }
            }
        });
        lay_assign_by_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Unapprove=false;
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
                Activity_AssignByMe=false;
                if (tv_unapprove.getText().toString().equals("0")) {
                    drawer_layout.closeDrawers();
                } else {
                    getunapproveActicityData();
                    drawer_layout.closeDrawers();

                }
            }
        });


        // edit Profile

        img_userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoreImages();

            }
        });
    }

    private void addMoreImages() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_option_dialog);
        dialog.setTitle(getResources().getString(R.string.app_name));
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
        gallery.setVisibility(View.VISIBLE);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission();
                dialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestGalleryPermission();
                dialog.dismiss();
            }
        });
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    200);
        } else {
            startCameraIntent();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    201);
        } else {
            startGalleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                }
                break;
            case 201:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startGalleryIntent();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMG);
      /*  Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        // galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), RESULT_LOAD_IMG);*/
    }

    private void startCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),
                "attachment.jpg");
        outPutfileUri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_CAPTURE_IMG && resultCode == this.RESULT_OK) {
                String uri = outPutfileUri.toString();
                Log.e("uri-:", uri);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    FileOutputStream out = new FileOutputStream(file);
                    uploadFileBitMap = bitmap;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    outPutfileUri = Uri.parse(url);
                    img_userpic.setImageURI(outPutfileUri);
                    callChangeProfileImageApi(file.getAbsoluteFile().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (data.getData() != null) {
                    outPutfileUri = data.getData();
                    // Get the cursor
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    uploadFileBitMap = bitmap;
                    file = new File(getRealPathFromURI(outPutfileUri));
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    Uri fileUri = Uri.parse(url);
                    img_userpic.setImageURI(fileUri);
                    callChangeProfileImageApi(file.getAbsoluteFile().toString());


                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                if (requestCode == APP_REQUEST_CODE) {
                    Toast.makeText(this, "verification cancel",
                            Toast.LENGTH_LONG).show();
                } else if (requestCode == RESULT_LOAD_IMG) {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void callChangeProfileImageApi(final String outPutfileUri) {
        if (isnet()) {

            new StartSession(ActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new uploadProfileImageMethod().execute(String.valueOf(outPutfileUri));

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
            // callRerofitApi();


        }
    }


    private String getRealPathFromURI(Uri outPutfileUri) {
        Cursor cur = getContentResolver().query(outPutfileUri, null, null, null, null);
        cur.moveToFirst();
        int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cur.getString(idx);

    }

    public void rowClick(final int position) {
        if (Constants.type == Constants.Type.Delivery) {
            startActivity(new Intent(ActivityMain.this, DeliveryDetailPage.class)
                    .putExtra("activityName", lsActivityList.get(position).getActivityName())
                    .putExtra("activityId", lsActivityList.get(position).getActivityId())
                    .putExtra("isApproval", lsActivityList.get(position).getIsApproval())
                    .putExtra("activityName", lsActivityList.get(position).getActivityName()));
        } else if (Constants.type == Constants.Type.MilkRun) {
            startActivity(new Intent(ActivityMain.this, MilkRunLocationListActivity.class)
                    .putExtra("activityName", lsActivityList.get(position).getActivityName())
                    .putExtra("activityId", lsActivityList.get(position).getActivityId())
                    .putExtra("isApproval", lsActivityList.get(position).getIsApproval())
                    .putExtra("activityName", lsActivityList.get(position).getActivityName()));
        } else {
            if (getIntent().hasExtra("callFrom")){
                callFrom = getIntent().getStringExtra("callFrom");
                startActivity(new Intent(ActivityMain.this, DeliveryDetailPage.class)
                        .putExtra("activityName", lsActivityList.get(position).getActivityName())
                        .putExtra("activityId", lsActivityList.get(position).getActivityId())
                        .putExtra("isApproval", lsActivityList.get(position).getIsApproval())
                        .putExtra("activityName", lsActivityList.get(position).getActivityName()));
            }
            else {

                if (getIntent().hasExtra("flag")) {
                    String flag = getIntent().getStringExtra("flag");
                    if (flag.equalsIgnoreCase("1")) {
                        Intent intent = new Intent();
                        intent.putExtra("ID", lsActivityList.get(position).getActivityId());
                        intent.putExtra("Name", lsActivityList.get(position).getActivityName());
                        setResult(3, intent);
                        finish();
                    }
                } else {


                    Sourcetype = lsActivityList.get(position).getSourceType();

                    String ActivityName = lsActivityList.get(position).getActivityName();

                    if (ActivityMain.Activity_AssignByMe && Sourcetype.equalsIgnoreCase("DocAppr")) {

                    } else if (Sourcetype.equalsIgnoreCase("Datasheet")) {
                        String activityId = lsActivityList.get(position).getActivityId();
                        String projectId = lsActivityList.get(position).getProjectID();
                        String assignBy = lsActivityList.get(position).getAssigned_By();
                        String assignById = lsActivityList.get(position).getAssignedById();
                        String sourceId = lsActivityList.get(position).getSourceId();

                        Intent intent = new Intent(ActivityMain.this, DatasheetMainActivity.class);
                        intent.putExtra("ActivityName", ActivityName);
                        intent.putExtra("ActivityId", activityId);
                        intent.putExtra("SourceId", sourceId);
                        intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + Sourcetype);// Source Type
                        intent.putExtra("projmasterId", projectId);
                        intent.putExtra("AssignBy", assignBy);
                        intent.putExtra("AssignById", assignById);
                        //intent.putExtra("flagFromTeam", 1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //finish();
                    } else {

                        String RRF = lsActivityList.get(position).getActivityName();
                        if (RRF.contains("RRF")) {
                            Toast.makeText(ActivityMain.this, "This functionality not available in mobile application", Toast.LENGTH_SHORT).show();
                        } else if (RRF.contains("support")) {
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
                            // finish();

                        } else {

                            if (Designation.equalsIgnoreCase("School")) {

                                SourceId = lsActivityList.get(position).getSourceId();

                                if (isnet()) {
                                    new StartSession(ActivityMain.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            //new DownloadDatasheetGetData().execute();
                                            new DownloadGetformData().execute(String.valueOf(position));
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {
                                            ut.displayToast(getApplicationContext(), msg);

                                        }
                                    });
                                } else {
                                    Toast.makeText(ActivityMain.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }

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
                                // finish();
                            }
                        }

                    }

                }
            }
        }
    }

    public void longPress(int position) {
        String s = lsActivityList.get(position).getActivityName();
        String actid = ActivityMain.AtendanceSheredPreferance
                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        String Starttime = ActivityMain.AtendanceSheredPreferance
                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
        backToposition = position;
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
                if (EnvMasterId.contains("eno") || EnvMasterId.contains("dabur")) {
                    myIntent.putExtra("Flag", "End");
                } else {
                    myIntent.putExtra("Flag", "Start");
                }
                startActivity(myIntent);
            }
        }


    }


/*
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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                ut.displayToast(ActivityMain.this, "Data Send to server");

            } else {

                ut.displayToast(ActivityMain.this, "Failed to Send Data");
            }

        }
    }
*/


    private void getTicketActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE SourceType='Support'";
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
         /*   activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);

        }
    }

    private void getTicketActivity_Paging() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE PriorityIndex='1'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                // bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                //values.put("ROWNo",bean.getrowNo());
                // bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                //  values.put("Assigned_By",bean.getAssigned_By());
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                //  values.put("ActivityCode",bean.getActivityCode());
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                //  values.put("ConsigneeName",bean.getConsigneeName());
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                // values.put("ContMob",bean.getContMob());
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                //  values.put("SourceId",bean.getSourceId());
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                // values.put("ActivityName",bean.getActivityName());
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                // values.put("FormatEndDt",bean.getFormatEndDt());
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                //   bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                // bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                // bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                // bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //  bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                // bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));


                lsActivityList.add(bean);

            } while (cur.moveToNext());
            /*activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);
            activityType = "Ticket";
        } else {
            rowStart =0;
            rowEnd = 9;
            reQuery = "Y";
            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, "Ticket");

                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(ActivityMain.this, "Unable to Parse Json", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ActivityMain.this, "No Record Present", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCriticalActivity_Paging() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE PriorityIndex='1'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                //bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                //values.put("ROWNo",bean.getrowNo());
                // bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                //  values.put("Assigned_By",bean.getAssigned_By());
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                //  values.put("ActivityCode",bean.getActivityCode());
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                //  values.put("ConsigneeName",bean.getConsigneeName());
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                // values.put("ContMob",bean.getContMob());
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                //  values.put("SourceId",bean.getSourceId());
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                // values.put("ActivityName",bean.getActivityName());
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                // values.put("FormatEndDt",bean.getFormatEndDt());
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                //          bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                //        bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //      bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                //  bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                // bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //               bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                //             bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                lsActivityList.add(bean);

            } while (cur.moveToNext());
            /*activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);
            activityType = "Critical";
        } else {
            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, "Critical");
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(ActivityMain.this, "Unable to Parse Json", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ActivityMain.this, "No Record Present", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getNotActedActivity() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE TotalHoursBooked='0'";
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
                //bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                //bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                //  bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                //bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));


                lsActivityList.add(bean);

            } while (cur.moveToNext());
         /*   activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);

        }
    }

    private void getNotActedActivity_Paging() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE TotalHoursBooked='0'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                ActivityBean bean = new ActivityBean();
                bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                //values.put("ROWNo",bean.getrowNo());
                // bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                //  values.put("Assigned_By",bean.getAssigned_By());
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                //  values.put("ActivityCode",bean.getActivityCode());
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                //  values.put("ConsigneeName",bean.getConsigneeName());
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                // values.put("ContMob",bean.getContMob());
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                //  values.put("SourceId",bean.getSourceId());
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                // values.put("ActivityName",bean.getActivityName());
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                // values.put("FormatEndDt",bean.getFormatEndDt());
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                // bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
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
         /*   activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);

        }
    }

    private void getOverdueActivity_Paging() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
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
                    // bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                    //values.put("ROWNo",bean.getrowNo());
                    // bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                    bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                    //  values.put("Assigned_By",bean.getAssigned_By());
                    bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                    //  values.put("ActivityCode",bean.getActivityCode());
                    bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                    //  values.put("ConsigneeName",bean.getConsigneeName());
                    bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                    // values.put("ContMob",bean.getContMob());
                    bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                    //  values.put("SourceId",bean.getSourceId());
                    bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                    // values.put("ActivityName",bean.getActivityName());
                    bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                    // values.put("FormatEndDt",bean.getFormatEndDt());
                    bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                    bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                    bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                    bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                    bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                    bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                    bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                    bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                    //   bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                    //   bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                    //  bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                    // bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                    //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                    //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                    bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                    bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                    bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                    //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                    // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                    bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                    bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                    bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                    //  bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                    bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                    bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                    bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                    //  bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                    // bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                    lsActivityList.add(bean);
                }

            } while (cur.moveToNext());
            /*activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);
            activityType = "Overdue";
        } else {

            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, "Overdue");
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
    }

    private void getTodayActivity_Paging() {
        lsActivityList.clear();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
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
                    //  bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                    //values.put("ROWNo",bean.getrowNo());
                    // bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                    bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                    //  values.put("Assigned_By",bean.getAssigned_By());
                    bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                    //  values.put("ActivityCode",bean.getActivityCode());
                    bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                    //  values.put("ConsigneeName",bean.getConsigneeName());
                    bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                    // values.put("ContMob",bean.getContMob());
                    bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                    //  values.put("SourceId",bean.getSourceId());
                    bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                    // values.put("ActivityName",bean.getActivityName());
                    bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                    // values.put("FormatEndDt",bean.getFormatEndDt());
                    bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                    bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                    bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                    bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                    bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                    bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                    bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                    bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                    //         bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                    //       bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                    //     bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
//                    bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                    // bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                    //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                    bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                    bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                    bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                    //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                    // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                    bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                    bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                    bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                    //bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                    bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                    bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                    bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                    //        bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                    //      bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                    lsActivityList.add(bean);
                }

            } while (cur.moveToNext());
            /*activityListadapter = new ActivityListMainAdapter(ActivityMain.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);*/
            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);
            activityType = "Today";
        } else {


            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, "Today");
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
      /*  if(id == R.id.SupportStaff){
        }*/

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.refresh) {
            Activity_AssignByMe=false;
            Activity_Unapprove=false;
            if (isnet()) {
                showProgresHud();
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        lsActivityList.clear();
                        rowEnd = 9;
                        rowStart = 0;
                        reQuery = "Y";
                        new DownloadTeamDataJSON().execute();
                        new DownloadWorkloadActivity().execute(UserMasterId, "");
                        new DownloadBirthdayDataJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(ActivityMain.this, msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(ActivityMain.this, "No Internet connection");
                //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
            }

            if (isnet()) {
                showProgresHud();
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        FlagiSRefresh = false;

                        startService(new Intent(ActivityMain.this, GpsDataLocalSendSevice.class));


                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(ActivityMain.this, msg);
                        hideProgresHud();

                    }
                });
            } else {
                ut.displayToast(ActivityMain.this, "No Internet Connection");
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
                        ut.displayToast(ActivityMain.this, msg);
                        hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(ActivityMain.this, "No Internet Connection");
            }
            return true;
        }/* else if (id == R.id.empinfo) {

            Intent i = new Intent(ActivityMain.this, ActivityEmployeeInformation.class);
            startActivity(i);
            return true;
        } else if (id == R.id.management) {

            Intent i1 = new Intent(ActivityMain.this, ActivityGPSLocalData.class);
            startActivity(i1);
            return true;
        } */ else if (id == R.id.assign_activity) {

            Intent intent = new Intent(ActivityMain.this, AssignActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.apply_claim) {

            // Intent intent1 = new Intent(ActivityMain.this, ClaimMainActivity.class);
            Intent intent1 = new Intent(ActivityMain.this, ClaimRecordActivity.class);
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
                /*Intent intent5 = new Intent(ActivityMain.this, OpenChatroomActivity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent5);*/
                AppCommon.getInstance(ActivityMain.this).setChatPostion(0);
            } else {
                Toast.makeText(ActivityMain.this, "Chat module is not installed", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == R.id.meeting) {
            Intent intent = new Intent(ActivityMain.this, MeetingActivity.class);
            startActivity(intent);
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
        } else if (id == R.id.asset) {

            Intent advanceintent = new Intent(ActivityMain.this, AssetTransferListActivity.class);//LeaveSummary
            startActivity(advanceintent);
            return true;
        } else if (id == R.id.SupportStaff) {

            Intent intentsupport = new Intent(ActivityMain.this, SupportStaffMaster.class);//LeaveSummary
            startActivity(intentsupport);
            return true;
        } else if (id == R.id.holiday) {
            Intent intentsupport = new Intent(ActivityMain.this, HolidayActivity.class);//LeaveSummary
            startActivity(intentsupport);
            return true;
        }/*else if (id == R.id.credit_sanction) {

            Intent intentsupport = new Intent(ActivityMain.this, CreditApprovalListActivity.class);
            intentsupport.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentsupport);

            return true;

        }*//* else if (id == R.id.docCompleted) {
            if (Designation.equalsIgnoreCase("school")) {

                getActivityDetails(UserMasterId, "PreviousView");

            } else {
                Intent intentdoc = new Intent(ActivityMain.this, DocumentReviewActivity.class);//Doc review
                intentdoc.putExtra("Type", "Previous");
                intentdoc.putExtra("text", "Completed");
                startActivity(intentdoc);

            }
            return true;

        } else if (id == R.id.docUnderApproval) {
            if (Designation.equalsIgnoreCase("school")) {

                getActivityDetails(UserMasterId, "CurrentReview");

            } else {
                Intent intentdoc = new Intent(ActivityMain.this, DocumentReviewActivity.class);//Doc review
                intentdoc.putExtra("Type", "Current");
                intentdoc.putExtra("text", "Under Approval");
                startActivity(intentdoc);

            }
            return true;

        } else if (id == R.id.docSendBack) {
            if (Designation.equalsIgnoreCase("Kendra")) {
                startActivity(new Intent(ActivityMain.this, SendBackDocuments.class).putExtra("methodName", "getBackwardKendraActivities"));
            } else if (Designation.equalsIgnoreCase("BEO")) {
                startActivity(new Intent(ActivityMain.this, SendBackDocuments.class).putExtra("methodName", "getBackBEOActivities"));
            }


        } else if (id == R.id.docSendForward) {
            if (Designation.equalsIgnoreCase("Kendra")) {
                startActivity(new Intent(ActivityMain.this, SendBackDocuments.class).putExtra("methodName", "getForwardKendraActivities"));
            } else if (Designation.equalsIgnoreCase("EXTENSION OFFICER")) {
                startActivity(new Intent(ActivityMain.this, SendBackDocuments.class).putExtra("methodName", "getForwardEOActivities"));
            }


        }*/ else {
            return super.onOptionsItemSelected(item);
        }

        // return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        if(Constants.type == Constants.Type.PM||Constants.type == Constants.Type.NepalPM){
            for(int i=1;i<menu.size();i++){
              /*  if(i == 7 || i == 9 ){
                    menu.getItem(i).setVisible(false);
                }*/
            }
        }else if (Constants.type == Constants.Type.Delivery || Constants.type == Constants.Type.MilkRun) {
            for (int i = 1; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
            }


        } else if (Constants.type == Constants.Type.Sahara) {
            for (int i = 0; i < menu.size(); i++) {

                if (Designation.equalsIgnoreCase("School")) {

                    if (i == 13 || i == 14 || i == 17) {
                        menu.getItem(i).setVisible(true);
                    } else
                        menu.getItem(i).setVisible(false);

                } else if (Designation.equalsIgnoreCase("Kendra")) {

                    if (i == 13 || i == 14 || i == 15 || i == 16 || i == 17) {
                        menu.getItem(i).setVisible(true);
                    } else
                        menu.getItem(i).setVisible(false);

                } else if (Designation.equalsIgnoreCase("Extension Officer")) {

                    if (i == 13 || i == 14 || i == 16 || i == 17) {
                        menu.getItem(i).setVisible(true);
                    } else
                        menu.getItem(i).setVisible(false);

                } else if (Designation.equalsIgnoreCase("BEO")) {
                    if (i == 13 || i == 14 || i == 15 || i == 17) {
                        menu.getItem(i).setVisible(true);
                    } else
                        menu.getItem(i).setVisible(false);
                }
            }
        } else {
            for (int i = 0; i < menu.size(); i++) {
                if (i == 5 || i == 14) {
                    menu.getItem(i).setVisible(false);
                } else
                    menu.getItem(i).setVisible(true);
            }
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // management = menu.findItem(R.id.management);
        assign_activity = menu.findItem(R.id.assign_activity);
        // progressBar = menu.findItem(R.id.miActionProgress);
        return super.onPrepareOptionsMenu(menu);
    }


    private void getWorkspaceData() {
        lsmyworkspace.clear();
        String query = "SELECT * FROM " + db.TABLE_MYWORKSPACE;
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<MyWorkspaceBean> ls = new ArrayList<MyWorkspaceBean>();
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            //tv_workspacecnt.setText("" + cur.getCount());
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
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            // tv_workspacecnt.setText("" + cur.getCount());
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

       /* if (isnet()) {
            new StartSession(ActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadSubTeamDataJSON().execute();


                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(ActivityMain.this, msg);
                    hideProgresHud();

                }
            });
        } else {
            ut.displayToast(ActivityMain.this, "No Internet Connection");
        }*/

        //testsubtemcount

    }

    private void getMyWork() {
        String query = "SELECT * FROM " + db.TABLE_MYWORK;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_critical.setText(cur.getString(cur.getColumnIndex("Critical")));
            String c = cur.getString(cur.getColumnIndex("Critical"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_critical.setVisibility(View.GONE);
            } else {
                if (c.equalsIgnoreCase("0")) {
                    lay_critical.setVisibility(View.GONE);
                } else {
                    lay_critical.setVisibility(View.VISIBLE);
                }
            }
            tv_notacted.setText(cur.getString(cur.getColumnIndex("NotActed")));
            String n = cur.getString(cur.getColumnIndex("NotActed"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_not_acted.setVisibility(View.GONE);
            } else {
                if (n.equalsIgnoreCase("0")) {
                    lay_not_acted.setVisibility(View.GONE);
                } else {
                    lay_not_acted.setVisibility(View.VISIBLE);
                }
            }

            tv_overdue.setText(cur.getString(cur.getColumnIndex("Overdue")));
            String o = cur.getString(cur.getColumnIndex("Overdue"));
            if (o.equalsIgnoreCase("0")) {
                lay_overdue.setVisibility(View.GONE);
            } else {
                lay_overdue.setVisibility(View.VISIBLE);
            }
            tv_ticket.setText(cur.getString(cur.getColumnIndex("Tickets")));
            String a = cur.getString(cur.getColumnIndex("Tickets"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_ticket.setVisibility(View.GONE);
            } else {
                if (a.equalsIgnoreCase("0")) {
                    lay_ticket.setVisibility(View.GONE);
                } else {
                    lay_ticket.setVisibility(View.VISIBLE);
                }
            }
            tv_today.setText(cur.getString(cur.getColumnIndex("Today")));
            String today = cur.getString(cur.getColumnIndex("Today"));
            if (today.equalsIgnoreCase("0")) {
                lay_today.setVisibility(View.GONE);
            } else {
                lay_today.setVisibility(View.VISIBLE);
            }


            tv_workcnt.setText(cur.getString(cur.getColumnIndex("TotalCount")));

            tv_assign_by_me.setText(cur.getString(cur.getColumnIndex("AssByCount")));
            String a1 = cur.getString(cur.getColumnIndex("AssByCount"));
            if (Constants.type == Constants.Type.Sahara) {
                lay_assign_by_me.setVisibility(View.VISIBLE);
            } else {
                if (a1.equalsIgnoreCase("0")) {
                    lay_assign_by_me.setVisibility(View.GONE);
                } else {
                    lay_assign_by_me.setVisibility(View.VISIBLE);
                }
            }
            tv_unapprove.setText(cur.getString(cur.getColumnIndex("UnApproved")));
            String a2 = cur.getString(cur.getColumnIndex("UnApproved"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_unapprove.setVisibility(View.GONE);
            } else {
                if (a2.equalsIgnoreCase("0")) {
                    lay_unapprove.setVisibility(View.GONE);
                } else {
                    lay_unapprove.setVisibility(View.VISIBLE);
                }
            }


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
            String a = cur.getString(cur.getColumnIndex("Tickets"));
            if (a.equalsIgnoreCase("0")) {
                lay_ticket.setVisibility(View.GONE);
            } else {
                lay_ticket.setVisibility(View.VISIBLE);
            }
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
        Activity_Unapprove = false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;

        try {

            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);//  "317250de-e70b-4a1c-9b6d-e41bc3fb824a"

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

        ArrayList<String> schholId = new ArrayList<>();

        try {
            jsonObj = jsoncommonObj.getJSONObject("SchholId");
            jsonObj.put("value1", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");


        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanDataURLJSON().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(ActivityMain.this, msg);
                }
            });
        } else {
            ut.displayToast(ActivityMain.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }
    }

    public void getunapproveActicityData() {
        Activity_Unapprove = true;
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

    /*public void getAssignByMeActicityData() {
        Activity_AssignByMe = true;
        Activity_Unapprove = false;
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
    }*/

    public void getAssignByMeActicityData() {
        Activity_AssignByMe = true;
        Activity_Unapprove = false;
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
            // jsonObj.put("value1", "('13')");
            jsonObj.put("value1", "('12','15')");
            jsonObj.put("Operator", "<>");


        } catch (Exception e) {
            e.printStackTrace();
            //dismissProgressDialog();
        }

        try {
      /*  JSONObject jsonObject = new JSONObject();
        jsonObject.put()*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "ClientId");
            jsonObject.put("IsSet", false);
            jsonObject.put("Operator", "eq");
            jsonObject.put("value1", "");
            jsonObject.put("value2", "");

            jsoncommonObj.put("ClientId", jsonObject);

            jsoncommonObj.put("ReQuery", "Y");
            jsoncommonObj.put("RowEnd", 10);
            jsoncommonObj.put("RowStart", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        //new DownloadCommanDataURLJSON().execute();
        new DownloadCommanDataURLJSON_Paging().execute();
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

    public void updateList() {
        lsActivityList.clear();

        Cursor cur = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER, null);
        int count = cur.getCount();
        Log.i("cnt:", String.valueOf(cur.getCount()));

        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                /* bean.setrowNo(cur.getString(cur.getColumnIndex("ROWNo")));*/
                //  bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
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
                // bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
//                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                /************************/
//               bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
//                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                /*************/
                //   bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));

                /*****************/
                //   bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                // bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                //bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
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
            if (Constants.type == Constants.Type.Sahara) {
                edt_search.setVisibility(View.GONE);
            } else {
                edt_search.setVisibility(View.VISIBLE);
            }

            activityListadapterNew.notifyDataSetChanged();
            filterTempList.clear();
            filterTempList.addAll(lsActivityList);


        } else {
            if (Constants.type == Constants.Type.Sahara) {
                Toast.makeText(getApplicationContext(), "No Activity Present", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityMain.this, ActvityNotificationTypeNameActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }


        }
    }

    public void updateList_Paging() {
        lsActivityList.clear();
        filterTempList.clear();

        Cursor cur = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING, null);
        int count = cur.getCount();
        Log.i("cnt:", String.valueOf(cur.getCount()));

        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                //   bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                //values.put("ROWNo",bean.getrowNo());
                // bean.setIssuedToName(cur.getStrin7yg(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                //  values.put("Assigned_By",bean.getAssigned_By());
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                //  values.put("ActivityCode",bean.getActivityCode());
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                //  values.put("ConsigneeName",bean.getConsigneeName());
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                // values.put("ContMob",bean.getContMob());
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                //  values.put("SourceId",bean.getSourceId());
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                // values.put("ActivityName",bean.getActivityName());
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                // values.put("FormatEndDt",bean.getFormatEndDt());
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                // bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                // bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                //  bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                //  bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //  bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                //    bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                lsActivityList.add(bean);


            } while (cur.moveToNext());
            if (Constants.type == Constants.Type.Sahara) {
                edt_search.setVisibility(View.GONE);
            } else {
                edt_search.setVisibility(View.VISIBLE);
            }

            activityListadapterNew.notifyDataSetChanged();
            //   activityListadapterNew.notifyItemRangeChanged(0, activityListadapterNew.getItemCount());
            filterTempList.addAll(lsActivityList);
            progress_bar.setVisibility(View.GONE);

/*
            if (lsActivityList.size()==1){
                rowStart = 0;
                rowEnd = 9;
                reQuery = "Y";
                //Toast.makeText(ActivityMain.this,"Resume 1",Toast.LENGTH_LONG).show();
                if (ut.isNet(ActivityMain.this)) {
                    new StartSession(ActivityMain.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadWorkloadActivity().execute(UserMasterId, "");
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(ActivityMain.this, "Unable to Parse Json", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ActivityMain.this, "No Record Present", Toast.LENGTH_SHORT).show();
                }
            }
*/


        } else {

            Cursor cursor = sql.rawQuery(
                    "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=? AND AttemptCount<=3",
                    new String[]{WebUrlClass.FlagisUploadedFalse});
            int a = cursor.getCount();
            if (cursor.getCount() == 0) {
                rowStart = 0;
                rowEnd = 9;
                reQuery = "Y";
                if (ut.isNet(context)) {
                    new StartSession(ActivityMain.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadWorkloadActivity().execute(UserMasterId, "");
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(ActivityMain.this, "Unable to Parse Json", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ActivityMain.this, "No Record Present", Toast.LENGTH_SHORT).show();
                }
            }else {

            }




            if (Constants.type == Constants.Type.Sahara) {
                Toast.makeText(getApplicationContext(), "No Activity Present", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityMain.this, ActvityNotificationTypeNameActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        }


        //}
    }


    private void UpdateNotification() {
        String query = "SELECT * FROM " + db.TABLE_NOTIFICATION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            // tv_notification_cnt.setVisibility(View.VISIBLE);
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
            //  tv_notification_cnt.setVisibility(View.VISIBLE);
            tv_notification_cnt.setText(cur.getCount() + "");
        } else {
            tv_notification_cnt.setText(0 + "");
        }


    }

    private void UpdateMeeting() {
        /*String query = "SELECT * FROM " + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_meeting_cnt.setVisibility(View.VISIBLE);
            tv_meeting_cnt.setText(cur.getCount() + "");
        } else {
            tv_meeting_cnt.setText(0 + "");
        }
*/
        UpdateNotification();

    }

    private void UpdateMeetingOnly() {
        /*String query = "SELECT * FROM " + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_meeting_cnt.setVisibility(View.VISIBLE);
            tv_meeting_cnt.setText(cur.getCount() + "");
        } else {
            tv_meeting_cnt.setText(0 + "");
        }
*/

    }


    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }

    class DownloadValidBackDateDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        String ValidBackDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_valid_backdate_entry;
            try {
                res = ut.OpenConnection(url, ActivityMain.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ValidBackDate = response;


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            hideProgresHud();
            if (!(res.equalsIgnoreCase(WebUrlClass.setError))) {
               /* DatabaseHandlers db = new DatabaseHandlers(ActivityMain.this);
                SQLiteDatabase sql = db.getWritableDatabase();*/
                ContentValues cv = new ContentValues();
                cv.put(WebUrlClass.GET_BACKDATE_TIMESHEET_KEY, ValidBackDate);
                sql.update(db.TABLE_SETTING, cv, "LogInKey=?", new String[]{settingKey});

            } else {

            }


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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                    //    ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                ///   ut.displayToast(ActivityMain.this, "Server error...");
            }
            res = "1";
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("Yesterday", current_date);
            editor.putString("Birthday", res);
            offday = res;
            editor.commit();
            UpdateBirthdayListonly();
            new DownloadMeetingDataJSON().execute();

        }

    }
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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                      "        \"NotifText\": \"Dear All,We take this opportunity Ã‚Â in extending a warm welcome to Mr. Rajagopala Bhat designated as Operations Manager at Ã‚Â Karnataka Ã‚Â who has joined us from Ã‚Â 15th Ã‚Â Feb,2017.He may be reached on email id : r.bhat@vritti.co.inHe shall be immediately reporting to Mr. Jeevan PawarWe look forward to your support and cooperation to Mr.Bhat in his current assignments and wish him an exciting u0026 long term career with all of us at VRITTI Family !!!Ã‚Â Thanks u0026 Regards,Ã‚Â HR TeamVritti Solutions LimitedÃ‚Â \",\n" +
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
                      "        \"NotifText\": \"Dear all,As per Tax projection, all are requested to send their investment documents (Xerox copy/ proof) to HR before end of this month.Ã‚Â This is very important for Tax deduction.\",\n" +
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
            hideProgresHud();
            UpdateNotificationOnly();
            getWorkspaceData();

            new DownloadValidBackDateDataJSON().execute();


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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                    ///   ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                // ut.displayToast(ActivityMain.this, "Server error...");
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
        Object res, res_subteam;
        String response, response_subteam;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            //to get count of team
            String url = CompanyURL + WebUrlClass.api_MyTeam;
            try {
                res = ut.OpenConnection(url, ActivityMain.this);
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
            hideProgresHud();
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {

                } else {
                    //   ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
            }

            getMyTeamData();
            /*  Toast.makeText(ActivityMain.this,"Get Data",Toast.LENGTH_SHORT).show();*/

            //  new DownloadBirthdayDataJSON().execute();

        }

    }

    class DownloadSubTeamDataJSON extends AsyncTask<Integer, Void, String> {
        Object res_subteam;
        String response_subteam = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            //to get count of team
            String url_subteam = CompanyURL + WebUrlClass.api_GetMySubteamCount;
            //api/myteamapi/GetMySubTeam?UserMasterId=%2070377995-cbfd-426e-b7c1-10e41cc31854
            //http://a207.ekatm.com/api/myteamapi/GetMySubTeam?UserMasterId=%2070377995-cbfd-426e-b7c1-10e41cc31854

            try {
                res_subteam = ut.OpenConnection(url_subteam, ActivityMain.this);
                response_subteam = res_subteam.toString();
                response_subteam = response_subteam.replaceAll("\"", "");


               /*response_subteam = res_subteam.toString().replaceAll("\\\\\\\\\\\"", "");
                response_subteam = response_subteam.replaceAll("\\\\", "");
                response_subteam = response_subteam.replaceAll("u0026", "&");
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/

                // ContentValues values = new ContentValues();


            } catch (Exception e) {
                e.printStackTrace();
                response_subteam = WebUrlClass.Errormsg;
            }


            return response_subteam;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (response_subteam != null) {
                //set text of subteam count
                tv_subteam_mem_cnt.setText(response_subteam);


            }

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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                    // ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
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
                res = ut.OpenConnection(url, ActivityMain.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");

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
                    //  ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
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
                Intent intent = new Intent(ActivityMain.this, MyTeamMemberActivity.class);
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
                res = ut.OpenPostConnection(url, FinalObj, ActivityMain.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.replaceAll("%", "per.");
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
                    ut.displayToast(ActivityMain.this, "Could not connect to server");
                }
            } else {
                ut.displayToast(ActivityMain.this, "Could Not Connect to server");
            }

            if (Constants.type == Constants.Type.Sahara) {
              /*  Intent intent = new Intent(ActivityMain.this, SaharaDisplayActivity.class);
                intent.putExtra("response", response);
                intent.putExtra("forSchool", "forSchool");
                startActivity(intent);
          */
            } else {

                updateList();
            }
            if (FlagiSRefresh) {
                showProgresHud();
                new DownloadMyWorkspaceDataJSON().execute();
            }
        }

    }

    class DownloadCommanDataURLJSON_Paging extends AsyncTask<Integer, Void, String> {
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
                res = ut.OpenPostConnection(url, FinalObj, ActivityMain.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.replaceAll("%", "per.");
                response = response.substring(1, response.length() - 1);


                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING_ASSIGN, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING_ASSIGN, null);
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
                    long a = sql.insert(db.TABLE_ACTIVITYMASTER_PAGING_ASSIGN, null, values);
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
                    ut.displayToast(ActivityMain.this, "Could not connect to server");
                }
            } else {
                ut.displayToast(ActivityMain.this, "Could Not Connect to server");
            }

            if (Constants.type == Constants.Type.Sahara /*|| Constants.type == Constants.Type.ZP*/) {
              /*  Intent intent = new Intent(ActivityMain.this, SaharaDisplayActivity.class);
                intent.putExtra("response", response);
                intent.putExtra("forSchool", "forSchool");
                startActivity(intent);
          */
            } else {

                AssignByMe_Paging();
            }
            if (FlagiSRefresh) {
                showProgresHud();
                new DownloadMyWorkspaceDataJSON().execute();
            }
        }

    }

    public void AssignByMe_Paging() {
        lsActivityList.clear();
        filterTempList.clear();

        Cursor cur = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING_ASSIGN, null);
        int count = cur.getCount();
        Log.i("cnt:", String.valueOf(cur.getCount()));

        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                //   bean.setRowNo(cur.getString(cur.getColumnIndex("ROWNo")));
                //values.put("ROWNo",bean.getrowNo());
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                //  values.put("Assigned_By",bean.getAssigned_By());
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                //  values.put("ActivityCode",bean.getActivityCode());
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                //  values.put("ConsigneeName",bean.getConsigneeName());
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                // values.put("ContMob",bean.getContMob());
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                //  values.put("SourceId",bean.getSourceId());
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                // values.put("ActivityName",bean.getActivityName());
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                // values.put("FormatEndDt",bean.getFormatEndDt());
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                // bean.setEndDateAct(cur.getString(cur.getColumnIndex("EndDateAct")));
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
                // bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                //  bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                //bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
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
                //bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                //bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                // bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                //  bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //  bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                //    bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                lsActivityList.add(bean);

            } while (cur.moveToNext());
            if (Constants.type == Constants.Type.Sahara /*|| Constants.type == Constants.Type.ZP*/) {
                edt_search.setVisibility(View.GONE);
            } else {
                edt_search.setVisibility(View.VISIBLE);
            }


            activityListadapterNew = new ActivityListMainAdapter_New(ActivityMain.this, lsActivityList,"fromDoc");
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            lsactivity_list.setLayoutManager(mLayoutManager);
            lsactivity_list.setItemAnimator(new DefaultItemAnimator());
            lsactivity_list.setAdapter(activityListadapterNew);
            filterTempList.addAll(lsActivityList);
            progress_bar.setVisibility(View.GONE);


        } else {


            if (Constants.type == Constants.Type.Sahara /*|| Constants.type == Constants.Type.ZP*/) {
                Toast.makeText(getApplicationContext(), "No Activity Present", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityMain.this, ActvityNotificationTypeNameActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

        }


        //}
    }

    class DownloadWorkloadActivity extends AsyncTask<String, Void, String> {
        Object res="";
        String response="";
        JSONArray jResults;
        String activity_Type = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);

        }


        @Override
        protected String doInBackground(String... params) {
            String userMasterId = params[0];
            activityType = params[1];
           /* rowStart = Integer.parseInt(params[2]);
            rowEnd = Integer.parseInt(params[3]);*/




            if (rowStart == 0) {
                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, null, null);
            } else {

            }


            String url = CompanyURL + WebUrlClass.api_PostloadWorkData_Indexing + "?UserMstrId=" + userMasterId +
                    "&RowStart=" + rowStart + "&RowEnd=" + rowEnd + "&ReQuery=" + reQuery + "&Flag=" + activityType;

            Log.d("API",url);
            try {
                res = ut.OpenConnection(url, ActivityMain.this);
                response = res.toString();
                response = response.substring(1, response.length() - 1);

                response="{\"Activity\":\""+response+"\n" +"\"}";

                  /*String f="{\n" +
                          "\t\" Activity \": \"[{\\\"ActivityName\\\":\\\"Te\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"7EE83A41-6708-4DD8-B57A-60C768339F27\\\",\\\"StartDate\\\":\\\"\\\\/Date(1612290600000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1612290600000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1612290600000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1612290600000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1612290600000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"03-Feb-21 \\\",\\\"FormatEndDt\\\":\\\"03-Feb-21 \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1612356251590)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1612356251590)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":6},{\\\"ActivityName\\\":\\\"Test  symbol \\\\u0027\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"209BF416-8A94-4DDD-B340-777E543B6038\\\",\\\"StartDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"Today \\\",\\\"FormatEndDt\\\":\\\"Today \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1616069276473)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1616069276473)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":null},{\\\"ActivityName\\\":\\\"Test of data $%#\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"E0E8300F-7972-43F4-9B66-3F74510DC639\\\",\\\"StartDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"Today \\\",\\\"FormatEndDt\\\":\\\"Today \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1616060483423)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1616060483423)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":null},{\\\"ActivityName\\\":\\\"Testing \\\\u0026 @ json\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"FCFE3DE6-2D15-437D-AFBB-B6B48C3A6D26\\\",\\\"StartDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"Today \\\",\\\"FormatEndDt\\\":\\\"Today \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1616059949507)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1616059949507)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":null},{\\\"ActivityName\\\":\\\"Testing data for \\\\u0027\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"FABEB541-ECC3-4F42-B905-BE72C0C77912\\\",\\\"StartDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"Today \\\",\\\"FormatEndDt\\\":\\\"Today \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1616069243690)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1616069243690)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":null},{\\\"ActivityName\\\":\\\"Testing notification\\\",\\\"AssignedById\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"Assigned_By\\\":\\\"Chetana Salunkhe\\\",\\\"ActivityId\\\":\\\"5F1FB7B9-3F4A-4E27-9A6F-4B206F87CEBC\\\",\\\"StartDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"EndDateTime\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedCompleteDate\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ExpectedComplete_Date\\\":\\\"\\\\/Date(1616005800000)\\\\/\\\",\\\"ModifiedBy\\\":\\\"300212\\\",\\\"Modified_By\\\":null,\\\"FormatStDt\\\":\\\"Today \\\",\\\"FormatEndDt\\\":\\\"Today \\\",\\\"Status\\\":\\\"ASSIGNED\\\",\\\"ProjectId\\\":\\\"9f148bec-498e-495d-9b44-7d48030b31ce\\\",\\\"PAllowUsrTimeSlotHrs\\\":\\\"N\\\",\\\"Cd\\\":13,\\\"UnitId\\\":\\\"5299BE96-210B-4331-AAD9-3EE90C1B3DEC\\\",\\\"PKModuleMastId\\\":\\\"7548C8C2-E568-49E9-8D9D-8A8415AE7A7E\\\",\\\"PriorityName\\\":\\\"Normal\\\",\\\"Colour\\\":\\\"#ffffff\\\",\\\"PriorityIndex\\\":3,\\\"TotalHoursBooked\\\":0.00,\\\"AddedDt\\\":\\\"\\\\/Date(1616058297817)\\\\/\\\",\\\"UserMasterId\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"AssignedById1\\\":\\\"dc99ff67-93ef-4528-9a34-1c47ab27d60f\\\",\\\"IsDeleted\\\":\\\"N\\\",\\\"IsApproved\\\":\\\" \\\",\\\"IsChargable\\\":false,\\\"ActivityTypeId\\\":\\\"VSLPune-100\\\",\\\"IsApproval\\\":false,\\\"HoursRequired\\\":4.00,\\\"AttachmentName\\\":null,\\\"ModifiedDt\\\":\\\"\\\\/Date(1616058297817)\\\\/\\\",\\\"SourceType\\\":\\\"\\\",\\\"SourceId\\\":\\\"\\\",\\\"UnitName\\\":\\\"admin\\\",\\\"UnitDesc\\\":\\\"Admin\\\",\\\"ModuleName\\\":\\\"Admin\\\",\\\"Remarks\\\":\\\"\\\",\\\"ProjectCode\\\":\\\"Agile19\\\",\\\"ProjectName\\\":\\\"Agile Development-2019\\\",\\\"IsDelayedActivityAllowed\\\":\\\"Y\\\",\\\"UserName\\\":\\\"Chetana Salunkhe\\\",\\\"DeptDesc\\\":\\\"Development-Enterprise Product\\\",\\\"DeptMasterId\\\":\\\"VSLPune-75\\\",\\\"CompletionIntimate\\\":\\\"N\\\",\\\"ActivityCode\\\":\\\"\\\",\\\"ModifiedBy1\\\":\\\"300212\\\",\\\"ReassignedBy\\\":null,\\\"ReassignedDt\\\":null,\\\"ActualCompletionDate\\\":null,\\\"WarrantyCode\\\":\\\"\\\",\\\"TicketCategory\\\":\\\"\\\",\\\"IsEndTime\\\":\\\"N\\\",\\\"IsCompActPresent\\\":\\\"Y\\\",\\\"SODetailId\\\":\\\"\\\",\\\"CompletionActId\\\":null,\\\"TktCustReportedBy\\\":null,\\\"TktCustApprovedBy\\\":\\\"\\\",\\\"IsSubActivity\\\":\\\"N\\\",\\\"ParentActId\\\":\\\"\\\",\\\"ConsigneeName\\\":\\\"\\\",\\\"ContMob\\\":\\\"\\\",\\\"ActivityTypeName\\\":\\\"Activity Review\\\",\\\"AllowDate\\\":\\\"No\\\",\\\"SubActCount\\\":0,\\\"SubActStaus\\\":null,\\\"FYCode\\\":\\\"\\\",\\\"ChargedAmount\\\":0.00,\\\"ApprovedAmount\\\":0.00,\\\"TotalCount\\\":null}]\"\n" +
                          "}";
                  */

                //    response = res.toString();
                /*response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.replaceAll("%", "per.");
                response = response.substring(1, response.length() - 1);
               */
                Log.d("API1",response);

                ContentValues values = new ContentValues();
                JSONObject  obj = new JSONObject(response);

                String Msgcontent=obj.getString("Activity");
                jResults = new JSONArray(Msgcontent);

                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_PAGING, null);
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

                    long a = sql.insert(db.TABLE_ACTIVITYMASTER_PAGING, null, values);
                    Log.i("Activity List", "" + a);
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

            progress_bar.setVisibility(View.GONE);
            if (response.equals("[]")) {

                progress_bar.setVisibility(View.GONE);
            } else {
                if (res != null) {
                    if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                        if (res.equals("[]")) {
                            // activityListadapterNew.hideLayout();
                            if (!activityType.equals("")) {
                                drawer_layout.closeDrawers();
                            }


                        } else {
                            if (activityType.equalsIgnoreCase("critical")) {
                                getCriticalActivity_Paging();
                            }else if(activity_Type.equalsIgnoreCase("Ticket")) {
                                getTicketActivity_Paging();
                            }
                            else {
                                // test(jResults);
                                updateList_Paging();
                            }

                        }
                        activityListadapterNew.notifyDataSetChanged();

                    } else if (res.equals("[]")) {
                        // activityListadapterNew.hideLayout();

                    } else {
                        ut.displayToast(ActivityMain.this, "Could not connect to server");
                    }
                } else {
                    ut.displayToast(ActivityMain.this, "Could Not Connect to server");
                }
            }

            /*if (FlagiSRefresh) {
                showProgresHud();
                new DownloadMyWorkspaceDataJSON().execute();
            }*/

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
                res = ut.OpenConnection(url, ActivityMain.this);
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
                res = ut.OpenConnection(url, ActivityMain.this);
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
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString(WebUrlClass.USERINFO_TIMESHEET_ISTIMESlOT, integer);
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

   /* @Override
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
    }*/

  /*  @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(ActivityMain.this, "In Save Instance ", Toast.LENGTH_SHORT).show();
       // state = lsactivity_list.onSaveInstanceState();


        }*/


    @Override
    public void onResume() {
        super.onResume();

        //updateList();
        //  Toast.makeText(ActivityMain.this,"Resume",Toast.LENGTH_LONG).show();

        progress_bar.setVisibility(View.GONE);
        if (cf.getActivityMasterCount_Paging() > 0) {
            updateList_Paging();
            if (startpos != -1) {
                updateList_Paging();
            } else {
                startpos = 1;

            }
            getWorkspaceData();

        }

        //activityListadapterNew.notifyDataSetChanged();

        if (backToposition != -1)
            activityListadapterNew.notifyItemChanged(backToposition);

        isInFront = true;
        ShortcutBadger.with(ActivityMain.this).remove();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("count");
        editor.commit();
        ChatMessageCount();
    }

       /* if (cf.getActivityMasterCount() > 0) {
            updateList();
            getWorkspaceData();
        }*/

    //notification call

    BroadcastReceiver mMyBroadcastReceiver = new BroadcastReceiver() {
        @Override

        public void onReceive(Context context, Intent intent) {

           /* if (cf.getActivityMasterCount() > 0) {
                updateList();
                getWorkspaceData();
            }
*/
            if (cf.getActivityMasterCount_Paging() > 0) {

                updateList_Paging();
                getWorkspaceData();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityMain.this.unregisterReceiver(mMyBroadcastReceiver);
    }

    public class downloadworkspacecnt extends AsyncTask<Integer, Void, String> {
        Object res_subteam;
        String response_subteam;
        String[] resp;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            //to get count of team
            String url_subteam = CompanyURL + WebUrlClass.api_myworkspacecnt;

            try {
                res_subteam = ut.OpenConnection(url_subteam, ActivityMain.this);
                response_subteam = res_subteam.toString().replaceAll("\\\\", "");
                response_subteam = response_subteam.replaceAll("u0026", "&");
                response_subteam = response_subteam.substring(1, response_subteam.length() - 1);
                String resp1 = response_subteam;


            } catch (Exception e) {
                e.printStackTrace();
                response_subteam = WebUrlClass.Errormsg;
            }


            return response_subteam;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (res != null) {
                try {
                    JSONArray jsonArray = new JSONArray(res);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String count = jsonObject.getString("OpenActivities");
                        tv_workspacecnt.setText(count);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tv_workspacecnt.setText(0);

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

            //showProgresHud();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetUserProfile;

            try {
                res = ut.OpenConnection(url, ActivityMain.this);
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
                        ImagePath1 = ImagePath1.replace(" ", "%20");
                        //  img_userpic.setImageURI(ImagePath1);
                        img_userpic.setController(AppCommon.getDraweeController(img_userpic, ImagePath1, 300));
                        AppCommon.getInstance(context).setImageUrl(ImagePath1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class uploadProfileImageMethod extends AsyncTask<String, Void, String> {
        String params = "";
        String serverResponseMessage = "";

        @Override
        protected String doInBackground(String... strings) {
            params = strings[0];
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // uploadFileBitMap.compress(Bitmap.CompressFormat.PNG, 0, baos);
            try {

                //File sourceFile = new File(params);
                File sourceFile = FileUtils.getFile(context, Uri.fromFile(new File(params)));
                //String upLoadServerUri = CompanyURL + "/api/TimesheetAPI/UploadFiles_Android";
                String upLoadServerUri = CompanyURL + "/api/TimesheetAPI/PostUserProfileUpload";
                //String upLoadServerUri = CompanyURL + "/api/ChatRoomAPI/UploadFilesProfile";
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                String response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri, sourceFile));
                //conn.setRequestProperty("uploaded_file", params);
                if (response != null)
                    Log.i("uploadImageStatus :", response);

                if (serverResponseMessage.equals("OK")) {

                    // Log.i("resuslt :", String.valueOf(serverResponseCode));
                } else {

                    if (serverResponseMessage.contains("Error")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                // progress.setVisibility(View.GONE);
                                Toast.makeText(ActivityMain.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class DownloadGetformData extends AsyncTask<String, Void, String> {
        String res;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            pos = Integer.parseInt(params[0]);

            try {

                String url = CompanyURL + WebUrlClass.api_get_formData + "?sourceId=" + URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override

        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res != null) {
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    String AllowDatasheetEntryOn = jsonObject.getString("AllowDatasheetEntryOn");
                    if (AllowDatasheetEntryOn.equalsIgnoreCase("P")) {

                        Toast.makeText(ActivityMain.this, "This datasheet can be filled only from portal", Toast.LENGTH_SHORT).show();

                    } else {
                        String assign = String.valueOf(ActivityMain.Activity_AssignByMe);
                        String Unapprove = String.valueOf(ActivityMain.Activity_Unapprove);

                        ActivityBean bean = lsActivityList.get(pos);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Server error please try after sometime", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }
    }

    public void loadNextActivity(String isRequery) {



        rowStart = lsActivityList.size();//9+1=10
        rowEnd = lsActivityList.size() + 9;//10+9
        reQuery = isRequery;

        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=? AND AttemptCount<=3",
                new String[]{WebUrlClass.FlagisUploadedFalse});
        int a = cursor.getCount();


      //  if (cursor.getCount() == 0) {

            if (ut.isNet(context)) {
                new StartSession(ActivityMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWorkloadActivity().execute(UserMasterId, activityType);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(ActivityMain.this, "Unable to Parse Json", Toast.LENGTH_SHORT).show();
                    }
                });
            }
       // }
    }


/*
    public void getActivityDetails(String schoolId, String type) {
        //  showProgressDialog();

        // JSONArray jsArray = new JSONArray(schoolId);

        commonObjectProperties = new commonObjectProperties();

        JSONObject jsoncommonObj = commonObjectProperties.WorkDataObj();
        JSONObject jsonObj;
        commonObgj = new CommonObject();


        commonObgj.setActivityCode(new CommonSubObject("activityCode"));
        commonObgj.getActivityCode().setOperator("eq");
        commonObgj.setActDesc(new CommonSubObject("actDesc"));
        commonObgj.getActDesc().setOperator("eq");
        commonObgj.setIssuedTo(new CommonSubObject("issuedTo"));
        commonObgj.getIssuedTo().setOperator("eq");
        commonObgj.setAssignBy(new CommonSubObject("assignBy"));
        commonObgj.getAssignBy().setOperator("eq");
        commonObgj.setProjId(new CommonSubObject("ProjId"));
        commonObgj.getProjId().setOperator("eq");
        commonObgj.setMainGrpId(new CommonSubObject("MainGrpId"));
        commonObgj.getMainGrpId().setOperator("eq");
        commonObgj.setUnitId(new CommonSubObject("UnitId"));
        commonObgj.getUnitId().setOperator("eq");

        commonObgj.setFromDt(new CommonSubObject("FromDt"));
        commonObgj.getFromDt().setOperator("eq");
        commonObgj.getFromDt().setSet(false);
        commonObgj.getFromDt().setValue1("");

        commonObgj.setToDt(new CommonSubObject("ToDt"));
        //commonObgj.getToDt().setName("ToDt");
        commonObgj.getToDt().setOperator("eq");
        commonObgj.getToDt().setValue1("");
        commonObgj.getToDt().setSet(false);


        commonObgj.setExpCompltnDt(new CommonSubObject("ExpCompltnDt"));
        commonObgj.getExpCompltnDt().setOperator("eq");
        commonObgj.setSourceType(new CommonSubObject("SourceType"));
        commonObgj.getSourceType().setOperator("eq");
        commonObgj.setSourceId(new CommonSubObject("SourceId"));
        commonObgj.getSourceId().setOperator("eq");

        commonObgj.setStatus(new CommonSubObject("Status"));
        commonObgj.getStatus().setName("Status");
        commonObgj.getStatus().setSet(true);
        commonObgj.getStatus().setOperator("<>");
        if (type.equalsIgnoreCase("PreviousView")) {
            commonObgj.getStatus().setValue1("('15','13')");
        } else if (type.equalsIgnoreCase("CurrentReview")) {
            commonObgj.getStatus().setValue1("('15','12')");
        }

        commonObgj.setPriorityId(new CommonSubObject("PriorityId"));
        commonObgj.getPriorityId().setOperator("eq");
        commonObgj.setHoursRequired(new CommonSubObject("HoursRequired"));
        commonObgj.getHoursRequired().setOperator("eq");
        commonObgj.setComFromDt(new CommonSubObject("comFromDt"));
        commonObgj.getComFromDt().setOperator("eq");
        commonObgj.setComToDt(new CommonSubObject("comToDt"));
        commonObgj.getComToDt().setOperator("eq");
        commonObgj.setPerform(new CommonSubObject("perform"));
        commonObgj.getPerform().setOperator("eq");
        commonObgj.setIsSystemGenerated(new CommonSubObject("IsSystemGenerated"));
        commonObgj.getIsSystemGenerated().setOperator("eq");
        commonObgj.setIsUnplanned(new CommonSubObject("IsUnplanned"));
        commonObgj.getIsUnplanned().setOperator("eq");
        commonObgj.setAwait(new CommonSubObject("Await"));
        commonObgj.getAwait().setOperator("eq");
        commonObgj.setPriorityIndex(new CommonSubObject("PriorityIndex"));
        commonObgj.getPriorityIndex().setOperator("eq");


        commonObgj.setParentActId(new CommonSubObject("ParentActId"));
        commonObgj.getParentActId().setName("ParentActId");
        commonObgj.getParentActId().setSet(true);
        commonObgj.getParentActId().setValue1("");//"["wsc
        commonObgj.getParentActId().setOperator("eq");

        commonObgj.setModifiedDt(new CommonSubObject("ModifiedDt"));
        commonObgj.getModifiedDt().setOperator("eq");

        commonObgj.setYear(new CommonSubObject("year"));
        commonObgj.getYear().setSet(false);
        commonObgj.getYear().setValue1("");
        commonObgj.getYear().setOperator("eq");


        commonObgj.setFormId(new CommonSubObject("FormId"));
        commonObgj.getFormId().setSet(false);
        commonObgj.getFormId().setValue1("");
        commonObgj.getFormId().setOperator("eq");


        commonObgj.setDocumentType(new CommonSubObject("DocumentType"));
        commonObgj.getDocumentType().setSet(true);
        commonObgj.getDocumentType().setValue1(type);
        commonObgj.getDocumentType().setOperator("eq");


        commonObgj.setSchholId(new CommonSubObject("SchholId"));
        commonObgj.getSchholId().setValue1(schoolId);
        commonObgj.getSchholId().setSet(true);
        commonObgj.getSchholId().setOperator("eq");


        commonObgj.setDatasheetStatus(new CommonSubObject("DatasheetStatus"));
        commonObgj.getDatasheetStatus().setSet(true);
        String es = "=";
        if (type.equalsIgnoreCase("PreviousView")) {
            commonObgj.getDatasheetStatus().setValue1("30");
            commonObgj.getDatasheetStatus().setOperator(es);
        } else if (type.equalsIgnoreCase("CurrentReview")) {
            commonObgj.getDatasheetStatus().setValue1("('20','21','11')");
            commonObgj.getDatasheetStatus().setOperator("bet");
        }


        try {
            FinalObj = new JSONObject(new Gson().toJson(commonObgj)).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ut.isNet(context)) {
            new StartSession(ActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanDataURLJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(ActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ActivityMain.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }
*/




    private String getDateAdded(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }
}


