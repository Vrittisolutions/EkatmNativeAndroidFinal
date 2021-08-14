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
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.PriceListBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SalesFamilyActivity_one extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar progressBar;
    Button btnshow;
    AutoCompleteTextView edtmod_no,edtmod_size,edtfamily;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<String> modelNoList;
    ArrayList<ConfigDropDownData> mnumList;
    ArrayList<String> modelSizeList;
    ArrayList<ConfigDropDownData> msizeList;
    ArrayList<String> saleFamilyList;
    ArrayList<PriceListBean> sfamList;

    String modeNo = "", modeNoId = "", modeSize = "", modeSizeId = "",familyId = "",SoDate = "";
    String HeaderDetails = "", ItemDetails = "",ScheduleDetails = "", UOMMasterId = "";
    public static final int SALES_FAMILY = 13;
    public static final int SFamilyDtl = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_family_one);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //call API to get salesfamily
        if(tcf.getSFamilyCount() > 0){
            getSalesFamilyData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DwnldSaleFamilyJSON().execute();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        //call API to get modelno
        if(tcf.getModelCount() > 0){
            getModelNoData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DwnldModelNoJSON().execute();
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
        parent = SalesFamilyActivity_one.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Sales Family");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        btnshow = findViewById(R.id.btnshow);
        edtfamily = findViewById(R.id.edtfamily);
        edtmod_no = findViewById(R.id.edtmod_no);
        edtmod_size = findViewById(R.id.edtmod_size);

        Intent intent = getIntent();
        SoDate = intent.getStringExtra("SoDate");

        ut = new Utility();
        cf = new CommonFunction(SalesFamilyActivity_one.this);
        tcf = new Tbuds_commonFunctions(SalesFamilyActivity_one.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesFamilyActivity_one.this);
        String dabasename = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesFamilyActivity_one.this, dabasename);
        CompanyURL = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesFamilyActivity_one.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        modelNoList = new ArrayList<String>();
        mnumList = new ArrayList<ConfigDropDownData>();
        modelSizeList = new ArrayList<String>();
        msizeList = new ArrayList<ConfigDropDownData>();
        saleFamilyList = new ArrayList<String>();
        sfamList = new ArrayList<PriceListBean>();
    }

    public void setListeners(){
        edtfamily.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtfamily.showDropDown();
                return false;
            }
        });

        edtfamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                familyId = sfamList.get(position).getFamilyId();
            }
        });

        edtmod_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtmod_no.showDropDown();
                return false;
            }
        });

        edtmod_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modeNo = mnumList.get(position).getConfiguration();
                modeNoId = mnumList.get(position).getConfigurationDetailId();
            }
        });

        edtmod_size.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtmod_size.showDropDown();
                return false;
            }
        });

        edtmod_size.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modeSize = msizeList.get(position).getConfiguration();
                modeSizeId = msizeList.get(position).getConfigurationDetailId();
            }
        });

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    Intent intent = new Intent(parent,SalesFamilyDtlActivity.class);
                    intent.putExtra("FamilyID",familyId);
                    intent.putExtra("ModelNo",modeNo);
                    intent.putExtra("ModelSize",modeSize);
                    intent.putExtra("SoDate",SoDate);
                    startActivityForResult(intent,SFamilyDtl);
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

    class DwnldModelNoJSON extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getModelNo;

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

                    sql.delete(db.TABLE_MODEL,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String ConfigurationDetailId = jsonObject.getString("ConfigurationDetailId");
                            String Configuration = jsonObject.getString("Configuration");
                            String Type = "MNumber";

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setConfigurationDetailId(ConfigurationDetailId);
                            dropdown.setConfiguration(Configuration);
                            dropdown.setType(Type);

                            tcf.insertModel(ConfigurationDetailId,Configuration,Type);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //call API of modelsize
                    new DwnldModelSizeJSON().execute();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class DwnldSaleFamilyJSON extends AsyncTask<String, Void, String> {
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
                String url = CompanyURL + WebUrlClass.api_getSalesFamily+"?salesparentID=";

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

                    sql.delete(db.TABLE_SALES_FAMILY,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String FamilyId = jsonObject.getString("FamilyId");
                            String FamilyCode = jsonObject.getString("FamilyCode");
                            String FamilyDesc = jsonObject.getString("FamilyDesc");

                            PriceListBean pBean = new PriceListBean();
                            pBean.setFamilyId(FamilyId);
                            pBean.setFamilyCode(FamilyCode);
                            pBean.setFamilyDesc(FamilyDesc);

                            tcf.insertSalesFamily(FamilyId,FamilyCode,FamilyDesc);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getSalesFamilyData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class DwnldModelSizeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            //rogressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getModelSize;

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

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String ConfigurationDetailId = jsonObject.getString("ConfigurationDetailId");
                            String Configuration = jsonObject.getString("Configuration");
                            String Type = "MSize";

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setConfigurationDetailId(ConfigurationDetailId);
                            dropdown.setConfiguration(Configuration);
                            dropdown.setType(Type);

                            tcf.insertModel(ConfigurationDetailId,Configuration,Type);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getModelNoData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getModelNoData(){
        if(mnumList.size() > 0){
            mnumList.clear();
            modelNoList.clear();
        }

        String qry = "Select * from "+db.TABLE_MODEL+" WHERE Type='MNumber'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                ConfigDropDownData cData = new ConfigDropDownData();
                cData.setConfigurationDetailId(c.getString(c.getColumnIndex("ConfigurationDetailId")));
                cData.setConfiguration(c.getString(c.getColumnIndex("Configuration")));
                cData.setType(c.getString(c.getColumnIndex("Type")));

                modelNoList.add(c.getString(c.getColumnIndex("Configuration")));
                mnumList.add(cData);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,modelNoList);
            edtmod_no.setAdapter(adapter);
        }

        String qry1 = "Select * from "+db.TABLE_MODEL+" WHERE Type='MSize'";
        Cursor c1 = sql.rawQuery(qry1,null);
        if(c1.getCount() > 0){
            c1.moveToFirst();
            do{
                ConfigDropDownData cData1 = new ConfigDropDownData();
                cData1.setConfigurationDetailId(c1.getString(c1.getColumnIndex("ConfigurationDetailId")));
                cData1.setConfiguration(c1.getString(c1.getColumnIndex("Configuration")));
                cData1.setType(c1.getString(c1.getColumnIndex("Type")));

                modelSizeList.add(c1.getString(c1.getColumnIndex("Configuration")));
                msizeList.add(cData1);

            }while (c1.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,modelSizeList);
            edtmod_size.setAdapter(adapter);
        }
    }

    public void getSalesFamilyData(){
        if(sfamList.size() > 0){
            sfamList.clear();
            saleFamilyList.clear();
        }

        String qry = "Select * from "+db.TABLE_SALES_FAMILY;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                PriceListBean pBean = new PriceListBean();
                pBean.setFamilyId(c.getString(c.getColumnIndex("FamilyId")));
                pBean.setFamilyCode(c.getString(c.getColumnIndex("FamilyCode")));
                pBean.setFamilyDesc(c.getString(c.getColumnIndex("FamilyDesc")));

                saleFamilyList.add(c.getString(c.getColumnIndex("FamilyDesc")));
                sfamList.add(pBean);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,saleFamilyList);
            edtfamily.setAdapter(adapter);
        }
    }

    public boolean validate() {
        boolean val = false;

        if(edtfamily.getText().toString().equalsIgnoreCase("") /*&&
                txt_pdesc.getText().toString().equalsIgnoreCase("") &&
                txt_taxclass.getText().toString().equalsIgnoreCase("")*/){
            Toast.makeText(SalesFamilyActivity_one.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(edtfamily.getText().toString().equalsIgnoreCase("") ||
                edtfamily.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesFamilyActivity_one.this, "Select sales family", Toast.LENGTH_SHORT).show();
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

        if (requestCode == SFamilyDtl && resultCode == SFamilyDtl) {
            String priceListArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            String headerArray = data.getStringExtra("jHeaderArray");
            UOMMasterId = data.getStringExtra("UOMMasterId");
            ItemDetails = priceListArray;
            ScheduleDetails = scheduleArray;
            // HeaderDetails = headerArray;

            Intent intent = new Intent(SalesFamilyActivity_one.this, SalesItemActivity.class);
            intent.putExtra("jItemArray",priceListArray);
            intent.putExtra("jScheduleArray",ScheduleDetails);
            intent.putExtra("jHeaderArray",HeaderDetails);
            intent.putExtra("UOMMasterId",UOMMasterId);
            setResult(SALES_FAMILY,intent);
            finish();
        }
    }

}
