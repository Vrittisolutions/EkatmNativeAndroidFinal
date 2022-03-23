package com.vritti.ekatm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scichart.charting.visuals.SciPieChartSurface;
import com.scichart.charting.visuals.legend.SciChartLegend;
import com.scichart.extensions.builders.SciChartBuilder;
import com.vritti.AlfaLavaModule.activity.AlfaHomePage;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.vcrm7.CRMHomeActivity;
import com.vritti.crm.vcrm7.CRM_CallLogList;
import com.vritti.crm.vcrm7.KnowMoreActivity;
import com.vritti.crm.vcrm7.TargetAchievmentReport;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.adapter.CustomAdapter;
import com.vritti.ekatm.bean.BeanLogInsetting;
import com.vritti.ekatm.bean.ModuleName;
import com.vritti.ekatm.receiver.ConnectivityReceiver;
import com.vritti.ekatm.receiver.MyAlarmReceiver;
import com.vritti.ekatm.services.DownloadJobService;
import com.vritti.ekatm.services.PaidLocationFusedLocationTracker1;
import com.vritti.expensemanagement.ExpenseSelectionActivity;
import com.vritti.inventory.activity.SelectModuleActivity;
import com.vritti.inventory.physicalInventory.activity.BluetoothConnectivityActivity;
import com.vritti.sales.activity.Sales_HomeSActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.BirthdayBean;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.ActvityNotificationTypeNameActivity;
import com.vritti.vwb.vworkbench.AttendanceDisplayActivity;
import com.vritti.vwb.vworkbench.BirthdayListAcyivity;
import com.vritti.ekatm.receiver.MyBroadcastReceiver;
import com.vritti.vwb.vworkbench.TicketRegisterActivity;
import com.vritti.vwb.vworkbench.WelcomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.view.PieChartView;

public class ActivityModuleSelection extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener /*LoaderManager.LoaderCallbacks<Cursor>*/ {

    private DrawerLayout mDrawerLayout;
    private DrawerLayout mDrawerLayout1;

    private ListView mDrawerList;
    //CardView card_vwb, card_crm, card_pm, card_service;
    ImageView
            card_vwb, card_crm, card_pm, card_service, card_pm_crm,
            card_inventory, chat_module ,card_sales, card_print,img_expense,img_wms,img_call;

    String[] titles = {"Nigeria", "Ghana", "Senegal", "Togo"};
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar topToolBar;
    ArrayList<BeanLogInsetting> beanLogInsettingArrayList;
    private DatabaseHandlers db;
    Utility ut;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    static String settingKey = "";
    private TextView txtUserLoginID, txtMobile, txtcompcode, txt_vwb, txt_crm, txt_pm, txt_service;
    private ImageView img_userprofile, img_adduser;
    String IsCRMUser = "";
    public static FirebaseJobDispatcher dispatcher;
    public static Job myJob = null;
    private GoogleApiClient googleApiClient = null;
    private static int REQUEST_CODE = 12;
    private int IGNORE_BATTERY_OPTIMIZATION_REQUEST = 1002;
    private BroadcastReceiver mNetworkReceiver;
    LinearLayout len_pm, len_vwb;
    TextView chatCount;
    String IsChatApplicable;
    Context context;
    SQLiteDatabase sql;
    RelativeLayout conversation;
    int PLACE_PICKER_REQUEST = 1;
    ArrayList <ModuleName>moduleNameArrayList=new ArrayList<>();
    ProgressDialog progressDialog;
    LinearLayout len_vwb_crm,len_service,len_chat,len_wms,len_pi;
    private PieChartView pieChartView;
    RecyclerView recyclerView;
    TextView tv_activity_status, tv_birthday_cnt, tv_username, tv_meeting_cnt, tv_notification_cnt,tv_call_cnt;
    private ImageView img_notification, img_birthday,img_att;
    private String current_date, yesterday_date = "06-06-2018", offday;
    public static SharedPreferences userpreferences;
    CommonFunction cf;
    LinearLayout layout_birthday, layout_meeting, layout_notification;
    Loader<Cursor> cursorLoader;
    RelativeLayout rel_call;
    HorizontalScrollView horizontal_view;
    ImageView img_back;
    TextView txt_title;

    SciPieChartSurface pieChartSurface;
    SciChartLegend legend;
    private SciChartBuilder sciChartBuilder;


    private String[] xData = {"Present", "Absent" , "Leave" , "Not on Roll",};
    PieChart pieChart,pichart_claim;
    LinearLayout card,len_call_log,len;
    ImageView img_playstore;
    TextView txt_calllog,txt_exp;

