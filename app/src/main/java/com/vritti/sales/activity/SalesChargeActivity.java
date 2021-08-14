package com.vritti.sales.activity;

import android.content.ContentValues;
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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.TaxGSTCalculationClass;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class SalesChargeActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    TextInputLayout laypctg,laycharge,layqty;
    AutoCompleteTextView edt_charge,edt_calc_method,edt_taxclass;
    EditText edt_percntg, edt_tot_qty_val,edt_charge_amt;
    static EditText edt_taxamt,edt_totamt;
    Button btnadd,btn_cancel;
    ProgressBar progressBar;
    ListView listcharges;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;
    static Tbuds_commonFunctions tcf;
    ArrayList<String> taxclass_List;
    ArrayList<TaxClassBean> taxClassArrayList;

    ArrayList<String> chargeStringlist;
    ArrayList<ConfigDropDownData> chargeList;

    String[] calcMethod = {"Fix","% of value","Rate per quantity","Weight","Volume"};
    String TaxClassMasterId = "", TaxClassCode = "", TaxClassDesc = "";
    String selTaxId = "", selTaxCode = "", selTaxDesc = "",ChrgMasterId = "",ChrgDesc = "",CalcMethod="",
            FinalChargetaxAmt = "", FinalChargeAmt = "", finalqty = "", finalValue = "";

    public static final int TAXDATA = 9;
    public static final int SALES_CHARGE_FILLED = 3;
    float pctgval = 0.0f;

    JSONObject jobj_charge;
    JSONArray jArray;
    String finalOBJ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_charge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(cf.getChargeCount() > 0){
            getChargeData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadChargeData().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        /*if(tcf.gettaxclscount() > 0){
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
        }*/

        setListeners();
    }

    public void init(){
        parent = SalesChargeActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Charge");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        laypctg = findViewById(R.id.laypctg);
        laycharge = findViewById(R.id.laycharge);
        layqty = findViewById(R.id.layqty);
        edt_charge = findViewById(R.id.edt_charge);
        edt_calc_method = findViewById(R.id.edt_calc_method);
        edt_taxclass = findViewById(R.id.edt_taxclass);
        edt_percntg = findViewById(R.id.edt_percntg);
        edt_percntg.setSelection(edt_percntg.getText().toString().length());
        edt_tot_qty_val = findViewById(R.id.edt_tot_qty_val);
        edt_tot_qty_val.setHint("Total QTY / Value");
        edt_charge_amt = findViewById(R.id.edt_charge_amt);
        edt_taxamt = findViewById(R.id.edt_taxamt);
        edt_totamt = findViewById(R.id.edt_totamt);
        btnadd = findViewById(R.id.btnadd);
        btn_cancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        listcharges = findViewById(R.id.listcharges);

        ut = new Utility();
        cf = new CommonFunction(SalesChargeActivity.this);
        tcf = new Tbuds_commonFunctions(SalesChargeActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesChargeActivity.this);
        String dabasename = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesChargeActivity.this, dabasename);
        CompanyURL = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesChargeActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        Intent intent = getIntent();
        finalqty = intent.getStringExtra("Qty");
        finalValue = intent.getStringExtra("TotValue");

        taxclass_List = new ArrayList<String>();
        taxClassArrayList = new ArrayList<TaxClassBean>();

        chargeStringlist = new ArrayList<String>();
        chargeList = new ArrayList<ConfigDropDownData>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,calcMethod);
        edt_calc_method.setAdapter(adapter);

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

                    Intent intent = new Intent(SalesChargeActivity.this, NewSalesOrderBooking.class);
                    intent.putExtra("jChargeArray",finalOBJ);
                    intent.putExtra("FinalChargeAmt",FinalChargeAmt);
                    intent.putExtra("FinalChargetaxAmt",FinalChargetaxAmt);
                    setResult(SALES_CHARGE_FILLED,intent);
                    finish();
                }
            }
        });

        edt_charge.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_charge.showDropDown();
                return false;
            }
        });

        edt_charge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChrgMasterId = chargeList.get(position).getChargeMasterId();
                ChrgDesc = chargeList.get(position).getChargeDesc();
            }
        });

        edt_calc_method.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_calc_method.showDropDown();
                return false;
            }
        });

        edt_calc_method.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(edt_calc_method.getText().toString().equalsIgnoreCase("Weight") ||
                        edt_calc_method.getText().toString().equalsIgnoreCase("Rate per quantity") ||
                        edt_calc_method.getText().toString().equalsIgnoreCase("Fix")){
                    edt_tot_qty_val.setHint("Total QTY");
                    layqty.setHint("Total QTY");
                    laypctg.setHint("Rate");
                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");
                }else  if(edt_calc_method.getText().toString().equalsIgnoreCase("Volume") ||
                        edt_calc_method.getText().toString().equalsIgnoreCase("% of value") ){
                    edt_tot_qty_val.setHint("Total Value");
                    layqty.setHint("Total Value");
                    laypctg.setHint("%");
                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");
                }else {
                    edt_tot_qty_val.setHint("Total QTY / Value");
                    layqty.setHint("Total QTY / Value");
                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");
                }

                if(edt_calc_method.getText().toString().equalsIgnoreCase("Fix")){
                    edt_charge_amt.setEnabled(true);
                    edt_charge_amt.setFocusable(true);
                    edt_charge_amt.setClickable(true);
                    edt_percntg.setEnabled(false);
                    edt_percntg.setText("0");
                    edt_tot_qty_val.setText("0.00");

                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");

                    laycharge.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    laypctg.setBackgroundColor(Color.parseColor("#EDEDED"));

                }else if(edt_calc_method.getText().toString().equalsIgnoreCase("% of value")){
                    edt_charge_amt.setEnabled(false);
                    //edt_charge_amt.setFocusable(false);
                    edt_percntg.setEnabled(true);
                    edt_percntg.setFocusable(true);

                    edt_tot_qty_val.setText(finalValue);
                    edt_charge_amt.setText("0.00");

                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");

                    laycharge.setBackgroundColor(Color.parseColor("#EDEDED"));
                    laypctg.setBackgroundColor(Color.parseColor("#FFFFFF"));

                }else if(edt_calc_method.getText().toString().equalsIgnoreCase("Rate per quantity") ||
                        edt_calc_method.getText().toString().equalsIgnoreCase("Weight") ||
                        edt_calc_method.getText().toString().equalsIgnoreCase("Volume")){
                    edt_charge_amt.setEnabled(false);
                    //edt_charge_amt.setFocusable(false);
                    edt_percntg.setEnabled(true);
                    edt_percntg.setFocusable(true);

                    edt_tot_qty_val.setText(finalqty);
                    edt_charge_amt.setText("0.00");

                    edt_taxclass.setText("");
                    edt_taxamt.setText("0.00");
                    edt_totamt.setText("0.00");

                    laycharge.setBackgroundColor(Color.parseColor("#EDEDED"));
                    laypctg.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

       /* edt_taxclass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_taxclass.showDropDown();
                return false;
            }
        });*/

        edt_taxclass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,TaxClassActivity.class);
                intent.putExtra("callFrom","Charge");
                startActivityForResult(intent,TAXDATA);
            }
        });

        edt_charge_amt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() == "" || s.toString() == null){

                }else {

                }

            }
        });

        edt_percntg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString() == "" || s.toString() == null){

                }else {
                    float pctg = 0.0f, val = 0.0f, chgAmt = 0.0f, taxAmt = 0.0f, totTaxAmt = 0.0f;

                    if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                        //edt_percntg.setText("0");
                        pctg = 0;
                    }else {
                        pctg = Float.parseFloat(edt_percntg.getText().toString());
                    }

                    if(edt_calc_method.getText().toString().equalsIgnoreCase("% of value")){
                        val = Float.parseFloat(finalValue);
                       // pctg = Float.parseFloat(edt_percntg.getText().toString());

                        if(val == 0 || val == 0.00 || pctg == 0 || pctg == 0.00){
                            edt_charge_amt.setText("0.00");
                        }else {
                            //chgAmt = pctg on val
                            chgAmt = val * (pctg/100);

                            edt_charge_amt.setText(String.format("%.2f",chgAmt));
                        }
                    }else if(edt_calc_method.getText().toString().equalsIgnoreCase("Rate per quantity")){
                        val = Float.parseFloat(finalqty);
                       // pctg = Float.parseFloat(edt_percntg.getText().toString());

                        if(val == 0 || val == 0.00 || pctg == 0 || pctg == 0.00){
                            edt_charge_amt.setText("0.00");
                        }else {
                            //chgAmt = pctg on val
                            chgAmt = val * pctg;

                            edt_charge_amt.setText(String.format("%.2f",chgAmt));
                        }
                    }else {

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

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    //get taxclass list
    class DownloadTAXDataJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
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
            hideProgress();

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
                taxclass_List.add(TaxClassDesc);

            }while (c.moveToNext());

        }else {

        }

        ArrayAdapter<String> itmDescAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_spinner_item,taxclass_List);
        edt_taxclass.setAdapter(itmDescAdapter);

    }

    class DownloadChargeData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getChargeCode;

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

                    sql.delete(db.TABLE_CHARGE,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String ChargeMasterId = jsonObject.getString("ChargeMasterId");
                            String ChargeCode = jsonObject.getString("ChargeCode");
                            String ChargeDesc = jsonObject.getString("ChargeDesc");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setChargeMasterId(ChargeMasterId);
                            dropdown.setChargeCode(ChargeCode);
                            dropdown.setChargeDesc(ChargeDesc);

                            chargeList.add(dropdown);
                            chargeStringlist.add(ChargeDesc);

                            cf.insertCharge(ChargeMasterId,ChargeCode,ChargeDesc);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getChargeData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void getChargeData() {
        if(chargeList.size() > 0){
            chargeList.clear();
            chargeStringlist.clear();
        }

        String charge = "Select * from "+db.TABLE_CHARGE;
        Cursor c = sql.rawQuery(charge,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdowndata = new ConfigDropDownData();
                dropdowndata.setChargeDesc(c.getString(c.getColumnIndex("ChargeDesc")));
                dropdowndata.setChargeCode(c.getString(c.getColumnIndex("ChargeCode")));
                dropdowndata.setChargeMasterId(c.getString(c.getColumnIndex("ChargeMasterId")));

                chargeStringlist.add(c.getString(c.getColumnIndex("ChargeDesc")));
                chargeList.add(dropdowndata);
            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,chargeStringlist);
            edt_charge.setAdapter(adapter);

        }else {

        }
    }

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        //String ChrgMasterId = uuid.toString();

        FinalChargeAmt = edt_charge_amt.getText().toString();
        FinalChargetaxAmt = edt_taxamt.getText().toString();
        ChrgDesc = edt_charge.getText().toString();

        try {
            jArray = new JSONArray();
            jobj_charge = new JSONObject();

            jobj_charge.put("SrNo","1");
            jobj_charge.put("ChrgMasterId",ChrgMasterId);  //guid
            jobj_charge.put("ChrgDesc",edt_charge.getText().toString());
            jobj_charge.put("CalcMethod",edt_calc_method.getText().toString());
            jobj_charge.put("QtyValue",edt_tot_qty_val.getText().toString());
            jobj_charge.put("RatePercent",edt_percntg.getText().toString());
            jobj_charge.put("ChargeAmount",edt_charge_amt.getText().toString());
            jobj_charge.put("TaxClassMasterId",selTaxId);
            jobj_charge.put("TaxClassDesc",selTaxDesc);
            jobj_charge.put("TaxAmount",edt_taxamt.getText().toString());
            jobj_charge.put("TotalAmount",edt_totamt.getText().toString());
            jobj_charge.put("ChargeDetailsId","0");
            jobj_charge.put("Action","");

            jArray.put(jobj_charge);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }

    /*"ChargesDetails": [
    {
      "SrNo": "1",
      "ChrgMasterId": "Ren/1",
      "ChrgDesc": "Travelling",
      "CalcMethod": "Rate per quantity",
      "QtyValue": "20.00",
      "RatePercent": "5",
      "ChargeAmount": "100.00",
      "TaxClassMasterId": "1102",
      "TaxClassDesc": "GOA IGST 18% INPUT(TXC0101)",
      "TaxAmount": "18.00",
      "TotalAmount": "118.0000",
      "ChargeDetailsId": "0",
      "Action": "<div style=\"margin: 0 8px 0 0;position:relative;\"> <button data-toggle=\"dropdown\" class=\"btn btn-default\">Actions<b class=\"caret\"></b></button><ul role=\"menu\" class=\"dropdown-menu animated fadeInDown\" style=\"left:auto;\"><li onclick=\"funChargePopupEdit(1)\"><a href=\"#\">Edit</a></li><li onclick=\"funChargePopupDelete(1)\"><a href=\"#\">Delete</a></li></ul></div>"
    }
  ]
*/

    public static void setTaxAmtData(float taxamt, float finalTotal_incltax, float pctgval){
        edt_taxamt.setText(String.format("%.02f",taxamt));
        edt_totamt.setText(String.format("%.02f",finalTotal_incltax));
    }

    public boolean validate() {
        boolean val = false;

        if(edt_charge.getText().toString().equalsIgnoreCase("") &&
                edt_calc_method.getText().toString().equalsIgnoreCase("") &&
                edt_taxclass.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(SalesChargeActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_charge.getText().toString().equalsIgnoreCase("") ||
                edt_charge.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesChargeActivity.this, "Select charge type", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_calc_method.getText().toString().equalsIgnoreCase("") ||
                edt_calc_method.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesChargeActivity.this, "Select calculation method", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edt_taxclass.getText().toString().equalsIgnoreCase("") ||
                edt_taxclass.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesChargeActivity.this, "Select tax class", Toast.LENGTH_SHORT).show();
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
            edt_taxclass.setText(selTaxDesc);

            TaxClassMasterId = selTaxId;

            float Amt = Float.parseFloat(edt_charge_amt.getText().toString());
            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(edt_taxclass.getText().toString(),Amt,"SalesCharge");

        }
    }

}
