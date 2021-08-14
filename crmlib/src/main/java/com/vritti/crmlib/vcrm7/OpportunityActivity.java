package com.vritti.crmlib.vcrm7;

import android.app.DatePickerDialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ApproverData;
import com.vritti.crmlib.bean.PartialCallList;
import com.vritti.crmlib.chat.MultipleGroupActivity;
import com.vritti.crmlib.classes.CollectionCallCommonObjectProperties;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.CommonObjectProperties;
import com.vritti.crmlib.classes.FeedbackCommonObjectProperties;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class OpportunityActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    CommonObjectProperties commonObj;
    FeedbackCommonObjectProperties feedbackcommonObj;
    CollectionCallCommonObjectProperties collectioncallcommonObj;
    SimpleDateFormat dfDate, dftime;
    String FinalObj, Firmname;
    String obj;
    String Opportunity_type;
    SharedPreferences userpreferences;
    String  UserType;
    SQLiteDatabase sql;
    ProgressHUD progressHUD;
    ArrayList<PartialCallList> partialCallLists;
    LinearLayout lsCall_list, realcolors, laycall_type, len_collection;
    TextView txtfirmname, tvcall, txtcityname, txtactiondatetime, txtopportunity_type, tv_latestremark, txt_Save, txt_Close, txt_invoice_no, txt_amount;
    EditText edt_amount, edt_instrument_no, edt_bankname, edt_tds_amount, edt_narration, edt_date, edt_deposite_bank;
    ImageView img_date;
    TextView spinner_action;
    ProgressBar progressbar;
    JSONObject jsoncommonObj;
    JSONObject jsonObj;
    EditText editTextExpecteddate, expectedvalue, edt_search_firm;
    ImageView img_search_firm;
    int pos;
    private String ProvisionalRecieptjson;
    AlertDialog.Builder builder;
    AlertDialog alertDialog, alertDialog1;
    String callId, date;
    private String FKCustomerId, FKConsigneeId, InvoiceNo;
    Spinner spinner_provisional;
    AlertDialog dialog;
    String Reversal_amount = "", reason, Call_ID, Invoice_NO;
    public String IsChatApplicable;
    int check = 0;
    Spinner spinner_approver;
    ArrayList<ApproverData> approverDatas = new ArrayList<>();
    ArrayList<String> Approverlist1;
    private String ApprId;
    ProgressBar mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_opportunity);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);
        IsChatApplicable = userpreferences.getString("chatapplicable", "");

        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);

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
        sql = db.getWritableDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtopportunity_type = (TextView) findViewById(R.id.txtopportunity_type);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        edt_search_firm = (EditText) findViewById(R.id.edt_search_firm);
        img_search_firm = (ImageView) findViewById(R.id.img_search_firm);


        Intent intent = getIntent();
        partialCallLists = new ArrayList<PartialCallList>();
        Opportunity_type = intent.getStringExtra("Opportunity");
        // callId=intent.getStringExtra("callId");

        if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
            obj = getObj_OPP(Opportunity_type);
            //   txtopportunity_type.setText(Opportunity_type);

            if (isnet()) {
                new StartSession(OpportunityActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCollectionDataURLJSON().execute(obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        } else if (Opportunity_type.equalsIgnoreCase("feedback") || Opportunity_type.equalsIgnoreCase("overdue_feedback") || Opportunity_type.equalsIgnoreCase("today_overdue_feedback") || Opportunity_type.equalsIgnoreCase("tomorrow_overdue_feedback") || Opportunity_type.equalsIgnoreCase("week_overdue_feedback")) {
            obj = getObj_OPP(Opportunity_type);
            //   txtopportunity_type.setText(Opportunity_type);

            if (isnet()) {
                new StartSession(OpportunityActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadFeedbackCallDataURLJSON().execute(obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        } else {
            obj = getObj_OPP(Opportunity_type);
            //   txtopportunity_type.setText(Opportunity_type);

            if (isnet()) {
                new StartSession(OpportunityActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOppDataURLJSON().execute(obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }



       /* obj = getObj_OPP(Opportunity_type);
     //   txtopportunity_type.setText(Opportunity_type);

        if (isnet()) {
            new StartSession(OpportunityActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadOppDataURLJSON().execute(obj);
                }
            });
        }*/


        img_search_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                Firmname = edt_search_firm.getText().toString();
                if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                    UpdatList_CollectionOPPfilter(Firmname);
                } else if (Opportunity_type.equalsIgnoreCase("feedback") || Opportunity_type.equalsIgnoreCase("overdue_feedback") || Opportunity_type.equalsIgnoreCase("today_overdue_feedback") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                    UpdatList_FeedbackOPPfilter(Firmname);
                } else {
                    UpdatList_OPPfilter(Firmname);
                }


               /* String query = "SELECT distinct FirmName" +
                        " FROM " + db.TABLE_CRM_CALL_PARTIAL +
                        " WHERE  FirmName like '%"+Firmname+"%'";
                Cursor cur = sql.rawQuery(query, null);

                String count= String.valueOf(cur.getCount());
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Firmname = cur.getString(cur.getColumnIndex("FirmName"));
                        UpdatList_OPPfilter(Firmname);

                    } while (cur.moveToNext());

                } else {
                    Firmname = "";
                }



*/
            }
        });

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

    private String getObj_OPP(String Opportunity_type) {
        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        dftime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        commonObj = new CommonObjectProperties();
        jsoncommonObj = commonObj.DataObj();


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("IsPartial");
            jsonObj.put("IsSet", false);
            jsonObj.put("value1", "P");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Opportunity_type.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("new_opp")) {
            txtopportunity_type.setText("New Opportunity");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                /*calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                // calendar.add(Calendar.DAY_OF_YEAR);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("AddedDt");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("overdue_opp")) {
            txtopportunity_type.setText("Overdue Opportunity");

            try {
                String currentDateandTime = dftime.format(new Date());
                Date cdate = dftime.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                /*calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                // calendar.add(Calendar.DAY_OF_YEAR);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dftime.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("yesterday_opp")) {
            txtopportunity_type.setText("Yesterday Opportunity");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("today_opp")) {
            txtopportunity_type.setText("Today Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("callagain_opp")) {
            txtopportunity_type.setText("Call Again Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));


                jsonObj = jsoncommonObj.getJSONObject("OutcomeCode");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", "CA");
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("Tommorow_opp")) {
            txtopportunity_type.setText("Tomorrow Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("revived_opp")) {
            txtopportunity_type.setText("Revived Opportunity");
            try {
               /* String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");


                jsonObj = jsoncommonObj.getJSONObject("Revived");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (Opportunity_type.equalsIgnoreCase("this_week_opp")) {
            txtopportunity_type.setText("This Week Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("collection")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", false);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("overdue_collection")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Overdue Collection");


            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                // calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("today_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Today Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Tomorrow Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("This Week Collection");
            try {

                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("hot_call")) {
            txtopportunity_type.setText("Hot");

            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Hot");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("warm_call")) {
            txtopportunity_type.setText("Warm");
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Warm");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("feedback")) {

            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            txtopportunity_type.setText("Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            txtopportunity_type.setText("Overdue Feedback");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("today_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Today Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Tomorrow Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("week_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("This Week Feedback");
            try {

                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 3);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        return FinalObj;
    }

    private void showProgressDialog() {


        //progressHUD = ProgressHUD.show(OpportunityActivity.this, "", false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
       /* if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }*/

        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    class DownloadOppDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0],OpportunityActivity.this);

                response = res.toString().replaceAll("\\\\", "");
                //  response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);


                String msg = "";
                sql.delete(db.TABLE_CRM_CALL, null,
                        null);
                sql.delete(db.TABLE_CRM_CALL_OPPORTUNITY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        String jsonDOB = jorder.getString("NextActionDateTime");
                        if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                            jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonDOB);
                            DOBDate = new Date(DOB_date);
                            jsonDOB = sdf.format(DOBDate);
                            values.put(columnName, jsonDOB);

                        } else if (columnName.equalsIgnoreCase("isPartial")) {

                            values.put(columnName, "P");
                        } else if (columnName.equalsIgnoreCase("Mobile")) {
                            if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                values.put(columnName, "No Contact Available");
                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }

                        } else if (columnName.equalsIgnoreCase("ContactName")) {
                            if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                values.put(columnName, "");
                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_CRM_CALL_OPPORTUNITY, null, values);
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
            if (response.contains("CallId")) {
                UpdatList_OPP();

            } else if (response.contains("error")) {

            }
            DimissProgress();
        }

    }

    private void UpdatList_OPP() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }

    private void addView_CallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        spinner_action = (TextView) convertView.findViewById(R.id.spinner_action);
        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        ImageView img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);


        TextView txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);


        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        String Date = partialCallLists.get(i)
                .getActiondatetime();
        txtactiondatetime.setText(Date);
        tv_latestremark.setText(" For " +
                partialCallLists.get(i).getLatestRemark());

        String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();
        String Product = partialCallLists.get(position).getProduct();

        String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";


        tvcall.setText(Concatdata);
        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsChatApplicable = userpreferences.getString("chatapplicable", "");
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(OpportunityActivity.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Opportunity");
                    intent.putExtra("firm", Firm_name);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(OpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        img_appotunity_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OpportunityActivity.this, CallRatingActivity.class);
                intent.putExtra("callid", partialCallLists.get(position).getCallId());
                intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String callId = partialCallLists.get(position).getCallId();
                String Partial = partialCallLists.get(position).getIsPartial();

                if (Partial.equalsIgnoreCase("P")) {
                /*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                */


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(OpportunityActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }


               /* if (db.getCallListOppcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_OPP, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(OpportunityActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    intent.putExtra("table", "Opportunity");
                    startActivity(intent);

                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                       getCallListData_Full(partialCallLists.get(position).getCallId(), position);

                }*/
            }
        });


        lsCall_list.addView(convertView);
    }


    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date datestop = sdf.parse(datedb);


            return_value = datedb;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("crm_dialog_action", "crm_dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(datedb);
            return_value = "Today" + tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(datedb);
            return_value = "Yesterday" + tm[0];
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(datedb);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {
            String k = datedb.substring(0, datedb.length() - 15);
            String tm[] = splitfrom(return_value);
            return k + tm[0];
        }

    }

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    public void getCallListData_Full(String cid, int position) {



        /*FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanData_fullURLJSON().execute(position);*/


        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Opportunity_type.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("overdue_opp")) {

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("today_opp")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("Tommorow_opp")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("this_week_opp")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("collection")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", false);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("overdue_collection")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Overdue Collection");


            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("today_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Today Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Tomorrow Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("Isclose");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "N");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsPartial");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "P");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            txtopportunity_type.setText("Week Collection");
            try {

                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("hot_call")) {

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Hot");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("warm_call")) {

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Warm");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        new DownloadCommanData_fullURLJSON().execute(String.valueOf(position), FinalObj);

    }

    class DownloadCommanData_fullURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[1],OpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    pos = Integer.parseInt(params[0]);
                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    Cursor c;

                    sql.delete(db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_FEEDBACK, null,
                            null);
                    if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                        c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null);
                    } else if (Opportunity_type.equalsIgnoreCase("feedback") || Opportunity_type.equalsIgnoreCase("overdue_feedback") || Opportunity_type.equalsIgnoreCase("today_overdue_feedback") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                        c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_FEEDBACK, null);

                    } else {
                        c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    }

                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            String jsonDt = jorder.getString("ExpectedCloserDate");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            }
                            if (columnName.equalsIgnoreCase("ExpectedCloserDate")) {
                                jsonDt = jsonDt.substring(jsonDt.indexOf("(") + 1, jsonDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDt);
                                DOBDate = new Date(DOB_date);
                                jsonDt = sdf.format(DOBDate);
                                values.put(columnName, jsonDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }

                        }

                        if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                            long a = sql.insert(db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null, values);

                        } else if (Opportunity_type.equalsIgnoreCase("feedback") || Opportunity_type.equalsIgnoreCase("overdue_feedback") || Opportunity_type.equalsIgnoreCase("today_overdue_feedback") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                            long a = sql.insert(db.TABLE_CRM_CALL_FEEDBACK, null, values);

                        } else {
                            long a = sql.insert(db.TABLE_CRM_CALL, null, values);

                        }
                        long a = sql.insert(db.TABLE_CRM_CALL, null, values);
                        Log.d("crm_dialog_action", "count " + a);
                    }
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                    int count1 = c1.getCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            ;
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            DimissProgress();


            if (response.equalsIgnoreCase("[]")) {

                /*if (isnet()) {
                    new StartSession(CallListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            getCallListData();
                        }
                    });


                }*/
            } else {
                String Partial = partialCallLists.get(pos).getIsPartial();

                /*if (db.getCallListcount(partialCallLists.get(pos).getCallId()) > 0) {
                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL, cv, "CallId=?",
                            new String[]{partialCallLists.get(pos).getCallId()});*/


                if (Partial.equalsIgnoreCase("P")) {

                    Intent intent = new Intent(OpportunityActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(pos).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(pos).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(pos).getCallType());
                    intent.putExtra("table", "Call");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {

            if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                obj = getObj_OPP(Opportunity_type);
                //   txtopportunity_type.setText(Opportunity_type);

                if (isnet()) {
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCollectionDataURLJSON().execute(obj);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            } else if (Opportunity_type.equalsIgnoreCase("feedback") || Opportunity_type.equalsIgnoreCase("overdue_feedback") || Opportunity_type.equalsIgnoreCase("today_overdue_feedback") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                obj = getObj_OPP(Opportunity_type);
                //   txtopportunity_type.setText(Opportunity_type);

                if (isnet()) {
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadFeedbackCallDataURLJSON().execute(obj);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            } else {
                obj = getObj_OPP(Opportunity_type);
                //   txtopportunity_type.setText(Opportunity_type);
                if (isnet()) {
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadOppDataURLJSON().execute(obj);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(OpportunityActivity.this, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        OpportunityActivity.this.finish();
    }

    private void UpdatList_CollectionOPPfilter(String Firmname) {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CollectionCallList(i);
                }
            }
        }

    }

    private void UpdatList_FeedbackOPPfilter(String Firmname) {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                //  partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_FeedbackCallList(i);
                }
            }
        }

    }

    private void UpdatList_OPPfilter(String Firmname) {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }


    private void showdialog(final int i) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_provisional_receipt_lay, null, false);
        builder = new AlertDialog.Builder(OpportunityActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();
        edt_amount = (EditText) v.findViewById(R.id.edt_amount);
        edt_instrument_no = (EditText) v.findViewById(R.id.edt_instrument_no);
        edt_bankname = (EditText) v.findViewById(R.id.edt_bankname);
        edt_tds_amount = (EditText) v.findViewById(R.id.edt_tds_amount);
        edt_narration = (EditText) v.findViewById(R.id.edt_narration);
        edt_date = (EditText) v.findViewById(R.id.edt_date);
        // edt_deposite_bank = (EditText) v.findViewById(R.id.edt_deposite_bank);

        img_date = (ImageView) v.findViewById(R.id.img_date);
        txt_Save = (TextView) v.findViewById(R.id.buttonSave);
        txt_Close = (TextView) v.findViewById(R.id.buttonClose);

        txt_Save.setOnClickListener(new View.OnClickListener() {
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
                    String Deposite = edt_deposite_bank.getText().toString();
                    String CallId = partialCallLists.get(i).getCallId();
                    JSONObject jsonprovisionalreciptadd = new JSONObject();

                    dfDate = new SimpleDateFormat("dd/MM/yyyy");
                    date = dfDate.format(new Date());

                    try {
                        jsonprovisionalreciptadd.put("CallId", CallId);
                        jsonprovisionalreciptadd.put("InvoiceNumber", InvoiceNo);
                        jsonprovisionalreciptadd.put("Amount", Amount);
                        jsonprovisionalreciptadd.put("InstrumentNo", Instrument);
                        jsonprovisionalreciptadd.put("BankName", Bankname);
                        jsonprovisionalreciptadd.put("TDSAmount", TDS_Amount);
                        jsonprovisionalreciptadd.put("Narration", Narration);
                        jsonprovisionalreciptadd.put("PaymentDepBank", Deposite);
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
                        new StartSession(OpportunityActivity.this, new CallbackInterface() {
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
        img_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityActivity.this,
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
        edt_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityActivity.this,
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
        txt_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = builder.create();
        alertDialog1.setCancelable(true);
        alertDialog1.setCanceledOnTouchOutside(false);
        alertDialog1.show();
    }

    class DownloadCustomerIdData extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getCustCon + "?CallId=" +
                        URLEncoder.encode(callId, "UTF-8");

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                //JSONArray jResults = new JSONArray(response);

                JSONObject jsoncustomer_id = null;
                try {
                    jsoncustomer_id = new JSONObject(response);
                    FKCustomerId = jsoncustomer_id.getString("FKCustomerId");

                    FKConsigneeId = jsoncustomer_id.getString("FKConsigneeId");
                    InvoiceNo = jsoncustomer_id.getString("InvoiceNo");


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            dismissProgressDialog();


        }


    }

    class PostSaveReciptJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveCollectionReceipt;
            System.out.println("BusinessAPIURL-1 :" + ProvisionalRecieptjson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);
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
            DimissProgress();

            Toast.makeText(OpportunityActivity.this, "Receipt save successfully", Toast.LENGTH_LONG).show();
            alertDialog1.dismiss();
        }

    }


    //Collection Call COde

    class DownloadCollectionDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0]);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("u0026", "&");
                    //  response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);

                    System.out.println("CollectionData" + response);


                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY, null, values);
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
            if (response.contains("CallId")) {
                UpdatList_COllection_call();

            } else if (response.contains("error")) {

            }
            DimissProgress();
        }

    }


    private void UpdatList_COllection_call() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallList.setProvisionalCount(cur.getString(cur.getColumnIndex("ProvisionalCount")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CollectionCallList(i);
                }
            }
        }

    }

    private void addView_CollectionCallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_collection_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        spinner_provisional = (Spinner) convertView.findViewById(R.id.spinner_provisional);
        txt_invoice_no = (TextView) convertView.findViewById(R.id.txt_invoice_no);
        txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
        // len_collection= (LinearLayout) convertView.findViewById(R.id.len_collection);
        TextView txt_Unallocateamount = (TextView) convertView.findViewById(R.id.txt_Unallocateamount);


        TextView txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);


        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        ImageView img_provisional_count = (ImageView) convertView.findViewById(R.id.img_provisional_count);

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                //     img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                //   img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                // img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        txtactiondatetime.setText(partialCallLists.get(i)
                .getActiondatetime());
        tv_latestremark.setText(" For " +
                partialCallLists.get(i).getLatestRemark());

        String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();
        String Product = partialCallLists.get(position).getProduct();
        final String Invoice_no = partialCallLists.get(position).getInvoiceNo();
        String Amount = partialCallLists.get(position).getAmount();
        String Invoice_date = partialCallLists.get(position).getInvoiceDt();
        String Unallocateamount = "Unallocated " + "\n" + "Cash" + "\n" + partialCallLists.get(position).getUnAllocatedCash();

        String Invoice_Date = Invoice_no + "\n" + Invoice_date + "\n" + Amount;
        txt_invoice_no.setText(Invoice_Date);
        txt_amount.setText(Amount);

        if (partialCallLists.get(position).getUnAllocatedCash().equals("0.0")) {
            txt_Unallocateamount.setVisibility(View.GONE);
        } else {
            txt_Unallocateamount.setText(Unallocateamount);
            txt_Unallocateamount.setVisibility(View.VISIBLE);
        }

        String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";
        tvcall.setText(Concatdata);

        final int Provisional_count = Integer.parseInt(partialCallLists.get(position).getProvisionalCount());

        System.out.println("ProvisionalCount" + Provisional_count);

        if (Provisional_count > 0) {
            img_provisional_count.setVisibility(View.VISIBLE);
        }


        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsChatApplicable = userpreferences.getString("chatapplicable", "");
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(OpportunityActivity.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Collection");
                    intent.putExtra("firm", Firm_name);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(OpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner_provisional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String Action = adapterView.getItemAtPosition(i).toString();
                if (++check > 1) {
                    if (Action.equalsIgnoreCase("Provisional Receipt")) {
                        callId = partialCallLists.get(position).getCallId();
                        Intent intent = new Intent(OpportunityActivity.this, ProvisionalActivity.class);
                        intent.putExtra("Call_id", callId);
                        intent.putExtra("procount", Provisional_count);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else if (Action.equalsIgnoreCase("Discount Voucher")) {
                        callId = partialCallLists.get(position).getCallId();
                        String invoice = partialCallLists.get(position).getInvoiceNo();
                        getDiscountVoucherdialog(callId, invoice);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       /* spinner_provisional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callId = partialCallLists.get(position).getCallId();
                Intent intent=new Intent(OpportunityActivity.this,ProvisionalActivity.class);
                intent.putExtra("Call_id",callId);
                intent.putExtra("procount",Provisional_count);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
*/


        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String callId = partialCallLists.get(position).getCallId();
                String Partial = partialCallLists.get(position).getIsPartial();

                if (Partial.equalsIgnoreCase("P")) {
                /*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                */


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(OpportunityActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }


               /* if (db.getCallListOppcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_OPP, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(OpportunityActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    intent.putExtra("table", "Opportunity");
                    startActivity(intent);

                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                       getCallListData_Full(partialCallLists.get(position).getCallId(), position);

                }*/
            }
        });


        lsCall_list.addView(convertView);
    }


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    class DownloadFeedbackCallDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0]);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    //  response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);


                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_FEEDBACK, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_FEEDBACK, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_FEEDBACK, null, values);
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
            if (response.contains("CallId")) {
                UpdatList_Feedback_call();

            } else if (response.contains("error")) {

            }
            DimissProgress();
        }

    }

    private void UpdatList_Feedback_call() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,Product,isPartial" +
                " FROM " + db.TABLE_CRM_CALL_FEEDBACK + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_FeedbackCallList(i);
                }
            }
        }
    }

    private void addView_FeedbackCallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_feedback_callslist_partial_lay,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        spinner_action = (TextView) convertView.findViewById(R.id.spinner_action);
        TextView txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
        // len_collection= (LinearLayout) convertView.findViewById(R.id.len_collection);


