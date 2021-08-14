package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.SaharaModule.SaharaBeans.AttachmentBean;
import com.vritti.chat.activity.AddChatRoomActivity;
import com.vritti.chat.activity.AddGroupActivity;
import com.vritti.chat.activity.ChatRoomMultipleAdapter;
import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.crm.adapter.AttachmentDetailsAdapter;
import com.vritti.crm.adapter.CRMAttachmentDetailsAdapter;
import com.vritti.crm.adapter.CallHistoryAdapter;
import com.vritti.crm.bean.ApproverData;
import com.vritti.crm.bean.ProvisionalData;
import com.vritti.crm.bean.ShowContact;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.vritti.vwb.vworkbench.NotificationActivity.progress_bar_type;

public class CRM_Collection_List extends AppCompatActivity {

    //private final ArrayList<PartialCallList> partialCallListArrayList;
    TextView tcontact, laycall_type, tconvers, tdetails, thistory, tattachment,txt_prospect,call_rating;

    int startPos = 0;


    TextView firmname,actiondatetime,tv_latestremark,tvcall,txt_invoice_no,txt_invoice_date,txt_amount;
    ImageView callrating,img_nextaction;
    ImageView img_add,img_refresh,img_back,img_appotunity_update;
    TextView txt_title;

    String Firmname="",CallId="",Datetime="",NextAction="",Status="",Call="",Remark="",Prospect="",call_type="";

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",IsChatApplicable="";
    String  SourceId="",Invoice_No="",Invoice_Date="",Invoice_Amount="";

    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;
    int Provisional_count;
    String CallLogType="",Start="",EndTime="",Duration="",RowNo="";
    String Milestone="",Mobile="";


    // Attachment Code

    AttachmentBean attachmentBean;
    public static ArrayList<AttachmentBean> attachmentList=new ArrayList<>();
    RecyclerView ls_attachname;
    private CRMAttachmentDetailsAdapter attachmentDetailsAdapter;
    int selectedPos = -1;
    private ProgressDialog pDialog;
    private final int MEGABYTE = 1024 * 1024;


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
    ImageView image_add;
    private ProgressBar dialog_loading;


    // Provisional Receipt

    private boolean isProvision=false;
    private int Followup=2;
    EditText edt_bankname, edt_amount, edt_instrument_no, edt_tds_amount, edt_narration,edt_deposite_bank;
    TextView txtfirmname, txtcall, txtcityname, txtactiondatetime, txtopportunity_type, txt_latestremark, txt_Save, txt_Close, txt_invoice_number, txt_Amount;
    ImageView img_date;
    int Provi_count;
    SimpleDateFormat dfDate;
    LinearLayout lsCall_list;
    ArrayList<ProvisionalData> provisionalDataArrayList=new ArrayList<>();
    String BankMasterId, CustomerId,Amount, InstrumentNo, BankName, TDSAmount, Narration, ReceiptStatus, AddedBy, AddedDt, PaymentDepBank, DepositedDt;
    AutoCompleteTextView spinner_bankname;
    List<String> listBanknamedata = new ArrayList<String>();
    TextView edt_date;
    ImageView imag_save_provision;
    private String date="";
    String FKCustomerId, FKConsigneeId,ProvisionalRecieptjson;
    LinearLayout len_pro;

    // Discount Voucher

    ImageView image_save_discount;
    private String Reversal_amount="",reason="",Invoice_NO="";
    private String ApprId="";
    Spinner spinner_approver;
    ArrayList<ApproverData> approverDatas = new ArrayList<>();
    ArrayList<String> Approverlist1;
    private EditText edt_reversal_amount,edt_reason;
    AlertDialog.Builder builder;
    AlertDialog alertDialog, alertDialog1;
    LinearLayout len_discount;


    // Call History

