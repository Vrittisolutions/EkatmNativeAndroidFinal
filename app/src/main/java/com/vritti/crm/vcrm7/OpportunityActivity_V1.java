package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.AlfaHomePage;
import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.crm.adapter.OpportunityAdapter;
import com.vritti.crm.adapter.OpportunityAdapter_V1;
import com.vritti.crm.bean.ApproverData;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.classes.CollectionCallCommonObjectProperties;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.crm.classes.FeedbackCommonObjectProperties;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.activity.ActivityLogIn;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.ForegroundService;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.WelcomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.codetail.widget.RevealFrameLayout;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;


public class OpportunityActivity_V1 extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    CommonObjectProperties commonObj;
    FeedbackCommonObjectProperties feedbackcommonObj;
    CollectionCallCommonObjectProperties collectioncallcommonObj;
    SimpleDateFormat dfDate, dftime;
    String FinalObj, Firmname;
    String obj = "";
    String Opportunity_type="", contactno = "", starttime = "", endtime = "", duration = "", rowNo = "";
    SharedPreferences userpreferences;
    String UserType;
    SQLiteDatabase sql;
    ProgressHUD progressHUD;
    ArrayList<PartialCallList> partialCallLists;
    LinearLayout lsCall_list, realcolors, laycall_type, len_collection;
    RecyclerView list_Opportunity;
    TextView txtaddress,txtfirmname, tvcall, txtcityname, txtactiondatetime, txtopportunity_type, tv_latestremark, txt_Save, txt_Close, txt_invoice_no, txt_amount,txt_invoice_date;
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
    String callId, date, invoice = "", ProspectPkSusId = "";
    private String FKCustomerId, FKConsigneeId, InvoiceNo;
    int rowClickPos;
    //Spinner spinner_provisional;
    // Button btn_provisional, btn_action;
    ImageView btn_provisional;
    Button img_action;
    AlertDialog dialog;
    String Reversal_amount = "", reason, Call_ID, Invoice_NO;
    public String IsChatApplicable;
    int check = 0;
    Spinner spinner_approver;
    ArrayList<ApproverData> approverDatas = new ArrayList<>();
    ArrayList<String> Approverlist1;
    private String ApprId;
    ProgressBar mProgress;
    private Integer pos1;
    int Provisional_count;
    String firmName = "", firmAlias = "", address = "", countryId = "", stateId = "", cityId = "",
            territoryId = "", remark = "", businessSegmentId = "", businessDetails = "", website = "", sourceOfProspect = "";

    int oppPosition;

    int startPos = 0;
    int EndofAPILoop = 0;
    int rowStart = 0;
    int rowEnd = 9;
    OpportunityAdapter_V1 opportunityAdapter;
    List<android.location.Address> Listaddress;
    String Address="";
    double Lat=0,Lng=0;
    LinearLayout len_filter;
     ScrollView scroll;
    ArrayList<PartialCallList> filterTempList;
    public Toolbar toolbar;
    ImageView img_back;
    private SearchView editsearch;
    private String Year="";
    LinearLayout len_firm,len_city,len_mobile,len_product,len_name,len_next;
    RevealFrameLayout lay_reveal;
    String Filtername="",Flag="";
    private String  invoice_date="";
    private String contact="";


    String actid, time, Starttime, sp_date;
    private SharedPreferences AtendanceSheredPreferance;
    String getdate, currentTime;
    private int backToposition;
    String Response_Call="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_opportunity);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);


        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);
        list_Opportunity = findViewById(R.id.list_Opportunity);
        scroll = findViewById(R.id.scroll);

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
        //UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         toolbar.setTitle("");
        //toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
      //  toolbar.setLogo(R.drawable.crm_logo_1);

        setSupportActionBar(toolbar);
        txtopportunity_type = (TextView) findViewById(R.id.txtopportunity_type);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        edt_search_firm = (EditText) findViewById(R.id.edt_search_firm);
        img_search_firm = (ImageView) findViewById(R.id.img_search_firm);
        len_filter=findViewById(R.id.len_filter);
        img_back=findViewById(R.id.img_back);
        lay_reveal=findViewById(R.id.lay_reveal);
        len_firm=findViewById(R.id.len_firm);
        len_mobile=findViewById(R.id.len_mobile);
        len_city=findViewById(R.id.len_city);
        len_product=findViewById(R.id.len_product);
        len_name=findViewById(R.id.len_name);
        len_next=findViewById(R.id.len_next);

        edt_search_firm.setCursorVisible(true);
        edt_search_firm.setFocusable(true);

    //    editsearch = (SearchView) findViewById(R.id.search);

     //   editsearch.setOnQueryTextListener(this);

       /* AutoCompleteTextView search_text = (AutoCompleteTextView) editsearch.findViewById(editsearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.WHITE);
        search_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.sp_14));
       */



        Intent intent = getIntent();
        partialCallLists = new ArrayList<PartialCallList>();
        Opportunity_type = intent.getStringExtra("Opportunity");
        // UserMasterId = intent.getStringExtra("UserMasterId");


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        filterTempList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Year= String.valueOf(year);



/*
        edt_search_firm.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                // TODO Auto-generated method stub

                String text1 = edt_search_firm.getText().toString().toLowerCase(Locale.getDefault());

                if (Opportunity_type.contains("opp")) {
                    filter(text1);
                }else if (Opportunity_type.contains("collection")) {
                    UpdatList_CollectionOPPfilter(text1);
                }

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

               */
