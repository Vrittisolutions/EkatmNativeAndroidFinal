package com.vritti.crmlib.vcrm7;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.FileUtilities;
import com.vritti.crmlib.services.SendOfflineData;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class OpportunityUpdateActivity extends AppCompatActivity {
    LinearLayout lay_AssigntoBOE_SE, lay_Reason, lay_Approver, lay_Details, lay_Callagain, lay_Notes,
            lay_Mode, lay_InstrumentNo, lay_BankName, lay_Branch, lay_ChqAmount, lay_TDSAmount,
            lay_DiffAmount, lay_ReasonED, lay_PBT, lay_PTA, lay_Networth, lay_Creditrate,
            lay_PresentBorrowing, lay_currency, lay_rs, lay_Managementcomment, lay_DemoComplete,
            lay_Demo, lay_Date_time_custom, lay_ProductForBank, lay_cospreferred,
            lay_PrepaymentSecuterization, lay_ParticipateInsyndication, lay_Receiveddate,
            lay_PONo, lay_POvalue, lay_Ordertype, lay_Contractreviewrequest, lay_CustomerBudgetSanction,
            lay_CustomerBudget, lay_QuotationValue, lay_QuotationDocument, lay_Quotationno,
            lay_ReassigntoBOE, lay_PresaleSE, lay_SEName, lay_whowillvisit, lay_Whendoyoucall,
            lay_Receivedby, laytime1, laytime2, layoutfooter, layhead, lay_gstn;
    public static Spinner spinner_Natureofcall, spinner_Initiatedby, spinner_With_Towhom,
            spinner_Followupreason, spinner_Outcome, spinner_Nextaction, spinner_AssigntoBOE_SE,
            spinner_Reason, spinner_demo, spinner_Approver, spinner_currency,
            spinner_Ordertype, spinner_Receivedby, spinner_whowillvisit, spinner_PresaleSE,
            spinner_ReassigntoBOE, spinner_SEName, spinner_From, spinner_To, spinner_time,
            spinner_Mode, spinner_time_custom, spinner_Callagain, spinner_rs,
            spinner_CustomerBudgetSanction;
    Button btngetordertype, buttonminusca, buttonplusca, buttonminusta, buttonplusta, buttonminusda,
            buttonplusda, buttonplusnw, buttonminusnw, buttonpluspta, buttonminuspta, buttonpluspbt,
            buttonminuspbt, buttonSave_opportunity, buttonClose_opportunity, buttonminuspb, buttonpluspb,
            buttonminuscb, buttonpluscb, buttonminusqv, buttonplusqv, btnclear, btnclear1, btnplay, btnpause;
    public static TextView txtwhenyoucall, txtca, txtta, txtda, txtnw, txtpta, txtpbt, txtpb, txtcb, txtqv, txt_lbl_ordervalue;
    public static EditText editTextFollowupDate, EdttxtHours, editTextDetails, editTextWhendoyoucall,
            editTextDate, editTextDate_custom, editTextReceiveddate, editTextNotes, editTextInstrumentNo,
            editTextBankName, editTextBranch, editTextReason, editTextCreditrate,
            editTextManagementcomment, editTextProductForBank, editTextcospreferred,
            editTextPrepaymentSecuterization, editTextParticipateInsyndication, editTextPONo,
            editTextPOvalue, editTextQuotationno, editTextQuotationDocument, EdttxtNotes, edtfrom, edtto, editTextGSTINo;
    CheckBox checkBoxDemoComplete, checkBoxContractreviewrequest;
    public static String   ProspectId, ProductId = "";
    int minteger_ca = 0, minteger_ta = 0, minteger_da = 0, minteger_nw = 0, minteger_pta = 0,
            minteger_pbt = 0, minteger_pb = 0, minteger_cb = 0, minteger_qv = 0;
    public static String NextActionid, Natureofcallid, initiatedbyid, followupwithid, followupreasonid,
            outcomeid, reasonid, approverid = "", currencyid, ordertypeid, demoresId, chk_Contractreviewrequest,
            AssignToSEId, receivedbyid, whowillvisitid, presaleseid, reassignboeid, senameid, selected_mode,
            selected_budget, selected_rs, selected_call_again, selected_custom_time, selected_time,
            chk_DemoComplete, finaljson = "", UserType, date = null, today, CurrentDate,
            selected_from_time, selected_to_time, filePath, currentTime, callid, calltype, firmname,
            selected_outcome = "", selected_outcome_code = "", getoutcome, Status, GSTNINNO, Telestarttime, Teleendtime, Flag_is_tele = "", Teleinitiatedby, TeleCallNature = "";
    static String isapprover = "";
    SharedPreferences userpreferences;


    public  static  String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;

    SQLiteDatabase sql;
    public static int a = 1, millis = 1000;
    static List reason, natureofcall, initiatedby, whomwith, followupreason, category, outcome, nextaction,
            approver, currency, ordertype, TMESName;
    static int year, month, day;
    SimpleDateFormat dfDate;
    /*    progressHUD1, progressHUD2, progressHUD3, progressHUD4, progressHUD5,
        progressHUD6, progressHUD7, progressHUD8, progressHUD9,
        progressHUD10, progressHUD11;*/
    static Uri attachment;
    public static Context context;
    DateFormat dateFormat;
    Calendar cl;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String table, tablename = "", tablename_partial = "", status;

    private long mLastClickTime = 0;
    ProgressDialog progressDialog;
    ProgressBar progressbar;

    static boolean flagIsEnterprise = false;
    static int positionReceivedby = 0;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_opportunity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = OpportunityUpdateActivity.this;

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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserType = userpreferences.getString("UserType", null);

        init_layout();
        init_spinner();
        init();
        dateFormat = new SimpleDateFormat("HH:mm");
        cl = Calendar.getInstance();
        currentTime = dateFormat.format(cl.getTime());
        dfDate = new SimpleDateFormat("dd/MM/yyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //calendar.add(Calendar.MONTH, +6);
        Date newDate = calendar.getTime();
        String whendoucall = dfDate.format(newDate);
        simpleDateFormat = new SimpleDateFormat("hh:mm aa");

        Intent intent = getIntent();
        if (intent.hasExtra("nature")) {
            Flag_is_tele = intent.getStringExtra("nature");
            Telestarttime = intent.getStringExtra("starttime");
            Teleendtime = intent.getStringExtra("endtime");
            Teleinitiatedby = intent.getStringExtra("initiatedby");
            TeleCallNature = intent.getStringExtra("Callnature");

        } else {
            Flag_is_tele = "";
        }
        callid = intent.getStringExtra("callid");
        calltype = intent.getStringExtra("calltype");
        firmname = intent.getStringExtra("firmname");
        table = intent.getStringExtra("table");
        ProspectId = intent.getStringExtra("ProspectId");

        if (table.equalsIgnoreCase("Call")) {
            tablename = db.TABLE_CRM_CALL;
            tablename_partial = db.TABLE_CRM_CALL_PARTIAL;
        } else if (table.equalsIgnoreCase("Opportunity")) {
            tablename = db.TABLE_CRM_CALL_OPP;
            tablename_partial = db.TABLE_CRM_CALL_PARTIAL_OPP;
        }
        if (calltype.equalsIgnoreCase("1")) {
            getoutcome = "Sales";
        } else if (calltype.equalsIgnoreCase("2")) {
            getoutcome = "Collection";
        } else if (calltype.equalsIgnoreCase("3")) {
            getoutcome = "Feedback";
            layhead.setVisibility(View.GONE);
        }


        CurrentDate = dfDate.format(new Date());

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
            laytime1.setVisibility(View.GONE);
        }
        edtfrom.setText(currentTime);
        editTextFollowupDate.setText(CurrentDate);


        editTextDate.setText(whendoucall);
        editTextDate_custom.setText(CurrentDate);
        editTextReceiveddate.setText(CurrentDate);


        sql = db.getWritableDatabase();
        getData();
        setListrner();
        if (cf.getNatureOfcallcount() > 0) {
            getNextAction();
            getNatureofCall();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
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
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
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
        if (cf.getwhomwithcount() > 0) {
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
        }

        if (cf.getFollowupreasoncount() > 0) {
            getFollowupreason();
        } else {
            if (isnet()) {
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
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
        }

        if (cf.getReasonMastercount() > 0) {
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
        }

        if (cf.getCategorycount() > 0) {
            getCategory();
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
            getCategory();
        }

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


        if (Flag_is_tele.equalsIgnoreCase("Telephone")) {
            edtfrom.setText(Telestarttime);
            edtto.setText(Teleendtime);
            String count = calculate_time_diff(Telestarttime, Teleendtime);
            Log.d("crm_dialog_action", "count" + count);
            EdttxtHours.setText(count);
            getWhomwith();
            getInitiatedby();
            getNatureofCall();
            int pos = 0;
            try {
                pos = natureofcall.indexOf("Telephone");
            } catch (Exception e) {
                e.printStackTrace();
                pos = 0;
            }
            spinner_Natureofcall.setSelection(pos);
            int pos1 = 0;
            try {
                pos1 = initiatedby.indexOf(Teleinitiatedby);
            } catch (Exception e) {
                e.printStackTrace();
                pos1 = 0;
            }

            spinner_Initiatedby.setSelection(pos1);
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
        if (requestCode == 1
                && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {


                attachment = data.getData();
                filePath = FileUtilities.getPath(OpportunityUpdateActivity.this, attachment);
                editTextQuotationDocument.setText(filePath);

            } else {
                Toast.makeText(OpportunityUpdateActivity.this,
                        "Nothing Selected.", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_CANCELED) {
            attachment = null;
            Toast.makeText(OpportunityUpdateActivity.this, "Cancelled!",
                    Toast.LENGTH_LONG).show();
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

    private void getNextAction() {

        nextaction = new ArrayList();
        String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                " FROM " + db.TABLE_NatureofCall;
        Cursor cur = sql.rawQuery(query, null);
        // nextaction.add("Select");

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                nextaction.add(cur.getString(cur.getColumnIndex("NatureofCall")));

            } while (cur.moveToNext());

        }
        Collections.sort(nextaction, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OpportunityUpdateActivity.this,
                android.R.layout.simple_spinner_item, nextaction);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Nextaction.setAdapter(dataAdapter);


    }

    private void getNatureofCall() {
        SQLiteDatabase sql = db.getWritableDatabase();
        spinner_Natureofcall = (Spinner) findViewById(R.id.spinner_Natureofcall);
        natureofcall = new ArrayList();
        String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                " FROM " + db.TABLE_NatureofCall;
        Cursor cur = sql.rawQuery(query, null);
        // natureofcall.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                natureofcall.add(cur.getString(cur.getColumnIndex("NatureofCall")));

            } while (cur.moveToNext());

        }
        Collections.sort(natureofcall, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, natureofcall);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Natureofcall.setAdapter(dataAdapter);


    }

    private void getInitiatedby() {
        SQLiteDatabase sql = db.getWritableDatabase();
        spinner_Initiatedby = (Spinner) findViewById(R.id.spinner_Initiatedby);


        initiatedby = new ArrayList();
        String query = "SELECT distinct PKInitiatedBy,InitiatedBy" +
                " FROM " + db.TABLE_InitiatedBy;
        Cursor cur = sql.rawQuery(query, null);
        // initiatedby.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                initiatedby.add(cur.getString(cur.getColumnIndex("InitiatedBy")));

            } while (cur.moveToNext());

        }
        Collections.sort(initiatedby, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, initiatedby);
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
                whomwith.add(cur.getString(cur.getColumnIndex("ContactName")));
            } while (cur.moveToNext());
        }
        // Collections.sort(whomwith, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, whomwith);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_With_Towhom.setAdapter(dataAdapter);
        if (isnet()) {
            new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
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
        // followupreason.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                followupreason.add(cur.getString(cur.getColumnIndex("CallPurposeDesc")));

            } while (cur.moveToNext());

        }
        Collections.sort(followupreason, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, followupreason);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Followupreason.setAdapter(dataAdapter);

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

                outcome.add(cur.getString(cur.getColumnIndex("Outcome")));

            } while (cur.moveToNext());

        }
        Collections.sort(outcome, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, outcome);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Outcome.setAdapter(dataAdapter);

        if (cf.getReasonMastercount() > 0) {

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
        }
        if (cf.getCategorycount() > 0) {
            getCategory();
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

                reason.add(cur.getString(cur.getColumnIndex("ReasonDescription")));

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, reason);
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

                category.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }
        Collections.sort(category, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, category);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_AssigntoBOE_SE.setAdapter(dataAdapter);
        spinner_demo.setAdapter(dataAdapter);

    }

    private void getApprover() {

        approver = new ArrayList();
        String query = "SELECT distinct UserName,UserMasterID" +
                " FROM " + db.TABLE_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        //  approver.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                approver.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }
        Collections.sort(approver, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, approver);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Approver.setAdapter(dataAdapter);
    }

    private void getCurrency() {
        currency = new ArrayList();
        String query = "SELECT distinct CurrDesc,CurrencyMasterId" +
                " FROM " + db.TABLE_CurrencyMaster;
        Cursor cur = sql.rawQuery(query, null);
        //currency.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                currency.add(cur.getString(cur.getColumnIndex("CurrDesc")));

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, currency);
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
                ordertype.add(cur.getString(cur.getColumnIndex("Description")));

            } while (cur.moveToNext());

        }
        Collections.sort(ordertype, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, ordertype);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Ordertype.setAdapter(dataAdapter);


    }

    private void getTMESName() {

        TMESName = new ArrayList();
        TMESName.clear();
        String query = "SELECT distinct EkatmUserMasterId,UserName,ReportingName,ReportingToEmail" +
                " FROM " + db.TABLE_TMESEName;
        Cursor cur = sql.rawQuery(query, null);
        // TMESName.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                TMESName.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }
        Collections.sort(TMESName, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, TMESName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Receivedby.setAdapter(dataAdapter);
        try {
            positionReceivedby = TMESName.indexOf(UserName);
        } catch (Exception e) {
            e.printStackTrace();
            positionReceivedby = 0;
        }
        spinner_whowillvisit.setAdapter(dataAdapter);
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

    public static boolean validate_Header() {
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

    public static boolean validate_callagain() {
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

    public static boolean validate_appointments() {
        // TODO Auto-generated method stub

        if ((AssignToSEId.equalsIgnoreCase("") ||
                AssignToSEId.equalsIgnoreCase(" ") ||
                AssignToSEId.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Assign to BOE/SE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_AssigntoBOE_SE.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_AssigntoBOE_SE.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_AssigntoBOE_SE.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            // Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_ttboe() {
        // TODO Auto-generated method stub

        if ((reassignboeid.equalsIgnoreCase("") ||
                reassignboeid.equalsIgnoreCase(" ") ||
                reassignboeid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reassign to BOE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_ReassigntoBOE.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_ReassigntoBOE.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_ReassigntoBOE.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_orderrec() {
        // TODO Auto-generated method stub

        if ((receivedbyid.equalsIgnoreCase("") ||
                receivedbyid.equalsIgnoreCase(" ") ||
                receivedbyid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Received by", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Receivedby.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Receivedby.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Receivedby.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_orderlost() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if (((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) && (isapprover.equalsIgnoreCase("Y"))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_visit() {
        // TODO Auto-generated method stub

        if ((whowillvisitid.equalsIgnoreCase("") ||
                whowillvisitid.equalsIgnoreCase(" ") ||
                whowillvisitid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select who will visit", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_whowillvisit.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_whowillvisit.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_whowillvisit.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_reschedule() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_ttse() {
        // TODO Auto-generated method stub
        if ((senameid.equalsIgnoreCase("") ||
                senameid.equalsIgnoreCase(" ") ||
                senameid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select SEName", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_SEName.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_SEName.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_SEName.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_ccwo() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if (((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) && (isapprover.equalsIgnoreCase("Y"))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_disput() {
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

    public static boolean validate_collection() {
        // TODO Auto-generated method stub

        if ((spinner_Mode.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Mode.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Mode.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_wcf() {
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

    public static boolean validate_cpu() {
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
        } else if ((spinner_currency.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_currency.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_currency.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select currency", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextManagementcomment.getText().toString().equalsIgnoreCase("") ||
                editTextManagementcomment.getText().toString().equalsIgnoreCase(" ") ||
                editTextManagementcomment.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Management comment", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_rs.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_rs.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_rs.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Currency value", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_iu() {
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

    public static boolean validate_dr() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((demoresId.equalsIgnoreCase("") ||
                demoresId.equalsIgnoreCase(" ") ||
                demoresId.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Who will give demo?", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_demo.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_demo.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_demo.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((editTextDate_custom.getText().toString().equalsIgnoreCase("") ||
                editTextDate_custom.getText().toString().equalsIgnoreCase(" ") ||
                editTextDate_custom.getText().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_dc() {
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

    public static boolean validate_cwc() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else {
            return true;
        }
    }

    public static boolean validate_orderreg() {
        // TODO Auto-generated method stub

        if ((reasonid.equalsIgnoreCase("") ||
                reasonid.equalsIgnoreCase(" ") ||
                reasonid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Enter Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Reason.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Reason.getSelectedItem().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");

            return false;
        } else if ((approverid.equalsIgnoreCase("") ||
                approverid.equalsIgnoreCase(" ") ||
                approverid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Approver", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_Approver.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_Approver.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_ps() {
        // TODO Auto-generated method stub

        if ((presaleseid.equalsIgnoreCase("") ||
                presaleseid.equalsIgnoreCase(" ") ||
                presaleseid.equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select Presale SE", Toast.LENGTH_LONG).show();
            return false;
        } else if ((spinner_PresaleSE.getSelectedItem().toString().equalsIgnoreCase("Select") ||
                spinner_PresaleSE.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                spinner_PresaleSE.getSelectedItem().toString().equalsIgnoreCase(null))) {
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

    public static boolean validate_qs() {
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

    public static boolean validate_pdc() {
        // TODO Auto-generated method stub

        if ((editTextWhendoyoucall.getText().toString().equalsIgnoreCase("") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(" ") ||
                editTextWhendoyoucall.getText().toString().equalsIgnoreCase(null))) {
            //  edt_inwarddate.setError("Fill Inward date");
            Toast.makeText(context, "Select When do you call", Toast.LENGTH_LONG).show();
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
        EdttxtHours.setText("0");
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_From.setSelection(0);
                spinner_To.setSelection(0);
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
        editTextQuotationDocument.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showFileChooser();
                return true;
            }
        });

        buttonClose_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObj();
                if (Flag_is_tele.equalsIgnoreCase("")) {
                    OpportunityUpdateActivity.this.finish();
                }
            }
        });

        buttonSave_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((ProductId.equalsIgnoreCase("") ||
                        ProductId.equalsIgnoreCase(" ") ||
                        ProductId.equalsIgnoreCase(null)
                        || ProductId.equalsIgnoreCase("null"))) {
                    ProductId = "";

                }
                if (validate_Header() == true) {
                    if (SystemClock.elapsedRealtime() - mLastClickTime < 30000) {

                        Toast.makeText(getApplicationContext(), "You can click only after 30 sec from your first click", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CallId", callid);
                        jsonObject.put("ProspectId", ProspectId);
                        jsonObject.put("CallType", calltype);
                        jsonObject.put("ProductId", ProductId);
                        jsonObject.put("FollowupDate", editTextFollowupDate.getText().toString());
                        jsonObject.put("FollowupHours", EdttxtHours.getText().toString());
                        jsonObject.put("FollowupReason", followupreasonid);
                        jsonObject.put("FollowupWith", followupwithid);
                        if ((((String) spinner_Natureofcall.getSelectedItem()).equalsIgnoreCase("Select"))
                                || ((String) spinner_Natureofcall.getSelectedItem()).equalsIgnoreCase("")) {
                            jsonObject.put("NatureofCall", "");
                        } else {
                            jsonObject.put("NatureofCall", (((String) spinner_Natureofcall.getSelectedItem())));
                        }
                        if ((selected_from_time.equalsIgnoreCase("Select"))) {
                            jsonObject.put("FollowupFrom", "");

                        } else {
                            jsonObject.put("FollowupFrom", selected_from_time);
                        }
                        if ((selected_to_time.equalsIgnoreCase("Select"))
                                ) {
                            jsonObject.put("FollowupTo", "");

                        } else {
                            jsonObject.put("FollowupTo", selected_to_time);
                        }
                        if (((((String) spinner_Followupreason.getSelectedItem()).equalsIgnoreCase("Select")))
                                || (((String) spinner_Followupreason.getSelectedItem()).equalsIgnoreCase(""))) {
                            jsonObject.put("FollowupReasonName", "");

                        } else {
                            jsonObject.put("FollowupReasonName", ((String) spinner_Followupreason.getSelectedItem()));
                        }

                        if ((((String) spinner_Initiatedby.getSelectedItem()).equalsIgnoreCase("Select"))
                                || (((String) spinner_Initiatedby.getSelectedItem()).equalsIgnoreCase(""))) {
                            jsonObject.put("InitiatedBy", "");


                        } else {
                            jsonObject.put("InitiatedBy", spinner_Initiatedby.getSelectedItem().toString());

                        }
                        if ((spinner_Nextaction.getSelectedItem().toString().equalsIgnoreCase("Select"))
                                || (spinner_Nextaction.getSelectedItem().toString().equalsIgnoreCase(""))) {
                            jsonObject.put("NextAction", "");

                        } else {
                            jsonObject.put("NextAction", spinner_Nextaction.getSelectedItem().toString());
                        }
                        if (((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase("Select")
                                || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase("")
                                || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase(" ")
                                || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase(null)) {
                            jsonObject.put("FollowupWithName", "");
                        } else {
                            jsonObject.put("FollowupWithName", (String) spinner_With_Towhom.getSelectedItem());
                        }
                        if (selected_time.equalsIgnoreCase("Select")) {
                            jsonObject.put("NextActionDateTime", editTextDate.getText().toString()
                            );//+ "00:00"
                        } else {
                            jsonObject.put("NextActionDateTime", editTextDate.getText().toString()
                                    + "=" + selected_time);
                        }

                        jsonObject.put("Notes", EdttxtNotes.getText().toString());
                        jsonObject.put("Outcome", outcomeid);
                        jsonObject.put("TMEDisplayId", UserMasterId);
                        jsonObject.put("TMEDisplayName", UserName);
                        jsonObject.put("Firm", firmname);


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
                        } else if (selected_outcome_code.equalsIgnoreCase("ATS")) {
                            if (validate_appointments() == true) {
                                //Call Again
                                jsonObject.put("CallReason", "");
                                //Appointment
                                jsonObject.put("AssignToSEId", AssignToSEId);
                                jsonObject.put("AssignToSEName", (String) spinner_AssigntoBOE_SE.getSelectedItem());
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
                            } else {

                            }
                        } else if (selected_outcome_code.equalsIgnoreCase("CC") ||
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
                                jsonObject.put("CC_CallCloseReasonName", spinner_Reason.getSelectedItem().toString());
                                jsonObject.put("CC_CallCloseApproverId", approverid);
                                jsonObject.put("CC_CallCloseApproverName", spinner_Approver.getSelectedItem().toString());
                                jsonObject.put("CC_CallCloseDetails", editTextDetails.getText().toString());
                                jsonObject.put("CC_CallCloseCallCustAgain", selected_call_again);
                                jsonObject.put("CC_CallCloseWhenUCall", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("CC_CallCloseNotes", editTextNotes.getText().toString());
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
                                jsonObject.put("OL_OrderLostReasonName", (String) spinner_Reason.getSelectedItem());
                                jsonObject.put("OL_OrderLostApproverId", approverid);
                                jsonObject.put("OL_OrderLostApproverName", (String) spinner_Approver.getSelectedItem());
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
                                jsonObject.put("PS_PresaleSEName", spinner_PresaleSE.getSelectedItem().toString());
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
                                jsonObject.put("CPU_ddlCurrency", spinner_currency.getSelectedItem().toString());
                                jsonObject.put("CPU_txtMComments", editTextManagementcomment.getText().toString());
                                jsonObject.put("CPU_ddlCurrencyVal", spinner_rs.getSelectedItem().toString());
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
                                jsonObject.put("ReAssignToTMEName", (String) spinner_ReassigntoBOE.getSelectedItem());
                                jsonObject.put("CallReasonTTB", reasonid);
                                jsonObject.put("CallReasonTTBName", (String) spinner_Reason.getSelectedItem());
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
                                jsonObject.put("DRes_DemoResReasonName", spinner_Reason.getSelectedItem().toString());
                                jsonObject.put("DRes_DemoGivenById", demoresId);
                                jsonObject.put("DRes_DemoGivenByName", spinner_demo.getSelectedItem().toString());
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

                            String calldate = editTextDate.getText().toString();
                            Date initDate = null;
                            try {
                                initDate = new SimpleDateFormat("dd/MM/yyyy").parse(calldate);
                                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                                calldate = formatter.format(initDate);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            jsonObject.put("DReq_DemoDate", calldate);

                            DateFormat inputFormat = new SimpleDateFormat("hh:mm");
                            try {
                                Date date = inputFormat.parse(selected_custom_time);
                                selected_custom_time = simpleDateFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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
                                jsonObject.put("OR_OrderReceivedByName", spinner_Receivedby.getSelectedItem().toString());
                                jsonObject.put("OR_OrderReceivedDate", editTextReceiveddate.getText().toString());
                                jsonObject.put("OR_OrderPONo", "");
                                jsonObject.put("OR_OrderPOValue", editTextPOvalue.getText().toString());
                                jsonObject.put("OR_OrderContractReview", chk_Contractreviewrequest);
                                jsonObject.put("OR_OrderType", ordertypeid);
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
                                jsonObject.put("Res_RescheduleReasonName", spinner_Reason.getSelectedItem().toString());
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
                                jsonObject.put("RTS_TransferSEName", spinner_SEName.getSelectedItem().toString());
                                jsonObject.put("RTS_TransferReasonId", reasonid);
                                jsonObject.put("RTS_TransferReasonName", spinner_Reason.getSelectedItem().toString());
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
                                jsonObject.put("SV_VisitByName", spinner_whowillvisit.getSelectedItem().toString());
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
                                jsonObject.put("Collect_Mode", spinner_Mode.getSelectedItem().toString());
                                jsonObject.put("Collect_InstrNo", editTextInstrumentNo.getText().toString());
                                jsonObject.put("Collect_InstrDate", editTextWhendoyoucall.getText().toString());
                                jsonObject.put("Collect_BankName", editTextBankName.getText().toString());
                                jsonObject.put("Collect_BranchName", editTextBranch.getText().toString());
                                jsonObject.put("Collect_ChqAmount", txtca.getText().toString());
                                jsonObject.put("Collect_TDSAmount", txtta.getText().toString());
                                jsonObject.put("Collect_DiffAmount", txtda.getText().toString());
                                jsonObject.put("Collect_Reason", editTextReason.getText().toString());
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
                                jsonObject.put("PRMS_Reason", editTextReason.getText().toString());

                                finaljson = jsonObject.toString();
                                finaljson = finaljson.replaceAll("\\\\", "");
                                finaljson = finaljson.replaceAll(" ", " ");
                                finaljson = finaljson.replaceAll("=", " ");
                                UpdateOpportunity(finaljson);
                                Log.d("crm_dialog_action", "json" + finaljson);
                                buttonSave_opportunity.setClickable(false);
                            } else {

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
                                jsonObject.put("CustCall_ReasonName", spinner_Reason.getSelectedItem().toString());
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
                                jsonObject.put("OReg_OrderRegretReasonName", spinner_Reason.getSelectedItem().toString());
                                jsonObject.put("OReg_OrderRegretApproverId", approverid);
                                jsonObject.put("OReg_OrderRegretApproverName", spinner_Approver.getSelectedItem().toString());
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

                        } else {

                        }


                    } catch (JSONException e) {

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wait", Toast.LENGTH_LONG).show();
                }

            }
        });

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_time = (String) spinner_time.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_CustomerBudgetSanction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_budget = (String) spinner_CustomerBudgetSanction.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_rs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_rs = (String) spinner_rs.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Callagain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_call_again = (String) spinner_Callagain.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_time_custom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_custom_time = (String) spinner_time_custom.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_mode = (String) spinner_Mode.getSelectedItem();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_SEName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                senameid = "";
                String SE = (String) spinner_SEName.getSelectedItem();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2' AND UserName='" + SE + "'";

                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        senameid = cur.getString(cur.getColumnIndex("UserName"));

                    } while (cur.moveToNext());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_ReassigntoBOE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reassignboeid = "";
                String EOE = (String) spinner_ReassigntoBOE.getSelectedItem();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='1' AND UserName='" + EOE + "'";
                ;
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        reassignboeid = cur.getString(cur.getColumnIndex("UserMasterId"));

                    } while (cur.moveToNext());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_PresaleSE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presaleseid = "";
                String SE = (String) spinner_PresaleSE.getSelectedItem();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE CRMCategory='2' AND UserName='" + SE + "'";

                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        presaleseid = cur.getString(cur.getColumnIndex("UserMasterId"));

                    } while (cur.moveToNext());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_whowillvisit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                whowillvisitid = "";
                String visit = (String) spinner_whowillvisit.getSelectedItem();
                String query = "SELECT distinct EkatmUserMasterId,UserName,ReportingName,ReportingToEmail" +
                        " FROM " + db.TABLE_TMESEName + " WHERE UserName='" + visit + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        whowillvisitid = cur.getString(cur.getColumnIndex("EkatmUserMasterId"));


                    } while (cur.moveToNext());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Receivedby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receivedbyid = "";
                String TMES = (String) spinner_Receivedby.getSelectedItem();

                String query = "SELECT distinct EkatmUserMasterId,UserName,ReportingName,ReportingToEmail" +
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Ordertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordertypeid = "";
                String odertp = (String) spinner_Ordertype.getSelectedItem();
                if (cf.getOrdertypecount() > 0) {
                    String query = "SELECT distinct OrderTypeMasterId,Description " +
                            " FROM " + db.TABLE_OrderTypeMaster +
                            " WHERE Description='" + odertp + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        do {
                            ordertypeid = cur.getString(cur.getColumnIndex("OrderTypeMasterId"));


                        } while (cur.moveToNext());

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyid = "";
                String currency = (String) spinner_currency.getSelectedItem();
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
        spinner_Approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                approverid = "";
                String approver = (String) spinner_Approver.getSelectedItem();
                if (cf.getApprovercount() > 0) {
                    //getApprover();
                    String query = "SELECT distinct UserName,UserMasterID" +
                            " FROM " + db.TABLE_APPROVER +
                            " WHERE UserName='" + approver + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            approverid = cur.getString(cur.getColumnIndex("UserMasterID"));


                        } while (cur.moveToNext());

                    }
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
                    String query = "SELECT distinct UserName,UserMasterID" +
                            " FROM " + db.TABLE_APPROVER +
                            " WHERE UserName='" + approver + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            approverid = cur.getString(cur.getColumnIndex("UserMasterID"));


                        } while (cur.moveToNext());

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_demo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                demoresId = "";
                String demo = (String) spinner_demo.getSelectedItem();
                if (cf.getCategorycount() > 0) {
                    //   getCategory();
                    String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                            " FROM " + db.TABLE_Category + " WHERE CRMCategory='1' OR CRMCategory='2' AND " +
                            "UserName='" + demo + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            demoresId = cur.getString(cur.getColumnIndex("UserMasterId"));


                        } while (cur.moveToNext());

                    }

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
                    getCategory();
                    String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                            " FROM " + db.TABLE_Category + " WHERE CRMCategory='1' OR CRMCategory='2' AND " +
                            "UserName='" + demo + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            demoresId = cur.getString(cur.getColumnIndex("UserMasterId"));


                        } while (cur.moveToNext());

                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonid = "";
                String reson = (String) spinner_Reason.getSelectedItem();
                String query = "SELECT distinct ReasonDescription,PKReasonID" +
                        " FROM " + db.TABLE_REASON_Master
                        + " WHERE ReasonDescription='" + reson + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        reasonid = cur.getString(cur.getColumnIndex("PKReasonID"));


                    } while (cur.moveToNext());

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_AssigntoBOE_SE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AssignToSEId = "";
                String asb = (String) spinner_AssigntoBOE_SE.getSelectedItem();
                String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                        " FROM " + db.TABLE_Category + " WHERE " +
                        "UserName='" + asb + "'";//CRMCategory='1' OR CRMCategory='2' AND
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        AssignToSEId = cur.getString(cur.getColumnIndex("UserMasterId"));
                    } while (cur.moveToNext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Nextaction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NextActionid = "";
                String sp_na = (String) spinner_Nextaction.getSelectedItem();
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
        spinner_Followupreason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                followupreasonid = "";
                String freason = (String) spinner_Followupreason.getSelectedItem();
                String query = "SELECT distinct PKCallPurposeId,CallPurposeDesc" +
                        " FROM " + db.TABLE_Followup_reason +
                        " WHERE CallPurposeDesc='" + freason + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        followupreasonid = cur.getString(cur.getColumnIndex("PKCallPurposeId"));
                    } while (cur.moveToNext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_With_Towhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                followupwithid = "";
                String whom = (String) spinner_With_Towhom.getSelectedItem();
                String query = "SELECT distinct PKSuspContactDtlsID,ContactName" +
                        " FROM " + db.TABLE_With_whom + " WHERE ContactName='" + whom + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {
                        followupwithid = cur.getString(cur.getColumnIndex("PKSuspContactDtlsID"));


                    } while (cur.moveToNext());

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_Initiatedby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initiatedbyid = "";
                String initiatedby = (String) spinner_Initiatedby.getSelectedItem();
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
        spinner_Natureofcall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Natureofcallid = "";
                String natureofcall = (String) spinner_Natureofcall.getSelectedItem();
                if (cf.getNatureOfcallcount() > 0) {
                    String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                            " FROM " + db.TABLE_NatureofCall + " WHERE NatureofCall='" +
                            natureofcall + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        do {
                            Natureofcallid = cur.getString(cur.getColumnIndex("PKNatureofCall"));


                        } while (cur.moveToNext());

                    }
                } else {
                    if (isnet()) {
                        new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadNatureOfCallJSON().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    }
                    String query = "SELECT distinct PKNatureofCall,NatureofCall" +
                            " FROM " + db.TABLE_NatureofCall + " WHERE NatureofCall='" +
                            natureofcall + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();
                        do {
                            Natureofcallid = cur.getString(cur.getColumnIndex("PKNatureofCall"));
                        } while (cur.moveToNext());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity.this,
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
                                    Toast.makeText(OpportunityUpdateActivity.this,
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity.this,
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
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity.this,
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
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        editTextWhendoyoucall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextWhendoyoucall.setText(date);
                                } else {
                                    editTextWhendoyoucall.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });

        editTextFollowupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityUpdateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextFollowupDate.setText(date);
                                } else {
                                    editTextFollowupDate.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });


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
                        (OpportunityUpdateActivity.this, android.R.layout.simple_spinner_item, ordertype);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_Ordertype.setAdapter(dataAdapter);

            }
        });
        spinner_Outcome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_outcome = (String) spinner_Outcome.getSelectedItem();
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

                } else if (selected_outcome_code.equalsIgnoreCase("CC")
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
                    spinner_Callagain.setSelection(2);
                    editTextNotes.setText("");
                } else if (selected_outcome_code.equalsIgnoreCase("OL")) {
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

                                category.add(cur.getString(cur.getColumnIndex("UserName")));

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
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

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
                } else if (selected_outcome_code.equalsIgnoreCase("OR")) {
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
                    txt_lbl_ordervalue.setText("Order Value :");
                    if (Status.equalsIgnoreCase("false")) {

                        lay_gstn.setVisibility(View.VISIBLE);
                    }
                    lay_Ordertype.setVisibility(View.VISIBLE);
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


                    spinner_CustomerBudgetSanction.setSelection(0);
                    txtcb.setText("0");
                    buttonminusqv.setText("0");
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
                                category.add(cur.getString(cur.getColumnIndex("UserName")));

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
                } else if (selected_outcome_code.equalsIgnoreCase("COLLCT")) {
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
                    txtwhenyoucall.setText("Due date :");


                } else if (selected_outcome_code.equalsIgnoreCase("Disp")) {
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
                } else if (selected_outcome_code.equalsIgnoreCase("PRMS")) {
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
                    txtwhenyoucall.setText("Next Promise Date :");

                    editTextReason.setText("");
                }


                outcomeid = "";

                String query = "SELECT distinct Code,PKOutcomeId,Outcome" +
                        " FROM " + db.TABLE_Outcome + " WHERE Outcome='"
                        + spinner_Outcome.getSelectedItem().toString() + "'";
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
        });


    }

    private void getData() {

        String query = "SELECT ProspectId,ProductId FROM "
                + tablename + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);
        selected_from_time = spinner_From.getSelectedItem().toString();

        System.out.println("select_item :" + selected_from_time);

        spinner_From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_from_time = spinner_From.getSelectedItem().toString();

                System.out.println("select_item-1 :" + selected_from_time);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        selected_to_time = spinner_To.getSelectedItem().toString();

        System.out.println("Select To time :" + selected_to_time);

        spinner_To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String count = "";
                selected_to_time = spinner_To.getSelectedItem().toString();
                if (selected_from_time.equalsIgnoreCase("Select")) {
                 /*   Toast.makeText(OpportunityUpdateActivity.this,
                            "Select From time first",
                            Toast.LENGTH_SHORT).show();*/
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
        });

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            ProductId = cur.getString(cur.getColumnIndex("ProductId"));
            ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));
        } else {
            ProductId = "0";
            ProspectId = "0";
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
        layhead = (LinearLayout) findViewById(R.id.layhead);
        layoutfooter = (LinearLayout) findViewById(R.id.layoutfooter);
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
        txt_lbl_ordervalue = (TextView) findViewById(R.id.txt_lbl_ordervalue);
        lay_gstn = (LinearLayout) findViewById(R.id.lay_gstn);
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
    }

    private void init_spinner() {
        spinner_Natureofcall = (Spinner) findViewById(R.id.spinner_Natureofcall);
        spinner_Initiatedby = (Spinner) findViewById(R.id.spinner_Initiatedby);
        spinner_With_Towhom = (Spinner) findViewById(R.id.spinner_With_Towhom);
        spinner_Followupreason = (Spinner) findViewById(R.id.spinner_Followupreason);
        spinner_Outcome = (Spinner) findViewById(R.id.spinner_Outcome);
        spinner_AssigntoBOE_SE = (Spinner) findViewById(R.id.spinner_AssigntoBOE_SE);
        spinner_Nextaction = (Spinner) findViewById(R.id.spinner_Nextaction);
        spinner_Reason = (Spinner) findViewById(R.id.spinner_Reason);
        spinner_demo = (Spinner) findViewById(R.id.spinner_demo);
        spinner_Approver = (Spinner) findViewById(R.id.spinner_Approver);
        spinner_currency = (Spinner) findViewById(R.id.spinner_currency);
        spinner_Ordertype = (Spinner) findViewById(R.id.spinner_Ordertype);
        spinner_Receivedby = (Spinner) findViewById(R.id.spinner_Receivedby);
        spinner_whowillvisit = (Spinner) findViewById(R.id.spinner_whowillvisit);
        spinner_PresaleSE = (Spinner) findViewById(R.id.spinner_PresaleSE);
        spinner_ReassigntoBOE = (Spinner) findViewById(R.id.spinner_ReassigntoBOE);
        spinner_SEName = (Spinner) findViewById(R.id.spinner_SEName);
        spinner_From = (Spinner) findViewById(R.id.spinner_From);
        spinner_To = (Spinner) findViewById(R.id.spinner_To);
        spinner_Mode = (Spinner) findViewById(R.id.spinner_Mode);
        spinner_time_custom = (Spinner) findViewById(R.id.spinner_time_custom);
        spinner_Callagain = (Spinner) findViewById(R.id.spinner_Callagain);
        spinner_rs = (Spinner) findViewById(R.id.spinner_rs);
        spinner_CustomerBudgetSanction = (Spinner) findViewById(R.id.spinner_CustomerBudgetSanction);
        spinner_time = (Spinner) findViewById(R.id.spinner_time);

        spinner_Natureofcall.setSelection(0);
        spinner_Initiatedby.setSelection(0);
        spinner_With_Towhom.setSelection(0);
        spinner_Followupreason.setSelection(0);
        spinner_Outcome.setSelection(0);
        spinner_AssigntoBOE_SE.setSelection(0);
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
        spinner_SEName.setSelection(0);
        spinner_From.setSelection(0);
        spinner_To.setSelection(0);
        spinner_Mode.setSelection(0);
        spinner_time_custom.setSelection(0);
        spinner_Callagain.setSelection(2);
        spinner_rs.setSelection(0);
        spinner_CustomerBudgetSanction.setSelection(0);
        spinner_time.setSelection(0);
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_crm);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        btnpause = (Button) findViewById(R.id.btnpause);
        edtfrom = (EditText) findViewById(R.id.edtfrom);
        edtto = (EditText) findViewById(R.id.edtto);
        btnclear = (Button) findViewById(R.id.btnclear);
        btnclear1 = (Button) findViewById(R.id.btnclear1);
        btnplay = (Button) findViewById(R.id.btnplay);
        btngetordertype = (Button) findViewById(R.id.btngetordertype);
        txtwhenyoucall = (TextView) findViewById(R.id.txtwhenyoucall);
        editTextFollowupDate = (EditText) findViewById(R.id.editTextFollowupDate);
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
        buttonminusca = (Button) findViewById(R.id.buttonminusca);
        buttonplusca = (Button) findViewById(R.id.buttonplusca);
        txtca = (TextView) findViewById(R.id.txtca);
        buttonminusta = (Button) findViewById(R.id.buttonminusta);
        buttonplusta = (Button) findViewById(R.id.buttonplusta);
        buttonminusda = (Button) findViewById(R.id.buttonminusda);
        buttonplusda = (Button) findViewById(R.id.buttonplusda);
        txtta = (TextView) findViewById(R.id.txtta);
        txtda = (TextView) findViewById(R.id.txtda);
        buttonplusnw = (Button) findViewById(R.id.buttonplusnw);
        buttonminusnw = (Button) findViewById(R.id.buttonminusnw);
        buttonpluspta = (Button) findViewById(R.id.buttonpluspta);
        buttonminuspta = (Button) findViewById(R.id.buttonminuspta);
        buttonpluspbt = (Button) findViewById(R.id.buttonpluspbt);
        buttonminuspbt = (Button) findViewById(R.id.buttonminuspbt);
        txtnw = (TextView) findViewById(R.id.txtnw);
        txtpta = (TextView) findViewById(R.id.txtpta);
        txtpbt = (TextView) findViewById(R.id.txtpbt);
        checkBoxDemoComplete = (CheckBox) findViewById(R.id.checkBoxDemoComplete);
        checkBoxContractreviewrequest = (CheckBox) findViewById(R.id.checkBoxContractreviewrequest);
        buttonSave_opportunity = (Button) findViewById(R.id.buttonSave_opportunity);
        buttonClose_opportunity = (Button) findViewById(R.id.buttonClose_opportunity);
        EdttxtNotes = (EditText) findViewById(R.id.EdttxtNotes);
        buttonminuspb = (Button) findViewById(R.id.buttonminuspb);
        buttonpluspb = (Button) findViewById(R.id.buttonpluspb);
        txtpb = (TextView) findViewById(R.id.txtpb);
        buttonminuscb = (Button) findViewById(R.id.buttonminuscb);
        buttonpluscb = (Button) findViewById(R.id.buttonpluscb);
        buttonminusqv = (Button) findViewById(R.id.buttonminusqv);
        buttonplusqv = (Button) findViewById(R.id.buttonplusqv);
        txtcb = (TextView) findViewById(R.id.txtcb);
        txtqv = (TextView) findViewById(R.id.txtqv);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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
                progressDialog = new ProgressDialog(OpportunityUpdateActivity.this);
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
                        "?FKSuspectId=" + URLEncoder.encode(ProspectId, "UTF-8");

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
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                            if (columnName.equalsIgnoreCase("ContactName")) {
                                whomwith.add(jorder.getString("ContactName"));
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
            getApprover();


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
                res = ut.OpenPostConnection(url, params[0]);

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
                Toast.makeText(OpportunityUpdateActivity.this, "Opportunity updated successfully", Toast.LENGTH_LONG).show();

                // onBackPressed();
                Intent i = new Intent(OpportunityUpdateActivity.this, CallListActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(OpportunityUpdateActivity.this, "Opportunity not updated", Toast.LENGTH_LONG).show();
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
            OpportunityUpdateActivity.this.finish();
        }

    }


    @Override
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
                new StartSession(OpportunityUpdateActivity.this, new CallbackInterface() {
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
            buttonSave_opportunity.setClickable(true);
        } else {
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
            Intent intent1 = new Intent(OpportunityUpdateActivity.this,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            startService(intent1);

            sql.delete(tablename_partial, "CallId=?",
                    new String[]{callid});
            sql.delete(tablename, "CallId=?",
                    new String[]{callid});
            //OpportunityUpdateActivity.this.finish();
            Toast.makeText(OpportunityUpdateActivity.this, "Opportunity update record saved successfully", Toast.LENGTH_LONG).show();

            // onBackPressed();
            Intent i = new Intent(OpportunityUpdateActivity.this, CallListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved. Please contact administrator", Toast.LENGTH_LONG).show();
        }

    }

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

                if ((selected_from_time.equalsIgnoreCase("Select"))) {
                    jsonObject.put("FollowupFrom", "");

                } else {
                    jsonObject.put("FollowupFrom", selected_from_time);
                }
                if ((selected_to_time.equalsIgnoreCase("Select"))
                        ) {
                    jsonObject.put("FollowupTo", "");

                } else {
                    jsonObject.put("FollowupTo", selected_to_time);
                }
                if (((((String) spinner_Followupreason.getSelectedItem()).equalsIgnoreCase("Select")))
                        || (((String) spinner_Followupreason.getSelectedItem()).equalsIgnoreCase(""))) {
                    jsonObject.put("FollowupReasonName", "");

                } else {
                    jsonObject.put("FollowupReasonName", ((String) spinner_Followupreason.getSelectedItem()));
                }

                if ((((String) spinner_Initiatedby.getSelectedItem()).equalsIgnoreCase("Select"))
                        || (((String) spinner_Initiatedby.getSelectedItem()).equalsIgnoreCase(""))) {
                    jsonObject.put("InitiatedBy", "");


                } else {
                    jsonObject.put("InitiatedBy", spinner_Initiatedby.getSelectedItem().toString());

                }
                if ((spinner_Nextaction.getSelectedItem().toString().equalsIgnoreCase("Select"))
                        || (spinner_Nextaction.getSelectedItem().toString().equalsIgnoreCase(""))) {
                    jsonObject.put("NextAction", "");

                } else {
                    jsonObject.put("NextAction", spinner_Nextaction.getSelectedItem().toString());
                }
                if (((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase("Select")
                        || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase("")
                        || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase(" ")
                        || ((String) spinner_With_Towhom.getSelectedItem()).equalsIgnoreCase(null)) {
                    jsonObject.put("FollowupWithName", "");
                } else {
                    jsonObject.put("FollowupWithName", (String) spinner_With_Towhom.getSelectedItem());
                }
                if (selected_time.equalsIgnoreCase("Select")) {
                    jsonObject.put("NextActionDateTime", editTextDate.getText().toString()
                    );//+ "00:00"
                } else {
                    jsonObject.put("NextActionDateTime", editTextDate.getText().toString()
                            + "=" + selected_time);
                }
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
}