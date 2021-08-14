package com.vritti.sales.CounterBilling;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.LocationPIActivity;
import com.vritti.inventory.physicalInventory.adapter.LocationListAdapter;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.sales.activity.AddDirectItemActivity;
import com.vritti.sales.activity.ProductList_TabActivity;
import com.vritti.sales.activity.Sales_OrderTypeSelectionActivity;
import com.vritti.sales.adapters.CustomerSelectionListAdapter;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.TalukaBean;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.TicketUpdateDEPLActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/*created by Chetana Salunkhe */
public class AddEditItemForCBilling extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ImageView imgrefresh;
    Button btndelete, btnsave, btncancel;
    AutoCompleteTextView edtitmcode, edtitmdesc, edtmrp,selwarehouse,sellocation,selordtype;
    EditText edtqty,/*edttaxinprcntg*/edtdisc,edtmargin;
    TextView txttaxinrups, txttotamt_incltax,txtlineamt, txtdisc,txttotwithdisc;
    AutoCompleteTextView spintaxclass;
    float mrp = 0,rate = 0, taxinrups = 0, taxinprcntg = 0, totincltax = 0, discinrups = 0,
            disc = 0, lineamt = 0, discamt = 0, totline_disc = 0;
    String itemCode = "", itemMRP = "";
    String ItemPlantId ="",ItemCode = "", ItemDesc = "", ItemMRP = "", TAXClass = "", TaxAmount = "", DiscAmount = "";
    String TaxClassMasterId = "", TaxClassCode = "", TaxClassDesc = "";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    ArrayList<CounterbillingBean> listdtls;
    ArrayList<CounterbillingBean> itemsDataList;
    ArrayList<String> itemdesc_List;
    ArrayList<String> itemcode_List;
    ArrayList<String> taxclass_List;
    ArrayList<String> pricelist;
    ArrayList<TaxClassBean> taxClassArrayList;

    String taxClass;
    Switch swtchdisc;
    String key;

    boolean isInclusiveTax = false;

    String taxcls[] = {"NO TAX", "IGST 18% OUTPUT", "SGST 9% + CGST 9% OUTPUT",
            "Karnataka SGST 9% output + Karnataka CGST 9% output", "CGST 14% OUTPUT + SGST 14% OUTPUT"};

    String WareHouseMasterId="",WarehouseDescription="";
    String LocationMasterId="",LocationDesc="";
    String OrderTypeId="",OrdTypeDescription="";
    ArrayList<String> whouseList;
    ArrayList<String>locList;

    ArrayList<LocationList>locationListArrayList;
    LocationListAdapter locationListAdapter;
    CommonFunction cf;
    SharedPreferences sharedpreferences;
    boolean IsShipInvRequired;
    List<String> lstOrdertype = new ArrayList<String>();
    ArrayList<Customer> OrderTypeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_adedt_item_for_cbilling);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Add/Edit bill details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        isInclusiveTax = true;

        Intent intent1 = getIntent();
        key = intent1.getStringExtra("CallFrom");

        if(key.equalsIgnoreCase("Edit")){

            btncancel.setVisibility(View.GONE);
            btndelete.setVisibility(View.VISIBLE);

            //get data from database and store to respected text views.
            Intent intent = getIntent();

            lineamt = Float.parseFloat(intent.getStringExtra("lineamt"));
            mrp = Float.parseFloat(intent.getStringExtra("mrp"));
            edtmrp.setText(String.format("%.02f",mrp));

            if(isInclusiveTax){
                spintaxclass.setFocusable(false);
                spintaxclass.setEnabled(false);
                spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                taxClass = spintaxclass.getText().toString().trim();
                float tax = checkTaxType(taxClass);  //get tax summation here

                /*inclusive tax frmula*/
                mrp = (mrp/((100 + tax)/100));
                rate = mrp;
            }else {
                spintaxclass.setFocusable(true);
                spintaxclass.setEnabled(true);

                mrp = mrp;
                rate = mrp;
            }

            String isdiscinrups = intent.getStringExtra("discinrups");
            String DISC = intent.getStringExtra("discount");
            String DISC_AMT = intent.getStringExtra("discamt");

            discamt = Float.parseFloat(intent.getStringExtra("discamt"));

            /*if(DISC == null ||
                    DISC == "0.0"){
                disc = 00.00F;
                discamt = 00.00F;
                totline_disc = lineamt - discamt;
            }else {
                disc = Float.parseFloat(intent.getStringExtra("discount"));
                discamt = (lineamt * disc)/100;
                totline_disc = lineamt - discamt;
            }*/

            if(DISC_AMT == null){
                disc = 00.00F;
                discamt = 00.00F;
                totline_disc = lineamt - discamt;
            }else {
                disc = Float.parseFloat(DISC);
                //discamt = (lineamt * disc)/100;
                totline_disc = lineamt - discamt;
            }

            edtdisc.setText(DISC);
            txtdisc.setText(String.format("%.02f",discamt));
            txttotwithdisc.setText(String.format("%.02f", totline_disc));

            edtitmcode.setText(intent.getStringExtra("itemcode"));
            edtitmdesc.setText(intent.getStringExtra("itemdesc"));
            edtqty.setText(intent.getStringExtra("qty"));
            //edtmrp.setText(String.format("%.02f",mrp));
            txtlineamt.setText(String.format("%.02f",lineamt));
         //   lineamt = lineamt - disc;
            edtdisc.setText(String.format("%.02f",disc));

            taxinrups = Float.parseFloat(intent.getStringExtra("taxamtinrps"));
            totincltax = Float.parseFloat(intent.getStringExtra("totinctax"));
            txttaxinrups.setText(String.format("%.02f",taxinrups));

            txttotamt_incltax.setText(String.format("%.02f",totincltax));

            taxClass = intent.getStringExtra("taxclass");

       //     checkTaxType(taxClass);

            if(taxClass.equalsIgnoreCase("")){

            }else {
                /*for(int t = 0; t < taxcls.length; t++){
                    if(taxClass.equalsIgnoreCase(taxcls[t].toString())){
                        int position = t;
                        String taxtypename = taxcls[t];
                        spintaxclass.setText(taxClass);
                        //spintaxclass.setSelection(1);

                    }else {
                    }
                }*/

                for(int t = 0; t < taxclass_List.size(); t++){
                    if(taxClass.equalsIgnoreCase(taxclass_List.get(t).toString())){
                        int position = t;
                        String taxtypename = taxclass_List.get(t).toString();
                        spintaxclass.setText(taxClass);
                        //spintaxclass.setSelection(1);

                    }else {
                    }
                }

            }

            lineamt = lineamt  - discamt;
            CSGT_SGST_calculate(taxClass,/*lineamt*/Float.parseFloat(txttotwithdisc.getText().toString().trim()));

        }else {
            btncancel.setVisibility(View.VISIBLE);
            btndelete.setVisibility(View.GONE);
        }

        //get data from API and fetch it and store item details in table/list and display it
        if(tcf.getCounterBillItemcount() > 0){
            try{
                getDataFromdataBase();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadItemsDataJSON().execute();
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
                        new DownloadTAXDataJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        //check location and warehouse
        //getWareHouse();

        //call warehouse API
        new GetMaterialWarehouse().execute();

        if (getOrderTypecount() > 0) {
            displayOrderType();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
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

        edtxtchnglistener();

        setListeners();
    }

    public void init(){
        parent = AddEditItemForCBilling.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Add bill details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_edititem);
        imgrefresh = (ImageView)findViewById(R.id.imgrefresh);

        selwarehouse = findViewById(R.id.selwarehouse);
        sellocation = findViewById(R.id.sellocation);
        selordtype = findViewById(R.id.selordtype);
        btnsave = (Button)findViewById(R.id.btnsave);
        btndelete = (Button)findViewById(R.id.btndelete);
        btncancel = (Button)findViewById(R.id.btncancel);
        edtitmcode = (AutoCompleteTextView) findViewById(R.id.edtitmcode);
        edtitmcode.setClickable(false);
        edtitmdesc = (AutoCompleteTextView)findViewById(R.id.edtitmdesc);
        edtqty = (EditText)findViewById(R.id.edtqty);
        edtmrp = (AutoCompleteTextView) findViewById(R.id.edtmrp);
        edtdisc = (EditText)findViewById(R.id.edtdisc);
        edtmargin = (EditText)findViewById(R.id.edtmargin);
        txttaxinrups = (TextView)findViewById(R.id.txttaxinrups);
        txttotamt_incltax = (TextView)findViewById(R.id.txttotamt_incltax);
        txtlineamt = (TextView)findViewById(R.id.txtlineamt);
        spintaxclass = (AutoCompleteTextView)findViewById(R.id.spintaxclass);
        txtdisc = (TextView)findViewById(R.id.txtdisc);
        txttotwithdisc = (TextView)findViewById(R.id.txttotwithdisc);
        swtchdisc = (Switch)findViewById(R.id.swtchdisc);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(AddEditItemForCBilling.this);
        cf = new CommonFunction(AddEditItemForCBilling.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        listdtls = new ArrayList<CounterbillingBean>();
        itemsDataList = new ArrayList<CounterbillingBean>();
        itemdesc_List = new ArrayList<String>();
        itemcode_List = new ArrayList<String>();
        taxclass_List = new ArrayList<String>();
        pricelist = new ArrayList<String>();
        whouseList = new ArrayList<String>();
        locList = new ArrayList<String>();
        taxClassArrayList = new ArrayList<TaxClassBean>();
        OrderTypeArrayList = new ArrayList<>();

    }

    public void setListeners(){
        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadItemsDataJSON().execute();
                        }
                        @Override
                        public void callfailMethod(String msg) {
                        }
                    });
                }
            }
        });

        selwarehouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selwarehouse.showDropDown();
                return false;
            }
        });

        sellocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selwarehouse.showDropDown();
                return false;
            }
        });

        selordtype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selordtype.showDropDown();
                return false;
            }
        });

        selwarehouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WarehouseDescription = selwarehouse.getText().toString();

                String qId = "Select WareHouseMasterId from "+dbhandler.TABLE_TICKET_UPDATE_WAREHOUSE+
                        " WHERE WarehouseDescription='"+WarehouseDescription+"'";
                Cursor cq = sql_db.rawQuery(qId,null);
                if(cq.getCount()>0){
                    cq.moveToFirst();
                    WareHouseMasterId = cq.getString(cq.getColumnIndex("WareHouseMasterId"));
                    //WareHouseMasterId = "1";        //test purpose
                }else {
                    WareHouseMasterId = "1";        //test purpose
                }

                getLocation();
            }
        });

        sellocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationDesc = sellocation.getText().toString();

                String qId = "Select LocationMasterId from "+dbhandler.TABLE_LOCATION_PI+ " WHERE LocationDesc='"+LocationDesc+"'";
                Cursor cq = sql_db.rawQuery(qId,null);
                if(cq.getCount()>0){
                    cq.moveToFirst();
                    LocationMasterId = cq.getString(cq.getColumnIndex("LocationMasterId"));

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("WareHouseMasterId", WareHouseMasterId);
                    editor.putString("WarehouseDescription", WarehouseDescription);
                    editor.putString("LocationMasterId", LocationMasterId);
                    editor.putString("LocationDesc", LocationDesc);
                    editor.commit();

                }
            }
        });

        selordtype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrdTypeDescription = selordtype.getText().toString();

                String qId = "Select OrderTypeMasterId from "+dbhandler.TABLE_OrderType+ " WHERE Description='"+OrdTypeDescription+"'";
                Cursor cq = sql_db.rawQuery(qId,null);
                if(cq.getCount()>0){
                    cq.moveToFirst();
                    OrderTypeId = cq.getString(cq.getColumnIndex("OrderTypeMasterId"));

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("OrderTypeId", OrderTypeId);
                    editor.commit();

                }
            }
        });

        edtitmdesc.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtitmcode.setText("");
                edtmrp.setText("");
                edtqty.setText("");

                String itemdesc = edtitmdesc.getText().toString().trim();
                try{
                    itemCode = getItemCodeID(itemsDataList, itemdesc);
                    edtitmcode.setText(itemCode);

                    String query = "SELECT ItemMRP FROM "+ dbhandler.TABLE_ADD_ITEMS_COUNTERBILL + " WHERE ItemCode='"+itemCode+"'";
                    Cursor c = sql_db.rawQuery(query,null);

                    if(c.getCount() != 0){
                        c.moveToFirst();
                        do{
                            itemMRP = c.getString(c.getColumnIndex("ItemMRP"));
                        }while (c.moveToNext());
                    }else {
                        itemMRP = "00.00";
                    }

                    edtmrp.setText(itemMRP);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        swtchdisc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(swtchdisc.isChecked()){
                    swtchdisc.setText("Discount in ₹ ");
                    edtdisc.setText("");
                    txtdisc.setText("00.00");
                    edtxtchnglistener();
                }else {
                    swtchdisc.setText("Discount in % ");
                    edtdisc.setText("");
                    txtdisc.setText("00.00");
                    edtxtchnglistener();
                }
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    savedtlsandexit();
                }
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletefromdatabase(edtitmcode.getText().toString());

            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spintaxclass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                taxClass = parent.getItemAtPosition(position).toString();
                taxClass = spintaxclass.getText().toString().trim();

                if(!taxClass.equalsIgnoreCase("") || !taxClass.equalsIgnoreCase(null)){
                    CSGT_SGST_calculate(taxClass, /*lineamt*/Float.parseFloat(txttotwithdisc.getText().toString().trim()));
                }else {

                }
            }
        });
    }

    public void edtxtchnglistener(){

        edtdisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtdisc.setText("");
                txttotwithdisc.setText("");
                txttaxinrups.setText("");
                txttotamt_incltax.setText("");

                lineamt = 0; mrp = 0;
                String qty = edtqty.getText().toString().trim();
                mrp = Float.parseFloat(edtmrp.getText().toString().trim());

                //int tax = 5;    //get tax summation here

                //if exclusive of tax mrp = mrp;
                if(isInclusiveTax){
                    spintaxclass.setFocusable(false);
                    spintaxclass.setEnabled(false);
                    spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                    taxClass = spintaxclass.getText().toString().trim();
                    float tax = checkTaxType(taxClass);  //get tax summation here

                    mrp = (mrp/((100 + tax)/100));
                    rate = mrp;
                }else {
                    spintaxclass.setFocusable(true);
                    spintaxclass.setEnabled(true);

                    mrp = mrp;
                    rate = mrp;
                }
                //if inclusive of tax mrp = (mrp/((100+tax)/100))

                    if(edtmargin.getText().toString().equals("") || edtmargin.getText().toString().equals(null)){

                    }else {
                        float margin = Float.parseFloat(edtmargin.getText().toString());

                        if(margin > 0){
                            mrp = (mrp / (1+(margin/100)));
                        }else {
                            mrp = mrp;
                        }

                        rate = mrp;
                    }

                lineamt = mrp * Float.parseFloat(qty);
                taxClass = spintaxclass.getText().toString().trim();

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                    lineamt = lineamt - 0;
                    txtdisc.setText("00.00");
                    txttotwithdisc.setText(String.valueOf(lineamt));
                    txttotamt_incltax.setText(String.valueOf(lineamt));

                } else {

                    if(swtchdisc.isChecked()){
                        //mrp = 0;
                        //mrp = Float.parseFloat(edtmrp.getText().toString());
                        mrp = mrp;
                        swtchdisc.setText("Discount in ₹ ");
                        disc = Float.parseFloat(String.valueOf(s));
                        discamt = disc;
                        lineamt = lineamt - disc;
                        txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txttotamt_incltax.setText(String.format("%.02f",lineamt));
                        txtdisc.setText(String.format("%.02f",discamt));
                    }else {
                        //mrp = 0;
                        //mrp = Float.parseFloat(edtmrp.getText().toString());
                        mrp = mrp;
                        swtchdisc.setText("Discount in % ");
                        disc = Float.parseFloat(String.valueOf(s));
                        discamt = (lineamt * disc)/100;
                        lineamt = lineamt - discamt;
                        txtdisc.setText(String.format("%.02f",discamt));
                        txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txttotamt_incltax.setText(String.format("%.02f",lineamt));
                    }
                }

                CSGT_SGST_calculate(taxClass, /*lineamt*/Float.parseFloat(txttotwithdisc.getText().toString().trim()));
            }
        });

        edtmrp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                lineamt = 0; mrp = 0;
                String qty = edtqty.getText().toString().trim();
                taxClass = spintaxclass.getText().toString().trim();

                float tax = 0;    //get tax summation here

                //if exclusive of tax mrp = mrp;
                if(isInclusiveTax){
                    spintaxclass.setFocusable(false);
                    spintaxclass.setEnabled(false);
                    spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                    taxClass = spintaxclass.getText().toString().trim();
                    tax = checkTaxType(taxClass);  //get tax summation here

                    mrp = (mrp/((100 + tax)/100));
                    rate = mrp;
                }else {
                    spintaxclass.setEnabled(true);
                    spintaxclass.setFocusable(true);
                    mrp = mrp;
                    rate = mrp;
                }
                //if inclusive of tax mrp = (mrp/((100+tax)/100))

                /**************************MRP exclusive of tax*********************************/

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                    if(qty.equalsIgnoreCase("") || qty.equalsIgnoreCase(null)){

                    }else {
                        lineamt = Float.parseFloat("0.00");
                        txtlineamt.setText(String.format("%.02f",lineamt));
                        txttotamt_incltax.setText(String.format("%.02f",lineamt));
                        /*txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txtdisc.setText("0.00");
                        edtdisc.setText("");*/
                        CSGT_SGST_calculate(taxClass, lineamt);
                    }

                } else {

                    edtdisc.setText("0.00");
                    edtmargin.setText("0");

                    if(qty.equalsIgnoreCase("") || qty.equalsIgnoreCase(null)){

                    }else {
                        mrp = Float.parseFloat(s.toString());

                       // int tax = 0;    //get tax summation here

                        //if exclusive of tax mrp = mrp;
                        if(isInclusiveTax){
                            spintaxclass.setFocusable(false);
                            spintaxclass.setEnabled(false);
                            spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                            taxClass = spintaxclass.getText().toString().trim();
                            tax = checkTaxType(taxClass);  //get tax summation here

                            mrp = (mrp/((100 + tax)/100));
                            rate = mrp;
                        }else {
                            spintaxclass.setFocusable(true);
                            spintaxclass.setEnabled(true);
                            mrp = mrp;
                            rate = mrp;
                        }
                        //if inclusive of tax mrp = (mrp/((100+tax)/100))

                        lineamt = mrp * Float.parseFloat(qty);
                        txtlineamt.setText(String.format("%.02f",lineamt));
                        txttotamt_incltax.setText(String.format("%.02f",lineamt));
                       /* txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txtdisc.setText("0.00");
                        edtdisc.setText("");*/
                        CSGT_SGST_calculate(taxClass, lineamt);
                    }
                }

                /****************************************************************************************/
                //MRP inclusive of tax
                /****************************************************************************************/
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
                lineamt = 0; mrp = 0;
                float qty = 0;
                String discToCalc = "";

                try{
                    mrp = Float.parseFloat(edtmrp.getText().toString().trim());

                    float tax = 0;    //get tax summation here

                    //if exclusive of tax mrp = mrp;
                    if(isInclusiveTax){
                        spintaxclass.setFocusable(false);
                        spintaxclass.setEnabled(false);
                        spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                        taxClass = spintaxclass.getText().toString().trim();
                        tax = checkTaxType(taxClass);  //get tax summation here

                        mrp = (mrp/((100 + tax)/100));
                        rate = mrp;
                    }else {
                        spintaxclass.setFocusable(true);
                        spintaxclass.setEnabled(true);

                        mrp = mrp;
                        rate = mrp;
                    }
                    //if inclusive of tax mrp = (mrp/((100+tax)/100))

                }catch (Exception e){
                    e.printStackTrace();
                    mrp = 0;
                }

                taxClass = spintaxclass.getText().toString().trim();

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                    txtlineamt.setText("00.00");
                    txtdisc.setText("00.00");
                    txttotwithdisc.setText("00.00");
                    txttotamt_incltax.setText("00.00");

                    if(String.valueOf(mrp).equalsIgnoreCase("") || String.valueOf(mrp).equalsIgnoreCase(null) ||
                            String.valueOf(mrp).equalsIgnoreCase("0")){

                    }else {

                        lineamt = Float.parseFloat("0.00");
                        txtlineamt.setText(String.valueOf(lineamt));
                        txttotamt_incltax.setText(String.valueOf(lineamt));

                        /*txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txtdisc.setText("0.00");
                        edtdisc.setText("");*/
                    }

                } else {

                    if(String.valueOf(mrp).equalsIgnoreCase("") || String.valueOf(mrp).equalsIgnoreCase(null)){

                    }else {

                        float margin = 0f;
                        if(edtmargin.getText().toString().equals("") || edtmargin.getText().toString().equals(null)){

                        }else {
                            margin = Float.parseFloat(edtmargin.getText().toString());

                            if(margin > 0){
                                mrp = (mrp / (1+(margin/100)));
                            }else {
                                mrp = mrp;
                            }

                            rate = mrp;
                        }

                        qty = Float.parseFloat(s.toString());
                        lineamt = mrp * Float.parseFloat(String.valueOf(qty));
                        txtlineamt.setText(String.format("%.02f",lineamt));
                        //txttotwithdisc.setText(String.format("%.02f",lineamt));
                        //txttotamt_incltax.setText(String.format("%.02f",lineamt));

                        discToCalc = edtdisc.getText().toString().trim();

                        if(discToCalc.equalsIgnoreCase("") || discToCalc.equalsIgnoreCase(null)){

                            txtdisc.setText("00.00");
                            txttotwithdisc.setText(String.format("%.02f",lineamt));
                            txttotamt_incltax.setText(String.format("%.02f",lineamt));

                        }else {
                            if(swtchdisc.isChecked()){
                                //mrp = Float.parseFloat(edtmrp.getText().toString());
                                mrp = mrp;
                                swtchdisc.setText("Discount in ₹ ");
                                disc = Float.parseFloat(discToCalc);
                                discamt = disc;
                                lineamt = lineamt - disc;
                                txttotwithdisc.setText(String.format("%.02f",lineamt));
                                txttotamt_incltax.setText(String.format("%.02f",lineamt));
                                txtdisc.setText(String.format("%.02f",discamt));
                            }else {
                                //mrp = Float.parseFloat(edtmrp.getText().toString());
                                mrp = mrp;
                                swtchdisc.setText("Discount in % ");
                                disc = Float.parseFloat(discToCalc);
                                discamt = (lineamt * disc)/100;
                                lineamt = lineamt - discamt;
                                txtdisc.setText(String.format("%.02f",discamt));
                                txttotwithdisc.setText(String.format("%.02f",lineamt));
                                txttotamt_incltax.setText(String.format("%.02f",lineamt));
                            }
                        }

                        if(!taxClass.equalsIgnoreCase("") || !taxClass.equalsIgnoreCase(null)){
                            CSGT_SGST_calculate(taxClass, /*lineamt*/Float.parseFloat(txttotwithdisc.getText().toString().trim()));
                        }else {

                        }
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
                lineamt = 0; mrp = 0;
                float qty = 0;
                String discToCalc = "";

                try{
                    mrp = Float.parseFloat(edtmrp.getText().toString().trim());

                    float tax = 0;    //get tax summation here

                    //if exclusive of tax mrp = mrp;
                    if(isInclusiveTax){
                        spintaxclass.setFocusable(false);
                        spintaxclass.setEnabled(false);
                        spintaxclass.setText("SGST 2.5% + CGST 2.5% INPUT");
                        taxClass = spintaxclass.getText().toString().trim();
                        tax = checkTaxType(taxClass);  //get tax summation here

                        mrp = (mrp/((100 + tax)/100));
                        rate = mrp;
                    }else {
                        spintaxclass.setFocusable(true);
                        spintaxclass.setEnabled(true);

                        mrp = mrp;
                        rate = mrp;
                    }
                    //if inclusive of tax mrp = (mrp/((100+tax)/100))

                }catch (Exception e){
                    e.printStackTrace();
                    mrp = 0;
                }

                taxClass = spintaxclass.getText().toString().trim();

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                    txtlineamt.setText("00.00");
                    txtdisc.setText("00.00");
                    txttotwithdisc.setText("00.00");
                    txttotamt_incltax.setText("00.00");

                    if(String.valueOf(mrp).equalsIgnoreCase("") || String.valueOf(mrp).equalsIgnoreCase(null) ||
                            String.valueOf(mrp).equalsIgnoreCase("0")){

                    }else {

                        lineamt = Float.parseFloat("0.00");
                        txtlineamt.setText(String.valueOf(lineamt));
                        txttotamt_incltax.setText(String.valueOf(lineamt));

                        /*txttotwithdisc.setText(String.format("%.02f",lineamt));
                        txtdisc.setText("0.00");
                        edtdisc.setText("");*/
                    }

                } else {

                    float margin = Float.parseFloat(s.toString());

                    if(String.valueOf(mrp).equalsIgnoreCase("") || String.valueOf(mrp).equalsIgnoreCase(null)){

                    }else {

                        rate = (mrp / (1+(margin/100)));

                        qty = Float.parseFloat(edtqty.getText().toString());
                        //lineamt = mrp * Float.parseFloat(String.valueOf(qty));
                        lineamt = rate * Float.parseFloat(String.valueOf(qty));
                        txtlineamt.setText(String.format("%.02f",lineamt));
                        //txttotwithdisc.setText(String.format("%.02f",lineamt));
                        //txttotamt_incltax.setText(String.format("%.02f",lineamt));

                        discToCalc = edtdisc.getText().toString().trim();

                        if(discToCalc.equalsIgnoreCase("") || discToCalc.equalsIgnoreCase(null)){

                            txtdisc.setText("00.00");
                            txttotwithdisc.setText(String.format("%.02f",lineamt));
                            txttotamt_incltax.setText(String.format("%.02f",lineamt));

                        }else {
                            if(swtchdisc.isChecked()){
                                //mrp = Float.parseFloat(edtmrp.getText().toString());
                                mrp = mrp;
                                swtchdisc.setText("Discount in ₹ ");
                                disc = Float.parseFloat(discToCalc);
                                discamt = disc;
                                lineamt = lineamt - disc;
                                txttotwithdisc.setText(String.format("%.02f",lineamt));
                                txttotamt_incltax.setText(String.format("%.02f",lineamt));
                                txtdisc.setText(String.format("%.02f",discamt));
                            }else {
                                //mrp = Float.parseFloat(edtmrp.getText().toString());
                                mrp = mrp;
                                swtchdisc.setText("Discount in % ");
                                disc = Float.parseFloat(discToCalc);
                                discamt = (lineamt * disc)/100;
                                lineamt = lineamt - discamt;
                                txtdisc.setText(String.format("%.02f",discamt));
                                txttotwithdisc.setText(String.format("%.02f",lineamt));
                                txttotamt_incltax.setText(String.format("%.02f",lineamt));
                            }
                        }

                        if(!taxClass.equalsIgnoreCase("") || !taxClass.equalsIgnoreCase(null)){
                            CSGT_SGST_calculate(taxClass, /*lineamt*/Float.parseFloat(txttotwithdisc.getText().toString().trim()));
                        }else {

                        }
                    }
                }
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

    public float checkTaxType(String taxClass){
        String igstType = "" , sgstType = "" , cgstType = "", ugstType = "", sgstVal = "", cgstVal = "", igstVal = "", ugstVal = "",
                vatVal = "", vatType = "";
        float pctgvalTocalc = 0.0F;

        if(taxClass.contains("IGST")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

           // String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    igstVal = pcgval[0];

                    float IGST = Float.parseFloat(igstVal);

                    float i1 = IGST / 2;

                    cgstType = "CGST";
                    cgstVal = String.valueOf(i1);

                    sgstType = "SGST";
                    sgstVal = String.valueOf(i1);
                }
            }

            pctgvalTocalc = Float.parseFloat(igstVal);

        }else if(taxClass.contains("CGST") && taxClass.contains("SGST") ){

            String[] taxtypes = taxClass.split("\\+");         // types[0] = SGST 9% , types[1] =CGST 9% OUTPUT

            System.out.println(taxtypes[0]);       //CGST 9 % OUTPUT
            // System.out.println(types[1]);       //SGST 9 % OUTPUT

            for(int i = 0; i < taxtypes.length; i++){
                String TYPE = "";
                String[] data;
                if(taxtypes[i].contains(".")){
                  data = taxtypes[i].split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
                }else {
                  data = taxtypes[i].split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
                }

                if(taxtypes[i].contains("CGST")){
                    cgstType = "CGST";
                    TYPE = cgstType;

                }else if(taxtypes[i].contains("SGST")){
                    sgstType = "SGST";
                    TYPE = sgstType;
                }

                for(int j = 0 ; j < data.length ; j++){
                    String[] pcgval = new String[0];

                    String taxTYPE = data[j];
                    System.out.println(taxTYPE);        //CGST, 9 % OUTPUT

                    if(data[j].contains("%")){
                        pcgval = data[j].split("%");
                        System.out.println(pcgval[0]);

                        if(TYPE.equalsIgnoreCase("CGST")){
                            cgstVal = String.valueOf(pcgval[0]);
                        }else if(TYPE.equalsIgnoreCase("SGST")){
                            sgstVal = String.valueOf(pcgval[0]);
                        }
                    }
                }
            }

            pctgvalTocalc = Float.parseFloat(cgstVal) + Float.parseFloat(sgstVal);

        }else if(taxClass.contains("SGST") || taxClass.contains("CGST") || taxClass.contains("UGST") || taxClass.contains("VAT")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            //String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    if(taxClass.contains("SGST")){
                        sgstVal = pcgval[0];
                        sgstType = "SGST";
                        pctgvalTocalc = Float.parseFloat(sgstVal);
                    }else if(taxClass.contains("CGST")){
                        cgstVal = pcgval[0];
                        cgstType = "CGST";
                        pctgvalTocalc = Float.parseFloat(cgstVal);
                    }else if(taxClass.contains("UGST")){
                        ugstVal = pcgval[0];
                        ugstType = "UGST";
                        pctgvalTocalc = Float.parseFloat(ugstVal);
                    }else if(taxClass.contains("VAT")){
                        vatVal = pcgval[0];
                        vatType = "VAT";
                        pctgvalTocalc = Float.parseFloat(vatVal);
                    }
                }
            }

        }else if(taxClass.contains("SGCT/CGST/IGST INCLUSIV")) {

        }

        return pctgvalTocalc;
    }

    public void savedtlsandexit(){

        boolean discinrups;
        String IGST="", CGST="", SGST="";

        String itmcode = edtitmcode.getText().toString().trim();
        String itmdesc = edtitmdesc.getText().toString().trim();    //itemdesc
        String qty = edtqty.getText().toString().trim();            //qty
        String discount = edtdisc.getText().toString().trim();      //disc in %
        mrp = Float.parseFloat(edtmrp.getText().toString().trim()); //mrp
        taxinrups = Float.parseFloat(txttaxinrups.getText().toString().trim()); //
        totincltax = Float.parseFloat(txttotamt_incltax.getText().toString());      //taxclass CGST + SGST
        taxClass = String.valueOf(spintaxclass.getText().toString().trim());

        if(isInclusiveTax){
            taxClass = spintaxclass.getText().toString().trim();
            float tax = checkTaxType(taxClass);  //get tax summation here

            mrp = (mrp/((100 + tax)/100));
            rate = mrp;
        }else {
            mrp = mrp;
            rate = mrp;
        }

        lineamt = mrp * Float.parseFloat(qty);      //lineamt
        totline_disc = Float.parseFloat(txttotwithdisc.getText().toString().trim());

        CounterbillingBean cbilling = new CounterbillingBean();
        cbilling.setItemCode(itmcode);
        cbilling.setItemDesc(itmdesc);
        cbilling.setQty(qty);
        cbilling.setRate(Float.parseFloat(String.format("%.02f",rate)));
        //cbilling.setMRP(Float.parseFloat(String.format("%.02f",edtmrp.getText().toString())));
        cbilling.setMRP(Float.parseFloat(edtmrp.getText().toString()));
        cbilling.setLineamt(Float.parseFloat(String.format("%.02f",lineamt)));
        cbilling.setDicountedTotal(Float.parseFloat(String.format("%.02f",totline_disc)));

        if(discount.equalsIgnoreCase("") || discount.equalsIgnoreCase(null)){
            cbilling.setDiscount(00.00F);
            discount = "00.00";
        }else {
            cbilling.setDiscount(Float.parseFloat(discount));
        }
        cbilling.setDiscamt(Float.parseFloat(String.format("%.02f",discamt)));

        if(swtchdisc.isChecked()){
            cbilling.setDiscinrupees(true);
            discinrups = true;

        }else {
            cbilling.setDiscinrupees(false);
            discinrups = false;
        }

        cbilling.setTaxclass(String.valueOf(taxClass));
        cbilling.setTax_inRups(String.valueOf(taxinrups));
        cbilling.setTotAmt_incltax_lineamt(Float.parseFloat(String.format("%.02f",totincltax)));

        listdtls.add(cbilling);

        if(key.equalsIgnoreCase("Edit")){
            ContentValues values = new ContentValues();
            values.put("itmcode",itmcode);
            values.put("qnty",qty);
            values.put("lineamt",String.valueOf(lineamt));
            values.put("discount",discount);
            values.put("taxclass",String.valueOf(taxClass));
            values.put("taxamtinrups",String.valueOf(taxinrups));
            values.put("total_incl_taxanddisc",String.valueOf(totincltax));
            values.put("taxinprcntg","");
            values.put("totwithdisc",String.valueOf(totline_disc));
            values.put("isbilluploaded","N");
            values.put("isdiscinrupees",String.valueOf(discinrups));
            values.put("discamt",String.valueOf(discamt));

          //  if(tcf.getcount_tempTable() > 0){
                sql_db.update(dbhandler.TABLE_ADD_ITMDTLS_FORBILL, values, "itmcode=?",new String[]{itmcode});
                Toast.makeText(parent," "+ itmcode + " item updated successfully",Toast.LENGTH_SHORT).show();
            /*}else {

                tcf.additmforbilling(itmcode, itmdesc, qty, String.valueOf(mrp), String.valueOf(lineamt),discount,String.valueOf(taxClass),
                        String.valueOf(taxinrups),String.valueOf(totincltax),"",String.valueOf(totline_disc),"N",
                        String.valueOf(discinrups), String.valueOf(discamt));      //taxType IGST SGST CGST
            }*/

        }else {
            tcf.additmforbilling(itmcode, itmdesc, qty, String.valueOf(edtmrp.getText().toString()),String.valueOf(rate),
                    String.valueOf(lineamt),discount,String.valueOf(taxClass),
                    String.valueOf(taxinrups),String.valueOf(totincltax),"",String.valueOf(totline_disc),"N",
                    String.valueOf(discinrups), String.valueOf(discamt));      //taxType IGST SGST CGST
        }

       // Intent intent = new Intent(this, ItemListCB.class);
      //  startActivity(intent);
        finish();
    }

    public void CSGT_SGST_calculate(String TAXcls, float netamt){
        float finalTotal_incltax = 0;
        float taxamt =  0;
        float pctgval = 0;
        txttaxinrups.setText("00.00");

        pctgval = checkTaxType(taxClass);

        /*if(taxClass.equalsIgnoreCase("NO TAX")){
           // txttaxinrups.setText("0.00");
            txttaxinrups.setEnabled(false);
            pctgval = 0;
        }else if(taxClass.equalsIgnoreCase("IGST 18% OUTPUT")){
            pctgval = 18;
        }else if(taxClass.equalsIgnoreCase("SGST 9% + CGST 9% OUTPUT")){
            pctgval = 18;
        }else if(taxClass.equalsIgnoreCase("Karnataka SGST 9% output + Karnataka CGST 9% output")){
            pctgval = 18;
        }else if(taxClass.equalsIgnoreCase("CGST 14% OUTPUT + SGST 14% OUTPUT")){
            pctgval = 28;
        }*/

        taxamt = (netamt * pctgval)/100;
        finalTotal_incltax = taxamt + netamt;

        txttaxinrups.setText(String.format("%.02f",taxamt));
        txttotamt_incltax.setText(String.format("%.02f",finalTotal_incltax));

    }

    public void deletefromdatabase(String itemcode){
        sql_db.execSQL("DELETE FROM " + dbhandler.TABLE_ADD_ITMDTLS_FORBILL + " WHERE itmcode='"+itemcode +"'");
        Toast.makeText(parent," "+ itemcode + " deleted from record successfully",Toast.LENGTH_SHORT).show();

        String data = "SELECT * from "+dbhandler.TABLE_ADD_ITMDTLS_FORBILL;
        Cursor c = sql_db.rawQuery(data,null);
        if(c.getCount() == 0){
            AppCommon.getInstance(parent).setBillingObject("");
        }

        finish();
    }

    class DownloadItemsDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                mprogress.setVisibility(View.VISIBLE);
                Toast.makeText(parent,"Downloading data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetItemsListForCounterBilling;

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
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
            mprogress.setVisibility(View.GONE);

            try{
                if(jResults != null){
                    parseJson(jResults);

                    new DownloadTAXDataJSON().execute();
                }else {
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void parseJson(JSONArray jResults){

        try{
            itemsDataList.clear();
            itemdesc_List.clear();

            tcf.clearTable(parent, dbhandler.TABLE_ADD_ITEMS_COUNTERBILL);

            for(int i=0; i<=jResults.length();i++){
                try {
                    JSONObject jsonObject = jResults.getJSONObject(i);
                    ItemPlantId = jsonObject.getString("ItemPlantId").trim();
                    ItemCode = jsonObject.getString("ItemCode").trim();
                    ItemDesc = jsonObject.getString("ItemDesc").trim();

                    CounterbillingBean cbean = new CounterbillingBean();
                    cbean.setItemPlantId(ItemPlantId);
                    cbean.setItemCode(ItemCode);
                    cbean.setItemDesc(ItemDesc);

                    itemsDataList.add(cbean);
                    itemdesc_List.add(ItemDesc);
                    itemcode_List.add(ItemCode);

                    String query = "Select PricelistRate, itemmasterid from "+ dbhandler.TABLE_ALL_CAT_SUBCAT_ITEMS +
                            " WHERE itemmasterid='"+ItemPlantId/*ItemCode*/+"'";
                    Cursor c = sql_db.rawQuery(query,null);

                    if(c.getCount() != 0){
                        c.moveToFirst();
                        do{
                            ItemMRP = c.getString(c.getColumnIndex("PricelistRate"));
                        }while (c.moveToNext());
                    }else {
                        ItemMRP = "00.00";
                    }

                    tcf.insertItemsData_CounterBilling(ItemPlantId, ItemCode, ItemDesc, ItemMRP, TAXClass, TaxAmount, DiscAmount);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            getDataFromdataBase();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getDataFromdataBase(){
        itemsDataList.clear();
        itemdesc_List.clear();

        listdtls.clear();

        //String p_qry = "Select PricelistRate,itemmasterid from "+ dbhandler.TABLE_ALL_CAT_SUBCAT_ITEMS;   //for mrp pricelist from order booking
        String p_qry = "Select ItemMRP,ItemPlantId from "+ dbhandler.TABLE_ADD_ITEMS_COUNTERBILL;   //for direct itemlist
        Cursor cp = sql_db.rawQuery(p_qry,null);
        if(cp.getCount()>0){
            cp.moveToFirst();
            do{
                ItemMRP = cp.getString(cp.getColumnIndex("ItemMRP")).trim();
                pricelist.add(cp.getString(cp.getColumnIndex("ItemPlantId")));
            }while (cp.moveToNext());
        }

        String pId = "(";
        for(int j = 0; j< pricelist.size(); j++){
            if(j!=0){
                pId = pId+",";
            }
            pId = pId + "'" + pricelist.get(j).toString() +"'";

        }
        pId = pId + ")";

        /******************************************************************************************/

        String query = "Select ItemPlantId,ItemCode,ItemDesc,ItemMRP from "+ dbhandler.TABLE_ADD_ITEMS_COUNTERBILL+
                " WHERE ItemPlantId IN "+pId;
        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                ItemPlantId = c.getString(c.getColumnIndex("ItemPlantId"));
                ItemCode = c.getString(c.getColumnIndex("ItemCode")).trim();
                ItemDesc = c.getString(c.getColumnIndex("ItemDesc")).trim();
                ItemMRP = c.getString(c.getColumnIndex("ItemMRP")).trim();

                CounterbillingBean cbean = new CounterbillingBean();
                cbean.setItemPlantId(ItemPlantId);
                cbean.setItemCode(ItemCode);
                cbean.setItemDesc(ItemDesc);

                itemsDataList.add(cbean);
                itemdesc_List.add(ItemDesc);
                itemcode_List.add(ItemCode);

            }while (c.moveToNext());

            ArrayAdapter<String> itmDescAdapter = new ArrayAdapter<String>(parent,
                    android.R.layout.simple_list_item_1,itemdesc_List);
            edtitmdesc.setAdapter(itmDescAdapter);

        }else {

        }

    }

    //get taxclass list
    class DownloadTAXDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
            Toast.makeText(parent,"Downloading data", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getTaxClassList;

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
            mprogress.setVisibility(View.GONE);

            try{
                if(jResults != null){
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

                /*TaxClassBean tcbean = new TaxClassBean();
                tcbean.setTaxClassMasterId(TaxClassMasterId);
                tcbean.setTaxClassCode(TaxClassCode);
                tcbean.setTaxClassDesc(TaxClassDesc);
                taxClassArrayList.add(tcbean);
                taxclass_List.add(TaxClassDesc);*/

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
        taxclass_List.clear();
        String query = "Select TaxClassMasterId,TaxClassCode,TaxClassDesc from "+ dbhandler.TABLE_TAXCLASS;
        Cursor c = sql_db.rawQuery(query,null);
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
                taxclass_List.add(TaxClassDesc);

            }while (c.moveToNext());

        }else {

        }

        ArrayAdapter<String> itmDescAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1,taxclass_List);
        spintaxclass.setAdapter(itmDescAdapter);

    }

    private String getItemCodeID(ArrayList<CounterbillingBean> lst_item, String itemdesc) throws JSONException {
        String itemCode =null;
        for(CounterbillingBean cBean : lst_item) {
            if(cBean.getItemDesc().equalsIgnoreCase(itemdesc)){
                itemCode = cBean.getItemCode();
            }
        }
        return itemCode; //it wasn't found at all
    }

    private boolean validate() {
        // TODO Auto-generated method stub

        if (edtitmdesc.getText().toString().equalsIgnoreCase("") ||
                edtitmdesc.getText().toString().equalsIgnoreCase(" ") ||
                edtitmdesc.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(this, "Please Select Item", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edtqty.getText().toString().equalsIgnoreCase("") ||
                edtqty.getText().toString().equalsIgnoreCase(" ") ||
                edtqty.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(this, "Please Fill quantity", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edtmrp.getText().toString().equalsIgnoreCase("") ||
                edtmrp.getText().toString().equalsIgnoreCase(" ") ||
                edtmrp.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(this, "Please Fill MRP", Toast.LENGTH_LONG).show();
            return false;
        }else if (OrderTypeId.equalsIgnoreCase("") || OrderTypeId.equalsIgnoreCase(" ") ||
                OrderTypeId.equalsIgnoreCase(null)) {
            Toast.makeText(this, "Please Select Order Type", Toast.LENGTH_LONG).show();
            return false;
        }else if (WareHouseMasterId.equalsIgnoreCase("") || WareHouseMasterId.equalsIgnoreCase(" ") ||
                WareHouseMasterId.equalsIgnoreCase(null)) {
            Toast.makeText(this, "Please Select Warehouse", Toast.LENGTH_LONG).show();
            return false;
        } else if (LocationMasterId.equalsIgnoreCase("") || LocationMasterId.equalsIgnoreCase(" ") ||
                LocationMasterId.equalsIgnoreCase(null)) {
            Toast.makeText(this, "Please Select Location", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void getWareHouse(){
        if(whouseList.size() > 0){
            whouseList.clear();
        }

        String q = "Select * from "+dbhandler.TABLE_TICKET_UPDATE_WAREHOUSE;
        Cursor c = sql_db.rawQuery(q,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                String warehouseId = c.getString(c.getColumnIndex("WareHouseMasterId"));
                String warehouseDesc = c.getString(c.getColumnIndex("WarehouseDescription"));

                whouseList.add(warehouseDesc);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,whouseList);
            selwarehouse.setAdapter(adapter);
            selwarehouse.showDropDown();

        }else {
            //call warehouse API
            new GetMaterialWarehouse().execute();
        }

    }

    private class GetMaterialWarehouse extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getWarehouse;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql_db.delete(dbhandler.TABLE_TICKET_UPDATE_WAREHOUSE, null,
                        null);
                Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_TICKET_UPDATE_WAREHOUSE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql_db.insert(dbhandler.TABLE_TICKET_UPDATE_WAREHOUSE, null, values);
                    Log.e("Warehouse : ", "" + a);

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

            getWareHouse();
        }

    }

    public void getLocation(){
        if(isnet()){
            //call location API
            new StartSession(AddEditItemForCBilling.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadGetLocationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                }
            });
        }
    }

    class DownloadGetLocationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Loading locations please wait...",Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetLocation+"?WareHouseMasterId="+WareHouseMasterId;

            try {
                res = ut.OpenConnection(url, AddEditItemForCBilling.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    locationListArrayList=new ArrayList<>();
                    locationListArrayList.clear();
                    locList.clear();
                    JSONArray jResults = new JSONArray(response);

                    tcf.clearTable(AddEditItemForCBilling.this,dbhandler.TABLE_LOCATION_PI);

                    for (int i = 0; i < jResults.length(); i++) {
                        LocationList locationList = new LocationList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        locationList.setLocationMasterId(jorder.getString("LocationMasterId"));
                        locationList.setLocationCode(jorder.getString("LocationCode"));
                        locationListArrayList.add(locationList);
                        locList.add(jorder.getString("LocationDesc"));

                        cf.insertLocationPI(jorder.getString("LocationMasterId"),
                                jorder.getString("LocationCode"),
                                jorder.getString("LocationDesc"),
                                WareHouseMasterId,UserMasterId);
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

            if (response.contains("[]")) {
                Toast.makeText(AddEditItemForCBilling.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                try{
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AddEditItemForCBilling.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(locationListArrayList);
                    editor.putString("location", json);
                    editor.commit();

                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEditItemForCBilling.this,
                           android.R.layout.simple_spinner_item,locList);
                   sellocation.setAdapter(adapter);
                   sellocation.showDropDown();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class DownloadOrderTypeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql_db.delete(dbhandler.TABLE_OrderType, null,null);
                    Cursor c = sql_db.rawQuery("SELECT * FROM " + dbhandler.TABLE_OrderType, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql_db.insert(dbhandler.TABLE_OrderType, null, values);
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
            displayOrderType();
        }
    }

    public int getOrderTypecount() {
        String countQuery = "SELECT  * FROM " + dbhandler.TABLE_OrderType;
        int count = 0;
        SQLiteDatabase sql = dbhandler.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private void displayOrderType() {
        lstOrdertype.clear();
        String countQuery = "SELECT  Description, OrderTypeMasterId, IsShipInvRequired FROM "+ dbhandler.TABLE_OrderType;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String OrderTypeMasterId = cursor.getString(cursor.getColumnIndex("OrderTypeMasterId"));
                String _IsShipInvRequired = cursor.getString(cursor.getColumnIndex("IsShipInvRequired"));

                if(_IsShipInvRequired.equalsIgnoreCase("N")){
                    IsShipInvRequired = false;
                }else if(_IsShipInvRequired.equalsIgnoreCase("Y")){
                    IsShipInvRequired = true;
                }

                if(!Description.equalsIgnoreCase("")){
                    lstOrdertype.add(Description);
                }

                Customer c_ordertype = new Customer();
                c_ordertype.setOrderType(Description);
                c_ordertype.setOrderTypeMasterId(OrderTypeMasterId);
                c_ordertype.setShipInvRequired(IsShipInvRequired);
                OrderTypeArrayList.add(c_ordertype);

            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(parent, R.layout.crm_custom_spinner_txt, lstOrdertype);
        selordtype.setAdapter(adapter);
        //list_ordertypes.setAdapter(adapter);
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