/* if (name.length() > 0) {
                    getchatroomsearch(name);
                }
                else {
                  //  Toast.makeText(getApplicationContext(), "Please enter atleast two charactor to customize your search", Toast.LENGTH_LONG).show();

                }
*//*



            }
        });
*/



        // callId=intent.getStringExtra("callId");



       /* editsearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtopportunity_type.setVisibility(View.GONE);
                lay_reveal.setVisibility(View.VISIBLE);
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                Filtername="Firm";

            }
        });*/

        len_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="City";
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                //lay_reveal.setVisibility(View.GONE);
            }
        });

        len_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="Firm";

                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                //lay_reveal.setVisibility(View.GONE);
            }
        });

        len_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="Mobile";

                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
               // lay_reveal.setVisibility(View.GONE);
            }
        });

        len_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="Product";

                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
               // lay_reveal.setVisibility(View.GONE);
            }
        });

        len_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="Contact";

                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
               // lay_reveal.setVisibility(View.GONE);
            }
        });
        len_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filtername="Next";

                len_next.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_mobile.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_product.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_name.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
                len_city.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background));
              //  lay_reveal.setVisibility(View.GONE);

            }
        });



        if (Opportunity_type.equalsIgnoreCase("collectioncall")) {
            txtopportunity_type.setText("Collection");

            contactno = getIntent().getStringExtra("MobileNo");
            starttime = getIntent().getStringExtra("starttime");
            endtime = getIntent().getStringExtra("endtime");
            duration = getIntent().getStringExtra("duration");
            rowNo = getIntent().getStringExtra("Rowno");

            UpdatList_COllection_call_log();

        }
        else if (Opportunity_type.equalsIgnoreCase("opportunitycall")) {
            txtopportunity_type.setText("Opportunity");
            contactno = getIntent().getStringExtra("MobileNo");
            starttime = getIntent().getStringExtra("starttime");
            endtime = getIntent().getStringExtra("endtime");
            duration = getIntent().getStringExtra("duration");
            rowNo = getIntent().getStringExtra("Rowno");
            UpdatList_OPP_log();

        }
        else {
            scroll.setVisibility(View.VISIBLE);
            if (Opportunity_type.equalsIgnoreCase("collection")

                    || Opportunity_type.equalsIgnoreCase("overdue_collection") ||
                    Opportunity_type.equalsIgnoreCase("today_overdue_collection")
                    || Opportunity_type.equalsIgnoreCase("week_overdue_collection") ||
                    Opportunity_type.equalsIgnoreCase("tomorrow_overdue_collection") ||
                    Opportunity_type.equalsIgnoreCase("new_collection")) {
                obj = getObj_OPP(Opportunity_type);
                scroll.setVisibility(View.VISIBLE);
                //   txtopportunity_type.setText(Opportunity_type);
                if (isnet()) {
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new DownloadCollectionDataURLJSON().execute(obj);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            } else if (Opportunity_type.equalsIgnoreCase("feedback") ||
                    Opportunity_type.equalsIgnoreCase("overdue_feedback") ||
                    Opportunity_type.equalsIgnoreCase("today_overdue_feedback") ||
                    Opportunity_type.equalsIgnoreCase("tomorrow_overdue_feedback") ||
                    Opportunity_type.equalsIgnoreCase("week_overdue_feedback")) {
                obj = getObj_OPP(Opportunity_type);

                //   txtopportunity_type.setText(Opportunity_type);
                scroll.setVisibility(View.VISIBLE);
                if (isnet()) {
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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

                scroll.setVisibility(View.GONE);

                if (getIntent().hasExtra("flag")){
                    Flag=getIntent().getStringExtra("flag");
                    opportunityAdapter = new OpportunityAdapter_V1(OpportunityActivity_V1.this, partialCallLists,Flag,"","","","","",Opportunity_type);

                }else if (getIntent().hasExtra("logupdate")){
                    Flag=getIntent().getStringExtra("logupdate");
                    contact =getIntent().getStringExtra("callmob");
                    starttime = getIntent().getStringExtra("starttime");
                    endtime = getIntent().getStringExtra("endtime");
                    duration = getIntent().getStringExtra("duration");
                    rowNo = getIntent().getStringExtra("Rowno");
                    opportunityAdapter = new OpportunityAdapter_V1(OpportunityActivity_V1.this, partialCallLists,Flag,contact,starttime,endtime,duration,rowNo,Opportunity_type);
                }
                else {
                    opportunityAdapter = new OpportunityAdapter_V1(OpportunityActivity_V1.this, partialCallLists,"","","","","","",Opportunity_type);
                }
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                list_Opportunity.setLayoutManager(mLayoutManager);
                list_Opportunity.setItemAnimator(new DefaultItemAnimator());
                list_Opportunity.setAdapter(opportunityAdapter);


                if (isnet()) {
                        new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadOppDataURLJSON_Paging().execute(obj);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                Toast.makeText(OpportunityActivity_V1.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{

                        Toast.makeText(OpportunityActivity_V1.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
              }

            //   txtopportunity_type.setText(Opportunity_type);

           /*     if (isnet()) {
                    new StartSession(OpportunityActivity_V1_V1.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadOppDataURLJSON().execute(obj);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }*/


        }


        list_Opportunity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                int l = layoutManager.findLastCompletelyVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                /*  if (totalItemCount > 0 && endHasBeenReached) {*/
                //you have reached to the bottom of your recycler viewi
                if(lastVisible == (partialCallLists.size() -1)){
                    String reQuery = "N";
                    if (Filtername.equals("City")){
                    } else if (Filtername.equals("Firm")){
                    } else if (Filtername.equals("Mobile")){
                    }else if (Filtername.equals("Contact")) {
                    }else if (Filtername.equals("Next")){
                    }else {
                        loadNextActivity(partialCallLists.size());
                    }
                } else {

                }
            }
        });

        img_search_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edt_search_firm.setVisibility(View.VISIBLE);
                txtopportunity_type.setVisibility(View.GONE);


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

        if (Opportunity_type.equalsIgnoreCase("start_again")) {
             try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "Y");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
        }else {
           try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
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

        }
        else if (Opportunity_type.equalsIgnoreCase("main_opp")) {
            txtopportunity_type.setText("Opportunity");
            /************************************************/

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 30);
                Date newDate1 = calendar.getTime();

            /*Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();*/

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "<");


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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }



            /************************************************/
