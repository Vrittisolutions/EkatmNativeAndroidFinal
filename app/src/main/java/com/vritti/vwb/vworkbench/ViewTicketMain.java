package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;


public class ViewTicketMain extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",PromiseTime="";
    Utility ut;
    static DatabaseHandlers db;
    CommonFunction cf;

    Context context;
    private Context mContext;
    String effDetailHr = "";
    String effDetailto = "";
    String effDetailfrom = "";
    String timeFrom = "";
    String timeTo = "";
    String ActualStartDate = "";
    SharedPreferences userpreferences;
    String mobile;
    String Result = "", ProjectId = "", SaveToDelegate = "", Attachment = "", MailClient = "NoMail", Action = "", effortdetailTime = "", remaindraction = "", remainderactiondateselect = "";
    private String timeSpinnerFrom = "", ActivityId = "", SourceId, FinalOutcome = "", RootCause = "", timeSpinnerTo = "", LogInUserMasterId = "",
            UnitId = "",
            ActivityTypeId = "",
            IssuedTo = "",
            ReportingToEmail = "",
            ReportingToName = "", ActivityName,
            ReportedByEmail = "",
            ConsigneeName = "",
            TicketCategory = "", TicketDiscription = "",
            TicketNo = "", ProductNAme = "", txtCurrentPromise = "", Startdate = "",ActivityCode="",
            hdnfldPromiseCount = "", hdnfldSaveToDelegate,
            hdnfld1stpromisedt = "", hdnfldcurntpromse = "", chkMailClient, hdnfldMailClient, lblReportedByEmail = "", Solution = "";
    private Button btn_Save, btn_Cancel,
            btn_SaveTransfer;
    private EditText edt_Action, edt_effertdetaildateset, edt_result,
            edt_resonfortransfer, edt_remaindraction,
            edt_remainderactionsetdate, edt_hourremainderaction, edt_solution,edt_promise;
    private TextView txt_effortdetailfrom, txt_effortdetailto,
            txt_effortdetailhour, txt_customername, txt_lblAction,
            txt_lblEffortDetail, textView2,
            txt_lblFinalOutcome;
    private ImageView img_effortdetailstart, btn_remainderactiondateselect, img_effortdetailpause, img_effortdetailrestart, btn_effortdetaildateselect;
    private Spinner spinner_rootcause, spinner_finaloutcome;

    TextView spinner_effortdetailfrom, spinner_effortdetailto,spinnere_promisetime;

    private SearchableSpinner Autocomplettransferto;
    private LinearLayout lin_transferto, lin_resonfortranfer, lin_Consignee;
    private CheckBox checkboxmail;
    private DownloadFinalOutcome finalOutcome;
    private DownloadTicketUpdateData ticketUpdateData;//DownloadPromisCount
    private UploadTransfer uploadTransfer;
    ArrayList<String> UnChkUser_list;
    private long startTime = 0L;
    private Handler customHandler;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    SimpleDateFormat dateFormat;
    SimpleDateFormat dateFormatdate,dateFormatdate1;
    //ProgressHUD progressHUD;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
    Date DOBDate = null;

    AlertDialog dialog;
    private Uri mImageCaptureUri;
    android.support.v7.app.ActionBar actionBar;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo;
    String imageName;
    String encodedImage, Imagejson;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    Button btnFusedLocation;
    EditText chargeAmount;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime, placename, Loacationname;
    private BufferedReader UploadedImage;
    StringBuilder text;
    private Bitmap bitmap;
    private File imageFile;
    private String currentdate, key;
    private String date, time;
    Date date_1;
    int current_time;
    static SQLiteDatabase sql;
    List<String> lstReason_Outage = new ArrayList<String>();
    private String lat = "", lng = "";
    private String latitude = "", longitude = "", Address = "";
    private String ActualCompletionDate = "", PriorityId = "";
   public String OutcomeID;
   TextView txt_customerinfo;

     ImageView img_promise;
    String[] ArrarlisttimeSpinnerFrom = {"08:00", "08:05", "08:10", "08:15", "08:20", "08:25", "08:30", "08:35", "08:40", "08:45", "08:50", "08:55",
            "09:00", "09:05", "09:10", "09:15", "09:20", "09:25", "09:30", "09:35", "09:40", "09:45", "09:50", "09:55",
            "10:00", "10:05", "10:10", "10:15", "10:20", "10:25", "10:30", "10:35", "10:40", "10:45", "10:50", "10:55",
            "11:00", "11:05", "11:10", "11:15", "11:20", "11:25", "11:30", "11:35", "11:40", "11:45", "11:50", "11:55",
            "12:00", "12:05", "12:10", "12:15", "12:20", "12:25", "12:30", "12:35", "12:40", "12:45", "12:50", "12:55",
            "13:00", "13:05", "13:10", "13:15", "13:20", "13:25", "13:30", "13:35", "13:40", "13:45", "13:50", "13:55",
            "14:00", "14:05", "14:10", "14:15", "14:20", "14:25", "14:30", "14:35", "14:40", "14:45", "14:50", "14:55",
            "15:00", "15:05", "15:10", "15:15", "15:20", "15:25", "15:30", "15:35", "15:40", "15:45", "15:50", "15:55",
            "16:00", "16:05", "16:10", "16:15", "16:20", "16:25", "16:30", "16:35", "16:40", "16:45", "16:50", "16:55",
            "17:00", "17:05", "17:10", "17:15", "17:20", "17:25", "17:30", "17:35", "17:40", "17:45", "17:50", "17:55",
            "18:00", "18:05", "18:10", "18:15", "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55",
            "19:00", "19:05", "19:10", "19:15", "19:20", "19:25", "19:30", "19:35", "19:40", "19:45", "19:50", "19:55",
            "20:00", "20:05", "20:10", "20:15", "20:20", "20:25", "20:30", "20:35", "20:40", "20:45", "20:50", "20:55",
            "21:00", "21:05", "21:10", "21:15", "21:20", "21:25", "21:30", "21:35", "21:40", "21:45", "21:50", "21:55",
            "22:00", "22:05", "22:10", "22:15", "22:20", "22:25", "22:30", "22:35", "22:40", "22:45", "22:50", "22:55",
            "23:00", "23:05", "23:10", "23:15", "23:20", "23:25", "23:30", "23:35", "23:40", "23:45", "23:50", "23:55",
            "24:00"
    };


            ArrayAdapter<String> spinner_fromtime;

    ProgressBar progressbar;
    public String FinalObj;
    public String User="";
    public String UserMasterID;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_view_ticket_main);
        initObject();
        initView();
        setListner();

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
        LogInUserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



        final String[] items = new String[]{"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ViewTicketMain.this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewTicketMain.this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    if (Build.VERSION.SDK_INT < 19) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, PICK_FROM_FILE);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, PICK_FROM_FILE);
                    }


                }
            }
        });

        dialog = builder.create();


        if (cf.check_setup() > 0) {





        } else {
            if (ut.isNet(mContext)) {
                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadDataJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(mContext, msg);

                    }
                });

            }
        }

        if (ut.isNet(mContext)) {
            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCustomerData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(mContext, msg);

                }
            });

        }


        if (cf.getStatecount() > 0) {
            getState();
        } else {
            if (isnet()) {
                new StartSession(ViewTicketMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadReasonOutageJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (isnet()) {
            new StartSession(ViewTicketMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPromisCount().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


        if (cf.isPresentToLocal(mContext, db.TABLE_BIND_FINAL_OUTCOME)) {
            updateSpinnerFinalOutcome();
            if (CheckPresentToLocal()) {


            } else {

                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        ticketUpdateData = new DownloadTicketUpdateData();
                        ticketUpdateData.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        //   dismissProgressDialog();
                    }
                });
            }

        } else {
            if (ut.isNet(mContext)) {

                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        finalOutcome = new DownloadFinalOutcome();
                        finalOutcome.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                ut.displayToast(mContext, "No Internet Connection");
            }
        }
        if (cf.getTeamMebercount() > 0) {
            getAllMembers();
        } else {
            if (isnet()) {
                new StartSession(ViewTicketMain.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadUserlistData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
    }

    private void initObject() {
        Intent intent = getIntent();
        ActivityId = intent.getStringExtra("ActivityId");//Contact
        SourceId = intent.getStringExtra("SourceId");
        ProjectId = intent.getStringExtra("ProjectID");


        mContext = ViewTicketMain.this;
        customHandler = new Handler();
        UnChkUser_list = new ArrayList<String>();
        dateFormat = new SimpleDateFormat("HH:mm");
        dateFormatdate = new SimpleDateFormat("yyyy/MM/dd");
        dateFormatdate1 = new SimpleDateFormat("yyyy-MM-dd");

       /* Calendar c = Calendar.getInstance();

        timeSpinnerFrom= String.valueOf(c.getTime());

        timeSpinnerFrom.replace("IST 2017","");
*/

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        btn_Save = (Button) findViewById(R.id.btnsave);
        btn_Cancel = (Button) findViewById(R.id.btncancel);
        btn_effortdetaildateselect = (ImageView) findViewById(R.id.buttoneffertdetaildateselect);
        btn_remainderactiondateselect = (ImageView) findViewById(R.id.btnremainderactiondateselect);//btnsavetransfer
        btn_SaveTransfer = (Button) findViewById(R.id.btnsavetransfer);
        edt_Action = (EditText) findViewById(R.id.editAction);
        edt_effertdetaildateset = (EditText) findViewById(R.id.editTexteffertdetaildateset);
        edt_effertdetaildateset.setText(dateFormatdate.format(new Date()));
        edt_result = (EditText) findViewById(R.id.edtresult);
        chargeAmount = (EditText) findViewById(R.id.charge_amt);
        edt_resonfortransfer = (EditText) findViewById(R.id.edtresonfortransfer);
        edt_remaindraction = (EditText) findViewById(R.id.remaindraction);
        edt_remainderactionsetdate = (EditText) findViewById(R.id.edtremainderactionsetdate);
        edt_solution = (EditText) findViewById(R.id.edt_solution);
        edt_remainderactionsetdate.setText(dateFormatdate1.format(new Date()));
        edt_hourremainderaction = (EditText) findViewById(R.id.edthourremainderaction);
        txt_effortdetailfrom = (TextView) findViewById(R.id.edteffortdetailfrom);
        textView2 = (TextView) findViewById(R.id.textView2);
        txt_effortdetailto = (TextView) findViewById(R.id.edteffortdetailto);
        txt_effortdetailhour = (TextView) findViewById(R.id.edteffortdetailhour);
        txt_customername = (TextView) findViewById(R.id.consinee);
        img_effortdetailstart = (ImageView) findViewById(R.id.imgeffortdetailstart);
        img_effortdetailpause = (ImageView) findViewById(R.id.imgeffortdetailpause);
        img_effortdetailrestart = (ImageView) findViewById(R.id.imgeffortdetailrestart);
        spinner_finaloutcome = (Spinner) findViewById(R.id.spinnerfinaloutcome);
        spinner_rootcause = (Spinner) findViewById(R.id.spinnerrootCause);
        spinner_effortdetailfrom = (TextView) findViewById(R.id.spinnereffortdetailfrom);
        spinner_effortdetailto = (TextView) findViewById(R.id.spinnereffortdetailto);
        spinnere_promisetime = (TextView) findViewById(R.id.spinnere_promisetime);
        Autocomplettransferto = (SearchableSpinner) findViewById(R.id.Autocomplettransferto);
        lin_transferto = (LinearLayout) findViewById(R.id.lintransferto);
        lin_resonfortranfer = (LinearLayout) findViewById(R.id.linresonfortranfer);
        lin_Consignee = (LinearLayout) findViewById(R.id.linconsinee);
        checkboxmail = (CheckBox) findViewById(R.id.checkboxmail);
        txt_lblAction = (TextView) findViewById(R.id.lblAction);
        txt_lblEffortDetail = (TextView) findViewById(R.id.lblResolDt);
        txt_lblFinalOutcome = (TextView) findViewById(R.id.lblFinalOut);
        img_promise = (ImageView) findViewById(R.id.img_promise);
        edt_promise = (EditText) findViewById(R.id.edt_promise);
        txt_customerinfo = (TextView) findViewById(R.id.txt_customerinfo);
       // View view = findViewById(R.id.app_bar_main_lay);

        progressbar = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

        spinner_rootcause.setSelection(0);



        if (CompanyURL.equalsIgnoreCase("http://www.appnet.co.in")) {
            img_effortdetailrestart.setVisibility(View.GONE);
            img_effortdetailpause.setVisibility(View.GONE);
        }


        Date date2 = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        timeSpinnerFrom = dateFormat.format(date2);
        timeSpinnerTo = dateFormat.format(date2);
       // PromiseTime = dateFormat.format(date2);

        spinner_effortdetailfrom.setText(timeSpinnerFrom);
        spinner_effortdetailto.setText(timeSpinnerFrom);
        spinner_fromtime = new ArrayAdapter<String>(ViewTicketMain.this, android.R.layout.simple_spinner_dropdown_item, ArrarlisttimeSpinnerFrom);




    }

    private void setListner() {


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()) {
                    showProgressDialog();
                    getTicketDetail();
                    Action = edt_Action.getText().toString();
                    String chagre = chargeAmount.getText().toString();
                    if (chagre.equalsIgnoreCase("")) {
                        chagre = "0";
                    }
                    effortdetailTime = edt_effertdetaildateset.getText().toString();
                    DateFormat originalFormat = new SimpleDateFormat("yyyy/MM/dd");
                    DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date_1 = originalFormat.parse(effortdetailTime);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    effortdetailTime = targetFormat.format(date_1);


                    // FinalOutcome,
                    Result = edt_result.getText().toString();

                    if (txt_effortdetailto.getVisibility() == View.VISIBLE &&
                            txt_effortdetailfrom.getVisibility() == View.VISIBLE) {
                        effDetailHr = txt_effortdetailhour.getText().toString();
                        effDetailto = spinner_effortdetailto.getText().toString();
                        effDetailfrom = spinner_effortdetailfrom.getText().toString();

                        timeFrom = "";
                        timeTo = "";
                    } else {
                        effDetailHr = txt_effortdetailhour.getText().toString();
                        timeFrom = timeSpinnerFrom;
                        timeTo = timeSpinnerTo;
                        effDetailto = spinner_effortdetailto.getText().toString();
                        effDetailfrom = spinner_effortdetailfrom.getText().toString();

                    }

                    remaindraction = edt_remaindraction.getText().toString();
                    remainderactiondateselect = edt_remainderactionsetdate.getText().toString();

                    originalFormat = new SimpleDateFormat("yyyy/MM/dd");
                    targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date_1 = originalFormat.parse(remainderactiondateselect);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    remainderactiondateselect = targetFormat.format(date_1);


                    String s = hdnfld1stpromisedt;

                    if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
                        hdnfld1stpromisedt = hdnfldcurntpromse;
                    }
                    txtCurrentPromise="";
                    final String finalChagre = chagre;
                    Solution = edt_solution.getText().toString();
                    PromiseTime=spinnere_promisetime.getText().toString();
                    hdnfld1stpromisedt=edt_promise.getText().toString();
                    hdnfldcurntpromse=edt_promise.getText().toString();
                    txtCurrentPromise=edt_promise.getText().toString();

                    JSONObject jsonObject = new JSONObject();
                    JSONObject data = new JSONObject();


                    try {
                        jsonObject.put("txtActionPerform", Action);
                        jsonObject.put("txtEffortDate", effortdetailTime);
                        jsonObject.put("ddlOutcome", OutcomeID);
                        jsonObject.put("txtResult", Result);
                        jsonObject.put("txtcallfromFolwup", effDetailfrom);
                        jsonObject.put("txtcalltoFolwup", effDetailto);
                        jsonObject.put("txtFlowupHrs", effDetailHr);
                        jsonObject.put("txtRemindAction", remaindraction);
                        jsonObject.put("txtReminderDate", remainderactiondateselect);
                        jsonObject.put("txtReminderTime", "00:00");
                        jsonObject.put("ddlcallfromFolwup", timeFrom);
                        jsonObject.put("ddlcalltoFolwup", timeTo);
                        jsonObject.put("txtCurrentPromise", txtCurrentPromise);
                        jsonObject.put("hdnfldActivityId", ActivityId);
                        jsonObject.put("hdnfldUnitId", UnitId);
                        jsonObject.put("hdnfldActivityTypeId", ActivityTypeId);
                        jsonObject.put("hdnfldProjectId", ProjectId);
                        if (OutcomeID.equals("38")){
                            jsonObject.put("hdnfldIssuedTo", UserMasterID);
                            jsonObject.put("hdnfldIssuedToName", User);
                        }else{
                            jsonObject.put("hdnfldIssuedTo", IssuedTo);
                            jsonObject.put("hdnfldIssuedToName", IssuedTo);
                        }
                        jsonObject.put("hdnfldPromiseCount", hdnfldPromiseCount);
                        jsonObject.put("hdnfldReportingToEmail", ReportingToEmail);
                        jsonObject.put("hdnfldReportingToName", ReportingToName);
                        jsonObject.put("lblTktNo", TicketNo);
                        jsonObject.put("lblTktDescrptn", TicketDiscription);
                        jsonObject.put("hdnfldSaveToDelegate", SaveToDelegate);
                        jsonObject.put("hdnfldReportedByEmail", ReportedByEmail);
                        jsonObject.put("lblCustomerName", ConsigneeName);
                        jsonObject.put("fuAttachment", Attachment);
                        jsonObject.put("hdnfld1stpromisedt", hdnfld1stpromisedt);
                        jsonObject.put("hdnfldcurntpromse", hdnfldcurntpromse);
                        jsonObject.put("lblReportedByName", lblReportedByEmail);
                        jsonObject.put("chkMailClient", MailClient);
                        jsonObject.put("hdnfldMailClient", MailClient);
                        jsonObject.put("lblProductName", ProductNAme);
                        jsonObject.put("ticketCategory", TicketCategory);
                        jsonObject.put("ActualCompletionDate", hdnfld1stpromisedt);
                        jsonObject.put("ActionMasterId", RootCause);
                        jsonObject.put("PriorityId", PriorityId);
                        jsonObject.put("ActualStartDate", Startdate);
                        jsonObject.put("Latitude", latitude);
                        jsonObject.put("Longitude", longitude);
                        jsonObject.put("Address", "");
                        jsonObject.put("MobileNo", mobile);
                        jsonObject.put("ServiceChargetoCust", finalChagre);
                        jsonObject.put("IsCheck", "N");
                        String a1 = "";
                        String a2 = "";
                        String a3 = "";
                        jsonObject.put("VersionMasterId", "");
                        jsonObject.put("InstallerId", "");
                        jsonObject.put("InstallDt", "");
                        jsonObject.put("Note", "");
                        jsonObject.put("ExtraRtCause", Solution);
                        jsonObject.put("PromiseTime",PromiseTime);
                        data.put("obj", jsonObject);
                        FinalObj = data.toString();
                        FinalObj = FinalObj.replaceAll("\\\\", "");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String remark = "Ticket update sucsessfully";
                 //   String url = CompanyURL + WebUrlClass.api_PostSavedata;
                  //  CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, null);

                    new StartSession(mContext, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            showProgressDialog();
                            UploadSave uploadSave = new UploadSave();
                            uploadSave.execute(FinalObj);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dismissProgressDialog();
                            ut.displayToast(getApplicationContext(), msg);
                        }
                    });


                }
            }
        });

        btn_SaveTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkEditText()) {
                    getTicketDetail();
                    final String Action = edt_Action.getText().toString();
                    String chagre = chargeAmount.getText().toString();
                    if (chagre.equalsIgnoreCase("")) {
                        chagre = "0";
                    }
                    final String effortdetailTime = edt_effertdetaildateset.getText().toString();
                    // FinalOutcome,
                    final String Result = edt_result.getText().toString();
                    final String effDetailHr;
                    final String effDetailto;
                    final String effDetailfrom;
                    final String timeFrom;
                    final String timeTo;
                    if (txt_effortdetailto.getVisibility() == View.VISIBLE &&
                            txt_effortdetailfrom.getVisibility() == View.VISIBLE) {
                        effDetailHr = txt_effortdetailhour.getText().toString();
                        effDetailto = txt_effortdetailto.getText().toString();
                        effDetailfrom = txt_effortdetailfrom.getText().toString();
                        timeFrom = "";
                        timeTo = "";
                    } else {
                        effDetailHr = txt_effortdetailhour.getText().toString();
                        effDetailto = "";
                        effDetailfrom = "";
                        timeFrom = timeSpinnerFrom;
                        timeTo = timeSpinnerTo;
                    }

                    final String remaindraction = edt_remaindraction.getText().toString();
                    final String remainderactiondateselect = edt_remainderactionsetdate.getText().toString();
                    String s = hdnfld1stpromisedt;
                    if (s.equalsIgnoreCase("")) {
                        hdnfld1stpromisedt = hdnfldcurntpromse;
                    }

                    final String finalChagre = chagre;

                    Solution = edt_solution.getText().toString();
                    PromiseTime=spinnere_promisetime.getText().toString();
                    hdnfld1stpromisedt=edt_promise.getText().toString();
                    hdnfldcurntpromse=edt_promise.getText().toString();
                    txtCurrentPromise=edt_promise.getText().toString();

                    JSONObject jsonObject = new JSONObject();


                    try {
                        jsonObject.put("txtActionPerform", Action);
                        jsonObject.put("txtEffortDate", effortdetailTime);
                        jsonObject.put("ddlOutcome", OutcomeID);
                        jsonObject.put("txtResult", Result);
                        jsonObject.put("txtcallfromFolwup", effDetailfrom);
                        jsonObject.put("txtcalltoFolwup", effDetailto);
                        jsonObject.put("txtFlowupHrs", effDetailHr);
                        jsonObject.put("txtRemindAction", remaindraction);
                        jsonObject.put("txtReminderDate", remainderactiondateselect);
                        jsonObject.put("txtReminderTime", "00:00");
                        jsonObject.put("ddlcallfromFolwup", timeFrom);
                        jsonObject.put("ddlcalltoFolwup", timeTo);
                        jsonObject.put("txtCurrentPromise", txtCurrentPromise);
                        jsonObject.put("hdnfldActivityId", ActivityId);
                        jsonObject.put("hdnfldUnitId", UnitId);
                        jsonObject.put("hdnfldActivityTypeId", ActivityTypeId);
                        jsonObject.put("hdnfldProjectId", ProjectId);
                        jsonObject.put("hdnfldIssuedTo", IssuedTo);
                        jsonObject.put("hdnfldIssuedToName", IssuedTo);
                        jsonObject.put("hdnfldPromiseCount", hdnfldPromiseCount);
                        jsonObject.put("hdnfldReportingToEmail", ReportingToEmail);
                        jsonObject.put("hdnfldReportingToName", ReportingToName);
                        jsonObject.put("lblTktNo", TicketNo);
                        jsonObject.put("lblTktDescrptn", TicketDiscription);
                        jsonObject.put("hdnfldSaveToDelegate", SaveToDelegate);
                        jsonObject.put("hdnfldReportedByEmail", ReportedByEmail);
                        jsonObject.put("lblCustomerName", ConsigneeName);
                        jsonObject.put("fuAttachment", Attachment);
                        jsonObject.put("hdnfld1stpromisedt", hdnfld1stpromisedt);
                        jsonObject.put("hdnfldcurntpromse", hdnfldcurntpromse);
                        jsonObject.put("lblReportedByName", lblReportedByEmail);
                        jsonObject.put("chkMailClient", MailClient);
                        jsonObject.put("hdnfldMailClient", MailClient);
                        jsonObject.put("lblProductName", ProductNAme);
                        jsonObject.put("ticketCategory", TicketCategory);
                        jsonObject.put("ActualCompletionDate", hdnfld1stpromisedt);
                        jsonObject.put("ActionMasterId", RootCause);
                        jsonObject.put("PriorityId", PriorityId);
                        jsonObject.put("ActualStartDate", Startdate);
                        jsonObject.put("Latitude", latitude);
                        jsonObject.put("Longitude", longitude);
                        jsonObject.put("Address", "");
                        jsonObject.put("MobileNo", mobile);
                        jsonObject.put("ServiceChargetoCust", finalChagre);
                        jsonObject.put("IsCheck", "N");
                        String a1 = "";
                        String a2 = "";
                        String a3 = "";
                        jsonObject.put("VersionMasterId", "");
                        jsonObject.put("InstallerId", "");
                        jsonObject.put("InstallDt", "");
                        jsonObject.put("Note", "");
                        jsonObject.put("ExtraRtCause", Solution);
                        jsonObject.put("PromiseTime",PromiseTime);


                        FinalObj = jsonObject.toString();
                        FinalObj = FinalObj.replaceAll("\\\\", "");
                        String remark = "Ticket update sucsessfully";
                      //  String url = CompanyURL + WebUrlClass.api_PostSavedata;
                     //   CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    new StartSession(mContext, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            showProgressDialog();
                            UploadSave save = new UploadSave();
                            save.execute(FinalObj);

                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                        }
                    });


                }
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                canceldata();
               finish();
            }
        });
        btn_effortdetaildateselect.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", (dayOfMonth));
                                edt_effertdetaildateset.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        edt_effertdetaildateset.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", (dayOfMonth));
                                edt_effertdetaildateset.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        btn_remainderactiondateselect.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_remainderactionsetdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        edt_promise.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_promise.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        img_promise.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_promise.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        edt_remainderactionsetdate.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewTicketMain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_remainderactionsetdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        // updateSpinnerRootCause();
        spinner_rootcause.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct ActionDesc,ActionMasterId" +
                        " FROM " + db.TABLE_REASON_OUTAGE +
                        " WHERE ActionDesc='" + spinner_rootcause.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        RootCause = cur.getString(cur.getColumnIndex("ActionMasterId"));

                    } while (cur.moveToNext());

                } else {
                    RootCause = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_finaloutcome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FinalOutcome = spinner_finaloutcome.getSelectedItem().toString();
                OutcomeID = getFinalOutComeID(FinalOutcome);
                if (OutcomeID.equals("39")){
                    spinnere_promisetime.setVisibility(View.VISIBLE);
                }
                else if (OutcomeID.equalsIgnoreCase("Permanent") || OutcomeID.equalsIgnoreCase("42")) {
                    latitude = lat;
                    longitude = lng;
                    Address = Loacationname;
                    OutcomeID = getFinalOutComeID(FinalOutcome);//40&38
                    edt_solution.setVisibility(View.VISIBLE);
                    spinnere_promisetime.setVisibility(View.GONE);
                } else {
                    edt_solution.setVisibility(View.GONE);
                    spinnere_promisetime.setVisibility(View.GONE);
                }
                // Toast.makeText(getApplicationContext(), FinalOutcome, Toast.LENGTH_LONG).show();
                OutcomeID = getFinalOutComeID(FinalOutcome);//40&38
               /* int ID = Integer.parseInt(OutcomeID);*/
               String ID = OutcomeID;

               //40- Development Required and 38 - transfer ticket.
                if (ID.equals("39")){
                    spinnere_promisetime.setVisibility(View.VISIBLE);
                }else if(ID.equalsIgnoreCase("40") || ID.equalsIgnoreCase("38")){
                   lin_resonfortranfer.setVisibility(View.VISIBLE);
                   lin_transferto.setVisibility(View.VISIBLE);
                   Autocomplettransferto.setVisibility(View.VISIBLE);
                 //  Autocomplettransferto.setText("");
                   edt_resonfortransfer.setVisibility(View.VISIBLE);
                   edt_resonfortransfer.setText("");
                   btn_SaveTransfer.setVisibility(View.GONE);
                    spinnere_promisetime.setVisibility(View.GONE);


                }else {

                   lin_resonfortranfer.setVisibility(View.GONE);
                   lin_transferto.setVisibility(View.GONE);
                   Autocomplettransferto.setVisibility(View.GONE);
                   edt_resonfortransfer.setVisibility(View.GONE);
                   btn_SaveTransfer.setVisibility(View.GONE);
                    spinnere_promisetime.setVisibility(View.GONE);

                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_effortdetailfrom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(ViewTicketMain.this)
/*
                        .setTitle("Select Countries")
*/
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                timeSpinnerFrom = ArrarlisttimeSpinnerFrom[position].toString();

                                String Diff = calculate_time_diff(timeSpinnerFrom, timeSpinnerTo);

                                if (Diff.contains("-")){
                                    Toast.makeText(ViewTicketMain.this,"From time should be less than To time ",Toast.LENGTH_SHORT).show();
                                }else {
                                    spinner_effortdetailfrom.setText(timeSpinnerFrom);
                                    txt_effortdetailhour.setText(Diff);
                                }

                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });//timeSpinnerTo

        spinner_effortdetailto.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                new AlertDialog.Builder(ViewTicketMain.this)
