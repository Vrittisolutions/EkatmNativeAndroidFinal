package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.github.javiersantos.materialstyleddialogs.BuildConfig;
import com.google.gson.Gson;
import com.vritti.ekatm.Constants;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.vritti.chat.activity.AttachmentsActivity;
import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.FileUtilities;
import com.vritti.ekatm.services.EnoJobService;
import com.vritti.expensemanagement.AddExpenseActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.Beans.Attachment;
import com.vritti.ekatm.services.SendOfflineData;

import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.ImageWithLocation.EnoSampleSubmitClass;
import com.vritti.vwb.ImageWithLocation.FileUtils;
import com.vritti.vwb.ImageWithLocation.FinalObjectForENO;
import com.vritti.vwb.ImageWithLocation.ListObjectForEno;
import com.vritti.vwb.ImageWithLocation.SamplePojoClass;
import com.vritti.vwb.ImageWithLocation.Sample_List_Object;
import com.vritti.vwb.classes.CommonFunction;

import static com.vritti.chat.activity.AddChatRoomActivity.getOutputMediaFile;
import static com.vritti.ekatm.services.EnoJobService.isInternetAvailable;


/**
 * Created by 300151 on 11/15/2016.
 */
public class ActivityDetailsActivity extends AppCompatActivity {
    // String[] options;
    ListView ls_activity_option;
    String SourceType, ProjectName, Status, Assigned_By, ActivityCode, Assigned_To, mystring;
    static String ActivityName, ActivityId, SourceId;
    TextView txtstatus, txtprojectgroup, txtassignedby, tv_activity_name;
    Spinner sp_project, sp_module, sp_unit;
    Button btn_save, btn_cancel;
    int year, month, day;
    ArrayList<String> Workspace_list, Subgroup_List, MainGroup_List;
    public static String prjMstId, moduleId, UnitId;
    JSONObject ChangeWorkspaceObj, ChangeActivityStatus, RescheduleObj;
    String FinalObj, ProjectId, PAllowUsrTimeSlotHrs;
    SharedPreferences userpreferences;
    Spinner sp_change_status;
    LinearLayout mLinCharged;
    EditText mEditChaged;
    Button btn_reschedule, btn_rescheduleOk, btn_rescheduleCancel;
    TextView txt_activityName;
    int flag_fromTeam;
    String Mode = "";
    static String StatusFlag = "";
    String IsChargable,
            AssignedById,
            SubActCount,
            SubActStaus,
            ExpectedCompleteDate,
            ExpectedComplete_Date,
            ModifiedBy,
            Modified_By,
            StartDt,
            EndDt,
            IsActivityMandatory,
            IsDelayedActivityAllowed,
            Cd,
            PKModuleMastId,
            PriorityName,
            Colour,
            AddedDt,
            ModifiedDt,
            AssignedById1,
            IsDeleted,
            IsApproved,
            IsChargable1,
            IsApproval,
            AttachmentName,
            AttachmentContent,
            ModifiedDt1,
            UnitName,
            UnitDesc,
            ModuleName,
            ActivityName1,
            Remarks,
            ProjectCode,
            ExpectedComplete_Date1,
            DeptDesc,
            DeptMasterId,
            CompletionIntimate,
            ModifiedBy1,
            ReassignedBy,
            ReassignedDt,
            ActualCompletionDate,
            WarrantyCode,
            TicketCategory,
            IsEndTime,
            IsCompActPresent,
            CompletionActId,
            TktCustReportedBy,
            TktCustApprovedBy,
            IsSubActivity,
            ParentActId,
            ActivityTypeName,
            CompActName, ConMob;
    String IsChatApplicable;
    ArrayList<Attachment> attachmentArrayList = new ArrayList<>();
    String Attachment_count = "", Assign, unapprove = "";
    public static ProgressBar mprogress;
    EditText edt_remark;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;


    private static final int PICK_FILE_REQUEST = 1;

    private Uri fileUri;
    String path, Imagefilename;
    private String serverResponseMessage;
    private String Promise_date, parsedDate;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    static File mediaFile;
    private static final String IMAGE_DIRECTORY_NAME = "Konnect";

    private SimpleDateFormat dateFormatdate;
    String Current_date;
    AsyncTask async;
    private String Vendordata, backToposition;

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonimage = new JSONObject();
    ImageView txt_add_attachment, img_refresh;

    File file;
    private static int RESULT_LOAD_IMG = 2;

    private static final int RESULT_CAPTURE_IMG = 3;
    private static final int RESULT_DOCUMENT = 4;
    private Uri outPutfileUri;
    private int APP_REQUEST_CODE = 4478;
    private String attachment;
    public static FirebaseJobDispatcher dispatcherNew;
    public static Job myJobNew = null;
    private SQLiteDatabase sql;
    FirebaseJobDispatcher firebaseJobDispatcher = EnoSampleSubmitClass.dispatcherNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
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
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        Intent intent = getIntent();

        Assign = intent.getStringExtra("checkassign");
        unapprove = intent.getStringExtra("unapprove");

        Bundle bundle = intent.getExtras();
        ls_activity_option = (ListView) findViewById(R.id.ls_activity_option);
        attachmentArrayList = new ArrayList<>();