/*
            try {
                String currentDateandTime = dftime.format(new Date());
                Date cdate = dftime.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                *//*calendar.add(Calendar.DAY_OF_YEAR, -7);*//*
                // calendar.add(Calendar.DAY_OF_YEAR);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dftime.format(newDate1));
                jsonObj.put("value2", ""*//*dfDate.format(newDate1)*//*);
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
            }*/



        }
        else if (Opportunity_type.equalsIgnoreCase("new_opp")) {
            txtopportunity_type.setText("New Opportunity");



            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }


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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("overdue_opp")) {
            txtopportunity_type.setText("Overdue Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("yesterday_opp")) {
            txtopportunity_type.setText("Yesterday Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("today_opp")) {
            txtopportunity_type.setText("Today Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



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


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("callagain_opp")) {
            txtopportunity_type.setText("Call Again Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Opportunity_type.equalsIgnoreCase("Tommorow_opp")) {
            txtopportunity_type.setText("Tomorrow Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }



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



            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("revived_opp")) {
            txtopportunity_type.setText("Revived Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }





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


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Opportunity_type.equalsIgnoreCase("this_week_opp")) {
            txtopportunity_type.setText("This Week Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }




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

                   /* if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }*/
                    newDate1 = cdate;
                    calendar.setTime(newDate1);

                    calendar.add(Calendar.DATE, +7);
                    newDate2 = calendar.getTime();

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


            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("start_again")) {
            txtopportunity_type.setText("Start Again Opportunity");
            /************************************************/


            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }




            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 30);
                Date newDate1 = calendar.getTime();

            /*Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(cdate);
            calendar1.add(Calendar.DAY_OF_YEAR, 7);
            Date newDate2 = calendar1.getTime();*/

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "<");


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

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Yes");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else if (Opportunity_type.equalsIgnoreCase("collection")) {

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
        }
        else if (Opportunity_type.equalsIgnoreCase("overdue_collection")) {

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
        }
        else if (Opportunity_type.equalsIgnoreCase("today_overdue_collection")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_collection")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("hot_call")) {
            txtopportunity_type.setText("Hot Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (Opportunity_type.equalsIgnoreCase("warm_call")) {
            txtopportunity_type.setText("Warm Opportunity");

            try {
                jsoncommonObj.put("TypeofCall","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowStart");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowStart);
                jsonObject.put("value2", "");


                jsoncommonObj.put("RowStart", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "RowEnd");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", rowEnd);
                jsonObject.put("value2", "");

                jsoncommonObj.put("RowEnd", jsonObject);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "ReQuery");
                jsonObject.put("IsSet", false);
                jsonObject.put("Operator", "eq");
                jsonObject.put("value1", "Y");
                jsonObject.put("value2", "");

                jsoncommonObj.put("ReQuery", jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("IsCallAgain");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else if (Opportunity_type.equalsIgnoreCase("feedback")) {

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
                calendar.add(Calendar.DATE, +30);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

                //  jsonObj.put("value2", dfDate.format(newDate2));

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
        }
        else if (Opportunity_type.equalsIgnoreCase("overdue_feedback")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("today_overdue_feedback")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_feedback")) {
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
        }
        else if (Opportunity_type.equalsIgnoreCase("week_overdue_feedback")) {
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

        }

        else {

        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        return FinalObj;
    }

    private void showProgressDialog() {


        //progressHUD = ProgressHUD.show(OpportunityActivity_V1.this, "", false, false, null);

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


    private void UpdatList_OPP() {
        //  partialCallLists.clear();
      /*  if (startPos == 0) {
            partialCallLists.clear();
        }else{
            startPos = 1;
        }*/
        partialCallLists.clear();
        filterTempList.clear();

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId,Address,lat,Long,ExpectedValue,EmailId,NextAction,NextMilestone,ExpectedCloserDate" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                //  String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                String callId= cur.getString(cur.getColumnIndex("CallId"));
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
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallList.setLat(cur.getString(cur.getColumnIndex("lat")));
                partialCallList.setLong(cur.getString(cur.getColumnIndex("Long")));
                partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallList.setNextMilestone(cur.getString(cur.getColumnIndex("NextMilestone")));
                partialCallList.setExpectedCloserDate(cur.getString(cur.getColumnIndex("ExpectedCloserDate")));

                int pos = -1;
                if(partialCallLists.size() != 0) {
                    for (int i = 0; i < partialCallLists.size(); i++) {
                        if (partialCallLists.get(i).getCallId().equals(callId)) {
                            pos = 1;
                        }
                    }
                    if (pos != 1) {
                        partialCallLists.add(partialCallList);
                    }
                }else{
                    partialCallLists.add(partialCallList);
                }

            } while (cur.moveToNext());
            opportunityAdapter.notifyDataSetChanged();
            filterTempList.addAll(partialCallLists);

            startPos = 1;

           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
          /*  lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }*/
        }

    }

   private void UpdatList_OPP_log() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId,Address,lat,Long,ExpectedValue,EmailId,NextAction,NextMilestone" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  Mobile ='" + contactno + "'";


//                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId" +

        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                PartialCallList partialCallList = new PartialCallList();
              //  String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));

                String dt = cur.getString(cur.getColumnIndex("NextActionDateTime"));

                if (dt.contains("T")) {
                    dt = dt.replace("T", " ");

                    if (dt.contains(Year)) {
                        dt = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM  hh:mm aa", dt);
                        partialCallList.setActiondatetime(dt);
                    } else {
                        dt = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy  hh:mm aa", dt);
                        partialCallList.setActiondatetime(dt);

                    }
                }else {
                    partialCallList.setActiondatetime(dt);
                }

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
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallList.setLat(cur.getString(cur.getColumnIndex("lat")));
                partialCallList.setLong(cur.getString(cur.getColumnIndex("Long")));
                partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));
                partialCallList.setEmailid(cur.getString(cur.getColumnIndex("EmailId")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallList.setNextMilestone(cur.getString(cur.getColumnIndex("NextMilestone")));
                partialCallLists.add(partialCallList);

               /* int pos = -1;
                if (partialCallLists.size() != 0) {
                    for (int i = 0; i < partialCallLists.size(); i++) {
                        if (partialCallLists.get(i).getCallId().equals(callId)) {
                            pos = 1;
                        }
                    }
                    if (pos != 1) {
                        partialCallLists.add(partialCallList);
                    }
                } else {
                    partialCallLists.add(partialCallList);
                }*/

            } while (cur.moveToNext());
           /* opportunityAdapter.notifyDataSetChanged();
            filterTempList.addAll(partialCallLists);

            startPos = 1;*/

        }


           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/

           lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                scroll.setVisibility(View.VISIBLE);
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }


    }

    private void addView_CallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity_V1.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;
        oppPosition = position;

        LinearLayout  laycall_type,len_action,len_call,len_callslist;
        final TextView txtfirmname, txtcityname, tv_latestremark, txtactiondatetime,
                tvcall, txt_chat,txtaddress,txt_expvalue,txt_email,milestone;
        ImageView img_action, img_appotunity_update,img_contact,img_nextaction,callrating;
        RelativeLayout realcolors;

        final View convertView = layoutInflater.inflate(R.layout.crm_callslist_partial_lay_v1,
                null);


        realcolors = (RelativeLayout) convertView.findViewById(R.id.realcolors);
        txtfirmname = (TextView) convertView.findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        //   spinner_action = (TextView) convertView.findViewById(R.id.spinner_action);
        img_action = (ImageView) convertView.findViewById(R.id.btn_action);
        //    btn_action = (ButAton) convertView.findViewById(R.id.btn_action);
        txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView.findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);
        txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
        txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);
        txt_expvalue = (TextView) convertView.findViewById(R.id.txt_expvalue);
        len_action = (LinearLayout) convertView.findViewById(R.id.len_action);
        img_contact =convertView.findViewById(R.id.img_contact);
        txt_email =convertView.findViewById(R.id.txt_email);
        img_nextaction =convertView.findViewById(R.id.img_nextaction);
        callrating = convertView.findViewById(R.id.callrating);
        len_call = (LinearLayout)convertView.findViewById(R.id.lencall);
        len_callslist = (LinearLayout)convertView.findViewById(R.id.len_callslist);
        milestone = (TextView) convertView.findViewById(R.id.milestone);
        CardView card_viewfill = (CardView) convertView.findViewById(R.id.card_viewfill);
        card_viewfill.setPreventCornerOverlap(false);



        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                //callrating.setImageDrawable(getResources().getDrawable(R.drawable.square));
                //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));


                callrating.setImageResource(R.drawable.ic_cube);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                // holder.callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                //  ImageViewCompat.setImageTintList(holder.callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.hot)));

                callrating.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                //   holder.callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                // ImageViewCompat.setImageTintList(holder.callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.warm)));
                callrating.setImageResource(R.drawable.img_warm_call);
            }
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            callrating.setImageResource(R.drawable.coldcall);
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            callrating.setImageResource(R.drawable.coldcall);
        }
        String City = partialCallLists.get(i).getCityname();

        txtfirmname.setText(partialCallLists.get(i).getFirmname()+", "+City);
        String Date = partialCallLists.get(i).getActiondatetime();

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        String dateString = sdf.format(date1);
        if (Date.contains(dateString)){
        }else {
            txt_expvalue.setTextColor(context.getResources().getColor((R.color.red)));
        }

        String latestRemark = partialCallLists.get(i).getLatestRemark();
        if (!(latestRemark == null || latestRemark.equals("null") || latestRemark.equals(""))) {
            tv_latestremark.setText("For " + partialCallLists.get(i).getLatestRemark());

        }
        txtactiondatetime.setText(Date);
        if (partialCallLists.get(i).getEmailid().equalsIgnoreCase("")){
            txt_email.setVisibility(View.GONE);
        }else {
            txt_email.setText(partialCallLists.get(i).getEmailid());
        }
        String  NextAction=partialCallLists.get(i).getNextAction();
        if (NextAction.equalsIgnoreCase("Email")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.email_24));
            img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityActivity_V1.this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else if (NextAction.equalsIgnoreCase("Telephone")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.visit24));
            img_nextaction.setColorFilter(ContextCompat.getColor(OpportunityActivity_V1.this, R.color.colorPrimary
            ), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else {
            img_nextaction.setImageDrawable(getResources().getDrawable(R.drawable.call_24));
        }


        String Mobile = partialCallLists.get(i).getMobileno();

        String Contactname = partialCallLists.get(i).getContactName();
        String Product = partialCallLists.get(i).getProduct();
        if (City.equals("null")) {
            City = "";
        }
        if (Product==null||Product.equals("null")) {
            Product = "";
        }
        String appenContact = Contactname + ", " +  Mobile;
        if (appenContact.equals(" , ")) {
            appenContact = "No Contact Available";
        }
        String appendCityProduct =  Product;
        if (appendCityProduct.equals("null")) {
            appendCityProduct = "";
        }


        String Concatdata = appendCityProduct +"\n"+appenContact;


        tvcall.setText(Concatdata);

        //}


        if (i % 2 == 1) {
            //holder.realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            //holder.realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }

        Address=partialCallLists.get(i).getAddress();
        txtaddress.setText(Address);

       /* Lat=partialCallLists.get(position).getLat();
        Long=partialCallLists.get(position).getLong();
       */
        Geocoder coder = new Geocoder(context);
        try {
            Listaddress = coder.getFromLocationName(Address, 5);
            android.location.Address location = Listaddress.get(0);
            Lat = location.getLatitude();
            Lng = location.getLongitude();

        }catch (Exception e){
            e.printStackTrace();
        }


        String ExpValue=partialCallLists.get(i).getExpectedValue();
        if (ExpValue.equals("0.00")||ExpValue.equals("0.0")||ExpValue.equals("0")){

        }else {
            txt_expvalue.setVisibility(View.VISIBLE);
            double d=Double.parseDouble(ExpValue);
            txt_expvalue.setText("EV-"+String.format("%.2f", d));


            //holder.txt_expvalue.setText("EV-"+ExpValue);

        }

        String Milestone=partialCallLists.get(i).getNextMilestone();
        milestone.setText(Milestone);
        txtaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lat==0&Lng==0){

                }else {

                    try {
                        Address=partialCallLists.get(i).getAddress();
                        Geocoder coder = new Geocoder(context);
                        Listaddress = coder.getFromLocationName(Address, 5);
                        android.location.Address location = Listaddress.get(0);
                        Lat = location.getLatitude();
                        Lng = location.getLongitude();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    String geoUri = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng + " (" + partialCallLists.get(i).getFirmname() + ")";
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(mapIntent);


                    }
                }
            }
        });



        len_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OpportunityActivity_V1.this, CRM_Callslist_Partial.class);
                intent.putExtra("firm",partialCallLists.get(i).getFirmname());
                intent.putExtra("date",partialCallLists.get(i).getActiondatetime());
                intent.putExtra("action",partialCallLists.get(i).getNextAction());
                intent.putExtra("status",partialCallLists.get(i).getCallStatus());
                intent.putExtra("call",tvcall.getText().toString());
                intent.putExtra("remark",tv_latestremark.getText().toString());
                String  Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                String Call_CallType = partialCallLists.get(i).getCallType();
                String Call_Callid = partialCallLists.get(i).getCallId();
                String SourceId = partialCallLists.get(i).getSourceId();
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("call_type_1", "Crm_Opportunity");
                intent.putExtra("projmasterId", "");
                intent.putExtra("AssignBy", UserName);
                intent.putExtra("AssignById", UserMasterId);
                intent.putExtra("SourceId", SourceId);
                intent.putExtra("mile", partialCallLists.get(i).getNextMilestone());
                intent.putExtra("mobile", partialCallLists.get(i).getMobileno());
                intent.putExtra("evalue", partialCallLists.get(i).getExpectedValue());
                intent.putExtra("table", "Call");
                intent.putExtra("type", "Callfromcalllogs");
                intent.putExtra("starttime", starttime);
                intent.putExtra("endtime", endtime);
                intent.putExtra("duration", duration);
                intent.putExtra("rowNo", rowNo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {

            if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("overdue_collection") || Opportunity_type.equalsIgnoreCase("today_overdue_collection") || Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
                obj = getObj_OPP(Opportunity_type);
                //   txtopportunity_type.setText(Opportunity_type);

                if (isnet()) {
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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
        startPos=0;
       /* Intent intent = new Intent(OpportunityActivity_V1.this, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        OpportunityActivity_V1.this.finish();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
    }

    private void UpdatList_CollectionOPPfilter(String Firmname) {
        partialCallLists.clear();
        String query="";
        if (Filtername.equals("City")){
            query = "SELECT  FirmName,CallId," +
                    "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  CityName like '%" + Firmname + "%'";

        }else if (Filtername.equals("Firm")) {
             query = "SELECT  FirmName,CallId," +
                    "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";

        }else if (Filtername.equals("Mobile")){
             query = "SELECT  FirmName,CallId," +
                    "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  Mobile like '%" + Firmname + "%'";

        }else if (Filtername.equals("Contact")) {
             query = "SELECT  FirmName,CallId," +
                    "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  ContactName like '%" + Firmname + "%'";

        }else if (Filtername.equals("Next")){
             query = "SELECT  FirmName,CallId," +
                    "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                    " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  NextAction like '%" + Firmname + "%'";

        }


          /*  String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        */
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
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallList.setProvisionalCount(cur.getString(cur.getColumnIndex("ProvisionalCount")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setExpectedValue("");
                partialCallList.setBalVal(cur.getString(cur.getColumnIndex("BalVal")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
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
       /* String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProspectId" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);*/

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,isPartial,ProspectId"+
                " FROM " + db.TABLE_CRM_CALL_FEEDBACK + " WHERE  FirmName like '%" + Firmname + "%'";
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
                //  partialCallList.setSourceName(cur.getString(cur.getColumnIndex("SourceName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                //  partialCallList.setSourceId(cur.getString(cur.getColumnIndex("SouceId")));
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
       /* String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);*/

        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,Product,isPartial,ProspectId,Address,lat,Long,ExpectedValue" +
                " FROM " + db.TABLE_CRM_CALL_OPPORTUNITY + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                //  String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                String callId= cur.getString(cur.getColumnIndex("CallId"));
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
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setAddress(cur.getString(cur.getColumnIndex("Address")));
                partialCallList.setLat(cur.getString(cur.getColumnIndex("lat")));
                partialCallList.setLong(cur.getString(cur.getColumnIndex("Long")));
                partialCallList.setExpectedValue(cur.getString(cur.getColumnIndex("ExpectedValue")));

                int pos = -1;
                if(partialCallLists.size() != 0) {
                    for (int i = 0; i < partialCallLists.size(); i++) {
                        if (partialCallLists.get(i).getCallId().equals(callId)) {
                            pos = 1;
                        }
                    }
                    if (pos != 1) {
                        partialCallLists.add(partialCallList);
                    }
                }else{
                    partialCallLists.add(partialCallList);
                }

            } while (cur.moveToNext());
            opportunityAdapter.notifyDataSetChanged();

            startPos = 1;

           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
          /*  lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }*/
        }


    }


    private void showdialog(final int i) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_provisional_receipt_lay, null, false);
        builder = new AlertDialog.Builder(OpportunityActivity_V1.this);
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
                        new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityActivity_V1.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(OpportunityActivity_V1.this,
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

    /*@Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

      //  String text1 = edt_search_firm.getText().toString().toLowerCase(Locale.getDefault());
        String text1 = newText;

            if (Opportunity_type.contains("opp")) {
                filter(text1);
            } else if (Opportunity_type.contains("collection")) {
                UpdatList_CollectionOPPfilter(text1);
            }


        return false;
    }
*/
    public void expense(int adapterPosition, String callId, String firmname) {

        Intent intent = new Intent();
        intent.putExtra("ID", callId);
        intent.putExtra("Name",firmname);
        setResult(COUNTRY, intent);
        finish();

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
                res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);
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

            Toast.makeText(OpportunityActivity_V1.this, "Receipt save successfully", Toast.LENGTH_LONG).show();
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
                //res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);

                res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);


                if (res != null) {

                   /* response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("u0026", "&");
                    response = response.substring(1, response.length() - 1);*/

                    response = res.toString();
                    response = response.substring(1, response.length() - 1);
                    response="{\"Activity\":\""+response+"\n" +"\"}";

                    JSONObject  obj = new JSONObject(response);

                    String Msgcontent=obj.getString("Activity");

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(Msgcontent);


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
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash,ProvisionalCount,ProspectId,BalVal,NextAction" +
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
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallList.setProvisionalCount(cur.getString(cur.getColumnIndex("ProvisionalCount")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setExpectedValue("");
                partialCallList.setBalVal(cur.getString(cur.getColumnIndex("BalVal")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
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




   private void UpdatList_COllection_call_log() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CallId," +
                "NextActionDateTime,CallType,CallStatus,LatestRemark,ContactName,Mobile,CityName,isPartial,Amount,InvoiceNo,InvoiceDt,UnAllocatedCash," +
                "ProvisionalCount,ProspectId,BalVal,NextAction" +
                " FROM " + db.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY + " WHERE  Mobile ='" + contactno + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                PartialCallList partialCallList = new PartialCallList();

                if (Opportunity_type.equalsIgnoreCase("collectioncall")){
                    String dt = cur.getString(cur.getColumnIndex("NextActionDateTime"));
                    partialCallList.setActiondatetime(dt);

                    /*dt = dt.replace("T", " ");
                    if (dt.contains(Year)) {
                        dt = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM  hh:mm aa", dt);
                        partialCallList.setActiondatetime(dt);
                    } else {
                        dt = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy  hh:mm aa", dt);
                    }*/
                }
                else{
                    String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                    partialCallList.setActiondatetime(dt);
                    }
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
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setInvoiceNo(cur.getString(cur.getColumnIndex("InvoiceNo")));
                partialCallList.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                partialCallList.setInvoiceDt(cur.getString(cur.getColumnIndex("InvoiceDt")));
                partialCallList.setUnAllocatedCash(cur.getString(cur.getColumnIndex("UnAllocatedCash")));
                partialCallList.setProvisionalCount(cur.getString(cur.getColumnIndex("ProvisionalCount")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
                partialCallList.setExpectedValue("");
                partialCallList.setBalVal(cur.getString(cur.getColumnIndex("BalVal")));
                partialCallList.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            DimissProgress();
            if (partialCallLists.size() > 0) {
                scroll.setVisibility(View.VISIBLE);
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CollectionCallList(i);
                }
            }
        }

    }


    private void addView_CollectionCallList(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity_V1.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_collection_callslist_partial_lay_v1,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
       /* if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }*/
        txtfirmname = (TextView) convertView.findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
        tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
        // btn_provisional = (Button) convertView.findViewById(R.id.btn_provisional);
        btn_provisional = (ImageView) convertView.findViewById(R.id.btn_provisional);
        txt_invoice_no = (TextView) convertView.findViewById(R.id.txt_invoice_no);
        txt_amount = (TextView) convertView.findViewById(R.id.txt_amount);
        txt_invoice_date = (TextView) convertView.findViewById(R.id.txt_invoice_date);
        // len_collection= (LinearLayout) convertView.findViewById(R.id.len_collection);
        TextView txt_Unallocateamount = (TextView) convertView.findViewById(R.id.txt_Unallocateamount);
        ImageView callrating=convertView.findViewById(R.id.callrating);
        ImageView img_nextaction=convertView.findViewById(R.id.img_nextaction);


        TextView txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);


        txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView.findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
        ImageView img_provisional_count = (ImageView) convertView.findViewById(R.id.img_provisional_count);
        ImageView img_contact=convertView.findViewById(R.id.img_contact);
        CardView card_viewfill=convertView.findViewById(R.id.card_viewfill);
        card_viewfill.setPreventCornerOverlap(false);

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                //callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                //ImageViewCompat.setImageTintList(callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
                callrating.setImageResource(R.drawable.coldcall);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                callrating.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                callrating.setImageResource(R.drawable.img_warm_call);
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            callrating.setImageResource(R.drawable.coldcall);        }
        else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            callrating.setImageResource(R.drawable.coldcall);
        }
        txtfirmname.setText(partialCallLists.get(i).getFirmname()+", "+partialCallLists.get(i).getCityname());
        txtactiondatetime.setText(partialCallLists.get(i).getActiondatetime());
        if (partialCallLists.get(i).getLatestRemark().equalsIgnoreCase("")
                || partialCallLists.get(i).getLatestRemark().equalsIgnoreCase(null)
                || partialCallLists.get(i).getLatestRemark().equalsIgnoreCase("null")) {
            // tv_latestremark.setText("");
        } else {
            tv_latestremark.setText(" For " +
                    partialCallLists.get(i).getLatestRemark());
        }

      /*  String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();*/



          String Mobile = partialCallLists.get(i).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(i).getContactName();
        String Product = partialCallLists.get(i).getProduct();
        if (City.equals("null")) {
            City = "";
        }
        if (Product==null||Product.equals("null")) {
            Product = "";
        }
        String appenContact = Contactname + ", " +  Mobile;
        if (appenContact.equals(" , ")) {
            appenContact = "No Contact Available";
        }
        String appendCityProduct =  Product;
        if (appendCityProduct.equals("null")) {
            appendCityProduct = "";
        }


        final String Concatdata =appenContact;



        // String Product = partialCallLists.get(position).getProduct();
        final String Invoice_no = partialCallLists.get(position).getInvoiceNo();
        final String Amount = partialCallLists.get(position).getAmount();
        String Invoice_date = partialCallLists.get(position).getInvoiceDt();
        String[] invoiceDate = Invoice_date.split("T");
        Invoice_date = invoiceDate[0];
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Invoice_date = Invoice_date.substring(Invoice_date.indexOf("(") + 1, Invoice_date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Invoice_date);
            Date date = new Date(timestamp);
            String invoice_d = sdf.format(date);
             invoice_date = formateDateFromstring("dd MMM yyyy", "dd-MM-yyyy", invoice_d);
        }catch (Exception e){
            e.printStackTrace();
        }
        String Unallocateamount = "Unallocated Cash" + " - " + partialCallLists.get(position).getUnAllocatedCash();

        final String BalVal=partialCallLists.get(position).getBalVal();

      //  String Invoice_Date = Invoice_no + "\n" + invoice_date + "\n" + Amount;
        txt_invoice_no.setText(Invoice_no);
        txt_invoice_date.setText(invoice_date);
        final double d=Double.parseDouble(BalVal);
        final double d1=Double.parseDouble(Amount);
        txt_amount.setText(String.format("%.2f", d)+" / "+String.format("%.2f", d1));

        if (partialCallLists.get(position).getUnAllocatedCash().equals("0.0")) {
            txt_Unallocateamount.setVisibility(View.GONE);
        } else {
            txt_Unallocateamount.setText(Unallocateamount);
            txt_Unallocateamount.setVisibility(View.VISIBLE);
        }

        //String Concatdata = City + "-" + Product + "(" + Contactname + "-" + Mobile + ")";

      //  final String Concatdata = City + "-" + "(" + Contactname + "-" + Mobile + ")";

        tvcall.setText(Concatdata);

        Provisional_count = Integer.parseInt(partialCallLists.get(position).getProvisionalCount());

        String  NextAction=partialCallLists.get(i).getNextAction();
        if (NextAction.equalsIgnoreCase("Email")){
            img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.email_24));
           img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else if (NextAction.equalsIgnoreCase("Telephone")){
            img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.visit24));
           img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary
            ), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else {
            img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
        }


       /* if (Provisional_count > 0) {
            img_provisional_count.setVisibility(View.VISIBLE);
        }
*/

  /*      txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(OpportunityActivity_V1.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_Collection");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(OpportunityActivity_V1.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  Call_ProspectId = partialCallLists.get(i).getPKSuspectId();
                String Call_CallType = partialCallLists.get(i).getCallType();
                String Call_Callid = partialCallLists.get(i).getCallId();

                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/




        btn_provisional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callId = partialCallLists.get(position).getCallId();
                invoice = partialCallLists.get(position).getInvoiceNo();
                PopupMenu popup = new PopupMenu(OpportunityActivity_V1.this, v);
                popup.setOnMenuItemClickListener(OpportunityActivity_V1.this);
                popup.inflate(R.menu.menu_collectioncall);

                popup.show();


            }
        });





        /*laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Partial = partialCallLists.get(position).getIsPartial();

                if (Partial.equalsIgnoreCase("P")) {
                    Intent intent = new Intent(context, OpportunityUpdateActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("table", "Call");
                    intent.putExtra("ProspectId", partialCallLists.get(i).getPKSuspectId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }

            }
        });*/

        card_viewfill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("logupdate")) {
                    Flag = getIntent().getStringExtra("logupdate");
                    if (Flag.equalsIgnoreCase("2")) {
                        contact =getIntent().getStringExtra("callmob");
                        starttime = getIntent().getStringExtra("starttime");
                        endtime = getIntent().getStringExtra("endtime");
                        duration = getIntent().getStringExtra("duration");
                        rowNo = getIntent().getStringExtra("Rowno");
                        Intent intent = new Intent(context, ContactActivity.class);
                        intent.putExtra("callid", partialCallLists.get(i).getCallId());
                        intent.putExtra("firm", partialCallLists.get(i).getFirmname());
                        intent.putExtra("remark", partialCallLists.get(i).getLatestRemark());
                        intent.putExtra("call_type", partialCallLists.get(i).getCallType());
                        intent.putExtra("table", "Call");
                        intent.putExtra("call_prospect", partialCallLists.get(i).getPKSuspectId());
                        intent.putExtra("date", partialCallLists.get(i).getActiondatetime());
                        intent.putExtra("Provisional_count", Provisional_count);
                        intent.putExtra("status", partialCallLists.get(i).getCallStatus());
                        intent.putExtra("SourceId", partialCallLists.get(i).getSourceId());
                        intent.putExtra("call", Concatdata);
                        intent.putExtra("invoice_no", Invoice_no);
                        intent.putExtra("invoice_date", invoice_date);
                        intent.putExtra("invoice_amount", String.format("%.2f", d) + " / " + String.format("%.2f", d1));
                        intent.putExtra("action", partialCallLists.get(i).getNextAction());
                        intent.putExtra("mobile", partialCallLists.get(i).getMobileno());
                        intent.putExtra("type", "Callfromcalllogs");
                        intent.putExtra("starttime", starttime);
                        intent.putExtra("endtime", endtime);
                        intent.putExtra("duration", duration);
                        intent.putExtra("rowNo", rowNo);
                        if (Flag.equalsIgnoreCase("2")) {
                            intent.putExtra("callmob", contact);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                    }
                }else {
                    Intent intent = new Intent(context, CRM_Collection_List.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firm", partialCallLists.get(i).getFirmname());
                    intent.putExtra("remark", partialCallLists.get(i).getLatestRemark());
                    intent.putExtra("call_type", partialCallLists.get(i).getCallType());
                    intent.putExtra("table", "Call");
                    intent.putExtra("call_prospect", partialCallLists.get(i).getPKSuspectId());
                    intent.putExtra("date", partialCallLists.get(i).getActiondatetime());
                    intent.putExtra("Provisional_count", Provisional_count);
                    intent.putExtra("status", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("SourceId", partialCallLists.get(i).getSourceId());
                    intent.putExtra("call", Concatdata);
                    intent.putExtra("invoice_no", Invoice_no);
                    intent.putExtra("invoice_date", invoice_date);
                    intent.putExtra("invoice_amount", String.format("%.2f", d) + " / " + String.format("%.2f", d1));
                    intent.putExtra("action", partialCallLists.get(i).getNextAction());
                    intent.putExtra("mobile", partialCallLists.get(i).getMobileno());
                    if (Opportunity_type.equalsIgnoreCase("collectioncall")) {
                        intent.putExtra("type", "Callfromcalllogs");
                        intent.putExtra("starttime", starttime);
                        intent.putExtra("endtime", endtime);
                        intent.putExtra("duration", duration);
                        intent.putExtra("rowNo", rowNo);
                    } else if (Opportunity_type.equalsIgnoreCase("collection")) {
                        if (getIntent().hasExtra("logupdate")) {
                            intent.putExtra("type", "Callfromcalllogs");
                            Flag = getIntent().getStringExtra("logupdate");
                            contact = getIntent().getStringExtra("callmob");
                            starttime = getIntent().getStringExtra("starttime");
                            endtime = getIntent().getStringExtra("endtime");
                            duration = getIntent().getStringExtra("duration");
                            rowNo = getIntent().getStringExtra("Rowno");
                        }
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                }
            }
        });

        lsCall_list.addView(convertView);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.provisionalreceipt:
                //callId = partialCallLists.get(position).getCallId();
                Intent intent = new Intent(OpportunityActivity_V1.this, ProvisionalActivity.class);
                intent.putExtra("Call_id", callId);
                intent.putExtra("procount", Provisional_count);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.discountvoucher:
                // callId = partialCallLists.get(position).getCallId();
                getDiscountVoucherdialog(callId, invoice);
                return true;
            case R.id.EditProspect:

                getEditDetailsProspect();
                return true;
            case R.id.EditOpportunity:
                callId = partialCallLists.get(rowClickPos).getCallId();

                startActivity(new Intent(OpportunityActivity_V1.this, CreateOpportunityActivity.class).putExtra("SuspectID", ProspectPkSusId).
                        putExtra("CallId",callId));
                return true;

            case R.id.ViewDetails:
                callId = partialCallLists.get(rowClickPos).getCallId();
                String Partial = partialCallLists.get(rowClickPos).getIsPartial();

                Intent i = new Intent(OpportunityActivity_V1.this, CallHistoryActivity.class);
                i.putExtra("callid", partialCallLists.get(rowClickPos).getCallId());
                i.putExtra("firmname", partialCallLists.get(rowClickPos).getFirmname());
                i.putExtra("calltype", partialCallLists.get(rowClickPos).getCallType());
                i.putExtra("ProspectId", partialCallLists.get(rowClickPos).getPKSuspectId());
                i.putExtra("SourceId", partialCallLists.get(rowClickPos).getSourceId());
                i.putExtra("table", "Call");
                i.putExtra("type", "Callfromcalllogs");
                i.putExtra("MobileCalltype", Opportunity_type);
                i.putExtra("starttime", starttime);
                i.putExtra("endtime", endtime);
                i.putExtra("duration", duration);
                i.putExtra("rowNo", rowNo);
                startActivity(i);
                return true;

            /*    String callId = partialCallLists.get(rowClickPos).getCallId();
                String Partial = partialCallLists.get(rowClickPos).getIsPartial();
                pos1 = oppPosition;
                if (Partial.equalsIgnoreCase("P")) {

                    Intent intent1 = new Intent(OpportunityActivity_V1.this, CallListActionActivity.class);
                    intent1.putExtra("callid", partialCallLists.get(rowClickPos).getCallId());
                    intent1.putExtra("firmname", partialCallLists.get(rowClickPos).getFirmname());
                    intent1.putExtra("calltype", partialCallLists.get(rowClickPos).getCallType());
                    intent1.putExtra("ProspectId", partialCallLists.get(rowClickPos).getPKSuspectId());
                    intent1.putExtra("SourceId", partialCallLists.get(rowClickPos).getSourceId());
                    intent1.putExtra("table", "Call");
                    intent1.putExtra("type", "Callfromcalllogs");
                    intent1.putExtra("MobileCalltype", Opportunity_type);
                    intent1.putExtra("starttime", starttime);
                    intent1.putExtra("endtime", endtime);
                    intent1.putExtra("duration", duration);
                    intent1.putExtra("rowNo", rowNo);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }*/


            default:
                return false;
        }
    }

    private void getEditDetailsProspect() {
        if (isnet()) {
            new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadContactFetchData().execute(ProspectPkSusId);

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });

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
            e.printStackTrace();
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
                res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);
                /* res = ut.OpenPostConnection_test(url, params[0]);*/
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