    RecyclerView callhistory_listview;
    ArrayList<CallHistory> callHistoryArrayList=new ArrayList<>();
    CallHistoryAdapter callHistoryAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_collection_partial_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        context= CRM_Collection_List.this;
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
        //UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        tcontact = (TextView)findViewById(R.id.contact);
        laycall_type = (TextView)findViewById(R.id.laycall_type);
        tconvers = (TextView)findViewById(R.id.convers);
        tdetails = (TextView)findViewById(R.id.details);
        thistory = (TextView)findViewById(R.id.history);
        tattachment = (TextView)findViewById(R.id.attachment);
        dialog_loading=findViewById(R.id.dialog_loading);
        callhistory_listview=findViewById(R.id.callhistory_listview);


        firmname = (TextView)findViewById(R.id.firmname);
        actiondatetime = (TextView)findViewById(R.id.actiondatetime);
        tv_latestremark = (TextView)findViewById(R.id.tv_latestremark);
        tvcall = (TextView)findViewById(R.id.tvcall);
        callrating = (ImageView) findViewById(R.id.callrating);
        img_nextaction = (ImageView) findViewById(R.id.img_nextaction);
        img_appotunity_update=findViewById(R.id.img_appotunity_update);
        txt_prospect=findViewById(R.id.txt_prospect);
        call_rating=findViewById(R.id.call_rating);
        txt_amount=findViewById(R.id.txt_amount);
        txt_invoice_date=findViewById(R.id.txt_invoice_date);
        txt_invoice_no=findViewById(R.id.txt_invoice_no);
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
        Invoice_No=getIntent().getStringExtra("invoice_no");
        Invoice_Date=getIntent().getStringExtra("invoice_date");
        Invoice_Amount=getIntent().getStringExtra("invoice_amount");
        Mobile=getIntent().getStringExtra("mobile");


        txt_invoice_no.setText(Invoice_No);
        txt_invoice_date.setText(Invoice_Date);
        txt_amount.setText(Invoice_Amount);

        Provisional_count=getIntent().getIntExtra("Provisional_count",0);

         if (getIntent().hasExtra("type")){
            CallLogType=getIntent().getStringExtra("Callfromcalllogs");
            Start=getIntent().getStringExtra("starttime");
            EndTime=getIntent().getStringExtra("endtime");
            Duration=getIntent().getStringExtra("duration");
            RowNo=getIntent().getStringExtra("rowNo");

        }




        if (call_type.equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (Status.equalsIgnoreCase("Cold")) {
              //  callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
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
            callrating.setImageResource(R.drawable.ic_cube);
        } else if (call_type.equalsIgnoreCase("3")) {
            callrating.setImageResource(R.drawable.ic_cube);
        }



        NextAction=getIntent().getStringExtra("action");
        if (NextAction.equalsIgnoreCase("Email")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.email_24));
            img_nextaction.setColorFilter(ContextCompat.getColor(CRM_Collection_List.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else if (NextAction.equalsIgnoreCase("Telephone")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
           img_nextaction.setColorFilter(ContextCompat.getColor(CRM_Collection_List.this, R.color.colorPrimary
            ), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else {
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);


        txt_title.setText("Collection");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Contact
        linear_contact = (LinearLayout) findViewById(R.id.linear_contact);
        len_show_calldetails=findViewById(R.id.len_show_calldetails);
        image_add=findViewById(R.id.image_add);

        //Chat Development

        listview_multiple_group=findViewById(R.id.listview_multiple_group);
        linear_chat=findViewById(R.id.linear_chat);
        img_create=findViewById(R.id.img_create);

        //Attachment

        ls_attachname=findViewById(R.id.ls_attachname);

        //Provisional Reeipt

        edt_amount = (EditText) findViewById(R.id.edt_amount);
        edt_instrument_no = (EditText) findViewById(R.id.edt_instrument_no);
        spinner_bankname = (AutoCompleteTextView) findViewById(R.id.spinner_bankname);
        edt_tds_amount = (EditText) findViewById(R.id.edt_tds_amount);
        edt_narration = (EditText) findViewById(R.id.edt_narration);
        edt_date = (TextView) findViewById(R.id.edt_date);
        edt_bankname = (EditText) findViewById(R.id.edt_bankname);
        img_date = (ImageView) findViewById(R.id.img_date);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);
        imag_save_provision = (ImageView) findViewById(R.id.imag_save_provision);
        len_pro = (LinearLayout) findViewById(R.id.len_pro);
        dfDate = new SimpleDateFormat("dd/MM/yyyy");
        date = dfDate.format(new Date());
        edt_date.setText(date);

        // Discount

         edt_reversal_amount = (EditText) findViewById(R.id.edt_reversal_amount);
         edt_reason = (EditText) findViewById(R.id.edt_reason);
         image_save_discount = (ImageView) findViewById(R.id.image_save_discount);
        len_discount = (LinearLayout) findViewById(R.id.len_discount);


        //
        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mobile.equals("")){

                }else {
                    tvCallMethd(Mobile);
                }
            }
        });