        if (bundle != null) {
            ActivityBean selectedObj = (ActivityBean) bundle.getSerializable("actbean");


            ActivityName = selectedObj.getActivityName();
            ActivityId = selectedObj.getActivityId();
            SourceType = selectedObj.getSourceType();
            Assigned_By = selectedObj.getAssigned_By();
            ProjectName = selectedObj.getProjectName();
            Status = selectedObj.getStatus();
            SourceId = selectedObj.getSourceId();
            Assigned_To = selectedObj.getIssuedToName();
            ProjectId = selectedObj.getProjectID();
            PAllowUsrTimeSlotHrs = selectedObj.getPAllowUsrTimeSlotHrs();

            IsChargable = selectedObj.getIsChargable();
            AssignedById = selectedObj.getAssignedById();
            //SubActCount = selectedObj.getSubActCount();
            //SubActStaus = selectedObj.getSubActStaus();
            ExpectedCompleteDate = selectedObj.getExpectedCompleteDate();
            ExpectedComplete_Date = selectedObj.getExpectedComplete_Date();
            ModifiedBy = selectedObj.getModifiedBy();
            Modified_By = selectedObj.getModified_By();
            StartDt = selectedObj.getStartDt();
            EndDt = selectedObj.getEndDt();
            IsActivityMandatory = selectedObj.getIsActivityMandatory();
            IsDelayedActivityAllowed = selectedObj.getIsDelayedActivityAllowed();
            Cd = selectedObj.getCd();
            PKModuleMastId = selectedObj.getPKModuleMastId();
            PriorityName = selectedObj.getPriorityName();
            Colour = selectedObj.getColour();
            AddedDt = selectedObj.getAddedDt();
            ModifiedDt = selectedObj.getModifiedDt();
            IsDeleted = selectedObj.getIsDeleted();
            IsApproved = selectedObj.getIsApproved();
            IsApproval = selectedObj.getIsApproval();
            AttachmentName = selectedObj.getAttachmentName();
           // AttachmentContent = selectedObj.getAttachmentContent();
            UnitName = selectedObj.getUnitName();
            UnitDesc = selectedObj.getUnitDesc();
            ModuleName = selectedObj.getModuleName();
            Remarks = selectedObj.getRemarks();
            ProjectCode = selectedObj.getProjectCode();
            DeptDesc = selectedObj.getDeptDesc();
            DeptMasterId = selectedObj.getDeptMasterId();
            CompletionIntimate = selectedObj.getCompletionIntimate();
            ReassignedBy = selectedObj.getReassignedBy();
            ReassignedDt = selectedObj.getReassignedDt();
            ActualCompletionDate = selectedObj.getActualCompletionDate();
            WarrantyCode = selectedObj.getWarrantyCode();
            TicketCategory = selectedObj.getTicketCategory();
            IsEndTime = selectedObj.getIsEndTime();
            IsCompActPresent = selectedObj.getIsCompActPresent();
            CompletionActId = selectedObj.getCompletionActId();
            TktCustReportedBy = selectedObj.getTktCustReportedBy();
            TktCustApprovedBy = selectedObj.getTktCustApprovedBy();
            IsSubActivity = selectedObj.getIsSubActivity();
            ParentActId = selectedObj.getParentActId();
            ActivityTypeName = selectedObj.getActivityTypeName();
            ConMob = selectedObj.getContMob();
            CompActName = selectedObj.getCompActName();

            flag_fromTeam = intent.getIntExtra("Flag_fromteam", 0);
            if (intent.hasExtra("Mode")) {
                Mode = intent.getStringExtra("Mode");
            }

        }
       /* if (getIntent().hasExtra("SourceId")) {
            Intent i = getIntent();
  }*/

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);


        if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUploadAttachment().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (SourceType == null) {

        } else {

            if (SourceType.equalsIgnoreCase("DocAppr") ||
                    SourceType.equalsIgnoreCase("PreBid") || SourceType.equalsIgnoreCase("Indent") ||
                    SourceType.equalsIgnoreCase("PO") || SourceType.equalsIgnoreCase("SO") ||
                    SourceType.equalsIgnoreCase("Ship") || SourceType.equalsIgnoreCase("CLAIM") ||
                    SourceType.equalsIgnoreCase("Leave") || SourceType.equalsIgnoreCase("ExtraWork") ||
                    SourceType.equalsIgnoreCase("LeaveApply") || SourceType.equalsIgnoreCase("LeaveCancel") ||
                    SourceType.equalsIgnoreCase("RRF")) {
                // options = new String[]{"Chat", "open","Attachments"};
                attachmentArrayList.add(new Attachment("Chat", ""));
                attachmentArrayList.add(new Attachment("Open", ""));
                // attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

            } else {
                if (SourceType.equalsIgnoreCase("Datasheet")) {

                    if(Constants.type == Constants.Type.Sahara){
                        attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                    }else{
                        attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                        attachmentArrayList.add(new Attachment("Change status", ""));
                    }
                    // options = new String[]{"Fill datasheet", "Change status"};
                } else if (SourceType.equalsIgnoreCase("ENO_SAMPLING")) {

                    //  attachmentArrayList.add(new Attachment("Attachments", Attachment_count));
                    attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                } else if (SourceType.equalsIgnoreCase("Support")) {
                    if (EnvMasterId.contains("eno")||EnvMasterId.contains("dabar")) {


                        String actid = ActivityMain.AtendanceSheredPreferance
                                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                        String Starttime = ActivityMain.AtendanceSheredPreferance
                                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                        //  String id = lsActivityList.get(position).getActid();

                        if (actid != null) {

                            if (!(actid.equalsIgnoreCase(ActivityId))) {
               /* Utilities.showCustomMessageDialog(
                        "One Activity is already started...",
                        "Alert!!", parent);*/

                                Toast.makeText(getApplicationContext(), "One Activity has already started...", Toast.LENGTH_LONG).show();
                            } else if (Starttime != null) {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "End");
                                startActivity(myIntent);
                                finish();
                            }
                        } else {
                            if (Starttime != null) {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "End");
                                startActivity(myIntent);
                                finish();


                            } else {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "Start");
                                startActivity(myIntent);
                                finish();

                            }
                        }

                    } else {
                        if (Constants.type == Constants.Type.PM) {
                            attachmentArrayList.add(new Attachment("Chat", ""));
                            attachmentArrayList.add(new Attachment("Timesheet", ""));
                            attachmentArrayList.add(new Attachment("Timesheet log", ""));
                            attachmentArrayList.add(new Attachment("Change status", ""));
                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                            attachmentArrayList.add(new Attachment("Reassign", ""));
                            attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_Subordinate_work), ""));
                            attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_change_workspace), ""));
                            attachmentArrayList.add(new Attachment("Activity trail", ""));

                        }else{
                            attachmentArrayList.add(new Attachment("Chat", ""));
                            attachmentArrayList.add(new Attachment("View ticket", ""));
                            attachmentArrayList.add(new Attachment("Timesheet", ""));
                            attachmentArrayList.add(new Attachment("Timesheet log", ""));
                            attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_Subordinate_work), ""));
                            attachmentArrayList.add(new Attachment("Activity trail", ""));
                        }

                    }


                    // attachmentArrayList.add(new Attachment("Timesheet log", ""));

                    // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                } else if (unapprove.equalsIgnoreCase("true")) {
                    attachmentArrayList.add(new Attachment("Approve", ""));
                    attachmentArrayList.add(new Attachment("Disapprove", ""));
                    attachmentArrayList.add(new Attachment("Activity trail", ""));

                    // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                } else {
                   /* if (EnvMasterId.contains(EnvMasterId)) {
                        Intent myIntent = new Intent();
                        myIntent.setClass(ActivityDetailsActivity.this, EnoSamplingScreen1.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(myIntent);
                        finish();
                    }*/
                   /* if (EnvMasterId.contains(EnvMasterId)) {


                        String actid = ActivityMain.AtendanceSheredPreferance
                                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                        String Starttime = ActivityMain.AtendanceSheredPreferance
                                .getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                        //  String id = lsActivityList.get(position).getActid();

                        if (actid != null) {

                            if (!(actid.equalsIgnoreCase(ActivityId))) {
               *//* Utilities.showCustomMessageDialog(
                        "One Activity is already started...",
                        "Alert!!", parent);*//*
                            } else if (Starttime != null) {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "End");
                                startActivity(myIntent);
                                finish();
                            }
                        } else {
                            if (Starttime != null) {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "End");
                                startActivity(myIntent);
                                finish();


                            } else {
                                Intent myIntent = new Intent();
                                myIntent.setClass(ActivityDetailsActivity.this, LoggingTimeActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                myIntent.putExtra("ActivityId",
                                        ActivityId);
                                myIntent.putExtra("ActivityName", ActivityName);
                                myIntent.putExtra("Flag", "Start");
                                startActivity(myIntent);
                                finish();

                            }
                        }

                    }*/
                    if (Mode.equalsIgnoreCase("")) {
                        if (flag_fromTeam == 1) {

                            attachmentArrayList.add(new Attachment("Chat", ""));
                            attachmentArrayList.add(new Attachment("Change status", ""));
                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                            attachmentArrayList.add(new Attachment("Reassign", ""));
                            attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_Subordinate_work), ""));
                            attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_change_workspace), ""));
                            attachmentArrayList.add(new Attachment("Activity trail", ""));
                            //    attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                            // options = new String[]{"Chat", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                        } else {
                            if(Assign.equalsIgnoreCase("true")) {
                                attachmentArrayList.add(new Attachment("Edit", ""));
                            }else{

                            }
                                attachmentArrayList.add(new Attachment("Chat", ""));
                                attachmentArrayList.add(new Attachment("Timesheet", ""));
                                attachmentArrayList.add(new Attachment("Timesheet log", ""));
                                attachmentArrayList.add(new Attachment("Change status", ""));
                                attachmentArrayList.add(new Attachment("Reschedule", ""));
                                attachmentArrayList.add(new Attachment("Reassign", ""));
                                attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_Subordinate_work), ""));
                                attachmentArrayList.add(new Attachment(getResources().getString(R.string.Detail_activity_change_workspace), ""));
                                attachmentArrayList.add(new Attachment("Activity trail", ""));


                            //    attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                            // options = new String[]{"Chat", "Timesheet", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                        }
                    } else {

                        attachmentArrayList.add(new Attachment("Chat", ""));
                        attachmentArrayList.add(new Attachment("Approve", ""));
                        attachmentArrayList.add(new Attachment("Disapprove", ""));
                        attachmentArrayList.add(new Attachment("Activity trail", ""));
                        //  attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                        //  options = new String[]{"Chat", "Approve", "Disapprove", "Activity trail","Attachments"};
                    }
                }
            }
        }
        MySpinnerAdapter customoption = new MySpinnerAdapter(ActivityDetailsActivity.this,
                R.layout.vwb_custom_spinner_txt, attachmentArrayList);
        ls_activity_option.setAdapter(customoption);

        InitView();
        setListner();
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        txtprojectgroup = (TextView) findViewById(R.id.txtprojectgroup);
        txtassignedby = (TextView) findViewById(R.id.txtassignedby);
        txt_activityName = (TextView) findViewById(R.id.txt_activityName);
        txtassignedby.setText(Assigned_By);
        txtprojectgroup.setText(ProjectName);
        txtstatus.setText(Status);
        txt_activityName.setText(ActivityName);
        //   ls_activity_option.setAdapter(new ActivityOptionAdapter(this, options));


        Workspace_list = new ArrayList<String>();
        Subgroup_List = new ArrayList<String>();
        MainGroup_List = new ArrayList<String>();
    }

    private void setListner() {
        ls_activity_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = attachmentArrayList.get(position).getOption();

                if(option.equalsIgnoreCase("Edit")){
                    Intent intent = new Intent(ActivityDetailsActivity.this, AssignActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);//Assigned_To
                    intent.putExtra("Assigned_To", Assigned_To);
                    intent.putExtra("ProjectId", ProjectId);
                    intent.putExtra("FlagFromTeam", flag_fromTeam);
                    intent.putExtra("Mode","Edit");
                    startActivity(intent);
                    finish();
                } else if (option.equalsIgnoreCase("Timesheet")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, AddTimesheetActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("PAllowUsrTimeSlotHrs", PAllowUsrTimeSlotHrs);
                    startActivity(intent);
                } else if (option.equalsIgnoreCase("Reassign")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, ReassignActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);//Assigned_To
                    intent.putExtra("Assigned_To", Assigned_To);
                    intent.putExtra("ProjectId", ProjectId);
                    intent.putExtra("FlagFromTeam", flag_fromTeam);
                    startActivity(intent);

                } else if (option.equalsIgnoreCase("fill Datasheet")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, DatasheetMainActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + SourceType);// Source Type
                    intent.putExtra("projmasterId", ProjectId);
                    intent.putExtra("AssignBy", Assigned_By);
                    intent.putExtra("AssignById", AssignedById);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else if (option.equalsIgnoreCase("Change status")) {
                    CreateChangeStatusdialog();
                } else if (option.equalsIgnoreCase(getResources().getString(R.string.Detail_activity_change_workspace))) {
                    CreateChangeWorkspaceDialog();
                } else if (option.equalsIgnoreCase("Reschedule")) {
                    CreateRescheduledialog();
                } else if (option.equalsIgnoreCase(getResources().getString(R.string.Detail_activity_Subordinate_work))) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, AssignActivity.class);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("IsSubordinate", "Y");
                    startActivity(intent);
                    // finish();
                } else if (option.equalsIgnoreCase("Open")) {
                    if (ActivityName.contains("RRF")) {
                        Toast.makeText(ActivityDetailsActivity.this, "This functionality not available in mobile application", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(ActivityDetailsActivity.this, ActivityIndentApprovalNew.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("ActivityNAme", ActivityName);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("AssignedById", AssignedById);
                        intent.putExtra("Assigned_By", Assigned_By);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else if (option.equalsIgnoreCase("View ticket")) {
                    String company_name = "http://www.appnet.co.in";
                    //String company_name1 = "http://c207.ekatm.com";
                  //  String company_name3 = "http://dna.ekatm.com";
                    String company_name2 = "http://www.dconnect.in";
                    //www.appnet.co.in
                    if (CompanyURL.equals(company_name2)|| CompanyURL.equals(company_name)) {
                        Intent intent = new Intent(ActivityDetailsActivity.this, TicketUpdateDEPLActivity.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("ProjectID", ProjectId);
                        startActivity(intent);
                        // finish();
                    }else if (EnvMasterId.contains("dna")) {
                        Intent intent = new Intent(ActivityDetailsActivity.this, TicketUpdateDNAActivity.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("ProjectID", ProjectId);
                        startActivity(intent);
                        // finish();
                    } else {
                        Intent intent = new Intent(ActivityDetailsActivity.this, ViewTicketMain.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("ProjectID", ProjectId);
                        intent.putExtra("Contact", ConMob);
                        startActivity(intent);
                        // finish();
                    }
                } else if (option.equalsIgnoreCase("Approve")) {
                    if (unapprove.equalsIgnoreCase("true")) {
                        CreateApprrovejson();
                    } else {
                        CreateApprrovedialog();
                    }
                } else if (option.equalsIgnoreCase("Disapprove")) {
                    if (unapprove.equalsIgnoreCase("true")) {
                        CreateDisapprrovejson();
                    } else {
                        ChangedisApproveJson();
                    }
                } else if (option.equalsIgnoreCase("Activity trail")) {
                    startActivity(new Intent(ActivityDetailsActivity.this, ActivityTrailActivity.class)
                            .putExtra("ActId", ActivityId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                } else if (option.equalsIgnoreCase("Chat")) {
                    if (IsChatApplicable.equalsIgnoreCase("true")) {
                        Intent intent = new Intent(ActivityDetailsActivity.this, MultipleGroupActivity.class);
                        intent.putExtra("callid", ActivityId); // Source Id
                        intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + SourceType);// Source Type
                        intent.putExtra("firm", ActivityName);
                        intent.putExtra("projmasterId", ProjectId);
                        intent.putExtra("AssignBy", Assigned_By);
                        intent.putExtra("AssignById", AssignedById);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Chat module is not installed", Toast.LENGTH_SHORT).show();
                    }
                } else if (option.equalsIgnoreCase("Attachments")) {

                    startActivity(new Intent(ActivityDetailsActivity.this,
                            AttachmentsActivity.class).putExtra("SourceId", SourceId).putExtra("ActivityId", ActivityId));//Assigned_To
                } else if (option.equalsIgnoreCase("Timesheet log")) {
                    startActivity(new Intent(ActivityDetailsActivity.this, TimeSheetLogActivity.class).putExtra("ActId", ActivityId)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

            }
        });


    }

    public void CreateRescheduledialog() {
        Date Edate;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.vwb_dialog_reschedule, null);
        dialogBuilder.setView(dialogView);
        /*final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_dialog_reschedule);
        dialog.setTitle("Select New End Date");*/
        btn_reschedule = (Button) dialogView.findViewById(R.id.btn_reschedule);
        btn_rescheduleCancel = (Button) dialogView.findViewById(R.id.btn_rescheduleCancel);
        btn_rescheduleOk = (Button) dialogView.findViewById(R.id.btn_rescheduleOk);
        edt_remark = (EditText) dialogView.findViewById(R.id.edt_remark);

        String que = "SELECT EndDate FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityId='" + ActivityId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String EndDresults = cur.getString(cur.getColumnIndex("EndDate"));
            EndDresults = EndDresults.substring(EndDresults.indexOf("(") + 1, EndDresults.lastIndexOf(")"));
            long Etime = Long.parseLong(EndDresults);
            Edate = new Date(Etime);
            String FEndDate = sdf.format(Edate);
            btn_reschedule.setText(FEndDate);
        }
        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth)) + "-" + year;
                                btn_reschedule.setText(date);



                            }
                        }, year, month, day);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();
        btn_rescheduleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        btn_rescheduleOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRescheduleJSONObj();
                b.dismiss();
            }
        });
    }

    private void getRescheduleJSONObj() {
        RescheduleObj = new JSONObject();
        try {
            RescheduleObj.put("ActivityId", ActivityId);
            RescheduleObj.put("EndDate", btn_reschedule.getText().toString());
            RescheduleObj.put("ResRemark", edt_remark.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = RescheduleObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        String remark = "Reschedule activity  " + ActivityName + " to " + btn_reschedule.getText().toString();
        String url = CompanyURL + WebUrlClass.api_Reschedule + "?ActivityId=" + ActivityId + "&EndDate=" + btn_reschedule.getText().toString() + "&ResRemark=" + edt_remark.getText().toString();

       /* JSONObject jobj = new JSONObject();
        try {
            jobj.put("output1","Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String op = jobj.toString();*/
        String op = "Success";
        CreateOfflineModeReschedule(url, null, WebUrlClass.GETFlAG, remark, op);
        onBackPressed();
       /* new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new UpdaterescheduleActivity().execute(ActivityId,btn_reschedule.getText().toString());
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
            }
        });*/
    }

    public void CreateChangeStatusdialog() {
        final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_change_activity_status);//ed_billable_client ln_billable_client
        sp_change_status = (Spinner) dialog.findViewById(R.id.sp_change_status);
        mLinCharged = (LinearLayout) dialog.findViewById(R.id.ln_billable_client);
        mEditChaged = (EditText) dialog.findViewById(R.id.ed_billable_client);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        String[] change_status = {"WIP", "Complete", "Pause", "Cancelled"};
        dialog.setCancelable(true);
        dialog.setTitle("Change Status");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, change_status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_change_status.setAdapter(dataAdapter);
        dialog.show();
        sp_change_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp_status = parent.getItemAtPosition(position).toString();
                if (sp_status.equalsIgnoreCase("Complete")) {
                    if (IsChargable.equalsIgnoreCase("true")
                            && IsApproval.equalsIgnoreCase("true")) {
                        mLinCharged.setVisibility(View.VISIBLE);
                    } else {
                        mLinCharged.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivityStatusJSONObj();
                dialog.dismiss();

            }
        });

    }


    public void CreateApprrovedialog() {
        final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_approve_periodic_billing);//ed_billable_client ln_billable_client
        mEditChaged = (EditText) dialog.findViewById(R.id.ed_billable_client);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialog.setCancelable(true);
        dialog.setTitle("Approve Status");
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mEditChaged.getText().toString().equalsIgnoreCase(""))) {
                    String amt = mEditChaged.getText().toString();
                    ChangeApproveJson(amt);

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Amount", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void getProject() {
        Workspace_list.clear();
        String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("ProjectName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_project.setAdapter(adapter);
            sp_project.setSelection(Workspace_list.indexOf(ProjectName));
        }
        sql.close();
    }

    private void getMainGroupList() {
        MainGroup_List.clear();
        String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ProjectId='" + prjMstId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MainGroup_List.add(cur.getString(cur.getColumnIndex("ModuleName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, MainGroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_module.setAdapter(adapter);
        }
        sql.close();
    }

    private void getSubGroupList() {
        Subgroup_List.clear();
        String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE PKModuleMastId='" + moduleId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Subgroup_List.add(cur.getString(cur.getColumnIndex("UnitDesc")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, Subgroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_unit.setAdapter(adapter);
        }
        sql.close();
    }


    private void CreateChangeWorkspaceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialog = inflater.inflate(R.layout.dialog_change_workspace, null);
        dialogBuilder.setView(dialog);

        tv_activity_name = (TextView) dialog.findViewById(R.id.tv_activity_name);
        sp_project = (Spinner) dialog.findViewById(R.id.sp_project);
        sp_module = (Spinner) dialog.findViewById(R.id.sp_module);
        sp_unit = (Spinner) dialog.findViewById(R.id.sp_unit);
        btn_save = (Button) dialog.findViewById(R.id.btn_save);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        SetListnerChangeWorkspace();
        getProject();
        dialogBuilder.setCancelable(false);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        tv_activity_name.setText(ActivityName);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeWorkspaceJSONObj();
                b.dismiss();
            }
        });

    }

    private void CreateOfflineModeChageStatus(final String url, final String parameter,
                                              final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagCancel)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});

            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagWIP)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "14");
                values.put("Status", "WIP");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                sql1.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});

            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagPause)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "25");
                values.put("Status", "PAUSED");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                sql1.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});

            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagComplete)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});


            }
            sql1.close();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

    private void CreateOfflineModeChangeWorkspace(final String url, final String parameter,
                                                  final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);

            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void CreateOfflineModeDisapprove(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record saved for Disapprove", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);

            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void CreateOfflineModeReschedule(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler cf = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql = db.getWritableDatabase();
            String data = btn_reschedule.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date date = null;
            try {
                date = sdf.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long startDate = date.getTime();
            String fiDate = "/Date(" + startDate + ")/";
            ContentValues values = new ContentValues();
            values.put("EndDate", fiDate);
            sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
            sql.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});
            Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }


    /*  private void SaveOfflineMode() {
          Button bt_save, bt_cancel;
          final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
          dialog.setContentView(R.layout.vwb_dialog_custom_offline_check);
          bt_save = (Button) dialog.findViewById(R.id.btn_save);
          bt_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
          dialog.setCancelable(false);
          dialog.show();
          bt_cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  dialog.dismiss();
              }
          });
          bt_save.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_LONG).show();
                  dialog.dismiss();
              }
          });

      }*/
    private void SetListnerChangeWorkspace() {

        sp_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProjectName = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST + " WHERE ProjectName='" + ProjectName + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    prjMstId = cur.getString(cur.getColumnIndex("ProjectId"));
                }
                getMainGroupList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_module.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ModuleName = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ModuleName='" + ModuleName + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    moduleId = cur.getString(cur.getColumnIndex("PKModuleMastId"));
                }
                getSubGroupList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitdesc = parent.getItemAtPosition(position).toString();


                String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE UnitDesc='" + unitdesc + "'";
                SQLiteDatabase sql = db.getWritableDatabase();

                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UnitId = cur.getString(cur.getColumnIndex("UnitId"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ChangeActivityStatusJSONObj() {

        ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

           /* string ActivityId = JsonData.ActivityId;
            string StatusCode = JsonData.StatusCode;
            string IsApproval = JsonData.IsApproval;
            string Amt = JsonData.Amt;*/
            ChangeActivityStatus.put("ActivityId", ActivityId);
            if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Cancelled")) {
                StatusFlag = WebUrlClass.FlagCancel;
                ChangeActivityStatus.put("StatusCode", "15");
                remark = "Cancel the activity " + ActivityName;

            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("WIP")) {
                StatusFlag = WebUrlClass.FlagWIP;
                ChangeActivityStatus.put("StatusCode", "14");
                remark = "Change status of activity " + ActivityName + " to WIP ";

            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Pause")) {
                StatusFlag = WebUrlClass.FlagPause;
                ChangeActivityStatus.put("StatusCode", "25");
                remark = "Change status of activity " + ActivityName + " to Pause ";
            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Complete")) {
                StatusFlag = WebUrlClass.FlagComplete;
                if (mLinCharged.getVisibility() == View.VISIBLE) {
                    String d = mEditChaged.getText().toString();
                    ChangeActivityStatus.put("StatusCode", "12");
                    ChangeActivityStatus.put("IsApproval", IsApproval);
                    ChangeActivityStatus.put("Amt", d);
                } else {
                    ChangeActivityStatus.put("StatusCode", "12");
                    ChangeActivityStatus.put("IsApproval", IsApproval);
                    ChangeActivityStatus.put("Amt", "");
                }

                remark = "Complete the activity " + ActivityName;

            }
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpdateChangeActivityStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {*/
        String url = CompanyURL + WebUrlClass.api_change_activity_status;
        String op = "Success";
        CreateOfflineModeChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);


    }

    private void ChangeApproveJson(String amt) {

        JSONObject ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

            ChangeActivityStatus.put("ActivityId", ActivityId);
            ChangeActivityStatus.put("StatusCode", amt);

            remark = "Approve the activity " + ActivityName + " with billable amount " + amt;

            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = CompanyURL + WebUrlClass.api_Approve_periodic;
        String op = "";
        CreateOfflineModeChangeWorkspace(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

    }

    private void ChangedisApproveJson() {

        JSONObject ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

            ChangeActivityStatus.put("ActivityId", ActivityId);
            remark = "DisApprove the activity " + ActivityName;
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = CompanyURL + WebUrlClass.api_Disapprove_periodic;
        String op = "";
        CreateOfflineModeDisapprove(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        finish();
    }

    private void ChangeWorkspaceJSONObj() {
        String que = "SELECT ActivityCode FROM " + db.TABLE_ACTIVITYMASTER_PAGING+ " WHERE ActivityId='" + ActivityId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            ActivityCode = cur.getString(cur.getColumnIndex("ActivityCode"));
        }
        ChangeWorkspaceObj = new JSONObject();
        try {
            ChangeWorkspaceObj.put("ActivityId", ActivityId);
            ChangeWorkspaceObj.put("ActivityNm", ActivityName);
            ChangeWorkspaceObj.put("ActivityCode", ActivityCode);
            ChangeWorkspaceObj.put("ProjectId", prjMstId);
            ChangeWorkspaceObj.put("UnitId", UnitId);
            ChangeWorkspaceObj.put("ModuleId", moduleId);
            ChangeWorkspaceObj.put("ActivityType", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = ChangeWorkspaceObj.toString();
        String remark = "Change assignment of activity  " + ActivityName + " to " + ProjectName;
        String url = CompanyURL + WebUrlClass.api_change_workspace;
        String op = "";
        CreateOfflineModeChangeWorkspace(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        onBackPressed();

       /* new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new UpdateChangeWorkspace().execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);

            }
        });*/
    }

    /* public class UpdateChangeWorkspace extends AsyncTask<Integer, Void, Integer> {
         Object res;
         String responce;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected void onPostExecute(Integer integer) {
             super.onPostExecute(integer);
             if (responce.toString().equalsIgnoreCase("")) {
                 Toast.makeText(ActivityDetailsActivity.this, "Workspace changed successfully", Toast.LENGTH_LONG).show();
             }
         }

         @Override
         protected Integer doInBackground(Integer... params) {
             String url = CompanyURL + WebUrlClass.api_change_workspace;
             res = ut.OpenPostConnection(url, FinalObj);
             responce = res.toString().substring(1, res.toString().length() - 1);

             return null;
         }
     }
 */
    public class UpdaterescheduleActivity extends AsyncTask<String, Void, Integer> {
        Object res;
        String responce;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (responce.toString().equalsIgnoreCase("")) {
                Toast.makeText(ActivityDetailsActivity.this, "Activity Rescheduled successfully", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Reschedule + "?ActivityId=" + params[0] + "&EndDate=" + params[1];
            res = ut.OpenPostConnection(url, FinalObj, getApplicationContext());
            responce = res.toString().substring(1, res.toString().length() - 1);
            return 0;
        }
    }

    public class UpdateChangeActivityStatus extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String responce;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (responce.toString().equalsIgnoreCase("Success")) {
                SQLiteDatabase sql = db.getWritableDatabase();
                Toast.makeText(ActivityDetailsActivity.this, "Activity status changed successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityDetailsActivity.this, ActivityMain.class);
                startActivity(intent);
                finish();
                if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagCancel)) {
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});

                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagWIP)) {
                    ContentValues values = new ContentValues();
                    values.put("Cd", "14");
                    values.put("Status", "WIP");
                    sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                    sql.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});
                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagPause)) {
                    ContentValues values = new ContentValues();
                    values.put("Cd", "25");
                    values.put("Status", "PAUSED");
                    sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                    sql.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});
                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagComplete)) {
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});

                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_change_activity_status;
            res = ut.OpenPostConnection(url, FinalObj, getApplicationContext());
            responce = res.toString().substring(1, res.toString().length() - 1);

            return null;
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finish();
       /* startActivity(new Intent(ActivityDetailsActivity.this,ActivityMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();*/


    }

   /* @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Toast.makeText(getApplicationContext(),"In restore ",Toast.LENGTH_SHORT).show();
    }*/


    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    private class MySpinnerAdapter extends ArrayAdapter<Attachment> {
        // Initialise custom font, for example:
        ArrayList<Attachment> attachmentArrayList = new ArrayList<>();


        public MySpinnerAdapter(Context context, int textViewResourceId, ArrayList<Attachment> attachmentArrayList) {
            super(context, textViewResourceId, attachmentArrayList);
            this.attachmentArrayList = attachmentArrayList;
            attachmentArrayList = attachmentArrayList;
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vwb_attachnmet_count, null);
            TextView textView = (TextView) v.findViewById(R.id.txt);
            TextView text_cartcount = (TextView) v.findViewById(R.id.text_cartcount);
            textView.setText(attachmentArrayList.get(position).getOption());

            if (attachmentArrayList.get(position).getOption().equalsIgnoreCase("Attachments")) {
                text_cartcount.setVisibility(View.VISIBLE);
                text_cartcount.setText(attachmentArrayList.get(position).getCount());
            }
            return v;

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

    class DownloadUploadAttachment extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {

               /* if (res.contains("error")||res.equalsIgnoreCase("[]")) {


                } else {*/
                try {
                    JSONArray jResults = new JSONArray(res);
                    if (jResults.length() > 0) {
                        Attachment_count = String.valueOf(jResults.length());
                        if (SourceType.equalsIgnoreCase("DocAppr")
                                || SourceType.equalsIgnoreCase("PreBid")
                                || SourceType.equalsIgnoreCase("Indent")
                                || SourceType.equalsIgnoreCase("PO")
                                || SourceType.equalsIgnoreCase("SO")
                                || SourceType.equalsIgnoreCase("Ship")
                                || SourceType.equalsIgnoreCase("CLAIM")
                                || SourceType.equalsIgnoreCase("Leave")
                                || SourceType.equalsIgnoreCase("ExtraWork")
                                || SourceType.equalsIgnoreCase("LeaveApply")
                                || SourceType.equalsIgnoreCase("LeaveCancel")
                                || SourceType.equalsIgnoreCase("RRF")) {
                            // options = new String[]{"Chat", "open","Attachments"};
                                /*attachmentArrayList.add(new Attachment("Chat", ""));
                                attachmentArrayList.add(new Attachment("Open", ""));*/
                            attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                        } else {
                            if (SourceType.equalsIgnoreCase("Datasheet")) {

                                //attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                                // attachmentArrayList.add(new Attachment("Change status", ""));
                                // options = new String[]{"Fill datasheet", "Change status"};
                            } else if (SourceType.equalsIgnoreCase("ENO_SAMPLING")) {
                                attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                            } else if (SourceType.equalsIgnoreCase("Support")) {
                                //  attachmentArrayList.add(new Attachment("Chat", ""));
                                //  attachmentArrayList.add(new Attachment("View ticket", ""));
                                // attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                // attachmentArrayList.add(new Attachment("Activity trail", ""));
                                //   attachmentArrayList.add(new Attachment("Timesheet log", ""));

                                // attachmentArrayList.add(new Attachment("View ticket", ""));

                                // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                            } else if (unapprove.equalsIgnoreCase("true")) {
                                // attachmentArrayList.add(new Attachment("Approve", ""));
                                // attachmentArrayList.add(new Attachment("Disapprove", ""));
                                // attachmentArrayList.add(new Attachment("Activity trail", ""));

                                // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                            } else {
                                if (Mode.equalsIgnoreCase("")) {
                                    if (flag_fromTeam == 1) {
/*                                            attachmentArrayList.add(new Attachment("Chat", ""));
                                            attachmentArrayList.add(new Attachment("Change status", ""));
                                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                                            attachmentArrayList.add(new Attachment("Reassign", ""));
                                            attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                            attachmentArrayList.add(new Attachment("Change workspace", ""));
                                            attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                        attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                                        // options = new String[]{"Chat", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                                    } else {
                                        /* attachmentArrayList.add(new Attachment("Chat", ""));
                                         *//*if (Assign.equalsIgnoreCase("true")){
                                                attachmentArrayList.add(new Attachment("Timesheet log", ""));
                                            }else {
                                            }*//*
                                            attachmentArrayList.add(new Attachment("Timesheet", ""));
                                            attachmentArrayList.add(new Attachment("Timesheet log", ""));
                                            attachmentArrayList.add(new Attachment("Change status", ""));
                                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                                            attachmentArrayList.add(new Attachment("Reassign", ""));
                                            attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                            attachmentArrayList.add(new Attachment("Change workspace", ""));
                                            attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                        attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                                        // options = new String[]{"Chat", "Timesheet", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                                    }
                                } else {

                                       /* attachmentArrayList.add(new Attachment("Chat", ""));
                                        attachmentArrayList.add(new Attachment("Approve", ""));
                                        attachmentArrayList.add(new Attachment("Disapprove", ""));
                                        attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                    attachmentArrayList.add(new Attachment("Attachments", Attachment_count));


                                    //  options = new String[]{"Chat", "Approve", "Disapprove", "Activity trail","Attachments"};
                                }
                            }
                        }
                    }
                    MySpinnerAdapter customoption = new MySpinnerAdapter(ActivityDetailsActivity.this,
                            R.layout.vwb_custom_spinner_txt, attachmentArrayList);
                    ls_activity_option.setAdapter(customoption);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUploadedAttachment +
                        "?activityId=" + URLEncoder.encode(ActivityId, "UTF-8")+"&SourceType=Activity";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
            if (res != null) {
                res = res.replaceAll("\\\\", "");
                res = res.toString();

                // res = res.substring(1, res.length() - 1);
            }
            return "";

        }
    }

    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);


    }

    private void CreateApprrovejson() {

        JSONObject jsonapprove = new JSONObject();
        String remark = "";
        try {

            jsonapprove.put("ActivityId", ActivityId);
            jsonapprove.put("StatusCode", "0");

            remark = "Complete activity " + ActivityName;


            FinalObj = jsonapprove.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = CompanyURL + WebUrlClass.api_PostApproveStatus;
        String op = "";
        CreateOfflineApproveChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        //onBackPressed();


    }

    private void CreateDisapprrovejson() {

        JSONObject jsondisapprove = new JSONObject();
        String remark = "";
        try {


            jsondisapprove.put("ActivityId", ActivityId);
            remark = "Complete the activity " + ActivityName;
            FinalObj = jsondisapprove.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpdateChangeActivityStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {*/
        String url = CompanyURL + WebUrlClass.api_PostActivityDisapprove;
        String op = "";
        CreateOfflineApproveChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        //  onBackPressed();

    }

    private void CreateOfflineApproveChageStatus(final String url, final String parameter,
                                                 final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            sql1.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
            sql1.close();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_upload) {

//            Intent intent = new Intent();
//            //sets the select file to all types of files
//            intent.setType("*/*");
//            //allows to select data and return it
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            //starts new activity to select file and return data
//            startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST);

            addMoreImages();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri selectedFileUri = data.getData();
                path = FileUtilities.getPath(this, selectedFileUri);
                File f = new File(FileUtilities.getPath(this, selectedFileUri));
                Imagefilename = f.getName();

                //File originalFile = new File(path);
                try {
                    FileInputStream fileInputStreamReader = new FileInputStream(f);
                    byte[] bytes = new byte[(int) f.length()];
                    fileInputStreamReader.read(bytes);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // image_encode=getStringFile(f);

           */
/*     StringTokenizer tokens = new StringTokenizer(Imagefilename, ":");
                String file_1 = tokens.nextToken().trim();*//*



                if (isnet()) {
                    PostUploadImageMethod postUploadImageMethod = new PostUploadImageMethod();
                    postUploadImageMethod.execute();
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                File f = new File(fileUri.getPath().toString());
                path = f.toString();
                Imagefilename = f.getName();
                // new VideoCompressor().execute();


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    File f = new File(fileUri.getPath().toString());
                    path = f.toString();
                    Imagefilename = f.getName();

                    if (isnet()) {
                        async = new PostUploadImageMethod().execute();

                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled operation", Toast.LENGTH_SHORT)
                        .show();
            } else {

            }
        }
    }
*/

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();

        }

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(path);
                // String upLoadServerUri="http://192.168.1.53/api/ChatRoomAPI/UploadFiles";

                String upLoadServerUri = CompanyURL + WebUrlClass.api_FileUpload;

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", path);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + path + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();


                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {
//

                    ActivityDetailsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            hidprogress();
                            Toast.makeText(ActivityDetailsActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            //attachment_name.setText(Imagefilename);

                            // jsonArray.put(Imagefilename);
                            try {
                                jsonimage.put("File", Imagefilename);
                                jsonArray.put(jsonimage);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        ActivityDetailsActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                hidprogress();
                                Toast.makeText(ActivityDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String feed) {

            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                JSONArray Idjsonarray = new JSONArray();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isnet()) {
                    new SaveAttachment().execute(Vendordata);
                }
            }

        }
    }

    class SaveAttachment extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();

            if (response != null) {
                if (response.equalsIgnoreCase("true")) {
                    Toast.makeText(ActivityDetailsActivity.this, "Attachment save successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    Toast.makeText(ActivityDetailsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(ActivityDetailsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0],ActivityDetailsActivity.this);
                response = res.toString();
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    private void addMoreImages() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.choose_attachment_option_dialog);
        dialog.setTitle(getResources().getString(R.string.app_name));
        TextView camera = (TextView) dialog.findViewById(R.id.camera);
        TextView gallery = (TextView) dialog.findViewById(R.id.gallery);
        TextView textViewCancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView document=dialog.findViewById(R.id.document);
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

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityDetailsActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void startCameraIntent() {

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "attachment.jpg");
        outPutfileUri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outPutfileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);

    }
    private String getRealPathFromURI(Uri outPutfileUri) {
        Cursor cur = getContentResolver().query(outPutfileUri, null, null, null, null);
        cur.moveToFirst();
        int idx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cur.getString(idx);

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
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString();
            File file = new File(path1 + "/" + "ActivityAttachment"+"/"+"Sender");
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
            attachment = file1.getAbsolutePath();
            File f = new File(attachment);
            //Attachment=f.getName();

            Toast.makeText(ActivityDetailsActivity.this,"Image send successfully",Toast.LENGTH_SHORT).show();
            CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",ActivityId);

			/*if (isnet()) {
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
			}*/


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_CAPTURE_IMG && resultCode == this.RESULT_OK) {
                String uri = outPutfileUri.toString();
                Log.e("uri-:", uri);
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                 //   FileOutputStream out = new FileOutputStream(file);

                 //   bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    outPutfileUri = Uri.parse(url);
                    if (outPutfileUri.toString().contains("content")) {
                        handleSendImage(outPutfileUri);
                    }else {
                        File file = new File(getRealPathFromUri(ActivityDetailsActivity.this,outPutfileUri));//create path from uri
                        attachment = file.getName();
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


            } else if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (data.getData() != null) {
                    outPutfileUri = data.getData();
                    // Get the cursor
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
                    //	uploadFileBitMap = bitmap;
                    file = new File(getRealPathFromURI(outPutfileUri));
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);
                    outPutfileUri = Uri.parse(url);
                    if (outPutfileUri.toString().contains("content")) {
                        handleSendImage(outPutfileUri);
                    }else {
                        File file = new File(getRealPathFromUri(ActivityDetailsActivity.this,outPutfileUri));//create path from uri
                        attachment = file.getName();
                        CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",ActivityId);

						/*if (isnet()) {
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
						}*/

                    }



                    //img_userpic.setImageURI(fileUri);
                    //callChangeProfileImageApi(file.getAbsoluteFile().toString());


                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            }else if (requestCode == RESULT_DOCUMENT && null != data) {

                Uri selectedFileURI = data.getData();
                File file = new File(getRealPathFromUri(ActivityDetailsActivity.this,selectedFileURI));//create path from uri
                Log.d("", "File : " + file.getName());
                String uploadedFileName = file.toString();
                Toast.makeText(ActivityDetailsActivity.this,"Document send successfully",Toast.LENGTH_SHORT).show();
                CreateOfflineSaveAttachment(uploadedFileName,uploadedFileName,3,"Document send successfully",ActivityId);



            }
            else {
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
    private void CreateOfflineSaveAttachment(String imageUri, String name, int attachmentFlAG, String remark, String activityId) {

        long a = cf.addofflinedata(imageUri, name, attachmentFlAG, remark, activityId);

        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Attachment Saved", Toast.LENGTH_LONG).show();

            getRowFromDatabase();

            //setJobShedulder();


        } else {
            Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

        }
    }

    private void getRowFromDatabase() {
        Object obj = null;
        int methodtype = 0;
        String RecordID = "";
        String Filename = "";

        String Url = null, remark, date, is, output = null, filepath = "", filename = "";
         sql  = db.getWritableDatabase();
       /* Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?",
                new String[]{WebUrlClass.FlagisUploadedFalse});*/

        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=? AND AttemptCount<=3",
                new String[]{WebUrlClass.FlagisUploadedFalse});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            System.out.println("======= c= 0  fetchall ");
           // stopSelf();
        } else {
            cursor.moveToFirst();
            do {
                try {

                    RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                    Url = cursor.getString(cursor.getColumnIndex("linkurl"));// for attachment it is path

                    methodtype = cursor.getInt(cursor.getColumnIndex("methodtype"));
                    if (methodtype == WebUrlClass.ATTACHMENTFlAG) {
                        Filename = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                    } else {
                        obj = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                    }

                    filepath = cursor.getString(cursor.getColumnIndex("AttachmentPath"));
                    filename = cursor.getString(cursor.getColumnIndex("AttachmentFileName"));
                    output = cursor.getString(cursor.getColumnIndex("output"));//for attachment it is ActivityID
                    is = cursor.getString(cursor.getColumnIndex("isUploaded"));
                    remark = cursor.getString(cursor.getColumnIndex("remark"));
                    date = cursor.getString(cursor.getColumnIndex("AddedDt"));


                    if(is.equals(WebUrlClass.FlagisUploadedFalse)) {
                        if (methodtype == 3) {
                            if (isInternetAvailable(getApplicationContext())) {
                                final String finalUrl = Url;
                                final String finalFilename1 = Filename;
                                final String finalOutput = output;
                                final String finalRecordID2 = RecordID;
                                //startSesstionApi(finalUrl, finalFilename1, finalOutput, finalRecordID2);
                                //callRerofitApi(finalUrl, finalFilename1, finalOutput, finalRecordID2);
                                new StartSession(context, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        Log.i("imageNameCall:", finalFilename1);
                                        Cursor c = sql.rawQuery(
                                                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE linkurl=?",
                                                new String[]{finalUrl});
                                        int a = c.getCount();
                                        String localVar = null;
                                        if ((c.getCount() > 0)) {
                                            c.moveToFirst();
                                            localVar = c.getString(c.getColumnIndex("isUploaded"));
                                        }
                                        final String finalLocalVar = localVar;

                                        if (finalLocalVar != null && (!finalLocalVar.equals("send")) && (!finalLocalVar.equals(WebUrlClass.FlagisUploadedTrue)))
                                            new PostUploadImageMethodProspectNew().execute(finalUrl, finalFilename1, finalOutput, finalRecordID2);

                                    }@Override
                                    public void callfailMethod(String msg) {
                                        ut.displayToast(context, msg);
                                    }
                                });

                                //callRerofitApi(Url, Filename, output, RecordID);
                            }
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
            /**/
        }
    }

    public class PostUploadImageMethodProspectNew extends AsyncTask<String, Void, ArrayList<String>> {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonimage = new JSONObject();
        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected ArrayList<String> doInBackground(String... urls) {

            try {

                Log.i("imageNameTry:", urls[0]);
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "send");
                SQLiteDatabase sql = db.getWritableDatabase();
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "linkurl=?",
                        new String[]{ urls[0]});

                File sourceFile = FileUtils.getFile(context, Uri.fromFile(new File(urls[0])));
                String ActivityID = urls[2];//AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + EnvMasterId +"&ActivityId="+ ActivityID;
                // FileInputStream fileInputStream = new FileInputStream(sourceFile);
                Object res = null;
                String response = null;
                response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri , sourceFile));
                if (response!= null && (!response.equals(""))) {
                    try {
                        Log.i("imageNameDone:", urls[0]);
                        jsonimage.put("File", urls[0]);
                        jsonArray.put(jsonimage);
                        Log.i("imageNameError:", urls[0]);
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                                new String[]{ urls[0]});

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.i("imageNameError:", urls[0]);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                    SQLiteDatabase sql1 = db.getWritableDatabase();
                    sql1.update(db.TABLE_DATA_OFFLINE, contentValues1, "linkurl=?",
                            new String[]{ urls[0]});
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(urls[0]);
            Data.add(urls[1]);
            Data.add(urls[2]);
            Data.add(urls[3]);

            return Data;

        }

        protected void onPostExecute(ArrayList<String> feed) {
            String path = feed.get(0);
            String Imagefilename = feed.get(1);
            String ActivityId = feed.get(2);
            String recid = feed.get(3);

            String Vendordata = "";
            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isInternetAvailable(getApplicationContext())) {

                    checkAllImageUpload(path.substring(path.lastIndexOf("/")).replace("/", ""), ActivityId);


                } else {
                    //  Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }

        }
    }

    private void checkAllImageUpload(String imagefilename, String activityId) {

        ContentValues contentValues = new ContentValues();
       /* contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.update(db.TABLE_DATA_OFFLINE, contentValues, "parameter=?",
                new String[]{imagefilename});*/

        ListObjectForEno listObjectForEno = new Gson().fromJson(AppCommon.getInstance(context).getFilnalListSampl(), ListObjectForEno.class);
        if (listObjectForEno != null) {
            ArrayList<FinalObjectForENO> finalObjectForENOList = listObjectForEno.getFinalObjectForENOArrayList();
            if (finalObjectForENOList != null && finalObjectForENOList.size() != 0) {

       /* Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?",
                new String[]{WebUrlClass.FlagisUploadedFalse});*/

                Cursor cursor = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where output='" + activityId + "'", null);
                int a = cursor.getCount();
                if (cursor.getCount() == 0) {
                    System.out.println("======= c= 0  fetchall ");
                    //stopSelf();
                } else {
                    cursor.moveToFirst();
                    boolean flag;
                    do {
                        try {

                            String is = cursor.getString(cursor.getColumnIndex("isUploaded"));
                            if (!is.equals(WebUrlClass.FlagisUploadedTrue)) {
                                flag = false;
                                break;
                            } else {
                                flag = true;
                            }

                        } catch (Exception e) {
                            flag = false;
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                    // call all Image Done
                    if (flag) {
                        Log.i("image ::", "allDone");
                        new SendEnoSampleObjectPosNew().execute(activityId);
                    }

                }
            } else {
                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where output='" + activityId + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "output=?",
                        new String[]{activityId});
                //SendNotification(integer);

                getRowFromDatabase();
            }
        }
    }



    private class SendEnoSampleObjectPosNew extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        String activityId;

        @Override
        protected String doInBackground(String... strings) {
            String activityId = strings[0];
            String url = null;
            url = CompanyURL + WebUrlClass.api_postSurvayData;
            ListObjectForEno listObjectForEno = new Gson().fromJson(AppCommon.getInstance(context).getFilnalListSampl(), ListObjectForEno.class);
            this.activityId = activityId;
            if (listObjectForEno != null) {
                ArrayList<FinalObjectForENO> finalObjectForENOArrayList = listObjectForEno.getFinalObjectForENOArrayList();
                if (finalObjectForENOArrayList.size() != 0) {
                    for (FinalObjectForENO finalObjectForENO : finalObjectForENOArrayList) {
                        if (finalObjectForENO.getActivityObject().getActivityId().equals(activityId)) {
                            // find activity id

                            try {
                                res = Utility.OpenPostConnection(url, new Gson().toJson(finalObjectForENO), context);
                                response = res.toString();
                                response = response.replaceAll("\\\\", "");
                                response = response.substring(1, response.length() - 1);
                                return activityId;
                            } catch (Exception e) {
                                e.printStackTrace();
                                response = WebUrlClass.setError;
                            }

                        } else {
                            // not find activity

                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    // remove to
                    Sample_List_Object sample_list_object = new Gson().fromJson(AppCommon.getInstance(context).getEnoSampelList(), Sample_List_Object.class);
                    ArrayList<SamplePojoClass> samplePojoClassArrayList = sample_list_object.getSamplePojoClassArrayList();
                    ArrayList<SamplePojoClass> tempSampleClassArray = new ArrayList<>();

                    for (int i = 0; i < samplePojoClassArrayList.size(); i++) {
                        samplePojoClassArrayList.remove(i);
                    }
                    if (samplePojoClassArrayList.size() != 0) {
                        String listObj = new Gson().toJson(new Sample_List_Object(samplePojoClassArrayList));
                        AppCommon.getInstance(context).setEnoSampelList(listObj);
                    } else {
                        AppCommon.getInstance(context).setEnoSampelList(null);
                    }
                    if(firebaseJobDispatcher != null){
                        SendNotificatioMsg();
                        // firebaseJobDispatcher.cancelAll();
                        // EnoSampleSubmitClass.myJobNew = null;
                    }
               /* samplePojoClassArrayList.clear();
                samplePojoClassArrayList = tempSampleClassArray;*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void SendNotificatioMsg() {

            String data = "Your Images is uploaded";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
            // Intent intent = new Intent(Intent.ACTION_VIEW, Settings.ACTION_LOCATION_SOURCE_SETTINGS));
       /* Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);*/
            //builder.setContentIntent(pendingIntent);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
            builder.setContentTitle("Action alert");
            builder.setContentText(data);
            builder.setSound(defaultSoundUri);

            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data));
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Will display the notification in the notification bar
            Random random = new Random();
            int code = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(code, builder.build());
        }



    }


    private void setJobShedulder() {
        if (myJobNew == null) {
            dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityDetailsActivity.this));
            callJobDispacher();
        } else {
            if (!AppCommon.getInstance(this).isServiceIsStart()) {
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityDetailsActivity.this));
                callJobDispacher();
            } else {
                dispatcherNew.cancelAll();
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(ActivityDetailsActivity.this));
                myJobNew = null;
                callJobDispacher();
            }
        }
    }

    private void callJobDispacher() {
        myJobNew = dispatcherNew.newJobBuilder()
                // the JobService that will be called
                .setService(EnoJobService.class)
                // uniquely identifies the job
                .setTag("Eno")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)

                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 180))
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

        dispatcherNew.mustSchedule(myJobNew);
        AppCommon.getInstance(this).setServiceStarted(true);
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


}