/*
                        .setTitle("Select Countries")
*/
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                timeSpinnerTo = ArrarlisttimeSpinnerFrom[position].toString();

                                String Diff = calculate_time_diff(timeSpinnerFrom, timeSpinnerTo);
                                if (Diff.contains("-")){
                                    Toast.makeText(ViewTicketMain.this,"To time should be greater than From time ",Toast.LENGTH_SHORT).show();
                                }else {
                                    spinner_effortdetailto.setText(timeSpinnerTo);
                                    txt_effortdetailhour.setText(Diff);

                                }



                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        spinnere_promisetime.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                new AlertDialog.Builder(ViewTicketMain.this)
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                PromiseTime = ArrarlisttimeSpinnerFrom[position].toString();

                                spinnere_promisetime.setText(PromiseTime);





                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });


        img_effortdetailstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_effortdetailto.setVisibility(View.GONE);
                spinner_effortdetailfrom.setVisibility(View.GONE);
                txt_effortdetailfrom.setVisibility(View.VISIBLE);
                txt_effortdetailto.setVisibility(View.VISIBLE);
                txt_effortdetailhour.setText("00:00");
                img_effortdetailpause.setVisibility(View.VISIBLE);
                img_effortdetailrestart.setVisibility(View.VISIBLE);
                Calendar cl = Calendar.getInstance();
                String currentTimeinstance = dateFormat.format(cl.getTime());
                txt_effortdetailfrom.setText(currentTimeinstance);
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);


            }
        });
        img_effortdetailpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_effortdetailfrom.getVisibility() == View.VISIBLE &&
                        txt_effortdetailto.getVisibility() == View.VISIBLE) {
                    timeSwapBuff += timeInMilliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    txt_effortdetailhour.setText("" + txt_effortdetailto.getText().toString());
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                    Calendar cl = Calendar.getInstance();
                    String currentTime;
                    currentTime = dateFormat1.format(cl.getTime());
                    txt_effortdetailto.setText(currentTime);
                    img_effortdetailpause.setVisibility(View.INVISIBLE);
                    img_effortdetailrestart.setVisibility(View.INVISIBLE);
                }
            }
        });
        img_effortdetailrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_effortdetailfrom.getVisibility() == View.VISIBLE &&
                        txt_effortdetailto.getVisibility() == View.VISIBLE) {
                    startTime = 0L;
                    timeInMilliseconds = 0L;
                    timeSwapBuff = 0L;
                    updatedTime = 0L;
                    customHandler.removeCallbacks(updateTimerThread);
                    txt_effortdetailfrom.setText("00:00");
                    txt_effortdetailto.setText("00:00");
                    txt_effortdetailhour.setText("00:00");
                }
            }
        });

        Autocomplettransferto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 User = parent.getSelectedItem().toString();
                 UserMasterID = getUserMAsterID(User);
                if (!(UserMasterID.equals(""))) {
                    if (UserMasterID.equalsIgnoreCase(LogInUserMasterId)) {
                        btn_SaveTransfer.setVisibility(View.VISIBLE);
                        btn_Save.setVisibility(View.GONE);
                    } else {
                        btn_SaveTransfer.setVisibility(View.GONE);
                        btn_Save.setVisibility(View.VISIBLE);
                    }
                } else {
                    btn_SaveTransfer.setVisibility(View.GONE);
                    btn_Save.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void CreateOfflineAssignActivity(String url, String parameter, int method, String remark, String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Ticket Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();

        }
    }

    private void canceldata() {
        edt_Action.setText("");
        edt_result.setText("");
        edt_remaindraction.setText("");
        btn_SaveTransfer.setVisibility(View.GONE);
        btn_Save.setVisibility(View.VISIBLE);
        spinner_effortdetailto.setVisibility(View.VISIBLE);
        spinner_effortdetailfrom.setVisibility(View.VISIBLE);
        txt_effortdetailfrom.setVisibility(View.GONE);
        txt_effortdetailto.setVisibility(View.GONE);
        txt_effortdetailhour.setText("00:00");
        img_effortdetailpause.setVisibility(View.VISIBLE);
        img_effortdetailrestart.setVisibility(View.VISIBLE);
        updateSpinnerFinalOutcome();
        //  updateSpinnerRootCause();
/*
        String EffortDate = dateFormatdate.format(new Date());
        // effortdetailTime=getDate1DD_MMM_YYYY(EffortDate);

        edt_effertdetaildateset.setText(EffortDate);

        String EffortDate1 = dateFormatdate.format(new Date());
        remainderactiondateselect = getDate1DD_MMM_YYYY2(EffortDate1);
        edt_remainderactionsetdate.setText(remainderactiondateselect);

*/

    }

    private String getUserMAsterID(String User) {
        String UserMAsterID = "";
      //  sql = db.getWritableDatabase();
        try {
            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ALL_MEMBERS + " WHERE UserName ='" + User + "'", null);
            int a = c.getCount();
            if (a > 0) {
                c.moveToFirst();
                do {
                    UserMAsterID = c.getString(c.getColumnIndex("UserMasterId"));
                } while (c.moveToNext());
            } else {
                UserMAsterID = "";
            }
        } catch (Exception e) {
            e.printStackTrace();

            UserMAsterID = "";
        }
        return UserMAsterID;
    }

    private void getAllMembers() {
        UnChkUser_list.clear();
       sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ALL_MEMBERS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                UnChkUser_list.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, UnChkUser_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Autocomplettransferto.setAdapter(adapter);
    }

    private String getFinalOutComeID(String data) {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BIND_FINAL_OUTCOME + " WHERE Outcome=?", new String[]{data});
        int a = c.getCount();
        String output = "";
        if (a > 0) {
            c.moveToFirst();
            do {
                output = c.getString(c.getColumnIndex("PKOutcomeId"));
            } while (c.moveToNext());
        }

        return output;
    }

    private void updateSpinnerFinalOutcome() {
        List<String> listBugFixing = getSpinnerFinalOutcome();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listBugFixing);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_finaloutcome.setAdapter(dataAdapter);
    }


    private List<String> getSpinnerFinalOutcome() {
        List<String> data = new ArrayList<String>();
        data.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_BIND_FINAL_OUTCOME, null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("Outcome")));
            } while (c.moveToNext());
        }
        c.close();
        sql.close();
        return data;
    }

    private void getTicketDetail() {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA_Vw + " WHERE ActivityId='" + ActivityId + "'", null);
        int a = c.getCount();
        if (a > 0) {
            c.moveToFirst();
            do {
                try {
                    String Id = ActivityId;
                    TicketNo = c.getString(c.getColumnIndex("ActivityCode"));
                    ActivityCode=c.getString(c.getColumnIndex("ActivityCode"));
                    UnitId = c.getString(c.getColumnIndex("UnitId"));
                    ActivityTypeId = c.getString(c.getColumnIndex("ActivityTypeId"));
                    IssuedTo = c.getString(c.getColumnIndex("IssuedTo"));
                    ReportingToEmail = c.getString(c.getColumnIndex("ReportingToEmail"));
                    ReportingToName = c.getString(c.getColumnIndex("ReportingToName"));
                    ActivityName = c.getString(c.getColumnIndex("ActivityName"));
                    TicketDiscription = c.getString(c.getColumnIndex("ActivityName"));
                    lblReportedByEmail = c.getString(c.getColumnIndex("ContMob"));
                    mobile = c.getString(c.getColumnIndex("Mobile"));
                    ReportedByEmail = c.getString(c.getColumnIndex("ReportedByEmail"));
                    ConsigneeName = c.getString(c.getColumnIndex("ConsigneeName"));
                    if (!(ConsigneeName.equalsIgnoreCase(""))) {
                        lin_Consignee.setVisibility(View.VISIBLE);
                        txt_customername.setText(ConsigneeName);
                    }
                    TicketCategory = c.getString(c.getColumnIndex("TicketCategory"));//ItemDesc,EndDate
                    ProductNAme = c.getString(c.getColumnIndex("ItemDesc"));
                    //  txtCurrentPromise = getDate1DD_MMM_YYYY(as);
                    PriorityId = c.getString(c.getColumnIndex("PriorityId"));
                  //  hdnfldcurntpromse = c.getString(c.getColumnIndex("EndDate1"));

                    //txtCurrentPromise = c.getString(c.getColumnIndex("EndDate1"));
                    //hdnfld1stpromisedt = ActualCompletionDate;
                    //edt_promise.setText(txtCurrentPromise);
                    ActualStartDate = c.getString(c.getColumnIndex("StartDate1"));
                    // ActualStartDate=getDate1DD_MMM_YYYY(StartDate);
                    ActualCompletionDate = c.getString(c.getColumnIndex("ActualCompletionDate1"));//DD/MM/YYYY
                    if (!ActualCompletionDate.equalsIgnoreCase("null") && !ActualCompletionDate.isEmpty()) {
                        Startdate = c.getString(c.getColumnIndex("ActualCompletionDate1"));
                        Startdate = ActualCompletionDate;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (c.moveToNext());

        }

    }

    public String getDateDD_MM_YYYY(String InputDate) {
        String Output = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date DOBDate = null;
        InputDate = InputDate.substring(InputDate.indexOf("(") + 1, InputDate.lastIndexOf(")"));
        long DOB_date = Long.parseLong(InputDate);
        DOBDate = new Date(DOB_date);
        Output = sdf.format(DOBDate);
        return Output;
    }

    public String getDate1DD_MMM_YYYY(String InputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat opsdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date DOBDate = null;

        try {
            DOBDate = sdf.parse(InputDate);
        } catch (Exception e) {
              e.printStackTrace();
        }
        String Output = opsdf.format(DOBDate);
        return Output;
    }
    public String getDate1DD_MMM_YYYY2(String InputDate) {
        String Output = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat opsdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date DOBDate = null;

        try {
            DOBDate = sdf.parse(InputDate);
        } catch (Exception e) {

        }
        Output = opsdf.format(DOBDate);
        return Output;
    }
    public String getDate1DD_MMM_YYYY1(String InputDate) {
        String Output = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat opsdf = new SimpleDateFormat("dd/MM/yyyy");
        Date DOBDate = null;

        try {
            DOBDate = sdf.parse(InputDate);
        } catch (Exception e) {

        }
        Output = opsdf.format(DOBDate);
        return Output;
    }
    private Boolean checkEditText() {
        if (!(edt_Action.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter All Field");
            return false;
        } else if (!(edt_result.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter Result");
            return false;
        }

        return true;
    }
   private String calculate_time_diff(String t1, String t2) {

        float min = 00;
        int hours = 00;
        int minutes = 00;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


            Date d1 = sdf.parse(t1);

            Date d2 = sdf.parse(t2);


            long diffMs = d2.getTime() - d1.getTime();
            int diffSec = (int) diffMs / 1000;
            min = diffSec / 60;
            /*if(min<60){

            }else {
                min = min / 60;
            }*/
            int sec = (int) diffSec % 60;

            final int MINUTES_IN_AN_HOUR = 60;
            final int SECONDS_IN_A_MINUTE = 60;

            int seconds = diffSec % SECONDS_IN_A_MINUTE;
            int totalMinutes = diffSec / SECONDS_IN_A_MINUTE;
            minutes = totalMinutes % MINUTES_IN_AN_HOUR;
            hours = totalMinutes / MINUTES_IN_AN_HOUR;

            Log.d("dialog_action", "dialog_action" + hours + ":" + minutes);
            Log.d("dialog_action", "dialog_action" + min);
            Log.d("dialog_action", "dialog_action" + sec);
            if (min < 0) {
                min = 00;
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String hr = hours + "";
        String mi = minutes + "";
        if (hr.length() == 1) {
            hr = 0 + hr;
        }
        if (mi.length() == 1) {
            mi = 0 + mi;
        }

        return hr + ":" + mi;


    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            String mi = mins + "";
            if (mi.length() == 1) {
                mi = 0 + mi;
            }
            txt_effortdetailto.setText(mi + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
        }

    };

    public Boolean CheckPresentToLocal() {
        sql = null;
        Cursor c = null;
        try {
            sql = db.getWritableDatabase();
            c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA + " WHERE ActivityId='" + ActivityId + "'", null);
            if (c.getCount() > 0) {
                c.close();

                return true;
            } else {
                c.close();

                return false;
            }
        } catch (Exception e) {


            return false;
        }
    }

    public String setup(String vname) {
        sql = db.getWritableDatabase();
        Cursor cursor1 = sql.rawQuery("Select distinct Key,value from "
                + db.TABLE_Setup_TICKET_UPDATION + " where Key='" + vname + "'", null);
        Log.d("test", "" + cursor1.getCount());

        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            key = cursor1.getString(cursor1.getColumnIndex("value"));

            return key;

        } else {
            key = "";

            return key;
        }

    }

    private void showProgressDialog() {


        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {

        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    private class DownloadFinalOutcome extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getFinalOutcome + "?ActivityId=" + ActivityId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("PKOutcomeId")) {
                sql = db.getReadableDatabase();
                try {
                    Log.e("Responce Final Outcome", "PKOutcomeId");
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("Parsed data :", "jResults :=" + jsonArray);
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);

                    sql.delete(db.TABLE_BIND_FINAL_OUTCOME, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BIND_FINAL_OUTCOME, null);
                    int count = c.getCount();
                    ContentValues Container = new ContentValues();
                    String columnName, columnValue;
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jstring = jsonArray.getJSONObject(a);
                        for (int i = 0; i < c.getColumnCount(); i++) {
                            columnName = c.getColumnName(i);
                            columnValue = jstring.getString(columnName);
                            Container.put(columnName, columnValue);
                            Log.e("Count values ...",
                                    " count a: " + a + "  i:" + i);
                        }
                        long aa = sql.insert(db.TABLE_BIND_FINAL_OUTCOME, null, Container);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

                updateSpinnerFinalOutcome();


                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        ticketUpdateData = new DownloadTicketUpdateData();
                        ticketUpdateData.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });


            } else {
                ut.displayToast(mContext, "Server Error...");
            }
        }
    }

    private class DownloadTicketUpdateData extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getTicketUpdationData + "?ActivityId=" + ActivityId + "&ClientId=" + SourceId;

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("UnitId")) {
                sql = db.getReadableDatabase();
                try {
                    Log.e("Responce Final Outcome", "UnitId");
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("Parsed data :", "jResults :=" + jsonArray);
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                    sql.delete(db.TABLE_TICKET_UPDATION_DATA_Vw, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA_Vw, null);
                    int count = c.getCount();
                    ContentValues Container = new ContentValues();
                    String columnName, columnValue;
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jstring = jsonArray.getJSONObject(a);
                        for (int i = 0; i < c.getColumnCount(); i++) {
                            columnName = c.getColumnName(i);
                            columnValue = jstring.getString(columnName);
                            Container.put(columnName, columnValue);
                            Log.e("Count values ...",
                                    " count a: " + a + "  i:" + i);
                        }
                        long aa = sql.insert(db.TABLE_TICKET_UPDATION_DATA_Vw, null, Container);
                        Log.e("Response...", "Count" + aa);
                    }

                } catch (Exception e) {
                    e.printStackTrace();


                }

                getTicketDetail_1();

            } else {
                ut.displayToast(mContext, "Server Error...");

            }


        }
    }

    private class DownloadPromisCount extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getPromisCount + "?ActivityId=" + ActivityId;

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("cnt")) {
                sql = db.getReadableDatabase();
                try {
                    Log.e("Responce Final Outcome", "UnitId");
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("Parsed data :", "jResults :=" + jsonArray);
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);

                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jstring = jsonArray.getJSONObject(a);
                        hdnfldPromiseCount = jstring.getString("cnt");
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


            } else {
                ut.displayToast(mContext, "Server Error...");

            }


        }
    }

    private class DownloadBindAction extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getBindAction;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("cnt")) {
                sql = db.getReadableDatabase();
                try {
                    Log.e("Responce Final Outcome", "UnitId");
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d("Parsed data :", "jResults :=" + jsonArray);
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);

                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jstring = jsonArray.getJSONObject(a);
                        hdnfldPromiseCount = jstring.getString("cnt");

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }


            } else {
                ut.displayToast(mContext, "Server Error...");
            }

        }


    }

    private class UploadSave extends AsyncTask<String, String, String> {
        Object res;
        String response, url;

//        NoMail

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = CompanyURL + WebUrlClass.api_PostSavedata;
                res = ut.OpenPostConnection(url, params[0], ViewTicketMain.this);

                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();//OK
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();

            if (s != null) {
                if (s.contains("OK")) {
                    if (lin_resonfortranfer.getVisibility() == View.VISIBLE ||
                            lin_transferto.getVisibility() == View.VISIBLE) {
                        new StartSession(mContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                final String Reason = edt_resonfortransfer.getText().toString();
                                final String Transferto = User;
                                String UserMasterID = getUserMAsterID(Transferto);
                                String s = hdnfld1stpromisedt;
                                if (s.equalsIgnoreCase("")) {
                                    hdnfld1stpromisedt = hdnfldcurntpromse;
                                }
                                hdnfld1stpromisedt = getDate1DD_MMM_YYYY(hdnfld1stpromisedt);
                                String hdnfldcurntpromse=edt_remainderactionsetdate.getText().toString();
                                String hdnfldcurntpromse1=getDate1DD_MMM_YYYY1(hdnfldcurntpromse);


                                final String timeFrom;
                                final String timeTo;
                                if (!(UserMasterID.equalsIgnoreCase(""))) {
                                    showProgressDialog();
                                    uploadTransfer = new UploadTransfer();
                                    uploadTransfer.execute(Reason, UserMasterID, hdnfld1stpromisedt, hdnfldcurntpromse1, TicketDiscription, TicketNo, ConsigneeName, ActivityId);
                                } else {
                                    ut.displayToast(getApplicationContext(), "Can't Identify transferto user");
                                }
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(getApplicationContext(), msg);
                            }
                        });
                    } else {
                        //canceldata();
                        ut.displayToast(mContext, "Data Save Successfully");
                        SQLiteDatabase sql = db.getWritableDatabase();
                        sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                        sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});

                        Cursor c = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
                        int a1 = c.getCount();
                        Intent i = new Intent(ViewTicketMain.this, ActivityMain.class);
                        startActivity(i);
                        finish();

                    }

                } else if (s.contains("You have already filled the time slot")) {
                    ut.displayToast(mContext, "You have already filled the time slot");

                } else if (s.contains("You can not fill timesheet greater than current time!!!")) {
                    ut.displayToast(mContext, "You can not fill timesheet greater than current time!!!");

                } else {
                    ut.displayToast(mContext, "Data Saved Successfully");
                    finish();


                }
            }else {
                if (s==null) {
                    ut.displayToast(mContext, "Data Save Successfully");
                    SQLiteDatabase sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
                    Cursor c = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
                    int a1 = c.getCount();
                    Intent i = new Intent(ViewTicketMain.this, ActivityMain.class);
                    startActivity(i);
                    finish();
                }
            }
        }

    }


    private class UploadTransfer extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getsaveactivitytransferd + "?Reason=" + params[0] + "&hftxtTransferTo="
                        + params[1] + "&hdnfld1stpromisedt=" + params[2] + "&txtCurrentPromise=" + params[3] +
                        "&ConcateTktDescrptn=" + URLEncoder.encode(params[4],"UTF-8") + "&lblTktNo=" + params[5] + "&lblCustomerName=" + URLEncoder.encode(params[6],"UTF-8")  +
                        "&hdnfldActivityId=" + params[7];

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");//"true"
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();//OK
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("true")) {
                sql = db.getWritableDatabase();
                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
                ut.displayToast(mContext, "Activity Transfered successfully");
                finish();

            } else if (s.contains("You have already filled the time slot")) {
                ut.displayToast(mContext, "You have already filled the time slot");

            } else {
                ut.displayToast(mContext, "Ticket can not transfer");

            }
        }
    }

    class DownloadDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response, name, id;
        String a[], b[];

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + "/Ekatm/getresx";

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString();
                JSONArray jResults = new JSONArray(response);
                ContentValues values = new ContentValues();
                sql = db.getWritableDatabase();
                sql.delete(db.TABLE_Setup_TICKET_UPDATION, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Setup_TICKET_UPDATION, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Setup_TICKET_UPDATION, null, values);

                }
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("Error")) {
                getTicketDetail();
         /*       txt_lblAction.setText("Action");
                txt_lblEffortDetail.setText("Effort Details");
                txt_lblFinalOutcome.setText("Action ");
            } else {
                if (!(setup(txt_lblAction.getTag().toString()).equalsIgnoreCase(""))) {
                    txt_lblAction.setText("Action");
                } else {
                    txt_lblAction.setText("Action");
                }
                if (!(setup(txt_lblEffortDetail.getTag().toString()).equalsIgnoreCase(""))) {
                    txt_lblEffortDetail.setText(setup(txt_lblEffortDetail.getTag().toString()));
                } else {
                    txt_lblEffortDetail.setText("Effort Details ");
                }
                if (!(setup(txt_lblFinalOutcome.getTag().toString()).equalsIgnoreCase(""))) {
                    txt_lblFinalOutcome.setText(setup(txt_lblFinalOutcome.getTag().toString()));
                } else {
                    txt_lblFinalOutcome.setText("Final Outcome");
                }
*/

            }else {
                getTicketDetail();

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.click) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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

    /*  class PostImageUpload extends AsyncTask<String, Void, String> {
          Object res;
          String response;


          @Override
          protected void onPreExecute() {
              super.onPreExecute();
              showProgressDialog();
          }

          @Override
          protected String doInBackground(String... params) {
              String url = CompanyURL + WebUrlClass.api_post_travel_plan;
              try {
                  res = ut.OpenPostConnection(url, params[0]);
                  response = res.toString().replaceAll("\\\\", "");
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
              dismissProgressDialog();

              if (response != null) {
                  Toast.makeText(ViewTicketMain.this, "Upload succcessfully ", Toast.LENGTH_LONG).show();
              }
              onBackPressed();
          }

      }
  */
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        updateUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        mGoogleApiClient.isConnected();
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        if (null != mCurrentLocation) {
            lat = String.valueOf(mCurrentLocation.getLatitude());
            lng = String.valueOf(mCurrentLocation.getLongitude());
           /* Toast.makeText(ViewTicketMain.this,
                    "Latitude: " + lat + "\n" +
                            "Longitude: " + lng + "\n", Toast.LENGTH_SHORT).show();*/
            try {
                Geocoder gcd = new Geocoder(ViewTicketMain.this, Locale.getDefault());
                List<Address> addresses = gcd.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Loacationname = addresses.get(0).getLocality();


                } else {
                    // do your staff
                }

            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }


    private Bitmap getImageFromPath(Uri selectedImage) {

        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        // Get the cursor
        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        Bitmap bitmap = BitmapFactory
                .decodeFile(imgDecodableString);

        return bitmap;
    }

    private File bitmapToFile(Bitmap bitmap, String fileName) {
        File filesDir = getApplicationContext().getFilesDir();
        imageFile = new File(filesDir, fileName + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            return imageFile;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return null;

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(context, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        //  TravelPlanAddActivity.this.finish();
    }


    class DownloadReasonOutageJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getBindAction;

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_REASON_OUTAGE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_REASON_OUTAGE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_REASON_OUTAGE, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
         /*   if (progressHUD1 != null && progressHUD1.isShowing()) {
                progressHUD1.dismiss();
            }*/
            dismissProgressDialog();
            if (response.contains("")) {

            }
            getState();
        }

    }

    private void getState() {
//sReferenceType, sReference,sEntity,sConsignee;
        lstReason_Outage.clear();
        String query = "SELECT distinct ActionMasterId,ActionDesc" +
                " FROM " + db.TABLE_REASON_OUTAGE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstReason_Outage.add(cur.getString(cur.getColumnIndex("ActionDesc")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(ViewTicketMain.this,
                R.layout.vwb_custom_spinner_txt, lstReason_Outage);
        spinner_rootcause.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_rootcause.setSelection(0);
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

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
    class DownloadUserlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUnChkUser_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
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
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();

            if (res!=null){
                getAllMembers();
            }



        }
    }

    private void getTicketDetail_1() {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA_Vw + " WHERE ActivityId='" + ActivityId + "'", null);
        int a = c.getCount();
        if (a > 0) {
            c.moveToFirst();
            do {
                try {
                    String Id = ActivityId;
                    TicketNo = c.getString(c.getColumnIndex("ActivityCode"));
                    ActivityCode=c.getString(c.getColumnIndex("ActivityCode"));
                    UnitId = c.getString(c.getColumnIndex("UnitId"));
                    ActivityTypeId = c.getString(c.getColumnIndex("ActivityTypeId"));
                    IssuedTo = c.getString(c.getColumnIndex("IssuedTo"));
                    ReportingToEmail = c.getString(c.getColumnIndex("ReportingToEmail"));
                    ReportingToName = c.getString(c.getColumnIndex("ReportingToName"));
                    ActivityName = c.getString(c.getColumnIndex("ActivityName"));
                    TicketDiscription = c.getString(c.getColumnIndex("ActivityName"));
                    lblReportedByEmail = c.getString(c.getColumnIndex("ContMob"));
                    mobile = c.getString(c.getColumnIndex("Mobile"));
                    ReportedByEmail = c.getString(c.getColumnIndex("ReportedByEmail"));
                    ConsigneeName = c.getString(c.getColumnIndex("ConsigneeName"));
                    if (!(ConsigneeName.equalsIgnoreCase(""))) {
                        lin_Consignee.setVisibility(View.VISIBLE);
                        txt_customername.setText(ConsigneeName);
                    }
                    TicketCategory = c.getString(c.getColumnIndex("TicketCategory"));//ItemDesc,EndDate
                    ProductNAme = c.getString(c.getColumnIndex("ItemDesc"));
                    //  txtCurrentPromise = getDate1DD_MMM_YYYY(as);
                    PriorityId = c.getString(c.getColumnIndex("PriorityId"));
                    //  hdnfldcurntpromse = c.getString(c.getColumnIndex("EndDate1"));

                    txtCurrentPromise = c.getString(c.getColumnIndex("EndDate1"));
                    txtCurrentPromise= getDate_YYYY_MM_DD(txtCurrentPromise);
                    //hdnfld1stpromisedt = ActualCompletionDate;
                    edt_promise.setText(txtCurrentPromise);
                    ActualStartDate = c.getString(c.getColumnIndex("StartDate1"));
                    // ActualStartDate=getDate1DD_MMM_YYYY(StartDate);
                    ActualCompletionDate = c.getString(c.getColumnIndex("ActualCompletionDate1"));//DD/MM/YYYY
                    if (!ActualCompletionDate.equalsIgnoreCase("null") && !ActualCompletionDate.isEmpty()) {
                        Startdate = c.getString(c.getColumnIndex("ActualCompletionDate1"));
                        Startdate = ActualCompletionDate;
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (c.moveToNext());

        }

    }

    public String getDate_YYYY_MM_DD(String InputDate) {
        String Output = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat opsdf = new SimpleDateFormat("yyyy-MM-dd");
        Date DOBDate = null;

        try {
            DOBDate = sdf.parse(InputDate);
        } catch (Exception e) {

        }
        Output = opsdf.format(DOBDate);
        return Output;
    }

    class DownloadCustomerData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetCustomerDetails+"?"+"cid="+SourceId;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
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
            dismissProgressDialog();

            if (res!=null){
                try {
                    txt_customerinfo.setVisibility(View.VISIBLE);
                    JSONArray jResults = new JSONArray(response);
                    for (int i=0;i<jResults.length();i++){
                        JSONObject jsonObject=jResults.getJSONObject(i);

                        txt_customerinfo.setText(jsonObject.getString("ConsigneeName")
                                + " - " +jsonObject.getString("Address")
                                + " - " +jsonObject.getString("City")
                                + " - " +jsonObject.getString("Mobile"));

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



        }
    }

}
