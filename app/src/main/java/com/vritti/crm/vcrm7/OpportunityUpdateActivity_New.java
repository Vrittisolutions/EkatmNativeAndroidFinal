package com.vritti.crm.vcrm7;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.crm.adapter.AttachmentDetailsAdapter;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.OnSwipeTouchListener;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.FileUtilities;
import com.vritti.ekatm.services.ForegroundService;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.expensemanagement.AddExpenseActivity_V1;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static com.vritti.crm.vcrm7.OpportunityUpdateActivity.timeCoversion12to24;
import static com.vritti.crm.vcrm7.ProspectEnterpriseActivity.formateDateFromstring;

public class OpportunityUpdateActivity_New extends AppCompatActivity {
    LinearLayout lay_AssigntoBOE_SE, lay_Reason, lay_Approver, lay_Details, lay_Callagain, lay_Notes,
            lay_Mode, lay_InstrumentNo, lay_BankName, lay_Branch, lay_ChqAmount, lay_TDSAmount,
            lay_DiffAmount, lay_ReasonED, lay_PBT, lay_PTA, lay_Networth, lay_Creditrate,
            lay_PresentBorrowing, lay_currency, lay_rs, lay_Managementcomment, lay_DemoComplete,
            lay_Demo, lay_Date_time_custom, lay_ProductForBank, lay_cospreferred,
            lay_PrepaymentSecuterization, lay_ParticipateInsyndication, lay_Receiveddate,
            lay_PONo, lay_POvalue, lay_Ordertype, lay_Contractreviewrequest, lay_CustomerBudgetSanction,
            lay_CustomerBudget, lay_QuotationValue, lay_QuotationDocument, lay_Quotationno,
            lay_ReassigntoBOE, lay_PresaleSE, lay_SEName, lay_whowillvisit, lay_Whendoyoucall,
            lay_Receivedby, laytime1, laytime2, layhead, lay_gstn, lay_application_no, lay_dochandover, lay_savingacc,
            lay_fdacc, lay_disburseVal, lay_tenure, lay_processed;

    Button button_cancelcontent, btnemailid, btnvisit, btntelep, btnvisit1, btntelep1, btnemailid1;
    ImageView button_previous, button_nextcontent;
    LinearLayout layHdr_one, layHdr_two, lay_content_opp, lay_next_action, len_outcome,len_call_log;
    ImageView img_shuffle;
    boolean frm_to_val = false;
    boolean islayhdrone = true, islayhdrtwo = false, islaycontopp = false, islayfooter = false;
    int layVisCnt = 0;


    AutoCompleteTextView spinner_currency, spinner_rs, spinner_CustomerBudgetSanction, spinner_time_custom, spinner_Receivedby, spinner_Ordertype,
            spinner_demo, spinner_whowillvisit, spinner_ReassigntoBOE, spinner_processed, spinner_Natureofcall, spinner_Initiatedby,
            spinner_With_Towhom, spinner_Reason, spinner_time,spinner_Followupreason, spinner_Outcome, spinner_SEName, spinner_Approver, spinner_Nextaction,
            spinner_AssigntoBOE_SE, spinner_PresaleSE, spinner_Mode, spinner_Callagain, spinner_dochandover;

    Button btngetordertype, buttonminusca, buttonplusca, buttonminusta, buttonplusta, buttonminusda,
            buttonplusda, buttonplusnw, buttonminusnw, buttonpluspta, buttonminuspta, buttonpluspbt,
            buttonminuspbt, buttonSave_opportunity, buttonClose_opportunity, buttonminuspb, buttonpluspb,
            buttonminuscb, buttonpluscb, buttonplusqv, btnclear, btnclear1, btnplay, btnpause;
    TextInputLayout txt_lbl_ordervalue, buttonminusqv, txtwhenyoucall;
    AppCompatButton btnyesterday, btnbefore;
    Button btntommorrow1, btnafter;
    TextView txt3scheduledtimeshow, txt3dateshow;
    public TextView txtcurrentday, txtnamefrm, txtnameto, txtcall, txtta, txtda, txtnw, txtpta, txtpb, txtcb, txtqv, txtpbt,
            editTextFollowupDate, txtpurpose, txtcurrentday_1;
    TextInputEditText txtwhom, txtto;
    public EditText txtca, EdttxtHours, editTextDetails, editTextWhendoyoucall,
            editTextDate, editTextDate_custom, editTextReceiveddate, editTextNotes, editTextInstrumentNo,
            editTextBankName, editTextBranch, editTextReason, editTextCreditrate,
            editTextManagementcomment, editTextProductForBank, editTextcospreferred,
            editTextPrepaymentSecuterization, editTextParticipateInsyndication, editTextPONo,
            editTextPOvalue, editTextQuotationno, editTextQuotationDocument, editTextGSTINo,
            edit_applicaton_no, edit_savingacc, edit_fdacc, edit_disburseVal, edit_tenure,EdttxtNotes;
    CheckBox checkBoxDemoComplete, checkBoxContractreviewrequest;
    TextView edtfrom, edtto;
    public String ProspectId="", ProductId = "";
    int minteger_ca = 0, minteger_ta = 0, minteger_da = 0, minteger_nw = 0, minteger_pta = 0,
            minteger_pbt = 0, minteger_pb = 0, minteger_cb = 0, minteger_qv = 0;
    public String NextActionid="", Natureofcallid="", initiatedbyid="", followupwithid="", followupreasonid="",
            outcomeid="", reasonid="", approverid= "", currencyid="", ordertypeid="", demoresId="", chk_Contractreviewrequest,
            AssignToSEId="", receivedbyid="", whowillvisitid="", presaleseid="", reassignboeid="", senameid="", selected_mode="",
            selected_budget="", selected_rs="", selected_call_again="", selected_custom_time="", selected_time="",
            chk_DemoComplete="", finaljson = "", UserType="", date = "", today="", CurrentDate="",
            selected_from_time="", selected_to_time="", filePath="", currentTime="", callid="", calltype="", firmname="",
            selected_outcome = "", selected_outcome_code = "", getoutcome="", Status="", GSTNINNO="", Telestarttime="", Teleendtime="",
            Flag_is_tele = "", Teleinitiatedby="", TeleCallNature = "", DocHandover = "", processedmode = "";
    String isapprover = "";

    SharedPreferences userpreferences;
    ImageView img_birth_calender, upload;
    EditText edt_Birthdate;
    public String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;

    SQLiteDatabase sql;
    public int a = 1, millis = 1000;
    ArrayList<String> reason, natureofcall, initiatedby, whomwith, followupreason, category, outcome, nextaction,
            approver, currency, ordertype, TMESName;
    int year, month, day;
    String weekDay="";

    SimpleDateFormat dfDate;
    /*    progressHUD1, progressHUD2, progressHUD3, progressHUD4, progressHUD5,
        progressHUD6, progressHUD7, progressHUD8, progressHUD9,
        progressHUD10, progressHUD11;*/
    Uri attachment;
    public Context context;
    DateFormat dateFormat;
    Calendar cl;
    int thour, tmin, tohour, tomin;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String table="", tablename = "", tablename_partial = "", status;
    String starttime = "", endtime = "", duration = "", type = "", MobileCallType = "", rowNo = "", date_Before = "",
            date_Current="", Contact_DateBirth="";

    private long mLastClickTime = 0;
    ProgressDialog progressDialog;
    ProgressBar progressbar;

    boolean flagIsEnterprise = false;
    int positionReceivedby = 0, SELECT_IMAGE = 2;
    private SimpleDateFormat simpleDateFormat;
    ImageView img_contact, timer;
    String amPm="";
    DatePickerDialog datePickerDialog;


    ImageView img_add, img_refresh, img_back, img_add_1;
    TextView txt_title;
    private boolean isFollowup = false, IsOutcome = false,
            isReason = false, Contact = false, Approver = false,
            Receive = false, Schedule = false,isOrder = false,isAssignBOE=false,
            isAssignSE=false,isDemo=false,isPreSale=false,isHandOver=false,isVisit=false,isCurrency=false,isAssignBOESE=false;
    public final int Followup = 2;

    TextView Firmname, actiondatetime, tv_latestremark, tvcall, txt_expvalue, milestone, txt_prospect, call_rating;
    ImageView callrating, img_nextaction, img_appotunity_update;
    String ffirmname = "", CallId = "", Datetime = "", NextAction = "", sstatus = "", Call = "", Remark = "", Prospect = "", call_type = "", EstimateValue = "";


    String SourceId = "", Milestone = "", Mobile = "";

    ScrollView scroll;
    private String NatureOfCall = "Telephone";
    private String NatureofAction = "Telephone";
    private String Notes = "";

    CardView cardview_outcome;
    ImageView img_contactt;
    TextInputLayout txt_whom;
    ImageView img_fwd, img_back_1, img_fwd_1, img_fwd_2, img_back_2, img_back_3;
    private String ReasonCode = "";
    private Intent intent;
    String LogContact = "";
    private int hour, minute, fromHour, fromMinute;
    int clickYesterday = -1, clickDayBefore = -1;
    private String format="";


    // Attachment Code

    AttachmentBean attachmentBean;
    public static ArrayList<AttachmentBean> attachmentList=new ArrayList<>();
    RecyclerView ls_attachname;
    LinearLayout linear_attachment;
    ImageView image_att_plus;
    private AttachmentDetailsAdapter attachmentDetailsAdapter;
    int selectedPos = -1;
    private ProgressDialog pDialog;
    private final int MEGABYTE = 1024 * 1024;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private static File mediaFile;
    private UUID uuid;
    String uuidInString = "";
    private String  Attachment="";
    public static FirebaseJobDispatcher dispatcherNew;
    public static Job myJobNew = null;
    File file;
    private static int RESULT_LOAD_IMG = 22;

    private static final int RESULT_CAPTURE_IMG = 3;
    private static final int RESULT_DOCUMENT = 4;
    private Uri outPutfileUri;
    private int APP_REQUEST_CODE = 4478;

