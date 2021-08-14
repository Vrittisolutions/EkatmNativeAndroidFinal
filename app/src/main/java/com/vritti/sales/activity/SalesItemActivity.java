package com.vritti.sales.activity;

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
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.SaleItemListAdapter;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.InclusiveExclusiveTaxCalc;
import com.vritti.sales.beans.SaleItemBean;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SalesItemActivity extends AppCompatActivity {
    public static Context parent;
    Toolbar toolbar;
    ProgressBar progressBar;
    public static ListView listitems;
    static TextView btnmode, txtamt, txtdiscamt, txttaxamt, txtfinalamt;
    Button btnadd, btndirect, btnpricelist,btnsalesfamily, btnquotation,  btncontract, btnsave;
    LinearLayout layoutoptions;
    ImageView imgbtnadd;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    private SharedPreferences sharedPrefs;
    String mode = "";

    String HeaderDetails = "", ItemDetails = "",ScheduleDetails = "", UOMMasterId = "", SoDate = "", CustomerId = "", editMode = "",
            SoHeaderId = "";
    static float finalQty = 0.0f;
    String TaxClassMasterId = "",TaxClassCode = "",TaxClassDesc = "";

    public static ArrayList<SaleItemBean> listsaleItems;
    private ArrayList<TaxClassBean> taxClassArrayList;
    private ArrayList<String> taxClsStringList;
    public static SaleItemListAdapter saleAdapter;

    JSONObject jObj_item,jobj_schedule;
    JSONArray jArray,jArray_schedule,jMainItemArray,jMainSchArray;
    String finalOBJ = "";

    int IsProRata=0,   AllowPartShipment=0;

    String ChildId="",SeqNo="",GLBItemDtlId="0",SODetailId="",ItemMasterId="",ItemCode="",ItemDesc="",Qty="",Rate="",
            WarrantyCode="",LineAmt="",LineTaxes="",LineTotal="",DiscAmount="0",ProUnit="",TaxClass="",DiscPC="0",ItemProcessId="",
            Description="",ItemClassificationId="",BillingCategoryId="",SegmentId="",RouteFrom="",RouteTo="",ProFigure="",
            PriceListHdrId="",SalesFamilyHdrId="",BQT_QuotationHeaderId="",ContractHdrId="";

    String ScheduleDate = "", ExVendorDate = "", BalQty = "", FinalDeliverDate = "", ItemProcessCode = "";
    String RecStartDate = "",PeriodicEndDate="",RecEndDate="",ItemSrNo="",ItemSize="0",RecurDaysCount="",RecurWeeksCount="",srno="",
            IsSunday="",IsMonday="",IsTuesday="",IsWednesday="",IsFriday="",IsThursday="",IsSaturday="",EveryMonthCount="",
            MonthlyDayNo="",MonthlyMonth="",MonthlyWeek="",MonthlyDay="",YearlyMonthName="",YearlyWeek="",YearlyDay="",YearlyMonth="",
            TypeOfPeriod="",RecurYearCount="",IsNoEndDate="",Occurrences="";

    String IsInclusiveTax="", IsMargin="";

    public static final int SALES_ITEM_FILLED = 2;
    public static final int DIRECT_ITEM = 11;
    public static final int PRICELIST = 12;
    public static final int SALES_FAMILY = 13;
    public static final int QUOTATION = 14;
    public static final int CONTRACT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_item);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getUOMNewCount() > 0){
           //getUOMData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadUOMJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

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

        setListeners();
    }

    private void init() {
        parent = SalesItemActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Add Item");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        listitems = findViewById(R.id.listitems);
        layoutoptions = findViewById(R.id.layoutoptions);
        layoutoptions.setVisibility(View.VISIBLE);
        btnadd = findViewById(R.id.btnadd);
        btndirect = findViewById(R.id.btndirect);
        btnpricelist = findViewById(R.id.btnpricelist);
        btnsalesfamily = findViewById(R.id.btnsalesfamily);
        btnquotation = findViewById(R.id.btnquotation);
        btncontract = findViewById(R.id.btncontract);
        btnsave = findViewById(R.id.btnsave);
        imgbtnadd = findViewById(R.id.imgbtnadd);
        btnmode = findViewById(R.id.btnmode);
        btnmode.setVisibility(View.GONE);
        txtamt = findViewById(R.id.txtamt);
        txtdiscamt = findViewById(R.id.txtdiscamt);
        txttaxamt = findViewById(R.id.txttaxamt);
        txtfinalamt = findViewById(R.id.txtfinalamt);

        ut = new Utility();
        cf = new CommonFunction(SalesItemActivity.this);
        tcf = new Tbuds_commonFunctions(SalesItemActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesItemActivity.this);
        String dabasename = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesItemActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesItemActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesItemActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        listsaleItems = new ArrayList<SaleItemBean>();
        taxClassArrayList = new ArrayList<TaxClassBean>();
        taxClsStringList = new ArrayList<String>();

        Intent i = getIntent();
        HeaderDetails = i.getStringExtra("jHeaderArray");
        SoDate = i.getStringExtra("SoDate");
        CustomerId = i.getStringExtra("CustomerId");
        editMode = i.getStringExtra("Mode");
        SoHeaderId = i.getStringExtra("SoHeaderId");

        if(editMode.equalsIgnoreCase("Edit")){
            //display this screen only
            //set data to listview and also add in json.
            callEditItemDetailAPI();

        }else {
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
            mode = sharedPrefs.getString("MODE", "");
            if(mode.equalsIgnoreCase("")){
                btnmode.setText("");
            }else if(mode.equalsIgnoreCase("DirectItem")){
                //btnmode.setText(" by Direct Item Selection");
                toolbar.setTitle("Add Item by Direct Item Selection");

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","DirectItem");
                editor.commit();

                Intent intent = new Intent(parent,AddDirectItemActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,DIRECT_ITEM);

            }else if(mode.equalsIgnoreCase("PriceList")){
                //btnmode.setText(" by Price List Selection");
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","PriceList");
                editor.commit();

                toolbar.setTitle("Add Item by Price List Selection");

                Intent intent = new Intent(parent, PriceListTabActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,PRICELIST);

            }else if(mode.equalsIgnoreCase("SalesFamily")){
                //btnmode.setText(" by Sales Family Selection");
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","SalesFamily");
                editor.commit();

                toolbar.setTitle("Add Item by Sales Family Selection");

                Intent intent = new Intent(parent,SalesFamilyActivity_one.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,SALES_FAMILY);

            }else if(mode.equalsIgnoreCase("Quotation")){
                //btnmode.setText(" by Quotation Selection");
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","Quotation");
                editor.commit();

                toolbar.setTitle("Add Item by Quotation Selection");

                Intent intent = new Intent(parent,QuotationActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                intent.putExtra("CustomerId",CustomerId);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,QUOTATION);

            }else if(mode.equalsIgnoreCase("Contract")){
                //btnmode.setText(" by Contract Selection");
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","Contract");
                editor.commit();

                toolbar.setTitle("Add Item by Contract Selection");

                Intent intent = new Intent(parent,ContractActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,CONTRACT);
            }
        }
    }

    private void setListeners() {
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutoptions.setVisibility(View.VISIBLE);
            }
        });

        imgbtnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutoptions.setVisibility(View.VISIBLE);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save listview data to table and return to NewSalesOrderBooking

                ItemDetails = createJSON().toString();
                ScheduleDetails = createJSON_scheduleArray().toString();

                Intent intent = new Intent(SalesItemActivity.this, NewSalesOrderBooking.class);
                intent.putExtra("jItemArray",ItemDetails);
                intent.putExtra("jScheduleArray",ScheduleDetails);
                //intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("finalQty",String.valueOf(finalQty));
                intent.putExtra("FinalProductAmt",txtamt.getText().toString());
                intent.putExtra("FinalProductTaxAmt",txttaxamt.getText().toString());
                intent.putExtra("lastDiscAmount",txtdiscamt.getText().toString());
                intent.putExtra("lastTotalamt",txtfinalamt.getText().toString());
                intent.putExtra("UOMMasterId",UOMMasterId);
                setResult(SALES_ITEM_FILLED,intent);
                finish();

                /*Intent intent = new Intent(parent,NewSalesOrderBooking.class);
                setResult(SALES_ITEM_FILLED, intent);
                finish();*/
            }
        });

        btndirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","DirectItem");
                editor.commit();

               // btnmode.setText("Direct Item Selection");
                toolbar.setTitle("Add Item by Direct Item Selection");

                Intent intent = new Intent(parent,AddDirectItemActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,DIRECT_ITEM);
            }
        });

        btnpricelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","PriceList");
                editor.commit();

               // btnmode.setText("Price List Selection");
                toolbar.setTitle("Add Item by Price List Selection");

                Intent intent = new Intent(parent, PriceListTabActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,PRICELIST);
            }
        });

        btnsalesfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","SalesFamily");
                editor.commit();

                //btnmode.setText("Sales Family Selection");
                toolbar.setTitle("Add Item by Sales Family Selection");

                Intent intent = new Intent(parent,SalesFamilyActivity_one.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,SALES_FAMILY);
            }
        });

        btnquotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","Quotation");
                editor.commit();

                //btnmode.setText("Quotation Selection");
                toolbar.setTitle("Add Item by Quotation Selection");

                Intent intent = new Intent(parent,QuotationActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                intent.putExtra("CustomerId",CustomerId);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,QUOTATION);

            }
        });

        btncontract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SalesItemActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("MODE","Contract");
                editor.commit();

                //btnmode.setText("Contract Selection");
                toolbar.setTitle("Add Item by Contract Selection");

                Intent intent = new Intent(parent,ContractActivity.class);
                intent.putExtra("jHeaderArray",HeaderDetails);
                intent.putExtra("SoDate",SoDate);
                //intent.putExtra("SeqNo",String.valueOf(listsaleItems.size()));
                startActivityForResult(intent,CONTRACT);
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

    public void setDataToList(String directItemArray){
        float taxAmt = 0.0f, fAmt = 0.0f, discAmt = 0.0f,discLineAmt = 0.0f, lineAmt = 0.0f, discPer = 0.0f,mrp=0.0f;

        getTaxFromDatabase();

        try{
            JSONArray jsonArray = new JSONArray(directItemArray);

            for(int i=0; i<jsonArray.length();i++){

                JSONObject jobj = jsonArray.getJSONObject(i);
                taxAmt = Float.parseFloat(jobj.getString("LineTaxes"));
                fAmt = Float.parseFloat(jobj.getString("LineTotal"));
                mrp = Float.parseFloat(jobj.getString("LineTotal"));//mrp=linetotal
                discAmt = Float.parseFloat(jobj.getString("DiscAmount"));
                lineAmt = Float.parseFloat(jobj.getString("LineAmt")); //lineamt=rate
                discPer = Float.parseFloat(jobj.getString("DiscPC"));
                discLineAmt = lineAmt - discAmt;

                SaleItemBean saleBean = new SaleItemBean();
                saleBean.setItemcode(jobj.getString("ItemCode"));
                saleBean.setItemdesc(jobj.getString("ItemDesc"));
                saleBean.setQty(jobj.getString("Qty"));

                saleBean.setAmtLine(String.format("%.2f",lineAmt)); //=rate
                saleBean.setDiscAmtLine(String.format("%.2f",discLineAmt));
                saleBean.setDiscAmt(String.format("%.2f",discAmt));
                saleBean.setTaxAmtLine(String.format("%.2f",taxAmt));
                saleBean.setFinalAmtLine(String.format("%.2f",fAmt));
                saleBean.setDiscPer(String.format("%.2f",discPer));
                saleBean.setTaxClsId(jobj.getString("TaxClass"));
                saleBean.setTaxclass(getTaxClass(jobj.getString("TaxClass")));
                saleBean.setEditedClicked(false);

                if(IsInclusiveTax.equals("true")){
                    saleBean.setRate(String.format("%.2f",lineAmt));
                    float rate = InclusiveExclusiveTaxCalc.calcRateInclTax(true,
                            Float.parseFloat(jobj.getString("Rate")),getTaxClass(jobj.getString("TaxClass")),0);//=mrp
                    rate = InclusiveExclusiveTaxCalc.calcMarginVal(rate,Float.parseFloat(IsMargin));
                    saleBean.setRate(String.format("%.2f",rate));   //=mrp
                    saleBean.setMRP(jobj.getString("Rate"));


                }else {
                    float rate = InclusiveExclusiveTaxCalc.calcMarginVal
                            (Float.parseFloat(jobj.getString("Rate")),Float.parseFloat(IsMargin));
                    saleBean.setRate(String.format("%.2f",rate));   //=mrp
                    saleBean.setMRP(jobj.getString("Rate"));
                }


                /*if(IsMargin.equals(0)){
                    urate = InclusiveExclusiveTaxCalc.calcMarginVal(urate,IsMargin);
                }else {
                    urate = InclusiveExclusiveTaxCalc.calcMarginVal(urate,IsMargin);
                }*/

                //other fields to list
                //saleBean.setSOHeaderId(jobj.getString(""));
                saleBean.setSODetailId(jobj.getString("SODetailId"));
                saleBean.setSeqNo(jobj.getString("SeqNo"));
                saleBean.setGLBItemDtlId(jobj.getString("GLBItemDtlId"));
                saleBean.setItemMasterId(jobj.getString("ItemMasterId"));
                saleBean.setItemcode(jobj.getString("ItemCode"));
                saleBean.setItemdesc(jobj.getString("ItemDesc"));
                saleBean.setUOMMasterId(jobj.getString("UOMMasterId"));
                saleBean.setUOMCode(getUomCode(jobj.getString("UOMMasterId")));
                saleBean.setWarrantyCode(jobj.getString("WarrantyCode"));
                saleBean.setProUnit(jobj.getString("ProUnit"));
                saleBean.setItemProcessId(jobj.getString("ItemProcessId"));
                saleBean.setDescription(jobj.getString("Description"));
                saleBean.setItemClassificationId(jobj.getString("ItemClassificationId"));
                saleBean.setBillingCategoryId(jobj.getString("BillingCategoryId"));
                saleBean.setSegmentId(jobj.getString("SegmentId"));
                saleBean.setRouteFrom(jobj.getString("RouteFrom"));
                saleBean.setRouteTo(jobj.getString("RouteTo"));
                saleBean.setRecStartDate(jobj.getString("RecStartDate"));
                saleBean.setPeriodicEndDate(jobj.getString("PeriodicEndDate"));
                saleBean.setRecEndDate(jobj.getString("RecEndDate"));
                saleBean.setItemSrNo(jobj.getString("ItemSrNo"));
                saleBean.setItemSize(jobj.getString("ItemSize"));
                saleBean.setRecurDaysCount(jobj.getString("RecurDaysCount"));
                saleBean.setRecurWeeksCount(jobj.getString("RecurWeeksCount"));
                saleBean.setSrno(jobj.getString("srno"));
                saleBean.setIsSunday(jobj.getString("IsSunday"));
                saleBean.setIsMonday(jobj.getString("IsMonday"));
                saleBean.setIsTuesday(jobj.getString("IsTuesday"));
                saleBean.setIsWednesday(jobj.getString("IsWednesday"));
                saleBean.setIsThursday(jobj.getString("IsThursday"));
                saleBean.setIsFriday(jobj.getString("IsFriday"));
                saleBean.setIsSaturday(jobj.getString("IsSaturday"));
                saleBean.setEveryMonthCount(jobj.getString("EveryMonthCount"));
                saleBean.setMonthlyDayNo(jobj.getString("MonthlyDayNo"));
                saleBean.setMonthlyMonth(jobj.getString("MonthlyMonth"));
                saleBean.setMonthlyWeek(jobj.getString("MonthlyWeek"));
                saleBean.setMonthlyDay(jobj.getString("MonthlyDay"));
                saleBean.setYearlyMonthName(jobj.getString("YearlyMonthName"));
                saleBean.setYearlyWeek(jobj.getString("YearlyWeek"));
                saleBean.setYearlyDay(jobj.getString("YearlyDay"));
                saleBean.setYearlyMonth(jobj.getString("YearlyMonth"));
                saleBean.setTypeOfPeriod(jobj.getString("TypeOfPeriod"));
                saleBean.setIsNoEndDate(jobj.getString("IsNoEndDate"));
                saleBean.setRecurYearCount(jobj.getString("RecurYearCount"));
                saleBean.setOccurrences(jobj.getString("Occurrences"));

                saleBean.setIsProRata(jobj.getString("IsProRata"));
                saleBean.setProFigure(jobj.getString("ProFigure"));
                saleBean.setAllowPartShipment(jobj.getString("AllowPartShipment"));
                saleBean.setSalesFamilyHdrId(jobj.getString("SalesFamilyHdrId"));
                saleBean.setPriceListHdrId(jobj.getString("PriceListHdrId"));
                saleBean.setBQT_QuotationHeaderId(jobj.getString("BQT_QuotationHeaderId"));
                saleBean.setContractHdrId(jobj.getString("ContractHdrId"));

                listsaleItems.add(saleBean);
            }

            saleAdapter = new SaleItemListAdapter(parent,listsaleItems,taxClassArrayList,taxClsStringList);
            listitems.setAdapter(saleAdapter);

        }catch (Exception e){
            e.printStackTrace();
        }

        setFinalValues();
    }

    public static void setFinalValues() {

        float amt = 0.0f, discamt = 0.0f, taxamt = 0.0f, famt = 0.0f;
        finalQty = 0.0f;

        if(listsaleItems.size() > 0){

            /*saleAdapter = new SaleItemListAdapter(parent,listsaleItems);
            listitems.setAdapter(saleAdapter);*/

            for(int i=0; i<listsaleItems.size(); i++){

                finalQty = finalQty + (int)Float.parseFloat(listsaleItems.get(i).getQty());
                amt = amt + Float.parseFloat(listsaleItems.get(i).getAmtLine());
                discamt = discamt + Float.parseFloat(listsaleItems.get(i).getDiscAmt());
                //discamt = discamt + Float.parseFloat(listsaleItems.get(i).getDiscAmtLine());
                taxamt = taxamt + Float.parseFloat(listsaleItems.get(i).getTaxAmtLine());
                famt = famt + Float.parseFloat(listsaleItems.get(i).getFinalAmtLine());

                txtamt.setText(String.format("%.2f",amt));
                discamt = Math.round(discamt);
                txtdiscamt.setText(String.format("%.2f",discamt));
                taxamt = Math.round(taxamt);
                txttaxamt.setText(String.format("%.2f",taxamt));
                famt = Math.round(famt);
                txtfinalamt.setText(String.format("%.2f",famt));
            }
        }else {
            txtamt.setText("0.00");
            txtdiscamt.setText("0.00");
            txttaxamt.setText("0.00");
            txtfinalamt.setText("0.00");
        }
    }

    class DownloadUOMJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            //showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getUOM_new;

                res = ut.OpenConnection(url);
                if (res != null) {
                 /*   String a = "\\";
                    String b = "\"";
                    String c = a+b;
                    response = res.toString().replaceAll(c,"");*/

                    response = res.toString().replaceAll("\\r\\n","");
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
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
            //  mprogress.setVisibility(View.GONE);
           // hideProgress();
            try{
                if(jResults != null){

                    sql.delete(db.TABLE_UOM_new,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String UOMMasterId = jsonObject.getString("UOMMasterId");
                            String UOMCode = jsonObject.getString("UOMCode");
                            String UOMDesc = jsonObject.getString("UOMDesc");
                            String UOMDigit = jsonObject.getString("UOMDigit");

                            ConfigDropDownData dropdown = new ConfigDropDownData(UOMMasterId,UOMCode,UOMDesc,UOMDigit);

                            if(UOMDesc.equalsIgnoreCase("")){

                            }else {
                                tcf.insertUOM_new(UOMMasterId,UOMCode,UOMDesc,UOMDigit);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public String getTaxClass(String taxClsId){
        String taxClsName = "";
        String qry = "Select TaxClassDesc from "+db.TABLE_TAXCLASS+" WHERE TaxClassMasterId="+taxClsId;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            taxClsName = c.getString(c.getColumnIndex("TaxClassDesc"));
        }else {

        }
        return taxClsName;
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

    public JSONArray createJSON(){

        jArray = new JSONArray();

        for(int i=0; i<listsaleItems.size(); i++){

            try{
                JSONObject jsonObject = jMainItemArray.getJSONObject(i);
            }catch (Exception e){
                e.printStackTrace();
            }

            int cnt = i + 1;
            SeqNo = listsaleItems.get(i).getSeqNo();
            ChildId = SeqNo;
            srno = SeqNo;
            GLBItemDtlId = listsaleItems.get(i).getGLBItemDtlId();
            Qty = listsaleItems.get(i).getQty();
            Rate = listsaleItems.get(i).getRate();
            LineAmt = listsaleItems.get(i).getAmtLine();
            LineTaxes = listsaleItems.get(i).getTaxAmtLine();
            LineTotal = listsaleItems.get(i).getFinalAmtLine();
            ItemMasterId = listsaleItems.get(i).getItemMasterId();
            ItemCode = listsaleItems.get(i).getItemcode();
            ItemDesc = listsaleItems.get(i).getItemdesc();
            UOMMasterId = listsaleItems.get(i).getUOMMasterId();
            DiscAmount = listsaleItems.get(i).getDiscAmtLine();
            DiscPC = listsaleItems.get(i).getDiscPer();
            TaxClass = listsaleItems.get(i).getTaxClsId();
            WarrantyCode = listsaleItems.get(i).getWarrantyCode();
            ContractHdrId = listsaleItems.get(i).getContractHdrId();
            PriceListHdrId = listsaleItems.get(i).getPriceListHdrId();
            SalesFamilyHdrId = listsaleItems.get(i).getSalesFamilyHdrId();
            BQT_QuotationHeaderId = listsaleItems.get(i).getBQT_QuotationHeaderId();
            ProUnit = "Day";
            if(DiscAmount == "" || DiscAmount == null){
                DiscAmount = "0";
            }
            IsProRata = 0;
            AllowPartShipment = 0;

            jObj_item = new JSONObject();
            try {

                jObj_item.put("ChildId",ChildId);
                jObj_item.put("SeqNo",SeqNo);
                jObj_item.put("GLBItemDtlId",GLBItemDtlId);
                jObj_item.put("SODetailId",listsaleItems.get(i).getSODetailId());
                jObj_item.put("ItemMasterId",listsaleItems.get(i).getItemMasterId());
                jObj_item.put("ItemCode",listsaleItems.get(i).getItemcode());
                jObj_item.put("ItemDesc",listsaleItems.get(i).getItemdesc());
                jObj_item.put("UOMMasterId",listsaleItems.get(i).getUOMMasterId());
                jObj_item.put("Qty",Qty);
                jObj_item.put("Rate",Rate);
                jObj_item.put("WarrantyCode",WarrantyCode);
                jObj_item.put("LineAmt",LineAmt);
                jObj_item.put("LineTaxes",LineTaxes);
                jObj_item.put("LineTotal",LineTotal);
                jObj_item.put("DiscAmount",DiscAmount);
                jObj_item.put("ProUnit",listsaleItems.get(i).getProUnit());       //set value for it
                jObj_item.put("TaxClass",listsaleItems.get(i).getTaxClsId());
                jObj_item.put("DiscPC",listsaleItems.get(i).getDiscPer());
                jObj_item.put("ItemProcessId",listsaleItems.get(i).getItemProcessId());
                jObj_item.put("Description",listsaleItems.get(i).getDescription());
                jObj_item.put("ItemClassificationId",listsaleItems.get(i).getItemClassificationId());
                jObj_item.put("BillingCategoryId",listsaleItems.get(i).getBillingCategoryId());
                jObj_item.put("SegmentId",listsaleItems.get(i).getSegmentId());
                jObj_item.put("RouteFrom",listsaleItems.get(i).getRouteFrom());
                jObj_item.put("RouteTo",listsaleItems.get(i).getRouteTo());

                /**************************Periodic schedule data************************************/
                jObj_item.put("RecStartDate",listsaleItems.get(i).getRecStartDate());
                jObj_item.put("PeriodicEndDate",listsaleItems.get(i).getPeriodicEndDate());
                jObj_item.put("RecEndDate",listsaleItems.get(i).getRecEndDate());
                jObj_item.put("ItemSrNo",listsaleItems.get(i).getItemSrNo());     //1
                jObj_item.put("ItemSize",listsaleItems.get(i).getItemSize());
                jObj_item.put("RecurDaysCount",listsaleItems.get(i).getRecurDaysCount());
                jObj_item.put("RecurWeeksCount",listsaleItems.get(i).getRecurWeeksCount());
                jObj_item.put("srno",listsaleItems.get(i).getSrno());     //1
                jObj_item.put("IsSunday",listsaleItems.get(i).getIsSunday());
                jObj_item.put("IsMonday",listsaleItems.get(i).getIsMonday());
                jObj_item.put("IsTuesday",listsaleItems.get(i).getIsTuesday());
                jObj_item.put("IsWednesday",listsaleItems.get(i).getIsWednesday());
                jObj_item.put("IsThursday",listsaleItems.get(i).getIsThursday());
                jObj_item.put("IsFriday",listsaleItems.get(i).getIsFriday());
                jObj_item.put("IsSaturday",listsaleItems.get(i).getIsSaturday());
                jObj_item.put("EveryMonthCount",listsaleItems.get(i).getEveryMonthCount());
                jObj_item.put("MonthlyDayNo",listsaleItems.get(i).getMonthlyDayNo());
                jObj_item.put("MonthlyMonth",listsaleItems.get(i).getMonthlyMonth());
                jObj_item.put("MonthlyWeek",listsaleItems.get(i).getMonthlyWeek());
                jObj_item.put("MonthlyDay",listsaleItems.get(i).getMonthlyDay());
                jObj_item.put("YearlyMonthName",listsaleItems.get(i).getYearlyMonthName());
                jObj_item.put("YearlyWeek",listsaleItems.get(i).getYearlyWeek());
                jObj_item.put("YearlyDay",listsaleItems.get(i).getYearlyDay());
                jObj_item.put("YearlyMonth",listsaleItems.get(i).getYearlyMonth());
                jObj_item.put("TypeOfPeriod",listsaleItems.get(i).getTypeOfPeriod());
                jObj_item.put("IsNoEndDate",listsaleItems.get(i).getIsNoEndDate());
                jObj_item.put("RecurYearCount",listsaleItems.get(i).getRecurYearCount());
                jObj_item.put("Occurrences",listsaleItems.get(i).getOccurrences());
                /************************************************************************************/

                jObj_item.put("IsProRata",listsaleItems.get(i).getIsProRata());   //0 or cnt
                jObj_item.put("ProFigure",listsaleItems.get(i).getProFigure());       //0 or cnt
                jObj_item.put("AllowPartShipment",listsaleItems.get(i).getAllowPartShipment());   //0 for false or 1 for true
                jObj_item.put("SalesFamilyHdrId",listsaleItems.get(i).getSalesFamilyHdrId());
                jObj_item.put("PriceListHdrId",listsaleItems.get(i).getPriceListHdrId());
                jObj_item.put("BQT_QuotationHeaderId",listsaleItems.get(i).getBQT_QuotationHeaderId());
                jObj_item.put("ContractHdrId",listsaleItems.get(i).getContractHdrId());

                jArray.put(jObj_item);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return jArray;
    }

    public JSONArray createJSON_scheduleArray(){
        jArray_schedule = new JSONArray();

        for(int i=0; i<jMainSchArray.length();i++){
            try {
                JSONObject job = jMainSchArray.getJSONObject(i);
                try {
                    jArray_schedule = new JSONArray();
                    jobj_schedule = new JSONObject();

                    jobj_schedule.put("SrNo",job.getString("SrNo"));
                    jobj_schedule.put("ItemSrNo",job.getString("ItemSrNo"));
                    jobj_schedule.put("ScheduleId",job.getString("ScheduleId")); //guid
                    jobj_schedule.put("ScheduleDate",job.getString("ScheduleDate"));
                    jobj_schedule.put("ExVendorDate",job.getString("ExVendorDate"));
                    jobj_schedule.put("Qty",listsaleItems.get(i).getQty());
                    jobj_schedule.put("BalQty",job.getString("BalQty"));
                    jobj_schedule.put("SoDate",job.getString("SoDate"));
                    jobj_schedule.put("FinalDeliverDate",job.getString("FinalDeliverDate"));
                    jobj_schedule.put("ItemProcessId",job.getString("ItemProcessId"));
                    jobj_schedule.put("ItemProcessCode",job.getString("ItemProcessCode"));
                    jobj_schedule.put("Action","");

                    jArray_schedule.put(jobj_schedule);

                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jArray_schedule;
    }

    /***********Edit ItemDetail********************/
    public void callEditItemDetailAPI(){
        if (isnet()) {

            new StartSession(SalesItemActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //call CreateSession API
                    new CreateSession().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    //progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class CreateSession extends AsyncTask<Integer, Void, Integer> {
        String res;
        Boolean IsSessionActivate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SessionSO +"?CallId=&CustId&='"+CustomerId+"'&Menu='SOMenu'";
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // dismissProgressDialog();
            if(res.equalsIgnoreCase("")){
                    //call another edit method
                callEditSODetailAPI();
            }
        }
    }

    public void callEditSODetailAPI(){
        if (isnet()) {
            new StartSession(SalesItemActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //call EditSOheader API
                    new GetEditData_SODetail().execute();
                }
                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    //progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class GetEditData_SODetail extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getSODetailEditData +"?HeaderId="+SoHeaderId;
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length()-1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.GONE);

            if(!res.equalsIgnoreCase("")){
                parseJsonData(res);
            }else {
                Toast.makeText(parent,"No data found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parseJsonData(String res){

        try {
            JSONArray j_Arr = new JSONArray(res);
            jMainItemArray = new JSONArray();

            for(int i=0; i<j_Arr.length(); i++){
                jObj_item = new JSONObject();
                JSONObject j  = j_Arr.getJSONObject(i);

                jObj_item.put("ChildId",ChildId);
                jObj_item.put("SeqNo",j.getString("SeqNo"));
                jObj_item.put("GLBItemDtlId",GLBItemDtlId);
                jObj_item.put("SODetailId",j.getString("SODetailId"));
                jObj_item.put("ItemMasterId",j.getString("ItemMasterId"));
                jObj_item.put("ItemCode",j.getString("ItemCode"));
                jObj_item.put("ItemDesc",j.getString("ItemDesc"));
                jObj_item.put("UOMMasterId",j.getString("UOMMasterId"));
                jObj_item.put("Qty",j.getString("Qty"));
                jObj_item.put("Rate",j.getString("Rate"));
                jObj_item.put("WarrantyCode",j.getString("WarrantyCode"));
                jObj_item.put("LineAmt",j.getString("LineAmt"));
                jObj_item.put("LineTaxes",j.getString("LineTaxes"));
                jObj_item.put("LineTotal",j.getString("LineTotal"));
                jObj_item.put("DiscAmount",j.getString("DiscAmount"));
                jObj_item.put("ProUnit",j.getString("ProUnit"));       //set value for it
                jObj_item.put("TaxClass",j.getString("TaxClassMasterId1"));
                jObj_item.put("DiscPC",j.getString("DiscPC"));
                jObj_item.put("ItemProcessId",j.getString("ItemProcessId"));
                jObj_item.put("Description",j.getString("Description"));
                jObj_item.put("ItemClassificationId",j.getString("ItemClassificationId"));
                jObj_item.put("BillingCategoryId",j.getString("BillingCategoryId"));
                jObj_item.put("SegmentId",j.getString("SegmentId"));
                jObj_item.put("RouteFrom",j.getString("RouteFrom"));
                jObj_item.put("RouteTo",j.getString("RouteTo"));

                //schedule, periodic json insert here
                /**************************Periodic schedule data************************************/
                RecStartDate = getDate(Long.parseLong(parseDate(j.getString("RecStartDate").replace("/",""))));
                PeriodicEndDate = getDate(Long.parseLong(parseDate(j.getString("PeriodicEndDate").replace("/",""))));
                RecEndDate = getDate(Long.parseLong(parseDate(j.getString("RecEndDate").replace("/",""))));
                jObj_item.put("RecStartDate",RecStartDate);
                jObj_item.put("PeriodicEndDate",PeriodicEndDate);
                jObj_item.put("RecEndDate",RecEndDate);
                jObj_item.put("ItemSrNo",j.getString("ItemSrNo"));     //1
                jObj_item.put("ItemSize",j.getString("ItemSize"));
                jObj_item.put("RecurDaysCount",j.getString("RecurDaysCount"));
                jObj_item.put("RecurWeeksCount",j.getString("RecurWeeksCount"));
                jObj_item.put("srno",j.getString("SeqNo"));     //1
                jObj_item.put("IsSunday",j.getString("IsSunday"));
                jObj_item.put("IsMonday",j.getString("IsMonday"));
                jObj_item.put("IsTuesday",j.getString("IsTuesday"));
                jObj_item.put("IsWednesday",j.getString("IsWednesday"));
                jObj_item.put("IsThursday",j.getString("IsThursday"));
                jObj_item.put("IsFriday",j.getString("IsFriday"));
                jObj_item.put("IsSaturday",j.getString("IsSaturday"));
                jObj_item.put("EveryMonthCount",j.getString("EveryMonthCount"));
                jObj_item.put("MonthlyDayNo",j.getString("MonthlyDayNo"));
                jObj_item.put("MonthlyMonth",j.getString("MonthlyMonth"));
                jObj_item.put("MonthlyWeek",j.getString("MonthlyWeek"));
                jObj_item.put("MonthlyDay",j.getString("MonthlyDay"));
                jObj_item.put("YearlyMonthName",j.getString("YearlyMonthName"));
                jObj_item.put("YearlyWeek",j.getString("YearlyWeek"));
                jObj_item.put("YearlyDay",j.getString("YearlyDay"));
                jObj_item.put("YearlyMonth",j.getString("YearlyMonth"));
                jObj_item.put("TypeOfPeriod",j.getString("TypeOfPeriod"));
                jObj_item.put("IsNoEndDate",j.getString("IsNoEndDate"));
                jObj_item.put("RecurYearCount",j.getString("RecurYearCount"));
                jObj_item.put("Occurrences",j.getString("Occurrences"));
                /************************************************************************************/

                jObj_item.put("IsProRata",j.getString("IsProRata"));   //0 or cnt
                jObj_item.put("ProFigure",j.getString("ProFigure"));       //0 or cnt
                jObj_item.put("AllowPartShipment",j.getString("AllowPartShipment"));   //0 for false or 1 for true
                jObj_item.put("SalesFamilyHdrId",j.getString("SalesFamilyHdrId"));
                jObj_item.put("PriceListHdrId",j.getString("PriceListHdrId"));
                jObj_item.put("BQT_QuotationHeaderId",j.getString("BQT_QuotationHeaderId"));
                jObj_item.put("ContractHdrId",j.getString("ContractHdrId"));

                jMainItemArray.put(jObj_item);

                jMainSchArray = new JSONArray();
                jobj_schedule = new JSONObject();

                jobj_schedule.put("SrNo","");
                jobj_schedule.put("ItemSrNo",j.getString("ItemSrNo"));
                jobj_schedule.put("ScheduleId",""); //guid
                jobj_schedule.put("ScheduleDate",getDate(Long.parseLong(parseDate(j.getString("DeliveryDt").replace("/","")))));
                jobj_schedule.put("ExVendorDate",getDate(Long.parseLong(parseDate(j.getString("DeliveryDt").replace("/","")))));
                jobj_schedule.put("Qty",j.getString("Qty"));
                jobj_schedule.put("BalQty","0");
                jobj_schedule.put("SoDate",getDate(Long.parseLong(parseDate(j.getString("SODate").replace("/","")))));
                jobj_schedule.put("FinalDeliverDate",getDate(Long.parseLong(parseDate(j.getString("DeliveryDt").replace("/","")))));
                jobj_schedule.put("ItemProcessId",j.getString("ItemProcessId"));
                jobj_schedule.put("ItemProcessCode",j.getString("ItemProcessCode"));
                jobj_schedule.put("Action","");

                jMainSchArray.put(jobj_schedule);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        setDataToList(jMainItemArray.toString());
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

    public String getUomCode(String uomId){
        String uomCode = "";
        String qry = "Select UOMCode from "+db.TABLE_UOM_new+" WHERE UOMMasterId='"+uomId+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            uomCode = c.getString(c.getColumnIndex("UOMCode"));

        }else {

        }
        return uomCode;
    }

    public boolean validate() {
        boolean val = false;

        if(listsaleItems.size()>0){
            Toast.makeText(parent,"No data to continue",Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else{
            val = true;
            return val;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getTaxFromDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIRECT_ITEM && resultCode == DIRECT_ITEM) {
            //get data from list and save it in table and display it in list
            String directItemArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            IsInclusiveTax = data.getStringExtra("IsInclusiveTax");
            IsMargin = data.getStringExtra("IsMargin");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = directItemArray;
            ScheduleDetails = scheduleArray;
           // HeaderDetails = headerArray;

            try {
                jMainItemArray = new JSONArray(directItemArray);
                jMainSchArray = new JSONArray(scheduleArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setDataToList(directItemArray);

        }else  if (requestCode == PRICELIST && resultCode == PRICELIST) {
            String priceListArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = priceListArray;
            ScheduleDetails = scheduleArray;
            // HeaderDetails = headerArray;

            //here rate is updating as per inclusive and exclusive value so no need to transfer true/false value.

            try {
                jMainItemArray = new JSONArray(priceListArray);
                jMainSchArray = new JSONArray(scheduleArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setDataToList(priceListArray);

        }else if (requestCode == SALES_FAMILY && resultCode == SALES_FAMILY) {
            String salrFamilyArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = salrFamilyArray;
            ScheduleDetails = scheduleArray;
            // HeaderDetails = headerArray;

            try {
                jMainItemArray = new JSONArray(salrFamilyArray);
                jMainSchArray = new JSONArray(scheduleArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setDataToList(salrFamilyArray);
        }else if (requestCode == QUOTATION && resultCode == QUOTATION) {
            String contractArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = contractArray;
            ScheduleDetails = scheduleArray;
            // HeaderDetails = headerArray;

            try {
                jMainItemArray = new JSONArray(contractArray);
                jMainSchArray = new JSONArray(scheduleArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setDataToList(contractArray);

        }else if (requestCode == CONTRACT && resultCode == CONTRACT) {
            String contractArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = contractArray;
            ScheduleDetails = scheduleArray;
            // HeaderDetails = headerArray;

            try {
                jMainItemArray = new JSONArray(contractArray);
                jMainSchArray = new JSONArray(scheduleArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setDataToList(contractArray);
        }
    }

}