    TextView btn_unclaimed,btn_unpaid,btn_unapp,btn_advance;
    TextView btn_achived,btn_target,btn_collected,btn_outstanding;
    TextView txt_avg;
    CardView card_target;
    String EndMonth="",EndDt="";
    private MyBroadcastReceiver myBroadcastReceiver;
    private NotificationChannel channel;
    private String incoming="Incoming",outgoing="Outgoing",opportunitycollection="Opportunity/collection",opportunity="Opportunity";
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_selection);
        // isServiceRunning();


        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }



        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        topToolBar.setTitle("");

        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        card = findViewById(R.id.card);
        txt_calllog = findViewById(R.id.txt_calllog);
        txt_exp = findViewById(R.id.txt_exp);
        txt_title.setText(R.string.app_name_toolbar_Ekatm);


        pieChartSurface=findViewById(R.id.pieChart);
        legend=findViewById(R.id.pieChartLegend);
        btn_unapp=findViewById(R.id.btn_unapp);
        btn_advance=findViewById(R.id.btn_advance);
        btn_unclaimed=findViewById(R.id.btn_unclaimed);
        btn_unpaid=findViewById(R.id.btn_unpaid);

        btn_achived=findViewById(R.id.btn_achived);
        btn_collected=findViewById(R.id.btn_collected);
        btn_outstanding=findViewById(R.id.btn_outstanding);
        btn_target=findViewById(R.id.btn_target);
        txt_avg=findViewById(R.id.txt_avg);
        card_target=findViewById(R.id.card_target);

        if(Constants.type == Constants.Type.MilkRun)
            img_back.setImageDrawable(getResources().getDrawable(R.mipmap.vsm_image));
        if(Constants.type == Constants.Type.Sahara)
            img_back.setImageDrawable(getResources().getDrawable(R.mipmap.ic_toolbar_logo_vwb));
       // topToolBar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        if(Constants.type == Constants.Type.PM)
            img_back.setImageDrawable(getResources().getDrawable(R.mipmap.ic_simplify));

        else
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.app_logo_1));
      //  topToolBar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
      //  topToolBar.setTitleTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

         try {
             myBroadcastReceiver = new MyBroadcastReceiver();
             IntentFilter intentFilter = new IntentFilter("com.crm.calllogs");
             if (intentFilter != null) {
                 registerReceiver(myBroadcastReceiver, intentFilter);
             }
         }catch (Exception e){
             e.printStackTrace();
         }

     /*   MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();
        registerReceiver(mReceiver,
                new IntentFilter("com.crm.calllogs"));
*/


        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pichart_claim = (PieChart) findViewById(R.id.pichart_claim);


        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        EndMonth = sdf.format(date1);

        long Enddate = System.currentTimeMillis();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        EndDt = sdf1.format(Enddate);



        progressDialog=new ProgressDialog(ActivityModuleSelection.this);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        cf = new CommonFunction(ActivityModuleSelection.this);
        if (getIntent() != null && getIntent().getData() != null) {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            String param = data.getQuery();
            Log.d("DeepLink","Schema : " + scheme);
            Log.d("DeepLink","Host : " + host);
            Log.d("DeepLink","param : " + host);

            if (host.contains("c207.ekatm.com")){
                Intent intent = new Intent(this,ActivityMain.class);
                intent.putExtra("a",data.getQueryParameter("a"));  // URL query values as string, you need to parse string to long.
                startActivity(intent);
            }else{
                // ... other logic
            }
        }

        context = ActivityModuleSelection.this;
        ut = new Utility();
        settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
          LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
      /*  txt_vwb = (TextView) findViewById(R.id.txt_vwb);
        txt_crm = (TextView) findViewById(R.id.txt_crm);
        txt_pm = (TextView) findViewById(R.id.txt_pm);
        txt_service = (TextView) findViewById(R.id.txt_service);*/

        card_vwb = (ImageView) findViewById(R.id.card_vwb);
        chat_module = (ImageView) findViewById(R.id.chat_module);
        card_crm = (ImageView) findViewById(R.id.card_crm);
        card_pm = (ImageView) findViewById(R.id.card_pm);
        card_service = (ImageView) findViewById(R.id.card_service);
        len_pm = (LinearLayout) findViewById(R.id.len_pm);
        len_vwb = (LinearLayout) findViewById(R.id.len_vwb);
        card_pm_crm = (ImageView) findViewById(R.id.card_pm_crm);
        card_inventory = (ImageView) findViewById(R.id.card_inventory);
        card_sales = (ImageView)findViewById(R.id.card_sales);
        card_print = findViewById(R.id.card_print);
        chatCount = findViewById(R.id.chatCount);
        conversation = findViewById(R.id.conversation);
        img_expense = (ImageView) findViewById(R.id.img_expense);
        img_wms = (ImageView) findViewById(R.id.img_wms);
        img_call = (ImageView) findViewById(R.id.img_call);

       BackgroundLocationTracking backgroundLocationTracking=new BackgroundLocationTracking(ActivityModuleSelection.this);


        len_wms=findViewById(R.id.len_wms);
        len_vwb_crm=findViewById(R.id.len_vwb_crm);
        len_service=findViewById(R.id.len_service);
      //  len_chat=findViewById(R.id.len_chat);
        len_pi=findViewById(R.id.len_pi);

        pieChartView = findViewById(R.id.chart);

        tv_birthday_cnt = (TextView) findViewById(R.id.tv_birthday_cnt);
        tv_meeting_cnt = (TextView) findViewById(R.id.tv_meeting_cnt);
        tv_notification_cnt = (TextView) findViewById(R.id.tv_notification_cnt);
        img_notification = (ImageView) findViewById(R.id.img_notification);
        img_birthday = (ImageView) findViewById(R.id.img_birthday);
        img_att = (ImageView) findViewById(R.id.img_att);
        layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
        layout_notification = (LinearLayout) findViewById(R.id.layout_notification);
        layout_meeting = (LinearLayout) findViewById(R.id.layout_meeting);
        tv_call_cnt = (TextView) findViewById(R.id.tv_call_cnt);
        rel_call = (RelativeLayout) findViewById(R.id.rel_call);
        horizontal_view = (HorizontalScrollView) findViewById(R.id.horizontal_view);

        len=findViewById(R.id.len);
        img_playstore=findViewById(R.id.img_playstore);

        pm = ActivityModuleSelection.this.getPackageManager();









        txt_calllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.crm.calllogs"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });


        card_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityModuleSelection.this, TargetAchievmentReport.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });


       // getSupportLoaderManager().initLoader(1, null, ActivityModuleSelection.this);


        /*Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();*/

        //count1 = count1 + 1;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        current_date = df.format(c);
        String yesterday_date1 = userpreferences.getString("Yesterday", yesterday_date);
        offday = userpreferences.getString("Birthday", "0");
        if (!current_date.equals(yesterday_date1)) {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadBirthdayDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(ActivityModuleSelection.this, msg);
                    }
                });
            } else {
                ut.displayToast(ActivityModuleSelection.this, "No Internet connection");
            }
        } else {
            if (offday.equalsIgnoreCase("1")) {
                UpdateBirthdayListonly();
            } else {
                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadBirthdayDataJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(ActivityModuleSelection.this, msg);
                        }
                    });
                } else {
                    ut.displayToast(ActivityModuleSelection.this, "No Internet connection");
                    //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                }
            }
        }


        pieChart.setOnTouchListener(new ChartTouchListener(pieChart) {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(ActivityModuleSelection.this, AttendanceDisplayActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                return false;
            }
        });

        /*pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        if (isnet()) {

            progressDialog.setCancelable(true);
            progressDialog.show();
            progressDialog.setContentView(R.layout.vwb_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            new StartSession(ActivityModuleSelection .this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadAttendanceWidget().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }


        if (isnet()) {

            new StartSession(ActivityModuleSelection .this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadClaimWidget().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

        if (isnet()) {

            new StartSession(ActivityModuleSelection .this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTarget().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }



        if(EnvMasterId.equalsIgnoreCase("hajmola")) {
            startActivity(new Intent(ActivityModuleSelection.this, WelcomeScreenActivity.class));
            finish();

        }

        if (Constants.type == Constants.Type.CRM) {

            if (isnet()) {

                if (progressDialog == null) {
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                }
                progressDialog.show();

                new StartSession(ActivityModuleSelection .this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadModuleList().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });

            }

            //  txt_crm.setText(WebUrlClass.app_name_moduleselection_CRM);

            card_pm.setVisibility(View.GONE);
            card_vwb.setVisibility(View.VISIBLE);
            card_crm.setVisibility(View.VISIBLE);
            card_service.setVisibility(View.VISIBLE);
            img_wms.setVisibility(View.GONE);

        } else if (Constants.type == Constants.Type.Vwb) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ActivityModuleSelection.this);
            Gson gson = new Gson();
            String json = sharedPrefs.getString("moduleselection", "");
            Type type = new TypeToken<List<ModuleName>>() {
            }.getType();
            if (gson.fromJson(json, type) != null) {
                moduleNameArrayList = gson.fromJson(json, type);
            }
            if (moduleNameArrayList.size() == 0) {
                if (isnet()) {

                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                    new StartSession(ActivityModuleSelection.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadModuleList().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                }

            }else {
                if (moduleNameArrayList.size() > 0) {
                    horizontal_view.setVisibility(View.VISIBLE);
                 //   len_chat.setVisibility(View.GONE);
                    img_expense.setVisibility(View.GONE);
                    card_vwb.setVisibility(View.GONE);
                    card_crm.setVisibility(View.GONE);
                    card_sales.setVisibility(View.GONE);
                    card_service.setVisibility(View.GONE);
                    img_wms.setVisibility(View.GONE);
                    card_inventory.setVisibility(View.GONE);
                    card_print.setVisibility(View.GONE);
                  //  len_pi.setVisibility(View.GONE);

                    for (int i=0;i<moduleNameArrayList.size();i++){
                            String home=moduleNameArrayList.get(i).getModuleName();

                        if (home.equalsIgnoreCase("VWB")){
                            card_vwb.setVisibility(View.VISIBLE);
                            img_expense.setVisibility(View.VISIBLE);
                        }
                        if (home.equalsIgnoreCase("CRM")){
                            card_crm.setVisibility(View.VISIBLE);
                            isPackageInstalled("com.crm.calllogs",pm);

                        }
                        if (home.equalsIgnoreCase("Inventory")){
                            card_inventory.setVisibility(View.VISIBLE);
                        }
                        if (home.equalsIgnoreCase("Support")){
                            card_service.setVisibility(View.VISIBLE);
                        }
                        if (home.equalsIgnoreCase("Sales")){
                            card_sales.setVisibility(View.VISIBLE);
                        }if (home.equalsIgnoreCase("WMS")){
                            img_wms.setVisibility(View.VISIBLE);
                        }
                        if (home.equalsIgnoreCase("PI")){
                            //len_pi.setVisibility(View.VISIBLE);
                            card_print.setVisibility(View.VISIBLE);
                        }
                        else {

                        }

                        }



                }
            }
            // Expense Count

            if (isnet()) {



                new StartSession(ActivityModuleSelection.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownExpenseCount().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });

            }



        } else if (Constants.type == Constants.Type.PM) {



            // card_pm.setVisibility(View.VISIBLE);
            horizontal_view.setVisibility(View.VISIBLE);
            len_vwb.setVisibility(View.GONE);
            card_vwb.setVisibility(View.GONE);
            card_crm.setVisibility(View.VISIBLE);
            card_pm.setVisibility(View.VISIBLE);
            card_service.setVisibility(View.GONE);
            img_wms.setVisibility(View.GONE);

        } else if (Constants.type == Constants.Type.Delivery) {

            Intent intent = new Intent(ActivityModuleSelection.this, ActivityMain.class);
            startActivity(intent);
            finishAffinity();
        }else if(Constants.type == Constants.Type.MilkRun){
            card_crm.setVisibility(View.INVISIBLE);
            card_service.setVisibility(View.GONE);
            card_inventory.setVisibility(View.GONE);
            conversation.setVisibility(View.GONE);
            card_sales.setVisibility(View.GONE);
            img_wms.setVisibility(View.GONE);
            card_vwb.setVisibility(View.VISIBLE);

        } else if (Constants.type == Constants.Type.Sahara) {

            card_crm.setVisibility(View.INVISIBLE);
            card_service.setVisibility(View.GONE);
            card_inventory.setVisibility(View.GONE);
            conversation.setVisibility(View.VISIBLE);
            card_sales.setVisibility(View.GONE);
            card_vwb.setVisibility(View.VISIBLE);
            img_wms.setVisibility(View.GONE);

        }


        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);


        beanLogInsettingArrayList = new ArrayList<>();

        if (googleApiClient == null) {
            if (ut.isNet(ActivityModuleSelection.this)) {
                boolean gpsEnable = true;
                if (Constants.type == Constants.Type.PM) {
                   gpsEnable = AppCommon.getInstance(context).IsGpsLocationEnable();
                }
                if (gpsEnable)
                    EnableGPSAutoMatically();
            } else {
                ut.displayToast(this, "No Internet Connection");
            }
        }

        setJobShedulder();
        mNetworkReceiver = new ConnectivityReceiver();
        registerNetworkBroadcastForNougat();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.nav_header_activity_module_selection, null, false);
        txtUserLoginID = (TextView) listHeaderView.findViewById(R.id.txt_userLoginID);
        txtMobile = (TextView) listHeaderView.findViewById(R.id.txt_mobile);
        txtcompcode = (TextView) listHeaderView.findViewById(R.id.txt_compcode);
        img_adduser = (ImageView) listHeaderView.findViewById(R.id.img_adduser);
        img_userprofile = (ImageView) listHeaderView.findViewById(R.id.img_profile);
        mDrawerList.addHeaderView(listHeaderView);

        /*getLogindata();
        SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        String setKey = sharedpreferences.getString(WebUrlClass.MyPREFERENCES_SETTING_KEY, "");
        setLogindata(setKey);*/


        /*try {
            PackageManager packageManager = context.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=+917020256278&text=" + URLEncoder.encode("Hi", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Whtsapp",e.getMessage());
        }*/



        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // mSpinner.getSelectedItem().toString();
                settingKey = beanLogInsettingArrayList.get(position - 1).getLogInKey();
                String User = beanLogInsettingArrayList.get(position - 1).getUserLogInId();
                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_SETTING_KEY, settingKey);
                editor.putInt(WebUrlClass.MyPREFERENCES_SETTING_POSITION_KEY, position);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Selected user " + User, Toast.LENGTH_LONG).show();
                setLogindata(settingKey);
                IsCRMUser = ut.getValue(getApplicationContext(), WebUrlClass.GET_ISCRMUSER_KEY, settingKey);

                SharedPreferences AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = AtendanceSheredPreferance
                        .edit();
                editor1.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                editor1.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                editor1.commit();

                mDrawerLayout.closeDrawers();

            }
        });

        img_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityLogIn.class);
                i.putExtra(WebUrlClass.INTENT_LOGIN_SCREEN_BACKFLAG, WebUrlClass.VALUE_LOGIN_SCREEN_BACKFLAG);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        card_vwb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(ActivityModuleSelection.this, VWBHomeActivity.class);
                Intent intent = new Intent(ActivityModuleSelection.this, ActivityMain.class);
               // Intent intent = new Intent(ActivityModuleSelection.this, VWBHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        chat_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    Intent intent5 = new Intent(ActivityModuleSelection.this, OpenChatroomActivity.class);
                    intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent5);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                } else {
                    Toast.makeText(ActivityModuleSelection.this, "Chat module is not installed", Toast.LENGTH_SHORT).show();

                }
                //AppCommon.getInstance(ActivityMain.this).setChatPostion(0);
            }
        });

        card_crm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(ActivityModuleSelection.this, CRMHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/

                if (IsCRMUser.equalsIgnoreCase("false")) {

                  /*  Intent intent = new Intent(ActivityModuleSelection.this, CallListActivity.class);
                    startActivity(intent);*/

                     Intent intent = new Intent(ActivityModuleSelection.this, CRMHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                } else {
                  // ut.displayToast(getApplicationContext(), "You are not CRM user");

                    Intent intent = new Intent(ActivityModuleSelection.this, CRMHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
        card_pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityModuleSelection.this, ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        card_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent ticket = new Intent(ActivityModuleSelection.this, TicketRegisterActivity.class);//LeaveSummary
                ticket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ticket);

                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);




               /*Intent ticket = new Intent(ActivityModuleSelection.this, WelcomeScreenActivity.class);//LeaveSummary
                startActivity(ticket);
*/

            }
        });
        card_pm_crm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsCRMUser.equalsIgnoreCase("false")) {
                    Intent intent = new Intent(ActivityModuleSelection.this, CRMHomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                } else {
                    ut.displayToast(getApplicationContext(), "You are not CRM user");
                }

            }
        });
        card_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent ticket = new Intent(ActivityModuleSelection.this, SelectModuleActivity.class);//LeaveSummary
                ticket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ticket);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

              /*  Intent ticket = new Intent(ActivityModuleSelection.this, HomeScreenActvity.class);//LeaveSummary
                startActivity(ticket);
*/

               /* Intent ticket = new Intent(ActivityModuleSelection.this, DeviceSettingActivity.class);//LeaveSummary
                startActivity(ticket);
*/
                //ut.displayToast(getApplicationContext(),"Comming soon");


            }
        });
        card_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsCRMUser.equalsIgnoreCase("false")) {
                    Intent intent = new Intent(ActivityModuleSelection.this, Sales_HomeSActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }else {
                    ut.displayToast(getApplicationContext(),"You are not Sales user");
                }
            }
        });

        card_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ActivityModuleSelection.this, BluetoothConnectivityActivity.class);
                startActivity(intent);*/

               // For Jal project
                Intent intent = new Intent(ActivityModuleSelection.this, BluetoothConnectivityActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


            }
        });

        img_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent ticket = new Intent(ActivityModuleSelection.this, AddExpenseActivity.class);//LeaveSummary
                 ticket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ticket);*/

                Intent ticket = new Intent(ActivityModuleSelection.this, ExpenseSelectionActivity.class);//LeaveSummary
                ticket.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ticket);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);



            }
        });
        img_wms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityModuleSelection.this, AlfaHomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        layout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ActivityMain.this, NotificationActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(ActivityModuleSelection.this, ActvityNotificationTypeNameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


            }
        });
        img_att.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityModuleSelection.this, AttendanceDisplayActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        layout_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    Intent intent = new Intent(ActivityModuleSelection.this, BirthdayListAcyivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);




            }
        });

        rel_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(ActivityModuleSelection.this)) {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 2909);
                    } else {

                    }
                } else {
                    Intent intent = new Intent(ActivityModuleSelection.this, CRM_CallLogList.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }




            }
        });

        getIsDeliveryBoy();
       /* if(ut.IsChangePassword(context)){
            startActivity(new Intent(this , ActivityLogIn.class));
            finishAffinity();
        }*/
    }




    private void getIsDeliveryBoy() {
        if (isnet()) {
            new StartSession(this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //ShowProgress();
                    new FindIsUserDeliveryBoy().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(ActivityModuleSelection.this, msg, Toast.LENGTH_LONG).show();
                    // HideProgress();
                }
            });

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

    private void registerNetworkBroadcastForNougat() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void setJobShedulder() {

        // checkBatteryOptimized();
        if (myJob == null) {
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityModuleSelection.this));
            callJobDispacher();
        } else {
            if (!AppCommon.getInstance(this).isServiceIsStart()) {
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityModuleSelection.this));
                callJobDispacher();
            } else {
                dispatcher.cancelAll();
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityModuleSelection.this));
                myJob = null;
                callJobDispacher();
            }
        }

    }

    private void checkBatteryOptimized() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName())) {
                askIgnoreOptimization();
            } else {
                // accepted;
            }
        } else {
            // accepted;
        }
    }

    private void askIgnoreOptimization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, IGNORE_BATTERY_OPTIMIZATION_REQUEST);
        } else {
            //  openNextActivity();
        }
    }

    private void callJobDispacher() {

        myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(DownloadJobService.class)
                // uniquely identifies the job
                .setTag("test")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)

                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(180, 240))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(

                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_IDLE
                )
                .build();

        dispatcher.schedule(myJob);
        AppCommon.getInstance(this).setServiceStarted(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
       BackgroundLocationTracking backgroundLocationTracking=new BackgroundLocationTracking(ActivityModuleSelection.this);

        getLogindata();
        SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        String setKey = sharedpreferences.getString(WebUrlClass.MyPREFERENCES_SETTING_KEY, "");
        setLogindata(setKey);
        IsCRMUser = ut.getValue(getApplicationContext(), WebUrlClass.GET_ISCRMUSER_KEY, setKey);
        Log.e("data", "user" + IsCRMUser);

        int notiCount = AppCommon.getInstance(this).getNotificationCount();
        String Msgcount = String.valueOf(notiCount);
        if (notiCount != 0) {
            chatCount.setVisibility(View.VISIBLE);
        } else
            chatCount.setVisibility(View.GONE);
        chatCount.setText(Msgcount);
        Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG +
                " WHERE MobileCallType='" + incoming + "' OR MobileCallType='" + outgoing + "'"+
                " OR MobileCallType='" + opportunity + "' OR MobileCallType='" + opportunitycollection + "'" +
                "Order by StartTime DESC limit 100", null);
        int count1 = c1.getCount();
        if (count1>0) {
            rel_call.setVisibility(View.VISIBLE);
            tv_call_cnt.setVisibility(View.VISIBLE);
            tv_call_cnt.setText(String.valueOf(count1));
        }


    }

    private void getLogindata() {
        beanLogInsettingArrayList.clear();
        String searchQuery = "SELECT * FROM " + db.TABLE_LOGIN_SETTING;
        Cursor c = sql.rawQuery(searchQuery, null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                BeanLogInsetting Bean = new BeanLogInsetting();
                Bean.setLogInKey(c.getString(c.getColumnIndex("LogInKey")));
                Bean.setCompanyURL(c.getString(c.getColumnIndex("CompanyURL")));
                Bean.setEnvId(c.getString(c.getColumnIndex("EnvId")));
                Bean.setPlantID(c.getString(c.getColumnIndex("PlantID")));
                Bean.setPlantName(c.getString(c.getColumnIndex("PlantName")));
                Bean.setUserLogInId(c.getString(c.getColumnIndex("UserLogInId")));
                Bean.setUserMasterId(c.getString(c.getColumnIndex("UserMasterId")));
                Bean.setPassword(c.getString(c.getColumnIndex("Password")));
                Bean.setMobile(c.getString(c.getColumnIndex("Mobile")));
                Bean.setDatabaseName(c.getString(c.getColumnIndex("DatabaseName")));
                beanLogInsettingArrayList.add(Bean);

            } while (c.moveToNext());

            mDrawerList.setAdapter(new CustomAdapter(this, beanLogInsettingArrayList));

            //  c.close();
            //Sql.close();
        }

        // return data;
    }

    private void setLogindata(String settingKey) {
        //   beanLogInsettingArrayList.clear();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOGIN_SETTING + " where LogInKey='" + settingKey + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                String loginId = c.getString(c.getColumnIndex("UserLogInId"));
                String companyURL = c.getString(c.getColumnIndex("CompanyURL"));
                String mobile = c.getString(c.getColumnIndex("Mobile"));
                String envId = c.getString(c.getColumnIndex("EnvId"));
                txtUserLoginID.setText(loginId);
                txtMobile.setText(mobile);
                txtcompcode.setText(companyURL);

                if (Constants.type == Constants.Type.CRM) {
                    txtcompcode.setText(companyURL);

                } else if (Constants.type == Constants.Type.Vwb) {
                    txtcompcode.setText(companyURL);

                } else if (Constants.type == Constants.Type.PM) {

                    txtcompcode.setText(envId);
                }

            } while (c.moveToNext());
        }

    }


   /* @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }*/


    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.vritti.ekatm.services.PaidLocationFusedLocationTracker1".equals(service.service.getClassName())) {
                //  Toast.makeText(ActivityMain.this,"Yes",Toast.LENGTH_SHORT).show();

                return true;
            }
        }
        Intent serviceIntent = new Intent(ActivityModuleSelection.this, PaidLocationFusedLocationTracker1.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            ActivityModuleSelection.this.startService(serviceIntent);
        }

        //  Toast.makeText(ActivityMain.this,"False",Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //   mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //   mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
       /* if (mdrawertoggle.onoptionsitemselected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void EnableGPSAutoMatically() {
        // GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(ActivityModuleSelection.this)
                    .addApi(LocationServices.API).addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) ActivityModuleSelection.this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) ActivityModuleSelection.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                           /* Intent intent = new Intent(ActivityModuleSelection.this, ActivityMain.class);
                            startActivity(intent);*/

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //  toast("GPS is not on");
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.

                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ActivityModuleSelection.this, REQUEST_CODE);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //  toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                System.out.println("Resultdata" + result);
                // splash();

               /* Intent i = getIntent();

                Intent intent = new Intent(ActivityModuleSelection.this, ActivityMain.class);
                startActivity(intent);
                finish();*/

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
              //  ActivityModuleSelection.this.finish();
            }
        }
        /*if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class FindIsUserDeliveryBoy extends AsyncTask<String, Void, String> {
        String response = "";
        List<String> EnvName = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_IsDelieveryBoy + "=" + UserMasterId;
            try {

                response = ut.OpenConnection(url, getApplicationContext());
                response = response.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

                Log.i("data: ", response.toString());
                JSONObject jsonObject = new JSONObject(response);

                String isDeliveryBoy = String.valueOf(jsonObject.get("IsDeliveryAgent"));
                if (isDeliveryBoy != null) {
                    AppCommon.getInstance(context).setDeliveryBoy(isDeliveryBoy);
                } else {
                    AppCommon.getInstance(context).setDeliveryBoy("false");
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


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    class DownloadModuleList extends AsyncTask<String, Void, String> {

        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }
        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetLicenceWiseModule;
            try {
                res = ut.OpenConnection(url,ActivityModuleSelection.this);
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")){


            }else {
                if (response != null) {

                    moduleNameArrayList.clear();
                    if (response.contains("#")){
                        String[]  res=response.split("#");
                        String[] res_1=res[0].split(",");
                        horizontal_view.setVisibility(View.VISIBLE);
                        for (int i=0;i<res_1.length;i++)
                        {
                            ModuleName moduleName=new ModuleName();
                            String moduel=res_1[i].toString();
                            moduleName.setModuleName(moduel);
                            moduleNameArrayList.add(moduleName);

                        }


                       /* ModuleAdapter mLogoGridAdapter = new ModuleAdapter(ActivityModuleSelection.this, moduleNameArrayList);
                        recyclerView.setLayoutManager(new GridLayoutManager(ActivityModuleSelection.this, 3));
                        recyclerView.setAdapter(mLogoGridAdapter);
*/
                      //  len_chat.setVisibility(View.GONE);

                        img_expense.setVisibility(View.GONE);
                        card_vwb.setVisibility(View.GONE);
                        card_crm.setVisibility(View.GONE);
                        card_sales.setVisibility(View.GONE);
                        card_service.setVisibility(View.GONE);
                        img_wms.setVisibility(View.GONE);
                        card_inventory.setVisibility(View.GONE);
                        card_print.setVisibility(View.GONE);
                        //len_pi.setVisibility(View.GONE);

                        if (moduleNameArrayList.size()>0){
                            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ActivityModuleSelection.this);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            Gson gson = new Gson();

                            String json = gson.toJson(moduleNameArrayList);
                            editor.putString("moduleselection", json);
                            editor.commit();
                            for (int i=0;i<moduleNameArrayList.size();i++){
                                String home=moduleNameArrayList.get(i).getModuleName();

                                if (home.equalsIgnoreCase("VWB")){
                                    card_vwb.setVisibility(View.VISIBLE);
                                    img_expense.setVisibility(View.VISIBLE);
                                }
                                if (home.equalsIgnoreCase("CRM")){
                                    card_crm.setVisibility(View.VISIBLE);
                                    isPackageInstalled("com.crm.calllogs",pm);

                                }
                                if (home.equalsIgnoreCase("Inventory")){
                                    card_inventory.setVisibility(View.VISIBLE);
                                }
                                if (home.equalsIgnoreCase("Support")){
                                    card_service.setVisibility(View.VISIBLE);
                                }
                                if (home.equalsIgnoreCase("Sales")){
                                    card_sales.setVisibility(View.VISIBLE);
                                }if (home.equalsIgnoreCase("WMS")){
                                    img_wms.setVisibility(View.VISIBLE);
                                }
                                if (home.equalsIgnoreCase("PI")){
                                    card_print.setVisibility(View.VISIBLE);
                                }
                                else {

                                }
                            }
                        }




                    }


                }
            }

        }

    }

    class DownloadAttendanceWidget extends AsyncTask<String, Void, String> {

        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetAttenDashData;
            try {
                res = ut.OpenConnection(url, ActivityModuleSelection.this);
                if (res!=null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ActivityModuleSelection.this,e.getMessage(),Toast.LENGTH_LONG).show();
                response=e.getMessage();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                if (response != null) {
                    try {

                        JSONArray jsonArray = new JSONArray(integer);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            List pieData = new ArrayList<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            double Present=jsonObject.getDouble("Present");
                            double AU=jsonObject.getDouble("AU");
                           /* String s = String.valueOf(AU);
                            String str = s.replace(".0", "");
                            int AB = Integer.parseInt(str);
                            tv_meeting_cnt.setText(String.valueOf(AB));*/
                            double Leave=jsonObject.getDouble("Leave");
                            double NotOnRoll=jsonObject.getDouble("NotOnRoll");
                            /*                    if (Present==0.00||Present==0){
                            }else {
                                pieData.add(new SliceValue((float) Present, getResources().getColor(R.color.present)).setLabel(String.valueOf(Present)));
                            }
                            if (AU==0.00||AU==0){
                            }else {
                                pieData.add(new SliceValue((float) AU, getResources().getColor(R.color.absent)).setLabel(String.valueOf(AU)));
                            }
                            if (Leave==0.00||Leave==0){
                            }else {
                                pieData.add(new SliceValue((float) Leave, getResources().getColor(R.color.leave)).setLabel(String.valueOf(Leave)));
                            }
                            if (NotOnRoll==0.00||NotOnRoll==0){
                            }else {
                                pieData.add(new SliceValue((float) NotOnRoll, getResources().getColor(R.color.roll)).setLabel(String.valueOf(NotOnRoll)));
                            }

                            PieChartData pieChartData = new PieChartData(pieData);
                            pieChartData.setHasLabels(true).setValueLabelTextSize(10);
                            pieChartData.setHasCenterCircle(true).setCenterText1("Attendance").setCenterText1FontSize(11).setCenterText1Color(Color.parseColor("#0097A7"));
                            pieChartView.setPieChartData(pieChartData);
        */
                            pieChart.setDescription("");
                            pieChart.setRotationEnabled(false);
                            pieChart.setHoleRadius(30f);
                            pieChart.setTransparentCircleAlpha(0);
                            pieChart.setCenterText("Attendance");
                            pieChart.setCenterTextSize(7);
                            pieChart.setNoDataText("Attendance");


                            ArrayList<PieEntry> yEntrys = new ArrayList<>();
                            ArrayList<String> xEntrys = new ArrayList<>();
                            ArrayList<Integer> colors = new ArrayList<>();
                           /* Present=27f;
                            AU=20f;
                            Leave=5f;
                            NotOnRoll=10f;*/

                            if (Present==0.00||Present==0){
                            }else {
                                yEntrys.add(new PieEntry((float) Present));
                                colors.add(getResources().getColor(R.color.present));

                            }
                            if (AU==0.00||AU==0){
                            }else {
                                yEntrys.add(new PieEntry((float) AU));
                                colors.add(getResources().getColor(R.color.absent));

                            }
                            if (Leave==0.00||Leave==0){
                            }else {
                                yEntrys.add(new PieEntry((float) Leave));
                                colors.add(getResources().getColor(R.color.leave));

                            }
                            if (NotOnRoll==0.00||NotOnRoll==0){
                            }else {
                                yEntrys.add(new PieEntry((float) NotOnRoll));
                                colors.add(getResources().getColor(R.color.roll));

                            }
                            xEntrys.add("Present");
                            xEntrys.add("Absent");
                            xEntrys.add("Leave");
                            xEntrys.add("Not on Roll");



                            //create the data set
                            PieDataSet pieDataSet = new PieDataSet(yEntrys, xEntrys.toString());
                            pieDataSet.setSliceSpace(2);
                            pieDataSet.setValueTextSize(8);
                            pieDataSet.setColors(colors);


                            //add colors to dataset


                           /* Legend legend = pieChart.getLegend();
                            legend.setForm(Legend.LegendForm.CIRCLE);
                            legend.setTextSize(8);
                            legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
*/

                            PieData pieData1 = new PieData(pieDataSet);
                            pieChart.setData(pieData1);
                            pieChart.invalidate();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pieChart.setNoDataText("Attendance");

                    }
                }

            }

        }
    }

    class DownloadClaimWidget extends AsyncTask<String, Void, String> {

        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.ShowWidgetClaim+"?UserMasterId="+UserMasterId+"&UserLoginId="+LoginId;
            try {
                res = ut.OpenConnection(url, ActivityModuleSelection.this);
                response = res.toString();
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                if (response != null) {
                    try {

                        JSONObject jsonObject=new JSONObject(response);

                        JSONArray jsonArray=jsonObject.getJSONArray("UnderApprovalClaim");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String CountUnderClaim=object.getString("CountUnderClaim");
                            double UnderClaim=Double.parseDouble(CountUnderClaim);
                            btn_unclaimed.setText(String.format("%.2f", UnderClaim));
                            String UnderApproval=object.getString("UnderApproval");
                            double underApproval=Double.parseDouble(UnderApproval);
                            btn_unapp.setText(String.format("%.2f", underApproval));

                            JSONArray jsonArray1=jsonObject.getJSONArray("PendingAdvAmount");
                            JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                            String PendingAdvance=jsonObject1.getString("PendingAdvance");
                            double pendingAdvance=Double.parseDouble(PendingAdvance);
                            btn_advance.setText(String.format("%.2f", pendingAdvance));

                            JSONArray jsonArray2=jsonObject.getJSONArray("PendingFinalAmount");
                            JSONObject jsonObject2=jsonArray2.getJSONObject(0);
                            String PendingAmount=jsonObject2.getString("PendingAmount");
                            double pendingAmount=Double.parseDouble(PendingAmount);
                            btn_unpaid.setText(String.format("%.2f", pendingAmount));



                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }


    class DownloadTarget extends AsyncTask<String, Void, String> {

        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.GetTargetAndAchievement+"?StartMonth=2021-03&EndMonth="+EndMonth+"&UsermasterId="+UserMasterId+"&StartDt=2021-04-01&EndDt="+EndDt;
           // String url = CompanyURL + WebUrlClass.GetTargetAndAchievement+"?StartMonth=2021-03&EndMonth="+EndMonth+"&UsermasterId=fe66846d-6656-41b1-b3e0-2b2d20d71592&StartDt=2021-04-01&EndDt="+EndDt;

            try {
                res = ut.OpenConnection(url, ActivityModuleSelection.this);
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                if (response != null) {
                    try {

                       // JSONObject jsonObject=new JSONObject(response);

                         JSONArray jsonArray=new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String  IndividualAchievement=object.getString("IndividualAchievement");
                            double Achived=Double.parseDouble(IndividualAchievement);
                            btn_achived.setText(String.format("%.2f", Achived));
                            String  IndividualTarget=object.getString("IndividualTarget");
                            double Target=Double.parseDouble(IndividualTarget);
                            btn_target.setText(String.format("%.2f", Target));
                            String  OutStandingAmount=object.getString("OutStandingAmount");
                            double OutStanding=Double.parseDouble(OutStandingAmount);
                            btn_outstanding.setText(String.format("%.2f", OutStanding));
                            String CollectedAmount=object.getString("CollectedAmount");
                            double Collection=Double.parseDouble(CollectedAmount);
                            btn_collected.setText(String.format("%.2f", Collection));

                            double avg= Achived/Target*100;
                            int value = (int)avg;
                            txt_avg.setText(String.valueOf(value+"%"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }



    private void UpdateBirthdayListonly() {
        String query = "SELECT * FROM " + db.TABLE_BIRTHDAY+ " WHERE " +
                " DtDay='Today'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            tv_birthday_cnt.setVisibility(View.VISIBLE);
        }
        tv_birthday_cnt.setText(cur.getCount() + "");
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
                res = ut.OpenConnection(url, ActivityModuleSelection.this);
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

        }

    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        cursorLoader = new Loader<>(this);
        try {
            //content://com.example.contentproviderexample.MyProvider1/cte
            cursorLoader = new CursorLoader(this, Uri.parse("content://com.example.contentproviderexample.MyProvider1/cte"), null,
                    null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null) {
        } else {
            ArrayList<CallLogsDetails> callLogsDetailsArrayList = new ArrayList<>();


           // sql.delete(db.TABLE_CALL_LOG, null, null);

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
                rel_call.setVisibility(View.VISIBLE);
                tv_call_cnt.setVisibility(View.VISIBLE);
                tv_call_cnt.setText(String.valueOf(stringArrayList.size()));
            } else {
            }
        }


        //resultView.setText(res);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
*/

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            callloglayout();

            return false;
        }
    }

    private void callloglayout() {

        final Snackbar snackbar = Snackbar.make(len, "", Snackbar.LENGTH_LONG);

        // inflate the custom_snackbar_view created previously
        View customSnackView = getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        len_call_log=customSnackView.findViewById(R.id.len_call_log);
        ImageView cancel=customSnackView.findViewById(R.id.cancel);
        ImageView img_calllog=customSnackView.findViewById(R.id.img_calllog);
        TextView txt_know_more=customSnackView.findViewById(R.id.txt_know_more);
        snackbar.setDuration(5000);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        img_calllog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.crm.calllogs"));
                startActivity(intent);
            }
        });
        txt_know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityModuleSelection.this, KnowMoreActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        // set the background of the default snackbar as transparent
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);

        // now change the layout of the snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        // set padding of the all corners as 0
        snackbarLayout.setPadding(0, 0, 0, 0);

        // register the button from the custom_snackbar_view layout file

        // now handle the same button with onClickListener

        // add the custom snack bar layout to snackbar layout
        snackbarLayout.addView(customSnackView, 0);

        snackbar.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
        if(myBroadcastReceiver != null){
            unregisterReceiver(myBroadcastReceiver);
    }
    }

    class DownExpenseCount extends AsyncTask<String, Void, String> {

        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetCountExpReg+"?UserMasterId="+UserMasterId;
            try {
                res = ut.OpenConnection(url, ActivityModuleSelection.this);
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                if (response != null) {
                    try {
                        JSONArray jResults = new JSONArray(response);
                        JSONObject jsonObject=jResults.getJSONObject(0);
                        String GetCountExpReg=jsonObject.getString("GetCountExpReg");
                        if (GetCountExpReg.equalsIgnoreCase("0")){

                        }else {
                            txt_exp.setVisibility(View.VISIBLE);
                            txt_exp.setText(GetCountExpReg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    Intent intent = new Intent(ActivityModuleSelection.this, CRM_CallLogList.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}