    private String Flag="",Start="";
    private SharedPreferences AtendanceSheredPreferance;
    private int check=0;
    private String Duration="",CalllogDuration;
    private Date d;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_opportunity_update_v1);
        context = OpportunityUpdateActivity_New.this;
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, "");
        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES, Context.MODE_PRIVATE);

        dateFormat = new SimpleDateFormat("HH:mm");
        cl = Calendar.getInstance();
        init_layout();
        init_spinner();
        init();

        currentTime = dateFormat.format(cl.getTime());
        dfDate = new SimpleDateFormat("dd/MM/yyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //calendar.add(Calendar.MONTH, +6);
        Date newDate = calendar.getTime();
        String whendoucall = dfDate.format(newDate);
        simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        gettomorowdate();
        //    selected_from_time = currentTime;


        //   spinner_From.setText(selected_from_time);
        //from time should be half hr less than current time

        CurrentDate = dfDate.format(new Date());
        editTextFollowupDate.setText(CurrentDate);


        intent = getIntent();
        if (intent.hasExtra("nature")) {
            Flag_is_tele = intent.getStringExtra("nature");
            Telestarttime = intent.getStringExtra("starttime");
            Teleendtime = intent.getStringExtra("endtime");
            Teleinitiatedby = intent.getStringExtra("initiatedby");
            TeleCallNature = intent.getStringExtra("Callnature");

        } else {
            Flag_is_tele = "";
        }
        if (getIntent().hasExtra("Flag")){
            Flag=intent.getStringExtra("Flag");
            Start=intent.getStringExtra("Start");
        }
        if (intent.hasExtra("type")) {
            Flag_is_tele = "telephone";
            type = intent.getStringExtra("type");
            starttime = intent.getStringExtra("starttime");

            String Tstarttime = intent.getStringExtra("starttime");
            //Telestarttime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm", Tstarttime);

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            try {
                d = dateformat.parse(Tstarttime /*your date as String*/);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Telestarttime = output.format(d);

            Log.d("Date format", "output Startdate :" + Telestarttime);

            edtfrom.setText(Telestarttime);
            endtime = intent.getStringExtra("endtime");
            Teleendtime = intent.getStringExtra("endtime");
           // Teleendtime = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "hh:mm aa", Teleendtime);
            dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            output = new SimpleDateFormat("HH:mm");
            try {
                d = dateformat.parse(Teleendtime /*your date as String*/);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Teleendtime = output.format(d);
            Log.d("Date format", "output Enddate :" + Teleendtime);
            edtto.setText(Teleendtime);

            Duration = intent.getStringExtra("duration");
           try{
            String namepass[] = Duration.split(":");
            String hh = namepass[1];
            String mm = namepass[2];
            Duration=hh+":"+mm;
            //Log.d("Duration",Duration);

            EdttxtHours.setText(Duration);

            }catch (Exception e){
                e.printStackTrace();
            }

            MobileCallType = intent.getStringExtra("MobileCalltype");
            rowNo = intent.getStringExtra("rowNo");
            CurrentDate = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "dd/MM/yyyy", Tstarttime);
            editTextFollowupDate.setText(CurrentDate);

            btnyesterday.setEnabled(false);
            btnyesterday.setClickable(false);
            btnbefore.setEnabled(false);
            btnbefore.setClickable(false);
            spinner_With_Towhom.setEnabled(false);
            spinner_With_Towhom.setClickable(false);
            img_shuffle.setEnabled(false);
            img_shuffle.setClickable(false);
            editTextFollowupDate.setEnabled(false);
            editTextFollowupDate.setClickable(false);
            btnemailid.setEnabled(false);
            btnemailid.setClickable(false);
            btnvisit.setEnabled(false);
            btnvisit.setClickable(false);
            edtto.setClickable(false);
            edtfrom.setClickable(false);
            layhead.setVisibility(View.GONE);
            layHdr_two.setVisibility(View.VISIBLE);
            len_call_log.setVisibility(View.GONE);
            txt_title.setText("Call Log Update");

        }
        else {

            if (getIntent().hasExtra("Start")){
                btnemailid.setEnabled(false);
                btnemailid.setClickable(false);
                btntelep.setEnabled(false);
                btntelep.setClickable(false);
                edtto.setClickable(false);
                edtto.setEnabled(false);
                edtfrom.setEnabled(false);
                edtfrom.setClickable(false);
                btnyesterday.setEnabled(false);
                btnyesterday.setClickable(false);
                btnbefore.setEnabled(false);
                btnbefore.setClickable(false);
                editTextFollowupDate.setEnabled(false);
                editTextFollowupDate.setClickable(false);
                layhead.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.VISIBLE);

                getheader_2();

                if (starttime.equals("")) {
                if (Start != null) {

                    try {
                        Start = timeCoversion12to24(Start);
                        edtfrom.setText(Start);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else {
                    edtfrom.setText(currentTime);
                }
            } else {
                edtfrom.setText(starttime);
            }
            }else {
                if (starttime.equals("")) {
                    cl.add(Calendar.MINUTE, -10);
                    String currentTimee = dateFormat.format(cl.getTime());
                    edtfrom.setText(currentTimee);
                    fromHour = Calendar.HOUR;
                    fromMinute = Calendar.MINUTE - 10;
                    //edtfrom.setText(currentTime);
                    edtto.setText(starttime);
                } else {
                    cl.add(Calendar.MINUTE, -10);
                    String currentTimee = dateFormat.format(cl.getTime());
                    edtfrom.setText(currentTimee);
                    fromHour = Calendar.HOUR;
                    fromMinute = Calendar.MINUTE - 10;
                    edtto.setText(starttime);


                }
                if (!endtime.equals("")) {
                    edtto.setText(endtime);
                }
                if (!Duration.equals("")) {
                    EdttxtHours.setText(Duration);
                }
                //   editTextFollowupDate.setText(CurrentDate);
                //  txt3dateshow.setText(whendoucall);
                //   txt3dateshow.setText("03/06/2021");

                editTextReceiveddate.setText(CurrentDate);
            }
            // txt3dateshow.setText("03/06/2021");
            // txt3dateshow.setText(CurrentDate);
        }
        callid = intent.getStringExtra("callid");
        calltype = intent.getStringExtra("calltype");
        firmname = intent.getStringExtra("firmname");
        table = intent.getStringExtra("table");
        ProspectId = intent.getStringExtra("ProspectId");

        if (getIntent().hasExtra("callmob")) {
            LogContact = getIntent().getStringExtra("callmob");
        }

        if (table.equalsIgnoreCase("Call")) {
            tablename = db.TABLE_CRM_CALL;
            tablename_partial = db.TABLE_CRM_CALL_PARTIAL;
        }
        else if (table.equalsIgnoreCase("Opportunity")) {
            tablename = db.TABLE_CRM_CALL_OPP;
            tablename_partial = db.TABLE_CRM_CALL_PARTIAL_OPP;
        }
        else if (table.equalsIgnoreCase("Callfromcalllogs")) {
            tablename = db.TABLE_CRM_CALL;
            tablename_partial = db.TABLE_CRM_CALL_PARTIAL;

        }

        if (calltype.equalsIgnoreCase("1")) {
            getoutcome = "Sales";
        }
        else if (calltype.equalsIgnoreCase("2")) {
            getoutcome = "Collection";
        }
        else if (calltype.equalsIgnoreCase("3")) {
            getoutcome = "Feedback";
            layhead.setVisibility(View.GONE);
        }


        if (selected_outcome.equalsIgnoreCase("Call Close Without Order")) {
            editTextWhendoyoucall.setText(whendoucall);
        } else {
            editTextWhendoyoucall.setText(CurrentDate);
        }
        if (UserType.equalsIgnoreCase("SE")) {
            laytime2.setVisibility(View.GONE);
            laytime1.setVisibility(View.VISIBLE);
        } else if (UserType.equalsIgnoreCase("BOE")) {
            laytime2.setVisibility(View.VISIBLE);
            laytime1.setVisibility(View.VISIBLE);
        }

        long date1 = System.currentTimeMillis();


        cl = Calendar.getInstance();
        cl.add(Calendar.MINUTE, -10);
        String currentTimee = dateFormat.format(cl.getTime());
        if (getIntent().hasExtra("Start")){

        }else {
            edtfrom.setText(currentTimee);
        }
        edtto.setText(currentTime);


        sql = db.getWritableDatabase();
        getData();

        setListrner();

        if (cf.getNatureOfcallcount() > 0) {
            getNextAction();
            getNatureofCall();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadNatureOfCallJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        }

        if (cf.getInitiatedbycount() > 0) {
            getInitiatedby();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadInitiatedByJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        /*if (cf.getwhomwithcount() > 0) {
            getWhomwith();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFollowupWithJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }*/


        if (cf.getFollowupreasoncount() > 0) {
            getFollowupreason();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFollowupReasonJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }

        if (cf.getOutcomecount(getoutcome) > 0) {
            getOutcome();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOutcomeJSON().execute();


                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }

        if (cf.getReasonMastercount() > 0) {
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadReasonJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }

        if (cf.getCategorycount() > 0) {
            getCategory();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
            getCategory();
        }

        if (cf.getTMESNamecount() > 0) {
            getTMESName();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTMESENameJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
            // getTMESName();


        }


        if (cf.getOrdertypecount() > 0) {
            getOrdertype();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOrdertypeJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
            getOrdertype();
        }


        if (isnet()) {
            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadFollowupWithJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

        }

        if (Flag_is_tele.equalsIgnoreCase("Telephone")) {
            edtfrom.setText(Telestarttime);
            edtto.setText(Teleendtime);
            if (!type.equalsIgnoreCase("")) {
                EdttxtHours.setText(Duration);
            } else {
                String count = calculate_time_diff(Telestarttime, Teleendtime);
                Log.d("crm_dialog_action", "count" + count);
                EdttxtHours.setText(count);
            }
            //  getWhomwith();
            getInitiatedby();
            getNatureofCall();
            int pos = 0;
            try {
                pos = natureofcall.indexOf("Telephone");
            } catch (Exception e) {
                e.printStackTrace();
                pos = 0;
            }
            // spinner_Natureofcall.setText(natureofcall.get(1));

            int pos1 = 0;
            try {
                pos1 = initiatedby.indexOf(Teleinitiatedby);
            } catch (Exception e) {
                e.printStackTrace();
                pos1 = 0;
            }

            // spinner_Initiatedby.setText(initiatedby.get(1));
        }



    }

    protected void showFileChooser() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("image/*");
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent,
                1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {
                attachment = data.getData();
                filePath = FileUtilities.getPath(OpportunityUpdateActivity_New.this, attachment);
                editTextQuotationDocument.setText(filePath);

            } else {
                Toast.makeText(OpportunityUpdateActivity_New.this,
                        "Nothing Selected.", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_CANCELED) {
            attachment = null;

        } else if (requestCode == Followup && resultCode == Followup) {
            if (isFollowup == true) {
                String Reason = data.getStringExtra("Name");
                followupreasonid = data.getStringExtra("ID");

                spinner_Followupreason.setText(Reason);
                spinner_Followupreason.setError(null);
                Openoutcome();



            } else if (IsOutcome == true) {
                String Outcome = data.getStringExtra("Name");
                outcomeid = data.getStringExtra("ID");

                spinner_Outcome.setText(Outcome);

                String query = "SELECT distinct Outcome,Code" +
                        " FROM " + db.TABLE_Outcome + " WHERE Outcome='" + Outcome + "'";
                Cursor cur = sql.rawQuery(query, null);
                //reason.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        ReasonCode = (cur.getString(cur.getColumnIndex("Code")));

                    } while (cur.moveToNext());

                }

                Outcomedata(outcomeid);
            } else if (isReason == true) {
                String reason = data.getStringExtra("Name");
                reasonid = data.getStringExtra("ID");

                spinner_Reason.setText(reason);
                spinner_Reason.setError(null);

            } else if (Schedule == true) {
                String reason = data.getStringExtra("Name");
                txt3dateshow.setText(reason);
            }
            else if (isOrder == true) {
                String reason = data.getStringExtra("Name");
                ordertypeid = data.getStringExtra("ID");
                spinner_Ordertype.setText(reason);
            }
            else if (isAssignBOE == true) {
                String reason = data.getStringExtra("Name");
                reassignboeid = data.getStringExtra("ID");
                spinner_ReassigntoBOE.setText(reason);
                spinner_ReassigntoBOE.setError(null);
            }
            else if (isAssignBOESE == true) {
                String reason = data.getStringExtra("Name");
                reassignboeid = data.getStringExtra("ID");
                spinner_AssigntoBOE_SE.setText(reason);
                spinner_AssigntoBOE_SE.setError(null);
            }
            else if (isPreSale == true) {
                String reason = data.getStringExtra("Name");
                presaleseid = data.getStringExtra("ID");
                spinner_PresaleSE.setText(reason);
            }
            else if (isAssignSE == true) {
                String reason = data.getStringExtra("Name");
                senameid = data.getStringExtra("ID");
                spinner_SEName.setText(reason);
            }
            else if (isDemo == true) {
                String reason = data.getStringExtra("Name");
                demoresId = data.getStringExtra("ID");
                spinner_demo.setText(reason);
            } else if (Contact == true) {
                String whom = data.getStringExtra("Name");
                followupwithid = data.getStringExtra("ID");
                spinner_With_Towhom.setText(whom);
                spinner_With_Towhom.setError(null);



                OpenFollouppurpose();

            }

            else if (Approver == true) {
                String Approver = data.getStringExtra("Name");
                spinner_Approver.setText(Approver);
                String query = "SELECT distinct UserName,UserMasterID" +
                        " FROM " + db.TABLE_APPROVER +
                        " WHERE UserName='" + Approver + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        approverid = cur.getString(cur.getColumnIndex("UserMasterID"));


                    } while (cur.moveToNext());

                }
            }
            else if (Receive == true) {
                String TMES = data.getStringExtra("Name");
                spinner_Receivedby.setText(TMES);
                String query = "SELECT distinct EkatmUserMasterId,UserName" +
                        " FROM " + db.TABLE_TMESEName + " WHERE UserName='" + TMES + "'";
                if (!sql.isOpen()) {
                    sql = db.getWritableDatabase();
                }
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        receivedbyid = cur.getString(cur.getColumnIndex("EkatmUserMasterId"));


                    } while (cur.moveToNext());

                }


            }
            else if (isHandOver == true) {
                String reason = data.getStringExtra("Name");
                DocHandover = data.getStringExtra("ID");
                spinner_dochandover.setText(reason);
            }
            else if (isVisit == true) {
                String reason = data.getStringExtra("Name");
                whowillvisitid = data.getStringExtra("ID");
                spinner_whowillvisit.setText(reason);
            }
            else if (isCurrency == true) {
                String reason = data.getStringExtra("Name");
                currencyid = data.getStringExtra("ID");
                spinner_currency.setText(reason);
            }
        }
        else if (requestCode == RESULT_CAPTURE_IMG && resultCode == this.RESULT_OK) {
                String uri = outPutfileUri.toString();
                Log.e("uri-:", uri);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    FileOutputStream out = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    outPutfileUri = Uri.parse(url);
                    if (outPutfileUri.toString().contains("content")) {
                        handleSendImage(outPutfileUri);
                    } else {
                        File file = new File(getRealPathFromUri(OpportunityUpdateActivity_New.this, outPutfileUri));//create path from uri
                        Attachment = file.getName();
                        //	CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);
					/*	if (isnet()) {
							new StartSession(context, new CallbackInterface() {
								@Override
								public void callMethod() {
									new PostUploadImageMethodProspect().execute();
								}

								@Override
								public void callfailMethod(String msg) {
									ut.displayToast(AddExpenseActivity.this, msg);
								}
							});
						} else {
							ut.displayToast(AddExpenseActivity.this, "No Internet connection");
							//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
						}
*/
                    }
                    //Log.d("FileURI",file.getAbsoluteFile().toString());
                    //callChangeProfileImageApi(file.getAbsoluteFile().toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        else if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (data.getData() != null) {
                    outPutfileUri = data.getData();
                    // Get the cursor
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //	uploadFileBitMap = bitmap;
                    file = new File(getRealPathFromURI(outPutfileUri));
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    outPutfileUri = Uri.parse(url);
                    if (outPutfileUri.toString().contains("content")) {
                        try {
                            handleSendImage(outPutfileUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        File file = new File(getRealPathFromUri(OpportunityUpdateActivity_New.this, outPutfileUri));//create path from uri
                        Attachment = file.getName();

                        //CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostUploadImageMethodProspect().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(OpportunityUpdateActivity_New.this, msg);
                                }
                            });
                        } else {
                            ut.displayToast(OpportunityUpdateActivity_New.this, "No Internet connection");
                            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                        }

                    }


                    //img_userpic.setImageURI(fileUri);
                    //callChangeProfileImageApi(file.getAbsoluteFile().toString());


                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode == RESULT_DOCUMENT && null != data) {

                Uri selectedFileURI = data.getData();
                File file = new File(getRealPathFromUri(OpportunityUpdateActivity_New.this, selectedFileURI));//create path from uri
                Log.d("", "File : " + file.getName());
                Attachment = file.getName();
                Toast.makeText(OpportunityUpdateActivity_New.this, "Document send successfully", Toast.LENGTH_SHORT).show();
                //	CreateOfflineSaveAttachment(attachment,attachment,3,"Document send successfully",uuidInString);


                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostUploadImageMethodProspect().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(OpportunityUpdateActivity_New.this, msg);
                        }
                    });
                } else {
                    ut.displayToast(OpportunityUpdateActivity_New.this, "No Internet connection");
                    //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                }

            }


    }

    private void OpenFollouppurpose() {

        Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                CountryListActivity.class);

        IsOutcome = false;
        isFollowup = true;
        isReason = false;
        isOrder = false;
        isAssignBOE = false;
        isAssignBOESE = false;
        isAssignSE = false;
        isDemo = false;
        Receive=false;
        Contact = false;
        Approver=false;
        isHandOver=false;
        isPreSale=false;
        isVisit = false;
        isCurrency = false;
        String url = CompanyURL + WebUrlClass.api_getFollowupReason;
        intent.putExtra("Table_Name", db.TABLE_Followup_reason);
        intent.putExtra("Id", "PKCallPurposeId");
        intent.putExtra("DispName", "CallPurposeDesc");
        intent.putExtra("WHClauseParameter", "");
        //intent.putExtra("WHClauseParamVal","");
        intent.putExtra("APIName", url);
        intent.putExtra("out", "follow");
        //intent.putExtra("APIParameters","");
        //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
        startActivityForResult(intent, Followup);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);


    }

    private void Openoutcome() {

        Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                CountryListActivity.class);

        IsOutcome = true;
        isFollowup = false;
        isReason = false;
        isOrder = false;
        isAssignBOE = false;
        isAssignBOESE = false;
        isAssignSE = false;
        isDemo = false;
        Receive=false;
        Contact = false;
        Approver=false;
        isHandOver=false;
        isPreSale=false;
        isVisit = false;
        isCurrency = false;

        String url = "";
        if (calltype.equalsIgnoreCase("1")) {
            getoutcome = "Sales";
        } else if (calltype.equalsIgnoreCase("2")) {
            getoutcome = "Collection";
        } else if (calltype.equalsIgnoreCase("3")) {
            getoutcome = "Feedback";
            layhead.setVisibility(View.GONE);
        }
        try {
            url = CompanyURL + WebUrlClass.api_Outcome
                    + "?CallType=" + URLEncoder.encode(getoutcome, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        intent.putExtra("Table_Name", db.TABLE_Outcome);
        intent.putExtra("Id", "Code");
        intent.putExtra("DispName", "Outcome");
        intent.putExtra("WHClauseParameter", "WHERE OutcomeType='" + getoutcome + "'");
        intent.putExtra("APIName", url);
        intent.putExtra("out", "1");
        startActivityForResult(intent, Followup);
        Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
        animation.setDuration(1);
        len_outcome.setAnimation(animation);
        len_outcome.animate();
        animation.start();
        layHdr_one.setVisibility(View.GONE);
        layHdr_two.setVisibility(View.GONE);
        lay_next_action.setVisibility(View.GONE);
        len_outcome.setVisibility(View.VISIBLE);

    }

    private void Outcomedata(String outcomeid) {
        selected_outcome_code = outcomeid;
        //selected_outcome_code = cf.getOutcomecode(outcomeid);
        isapprover = cf.getOutcomeIsapprover(selected_outcome_code);
        lay_gstn.setVisibility(View.GONE);
        lay_content_opp.setVisibility(View.VISIBLE);

        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        if (selected_outcome_code.equalsIgnoreCase("ATS"))
        {
            //  spinner_Nextaction.setSelection();
            lay_AssigntoBOE_SE.setVisibility(View.VISIBLE);
            spinner_AssigntoBOE_SE.setSelection(0);
            lay_Reason.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

            getCategory();
        }
        else if (selected_outcome_code.equalsIgnoreCase("QR"))
        {
            //  spinner_Nextaction.setSelection();
            lay_AssigntoBOE_SE.setVisibility(View.VISIBLE);
            spinner_AssigntoBOE_SE.setSelection(0);
            lay_Reason.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

            getCategory();
        }
        else if (selected_outcome_code.equalsIgnoreCase("CA")
                || selected_outcome_code.equalsIgnoreCase("CustCall")) {
            lay_Reason.setVisibility(View.VISIBLE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
           /* if (cf.getReasonMastercount() > 0) {
                getCall_Reason(selected_outcome_code);
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason(selected_outcome_code);
            }
            spinner_Reason.setSelection(0);
*/
        } else if (selected_outcome_code.equalsIgnoreCase("CC")
                || selected_outcome_code.equalsIgnoreCase("Oreg")) {
            len_outcome.setVisibility(View.VISIBLE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            if (isapprover.equalsIgnoreCase("Y")) {
                lay_Approver.setVisibility(View.VISIBLE);
            } else {
                lay_Approver.setVisibility(View.GONE);
            }
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Details.setVisibility(View.VISIBLE);
            lay_Callagain.setVisibility(View.VISIBLE);
            lay_Notes.setVisibility(View.VISIBLE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            //  txtwhenyoucall.setText("When you call? :");

            /*if (cf.getReasonMastercount() > 0) {
                getCall_Reason(selected_outcome_code);
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("ClsCallWO");
            }*/

           /* if (cf.getApprovercount() > 0) {
               // getApprover();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadApproverJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
               // getApprover();
            }*/

            spinner_Reason.setSelection(0);
            //spinner_Approver.setSelection(0);
            editTextWhendoyoucall.setText(CurrentDate);
            //       txt3dateshow.setText("03/06/2021");
            //txt3dateshow.setText(CurrentDate);
            editTextDetails.setText("");
            spinner_Callagain.setText("");
            editTextNotes.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("OL")) {
            len_outcome.setVisibility(View.VISIBLE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            if (isapprover.equalsIgnoreCase("Y")) {
                lay_Approver.setVisibility(View.VISIBLE);
            } else {
                lay_Approver.setVisibility(View.GONE);
            }
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Details.setVisibility(View.VISIBLE);
            lay_Callagain.setVisibility(View.VISIBLE);
            lay_Notes.setVisibility(View.VISIBLE);

            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            //  txtwhenyoucall.setText("When you call? :");
            /*if (cf.getReasonMastercount() > 0) {
                getCall_Reason("OL");
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("OL");
            }
*/

           /* if (cf.getApprovercount() > 0) {
              //  getApprover();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadApproverJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                //getApprover();
            }*/

            //  spinner_Reason.setSelection(0);
            // spinner_Approver.setSelection(0);
            editTextWhendoyoucall.setText(CurrentDate);
            editTextDetails.setText("");
            // spinner_Callagain.setSelection(2);
            editTextNotes.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("CS")
                || selected_outcome_code.equalsIgnoreCase("FC")
                || selected_outcome_code.equalsIgnoreCase("CRS")
                || selected_outcome_code.equalsIgnoreCase("NG")
                || selected_outcome_code.equalsIgnoreCase("PC")
                || selected_outcome_code.equalsIgnoreCase("PR")
                || selected_outcome.equalsIgnoreCase("Select")
                || selected_outcome_code.equalsIgnoreCase("PFS")
                || selected_outcome_code.equalsIgnoreCase("SPA")
                || selected_outcome_code.equalsIgnoreCase("ATB")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome_code.equalsIgnoreCase("NI")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.VISIBLE);
            editTextDetails.setText("");
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome.equalsIgnoreCase("PS")) {

            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.VISIBLE);
            editTextDetails.setText("");
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.VISIBLE);

            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            // txtwhenyoucall.setText("Due date :");
            if (cf.getCategorycount() > 0) {

                category = new ArrayList();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                Cursor cur = sql.rawQuery(query, null);
                // category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        if (category.size() == 0) {
                            category.add("-Select-");

                        } else {
                            category.add(cur.getString(cur.getColumnIndex("UserName")));
                        }


                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_PresaleSE.setAdapter(dataAdapter);
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCategoryJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }

                category = new ArrayList();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                Cursor cur = sql.rawQuery(query, null);
                // category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        category.add(cur.getString(cur.getColumnIndex("UserName")));

                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_PresaleSE.setAdapter(dataAdapter);
            }

            // spinner_PresaleSE.setSelection(0);
        } else if (selected_outcome_code.equalsIgnoreCase("CPU")) {
            lay_Receivedby.setVisibility(View.GONE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.VISIBLE);
            lay_PTA.setVisibility(View.VISIBLE);
            lay_Networth.setVisibility(View.VISIBLE);
            lay_Creditrate.setVisibility(View.VISIBLE);
            lay_PresentBorrowing.setVisibility(View.VISIBLE);
            lay_currency.setVisibility(View.VISIBLE);
            lay_rs.setVisibility(View.VISIBLE);
            lay_Managementcomment.setVisibility(View.VISIBLE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

            if (cf.getCurrencycount() > 0) {
                getCurrency();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCurrencyMasterJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCurrency();
            }
            txtpbt.setText("0");
            txtpta.setText("0");
            txtnw.setText("0");
            editTextCreditrate.setText("");
            txtpb.setText("0");
            //  spinner_currency.setSelection(0);
            //  spinner_rs.setSelection(0);
            editTextManagementcomment.setText("");

        } else if (selected_outcome_code.equalsIgnoreCase("TTB")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.VISIBLE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

            /*if (cf.getReasonMastercount() > 0) {
                getCall_Reason("TrfBOE");
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("TrfBOE");
            }
*/

            if (cf.getCategorycount() > 0) {
                category = new ArrayList();
                String query = "SELECT distinct UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                Cursor cur = sql.rawQuery(query, null);
                // category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        if (category.size() == 0) {

                            category.add("-Select-");
                        } else {
                            category.add(cur.getString(cur.getColumnIndex("UserName")));
                        }


                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ReassigntoBOE.setAdapter(dataAdapter);
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCategoryJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                category = new ArrayList();
                String query = "SELECT distinct UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                Cursor cur = sql.rawQuery(query, null);
                // category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        category.add(cur.getString(cur.getColumnIndex("UserName")));

                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_ReassigntoBOE.setAdapter(dataAdapter);
            }

        } else if (selected_outcome_code.equalsIgnoreCase("DC")) {
            lay_Receivedby.setVisibility(View.GONE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.VISIBLE);
            checkBoxDemoComplete.setChecked(false);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome_code.equalsIgnoreCase("DRes")) {
            lay_Receivedby.setVisibility(View.GONE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);

            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.VISIBLE);
            lay_Date_time_custom.setVisibility(View.VISIBLE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);


            /*if (cf.getReasonMastercount() > 0) {
                getCall_Reason("DR");
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("DR");
            }*/
            getCategory();
            //   spinner_Reason.setSelection(0);
            //  spinner_demo.setSelection(0);
            editTextDate_custom.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("DReq")) {
            lay_Receivedby.setVisibility(View.GONE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);

            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.VISIBLE);
            lay_Date_time_custom.setVisibility(View.VISIBLE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);


        } else if (selected_outcome_code.equalsIgnoreCase("IU")) {
            lay_Receivedby.setVisibility(View.GONE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.VISIBLE);
            lay_cospreferred.setVisibility(View.VISIBLE);
            lay_PrepaymentSecuterization.setVisibility(View.VISIBLE);
            lay_ParticipateInsyndication.setVisibility(View.VISIBLE);

            editTextProductForBank.setText("");
            editTextcospreferred.setText("");
            editTextParticipateInsyndication.setText("");
            editTextPrepaymentSecuterization.setText("");
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome_code.equalsIgnoreCase("OR")) {
            len_outcome.setVisibility(View.VISIBLE);
            lay_Receivedby.setVisibility(View.VISIBLE);
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.VISIBLE);
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.VISIBLE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.VISIBLE);
            // txt_lbl_ordervalue.setText("Order Value :");
            if (Status.equalsIgnoreCase("false")) {

                lay_gstn.setVisibility(View.VISIBLE);
            }
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.VISIBLE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);


            if (cf.getTMESNamecount() > 0) {
                getTMESName();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadTMESENameJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getTMESName();
            }
            if (cf.getOrdertypecount() > 0) {
                getOrdertype();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadOrdertypeJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getOrdertype();
            }

            // spinner_Receivedby.setSelection(positionReceivedby);
            editTextReceiveddate.setText(CurrentDate);
            editTextPONo.setText("");
            editTextPOvalue.setText("");
            // spinner_Ordertype.setSelection(0);
            checkBoxContractreviewrequest.setChecked(false);
        } else if (selected_outcome_code.equalsIgnoreCase("WCF")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.VISIBLE);
            editTextDetails.setText("");
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

        } else if (selected_outcome_code.equalsIgnoreCase("QS")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.VISIBLE);
            lay_CustomerBudget.setVisibility(View.VISIBLE);
            lay_QuotationValue.setVisibility(View.VISIBLE);
            lay_QuotationDocument.setVisibility(View.VISIBLE);
            lay_Quotationno.setVisibility(View.VISIBLE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);


            spinner_CustomerBudgetSanction.setText("0");
            txtcb.setText("0");
            // buttonminusqv.setText("0");
            editTextQuotationDocument.setText("");
            editTextQuotationno.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("Res")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);

            /*if (cf.getReasonMastercount() > 0) {
                getCall_Reason("Res");
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("Res");
            }*/
            //   spinner_Reason.setSelection(0);

        } else if (selected_outcome_code.equalsIgnoreCase("RTS")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.VISIBLE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);


           /* if (cf.getReasonMastercount() > 0) {
                getCall_Reason("TrfBOE");
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReasonJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getCall_Reason("TrfBOE");
            }*/

            if (cf.getCategorycount() > 0) {
                category = new ArrayList();
                String query = "SELECT distinct UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                Cursor cur = sql.rawQuery(query, null);
                // category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        if (category.size() == 0) {
                            category.add("-Select-");
                        } else {
                            category.add(cur.getString(cur.getColumnIndex("UserName")));
                        }


                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_SEName.setAdapter(dataAdapter);
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCategoryJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                category = new ArrayList();
                String query = "SELECT distinct UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                Cursor cur = sql.rawQuery(query, null);
                //  category.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        category.add(cur.getString(cur.getColumnIndex("UserName")));

                    } while (cur.moveToNext());

                }
                Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_SEName.setAdapter(dataAdapter);
            }
            //  spinner_Reason.setSelection(0);
            //spinner_SEName.setSelection(0);

        } else if (selected_outcome_code.equalsIgnoreCase("SV")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.VISIBLE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            //  txtwhenyoucall.setText("Visit date :");

            if (cf.getTMESNamecount() > 0) {//|| db.getOrdertypeMasterAllcount() > 0
                getTMESName();
            } else {
                if (isnet()) {
                    new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadTMESENameJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
                getTMESName();
            }
            //  spinner_whowillvisit.setSelection(0);
        } else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.VISIBLE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.VISIBLE);
            lay_InstrumentNo.setVisibility(View.VISIBLE);
            lay_BankName.setVisibility(View.VISIBLE);
            lay_Branch.setVisibility(View.VISIBLE);
            lay_ChqAmount.setVisibility(View.VISIBLE);
            lay_TDSAmount.setVisibility(View.VISIBLE);
            lay_DiffAmount.setVisibility(View.VISIBLE);
            lay_ReasonED.setVisibility(View.GONE);
            editTextReason.setText("");
            txtda.setText("0");
            txtta.setText("0");
            txtca.setText("0");
            editTextBranch.setText("");
            editTextBankName.setText("");
            editTextInstrumentNo.setText("");
            //  spinner_Mode.setSelection(0);
            editTextDetails.setText("");

            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);

            // editTextWhendoyoucall.setText(CurrentDate);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            //  txtwhenyoucall.setText("Due date :");


        } else if (selected_outcome_code.equalsIgnoreCase("Disp")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            editTextReason.setText("");
            editTextDetails.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("DCans")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome_code.equalsIgnoreCase("PRMS")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.VISIBLE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.GONE);
            lay_dochandover.setVisibility(View.GONE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
            //  txtwhenyoucall.setText("Next Promise Date :");

            editTextReason.setText("");
        } else if (selected_outcome_code.equalsIgnoreCase("LA")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.VISIBLE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.VISIBLE);
            lay_dochandover.setVisibility(View.VISIBLE);
            lay_savingacc.setVisibility(View.GONE);
            lay_fdacc.setVisibility(View.GONE);
            lay_disburseVal.setVisibility(View.GONE);
            lay_tenure.setVisibility(View.GONE);
            lay_processed.setVisibility(View.GONE);
        } else if (selected_outcome_code.equalsIgnoreCase("LL")) {
            lay_AssigntoBOE_SE.setVisibility(View.GONE);
            lay_Reason.setVisibility(View.GONE);
            lay_Approver.setVisibility(View.GONE);
            lay_Details.setVisibility(View.GONE);
            lay_Callagain.setVisibility(View.GONE);
            lay_Notes.setVisibility(View.GONE);
            lay_Mode.setVisibility(View.GONE);
            lay_InstrumentNo.setVisibility(View.GONE);
            lay_BankName.setVisibility(View.GONE);
            lay_Branch.setVisibility(View.GONE);
            lay_ChqAmount.setVisibility(View.GONE);
            lay_TDSAmount.setVisibility(View.GONE);
            lay_DiffAmount.setVisibility(View.GONE);
            lay_ReasonED.setVisibility(View.GONE);
            lay_PBT.setVisibility(View.GONE);
            lay_PTA.setVisibility(View.GONE);
            lay_Networth.setVisibility(View.GONE);
            lay_Creditrate.setVisibility(View.GONE);
            lay_PresentBorrowing.setVisibility(View.GONE);
            lay_currency.setVisibility(View.GONE);
            lay_rs.setVisibility(View.GONE);
            lay_Managementcomment.setVisibility(View.GONE);
            lay_DemoComplete.setVisibility(View.GONE);
            lay_Demo.setVisibility(View.GONE);
            lay_Date_time_custom.setVisibility(View.GONE);
            lay_ProductForBank.setVisibility(View.GONE);
            lay_cospreferred.setVisibility(View.GONE);
            lay_PrepaymentSecuterization.setVisibility(View.GONE);
            lay_ParticipateInsyndication.setVisibility(View.GONE);
            lay_Receiveddate.setVisibility(View.GONE);
            lay_PONo.setVisibility(View.GONE);
            lay_POvalue.setVisibility(View.GONE);
            lay_Ordertype.setVisibility(View.GONE);
            lay_Contractreviewrequest.setVisibility(View.GONE);
            lay_CustomerBudgetSanction.setVisibility(View.GONE);
            lay_CustomerBudget.setVisibility(View.GONE);
            lay_QuotationValue.setVisibility(View.GONE);
            lay_QuotationDocument.setVisibility(View.GONE);
            lay_ReassigntoBOE.setVisibility(View.GONE);
            lay_PresaleSE.setVisibility(View.GONE);
            lay_SEName.setVisibility(View.GONE);
            lay_whowillvisit.setVisibility(View.GONE);
            lay_Receivedby.setVisibility(View.GONE);
            lay_Whendoyoucall.setVisibility(View.GONE);
            lay_Quotationno.setVisibility(View.GONE);
            lay_application_no.setVisibility(View.VISIBLE);
            lay_dochandover.setVisibility(View.VISIBLE);
            lay_savingacc.setVisibility(View.VISIBLE);
            lay_fdacc.setVisibility(View.VISIBLE);
            lay_disburseVal.setVisibility(View.VISIBLE);
            lay_tenure.setVisibility(View.VISIBLE);
            lay_processed.setVisibility(View.VISIBLE);

        }


        this.outcomeid = "";

        String query = "SELECT distinct Code,PKOutcomeId,Outcome" +
                " FROM " + db.TABLE_Outcome + " WHERE Outcome='"
                + spinner_Outcome.getText().toString() + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                this.outcomeid = cur.getString(cur.getColumnIndex("Code"));


            } while (cur.moveToNext());

        }

    }


    private String calculate_time_diff(String t1, String t2) {

        float min = 0;
        int hours = 0;
        int minutes = 0;
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

            Log.d("crm_dialog_action", "crm_dialog_action" + hours + ":" + minutes);
            Log.d("crm_dialog_action", "crm_dialog_action" + min);
            Log.d("crm_dialog_action", "crm_dialog_action" + sec);
            if (min < 0) {
                min = 0;
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hours + ":" + minutes;


    }
    private String calculate_time_diff_calllog(String t1, String t2) {

        float min = 0;
        int hours = 0;
        int minutes = 0;
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

            Log.d("crm_dialog_action", "crm_dialog_action" + hours + ":" + minutes);
            Log.d("crm_dialog_action", "crm_dialog_action" + min);
            Log.d("crm_dialog_action", "crm_dialog_action" + sec);
            if (min < 0) {
                min = 0;
            } else {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hours + ":" + minutes;


    }


    private void getNextAction() {

        nextaction = new ArrayList();
        String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                " FROM " + db.TABLE_NatureofCall;
        Cursor cur = sql.rawQuery(query, null);
        // nextaction.add("Select");

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (nextaction.size() == 0) {
                    nextaction.add("-Select-");
                } else {
                    nextaction.add(cur.getString(cur.getColumnIndex("NatureofCall")));
                }


            } while (cur.moveToNext());

        }
        Collections.sort(nextaction, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OpportunityUpdateActivity_New.this,
                android.R.layout.simple_spinner_item, nextaction);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Nextaction.setAdapter(dataAdapter);


    }

    private void getNatureofCall() {

        SQLiteDatabase sql = db.getWritableDatabase();
        spinner_Natureofcall = findViewById(R.id.spinner_Natureofcall);
        natureofcall = new ArrayList();
        String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                " FROM " + db.TABLE_NatureofCall;
        Cursor cur = sql.rawQuery(query, null);
        // natureofcall.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (natureofcall.size() == 0) {
                    natureofcall.add("-Select-");
                } else {
                    natureofcall.add(cur.getString(cur.getColumnIndex("NatureofCall")));
                }

            } while (cur.moveToNext());

        }
        Collections.sort(natureofcall, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, natureofcall);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Natureofcall.setAdapter(dataAdapter);


    }

    private void getInitiatedby() {
        SQLiteDatabase sql = db.getWritableDatabase();
        spinner_Initiatedby = findViewById(R.id.spinner_Initiatedby);


        initiatedby = new ArrayList();
        String query = "SELECT distinct PKInitiatedBy,InitiatedBy" +
                " FROM " + db.TABLE_InitiatedBy;
        Cursor cur = sql.rawQuery(query, null);
        // initiatedby.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (initiatedby.size() == 0) {
                    initiatedby.add("-Select-");
                } else {
                    initiatedby.add(cur.getString(cur.getColumnIndex("InitiatedBy")));
                }


            } while (cur.moveToNext());

        }
        Collections.sort(initiatedby, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, initiatedby);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Initiatedby.setAdapter(dataAdapter);

    }

    private void getWhomwith() {

        whomwith = new ArrayList();
        String query = "SELECT distinct PKSuspContactDtlsID,ContactName" +
                " FROM " + db.TABLE_With_whom;
        Cursor cur = sql.rawQuery(query, null);
        whomwith.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                /*if(whomwith.size()==0){
                    whomwith.add("-Select-");
                }else{
                    whomwith.add(cur.getString(cur.getColumnIndex("ContactName")));
                }*/
                whomwith.add(cur.getString(cur.getColumnIndex("ContactName")));

            } while (cur.moveToNext());
        }
        // Collections.sort(whomwith, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, whomwith);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_With_Towhom.setAdapter(dataAdapter);

        if (intent.hasExtra("type")) {
            spinner_With_Towhom.setText(whomwith.get(1).toString());
        } else {
            spinner_With_Towhom.setText(whomwith.get(0).toString());
        }
        if (!type.equalsIgnoreCase("")) {
            if (whomwith.size() == 2) {
                // spinner_With_Towhom.setText(whomwith.get(1));
            }

        }
        if (isnet()) {
            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetCheckGstnNo().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

        }
    }


    private void getFollowupreason() {

        followupreason = new ArrayList();
        String query = "SELECT distinct PKCallPurposeId,CallPurposeDesc" +
                " FROM " + db.TABLE_Followup_reason;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (followupreason.size() == 0) {
                    followupreason.add("Select");
                } else {
                    followupreason.add(cur.getString(cur.getColumnIndex("CallPurposeDesc")));
                }
                followupreason.add(cur.getString(cur.getColumnIndex("CallPurposeDesc")));


            } while (cur.moveToNext());

        }
        Collections.sort(followupreason, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, followupreason);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Followupreason.setAdapter(dataAdapter);
        //spinner_Followupreason.setText(followupreason.get(0).toString());

       /* if (db.getOutcomecount(getoutcome) > 0) {
            getOutcome();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOutcomeJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }*/
    }

    private void getOutcome() {

        outcome = new ArrayList();
        String query = "SELECT distinct PKOutcomeId,Outcome" +
                " FROM " + db.TABLE_Outcome + " WHERE OutcomeType='" + getoutcome + "'";
        Cursor cur = sql.rawQuery(query, null);
        //outcome.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (outcome.size() == 0) {
                    outcome.add("-Select-");
                } else {
                    outcome.add(cur.getString(cur.getColumnIndex("Outcome")));
                }


            } while (cur.moveToNext());

        }
        Collections.sort(outcome, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, outcome);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Outcome.setAdapter(dataAdapter);

        if (cf.getReasonMastercount() > 0) {

        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadReasonJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (cf.getCategorycount() > 0) {
            getCategory();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
            getCategory();
        }
    }

    private void getCall_Reason(String reasontxt) {

        reason = new ArrayList();
        String query = "SELECT distinct ReasonDescription,PKReasonID" +
                " FROM " + db.TABLE_REASON_Master + " WHERE ReasonCode='" + reasontxt + "'";
        Cursor cur = sql.rawQuery(query, null);
        //reason.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (reason.size() == 0) {
                    reason.add("-Select-");
                } else {
                    reason.add(cur.getString(cur.getColumnIndex("ReasonDescription")));
                }


            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, reason);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Reason.setAdapter(dataAdapter);


    }

    private void getCategory() {

        category = new ArrayList();
        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                " FROM " + db.TABLE_Category + " WHERE CRMCategory='1' OR CRMCategory='2'";
        Cursor cur = sql.rawQuery(query, null);
        //category.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (category.size() == 0) {
                    category.add("-Select-");

                } else {
                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                }


            } while (cur.moveToNext());

        }
        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AssigntoBOE_SE.setAdapter(dataAdapter);
        spinner_demo.setAdapter(dataAdapter);
        spinner_dochandover.setAdapter(dataAdapter);

    }

