package com.vritti.sales.activity;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.SaleCommonAdapter;
import com.vritti.sales.beans.PriceListBean;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class SalesFamilyDtlActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar progressBar;
    Button btnadd,btn_cancel;
    ListView listfamily;
    EditText txt_pdesc,edt_disc,txt_taxclass,txt_startdate,txt_del_date,txt_doordate;
    ImageView imgdatestart,img_delschedule,img_doordate;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;
    private SharedPreferences sharedPrefs;
    String StartDate = "", EndDate = "";

    String selTaxCode = "", selTaxDesc = "", selTaxId = "";
    String pListCode = "", pListDesc = "", pListHDRId = "";
    String TaxClassMasterId = "",TaxClassCode = "",TaxClassDesc = "";
    String modelSize = "", modelNo = "", saleFamilyId = "";

    ArrayList<PriceListBean> listItems;
    ArrayList<PriceListBean> selected_listItems;
    private ArrayList<TaxClassBean> taxClassArrayList;
    private ArrayList<String> taxClsStringList;
    SaleCommonAdapter cAdapter;

    JSONObject jObj_item,jobj_schedule;
    JSONArray jArray,jArray_schedule;
    String finalOBJ = "";

    String HeaderDetails = "",ScheduleDetails = "", PeriodicDetails = "";
    int IsProRata=0,   AllowPartShipment=0;

    String ChildId="",SeqNo="",GLBItemDtlId="",SODetailId="",ItemMasterId="",ItemCode="",ItemDesc="",UOMMasterId="",Qty="",Rate="",
            WarrantyCode="",LineAmt="",LineTaxes="",LineTotal="",DiscAmount="0",ProUnit="",TaxClass="",DiscPC="0",ItemProcessId="",
            Description="",ItemClassificationId="",BillingCategoryId="",SegmentId="",RouteFrom="",RouteTo="",ProFigure="",
            PriceListHdrId="",SalesFamilyHdrId="",BQT_QuotationHeaderId="",ContractHdrId="";

    String ScheduleDate = "", ExVendorDate = "", BalQty = "", SoDate = "", FinalDeliverDate = "", ItemProcessCode = "";
    String RecStartDate = "",PeriodicEndDate="",RecEndDate="",ItemSrNo="",ItemSize="0",RecurDaysCount="",RecurWeeksCount="",srno="",
            IsSunday="",IsMonday="",IsTuesday="",IsWednesday="",IsFriday="",IsThursday="",IsSaturday="",EveryMonthCount="",
            MonthlyDayNo="",MonthlyMonth="",MonthlyWeek="",MonthlyDay="",YearlyMonthName="",YearlyWeek="",YearlyDay="",YearlyMonth="",
            TypeOfPeriod="",RecurYearCount="",IsNoEndDate="",Occurrences="";

    public static final int SALES_FAMILY = 13;
    public static final int TAXDATA = 9;
    public static final int PRICELISTDATA = 1;
    public static final int SFamilyDtl = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_family_dtl);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.gettaxclscount() > 0){
            getTaxFromDatabase();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTAXDataJSON().execute(SoDate);
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        //call API pass pListHDRId to it and display it
        if (isnet()) {
            new StartSession(SalesFamilyDtlActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadItemsListJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                }
            });
        }

        setListeners();
    }

    public void init(){
        parent = SalesFamilyDtlActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        //toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Sales Family");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        btnadd = findViewById(R.id.btnadd);
        btn_cancel = findViewById(R.id.btn_cancel);
        imgdatestart = findViewById(R.id.imgdatestart);
        img_delschedule = findViewById(R.id.img_delschedule);
        img_doordate = findViewById(R.id.img_doordate);
        txt_pdesc = findViewById(R.id.txt_pdesc);
        edt_disc = findViewById(R.id.edt_disc);
        txt_taxclass = findViewById(R.id.txt_taxclass);
        txt_startdate = findViewById(R.id.txt_startdate);
        txt_del_date = findViewById(R.id.txt_del_date);
        txt_doordate = findViewById(R.id.txt_doordate);
        listfamily = findViewById(R.id.listfamily);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(SalesFamilyDtlActivity.this);
        cf = new CommonFunction(SalesFamilyDtlActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        Intent intent = getIntent();
        modelNo = intent.getStringExtra("ModelNo");
        modelSize = intent.getStringExtra("ModelSize");
        saleFamilyId = intent.getStringExtra("FamilyID");
        SoDate = intent.getStringExtra("SoDate");

        listItems = new ArrayList<PriceListBean>();
        selected_listItems = new ArrayList<PriceListBean>();
        taxClassArrayList = new ArrayList<TaxClassBean>();
        taxClsStringList = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat f1 = new SimpleDateFormat("EEE, dd MMM yyyy");
        SimpleDateFormat f2 = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        date = String.format("%2d",day) + "/" + String.format("%02d", (month + 1)) + "/" + year;
        txt_startdate.setText(date);
        txt_del_date.setText(date);
        txt_doordate.setText(date);
    }

    public void setListeners(){
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    selected_listItems = ((SaleCommonAdapter) listfamily.getAdapter()).getAllCheckedItems();
                    if(selected_listItems.size() > 0){
                        jArray_schedule = createJSON_scheduleArray();
                        jArray = createJSON(selected_listItems.size());
                        finalOBJ = jArray.toString();

                        ScheduleDetails = jArray_schedule.toString();

                        Intent intent = new Intent(SalesFamilyDtlActivity.this, SalesFamilyActivity_one.class);
                        intent.putExtra("jItemArray",finalOBJ);
                        intent.putExtra("jScheduleArray",ScheduleDetails);
                        intent.putExtra("jHeaderArray",HeaderDetails);
                        intent.putExtra("UOMMasterId",UOMMasterId);
                        setResult(SFamilyDtl,intent);
                        finish();

                    }else {

                    }
                }
            }
        });

        txt_taxclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open taxclass activity all tax list
                Intent intent = new Intent(parent,TaxClassActivity.class);
                intent.putExtra("callFrom","SalesFamily");
                startActivityForResult(intent,TAXDATA);
            }
        });

        txt_pdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,PriceListActivity.class);
                intent.putExtra("key","PriceListDesc");
                intent.putExtra("CallFrom","SalesFamily");
                startActivityForResult(intent,PRICELISTDATA);
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
                                    txt_startdate.setText(date);
                                } else {
                                    txt_startdate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_delschedule.setOnClickListener(new View.OnClickListener() {
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
                                    txt_del_date.setText(date);
                                } else {
                                    txt_del_date.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_doordate.setOnClickListener(new View.OnClickListener() {
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
                                    txt_doordate.setText(date);
                                } else {
                                    txt_doordate.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
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

    class DownloadTAXDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = "";

                url = CompanyURL + WebUrlClass.api_getTaxClassCode+"?SODt="+params[0];

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length()-1);
                    ContentValues values = new ContentValues();
                    jResults = new JSONArray(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //hideProgress();

            try{
                if(jResults != null){
                    sql.delete(db.TABLE_TAXCLASS,null,null);

                    parseTaxClassJson(jResults);
                }else {
                }
            }catch (Exception e){

            }
        }
    }

    public void parseTaxClassJson(JSONArray jResults){

        //    tcf.clearTable(parent, dbhandler.TABLE_ADD_ITEMS_COUNTERBILL);

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                TaxClassMasterId = jsonObject.getString("TaxClassMasterId");
                TaxClassCode = jsonObject.getString("TaxClassCode");
                TaxClassDesc = jsonObject.getString("TaxClassDesc");

                // insert in table
                tcf.insertTaxClass(TaxClassMasterId, TaxClassCode, TaxClassDesc);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getTaxFromDatabase();
    }

    public void getTaxFromDatabase(){
        taxClassArrayList.clear();
        taxClsStringList.clear();
        String query = "Select TaxClassMasterId,TaxClassCode,TaxClassDesc from "+ db.TABLE_TAXCLASS;
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                TaxClassMasterId = c.getString(c.getColumnIndex("TaxClassMasterId"));
                TaxClassCode = c.getString(c.getColumnIndex("TaxClassCode"));
                TaxClassDesc = c.getString(c.getColumnIndex("TaxClassDesc"));

                TaxClassBean tcbean = new TaxClassBean();
                tcbean.setTaxClassMasterId(TaxClassMasterId);
                tcbean.setTaxClassCode(TaxClassCode);
                tcbean.setTaxClassDesc(TaxClassDesc);

                taxClassArrayList.add(tcbean);
                taxClsStringList.add(TaxClassDesc);

            }while (c.moveToNext());

        }else {

        }

    }

    class DownloadItemsListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getSalesFamilyItems+"?FamilyId="+saleFamilyId+"&ModelNo=&ModelSize=";
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.GONE);
            if (response != "" || response != null || response != "error") {
                displayItemsList(response);
            }else {
                Toast.makeText(parent,"No data found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void displayItemsList(String response){
        listItems.clear();

        try{
            ContentValues values = new ContentValues();
            JSONArray jResults = new JSONArray(response);

            for (int i = 0; i < jResults.length(); i++) {
                JSONObject jObj = jResults.getJSONObject(i);
                PriceListBean pbean = new PriceListBean();
                pbean.setFamilyId(jObj.getString("familyid"));
                pbean.setItemPlantid(jObj.getString("ItemPlantid"));
                pbean.setItemCode(jObj.getString("ItemCode"));
                pbean.setItemDesc(jObj.getString("ItemDesc"));
                pbean.setUOMCode(jObj.getString("UOMCode"));
                pbean.setSalesUnit(jObj.getString("SalesUnit"));
                pbean.setUOMMasterId(getUOMMasterId(jObj.getString("UOMCode")));
                pbean.setQtyLine("0");
                pbean.setBaseRate("0");
                pbean.setLineAmt("0");
                pbean.setDiscountPer("0");
                pbean.setDiscLineAmt("0");
                pbean.setTaxCls("");
                pbean.setTaxclsId("");
                pbean.setTaxLineAmt("0");
                pbean.setTaxList(taxClassArrayList);
                listItems.add(pbean);
            }
            cAdapter = new SaleCommonAdapter(this,listItems,"SalesFamily",taxClassArrayList,taxClsStringList);
            listfamily.setAdapter(cAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUOMMasterId(String uomCode){
        String UOMMasterID = "";
        String qry = "Select UOMMasterId from "+db.TABLE_UOM_new+" WHERE UOMCode='"+uomCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            UOMMasterID = c.getString(c.getColumnIndex("UOMMasterId"));
        }
        return UOMMasterID;
    }

    public void setTaxClsToAllItems(String _taxClsId, String _taxClsDesc, String _taxClsCode){
        String disc_per = edt_disc.getText().toString();
        if(listItems.size()>0){
            for(int i=0; i<listItems.size(); i++){
                listItems.get(i).setTaxclsId(_taxClsId);
                listItems.get(i).setTaxCls(_taxClsDesc);
                listItems.get(i).setDiscountPer(disc_per);
            }
            cAdapter = new SaleCommonAdapter(this,listItems,"SalesFamily",taxClassArrayList,taxClsStringList);
            listfamily.setAdapter(cAdapter);
        }else {

        }
    }

    public JSONArray createJSON(int size){
        UUID uuid = UUID.randomUUID();
        //PriceListHdrId = uuid.toString();
        jArray = new JSONArray();

        for(int i=0; i<size; i++){
            int cnt = i + 1;
            SeqNo = String.valueOf(cnt);
            ChildId = SeqNo;
            srno = SeqNo;
            GLBItemDtlId = "0";
            Qty = /*edtqty.getText().toString();*/selected_listItems.get(i).getQtyLine();
            Rate = /*edtunitrate.getText().toString();*/selected_listItems.get(i).getBaseRate();
            LineAmt = /*edtlineamt.getText().toString();*/selected_listItems.get(i).getLineAmt();
            LineTaxes = /*txt_taxamt.getText().toString();*/selected_listItems.get(i).getTaxamt();
            LineTotal = /*txt_totamt.getText().toString();*/selected_listItems.get(i).getTaxLineAmt();
            ItemMasterId = selected_listItems.get(i).getItemPlantid();
            ItemCode = selected_listItems.get(i).getItemCode();
            ItemDesc = selected_listItems.get(i).getItemDesc();
            UOMMasterId = selected_listItems.get(i).getUOMMasterId();
           /* DiscAmount = String.valueOf(Float.parseFloat(edtlineamt.getText().toString()) -
                    Float.parseFloat(edtdiscamt.getText().toString()));*/
            DiscAmount = selected_listItems.get(i).getDiscamt();
            DiscPC = selected_listItems.get(i).getDiscountPer();
            TaxClass = /*edtaxclass.getText().toString();*/selected_listItems.get(i).getTaxCls();
            //DiscAmount = edtdiscamt.getText().toString();
            WarrantyCode = /*edtwarntycode.getText().toString();*/"";

            /*if(spindays.getText().toString() == ""){
                ProUnit = "Day";
            }else {

            }*/
            ProUnit = "Day";

            if(DiscAmount == "" || DiscAmount == null){
                DiscAmount = "0";
            }

          /*  if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
                DiscPC = edtdisc.getText().toString();
            }else {
                DiscPC = "0";
            }*/

            /*if(radbtnprorate.isChecked()){
                IsProRata = 1;
            }else {
                IsProRata = 0;
            }*/
            IsProRata = 0;

            /*if(chk_allow.isChecked()){
                AllowPartShipment = 1;
            }else {
                AllowPartShipment = 0;
            }*/
            AllowPartShipment = 0;

            //PriceListHdrId = selected_listItems.get(i).getPriceListHdrID();
            SalesFamilyHdrId = selected_listItems.get(i).getFamilyId();

            try {
                jObj_item = new JSONObject();

                jObj_item.put("ChildId",ChildId);
                jObj_item.put("SeqNo",SeqNo);
                jObj_item.put("GLBItemDtlId",GLBItemDtlId);
                jObj_item.put("SODetailId",SODetailId);
                jObj_item.put("ItemMasterId",ItemMasterId);
                jObj_item.put("ItemCode",ItemCode);
                jObj_item.put("ItemDesc",ItemDesc);
                jObj_item.put("UOMMasterId",UOMMasterId);
                jObj_item.put("Qty",Qty);
                jObj_item.put("Rate",Rate);
                jObj_item.put("WarrantyCode",WarrantyCode);
                jObj_item.put("LineAmt",LineAmt);
                jObj_item.put("LineTaxes",LineTaxes);
                jObj_item.put("LineTotal",LineTotal);
                jObj_item.put("DiscAmount",DiscAmount);
                jObj_item.put("ProUnit",ProUnit);       //set value for it
                jObj_item.put("TaxClass",selected_listItems.get(i).getTaxclsId()/*TaxClassMasterId*/);
                jObj_item.put("DiscPC",DiscPC);
                jObj_item.put("ItemProcessId",ItemProcessId);
                jObj_item.put("Description",Description);
                jObj_item.put("ItemClassificationId",ItemClassificationId);
                jObj_item.put("BillingCategoryId",BillingCategoryId);
                jObj_item.put("SegmentId",SegmentId);
                jObj_item.put("RouteFrom",RouteFrom);
                jObj_item.put("RouteTo",RouteTo);

                //schedule, periodic json insert here
                /**************************Periodic schedule data************************************/
                jObj_item.put("RecStartDate",RecStartDate);
                jObj_item.put("PeriodicEndDate",PeriodicEndDate);
                jObj_item.put("RecEndDate",RecEndDate);
                jObj_item.put("ItemSrNo",ItemSrNo);     //1
                jObj_item.put("ItemSize",ItemSize);
                jObj_item.put("RecurDaysCount",RecurDaysCount);
                jObj_item.put("RecurWeeksCount",RecurWeeksCount);
                jObj_item.put("srno",srno);     //1
                jObj_item.put("IsSunday",IsSunday);
                jObj_item.put("IsMonday",IsMonday);
                jObj_item.put("IsTuesday",IsTuesday);
                jObj_item.put("IsWednesday",IsWednesday);
                jObj_item.put("IsThursday",IsThursday);
                jObj_item.put("IsFriday",IsFriday);
                jObj_item.put("IsSaturday",IsSaturday);
                jObj_item.put("EveryMonthCount",EveryMonthCount);
                jObj_item.put("MonthlyDayNo",MonthlyDayNo);
                jObj_item.put("MonthlyMonth",MonthlyMonth);
                jObj_item.put("MonthlyWeek",MonthlyWeek);
                jObj_item.put("MonthlyDay",MonthlyDay);
                jObj_item.put("YearlyMonthName",YearlyMonthName);
                jObj_item.put("YearlyWeek",YearlyWeek);
                jObj_item.put("YearlyDay",YearlyDay);
                jObj_item.put("YearlyMonth",YearlyMonth);
                jObj_item.put("TypeOfPeriod",TypeOfPeriod);
                jObj_item.put("IsNoEndDate",IsNoEndDate);
                jObj_item.put("RecurYearCount",RecurYearCount);
                jObj_item.put("Occurrences",Occurrences);
                /************************************************************************************/

                jObj_item.put("IsProRata",IsProRata);   //0 or cnt
                jObj_item.put("ProFigure",ProFigure);       //0 or cnt
                jObj_item.put("AllowPartShipment",0);   //0 for false or 1 for true
                jObj_item.put("SalesFamilyHdrId",SalesFamilyHdrId);
                jObj_item.put("PriceListHdrId",PriceListHdrId);
                jObj_item.put("BQT_QuotationHeaderId",BQT_QuotationHeaderId);
                jObj_item.put("ContractHdrId",ContractHdrId);

                jArray.put(jObj_item);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return jArray;
    }

    public JSONArray createJSON_scheduleArray(){

        for(int i=0; i<selected_listItems.size();i++){
            UUID uuid = UUID.randomUUID();
            String scheduleID = uuid.toString();

            ScheduleDate = txt_del_date.getText().toString();
            FinalDeliverDate = txt_doordate.getText().toString();
            ExVendorDate = SoDate;
            BalQty = "0";
            int cnt = i + 1;

            try {
                jArray_schedule = new JSONArray();
                jobj_schedule = new JSONObject();

                jobj_schedule.put("SrNo",cnt);
                jobj_schedule.put("ItemSrNo",cnt);
                jobj_schedule.put("ScheduleId",scheduleID); //guid
                jobj_schedule.put("ScheduleDate",ScheduleDate);
                jobj_schedule.put("ExVendorDate",ExVendorDate);
                jobj_schedule.put("Qty",selected_listItems.get(i).getQtyLine());
                jobj_schedule.put("BalQty",BalQty);
                jobj_schedule.put("SoDate",SoDate);
                jobj_schedule.put("FinalDeliverDate",FinalDeliverDate);
                jobj_schedule.put("ItemProcessId",ItemProcessId);
                jobj_schedule.put("ItemProcessCode",ItemProcessCode);
                jobj_schedule.put("Action","");

                jArray_schedule.put(jobj_schedule);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return jArray_schedule;
    }

    public boolean validate() {
        boolean val = false;

        if(txt_taxclass.getText().toString().equalsIgnoreCase("") /*&&
                txt_pdesc.getText().toString().equalsIgnoreCase("") &&
                txt_taxclass.getText().toString().equalsIgnoreCase("")*/){
            Toast.makeText(SalesFamilyDtlActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(txt_taxclass.getText().toString().equalsIgnoreCase("") ||
                txt_taxclass.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesFamilyDtlActivity.this, "Select tax class", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }/*else if(txt_pdesc.getText().toString().equalsIgnoreCase("") ||
                txt_pdesc.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesFamilyDtlActivity.this, "Select price list description", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(txt_taxclass.getText().toString().equalsIgnoreCase("") ||
                txt_taxclass.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesFamilyDtlActivity.this, "Select tax class", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }*/else {
            val = true;
            return val;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAXDATA && resultCode == TAXDATA) {
            //enable other buttons one by one
            selTaxCode = data.getStringExtra("TaxClassCode");
            selTaxDesc = data.getStringExtra("TaxClassDesc");
            selTaxId = data.getStringExtra("TaxClassID");
            txt_taxclass.setText(selTaxDesc);

            TaxClassMasterId = selTaxId;

            setTaxClsToAllItems(TaxClassMasterId,selTaxDesc,selTaxCode);

            //float Amt = Float.parseFloat(edt_charge_amt.getText().toString());
            //pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edt_taxclass.getText().toString(),Amt,"PriceList");
        }else if(requestCode == PRICELISTDATA && resultCode == PRICELISTDATA){
            pListCode = data.getStringExtra("PListCode");
            pListDesc = data.getStringExtra("PListDesc");
            pListHDRId = data.getStringExtra("PListHDRID");

            txt_pdesc.setText(pListDesc);
        }
    }

}