/*

        if (Opportunity_type.equalsIgnoreCase("collection")||Opportunity_type.equalsIgnoreCase("overdue_collection")||Opportunity_type.equalsIgnoreCase("today_overdue_collection")||Opportunity_type.equalsIgnoreCase("week_overdue_collection")){
            spinner_action.setVisibility(View.VISIBLE);
        }

*/
/*

        spinner_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
                callId=partialCallLists.get(i).getCallId();

                if (isnet()) {
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCustomerIdData().execute(callId);
                        }
                    });
                }

            }
        });
*/


        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        ImageView img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            /*if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            }*/


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        String Date = partialCallLists.get(i)
                .getActiondatetime();
        txtactiondatetime.setText(Date);
        tv_latestremark.setText(" For " +
                partialCallLists.get(i).getLatestRemark());

        String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();
        String Product = partialCallLists.get(position).getProduct();

        String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";


        tvcall.setText(Concatdata);

        /*if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("")) {
            tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
        } else {
            if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {
                tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
            } else {
                tvcall.setText("(" + partialCallLists.get(i).getContactName() + " - "
                        + partialCallLists.get(i).getMobileno() + ")");
            }

        }*/

        //Collection Call Save Code


       /* img_appotunity_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OpportunityActivity.this, CallRatingActivity.class);
                intent.putExtra("callid", partialCallLists.get(position).getCallId());
                intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
*/

        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String callId = partialCallLists.get(position).getCallId();
                String Partial = partialCallLists.get(position).getIsPartial();

                if (Partial.equalsIgnoreCase("P")) {
                /*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                */


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(OpportunityActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }


               /* if (db.getCallListOppcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_OPP, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(OpportunityActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    intent.putExtra("table", "Opportunity");
                    startActivity(intent);

                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                       getCallListData_Full(partialCallLists.get(position).getCallId(), position);

                }*/
            }
        });

        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsChatApplicable = userpreferences.getString("chatapplicable", "");
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(OpportunityActivity.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_feedback");
                    intent.putExtra("firm", Firm_name);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(OpportunityActivity.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        lsCall_list.addView(convertView);
    }


    public boolean validate() {
        // TODO Auto-generated method stub
        if ((edt_amount.getText().toString().equalsIgnoreCase("") ||
                edt_amount.getText().toString().equalsIgnoreCase(" ") ||
                edt_amount.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(OpportunityActivity.this, "Amount is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_instrument_no.getText().toString().equalsIgnoreCase("") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(" ") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(OpportunityActivity.this, "Instrument No is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(OpportunityActivity.this, "Bank name is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_deposite_bank.getText().toString().equalsIgnoreCase("") ||
                edt_deposite_bank.getText().toString().equalsIgnoreCase(" ") ||
                edt_deposite_bank.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(OpportunityActivity.this, "Bank name is required", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private void getDiscountVoucherdialog(final String call_id, final String invoice_no) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_discount_lay, null, false);
        builder = new AlertDialog.Builder(OpportunityActivity.this);
        builder.setView(v);
        alertDialog = builder.create();

        final EditText edt_reversal_amount = (EditText) v.findViewById(R.id.edt_reversal_amount);
        final EditText edt_reason = (EditText) v.findViewById(R.id.edt_reason);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Reversal_amount = edt_reversal_amount.getText().toString();
                reason = edt_reason.getText().toString();
                Call_ID = call_id;
                Invoice_NO = invoice_no;

                getapproverdialog(Reversal_amount, reason, Call_ID, Invoice_NO);

               /* if (isnet()) {
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(call_id,invoice_no,Reversal_amount,reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(OpportunityActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
*/


            }
        });


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private void getapproverdialog(final String reversal_amount, final String reason, final String call_id, final String invoice_no) {

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_approver_lay, null, false);
        builder = new AlertDialog.Builder(OpportunityActivity.this);
        builder.setView(v);
        alertDialog1 = builder.create();


        spinner_approver = (Spinner) v.findViewById(R.id.spinner_approver);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);

        alertDialog.dismiss();


        if (isnet()) {
            new StartSession(OpportunityActivity.this, new CallbackInterface() {
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
                    new StartSession(OpportunityActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(ApprId, call_id, invoice_no, reversal_amount, reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(OpportunityActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetSaveDiscount + "?CallId=" + Call_ID + "&InvoiceNo=" + Invoice_NO + "&ReversalAmount=" + URLEncoder.encode(Reversal_amount, "UTF-8") + "&Reason=" + URLEncoder.encode(reason, "UTF-8") + "&ApprId=" + ApprId + "&Type=R";  //ApprId


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
            DimissProgress();
            if (response.equalsIgnoreCase("success") || response != null) {
                Toast.makeText(OpportunityActivity.this, "Discount Voucher is Successfully sent for Approval", Toast.LENGTH_LONG).show();
                alertDialog1.dismiss();
            } else {
                Toast.makeText(OpportunityActivity.this, "Please try again", Toast.LENGTH_LONG).show();

            }

        }

    }

    class DownloadApproverDetailsData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
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

            Approverlist1 = new ArrayList<>();
            approverDatas = new ArrayList<>();

            try {
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jsonapproverlistdata = jResults.getJSONObject(i);

                    ApproverData approverData = new ApproverData();
                    approverData.setUserid(jsonapproverlistdata.getString("userid"));
                    approverData.setUsername(jsonapproverlistdata.getString("username"));
                    approverDatas.add(approverData);
                    String point = jsonapproverlistdata.getString("username");
                    Approverlist1.add(point);


                }
                spinner_approver.setAdapter(new ArrayAdapter<String>(OpportunityActivity.this,
                        R.layout.crm_custom_spinner_txt,
                        Approverlist1));
            } catch (Exception e) {
                e.printStackTrace();
            }
            DimissProgress();

        }

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

    private void ShowProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void DimissProgress() {
        mProgress.setVisibility(View.GONE);
    }

}