/*
    private void getApprover() {

        approver = new ArrayList();
        String query = "SELECT distinct UserName,UserMasterID" +
                " FROM " + db.TABLE_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        //  approver.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                if (approver.size() == 0) {
                    approver.add("-Select-");
                } else {
                    approver.add(cur.getString(cur.getColumnIndex("UserName")));
                }

                approver.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }
        Collections.sort(approver, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, approver);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Approver.setAdapter(dataAdapter);
    }
*/

    private void getCurrency() {
        currency = new ArrayList();
        String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                " FROM " + db.TABLE_CurrencyMaster;
        Cursor cur = sql.rawQuery(query, null);
        //currency.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                if (currency.size() == 0) {
                    currency.add("-Select-");

                } else {
                    currency.add(cur.getString(cur.getColumnIndex("CurrDesc")));

                }


            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, currency);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(dataAdapter);
    }

    private void getOrdertype() {


        ordertype = new ArrayList();
        String query = "SELECT distinct OrderTypeMasterId,Description" +
                " FROM " + db.TABLE_OrderTypeMaster;
        Cursor cur = sql.rawQuery(query, null);
        // ordertype.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (ordertype.size() == 0) {
                    ordertype.add("-Select-");

                } else {
                    ordertype.add(cur.getString(cur.getColumnIndex("Description")));

                }

            } while (cur.moveToNext());

        }
        Collections.sort(ordertype, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, ordertype);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Ordertype.setAdapter(dataAdapter);


    }

    private void getTMESName() {

        TMESName = new ArrayList();
        TMESName.clear();
        String query = "SELECT distinct EkatmUserMasterId,UserName" +
                " FROM " + db.TABLE_TMESEName;
        Cursor cur = sql.rawQuery(query, null);
        // TMESName.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                if (TMESName.size() == 0) {
                    TMESName.add("-Select-");
                } else {
                    TMESName.add(cur.getString(cur.getColumnIndex("UserName")));
                }


            } while (cur.moveToNext());

        }
        Collections.sort(TMESName, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, TMESName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  spinner_Receivedby.setAdapter(dataAdapter);
        try {
            positionReceivedby = TMESName.indexOf(UserName);
        } catch (Exception e) {
            e.printStackTrace();
            positionReceivedby = 0;
        }
        spinner_whowillvisit.setAdapter(dataAdapter);
    }

    public boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).before(dfDate.parse(fromdate))) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate))) {
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

    public boolean validate_Header() {
        // TODO Auto-generated method stub

        if ((callid.equalsIgnoreCase("") ||
                callid.equalsIgnoreCase(" ") ||
                callid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((ProspectId.equalsIgnoreCase("") ||
                ProspectId.equalsIgnoreCase(" ") ||
                ProspectId.equalsIgnoreCase(null))) {

            return false;
        } else if ((calltype.equalsIgnoreCase("") ||
                calltype.equalsIgnoreCase(" ") ||
                calltype.equalsIgnoreCase(null))) {
            return false;
        } else if ((outcomeid.equalsIgnoreCase("") ||
                outcomeid.equalsIgnoreCase(" ") ||
                outcomeid.equalsIgnoreCase(null))) {
            return false;
        } else if ((UserMasterId.equalsIgnoreCase("") ||
                UserMasterId.equalsIgnoreCase(" ") ||
                UserMasterId.equalsIgnoreCase(null))) {
            return false;
        } else if ((UserName.equalsIgnoreCase("") ||
                UserName.equalsIgnoreCase(" ") ||
                UserName.equalsIgnoreCase(null))) {
            return false;
        } else if ((firmname.equalsIgnoreCase("") ||
                firmname.equalsIgnoreCase(" ") ||
                firmname.equalsIgnoreCase(null))) {
            return false;
        } else {
            return true;
        }


    }

    public boolean validate_callagain() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
           // Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            spinner_Reason.setError("Select Reason");
            spinner_Reason.setFocusable(true);
            spinner_Reason.setFocusableInTouchMode(true);
            spinner_Reason.requestFocus();


            return false;
        } else {
            return true;
        }
    }

    public boolean validate_appointments() {
        // TODO Auto-generated method stub

        if ((AssignToSEId.equalsIgnoreCase("") ||
                AssignToSEId.equalsIgnoreCase(" ") ||
                AssignToSEId.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            spinner_AssigntoBOE_SE.setError("Select Assign to BOE/SE");
            spinner_AssigntoBOE_SE.setFocusable(true);
            spinner_AssigntoBOE_SE.setFocusableInTouchMode(true);
            spinner_AssigntoBOE_SE.requestFocus();

          //  Toast.makeText(context, "Select Assign to BOE/SE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_AssigntoBOE_SE.getText().toString().equalsIgnoreCase("Select") ||
                spinner_AssigntoBOE_SE.getText().toString().equalsIgnoreCase(" ") ||
                spinner_AssigntoBOE_SE.getText().toString().equalsIgnoreCase(null))) {
            spinner_AssigntoBOE_SE.setError("Select Assign to BOE/SE");
            spinner_AssigntoBOE_SE.setFocusable(true);
            spinner_AssigntoBOE_SE.setFocusableInTouchMode(true);
            spinner_AssigntoBOE_SE.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_ttboe() {
        // TODO Auto-generated method stub

        if ((reassignboeid.equalsIgnoreCase("") ||
                reassignboeid.equalsIgnoreCase(" ") ||
                reassignboeid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            spinner_ReassigntoBOE.setError("Select Reassign to BOE");
            spinner_ReassigntoBOE.setFocusable(true);
            spinner_ReassigntoBOE.setFocusableInTouchMode(true);
            spinner_ReassigntoBOE.requestFocus();

          //  Toast.makeText(context, "Select Reassign to BOE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_ReassigntoBOE.getText().toString().equalsIgnoreCase("Select") ||
                spinner_ReassigntoBOE.getText().toString().equalsIgnoreCase(" ") ||
                spinner_ReassigntoBOE.getText().toString().equalsIgnoreCase(null))) {
            spinner_ReassigntoBOE.setError("Select Reassign to BOE");
            spinner_ReassigntoBOE.setFocusable(true);
            spinner_ReassigntoBOE.setFocusableInTouchMode(true);
            spinner_ReassigntoBOE.requestFocus();
            return false;
        } else if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
         //   Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();

            spinner_Reason.setError("Select Reason");
            spinner_Reason.setFocusable(true);
            spinner_Reason.setFocusableInTouchMode(true);
            spinner_Reason.requestFocus();

            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            spinner_Reason.setError("Select Reason");
            spinner_Reason.setFocusable(true);
            spinner_Reason.setFocusableInTouchMode(true);
            spinner_Reason.requestFocus();

            return false;
        } else {
            return true;
        }
    }

    public boolean validate_orderrec() {
        // TODO Auto-generated method stub

        if ((receivedbyid.equalsIgnoreCase("") ||
                receivedbyid.equalsIgnoreCase(" ") ||
                receivedbyid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Received by", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Receivedby.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Receivedby.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Receivedby.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextReceiveddate.getText().toString().equalsIgnoreCase("") ||
                editTextReceiveddate.getText().toString().equalsIgnoreCase(" ") ||
                editTextReceiveddate.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Received date", Toast.LENGTH_LONG).show();
            return false;
        } /*else if ((editTextGSTINo.getText().toString().equalsIgnoreCase("") ||
                editTextGSTINo.getText().toString().equalsIgnoreCase(" ") ||
                editTextGSTINo.getText().toString().equalsIgnoreCase(null))) {
            if (flagIsEnterprise) {
                Toast.makeText(context, "Please enter GSTIN", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }


        } else if ((editTextGSTINo.getText().length() < 15)) {
            if (flagIsEnterprise) {
                Toast.makeText(context, "Please enter 15 digit GSTIN", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }

        }*/ else {
            return true;
        }
    }

    public boolean validate_orderlost() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
           // Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            spinner_Reason.setError("Select Reason");
            spinner_Reason.setFocusable(true);
            spinner_Reason.setFocusableInTouchMode(true);
            spinner_Reason.requestFocus();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            spinner_Reason.setError("Select Reason");
            spinner_Reason.setFocusable(true);
            spinner_Reason.setFocusableInTouchMode(true);
            spinner_Reason.requestFocus();

            return false;
        } else if (((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) && (isapprover.equalsIgnoreCase("Y"))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextDetails.getText().toString().equalsIgnoreCase("") ||
                editTextDetails.getText().toString().equalsIgnoreCase(" ") ||
                editTextDetails.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((selected_call_again.equalsIgnoreCase("") ||
                selected_call_again.equalsIgnoreCase(" ") ||
                selected_call_again.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Call again or not", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();

            return false;
        } else if ((editTextNotes.getText().toString().equalsIgnoreCase("") ||
                editTextNotes.getText().toString().equalsIgnoreCase(" ") ||
                editTextNotes.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Notes", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_visit() {
        // TODO Auto-generated method stub

        if ((whowillvisitid.equalsIgnoreCase("") ||
                whowillvisitid.equalsIgnoreCase(" ") ||
                whowillvisitid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select who will visit", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_whowillvisit.getText().toString().equalsIgnoreCase("Select") ||
                spinner_whowillvisit.getText().toString().equalsIgnoreCase(" ") ||
                spinner_whowillvisit.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select when do you call", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_reschedule() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public boolean validate_ttse() {
        // TODO Auto-generated method stub
        if ((senameid.equalsIgnoreCase("") ||
                senameid.equalsIgnoreCase(" ") ||
                senameid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select SEName", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_SEName.getText().toString().equalsIgnoreCase("Select") ||
                spinner_SEName.getText().toString().equalsIgnoreCase(" ") ||
                spinner_SEName.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public boolean validate_ccwo() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if (((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) && (isapprover.equalsIgnoreCase("Y"))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextDetails.getText().toString().equalsIgnoreCase("") ||
                editTextDetails.getText().toString().equalsIgnoreCase(" ") ||
                editTextDetails.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((selected_call_again.equalsIgnoreCase("") ||
                selected_call_again.equalsIgnoreCase(" ") ||
                selected_call_again.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Call again or not", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextNotes.getText().toString().equalsIgnoreCase("") ||
                editTextNotes.getText().toString().equalsIgnoreCase(" ") ||
                editTextNotes.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Notes", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_disput() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_collection() {
        // TODO Auto-generated method stub

        if ((spinner_Mode.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Mode.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Mode.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Mode", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextInstrumentNo.getText().toString().equalsIgnoreCase("") ||
                editTextInstrumentNo.getText().toString().equalsIgnoreCase(" ") ||
                editTextInstrumentNo.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Instrument No.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextBankName.getText().toString().equalsIgnoreCase("") ||
                editTextBankName.getText().toString().equalsIgnoreCase(" ") ||
                editTextBankName.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Bank Name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextBranch.getText().toString().equalsIgnoreCase("") ||
                editTextBranch.getText().toString().equalsIgnoreCase(" ") ||
                editTextBranch.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Branch", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtca.getText().toString().equalsIgnoreCase("") ||
                txtca.getText().toString().equalsIgnoreCase(" ") ||
                txtca.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");Chq Amount
            Toast.makeText(context, "Enter Chq Amount", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtta.getText().toString().equalsIgnoreCase("") ||
                txtta.getText().toString().equalsIgnoreCase(" ") ||
                txtta.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter TDS Amount", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtda.getText().toString().equalsIgnoreCase("") ||
                txtda.getText().toString().equalsIgnoreCase(" ") ||
                txtda.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Diff Amount ", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextReason.getText().toString().equalsIgnoreCase("") ||
                editTextReason.getText().toString().equalsIgnoreCase(" ") ||
                editTextReason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_wcf() {
        // TODO Auto-generated method stub

        if ((editTextDetails.getText().toString().equalsIgnoreCase("") ||
                editTextDetails.getText().toString().equalsIgnoreCase(" ") ||
                editTextDetails.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Details", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_cpu() {
        // TODO Auto-generated method stub

        if ((txtpbt.getText().toString().equalsIgnoreCase("") ||
                txtpbt.getText().toString().equalsIgnoreCase(" ") ||
                txtpbt.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter EBITDA/PBT ", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtpta.getText().toString().equalsIgnoreCase("") ||
                txtpta.getText().toString().equalsIgnoreCase(" ") ||
                txtpta.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter PTA", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtnw.getText().toString().equalsIgnoreCase("") ||
                txtnw.getText().toString().equalsIgnoreCase(" ") ||
                txtnw.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Net worth", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtpb.getText().toString().equalsIgnoreCase("") ||
                txtpb.getText().toString().equalsIgnoreCase(" ") ||
                txtpb.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Present Borrowing", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextCreditrate.getText().toString().equalsIgnoreCase("") ||
                editTextCreditrate.getText().toString().equalsIgnoreCase(" ") ||
                editTextCreditrate.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Credit rate", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_currency.getText().toString().equalsIgnoreCase("Select") ||
                spinner_currency.getText().toString().equalsIgnoreCase(" ") ||
                spinner_currency.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select currency", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextManagementcomment.getText().toString().equalsIgnoreCase("") ||
                editTextManagementcomment.getText().toString().equalsIgnoreCase(" ") ||
                editTextManagementcomment.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Management comment", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_rs.getText().toString().equalsIgnoreCase("Select") ||
                spinner_rs.getText().toString().equalsIgnoreCase(" ") ||
                spinner_rs.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Currency value", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_iu() {
        // TODO Auto-generated method stub

        if ((editTextProductForBank.getText().toString().equalsIgnoreCase("") ||
                editTextProductForBank.getText().toString().equalsIgnoreCase(" ") ||
                editTextProductForBank.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Ins. product for banks", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextcospreferred.getText().toString().equalsIgnoreCase("") ||
                editTextcospreferred.getText().toString().equalsIgnoreCase(" ") ||
                editTextcospreferred.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Insurance cospreferred", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextPrepaymentSecuterization.getText().toString().equalsIgnoreCase("") ||
                editTextPrepaymentSecuterization.getText().toString().equalsIgnoreCase(" ") ||
                editTextPrepaymentSecuterization.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Prepayment secuterization", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextParticipateInsyndication.getText().toString().equalsIgnoreCase("") ||
                editTextParticipateInsyndication.getText().toString().equalsIgnoreCase(" ") ||
                editTextParticipateInsyndication.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Participate insyndication", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_dr() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((demoresId.equalsIgnoreCase("") ||
                demoresId.equalsIgnoreCase(" ") ||
                demoresId.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Who will give demo?", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_demo.getText().toString().equalsIgnoreCase("Select") ||
                spinner_demo.getText().toString().equalsIgnoreCase(" ") ||
                spinner_demo.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((txt3dateshow.getText().toString().equalsIgnoreCase("") ||
                txt3dateshow.getText().toString().equalsIgnoreCase(" ") ||
                txt3dateshow.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Date", Toast.LENGTH_LONG).show();
            return false;
        } else if ((selected_custom_time.equalsIgnoreCase("") ||
                selected_custom_time.equalsIgnoreCase(" ") ||
                selected_custom_time.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Time", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_dc() {
        // TODO Auto-generated method stub

        if ((chk_DemoComplete.equalsIgnoreCase("") ||
                chk_DemoComplete.equalsIgnoreCase(" ") ||
                chk_DemoComplete.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public boolean validate_cwc() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public boolean validate_orderreg() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getText().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextDetails.getText().toString().equalsIgnoreCase("") ||
                editTextDetails.getText().toString().equalsIgnoreCase(" ") ||
                editTextDetails.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((selected_call_again.equalsIgnoreCase("") ||
                selected_call_again.equalsIgnoreCase(" ") ||
                selected_call_again.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Call again or not", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextNotes.getText().toString().equalsIgnoreCase("") ||
                editTextNotes.getText().toString().equalsIgnoreCase(" ") ||
                editTextNotes.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Notes", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_ps() {
        // TODO Auto-generated method stub

        if ((presaleseid.equalsIgnoreCase("") ||
                presaleseid.equalsIgnoreCase(" ") ||
                presaleseid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Presale SE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_PresaleSE.getText().toString().equalsIgnoreCase("Select") ||
                spinner_PresaleSE.getText().toString().equalsIgnoreCase(" ") ||
                spinner_PresaleSE.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextDetails.getText().toString().equalsIgnoreCase("") ||
                editTextDetails.getText().toString().equalsIgnoreCase(" ") ||
                editTextDetails.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Details", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validate_qs() {
        // TODO Auto-generated method stub

        /*if ((editTextQuotationno.getText().toString().equalsIgnoreCase("") ||
                editTextQuotationno.getText().toString().equalsIgnoreCase(" ") ||
                editTextQuotationno.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Quotation no", Toast.LENGTH_LONG).show();
            return false;
        }*/
        if ((selected_budget.equalsIgnoreCase("") ||
                selected_budget.equalsIgnoreCase(" ") ||
                selected_budget.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Customer budget sanction", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtcb.getText().toString().equalsIgnoreCase("") ||
                txtcb.getText().toString().equalsIgnoreCase(" ") ||
                txtcb.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Quotation no", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtqv.getText().toString().equalsIgnoreCase("") ||
                txtqv.getText().toString().equalsIgnoreCase(" ") ||
                txtqv.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Quotation Value", Toast.LENGTH_LONG).show();
            return false;
        }/* else if ((editTextQuotationDocument.getText().toString().equalsIgnoreCase("") ||
                editTextQuotationDocument.getText().toString().equalsIgnoreCase(" ") ||
                editTextQuotationDocument.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Quotation document", Toast.LENGTH_LONG).show();
            return false;*/ else {
            return true;
        }
    }

    public boolean validate_pdc() {
        // TODO Auto-generated method stub

        if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();
            return false;
        } else if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            edtto.setText("" + mins + ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
//+ ":"+ String.format("%03d", milliseconds)
        }

    };


    private void setListrner() {



        edtfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String a1;
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                a = mcurrentTime.get(Calendar.AM_PM);
                //    int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }
                // String time1 = UpdateTime.updateTime(hour, minute);

                //  edt_andfrom_time.setText(time1);


                mTimePicker = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                if (clickDayBefore == 1 || clickYesterday == 1) {
                                    String time = checkDigit(selectedHour) + ":" +checkDigit(selectedMinute);
                                    fromHour = selectedHour;
                                    fromMinute = selectedMinute;
                                    edtfrom.setText(time);
                                } else {


                                    if (selectedHour <= hour) {
                                        String toTime = "";
                                        if (selectedMinute <= minute) {
                                            if (selectedHour < 24 && selectedHour >= 6) {
                                                String time = checkDigit(selectedHour) + ":" +checkDigit(selectedMinute);
                                                fromHour = selectedHour;
                                                fromMinute = selectedMinute;
                                                edtfrom.setText(time);
                                            /*if(selectedMinute >=60){
                                                selectedHour++;
                                                selectedMinute = selectedMinute - 60;
                                                 toTime = selectedHour+":"+selectedMinute;
                                                edtto.setText(toTime);
                                            }else{
                                                toTime = selectedHour+":"+(selectedMinute+10);
                                                edtto.setText();
                                            }*/
                                            } else {

                                                Toast.makeText(OpportunityUpdateActivity_New.this, "Please select time in between 6:00 AM to 11:59 PM", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } else {
                                        Toast.makeText(OpportunityUpdateActivity_New.this, "Please select time less than current time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                        }, hour, minute, false);// Yes 24 hour time
                // mTimePicker.setMin(hour + 1, minute);
                mTimePicker.setTitle("Select Time");

                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                mTimePicker.updateTime(hour, minute);
                mTimePicker.show();

            }
        });


        edtto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String a1;
                Calendar mcurrentTime = Calendar.getInstance();
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                a = mcurrentTime.get(Calendar.AM_PM);
                //    int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                if (a == 1) {
                    a1 = "PM";
                } else {
                    a1 = "AM";
                }
                // String time1 = UpdateTime.updateTime(hour, minute);

                //  edt_andfrom_time.setText(time1);


                mTimePicker = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                if (clickDayBefore == 1 || clickYesterday == 1) {


                                    if(selectedHour >= fromHour){

                                        String time = checkDigit(selectedHour) + ":" +checkDigit(selectedMinute);
                                        edtto.setText(time);
                                        /*try {
                                            duration = calculate_time_diff(edtfrom.getText().toString(), edtto.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            duration = "0:10";
                                        }*/
                                    }

                                } else {


                                    if (selectedHour <= hour) {
                                        String toTime = "";
                                        if (selectedMinute <= minute) {
                                            if (selectedHour > fromHour) {
                                                if (selectedHour < 24 && selectedHour >= 6) {
                                                    String time = checkDigit(selectedHour) + ":" +checkDigit(selectedMinute);
                                                    edtto.setText(time);
                                                    try {
                                                        duration = calculate_time_diff(edtfrom.getText().toString(), edtto.getText().toString());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        duration = "0:10";
                                                    }

                                            /*if(selectedMinute >=60){
                                                selectedHour++;
                                                selectedMinute = selectedMinute - 60;
                                                 toTime = selectedHour+":"+selectedMinute;
                                                edtto.setText(toTime);
                                            }else{
                                                toTime = selectedHour+":"+(selectedMinute+10);
                                                edtto.setText();
                                            }*/
                                                } else {

                                                    Toast.makeText(OpportunityUpdateActivity_New.this, "Please select time in between 6:00 AM to 11:59 PM", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(OpportunityUpdateActivity_New.this, "Please select time greater than from time", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    } else {
                                        Toast.makeText(OpportunityUpdateActivity_New.this, "Please select time less than current time", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        }, hour, minute, false);// Yes 24 hour time
                // mTimePicker.setMin(hour + 1, minute);
                mTimePicker.setTitle("Select Time");


                mTimePicker.show();

            }
        });


        txt3dateshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;



                                txt3dateshow.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();


            }
        });

        layHdr_one.setOnTouchListener(new OnSwipeTouchListener(OpportunityUpdateActivity_New.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_two.setAnimation(animation);
                layHdr_two.animate();
                animation.start();

                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.VISIBLE);
                len_outcome.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.GONE);



            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                onBackPressed();


            }
        });


        layHdr_two.setOnTouchListener(new OnSwipeTouchListener(OpportunityUpdateActivity_New.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = true;
                isFollowup = false;
                isReason = false;
                Contact = false;
                String url = "";
                try {
                    url = CompanyURL + WebUrlClass.api_Outcome
                            + "?CallType=" + URLEncoder.encode(calltype, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                intent.putExtra("Table_Name", db.TABLE_Outcome);
                intent.putExtra("Id", "Code");
                intent.putExtra("DispName", "Outcome");
                intent.putExtra("WHClauseParameter", "WHERE OutcomeType='" + getoutcome + "'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "1");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(1);
                len_outcome.setAnimation(animation);
                len_outcome.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.GONE);
                len_outcome.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_one.setAnimation(animation);
                layHdr_one.animate();
                animation.start();
                layHdr_one.setVisibility(View.VISIBLE);
                layHdr_two.setVisibility(View.GONE);
                lay_content_opp.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);


            }
        });


        len_outcome.setOnTouchListener(new OnSwipeTouchListener(OpportunityUpdateActivity_New.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                len_outcome.setAnimation(animation);
                len_outcome.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_two.setAnimation(animation);
                layHdr_two.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.VISIBLE);
                lay_next_action.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
            }
        });

        lay_next_action.setOnTouchListener(new OnSwipeTouchListener(OpportunityUpdateActivity_New.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

               /* Animation animation   =  AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layoutfooter.setAnimation(animation);
                layoutfooter.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                layoutfooter.setVisibility(View.VISIBLE);
                len_outcome.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_two.setAnimation(animation);
                layHdr_two.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
            }
        });

        button_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layVisCnt >= 1) {
                    layVisCnt = layVisCnt - 1;
                }

                if (layVisCnt == 0) {

                    button_nextcontent.setVisibility(View.VISIBLE);
                    button_previous.setVisibility(View.INVISIBLE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Cancel");
                } else if (layVisCnt == 2) {

                    button_nextcontent.setVisibility(View.GONE);
                    button_previous.setVisibility(View.VISIBLE);
                    buttonSave_opportunity.setVisibility(View.VISIBLE);
                    button_cancelcontent.setText("Close");
                } else {

                    button_nextcontent.setVisibility(View.VISIBLE);
                    button_previous.setVisibility(View.VISIBLE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Cancel");
                }

                handleBtnVisibility();
                //   overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });

        button_nextcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (layVisCnt >= 0 && layVisCnt < 4) {
                    layVisCnt = layVisCnt + 1;
                }

                //islayhdrone = false;
                if (layVisCnt == 0) {
                    button_nextcontent.setVisibility(View.VISIBLE);
                    button_previous.setVisibility(View.GONE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Cancel");
                } else if (layVisCnt == 3) {

                    button_nextcontent.setVisibility(View.GONE);
                    button_previous.setVisibility(View.VISIBLE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Close");
                } else if (layVisCnt == 2) {

                    button_nextcontent.setVisibility(View.VISIBLE);
                    button_previous.setVisibility(View.VISIBLE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Close");
                } else {
                    button_nextcontent.setVisibility(View.VISIBLE);

                    button_previous.setVisibility(View.VISIBLE);
                    buttonSave_opportunity.setVisibility(View.GONE);
                    button_cancelcontent.setText("Cancel");
                }

                handleBtnVisibility();


            }
        });

        button_cancelcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_anim, R.anim.slide_down);

            }
        });

        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
                btnplay.setVisibility(View.GONE);
                EdttxtHours.setText("" + edtto.getText().toString());
                currentTime = dateFormat.format(cl.getTime());
                edtto.setText(currentTime);
            }
        });
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtfrom.getText().toString().equalsIgnoreCase(null) ||
                        edtfrom.getText().toString().equalsIgnoreCase("")) {
                    currentTime = dateFormat.format(cl.getTime());
                    edtfrom.setText(currentTime);
                } else {

                }
                btnplay.setVisibility(View.GONE);
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                btnpause.setVisibility(View.VISIBLE);

            }
        });
        //EdttxtHours.setText("0");
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner_From.setSelection(0);
                //  spinner_To.setSelection(0);
            }
        });
        btnclear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtfrom.setText("");
                edtto.setText("");
                EdttxtHours.setText("0");
                btnplay.setVisibility(View.VISIBLE);
                btnpause.setVisibility(View.GONE);
            }
        });
       /* editTextQuotationDocument.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
*/

        editTextQuotationDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoreImages();
            }
        });

        /*buttonClose_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObj();
                if (Flag_is_tele.equalsIgnoreCase("")) {
                    OpportunityUpdateActivity.this.finish();
                }
            }
        });*/

        txt3scheduledtimeshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                thour = calendar.get(Calendar.HOUR_OF_DAY);
                tmin = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                txt3scheduledtimeshow.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                            }
                        }, thour, tmin, false);

                timePickerDialog.show();
            }
        });
        /*spinner_time_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                thour = calendar.get(Calendar.HOUR_OF_DAY);
                tmin = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = "PM";
                                } else {
                                    amPm = "AM";
                                }
                                spinner_time_custom.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                            }
                        }, thour, tmin, false);

                timePickerDialog.show();
            }
        });
*/

        img_add_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (selected_outcome_code.equalsIgnoreCase("PRMS")){
                    try {
                        String whenToCallString = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", editTextWhendoyoucall.getText().toString().trim());
                        String nextActionString = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", txt3dateshow.getText().toString().trim());


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date whenToCallDate = sdf.parse(whenToCallString);
                        Date nextActionDate = sdf.parse(nextActionString);

                        if(nextActionDate.after(whenToCallDate)){
                            Toast.makeText(OpportunityUpdateActivity_New.this,
                                    "The next action date is greater than current promise date. Please change promise date first.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }*/

                    try {
                        duration = calculate_time_diff(edtfrom.getText().toString(), edtto.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        duration = "0:10";
                    }



                if ((ProductId.equalsIgnoreCase("") ||
                        ProductId.equalsIgnoreCase(" ") ||
                        ProductId.equalsIgnoreCase(null)
                        || ProductId.equalsIgnoreCase("null"))) {
                    ProductId = "";

                }

                String applicationno = edit_applicaton_no.getText().toString();
                if (validate_Header() == true) {
                    /*if (SystemClock.elapsedRealtime() - mLastClickTime < 30000) {

                        Toast.makeText(getApplicationContext(), "You can click only after 30 sec from your first click", Toast.LENGTH_LONG).show();
                        return;
                    }*/
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CallId", callid);
                        jsonObject.put("ProspectId", ProspectId);
                        jsonObject.put("CallType", calltype);
                        jsonObject.put("ProductId", ProductId);
                        jsonObject.put("FollowupDate", editTextFollowupDate.getText().toString());
                        /*if (EdttxtHours.getText().toString().equals("")) {
                            jsonObject.put("FollowupHours", duration);
                        } else {
                            jsonObject.put("FollowupHours", duration);
                        }*/
                        if (intent.hasExtra("type")) {
                            jsonObject.put("FollowupHours", Duration);

                        }else {
                            if (duration.equalsIgnoreCase("0:0")) {
                                jsonObject.put("FollowupHours", "0:10");
                            }else {
                                jsonObject.put("FollowupHours", duration);
                            }
                        }
                        jsonObject.put("FollowupReason", followupreasonid);
                        jsonObject.put("FollowupWith", followupwithid);

                        jsonObject.put("NatureofCall", NatureOfCall);

                        jsonObject.put("FollowupFrom", edtfrom.getText().toString());

                        jsonObject.put("FollowupTo", edtto.getText().toString());


                        if (((((String) spinner_Followupreason.getText().toString().trim()).equalsIgnoreCase("Select")))
                                || (((String) spinner_Followupreason.getText().toString().trim()).equalsIgnoreCase(""))) {
                            jsonObject.put("FollowupReasonName", "");

                        } else {
                            jsonObject.put("FollowupReasonName", ((String) spinner_Followupreason.getText().toString().trim()));
                        }

                        /*if ((((String) spinner_Initiatedby.getText().toString().trim()).equalsIgnoreCase("Select"))
                                || (((String) spinner_Initiatedby.getText().toString().trim()).equalsIgnoreCase(""))) {
                            jsonObject.put("InitiatedBy", "");


                        } else {
                            jsonObject.put("InitiatedBy", spinner_Initiatedby.getText().toString().trim().toString());

                        }*/

                        jsonObject.put("InitiatedBy", txtnamefrm.getText().toString().trim().toString());


                        jsonObject.put("NextAction", NatureofAction);

                        if (((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase("Select")
                                || ((String) spinner_With_Towhom.getText().toString()).equalsIgnoreCase("")
                                || ((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase(" ")
                                || ((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase(null)) {
                            jsonObject.put("FollowupWithName", "");
                        } else {
                            jsonObject.put("FollowupWithName", (String) spinner_With_Towhom.getText().toString());
                        }
                        jsonObject.put("NextActionDateTime", txt3dateshow.getText().toString()
                                + "=" + txt3scheduledtimeshow.getText().toString());

                        Notes = EdttxtNotes.getText().toString();
                        jsonObject.put("Notes", Notes);
                        jsonObject.put("Outcome", outcomeid);
                        jsonObject.put("TMEDisplayId", UserMasterId);
                        jsonObject.put("TMEDisplayName", UserName);
                        jsonObject.put("Firm", firmname);
                        selected_rs = spinner_rs.getText().toString();
                        selected_budget = spinner_CustomerBudgetSanction.getText().toString().trim();
                        processedmode = spinner_processed.getText().toString().trim();
                        selected_mode = spinner_Mode.getText().toString().trim();


                        if (selected_outcome_code.equalsIgnoreCase("CA")
                                || selected_outcome_code.equalsIgnoreCase("CustCall")) {
                            if (validate_callagain() == true) {
                                //Call Again
                                jsonObject.put("CallReason", reasonid);
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");
                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");

                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                Log.d("crm_dialog_action", "json" + finaljson);
                                UpdateOpportunity(finaljson);
                            } else {

                            }
                        }
                        else if (selected_outcome_code.equalsIgnoreCase("ATS")) {
                            if (validate_appointments() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", AssignToSEId);
                                jsonObject.put("AssignToSEName", (String) spinner_AssigntoBOE_SE.getText().toString().trim());
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            }
                            else {

                            }
                        }

                        else if (selected_outcome_code.equalsIgnoreCase("QR")) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");

                                // QR
                                jsonObject.put("QR_AssignToSEBOEId", reassignboeid);
                                jsonObject.put("QR_AssignToSEBOEName", spinner_AssigntoBOE_SE.getText().toString().trim());
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);

                        }
                        else if (selected_outcome_code.equalsIgnoreCase("CC") ||
                                selected_outcome_code.equalsIgnoreCase("Oreg")) {
                            if (validate_ccwo() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", reasonid);
                                jsonObject.put("CC_CallCloseReasonName", spinner_Reason.getText().toString());
                                jsonObject.put("CC_CallCloseApproverId", approverid);
                                jsonObject.put("CC_CallCloseApproverName", spinner_Approver.getText().toString());
                                jsonObject.put("CC_CallCloseDetails", editTextDetails.getText().toString());
                                jsonObject.put("CC_CallCloseCallCustAgain", selected_call_again);
                                jsonObject.put("CC_CallCloseWhenUCall", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("CC_CallCloseNotes", EdttxtNotes.getText().toString());  //EdttxtNotes
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)

                                jsonObject.put("OReg_OrderRegretReasonId", reasonid);
                                jsonObject.put("OReg_OrderRegretReasonName", spinner_Reason.getText().toString());
                                jsonObject.put("OReg_OrderRegretApproverId", approverid);
                                jsonObject.put("OReg_OrderRegretApproverName", spinner_Approver.getText().toString());
                                jsonObject.put("OReg_OrderRegretDetails", editTextDetails.getText().toString());
                                jsonObject.put("CC_CallCloseWhenUCall", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("OReg_OrderRegretOLNotes", EdttxtNotes.getText().toString());
                                if (selected_outcome_code.equalsIgnoreCase("Oreg")){
                                    jsonObject.put("OReg_OrderRegretCallCustAgain", selected_call_again);
                                    jsonObject.put("OReg_OrderRegretWhenUCall", editTextWhendoyoucall.getText().toString());
                                }else {
                                    jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                    jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                }
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("OL")) {
                            if (validate_orderlost() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", reasonid);
                                jsonObject.put("OL_OrderLostReasonName", (String) spinner_Reason.getText().toString().trim());
                                jsonObject.put("OL_OrderLostApproverId", approverid);
                                jsonObject.put("OL_OrderLostApproverName", (String) spinner_Approver.getText().toString().trim());
                                jsonObject.put("OL_OrderLostDetails", editTextDetails.getText().toString());
                                jsonObject.put("OL_OrderLostCallCustAgain", selected_call_again);
                                jsonObject.put("OL_OrderLostWhenUCall", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("OL_OrderLostOLNotes", editTextNotes.getText().toString());
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("PS")) {
                            if (validate_ps() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", presaleseid);
                                jsonObject.put("PS_PresaleSEName", spinner_PresaleSE.getText().toString());
                                jsonObject.put("PS_PresaleDetails", editTextDetails.getText().toString());
                                jsonObject.put("PS_PresaleDueDate", editTextWhendoyoucall.getText().toString());
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("CPU")) {
                            if (validate_cpu() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", txtpbt.getText().toString());
                                jsonObject.put("CPU_txtPAT", txtpta.getText().toString());
                                jsonObject.put("CPU_txtNetworth", txtnw.getText().toString());
                                jsonObject.put("CPU_txtBorrowings", txtpb.getText().toString());
                                jsonObject.put("CPU_txtRatings", editTextCreditrate.getText().toString());
                                jsonObject.put("CPU_ddlCurrency", spinner_currency.getText().toString());
                                jsonObject.put("CPU_txtMComments", editTextManagementcomment.getText().toString());
                                jsonObject.put("CPU_ddlCurrencyVal", spinner_rs.getText().toString());
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("TTB")) {
                            if (validate_ttboe() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", reassignboeid);
                                jsonObject.put("ReAssignToTMEName", (String) spinner_ReassigntoBOE.getText().toString().trim());
                                jsonObject.put("CallReasonTTB", reasonid);
                                jsonObject.put("CallReasonTTBName", (String) spinner_Reason.getText().toString().trim());
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("DC")) {
                            if (validate_dc() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", chk_DemoComplete);
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("DRes")) {
                            if (validate_dr() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", reasonid);
                                jsonObject.put("DRes_DemoResReasonName", spinner_Reason.getText().toString());
                                jsonObject.put("DRes_DemoGivenById", demoresId);
                                jsonObject.put("DRes_DemoGivenByName", spinner_demo.getText().toString());
                                jsonObject.put("DRes_DemoDate", editTextDate_custom.getText().toString());
                                jsonObject.put("DRes_DemoTime", selected_custom_time);

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("DReq")) {

                            //Call Again
                            jsonObject.put("CallReason", "");
                            //Appointment
                            jsonObject.put("AssignToSEId", "");
                            jsonObject.put("AssignToSEName", "");
                            //Transfer to BOE
                            jsonObject.put("ReAssignToTMEId", "");
                            jsonObject.put("ReAssignToTMEName", "");
                            jsonObject.put("CallReasonTTB", "");
                            jsonObject.put("CallReasonTTBName", "");
                            //Order Received
                            jsonObject.put("OR_OrderReceivedById", "");
                            jsonObject.put("OR_OrderReceivedByName", "");
                            jsonObject.put("OR_OrderReceivedDate", "");
                            jsonObject.put("OR_OrderPONo", "");
                            jsonObject.put("OR_OrderPOValue", "");
                            jsonObject.put("OR_OrderContractReview", "");
                            jsonObject.put("OR_OrderType", "");
                            //Order Lost
                            jsonObject.put("OL_OrderLostReasonId", "");
                            jsonObject.put("OL_OrderLostReasonName", "");
                            jsonObject.put("OL_OrderLostApproverId", "");
                            jsonObject.put("OL_OrderLostApproverName", "");
                            jsonObject.put("OL_OrderLostDetails", "");
                            jsonObject.put("OL_OrderLostCallCustAgain", "");
                            jsonObject.put("OL_OrderLostWhenUCall", "");
                            jsonObject.put("OL_OrderLostOLNotes", "");
                            //Visit
                            jsonObject.put("SV_VisitById", "");
                            jsonObject.put("SV_VisitByName", "");
                            jsonObject.put("SV_VisitDate", "");
                            //Reschedule
                            jsonObject.put("Res_RescheduleReasonId", "");
                            jsonObject.put("Res_RescheduleReasonName", "");
                            //Transfer to SE
                            jsonObject.put("RTS_TransferSEId", "");
                            jsonObject.put("RTS_TransferSEName", "");
                            jsonObject.put("RTS_TransferReasonId", "");
                            jsonObject.put("RTS_TransferReasonName", "");
                            //Call Close Without Order
                            jsonObject.put("CC_CallCloseReasonId", "");
                            jsonObject.put("CC_CallCloseReasonName", "");
                            jsonObject.put("CC_CallCloseApproverId", "");
                            jsonObject.put("CC_CallCloseApproverName", "");
                            jsonObject.put("CC_CallCloseDetails", "");
                            jsonObject.put("CC_CallCloseCallCustAgain", "");
                            jsonObject.put("CC_CallCloseWhenUCall", "");
                            jsonObject.put("CC_CallCloseNotes", "");
                            //Disput
                            jsonObject.put("Disp_Reason", "");
                            //COLLCT
                            jsonObject.put("Collect_Mode", "");
                            jsonObject.put("Collect_InstrNo", "");
                            jsonObject.put("Collect_InstrDate", "");
                            jsonObject.put("Collect_BankName", "");
                            jsonObject.put("Collect_BranchName", "");
                            jsonObject.put("Collect_ChqAmount", "");
                            jsonObject.put("Collect_TDSAmount", "");
                            jsonObject.put("Collect_DiffAmount", "");
                            jsonObject.put("Collect_Reason", "");
                            //WI / WCF
                            jsonObject.put("WIWCF_Details", "");
                            //Customer Profile Update (CPU)
                            jsonObject.put("CPU_txtEBITDA", "");
                            jsonObject.put("CPU_txtPAT", "");
                            jsonObject.put("CPU_txtNetworth", "");
                            jsonObject.put("CPU_txtBorrowings", "");
                            jsonObject.put("CPU_txtRatings", "");
                            jsonObject.put("CPU_ddlCurrency", "");
                            jsonObject.put("CPU_txtMComments", "");
                            jsonObject.put("CPU_ddlCurrencyVal", "");
                            //Insurance Update (IU)
                            jsonObject.put("IU_txtInsuBank", "");
                            jsonObject.put("IU_txtInsCos", "");
                            jsonObject.put("IU_txtPreRec", "");
                            jsonObject.put("IU_txtPartIns", "");
                            //Demo Reschedule (DRes)
                            jsonObject.put("DRes_DemoResReasonId", "");
                            jsonObject.put("DRes_DemoResReasonName", "");
                            jsonObject.put("DRes_DemoGivenById", "");
                            jsonObject.put("DRes_DemoGivenByName", "");
                            jsonObject.put("DRes_DemoDate", "");
                            jsonObject.put("DRes_DemoTime", "");

                            //Demo request


                            jsonObject.put("DReq_DemoGivenById", demoresId);

                            String calldate = txt3dateshow.getText().toString();
                            Date initDate = null;
                            try {
                                initDate = new SimpleDateFormat("dd/MM/yyyy").parse(calldate);
                                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                calldate = formatter.format(initDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            jsonObject.put("DReq_DemoDate", calldate);

                           /* DateFormat inputFormat = new SimpleDateFormat("hh:mm aa");
                            try {
                                Date date = inputFormat.parse(selected_custom_time);
                                selected_custom_time = simpleDateFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/
                            //Demo Complete(DC)
                            jsonObject.put("DC_chkdemocomplete", "");
                            //Demo Cancelled (DCans)
                            jsonObject.put("DCans_ReasonId", "");
                            jsonObject.put("DCans_ReasonName", "");
                            //Customer will Call (CustCall)
                            jsonObject.put("CustCall_ReasonId", "");
                            jsonObject.put("CustCall_ReasonName", "");
                            //Order Regret (Oreg)
                            jsonObject.put("OReg_OrderRegretReasonId", "");
                            jsonObject.put("OReg_OrderRegretReasonName", "");
                            jsonObject.put("OReg_OrderRegretApproverId", "");
                            jsonObject.put("OReg_OrderRegretApproverName", "");
                            jsonObject.put("OReg_OrderRegretDetails", "");
                            jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                            jsonObject.put("OReg_OrderRegretWhenUCall", "");
                            jsonObject.put("OReg_OrderRegretOLNotes", "");
                            //Presales Support (PS)
                            jsonObject.put("PS_PresaleSEId", "");
                            jsonObject.put("PS_PresaleSEName", "");
                            jsonObject.put("PS_PresaleDetails", "");
                            jsonObject.put("PS_PresaleDueDate", "");
                            // Quotation Submitted(QS)
                            jsonObject.put("QS_QuotationNo", "");
                            jsonObject.put("QS_CustBudgetSanct", "");
                            jsonObject.put("QS_CustBudget", "");
                            jsonObject.put("QS_QuotationValue", "");
                            jsonObject.put("QS_QuotDoc", "");
                            //Promise Date Change (PRMS)
                            jsonObject.put("PRMS_NextDate", "");
                            jsonObject.put("PRMS_Reason", "");
                            finaljson = jsonObject.toString();
                            finaljson = finaljson.replaceAll("\\\\", "");
                            finaljson = finaljson.replaceAll(" ", " ");
                            finaljson = finaljson.replaceAll("=", " ");
                            UpdateOpportunity(finaljson);

                            Log.d("crm_dialog_action", "json" + finaljson);

                        } else if (selected_outcome_code.equalsIgnoreCase("IU")) {
                            if (validate_iu() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", editTextProductForBank.getText().toString());
                                jsonObject.put("IU_txtInsCos", editTextcospreferred.getText().toString());
                                jsonObject.put("IU_txtPreRec", editTextPrepaymentSecuterization.getText().toString());
                                jsonObject.put("IU_txtPartIns", editTextParticipateInsyndication.getText().toString());
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("OR")) {

                            if (validate_orderrec() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                if (Status.equalsIgnoreCase("false")) {
                                    jsonObject.put("OR_GSTIN", editTextGSTINo.getText().toString());
                                } else {
                                    jsonObject.put("OR_GSTIN", "");
                                }
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received

                                jsonObject.put("OR_OrderReceivedById", receivedbyid);
                                jsonObject.put("OR_OrderReceivedByName", spinner_Receivedby.getText().toString());
                                jsonObject.put("OR_OrderReceivedDate", editTextReceiveddate.getText().toString());
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", editTextPOvalue.getText().toString());
                                jsonObject.put("OR_OrderContractReview", chk_Contractreviewrequest);
                                jsonObject.put("OR_OrderType", "");  // ordertypeid
                                jsonObject.put("OR_OrderReceivedWhenUCall", editTextWhendoyoucall.getText().toString());  // ordertypeid
                                jsonObject.put("OR_OrderReceivedCallCustAgain", selected_call_again);  // ordertypeid
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("WCF")) {
                            if (validate_wcf() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", editTextDetails.getText().toString());
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("QS")) {
                            if (validate_qs() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", editTextQuotationno.getText().toString());
                                jsonObject.put("QS_CustBudgetSanct", selected_budget);
                                jsonObject.put("QS_CustBudget", txtcb.getText().toString());
                                jsonObject.put("QS_QuotationValue", txtqv.getText().toString());
                                jsonObject.put("QS_QuotDoc", editTextQuotationDocument.getText().toString());
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("Res")) {
                            if (validate_reschedule() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", reasonid);
                                jsonObject.put("Res_RescheduleReasonName", spinner_Reason.getText().toString());
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("RTS")) {
                            if (validate_ttse() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", senameid);
                                jsonObject.put("RTS_TransferSEName", spinner_SEName.getText().toString());
                                jsonObject.put("RTS_TransferReasonId", reasonid);
                                jsonObject.put("RTS_TransferReasonName", spinner_Reason.getText().toString());
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");


                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("SV")) {
                            if (validate_visit() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", whowillvisitid);
                                jsonObject.put("SV_VisitByName", spinner_whowillvisit.getText().toString());
                                jsonObject.put("SV_VisitDate", editTextWhendoyoucall.getText().toString());
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
                            if (validate_collection() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", spinner_Mode.getText().toString());
                                jsonObject.put("Collect_InstrNo", editTextInstrumentNo.getText().toString());
                                jsonObject.put("Collect_InstrDate", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("Collect_BankName", editTextBankName.getText().toString());
                                jsonObject.put("Collect_BranchName", editTextBranch.getText().toString());
                                jsonObject.put("Collect_ChqAmount", txtca.getText().toString());
                                jsonObject.put("Collect_TDSAmount", txtta.getText().toString());
                                jsonObject.put("Collect_DiffAmount", txtda.getText().toString());
                               // jsonObject.put("Collect_Reason", editTextReason.getText().toString());
                                jsonObject.put("Collect_Reason",reasonid);
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("Disp")) {
                            if (validate_disput() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", reasonid);
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("DCans")) {

                            //Call Again
                            jsonObject.put("CallReason", "");
                            //Appointment
                            jsonObject.put("AssignToSEId", "");
                            jsonObject.put("AssignToSEName", "");
                            //Transfer to BOE
                            jsonObject.put("ReAssignToTMEId", "");
                            jsonObject.put("ReAssignToTMEName", "");
                            jsonObject.put("CallReasonTTB", "");
                            jsonObject.put("CallReasonTTBName", "");
                            //Order Received
                            jsonObject.put("OR_OrderReceivedById", "");
                            jsonObject.put("OR_OrderReceivedByName", "");
                            jsonObject.put("OR_OrderReceivedDate", "");
                            jsonObject.put("OR_OrderPONo", "");
                            jsonObject.put("OR_OrderPOValue", "");
                            jsonObject.put("OR_OrderContractReview", "");
                            jsonObject.put("OR_OrderType", "");
                            //Order Lost
                            jsonObject.put("OL_OrderLostReasonId", "");
                            jsonObject.put("OL_OrderLostReasonName", "");
                            jsonObject.put("OL_OrderLostApproverId", "");
                            jsonObject.put("OL_OrderLostApproverName", "");
                            jsonObject.put("OL_OrderLostDetails", "");
                            jsonObject.put("OL_OrderLostCallCustAgain", "");
                            jsonObject.put("OL_OrderLostWhenUCall", "");
                            jsonObject.put("OL_OrderLostOLNotes", "");
                            //Visit
                            jsonObject.put("SV_VisitById", "");
                            jsonObject.put("SV_VisitByName", "");
                            jsonObject.put("SV_VisitDate", "");
                            //Reschedule
                            jsonObject.put("Res_RescheduleReasonId", "");
                            jsonObject.put("Res_RescheduleReasonName", "");
                            //Transfer to SE
                            jsonObject.put("RTS_TransferSEId", "");
                            jsonObject.put("RTS_TransferSEName", "");
                            jsonObject.put("RTS_TransferReasonId", "");
                            jsonObject.put("RTS_TransferReasonName", "");
                            //Call Close Without Order
                            jsonObject.put("CC_CallCloseReasonId", "");
                            jsonObject.put("CC_CallCloseReasonName", "");
                            jsonObject.put("CC_CallCloseApproverId", "");
                            jsonObject.put("CC_CallCloseApproverName", "");
                            jsonObject.put("CC_CallCloseDetails", "");
                            jsonObject.put("CC_CallCloseCallCustAgain", "");
                            jsonObject.put("CC_CallCloseWhenUCall", "");
                            jsonObject.put("CC_CallCloseNotes", "");
                            //Disput
                            jsonObject.put("Disp_Reason", "");
                            //COLLCT
                            jsonObject.put("Collect_Mode", "");
                            jsonObject.put("Collect_InstrNo", "");
                            jsonObject.put("Collect_InstrDate", "");
                            jsonObject.put("Collect_BankName", "");
                            jsonObject.put("Collect_BranchName", "");
                            jsonObject.put("Collect_ChqAmount", "");
                            jsonObject.put("Collect_TDSAmount", "");
                            jsonObject.put("Collect_DiffAmount", "");
                            jsonObject.put("Collect_Reason", "");
                            //WI / WCF
                            jsonObject.put("WIWCF_Details", "");
                            //Customer Profile Update (CPU)
                            jsonObject.put("CPU_txtEBITDA", "");
                            jsonObject.put("CPU_txtPAT", "");
                            jsonObject.put("CPU_txtNetworth", "");
                            jsonObject.put("CPU_txtBorrowings", "");
                            jsonObject.put("CPU_txtRatings", "");
                            jsonObject.put("CPU_ddlCurrency", "");
                            jsonObject.put("CPU_txtMComments", "");
                            jsonObject.put("CPU_ddlCurrencyVal", "");
                            //Insurance Update (IU)
                            jsonObject.put("IU_txtInsuBank", "");
                            jsonObject.put("IU_txtInsCos", "");
                            jsonObject.put("IU_txtPreRec", "");
                            jsonObject.put("IU_txtPartIns", "");
                            //Demo Reschedule (DRes)
                            jsonObject.put("DRes_DemoResReasonId", "");
                            jsonObject.put("DRes_DemoResReasonName", "");
                            jsonObject.put("DRes_DemoGivenById", "");
                            jsonObject.put("DRes_DemoGivenByName", "");
                            jsonObject.put("DRes_DemoDate", "");
                            jsonObject.put("DRes_DemoTime", "");

                            //Demo Request
                            jsonObject.put("DReq_DemoGivenById", "");
                            jsonObject.put("DReq_DemoTime", "");
                            jsonObject.put("DReq_DemoDate", "");
                            //Demo Complete(DC)
                            jsonObject.put("DC_chkdemocomplete", "");
                            //Demo Cancelled (DCans)
                            jsonObject.put("DCans_ReasonId", reasonid);
                            jsonObject.put("DCans_ReasonName", "");
                            //Customer will Call (CustCall)
                            jsonObject.put("CustCall_ReasonId", "");
                            jsonObject.put("CustCall_ReasonName", "");
                            //Order Regret (Oreg)
                            jsonObject.put("OReg_OrderRegretReasonId", "");
                            jsonObject.put("OReg_OrderRegretReasonName", "");
                            jsonObject.put("OReg_OrderRegretApproverId", "");
                            jsonObject.put("OReg_OrderRegretApproverName", "");
                            jsonObject.put("OReg_OrderRegretDetails", "");
                            jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                            jsonObject.put("OReg_OrderRegretWhenUCall", "");
                            jsonObject.put("OReg_OrderRegretOLNotes", "");
                            //Presales Support (PS)
                            jsonObject.put("PS_PresaleSEId", "");
                            jsonObject.put("PS_PresaleSEName", "");
                            jsonObject.put("PS_PresaleDetails", "");
                            jsonObject.put("PS_PresaleDueDate", "");
                            // Quotation Submitted(QS)
                            jsonObject.put("QS_QuotationNo", "");
                            jsonObject.put("QS_CustBudgetSanct", "");
                            jsonObject.put("QS_CustBudget", "");
                            jsonObject.put("QS_QuotationValue", "");
                            jsonObject.put("QS_QuotDoc", "");
                            //Promise Date Change (PRMS)
                            jsonObject.put("PRMS_NextDate", "");
                            jsonObject.put("PRMS_Reason", "");
                            finaljson = jsonObject.toString();
                            finaljson = finaljson.replaceAll("\\\\", "");
                            finaljson = finaljson.replaceAll(" ", " ");
                            finaljson = finaljson.replaceAll("=", " ");
                            UpdateOpportunity(finaljson);

                            Log.d("crm_dialog_action", "json" + finaljson);

                        } else if (selected_outcome_code.equalsIgnoreCase("PRMS")) {
                            try {
                                String whenToCallString = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", editTextWhendoyoucall.getText().toString().trim());
                                String nextActionString = formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd", txt3dateshow.getText().toString().trim());


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date whenToCallDate = sdf.parse(whenToCallString);
                                Date nextActionDate = sdf.parse(nextActionString);

                                if(nextActionDate.after(whenToCallDate)){
                                    Toast.makeText(OpportunityUpdateActivity_New.this,
                                            "The next action date is greater than current promise date. Please change promise date first.",
                                            Toast.LENGTH_LONG).show();
                                }else {


                                    if (validate_pdc() == true) {
                                        //Call Again
                                        jsonObject.put("CallReason", "");
                                        //Appointment
                                        jsonObject.put("AssignToSEId", "");
                                        jsonObject.put("AssignToSEName", "");
                                        //Transfer to BOE
                                        jsonObject.put("ReAssignToTMEId", "");
                                        jsonObject.put("ReAssignToTMEName", "");
                                        jsonObject.put("CallReasonTTB", "");
                                        jsonObject.put("CallReasonTTBName", "");
                                        //Order Received
                                        jsonObject.put("OR_OrderReceivedById", "");
                                        jsonObject.put("OR_OrderReceivedByName", "");
                                        jsonObject.put("OR_OrderReceivedDate", "");
                                        jsonObject.put("OR_OrderPONo", "");
                                        jsonObject.put("OR_OrderPOValue", "");
                                        jsonObject.put("OR_OrderContractReview", "");
                                        jsonObject.put("OR_OrderType", "");
                                        //Order Lost
                                        jsonObject.put("OL_OrderLostReasonId", "");
                                        jsonObject.put("OL_OrderLostReasonName", "");
                                        jsonObject.put("OL_OrderLostApproverId", "");
                                        jsonObject.put("OL_OrderLostApproverName", "");
                                        jsonObject.put("OL_OrderLostDetails", "");
                                        jsonObject.put("OL_OrderLostCallCustAgain", "");
                                        jsonObject.put("OL_OrderLostWhenUCall", "");
                                        jsonObject.put("OL_OrderLostOLNotes", "");
                                        //Visit
                                        jsonObject.put("SV_VisitById", "");
                                        jsonObject.put("SV_VisitByName", "");
                                        jsonObject.put("SV_VisitDate", "");
                                        //Reschedule
                                        jsonObject.put("Res_RescheduleReasonId", "");
                                        jsonObject.put("Res_RescheduleReasonName", "");
                                        //Transfer to SE
                                        jsonObject.put("RTS_TransferSEId", "");
                                        jsonObject.put("RTS_TransferSEName", "");
                                        jsonObject.put("RTS_TransferReasonId", "");
                                        jsonObject.put("RTS_TransferReasonName", "");
                                        //Call Close Without Order
                                        jsonObject.put("CC_CallCloseReasonId", "");
                                        jsonObject.put("CC_CallCloseReasonName", "");
                                        jsonObject.put("CC_CallCloseApproverId", "");
                                        jsonObject.put("CC_CallCloseApproverName", "");
                                        jsonObject.put("CC_CallCloseDetails", "");
                                        jsonObject.put("CC_CallCloseCallCustAgain", "");
                                        jsonObject.put("CC_CallCloseWhenUCall", "");
                                        jsonObject.put("CC_CallCloseNotes", "");
                                        //Disput
                                        jsonObject.put("Disp_Reason", "");
                                        //COLLCT
                                        jsonObject.put("Collect_Mode", "");
                                        jsonObject.put("Collect_InstrNo", "");
                                        jsonObject.put("Collect_InstrDate", "");
                                        jsonObject.put("Collect_BankName", "");
                                        jsonObject.put("Collect_BranchName", "");
                                        jsonObject.put("Collect_ChqAmount", "");
                                        jsonObject.put("Collect_TDSAmount", "");
                                        jsonObject.put("Collect_DiffAmount", "");
                                        jsonObject.put("Collect_Reason", "");
                                        //WI / WCF
                                        jsonObject.put("WIWCF_Details", "");
                                        //Customer Profile Update (CPU)
                                        jsonObject.put("CPU_txtEBITDA", "");
                                        jsonObject.put("CPU_txtPAT", "");
                                        jsonObject.put("CPU_txtNetworth", "");
                                        jsonObject.put("CPU_txtBorrowings", "");
                                        jsonObject.put("CPU_txtRatings", "");
                                        jsonObject.put("CPU_ddlCurrency", "");
                                        jsonObject.put("CPU_txtMComments", "");
                                        jsonObject.put("CPU_ddlCurrencyVal", "");
                                        //Insurance Update (IU)
                                        jsonObject.put("IU_txtInsuBank", "");
                                        jsonObject.put("IU_txtInsCos", "");
                                        jsonObject.put("IU_txtPreRec", "");
                                        jsonObject.put("IU_txtPartIns", "");
                                        //Demo Reschedule (DRes)
                                        jsonObject.put("DRes_DemoResReasonId", "");
                                        jsonObject.put("DRes_DemoResReasonName", "");
                                        jsonObject.put("DRes_DemoGivenById", "");
                                        jsonObject.put("DRes_DemoGivenByName", "");
                                        jsonObject.put("DRes_DemoDate", "");
                                        jsonObject.put("DRes_DemoTime", "");

                                        //Demo Request
                                        jsonObject.put("DReq_DemoGivenById", "");
                                        jsonObject.put("DReq_DemoTime", "");
                                        jsonObject.put("DReq_DemoDate", "");
                                        //Demo Complete(DC)
                                        jsonObject.put("DC_chkdemocomplete", "");
                                        //Demo Cancelled (DCans)
                                        jsonObject.put("DCans_ReasonId", "");
                                        jsonObject.put("DCans_ReasonName", "");
                                        //Customer will Call (CustCall)
                                        jsonObject.put("CustCall_ReasonId", "");
                                        jsonObject.put("CustCall_ReasonName", "");
                                        //Order Regret (Oreg)
                                        jsonObject.put("OReg_OrderRegretReasonId", "");
                                        jsonObject.put("OReg_OrderRegretReasonName", "");
                                        jsonObject.put("OReg_OrderRegretApproverId", "");
                                        jsonObject.put("OReg_OrderRegretApproverName", "");
                                        jsonObject.put("OReg_OrderRegretDetails", "");
                                        jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                        jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                        jsonObject.put("OReg_OrderRegretOLNotes", "");
                                        //Presales Support (PS)
                                        jsonObject.put("PS_PresaleSEId", "");
                                        jsonObject.put("PS_PresaleSEName", "");
                                        jsonObject.put("PS_PresaleDetails", "");
                                        jsonObject.put("PS_PresaleDueDate", "");
                                        // Quotation Submitted(QS)
                                        jsonObject.put("QS_QuotationNo", "");
                                        jsonObject.put("QS_CustBudgetSanct", "");
                                        jsonObject.put("QS_CustBudget", "");
                                        jsonObject.put("QS_QuotationValue", "");
                                        jsonObject.put("QS_QuotDoc", "");
                                        //Promise Date Change (PRMS)
                                        jsonObject.put("PRMS_NextDate", editTextWhendoyoucall.getText().toString());
                                        //     jsonObject.put("PRMS_Reason", editTextReason.getText().toString());
                                        jsonObject.put("PRMS_Reason", reasonid);

                                        finaljson = jsonObject.toString();
                                        finaljson = finaljson.replaceAll("\\\\", "");
                                        finaljson = finaljson.replaceAll(" ", " ");
                                        finaljson = finaljson.replaceAll("=", " ");
                                        UpdateOpportunity(finaljson);
                                        Log.d("crm_dialog_action", "json" + finaljson);

                                    } else {

                                    }
                                }
                              }catch (Exception e){
                                e.printStackTrace();
                            }
                        } else if (selected_outcome.equalsIgnoreCase("Customer will Call")) {
                            if (validate_cwc() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", reasonid);
                                jsonObject.put("CustCall_ReasonName", spinner_Reason.getText().toString());
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", "");
                                jsonObject.put("OReg_OrderRegretReasonName", "");
                                jsonObject.put("OReg_OrderRegretApproverId", "");
                                jsonObject.put("OReg_OrderRegretApproverName", "");
                                jsonObject.put("OReg_OrderRegretDetails", "");
                                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                                jsonObject.put("OReg_OrderRegretOLNotes", "");
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);
                                Log.d("crm_dialog_action", "json" + finaljson);
                            } else {

                            }
                        } else if (selected_outcome.equalsIgnoreCase("Order Regret")) {
                            if (validate_orderreg() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", "");
                                jsonObject.put("AssignToSEName", "");
                                //Transfer to BOE
                                jsonObject.put("ReAssignToTMEId", "");
                                jsonObject.put("ReAssignToTMEName", "");
                                jsonObject.put("CallReasonTTB", "");
                                jsonObject.put("CallReasonTTBName", "");
                                //Order Received
                                jsonObject.put("OR_OrderReceivedById", "");
                                jsonObject.put("OR_OrderReceivedByName", "");
                                jsonObject.put("OR_OrderReceivedDate", "");
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", "");
                                jsonObject.put("OR_OrderContractReview", "");
                                jsonObject.put("OR_OrderType", "");
                                //Order Lost
                                jsonObject.put("OL_OrderLostReasonId", "");
                                jsonObject.put("OL_OrderLostReasonName", "");
                                jsonObject.put("OL_OrderLostApproverId", "");
                                jsonObject.put("OL_OrderLostApproverName", "");
                                jsonObject.put("OL_OrderLostDetails", "");
                                jsonObject.put("OL_OrderLostCallCustAgain", "");
                                jsonObject.put("OL_OrderLostWhenUCall", "");
                                jsonObject.put("OL_OrderLostOLNotes", "");
                                //Visit
                                jsonObject.put("SV_VisitById", "");
                                jsonObject.put("SV_VisitByName", "");
                                jsonObject.put("SV_VisitDate", "");
                                //Reschedule
                                jsonObject.put("Res_RescheduleReasonId", "");
                                jsonObject.put("Res_RescheduleReasonName", "");
                                //Transfer to SE
                                jsonObject.put("RTS_TransferSEId", "");
                                jsonObject.put("RTS_TransferSEName", "");
                                jsonObject.put("RTS_TransferReasonId", "");
                                jsonObject.put("RTS_TransferReasonName", "");
                                //Call Close Without Order
                                jsonObject.put("CC_CallCloseReasonId", "");
                                jsonObject.put("CC_CallCloseReasonName", "");
                                jsonObject.put("CC_CallCloseApproverId", "");
                                jsonObject.put("CC_CallCloseApproverName", "");
                                jsonObject.put("CC_CallCloseDetails", "");
                                jsonObject.put("CC_CallCloseCallCustAgain", "");
                                jsonObject.put("CC_CallCloseWhenUCall", "");
                                jsonObject.put("CC_CallCloseNotes", "");
                                //Disput
                                jsonObject.put("Disp_Reason", "");
                                //COLLCT
                                jsonObject.put("Collect_Mode", "");
                                jsonObject.put("Collect_InstrNo", "");
                                jsonObject.put("Collect_InstrDate", "");
                                jsonObject.put("Collect_BankName", "");
                                jsonObject.put("Collect_BranchName", "");
                                jsonObject.put("Collect_ChqAmount", "");
                                jsonObject.put("Collect_TDSAmount", "");
                                jsonObject.put("Collect_DiffAmount", "");
                                jsonObject.put("Collect_Reason", "");
                                //WI / WCF
                                jsonObject.put("WIWCF_Details", "");
                                //Customer Profile Update (CPU)
                                jsonObject.put("CPU_txtEBITDA", "");
                                jsonObject.put("CPU_txtPAT", "");
                                jsonObject.put("CPU_txtNetworth", "");
                                jsonObject.put("CPU_txtBorrowings", "");
                                jsonObject.put("CPU_txtRatings", "");
                                jsonObject.put("CPU_ddlCurrency", "");
                                jsonObject.put("CPU_txtMComments", "");
                                jsonObject.put("CPU_ddlCurrencyVal", "");
                                //Insurance Update (IU)
                                jsonObject.put("IU_txtInsuBank", "");
                                jsonObject.put("IU_txtInsCos", "");
                                jsonObject.put("IU_txtPreRec", "");
                                jsonObject.put("IU_txtPartIns", "");
                                //Demo Reschedule (DRes)
                                jsonObject.put("DRes_DemoResReasonId", "");
                                jsonObject.put("DRes_DemoResReasonName", "");
                                jsonObject.put("DRes_DemoGivenById", "");
                                jsonObject.put("DRes_DemoGivenByName", "");
                                jsonObject.put("DRes_DemoDate", "");
                                jsonObject.put("DRes_DemoTime", "");

                                //Demo Request
                                jsonObject.put("DReq_DemoGivenById", "");
                                jsonObject.put("DReq_DemoTime", "");
                                jsonObject.put("DReq_DemoDate", "");
                                //Demo Complete(DC)
                                jsonObject.put("DC_chkdemocomplete", "");
                                //Demo Cancelled (DCans)
                                jsonObject.put("DCans_ReasonId", "");
                                jsonObject.put("DCans_ReasonName", "");
                                //Customer will Call (CustCall)
                                jsonObject.put("CustCall_ReasonId", "");
                                jsonObject.put("CustCall_ReasonName", "");
                                //Order Regret (Oreg)
                                jsonObject.put("OReg_OrderRegretReasonId", reasonid);
                                jsonObject.put("OReg_OrderRegretReasonName", spinner_Reason.getText().toString());
                                jsonObject.put("OReg_OrderRegretApproverId", approverid);
                                jsonObject.put("OReg_OrderRegretApproverName", spinner_Approver.getText().toString());
                                jsonObject.put("OReg_OrderRegretDetails", editTextDetails.getText().toString());
                                jsonObject.put("OReg_OrderRegretCallCustAgain", selected_call_again);
                                jsonObject.put("OReg_OrderRegretWhenUCall", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("OReg_OrderRegretOLNotes", editTextNotes.getText().toString());
                                //Presales Support (PS)
                                jsonObject.put("PS_PresaleSEId", "");
                                jsonObject.put("PS_PresaleSEName", "");
                                jsonObject.put("PS_PresaleDetails", "");
                                jsonObject.put("PS_PresaleDueDate", "");
                                // Quotation Submitted(QS)
                                jsonObject.put("QS_QuotationNo", "");
                                jsonObject.put("QS_CustBudgetSanct", "");
                                jsonObject.put("QS_CustBudget", "");
                                jsonObject.put("QS_QuotationValue", "");
                                jsonObject.put("QS_QuotDoc", "");
                                //Promise Date Change (PRMS)
                                jsonObject.put("PRMS_NextDate", "");
                                jsonObject.put("PRMS_Reason", "");
                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);

                                Log.d("crm_dialog_action", "json" + finaljson);
                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("CS")
                                || selected_outcome_code.equalsIgnoreCase("FC")
                                || selected_outcome_code.equalsIgnoreCase("CRS")
                                || selected_outcome_code.equalsIgnoreCase("NG")
                                || selected_outcome_code.equalsIgnoreCase("PC")
                                || selected_outcome_code.equalsIgnoreCase("PR")
                                || selected_outcome.equalsIgnoreCase("Select")
                                || selected_outcome_code.equalsIgnoreCase("PFS")
                                || selected_outcome_code.equalsIgnoreCase("SPA")
                                || selected_outcome_code.equalsIgnoreCase("ATB")) {

                            //Call Again
                            jsonObject.put("CallReason", "");
                            //Appointment
                            jsonObject.put("AssignToSEId", "");
                            jsonObject.put("AssignToSEName", "");
                            //Transfer to BOE
                            jsonObject.put("ReAssignToTMEId", "");
                            jsonObject.put("ReAssignToTMEName", "");
                            jsonObject.put("CallReasonTTB", "");
                            jsonObject.put("CallReasonTTBName", "");
                            //Order Received
                            jsonObject.put("OR_OrderReceivedById", "");
                            jsonObject.put("OR_OrderReceivedByName", "");
                            jsonObject.put("OR_OrderReceivedDate", "");
                            jsonObject.put("OR_OrderPONo", "");
                            jsonObject.put("OR_OrderPOValue", "");
                            jsonObject.put("OR_OrderContractReview", "");
                            jsonObject.put("OR_OrderType", "");
                            //Order Lost
                            jsonObject.put("OL_OrderLostReasonId", "");
                            jsonObject.put("OL_OrderLostReasonName", "");
                            jsonObject.put("OL_OrderLostApproverId", "");
                            jsonObject.put("OL_OrderLostApproverName", "");
                            jsonObject.put("OL_OrderLostDetails", "");
                            jsonObject.put("OL_OrderLostCallCustAgain", "");
                            jsonObject.put("OL_OrderLostWhenUCall", "");
                            jsonObject.put("OL_OrderLostOLNotes", "");
                            //Visit
                            jsonObject.put("SV_VisitById", "");
                            jsonObject.put("SV_VisitByName", "");
                            jsonObject.put("SV_VisitDate", "");
                            //Reschedule
                            jsonObject.put("Res_RescheduleReasonId", "");
                            jsonObject.put("Res_RescheduleReasonName", "");
                            //Transfer to SE
                            jsonObject.put("RTS_TransferSEId", "");
                            jsonObject.put("RTS_TransferSEName", "");
                            jsonObject.put("RTS_TransferReasonId", "");
                            jsonObject.put("RTS_TransferReasonName", "");
                            //Call Close Without Order
                            jsonObject.put("CC_CallCloseReasonId", "");
                            jsonObject.put("CC_CallCloseReasonName", "");
                            jsonObject.put("CC_CallCloseApproverId", "");
                            jsonObject.put("CC_CallCloseApproverName", "");
                            jsonObject.put("CC_CallCloseDetails", "");
                            jsonObject.put("CC_CallCloseCallCustAgain", "");
                            jsonObject.put("CC_CallCloseWhenUCall", "");
                            jsonObject.put("CC_CallCloseNotes", "");
                            //Disput
                            jsonObject.put("Disp_Reason", "");
                            //COLLCT
                            jsonObject.put("Collect_Mode", "");
                            jsonObject.put("Collect_InstrNo", "");
                            jsonObject.put("Collect_InstrDate", "");
                            jsonObject.put("Collect_BankName", "");
                            jsonObject.put("Collect_BranchName", "");
                            jsonObject.put("Collect_ChqAmount", "");
                            jsonObject.put("Collect_TDSAmount", "");
                            jsonObject.put("Collect_DiffAmount", "");
                            jsonObject.put("Collect_Reason", "");
                            //WI / WCF
                            jsonObject.put("WIWCF_Details", "");
                            //Customer Profile Update (CPU)
                            jsonObject.put("CPU_txtEBITDA", "");
                            jsonObject.put("CPU_txtPAT", "");
                            jsonObject.put("CPU_txtNetworth", "");
                            jsonObject.put("CPU_txtBorrowings", "");
                            jsonObject.put("CPU_txtRatings", "");
                            jsonObject.put("CPU_ddlCurrency", "");
                            jsonObject.put("CPU_txtMComments", "");
                            jsonObject.put("CPU_ddlCurrencyVal", "");
                            //Insurance Update (IU)
                            jsonObject.put("IU_txtInsuBank", "");
                            jsonObject.put("IU_txtInsCos", "");
                            jsonObject.put("IU_txtPreRec", "");
                            jsonObject.put("IU_txtPartIns", "");
                            //Demo Reschedule (DRes)
                            jsonObject.put("DRes_DemoResReasonId", "");
                            jsonObject.put("DRes_DemoResReasonName", "");
                            jsonObject.put("DRes_DemoGivenById", "");
                            jsonObject.put("DRes_DemoGivenByName", "");
                            jsonObject.put("DRes_DemoDate", "");
                            jsonObject.put("DRes_DemoTime", "");

                            //Demo Request
                            jsonObject.put("DReq_DemoGivenById", "");
                            jsonObject.put("DReq_DemoTime", "");
                            jsonObject.put("DReq_DemoDate", "");
                            //Demo Complete(DC)
                            jsonObject.put("DC_chkdemocomplete", "");
                            //Demo Cancelled (DCans)
                            jsonObject.put("DCans_ReasonId", "");
                            jsonObject.put("DCans_ReasonName", "");
                            //Customer will Call (CustCall)
                            jsonObject.put("CustCall_ReasonId", "");
                            jsonObject.put("CustCall_ReasonName", "");
                            //Order Regret (Oreg)
                            jsonObject.put("OReg_OrderRegretReasonId", "");
                            jsonObject.put("OReg_OrderRegretReasonName", "");
                            jsonObject.put("OReg_OrderRegretApproverId", "");
                            jsonObject.put("OReg_OrderRegretApproverName", "");
                            jsonObject.put("OReg_OrderRegretDetails", "");
                            jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                            jsonObject.put("OReg_OrderRegretWhenUCall", "");
                            jsonObject.put("OReg_OrderRegretOLNotes", "");
                            //Presales Support (PS)
                            jsonObject.put("PS_PresaleSEId", "");
                            jsonObject.put("PS_PresaleSEName", "");
                            jsonObject.put("PS_PresaleDetails", "");
                            jsonObject.put("PS_PresaleDueDate", "");
                            // Quotation Submitted(QS)
                            jsonObject.put("QS_QuotationNo", "");
                            jsonObject.put("QS_CustBudgetSanct", "");
                            jsonObject.put("QS_CustBudget", "");
                            jsonObject.put("QS_QuotationValue", "");
                            jsonObject.put("QS_QuotDoc", "");
                            //Promise Date Change (PRMS)
                            jsonObject.put("PRMS_NextDate", "");
                            jsonObject.put("PRMS_Reason", "");
                            finaljson = jsonObject.toString();
                            finaljson = finaljson.replaceAll("\\\\", "");
                            finaljson = finaljson.replaceAll(" ", " ");
                            finaljson = finaljson.replaceAll("=", " ");
                            UpdateOpportunity(finaljson);

                            Log.d("crm_dialog_action", "json" + finaljson);

                        } else if (selected_outcome_code.equalsIgnoreCase("LA")) {
                            //Call Again
                            jsonObject.put("CallReason", "");
                            //Appointment
                            jsonObject.put("AssignToSEId", "");
                            jsonObject.put("AssignToSEName", "");
                            //Login Assets

                            jsonObject.put("ApplicationNo", edit_applicaton_no.getText().toString());
                            jsonObject.put("DocHOUserId", DocHandover);
                            //Login Lialities
                            jsonObject.put("EXGSavACNo", "");
                            jsonObject.put("FDRDACNo", "");
                            jsonObject.put("DisbursedVal", 0);
                            jsonObject.put("Tenure", 0);
                            jsonObject.put("ProcessedAs", "");
                            //Transfer to BOE
                            jsonObject.put("ReAssignToTMEId", "");
                            jsonObject.put("ReAssignToTMEName", "");
                            jsonObject.put("CallReasonTTB", "");
                            jsonObject.put("CallReasonTTBName", "");
                            //Order Received
                            jsonObject.put("OR_OrderReceivedById", "");
                            jsonObject.put("OR_OrderReceivedByName", "");
                            jsonObject.put("OR_OrderReceivedDate", "");
                            jsonObject.put("OR_OrderPONo", "");
                            jsonObject.put("OR_OrderPOValue", "");
                            jsonObject.put("OR_OrderContractReview", "");
                            jsonObject.put("OR_OrderType", "");
                            //Order Lost
                            jsonObject.put("OL_OrderLostReasonId", "");
                            jsonObject.put("OL_OrderLostReasonName", "");
                            jsonObject.put("OL_OrderLostApproverId", "");
                            jsonObject.put("OL_OrderLostApproverName", "");
                            jsonObject.put("OL_OrderLostDetails", "");
                            jsonObject.put("OL_OrderLostCallCustAgain", "");
                            jsonObject.put("OL_OrderLostWhenUCall", "");
                            jsonObject.put("OL_OrderLostOLNotes", "");
                            //Visit
                            jsonObject.put("SV_VisitById", "");
                            jsonObject.put("SV_VisitByName", "");
                            jsonObject.put("SV_VisitDate", "");
                            //Reschedule
                            jsonObject.put("Res_RescheduleReasonId", "");
                            jsonObject.put("Res_RescheduleReasonName", "");
                            //Transfer to SE
                            jsonObject.put("RTS_TransferSEId", "");
                            jsonObject.put("RTS_TransferSEName", "");
                            jsonObject.put("RTS_TransferReasonId", "");
                            jsonObject.put("RTS_TransferReasonName", "");
                            //Call Close Without Order
                            jsonObject.put("CC_CallCloseReasonId", "");
                            jsonObject.put("CC_CallCloseReasonName", "");
                            jsonObject.put("CC_CallCloseApproverId", "");
                            jsonObject.put("CC_CallCloseApproverName", "");
                            jsonObject.put("CC_CallCloseDetails", "");
                            jsonObject.put("CC_CallCloseCallCustAgain", "");
                            jsonObject.put("CC_CallCloseWhenUCall", "");
                            jsonObject.put("CC_CallCloseNotes", "");
                            //Disput
                            jsonObject.put("Disp_Reason", "");
                            //COLLCT
                            jsonObject.put("Collect_Mode", "");
                            jsonObject.put("Collect_InstrNo", "");
                            jsonObject.put("Collect_InstrDate", "");
                            jsonObject.put("Collect_BankName", "");
                            jsonObject.put("Collect_BranchName", "");
                            jsonObject.put("Collect_ChqAmount", "");
                            jsonObject.put("Collect_TDSAmount", "");
                            jsonObject.put("Collect_DiffAmount", "");
                            jsonObject.put("Collect_Reason", "");
                            //WI / WCF
                            jsonObject.put("WIWCF_Details", "");
                            //Customer Profile Update (CPU)
                            jsonObject.put("CPU_txtEBITDA", "");
                            jsonObject.put("CPU_txtPAT", "");
                            jsonObject.put("CPU_txtNetworth", "");
                            jsonObject.put("CPU_txtBorrowings", "");
                            jsonObject.put("CPU_txtRatings", "");
                            jsonObject.put("CPU_ddlCurrency", "");
                            jsonObject.put("CPU_txtMComments", "");
                            jsonObject.put("CPU_ddlCurrencyVal", "");
                            //Insurance Update (IU)
                            jsonObject.put("IU_txtInsuBank", "");
                            jsonObject.put("IU_txtInsCos", "");
                            jsonObject.put("IU_txtPreRec", "");
                            jsonObject.put("IU_txtPartIns", "");
                            //Demo Reschedule (DRes)
                            jsonObject.put("DRes_DemoResReasonId", "");
                            jsonObject.put("DRes_DemoResReasonName", "");
                            jsonObject.put("DRes_DemoGivenById", "");
                            jsonObject.put("DRes_DemoGivenByName", "");
                            jsonObject.put("DRes_DemoDate", "");
                            jsonObject.put("DRes_DemoTime", "");

                            //Demo Request
                            jsonObject.put("DReq_DemoGivenById", "");
                            jsonObject.put("DReq_DemoTime", "");
                            jsonObject.put("DReq_DemoDate", "");
                            //Demo Complete(DC)
                            jsonObject.put("DC_chkdemocomplete", "");
                            //Demo Cancelled (DCans)
                            jsonObject.put("DCans_ReasonId", "");
                            jsonObject.put("DCans_ReasonName", "");
                            //Customer will Call (CustCall)
                            jsonObject.put("CustCall_ReasonId", "");
                            jsonObject.put("CustCall_ReasonName", "");
                            //Order Regret (Oreg)
                            jsonObject.put("OReg_OrderRegretReasonId", "");
                            jsonObject.put("OReg_OrderRegretReasonName", "");
                            jsonObject.put("OReg_OrderRegretApproverId", "");
                            jsonObject.put("OReg_OrderRegretApproverName", "");
                            jsonObject.put("OReg_OrderRegretDetails", "");
                            jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                            jsonObject.put("OReg_OrderRegretWhenUCall", "");
                            jsonObject.put("OReg_OrderRegretOLNotes", "");
                            //Presales Support (PS)
                            jsonObject.put("PS_PresaleSEId", "");
                            jsonObject.put("PS_PresaleSEName", "");
                            jsonObject.put("PS_PresaleDetails", "");
                            jsonObject.put("PS_PresaleDueDate", "");
                            // Quotation Submitted(QS)
                            jsonObject.put("QS_QuotationNo", "");
                            jsonObject.put("QS_CustBudgetSanct", "");
                            jsonObject.put("QS_CustBudget", "");
                            jsonObject.put("QS_QuotationValue", "");
                            jsonObject.put("QS_QuotDoc", "");
                            //Promise Date Change (PRMS)
                            jsonObject.put("PRMS_NextDate", "");
                            jsonObject.put("PRMS_Reason", "");
                            finaljson = jsonObject.toString();
                            finaljson = finaljson.replaceAll("\\\\", "");
                            finaljson = finaljson.replaceAll(" ", " ");
                            finaljson = finaljson.replaceAll("=", " ");
                            UpdateOpportunity(finaljson);

                            Log.d("crm_dialog_action", "json" + finaljson);

                        } else if (selected_outcome_code.equalsIgnoreCase("LL")) {
                            //Call Again
                            jsonObject.put("CallReason", "");
                            //Appointment
                            jsonObject.put("AssignToSEId", "");
                            jsonObject.put("AssignToSEName", "");
                            //Login Assets
                            jsonObject.put("ApplicationNo", edit_applicaton_no.getText().toString());
                            jsonObject.put("DocHOUserId", DocHandover);
                            //Login Lialities
                            jsonObject.put("EXGSavACNo", edit_savingacc.getText().toString());
                            jsonObject.put("FDRDACNo", edit_fdacc.getText().toString());
                            jsonObject.put("DisbursedVal", edit_disburseVal.getText().toString());
                            jsonObject.put("Tenure", edit_tenure.getText().toString());
                            jsonObject.put("ProcessedAs", processedmode);
                            //Transfer to BOE
                            jsonObject.put("ReAssignToTMEId", "");
                            jsonObject.put("ReAssignToTMEName", "");
                            jsonObject.put("CallReasonTTB", "");
                            jsonObject.put("CallReasonTTBName", "");
                            //Order Received
                            jsonObject.put("OR_OrderReceivedById", "");
                            jsonObject.put("OR_OrderReceivedByName", "");
                            jsonObject.put("OR_OrderReceivedDate", "");
                            jsonObject.put("OR_OrderPONo", "");
                            jsonObject.put("OR_OrderPOValue", "");
                            jsonObject.put("OR_OrderContractReview", "");
                            jsonObject.put("OR_OrderType", "");
                            //Order Lost
                            jsonObject.put("OL_OrderLostReasonId", "");
                            jsonObject.put("OL_OrderLostReasonName", "");
                            jsonObject.put("OL_OrderLostApproverId", "");
                            jsonObject.put("OL_OrderLostApproverName", "");
                            jsonObject.put("OL_OrderLostDetails", "");
                            jsonObject.put("OL_OrderLostCallCustAgain", "");
                            jsonObject.put("OL_OrderLostWhenUCall", "");
                            jsonObject.put("OL_OrderLostOLNotes", "");
                            //Visit
                            jsonObject.put("SV_VisitById", "");
                            jsonObject.put("SV_VisitByName", "");
                            jsonObject.put("SV_VisitDate", "");
                            //Reschedule
                            jsonObject.put("Res_RescheduleReasonId", "");
                            jsonObject.put("Res_RescheduleReasonName", "");
                            //Transfer to SE
                            jsonObject.put("RTS_TransferSEId", "");
                            jsonObject.put("RTS_TransferSEName", "");
                            jsonObject.put("RTS_TransferReasonId", "");
                            jsonObject.put("RTS_TransferReasonName", "");
                            //Call Close Without Order
                            jsonObject.put("CC_CallCloseReasonId", "");
                            jsonObject.put("CC_CallCloseReasonName", "");
                            jsonObject.put("CC_CallCloseApproverId", "");
                            jsonObject.put("CC_CallCloseApproverName", "");
                            jsonObject.put("CC_CallCloseDetails", "");
                            jsonObject.put("CC_CallCloseCallCustAgain", "");
                            jsonObject.put("CC_CallCloseWhenUCall", "");
                            jsonObject.put("CC_CallCloseNotes", "");
                            //Disput
                            jsonObject.put("Disp_Reason", "");
                            //COLLCT
                            jsonObject.put("Collect_Mode", "");
                            jsonObject.put("Collect_InstrNo", "");
                            jsonObject.put("Collect_InstrDate", "");
                            jsonObject.put("Collect_BankName", "");
                            jsonObject.put("Collect_BranchName", "");
                            jsonObject.put("Collect_ChqAmount", "");
                            jsonObject.put("Collect_TDSAmount", "");
                            jsonObject.put("Collect_DiffAmount", "");
                            jsonObject.put("Collect_Reason", "");
                            //WI / WCF
                            jsonObject.put("WIWCF_Details", "");
                            //Customer Profile Update (CPU)
                            jsonObject.put("CPU_txtEBITDA", "");
                            jsonObject.put("CPU_txtPAT", "");
                            jsonObject.put("CPU_txtNetworth", "");
                            jsonObject.put("CPU_txtBorrowings", "");
                            jsonObject.put("CPU_txtRatings", "");
                            jsonObject.put("CPU_ddlCurrency", "");
                            jsonObject.put("CPU_txtMComments", "");
                            jsonObject.put("CPU_ddlCurrencyVal", "");
                            //Insurance Update (IU)
                            jsonObject.put("IU_txtInsuBank", "");
                            jsonObject.put("IU_txtInsCos", "");
                            jsonObject.put("IU_txtPreRec", "");
                            jsonObject.put("IU_txtPartIns", "");
                            //Demo Reschedule (DRes)
                            jsonObject.put("DRes_DemoResReasonId", "");
                            jsonObject.put("DRes_DemoResReasonName", "");
                            jsonObject.put("DRes_DemoGivenById", "");
                            jsonObject.put("DRes_DemoGivenByName", "");
                            jsonObject.put("DRes_DemoDate", "");
                            jsonObject.put("DRes_DemoTime", "");

                            //Demo Request
                            jsonObject.put("DReq_DemoGivenById", "");
                            jsonObject.put("DReq_DemoTime", "");
                            jsonObject.put("DReq_DemoDate", "");
                            //Demo Complete(DC)
                            jsonObject.put("DC_chkdemocomplete", "");
                            //Demo Cancelled (DCans)
                            jsonObject.put("DCans_ReasonId", "");
                            jsonObject.put("DCans_ReasonName", "");
                            //Customer will Call (CustCall)
                            jsonObject.put("CustCall_ReasonId", "");
                            jsonObject.put("CustCall_ReasonName", "");
                            //Order Regret (Oreg)
                            jsonObject.put("OReg_OrderRegretReasonId", "");
                            jsonObject.put("OReg_OrderRegretReasonName", "");
                            jsonObject.put("OReg_OrderRegretApproverId", "");
                            jsonObject.put("OReg_OrderRegretApproverName", "");
                            jsonObject.put("OReg_OrderRegretDetails", "");
                            jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                            jsonObject.put("OReg_OrderRegretWhenUCall", "");
                            jsonObject.put("OReg_OrderRegretOLNotes", "");
                            //Presales Support (PS)
                            jsonObject.put("PS_PresaleSEId", "");
                            jsonObject.put("PS_PresaleSEName", "");
                            jsonObject.put("PS_PresaleDetails", "");
                            jsonObject.put("PS_PresaleDueDate", "");
                            // Quotation Submitted(QS)
                            jsonObject.put("QS_QuotationNo", "");
                            jsonObject.put("QS_CustBudgetSanct", "");
                            jsonObject.put("QS_CustBudget", "");
                            jsonObject.put("QS_QuotationValue", "");
                            jsonObject.put("QS_QuotDoc", "");
                            //Promise Date Change (PRMS)
                            jsonObject.put("PRMS_NextDate", "");
                            jsonObject.put("PRMS_Reason", "");
                            finaljson = jsonObject.toString();
                            finaljson = finaljson.replaceAll("\\\\", "");
                            finaljson = finaljson.replaceAll(" ", " ");
                            finaljson = finaljson.replaceAll("=", " ");
                            UpdateOpportunity(finaljson);

                            Log.d("crm_dialog_action", "json" + finaljson);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wait", Toast.LENGTH_LONG).show();
                }

            }
        });

       /* spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_time = (String) spinner_time.getText().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
       /* spinner_CustomerBudgetSanction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_budget = (String) spinner_CustomerBudgetSanction.getText().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        /*spinner_rs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_rs = spinner_rs.getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_Callagain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        spinner_Callagain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_call_again = spinner_Callagain.getText().toString().trim();
            }
        });



        /*spinner_processed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                processedmode = spinner_processed.getText().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        /*spinner_time_custom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_custom_time = spinner_time_custom.getText().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        /*spinner_Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_mode = spinner_Mode.getText().toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        spinner_SEName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = true;
                Receive=false;
                Approver=false;
                Contact = false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='2'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_PresaleSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                Receive=false;
                Approver=false;
                Contact = false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='2'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_dochandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                Receive=false;
                Approver=false;
                Contact = false;
                isPreSale = false;
                isHandOver = true;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='2' OR CRMCategory='1'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_dochandover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                Receive=false;
                Approver=false;
                Contact = false;
                isPreSale = false;
                isHandOver = true;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='2' OR CRMCategory='1'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_whowillvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                Receive=false;
                Approver=false;
                Contact = false;
                isPreSale = false;
                isHandOver = false;
                isVisit = true;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_getReceivedby;
                intent.putExtra("Table_Name", db.TABLE_TMESEName);
                intent.putExtra("Id", "EkatmUserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });

        spinner_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignSE = false;
                Receive=false;
                isAssignBOESE = false;
                Approver=false;
                Contact = false;
                isPreSale = false;
                isHandOver = false;
                isVisit = false;
                isCurrency = true;


                String url = "";


                url = CompanyURL + WebUrlClass.api_getCurrencyMaster;
                intent.putExtra("Table_Name", db.TABLE_CurrencyMaster);
                intent.putExtra("Id", "CurrencyMasterId");
                intent.putExtra("DispName", "CurrDesc");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });




       /* spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyid = "";
                String currency = (String) spinner_currency.getText().toString();
                if (cf.getCurrencycount() > 0) {
                    // getCurrency();
                    String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                            " FROM " + db.TABLE_CurrencyMaster +
                            " WHERE CurrDesc='" + currency + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            currencyid = cur.getString(cur.getColumnIndex("CurrencyMasterId"));
                        } while (cur.moveToNext());
                    }
                } else {
                    if (isnet()) {
                        new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCurrencyMasterJSON().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    }
                    getCurrency();
                    String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                            " FROM " + db.TABLE_CurrencyMaster +
                            " WHERE CurrDesc='" + currency + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            currencyid = cur.getString(cur.getColumnIndex("CurrencyMasterId"));
                        } while (cur.moveToNext());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

*/
        spinner_Approver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                Approver = true;
                isOrder=false;
                Receive=false;
                isAssignSE=false;
                Contact = false;
                isAssignBOE=false;
                isAssignBOESE = false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";
                try {
                    url = CompanyURL + WebUrlClass.api_getApprover
                            + "?CallId=" + URLEncoder.encode(callid, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                intent.putExtra("Table_Name", db.TABLE_APPROVER);
                intent.putExtra("Id", "UserMasterID");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "appr");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });
        spinner_Receivedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                Approver = false;
                isOrder=false;
                isAssignSE=false;
                isAssignBOE=false;
                isAssignBOESE = false;
                Contact = false;
                Receive = true;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = CompanyURL + WebUrlClass.api_getReceivedby;
                intent.putExtra("Table_Name", db.TABLE_TMESEName);
                intent.putExtra("Id", "EkatmUserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "receive");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });

        spinner_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = true;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='1' OR CRMCategory='2'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });






        spinner_Reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = true;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = CompanyURL + WebUrlClass.api_getReason;
                intent.putExtra("Table_Name", db.TABLE_REASON_Master);
                intent.putExtra("Id", "PKReasonID");
                intent.putExtra("DispName", "ReasonDescription");
                if (ReasonCode.equalsIgnoreCase("CustCall")) {
                    ReasonCode = "CA";
                    intent.putExtra("WHClauseParameter", "WHERE ReasonCode='" + ReasonCode + "'");
                } else {
                    intent.putExtra("WHClauseParameter", "WHERE ReasonCode='" + ReasonCode + "'");
                }
                // intent.putExtra("WHClauseParameter", query);
                intent.putExtra("APIName", url);
                intent.putExtra("out", "reason");
                startActivityForResult(intent, Followup);
            }
        });


        spinner_ReassigntoBOE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = true;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "WHERE CRMCategory='1'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });

        // BOE/SE

        spinner_AssigntoBOE_SE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = true;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_Category;
                intent.putExtra("Table_Name", db.TABLE_Category);
                intent.putExtra("Id", "UserMasterId");
                intent.putExtra("DispName", "UserName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_Nextaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NextActionid = "";
                String sp_na = (String) spinner_Nextaction.getText().toString().trim();
                String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                        " FROM " + db.TABLE_NatureofCall +
                        " WHERE NatureofCall='" + sp_na + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        NextActionid = cur.getString(cur.getColumnIndex("PKNatureofCall"));
                    } while (cur.moveToNext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_Followupreason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = true;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = CompanyURL + WebUrlClass.api_getFollowupReason;
                intent.putExtra("Table_Name", db.TABLE_Followup_reason);
                intent.putExtra("Id", "PKCallPurposeId");
                intent.putExtra("DispName", "CallPurposeDesc");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "follow");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_Outcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = true;
                isFollowup = false;
                isReason = false;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";
                if (calltype.equalsIgnoreCase("1")) {
                    getoutcome = "Sales";
                } else if (calltype.equalsIgnoreCase("2")) {
                    getoutcome = "Collection";
                } else if (calltype.equalsIgnoreCase("3")) {
                    getoutcome = "Feedback";
                    layhead.setVisibility(View.GONE);
                }
                try {
                    url = CompanyURL + WebUrlClass.api_Outcome
                            + "?CallType=" + URLEncoder.encode(getoutcome, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                intent.putExtra("Table_Name", db.TABLE_Outcome);
                intent.putExtra("Id", "Code");
                intent.putExtra("DispName", "Outcome");
                intent.putExtra("WHClauseParameter", "WHERE OutcomeType='" + getoutcome + "'");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "1");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });


        spinner_Ordertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                        CountryListActivity.class);

                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                isOrder = true;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Contact = false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;
                String url = "";


                url = CompanyURL + WebUrlClass.api_getOrdertype;
                intent.putExtra("Table_Name", db.TABLE_OrderTypeMaster);
                intent.putExtra("Id", "OrderTypeMasterId");
                intent.putExtra("DispName", "Description");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "order");
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });




        spinner_With_Towhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                Contact = true;
                isOrder = false;
                isAssignBOE = false;
                isAssignBOESE = false;
                isAssignSE = false;
                isDemo = false;
                Receive=false;
                Approver=false;
                isHandOver=false;
                isPreSale=false;
                isVisit = false;
                isCurrency = false;

                Intent intent = new Intent(OpportunityUpdateActivity_New.this, ContactShowActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_prospect", ProspectId);
                intent.putExtra("call_type", call_type);
                intent.putExtra("callmob", LogContact);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);


            }
        });

        spinner_Initiatedby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initiatedbyid = "";
                String initiatedby = (String) spinner_Initiatedby.getText().toString().trim();
                String query = "SELECT distinct PKInitiatedBy,InitiatedBy" +
                        " FROM " + db.TABLE_InitiatedBy + " WHERE InitiatedBy=" +
                        "'" + initiatedby + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        initiatedbyid = cur.getString(cur.getColumnIndex("PKInitiatedBy"));

                    } while (cur.moveToNext());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        img_birth_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                // Launch Date Picker Dialog
                datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                date_Before = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;
                                date_Current = mYear + "/"
                                        + String.format("%02d", (mMonth + 1))
                                        + "/" + mDay;

                                Contact_DateBirth = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Before);

                                // String ValidDate = formateDateFromstring("yyyy/MM/dd", "dd/MM/yyyy", date_Current);
                                Calendar validDate = Calendar.getInstance();
                                validDate.set(year, monthOfYear + 1, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();

                                edt_Birthdate.setText(Contact_DateBirth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                //datePickerDialog.getDatePicker().setMaxDate(enddate);
            }
        });
        checkBoxContractreviewrequest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chk_Contractreviewrequest = "1";
                } else {
                    chk_Contractreviewrequest = "0";
                }
            }
        });
        checkBoxDemoComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chk_DemoComplete = "1";
                } else {
                    chk_DemoComplete = "0";
                }
            }
        });
        buttonminuscb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_cb = minteger_cb - 1;
                txtcb.setText("" + minteger_cb);
            }
        });
        buttonpluscb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_cb = minteger_cb + 1;
                txtcb.setText("" + minteger_cb);

            }
        });
        buttonminusqv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_qv = minteger_qv - 1;
                txtqv.setText("" + minteger_qv);
            }
        });
        buttonplusqv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_qv = minteger_qv + 1;
                txtqv.setText("" + minteger_qv);

            }
        });
        buttonminuspb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pb = minteger_pb - 1;
                txtpb.setText("" + minteger_pb);
            }
        });
        buttonpluspb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pb = minteger_pb + 1;
                txtpb.setText("" + minteger_pb);

            }
        });
        buttonminuspbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pbt = minteger_pbt - 1;
                txtpbt.setText("" + minteger_pbt);
            }
        });

        buttonpluspbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pbt = minteger_pbt + 1;
                txtpbt.setText("" + minteger_pbt);

            }
        });

        buttonminuspta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pta = minteger_pta - 1;
                txtpta.setText("" + minteger_pta);
            }
        });

        buttonpluspta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_pta = minteger_pta + 1;
                txtpta.setText("" + minteger_pta);

            }
        });


        buttonminusnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_nw = minteger_nw - 1;
                txtnw.setText("" + minteger_nw);
            }
        });

        buttonplusnw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_nw = minteger_nw + 1;
                txtnw.setText("" + minteger_nw);

            }
        });


        buttonminusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_ca = minteger_ca - 1;
                txtca.setText("" + minteger_ca);
            }
        });

        buttonplusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_ca = minteger_ca + 1;
                txtca.setText("" + minteger_ca);

            }
        });
        buttonminusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_ta = minteger_ta - 1;
                txtta.setText("" + minteger_ta);
            }
        });
        buttonplusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_ta = minteger_ta + 1;
                txtta.setText("" + minteger_ta);

            }
        });
        buttonminusda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_da = minteger_da - 1;
                txtda.setText("" + minteger_da);
            }
        });

        buttonplusda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger_da = minteger_da + 1;
                txtda.setText("" + minteger_da);

            }
        });

        editTextReceiveddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextReceiveddate.setText(date);
                                } else {
                                    editTextReceiveddate.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity_New.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        editTextDate_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //     datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextDate_custom.setText(date);
                                } else {
                                    editTextDate_custom.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity_New.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });
       /* editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //     datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextDate.setText(date);
                                } else {
                                    editTextDate.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity_New.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });*/
        editTextWhendoyoucall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                               // String mDate = date + "/" + month + "/" + year;

                                if (show(date) > 15) {
                                    editTextWhendoyoucall.setText(date);
                                } else {
                                    String alert="You have set Call again date within the next " + day + " days .Are you sure to continue with this date?";
                                    getdialog(alert);

                                }

                                /*if (compare_date(date) == true) {

                                    editTextWhendoyoucall.setText(date);

                                }*/
                                /*else {
                                    editTextWhendoyoucall.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }*/


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        btnyesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setdate1("YESTERDAY");
                clickYesterday = 1;


            }
        });

        btnafter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IsOutcome = false;
                isFollowup = false;
                isReason = false;
                Contact = false;
                Schedule = true;

                Intent intent = new Intent(OpportunityUpdateActivity_New.this, FollowupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, Followup);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

            }
        });

        btntommorrow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setdate1("Tomorrow");

            }
        });
        btnbefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate1("BEFORE");
                clickDayBefore = 1;


            }
        });

        editTextFollowupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setdate1("TODAY");
                clickDayBefore = -1;
                clickYesterday = -1;

            }
        });
        /*txt3dateshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setdate1("TODAY");

            }
        });
*/


        btngetordertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordertype = new ArrayList();
                String query = "SELECT distinct OrderTypeMasterId,Description" +
                        " FROM " + db.TABLE_OrderTypeMaster_All;
                Cursor cur = sql.rawQuery(query, null);
                // ordertype.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        ordertype.add(cur.getString(cur.getColumnIndex("Description")));

                    } while (cur.moveToNext());

                }
                Collections.sort(ordertype, String.CASE_INSENSITIVE_ORDER);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, ordertype);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_Ordertype.setAdapter(dataAdapter);

            }
        });

        spinner_Outcome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_outcome = spinner_Outcome.getText().toString();
                selected_outcome_code = cf.getOutcomecode(selected_outcome);
                isapprover = cf.getOutcomeIsapprover(selected_outcome_code);
                lay_gstn.setVisibility(View.GONE);
                lay_content_opp.setVisibility(View.VISIBLE);

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (selected_outcome_code.equalsIgnoreCase("ATS")) {
                    //  spinner_Nextaction.setSelection();
                    lay_AssigntoBOE_SE.setVisibility(View.VISIBLE);
                    spinner_AssigntoBOE_SE.setSelection(0);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    getCategory();
                } else if (selected_outcome_code.equalsIgnoreCase("CA")
                        || selected_outcome_code.equalsIgnoreCase("CustCall")) {
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("CA");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("CA");
                    }
                    spinner_Reason.setSelection(0);

                } else if (selected_outcome_code.equalsIgnoreCase("CC")
                        || selected_outcome_code.equalsIgnoreCase("Oreg")) {
                    len_outcome.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    if (isapprover.equalsIgnoreCase("Y")) {
                        lay_Approver.setVisibility(View.VISIBLE);
                    } else {
                        lay_Approver.setVisibility(View.GONE);
                    }
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.VISIBLE);
                    lay_Notes.setVisibility(View.VISIBLE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    //  txtwhenyoucall.setText("When you call? :");

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("ClsCallWO");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("ClsCallWO");
                    }
                   /* if (cf.getApprovercount() > 0) {
                        getApprover();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadApproverJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getApprover();
                    }*/

                    spinner_Reason.setSelection(0);
                    spinner_Approver.setSelection(0);
                    editTextWhendoyoucall.setText(CurrentDate);
                    //txt3dateshow.setText(CurrentDate);
                    editTextDetails.setText("");
                    spinner_Callagain.setText("");
                    editTextNotes.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("OL")) {
                    len_outcome.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    if (isapprover.equalsIgnoreCase("Y")) {
                        lay_Approver.setVisibility(View.VISIBLE);
                    } else {
                        lay_Approver.setVisibility(View.GONE);
                    }
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.VISIBLE);
                    lay_Notes.setVisibility(View.VISIBLE);

                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    //  txtwhenyoucall.setText("When you call? :");
                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("OL");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("OL");
                    }


                    /*if (cf.getApprovercount() > 0) {
                        getApprover();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadApproverJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getApprover();
                    }*/

                    //  spinner_Reason.setSelection(0);
                    // spinner_Approver.setSelection(0);
                    editTextWhendoyoucall.setText(CurrentDate);
                    editTextDetails.setText("");
                    // spinner_Callagain.setSelection(2);
                    editTextNotes.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("CS")
                        || selected_outcome_code.equalsIgnoreCase("FC")
                        || selected_outcome_code.equalsIgnoreCase("CRS")
                        || selected_outcome_code.equalsIgnoreCase("NG")
                        || selected_outcome_code.equalsIgnoreCase("PC")
                        || selected_outcome_code.equalsIgnoreCase("PR")
                        || selected_outcome.equalsIgnoreCase("Select")
                        || selected_outcome_code.equalsIgnoreCase("PFS")
                        || selected_outcome_code.equalsIgnoreCase("SPA")
                        || selected_outcome_code.equalsIgnoreCase("ATB")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("NI")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome.equalsIgnoreCase("PS")) {

                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.VISIBLE);

                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    // txtwhenyoucall.setText("Due date :");
                    if (cf.getCategorycount() > 0) {

                        category = new ArrayList();
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if (category.size() == 0) {
                                    category.add("-Select-");

                                } else {
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }


                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_PresaleSE.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }

                        category = new ArrayList();
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {

                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_PresaleSE.setAdapter(dataAdapter);
                    }

                    // spinner_PresaleSE.setSelection(0);
                } else if (selected_outcome_code.equalsIgnoreCase("CPU")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.VISIBLE);
                    lay_PTA.setVisibility(View.VISIBLE);
                    lay_Networth.setVisibility(View.VISIBLE);
                    lay_Creditrate.setVisibility(View.VISIBLE);
                    lay_PresentBorrowing.setVisibility(View.VISIBLE);
                    lay_currency.setVisibility(View.VISIBLE);
                    lay_rs.setVisibility(View.VISIBLE);
                    lay_Managementcomment.setVisibility(View.VISIBLE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getCurrencycount() > 0) {
                        getCurrency();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCurrencyMasterJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCurrency();
                    }
                    txtpbt.setText("0");
                    txtpta.setText("0");
                    txtnw.setText("0");
                    editTextCreditrate.setText("");
                    txtpb.setText("0");
                    //  spinner_currency.setSelection(0);
                    //  spinner_rs.setSelection(0);
                    editTextManagementcomment.setText("");

                } else if (selected_outcome_code.equalsIgnoreCase("TTB")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.VISIBLE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("TrfBOE");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("TrfBOE");
                    }


                    if (cf.getCategorycount() > 0) {
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if (category.size() == 0) {

                                    category.add("-Select-");
                                } else {
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }


                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ReassigntoBOE.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ReassigntoBOE.setAdapter(dataAdapter);
                    }
                    // spinner_Reason.setSelection(0);
                    // spinner_ReassigntoBOE.setSelection(0);
                } else if (selected_outcome_code.equalsIgnoreCase("DC")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.VISIBLE);
                    checkBoxDemoComplete.setChecked(false);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("DRes")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);

                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.VISIBLE);
                    lay_Date_time_custom.setVisibility(View.VISIBLE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("DR");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("DR");
                    }
                    getCategory();
                    //   spinner_Reason.setSelection(0);
                    //  spinner_demo.setSelection(0);
                    editTextDate_custom.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("DReq")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);

                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.VISIBLE);
                    lay_Date_time_custom.setVisibility(View.VISIBLE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                } else if (selected_outcome_code.equalsIgnoreCase("IU")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.VISIBLE);
                    lay_cospreferred.setVisibility(View.VISIBLE);
                    lay_PrepaymentSecuterization.setVisibility(View.VISIBLE);
                    lay_ParticipateInsyndication.setVisibility(View.VISIBLE);

                    editTextProductForBank.setText("");
                    editTextcospreferred.setText("");
                    editTextParticipateInsyndication.setText("");
                    editTextPrepaymentSecuterization.setText("");
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("OR")) {
                    len_outcome.setVisibility(View.VISIBLE);
                    lay_Receivedby.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.VISIBLE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.VISIBLE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.VISIBLE);
                    // txt_lbl_ordervalue.setText("Order Value :");
                    if (Status.equalsIgnoreCase("false")) {

                        lay_gstn.setVisibility(View.VISIBLE);
                    }
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.VISIBLE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getTMESNamecount() > 0) {
                        getTMESName();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTMESENameJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getTMESName();
                    }
                    if (cf.getOrdertypecount() > 0) {
                        getOrdertype();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadOrdertypeJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getOrdertype();
                    }

                    // spinner_Receivedby.setSelection(positionReceivedby);
                    editTextReceiveddate.setText(CurrentDate);
                    editTextPONo.setText("");
                    editTextPOvalue.setText("");
                    // spinner_Ordertype.setSelection(0);
                    checkBoxContractreviewrequest.setChecked(false);
                } else if (selected_outcome_code.equalsIgnoreCase("WCF")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                } else if (selected_outcome_code.equalsIgnoreCase("QS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.VISIBLE);
                    lay_CustomerBudget.setVisibility(View.VISIBLE);
                    lay_QuotationValue.setVisibility(View.VISIBLE);
                    lay_QuotationDocument.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.VISIBLE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    spinner_CustomerBudgetSanction.setText("0");
                    txtcb.setText("0");
                    // buttonminusqv.setText("0");
                    editTextQuotationDocument.setText("");
                    editTextQuotationno.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("Res")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("Res");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("Res");
                    }
                    //   spinner_Reason.setSelection(0);

                } else if (selected_outcome_code.equalsIgnoreCase("RTS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.VISIBLE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("TrfBOE");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("TrfBOE");
                    }

                    if (cf.getCategorycount() > 0) {
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if (category.size() == 0) {
                                    category.add("-Select-");
                                } else {
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }


                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_SEName.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        //  category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity_New.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_SEName.setAdapter(dataAdapter);
                    }
                    //  spinner_Reason.setSelection(0);
                    //spinner_SEName.setSelection(0);

                } else if (selected_outcome_code.equalsIgnoreCase("SV")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.VISIBLE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    //  txtwhenyoucall.setText("Visit date :");

                    if (cf.getTMESNamecount() > 0) {//|| db.getOrdertypeMasterAllcount() > 0
                        getTMESName();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTMESENameJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getTMESName();
                    }
                    //  spinner_whowillvisit.setSelection(0);
                } else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.VISIBLE);
                    lay_InstrumentNo.setVisibility(View.VISIBLE);
                    lay_BankName.setVisibility(View.VISIBLE);
                    lay_Branch.setVisibility(View.VISIBLE);
                    lay_ChqAmount.setVisibility(View.VISIBLE);
                    lay_TDSAmount.setVisibility(View.VISIBLE);
                    lay_DiffAmount.setVisibility(View.VISIBLE);
                    lay_ReasonED.setVisibility(View.GONE);
                    editTextReason.setText("");
                    txtda.setText("0");
                    txtta.setText("0");
                    txtca.setText("0");
                    editTextBranch.setText("");
                    editTextBankName.setText("");
                    editTextInstrumentNo.setText("");
                    //  spinner_Mode.setSelection(0);
                    editTextDetails.setText("");

                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);

                    // editTextWhendoyoucall.setText(CurrentDate);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    //  txtwhenyoucall.setText("Due date :");


                } else if (selected_outcome_code.equalsIgnoreCase("Disp")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    editTextReason.setText("");
                    editTextDetails.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("DCans")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("PRMS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    //  txtwhenyoucall.setText("Next Promise Date :");

                    editTextReason.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("LA")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.VISIBLE);
                    lay_dochandover.setVisibility(View.VISIBLE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("LL")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.VISIBLE);
                    lay_dochandover.setVisibility(View.VISIBLE);
                    lay_savingacc.setVisibility(View.VISIBLE);
                    lay_fdacc.setVisibility(View.VISIBLE);
                    lay_disburseVal.setVisibility(View.VISIBLE);
                    lay_tenure.setVisibility(View.VISIBLE);
                    lay_processed.setVisibility(View.VISIBLE);

                }


                outcomeid = "";

                String query = "SELECT distinct Code,PKOutcomeId,Outcome" +
                        " FROM " + db.TABLE_Outcome + " WHERE Outcome='"
                        + spinner_Outcome.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        outcomeid = cur.getString(cur.getColumnIndex("Code"));


                    } while (cur.moveToNext());

                }

            }
        });
     /*
        spinner_Outcome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_outcome = (String) spinner_Outcome.getText().toString().trim();
                selected_outcome_code = cf.getOutcomecode(selected_outcome);
                isapprover = cf.getOutcomeIsapprover(selected_outcome_code);
                lay_gstn.setVisibility(View.GONE);


                if (selected_outcome_code.equalsIgnoreCase("ATS")) {
                    //  spinner_Nextaction.setSelection();
                    lay_AssigntoBOE_SE.setVisibility(View.VISIBLE);
                    spinner_AssigntoBOE_SE.setSelection(0);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    getCategory();
                }
                else if (selected_outcome_code.equalsIgnoreCase("CA")
                        || selected_outcome_code.equalsIgnoreCase("CustCall")) {
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("CA");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("CA");
                    }
                    spinner_Reason.setSelection(0);

                }
                else if (selected_outcome_code.equalsIgnoreCase("CC")
                        || selected_outcome_code.equalsIgnoreCase("Oreg")) {
                    layoutfooter.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    if (isapprover.equalsIgnoreCase("Y")) {
                        lay_Approver.setVisibility(View.VISIBLE);
                    } else {
                        lay_Approver.setVisibility(View.GONE);
                    }
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.VISIBLE);
                    lay_Notes.setVisibility(View.VISIBLE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("When you call? :");

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("ClsCallWO");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("ClsCallWO");
                    }
                    if (cf.getApprovercount() > 0) {
                        getApprover();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadApproverJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getApprover();
                    }

                    spinner_Reason.setSelection(0);
                    spinner_Approver.setSelection(0);
                    editTextWhendoyoucall.setText(CurrentDate);
                    editTextDetails.setText("");
                    spinner_Callagain.setText("");
                    editTextNotes.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("OL")) {
                    layoutfooter.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    if (isapprover.equalsIgnoreCase("Y")) {
                        lay_Approver.setVisibility(View.VISIBLE);
                    } else {
                        lay_Approver.setVisibility(View.GONE);
                    }
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.VISIBLE);
                    lay_Notes.setVisibility(View.VISIBLE);

                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("When you call? :");
                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("OL");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("OL");
                    }


                    if (cf.getApprovercount() > 0) {
                        getApprover();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadApproverJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getApprover();
                    }

                    spinner_Reason.setSelection(0);
                    spinner_Approver.setSelection(0);
                    editTextWhendoyoucall.setText(CurrentDate);
                    editTextDetails.setText("");
                    spinner_Callagain.setSelection(2);
                    editTextNotes.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("CS")
                        || selected_outcome_code.equalsIgnoreCase("FC")
                        || selected_outcome_code.equalsIgnoreCase("CRS")
                        || selected_outcome_code.equalsIgnoreCase("NG")
                        || selected_outcome_code.equalsIgnoreCase("PC")
                        || selected_outcome_code.equalsIgnoreCase("PR")
                        || selected_outcome.equalsIgnoreCase("Select")
                        || selected_outcome_code.equalsIgnoreCase("PFS")
                        || selected_outcome_code.equalsIgnoreCase("SPA")
                        || selected_outcome_code.equalsIgnoreCase("ATB")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                } else if (selected_outcome_code.equalsIgnoreCase("NI")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                }
                else if (selected_outcome.equalsIgnoreCase("PS")) {

                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.VISIBLE);

                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("Due date :");
                    if (cf.getCategorycount() > 0) {

                        category = new ArrayList();
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if(category.size()==0){
                                    category.add("-Select-");

                                }else{
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }



                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_PresaleSE.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }

                        category = new ArrayList();
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {

                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_PresaleSE.setAdapter(dataAdapter);
                    }

                    spinner_PresaleSE.setSelection(0);
                }
                else if (selected_outcome_code.equalsIgnoreCase("CPU")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.VISIBLE);
                    lay_PTA.setVisibility(View.VISIBLE);
                    lay_Networth.setVisibility(View.VISIBLE);
                    lay_Creditrate.setVisibility(View.VISIBLE);
                    lay_PresentBorrowing.setVisibility(View.VISIBLE);
                    lay_currency.setVisibility(View.VISIBLE);
                    lay_rs.setVisibility(View.VISIBLE);
                    lay_Managementcomment.setVisibility(View.VISIBLE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getCurrencycount() > 0) {
                        getCurrency();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCurrencyMasterJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCurrency();
                    }
                    txtpbt.setText("0");
                    txtpta.setText("0");
                    txtnw.setText("0");
                    editTextCreditrate.setText("");
                    txtpb.setText("0");
                    spinner_currency.setSelection(0);
                    spinner_rs.setSelection(0);
                    editTextManagementcomment.setText("");

                }
                else if (selected_outcome_code.equalsIgnoreCase("TTB")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.VISIBLE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("TrfBOE");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("TrfBOE");
                    }


                    if (cf.getCategorycount() > 0) {
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if(category.size()==0){

                                    category.add("-Select-");
                                }else {
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }


                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ReassigntoBOE.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='1'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_ReassigntoBOE.setAdapter(dataAdapter);
                    }
                    spinner_Reason.setSelection(0);
                    spinner_ReassigntoBOE.setSelection(0);
                }
                else if (selected_outcome_code.equalsIgnoreCase("DC")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.VISIBLE);
                    checkBoxDemoComplete.setChecked(false);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                }
                else if (selected_outcome_code.equalsIgnoreCase("DRes")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);

                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.VISIBLE);
                    lay_Date_time_custom.setVisibility(View.VISIBLE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("DR");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("DR");
                    }
                    getCategory();
                    spinner_Reason.setSelection(0);
                    spinner_demo.setSelection(0);
                    editTextDate_custom.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("DReq")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);

                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.VISIBLE);
                    lay_Date_time_custom.setVisibility(View.VISIBLE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                }
                else if (selected_outcome_code.equalsIgnoreCase("IU")) {
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.VISIBLE);
                    lay_cospreferred.setVisibility(View.VISIBLE);
                    lay_PrepaymentSecuterization.setVisibility(View.VISIBLE);
                    lay_ParticipateInsyndication.setVisibility(View.VISIBLE);

                    editTextProductForBank.setText("");
                    editTextcospreferred.setText("");
                    editTextParticipateInsyndication.setText("");
                    editTextPrepaymentSecuterization.setText("");
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                }
                else if (selected_outcome_code.equalsIgnoreCase("OR")) {
                    layoutfooter.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.VISIBLE);
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.VISIBLE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.VISIBLE);
                   // txt_lbl_ordervalue.setText("Order Value :");
                    if (Status.equalsIgnoreCase("false")) {

                        lay_gstn.setVisibility(View.VISIBLE);
                    }
                    lay_Ordertype.setVisibility(View.VISIBLE);VISIBLE
                    lay_Contractreviewrequest.setVisibility(View.VISIBLE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getTMESNamecount() > 0) {
                        getTMESName();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTMESENameJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getTMESName();
                    }
                    if (cf.getOrdertypecount() > 0) {
                        getOrdertype();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadOrdertypeJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getOrdertype();
                    }

                    spinner_Receivedby.setSelection(positionReceivedby);
                    editTextReceiveddate.setText(CurrentDate);
                    editTextPONo.setText("");
                    editTextPOvalue.setText("");
                    spinner_Ordertype.setSelection(0);
                    checkBoxContractreviewrequest.setChecked(false);
                }
                else if (selected_outcome_code.equalsIgnoreCase("WCF")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    editTextDetails.setText("");
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                }
                else if (selected_outcome_code.equalsIgnoreCase("QS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.VISIBLE);
                    lay_CustomerBudget.setVisibility(View.VISIBLE);
                    lay_QuotationValue.setVisibility(View.VISIBLE);
                    lay_QuotationDocument.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.VISIBLE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    spinner_CustomerBudgetSanction.setSelection(0);
                    txtcb.setText("0");
                    buttonminusqv.setText("0");
                    editTextQuotationDocument.setText("");
                    editTextQuotationno.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("Res")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);

                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("Res");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("Res");
                    }
                    spinner_Reason.setSelection(0);

                }
                else if (selected_outcome_code.equalsIgnoreCase("RTS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.VISIBLE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.VISIBLE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);


                    if (cf.getReasonMastercount() > 0) {
                        getCall_Reason("TrfBOE");
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadReasonJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getCall_Reason("TrfBOE");
                    }

                    if (cf.getCategorycount() > 0) {
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        // category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                if(category.size()==0) {
                                    category.add("-Select-");
                                }else{
                                    category.add(cur.getString(cur.getColumnIndex("UserName")));
                                }


                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_SEName.setAdapter(dataAdapter);
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadCategoryJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        category = new ArrayList();
                        String query = "SELECT distinct UserName,CRMCategory" +
                                " FROM " + db.TABLE_Category + " WHERE CRMCategory='2'";
                        Cursor cur = sql.rawQuery(query, null);
                        //  category.add("Select");
                        if (cur.getCount() > 0) {

                            cur.moveToFirst();
                            do {
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

                            } while (cur.moveToNext());

                        }
                        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_SEName.setAdapter(dataAdapter);
                    }
                    spinner_Reason.setSelection(0);
                    spinner_SEName.setSelection(0);

                }
                else if (selected_outcome_code.equalsIgnoreCase("SV")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.VISIBLE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("Visit date :");

                    if (cf.getTMESNamecount() > 0) {//|| db.getOrdertypeMasterAllcount() > 0
                        getTMESName();
                    } else {
                        if (isnet()) {
                            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTMESENameJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                        getTMESName();
                    }
                    spinner_whowillvisit.setSelection(0);
                }
                else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.VISIBLE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.VISIBLE);
                    lay_InstrumentNo.setVisibility(View.VISIBLE);
                    lay_BankName.setVisibility(View.VISIBLE);
                    lay_Branch.setVisibility(View.VISIBLE);
                    lay_ChqAmount.setVisibility(View.VISIBLE);
                    lay_TDSAmount.setVisibility(View.VISIBLE);
                    lay_DiffAmount.setVisibility(View.VISIBLE);
                    lay_ReasonED.setVisibility(View.VISIBLE);
                    editTextReason.setText("");
                    txtda.setText("0");
                    txtta.setText("0");
                    txtca.setText("0");
                    editTextBranch.setText("");
                    editTextBankName.setText("");
                    editTextInstrumentNo.setText("");
                    spinner_Mode.setSelection(0);
                    editTextDetails.setText("");

                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);

                    // editTextWhendoyoucall.setText(CurrentDate);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("Due date :");


                }
                else if (selected_outcome_code.equalsIgnoreCase("Disp")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.VISIBLE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    editTextReason.setText("");
                    editTextDetails.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("DCans")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                }
                else if (selected_outcome_code.equalsIgnoreCase("PRMS")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.VISIBLE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.VISIBLE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.GONE);
                    lay_dochandover.setVisibility(View.GONE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                    txtwhenyoucall.setText("Next Promise Date :");

                    editTextReason.setText("");
                }
                else if (selected_outcome_code.equalsIgnoreCase("LA")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.VISIBLE);
                    lay_dochandover.setVisibility(View.VISIBLE);
                    lay_savingacc.setVisibility(View.GONE);
                    lay_fdacc.setVisibility(View.GONE);
                    lay_disburseVal.setVisibility(View.GONE);
                    lay_tenure.setVisibility(View.GONE);
                    lay_processed.setVisibility(View.GONE);
                }
                else if (selected_outcome_code.equalsIgnoreCase("LL")) {
                    lay_AssigntoBOE_SE.setVisibility(View.GONE);
                    lay_Reason.setVisibility(View.GONE);
                    lay_Approver.setVisibility(View.GONE);
                    lay_Details.setVisibility(View.GONE);
                    lay_Callagain.setVisibility(View.GONE);
                    lay_Notes.setVisibility(View.GONE);
                    lay_Mode.setVisibility(View.GONE);
                    lay_InstrumentNo.setVisibility(View.GONE);
                    lay_BankName.setVisibility(View.GONE);
                    lay_Branch.setVisibility(View.GONE);
                    lay_ChqAmount.setVisibility(View.GONE);
                    lay_TDSAmount.setVisibility(View.GONE);
                    lay_DiffAmount.setVisibility(View.GONE);
                    lay_ReasonED.setVisibility(View.GONE);
                    lay_PBT.setVisibility(View.GONE);
                    lay_PTA.setVisibility(View.GONE);
                    lay_Networth.setVisibility(View.GONE);
                    lay_Creditrate.setVisibility(View.GONE);
                    lay_PresentBorrowing.setVisibility(View.GONE);
                    lay_currency.setVisibility(View.GONE);
                    lay_rs.setVisibility(View.GONE);
                    lay_Managementcomment.setVisibility(View.GONE);
                    lay_DemoComplete.setVisibility(View.GONE);
                    lay_Demo.setVisibility(View.GONE);
                    lay_Date_time_custom.setVisibility(View.GONE);
                    lay_ProductForBank.setVisibility(View.GONE);
                    lay_cospreferred.setVisibility(View.GONE);
                    lay_PrepaymentSecuterization.setVisibility(View.GONE);
                    lay_ParticipateInsyndication.setVisibility(View.GONE);
                    lay_Receiveddate.setVisibility(View.GONE);
                    lay_PONo.setVisibility(View.GONE);
                    lay_POvalue.setVisibility(View.GONE);
                    lay_Ordertype.setVisibility(View.GONE);
                    lay_Contractreviewrequest.setVisibility(View.GONE);
                    lay_CustomerBudgetSanction.setVisibility(View.GONE);
                    lay_CustomerBudget.setVisibility(View.GONE);
                    lay_QuotationValue.setVisibility(View.GONE);
                    lay_QuotationDocument.setVisibility(View.GONE);
                    lay_ReassigntoBOE.setVisibility(View.GONE);
                    lay_PresaleSE.setVisibility(View.GONE);
                    lay_SEName.setVisibility(View.GONE);
                    lay_whowillvisit.setVisibility(View.GONE);
                    lay_Receivedby.setVisibility(View.GONE);
                    lay_Whendoyoucall.setVisibility(View.GONE);
                    lay_Quotationno.setVisibility(View.GONE);
                    lay_application_no.setVisibility(View.VISIBLE);
                    lay_dochandover.setVisibility(View.VISIBLE);
                    lay_savingacc.setVisibility(View.VISIBLE);
                    lay_fdacc.setVisibility(View.VISIBLE);
                    lay_disburseVal.setVisibility(View.VISIBLE);
                    lay_tenure.setVisibility(View.VISIBLE);
                    lay_processed.setVisibility(View.VISIBLE);

                }


                outcomeid = "";

                String query = "SELECT distinct Code,PKOutcomeId,Outcome" +
                        " FROM " + db.TABLE_Outcome + " WHERE Outcome='"
                        + spinner_Outcome.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        outcomeid = cur.getString(cur.getColumnIndex("Code"));


                    } while (cur.moveToNext());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


    }

    private void getData() {

        String query = "SELECT ProspectId,ProductId FROM "
                + tablename + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);

        // selected_from_time = spinner_From.getText().toString();


        /*spinner_From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_from_time = spinner_From.getText().toString();

                System.out.println("select_item-1 :" + selected_from_time);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



       /* spinner_From.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();
                tohour = calendar.get(Calendar.HOUR_OF_DAY);
                tomin = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = " PM";
                                } else {
                                    amPm = " AM";
                                }
                                spinner_From.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                            }
                        }, tohour, tomin, false);

                timePickerDialog.show();
            }
        });


        selected_to_time = spinner_To.getText().toString();

        spinner_To.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();
                tohour = calendar.get(Calendar.HOUR_OF_DAY);
                tomin = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                if (hourOfDay >= 12) {
                                    amPm = " PM";
                                } else {
                                    amPm = " AM";
                                }
                                spinner_To.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                            }
                        }, tohour, tomin, false);

                timePickerDialog.show();
            }
        });



        System.out.println("Select To time :" + selected_to_time);
*/
       /* spinner_To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String count = "";
                selected_to_time = spinner_To.getSelectedItem().toString();
                if (selected_from_time.equalsIgnoreCase("Select")) {
                 *//*   Toast.makeText(OpportunityUpdateActivity.this,
                            "Select From time first",
                            Toast.LENGTH_SHORT).show();*//*
                } else if (selected_to_time.equalsIgnoreCase("Select")) {

                } else {
                    count = calculate_time_diff(selected_from_time, selected_to_time);
                    Log.d("crm_dialog_action", "count" + count);
                    EdttxtHours.setText(count);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            ProductId = cur.getString(cur.getColumnIndex("ProductId"));
            ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));
        } else {
            //ProductId = "0";
            // ProspectId = "0";
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

    private void init_layout() {
        layHdr_two = (LinearLayout) findViewById(R.id.lay_naturecalltooutcome);
        lay_content_opp = (LinearLayout) findViewById(R.id.lay_opportunity_update);
        lay_next_action = (LinearLayout) findViewById(R.id.lay_next_action);
        len_outcome = (LinearLayout) findViewById(R.id.len_outcome);
        len_call_log = (LinearLayout) findViewById(R.id.len_call_log);
        layhead = (LinearLayout) findViewById(R.id.layhead);
        cardview_outcome = (CardView) findViewById(R.id.cardview_outcome);
        button_previous = findViewById(R.id.button_previous);
        button_nextcontent = findViewById(R.id.button_nextcontent);
        button_cancelcontent = findViewById(R.id.button_cancelcontent);
        btnemailid = findViewById(R.id.btnemailid);
        btnemailid1 = findViewById(R.id.btnemailid1);
        btntelep1 = findViewById(R.id.btntelep1);
        btnvisit1 = findViewById(R.id.btnvisit1);
        btntelep = findViewById(R.id.btntelep);
        btnvisit = findViewById(R.id.btnvisit);
        layHdr_one = (LinearLayout) findViewById(R.id.layhead);
        //  scroll = (ScrollView) findViewById(R.id.scroll);
        layHdr_one.setVisibility(View.VISIBLE);
        layHdr_two.setVisibility(View.GONE);
        lay_content_opp.setVisibility(View.GONE);
        len_outcome.setVisibility(View.GONE);
        lay_next_action.setVisibility(View.GONE);

        laytime1 = (LinearLayout) findViewById(R.id.laytime1);
        laytime2 = (LinearLayout) findViewById(R.id.laytime2);
        lay_Whendoyoucall = (LinearLayout) findViewById(R.id.lay_Whendoyoucall);
        lay_AssigntoBOE_SE = (LinearLayout) findViewById(R.id.lay_AssigntoBOE_SE);
        lay_Reason = (LinearLayout) findViewById(R.id.lay_Reason);
        lay_Approver = (LinearLayout) findViewById(R.id.lay_Approver);
        lay_Details = (LinearLayout) findViewById(R.id.lay_Details);
        lay_Callagain = (LinearLayout) findViewById(R.id.lay_Callagain);
        lay_Notes = (LinearLayout) findViewById(R.id.lay_Notes);
        lay_Mode = (LinearLayout) findViewById(R.id.lay_Mode);
        lay_InstrumentNo = (LinearLayout) findViewById(R.id.lay_InstrumentNo);
        lay_BankName = (LinearLayout) findViewById(R.id.lay_BankName);
        lay_Branch = (LinearLayout) findViewById(R.id.lay_Branch);
        lay_ChqAmount = (LinearLayout) findViewById(R.id.lay_ChqAmount);
        lay_TDSAmount = (LinearLayout) findViewById(R.id.lay_TDSAmount);
        lay_DiffAmount = (LinearLayout) findViewById(R.id.lay_DiffAmount);
        lay_ReasonED = (LinearLayout) findViewById(R.id.lay_ReasonED);
        lay_PBT = (LinearLayout) findViewById(R.id.lay_PBT);
        lay_PTA = (LinearLayout) findViewById(R.id.lay_PTA);
        lay_Networth = (LinearLayout) findViewById(R.id.lay_Networth);
        lay_Creditrate = (LinearLayout) findViewById(R.id.lay_Creditrate);
        lay_PresentBorrowing = (LinearLayout) findViewById(R.id.lay_PresentBorrowing);
        lay_currency = (LinearLayout) findViewById(R.id.lay_currency);
        lay_rs = (LinearLayout) findViewById(R.id.lay_rs);
        lay_Managementcomment = (LinearLayout) findViewById(R.id.lay_Managementcomment);
        lay_DemoComplete = (LinearLayout) findViewById(R.id.lay_DemoComplete);
        lay_Demo = (LinearLayout) findViewById(R.id.lay_Demo);
        lay_Date_time_custom = (LinearLayout) findViewById(R.id.lay_Date_time_custom);
        lay_ProductForBank = (LinearLayout) findViewById(R.id.lay_ProductForBank);
        lay_cospreferred = (LinearLayout) findViewById(R.id.lay_cospreferred);
        lay_PrepaymentSecuterization = (LinearLayout) findViewById(R.id.lay_PrepaymentSecuterization);
        lay_ParticipateInsyndication = (LinearLayout) findViewById(R.id.lay_ParticipateInsyndication);
        lay_Receiveddate = (LinearLayout) findViewById(R.id.lay_Receiveddate);
        lay_PONo = (LinearLayout) findViewById(R.id.lay_PONo);
        lay_POvalue = (LinearLayout) findViewById(R.id.lay_POvalue);
        txt_lbl_ordervalue = findViewById(R.id.txt_lbl_ordervalue);
        lay_gstn = (LinearLayout) findViewById(R.id.lay_gstn);
        lay_application_no = (LinearLayout) findViewById(R.id.lay_application_no);
        lay_dochandover = findViewById(R.id.lay_dochandover);
        lay_savingacc = findViewById(R.id.lay_savingacc);
        lay_fdacc = findViewById(R.id.lay_fdacc);
        lay_disburseVal = findViewById(R.id.lay_disburseVal);
        lay_tenure = findViewById(R.id.lay_tenure);
        lay_processed = findViewById(R.id.lay_processed);
        lay_Ordertype = (LinearLayout) findViewById(R.id.lay_Ordertype);
        lay_Contractreviewrequest = (LinearLayout) findViewById(R.id.lay_Contractreviewrequest);
        lay_CustomerBudgetSanction = (LinearLayout) findViewById(R.id.lay_CustomerBudgetSanction);
        lay_CustomerBudget = (LinearLayout) findViewById(R.id.lay_CustomerBudget);
        lay_QuotationValue = (LinearLayout) findViewById(R.id.lay_QuotationValue);
        lay_QuotationDocument = (LinearLayout) findViewById(R.id.lay_QuotationDocument);
        lay_ReassigntoBOE = (LinearLayout) findViewById(R.id.lay_ReassigntoBOE);
        lay_PresaleSE = (LinearLayout) findViewById(R.id.lay_PresaleSE);
        lay_SEName = (LinearLayout) findViewById(R.id.lay_SEName);
        lay_whowillvisit = (LinearLayout) findViewById(R.id.lay_whowillvisit);
        lay_Receivedby = (LinearLayout) findViewById(R.id.lay_Receivedby);
        lay_Quotationno = (LinearLayout) findViewById(R.id.lay_Quotationno);
        btnyesterday = findViewById(R.id.btnyesterday);
        btnbefore = findViewById(R.id.btnbefore);
        btntommorrow1 = findViewById(R.id.btntommorrow1);
        btnafter = findViewById(R.id.btnafter);
        img_shuffle = findViewById(R.id.img_shuffle);
        img_contactt = findViewById(R.id.img_contactt);
        txt_whom = findViewById(R.id.txt_whom);
        img_fwd = findViewById(R.id.img_fwd);
        img_back_1 = findViewById(R.id.img_back_1);
        img_fwd_1 = findViewById(R.id.img_fwd_1);
        img_fwd_2 = findViewById(R.id.img_fwd_2);
        img_back_2 = findViewById(R.id.img_back_2);
        img_back_3 = findViewById(R.id.img_back_3);


    }

    private void init_spinner() {
        spinner_Natureofcall = findViewById(R.id.spinner_Natureofcall);
        spinner_Initiatedby = findViewById(R.id.spinner_Initiatedby);
        spinner_With_Towhom = findViewById(R.id.spinner_With_Towhom);
        spinner_Followupreason = findViewById(R.id.spinner_Followupreason);
        spinner_Outcome = findViewById(R.id.spinner_Outcome);
        spinner_AssigntoBOE_SE = findViewById(R.id.spinner_AssigntoBOE_SE);
        spinner_Nextaction = findViewById(R.id.spinner_Nextaction);
        spinner_Reason = findViewById(R.id.spinner_Reason);
        spinner_demo = findViewById(R.id.spinner_demo);
        spinner_Approver = findViewById(R.id.spinner_Approver);
        spinner_currency = findViewById(R.id.spinner_currency);
        spinner_Ordertype = findViewById(R.id.spinner_Ordertype);
        spinner_Receivedby = findViewById(R.id.spinner_Receivedby);
        spinner_whowillvisit = findViewById(R.id.spinner_whowillvisit);
        spinner_PresaleSE = findViewById(R.id.spinner_PresaleSE);
        spinner_ReassigntoBOE = findViewById(R.id.spinner_ReassigntoBOE);
        spinner_SEName = findViewById(R.id.spinner_SEName);
        spinner_Mode = findViewById(R.id.spinner_Mode);
        spinner_time_custom = findViewById(R.id.spinner_time_custom);
        spinner_Callagain = findViewById(R.id.spinner_Callagain);
        spinner_rs = findViewById(R.id.spinner_rs);
        spinner_CustomerBudgetSanction = findViewById(R.id.spinner_CustomerBudgetSanction);
        spinner_time = findViewById(R.id.spinner_time);
        spinner_dochandover = findViewById(R.id.spinner_dochandover);
        spinner_processed = findViewById(R.id.spinner_processed);

        ArrayAdapter<String> autocompletetextAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.CallAgain));

        spinner_Callagain.setAdapter(autocompletetextAdapter);

        //  spinner_Natureofcall.setSelection(0);
        // spinner_Initiatedby.setSelection(0);
        //spinner_With_Towhom.setSelection(0);
        //  spinner_Followupreason.setSelection(0);
        // spinner_Outcome.setSelection(0);
       /* spinner_AssigntoBOE_SE.setSelection(0);
        spinner_Nextaction.setSelection(0);
        spinner_Reason.setSelection(0);
        spinner_demo.setSelection(0);
        spinner_Approver.setSelection(0);
        spinner_currency.setSelection(0);
        spinner_Ordertype.setSelection(0);
        spinner_Receivedby.setSelection(0);
        spinner_whowillvisit.setSelection(0);
        spinner_PresaleSE.setSelection(0);
        spinner_ReassigntoBOE.setSelection(0);
        spinner_SEName.setSelection(0);*/
        // spinner_From.setSelection(0);
      /*  spinner_To.setSelection(0);
        spinner_Mode.setSelection(0);
        spinner_time_custom.setSelection(0);
//     spinner_Callagain.setSelection(2);
        spinner_rs.setSelection(0);
        spinner_CustomerBudgetSanction.setSelection(0);
        spinner_time.setSelection(0);*/

       /* spinner_Followupreason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });
*/



        spinner_time_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                spinner_time_custom.setText(hourOfDay + ":" + minute + " "+format);
                                selected_custom_time=hourOfDay + ":" + minute + " "+format;
                            }
                        }, hour, minute, false);
                mTimePicker.show();

            }
        });

        spinner_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(OpportunityUpdateActivity_New.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                                spinner_time.setText(hourOfDay + ":" + minute + format);
                            }
                        }, hour, minute, false);
                mTimePicker.show();

            }
        });


    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.mipmap.ic_crm);
        // toolbar.setTitle("Opportunity Update");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        btnpause = (Button) findViewById(R.id.btnpause);
        edtfrom = (TextView) findViewById(R.id.edtfrom);
        edtto = (TextView) findViewById(R.id.edtto);
        btnclear = (Button) findViewById(R.id.btnclear);
        btnclear1 = (Button) findViewById(R.id.btnclear1);
        btnplay = (Button) findViewById(R.id.btnplay);
        btngetordertype = (Button) findViewById(R.id.btngetordertype);
        txtwhenyoucall = findViewById(R.id.txtwhenyoucall);
        editTextFollowupDate = findViewById(R.id.editTextFollowupDate);
        txt3dateshow = findViewById(R.id.txt3dateshow);
        EdttxtHours = (EditText) findViewById(R.id.EdttxtHours);
        editTextDetails = (EditText) findViewById(R.id.editTextDetails);
        editTextWhendoyoucall = (EditText) findViewById(R.id.editTextWhendoyoucall);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextDate_custom = (EditText) findViewById(R.id.editTextDate_custom);
        editTextReceiveddate = (EditText) findViewById(R.id.editTextReceiveddate);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        editTextInstrumentNo = (EditText) findViewById(R.id.editTextInstrumentNo);
        editTextBankName = (EditText) findViewById(R.id.editTextBankName);
        editTextBranch = (EditText) findViewById(R.id.editTextBranch);
        editTextReason = (EditText) findViewById(R.id.editTextReason);
        editTextCreditrate = (EditText) findViewById(R.id.editTextCreditrate);
        editTextManagementcomment = (EditText) findViewById(R.id.editTextManagementcomment);
        editTextProductForBank = (EditText) findViewById(R.id.editTextProductForBank);
        editTextcospreferred = (EditText) findViewById(R.id.editTextcospreferred);
        editTextPrepaymentSecuterization = (EditText) findViewById(R.id.editTextPrepaymentSecuterization);
        editTextParticipateInsyndication = (EditText) findViewById(R.id.editTextParticipateInsyndication);
        editTextPONo = (EditText) findViewById(R.id.editTextPONo);
        editTextPOvalue = (EditText) findViewById(R.id.editTextPOvalue);
        editTextQuotationno = (EditText) findViewById(R.id.editTextQuotationno);
        editTextQuotationDocument = (EditText) findViewById(R.id.editTextQuotationDocument);
        editTextGSTINo = (EditText) findViewById(R.id.editTextGSTINo);
        edit_applicaton_no = (EditText) findViewById(R.id.edit_applicaton_no);
        edit_savingacc = findViewById(R.id.edit_savingacc);
        edit_fdacc = findViewById(R.id.edit_fdacc);
        edit_disburseVal = findViewById(R.id.edit_disburseVal);
        edit_tenure = findViewById(R.id.edit_tenure);
        buttonminusca = (Button) findViewById(R.id.buttonminusca);
        buttonplusca = (Button) findViewById(R.id.buttonplusca);
        txtca = findViewById(R.id.txtca);
        buttonminusta = (Button) findViewById(R.id.buttonminus);
        buttonplusta = (Button) findViewById(R.id.buttonplus);
        buttonminusda = (Button) findViewById(R.id.buttonminusd);
        buttonplusda = (Button) findViewById(R.id.buttonplussda);
        txtta = findViewById(R.id.txtta);
        txtda = (TextView) findViewById(R.id.txtda);
        buttonplusnw = (Button) findViewById(R.id.buttonplusnw);
        buttonminusnw = (Button) findViewById(R.id.buttonminusnw);
        buttonpluspta = (Button) findViewById(R.id.buttonpluspta);
        buttonminuspta = (Button) findViewById(R.id.buttonminuspta);
        buttonpluspbt = (Button) findViewById(R.id.buttonpluspbt);
        buttonminuspbt = (Button) findViewById(R.id.buttonminuspbt);
        txtnw = (TextView) findViewById(R.id.txtnw);
        txtpta = (TextView) findViewById(R.id.txtpta);
        txtpbt = findViewById(R.id.txtpbt);
        checkBoxDemoComplete = (CheckBox) findViewById(R.id.checkBoxDemoComplete);
        checkBoxContractreviewrequest = (CheckBox) findViewById(R.id.checkBoxContractreviewrequest);
        buttonSave_opportunity = (Button) findViewById(R.id.buttonSave_opportunity);
        //  buttonClose_opportunity = (Button) findViewById(R.id.buttonClose_opportunity);
        EdttxtNotes = findViewById(R.id.txtnotes);
        buttonminuspb = (Button) findViewById(R.id.buttonminuspb);
        buttonpluspb = (Button) findViewById(R.id.buttonpluspb);
        txtpb = (TextView) findViewById(R.id.txtpb);
        buttonminuscb = (Button) findViewById(R.id.buttonminuscb);
        buttonpluscb = (Button) findViewById(R.id.buttonpluscb);
        buttonminusqv = findViewById(R.id.buttonminusqv);
        buttonplusqv = (Button) findViewById(R.id.buttonplusqv);
        txtcb = (TextView) findViewById(R.id.txtcb);
        txtqv = (TextView) findViewById(R.id.txtqv);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        img_contact = findViewById(R.id.img_contact);
        //timer=findViewById(R.id.timer);
        img_birth_calender = (ImageView) findViewById(R.id.img_birth_calender);
        edt_Birthdate = findViewById(R.id.edt_Birthdate);
        txtnameto = findViewById(R.id.txtnameto);
        txtnamefrm = findViewById(R.id.txtnamefrm);
        txtpurpose = findViewById(R.id.txtpurpose);
        upload = findViewById(R.id.upload);
        txtcurrentday = findViewById(R.id.txtcurrentday);
        txtcurrentday_1 = findViewById(R.id.txtcurrentday_1);
        txt3scheduledtimeshow = findViewById(R.id.txt3scheduledtimeshow);
        currentTime = dateFormat.format(cl.getTime());
        //txt3scheduledtimeshow.setText(currentTime);
        txt3scheduledtimeshow.setText("9:00 AM");

       /* txtto = findViewById(R.id.txt_emailwhom);
        txtwhom = findViewById(R.id.txt_emailwhom);
*/
        txt_title = findViewById(R.id.txt_title);
        img_add = findViewById(R.id.img_add);
        img_add_1 = findViewById(R.id.img_add_1);
        img_back = findViewById(R.id.img_back);

       /* img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Firmname = (TextView) findViewById(R.id.firmname);
        actiondatetime = (TextView) findViewById(R.id.actiondatetime);
        tv_latestremark = (TextView) findViewById(R.id.tv_latestremark);
        tvcall = (TextView) findViewById(R.id.tvcall);
        callrating = (ImageView) findViewById(R.id.callrating);
        img_nextaction = (ImageView) findViewById(R.id.img_nextaction);
        img_appotunity_update = findViewById(R.id.img_appotunity_update);
        txt_prospect = findViewById(R.id.txt_prospect);
        call_rating = findViewById(R.id.call_rating);
        milestone = findViewById(R.id.milestone);
        txt_expvalue = findViewById(R.id.txt_expvalue);

        call_type = getIntent().getStringExtra("call_type");
        if (call_type.equalsIgnoreCase("1")) {
            txt_title.setText("Opportunity Update");
            Firmname.setText(getIntent().getStringExtra("firm"));
            actiondatetime.setText(getIntent().getStringExtra("date"));
            tv_latestremark.setText(getIntent().getStringExtra("remark"));
            tvcall.setText(getIntent().getStringExtra("call"));

            call_type = getIntent().getStringExtra("call_type");
            sstatus = getIntent().getStringExtra("status");
            CallId = getIntent().getStringExtra("callid");
            Prospect = getIntent().getStringExtra("call_prospect");
            Call = getIntent().getStringExtra("call_type_1");
            SourceId = getIntent().getStringExtra("SourceId");
            Milestone = getIntent().getStringExtra("mile");
            milestone.setText(Milestone);
            Mobile = getIntent().getStringExtra("mobile");
            EstimateValue = getIntent().getStringExtra("evalue");


            if (EstimateValue.equals("0.00") || EstimateValue.equals("0.0") || EstimateValue.equals("0")) {

            } else {
                txt_expvalue.setVisibility(View.VISIBLE);
                double d = Double.parseDouble(EstimateValue);
                txt_expvalue.setText("EV-" + String.format("%.2f", d));
            }


            if (call_type.equalsIgnoreCase("1")) {
                //Hot-Red,Warm-Green,Cold-Purple
                if (sstatus.equalsIgnoreCase("Cold")) {
                    //callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                    //  ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
                    callrating.setImageResource(R.drawable.ic_cube);
                } else if (sstatus.equalsIgnoreCase("Hot")) {
                    // callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
                    //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.hot)));
                    callrating.setImageResource(R.drawable.img_hot_call);
                } else if (sstatus.equalsIgnoreCase("Warm")) {
                    //callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
                    // ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.warm)));
                    callrating.setImageResource(R.drawable.img_warm_call);
                }
            } else if (call_type.equalsIgnoreCase("2")) {
                callrating.setBackgroundColor(Color.parseColor("#3366FF"));
            } else if (call_type.equalsIgnoreCase("3")) {
                callrating.setBackgroundColor(Color.parseColor("#FF1493"));
            }

            if (getIntent().hasExtra("Start")) {
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
                img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary
                ), android.graphics.PorterDuff.Mode.MULTIPLY);
                NatureOfCall = "Visit";
            }
            else {
            NextAction = getIntent().getStringExtra("action");
            if (NextAction.equalsIgnoreCase("Email")) {
                btnemailid.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.email_24));
                img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (NextAction.equalsIgnoreCase("Telephone")) {
                btntelep.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
            } else if (NextAction.equalsIgnoreCase("Visit")) {
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
                img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary
                ), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                btntelep.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
            }
            }
        }
        else {
            txt_title.setText("Collection Update");
            Firmname.setText(getIntent().getStringExtra("firm"));
            actiondatetime.setText(getIntent().getStringExtra("date"));
            tv_latestremark.setText(getIntent().getStringExtra("remark"));
            tvcall.setText(getIntent().getStringExtra("call"));

            call_type = getIntent().getStringExtra("call_type");
            sstatus = getIntent().getStringExtra("status");
            CallId = getIntent().getStringExtra("callid");
            Prospect = getIntent().getStringExtra("call_prospect");
            Call = getIntent().getStringExtra("call_type_1");
            SourceId = getIntent().getStringExtra("SourceId");
           /* Milestone = getIntent().getStringExtra("mile");
            milestone.setText(Milestone);*/
            Mobile = getIntent().getStringExtra("mobile");
            //EstimateValue = getIntent().getStringExtra("evalue");


            /*if (EstimateValue.equals("0.00") || EstimateValue.equals("0.0") || EstimateValue.equals("0")) {

            } else {
                txt_expvalue.setVisibility(View.VISIBLE);
                double d = Double.parseDouble(EstimateValue);
                txt_expvalue.setText("EV-" + String.format("%.2f", d));
            }*/


            if (call_type.equalsIgnoreCase("1")) {
                //Hot-Red,Warm-Green,Cold-Purple
                if (sstatus.equalsIgnoreCase("Cold")) {
                    // callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                    //  ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
                    callrating.setImageResource(R.drawable.ic_cube);

                } else if (sstatus.equalsIgnoreCase("Hot")) {
                    // callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
                    //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.hot)));
                    callrating.setImageResource(R.drawable.img_hot_call);

                } else if (sstatus.equalsIgnoreCase("Warm")) {
                    //callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
                    // ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.warm)));
                    callrating.setImageResource(R.drawable.img_warm_call);
                }
            } else if (call_type.equalsIgnoreCase("2")) {
                callrating.setImageResource(R.drawable.ic_cube);
            } else if (call_type.equalsIgnoreCase("3")) {
                callrating.setImageResource(R.drawable.ic_cube);
            }

            if (getIntent().hasExtra("Start")) {
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_orange));
                img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
                img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary
                ), android.graphics.PorterDuff.Mode.MULTIPLY);
                NatureOfCall = "Visit";
            }else {
                NextAction = getIntent().getStringExtra("action");
                if (NextAction.equalsIgnoreCase("Email")) {
                    btnemailid.setBackground(getResources().getDrawable(R.drawable.button_orange));
                    img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.email_24));
                    img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else if (NextAction.equalsIgnoreCase("Telephone")) {
                    btntelep.setBackground(getResources().getDrawable(R.drawable.button_orange));
                    img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
                } else if (NextAction.equalsIgnoreCase("Visit")) {
                    btnvisit.setBackground(getResources().getDrawable(R.drawable.button_orange));
                    img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
                    img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityUpdateActivity_New.this, R.color.colorPrimary
                    ), android.graphics.PorterDuff.Mode.MULTIPLY);
                } else {
                    btntelep.setBackground(getResources().getDrawable(R.drawable.button_orange));
                    img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
                }
            }
        }

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        date = day + "/"
                + String.format("%02d", (month + 1))
                + "/" + year;
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sunday";
        }

        editTextFollowupDate.setText(date);
        txtcurrentday.setText("Today, " + weekDay);

        //  txt3dateshow.setText(date);

        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this, ContactActivity.class);
                intent.putExtra("callid", callid);
                intent.putExtra("call_prospect", ProspectId);
                intent.putExtra("call_type", calltype);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btnemailid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color
                //get default email

                NatureOfCall = btnemailid.getText().toString();

                btnemailid.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btntelep.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_grey));

                /*if(btnemailid.isClickable()){
                   // txtto.setVisibility(View.VISIBLE);
                }
                else{
                   // txtwhom.setVisibility(View.GONE);
                }*/

            }
        });

        btntelep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NatureOfCall = btntelep.getText().toString();
                //change color
                //get default telephone
                btnemailid.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btntelep.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_grey));

                /*if(btntelep.isSelected()){
                   // txtwhom.setVisibility(View.GONE);
                    //txtto.setVisibility(View.VISIBLE);
                }*/
            }
        });

        btnvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color
                //get default telephone
                NatureOfCall = btnvisit.getText().toString();

                btnemailid.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btntelep.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btnvisit.setBackground(getResources().getDrawable(R.drawable.button_orange));

                /*if(btnvisit.isSelected()){
                   // txtwhom.setVisibility(View.VISIBLE);
                    //txtto.setVisibility(View.GONE);
                }*/
            }
        });


        btnemailid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color
                //get default email
                NatureofAction = btnemailid1.getText().toString();

                btnemailid1.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btntelep1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btnvisit1.setBackground(getResources().getDrawable(R.drawable.button_grey));

                /*if(btnemailid1.isSelected()){
                   // txtwhom.setVisibility(View.VISIBLE);
                    //txtto.setVisibility(View.GONE);
                }*/
            }
        });

        btntelep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color
                //get default telephone

                NatureofAction = btntelep1.getText().toString();

                btnemailid1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btntelep1.setBackground(getResources().getDrawable(R.drawable.button_orange));
                btnvisit1.setBackground(getResources().getDrawable(R.drawable.button_grey));

                /*if(btntelep1.isSelected()){
                    //txtwhom.setVisibility(View.GONE);
                    //txtto.setVisibility(View.VISIBLE);
                }*/
            }
        });

        btnvisit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change color
                //get default telephone

                NatureofAction = btnvisit1.getText().toString();


                btnemailid1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btntelep1.setBackground(getResources().getDrawable(R.drawable.button_grey));
                btnvisit1.setBackground(getResources().getDrawable(R.drawable.button_orange));

                /*if(btnvisit1.isSelected()){
                  //  txtwhom.setVisibility(View.VISIBLE);
                    //txtto.setVisibility(View.GONE);
                }*/
            }
        });


        img_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frm_to_val == true) {
                    frm_to_val = false;
                    txtnamefrm.setText("Me");
                    txtnameto.setText("Customer");
                    if (btnemailid.getText().toString().equalsIgnoreCase("Email")) {
                        txt_whom.setHint("To whom");
                        txt_whom.setHintEnabled(true);
                    } else if (btntelep.getText().toString().equalsIgnoreCase("Telephone")) {
                        txt_whom.setHint("With whom");
                        txt_whom.setHintEnabled(true);
                    } else if (btnvisit.getText().toString().equalsIgnoreCase("Visit")) {
                        txt_whom.setHint("With");
                        txt_whom.setHintEnabled(true);
                    }

                } else {
                    frm_to_val = true;
                    txtnamefrm.setText("Customer");
                    txtnameto.setText("Me");
                    if (btnemailid.getText().toString().equalsIgnoreCase("Email")) {
                        txt_whom.setHint("From whom");
                        txt_whom.setHintEnabled(true);
                    } else if (btntelep.getText().toString().equalsIgnoreCase("Telephone")) {
                        txt_whom.setHint("From with whom");
                        txt_whom.setHintEnabled(true);
                    } else if (btnvisit.getText().toString().equalsIgnoreCase("Visit")) {
                        txt_whom.setHint("By With");
                        txt_whom.setHintEnabled(true);
                    }
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,

                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_IMAGE);

            }
        });

        txtpurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_IMAGE);
            }
        });

        img_contactt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpportunityUpdateActivity_New.this, ContactActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_prospect", Prospect);
                intent.putExtra("call_type", call_type);
                intent.putExtra("Mode", "A");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
            }
        });

        img_fwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intent.hasExtra("type")) {
                    Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                    animation.setDuration(500);
                    len_outcome.setAnimation(animation);
                    len_outcome.animate();
                    animation.start();

                    layHdr_one.setVisibility(View.GONE);
                    layHdr_two.setVisibility(View.GONE);
                    len_outcome.setVisibility(View.VISIBLE);
                    lay_next_action.setVisibility(View.GONE);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                    animation.setDuration(500);
                    layHdr_two.setAnimation(animation);
                    layHdr_two.animate();
                    animation.start();

                    layHdr_one.setVisibility(View.GONE);
                    layHdr_two.setVisibility(View.VISIBLE);
                    len_outcome.setVisibility(View.GONE);
                    lay_next_action.setVisibility(View.GONE);

                    getheader_2();
                }
            }
        });
        img_back_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_one.setAnimation(animation);
                layHdr_one.animate();
                animation.start();
                layHdr_one.setVisibility(View.VISIBLE);
                layHdr_two.setVisibility(View.GONE);
                lay_content_opp.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
            }
        });
        img_fwd_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner_With_Towhom.getText().toString().equalsIgnoreCase("select")) {
                    spinner_With_Towhom.setError("Select With/To Whom");
                    spinner_With_Towhom.setFocusable(true);
                    spinner_With_Towhom.setFocusableInTouchMode(true);
                    spinner_With_Towhom.requestFocus();
                  //  Toast.makeText(OpportunityUpdateActivity_New.this, "Please select contact", Toast.LENGTH_LONG).show();
                } else if (spinner_Followupreason.getText().toString().equalsIgnoreCase("")) {
                    //Toast.makeText(OpportunityUpdateActivity_New.this, "Select followup purpose", Toast.LENGTH_LONG).show();
                    spinner_Followupreason.setError("Select followup purpose");
                    spinner_Followupreason.setFocusable(true);
                    spinner_Followupreason.setFocusableInTouchMode(true);
                    spinner_Followupreason.requestFocus();

                } else {
                    Intent intent = new Intent(OpportunityUpdateActivity_New.this,
                            CountryListActivity.class);

                    IsOutcome = true;
                    isFollowup = false;
                    isReason = false;
                    String url = "";
                    if (calltype.equalsIgnoreCase("1")) {
                        getoutcome = "Sales";
                    } else if (calltype.equalsIgnoreCase("2")) {
                        getoutcome = "Collection";
                    } else if (calltype.equalsIgnoreCase("3")) {
                        getoutcome = "Feedback";
                        layhead.setVisibility(View.GONE);
                    }
                    try {
                        url = CompanyURL + WebUrlClass.api_Outcome
                                + "?CallType=" + URLEncoder.encode(getoutcome, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    intent.putExtra("Table_Name", db.TABLE_Outcome);
                    intent.putExtra("Id", "Code");
                    intent.putExtra("DispName", "Outcome");
                    intent.putExtra("WHClauseParameter", "WHERE OutcomeType='" + getoutcome + "'");
                    intent.putExtra("APIName", url);
                    intent.putExtra("out", "1");
                    startActivityForResult(intent, Followup);
                    Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                    animation.setDuration(1);
                    len_outcome.setAnimation(animation);
                    len_outcome.animate();
                    animation.start();
                    layHdr_one.setVisibility(View.GONE);
                    layHdr_two.setVisibility(View.GONE);
                    lay_next_action.setVisibility(View.GONE);
                    len_outcome.setVisibility(View.VISIBLE);
                }
            }
        });
        img_back_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_one.setAnimation(animation);
                layHdr_one.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.VISIBLE);
                lay_content_opp.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
            }
        });
        img_fwd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                len_outcome.setAnimation(animation);
                len_outcome.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                len_outcome.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.VISIBLE);
            }
        });
        img_back_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(OpportunityUpdateActivity_New.this, R.anim.slide_right_to_left);
                animation.setDuration(500);
                layHdr_one.setAnimation(animation);
                layHdr_one.animate();
                animation.start();
                layHdr_one.setVisibility(View.GONE);
                layHdr_two.setVisibility(View.GONE);
                lay_next_action.setVisibility(View.GONE);
                len_outcome.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gettomorowdate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        String date = dateFormat.format(cal.getTime());
        txt3dateshow.setText(date);
        c.setTime(cal.getTime()); // yourdate is an object of type Date
        int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        String Week = "";
        if (Calendar.MONDAY == DayOfWeek) {
            Week = "Monday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.TUESDAY == DayOfWeek) {
            Week = "Tuesday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.WEDNESDAY == DayOfWeek) {
            Week = "Wednesday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.THURSDAY == DayOfWeek) {
            Week = "Thursday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.FRIDAY == DayOfWeek) {
            Week = "Friday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.SATURDAY == DayOfWeek) {
            Week = "Saturday";
            txtcurrentday_1.setText(Week);
        } else if (Calendar.SUNDAY == DayOfWeek) {
            Week = "Sunday";
            txtcurrentday_1.setText(Week);
        }
        Log.d("Yes", Week);

    }


    private void showProgressDialog() {
        progressbar.setVisibility(View.VISIBLE);
    }


    private void dismissProgressDialog() {
        progressbar.setVisibility(View.GONE);
    }

    private void showProgressDialog1() {


       /* progressDialog = new ProgressDialog(OpportunityUpdateActivity.this);
        progressDialog.setCancelable(true);
        if (!isFinishing()) {
            // show popup
            progressDialog.show();
        }
        progressDialog.setContentView(R.layout.crm_progress_lay);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/


        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(OpportunityUpdateActivity_New.this);
                progressDialog.setCancelable(true);
                progressDialog.setContentView(R.layout.crm_progress_lay);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }


    private void dismissProgressDialog1() {


        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    class DownloadNatureOfCallJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_NatureOfCall
                        + "?CallType=" + URLEncoder.encode(calltype, "UTF-8");

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    natureofcall = new ArrayList();
                    sql.delete(db.TABLE_NatureofCall, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NatureofCall, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    //natureofcall.add("Select");
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            if (columnName.equalsIgnoreCase("NatureofCall")) {
                                natureofcall.add(jorder.getString("NatureofCall"));
                            }

                        }

                        long a = sql.insert(db.TABLE_NatureofCall, null, values);

                    }
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
            dismissProgressDialog();

            if (response.contains("PKNatureofCall")) {
                getNextAction();
                getNatureofCall();

            }
            if (isnet()) {
                new DownloadInitiatedByJSON().execute();
            }

        }

    }

    class DownloadInitiatedByJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_InitiatedBy;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    initiatedby = new ArrayList();
                    sql.delete(db.TABLE_InitiatedBy, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_InitiatedBy, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    // initiatedby.add("Select");
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            if (columnName.equalsIgnoreCase("InitiatedBy")) {
                                initiatedby.add(jorder.getString("InitiatedBy"));
                            }


                        }

                        long a = sql.insert(db.TABLE_InitiatedBy, null, values);

                    }
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
            dismissProgressDialog();
            if (response.contains("PKInitiatedBy")) {
                getInitiatedby();
            }


        }

    }

    class DownloadFollowupWithJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_With_whom +
                        "?FKSuspectId=" + URLEncoder.encode(ProspectId, "UTF-8") +
                        "&CallType=" + URLEncoder.encode(calltype, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    whomwith = new ArrayList();
                    sql.delete(db.TABLE_With_whom, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_With_whom, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    // whomwith.add("Select");
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                            if (calltype.equals("1")) {
                                if (columnName.equalsIgnoreCase("ContactName")) {
                                    columnValue = jorder.getString(columnName);
                                    whomwith.add(jorder.getString("ContactName"));
                                    values.put(columnName, columnValue);
                                } else if (columnName.equalsIgnoreCase("Telephone")) {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                } else if (columnName.equalsIgnoreCase("Mobile")) {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                } else if (columnName.equalsIgnoreCase("PKSuspContactDtlsID")) {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }
                            } else {
                                if (columnName.equalsIgnoreCase("ContactName")) {
                                    columnValue = jorder.getString(columnName);
                                    whomwith.add(jorder.getString("ContactName"));
                                    values.put(columnName, columnValue);
                                } else if (columnName.equalsIgnoreCase("PKSuspContactDtlsID")) {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }
                            }
                        }

                        long a = sql.insert(db.TABLE_With_whom, null, values);
                        Log.e("data :", "" + a);

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
            dismissProgressDialog();
            if (response.contains("ContactName")) {
                getWhomwith();
            } else if (response.contains("[]")) {
                getWhomwith();
            }
        }

    }

    class DownloadFollowupReasonJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getFollowupReason;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    followupreason = new ArrayList();
                    sql.delete(db.TABLE_Followup_reason, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Followup_reason, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    // followupreason.add("Select");

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            if (columnName.equalsIgnoreCase("CallPurposeDesc")) {
                                followupreason.add(jorder.getString("CallPurposeDesc"));
                            }
                        }

                        long a = sql.insert(db.TABLE_Followup_reason, null, values);

                    }
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

            dismissProgressDialog();
            if (response.contains("PKCallPurposeId")) {
                getFollowupreason();
            }


        }

    }

    class DownloadOutcomeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            //progressHUD5 = ProgressHUD.show(OpportunityUpdateActivity.this, " ", false, false, null);
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Outcome
                        + "?CallType=" + URLEncoder.encode(calltype, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);


                    sql.delete(db.TABLE_Outcome, "OutcomeType=?",
                            new String[]{getoutcome});
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Outcome
                            + " WHERE OutcomeType='" + getoutcome + "'", null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);

                            String a = jorder.getString("OutcomeType");
                            if (calltype.equalsIgnoreCase("3")) {
                                if (columnName.equalsIgnoreCase("OutcomeType")) {
                                    values.put(columnName, "Feedback");

                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }
                            } else {

                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);

                            }


                        }

                        long a = sql.insert(db.TABLE_Outcome, null, values);

                    }

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
            dismissProgressDialog();

            if (response.contains("Outcome")) {
                getOutcome();

            }


        }

    }

    class DownloadReasonJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            // progressHUD6 = ProgressHUD.show(OpportunityUpdateActivity.this, " ", false, false, null);
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getReason;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_REASON_Master, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_REASON_Master, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_REASON_Master, null, values);
                        Log.e("data", "" + a);

                    }

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

            dismissProgressDialog();
            if (response != null) {

            }


        }

    }

    class DownloadCategoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            // progressHUD7 = ProgressHUD.show(OpportunityUpdateActivity.this, " ", false, false, null);
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Category;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Category, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Category, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }
                        long a = sql.insert(db.TABLE_Category, null, values);
                        Log.e("", "" + a);
                    }

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
            dismissProgressDialog();
            if (response != null) {

            }
            getCategory();


        }

    }

    class DownloadApproverJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getApprover
                        + "?CallId=" + URLEncoder.encode(callid, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_APPROVER, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_APPROVER, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_APPROVER, null, values);

                    }
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

            dismissProgressDialog();
            if (response != null) {

            }
            //getApprover();


        }

    }

    class DownloadCurrencyMasterJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "errror";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getCurrencyMaster;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_CurrencyMaster, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CurrencyMaster, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_CurrencyMaster, null, values);

                    }
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
            dismissProgressDialog();
            if (response != null) {

            }


        }

    }

    class DownloadOrdertypeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res, res1;
        String response = "error", response1 = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getOrdertypefromcall
                        + "?CallId=" + URLEncoder.encode(callid, "UTF-8");
                String url1 = CompanyURL + WebUrlClass.api_getOrdertype;

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);


                    sql.delete(db.TABLE_OrderTypeMaster, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_OrderTypeMaster, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_OrderTypeMaster, null, values);

                    }
                }
        /*
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {*/


                res1 = ut.OpenConnection(url1);
                if (res1 != null) {
                    response1 = res1.toString().replaceAll("\\\\", "");
                    response1 = response1.replaceAll("\\\\\\\\/", "");
                    response1 = response1.substring(1, response1.length() - 1);
                    ContentValues values1 = new ContentValues();
                    JSONArray jResults1 = new JSONArray(response1);


                    sql.delete(db.TABLE_OrderTypeMaster_All, null,
                            null);
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_OrderTypeMaster_All, null);
                    int count1 = c1.getCount();
                    String columnName1, columnValue1;
                    for (int i = 0; i < jResults1.length(); i++) {
                        JSONObject jorder = jResults1.getJSONObject(i);
                        for (int j = 0; j < c1.getColumnCount(); j++) {

                            columnName1 = c1.getColumnName(j);
                            columnValue1 = jorder.getString(columnName1);
                            values1.put(columnName1, columnValue1);

                        }

                        long a = sql.insert(db.TABLE_OrderTypeMaster_All, null, values1);
                    }
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

            dismissProgressDialog();
            if (response != null) {
                getOrdertype();
            }
            getOrdertype();

        }

    }

    class DownloadTMESENameJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getReceivedby;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_TMESEName, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TMESEName, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_TMESEName, null, values);
                        Log.e("data version updated", "" + a);
                    }
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

            dismissProgressDialog();
            if (response != null) {

            }
            getTMESName();


        }

    }

    class PostOpportunityUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getPostOppUpdate;
            try {
                res = ut.OpenPostConnection(url, params[0], OpportunityUpdateActivity_New.this);

                System.out.println("Call Update 2 :" + params[0]);
                response = res.toString().replaceAll("\\\\", "");
                System.out.println("Call Update 3 :" + response);
                response = response.replaceAll("\\\\\\\\/", "");
                System.out.println("Call Update 4 :" + response);
                response = response.substring(1, response.length() - 1);

                System.out.println("Call Update :" + response);
                System.out.println("Call Update1 :" + finaljson);


            } catch (NullPointerException e) {
                e.printStackTrace();
                response = "error";

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
            status = integer;
            if (integer.equals("Success")) {
                sql.delete(tablename_partial, "CallId=?",
                        new String[]{callid});
                sql.delete(tablename, "CallId=?",
                        new String[]{callid});
                //OpportunityUpdateActivity.this.finish();
                Toast.makeText(OpportunityUpdateActivity_New.this, "Opportunity updated successfully", Toast.LENGTH_LONG).show();

                // onBackPressed();

               /* Intent i = new Intent(OpportunityUpdateActivity_New.this, CRMHomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();*/

            } else {
                Toast.makeText(OpportunityUpdateActivity_New.this, "Opportunity not updated", Toast.LENGTH_LONG).show();
            }
        }

    }

    private class GetCheckGstnNo extends AsyncTask<String, String, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_GetCheckGSTIN + "?ProspectId=" + ProspectId;
            System.out.println("Output :" + url);
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                    System.out.println("Output :" + response);
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
            System.out.println("Output :" + s);
            Status = s;
            dismissProgressDialog();
            new GetprospectTypeID().execute();

        }
    }

    private class GetprospectTypeID extends AsyncTask<String, String, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_GetProspectType + "?ProspectId=" + ProspectId;
            System.out.println("Output :" + url);
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                System.out.println("Output :" + response);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();

            if (s.equalsIgnoreCase("1")) {
                flagIsEnterprise = true;
            } else {
                flagIsEnterprise = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressbar.setVisibility(View.GONE);

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Flag_is_tele.equalsIgnoreCase("")) {
            OpportunityUpdateActivity_New.this.finish();

        }
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.putString("UpdateOpp", "1");
        editor.commit();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (isnet()) {
                showProgressDialog();
                new StartSession(OpportunityUpdateActivity_New.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dismissProgressDialog();
                        new DownloadNatureOfCallJSON().execute();
                        new DownloadInitiatedByJSON().execute();
                        new DownloadFollowupWithJSON().execute();
                        new DownloadFollowupReasonJSON().execute();
                        new DownloadOutcomeJSON().execute();
                        new DownloadReasonJSON().execute();
                        new DownloadOrdertypeJSON().execute();
                        new DownloadTMESENameJSON().execute();
                        new DownloadCategoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), "Can not fetch data due to slow internet connetivity or server error..", Toast.LENGTH_LONG).show();

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        }
        if (id == android.R.id.home) {
            dataObj();
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // dataObj();
    }

    void UpdateOpportunity(final String finaljson1) {
        if ((finaljson1.equalsIgnoreCase("") &&
                finaljson1.equalsIgnoreCase(" ") &&
                finaljson1.equalsIgnoreCase(null))) {
            Toast.makeText(context, "Select Outcome", Toast.LENGTH_LONG).show();
            img_add_1.setClickable(true);
        } else {
            if (selected_outcome_code.equalsIgnoreCase("ATB")) {
                ContentValues cv = new ContentValues();
                cv.put("MobileCallType", "Opportunity");
                sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{rowNo});
            } else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
                ContentValues cv = new ContentValues();
                cv.put("MobileCallType", "Collection");
                sql.update(db.TABLE_CALL_LOG, cv, "RowNo=?", new String[]{rowNo});
            }


            String remark1 = "Apportunity update";
            String url = CompanyURL + WebUrlClass.api_getPostOppUpdate;
            String op = "Success";
            CreateOfflineIntend(url, finaljson1, WebUrlClass.POSTFLAG, remark1, op);
            /*if (isnet()) {
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        showProgressDialog();
                        new PostOpportunityUpdateJSON().execute(finaljson1);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dismissProgressDialog();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });
            }*/
        }
    }

    public int checkInitiatedby() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_InitiatedBy;
        int count = 0;

        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
           long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            // Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(OpportunityUpdateActivity_New.this,
                    SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);

            sql.delete(tablename_partial, "CallId=?",
                    new String[]{callid});
            sql.delete(tablename, "CallId=?",
                    new String[]{callid});
            //OpportunityUpdateActivity.this.finish();
            Toast.makeText(OpportunityUpdateActivity_New.this, "Opportunity updated successfully", Toast.LENGTH_LONG).show();

             onBackPressed();

            SharedPreferences.Editor editor = AtendanceSheredPreferance
                    .edit();
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
            editor.commit();
            Intent sintent = new Intent(OpportunityUpdateActivity_New.this, ForegroundService.class);
            stopService(sintent);

            /*Intent i = new Intent(OpportunityUpdateActivity_New.this, CallHistoryActivity.class);
            i.putExtra("callid", CallId);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();*/
            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved. Please contact administrator", Toast.LENGTH_LONG).show();
        }

    }

