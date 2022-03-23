package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.activity.AddGroupActivity;
import com.vritti.chat.activity.ChatRoomMultipleAdapter;
import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.crm.adapter.AttachmentDetailsAdapter;
import com.vritti.crm.adapter.CallHistoryAdapter;
import com.vritti.crm.bean.OrderType;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.bean.ProductBean;
import com.vritti.crm.bean.ReasonBean;
import com.vritti.crm.bean.ShowContact;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.EnoJobService;
import com.vritti.expensemanagement.AddExpenseActivity_Next;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.Sahara_AttachmentDetailsAdapter;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;
import static com.vritti.expensemanagement.AddExpenseActivity.getRealPathFromUri;
import static com.vritti.vwb.vworkbench.NotificationActivity.progress_bar_type;

public class CRM_Callslist_Partial extends AppCompatActivity {

    //private final ArrayList<PartialCallList> partialCallListArrayList;
    TextView tcontact, laycall_type, tconvers, tdetails, thistory, tattachment,txt_prospect,call_rating,milestone;

    int startPos = 0;


    TextView firmname,actiondatetime,tv_latestremark,tvcall,txt_expvalue;
    ImageView callrating,img_nextaction;
    ImageView img_add,img_refresh,img_back,img_appotunity_update,image_add;
    TextView txt_title;

    String Firmname="",CallId="",Datetime="",NextAction="",Status="",Call="",Remark="",Prospect="",call_type="",EstimateValue="";


    String  SourceId="",Milestone="",Mobile="";

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",IsChatApplicable="";

    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;

    // Call Log

    String CallLogType="",Start="",EndTime="",Duration="",RowNo="";

    // Contact
    LinearLayout linear_contact;
    ArrayList<ShowContact> showContactArrayList = new ArrayList<>();
    LinearLayout len_show_calldetails, len_action, len_contact_edit, len_contact;
    private int position1, pos;
    TextView spinner_department;
    AppCompatCheckBox checkbox_primary_contact;
    Button txt_save, txt_reset;
    String Call_Contactname, Call_Designation, Call_Department, Call_Mobile, Call_Email, Call_Office, Addcalljson, Call_Callid, Call_CallType, Call_ProspectId, Deletecalljson, Contact_id, Call_primary, Editcalljson;
    boolean Check_Value;
    String show_contactname, show_designation, show_department, show_email, show_mobile, show_primary_contact;
    View showcontactconvertView;
    TextView txt_show_contactname, txt_show_contact_designation, txt_show_contact_department, txt_show_contact_mobileno, txt_show_contact_email, txt_show_contact_primary;
    Spinner txt_action;
    EditText edit_contactname, edit_designation, edit_mobile, edit_email;
    ImageView edit, delete,add;
    Spinner spinner_edit_primary, spinner_edit_department;
    String ActionPerformitem,  UserType;
    ArrayAdapter<String> ActionPerform;
    int position;
    SharedPreferences userpreferences;
    LinearLayout len;


    //Call Status

    LinearLayout linear_sttatus;
    ImageView image_save;
    ArrayList<ReasonBean> reasonBeanArrayList;
    LinearLayout layreason;
    Button hot, warm, cold;
    List reason;
    AutoCompleteTextView spinner_reason;
    public  String PKReasonID="", callid="", jsonparams="", callstatus="", CurrentDate="";
    public  String callStatus="";
    Button buttonSave, buttonClose;
    public  String ReasonDescription="";
    ProgressHUD progressHUD;
    SimpleDateFormat dfDate;
    public static EditText  Expectedvalue;
    public static TextView editTextExpecteddate,txt3date;
    static int year, month, day;
    public static String date = null;
    public static String today, todaysDate;
    TextInputLayout layout_reason;
    String sevenDay="";
    String  dayValidate;
    Date localTime;


    // Chat Development
    LinearLayout linear_chat;
    ListView listview_multiple_group;
    ChatRoomMultipleAdapter chatRoomMultipleAdapter;
    ArrayList<ChatGroup> chatRoomDisplayArrayList=new ArrayList<>();
    EditText edt_search_name;
    TextView txt_chatroom_add;
    LinearLayout len_add_chatroom;
    private String ChatRoomId="";
    ImageView img_create;

    // Call History

    RecyclerView callhistory_listview;
    ArrayList<CallHistory> callHistoryArrayList=new ArrayList<>();
    CallHistoryAdapter callHistoryAdapter;
    ProgressBar dialog_loading;
    private ProgressDialog progressDialog;


    // Create Opportunity

    AutoCompleteTextView spinner_toWhom, spinner_Assigntocall, spinner_Ordertype, spinner_campaign;
    private boolean isOrderType=false;
    private boolean isCampaigh=false;
    EditText editTextNotes;
    ImageView btnAddProd;
    private String ordertypeid = "", campaignId = "";
    private String ReviewDate="";
    List<String> lstCampaignString = new ArrayList<String>();
    List<OrderType> lstCampaign = new ArrayList<>();
    List<OrderType> lstOrdertype = new ArrayList<>();
    List<String> lstOrderTypeString = new ArrayList<String>();
    String boeid = "", CurrentCallOwner = "", ProductId = "", OrderTypeMasterId = "";
    int seid;
    private String notes="";
    RelativeLayout rel_opportunity;
    LinearLayout linear_list_product;
    List<ProductBean> productBeanList = new ArrayList<ProductBean>();
    private ProductBean productBean;


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
    private String  attachment="";
    public static FirebaseJobDispatcher dispatcherNew;
    public static Job myJobNew = null;
    File file;
    private static int RESULT_LOAD_IMG = 2;

