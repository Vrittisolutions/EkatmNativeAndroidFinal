package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.graphics.Color;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AdapterAffectedCustomer;
import com.vritti.vwb.Beans.AffectedCustomer;
import com.vritti.vwb.Beans.MaterialAddBean;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sharvari on 10-Jul-17.
 */

public class TicketUpdateDNAActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    // TICKET UPDATE CODE 
    static private Context mContext;
    String effDetailHr = "";
    String effDetailto = "";
    String effDetailfrom = "";
    String timeFrom = "";
    String timeTo = "";
    String ActualStartDate = "";

    SharedPreferences userpreferences;
    SharedPreferences Sharedpreferenceticketsetting;

    String Result = "", ProjectId = "", SaveToDelegate = "", Attachment = "", MailClient = "No Mail", Action = "", effortdetailTime = "", remaindraction = "", remainderactiondateselect = "";
    private String timeSpinnerFrom = "", ActivityId = "", SourceId, FinalOutcome = "", RootCause = "", timeSpinnerTo = "", LogInUserMasterId = "", section = "Section",
            UnitId = "",
            ActivityTypeId = "",
            IssuedTo = "",
            ItemMasterId = "",
            ReportingToEmail = "",
            ReportingToName = "", ActivityName,
            ReportedByEmail = "",
            ConsigneeName = "",
            TicketCategory = "", TicketDiscription = "",
            TicketNo = "", ProductNAme = "", txtCurrentPromise = "", Startdate = "",
            hdnfldPromiseCount = "", hdnfldSaveToDelegate,
            hdnfld1stpromisedt = "", hdnfldcurntpromse = "", chkMailClient, hdnfldMailClient, lblReportedByEmail = "";
    private Button btn_Save, btn_Cancel,
            btn_SaveTransfer;
    private EditText edt_Action, edt_effertdetaildateset, edt_result,
            edt_resonfortransfer, edt_remaindraction,
            edt_remainderactionsetdate, edt_hourremainderaction;
    private TextView txt_effortdetailfrom, txt_effortdetailto,
            txt_effortdetailhour, txt_customername, txt_lblAction,
            txt_lblEffortDetail, textView2,
            txt_lblFinalOutcome;
    private ImageView img_effortdetailstart, btn_remainderactiondateselect, img_effortdetailpause, img_effortdetailrestart, btn_effortdetaildateselect;
    private Spinner spinner_rootcause, spinner_finaloutcome;

    TextView spinner_effortdetailfrom, spinner_effortdetailto, txt_affect_customer;

    private AutoCompleteTextView Autocomplettransferto;
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
    SimpleDateFormat dateFormatdate;
    //ProgressBar progressbar;

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
    int current_time;
    List<String> lstReason_Outage = new ArrayList<String>();
    private String lat = "", lng = "";
    private String latitude = "", longitude = "", Address = "", Mobile = "", lblTktDescrptn = "";
    private String ActualCompletionDate = "";
    String OutcomeID, PKActAffectedCust, UpdadteticketDatetime, IndividualTicket;
    ProgressDialog progressDialog;

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
            "18:00", "18:05", "18:10", "18:15", "18:20", "18:25", "18:30", "18:35", "18:40", "18:45", "18:50", "18:55"};
    ArrayAdapter<String> spinner_fromtime;
    ArrayList<AffectedCustomer> affectedCustomers = new ArrayList<>();
    List<String> Getbindcategorylist = new ArrayList<String>();
    AdapterAffectedCustomer adapterAffectedCustomer;

    LinearLayout listview_aff_cust_info;
    AlertDialog.Builder builder;
    AlertDialog alertDialog, alertDialog1;
    private View convertView;
    TextView txt_ticket_no1, txt_client_name, txt_reported_by, txt_resolve;
    int i;
    private ArrayList<MaterialAddBean> listMaterialAdd;


    //Ticket Details Code

    TextView txt_ticket_no, txt_reportedby_name, txt_assigned_name, txt_resolution_time, txt_priority_level, txt_update;
    Spinner spinner_from, spinner_to, spinner_fiber_type;
    ImageView img_edit;

    EditText txt_activity_name;
    String Contact_mobile, Activity_name, Activity_code, Assigned, Resolution_time, PriorityId;
    static SQLiteDatabase sql;
    String WarrantyCode, PKRouteMasterId = "", ItemPlantId, PKRouteMasterId1, RouteJson,Material_ItemPlantId;
    List<String> Routefromlist = new ArrayList<String>();
    List<String> RouteTolist = new ArrayList<String>();
    ProgressBar progress_section_to;
    Button btnsave;
    private String FinalObj;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    Button btn_solution, btn_details, btn_material;
    FrameLayout container;
    LayoutInflater layoutInflater;
    View view_ticket_update, view_ticket_details, view_ticket_material;
    TextView txt_tckname, txt_mail, Txt_mobno;
    LinearLayout mLin_mail;

    SearchableSpinner Spimmer_material_warehouse, Spimmer_material_location,
            Spimmer_material_code, Spimmer_material_discription;

    EditText Edittext_maerial_fieldname, Edittext_maerial_Toatalquant,edt_material_stk_qty,
            Edittext_maerial_consumedQuant, Edittext_maerial_remar;
    TextView Txt_Mat_setting;
    Button Btn_material_add;
    ListView lst_material_listview;
    ArrayList<String> listMID = new ArrayList<String>();
    static String warehousename, warehouseID, Material_locationID, Material_locationmane, materialcode, materialdis, materialItemplantID;
    static int warehousepos,Material_locationpos;
    LinearLayout mLinContainer;
    static String Material_UOM, Material_Quant;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_ticket_button_lay_adjust);


        Sharedpreferenceticketsetting = getSharedPreferences(WebUrlClass.SHARED_TICKET_UPDATE_MATERIAL_SETTING,
                Context.MODE_PRIVATE);


        btn_Save = (Button) findViewById(R.id.btnsave);
        btn_Cancel = (Button) findViewById(R.id.btncancel);
        btn_details = (Button) findViewById(R.id.btn_details);
        btn_solution = (Button) findViewById(R.id.btn_solution);
        btn_material = findViewById(R.id.btn_material);
        txt_tckname = findViewById(R.id.txt_tcknane);
        txt_mail = findViewById(R.id.tck_email);
        Txt_mobno = findViewById(R.id.tck_mob_no);
        mLin_mail = findViewById(R.id.lin_mail);
        txt_affect_customer = (TextView) findViewById(R.id.txt_affect_customer);//txt_tcknane  , lin_mail , tck_email , tck_mob_no

        container = (FrameLayout) findViewById(R.id.container);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view_ticket_update = layoutInflater.inflate(R.layout.vwb_fragment_view_ticket_main, container, false);
        view_ticket_details = layoutInflater.inflate(R.layout.vwb_ticket_details_lay, container, false);
        view_ticket_material = layoutInflater.inflate(R.layout.vwb_ticket_material_lay, container, false);
        container.addView(view_ticket_update);

        mContext = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(mContext);
        String settingKey = ut.getSharedPreference_SettingKey(mContext);
        String dabasename = ut.getValue(mContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(mContext, dabasename);
        CompanyURL = ut.getValue(mContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(mContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(mContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(mContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(mContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(mContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(mContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        LogInUserMasterId =  ut.getValue(mContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        initObject();
        initTicket_update_View();
        initTicket_details_View();
        initTicket_material_View();
        setListner();
        getAllMembers();

        if (!isGooglePlayServicesAvailable()) {
            TicketUpdateDNAActivity.this.finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(TicketUpdateDNAActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(TicketUpdateDNAActivity.this)
                .addOnConnectionFailedListener(this)
                .build();

        //Image upload code


        final String[] items = new String[]{"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TicketUpdateDNAActivity.this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(TicketUpdateDNAActivity.this);

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


            if (!(setup(txt_lblAction.getTag().toString()).equalsIgnoreCase(""))) {
                txt_lblAction.setText("Action");
            } else {
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


        if (cf.getStatecount() > 0) {
            getState();
        } else {
            if (isnet()) {
                new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
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
            new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
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
                getTicketDetail();
                getTicketDetails();
            } else {

                new StartSession(mContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        ticketUpdateData = new DownloadTicketUpdateData();
                        ticketUpdateData.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(TicketUpdateDNAActivity.this, msg);
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
                        ut.displayToast(TicketUpdateDNAActivity.this, msg);
                    }
                });
            } else {
                ut.displayToast(mContext, "No Internet Connection");
            }


        }

        if (ut.isNet(mContext)) {

            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    GetChkIsClientAffected getChkIsClientAffected = new GetChkIsClientAffected();
                    getChkIsClientAffected.execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(TicketUpdateDNAActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(mContext, "No Internet Connection");
        }


        if (ut.isNet(mContext)) {

            new StartSession(mContext, new CallbackInterface() {
                @Override
                public void callMethod() {
                    GetChkIsClientAffectedDetailsList getChkIsClientAffectedDetailsList = new GetChkIsClientAffectedDetailsList();
                    getChkIsClientAffectedDetailsList.execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(TicketUpdateDNAActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(mContext, "No Internet Connection");
        }

        if (cf.getbindcategorycount() > 0) {
            getGetBindcategory();
        } else {
            if (isnet()) {
                new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadGetBindTktCategryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        btn_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(view_ticket_details);//
                container.removeView(view_ticket_material);
                container.removeView(view_ticket_update);
                container.addView(view_ticket_update);

            }
        });
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(view_ticket_material);
                container.removeView(view_ticket_details);
                container.removeView(view_ticket_update);
                container.addView(view_ticket_details);

            }
        });
        btn_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.removeView(view_ticket_update);
                container.removeView(view_ticket_details);
                container.removeView(view_ticket_material);
                container.addView(view_ticket_material);
            }
        });

    }


    private void initObject() {
        Intent intent = getIntent();
        ActivityId = intent.getStringExtra("ActivityId");
        SourceId = intent.getStringExtra("SourceId");
        ProjectId = intent.getStringExtra("ProjectID");

        customHandler = new Handler();
        UnChkUser_list = new ArrayList<String>();
        dateFormat = new SimpleDateFormat("HH:mm");
        dateFormatdate = new SimpleDateFormat("yyyy/MM/dd");
        listMaterialAdd = new ArrayList<MaterialAddBean>();

       /* Calendar c = Calendar.getInstance();

        timeSpinnerFrom= String.valueOf(c.getTime());

        timeSpinnerFrom.replace("IST 2017","");
*/

    }

    private void initTicket_update_View() {
        btn_effortdetaildateselect = (ImageView) view_ticket_update.findViewById(R.id.buttoneffertdetaildateselect);
        btn_remainderactiondateselect = (ImageView) view_ticket_update.findViewById(R.id.btnremainderactiondateselect);//btnsavetransfer
        btn_SaveTransfer = (Button) view_ticket_update.findViewById(R.id.btnsavetransfer);
        edt_Action = (EditText) view_ticket_update.findViewById(R.id.editAction);
        edt_effertdetaildateset = (EditText) view_ticket_update.findViewById(R.id.editTexteffertdetaildateset);
        edt_effertdetaildateset.setText(dateFormatdate.format(new Date()));
        edt_result = (EditText) view_ticket_update.findViewById(R.id.edtresult);
        edt_resonfortransfer = (EditText) view_ticket_update.findViewById(R.id.edtresonfortransfer);
        edt_remaindraction = (EditText) view_ticket_update.findViewById(R.id.remaindraction);
        edt_remainderactionsetdate = (EditText) view_ticket_update.findViewById(R.id.edtremainderactionsetdate);
        edt_remainderactionsetdate.setText(dateFormatdate.format(new Date()));
        edt_hourremainderaction = (EditText) view_ticket_update.findViewById(R.id.edthourremainderaction);
        txt_effortdetailfrom = (TextView) view_ticket_update.findViewById(R.id.edteffortdetailfrom);
        textView2 = (TextView) view_ticket_update.findViewById(R.id.textView2);
        txt_effortdetailto = (TextView) view_ticket_update.findViewById(R.id.edteffortdetailto);
        txt_effortdetailhour = (TextView) view_ticket_update.findViewById(R.id.edteffortdetailhour);
        txt_customername = (TextView) view_ticket_update.findViewById(R.id.consinee);
        img_effortdetailstart = (ImageView) view_ticket_update.findViewById(R.id.imgeffortdetailstart);
        img_effortdetailpause = (ImageView) view_ticket_update.findViewById(R.id.imgeffortdetailpause);
        img_effortdetailrestart = (ImageView) view_ticket_update.findViewById(R.id.imgeffortdetailrestart);
        spinner_finaloutcome = (Spinner) view_ticket_update.findViewById(R.id.spinnerfinaloutcome);
        spinner_rootcause = (Spinner) view_ticket_update.findViewById(R.id.spinnerrootCause);
        spinner_effortdetailfrom = (TextView) view_ticket_update.findViewById(R.id.spinnereffortdetailfrom);
        spinner_effortdetailto = (TextView) view_ticket_update.findViewById(R.id.spinnereffortdetailto);
        Autocomplettransferto = (AutoCompleteTextView) view_ticket_update.findViewById(R.id.Autocomplettransferto);
        lin_transferto = (LinearLayout) view_ticket_update.findViewById(R.id.lintransferto);
        lin_resonfortranfer = (LinearLayout) view_ticket_update.findViewById(R.id.linresonfortranfer);
        lin_Consignee = (LinearLayout) view_ticket_update.findViewById(R.id.linconsinee);
        checkboxmail = (CheckBox) view_ticket_update.findViewById(R.id.checkboxmail);
        txt_lblAction = (TextView) view_ticket_update.findViewById(R.id.lblAction);
        txt_lblEffortDetail = (TextView) view_ticket_update.findViewById(R.id.lblResolDt);
        txt_lblFinalOutcome = (TextView) view_ticket_update.findViewById(R.id.lblFinalOut);
        // progressbar = (ProgressBar) view_ticket_update.findViewById(R.id.progressbar);

        spinner_rootcause.setSelection(0);


        if (CompanyURL.equals("http://www.appnet.co.in")) {
            img_effortdetailrestart.setVisibility(View.GONE);
            img_effortdetailpause.setVisibility(View.GONE);
        }


        Date date2 = new Date();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        timeSpinnerFrom = dateFormat.format(date2);
        timeSpinnerTo = dateFormat.format(date2);

        spinner_effortdetailfrom.setText(timeSpinnerFrom);
        spinner_effortdetailto.setText(timeSpinnerFrom);
        spinner_fromtime = new ArrayAdapter<String>(TicketUpdateDNAActivity.this, android.R.layout.simple_spinner_dropdown_item, ArrarlisttimeSpinnerFrom);


    }

    private void initTicket_details_View() {
        txt_ticket_no = (TextView) view_ticket_details.findViewById(R.id.txt_ticket_no);
        txt_activity_name = (EditText) view_ticket_details.findViewById(R.id.txt_activity_name);
        txt_reportedby_name = (TextView) view_ticket_details.findViewById(R.id.txt_reportedby_name);
        txt_assigned_name = (TextView) view_ticket_details.findViewById(R.id.txt_assigned_name);
        txt_resolution_time = (TextView) view_ticket_details.findViewById(R.id.txt_resolution_time);
        txt_priority_level = (TextView) view_ticket_details.findViewById(R.id.txt_priority_level);
        spinner_fiber_type = (Spinner) view_ticket_details.findViewById(R.id.spinner_fiber_type);
        spinner_from = (Spinner) view_ticket_details.findViewById(R.id.spinner_from);
        spinner_to = (Spinner) view_ticket_details.findViewById(R.id.spinner_to);
        progress_section_to = (ProgressBar) view_ticket_details.findViewById(R.id.progress_section_to);
        btnsave = (Button) view_ticket_details.findViewById(R.id.btnsave);
        txt_update = (TextView) view_ticket_details.findViewById(R.id.txt_update);
        img_edit = (ImageView) view_ticket_details.findViewById(R.id.img_edit);

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String query = "SELECT distinct PKRouteMasterId,Point,ItemPlantId" +
                        " FROM " + db.TABLE_GetRouteFrom +
                        " WHERE Point='" + spinner_from.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                // lstReference.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        PKRouteMasterId = cur.getString(cur.getColumnIndex("PKRouteMasterId"));
                        ItemPlantId = cur.getString(cur.getColumnIndex("ItemPlantId"));


                    } while (cur.moveToNext());

                } else {
                    PKRouteMasterId = "";
                }


                if (isnet()) {
                    new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetChangeRouteTo().execute();
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

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_activity_name.setEnabled(true);
            }
        });

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct PKRouteMasterId,Point,ItemPlantId" +
                        " FROM " + db.TABLE_GetRouteTo +
                        " WHERE Point='" + spinner_to.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                // lstReference.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        PKRouteMasterId1 = cur.getString(cur.getColumnIndex("PKRouteMasterId"));
                        //    ItemPlantId = cur.getString(cur.getColumnIndex("ItemPlantId"));

                    } while (cur.moveToNext());

                } else {
                    PKRouteMasterId1 = "";
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsondetailsupdate = new JSONObject();


                try {

                    jsondetailsupdate.put("fromRouteMstrId", PKRouteMasterId);
                    jsondetailsupdate.put("toRouteMstrId", PKRouteMasterId1);
                    jsondetailsupdate.put("AAMActId", ActivityId);

                    RouteJson = jsondetailsupdate.toString();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                RouteJson = RouteJson.replaceAll("\\\\", "");
                if (isnet()) {
                    new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostRouteUpdateJSON().execute(RouteJson);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(TicketUpdateDNAActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


    }

    private void initTicket_material_View() {
        Spimmer_material_warehouse = view_ticket_material.findViewById(R.id.spinner_warehouse);
        Spimmer_material_location = view_ticket_material.findViewById(R.id.spinner_Location);
        Spimmer_material_code = view_ticket_material.findViewById(R.id.spinner_code);
        Spimmer_material_discription = view_ticket_material.findViewById(R.id.spinner_Discription);
        Edittext_maerial_fieldname = view_ticket_material.findViewById(R.id.edt_material_Fileldname);
        Edittext_maerial_Toatalquant = view_ticket_material.findViewById(R.id.edtmaterial_total_quantity);
        Edittext_maerial_consumedQuant = view_ticket_material.findViewById(R.id.edtmaterial_consumeduant);
        Edittext_maerial_remar = view_ticket_material.findViewById(R.id.edt_material_Remark);
        edt_material_stk_qty = view_ticket_material.findViewById(R.id.edt_material_stk_qty);
        Btn_material_add = view_ticket_material.findViewById(R.id.txt_material_add);
        Txt_Mat_setting = view_ticket_material.findViewById(R.id.txt_mat_Setting);
        // lst_material_listview = view_ticket_material.findViewById(R.id.listview_material_add);
        mLinContainer = view_ticket_material.findViewById(R.id.lincontainer);
        mLinContainer.removeAllViews();
        ArrayList<MaterialAddBean> mArray = cf.getadddata(ActivityId);
        for (int i = 0; i < mArray.size(); i++) {
            addView_Material_add(i, mArray);
        }


        Btn_material_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAddBean bean = new MaterialAddBean();// Flag Upload 'N'  'Y'
                bean.setActivityID(ActivityId);
                bean.setWarehouse(warehousename);
                bean.setWarehouseid(warehouseID);
                bean.setLocation(Material_locationmane);
                bean.setLocationid(Material_locationID);
                bean.setItem_code(materialcode);
                bean.setItemplantid(materialItemplantID);
                bean.setDiscription(materialdis);
                bean.setQuant(Material_Quant);
                bean.setTotalquant(Edittext_maerial_Toatalquant.getText().toString());
                bean.setCosumedquant(Edittext_maerial_consumedQuant.getText().toString());
                bean.setFieldname(Edittext_maerial_fieldname.getText().toString());
                bean.setRemark(Edittext_maerial_remar.getText().toString());
                bean.setUOM(Material_UOM);
                bean.setIsUpload("N");
                listMaterialAdd.add(bean);
                cf.addTicketUpdationMaterialadd(bean);
                mLinContainer.removeAllViews();
                ArrayList<MaterialAddBean> mArray = cf.getadddata(ActivityId);
                ArrayList<MaterialAddBean> mArray1 = cf.getadddatanotuploaded(ActivityId);

                for (int i = 0; i < mArray.size(); i++) {
                    addView_Material_add(i, mArray);
                }

                if (mArray1.size() > 0) {
                    listMID.clear();
                    final String MID = mArray1.get(i).getMatAddID();
                    JSONArray arr = new JSONArray();
                    for (int i = 0; i < mArray1.size(); i++) {
                        JSONObject matdetails = new JSONObject();
                        try {
                            matdetails.put("Warehouse", mArray1.get(i).getWarehouseid());
                            matdetails.put("location", mArray1.get(i).getLocationid());
                            matdetails.put("FieldName", mArray1.get(i).getFieldname());
                            matdetails.put("Qty", mArray1.get(i).getQuant());
                            matdetails.put("Remark", mArray1.get(i).getRemark());
                            matdetails.put("UOM", mArray1.get(i).getUOM());
                            matdetails.put("ItemPlantId", mArray1.get(i).getItemplantid());
                            listMID.add(mArray1.get(i).getMatAddID());
                            arr.put(matdetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ActivityId", ActivityId);//
                        jsonObject.put("objarr", arr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String finobj = jsonObject.toString().replaceAll("\\\\", "");
                    new StartSession(getApplicationContext(), new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostMaterial().execute(finobj, MID);

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "No data to add", Toast.LENGTH_LONG);
                }
            }
        });
        Spimmer_material_warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                warehousename = Spimmer_material_warehouse.getSelectedItem().toString();
                warehouseID = getWarehouseID(warehousename);
                warehousepos = position;

                if (CheckLocation(warehouseID)) {
                    Sharedpreferenceticketsetting = getSharedPreferences(WebUrlClass.SHARED_TICKET_UPDATE_MATERIAL_SETTING,
                            Context.MODE_PRIVATE);
                    String location = Sharedpreferenceticketsetting.getString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION, "");
                    if (location.equalsIgnoreCase("")) {
                        ArrayList<String> lstloc = getLocation(warehouseID);
                        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                                R.layout.vwb_custom_spinner_txt, lstloc);
                        Spimmer_material_location.setAdapter(customDept);
                        Spimmer_material_location.setEnabled(true);
                        Txt_Mat_setting.setText(getString(R.string.UpTicMat_Default_Setting));
                    }else {
                        ArrayList<String> lstloc = getLocation(warehouseID);
                        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                                R.layout.vwb_custom_spinner_txt, lstloc);
                        Spimmer_material_location.setAdapter(customDept);
                        Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                        int warehousepos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, 0);
                        int locationpos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_POS, 0);
                        Spimmer_material_location.setEnabled(false);
                        Spimmer_material_location.setSelection(locationpos);

                    }
                } else {
                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetMaterialLocation().execute(warehouseID);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spimmer_material_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Material_locationmane = Spimmer_material_location.getSelectedItem().toString();
                Material_locationID = getLocationID(Material_locationmane);
                Material_locationpos = position;
                if (isnet()) {
                    new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetLocationQty().execute(Material_locationID, warehouseID, materialItemplantID);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }



                if (isnet()) {

                    new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPacketData().execute(Material_locationID, warehouseID, materialItemplantID);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spimmer_material_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialcode = Spimmer_material_code.getSelectedItem().toString();
                materialdis = getmaterialDiscription(materialcode);
                materialItemplantID = getmaterialcodePlantrID(materialcode);


                if (Checkmaterialcode()) {
                    Spimmer_material_discription.setSelection(position);

                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetStock().execute(Material_locationID, warehouseID, materialItemplantID);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                    }

                } else {
                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetmaterialCode().execute(PlantMasterId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spimmer_material_discription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                materialdis = Spimmer_material_discription.getSelectedItem().toString();
                materialcode = getmaterialcodeID(materialdis);
                materialItemplantID = getmaterialcodePlantrID(materialcode);

                if (Checkmaterialcode()) {
                    Spimmer_material_code.setSelection(position);


                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetStock().execute(Material_locationID, warehouseID, materialItemplantID);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                    }

                } else {
                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetmaterialCode().execute(PlantMasterId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (CheckWarehouse()) {
            ArrayList<String> lstWarehouse = getWarehouse();
            MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstWarehouse);
            Spimmer_material_warehouse.setAdapter(customDept);
            Sharedpreferenceticketsetting = getSharedPreferences(WebUrlClass.SHARED_TICKET_UPDATE_MATERIAL_SETTING,
                    Context.MODE_PRIVATE);
            String warehouse = Sharedpreferenceticketsetting.getString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE, "");
            int warehousepos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, 0);

            if (warehouse.equalsIgnoreCase("")) {
                if(warehousepos==-1){
                    Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                    Spimmer_material_warehouse.setEnabled(false);
                }else {
                    Spimmer_material_warehouse.setEnabled(true);
                    Txt_Mat_setting.setText(getString(R.string.UpTicMat_Default_Setting));
                }

            }else {
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                Spimmer_material_warehouse.setEnabled(false);
                Spimmer_material_warehouse.setSelection(warehousepos);

            }
        } else {
            if (isnet()) {
                new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetMaterialWarehouse().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
            }
        }
        if (Checkmaterialcode()) {
            ArrayList<String> lstcode = getmaterialCode();
            MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstcode);
            Spimmer_material_code.setAdapter(customDept);

            ArrayList<String> lstdis = getmaterialDiscription();
            MySpinnerAdapter customDept1 = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstdis);
            Spimmer_material_discription.setAdapter(customDept1);

        } else {
            if (isnet()) {
                new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetmaterialCode().execute(PlantMasterId);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
            }
        }
        Txt_Mat_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtdata = Txt_Mat_setting.getText().toString();
                if (txtdata.equalsIgnoreCase(getString(R.string.UpTicMat_Default_Setting))) {
                    Material_locationmane = (String) Spimmer_material_location.getSelectedItem();
                    Material_locationID = getLocationID(Material_locationmane);
                    if(Material_locationmane == null){
                        Material_locationpos = -1;
                    }

                    warehousename = (String) Spimmer_material_warehouse.getSelectedItem();
                    warehouseID = getWarehouseID(warehousename);
                    if(warehousename == null){
                        warehousepos = -1;
                    }
                    showdialog_matdefaultsetting(warehousename, Material_locationmane, warehouseID,
                            Material_locationID, warehousepos
                            , Material_locationpos);
                } else if (txtdata.equalsIgnoreCase(getString(R.string.UpTicMat_Change_Setting))) {
                    showdialog_materialchart();

                }

            }
        });

    }

    public void addView_Material_add(int i, ArrayList<MaterialAddBean> marray) {
        String[] claimAction = {"Edit", "Cancel"};
        TextView edit, cancel, tv_item_code, tv_discription, tv_warehouse,
                tv_location, tvquant, tv_fieldname, tv_renmark, tv_edit, tv_cancel;

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_item_add_material,
                null);
        edit = (TextView) baseView.findViewById(R.id.edit);
        cancel = (TextView) baseView.findViewById(R.id.cancel);
        tv_item_code = (TextView) baseView.findViewById(R.id.tv_Itemcode);
        tv_discription = (TextView) baseView.findViewById(R.id.tv_Discription);

        tv_warehouse = (TextView) baseView.findViewById(R.id.tv_warehouse);
        tv_location = (TextView) baseView.findViewById(R.id.tv_location);
        tvquant = (TextView) baseView.findViewById(R.id.tv_Quantity);
        tv_fieldname = (TextView) baseView.findViewById(R.id.tv_Field_Name);
        tv_renmark = (TextView) baseView.findViewById(R.id.tv_Remark);
        tv_edit = (TextView) baseView.findViewById(R.id.tv_mat_edit);
        tv_cancel = (TextView) baseView.findViewById(R.id.tv_mat_cancel);

        tv_item_code.setText(marray.get(i).getItem_code());
        tv_discription.setText(marray.get(i).getDiscription());
        tv_warehouse.setText(marray.get(i).getWarehouse());
        tv_location.setText(marray.get(i).getLocation());
        tvquant.setText(marray.get(i).getQuant());
        tv_fieldname.setText(marray.get(i).getFieldname());
        tv_renmark.setText(marray.get(i).getRemark());
        mLinContainer.addView(baseView);
    }


    private void setListner() {
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()) {
                    getTicketDetail();
                    getTicketDetails();
                    if (FinalOutcome.equalsIgnoreCase("Permanent") || FinalOutcome.equalsIgnoreCase("Complete")) {
                        latitude = lat;
                        longitude = lng;
                        Address = Loacationname;
                        OutcomeID = getFinalOutComeID(FinalOutcome);//40&38

                    }
                    Action = edt_Action.getText().toString();

                    effortdetailTime = edt_effertdetaildateset.getText().toString();

                    // FinalOutcome,
                    Result = edt_result.getText().toString();

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

                    remaindraction = edt_remaindraction.getText().toString();
                    remainderactiondateselect = edt_remainderactionsetdate.getText().toString();
                    String s = hdnfld1stpromisedt;
                    if (s.equalsIgnoreCase("")) {
                        hdnfld1stpromisedt = hdnfldcurntpromse;
                    }
                    JSONObject jsonObject=new JSONObject();



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
                        jsonObject.put("Address", Address);
                        jsonObject.put("MobileNo", Mobile);
                        jsonObject.put("ServiceChargetoCust","0");
                        jsonObject.put("IsCheck", "false");
                        String a1 = "";
                        String a2 = "";
                        String a3 = "";
                        jsonObject.put("VersionMasterId","");
                        jsonObject.put("InstallerId","" );
                        jsonObject.put("InstallDt","" );
                        jsonObject.put("Note","" );
                        jsonObject.put("ExtraRtCause","");

                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("obj",jsonObject);



                        // data.put("data",jsonObject1);


                        FinalObj = jsonObject1.toString();
                        FinalObj = FinalObj.replaceAll("\\\\", "");
                    }catch (Exception e){
                        e.printStackTrace();
                    }




                    new StartSession(mContext, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            UploadSave save = new UploadSave();
                            save.execute(FinalObj);

                            /*UploadSave save = new UploadSave();
                            save.execute(Action, effortdetailTime, OutcomeID, Result, effDetailfrom, effDetailto,
                                    effDetailHr, remaindraction, remainderactiondateselect,
                                    "00:00", timeFrom, timeTo, txtCurrentPromise,
                                    ActivityId,UnitId, ActivityTypeId, ProjectId, IssuedTo,
                                    IssuedTo, hdnfldPromiseCount, ReportingToEmail, ReportingToName, TicketNo,
                                    TicketDiscription,SaveToDelegate, ReportedByEmail,
                                    ConsigneeName,Attachment,hdnfld1stpromisedt, hdnfldcurntpromse,
                                    lblReportedByEmail, MailClient,MailClient,ProductNAme,TicketCategory,hdnfld1stpromisedt,RootCause,PriorityId,
                                    Startdate,latitude,longitude,Address,mobile, finalChagre,"false","","","","");
*/
                        }

                        @Override
                        public void callfailMethod(String msg) {
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

                    JSONObject jsonObject=new JSONObject();



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
                        jsonObject.put("Address", Address);
                        jsonObject.put("MobileNo", Mobile);
                        jsonObject.put("ServiceChargetoCust","0");
                        jsonObject.put("IsCheck", "false");
                        String a1 = "";
                        String a2 = "";
                        String a3 = "";
                        jsonObject.put("VersionMasterId","");
                        jsonObject.put("InstallerId","" );
                        jsonObject.put("InstallDt","" );
                        jsonObject.put("Note","" );
                        jsonObject.put("ExtraRtCause","");

                        JSONObject jsonObject1=new JSONObject();
                        jsonObject1.put("obj",jsonObject);



                        // data.put("data",jsonObject1);


                        FinalObj = jsonObject1.toString();
                        FinalObj = FinalObj.replaceAll("\\\\", "");
                    }catch (Exception e){
                        e.printStackTrace();
                    }




                    new StartSession(mContext, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            UploadSave save = new UploadSave();
                            save.execute(FinalObj);

                            /*UploadSave save = new UploadSave();
                            save.execute(Action, effortdetailTime, OutcomeID, Result, effDetailfrom, effDetailto,
                                    effDetailHr, remaindraction, remainderactiondateselect,
                                    "00:00", timeFrom, timeTo, txtCurrentPromise,
                                    ActivityId,UnitId, ActivityTypeId, ProjectId, IssuedTo,
                                    IssuedTo, hdnfldPromiseCount, ReportingToEmail, ReportingToName, TicketNo,
                                    TicketDiscription,SaveToDelegate, ReportedByEmail,
                                    ConsigneeName,Attachment,hdnfld1stpromisedt, hdnfldcurntpromse,
                                    lblReportedByEmail, MailClient,MailClient,ProductNAme,TicketCategory,hdnfld1stpromisedt,RootCause,PriorityId,
                                    Startdate,latitude,longitude,Address,mobile, finalChagre,"false","","","","");
*/
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
                canceldata();
                Intent i = new Intent(TicketUpdateDNAActivity.this, ActivityMain.class);
                startActivity(i);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketUpdateDNAActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketUpdateDNAActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketUpdateDNAActivity.this,
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

        edt_remainderactionsetdate.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketUpdateDNAActivity.this,
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
                if (FinalOutcome.equalsIgnoreCase("Permanent") || FinalOutcome.equalsIgnoreCase("Complete")) {
                    latitude = lat;
                    longitude = lng;
                    Address = Loacationname;
                    OutcomeID = getFinalOutComeID(FinalOutcome);//40&38

                }
                // Toast.makeText(getApplicationContext(), FinalOutcome, Toast.LENGTH_LONG).show();
                OutcomeID = getFinalOutComeID(FinalOutcome);//40&38
                //int ID = Integer.parseInt(OutcomeID);
                if (OutcomeID.equalsIgnoreCase("40") || OutcomeID.equalsIgnoreCase("38")) {
                    lin_resonfortranfer.setVisibility(View.VISIBLE);
                    lin_transferto.setVisibility(View.VISIBLE);
                    Autocomplettransferto.setVisibility(View.VISIBLE);
                    Autocomplettransferto.setText("");
                    edt_resonfortransfer.setVisibility(View.VISIBLE);
                    edt_resonfortransfer.setText("");
                    btn_SaveTransfer.setVisibility(View.GONE);
                } else {
                    lin_resonfortranfer.setVisibility(View.GONE);
                    lin_transferto.setVisibility(View.GONE);
                    Autocomplettransferto.setVisibility(View.GONE);
                    edt_resonfortransfer.setVisibility(View.GONE);
                    btn_SaveTransfer.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_effortdetailfrom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TicketUpdateDNAActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hr = selectedHour + "";
                        String mi = selectedMinute + "";
                        if (hr.length() == 1) {
                            hr = 0 + hr;
                        }
                        if (mi.length() == 1) {
                            mi = 0 + mi;
                        }
                        timeSpinnerFrom = hr + ":" + mi;
                        spinner_effortdetailfrom.setText(hr + ":" + mi);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


                /* new AlertDialog.Builder(TicketUpdateDEPLActivity.this)
                 *//*
                        .setTitle("Select Countries")
*//*
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                timeSpinnerFrom = ArrarlisttimeSpinnerFrom[position].toString();
                                spinner_effortdetailfrom.setText(timeSpinnerFrom);
                                dialog.dismiss();
                            }
                        }).create().show();*/
            }
        });

        spinner_effortdetailto.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TicketUpdateDNAActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String hr = selectedHour + "";
                        String mi = selectedMinute + "";
                        if (hr.length() == 1) {
                            hr = 0 + hr;
                        }
                        if (mi.length() == 1) {
                            mi = 0 + mi;
                        }
                        timeSpinnerTo = hr + ":" + mi;
                        spinner_effortdetailto.setText(hr + ":" + mi);

                        String Diff = calculate_time_diff(timeSpinnerFrom, timeSpinnerTo);
                        txt_effortdetailhour.setText(Diff);
                       /* int result = Integer.parseInt(Diff);
                        if (result<0) {
                            Toast.makeText(TicketUpdateDEPLActivity.this,"Invalid time",Toast.LENGTH_SHORT).show();

                        }else {
                            txt_effortdetailhour.setText(Diff);
                        }*/
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


                /*   new AlertDialog.Builder(TicketUpdateDEPLActivity.this)
                 *//*
                        .setTitle("Select Countries")
*//*
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                timeSpinnerTo = ArrarlisttimeSpinnerFrom[position].toString();

                                spinner_effortdetailto.setText(timeSpinnerTo);

                                String Diff = calculate_time_diff(timeSpinnerFrom, timeSpinnerTo);
                                txt_effortdetailhour.setText(Diff);




                             *//*  int result = Integer.parseInt(Diff);

                                if (result<0) {
                                    Toast.makeText(ViewTicketMain.this,"Invalid time",Toast.LENGTH_SHORT).show();

                                }else {
                                    txt_effortdetailhour.setText(Diff);
                                }*//*
                                dialog.dismiss();
                            }
                        }).create().show();*/
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

        Autocomplettransferto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String User = s.toString();
                String UserMasterID = getUserMAsterID(User);
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
            public void afterTextChanged(Editable s) {


            }
        });

        txt_affect_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });

       /* len_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });*/
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
        String EffortDate = dateFormatdate.format(new Date());
        effortdetailTime = getDate1DD_MMM_YYYY(EffortDate);

        edt_effertdetaildateset.setText(effortdetailTime);

        String EffortDate1 = dateFormatdate.format(new Date());
        remainderactiondateselect = getDate1DD_MMM_YYYY(EffortDate1);
        edt_remainderactionsetdate.setText(remainderactiondateselect);


    }

    private String getUserMAsterID(String User) {
        String UserMAsterID = "";
        sql = db.getWritableDatabase();
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(TicketUpdateDNAActivity.this, android.R.layout.simple_spinner_item, listBugFixing);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_finaloutcome.setAdapter(dataAdapter);
    }

    /*private void updateSpinnerRootCause() {
        final ArrayList<String> listBugFixing = new ArrayList<String>();
        listBugFixing.add("Bug Fixing");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listBugFixing);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rootcause.setAdapter(dataAdapter);
    }
*/
    private List<String> getSpinnerFinalOutcome() {
        List<String> data = new ArrayList<String>();
        data.clear();
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_BIND_FINAL_OUTCOME, null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("Outcome")));
            } while (c.moveToNext());
        }
        c.close();

        return data;
    }

    private void getTicketDetail() {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA + " WHERE ActivityId='" + ActivityId + "'", null);
        int a = c.getCount();
        if (a > 0) {
            c.moveToFirst();
            do {
                try {
                    String Id = ActivityId;
                    TicketNo = c.getString(c.getColumnIndex("ActivityId"));
                    UnitId = c.getString(c.getColumnIndex("UnitId"));
                    ActivityTypeId = c.getString(c.getColumnIndex("ActivityTypeId"));
                    IssuedTo = c.getString(c.getColumnIndex("IssuedTo"));
                    ReportingToEmail = c.getString(c.getColumnIndex("ReportingToEmail"));
                    ReportingToName = c.getString(c.getColumnIndex("ReportingToName"));
                    ActivityName = c.getString(c.getColumnIndex("ActivityName"));
                    TicketDiscription = c.getString(c.getColumnIndex("ActivityName"));
                    lblReportedByEmail = c.getString(c.getColumnIndex("ContMob"));
                    ReportedByEmail = c.getString(c.getColumnIndex("ReportedByEmail"));
                    ConsigneeName = c.getString(c.getColumnIndex("ConsigneeName"));
                    PriorityId = c.getString(c.getColumnIndex("PriorityId"));
                    Mobile = c.getString(c.getColumnIndex("Mobile"));//Email
                    String mailID = c.getString(c.getColumnIndex("Email"));
                    mLin_mail.setVisibility(View.GONE);
                    if (!(Mobile.equalsIgnoreCase("") && mailID.equalsIgnoreCase(""))) {
                        mLin_mail.setVisibility(View.VISIBLE);
                        txt_mail.setText(mailID);
                        Txt_mobno.setText(Mobile);
                    }
                    if (!(ConsigneeName.equalsIgnoreCase(""))) {
                        lin_Consignee.setVisibility(View.GONE);
                        txt_customername.setText(ConsigneeName);
                    }
                    TicketCategory = c.getString(c.getColumnIndex("TicketCategory"));//ItemDesc,EndDate
                    ProductNAme = c.getString(c.getColumnIndex("ItemDesc"));
                    if (!(ProductNAme.equalsIgnoreCase(""))) {
                        txt_tckname.setText(ProductNAme);
                    }
                    String as = c.getString(c.getColumnIndex("EndDate"));
                    txtCurrentPromise = getDatelong1DD_MMM_YYYY(as);
                    String StartDate = c.getString(c.getColumnIndex("ActualStartDate"));
                    ActualStartDate = getDatelong1DD_MMM_YYYY(StartDate);
                    ActualCompletionDate = c.getString(c.getColumnIndex("ActualCompletionDate"));//DD/MM/YYYY
                    if (!ActualCompletionDate.equalsIgnoreCase("null") && !ActualCompletionDate.isEmpty()) {

                        Startdate = getDatelong1DD_MMM_YYYY(ActualCompletionDate);
                    }
                    String asdf = c.getString(c.getColumnIndex("EndDate"));
                    hdnfldcurntpromse = getDatelong1DD_MMM_YYYY(asdf);
                    hdnfld1stpromisedt = getDatelong1DD_MMM_YYYY(ActualCompletionDate);


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

    private String getDatelong1DD_MMM_YYYY(String data) {

        SimpleDateFormat opsdf = new SimpleDateFormat("dd-MMM-yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    public String getDate1DD_MMM_YYYY(String InputDate) {
        String Output = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat opsdf = new SimpleDateFormat("dd-MMM-yyyy");
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
            ut.displayToast(TicketUpdateDNAActivity.this, "Please Enter All Field");
            return false;
        } else if (!(edt_result.getText().toString().length() > 0)) {
            ut.displayToast(TicketUpdateDNAActivity.this, "Please Enter Result");
            return false;
        }

        return true;
    }

    private JSONObject createJson() {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA + " WHERE ActivityId=" + ActivityId + "", null);
        int a = c.getCount();
        if (a > 0) {
            c.moveToFirst();
            do {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("txtActionPerform", edt_Action.getText().toString());
                    jsonObject.put("txtEffortDate", date);
                    jsonObject.put("ddlOutcome", FinalOutcome);
                    jsonObject.put("txtResult", edt_result.getText().toString());
                    jsonObject.put("txtcallfromFolwup", txt_effortdetailhour.getText().toString());
                    jsonObject.put("txtcalltoFolwup", txt_effortdetailto.getText().toString());
                    jsonObject.put("txtFlowupHrs", txt_effortdetailhour.getText().toString());
                    jsonObject.put("txtRemindAction", edt_remaindraction.getText().toString());
                    jsonObject.put("txtReminderDate", date);
                    jsonObject.put("txtReminderTime", "00:00");
                    jsonObject.put("ddlcallfromFolwup", timeSpinnerFrom);
                    jsonObject.put("ddlcalltoFolwup", timeSpinnerTo);
                    jsonObject.put("txtCurrentPromise", "");
                    jsonObject.put("hdnfldActivityId", c.getString(c.getColumnIndex("ActivityId")));
                    jsonObject.put("hdnfldUnitId", c.getString(c.getColumnIndex("UnitId")));
                    jsonObject.put("hdnfldActivityTypeId", c.getString(c.getColumnIndex("ActivityTypeId")));
                    jsonObject.put("hdnfldProjectId", " ");
                    jsonObject.put("hdnfldIssuedTo", c.getString(c.getColumnIndex("IssuedTo")));
                    jsonObject.put("hdnfldIssuedToName", c.getString(c.getColumnIndex("IssuedTo")));
                    jsonObject.put("hdnfldPromiseCount", "");
                    jsonObject.put("hdnfldReportingToEmail", c.getString(c.getColumnIndex("ReportingToEmail")));
                    jsonObject.put("hdnfldReportingToName", c.getString(c.getColumnIndex("ReportingToName")));
                    jsonObject.put("lblTktNo", c.getString(c.getColumnIndex("ActivityId")));
                    jsonObject.put("lblTktDescrptn", c.getString(c.getColumnIndex("ActivityName")));
                    jsonObject.put("hdnfldSaveToDelegate", "");
                    jsonObject.put("hdnfldReportedByEmail", c.getString(c.getColumnIndex("ReportedByEmail")));
                    jsonObject.put("lblCustomerName", c.getString(c.getColumnIndex("ConsigneeName")));
                    jsonObject.put("fuAttachment", "");
                    jsonObject.put("hdnfld1stpromisedt", "");
                    jsonObject.put("hdnfldcurntpromse", "");
                    jsonObject.put("lblReportedByName", "");
                    jsonObject.put("chkMailClient", "");
                    jsonObject.put("hdnfldMailClient", "");
                    jsonObject.put("lblProductName", "");
                    jsonObject.put("ticketCategory", c.getString(c.getColumnIndex("TicketCategory")));
                    jsonObject.put("ActualCompletionDate", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } while (c.moveToNext());

        } else {


        }
        return null;

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
        //  progressbar.setVisibility(View.VISIBLE);

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(TicketUpdateDNAActivity.this);
                progressDialog.setMessage("Loading. Please wait...");
                progressDialog.setIndeterminate(false);
                //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.setCancelable(true);

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void dismissProgressDialog() {

       /* if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }*/

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
                        ut.displayToast(TicketUpdateDNAActivity.this, msg);
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
                    sql.delete(db.TABLE_TICKET_UPDATION_DATA, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA, null);
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
                        long aa = sql.insert(db.TABLE_TICKET_UPDATION_DATA, null, Container);
                        Log.e("Response...", "Count" + aa);
                        getTicketDetail();
                        getTicketDetails();
                    }

                } catch (Exception e) {
                    e.printStackTrace();


                }


            } else {
                // ut.displayToast(mContext, "Server Error...");

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
        String response;

//        NoMail

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
               /* String url = CompanyURL + WebUrlClass.api_getSaveData + "?txtActionPerform=" + params[0] +
                        "&txtEffortDate=" + params[1] +
                        "&ddlOutcome=" + params[2] +
                        "&txtResult=" + params[3] +
                        "&txtcallfromFolwup=" + params[4] +
                        "&txtcalltoFolwup=" + params[5] +
                        "&txtFlowupHrs=" + params[6] +
                        "&txtRemindAction=" + params[7] +
                        "&txtReminderDate=" + params[8] +
                        "&txtReminderTime=" + params[9] +
                        "&ddlcallfromFolwup=" + params[10] +
                        "&ddlcalltoFolwup=" + params[11] +
                        "&txtCurrentPromise=" + params[12] +
                        "&hdnfldActivityId=" + params[13] +
                        "&hdnfldUnitId=" + params[14] +
                        "&hdnfldActivityTypeId=" + params[15] +
                        "&hdnfldProjectId=" + params[16] +
                        "&hdnfldIssuedTo=" + params[17] +
                        "&hdnfldIssuedToName=" + params[18] +
                        "&hdnfldPromiseCount=" + params[19] +
                        "&hdnfldReportingToEmail=" + params[20] +
                        "&hdnfldReportingToName=" + params[21] +
                        "&lblTktNo=" + params[22] +
                        "&lblTktDescrptn=" + params[23] +
                        "&hdnfldSaveToDelegate=" + params[24] +
                        "&hdnfldReportedByEmail=" + params[25] +
                        "&lblCustomerName=" + params[26] +
                        "&fuAttachment=" + params[27] +
                        "&hdnfld1stpromisedt=" + params[28] +
                        "&hdnfldcurntpromse=" + params[29] +
                        "&lblReportedByName=" + params[30] +
                        "&chkMailClient=" + params[31] +
                        "&hdnfldMailClient=" + params[32] +
                        "&lblProductName=" + params[33] +
                        "&ticketCategory=" + params[34] +
                        "&ActualCompletionDate=" + params[35] +
                        "&ActionMasterId=" + params[36] +
                        "&PriorityId=" + params[37] +
                        "&ActualStartDate=" + params[38] +
                        "&Latitude=" + params[39] +
                        "&Longitude=" + params[40] + "&Address=" + params[41] + "&MobileNo=" + params[42] +
                        "&ServiceChargetoCust=" + params[43] + "&IsCheck=" + params[44] + "&VersionMasterId=" + params[45] +
                        "&InstallerId=" + params[46] + "&InstallDt=" + params[47] + "&Note=" + params[48] + "&ExtraRtCause=";*/
                String url = CompanyURL + WebUrlClass.api_PostSavedata;
                url = url.replaceAll(" ","%20");//%20
                res = ut.OpenPostConnection(url, params[0], TicketUpdateDNAActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();//OK
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("OK")) {

                if (lin_resonfortranfer.getVisibility() == View.VISIBLE ||
                        lin_transferto.getVisibility() == View.VISIBLE) {
                    new StartSession(mContext, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            final String Reason = edt_resonfortransfer.getText().toString();
                            final String Transferto = Autocomplettransferto.getText().toString();
                            String UserMasterID = getUserMAsterID(Transferto);
                            String s = hdnfld1stpromisedt;
                            if (s.equalsIgnoreCase("")) {
                                hdnfld1stpromisedt = hdnfldcurntpromse;
                            }
                            hdnfld1stpromisedt = getDate1DD_MMM_YYYY(hdnfld1stpromisedt);
                            final String timeFrom;
                            final String timeTo;
                            if (!(UserMasterID.equalsIgnoreCase(""))) {
                                uploadTransfer = new UploadTransfer();
                                uploadTransfer.execute(Reason, UserMasterID, hdnfld1stpromisedt, txtCurrentPromise, TicketDiscription, TicketNo, ConsigneeName, ActivityId);
                            } else {
                                ut.displayToast(TicketUpdateDNAActivity.this, "Can't Identify transferto user");
                            }
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(TicketUpdateDNAActivity.this, msg);
                        }
                    });
                } else {
                    canceldata();
                    sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    ut.displayToast(mContext, "Data Save Successfully");//"You can not fill timesheet greater than current time!!!"
                    finish();
                }

            } else if (s.contains("You have already filled the time slot")) {
                ut.displayToast(mContext, "You have already filled the time slot");

            } else if (s.contains("You can not fill timesheet greater than current time!!!")) {
                ut.displayToast(mContext, "You can not fill timesheet greater than current time!!!");

            } else if(s.equalsIgnoreCase("error")){
                ut.displayToast(mContext, "Data not save ");
                finish();

            }else {
                ut.displayToast(mContext, "Data Save Successfully");
                finish();
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
                        "&ConcateTktDescrptn=" + params[4] + "&lblTktNo=" + params[5] + "&lblCustomerName=" + params[6] +
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
                ut.displayToast(mContext, "Activity Transfered successfully");
                finish();

            } else if (s.contains("You have already filled the time slot")) {
                ut.displayToast(mContext, "You have already filled the time slot");

            } else {
                ut.displayToast(mContext, "Can not transfer Data..error..");

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
                txt_lblAction.setText("Action");
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


            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.click) {

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
        Context context = getApplicationContext();
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
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(TicketUpdateDNAActivity.this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, TicketUpdateDNAActivity.this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(TicketUpdateDNAActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TicketUpdateDNAActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Geocoder gcd = new Geocoder(TicketUpdateDNAActivity.this, Locale.getDefault());
                List<android.location.Address> addresses = gcd.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
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
    protected void onDestroy() {
        super.onDestroy();
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;

    }

    @Override
    public void onPause() {
        super.onPause();
//        stopLocationUpdates();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
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

    private class GetChkIsClientAffected extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetChkIsClientAffected + "?ActivityId=" + ActivityId;
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

            System.out.println("Output :" + s);

            if (s.equalsIgnoreCase("Y")) {

                txt_affect_customer.setVisibility(View.VISIBLE
                );
            }
            dismissProgressDialog();
        }
    }

    private class GetChkIsClientAffectedDetailsList extends AsyncTask<Integer, String, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetPrevAffectedClientIfo + "?AAMActId=" + ActivityId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                sql.delete(db.TABLE_AFFECTED_CUSTOMER_INFO, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_AFFECTED_CUSTOMER_INFO, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jaffectedcustomerinfo = jResults.getJSONObject(i);

                    AffectedCustomer affectedCustomer = new AffectedCustomer();
                    affectedCustomer.setActivityName(jaffectedcustomerinfo.getString("ActivityName"));
                    affectedCustomer.setCustTicketNo(jaffectedcustomerinfo.getString("CustTicketNo"));
                    affectedCustomer.setCustVendorName(jaffectedcustomerinfo.getString("CustVendorName"));
                    affectedCustomer.setReportedBy(jaffectedcustomerinfo.getString("ReportedBy"));
                    affectedCustomer.setActivityCode(jaffectedcustomerinfo.getString("ActivityCode"));
                    affectedCustomer.setPKActAffectedCust(jaffectedcustomerinfo.getString("PKActAffectedCust"));
                    affectedCustomer.setStatus(jaffectedcustomerinfo.getString("Status"));
                    cf.AddAffectedCustomerInfo(affectedCustomer);
                    affectedCustomers.add(affectedCustomer);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            dismissProgressDialog();
            if (response != null) {
                adapterAffectedCustomer = new AdapterAffectedCustomer(TicketUpdateDNAActivity.this, affectedCustomers);
                //   callhistory_listview.setAdapter(adapterAffectedCustomer);
            }


            dismissProgressDialog();
        }

    }

    private void showdialog_matdefaultsetting(final String warehouse, final String location, final String warehouseid, final String locationid, final int warehousepos, final int locationpos) {
        TextView dg_txt_msg;
        Button dg_btn_ok;
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.vwb_dialog_upateticket_material, null, false);
        builder = new AlertDialog.Builder(TicketUpdateDNAActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();
        //there are a lot of settings, for dialog, check them all out!

        dg_txt_msg = (TextView) v.findViewById(R.id.bg_txt_msg);
        dg_txt_msg.setText("Do you want to save " + warehouse + " and " + location + " as your default warehouse and location respectively ?");
        dg_btn_ok = (Button) v.findViewById(R.id.dg_btn_ok);

        dg_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                SharedPreferences.Editor editor = Sharedpreferenceticketsetting.edit();
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE, warehouse);
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_ID, warehouseid);
                editor.putInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, warehousepos);
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION, location);
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_ID, locationid);
                editor.putInt(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_POS, locationpos);
                editor.commit();
                Spimmer_material_location.setEnabled(false);
                Spimmer_material_warehouse.setEnabled(false);

            }
        });
        //now that the dialog is set up, it's time to show it
        alertDialog1 = builder.create();
        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();
    }

    private void showdialog_materialchart() {
        TextView dg_txt_msg;
        Button dg_btn_ok;
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.vwb_dialog_upateticket_material, null, false);
        builder = new AlertDialog.Builder(TicketUpdateDNAActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();
        //there are a lot of settings, for dialog, check them all out!
        //there are a lot of settings, for dialog, check them all out!

        dg_txt_msg = (TextView) v.findViewById(R.id.bg_txt_msg);
        dg_txt_msg.setText("Do you want to chanage  default vwb_settings ?");
        dg_btn_ok = (Button) v.findViewById(R.id.dg_btn_ok);

        dg_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Default_Setting));

                SharedPreferences.Editor editor = Sharedpreferenceticketsetting.edit();
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE, "");
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_ID, "");
                editor.putInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, 0);
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION, "");
                editor.putString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_ID, "");
                editor.putInt(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_POS, 0);
                editor.commit();
                Spimmer_material_location.setEnabled(true);
                Spimmer_material_warehouse.setEnabled(true);
            }
        });
        //now that the dialog is set up, it's time to show it

        //now that the dialog is set up, it's time to show it
        alertDialog1 = builder.create();
        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(true);
        alertDialog1.show();
    }

    private void showdialog() {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.vwb_affected_customer_listview, null, false);
        builder = new AlertDialog.Builder(TicketUpdateDNAActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();
        //there are a lot of settings, for dialog, check them all out!

        listview_aff_cust_info = (LinearLayout) v.findViewById(R.id.listview_aff_cust_info);

        listview_aff_cust_info.removeAllViews();
        if (affectedCustomers.size() > 0) {
            for (int i = 0; i < affectedCustomers.size(); i++) {
                addView_CallList(i);


            }
        }

        //now that the dialog is set up, it's time to show it
        alertDialog1 = builder.create();
        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();
    }

    private void addView_CallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        convertView = layoutInflater.inflate(R.layout.vwb_affected_customer_lay,
                null);
        txt_ticket_no = (TextView) convertView.findViewById(R.id.txt_ticket_no);
        txt_client_name = (TextView) convertView.findViewById(R.id.txt_client_name);
        txt_reported_by = (TextView) convertView.findViewById(R.id.txt_reported_by);
        txt_resolve = (TextView) convertView.findViewById(R.id.txt_resolve);

        if (affectedCustomers.get(i).getStatus().equalsIgnoreCase("Y")) {
            txt_ticket_no.setTextColor(Color.RED);
            txt_client_name.setTextColor(Color.RED);
            txt_reported_by.setTextColor(Color.RED);
        }


        txt_ticket_no.setText(affectedCustomers.get(position).getCustTicketNo());
        txt_client_name.setText(affectedCustomers.get(position).getCustVendorName());
        txt_reported_by.setText(affectedCustomers.get(position).getReportedBy());


        txt_resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdialog(i);
                alertDialog1.dismiss();

            }
        });

        listview_aff_cust_info.addView(convertView);

    }

    private void getdialog(final int i) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.vwb_resolve_lay_update, null, false);
        builder = new AlertDialog.Builder(TicketUpdateDNAActivity.this);
        builder.setView(v);
        alertDialog = builder.create();
        final TextView spinner_time = (TextView) v.findViewById(R.id.spinner_time);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        ImageView img_date = (ImageView) v.findViewById(R.id.img_date);
        final EditText edt_date_set = (EditText) v.findViewById(R.id.edt_date_set);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        edt_date_set.setText(sdf.format(new Date()));
        date = edt_date_set.getText().toString().trim();
        spinner_time.setText(timeSpinnerFrom);
        spinner_fromtime = new ArrayAdapter<String>(TicketUpdateDNAActivity.this, android.R.layout.simple_spinner_dropdown_item, ArrarlisttimeSpinnerFrom);


        spinner_time.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(TicketUpdateDNAActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String hr = selectedHour + "";
                        String mi = selectedMinute + "";
                        if (hr.length() == 1) {
                            hr = 0 + hr;
                        }
                        if (mi.length() == 1) {
                            mi = 0 + mi;
                        }
                        timeSpinnerFrom = hr + ":" + mi;
                        spinner_time.setText(timeSpinnerFrom);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
            /* new android.app.AlertDialog.Builder(TicketUpdateDEPLActivity.this)
             *//*
                        .setTitle("Select Countries")
*//*,kfj;'sfk;slfjk;slfgk'
                        .setAdapter(spinner_fromtime, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int position) {
                                timeSpinnerFrom = ArrarlisttimeSpinnerFrom[position].toString();
                                spinner_time.setText(timeSpinnerFrom);
                                dialog.dismiss();
                            }
                        }).create().show();
            }*/
        });//timeSpinnerTo

        img_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TicketUpdateDNAActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_date_set.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(date.equalsIgnoreCase(""))) {

                    String datetimrupdateticket = formateDateFromstring("yyyy-MM-dd", "dd/MM/yyyy", date);

                    PKActAffectedCust = affectedCustomers.get(i).getPKActAffectedCust();
                    UpdadteticketDatetime = datetimrupdateticket + " " + timeSpinnerFrom;

                    JSONObject jsondetailsupdate = new JSONObject();


                    try {

                        jsondetailsupdate.put("datetime", UpdadteticketDatetime);
                        jsondetailsupdate.put("PKActAffectedCust", PKActAffectedCust);

                        IndividualTicket = jsondetailsupdate.toString();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    IndividualTicket = IndividualTicket.replaceAll("\\\\", "");
                    if (isnet()) {
                        new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostIndividualTktUpdateJSON().execute(IndividualTicket);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                Toast.makeText(TicketUpdateDNAActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Select Date", Toast.LENGTH_LONG).show();
                }

            }
        });
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    class DownloadGetBindTktCategryJSON extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebUrlClass.api_GetBindTktCategry;

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_GetBindCategory, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GetBindCategory, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_GetBindCategory, null, values);

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
                getGetBindcategory();
            }

        }

    }

    class PostIndividualTktUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TicketUpdateDNAActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostIndividualTkt;

            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("BusinessAPI-1 :" + response);
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
            alertDialog.dismiss();
            if (!(integer.equalsIgnoreCase("error"))) {
                Toast.makeText(TicketUpdateDNAActivity.this, "Update succcessfully", Toast.LENGTH_LONG).show();

            }


        }

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

    private void getTicketDetails() {
        sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATION_DATA + " WHERE ActivityId='" + ActivityId + "'", null);
        int a = c.getCount();
        if (a > 0) {
            c.moveToFirst();
            do {
                try {
                    Activity_code = c.getString(c.getColumnIndex("ActivityCode"));
                    txt_ticket_no.setText(Activity_code);
                    Activity_name = c.getString(c.getColumnIndex("ActivityName"));
                    txt_activity_name.setText(Activity_name);
                    Contact_mobile = c.getString(c.getColumnIndex("ContMob"));
                    txt_reportedby_name.setText(Contact_mobile);
                    Assigned = c.getString(c.getColumnIndex("AddedBy"));
                    txt_assigned_name.setText(Assigned);
                    Resolution_time = c.getString(c.getColumnIndex("HoursRequired"));
                    txt_resolution_time.setText(Resolution_time + " hours");
                    PriorityId = c.getString(c.getColumnIndex("PriorityId"));
                    if (PriorityId.equals("1")) {
                        txt_priority_level.setText("Normal");
                    }
                    WarrantyCode = c.getString(c.getColumnIndex("WarrantyCode"));


                    if (cf.getRoutefromcount() > 0) {
                        getroutefrom();
                    } else {

                        if (isnet()) {
                            new StartSession(TicketUpdateDNAActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetRouteFromAsynk().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } while (c.moveToNext());

        }

    }

    private class GetRouteFromAsynk extends AsyncTask<Integer, String, Integer> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TicketUpdateDNAActivity.this);
            progressDialog.setMessage("Please wait data loading...");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetRouteFrom + "?ItemMasterId=" + WarrantyCode + "&segType=" + section;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_GetRouteFrom, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GetRouteFrom, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_GetRouteFrom, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();

            getroutefrom();


        }

    }

    private class GetChangeRouteTo extends AsyncTask<Integer, String, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress_section_to.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetChangeRouteTo + "?fromRouteMstrId=" + PKRouteMasterId + "&segType=" + section + "&itemPlantId=" + ItemPlantId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_GetRouteTo, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GetRouteTo, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_GetRouteTo, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            //  dismissProgressDialog();
            progress_section_to.setVisibility(View.GONE);
            getrouteto();


        }

    }

    private void getroutefrom() {
//sReferenceType, sReference,sEntity,sConsignee;
        Routefromlist.clear();
        String query = "SELECT distinct PKRouteMasterId,Point" +
                " FROM " + db.TABLE_GetRouteFrom;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Routefromlist.add(cur.getString(cur.getColumnIndex("Point")));

            } while (cur.moveToNext());

        }
        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                R.layout.vwb_custom_spinner_txt, Routefromlist);
        spinner_from.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_from.setSelection(0);
    }

    private void getrouteto() {
//sReferenceType, sReference,sEntity,sConsignee;
        RouteTolist.clear();
        String query = "SELECT distinct PKRouteMasterId,Point" +
                " FROM " + db.TABLE_GetRouteTo;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                RouteTolist.add(cur.getString(cur.getColumnIndex("Point")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                R.layout.vwb_custom_spinner_txt, RouteTolist);
        spinner_to.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_to.setSelection(0);
    }

    private void getGetBindcategory() {
//sReferenceType, sReference,sEntity,sConsignee;
        Getbindcategorylist.clear();
        String query = "SELECT distinct PKProblemCategoryMaster,Category" +
                " FROM " + db.TABLE_GetBindCategory;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Getbindcategorylist.add(cur.getString(cur.getColumnIndex("Category")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                R.layout.vwb_custom_spinner_txt, Getbindcategorylist);
        spinner_fiber_type.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_fiber_type.setSelection(0);
    }

    class PostRouteUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(TicketUpdateDNAActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveRoute;
            System.out.println("BusinessAPIURL-1 :" + RouteJson);

            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
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

            Toast.makeText(TicketUpdateDNAActivity.this, "Update succcessfully", Toast.LENGTH_LONG).show();


        }

    }

    private class GetPrevAffectedClientIfo extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetChkIsClientAffected + "?ActivityId=" + ActivityId;
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

            System.out.println("Output :" + s);

            if (s.equalsIgnoreCase("Y")) {

                txt_affect_customer.setVisibility(View.VISIBLE
                );
            }
            dismissProgressDialog();
        }
    }

    private ArrayList<String> getWarehouse() {

        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data.add(cur.getString(cur.getColumnIndex("WarehouseDescription")));
            } while (cur.moveToNext());

        }
        return data;
    }

    private Boolean CheckWarehouse() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getWarehouseID(String name) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE + " WHERE WarehouseDescription='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("WareHouseMasterId"));
            } while (cur.moveToNext());

        }
        return data;
    }

    private ArrayList<String> getLocation(String id) {

        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_LOCATION + " WHERE WareHouseMasterId='" + id + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data.add(cur.getString(cur.getColumnIndex("LocationDesc")));
            } while (cur.moveToNext());

        }
        return data;
    }

    private Boolean CheckLocation(String id) {
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_LOCATION + " WHERE WareHouseMasterId='" + id + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getLocationID(String name) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_LOCATION + " WHERE LocationDesc='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("LocationMasterId"));
            } while (cur.moveToNext());

        }
        return data;
    }

    private ArrayList<String> getmaterialCode() {

        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data.add(cur.getString(cur.getColumnIndex("ItemCode")));
            } while (cur.moveToNext());

        }
        return data;
    }

    private ArrayList<String> getmaterialDiscription() {

        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data.add(cur.getString(cur.getColumnIndex("ItemDesc")));
            } while (cur.moveToNext());

        }
        return data;
    }

    private Boolean Checkmaterialcode() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getmaterialcodeID(String name) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE + " WHERE ItemDesc='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("ItemCode"));
            } while (cur.moveToNext());

        }
        return data;
    }

    private String getmaterialDiscription(String name) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE + " WHERE ItemCode='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("ItemDesc"));
            } while (cur.moveToNext());

        }
        return data;
    }

    private String getmaterialcodePlantrID(String ITEMCODE) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_CODE + " WHERE ItemCode='" + ITEMCODE + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("ItemPlantId"));
            } while (cur.moveToNext());

        }
        return data;
    }

    private class GetMaterialWarehouse extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUserWiseWarehouse+"?UsermasterId="+UserMasterId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_TICKET_UPDATE_WAREHOUSE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_TICKET_UPDATE_WAREHOUSE, null, values);
                    Log.e("Warehouse : ", "" + a);

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
            dismissProgressDialog();
            ArrayList<String> lstWarehouse = getWarehouse();
            MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstWarehouse);
            Spimmer_material_warehouse.setAdapter(customDept);

            Sharedpreferenceticketsetting = getSharedPreferences(WebUrlClass.SHARED_TICKET_UPDATE_MATERIAL_SETTING,
                    Context.MODE_PRIVATE);
            String warehouse = Sharedpreferenceticketsetting.getString(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE, "");
            int warehousepos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, 0);

            if (warehouse.equalsIgnoreCase("")) {
                if(warehousepos==-1){
                    Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                    Spimmer_material_warehouse.setEnabled(false);
                }else {
                    Spimmer_material_warehouse.setEnabled(true);
                    Txt_Mat_setting.setText(getString(R.string.UpTicMat_Default_Setting));
                }

            }else {
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                int locationpos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_POS, 0);
                Spimmer_material_warehouse.setEnabled(false);
                Spimmer_material_warehouse.setSelection(warehousepos);

            }

        }

    }

    private class GetMaterialLocation extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String warehouseID;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUserWiseLocation + "?ids=" + params[0]+"&UserMasterId="+UserMasterId;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_TICKET_UPDATE_LOCATION, null,
                        null);*/
                warehouseID = params[0];
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATE_LOCATION, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);

                        if (columnName.equalsIgnoreCase("WareHouseMasterId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_TICKET_UPDATE_LOCATION, null, values);
                    Log.e("LOcation : ", "" + a);

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
            dismissProgressDialog();
            Sharedpreferenceticketsetting = getSharedPreferences(WebUrlClass.SHARED_TICKET_UPDATE_MATERIAL_SETTING,
                    Context.MODE_PRIVATE);
            String location = Sharedpreferenceticketsetting.getString(WebUrlClass.KEY_TICKET_UPDATE_LOCATION, "");
            int locationpos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_LOCATION_POS, 0);

            if (location.equalsIgnoreCase("")) {
                ArrayList<String> lstloc = getLocation(warehouseID);
                MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                        R.layout.vwb_custom_spinner_txt, lstloc);
                Spimmer_material_location.setAdapter(customDept);
                Spimmer_material_location.setEnabled(true);
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Default_Setting));
                if(locationpos==-1){
                    Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                    Spimmer_material_location.setEnabled(false);
                }
            }else {
                ArrayList<String> lstloc = getLocation(warehouseID);
                MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                        R.layout.vwb_custom_spinner_txt, lstloc);
                Spimmer_material_location.setAdapter(customDept);
                Txt_Mat_setting.setText(getString(R.string.UpTicMat_Change_Setting));
                int warehousepos = Sharedpreferenceticketsetting.getInt(WebUrlClass.KEY_TICKET_UPDATE_WAREHOUSE_POS, 0);
                Spimmer_material_location.setEnabled(false);
                Spimmer_material_location.setSelection(locationpos);

            }
        }

    }

    private class GetmaterialCode extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getTicketUpdationcode + "?id=" + params[0];
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_TICKET_UPDATE_CODE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATE_CODE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_TICKET_UPDATE_CODE, null, values);
                    Log.e("Warehouse : ", "" + a);

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
            dismissProgressDialog();
            ArrayList<String> lstcode = getmaterialCode();
            MySpinnerAdapter customDept = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstcode);
            Spimmer_material_code.setAdapter(customDept);


            ArrayList<String> lstdis = getmaterialDiscription();
            MySpinnerAdapter customDept1 = new MySpinnerAdapter(TicketUpdateDNAActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstdis);
            Spimmer_material_discription.setAdapter(customDept1);
        }

    }

    private class GetStock extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getTicketUpdationgetstock + "?LocationId=" + params[0] + "&warehouseid="
                    + params[1] + "&itemplantid=" + params[2];
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (!(integer.equalsIgnoreCase("error"))) {
                if (integer.equalsIgnoreCase("[]")) {
                    Material_Quant = "0";
                    Material_UOM = "0";
                } else if (integer.contains("Qty")) {
                    JSONArray jResults = null;
                    try {
                        jResults = new JSONArray(response);

                        String columnName, columnValue;
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jorder = jResults.getJSONObject(i);
                            Material_Quant = jorder.getString("Qty");
                            Material_UOM = jorder.getString("stockunit");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Material_Quant = "0";
                        Material_UOM = "0";
                    }

                } else {
                    Material_Quant = "0";
                    Material_UOM = "0";
                }
            } else {
                Material_Quant = "0";
                Material_UOM = "0";
            }

        }

    }

    private class PostMaterial extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostAddMaterial;
            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return params[1];
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();//"True"
            if (!(response.equalsIgnoreCase("error"))) {
                for (int i = 0; i < listMID.size(); i++) {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues CV = new ContentValues();
                    CV.put("IsUpload", "Y");
                    sql.update(db.TABLE_TICKET_UPDATE_MATERIAL_Add, CV, "IDMAT=?", new String[]{listMID.get(i)});
                }

                Toast.makeText(getApplicationContext(), "Material added successfully", Toast.LENGTH_LONG).show();
                Edittext_maerial_Toatalquant.setText("");
                Edittext_maerial_consumedQuant.setText("");
                Edittext_maerial_fieldname.setText("");
                Edittext_maerial_remar.setText("");
            }

        }

    }

    private class GetLocationQty extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetLocationQty+
                    "?LocationId="+Material_locationID+"&ItemMasterId="+materialItemplantID+"&WarehouseMasterId="+warehouseID;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();

            if (integer!=null){
                edt_material_stk_qty.setText(integer);
            }


        }

    }

    private class GetPacketData extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetAllPacketData+
                    "?LocationId="+Material_locationID+"&ItemMasterId="+materialItemplantID+"&WarehouseMasterId="+warehouseID;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();

            if (integer!=null){
                edt_material_stk_qty.setText(integer);
            }


        }

    }
}