/*
    private void dataObj() {
        if (Flag_is_tele.equalsIgnoreCase("Telephone")) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CallId", callid);
                jsonObject.put("ProspectId", ProspectId);
                jsonObject.put("CallType", calltype);
                jsonObject.put("ProductId", "");
                jsonObject.put("FollowupDate", editTextFollowupDate.getText().toString());
                jsonObject.put("FollowupHours", "0");
                jsonObject.put("FollowupReason", "2");
                jsonObject.put("FollowupWith", "");

                jsonObject.put("NatureofCall", "Telephone");

                jsonObject.put("FollowupFrom", edtfrom.getText().toString());
                jsonObject.put("FollowupTo", edtto.getText().toString());

                */
/*    if ((selected_from_time.equalsIgnoreCase("Select"))) {
                    jsonObject.put("FollowupFrom", "");

                } else {
                    jsonObject.put("FollowupFrom", selected_from_time);
                }*//*

                */
/*if ((selected_to_time.equalsIgnoreCase("Select"))
                ) {
                    jsonObject.put("FollowupTo", "");

                } else {
                    jsonObject.put("FollowupTo", selected_to_time);
                }*//*

                if (((((String) spinner_Followupreason.getText().toString().trim()).equalsIgnoreCase("Select")))
                        || (((String) spinner_Followupreason.getText().toString().trim()).equalsIgnoreCase(""))) {
                    jsonObject.put("FollowupReasonName", "");

                } else {
                    jsonObject.put("FollowupReasonName", ((String) spinner_Followupreason.getText().toString().trim()));
                }

                */