/*
                "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,isPartial,ProspectId,SourceName,SouceId"
*/
         String query = "SELECT  FirmName,CallId," +
        "NextActionDateTime,CallType,LatestRemark,ContactName,Mobile,CityName,isPartial,ProspectId"+
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
              //  partialCallList.setSourceName(cur.getString(cur.getColumnIndex("SourceName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                //partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallList.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                //  partialCallList.setProduct(cur.getString(cur.getColumnIndex("Product")));
                partialCallList.setPKSuspectId(cur.getString(cur.getColumnIndex("ProspectId")));
              //  partialCallList.setSourceId(cur.getString(cur.getColumnIndex("SouceId")));
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

        LayoutInflater layoutInflater = (LayoutInflater) OpportunityActivity_V1.this
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
      //  TextView txt_srcname = (TextView) convertView.findViewById(R.id.txt_srcnname);


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
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCustomerIdData().execute(callId);
                        }
                    });
                }

            }
        });
*/


        txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView.findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type1);
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
        String remark = partialCallLists.get(i).getLatestRemark();
        if (remark.equalsIgnoreCase("") || remark.equalsIgnoreCase(" ") || remark.equalsIgnoreCase(null)) {
            tv_latestremark.setText("");
        } else {
            tv_latestremark.setText(" For " +
                    partialCallLists.get(i).getLatestRemark());
        }

        String Mobile = partialCallLists.get(position).getMobileno();
        String City = partialCallLists.get(position).getCityname();
        String Contactname = partialCallLists.get(position).getContactName();
        // String Product = partialCallLists.get(position).getProduct();
        String SourceName = partialCallLists.get(position).getSourceName();

      /*  if (SourceName.equalsIgnoreCase("") || SourceName.equalsIgnoreCase(" ")
                || SourceName.equalsIgnoreCase(null) || SourceName.equalsIgnoreCase("null")) {
            txt_srcname.setText("");
        } else {
            txt_srcname.setText(" From " + partialCallLists.get(i).getSourceName());
        }*/

        String Concatdata = City + "-" + "" + "(" + Contactname + "-" + Mobile + ")";


        tvcall.setText(Concatdata);

        if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("")) {
            tvcall.setText("(No Contact Available)");
        } else {
            if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {
                tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
            } else {
                tvcall.setText("(" + partialCallLists.get(i).getContactName() + " - "
                        + partialCallLists.get(i).getMobileno() + ")");
            }

        }

        //Collection Call Save Code


       /* img_appotunity_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OpportunityActivity_V1.this, CallRatingActivity.class);
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

                    Intent intent = new Intent(OpportunityActivity_V1.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("ProspectId", partialCallLists.get(position).getPKSuspectId());
                    intent.putExtra("SourceId", partialCallLists.get(position).getSourceId());

                    intent.putExtra("table", "Call");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
/*
                if (Partial.equalsIgnoreCase("P")) {
                *//*    Intent intent = new Intent(CallListActivity.this, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(i).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(i).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(i).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(i).getCallStatus());
                    intent.putExtra("table", "Call");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                *//*


                    //                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if (isnet()) {
                        new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                }*/


               /* if (db.getCallListOppcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_OPP, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(OpportunityActivity_V1.this, CallListActionActivity.class);
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
                if (IsChatApplicable.equalsIgnoreCase("true")) {
                    String Call_CallType = partialCallLists.get(i).getCallType();
                    String Call_Callid = partialCallLists.get(i).getCallId();
                    String Firm_name = partialCallLists.get(i).getFirmname();
                    Intent intent = new Intent(OpportunityActivity_V1.this, MultipleGroupActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_type", "Crm_feedback");
                    intent.putExtra("firm", Firm_name);
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    //intent.putExtra("chatCount",ChatCount);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(OpportunityActivity_V1.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(OpportunityActivity_V1.this, "Amount is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_instrument_no.getText().toString().equalsIgnoreCase("") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(" ") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(OpportunityActivity_V1.this, "Instrument No is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(OpportunityActivity_V1.this, "Bank name is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_deposite_bank.getText().toString().equalsIgnoreCase("") ||
                edt_deposite_bank.getText().toString().equalsIgnoreCase(" ") ||
                edt_deposite_bank.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(OpportunityActivity_V1.this, "Bank name is required", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private void getDiscountVoucherdialog(final String call_id, final String invoice_no) {
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.crm_discount_lay, null, false);
        builder = new AlertDialog.Builder(OpportunityActivity_V1.this);
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
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(call_id,invoice_no,Reversal_amount,reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(OpportunityActivity_V1.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
        builder = new AlertDialog.Builder(OpportunityActivity_V1.this);
        builder.setView(v);
        alertDialog1 = builder.create();


        spinner_approver = (Spinner) v.findViewById(R.id.spinner_approver);
        TextView txt_save = (TextView) v.findViewById(R.id.txt_save);
        TextView txt_cancel = (TextView) v.findViewById(R.id.txt_cancel);

        alertDialog.dismiss();


        if (isnet()) {
            new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
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
                    new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDiscountVoucher().execute(ApprId, call_id, invoice_no, reversal_amount, reason);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(OpportunityActivity_V1.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(OpportunityActivity_V1.this, "Discount Voucher is Successfully sent for Approval", Toast.LENGTH_LONG).show();
                alertDialog1.dismiss();
            } else {
                Toast.makeText(OpportunityActivity_V1.this, "Please try again", Toast.LENGTH_LONG).show();

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
                spinner_approver.setAdapter(new ArrayAdapter<String>(OpportunityActivity_V1.this,
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


    class DownloadContactFetchData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_getFillContDet + "?PKSuspectId=" + params[0];
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
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

            try {
                JSONArray jResults = null;
                jResults = new JSONArray(response);
                ContentValues values = new ContentValues();

                sql.delete(db.TABLE_CONTACT_DETAILS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CONTACT_DETAILS, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        if (columnValue.equalsIgnoreCase(null)) {
                            columnValue = "";
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_CONTACT_DETAILS, null, values);
                    Log.e("log data", "" + a);
                }

                new DownloadProductFetchData().execute(ProspectPkSusId);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    //data getting is blank
    class DownloadProductFetchData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_getFillFamilyDet + "?PKSuspectId=" + params[0];
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
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

            try {
                JSONArray jResults = null;
                jResults = new JSONArray(response);
                ContentValues values = new ContentValues();

                sql.delete(db.TABLE_PRODUCT_DATA_FETCH, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        if (columnValue.equalsIgnoreCase(null)) {
                            columnValue = "";
                        }
                        values.put(columnName, columnValue);

                        if (columnName.equals("BusinessDetails")) {
                            businessDetails = columnValue;
                        } else if (columnName.equals("CompanyURL")) {
                            website = columnValue;
                        } else if (columnName.equals("FKEnqSourceId")) {
                            sourceOfProspect = columnValue;
                        }

                    }

                    long a = sql.insert(db.TABLE_PRODUCT_DATA_FETCH, null, values);
                    Log.e("log data", "" + a);
                }

                new DownloadFillcontrolFetchData().execute(ProspectPkSusId);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

    class DownloadFillcontrolFetchData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_GetFillControls + "?PKSuspectId=" + params[0];
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
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

            try {
                JSONArray jResults = null;
                jResults = new JSONArray(response);
                ContentValues values = new ContentValues();

                sql.delete(db.TABLE_FILLCONTROL_DATA_FETCH, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_FILLCONTROL_DATA_FETCH, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        if (columnValue.equalsIgnoreCase(null)) {
                            columnValue = "";
                        }
                        values.put(columnName, columnValue);

                        if (columnName.equals("FirmName")) {
                            firmName = columnValue;
                        } else if (columnName.equals("FirmAlias")) {
                            firmAlias = columnValue;
                        } else if (columnName.equals("Address")) {
                            address = columnValue;
                        } else if (columnName.equals("FKCountryId")) {
                            countryId = columnValue;
                        } else if (columnName.equals("FKStateId")) {
                            stateId = columnValue;
                        } else if (columnName.equals("FKCityId")) {
                            cityId = columnValue;
                        } else if (columnName.equals("FKTerritoryId")) {
                            territoryId = columnValue;
                        } else if (columnValue.equals("Remark")) {
                            remark = columnValue;
                        } else if (columnName.equals("FKBusiSegmentId")) {
                            businessSegmentId = columnValue;
                        }

                    }


                    long a = sql.insert(db.TABLE_FILLCONTROL_DATA_FETCH, null, values);
                    Log.e("log data", "" + a);
                }

                new DownloadProspectTypeId().execute(ProspectPkSusId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    class DownloadProspectTypeId extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_GetProspectTypeID + "?PKSuspectId=" + params[0];
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
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

            if (response.equalsIgnoreCase("1")) {
                Intent intent = new Intent(OpportunityActivity_V1.this, ProspectEnterpriseActivity.class);
                intent.putExtra("keymode", "Edit");
                intent.putExtra("PKSuspectId", ProspectPkSusId);
                intent.putExtra("firmname", firmName);
                intent.putExtra("firmAlias", firmAlias);
                intent.putExtra("address", address);
                intent.putExtra("FKCountryId", countryId);
                intent.putExtra("FKStateId", stateId);
                intent.putExtra("FKCityId", cityId);
                intent.putExtra("FKTerritoryId", territoryId);
                intent.putExtra("Age", remark);
                intent.putExtra("BusinessSegmentId", businessSegmentId);
                intent.putExtra("website", website);/***********************/
                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                intent.putExtra("businessDetails", businessDetails);
                //intent.putExtra("",CityName);
                Log.e("Api response", response);
                System.out.println("Firm" + Firmname);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (response.equalsIgnoreCase("2")) {
                Intent intent = new Intent(OpportunityActivity_V1.this, BusinessProspectusActivity.class);
                intent.putExtra("PKSuspectId", ProspectPkSusId);
                intent.putExtra("firmname", firmName);
                intent.putExtra("FKCountryId", countryId);
                intent.putExtra("FKStateId", stateId);
                intent.putExtra("FKCityId", cityId);
                intent.putExtra("FKTerritoryId", territoryId);
                intent.putExtra("keymode", "Edit");
                intent.putExtra("BusinessSegmentId", businessSegmentId);
                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                intent.putExtra("Age", remark);
                System.out.println("Firm" + Firmname);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (response.equalsIgnoreCase("3")) {
                Intent intent = new Intent(OpportunityActivity_V1.this, IndividualProspectusActivity.class);
                intent.putExtra("keymode", "Edit");
                intent.putExtra("PKSuspectId", ProspectPkSusId);
                intent.putExtra("firmname", firmName);
                System.out.println("Firm" + firmName);
                intent.putExtra("FKCountryId", countryId);
                intent.putExtra("FKStateId", stateId);
                intent.putExtra("FKCityId", cityId);
                intent.putExtra("FKTerritoryId", territoryId);
                intent.putExtra("Address", address);
                intent.putExtra("Age", remark);
                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                //intent.putExtra("",)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
            }else {
                Intent intent = new Intent(OpportunityActivity_V1.this, ProspectEnterpriseActivity.class);
                intent.putExtra("keymode", "Edit");
                intent.putExtra("PKSuspectId", ProspectPkSusId);
                intent.putExtra("firmname", firmName);
                intent.putExtra("firmAlias", firmAlias);
                intent.putExtra("address", address);
                intent.putExtra("FKCountryId", countryId);
                intent.putExtra("FKStateId", stateId);
                intent.putExtra("FKCityId", cityId);
                intent.putExtra("FKTerritoryId", territoryId);
                intent.putExtra("Age", remark);
                intent.putExtra("BusinessSegmentId", businessSegmentId);
                intent.putExtra("website", website);/***********************/
                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                intent.putExtra("businessDetails", businessDetails);
                //intent.putExtra("",CityName);
                Log.e("Api response", response);
                System.out.println("Firm" + Firmname);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


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
                res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);

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
            DimissProgress();
            if (response.contains("CallId")) {
                UpdatList_OPP();

            } else if (response.contains("error")) {

            }

        }

    }

    class DownloadOppDataURLJSON_Paging extends AsyncTask<String, Void, String> {
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
            String url = CompanyURL + WebUrlClass.api_POST_CRMCallListPaging;

            try {
                res = ut.OpenPostConnection(url, params[0], OpportunityActivity_V1.this);
                if (res != null) {

                    response = res.toString();

                    /*replaceAll("\\\\", "");
                  response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);*/

                    int countRes = response.length();

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    if (jResults.length() >= 10) {

                    } else {
                        EndofAPILoop = 1;
                    }

                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL, null,
                            null);
                    if (startPos == 0) {
                        sql.delete(db.TABLE_CRM_CALL_OPPORTUNITY, null, null);
                    }
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_OPPORTUNITY, null);
                    int count = c.getCount();
                    String columnName, columnValue = "";
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                //2020-09-17T09:00:00
                                // String spiltJsonObj = jsonDOB.replace("T"," ");
                                jsonDOB = jsonDOB.replace("T", " ");
                                // jsonDOB = spiltJsonObj[0];//2020-01-20

                                if (jsonDOB.contains(Year)) {
                                    jsonDOB = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM  hh:mm aa", jsonDOB);
                                }else {
                                    jsonDOB = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy  hh:mm aa", jsonDOB);

                                }

                         /*  jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonDOB);
                            DOBDate = new Date(DOB_date);
                            jsonDOB = sdf.format(DOBDate);*/

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
/*
                    if (cf.CheckifRecordPresentCRM(db.TABLE_CRM_CALL_OPPORTUNITY, "CallId",columnValue)) {
                      //  cf.AddGroupMessage(chatMessage);

                    }*/


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
            DimissProgress();
            if (response.contains("CallId")) {
                UpdatList_OPP();

            } else if (response.contains("error")) {

            }

        }

    }

    public void loadNextActivity(int lastPos) {

        /*int rowStrt = Integer.parseInt(rowStart);
        int rowend = Integer.parseInt(rowEnd);*/

        if(EndofAPILoop == 1){

        }else{
            rowStart = lastPos;
            rowEnd = lastPos + 9;

            obj = getObj_OPP(Opportunity_type);


            if (isnet()) {
                new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOppDataURLJSON_Paging().execute(obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(OpportunityActivity_V1.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(OpportunityActivity_V1.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


    }


    public void ChatClick(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {

        int i = adapterPosition;
        if (IsChatApplicable.equalsIgnoreCase("true")) {
            String Call_CallType = partialCallListArrayList.get(i).getCallType();
            String Call_Callid = partialCallListArrayList.get(i).getCallId();
            String Firm_name = partialCallListArrayList.get(i).getFirmname();
            Intent intent = new Intent(OpportunityActivity_V1.this, MultipleGroupActivity.class);
            intent.putExtra("callid", Call_Callid);
            intent.putExtra("call_type", "Crm_Opportunity");
            intent.putExtra("firm", Firm_name);
            intent.putExtra("projmasterId", "");
            intent.putExtra("AssignBy", UserName);
            intent.putExtra("AssignById", UserMasterId);
            //intent.putExtra("chatCount",ChatCount);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            Toast.makeText(OpportunityActivity_V1.this, "Chat module not installed", Toast.LENGTH_SHORT).show();
        }

    }

    public void OpportunityUpdate(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

        Intent intent = new Intent(OpportunityActivity_V1.this, CallRatingActivity.class);
        intent.putExtra("callid", partialCallListArrayList.get(position).getCallId());
        intent.putExtra("callstatus", partialCallListArrayList.get(position).getCallStatus());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void tvCallMethd(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

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


    public void ActionClick(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList, View v) {
        rowClickPos = adapterPosition;

        callId = partialCallListArrayList.get(rowClickPos).getCallId();
        invoice = partialCallListArrayList.get(rowClickPos).getInvoiceNo();
        ProspectPkSusId = partialCallListArrayList.get(rowClickPos).getPKSuspectId();
        String oppCity = partialCallListArrayList.get(rowClickPos).getCityname();
        PopupMenu popup = new PopupMenu(OpportunityActivity_V1.this, v);
        popup.setOnMenuItemClickListener(OpportunityActivity_V1.this);
        popup.inflate(R.menu.menu_opportunitycall);

        popup.show();
    }

    private void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        partialCallLists.clear();
        if (charText.length() == 0) {
            partialCallLists.addAll(filterTempList);
        } else {
            for (PartialCallList wp : filterTempList) {
                if (Filtername.equals("City")){
                    if (wp.getCityname().toLowerCase(Locale.getDefault()).contains(charText)) {
                        partialCallLists.add(wp);
                    }
                }else if (Filtername.equals("Firm")){
                    if (wp.getFirmname().toLowerCase(Locale.getDefault()).contains(charText)) {
                        partialCallLists.add(wp);
                    }
                }else if (Filtername.equals("Mobile"))
                    if (wp.getMobileno().toLowerCase(Locale.getDefault()).contains(charText)) {
                    partialCallLists.add(wp);
                }else if (Filtername.equals("Product")){
                    if (wp.getProduct().toLowerCase(Locale.getDefault()).contains(charText)) {
                        partialCallLists.add(wp);
                    }
                }else if (Filtername.equals("Contact")){
                    if (wp.getContactName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        partialCallLists.add(wp);
                    }
                }else if (Filtername.equals("Next")){
                        if (wp.getNextAction().toLowerCase(Locale.getDefault()).contains(charText)) {
                            partialCallLists.add(wp);
                        }
                    }

            }
        }
        opportunityAdapter.notifyDataSetChanged();
    }



    // Seachview ligic change

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        /*searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
       */




        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text1 = newText;
              //  txtopportunity_type.setVisibility(View.GONE);
                if (newText.equalsIgnoreCase("")) {
                    txtopportunity_type.setVisibility(View.VISIBLE);
                    lay_reveal.setVisibility(View.GONE);
                    len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                    if (Opportunity_type.contains("opp")) {
                        filter("");
                    } else if (Opportunity_type.contains("collection")) {
                        UpdatList_CollectionOPPfilter("");
                    }
                }else {
                    lay_reveal.setVisibility(View.VISIBLE);
                    len_firm.setBackground(getResources().getDrawable(R.drawable.whtsaap_button_background_white));
                    Filtername = "Firm";

                    if (Opportunity_type.contains("opp")) {
                        filter(text1);
                    } else if (Opportunity_type.contains("collection")) {
                        UpdatList_CollectionOPPfilter(text1);
                    }

                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void callstartagain(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

        Call_ID=partialCallListArrayList.get(position).getCallId();

        if (ut.isNet(OpportunityActivity_V1.this)) {
            showProgressDialog();
            new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetRestartData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


    }

    public void callignore(int adapterPosition, ArrayList<PartialCallList> partialCallListArrayList) {
        int position = adapterPosition;

        Call_ID=partialCallListArrayList.get(position).getCallId();

        if (ut.isNet(OpportunityActivity_V1.this)) {
            showProgressDialog();
            new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetIgnoreData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }


    }


    class DownloadGetRestartData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetRestartCallOnebyOne + "?CallId=" +
                        Call_ID;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
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
            dismissProgressDialog();
            if (response.equals("[]")) {
                Toast.makeText(OpportunityActivity_V1.this, "Record not save successfully", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                    Toast.makeText(OpportunityActivity_V1.this, "Record save successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, CRMHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);


                }

            }
        }
    }
    class DownloadGetIgnoreData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetIgnoreCallOnebyOne + "?CallId=" +
                        Call_ID;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
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
            dismissProgressDialog();
            if (response.equals("[]")) {
                Toast.makeText(OpportunityActivity_V1.this, "Record not save successfully", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                    Toast.makeText(OpportunityActivity_V1.this, "Record save successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, CRMHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);


                }

            }
        }
    }

    public String Call_Review(String callId) {

        Call_ID=callId;

        if (isnet()) {
            new StartSession(OpportunityActivity_V1.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetCallReview().execute(Call_ID);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        return Response_Call ;
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
                res = ut.OpenConnection(url, getApplicationContext());
                Response_Call = res.toString();
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

}



