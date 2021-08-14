package com.vritti.sales.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vritti.SaharaModule.SplashActivity;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.MerchantPaymentHistoryAdapter;
import com.vritti.sales.beans.MerchantPaymentHistory;
import com.vritti.sales.beans.ShipmentEntryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

public class CollectionReceiptActivity extends AppCompatActivity {
    Context parent;
    ProgressHUD progress;
    public static final String MyPREFERENCES = "LoginPrefs";
    SharedPreferences sharedpreferences;

    private DatabaseHandlers dbHandler;
    SQLiteDatabase sql_db;
    DatabaseHandlers databaseHelper;

    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    String CustVendorMasterId, CustomerID;
    private UUID uuid;
    String uuidInString = "",attachment = "";
    ImageView img_from_date,img_to_date;
    RadioGroup radgroup;
    RadioButton rdbtn_mycoll,rdbtn_allcoll;
    LinearLayout showdialog;
    private String FromDate,Todate;
    RecyclerView recyclerView;
    TextView txt_record,txt_fromdate,txt_todate,txt_submit;
    private MerchantPaymentHistoryAdapter historyAdapter;
    ArrayList<MerchantPaymentHistory> merchantPaymentHistoryArrayList;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private static DatabaseHandlers db;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "", OrderType = "";

    boolean isAllColl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_report_lay);
     //   Smartlook.setupAndStartRecording(getResources().getString(R.string.smartlook));

        parent = CollectionReceiptActivity.this;

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+"Collection Receipt"+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        recyclerView=findViewById(R.id.assitant_response);
        txt_record=findViewById(R.id.txt_record);
        txt_fromdate=findViewById(R.id.txt_fromdate);
        txt_todate=findViewById(R.id.txt_todate);
        txt_submit=findViewById(R.id.txt_submit);
        img_from_date=findViewById(R.id.img_from_date);
        img_to_date=findViewById(R.id.img_to_date);
        showdialog=findViewById(R.id.showdialog);
        showdialog.setVisibility(View.GONE);
        rdbtn_allcoll=findViewById(R.id.rdbtn_allcoll);
        rdbtn_mycoll=findViewById(R.id.rdbtn_mycoll);
        radgroup=findViewById(R.id.radgroup);
        radgroup.setVisibility(View.VISIBLE);

        merchantPaymentHistoryArrayList=new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dbHandler = new DatabaseHandlers(CollectionReceiptActivity.this);
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO", null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO", null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL", null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId", null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId", null);
        AnyMartDatabaseConstants.DATABASE__NAME_URL = sharedpreferences.getString("DatabaseName", null);
        AnyMartData.MOBILE = restoredText;

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CollectionReceiptActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql_db = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        databaseHelper = new DatabaseHandlers(CollectionReceiptActivity.this, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        sql_db = databaseHelper.getWritableDatabase();

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        txt_fromdate.setText(sdf.format(cal.getTime()));
        txt_todate.setText(dateString);

        img_from_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CollectionReceiptActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                FromDate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;
                                txt_fromdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        img_to_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CollectionReceiptActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                Todate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;
                                txt_todate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FromDate=txt_fromdate.getText().toString();
                Todate=txt_todate.getText().toString();

                FromDate=formateDateFromstring("dd-MM-yyyy","yyyy-MM-dd",FromDate);
                Todate=formateDateFromstring("dd-MM-yyyy","yyyy-MM-dd",Todate);

                if (NetworkUtils.isNetworkAvailable(CollectionReceiptActivity.this)) {
                    showdialog.setVisibility(View.VISIBLE);
                    new StartSession_tbuds(CollectionReceiptActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadReportList().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        });

        rdbtn_allcoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isAllColl = true;
                if(isAllColl == true){
                    rdbtn_allcoll.setSelected(true);
                    rdbtn_mycoll.setSelected(false);
                }else {
                    rdbtn_allcoll.setSelected(false);
                    rdbtn_mycoll.setSelected(true);
                }

                new DownloadReportList().execute();
            }
        });

        rdbtn_mycoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAllColl = false;
                if(isAllColl == true){
                    rdbtn_allcoll.setSelected(true);
                    rdbtn_mycoll.setSelected(false);
                }else {
                    rdbtn_allcoll.setSelected(false);
                    rdbtn_mycoll.setSelected(true);
                }

                new DownloadReportList().execute();
            }
        });

    }

    class DownloadReportList extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                /*progress = ProgressHUD.show(CollectionReceiptActivity.this,
                        ""+getResources().getString(R.string.loading),
                        false, true, null);*/
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = AnyMartData.MAIN_URL + AnyMartData.Get_CollectionReceiptData +"?FromDate="+FromDate
                    +"&ToDate="+Todate+"&MerchId="+CustomerID;
            try {
                res = Utility.OpenconnectionOrferbilling(url, CollectionReceiptActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                //  response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            try{
                //progress.dismiss();
                showdialog.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (response.equalsIgnoreCase("[]")||response.equalsIgnoreCase("No Data")){
                txt_record.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                if (response != null) {
                    JSONArray jResults = new JSONArray();
                    try {
                        merchantPaymentHistoryArrayList.clear();
                        recyclerView.setVisibility(View.VISIBLE);
                        txt_record.setVisibility(View.GONE);
                        jResults = new JSONArray(response);
                        ContentValues values = new ContentValues();

                        if (jResults.length() > 0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jsonObject = jResults.getJSONObject(i);
                                MerchantPaymentHistory merchantPaymentHistory = new MerchantPaymentHistory();
                                merchantPaymentHistory.setInvoiceNo(jsonObject.getString("InvoiceNo"));
                                merchantPaymentHistory.setActivityName(jsonObject.getString("ActivityName"));
                                merchantPaymentHistory.setAmount(jsonObject.getString("ChargedAmount"));
                                merchantPaymentHistory.setModifiedDt(jsonObject.getString("ModifiedDt"));
                                merchantPaymentHistory.setAddeddate(jsonObject.getString("Addeddt"));
                                merchantPaymentHistory.setCustomerName(jsonObject.getString("CustVendorName"));
                                merchantPaymentHistory.setPaymentMode(jsonObject.getString("Transactionid"));
                                merchantPaymentHistory.setApprovedAmount(jsonObject.getString("ApprovedAmount"));
                                merchantPaymentHistory.setDiscountAmount(jsonObject.getString("DiscountAmount"));
                                merchantPaymentHistory.setBalanceAmount(jsonObject.getString("BalanceAmount"));
                                try{
                                    merchantPaymentHistory.setAddedbyid(jsonObject.getString("usermasterid"));
                                    merchantPaymentHistory.setAddedbyname(jsonObject.getString("UserName"));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                              //  merchantPaymentHistoryArrayList.add(merchantPaymentHistory);

                                try{
                                    String adby = jsonObject.getString("UserName");

                                    if(isAllColl == true){
                                        merchantPaymentHistoryArrayList.add(merchantPaymentHistory);
                                    }else {
                                        if(adby.equals(UserName)){
                                            merchantPaymentHistoryArrayList.add(merchantPaymentHistory);
                                        }else {

                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    merchantPaymentHistoryArrayList.add(merchantPaymentHistory);
                                }

                                
                            }
                        }

                        Collections.sort(merchantPaymentHistoryArrayList, new Comparator<MerchantPaymentHistory>() {
                            @Override
                            public int compare(MerchantPaymentHistory lhs, MerchantPaymentHistory rhs) {
                                return rhs.getAddeddate().compareTo(lhs.getAddeddate());
                            }
                        });

                        historyAdapter=new MerchantPaymentHistoryAdapter(CollectionReceiptActivity.this,merchantPaymentHistoryArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(historyAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
            e.printStackTrace();

        }

        return outputDate;

    }

}