/*if ((((String) spinner_Initiatedby.getText().toString().trim()).equalsIgnoreCase("Select"))
                        || (((String) spinner_Initiatedby.getText().toString().trim()).equalsIgnoreCase(""))) {
                    jsonObject.put("InitiatedBy", "");


                } else {
                    jsonObject.put("InitiatedBy", spinner_Initiatedby.getText().toString());

                }*//*

                jsonObject.put("InitiatedBy", txtnamefrm.getText().toString());

                if ((spinner_Nextaction.getText().toString().equalsIgnoreCase("Select"))
                        || (spinner_Nextaction.getText().toString().equalsIgnoreCase(""))) {
                    jsonObject.put("NextAction", "");

                } else {
                    jsonObject.put("NextAction", spinner_Nextaction.getText().toString());
                }
                if (((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase("Select")
                        || ((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase("")
                        || ((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase(" ")
                        || ((String) spinner_With_Towhom.getText().toString().trim()).equalsIgnoreCase(null)) {
                    jsonObject.put("FollowupWithName", "");
                } else {
                    jsonObject.put("FollowupWithName", (String) spinner_With_Towhom.getText().toString().trim());
                }

                jsonObject.put("NextActionDateTime", txt3dateshow.getText().toString()
                        + "=" + txt3scheduledtimeshow.getText().toString());

                String note = "";
                if (TeleCallNature.equalsIgnoreCase("OUT")) {
                    note = "Outgoing call recorded by system";
                } else if (TeleCallNature.equalsIgnoreCase("IN")) {
                    note = "Incoming Call recorded by system";
                } else if (TeleCallNature.equalsIgnoreCase("MISSED")) {
                    note = "Missed Call  by Customer at " + Telestarttime + " recorded by system";
                }
                jsonObject.put("Notes", note);
                jsonObject.put("Outcome", "CA");
                jsonObject.put("TMEDisplayId", UserMasterId);
                jsonObject.put("TMEDisplayName", UserName);
                jsonObject.put("Firm", firmname);

                jsonObject.put("CallReason", "a6abbb7c-ddf6-4cf0-ae23-9dcfbea2039a");
                //Appointment
                jsonObject.put("AssignToSEId", "");
                jsonObject.put("AssignToSEName", "");
                //Transfer to BOE
                jsonObject.put("ReAssignToTMEId", "");
                jsonObject.put("ReAssignToTMEName", "");
                jsonObject.put("CallReasonTTB", "");
                jsonObject.put("CallReasonTTBName", "");
                //Order Received
                jsonObject.put("OR_OrderReceivedById", "");
                jsonObject.put("OR_OrderReceivedByName", "");
                jsonObject.put("OR_OrderReceivedDate", "");
                jsonObject.put("OR_OrderPONo", "");
                jsonObject.put("OR_OrderPOValue", "");
                jsonObject.put("OR_OrderContractReview", "");
                jsonObject.put("OR_OrderType", "");
                //Order Lost
                jsonObject.put("OL_OrderLostReasonId", "");
                jsonObject.put("OL_OrderLostReasonName", "");
                jsonObject.put("OL_OrderLostApproverId", "");
                jsonObject.put("OL_OrderLostApproverName", "");
                jsonObject.put("OL_OrderLostDetails", "");
                jsonObject.put("OL_OrderLostCallCustAgain", "");
                jsonObject.put("OL_OrderLostWhenUCall", "");
                jsonObject.put("OL_OrderLostOLNotes", "");
                //Visit
                jsonObject.put("SV_VisitById", "");
                jsonObject.put("SV_VisitByName", "");
                jsonObject.put("SV_VisitDate", "");
                //Reschedule
                jsonObject.put("Res_RescheduleReasonId", "");
                jsonObject.put("Res_RescheduleReasonName", "");
                //Transfer to SE
                jsonObject.put("RTS_TransferSEId", "");
                jsonObject.put("RTS_TransferSEName", "");
                jsonObject.put("RTS_TransferReasonId", "");
                jsonObject.put("RTS_TransferReasonName", "");
                //Call Close Without Order
                jsonObject.put("CC_CallCloseReasonId", "");
                jsonObject.put("CC_CallCloseReasonName", "");
                jsonObject.put("CC_CallCloseApproverId", "");
                jsonObject.put("CC_CallCloseApproverName", "");
                jsonObject.put("CC_CallCloseDetails", "");
                jsonObject.put("CC_CallCloseCallCustAgain", "");
                jsonObject.put("CC_CallCloseWhenUCall", "");
                jsonObject.put("CC_CallCloseNotes", "");
                //Disput
                jsonObject.put("Disp_Reason", "");
                //COLLCT
                jsonObject.put("Collect_Mode", "");
                jsonObject.put("Collect_InstrNo", "");
                jsonObject.put("Collect_InstrDate", "");
                jsonObject.put("Collect_BankName", "");
                jsonObject.put("Collect_BranchName", "");
                jsonObject.put("Collect_ChqAmount", "");
                jsonObject.put("Collect_TDSAmount", "");
                jsonObject.put("Collect_DiffAmount", "");
                jsonObject.put("Collect_Reason", "");
                //WI / WCF
                jsonObject.put("WIWCF_Details", "");
                //Customer Profile Update (CPU)
                jsonObject.put("CPU_txtEBITDA", "");
                jsonObject.put("CPU_txtPAT", "");
                jsonObject.put("CPU_txtNetworth", "");
                jsonObject.put("CPU_txtBorrowings", "");
                jsonObject.put("CPU_txtRatings", "");
                jsonObject.put("CPU_ddlCurrency", "");
                jsonObject.put("CPU_txtMComments", "");
                jsonObject.put("CPU_ddlCurrencyVal", "");
                //Insurance Update (IU)
                jsonObject.put("IU_txtInsuBank", "");
                jsonObject.put("IU_txtInsCos", "");
                jsonObject.put("IU_txtPreRec", "");
                jsonObject.put("IU_txtPartIns", "");
                //Demo Reschedule (DRes)
                jsonObject.put("DRes_DemoResReasonId", "");
                jsonObject.put("DRes_DemoResReasonName", "");
                jsonObject.put("DRes_DemoGivenById", "");
                jsonObject.put("DRes_DemoGivenByName", "");
                jsonObject.put("DRes_DemoDate", "");
                jsonObject.put("DRes_DemoTime", "");
                //Demo Complete(DC)
                jsonObject.put("DC_chkdemocomplete", "");
                //Demo Cancelled (DCans)
                jsonObject.put("DCans_ReasonId", "");
                jsonObject.put("DCans_ReasonName", "");
                //Customer will Call (CustCall)
                jsonObject.put("CustCall_ReasonId", "");
                jsonObject.put("CustCall_ReasonName", "");
                //Order Regret (Oreg)
                jsonObject.put("OReg_OrderRegretReasonId", "");
                jsonObject.put("OReg_OrderRegretReasonName", "");
                jsonObject.put("OReg_OrderRegretApproverId", "");
                jsonObject.put("OReg_OrderRegretApproverName", "");
                jsonObject.put("OReg_OrderRegretDetails", "");
                jsonObject.put("OReg_OrderRegretCallCustAgain", "");
                jsonObject.put("OReg_OrderRegretWhenUCall", "");
                jsonObject.put("OReg_OrderRegretOLNotes", "");
                //Presales Support (PS)
                jsonObject.put("PS_PresaleSEId", "");
                jsonObject.put("PS_PresaleSEName", "");
                jsonObject.put("PS_PresaleDetails", "");
                jsonObject.put("PS_PresaleDueDate", "");
                // Quotation Submitted(QS)
                jsonObject.put("QS_QuotationNo", "");
                jsonObject.put("QS_CustBudgetSanct", "");
                jsonObject.put("QS_CustBudget", "");
                jsonObject.put("QS_QuotationValue", "");
                jsonObject.put("QS_QuotDoc", "");
                //Promise Date Change (PRMS)
                jsonObject.put("PRMS_NextDate", "");
                jsonObject.put("PRMS_Reason", "");
                finaljson = jsonObject.toString();
                finaljson = finaljson.replaceAll("\\\\", "");
                finaljson = finaljson.replaceAll(" ", " ");
                finaljson = finaljson.replaceAll("=", " ");
                Log.d("crm_dialog_action", "json" + finaljson);
                UpdateOpportunity(finaljson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/

    public void handleBtnVisibility() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_right_to_left);
        animation.setDuration(500);

        if (layVisCnt == 0) {
            layHdr_one.setAnimation(animation);
            layHdr_one.animate();
            animation.start();

            layHdr_one.setVisibility(View.VISIBLE);
            layHdr_two.setVisibility(View.GONE);
            lay_content_opp.setVisibility(View.GONE);
            len_outcome.setVisibility(View.GONE);
            lay_next_action.setVisibility(View.GONE);
        } else if (layVisCnt == 1) {
            layHdr_two.setAnimation(animation);
            layHdr_two.animate();
            animation.start();

            layHdr_one.setVisibility(View.GONE);
            layHdr_two.setVisibility(View.VISIBLE);

            getheader_2();

            len_outcome.setVisibility(View.GONE);
            lay_next_action.setVisibility(View.GONE);
        } else if (layVisCnt == 2) {
            len_outcome.setAnimation(animation);
            len_outcome.animate();
            animation.start();
            layHdr_one.setVisibility(View.GONE);
            layHdr_two.setVisibility(View.GONE);
            lay_content_opp.setVisibility(View.GONE);
            lay_next_action.setVisibility(View.GONE);
            len_outcome.setVisibility(View.VISIBLE);
        } else if (layVisCnt == 3) {
            lay_next_action.setAnimation(animation);
            lay_next_action.animate();
            animation.start();
            layHdr_one.setVisibility(View.GONE);
            layHdr_two.setVisibility(View.GONE);
            lay_content_opp.setVisibility(View.GONE);
            lay_next_action.setVisibility(View.VISIBLE);
            len_outcome.setVisibility(View.GONE);
        }
    }

    private void getheader_2() {

        IsOutcome = false;
        isFollowup = false;
        isReason = false;
        Contact = true;
        isOrder = false;
        isAssignBOE = false;
        isAssignBOESE = false;
        isAssignSE = false;
        isDemo = false;
        Receive=false;
        Approver=false;
        isHandOver=false;
        isPreSale=false;
        isVisit = false;
        isCurrency = false;

        Intent intent = new Intent(OpportunityUpdateActivity_New.this, ContactShowActivity.class);
        intent.putExtra("callid", CallId);
        intent.putExtra("call_prospect", ProspectId);
        intent.putExtra("call_type", call_type);
        intent.putExtra("callmob", LogContact);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Followup);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
    }


    public String calculateFromTime() {
        String frmtime = CurrentDate + " " + currentTime;
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        Date date = null;
        try {
            date = (Date) formatter.parse(frmtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date.getTime());

        Long i = date.getTime();
        long diff = i - (30 * 60000);
        Date date2 = new Date(diff);
        String dt_half = new SimpleDateFormat("hh:mm aa").format(date2);

        return dt_half;

    }


    public void setdate1(String key) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);


        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sunday";
        }

        if (key.equals("YESTERDAY")) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            String date = dateFormat.format(cal.getTime());
            editTextFollowupDate.setText(date);
            c.setTime(cal.getTime()); // yourdate is an object of type Date
            int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            String Week = "";
            if (Calendar.MONDAY == DayOfWeek) {
                Week = "Monday";
                txtcurrentday.setText(Week);
            } else if (Calendar.TUESDAY == DayOfWeek) {
                Week = "Tuesday";
                txtcurrentday.setText(Week);
            } else if (Calendar.WEDNESDAY == DayOfWeek) {
                Week = "Wednesday";
                txtcurrentday.setText(Week);
            } else if (Calendar.THURSDAY == DayOfWeek) {
                Week = "Thursday";
                txtcurrentday.setText(Week);
            } else if (Calendar.FRIDAY == DayOfWeek) {
                Week = "Friday";
                txtcurrentday.setText(Week);
            } else if (Calendar.SATURDAY == DayOfWeek) {
                Week = "Saturday";
                txtcurrentday.setText(Week);
            } else if (Calendar.SUNDAY == DayOfWeek) {
                Week = "Sunday";
                txtcurrentday.setText(Week);
            }
            Log.d("Yes", Week);


        } else if (key.equals("TODAY")) {
            day = c.get(Calendar.DAY_OF_MONTH);
        } else if (key.equals("BEFORE")) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -2);
            String date = dateFormat.format(cal.getTime());
            editTextFollowupDate.setText(date);
            c.setTime(cal.getTime()); // yourdate is an object of type Date
            int DayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            String Week = "";
            if (Calendar.MONDAY == DayOfWeek) {
                Week = "Monday";
                txtcurrentday.setText(Week);
            } else if (Calendar.TUESDAY == DayOfWeek) {
                Week = "Tuesday";
                txtcurrentday.setText(Week);
            } else if (Calendar.WEDNESDAY == DayOfWeek) {
                Week = "Wednesday";
                txtcurrentday.setText(Week);
            } else if (Calendar.THURSDAY == DayOfWeek) {
                Week = "Thursday";
                txtcurrentday.setText(Week);
            } else if (Calendar.FRIDAY == DayOfWeek) {
                Week = "Friday";
                txtcurrentday.setText(Week);
            } else if (Calendar.SATURDAY == DayOfWeek) {
                Week = "Saturday";
                txtcurrentday.setText(Week);
            } else if (Calendar.SUNDAY == DayOfWeek) {
                Week = "Sunday";
                txtcurrentday.setText(Week);
            }
            Log.d("Yes", Week);
        }
        if (key.equals("Tomorrow")) {
            day = c.get(Calendar.DAY_OF_MONTH) + 1;
        } else if (key.equals("TODAY")) {
            day = c.get(Calendar.DAY_OF_MONTH);
        } else if (key.equals("After")) {
            day = c.get(Calendar.DAY_OF_MONTH) + 2;
        }

        if (key.equals("TODAY")) {


            DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity_New.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year,
                                              int monthOfYear, int dayOfMonth) {

                            //  datePicker.setMinDate(c.getTimeInMillis());
                            date = dayOfMonth + "/"
                                    + String.format("%02d", (monthOfYear + 1))
                                    + "/" + year;

                            editTextFollowupDate.setText(date);
                            //  txt3dateshow.setText(date);
                            txtcurrentday.setText(weekDay);



                      /*  boolean a = compare_date(date);
                        boolean b = a;

                        if (a) {
                            editTextFollowupDate.setText(date);
                        }*//* else {

                            Toast.makeText(OpportunityUpdateActivity.this,

                                    "You cannot select a day earlier than today!",
                                    Toast.LENGTH_SHORT).show();
                        }
*/
                        }
                    }, year, month, day);
            datePickerDialog.setTitle("Select Date");
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if (hours >= 12) {
                hours = hours - 12;
                if (hours == 0) {
                    hours = 01;
                }

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




    //Quotation Attachment Development

    private void addMoreImages() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.choose_attachment_option_dialog);
		dialog.setTitle(getResources().getString(R.string.app_name));
		TextView camera = (TextView) dialog.findViewById(R.id.camera);
		TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
		TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
		TextView document=dialog.findViewById(R.id.document);
		document.setVisibility(View.GONE);
		gallery.setVisibility(View.VISIBLE);
		camera.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				requestCameraPermission();

			}
		});
		gallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				requestGalleryPermission();

			}
		});
		document.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				requestDocumentPermission();

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

	private void startGalleryIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMG);

	}
	private void DocumentIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("application/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					RESULT_DOCUMENT);

		} catch (ActivityNotFoundException ex) {
			Toast.makeText(OpportunityUpdateActivity_New.this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
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
	private String getRealPathFromURI(Uri outPutfileUri) {
		Cursor cur = getContentResolver().query(outPutfileUri, null, null, null, null);
		cur.moveToFirst();
		int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cur.getString(idx);

	}


	public static byte[] getFileFromPath(File file) {
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}


	public Uri bitmapToUriConverter(Bitmap mBitmap) {
		Uri uri = null;


		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 100, 100);
			int w = mBitmap.getWidth();
			int h = mBitmap.getHeight();
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, w, h,
					true);
			String path1 = Environment.getExternalStorageDirectory()
					.toString();
			File file = new File(path1 + "/" + "CRM"+"/"+"Sender");
			if (!file.exists())
				file.mkdirs();
			File file1 = new File(file, "Image-"+ new Random().nextInt() + ".jpg");
			if (file1.exists())
				file1.delete();
           /* File file = new File(SharefunctionActivity.this.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");*/
			FileOutputStream out = new FileOutputStream(file1);
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
			out.flush();
			out.close();
			Attachment = file1.getAbsolutePath();
			File f = new File(Attachment);
			//Attachment=f.getName();


			Toast.makeText(OpportunityUpdateActivity_New.this,"Image send successfully",Toast.LENGTH_SHORT).show();

			//CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

			if (isnet()) {
				new StartSession(context, new CallbackInterface() {
					@Override
					public void callMethod() {
						new PostUploadImageMethodProspect().execute();


					}

					@Override
					public void callfailMethod(String msg) {
						ut.displayToast(OpportunityUpdateActivity_New.this, msg);
					}
				});
			} else {
				ut.displayToast(OpportunityUpdateActivity_New.this, "No Internet connection");
				//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
			}





			//	uri = Uri.fromFile(f);
//file:///data/data/vworkbench7.vritti.com.vworkbench7/files/Image1825476171.jpeg


		} catch (Exception e) {
			Log.e("Your Error Message", e.getMessage());
		}
		return uri;
	}


	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public static String getRealPathFromUri(Context context, final Uri uri) {
		// DocumentProvider
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}

		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	private static String getDataColumn(Context context, Uri uri, String selection,
										String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	private static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

    public class PostUploadImageMethodProspect extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;
        String response = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            try {

                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + URLEncoder.encode(EnvMasterId,"UTF-8") +"&ActivityId="+ CallId;
                FileInputStream fileInputStream = new FileInputStream(Attachment);
                Object res = null;
                File file=new File(Attachment);
                response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri , file));
                if (response!= null && (!response.equals(""))) {
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ImageText",e.getMessage());
            }

            return response;

        }

        protected void onPostExecute(String feed) {

            if (feed != null) {

                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_LONG).show();

            }

        }
    }


    public void handleSendImage(Uri imageUri) throws IOException {
        //Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            File file = new File(getCacheDir(), "image");
            InputStream inputStream=getContentResolver().openInputStream(imageUri);
            try {

                OutputStream output = new FileOutputStream(file);
                try {
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }

                    output.flush();
                } finally {
                    output.close();
                }
            } finally {
                inputStream.close();
                byte[] bytes =getFileFromPath(file);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmapToUriConverter(bitmap);
                //Upload Bytes.
            }
        }
    }
