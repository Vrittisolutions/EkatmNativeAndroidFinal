package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.databaselib.videocompression.Config;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class TermsAndConditionsActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;

    AutoCompleteTextView edt_delivery_terms,edt_disp_mode,edt_freight,edt_del_by,edt_perf_gurantee,edt_road_permit,edt_liq_damages,
            edt_pre_disp_inspection,edt_insp_charges,edt_security_deposite,edt_payterms;
    EditText edt_latebycnt,edt_sec_deposite,edt_perfmnce;
    Button btnsave,btn_cancel;
    ProgressBar progressBar;
   
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    String[] mode_dispathc = {"By Air","By Road", "By Sea"};
    String[] freight = {"Paid","To pay"};
    String[] invoice_delby = {"By Courier","By Mail"};
    String[] road_permit = {"Required","Not Required"};

    ArrayList<ConfigDropDownData> delTermsList;
    ArrayList<String> tempdelTermsList;
    ArrayList<ConfigDropDownData> perGaranList;
    ArrayList<String> tempperGaranList;
    ArrayList<ConfigDropDownData> liqDmgList;
    ArrayList<String> tempiqDmgList;
    ArrayList<ConfigDropDownData> preDispList;
    ArrayList<String> temppreDispList;
    ArrayList<ConfigDropDownData> inspChrgeList;
    ArrayList<String> tempinspChrgeList;
    ArrayList<ConfigDropDownData> secDepList;
    ArrayList<String> tempsecDepList;
    ArrayList<ConfigDropDownData> payTermsList;
    ArrayList<String> tempPayTermList;
    ArrayList<ConfigDropDownData> freightList;
    ArrayList<String> tempfreightList;
    ArrayList<ConfigDropDownData> invdelbyList;
    ArrayList<String> tempinvdelbyList;
    ArrayList<ConfigDropDownData> dispmodeList;
    ArrayList<String> tempdispmodeList;

    public static final int SALES_TANDC_FILLED = 4;
    public static final int PAYMENTTERMS = 1;

    String TermsCode = "", TermsDescription = "", PymtSettTermMasterId = "", IsDeleted = "", CreditDays = "", BaseDate = "";
    String DeliveryTermId="",InvoiceDeliveryById="",PaymenttermsId="",FreightsId="",ModeOfDispatchId="",PerfGarunteeMode="",
            PerfGarunteePer="",RoadPermit="",LiqDamges="",PreDispatch="",InspectionChgs="";

    JSONObject jobj_tc;
    JSONArray jArray;
    String finalOBJ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }
        init();

        if(cf.getConfigurationcount() > 0){
            //new getDropDownData().execute();
            displayDropdowns();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new getDropDownData().execute();
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
        parent = TermsAndConditionsActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Terms & Conditions");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        edt_delivery_terms = findViewById(R.id.edt_delivery_terms);
        edt_disp_mode = findViewById(R.id.edt_disp_mode);
        edt_freight = findViewById(R.id.edt_freight);
        edt_del_by = findViewById(R.id.edt_del_by);
        edt_perf_gurantee = findViewById(R.id.edt_perf_gurantee);
        edt_road_permit = findViewById(R.id.edt_road_permit);
        edt_liq_damages = findViewById(R.id.edt_liq_damages);
        edt_pre_disp_inspection = findViewById(R.id.edt_pre_disp_inspection);
        edt_insp_charges = findViewById(R.id.edt_insp_charges);
        edt_security_deposite = findViewById(R.id.edt_security_deposite);
        edt_payterms = findViewById(R.id.edt_payterms);
        edt_latebycnt = findViewById(R.id.edt_latebycnt);
        edt_latebycnt.setSelection(edt_latebycnt.getText().toString().length());
        edt_sec_deposite = findViewById(R.id.edt_sec_deposite);
        edt_sec_deposite.setSelection(edt_sec_deposite.getText().toString().length());
        edt_perfmnce = findViewById(R.id.edt_perfmnce);
        edt_perfmnce.setSelection(edt_perfmnce.getText().toString().length());
        btnsave = findViewById(R.id.btnsave);
        btn_cancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);

        ut = new Utility();
        cf = new CommonFunction(TermsAndConditionsActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(TermsAndConditionsActivity.this);
        String dabasename = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(TermsAndConditionsActivity.this, dabasename);
        CompanyURL = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(TermsAndConditionsActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        delTermsList = new ArrayList<ConfigDropDownData>();
        perGaranList = new ArrayList<ConfigDropDownData>();
        liqDmgList = new ArrayList<ConfigDropDownData>();
        preDispList = new ArrayList<ConfigDropDownData>();
        inspChrgeList = new ArrayList<ConfigDropDownData>();
        secDepList = new ArrayList<ConfigDropDownData>();
        payTermsList = new ArrayList<ConfigDropDownData>();
        freightList = new ArrayList<ConfigDropDownData>();
        invdelbyList = new ArrayList<ConfigDropDownData>();
        dispmodeList = new ArrayList<ConfigDropDownData>();

        tempdelTermsList = new ArrayList<String>();
        tempperGaranList = new ArrayList<String>();
        tempiqDmgList = new ArrayList<String>();
        temppreDispList = new ArrayList<String>();
        tempinspChrgeList = new ArrayList<String>();
        tempsecDepList = new ArrayList<String>();
        tempPayTermList = new ArrayList<String>();
        tempfreightList = new ArrayList<String>();
        tempinvdelbyList = new ArrayList<String>();
        tempdispmodeList = new ArrayList<String>();

      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mode_dispathc);
        edt_disp_mode.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,freight);
        edt_freight.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,invoice_delby);
        edt_del_by.setAdapter(adapter2);*/

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,road_permit);
        edt_road_permit.setAdapter(adapter3);
        
    }
    
    public void setListeners(){
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    jArray = createJSON();
                    finalOBJ = jArray.toString();

                    Intent intent = new Intent(TermsAndConditionsActivity.this, NewSalesOrderBooking.class);
                    intent.putExtra("jtcArray",finalOBJ);
                    setResult(SALES_TANDC_FILLED,intent);
                    finish();
                }

            }
        });

        edt_disp_mode.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_disp_mode.showDropDown();
                return false;
            }
        });

        edt_disp_mode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModeOfDispatchId = dispmodeList.get(position).getConfigurationDetailId();
            }
        });

        edt_del_by.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_del_by.showDropDown();
                return false;
            }
        });

        edt_del_by.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvoiceDeliveryById = invdelbyList.get(position).getConfigurationDetailId();
            }
        });


        edt_freight.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_freight.showDropDown();
                return false;
            }
        });

        edt_freight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FreightsId = freightList.get(position).getConfigurationDetailId();
            }
        });

        edt_road_permit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_road_permit.showDropDown();
                return false;
            }
        });

        edt_road_permit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    RoadPermit = "Y";
                }else if(position == 1){
                    RoadPermit = "N";
                }
            }
        });

        edt_delivery_terms.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_delivery_terms.showDropDown();
                return false;
            }
        });

        edt_delivery_terms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeliveryTermId = delTermsList.get(position).getConfigurationDetailId();
            }
        });

        edt_payterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,PaymentTermsActivity.class);
                startActivityForResult(intent,PAYMENTTERMS);
            }
        });

        edt_perf_gurantee.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_perf_gurantee.showDropDown();
                return false;
            }
        });

        edt_liq_damages.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_liq_damages.showDropDown();
                return false;
            }
        });

        edt_liq_damages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiqDamges = liqDmgList.get(position).getConfigurationDetailId();
            }
        });

        edt_pre_disp_inspection.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_pre_disp_inspection.showDropDown();
                return false;
            }
        });

        edt_pre_disp_inspection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreDispatch = preDispList.get(position).getConfigurationDetailId();
            }
        });

        edt_insp_charges.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_insp_charges.showDropDown();
                return false;
            }
        });

        edt_insp_charges.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InspectionChgs = inspChrgeList.get(position).getConfigurationDetailId();
            }
        });

        edt_security_deposite.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_security_deposite.showDropDown();
                return false;
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

    class getDropDownData extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getDropDown_tc + "?ConfigDT=DeliveryTerms&ConfigID=InvoiceDeliveryBy" +
                        "&ConfigFR=Freight&ConfigDM=DispatchMode&ConfigST=Status&ConfigLD=LiqDmg&ConfigPG=PerGar" +
                        "&ConfigPD=PreDisInsp&ConfigIC=InspChrg&ConfigSD=SecDep";

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

                    sql.delete(db.TABLE_CONFIGURATION,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String ConfigurationDetailId = jsonObject.getString("ConfigurationDetailId");
                            String ConfigurationDesc = jsonObject.getString("Configuration");
                            String ConfigurationName = jsonObject.getString("ConfigurationName");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setConfigurationDetailId(ConfigurationDetailId);
                            dropdown.setConfiguration(ConfigurationDesc);
                            dropdown.setConfigurationName(ConfigurationName);

                            cf.insertConfiguration(ConfigurationDetailId,ConfigurationDesc,ConfigurationName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    displayDropdowns();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void displayDropdowns(){
        /*delivery terms*/
        getDeliveryTerms();
        //getPaymentTerms();
        getPerfGurantee();
        getLiqDamages();
        getPreDispInsp();
        getInspCharges();
        getSecDeposite();
        getFreight();
        getModeOfDispatch();
        getInvDelBy();
    }

    private void getSecDeposite() {
        if(secDepList.size() > 0){
            secDepList.clear();
            tempsecDepList.clear();
        }

        String SecDep = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='SecDep'";
        Cursor c = sql.rawQuery(SecDep,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempsecDepList.add(c.getString(c.getColumnIndex("Configuration")));
                secDepList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempsecDepList);
            edt_security_deposite.setAdapter(adapter);

        }else {

        }
    }

    private void getInspCharges() {
        if(inspChrgeList.size() > 0){
            inspChrgeList.clear();
            tempinspChrgeList.clear();
        }

        String InspChrg = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='InspChrg'";
        Cursor c = sql.rawQuery(InspChrg,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempinspChrgeList.add(c.getString(c.getColumnIndex("Configuration")));
                inspChrgeList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempinspChrgeList);
            edt_insp_charges.setAdapter(adapter);

        }else {

        }
    }

    private void getPreDispInsp() {
        if(preDispList.size() > 0){
            preDispList.clear();
            temppreDispList.clear();
        }

        String PreDisInsp = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='PreDisInsp'";
        Cursor c = sql.rawQuery(PreDisInsp,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                temppreDispList.add(c.getString(c.getColumnIndex("Configuration")));
                preDispList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,temppreDispList);
            edt_pre_disp_inspection.setAdapter(adapter);

        }else {

        }
    }

    private void getLiqDamages() {
        if(liqDmgList.size() > 0){
            liqDmgList.clear();
            tempiqDmgList.clear();
        }

        String LiqDmg = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='LiqDmg'";
        Cursor c = sql.rawQuery(LiqDmg,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempiqDmgList.add(c.getString(c.getColumnIndex("Configuration")));
                liqDmgList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempiqDmgList);
            edt_liq_damages.setAdapter(adapter);

        }else {

        }
    }

    private void getPerfGurantee() {
        if(perGaranList.size() > 0){
            perGaranList.clear();
            tempperGaranList.clear();
        }

        String perGar = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='PerGar'";
        Cursor c = sql.rawQuery(perGar,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempperGaranList.add(c.getString(c.getColumnIndex("Configuration")));
                perGaranList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempperGaranList);
            edt_perf_gurantee.setAdapter(adapter);


        }else {

        }
    }

    private void getFreight() {
        if(freightList.size() > 0){
            freightList.clear();
            tempfreightList.clear();
        }

        String perGar = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='Freight'";
        Cursor c = sql.rawQuery(perGar,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempfreightList.add(c.getString(c.getColumnIndex("Configuration")));
                freightList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempfreightList);
            edt_freight.setAdapter(adapter);


        }else {

        }
    }

    private void getModeOfDispatch() {
        if(dispmodeList.size() > 0){
            dispmodeList.clear();
            tempdispmodeList.clear();
        }

        String perGar = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='DispatchMode'";
        Cursor c = sql.rawQuery(perGar,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempdispmodeList.add(c.getString(c.getColumnIndex("Configuration")));
                dispmodeList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempdispmodeList);
            edt_disp_mode.setAdapter(adapter);

        }else {

        }
    }

    private void getInvDelBy() {
        if(invdelbyList.size() > 0){
            invdelbyList.clear();
            tempinvdelbyList.clear();
        }

        String perGar = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='InvoiceDeliveryBy'";
        Cursor c = sql.rawQuery(perGar,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempinvdelbyList.add(c.getString(c.getColumnIndex("Configuration")));
                invdelbyList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempinvdelbyList);
            edt_del_by.setAdapter(adapter);


        }else {

        }
    }

    private void getPaymentTerms() {
        if(payTermsList.size() > 0){
            payTermsList.clear();
            tempPayTermList.clear();
        }

        String payTerms = "Select * from "+db.TABLE_PAYMENT_TERMS;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setTermsCode(c.getString(c.getColumnIndex("TermsCode")));
                dropdown.setTermsDescription(c.getString(c.getColumnIndex("TermsDescription")));
                dropdown.setPymtSettTermMasterId(c.getString(c.getColumnIndex("PymtSettTermMasterId")));
                dropdown.setIsDeleted(c.getString(c.getColumnIndex("IsDeleted")));
                dropdown.setCreditDays(c.getString(c.getColumnIndex("CreditDays")));
                dropdown.setBaseDate(c.getString(c.getColumnIndex("BaseDate")));

                tempPayTermList.add(c.getString(c.getColumnIndex("TermsDescription")));
                payTermsList.add(dropdown);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempPayTermList);
            edt_payterms.setAdapter(adapter);

        }else {

        }
    }

    private void getDeliveryTerms() {
        if(delTermsList.size() > 0){
            delTermsList.clear();
            tempdelTermsList.clear();
        }

        String delTerms = "Select * from "+db.TABLE_CONFIGURATION+ " WHERE ConfigurationName='DeliveryTerms'";
        Cursor c = sql.rawQuery(delTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setConfigurationName(c.getString(c.getColumnIndex("ConfigurationName")));
                dropdowndata.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                dropdowndata.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));

                tempdelTermsList.add(c.getString(c.getColumnIndex("Configuration")));
                delTermsList.add(dropdowndata);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempdelTermsList);
            edt_delivery_terms.setAdapter(adapter);

        }else {

        }
    }

    class getPaymentTermsData extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getPaymentTerms;

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

                    sql.delete(db.TABLE_PAYMENT_TERMS,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String TermsCode = jsonObject.getString("TermsCode");
                            String TermsDescription = jsonObject.getString("TermsDescription");
                            String PymtSettTermMasterId = jsonObject.getString("PymtSettTermMasterId");
                            String IsDeleted = jsonObject.getString("IsDeleted");
                            String CreditDays = jsonObject.getString("CreditDays");
                            String BaseDate = jsonObject.getString("BaseDate");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setTermsCode(TermsCode);
                            dropdown.setTermsDescription(TermsDescription);
                            dropdown.setPymtSettTermMasterId(PymtSettTermMasterId);
                            dropdown.setIsDeleted(IsDeleted);
                            dropdown.setCreditDays(CreditDays);
                            dropdown.setBaseDate(BaseDate);

                            cf.insertPaymentTerms(TermsCode,TermsDescription,PymtSettTermMasterId,IsDeleted,CreditDays,BaseDate);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getPaymentTerms();

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

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        String commissionID = uuid.toString();

        try {
            jArray = new JSONArray();
            jobj_tc = new JSONObject();

            jobj_tc.put("DeliveryTermId",DeliveryTermId);
            jobj_tc.put("InvoiceDeliveryById",InvoiceDeliveryById);  //guid
            jobj_tc.put("PaymenttermsId",PaymenttermsId);
            jobj_tc.put("FreightsId",FreightsId);
            jobj_tc.put("ModeOfDispatchId",ModeOfDispatchId);
            jobj_tc.put("remark","");
            jobj_tc.put("SecurityDepAmt",edt_sec_deposite.getText().toString());
            jobj_tc.put("PerfGarunteeMode",PerfGarunteeMode);
            jobj_tc.put("PerfGarunteePer",PerfGarunteePer);
            jobj_tc.put("RoadPermit",RoadPermit);
            jobj_tc.put("LiqDamges",LiqDamges);
            jobj_tc.put("PreDispatch",PreDispatch);
            jobj_tc.put("InspectionChgs",InspectionChgs);

            jArray.put(jobj_tc);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }
    
    /*  "DeliveryTermId": "2910",
  "InvoiceDeliveryById": "1928",
  "PaymenttermsId": "Ren/10",
  "FreightsId": "1900",
  "ModeOfDispatchId": "74",
  "remark": "Test",
  "SecurityDepAmt": "100",
  "PerfGarunteeMode": "",
  "PerfGarunteePer": "10",
  "RoadPermit": "N",
  "LiqDamges": "",
  "PreDispatch": "",
  "InspectionChgs": "",
*/

    public boolean validate() {
        boolean val = false;

        if(edt_payterms.getText().toString().equalsIgnoreCase("") ||
                edt_payterms.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(TermsAndConditionsActivity.this, "Select payment terms", Toast.LENGTH_SHORT).show();
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

        if (requestCode == PAYMENTTERMS && resultCode == PAYMENTTERMS) {
            //enable other buttons one by one
            TermsCode = data.getStringExtra("TermsCode");
            TermsDescription = data.getStringExtra("TermsDescription");
            PymtSettTermMasterId = data.getStringExtra("PymtSettTermMasterId");
            CreditDays = data.getStringExtra("CreditDays");
            BaseDate = data.getStringExtra("BaseDate");

            PaymenttermsId = PymtSettTermMasterId;

            edt_payterms.setText(TermsDescription);

        }
    }


}