// Contact

        image_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Collection_List.this, ContactActivity.class);
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
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
                len_discount.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
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
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetAddShowcallDetailsJSON().execute(Prospect);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
            });


                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
                len_discount.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                if (ut.isNet(CRM_Collection_List.this)) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
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


        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                Intent intent = new Intent(CRM_Collection_List.this, OpportunityUpdateActivity_New.class);
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
                intent.putExtra("evalue", "");
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

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                laycall_type.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                callhistory_listview.setVisibility(View.GONE);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

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
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
                len_discount.setVisibility(View.GONE);
                tconvers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                callhistory_listview.setVisibility(View.GONE);

                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
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

            }
        });

        tdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                len_discount.setVisibility(View.VISIBLE);
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.VISIBLE);
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
               /* Intent intent;
                intent = new Intent(CRM_Collection_List.this, DiscountVoucherActivity.class);
                intent.putExtra("CallId",CallId);
                intent.putExtra("invoice_no", Invoice_No);*/
                tdetails.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                call_rating.setBackgroundColor(getResources().getColor(R.color.white));
                callhistory_listview.setVisibility(View.GONE);
               /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);*/
            }
        });

        thistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent i = new Intent(CRM_Collection_List.this, CallHistoryActivity.class);
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
                startActivity(i);*/
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.GONE);
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
                len_discount.setVisibility(View.GONE);
                callhistory_listview.setVisibility(View.GONE);
                if (ut.isNet(CRM_Collection_List.this)) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
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
              //  callhistory_listview.setVisibility(View.GONE);
               /* i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                linear_chat.setVisibility(View.GONE);
                len_pro.setVisibility(View.GONE);
                len_discount.setVisibility(View.GONE);


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
                callhistory_listview.setVisibility(View.GONE);
            }
        });

        txt_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("UpdateOpp", "");
                editor.commit();
               // getEditDetailsProspect();
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
                /*Intent intent = new Intent(CRM_Collection_List.this, ProvisionalActivity.class);
                intent.putExtra("Call_id", CallId);
                intent.putExtra("procount", Provisional_count);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                linear_contact.setVisibility(View.GONE);
                linear_chat.setVisibility(View.VISIBLE);
                ls_attachname.setVisibility(View.GONE);
                len_pro.setVisibility(View.VISIBLE);
                len_discount.setVisibility(View.GONE);
                if (isnet()) {
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCustomerIdData().execute(CallId);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

                if (Provisional_count > 0) {

                    if (isnet()) {
                        dialog_loading.setVisibility(View.VISIBLE);
                        new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadProvisionalListData().execute(CallId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }
                if (cf.getBankdatacount() > 0) {
                    getBankname();
                } else {
                    if (isnet()) {
                        new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadBanknameData().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }



                call_rating.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tcontact.setBackgroundColor(getResources().getColor(R.color.white));
                laycall_type.setBackgroundColor(getResources().getColor(R.color.white));
                tconvers.setBackgroundColor(getResources().getColor(R.color.white));
                tdetails.setBackgroundColor(getResources().getColor(R.color.white));
                thistory.setBackgroundColor(getResources().getColor(R.color.white));
                tattachment.setBackgroundColor(getResources().getColor(R.color.white));
                txt_prospect.setBackgroundColor(getResources().getColor(R.color.white));
                callhistory_listview.setVisibility(View.GONE);
            }
        });


        // Chat Development

        listview_multiple_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String ChatRoomStatus=chatRoomDisplayArrayList.get(i).getStatus();
                if (ChatRoomStatus.equalsIgnoreCase("Closed")){
                    Toast.makeText(CRM_Collection_List.this,"You can't send message because this chatroom has been closed",Toast.LENGTH_SHORT).show();
                }else {
                    ChatRoomId = chatRoomDisplayArrayList.get(i).getChatRoomId();
                    String ChatRoomName = chatRoomDisplayArrayList.get(i).getChatroom();
                    String Chat_status = chatRoomDisplayArrayList.get(i).getStatus();
                    Intent intent = new Intent(CRM_Collection_List.this, AddChatRoomActivity.class);
                    intent.putExtra("callid", CallId);
                    intent.putExtra("call_type", call_type);
                    intent.putExtra("ChatRoomid", ChatRoomId);
                    intent.putExtra("Chatroomname", ChatRoomName);
                    intent.putExtra("firm", Firmname);
                    intent.putExtra("status", Chat_status);
                    intent.putExtra("projmasterId", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                /* finish();*/




            }
        });


        img_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Collection_List.this, AddGroupActivity.class);
                intent.putExtra("callid", CallId);
                intent.putExtra("call_type", "Crm_Opportunity");
                intent.putExtra("firm", Firmname);
                intent.putExtra("projmasterId", "");
                intent.putExtra("AssignBy",UserName);
                intent.putExtra("AssignById",UserMasterId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        // Provisional


        imag_save_provision.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (view.getTag() instanceof Integer) {
                        pos = (Integer) view.getTag();
                    }

                    String Amount = edt_amount.getText().toString();
                    String Instrument = edt_instrument_no.getText().toString();
                    String Bankname = edt_bankname.getText().toString();
                    String TDS_Amount = edt_tds_amount.getText().toString();
                    String Narration = edt_narration.getText().toString();

                    // String Deposite = edt_deposite_bank.getText().toString();

                    JSONObject jsonprovisionalreciptadd = new JSONObject();
                    dfDate = new SimpleDateFormat("dd/MM/yyyy");
                    date = dfDate.format(new Date());

                    try {
                        jsonprovisionalreciptadd.put("CallId", CallId);
                        jsonprovisionalreciptadd.put("InvoiceNumber", Invoice_No);
                        jsonprovisionalreciptadd.put("Amount", Amount);
                        jsonprovisionalreciptadd.put("InstrumentNo", Instrument);
                        jsonprovisionalreciptadd.put("BankName", Bankname);
                        jsonprovisionalreciptadd.put("TDSAmount", TDS_Amount);
                        jsonprovisionalreciptadd.put("Narration", Narration);
                        jsonprovisionalreciptadd.put("PaymentDepBank", BankMasterId);
                        jsonprovisionalreciptadd.put("DepositedDt", date);
                        jsonprovisionalreciptadd.put("CustId", FKCustomerId);
                        jsonprovisionalreciptadd.put("ReceiptId: ", "");
                        jsonprovisionalreciptadd.put("ConsigneeId", FKConsigneeId);

                        ProvisionalRecieptjson = jsonprovisionalreciptadd.toString();

                        System.out.println("Contact list : " + ProvisionalRecieptjson.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProvisionalRecieptjson = ProvisionalRecieptjson.toString();
                    ProvisionalRecieptjson = ProvisionalRecieptjson.replaceAll("\\\\", "");
                    if (isnet()) {
                        dialog_loading.setVisibility(View.VISIBLE);
                        new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostSaveReciptJSON().execute(ProvisionalRecieptjson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener()

        {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CRM_Collection_List.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        spinner_bankname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRM_Collection_List.this,
                        CountryListActivity.class);

                isProvision = true;
                String url = CompanyURL + WebUrlClass.api_GetBankname;
                intent.putExtra("Table_Name", db.TABLE_BANKNAME);
                intent.putExtra("Id", "BankMasterId");
                intent.putExtra("DispName", "BankName");
                intent.putExtra("WHClauseParameter", "");
                //intent.putExtra("WHClauseParamVal","");
                intent.putExtra("APIName", url);
                //intent.putExtra("APIParameters","");
                //intent.putExtra("ArrayList",    "ArrayList<Territory> mList = new ArrayList<>()");
                startActivityForResult(intent, Followup);
            }
        });

        image_save_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Reversal_amount = edt_reversal_amount.getText().toString();
                reason = edt_reason.getText().toString();

                getapproverdialog(Reversal_amount, reason, CallId, Invoice_NO);


            }
        });


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
                res = ut.OpenConnection(url, CRM_Collection_List.this);
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

                Toast.makeText(CRM_Collection_List.this, "Data not Available", Toast.LENGTH_SHORT).show();
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
                len_show_calldetails.setVisibility(View.VISIBLE);
                for (int i = 0; i < showContactArrayList.size(); i++) {
                    addView_showcontact(i);


                }
            }
        }


    }

    private void addView_showcontact(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CRM_Collection_List.this
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
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
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
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("name",showContactArrayList.get(position1).getContactName());
                intent.putExtra("designation",showContactArrayList.get(position1).getDesignation());
                intent.putExtra("dept",showContactArrayList.get(position1).getContactPersonDept());
                intent.putExtra("contact",showContactArrayList.get(position1).getMobile());
                intent.putExtra("email",showContactArrayList.get(position1).getEmailId());
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
            if (Call_CallType.equals("2")){
                url = CompanyURL + WebUrlClass.api_POSTdeleteCustContact;
            }else {
                url = CompanyURL + WebUrlClass.api_POSTdeleteContact;
            }
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0],CRM_Collection_List.this);

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

            Toast.makeText(CRM_Collection_List.this, "Contact deleted successfully", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

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
                res = ut.OpenConnection(url, CRM_Collection_List.this);

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
                            chatRoomDisplayArrayList.add(chatUser);

                        }

                        chatRoomMultipleAdapter=new ChatRoomMultipleAdapter(CRM_Collection_List.this,chatRoomDisplayArrayList);
                        listview_multiple_group.setAdapter(chatRoomMultipleAdapter);
                        chatRoomMultipleAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        }

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
                res = ut.OpenConnection(url, CRM_Collection_List.this);
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
            attachmentDetailsAdapter = new CRMAttachmentDetailsAdapter(CRM_Collection_List.this, attachmentList , true);
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


    class DownloadCustomerIdData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           dialog_loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_getCustCon + "?CallId=" +
                        URLEncoder.encode(CallId, "UTF-8");

                System.out.println("Provisional List Data :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    //JSONArray jResults = new JSONArray(response);

                    JSONObject jsoncustomer_id = null;
                    try {
                        jsoncustomer_id = new JSONObject(response);
                        FKCustomerId = jsoncustomer_id.getString("FKCustomerId");

                        FKConsigneeId = jsoncustomer_id.getString("FKConsigneeId");
                        Invoice_No = jsoncustomer_id.getString("InvoiceNo");


                    } catch (JSONException e1) {
                        e1.printStackTrace();
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


            dialog_loading.setVisibility(View.GONE);


        }
    }

    class DownloadProvisionalListData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