private void requestDocumentPermission() {
		if (ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
					201);
		} else {
			DocumentIntent();
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

	private void requestCameraPermission() {
		if (ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.CAMERA)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA},
					200);
		} else {
			startCameraIntent();
		}
	}

	private void requestGalleryPermission() {
		if (ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
					201);
		} else {
			startGalleryIntent();
		}
	}

    private int show(String time) {
        try {
            String outputPattern = "dd/MM/yyyy";
            SimpleDateFormat format = new SimpleDateFormat(outputPattern);

            Date Date1 = format.parse(getdate());
            Date Date2 = format.parse(time);
            long mills = Date2.getTime() - Date1.getTime();
            long Day1 = mills / (1000 * 60 * 60);

            day = (int) Day1 / 24;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }
    private String getdate() {
        String time = "";
        try {
            String outputPattern = "dd/MM/yyyy";

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(outputPattern);
            time = df.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    private void getdialog(String alert) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OpportunityUpdateActivity_New.this);
        LayoutInflater inflater = OpportunityUpdateActivity_New.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.callagain_alert_lay, null);
        dialogBuilder.setView(myView);

        TextView btn_cancel=myView.findViewById(R.id.btn_cancel);
        TextView btn_yes=myView.findViewById(R.id.btn_yes);
        TextView Txt_wait_reshuffle=myView.findViewById(R.id.MO);

        Txt_wait_reshuffle.setText(alert);



        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextWhendoyoucall.setText(date);
                alertDialog.dismiss();




            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextWhendoyoucall.setText(CurrentDate);
                alertDialog.dismiss();
            }
        });






        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}