    private static final int RESULT_CAPTURE_IMG = 3;
    private static final int RESULT_DOCUMENT = 4;
    private Uri outPutfileUri;
    private int APP_REQUEST_CODE = 4478;
    String Contact="";
    TextView txt_notfound;
    Date review_date,current_date2;
    String Response_Call="",Call_ID="";
    private boolean Flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_calllist_partial);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        context=CRM_Callslist_Partial.this;
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

        //UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();

        tcontact = (TextView)findViewById(R.id.contact);
        txt_notfound = (TextView)findViewById(R.id.txt_notfound);
        laycall_type = (TextView)findViewById(R.id.laycall_type);

        tconvers = (TextView)findViewById(R.id.convers);
        tdetails = (TextView)findViewById(R.id.details);
        thistory = (TextView)findViewById(R.id.history);
        tattachment = (TextView)findViewById(R.id.attachment);
        dialog_loading=findViewById(R.id.dialog_loading);



        firmname = (TextView)findViewById(R.id.firmname);
        actiondatetime = (TextView)findViewById(R.id.actiondatetime);
        tv_latestremark = (TextView)findViewById(R.id.tv_latestremark);
        tvcall = (TextView)findViewById(R.id.tvcall);
        callrating = (ImageView) findViewById(R.id.callrating);
        img_nextaction = (ImageView) findViewById(R.id.img_nextaction);
        img_appotunity_update=findViewById(R.id.img_appotunity_update);
        txt_prospect=findViewById(R.id.txt_prospect);
        call_rating=findViewById(R.id.call_rating);
        milestone=findViewById(R.id.milestone);
        txt_expvalue=findViewById(R.id.txt_expvalue);
        layout_reason=findViewById(R.id.layout_reason);


        // Contact
        linear_contact = (LinearLayout) findViewById(R.id.linear_contact);
        len_show_calldetails=findViewById(R.id.len_show_calldetails);

        //Call Rating

        linear_sttatus=findViewById(R.id.linear_sttatus);
        image_save=findViewById(R.id.image_save);
        editTextExpecteddate = (TextView) findViewById(R.id.editTextExpecteddate);
        txt3date = (TextView) findViewById(R.id.txt3date);
        Expectedvalue = (EditText) findViewById(R.id.Expectedvalue);
        spinner_reason = (AutoCompleteTextView) findViewById(R.id.spinner_reason);
        spinner_reason.setSelection(0);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonClose = (Button) findViewById(R.id.buttonClose);
        layreason = (LinearLayout) findViewById(R.id.layreason);
        final TextInputLayout txt_value=findViewById(R.id.txt_value);

        hot = (Button)findViewById(R.id.btnhot);
        warm = (Button)findViewById(R.id.btnwarm);
        cold = (Button)findViewById(R.id.btncold);

        //Chat Development

        listview_multiple_group=findViewById(R.id.listview_multiple_group);
        linear_chat=findViewById(R.id.linear_chat);
        img_create=findViewById(R.id.img_create);


        //Call History

        callhistory_listview = findViewById(R.id.callhistory_listview);

        // Create Opportunity

        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        spinner_Ordertype = findViewById(R.id.spinner_Ordertype);
        spinner_campaign = findViewById(R.id.spinner_campaign);
        btnAddProd = (ImageView) findViewById(R.id.btnAddProd);
        rel_opportunity=findViewById(R.id.rel_opportunity);
        linear_list_product = (LinearLayout) findViewById(R.id.linear_list_product);

        //Attachment

        ls_attachname=findViewById(R.id.ls_attachname);
        linear_attachment=findViewById(R.id.linear_attachment);
        image_att_plus=findViewById(R.id.image_att_plus);




        progressDialog = new ProgressDialog(CRM_Callslist_Partial.this);


        image_att_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoreImages();

            }
        });






        hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReasonDescription="";
                PKReasonID="";
                layout_reason.setVisibility(View.GONE);

                callStatus=hot.getText().toString();
                double d=Double.parseDouble(EstimateValue);
                Expectedvalue.setText(String.format("%.2f", d));
                editTextExpecteddate.setVisibility(View.VISIBLE);
                txt3date.setVisibility(View.VISIBLE);
                Calendar aCalendar = Calendar.getInstance();
                Date firstDateOfCurrentMonth = aCalendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                CurrentDate = sdf.format(firstDateOfCurrentMonth);
                editTextExpecteddate.setText(CurrentDate);
                sevenDay = timeStampConversion(CurrentDate,callStatus);

                try {
                    localTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sevenDay);
                    System.out.println("TimeStamp is " + localTime.getTime());

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                hot.setBackground(getResources().getDrawable(R.drawable.button_hot));
                warm.setBackground(getResources().getDrawable(R.drawable.button_grey));
                cold.setBackground(getResources().getDrawable(R.drawable.button_grey));
            }
        });

        warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_reason.setVisibility(View.VISIBLE);
                callStatus=warm.getText().toString();
               // sevenDay = timeStampConversion(CurrentDate,callStatus);
                double d=Double.parseDouble(EstimateValue);
                Expectedvalue.setText(String.format("%.2f", d));
                editTextExpecteddate.setText("");
                editTextExpecteddate.setVisibility(View.GONE);
                txt3date.setVisibility(View.GONE);
                /*try {
                    localTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sevenDay);
                    System.out.println("TimeStamp is " + localTime.getTime());

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
               */
                hot.setBackground(getResources().getDrawable(R.drawable.button_grey));
                warm.setBackground(getResources().getDrawable(R.drawable.button_warm));
                cold.setBackground(getResources().getDrawable(R.drawable.button_grey));
            }
        });

        cold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callStatus=cold.getText().toString();
                layout_reason.setVisibility(View.VISIBLE);
                editTextExpecteddate.setVisibility(View.GONE);
                txt_value.setVisibility(View.GONE);
                Expectedvalue.setText("");
                txt3date.setVisibility(View.GONE);
                editTextExpecteddate.setText("");
                hot.setBackground(getResources().getDrawable(R.drawable.button_grey));
                warm.setBackground(getResources().getDrawable(R.drawable.button_grey));
                cold.setBackground(getResources().getDrawable(R.drawable.button_cold));
            }
        });

        Firmname=getIntent().getStringExtra("firm");
        firmname.setText(Firmname);
        actiondatetime.setText(getIntent().getStringExtra("date"));
        tv_latestremark.setText(getIntent().getStringExtra("remark"));
        tvcall.setText(getIntent().getStringExtra("call"));

        call_type=getIntent().getStringExtra("call_type");
        Status=getIntent().getStringExtra("status");
        CallId=getIntent().getStringExtra("callid");
        Prospect=getIntent().getStringExtra("call_prospect");
        Call=getIntent().getStringExtra("call_type_1");
        SourceId=getIntent().getStringExtra("SourceId");
        Milestone=getIntent().getStringExtra("mile");
        milestone.setText(Milestone);
        Mobile=getIntent().getStringExtra("mobile");
        EstimateValue=getIntent().getStringExtra("evalue");
        if (getIntent().hasExtra("callmob")){
            Contact= getIntent().getStringExtra("callmob");
        }


        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetCallReview().execute(CallId);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


        if (getIntent().hasExtra("type")){
            CallLogType=getIntent().getStringExtra("Callfromcalllogs");
            Start=getIntent().getStringExtra("starttime");
            EndTime=getIntent().getStringExtra("endtime");
            Duration=getIntent().getStringExtra("duration");
            RowNo=getIntent().getStringExtra("rowNo");

        }

        if (Status.equalsIgnoreCase("Warm")) {
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.add(Calendar.DATE, 30);
            Date firstDateOfCurrentMonth = aCalendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            CurrentDate = sdf.format(firstDateOfCurrentMonth);
            editTextExpecteddate.setText(CurrentDate);
        }else {
            if (Status.equalsIgnoreCase("Hot")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.add(Calendar.DATE, 15);
                Date firstDateOfCurrentMonth = aCalendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                CurrentDate = sdf.format(firstDateOfCurrentMonth);
                editTextExpecteddate.setText(CurrentDate);
            }
        }


         if (EstimateValue.equals("0.00")||EstimateValue.equals("0.0")||EstimateValue.equals("0")){

        }else {
            txt_expvalue.setVisibility(View.VISIBLE);
              double d=Double.parseDouble(EstimateValue);
             txt_expvalue.setText("EV-"+String.format("%.2f", d));
        }


        if (call_type.equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (Status.equalsIgnoreCase("Cold")) {
                //callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
              //  ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
                callrating.setImageResource(R.drawable.ic_cube);
            } else if (Status.equalsIgnoreCase("Hot")) {
               // callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
                //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.hot)));
                callrating.setImageResource(R.drawable.img_hot_call);
            } else if (Status.equalsIgnoreCase("Warm")) {
                //callrating.setImageDrawable(CRM_Callslist_Partial.this.getResources().getDrawable(R.drawable.square));
               // ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(CRM_Callslist_Partial.this.getResources().getColor(R.color.warm)));
                callrating.setImageResource(R.drawable.img_warm_call);
            }
        } else if (call_type.equalsIgnoreCase("2")) {
            callrating.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (call_type.equalsIgnoreCase("3")) {
            callrating.setBackgroundColor(Color.parseColor("#FF1493"));
        }



        NextAction=getIntent().getStringExtra("action");
        if (NextAction.equalsIgnoreCase("Email")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.email_24));
            img_nextaction.setColorFilter(ContextCompat.getColor(CRM_Callslist_Partial.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else if (NextAction.equalsIgnoreCase("Telephone")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
           img_nextaction.setColorFilter(ContextCompat.getColor(CRM_Callslist_Partial.this, R.color.colorPrimary
            ), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else {
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }

            tvcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Mobile.equals("")){

                    }else {
                        tvCallMethd(Mobile);
                    }
                }
            });

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        image_add=findViewById(R.id.image_add);

        txt_title.setText("Opportunity Update");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Contact
        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Callslist_Partial.this, ContactActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_prospect", Prospect);
                intent.putExtra("call_type", call_type);
                intent.putExtra("Mode", "A");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        tcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.VISIBLE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                linear_sttatus.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                tcontact.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));


                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
            new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetAddShowcallDetailsJSON().execute(Prospect);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


               /* Intent intent = new Intent(CRM_Callslist_Partial.this, ContactShowActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_prospect", Prospect);
                intent.putExtra("call_type", call_type);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                tcontact.setBackground(getResources().getDrawable(R.drawable.scrollbuttonbackground));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
*/

            }
        });

        //  Call Logs Direct

        if (getIntent().hasExtra("type")||getIntent().hasExtra("callmob")){
            Intent intent = new Intent(CRM_Callslist_Partial.this, OpportunityUpdateActivity_New.class);
            intent.putExtra("callid", CallId);
            intent.putExtra("firmname", firmname.getText().toString());
            intent.putExtra("firm", firmname.getText().toString());
            intent.putExtra("calltype", call_type);
            intent.putExtra("table", "Call");
            intent.putExtra("date", getIntent().getStringExtra("date"));
            intent.putExtra("remark", getIntent().getStringExtra("remark"));
            intent.putExtra("call", getIntent().getStringExtra("call"));
            intent.putExtra("status", Status);
            intent.putExtra("call_type_1", Call);
            intent.putExtra("SourceId", SourceId);
            intent.putExtra("mile", Milestone);
            intent.putExtra("mobile", Mobile);
            intent.putExtra("evalue", EstimateValue);
            intent.putExtra("call_type",call_type);
            intent.putExtra("action",NextAction);
            intent.putExtra("ProspectId",Prospect);
            if (getIntent().hasExtra("type")){
                intent.putExtra("type", "Callfromcalllogs");
                intent.putExtra("starttime", Start);
                intent.putExtra("endtime", EndTime);
                intent.putExtra("duration", Duration);
                intent.putExtra("rowNo", RowNo);
            }
            if (getIntent().hasExtra("callmob")){
                intent.putExtra("callmob", Contact);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
        }




        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                laycall_type.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));

                String reviewDate="";

                if (Response_Call.contains("-")){
                     reviewDate = formateDateFromstring("yyyy-MM-dd hh:mm:ss a", "yyyy-MM-dd", Response_Call);
                }else {
                      reviewDate = formateDateFromstring("MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd", Response_Call);
                }

                long date1 = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = sdf.format(date1);


                try {
                    review_date = sdf.parse(reviewDate);
                    Log.d("Response-5", review_date.toString());

                    current_date2 = sdf.parse(dateString);

                    Log.d("Response-6", current_date2.toString());

                           /* if (review_date.after(current_date2)) {
                                txtView.setText("true");
                            } else {
                                txtView.setText("false");
                            }*/
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                  try {


                    if (review_date.before(current_date2)) {
                              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                              Toast toast = Toast.makeText(context, "Cannot Update Call - It is Under Review State", Toast.LENGTH_SHORT);
                              View toastView = toast.getView();
                              TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                              toastMessage.setTextSize(18);
                              toastMessage.setTextColor(Color.RED);
                              toastMessage.setGravity(Gravity.CENTER);
                              toastView.setBackgroundColor(Color.WHITE);
                              toast.show();
                          } else {
                              Toast toast = Toast.makeText(context, Html.fromHtml("<font color='#EF4F4F'><b><big>Cannot Update Call - It is Under Review State</big></b></font>"), Toast.LENGTH_SHORT);
                              toast.setGravity(Gravity.CENTER, 0, 0);
                              toast.show();
                          }

                    }
                    else {

                        Intent intent = new Intent(CRM_Callslist_Partial.this, OpportunityUpdateActivity_New.class);
                        intent.putExtra("callid", CallId);
                        intent.putExtra("firmname", firmname.getText().toString());
                        intent.putExtra("firm", firmname.getText().toString());
                        intent.putExtra("calltype", call_type);
                        intent.putExtra("table", "Call");
                        intent.putExtra("date", getIntent().getStringExtra("date"));
                        intent.putExtra("remark", getIntent().getStringExtra("remark"));
                        intent.putExtra("call", getIntent().getStringExtra("call"));
                        intent.putExtra("status", Status);
                        intent.putExtra("call_type_1", Call);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("mile", Milestone);
                        intent.putExtra("mobile", Mobile);
                        intent.putExtra("evalue", EstimateValue);
                        intent.putExtra("call_type", call_type);
                        intent.putExtra("action", NextAction);
                        intent.putExtra("ProspectId", Prospect);
                        if (getIntent().hasExtra("type")) {
                            intent.putExtra("type", "Callfromcalllogs");
                            intent.putExtra("starttime", Start);
                            intent.putExtra("endtime", EndTime);
                            intent.putExtra("duration", Duration);
                            intent.putExtra("rowNo", RowNo);
                        }
                        if (getIntent().hasExtra("callmob")) {
                            intent.putExtra("callmob", Contact);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

                }
                  }catch (Exception e){
                      e.printStackTrace();
                  }
            }
        });



        tconvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.VISIBLE);
                linear_sttatus.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                tconvers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));


                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadChatRoomDisplayDataJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                /*Intent intent = new Intent(CRM_Callslist_Partial.this, MultipleGroupActivity.class);

                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    intent.putExtra("callid", CallId);
                    intent.putExtra("call_type", "Crm_Opportunity");
                    intent.putExtra("firm", firmname.getText().toString());
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(CRM_Callslist_Partial.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }*/

               /* tconvers.setBackground(getResources().getDrawable(R.drawable.scrollbuttonbackground));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));*/
                /*intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/

            }
        });

        tdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.VISIBLE);
                linear_attachment.setVisibility(View.GONE);
                tdetails.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadProductDetailJSON().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

                /*Intent intent;
                intent = new Intent(CRM_Callslist_Partial.this, CreateOpportunityActivity.class);
                intent.putExtra("SuspectID", Prospect);
                intent.putExtra("CallId",CallId);*/
                /*tdetails.setBackground(getResources().getDrawable(R.drawable.scrollbuttonbackground));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));*/
               /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/


                // API Call

                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new DownloadOpportunityDetails().execute();


                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                if (cf.getOrderTypecount() > 0) {
                    displayOrderType();
                } else {
                    if (isnet()) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new DownloadOrderTypeJSON().execute();

                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }

//campaign
                if (cf.getCampaigncount() > 0) {
                    displayCampaign();
                } else {
                    if (isnet()) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new DownloadCampaignJSON().execute();

                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
            }
        });

        thistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                if (ut.isNet(CRM_Callslist_Partial.this)) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCallHistoryData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

                thistory.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));


            /*    Intent i = new Intent(CRM_Callslist_Partial.this, CallHistoryActivity.class);
                i.putExtra("callid", CallId);
                i.putExtra("firmname", firmname.getText().toString());
                i.putExtra("calltype", call_type);
                i.putExtra("ProspectId", Prospect);
                i.putExtra("SourceId", SourceId);
                i.putExtra("table", "Call");
                i.putExtra("type", "Callfromcalllogs");
                i.putExtra("MobileCalltype", call_type);
                i.putExtra("starttime", "");
                i.putExtra("endtime", "");
                i.putExtra("duration", "");
                i.putExtra("rowNo", "");
                startActivity(i);
                *//*thistory.setBackground(getResources().getDrawable(R.drawable.scrollbuttonbackground));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));*//*
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/

            }
        });

        tattachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_sttatus.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.VISIBLE);


                 if (ut.isNet(context)) {
                     dialog_loading.setVisibility(View.VISIBLE);
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new downloadAttachmentDetails().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                });
            }else {
                     Toast.makeText(getApplicationContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                 }

                tattachment.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));



               /* Intent intent;
                intent = new Intent(CRM_Callslist_Partial.this, CallRatingActivity.class);
                tattachment.setBackground(getResources().getDrawable(R.drawable.scrollbuttonbackground));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });
        txt_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));

            }
        });
        call_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_sttatus.setVisibility(View.VISIBLE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                call_rating.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));


                if (Status.equalsIgnoreCase("Hot")) {
                    layout_reason.setVisibility(View.GONE);
                    hot.setBackground(getResources().getDrawable(R.drawable.button_hot));
                    Calendar aCalendar = Calendar.getInstance();
                    aCalendar.add(Calendar.DATE, 15);
                    Date firstDateOfCurrentMonth = aCalendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    CurrentDate = sdf.format(firstDateOfCurrentMonth);
                    editTextExpecteddate.setText(CurrentDate);


                    Calendar aCalendar1 = Calendar.getInstance();
                    Date Current = aCalendar1.getTime();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    String Currentdate = sdf1.format(Current);

                    sevenDay = timeStampConversion(Currentdate,Status);
                    double d=Double.parseDouble(EstimateValue);
                    Expectedvalue.setText(String.format("%.2f", d));

                    try {
                        localTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sevenDay);
                        System.out.println("TimeStamp is " + localTime.getTime());

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("Seven_HOT",sevenDay);

                } else if (Status.equalsIgnoreCase("Warm")) {

                    layout_reason.setVisibility(View.GONE);
                    warm.setBackground(getResources().getDrawable(R.drawable.button_warm));
                    Calendar aCalendar = Calendar.getInstance();
                    Date firstDateOfCurrentMonth = aCalendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    CurrentDate = sdf.format(firstDateOfCurrentMonth);
                    sevenDay = timeStampConversion(CurrentDate,Status);
                    double d=Double.parseDouble(EstimateValue);
                    Expectedvalue.setText(String.format("%.2f", d));
                    editTextExpecteddate.setVisibility(View.GONE);
                    txt3date.setVisibility(View.GONE);
                    editTextExpecteddate.setText("");
                    try {
                        localTime = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(sevenDay);
                        System.out.println("TimeStamp is " + localTime.getTime());

                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    Log.d("Seven_WARM",sevenDay);
                } else if (Status.equalsIgnoreCase("Cold")) {
                    layout_reason.setVisibility(View.GONE);
                    editTextExpecteddate.setVisibility(View.GONE);
                    txt3date.setVisibility(View.GONE);
                    editTextExpecteddate.setText("");
                    cold.setBackground(getResources().getDrawable(R.drawable.button_cold));
                }

            }
        });



        editTextExpecteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_Callslist_Partial.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;


                                if (Status.equalsIgnoreCase("Cold")) {
                                    editTextExpecteddate.setText(date);
                                }else {
                                    sevenDay = timeStampConversion(date, Status);
                                }

                                //editTextExpecteddate.setText(sevenDay);

                                if (compare_date(date) == true) {
                                    editTextExpecteddate.setText(date);
                                } else {
                                    editTextExpecteddate.setText(date);
                                    Toast.makeText(CRM_Callslist_Partial.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
               // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                if (Status.equalsIgnoreCase("Cold")) {
                }else {
                    datePickerDialog.getDatePicker().setMaxDate(localTime.getTime());
                }
                datePickerDialog.show();


            }
        });

        spinner_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = CompanyURL + WebUrlClass.api_Get_Reason
                        + "?ReasonCode=RD";
                Intent intent = new Intent(CRM_Callslist_Partial.this, CountryListActivity.class);
                intent.putExtra("Table_Name", db.TABLE_REASON);
                intent.putExtra("Id", "PKReasonID");
                intent.putExtra("DispName", "ReasonDescription");
                intent.putExtra("WHClauseParameter","");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });

        /*spinner_reason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReasonDescription = spinner_reason.getText().toString();


                String que = "SELECT PKReasonID FROM " + db.TABLE_REASON + " WHERE ReasonDescription='" + ReasonDescription + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    PKReasonID = cur.getString(cur.getColumnIndex("PKReasonID"));
                }

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("PKReasonID", PKReasonID);
                editor.commit();

            }
        });
*/
        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callStatus.equalsIgnoreCase("Cold")) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CallId", CallId);
                        jsonObject.put("CallStatus", callStatus);
                        jsonObject.put("CallStatusChangeId", PKReasonID);
                        jsonObject.put("ExpectedValue", Expectedvalue.getText().toString());
                        jsonObject.put("ExpectedClosureDate", editTextExpecteddate.getText().toString());
                        jsonparams = jsonObject.toString();
                        jsonparams = jsonparams.replaceAll("\\\\", "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (isnet()) {
                        progressDialog.setCancelable(true);
                        if (!isFinishing()) {
                            progressDialog.show();
                        }
                        progressDialog.setContentView(R.layout.crm_progress_lay);
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostCallRating().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                } else {
                   // if (validate(CRM_Callslist_Partial.this) == true) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("CallId", CallId);
                            jsonObject.put("CallStatus", callStatus);
                            jsonObject.put("CallStatusChangeId", PKReasonID);
                            jsonObject.put("ExpectedValue", Expectedvalue.getText().toString());
                            jsonObject.put("ExpectedClosureDate", editTextExpecteddate.getText().toString());
                            jsonparams = jsonObject.toString();
                            jsonparams = jsonparams.replaceAll("\\\\", "");


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        if (isnet()) {
                            new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostCallRating().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }
                }

        });


        // Chat Development

        listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ChatRoomStatus=chatRoomDisplayArrayList.get(i).getStatus();
                if (ChatRoomStatus.equalsIgnoreCase("Closed")){
                    Toast.makeText(CRM_Callslist_Partial.this,"You can't send message because this chatroom has been closed",Toast.LENGTH_SHORT).show();
                }else {
                    ChatRoomId = chatRoomDisplayArrayList.get(i).getChatRoomId();
                    String ChatRoomName = chatRoomDisplayArrayList.get(i).getChatroom();
                    String Chat_status = chatRoomDisplayArrayList.get(i).getStatus();
                    Intent intent = new Intent(CRM_Callslist_Partial.this, AddChatRoomActivity.class);
                    intent.putExtra("callid", CallId);
                    intent.putExtra("call_type", call_type);
                    intent.putExtra("ChatRoomid", ChatRoomId);
                    intent.putExtra("Chatroomname", ChatRoomName);
                    intent.putExtra("firm", Firmname);
                    intent.putExtra("status", Chat_status);
                    intent.putExtra("projmasterId", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
                /* finish();*/




            }
        });


        img_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Callslist_Partial.this, AddGroupActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_type", "Crm_Opportunity");
                intent.putExtra("firm", Firmname);
                intent.putExtra("projmasterId", "");
                intent.putExtra("AssignBy",UserName);
                intent.putExtra("AssignById",UserMasterId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        // Create Opportunity

        spinner_Ordertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Callslist_Partial.this,
                        /*CityListActivity.class*/CountryListActivity.class);
                isOrderType = true;
                isCampaigh = false;


                String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
                intent.putExtra("Table_Name", db.TABLE_OrderType);
                intent.putExtra("Id", "OrderTypeMasterId");
                intent.putExtra("DispName", "Description");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("out", "order");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });


        spinner_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Callslist_Partial.this,
                        /*CityListActivity.class*/CountryListActivity.class);
                isOrderType = false;
                isCampaigh = true;

                String url = CompanyURL + WebUrlClass.api_Get_Compaign;
                intent.putExtra("Table_Name", db.TABLE_Campaign);
                intent.putExtra("Id", "PKCampaignId");
                intent.putExtra("DispName", "CampaignName");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                intent.putExtra("out", "cam");
                startActivityForResult(intent, COUNTRY);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CRM_Callslist_Partial.this,ProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });



                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                rel_opportunity.setVisibility(View.GONE);
                linear_attachment.setVisibility(View.GONE);
                if (ut.isNet(CRM_Callslist_Partial.this)) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCallHistoryData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

                thistory.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));


    }

    public void tvCallMethd(String mobile) {

            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mobile));
                startActivity(callIntent);

            } catch (SecurityException e) {

            }
        }

    public void tvEmailMethd(String email) {

        Intent iemail = new Intent(Intent.ACTION_SENDTO);
        iemail.setData(Uri.parse("mailto:"));
        iemail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        iemail.putExtra(Intent.EXTRA_SUBJECT, "");
        iemail.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(iemail, "Select email options"));
        } catch (ActivityNotFoundException activityNotFoundException) {
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

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


    class GetAddShowcallDetailsJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url="";
                if (call_type.equals("2")) {
                    url= CompanyURL + WebUrlClass.api_getCustContactDetails + "?ProspectId=" + URLEncoder.encode(params[0], "utf-8");
                }else {
                    url= CompanyURL + WebUrlClass.api_getSuspectContactDetails + "?ProspectId=" + URLEncoder.encode(params[0], "utf-8");

                }
                res = ut.OpenConnection(url, CRM_Callslist_Partial.this);
                response = res.toString();
                response = response.substring(1, response.length() - 1);

                response="{\"Activity\":\""+response+"\n" +"\"}";
                /*res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    System.out.println("Response Call:" + response.toString());
                }*/

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";


            }
            return response;
        }

        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (integer.equals("[]")) {

                Toast.makeText(CRM_Callslist_Partial.this, "Data not Available", Toast.LENGTH_SHORT).show();
                dialog_loading.setVisibility(View.GONE);
            } else {

                try {
                    JSONArray jResults = null;
                    JSONObject obj = new JSONObject(response);

                    String Msgcontent=obj.getString("Activity");
                    jResults = new JSONArray(Msgcontent);

                    ContentValues values = new ContentValues();
                    sql.delete(db.TABLE_SHOW_CONTACT, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SHOW_CONTACT, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_SHOW_CONTACT, null, values);

                        Log.e("log data", "" + a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog_loading.setVisibility(View.GONE);
                Updatgetshowcontactlist();


            }
        }

    }

    private void Updatgetshowcontactlist() {
        showContactArrayList.clear();

        String query = "SELECT PKSuspContactDtlsID,ContactName,Designation,Mobile," +
                "EmailId,IsPrimaryContact,FKSuspectId" +
                " FROM " + db.TABLE_SHOW_CONTACT + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ShowContact showContact = new ShowContact();

                showContact.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                showContact.setPKSuspContactDtlsID(cur.getString(cur.getColumnIndex("PKSuspContactDtlsID")));
                showContact.setDesignation(cur.getString(cur.getColumnIndex("Designation")));
                showContact.setEmailId(cur.getString(cur.getColumnIndex("EmailId")));
                showContact.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                showContact.setFKSuspectId(cur.getString(cur.getColumnIndex("FKSuspectId")));
                showContact.setIsPrimaryContact(cur.getString(cur.getColumnIndex("IsPrimaryContact")));


                showContactArrayList.add(showContact);

            } while (cur.moveToNext());
            len_show_calldetails.removeAllViews();
            if (showContactArrayList.size() > 0) {
                txt_notfound.setVisibility(View.GONE);
                len_show_calldetails.setVisibility(View.VISIBLE);
                for (int i = 0; i < showContactArrayList.size(); i++) {
                    addView_showcontact(i);


                }
            }else {
                txt_notfound.setVisibility(View.VISIBLE);
            }
        }else {
            txt_notfound.setVisibility(View.VISIBLE);

        }


    }

    private void addView_showcontact(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CRM_Callslist_Partial.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        position1 = i;


        showcontactconvertView = layoutInflater.inflate(R.layout.crm_lay_show_contact_v1, null);

        txt_show_contactname = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contactname);
        txt_show_contact_designation = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_designation);
        txt_show_contact_department = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_department);
        txt_show_contact_mobileno = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_mobileno);
        txt_show_contact_email = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_email);
        txt_show_contact_primary = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_primary);
        txt_action = (Spinner) showcontactconvertView.findViewById(R.id.txt_action);
        len_action = (LinearLayout) showcontactconvertView.findViewById(R.id.len_action);
        len_contact_edit = (LinearLayout) showcontactconvertView.findViewById(R.id.len_contact_edit);
        edit_contactname = (EditText) showcontactconvertView.findViewById(R.id.edit_contactname);
        edit_designation = (EditText) showcontactconvertView.findViewById(R.id.edit_designation);
        edit_email = (EditText) showcontactconvertView.findViewById(R.id.edit_email);
        edit_mobile = (EditText) showcontactconvertView.findViewById(R.id.edit_mobile);
        edit = (ImageView) showcontactconvertView.findViewById(R.id.edit);
        delete = (ImageView) showcontactconvertView.findViewById(R.id.delete);
        add = (ImageView) showcontactconvertView.findViewById(R.id.add);
        spinner_edit_department = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_department);
        spinner_edit_primary = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_primary);
        len = (LinearLayout) showcontactconvertView.findViewById(R.id.len);
        TextView txt_flag=showcontactconvertView.findViewById(R.id.txt_flag);
        edit_mobile = (EditText) showcontactconvertView.findViewById(R.id.edit_mobile);
        show_contactname = showContactArrayList.get(position1).getContactName();
        if (!show_contactname.equals("")) {
            len.setVisibility(View.VISIBLE);
            txt_show_contactname.setText(show_contactname);
            show_designation = showContactArrayList.get(position1).getDesignation();
            txt_show_contact_designation.setText(show_designation);
            show_department = showContactArrayList.get(position1).getContactPersonDept();
            if (show_department==null||show_department.equals("null")||show_department.equals("")){
                txt_show_contact_designation.setVisibility(View.GONE);
            }else {
                txt_show_contact_department.setText(show_department);
            }
            txt_show_contact_department.setText(show_department);
            show_mobile = showContactArrayList.get(position1).getMobile();
            txt_show_contact_mobileno.setText(show_mobile);
            show_email = showContactArrayList.get(position1).getEmailId();
            txt_show_contact_email.setText(show_email);
            show_primary_contact = showContactArrayList.get(position1).getIsPrimaryContact();
            txt_show_contact_primary.setText(show_primary_contact);
            if (showContactArrayList.get(position1).getIsPrimaryContact().equalsIgnoreCase("Yes")){
                txt_show_contactname.setTextColor(getResources().getColor(R.color.orange));
            }else {
                txt_flag.setVisibility(View.GONE);
            }
        }

      /*  for (int position=0; position<partialCallLists.size(); position++) {
            showcontactconvertView.setTag(position);
        }
*/

        for (int position = 0; position < showContactArrayList.size(); position++) {
            showcontactconvertView.setTag(position);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact_id = showContactArrayList.get(position1).getPKSuspContactDtlsID();
                System.out.println("Call_id-1 :" + Contact_id);

                JSONObject jsoncontactdelete = new JSONObject();

                try {

                    jsoncontactdelete.put("CallId", Call_Callid);
                    jsoncontactdelete.put("PKSuspContactDtlsID", Contact_id);
                    Deletecalljson = jsoncontactdelete.toString();

                    System.out.println("Contact list : " + jsoncontactdelete.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Deletecalljson = Deletecalljson.toString();
                Deletecalljson = Deletecalljson.replaceAll("\\\\", "");

                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new POSTdeleteContact().execute(Deletecalljson);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

            }
        });

        txt_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (view.getTag() instanceof Integer) {
                    position1 = (Integer) view.getTag();
                }

                ActionPerformitem = txt_action.getSelectedItem().toString();

                if (ActionPerformitem.equals("Delete")) {


                    //  Call_Callid = partialCallLists.get(pos).getCallId();
                    System.out.println("Call_id :" + Call_Callid);

                }
                if (ActionPerformitem.equals("Edit")) {
                    len_contact_edit.setVisibility(View.VISIBLE);
                    Call_Contactname = txt_show_contactname.getText().toString();
                    edit_contactname.setText(Call_Contactname);
                    Call_Designation = txt_show_contact_designation.getText().toString();
                    edit_designation.setText(Call_Designation);
                    Call_Email = txt_show_contact_email.getText().toString();
                    edit_email.setText(Call_Email);
                    Call_Mobile = txt_show_contact_mobileno.getText().toString();
                    edit_mobile.setText(Call_Mobile);
                    Call_Department = spinner_edit_department.getSelectedItem().toString();
                    Call_primary = spinner_edit_primary.getSelectedItem().toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        txt_show_contact_mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=showContactArrayList.get(position1).getMobile();
                tvCallMethd(mobile);
            }
        });
        txt_show_contact_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=showContactArrayList.get(position1).getEmailId();
                tvEmailMethd(mobile);
            }
        });


       /* add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", call_type);
                intent.putExtra("name",showContactArrayList.get(position1).getContactName());
                intent.putExtra("designation",showContactArrayList.get(position1).getDesignation());
                intent.putExtra("dept",showContactArrayList.get(position1).getContactPersonDept());
                intent.putExtra("contact",showContactArrayList.get(position1).getMobile());
                intent.putExtra("email",showContactArrayList.get(position1).getEmailId());
                intent.putExtra("flag",showContactArrayList.get(position1).getIsPrimaryContact());
                intent.putExtra("Mode","E");
               /* if (Call_primary.equals("Yes")) {
                    Call_primary = "1";
                } else {
                    Call_primary = "0";

                }*/
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



            }
        });
        len_show_calldetails.addView(showcontactconvertView);
        int position = len_show_calldetails.indexOfChild(showcontactconvertView);
        len_show_calldetails.setTag(position);



    }
    class POSTdeleteContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;
        String url="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            if (call_type.equals("2")){
                url = CompanyURL + WebUrlClass.api_POSTdeleteCustContact;
            }else {
                url = CompanyURL + WebUrlClass.api_POSTdeleteContact;
            }
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0],CRM_Callslist_Partial.this);

                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dialog_loading.setVisibility(View.GONE);

            Toast.makeText(CRM_Callslist_Partial.this, "Contact deleted successfully", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

    }


    // Call Rating


    public boolean validate(Context context) {
        if (callStatus.equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Select Call Status", Toast.LENGTH_LONG).show();
            return false;
        }  else if (Expectedvalue.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Select Expected value", Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextExpecteddate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Select Expected closure date", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

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
    class GetReason extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Get_Reason
                        + "?ReasonCode=" + URLEncoder.encode("RD", "UTF-8");

                res = ut.OpenConnection(url);
                res = res.toString().replaceAll("\\\\", "");
                res = res.replaceAll("\\\\\\\\/", "");
                res = res.substring(1, res.length() - 1);

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_REASON, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_REASON, null);
                int count = c.getCount();
                String columnName, columnValue;
                reason.add("Select");
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (columnName.equalsIgnoreCase("ReasonDescription")) {
                            reason.add(jorder.getString("ReasonDescription"));
                        }


                    }
                    long a = sql.insert(db.TABLE_REASON, null, values);
                    Log.d("crm_dialog_action", "count " + a);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("PKReasonID")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CRM_Callslist_Partial.this, android.R.layout.simple_spinner_item, reason);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_reason.setAdapter(dataAdapter);
            }
        }

    }



    class PostCallRating extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Save_Call_Rating;

            try {
                res = ut.OpenPostConnection(url, jsonparams,CRM_Callslist_Partial.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
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
            if (response.equalsIgnoreCase("")) {
                Toast.makeText(CRM_Callslist_Partial.this,"Rating updated successfully",Toast.LENGTH_LONG).show();

               // Intent intent = new Intent(CRM_Callslist_Partial.this, CRMHomeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTRY && resultCode == COUNTRY) {
            if (isOrderType == true) {
                String Description = data.getStringExtra("Name");
                ordertypeid = data.getStringExtra("ID");

                spinner_Ordertype.setText(Description);

                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetReviewDaysJSON().execute(ordertypeid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }else if (isCampaigh == true) {
                String Campaign = data.getStringExtra("Name");
                campaignId = data.getStringExtra("ID");
                spinner_campaign.setText(Campaign);

            }else {
                ReasonDescription = data.getStringExtra("Name");
                spinner_reason.setText(ReasonDescription);

                String que = "SELECT PKReasonID FROM " + db.TABLE_REASON + " WHERE ReasonDescription='" + ReasonDescription + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    PKReasonID = cur.getString(cur.getColumnIndex("PKReasonID"));
                }
            }
        }
        try {
            if (requestCode == RESULT_CAPTURE_IMG && resultCode == this.RESULT_OK) {
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
                        File file = new File(getRealPathFromUri(CRM_Callslist_Partial.this, outPutfileUri));//create path from uri
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


            }
            else if (requestCode == RESULT_LOAD_IMG && resultCode == this.RESULT_OK && null != data) {

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
                    } else {
                        File file = new File(getRealPathFromUri(CRM_Callslist_Partial.this, outPutfileUri));//create path from uri
                        attachment = file.getName();

                        //CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostUploadImageMethodProspect().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(CRM_Callslist_Partial.this, msg);
                                }
                            });
                        } else {
                            ut.displayToast(CRM_Callslist_Partial.this, "No Internet connection");
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
                File file = new File(getRealPathFromUri(CRM_Callslist_Partial.this, selectedFileURI));//create path from uri
                Log.d("", "File : " + file.getName());
                attachment = file.getName();
                Toast.makeText(CRM_Callslist_Partial.this, "Document send successfully", Toast.LENGTH_SHORT).show();
                //	CreateOfflineSaveAttachment(attachment,attachment,3,"Document send successfully",uuidInString);


                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostUploadImageMethodProspect().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(CRM_Callslist_Partial.this, msg);
                        }
                    });
                } else {
                    ut.displayToast(CRM_Callslist_Partial.this, "No Internet connection");
                    //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    // Chat Development


    class DownloadChatRoomDisplayDataJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        private JSONObject obj;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
           /* progressDialog = new ProgressDialog(CRM_Callslist_Partial.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
*/

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getChatRoomsForCall + "?CallId=" + CallId;
            try {
                res = ut.OpenConnection(url, CRM_Callslist_Partial.this);

                response =  String.valueOf(new JSONTokener(res.toString()).nextValue());

                try {
                    obj = new JSONObject(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dialog_loading.setVisibility(View.GONE);
            if (response.contains("[]")) {
                /*Intent intent = new Intent(MultipleGroupActivity.this, AddGroupActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("firm", Firm_name);
                intent.putExtra("projmasterId", ProjectmasterID);
                intent.putExtra("AssignBy",AssignBy);
                intent.putExtra("AssignById",AssignById);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
            } else {
                if (response != null) {

                    try {
                        listview_multiple_group.setVisibility(View.VISIBLE);
                        ContentValues values = new ContentValues();
                        JSONArray jResults = new JSONArray();
                        jResults = obj.getJSONArray("ChatRoomsForCall");


                        chatRoomDisplayArrayList.clear();
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject Jsonchatmember = jResults.getJSONObject(i);
                            ChatGroup chatUser = new ChatGroup();
                            String Chatroomname = Jsonchatmember.getString("ChatRoomName");
                            chatUser.setChatroom(Chatroomname);
                            ChatRoomId = Jsonchatmember.getString("ChatRoomId");
                            chatUser.setChatRoomId(ChatRoomId);
                            String ChatRoomStatus=Jsonchatmember.getString("ChatRoomStatus");
                            chatUser.setStatus(ChatRoomStatus);
                            chatUser.setStartTime(Jsonchatmember.getString("StartTime"));
                            chatUser.setAddedBy(Jsonchatmember.getString("AddedBy"));
                            chatUser.setUserMasterId(UserMasterId);
                            chatUser.setChatSourceId(Call_Callid);
                            chatUser.setChatType(Jsonchatmember.getString("ChatType"));
                            chatUser.setChatMessage("");
                            if (ChatRoomStatus.equals("Closed")) {
                            } else {
                                chatRoomDisplayArrayList.add(chatUser);
                            }

                        }

                        chatRoomMultipleAdapter=new ChatRoomMultipleAdapter(CRM_Callslist_Partial.this,chatRoomDisplayArrayList);
                        listview_multiple_group.setAdapter(chatRoomMultipleAdapter);
                        chatRoomMultipleAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }


// Call History

    class DownloadCallHistoryData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            callHistoryArrayList.clear();
            try {
                String url = CompanyURL + WebUrlClass.api_GetCallHistory + "?CallId=" +
                        URLEncoder.encode(CallId, "UTF-8");

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                    sql.delete(db.TABLE_CALLHISTORY, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALLHISTORY, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jcallhistory = jResults.getJSONObject(i);

                        CallHistory callHistory = new CallHistory();

                        callHistory.setCallHistoryId(jcallhistory.getString("CallHistoryId"));
                        callHistory.setCallId(jcallhistory.getString("CallId"));
                        callHistory.setCurrentCallOwner(jcallhistory.getString("CurrentCallOwner"));
                        callHistory.setActionType(jcallhistory.getString("ActionType"));

                        String contact = jcallhistory.getString("Contact");

                        contact = contact.replace("rr0", "");
                        String Contact = contact;
                        System.out.println("Whoom :" + Contact);
                        callHistory.setContact(Contact);
                        callHistory.setPurpose(jcallhistory.getString("Purpose"));
                        callHistory.setNextAction(jcallhistory.getString("NextAction"));
                        // callHistory.setNextActionDateTime(jcallhistory.getString("NextActionDateTime"));
                        String jsonDOJ = jcallhistory.getString("NextActionDateTime");

                        System.out.println("Datetimecall :" + jsonDOJ);
                        jsonDOJ = jsonDOJ.substring(jsonDOJ.indexOf("(") + 1, jsonDOJ.lastIndexOf(")"));
                        long DOJ_date = Long.parseLong(jsonDOJ);
                        DOJDate = new Date(DOJ_date);
                        jsonDOJ = sdf1.format(DOJDate);
                        System.out.println("Datetimecall_1 :" + jsonDOJ);
                        callHistory.setNextActionDateTime(jsonDOJ);
                        String jsonDOB = jcallhistory.getString("ModifiedDt");
                        jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                        long DOB_date = Long.parseLong(jsonDOB);
                        DOBDate = new Date(DOB_date);
                        jsonDOB = sdf1.format(DOBDate);
                        callHistory.setModifiedDt(jsonDOB);
                        callHistory.setOutcome(jcallhistory.getString("Outcome"));
                        callHistory.setUserName(jcallhistory.getString("UserName"));
                        callHistory.setOutcomeCode(jcallhistory.getString("OutcomeCode"));
                        callHistory.setLatestRemark(jcallhistory.getString("LatestRemark"));
                        cf.AddCallHistory(callHistory);
                        callHistoryArrayList.add(callHistory);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dialog_loading.setVisibility(View.GONE);
            if (response.equals("[]")) {
                Toast.makeText(CRM_Callslist_Partial.this, "Call history not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {

                    UpdatList();

                }

            }
        }
    }

    private void UpdatList() {
        callHistoryArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CALLHISTORY + " WHERE CallId='" + CallId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                callhistory_listview.setVisibility(View.VISIBLE);
                CallHistory callHistory = new CallHistory();
                callHistory.setCallHistoryId(cur.getString(cur.getColumnIndex("CallHistoryId")));
                callHistory.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                callHistory.setCurrentCallOwner(cur.getString(cur.getColumnIndex("CurrentCallOwner")));
                callHistory.setActionType(cur.getString(cur.getColumnIndex("ActionType")));
                callHistory.setContact(cur.getString(cur.getColumnIndex("Contact")));
                callHistory.setPurpose(cur.getString(cur.getColumnIndex("Purpose")));
                callHistory.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                callHistory.setNextActionDateTime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                callHistory.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                callHistory.setOutcome(cur.getString(cur.getColumnIndex("Outcome")));
                callHistory.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                callHistory.setOutcomeCode(cur.getString(cur.getColumnIndex("OutcomeCode")));
                callHistory.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                callHistoryArrayList.add(callHistory);
            } while (cur.moveToNext());

            callHistoryAdapter = new CallHistoryAdapter(CRM_Callslist_Partial.this, callHistoryArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            callhistory_listview.setLayoutManager(mLayoutManager);
            callhistory_listview.setItemAnimator(new DefaultItemAnimator());
            callhistory_listview.setAdapter(callHistoryAdapter);

        }


    }

    public class GetReviewDaysJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetReviewDays + "?OrderType=" + ordertypeid;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
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
            //   dismissProgressDialog();
            if (response.equals("")) {
                ReviewDate = "12/30/9999 00:00:00.000";
            } else {
                ReviewDate = response;
            }


        }

    }

    private class DownloadOpportunityDetails extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getOpportunityDetail
                        + "?CallId=" + URLEncoder.encode(CallId, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog_loading.setVisibility(View.GONE);
            if (response != null) {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {

                        JSONObject jorder = jResults.getJSONObject(i);
                        notes = jorder.getString("LatestRemark");
                        editTextNotes.setText(notes);
                        ordertypeid = jorder.getString("OrderTypeMasterId");
                        if (lstOrdertype.size() != 0) {
                            int orderPos = -1;
                            for (int j = 0; j < lstOrdertype.size(); j++) {
                                if (lstOrdertype.get(j).getOrderTypeMasterId().equals(ordertypeid)) {
                                    orderPos = j;
                                    break;
                                }
                            }
                            if (orderPos != -1) {
                                spinner_Ordertype.setText(lstOrdertype.get(orderPos).getDescription());
                            } else {
                                spinner_Ordertype.setText(lstOrdertype.get(0).getDescription());
                            }
                        }
                        boeid = jorder.getString("CurrentCallOwner");
                        seid = jorder.getInt("CRMCategory");
                        campaignId = jorder.getString("Campaign");
                        if (lstCampaign.size() != 0) {
                            int pos = -1;
                            for (int j = 0; j < lstCampaign.size(); j++) {
                                if (campaignId.equals(lstCampaign.get(j).getOrderTypeMasterId())) {
                                    pos = j;
                                    break;
                                }
                            }

                            if (pos != -1) {
                                spinner_campaign.setText(lstCampaign.get(pos).getDescription());
                            } else {
                                spinner_campaign.setText("");
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayCampaign() {

        lstCampaignString.clear();
        lstCampaign.clear();
        String countQuery = "SELECT PKCampaignId,CampaignName FROM "
                + db.TABLE_Campaign;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstCampaign.add(new OrderType(cursor.getString(cursor.getColumnIndex("CampaignName")),
                        cursor.getString(cursor.getColumnIndex("PKCampaignId")), "", ""));
                lstCampaignString.add(cursor.getString(cursor.getColumnIndex("CampaignName")));
            } while (cursor.moveToNext());
        }

        CreateOpportunityActivity.MySpinnerAdapter adapter = new CreateOpportunityActivity.MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstCampaignString);
        spinner_campaign.setAdapter(adapter);
    }

    private void displayOrderType() {

        lstOrdertype.clear();
        lstOrderTypeString.clear();
        String countQuery = "SELECT  OrderTypeMasterId,Description FROM "
                + db.TABLE_OrderType;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstOrdertype.add(new OrderType(cursor.getString(cursor.getColumnIndex("Description")),
                        cursor.getString(cursor.getColumnIndex("OrderTypeMasterId"))));
                lstOrderTypeString.add(cursor.getString(cursor.getColumnIndex("Description")));
            } while (cursor.moveToNext());
        }


        CreateOpportunityActivity.MySpinnerAdapter adapter = new CreateOpportunityActivity.MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstOrderTypeString);
        spinner_Ordertype.setAdapter(adapter);


    }

    class DownloadOrderTypeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                    /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");*/
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_OrderType, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_OrderType, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_OrderType, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {

            }
            displayOrderType();

        }

    }


    class DownloadCampaignJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Compaign;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Campaign, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Campaign, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Campaign, null, values);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {

            }
            displayCampaign();

        }

    }

    public String timeStampConversion(String str_date, String status) {

        // String str_date="13-09-2011";
        Calendar cal = Calendar.getInstance();


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime() / 1000L;
        int val = 0;
        if (status.equalsIgnoreCase("Hot")) {  // 15 days
             val = 60 * 60 * 24 * 15;
        } else if (status.equalsIgnoreCase("Warm")) { // 30 days
             val = 60 * 60 * 24 * 30;
        }

        time = time + val;

        //  DateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy")
        new SimpleDateFormat("dd/MM/yyyy").format(time * 1000L);
        cal.setTimeInMillis(time);
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(time * 1000L);

        return dateString;
    }



    //  Attachement Details

    public class downloadAttachmentDetails extends AsyncTask<String, Void, String> {

        String res = "";
        String pkCssDetailsId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            try {
                String url = CompanyURL + WebUrlClass.api_getUploadedAttachment_Sahara + "?ActivityId=" + CallId + "&SourceType=Call";
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    //res = res.substring(1, res.length() - 1);
                    // res = res.replaceAll("\\\\", "");

                }
                String s = res;


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }


            return res;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            dialog_loading.setVisibility(View.GONE);
            if (res.contains("[]")) {


            } else {

                try {

                    JSONArray jResults = new JSONArray(res.toString());

                    ContentValues values = new ContentValues();
                    sql.delete(db.TABLE_ATTACHMENT_DETAILS, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ATTACHMENT_DETAILS, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_ATTACHMENT_DETAILS, null, values);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("res", e.getMessage());
                }

                attachmentList = getAttachmentDetails();



            }


        }
    }

    private ArrayList<AttachmentBean> getAttachmentDetails() {
        ArrayList<AttachmentBean> attachmentDetails = new ArrayList<AttachmentBean>();

        Cursor c = sql.rawQuery("SELECT * from " + db.TABLE_ATTACHMENT_DETAILS, null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ls_attachname.setVisibility(View.VISIBLE);
                attachmentBean = new AttachmentBean();

                attachmentBean.setPkAttachId(c.getString(c.getColumnIndex("PkAttachId")));
                attachmentBean.setAttachGuid(c.getString(c.getColumnIndex("AttachGuid")));
                attachmentBean.setAttachFilename(c.getString(c.getColumnIndex("AttachFilename")));
                attachmentBean.setPath(c.getString(c.getColumnIndex("Path")));
                attachmentBean.setActivityId(c.getString(c.getColumnIndex("ActivityId")));
                attachmentBean.setAddedBy(c.getString(c.getColumnIndex("AddedBy")));
                attachmentBean.setModifiedBy(c.getString(c.getColumnIndex("ModifiedBy")));
                attachmentBean.setModifiedDt(c.getString(c.getColumnIndex("ModifiedDt")));
                attachmentBean.setIsDeleted(c.getString(c.getColumnIndex("IsDeleted")));
                attachmentBean.setSourcetype(c.getString(c.getColumnIndex("Sourcetype")));
                attachmentBean.setGPSId(c.getString(c.getColumnIndex("GPSId")));
                attachmentBean.setAttachmentType(c.getString(c.getColumnIndex("AttachmentType")));
                attachmentBean.setLatitude(c.getString(c.getColumnIndex("Latitude")));
                attachmentBean.setLongitude(c.getString(c.getColumnIndex("Longitude")));
                attachmentBean.setAttachmentCode(c.getString(c.getColumnIndex("AttachmentCode")));
                attachmentBean.setAttachmentDesc(c.getString(c.getColumnIndex("AttachmentDesc")));

                attachmentDetails.add(attachmentBean);
            } while (c.moveToNext());

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            ls_attachname.setLayoutManager(layoutManager);
            attachmentDetailsAdapter = new AttachmentDetailsAdapter(CRM_Callslist_Partial.this, attachmentList , true);
            ls_attachname.setAdapter(attachmentDetailsAdapter);

        } else {

        }


        return attachmentDetails;


    }


    public void downloadFile(int adapterPosition, boolean isDownload) {
        selectedPos = adapterPosition;
        if (isDownload) {
            String attachmentName1 = attachmentList.get(adapterPosition).getAttachFilename();
            String path = attachmentList.get(adapterPosition).getPath();

            if (isnet()) {
                String path1 = Environment.getExternalStorageDirectory()
                        .toString();
                File file = new File(path1 + "/" + "Sahara" + "/" + "File");
                if (file.exists()) {
                    final File fileNew = new File(file + "/" + attachmentName1);
                    if (fileNew.exists()) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                urlGetMimeType(fileNew.getAbsolutePath());
                                Toast.makeText(context, "File Already downloaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        cllDownloadApi(path, attachmentName1);
                    }
                } else {
                    cllDownloadApi(path, attachmentName1);
                }
            }

        } else {


        }
    }

    private void urlGetMimeType(String path) {
        File file = new File(path);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);

        startActivity(intent);
    }

    public void deleteAttachment(int adapterPosition, boolean b) {

            callDeleteAttachmentApi(adapterPosition);

    }

    private void callDeleteAttachmentApi(final int selectedPos) {

        if(isnet())
        {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DeleteAttachmentApi().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    // ((NotificationActivity)context).showPopUp(false);
                }
            });
        }else
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();

    }

    private void cllDownloadApi(final String path, final String attachmentName1) {
        new StartSession(context, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadFileApi().execute(path, attachmentName1);
            }

            @Override
            public void callfailMethod(String msg) {
                // ((NotificationActivity)context).showPhhopUp(false);
            }
        });
    }


    private class DownloadFileApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... strings) {

            String pathName = strings[0];
            final String fileName = strings[1];
            int count;
            File file = null;
            String urlStr = CompanyURL + "/Downloads/" + EnvMasterId + "/" + fileName;
            try {
                final String path1 = Environment.getExternalStorageDirectory()
                        .toString();

                if (Constants.type == Constants.Type.Sahara) {
                    file = new File(path1 + "/" + "Sahara" + "/" + "File");

                } else {
                    file = new File(path1 + "/" + "Ekatm" + "/" + "File");
                }
                if (!file.exists())
                    file.mkdirs();
                //   pdfFile = new File(file + "/" + fileName);
                // file1 = String.valueOf(pdfFile);


                try {
                    //pdfFile = File.createTempFile(filename /* prefix */,prefix, pdfFile /* directory */);

                    final File fileNew = new File(file + "/" + fileName);
                    fileNew.createNewFile();
                    //downloadFileInloacl(url , new File(file + "/" + fileNew));

                    try {
                        urlStr = urlStr.replaceAll(" ", "%20");
                        //final File directory =  new File(file + "/" + fileNew);
                        URL url = new URL(urlStr);

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        long total = 0;


                        FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
                        InputStream inputStream = urlConnection.getInputStream();
                        int totalSize = urlConnection.getContentLength();
                        int serverResponseCode = urlConnection.getResponseCode();
                        String serverResponseMessage = urlConnection.getResponseMessage();
                        byte[] buffer = new byte[MEGABYTE];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            total += bufferLength;
                            fileOutputStream.write(buffer, 0, bufferLength);
                            onProgressUpdate("" + (int) ((total * 100) / lenghtOfFile));
                        }
                        fileOutputStream.close();
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                urlGetMimeType(fileNew.getAbsolutePath());
                              /*  MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                }*/

                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        // ((NotificationActivity)context).showPopUp(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    }
                    //publishProgress(""+(int)((total*100)/lenghtOfFile));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // ((NotificationActivity)context).showPopUp(false);
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissDialog(progress_bar_type);
        }
    }


    private class DeleteAttachmentApi extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String url = null;
            String attachId = attachmentList.get(selectedPos).getAttachGuid();
            url = CompanyURL + WebUrlClass.api_deleteAttachment + "?Attachid=" + attachId;

            try {
                res = ut.OpenPostConnection(url, "", getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("deleteResponse", response);
            if(!response.equals("error")){
                Toast.makeText(context , response , Toast.LENGTH_SHORT).show();
                attachmentDetailsAdapter.notifyDataSetChanged();
            }

        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }



    class DownloadProductDetailJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
               // String url = CompanyURL + WebUrlClass.api_getProductDetail + "?FKSuspectId=" + URLEncoder.encode(Prospect, "UTF-8");
                String url = CompanyURL + WebUrlClass.api_getproductforEdit + "?CallId=" + URLEncoder.encode(CallId, "UTF-8");

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                /*    sql.delete(db.TABLE_Product_Details, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details, null);   */

                    sql.delete(db.TABLE_Product_Details_New, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details_New, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                            if (columnName.equalsIgnoreCase("Quantity")) {
                                columnValue = "1";
                            } else {
                                columnValue = jorder.getString(columnName);
                            }

                            values.put(columnName, columnValue);
                        }

                        //   long a = sql.insert(db.TABLE_Product_Details, null, values);
                        long a = sql.insert(db.TABLE_Product_Details_New, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response.contains("")) {

            }
            displayProductDetails1();

        }

    }
    private void displayProductDetails1() {

        productBeanList.clear();
        //    String countQuery = "SELECT  ItemDesc,Qnty FROM " + db.TABLE_Product_Details;
        String countQuery = "SELECT  ItemDesc,FkProductId,Quantity FROM " + db.TABLE_Product_Details_New;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                productBean.setProductId(cursor.getString(cursor.getColumnIndex("FkProductId")));
                productBean.setQnty(cursor.getString(cursor.getColumnIndex("Quantity")));
                productBeanList.add(productBean);

            } while (cursor.moveToNext());
        }


        linear_list_product.removeAllViews();
        if (productBeanList.size() > 0) {

            for (int i = 0; i < productBeanList.size(); i++) {
                addViewList(i);
            }
        }else {
            btnAddProd.setVisibility(View.VISIBLE);
        }

    }


    private void addViewList(int i) {
        TextView txtProduct;
        TextView edtqty;
        //EditText edtqty;
        ImageView btn_delete;
        LayoutInflater layoutInflater = (LayoutInflater) CRM_Callslist_Partial.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_product_recycler,
                null);
        txtProduct = (TextView) convertView.findViewById(R.id.txtProduct);
        //edtqty = (EditText) convertView.findViewById(R.id.edtqty);
        edtqty = convertView.findViewById(R.id.edtqty);
        btn_delete = convertView.findViewById(R.id.btn_delete);
        CardView cardView_prod = convertView.findViewById(R.id.cardView_prod);


        txtProduct.setText(productBeanList.get(position).getItemDesc());
        edtqty.setText("Quantity :"+productBeanList.get(position).getQnty());


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name = productBeanList.get(position).getItemDesc();
                String productId = productBeanList.get(position).getProductId();
                long a = sql.delete(db.TABLE_Product_Details_New,
                        "FkProductId=?", new String[]{productId});
                displayProductDetails1();

            }
        });

        cardView_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CRM_Callslist_Partial.this,ProductActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });


        linear_list_product.addView(convertView);
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
	private void CreateOfflineSaveAttachment(String imageUri, String name, int attachmentFlAG, String remark, String activityId) {

		long a = cf.addofflinedata(imageUri, name, attachmentFlAG, remark, activityId);

		if (a != -1) {
			Toast.makeText(getApplicationContext(), "Attachment Saved", Toast.LENGTH_LONG).show();
			setJobShedulder();


		} else {
			Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

		}
	}

	private void setJobShedulder() {
		if (myJobNew == null) {
			dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(CRM_Callslist_Partial.this));
			callJobDispacher();
		} else {
			if (!AppCommon.getInstance(this).isServiceIsStart()) {
				dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(CRM_Callslist_Partial.this));
				callJobDispacher();
			} else {
				dispatcherNew.cancelAll();
				dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(CRM_Callslist_Partial.this));
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
			Toast.makeText(CRM_Callslist_Partial.this, "Please install a File Manager.",
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
			attachment = file1.getAbsolutePath();
			File f = new File(attachment);
			//Attachment=f.getName();


			Toast.makeText(CRM_Callslist_Partial.this,"Image send successfully",Toast.LENGTH_SHORT).show();

			//CreateOfflineSaveAttachment(attachment,attachment,3,"Image send successfully",uuidInString);

			if (isnet()) {
				new StartSession(context, new CallbackInterface() {
					@Override
					public void callMethod() {
						new PostUploadImageMethodProspect().execute();


					}

					@Override
					public void callfailMethod(String msg) {
						ut.displayToast(CRM_Callslist_Partial.this, msg);
					}
				});
			} else {
				ut.displayToast(CRM_Callslist_Partial.this, "No Internet connection");
				//  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
			}





			//	uri = Uri.fromFile(f);
//file:///data/data/vworkbench7.vritti.com.vworkbench7/files/Image1825476171.jpeg


		} catch (Exception e) {
			Log.e("Your Error Message", e.getMessage());
		}
		return uri;
	}


	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
                FileInputStream fileInputStream = new FileInputStream(attachment);
                Object res = null;
                File file=new File(attachment);
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


    @Override
    protected void onResume() {
        super.onResume();
            linear_contact.setVisibility(View.GONE);
            linear_chat.setVisibility(View.GONE);
            callhistory_listview.setVisibility(View.GONE);
            rel_opportunity.setVisibility(View.GONE);
            linear_attachment.setVisibility(View.GONE);
            if (ut.isNet(CRM_Callslist_Partial.this)) {
                dialog_loading.setVisibility(View.VISIBLE);
                new StartSession(CRM_Callslist_Partial.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCallHistoryData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }

            thistory.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tcontact.setBackgroundColor(getResources().getColor(R.color.white));
            laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
            tconvers.setBackgroundColor(getResources().getColor(R.color.white));
            tdetails.setBackgroundColor(getResources().getColor(R.color.white));
            tattachment.setBackgroundColor(getResources().getColor(R.color.white));
            txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
            call_rating.setBackgroundColor(getResources().getColor(R.color.white));
        }

    class GetCallReview extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url =CompanyURL+ WebUrlClass.api_GetReviewDate + "?CallId="+params[0];
            try {
                res = ut.OpenConnection(url, context);
                Response_Call = res;
                Response_Call = Response_Call.substring(1, Response_Call.length()-1);

               /* Response_Call = res.toString();
                Response_Call = Response_Call.substring(1, Response_Call.length()-1);*/

                Log.d("Response-1",Response_Call);


            } catch (Exception e) {
                e.printStackTrace();
                Response_Call = WebUrlClass.setError;
                Log.d("Response-2",Response_Call);
            }
            return Response_Call;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);


            if (Response_Call!=null||Response_Call.equalsIgnoreCase("")) {
                Log.d("Response-3",Response_Call);
            }
        }
    }

    public  String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

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

}
