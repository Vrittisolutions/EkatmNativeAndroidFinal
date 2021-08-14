package com.vritti.sales.activity;

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
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.mp4parser.authoring.Edit;
import com.vritti.crm.vcrm7.AdvanceProvisionalActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.BatchSelectionActivity;
import com.vritti.inventory.physicalInventory.activity.PIEntryPrintingActivity;
import com.vritti.sales.adapters.CustomerSelectionListAdapter;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SalesHeaderActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    AutoCompleteTextView edt_custname, edt_consignee, edt_billto, edt_ponumber, edt_sename;
    EditText edt_remark;
    EditText edtstartdate, edtenddate, edtpodate, edtsodate;
    Button btncontinue;
    ImageView imgdateso,imgdatepo, imgdatestart, imgdatend;
    LinearLayout lay_pro_apprvr;
    ProgressBar progressBar;
    TextInputLayout selproject, selapprvr;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    public static final int SALES_HEADER_FILLED = 1;
    public static final int SALES_ITEM_FILLED = 2;
    public static final int CUSTOMERDATA = 8;

    String SONO = "";
    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;

    String CustVendorMasterId = "", CustName = "";
    ArrayList<Customer> listCust;
    ArrayList<String> listCustString;
    boolean isCustSelected;
    String  AllowbackdatedSO = "", IsProjectOrderApp = "N", SOApprMthId = "";

    String CustomerId = "",ConsigneeName = "",ConsigneeId="",BilltoId= "",SENameId="",SEName="";

    JSONObject jobj_header, jHDR_obj;
    JSONArray jArray,jHDRArr;
    String finalOBJ = "";

    String Mode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_header);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //get value of SoApprMethod, IsProjectSelect, AllowBackDateSO
        try{
            getValidation(AnyMartData.OrderTypeMasterId);
        }catch (Exception e){
            e.printStackTrace();
        }

        setListeners();
    }

    private void init() {
        parent = SalesHeaderActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("SO Header");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        edt_custname = findViewById(R.id.edt_custname);
        edt_custname.setSelection(edt_custname.getText().toString().length());
        edt_consignee = findViewById(R.id.edt_consignee);
        edt_consignee.setSelection(edt_consignee.getText().toString().length());
        edt_billto = findViewById(R.id.edt_billto);
        edt_billto.setSelection(edt_billto.getText().toString().length());
        edt_ponumber = findViewById(R.id.edt_ponumber);
        edt_sename = findViewById(R.id.edt_sename);
        edt_remark = findViewById(R.id.edt_remark);
        edtstartdate = findViewById(R.id.edtstartdate);
        edtenddate = findViewById(R.id.edtenddate);
        edtpodate = findViewById(R.id.edtpodate);
        edtsodate = findViewById(R.id.edtsodate);
        imgdateso = findViewById(R.id.imgdateso);
        imgdatepo = findViewById(R.id.imgdatepo);
        imgdatestart = findViewById(R.id.imgdatestart);
        imgdatend = findViewById(R.id.imgdatend);
        btncontinue = findViewById(R.id.btncontinue);
        lay_pro_apprvr = findViewById(R.id.lay_pro_apprvr);
        //lay_pro_apprvr.setVisibility(View.GONE);
        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        selapprvr = findViewById(R.id.selapprvr);
        selproject = findViewById(R.id.selproject);

        Intent intent = getIntent();
        AnyMartData.Order_Type = intent.getStringExtra("OrderType");
        AnyMartData.OrderTypeMasterId = intent.getStringExtra("OrderTypeMasterId");
        Mode = intent.getStringExtra("Mode");

        ut = new Utility();
        cf = new CommonFunction(SalesHeaderActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesHeaderActivity.this);
        String dabasename = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        CompanyURL = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesHeaderActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesHeaderActivity.this, dabasename);
        sql = db.getWritableDatabase();

        SEName = UserName;
        SENameId = UserMasterId;

        listCust = new ArrayList<Customer>();
        listCustString = new ArrayList<String>();

        edt_sename.setText(UserName);
        toolbar.setTitle("SO Header - "+AnyMartData.Order_Type);

        if(Mode.equalsIgnoreCase("Edit")){
            String json = intent.getStringExtra("jHDRArray");
            setValuesToViews(json);
        }else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            date = String.format("%2d",day) + "/" + String.format("%02d", (month + 1)) + "/" + year;
            edtsodate.setText(date);
            edtpodate.setText(date);
        }

        /*Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        time = updateTime(hour, minute);
        TIME = time;*/

    }

    private void setListeners() {
        edt_custname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,Cust_Consignee_billto_ListActivity.class);
                startActivityForResult(intent,CUSTOMERDATA);
            }
        });

        edt_consignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,Cust_Consignee_billto_ListActivity.class);
                startActivityForResult(intent,CUSTOMERDATA);
            }
        });

        edt_billto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,Cust_Consignee_billto_ListActivity.class);
                startActivityForResult(intent,CUSTOMERDATA);
            }
        });

        imgdateso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    edtsodate.setText(date);
                                } else {
                                    edtsodate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        imgdatepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    edtpodate.setText(date);
                                } else {
                                    edtpodate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        imgdatestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    edtstartdate.setText(date);
                                } else {
                                    edtstartdate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        imgdatend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    edtenddate.setText(date);
                                } else {
                                    edtenddate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    jArray = createJSON();
                    finalOBJ = jArray.toString();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesHeaderActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("StartDate",edtstartdate.getText().toString());
                    editor.putString("EndDate",edtenddate.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(parent,NewSalesOrderBooking.class);
                    intent.putExtra("Customer",edt_custname.getText().toString().trim());
                    intent.putExtra("SONO",SONO);
                    intent.putExtra("jHeaderArray",finalOBJ);
                    setResult(SALES_HEADER_FILLED, intent);
                    finish();

                    //store data then proceed

               /* Intent intent = new Intent(parent,SalesItemActivity.class);
                intent.putExtra("Customer",edt_custname.getText().toString().trim());
                intent.putExtra("SONO",SONO);
                intent.putExtra("jHeaderArray",finalOBJ);
                startActivityForResult(intent,SALES_ITEM_FILLED);
                //setResult(SALES_ITEM_FILLED, intent);
                finish();*/
                }
            }
        });
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void setValuesToViews(String json){
        json = json.replaceAll("\\\\", "");
        //json = json.substring(1, json.length()-1);

        try{
            jHDRArr = new JSONArray(json);
            for(int i=0; i<jHDRArr.length();i++){
                jHDR_obj = jHDRArr.getJSONObject(i);
                CustVendorMasterId = jHDR_obj.getString("CustomerMasterId");
                ConsigneeName = jHDR_obj.getString("ConsigneeName");
                CustomerId = CustVendorMasterId;
                BilltoId = jHDR_obj.getString("BillToId");
                ConsigneeId = BilltoId;
                CustName = jHDR_obj.getString("CustVendorName");
                edt_billto.setText(jHDR_obj.getString("BillTo"));
                UserName = jHDR_obj.getString("UserName");
                edt_ponumber.setText(jHDR_obj.getString("CustOrderPONo"));
                edt_remark.setText(jHDR_obj.getString("Remarks"));

                String Sodate = jHDR_obj.getString("SODate").replace("/","");
                String EventFrmDt = jHDR_obj.getString("EventFrmDt").replace("/","");
                String EventToDt = jHDR_obj.getString("EventToDt").replace("/","");
                String CustOrderPODt = jHDR_obj.getString("CustOrderPODt").replace("/","");

               // long time = Long.parseLong(parseDate(date));

                edtsodate.setText(getDate(Long.parseLong(parseDate(Sodate))));
                edtpodate.setText(getDate(Long.parseLong(parseDate(CustOrderPODt))));
                edtstartdate.setText(getDate(Long.parseLong(parseDate(EventFrmDt))));
                edtenddate.setText(getDate(Long.parseLong(parseDate(EventToDt))));

                edt_consignee.setText(ConsigneeName);
                edt_custname.setText(CustName);
                edt_sename.setText(UserName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String parseDate(String date) {
        date = date.replace("(","");
        date = date.replace(")","");
        date = date.replace("Date","");
        return date;
    }

    private String getDate(long time) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(time);
        return date;
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

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

    public void getValidation(String OrdTypeMastId){
        try{
            String qry = "SELECT AllowbackdatedSO,IsProjectOrderApp,SOApprMthId FROM "+db.TABLE_OrderType+
                    " WHERE OrderTypeMasterId='"+AnyMartData.OrderTypeMasterId+"'";
        /*String qry = "SELECT SOApprMthId FROM "+db.TABLE_OrderType+
                " WHERE OrderTypeMasterId='"+AnyMartData.OrderTypeMasterId+"'";*/
            Cursor c = sql.rawQuery(qry,null);
            if(c.getCount() > 0){
                c.moveToFirst();
                AllowbackdatedSO = c.getString(c.getColumnIndex("AllowbackdatedSO"));
                IsProjectOrderApp = c.getString(c.getColumnIndex("IsProjectOrderApp"));
                SOApprMthId = c.getString(c.getColumnIndex("SOApprMthId"));

                if(SOApprMthId.equalsIgnoreCase("0")){
                    //hide select approver
                    selapprvr.setVisibility(View.GONE);
                }else /*if(SOApprMthId.equalsIgnoreCase("1"))*/{
                    //visible select approver
                    selapprvr.setVisibility(View.VISIBLE);
                }

                if(IsProjectOrderApp.equalsIgnoreCase("Y")){
                    //visible select project
                    selproject.setVisibility(View.VISIBLE);
                }else{
                    //hide select project
                    selproject.setVisibility(View.GONE);
                }

                if(AllowbackdatedSO.equalsIgnoreCase("Y")){
                    //allow prev date than current date as so date
                }else /*if(AllowbackdatedSO.equalsIgnoreCase("N"))*/{
                    //get todays date by default and no previous date selection
                }

            }else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        String commissionID = uuid.toString();

        try {
            jArray = new JSONArray();
            jobj_header = new JSONObject();

            jobj_header.put("SoDate",edtsodate.getText().toString());
            jobj_header.put("CustomerId",CustomerId);  //guid
            jobj_header.put("ConsigneeName",ConsigneeName);
            jobj_header.put("ConsigneeId",ConsigneeId);
            jobj_header.put("BilltoId",BilltoId);
            jobj_header.put("CustPONo",edt_ponumber.getText().toString());
            jobj_header.put("CustPoDate",edtpodate.getText().toString());
            jobj_header.put("SENameId",SENameId);
            jobj_header.put("SEName",SEName);
            jobj_header.put("StartDt",edtstartdate.getText().toString());
            jobj_header.put("EndDate",edtenddate.getText().toString());

            jArray.put(jobj_header);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }

    class DownloadConsigneeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(SalesHeaderActivity.this);
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
                String url = CompanyURL + WebUrlClass.api_GetFillConsignee +
                        "?CustId="+CustomerId;

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\\\\\/", "");
                // response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if(response.equalsIgnoreCase("")|| response.equalsIgnoreCase(null)){

            }else {
               try{
                   JSONArray jResults = new JSONArray(response);
                   for(int i=0; i<jResults.length(); i++){
                       JSONObject jobj = jResults.getJSONObject(i);
                       ConsigneeId = jobj.getString("ShipToMasterId");
                       BilltoId = ConsigneeId;
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
          // progressDialog.dismiss();
        }
    }

    public boolean validate() {
        boolean val = false;

        if (edtsodate.getText().toString().equalsIgnoreCase("")
                && edt_custname.getText().toString().equalsIgnoreCase("")
                && edt_consignee.getText().toString().equalsIgnoreCase("")
                && edt_billto.getText().toString().equalsIgnoreCase("")
                && edt_ponumber.getText().toString().equalsIgnoreCase("")
                && edt_sename.getText().toString().equalsIgnoreCase("")
                && edtstartdate.getText().toString().equalsIgnoreCase("")
                && edtenddate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SalesHeaderActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtsodate.getText().toString().equalsIgnoreCase("") ||
                edtsodate.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select SO date", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }/*else if(edt_ponumber.getText().toString().equalsIgnoreCase("") ||
                edt_ponumber.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Enter po number", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }*/else if(edtstartdate.getText().toString().equalsIgnoreCase("") ||
                edtstartdate.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select start date", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtenddate.getText().toString().equalsIgnoreCase("") ||
                edtenddate.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select end date", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_sename.getText().toString().equalsIgnoreCase("") ||
                edt_sename.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Enter SE name", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_custname.getText().toString().equalsIgnoreCase("") ||
                edt_custname.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select customer", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_consignee.getText().toString().equalsIgnoreCase("") ||
                edt_consignee.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select consignee", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_billto.getText().toString().equalsIgnoreCase("") ||
                edt_billto.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesHeaderActivity.this, "Select bill to ", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else {
            val = true;
            return val;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CUSTOMERDATA && resultCode == CUSTOMERDATA) {
            //enable other buttons one by one
            CustVendorMasterId = data.getStringExtra("CustVendorMasterId");
            CustName = data.getStringExtra("CustName");

            CustomerId = CustVendorMasterId;
            ConsigneeName = CustName;

            //call API to get consignee id
            //http://c207.ekatm.com/api/CollectionReceiptAPI/FillConsignee?CustId=6c1c887b-34d5-4df9-b17c-7542607c2815
            if (isnet()) {
                new StartSession(SalesHeaderActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadConsigneeJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

            //ConsigneeId = CustVendorMasterId;
            //BilltoId = CustVendorMasterId;

            edt_custname.setText(CustName);
            edt_consignee.setText(CustName);
            edt_billto.setText(CustName);
        }
    }
}
