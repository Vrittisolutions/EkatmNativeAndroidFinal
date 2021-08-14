package com.vritti.sales.activity;

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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.TaxAdapter;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.InclusiveExclusiveTaxCalc;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.TaxGSTCalculationClass;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;
/*created by Chetana Salunkhe */
public class AddDirectItemActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    static TextView txt_totamt;
    static TextView txt_taxamt;
    Button btnadditem, btnsoschedule,btnperiodic, btncreateitem;
    ImageView imgtaxdetails;
    EditText edt_remark,edtsize, edtqty, edtunitrate, edtlineamt, edtdaycnt, edtdisc, edtdiscamt, edtaxclass,edtitemcode, edtitemdesc,
            txtdispcnt;
    //AppCompatRadioButton radbtnprorate;
    AppCompatCheckBox chk_allow, radbtnprorate;
    Switch swtchdisc;
    LinearLayout taxcetails;
    AutoCompleteTextView spinuom,spindays,edt_disc_option,edt_wrnty_desc,edtwarntycode;
    TextInputLayout txtlaydisc;
    ProgressBar progressBar;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    String[] days = {"Day","Week","Month","Year"};
    String[] disc = {"%","Per Unit"};
    ArrayList<String> listdays;

    String TaxClassMasterId = "", TaxClassCode = "", TaxClassDesc = "";
    String selTaxId = "", selTaxCode = "", selTaxDesc = "";
    private SharedPreferences sharedPrefs;
    String StartDate = "", EndDate = "";
    String selectedRecPtrn = "";

    ArrayList<ConfigDropDownData> wrntycodeList;
    ArrayList<ConfigDropDownData> wrntydescList;
    ArrayList<String> tempwrntycodeList;
    ArrayList<String> tempwrntydescList;
    ArrayList<ConfigDropDownData> uomList;
    ArrayList<String> tempuomList;

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

    float pctgval = 0.0f;
    JSONObject jObj_item;
    JSONArray jArray;
    String finalOBJ = "";
    String HeaderDetails = "",ScheduleDetails = "", PeriodicDetails = "";

    public static final int DIRECT_ITEM = 11;
    public static final int SOSCHEDULE = 1;
    public static final int PERIODIC = 2;
    public static final int TAXDATA = 9;
    public static final int ITEMCODEDESC = 12;

    boolean isInclusiveTax = false;
    Switch swtincltax;
    EditText edtmargin;
    InclusiveExclusiveTaxCalc inclTaxBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_direct_item);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getWarrantyCount() > 0){
            getWarrantyData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadWarrantyJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if(tcf.getUOMNewCount() > 0){
            getUOMData();
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
        setListeners();
    }

    public void init(){
        parent = AddDirectItemActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Direct Item");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        txtdispcnt = findViewById(R.id.txtdispcnt);
        txtdispcnt.setSelection(txtdispcnt.getText().toString().length());
        txtlaydisc = findViewById(R.id.txtlaydisc);
       // swtchdisc = findViewById(R.id.swtchdisc);
        chk_allow = findViewById(R.id.chk_allow);
        radbtnprorate = findViewById(R.id.radbtnprorate);
        btncreateitem = findViewById(R.id.btncreateitem);
        imgtaxdetails = findViewById(R.id.imgtaxdetails);
        btnperiodic = findViewById(R.id.btnperiodic);
        btnsoschedule = findViewById(R.id.btnsoschedule);
        btnadditem = findViewById(R.id.btnadditem);
        edtitemcode = findViewById(R.id.edtitemcode);
        edtitemdesc = findViewById(R.id.edtitemdesc);
        edt_disc_option = findViewById(R.id.edt_disc_option);
        txt_totamt = findViewById(R.id.txt_totamt);
        txt_taxamt = findViewById(R.id.txt_taxamt);
        edt_remark = findViewById(R.id.edt_remark);
        edtsize = findViewById(R.id.edtsize);
        edtqty = findViewById(R.id.edtqty);
        edtunitrate = findViewById(R.id.edtunitrate);
        edtlineamt = findViewById(R.id.edtlineamt);
        edtdaycnt = findViewById(R.id.edtdaycnt);
        edtdaycnt.setSelection(edtdaycnt.getText().toString().length());
        edtdisc = findViewById(R.id.edtdisc);
        edtdiscamt = findViewById(R.id.edtdiscamt);
        edtaxclass = findViewById(R.id.edtaxclass);
        edtwarntycode = findViewById(R.id.edtwarntycode);
        edt_wrnty_desc = findViewById(R.id.edt_wrnty_desc);
        spinuom = findViewById(R.id.spinuom);
        spindays = findViewById(R.id.spindays);
        swtincltax = findViewById(R.id.swtincltax);
        edtmargin = findViewById(R.id.edtmargin);

        Intent i = getIntent();
        HeaderDetails = i.getStringExtra("jHeaderArray");
        SoDate = i.getStringExtra("SoDate");
        SeqNo = i.getStringExtra("SeqNo");

        int sq = 0;
        if(SeqNo == "0" || SeqNo == "0.0"){
            sq = sq+1;
            SeqNo = "1";
            ItemSrNo = SeqNo;

        }else {
            sq = Integer.parseInt(SeqNo)+1;
            SeqNo = String.valueOf(sq);
            ItemSrNo = SeqNo;
        }

        ut = new Utility();
        cf = new CommonFunction(AddDirectItemActivity.this);
        tcf = new Tbuds_commonFunctions(AddDirectItemActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(AddDirectItemActivity.this);
        String dabasename = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(AddDirectItemActivity.this, dabasename);
        CompanyURL = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(AddDirectItemActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AddDirectItemActivity.this);
        StartDate = sharedPrefs.getString("StartDate", "");
        EndDate = sharedPrefs.getString("EndDate", "");

        listdays = new ArrayList<String>();
        tempwrntycodeList = new ArrayList<String>();
        tempwrntydescList = new ArrayList<String>();
        tempuomList = new ArrayList<String>();
        wrntycodeList = new ArrayList<ConfigDropDownData>();
        wrntydescList = new ArrayList<ConfigDropDownData>();
        uomList = new ArrayList<ConfigDropDownData>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,days);
        spindays.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,disc);
        edt_disc_option.setAdapter(adapter1);

        inclTaxBean = new InclusiveExclusiveTaxCalc();

    }

    private void setListeners() {

        swtincltax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(selTaxDesc == ""){
                    Toast.makeText(AddDirectItemActivity.this,"Select tax class first",Toast.LENGTH_LONG).show();
                }else {
                       if(swtincltax.isChecked()){
                    //set inclusive tax formula
                    //set mrp = (mrp/((100 + tax)/100));
                    isInclusiveTax = true;
                    //set taxclass value to it
                    //selTaxDesc = "SGST 9% + CGST 9% OUTPUT";
                    edtaxclass.setText(selTaxDesc);

                    selTaxId = getTaxFromDatabase(selTaxDesc);
                    TaxClassMasterId = selTaxId;

                    float discountedAmt=0;

                    if(edtdiscamt.getText().toString().equals("")){
                        discountedAmt = 0;
                    }else {
                        discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                    }

                    float val = Math.round(discountedAmt);

                    pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");

                }else {
                    //keep execution as it is existing
                    isInclusiveTax = false;
                }
                }
            }
        });

        btnadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    jArray = createJSON();
                    finalOBJ = jArray.toString();

                    Intent intent = new Intent(AddDirectItemActivity.this, SalesItemActivity.class);
                    intent.putExtra("jItemArray",finalOBJ);
                    intent.putExtra("jScheduleArray",ScheduleDetails);
                    intent.putExtra("jHeaderArray",HeaderDetails);
                    intent.putExtra("UOMMasterId",UOMMasterId);
                    intent.putExtra("IsInclusiveTax",String.valueOf(isInclusiveTax));

                    if(Float.parseFloat(edtmargin.getText().toString())>0){
                        intent.putExtra("IsMargin",edtmargin.getText().toString());
                    }else {
                        intent.putExtra("IsMargin","0");
                    }
                    setResult(DIRECT_ITEM,intent);
                    finish();
                }
            }
        });

        imgtaxdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, TaxClassDetails_itemActivity.class);
                intent.putExtra("TaxClassName",edtaxclass.getText().toString());
                intent.putExtra("TaxPctgVal",String.valueOf(pctgval));
                intent.putExtra("TaxAmt",txt_taxamt.getText().toString());
                intent.putExtra("TaxClassMasterId",TaxClassMasterId);
                startActivity(intent);
            }
        });

        edtitemcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,ItemCodeDescActivity.class);
                intent.putExtra("callFor","ItemCode");
                startActivityForResult(intent,ITEMCODEDESC);
            }
        });

        edtitemdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,ItemCodeDescActivity.class);
                intent.putExtra("callFor","ItemDesc");
                startActivityForResult(intent,ITEMCODEDESC);
            }
        });

        btnsoschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parent, SO_Periodic_ScheduleActivity.class);
                intent.putExtra("callFrom","SOSchedule");
                intent.putExtra("SoDate",SoDate);
                startActivityForResult(intent,SOSCHEDULE);
            }
        });


        btnperiodic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, SO_Periodic_ScheduleActivity.class);
                intent.putExtra("callFrom","Periodic");
                startActivityForResult(intent,PERIODIC);
            }
        });

        spindays.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spindays.showDropDown();
                return false;
            }
        });

        edt_disc_option.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_disc_option.showDropDown();
                return false;
            }
        });

        edtwarntycode.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtwarntycode.showDropDown();
                return false;
            }
        });

        edtwarntycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edt_wrnty_desc.setText(wrntycodeList.get(position).getWarrantyDesc());
                WarrantyCode = edtwarntycode.getText().toString();
            }
        });

        edt_wrnty_desc.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_wrnty_desc.showDropDown();
                return false;
            }
        });

        edt_wrnty_desc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtwarntycode.setText(wrntydescList.get(position).getWarrantyCode());
                WarrantyCode = wrntydescList.get(position).getWarrantyCode();
            }
        });

        edt_disc_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    edtdisc.setText("");
                    edtdiscamt.setText(edtlineamt.getText().toString());
                    txtlaydisc.setHint("%");
                }else if(position == 1){
                    edtdisc.setText("");
                    edtdiscamt.setText(edtlineamt.getText().toString());
                    txtlaydisc.setHint("₹");
                }

                float discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");
            }
        });

        spinuom.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinuom.showDropDown();
                return false;
            }
        });

        spinuom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UOMMasterId = uomList.get(position).getUOMMasterId();
            }
        });

        edtaxclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(parent,TaxClassActivity.class);
                    intent.putExtra("callFrom","DirectItem");
                    startActivityForResult(intent,TAXDATA);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        edtqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float urate = 0, qty = 0, lineamt = 0;
                float disc = 0, discAmt = 0, discInRs = 0;

                if(s.toString() == "" || s.toString() == null){
                    edtlineamt.setText("0.00");
                    edtdisc.setText("");
                    edtdiscamt.setText("0.00");
                    txt_taxamt.setText("0.00");
                    txt_totamt.setText("0.00");
                }else {

                    try{
                        if(edtunitrate.getText().toString().equalsIgnoreCase("") ||
                                edtunitrate.getText().toString().equalsIgnoreCase(null)){
                            //unit rate mandatory
                        }else {
                            if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                                qty = 0;
                                edtdisc.setText("");
                            }else {
                                qty = Float.parseFloat(edtqty.getText().toString());
                            }

                            qty = Float.parseFloat(edtqty.getText().toString());
                            urate = Float.parseFloat(edtunitrate.getText().toString());

                            //inclusive exclusive tax logic
                            urate =  inclTaxBean.calcRateInclTax(isInclusiveTax,urate,selTaxDesc,0);

                            //check for margin
                            if(edtmargin.getText().toString().equals("") || edtmargin.getText().toString().equals(null)){

                            }else {
                                float margin = Float.parseFloat(edtmargin.getText().toString());
                                urate = inclTaxBean.calcMarginVal(urate,margin);
                            }

                            lineamt = qty * urate;

                            edtlineamt.setText(String.format("%.2f",lineamt));
                            edtdiscamt.setText(edtlineamt.getText().toString());
                            //txt_totamt.setText(edtdiscamt.getText().toString());
                        }

                        if(edtdisc.getText().toString().equalsIgnoreCase("")){
                            disc = 0;
                        }else {
                            disc = Float.parseFloat(edtdisc.getText().toString());
                        }

                        lineamt = Float.parseFloat(edtlineamt.getText().toString());

                        if(edt_disc_option.getText().toString().equalsIgnoreCase("Per Unit")){
                            //disc = Float.parseFloat(edtdisc.getText().toString());

                            if(lineamt > disc){
                                discAmt = lineamt - disc;
                                edtdiscamt.setText(String.format("%.2f",discAmt));
                            }else {
                                disc = 0;
                                discAmt = lineamt - disc;
                                Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                            }

                            edtdiscamt.setText(String.format("%.2f",discAmt));

                        }else if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
                            //disc = Float.parseFloat(edtdisc.getText().toString());

                            discInRs = (lineamt * disc)/100;

                            if(lineamt > discInRs){

                                discAmt = lineamt - discInRs;
                                edtdiscamt.setText(String.format("%.2f",discAmt));
                            }else {
                                discInRs = 0;

                                discAmt = lineamt - discInRs;
                                Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                            }

                            edtdiscamt.setText(String.format("%.2f",discAmt));

                        }else {
                            edtdiscamt.setText(edtlineamt.getText().toString());
                           txt_totamt.setText(edtdiscamt.getText().toString());
                            //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                        }

                        if(edtaxclass.getText().toString().equalsIgnoreCase("")
                                || edtaxclass.getText().toString().equalsIgnoreCase(null)){
                            txt_totamt.setText(edtdiscamt.getText().toString());
                            //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                        }else {
                            float discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        edtunitrate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float urate = 0, qty = 0, lineamt = 0;
                float disc = 0, discAmt = 0, discInRs = 0;

                if(s.toString() == "" || s.toString() == null){
                    edtdiscamt.setText("0.00");
                }else {

                    try{
                        if(edtqty.getText().toString().equalsIgnoreCase("") ||
                                edtqty.getText().toString().equalsIgnoreCase(null)){
                            //qty mandatory
                        }else {
                            if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                                urate = 0;
                            }else {
                                urate = Float.parseFloat(edtunitrate.getText().toString());
                                urate =   inclTaxBean.calcRateInclTax(isInclusiveTax,urate,selTaxDesc,0);

                                if(edtmargin.getText().toString().equals("") || edtmargin.getText().toString().equals(null)){

                                }else {
                                    float margin = Float.parseFloat(edtmargin.getText().toString());
                                    urate = inclTaxBean.calcMarginVal(urate,margin);
                                }
                            }

                            qty = Float.parseFloat(edtqty.getText().toString());
                            //urate = Float.parseFloat(edtunitrate.getText().toString());
                            lineamt = qty * urate;

                            edtlineamt.setText(String.format("%.2f",lineamt));
                            edtdiscamt.setText(String.format("%.2f",lineamt));
                            txt_totamt.setText(edtdiscamt.getText().toString());
                            //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                        }

                        if(edtdisc.getText().toString().equalsIgnoreCase("")){
                            disc = 0;
                        }else {
                            disc = Float.parseFloat(edtdisc.getText().toString());
                        }
                        lineamt = Float.parseFloat(edtlineamt.getText().toString());

                        if(edt_disc_option.getText().toString().equalsIgnoreCase("Per Unit")){
                            //disc = Float.parseFloat(edtdisc.getText().toString());

                            if(lineamt > disc){
                                discAmt = lineamt - disc;
                                edtdiscamt.setText(String.format("%.2f",discAmt));
                            }else {
                                disc = 0;
                                discAmt = lineamt - disc;
                                Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                            }

                            edtdiscamt.setText(String.format("%.2f",discAmt));

                        }else if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
                            //disc = Float.parseFloat(edtdisc.getText().toString());

                            discInRs = (lineamt * disc)/100;

                            if(lineamt > discInRs){

                                discAmt = lineamt - discInRs;
                                edtdiscamt.setText(String.format("%.2f",discAmt));
                            }else {
                                discInRs = 0;

                                discAmt = lineamt - discInRs;
                                Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                            }

                            edtdiscamt.setText(String.format("%.2f",discAmt));
                        }else {
                            edtdiscamt.setText(edtlineamt.getText().toString());
                            txt_totamt.setText(edtdiscamt.getText().toString());
                            //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                        }

                        if(edtaxclass.getText().toString().equalsIgnoreCase("")
                                || edtaxclass.getText().toString().equalsIgnoreCase(null)){
                            txt_totamt.setText(edtdiscamt.getText().toString());
                           // txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                        }else {
                            float discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        edtdisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float lineamt = 0, disc = 0, discAmt = 0, discInRs = 0;

                lineamt = Float.parseFloat(edtlineamt.getText().toString());

                if(s.toString() == "" || s.toString() == null){

                }else {

                    try{
                        if(edtlineamt.getText().toString().equalsIgnoreCase("") ||
                                edtlineamt.getText().toString().equalsIgnoreCase(null)){
                            //qty mandatory
                        }else {

                            if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                                disc = 0;
                            }else {
                                disc = Float.parseFloat(edtdisc.getText().toString());
                            }

                            if(edt_disc_option.getText().toString().equalsIgnoreCase("Per Unit")){
                                //disc = Float.parseFloat(edtdisc.getText().toString());

                                if(lineamt > disc){
                                    discAmt = lineamt - disc;
                                    edtdiscamt.setText(String.format("%.2f",discAmt));
                                }else {
                                    disc = 0;
                                    discAmt = lineamt - disc;
                                    Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                                }

                                edtdiscamt.setText(String.format("%.2f",discAmt));

                            }else if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
                                //disc = Float.parseFloat(edtdisc.getText().toString());

                                discInRs = (lineamt * disc)/100;

                                if(lineamt > discInRs){

                                    discAmt = lineamt - discInRs;
                                    edtdiscamt.setText(String.format("%.2f",discAmt));
                                }else {
                                    discInRs = 0;

                                    discAmt = lineamt - discInRs;
                                    Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                                }

                                edtdiscamt.setText(String.format("%.2f",discAmt));
                            }

                            if(edtaxclass.getText().toString().equalsIgnoreCase("")
                                    || edtaxclass.getText().toString().equalsIgnoreCase(null)){
                                txt_totamt.setText(edtdiscamt.getText().toString());
                                //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                            }else {
                                float discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        edtmargin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                float urate = 0, qty = 0, lineamt = 0;
                float disc = 0, discAmt = 0, discInRs = 0;
                String discToCalc = "";

                try{
                    urate = Float.parseFloat(edtunitrate.getText().toString());
                    urate =   inclTaxBean.calcRateInclTax(isInclusiveTax,urate,selTaxDesc,0);

                    float tax = 0;    //get tax summation here

                }catch (Exception e){
                    e.printStackTrace();
                }

              //  taxClass = spintaxclass.getText().toString().trim();

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {


                } else {

                    float margin = Float.parseFloat(s.toString());

                    if(String.valueOf(urate).equalsIgnoreCase("")
                            || String.valueOf(urate).equalsIgnoreCase(null)){

                    }else {

                        //margin = Float.parseFloat(edtmargin.getText().toString());
                        urate = inclTaxBean.calcMarginVal(urate,margin);

                        qty = Float.parseFloat(edtqty.getText().toString());
                        //urate = Float.parseFloat(edtunitrate.getText().toString());
                        lineamt = qty * urate;

                        edtlineamt.setText(String.format("%.2f",lineamt));
                        edtdiscamt.setText(String.format("%.2f",lineamt));
                        txt_totamt.setText(edtdiscamt.getText().toString());

                    }

                    if(edtdisc.getText().toString().equalsIgnoreCase("")){
                        disc = 0;
                    }else {
                        disc = Float.parseFloat(edtdisc.getText().toString());
                    }
                    lineamt = Float.parseFloat(edtlineamt.getText().toString());

                    if(edt_disc_option.getText().toString().equalsIgnoreCase("Per Unit")){
                        //disc = Float.parseFloat(edtdisc.getText().toString());

                        if(lineamt > disc){
                            discAmt = lineamt - disc;
                            edtdiscamt.setText(String.format("%.2f",discAmt));
                        }else {
                            disc = 0;
                            discAmt = lineamt - disc;
                            Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                        }

                        edtdiscamt.setText(String.format("%.2f",discAmt));

                    }else if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
                        //disc = Float.parseFloat(edtdisc.getText().toString());

                        discInRs = (lineamt * disc)/100;

                        if(lineamt > discInRs){

                            discAmt = lineamt - discInRs;
                            edtdiscamt.setText(String.format("%.2f",discAmt));
                        }else {
                            discInRs = 0;

                            discAmt = lineamt - discInRs;
                            Toast.makeText(parent,"Discount amount should not be greater than line amount",Toast.LENGTH_SHORT).show();
                        }

                        edtdiscamt.setText(String.format("%.2f",discAmt));
                    }else {
                        edtdiscamt.setText(edtlineamt.getText().toString());
                        txt_totamt.setText(edtdiscamt.getText().toString());
                        //txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                    }

                    if(edtaxclass.getText().toString().equalsIgnoreCase("")
                            || edtaxclass.getText().toString().equalsIgnoreCase(null)){
                        txt_totamt.setText(edtdiscamt.getText().toString());
                        // txt_totamt.setText(Math.round(Float.parseFloat(edtdiscamt.getText().toString())));
                    }else {
                        float discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
                        pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),discountedAmt,"AddDirectItem");
                    }
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

    class DownloadWarrantyJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getWarranty;

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
            hideProgress();
            try{
                if(jResults != null){

                    sql.delete(db.TABLE_WARRANTY,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String WarrantyMasterId = jsonObject.getString("WarrantyMasterId");
                            String WarrantyDesc = jsonObject.getString("WarrantyDesc");
                            String WarrantyCode = jsonObject.getString("WarrantyCode");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setWarrantyMasterId(WarrantyMasterId);
                            dropdown.setWarrantyDesc(WarrantyDesc);
                            dropdown.setWarrantyCode(WarrantyCode);

                            tcf.insertWarranty(WarrantyMasterId,WarrantyDesc,WarrantyCode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getWarrantyData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public void getWarrantyData(){
        if(wrntycodeList.size() > 0){
            wrntycodeList.clear();
            tempwrntycodeList.clear();
        }

        if(wrntydescList.size() > 0){
            wrntydescList.clear();
            tempwrntydescList.clear();
        }

        String payTerms = "Select * from "+db.TABLE_WARRANTY;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setWarrantyMasterId(c.getString(c.getColumnIndex("WarrantyMasterId")));
                dropdown.setWarrantyDesc(c.getString(c.getColumnIndex("WarrantyDesc")));
                dropdown.setWarrantyCode(c.getString(c.getColumnIndex("WarrantyCode")));

                tempwrntycodeList.add(c.getString(c.getColumnIndex("WarrantyCode")));
                wrntycodeList.add(dropdown);

                tempwrntydescList.add(c.getString(c.getColumnIndex("WarrantyDesc")));
                wrntydescList.add(dropdown);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempwrntycodeList);
            edtwarntycode.setAdapter(adapter);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempwrntydescList);
            edt_wrnty_desc.setAdapter(adapter1);

        }else {

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
            showProgress();
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
            hideProgress();
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

                    getUOMData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void getUOMData(){
        if(uomList.size() > 0){
            uomList.clear();
            tempuomList.clear();
        }

        String payTerms = "Select * from "+db.TABLE_UOM_new;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{

                String UOMMasterId = c.getString(c.getColumnIndex("UOMMasterId"));
                String UOMCode = c.getString(c.getColumnIndex("UOMCode"));
                String UOMDesc = c.getString(c.getColumnIndex("UOMDesc"));
                String UOMDigit = c.getString(c.getColumnIndex("UOMDigit"));

                ConfigDropDownData dropdown = new ConfigDropDownData(UOMMasterId,UOMCode,UOMDesc,UOMDigit);

                tempuomList.add(c.getString(c.getColumnIndex("UOMDesc")));
                uomList.add(dropdown);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,R.layout.myspinnerstyle,tempuomList);
            spinuom.setAdapter(adapter);

        }else {

        }
    }

    public JSONArray createJSON(){
        UUID uuid = UUID.randomUUID();
        //SODetailId = uuid.toString();

        SeqNo = txtdispcnt.getText().toString();
        GLBItemDtlId = "0";
        Qty = edtqty.getText().toString();
        Rate = edtunitrate.getText().toString();
        LineAmt = edtlineamt.getText().toString();
        LineTaxes = txt_taxamt.getText().toString();
        LineTotal = txt_totamt.getText().toString();
        DiscAmount = String.valueOf(Float.parseFloat(edtlineamt.getText().toString()) -
                Float.parseFloat(edtdiscamt.getText().toString()));
        TaxClass = edtaxclass.getText().toString();
        //DiscAmount = edtdiscamt.getText().toString();
        WarrantyCode = edtwarntycode.getText().toString();

        if(spindays.getText().toString() == ""){
            ProUnit = "Day";
        }else {

        }

        if(DiscAmount == "" || DiscAmount == null){
            DiscAmount = "0";
        }

        if(edt_disc_option.getText().toString().equalsIgnoreCase("%")){
            DiscPC = edtdisc.getText().toString();
        }else {
            DiscPC = "0";
        }

        if(radbtnprorate.isChecked()){
            IsProRata = 1;
        }else {
            IsProRata = 0;
        }

        if(chk_allow.isChecked()){
            AllowPartShipment = 1;
        }else {
            AllowPartShipment = 0;
        }

        try {
            jArray = new JSONArray();
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
            /*jObj_item.put("LineTaxes",Float.parseFloat(LineTaxes));
            jObj_item.put("LineTotal",Float.parseFloat(LineTotal));*/
            jObj_item.put("DiscAmount",DiscAmount);
            jObj_item.put("ProUnit",ProUnit);       //set value for it
            jObj_item.put("TaxClass",TaxClassMasterId);
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

        return jArray;
    }

    public static void setTaxAmtData(float taxamt, float finalTotal_incltax, float pctgval){
        float final_val = Math.round(finalTotal_incltax);
        txt_taxamt.setText(String.format("%.02f",taxamt));
        txt_totamt.setText(String.format("%.02f",final_val));
    }

    public boolean validate() {
        boolean val = false;

        if(txtdispcnt.getText().toString().equalsIgnoreCase("") &&
                edtitemcode.getText().toString().equalsIgnoreCase("") &&
                edtitemdesc.getText().toString().equalsIgnoreCase("") &&
                edtunitrate.getText().toString().equalsIgnoreCase("") &&
                edtlineamt.getText().toString().equalsIgnoreCase("") &&
                edtaxclass.getText().toString().equalsIgnoreCase("") &&
                txt_taxamt.getText().toString().equalsIgnoreCase("") &&
                txt_totamt.getText().toString().equalsIgnoreCase("") &&
                spinuom.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(AddDirectItemActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(txtdispcnt.getText().toString().equalsIgnoreCase("") ||
                txtdispcnt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Display sequence count should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtitemcode.getText().toString().equalsIgnoreCase("") ||
                edtitemcode.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Select item code", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtitemdesc.getText().toString().equalsIgnoreCase("") ||
                edtitemdesc.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Select item desc", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtunitrate.getText().toString().equalsIgnoreCase("") ||
                edtunitrate.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Unit rate should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtlineamt.getText().toString().equalsIgnoreCase("") ||
                edtlineamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Line amount should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtaxclass.getText().toString().equalsIgnoreCase("") ||
                edtaxclass.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Select tax class", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(txt_taxamt.getText().toString().equalsIgnoreCase("") ||
                txt_taxamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Tax amount should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(txt_totamt.getText().toString().equalsIgnoreCase("") ||
                txt_totamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Total amount should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(spinuom.getText().toString().equalsIgnoreCase("") ||
                spinuom.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(AddDirectItemActivity.this, "Select UOM", Toast.LENGTH_SHORT).show();
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

        if (requestCode == TAXDATA && resultCode == TAXDATA) {
            //enable other buttons one by one
            selTaxCode = data.getStringExtra("TaxClassCode");
            selTaxDesc = data.getStringExtra("TaxClassDesc");
            selTaxId = data.getStringExtra("TaxClassID");
            edtaxclass.setText(selTaxDesc);

            TaxClassMasterId = selTaxId;

            float discountedAmt=0;

            if(edtdiscamt.getText().toString().equals("")){
                discountedAmt = 0;
            }else {
                discountedAmt = Float.parseFloat(edtdiscamt.getText().toString());
            }

            float val = Math.round(discountedAmt);

            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edtaxclass.getText().toString(),val,"AddDirectItem");

        }else if(requestCode == ITEMCODEDESC && resultCode == ITEMCODEDESC){
            ItemMasterId = data.getStringExtra("ItemMasterId");
            ItemCode = data.getStringExtra("ItemCode");
            ItemDesc = data.getStringExtra("ItemDesc");

            edtitemcode.setText(ItemCode);
            edtitemdesc.setText(ItemDesc);
        }else if(requestCode == SOSCHEDULE && resultCode == SOSCHEDULE){
            String scheduleArray = data.getStringExtra("jSchedule");
            ScheduleDetails = scheduleArray;
            try{
                JSONArray jsch = new JSONArray(ScheduleDetails);

                for(int i =0; i< jsch.length(); i++){
                    JSONObject jsonSch = jsch.getJSONObject(i);
                    edtqty.setText(jsonSch.getString("Qty"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

       // }else if(requestCode == PERIODIC && resultCode == PERIODIC){
            String periodicArray = data.getStringExtra("jPeriodic");
            PeriodicDetails = periodicArray;
            selectedRecPtrn = data.getStringExtra("RecPtrn");

            if(selectedRecPtrn.equalsIgnoreCase("D")){
                edtdaycnt.setText("1");
                spindays.setText("Day");
            }else if(selectedRecPtrn.equalsIgnoreCase("W")){
                edtdaycnt.setText("7");
                spindays.setText("Day");
            }else if(selectedRecPtrn.equalsIgnoreCase("M")){
                edtdaycnt.setText("30");
                spindays.setText("Day");
            }else if(selectedRecPtrn.equalsIgnoreCase("Y")){
                edtdaycnt.setText("12");
                spindays.setText("Month");
            }else {

            }

            try{
                JSONArray jdata = new JSONArray(PeriodicDetails);

                for(int i =0; i< jdata.length(); i++){
                    JSONObject jsonObject = jdata.getJSONObject(i);

                    RecStartDate = jsonObject.getString("RecStartDate");
                    PeriodicEndDate = jsonObject.getString("PeriodicEndDate");
                    RecEndDate = jsonObject.getString("RecEndDate");
                    ItemSrNo = jsonObject.getString("ItemSrNo");
                    ItemSize = jsonObject.getString("ItemSize");
                    RecurDaysCount = jsonObject.getString("RecurDaysCount");
                    RecurWeeksCount = jsonObject.getString("RecurWeeksCount");
                    srno = jsonObject.getString("srno");
                    IsSunday = jsonObject.getString("IsSunday");
                    IsMonday = jsonObject.getString("IsMonday");
                    IsTuesday = jsonObject.getString("IsTuesday");
                    IsWednesday = jsonObject.getString("IsWednesday");
                    IsFriday = jsonObject.getString("IsFriday");
                    IsThursday = jsonObject.getString("IsThursday");
                    IsSaturday = jsonObject.getString("IsSaturday");
                    EveryMonthCount = jsonObject.getString("EveryMonthCount");
                    MonthlyDayNo = jsonObject.getString("MonthlyDayNo");
                    MonthlyMonth = jsonObject.getString("MonthlyMonth");
                    MonthlyWeek = jsonObject.getString("MonthlyWeek");
                    MonthlyDay = jsonObject.getString("MonthlyDay");
                    YearlyMonthName = jsonObject.getString("YearlyMonthName");
                    YearlyWeek = jsonObject.getString("YearlyWeek");
                    YearlyDay = jsonObject.getString("YearlyDay");
                    YearlyMonth = jsonObject.getString("YearlyMonth");
                    TypeOfPeriod = jsonObject.getString("TypeOfPeriod");
                    RecurYearCount = jsonObject.getString("RecurYearCount");
                    IsNoEndDate = jsonObject.getString("IsNoEndDate");
                    Occurrences = jsonObject.getString("Occurrences");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public String getTaxFromDatabase(String taxClsDesc){
        String query = "Select TaxClassMasterId,TaxClassCode from "+ db.TABLE_TAXCLASS+" WHERE TaxClassDesc='"+taxClsDesc+"'";
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                TaxClassMasterId = c.getString(c.getColumnIndex("TaxClassMasterId"));
                selTaxCode = c.getString(c.getColumnIndex("TaxClassCode"));

            }while (c.moveToNext());
        }else {

        }

        return TaxClassMasterId;

    }

}