/*            progressDialog = new ProgressDialog(ProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_GetLoadProvisionalList + "?CallId=" +
                        URLEncoder.encode(CallId, "UTF-8");

                System.out.println("Provisional CallList :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_PROVISINALLIST, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROVISINALLIST, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_PROVISINALLIST, null, values);

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
            dialog_loading.setVisibility(View.GONE);
            UpdateProvisionalListdata();
            //     progressDialog.dismiss();


        }

    }

    private void UpdateProvisionalListdata() {
        provisionalDataArrayList.clear();
        String query = "SELECT  InstrumentNo,BankName," +
                "DepositedDate,Amount,DepositedBank,Narration,TDSAmount" +
                " FROM " + db.TABLE_PROVISINALLIST + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ProvisionalData provisionalData = new ProvisionalData();

                provisionalData.setInstrumentNo(cur.getString(cur.getColumnIndex("InstrumentNo")));
                provisionalData.setBankName(cur.getString(cur.getColumnIndex("BankName")));
                provisionalData.setDepositedDt(cur.getString(cur.getColumnIndex("DepositedDate")));
                provisionalData.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                provisionalData.setPaymentDepBank(cur.getString(cur.getColumnIndex("DepositedBank")));
                provisionalData.setNarration(cur.getString(cur.getColumnIndex("Narration")));
                provisionalData.setTDSAmount(cur.getString(cur.getColumnIndex("TDSAmount")));
                provisionalDataArrayList.add(provisionalData);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (provisionalDataArrayList.size() > 0) {
                lsCall_list.setVisibility(View.VISIBLE);
                for (int i = 0; i < provisionalDataArrayList.size(); i++) {
                    addView_Provisional(i);
                }
            }
        }

    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((edt_amount.getText().toString().equalsIgnoreCase("") ||
                edt_amount.getText().toString().equalsIgnoreCase(" ") ||
                edt_amount.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(CRM_Collection_List.this, "Amount is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_instrument_no.getText().toString().equalsIgnoreCase("") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(" ") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(CRM_Collection_List.this, "Instrument No is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(CRM_Collection_List.this, "Bank name is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(" ") ||
                edt_date.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(CRM_Collection_List.this, "Date is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(" ") ||
                edt_date.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(CRM_Collection_List.this, "Date required", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    class PostSaveReciptJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveCollectionReceipt;
            System.out.println("BusinessAPIURL-1 :" + ProvisionalRecieptjson);

            try {
                res = ut.OpenPostConnection(url, params[0],CRM_Collection_List.this);
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



            Toast.makeText(CRM_Collection_List.this, "Receipt save successfully", Toast.LENGTH_LONG).show();


        }

    }

    private void addView_Provisional(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CRM_Collection_List.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_provisional_list, null);

        TextView txt_provisional_name = (TextView) convertView.findViewById(R.id.txt_provisional_name);
        String outputDateStr = null;

        Invoice_No = provisionalDataArrayList.get(i).getInvoiceNo();
        BankName = provisionalDataArrayList.get(i).getBankName();
        Amount = provisionalDataArrayList.get(i).getAmount();
        PaymentDepBank = provisionalDataArrayList.get(i).getPaymentDepBank();
        Narration = provisionalDataArrayList.get(i).getNarration();
        TDSAmount = provisionalDataArrayList.get(i).getTDSAmount();
        InstrumentNo = provisionalDataArrayList.get(i).getInstrumentNo();

        String amount = Amount.split("\\.", 2)[0];
        String tdsamount = TDSAmount.split("\\.", 2)[0];


        DepositedDt = provisionalDataArrayList.get(i).getDepositedDt();


        if (TDSAmount.equals("0.0")) {
            txt_provisional_name.setText("Instrument No : " + InstrumentNo + " drawn on " + BankName + " bank dated " + DepositedDt + " for amount " + amount + " is deposited in " + PaymentDepBank + " bank." + Narration);

        } else {
            txt_provisional_name.setText("Instrument No : " + InstrumentNo + " drawn on " + BankName + " bank dated " + DepositedDt + " for amount " + amount + " and TDS " + tdsamount + " is deposited in " + PaymentDepBank + " bank." + Narration);
        }


        lsCall_list.addView(convertView);
    }

    class DownloadBanknameData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog_loading.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetBankname;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_BANKNAME, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BANKNAME, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_BANKNAME, null, values);

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

            dialog_loading.setVisibility(View.GONE);


            if (response.contains("")) {

            }
            getBankname();
        }

    }

    private void getBankname() {
        listBanknamedata.clear();
        String query = "SELECT distinct BankMasterId,BankName" +
                " FROM " + db.TABLE_BANKNAME;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                listBanknamedata.add(cur.getString(cur.getColumnIndex("BankName")));

            } while (cur.moveToNext());

        }

       CreateOpportunityActivity.MySpinnerAdapter customDept = new CreateOpportunityActivity.MySpinnerAdapter(CRM_Collection_List.this,
                R.layout.crm_custom_spinner_txt, listBanknamedata);
        spinner_bankname.setAdapter(customDept);
        spinner_bankname.setSelection(0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Followup && resultCode == Followup) {
            if (isProvision == true) {
                String BankName = data.getStringExtra("Name");
                BankMasterId= data.getStringExtra("ID");

                spinner_bankname.setText(BankName);
            }


        }
    }

    // Discount Voucher


     private void getapproverdialog(final String reversal_amount, final String reason, final String call_id, final String invoice_no) {

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_approver_lay, null, false);
        builder = new AlertDialog.Builder(CRM_Collection_List.this);
        builder.setView(v);
        alertDialog1 = builder.create();


        spinner_approver = (Spinner) v.findViewById(R.id.spinner_approver);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);




         if (isnet()) {
             dialog_loading.setVisibility(View.VISIBLE);
             new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                 @Override
                 public void callMethod() {
                     new DownloadApproverDetailsData().execute();
                 }

                 @Override
                 public void callfailMethod(String msg) {

                 }
             });
         }


         spinner_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                approverDatas.size();
                ApprId = approverDatas.get(position).getUserid();


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isnet()) {
                    dialog_loading.setVisibility(View.VISIBLE);
                    new StartSession(CRM_Collection_List.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(ApprId, call_id, invoice_no, reversal_amount, reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(CRM_Collection_List.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });

        alertDialog1 = builder.create();
        alertDialog1.setCancelable(false);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog1.dismiss();
            }
        });


    }

    class GetDiscountVoucher extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetSaveDiscount + "?CallId=" + CallId + "&InvoiceNo=" + Invoice_NO + "&ReversalAmount=" + URLEncoder.encode(Reversal_amount, "UTF-8") + "&Reason=" + URLEncoder.encode(reason, "UTF-8") + "&ApprId=" + ApprId + "&Type=R";  //ApprId


                try {
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);

                        System.out.println("BusinessAPI-1 :" + response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dialog_loading.setVisibility(View.GONE);

            if (response.equalsIgnoreCase("success")) {
                Toast.makeText(CRM_Collection_List.this, "Discount Voucher is Successfully sent for Approval", Toast.LENGTH_LONG).show();
                alertDialog1.dismiss();
            } else {
                Toast.makeText(CRM_Collection_List.this, integer, Toast.LENGTH_LONG).show();

            }

        }

    }

    class DownloadApproverDetailsData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_FillApprover;
                res = ut.OpenConnection(url);
                System.out.println("AdvanceProList :" + url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dialog_loading.setVisibility(View.GONE);
            Approverlist1 = new ArrayList<>();
            approverDatas = new ArrayList<>();

            try {
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jsonapproverlistdata = jResults.getJSONObject(i);

                    ApproverData approverData = new ApproverData();
                    approverData.setUserid(jsonapproverlistdata.getString("UserMasterId"));
                    approverData.setUsername(jsonapproverlistdata.getString("UserName"));
                    approverDatas.add(approverData);
                    String point = jsonapproverlistdata.getString("UserName");
                    Approverlist1.add(point);


                }
                spinner_approver.setAdapter(new ArrayAdapter<String>(CRM_Collection_List.this,
                        R.layout.crm_custom_spinner_txt,
                        Approverlist1));
            } catch (Exception e) {
                e.printStackTrace();
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
                Toast.makeText(CRM_Collection_List.this, "Call history not found", Toast.LENGTH_SHORT).show();


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

            callHistoryAdapter = new CallHistoryAdapter(CRM_Collection_List.this, callHistoryArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            callhistory_listview.setLayoutManager(mLayoutManager);
            callhistory_listview.setItemAnimator(new DefaultItemAnimator());
            callhistory_listview.setAdapter(callHistoryAdapter);

        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        String OppUpdate=userpreferences.getString("UpdateOpp","");

        if (OppUpdate.equalsIgnoreCase("1")){
            linear_contact.setVisibility(View.GONE);
            linear_chat.setVisibility(View.GONE);
            ls_attachname.setVisibility(View.GONE);
            len_pro.setVisibility(View.GONE);
            len_discount.setVisibility(View.GONE);
            callhistory_listview.setVisibility(View.GONE);
            if (ut.isNet(CRM_Collection_List.this)) {
                dialog_loading.setVisibility(View.VISIBLE);
                new StartSession(CRM_Collection_List.this, new CallbackInterface() {
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
    }
}
