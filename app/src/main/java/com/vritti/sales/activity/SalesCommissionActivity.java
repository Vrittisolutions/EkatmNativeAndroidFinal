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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class SalesCommissionActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar progressBar;
    AutoCompleteTextView edt_commat,edt_comm_type,edt_payble_to,edt_payble_name;
    EditText edt_soamt,edt_credit,edt_creditamt,edt_commission,edt_commamt,edt_remark;
    Button btnadd,btn_cancel;
    AppCompatRadioButton rad_percntg, rad_amt;
    TextInputLayout lay_credptg,lay_credamt,lay_comptg, lay_comamt;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    String[] commat = {"Order Time","Invoice Time","Receipt Time"};
    String[] category = {"Customer","Vendor","Employee"};

    ArrayList<ConfigDropDownData> commtypelist;
    ArrayList<String> tempcommtypelist;

    public static final int SALES_COMMISSION_FILLED = 5;
    public static final int PAYABLETONAME = 1;
    String key_payble = "",PayableToName = "", PayableToId = "";

    JSONObject jobj_comm;
    JSONArray jArray;
    String finalOBJ = "";
    String CalculationMethod = "", CommissionType="",CommissionTypeId="",ProductAmt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_commission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getCommissionCnt() > 0){
            getCommTypeData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommTypeJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {
                    }
                });
            }
        }

        /*if(rad_amt.isSelected()){
            editTxtListener_Amount();
        }else if(rad_percntg.isSelected()){
            editTxtListener_pctg();
        }*/

        setListeners();
    }

    public void init(){
        parent = SalesCommissionActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Commission");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        lay_credptg = findViewById(R.id.lay_credptg);
        lay_credamt = findViewById(R.id.lay_credamt);
        lay_comptg = findViewById(R.id.lay_comptg);
        lay_comamt = findViewById(R.id.lay_comamt);
        edt_commat = findViewById(R.id.edt_commat);
        edt_payble_to = findViewById(R.id.edt_payble_to);
        edt_comm_type = findViewById(R.id.edt_comm_type);
        edt_payble_name = findViewById(R.id.edt_payble_name);
        edt_soamt = findViewById(R.id.edt_soamt);
        edt_credit = findViewById(R.id.edt_credit);
        edt_creditamt = findViewById(R.id.edt_creditamt);
        edt_commission = findViewById(R.id.edt_commission);
        edt_commamt = findViewById(R.id.edt_commamt);
        edt_remark = findViewById(R.id.edt_remark);
        rad_percntg = findViewById(R.id.rad_percntg);
        rad_amt = findViewById(R.id.rad_amt);
        btnadd = findViewById(R.id.btnadd);
        btn_cancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        ProductAmt = intent.getStringExtra("ProductAmt");

        edt_soamt.setText(ProductAmt);

        ut = new Utility();
        cf = new CommonFunction(SalesCommissionActivity.this);
        tcf = new Tbuds_commonFunctions(SalesCommissionActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesCommissionActivity.this);
        String dabasename = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesCommissionActivity.this, dabasename);
        CompanyURL = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesCommissionActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        commtypelist = new ArrayList<ConfigDropDownData>();
        tempcommtypelist = new ArrayList<String>();

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.myspinnerstyle,commat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,commat);
        edt_commat.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,category);
        edt_payble_to.setAdapter(adapter1);

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
                    jArray = createJSON();
                    finalOBJ = jArray.toString();

                    Intent intent = new Intent(SalesCommissionActivity.this, NewSalesOrderBooking.class);
                    intent.putExtra("jCommssnArray",finalOBJ);
                    setResult(SALES_COMMISSION_FILLED,intent);
                    finish();
                }
            }
        });

        edt_commat.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_commat.showDropDown();
                return false;
            }
        });

        edt_payble_to.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_payble_to.showDropDown();
                return false;
            }
        });

        edt_comm_type.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_comm_type.showDropDown();
                return false;
            }
        });

        edt_comm_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommissionType = commtypelist.get(position).getTypeDesc();
                CommissionTypeId = commtypelist.get(position).getCommTypeId();

            }
        });

        edt_payble_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"Customer","Vendor","Employee"
                if(edt_payble_to.getText().toString().trim().equals("Customer")){
                    key_payble = "c";
                }else if(edt_payble_to.getText().toString().trim().equals("Vendor")){
                    key_payble = "v";
                }else if(edt_payble_to.getText().toString().trim().equals("Employee")){
                    key_payble = "e";
                }

                if(key_payble.equalsIgnoreCase("") || key_payble.equalsIgnoreCase(null)){
                    Toast.makeText(parent,"Please select payable to",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(parent,PayableToName_ListActivity.class);
                    intent.putExtra("key_payble",key_payble);
                    startActivityForResult(intent,PAYABLETONAME);
                }
            }
        });

        rad_percntg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    CalculationMethod = rad_percntg.getText().toString();
                    rad_percntg.setChecked(true);
                    rad_amt.setChecked(false);

                    if(edt_creditamt.getText().toString().equalsIgnoreCase("") ||
                            edt_creditamt.getText().toString().equalsIgnoreCase(null)){

                    }else {
                        edt_commission.setText("0.00");
                        edt_commamt.setText("0.00");
                    }

                    edt_credit.setEnabled(true);
                    edt_commission.setEnabled(true);
                    edt_creditamt.setEnabled(false);
                    edt_commamt.setEnabled(false);
                    lay_credptg.setEnabled(true);
                    lay_comptg.setEnabled(true);
                    lay_credamt.setEnabled(false);
                    lay_comamt.setEnabled(false);
                    lay_comptg.setBackgroundColor(Color.parseColor("#ffffff"));
                    lay_credptg.setBackgroundColor(Color.parseColor("#ffffff"));
                    lay_credamt.setBackgroundColor(Color.parseColor("#ededed"));
                    lay_comamt.setBackgroundColor(Color.parseColor("#ededed"));

                }else {
                }
            }
        });

        rad_amt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    CalculationMethod = rad_amt.getText().toString();
                    rad_amt.setChecked(true);
                    rad_percntg.setChecked(false);

                    edt_credit.setText("0");
                    edt_creditamt.setText("0");

                    if(edt_commission.getText().toString().equalsIgnoreCase("") ||
                            edt_commission.getText().toString().equalsIgnoreCase(null) ||
                            edt_commamt.getText().toString().equalsIgnoreCase("") ||
                            edt_commamt.getText().toString().equalsIgnoreCase(null)  ){

                    }else {
                        edt_commission.setText("0.00");
                        edt_commamt.setText("0.00");
                    }

                    edt_credit.setEnabled(false);
                    edt_commission.setEnabled(false);
                    edt_creditamt.setEnabled(false);
                    edt_commamt.setEnabled(true);
                    lay_credptg.setEnabled(false);
                    lay_comptg.setEnabled(false);
                    lay_credamt.setEnabled(false);
                    lay_comamt.setEnabled(true);
                    lay_comptg.setBackgroundColor(Color.parseColor("#ededed"));
                    lay_credptg.setBackgroundColor(Color.parseColor("#ededed"));
                    lay_credamt.setBackgroundColor(Color.parseColor("#ededed"));
                    lay_comamt.setBackgroundColor(Color.parseColor("#ffffff"));

                }else {
                }
            }
        });

        edt_commamt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_amt.isChecked()){
                    float comAmt = 0.0f, com_ptg = 0.0f, soAmt = 0.0f;
                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        try{
                            if(edt_commamt.getText().toString().equalsIgnoreCase("") ||
                                    edt_commamt.getText().toString().equalsIgnoreCase(null)){
                                comAmt = 0.0f;
                            }else {
                                comAmt = Float.parseFloat(s.toString());
                            }

                            if(edt_soamt.getText().toString().equalsIgnoreCase("0.00") ||
                                    edt_soamt.getText().toString().equalsIgnoreCase("0.0") ||
                                    edt_soamt.getText().toString().equalsIgnoreCase("0")){
                                soAmt = 0;
                            }else {
                                soAmt = Float.parseFloat(edt_soamt.getText().toString());
                                com_ptg = (comAmt / soAmt)*100;
                                edt_commission.setText(String.format("%.2f",com_ptg));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }

            }
        });

        edt_credit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_percntg.isChecked()){
                    float cred_ptg = 0.0f, credAmt = 0.0f, com_ptg = 0.0f, comAmt = 0.0f, soAmt = 0.0f;

                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        if(edt_credit.getText().toString().equalsIgnoreCase("") ||
                                edt_credit.getText().toString().equalsIgnoreCase(null) ){
                            cred_ptg = 0;
                        }else {
                            cred_ptg = Float.parseFloat(s.toString());
                        }

                        if(edt_soamt.getText().toString().equalsIgnoreCase("") ||
                                edt_soamt.getText().toString().equalsIgnoreCase(null)){
                            soAmt = 0;
                        }else {
                            soAmt = Float.parseFloat(edt_soamt.getText().toString());
                            credAmt = soAmt * (cred_ptg/100);
                            edt_creditamt.setText(String.format("%.2f",credAmt));
                        }
                    }
                }
            }
        });

        edt_commission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_percntg.isChecked()){
                    float credAmt = 0.0f, com_ptg = 0.0f, comAmt = 0.0f;

                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        try{
                            if(edt_commission.getText().toString().equalsIgnoreCase("") ||
                                    edt_commission.getText().toString().equalsIgnoreCase(null) ){
                                com_ptg = 0;
                            }else {
                                com_ptg = Float.parseFloat(s.toString());
                            }

                            if(edt_creditamt.getText().toString().equalsIgnoreCase("") ||
                                    edt_creditamt.getText().toString().equalsIgnoreCase(null) ){
                                credAmt = 0;
                            }else {
                                credAmt = Float.parseFloat(edt_creditamt.getText().toString());
                                comAmt = credAmt * (com_ptg/100);
                                edt_commamt.setText(String.format("%.2f",comAmt));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }

            }
        });

    }

    public void editTxtListener_pctg(){
        edt_credit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_percntg.isChecked()){
                    float cred_ptg = 0.0f, credAmt = 0.0f, com_ptg = 0.0f, comAmt = 0.0f, soAmt = 0.0f;

                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        if(edt_credit.getText().toString().equalsIgnoreCase("") ||
                                edt_credit.getText().toString().equalsIgnoreCase(null) ){
                            cred_ptg = 0;
                        }else {
                            cred_ptg = Float.parseFloat(s.toString());
                        }

                        if(edt_soamt.getText().toString().equalsIgnoreCase("") ||
                                edt_soamt.getText().toString().equalsIgnoreCase(null)){
                            soAmt = 0;
                        }else {
                            soAmt = Float.parseFloat(edt_soamt.getText().toString());
                            credAmt = soAmt * (cred_ptg/100);
                            edt_creditamt.setText(String.format("%.2f",credAmt));
                        }
                    }
                }

            }
        });

        edt_commission.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_percntg.isChecked()){
                    float credAmt = 0.0f, com_ptg = 0.0f, comAmt = 0.0f;

                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        try{
                            if(edt_commission.getText().toString().equalsIgnoreCase("") ||
                                    edt_commission.getText().toString().equalsIgnoreCase(null) ){
                                com_ptg = 0;
                            }else {
                                com_ptg = Float.parseFloat(s.toString());
                            }

                            if(edt_creditamt.getText().toString().equalsIgnoreCase("") ||
                                    edt_creditamt.getText().toString().equalsIgnoreCase(null) ){
                                credAmt = 0;
                            }else {
                                credAmt = Float.parseFloat(edt_creditamt.getText().toString());
                                comAmt = credAmt * (com_ptg/100);
                                edt_commamt.setText(String.format("%.2f",comAmt));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }

            }
        });
    }

    public void editTxtListener_Amount(){

        edt_commamt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(rad_amt.isChecked()){
                    float comAmt = 0.0f, com_ptg = 0.0f, soAmt = 0.0f;
                    if(s.toString() == "" || s.toString() == null){

                    }else {

                        try{
                            if(edt_commamt.getText().toString().equalsIgnoreCase("") ||
                                    edt_commamt.getText().toString().equalsIgnoreCase(null)){
                                comAmt = 0.0f;
                            }else {
                                comAmt = Float.parseFloat(s.toString());
                            }

                            if(edt_soamt.getText().toString().equalsIgnoreCase("0.00") ||
                                    edt_soamt.getText().toString().equalsIgnoreCase("0.0") ||
                                    edt_soamt.getText().toString().equalsIgnoreCase("0")){
                                soAmt = 0;
                            }else {
                                soAmt = Float.parseFloat(edt_soamt.getText().toString());
                                com_ptg = (comAmt / soAmt)*100;
                                edt_commission.setText(String.format("%.2f",com_ptg));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

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

    class DownloadCommTypeJSON extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getCommissionType;

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

                    sql.delete(db.TABLE_COMMISSION,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String CommTypeId = jsonObject.getString("CommTypeId");
                            String TypeCode = jsonObject.getString("TypeCode");
                            String TypeDesc = jsonObject.getString("TypeDesc");
                            String VouMasterId = jsonObject.getString("VouMasterId");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setCommTypeId(CommTypeId);
                            dropdown.setTypeCode(TypeCode);
                            dropdown.setTypeDesc(TypeDesc);
                            dropdown.setVouMasterId(VouMasterId);

                            tcf.insertCommission(CommTypeId,TypeCode,TypeDesc,VouMasterId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getCommTypeData();

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

    public void getCommTypeData(){
        if(commtypelist.size() > 0){
            commtypelist.clear();
            tempcommtypelist.clear();
        }

        String payTerms = "Select * from "+db.TABLE_COMMISSION;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setCommTypeId(c.getString(c.getColumnIndex("CommTypeId")));
                dropdown.setTypeCode(c.getString(c.getColumnIndex("TypeCode")));
                dropdown.setTypeDesc(c.getString(c.getColumnIndex("TypeDesc")));
                dropdown.setVouMasterId(c.getString(c.getColumnIndex("VouMasterId")));

                tempcommtypelist.add(c.getString(c.getColumnIndex("TypeDesc")));
                commtypelist.add(dropdown);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempcommtypelist);
            edt_comm_type.setAdapter(adapter);

        }else {

        }
    }

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        String commissionID = uuid.toString();

        try {
            jArray = new JSONArray();
            jobj_comm = new JSONObject();

            jobj_comm.put("SrNo","1");
            jobj_comm.put("CommissionId",commissionID);  //guid
            jobj_comm.put("CommissionType",CommissionType);
            jobj_comm.put("CommissionAT",edt_commat.getText().toString());
            jobj_comm.put("CalculationMethod",CalculationMethod);
            jobj_comm.put("AgentName",PayableToName);
            jobj_comm.put("CreditPerc",edt_credit.getText().toString());
            jobj_comm.put("CreditAmount",edt_creditamt.getText().toString());
            jobj_comm.put("CommissionPerc",edt_commission.getText().toString());
            jobj_comm.put("CommissionAmount",edt_commamt.getText().toString());
            jobj_comm.put("CommissionTypeId",CommissionTypeId);
            jobj_comm.put("AgentId",PayableToId);
            jobj_comm.put("Remark",edt_remark.getText().toString());
            jobj_comm.put("CommissionFC","");
            jobj_comm.put("Action","");

            jArray.put(jobj_comm);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }
    
    /*  "CommisssionDetails": [
    {
      "SrNo": "1",
      "CommissionId": "undefined",
      "CommissionType": "Lead Incentive",
      "CommissionAT": "Invoice time",
      "CalculationMethod": "Percentage",
      "AgentName": "SUJATA COMPUTER PVT. LTD",
      "CreditPerc": "0.00",
      "CreditAmount": "0.00",
      "CommissionPerc": "5.00",
      "CommissionAmount": "10.0000",
      "CommissionTypeId": "2",
      "AgentId": "1196",
      "Remark": "Test",
      "CommissionFC": "10",
      "Action": "<div style=\"margin: 0 8px 0 0;position:relative;\"> <button data-toggle=\"dropdown\" class=\"btn btn-default\">Actions<b class=\"caret\"></b></button><ul role=\"menu\" class=\"dropdown-menu animated fadeInDown\" style=\"left:auto;\"><li onclick=\"funCommisionPopupEdit(1)\"><a href=\"#\">Edit</a></li><li onclick=\"funCommisionPopupDelete(1)\"><a href=\"#\">Delete</a></li></ul></div>"
    }
  ]
*/

    public boolean validate() {
        boolean val = false;

        if (edt_comm_type.getText().toString().equalsIgnoreCase("")
                && edt_commat.getText().toString().equalsIgnoreCase("")
                && edt_payble_to.getText().toString().equalsIgnoreCase("")
                && edt_payble_name.getText().toString().equalsIgnoreCase("")
                && edt_soamt.getText().toString().equalsIgnoreCase("")
                && edt_credit.getText().toString().equalsIgnoreCase("")
                && edt_commission.getText().toString().equalsIgnoreCase("")
                && edt_commamt.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SalesCommissionActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_comm_type.getText().toString().equalsIgnoreCase("") ||
                edt_comm_type.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Select commission type", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_commat.getText().toString().equalsIgnoreCase("") ||
                edt_commat.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Select commission at", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_payble_to.getText().toString().equalsIgnoreCase("") ||
                edt_payble_to.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Select payable to", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_payble_name.getText().toString().equalsIgnoreCase("") ||
                edt_payble_name.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Select payable name", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_soamt.getText().toString().equalsIgnoreCase("") ||
                edt_soamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "SO amount should not be empty", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_credit.getText().toString().equalsIgnoreCase("") ||
                edt_credit.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Enter credit %", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }/*else if(edt_creditamt.getText().toString().equalsIgnoreCase("") ||
                edt_creditamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Enter credit amount", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }*/else if(edt_commission.getText().toString().equalsIgnoreCase("") ||
                edt_commission.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Enter commission %", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_commamt.getText().toString().equalsIgnoreCase("") ||
                edt_commamt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesCommissionActivity.this, "Enter commission amount", Toast.LENGTH_SHORT).show();
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

        if (requestCode == PAYABLETONAME && resultCode == PAYABLETONAME) {
            //enable other buttons one by one
            PayableToId = data.getStringExtra("PayableToId");
            PayableToName = data.getStringExtra("PayableToName");

            edt_payble_name.setText(PayableToName);

        }
    }

}
