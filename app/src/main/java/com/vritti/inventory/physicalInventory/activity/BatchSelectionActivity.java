package com.vritti.inventory.physicalInventory.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.inventory.physicalInventory.adapter.BatchListAdapter;
import com.vritti.inventory.physicalInventory.bean.BatchList;
import com.vritti.inventory.physicalInventory.bean.BeanTag;
import com.vritti.inventory.physicalInventory.bean.BeanTagList;
import com.vritti.sales.beans.BillNoClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BatchSelectionActivity extends AppCompatActivity {
    private Context parent;
    ListView listbatch;
    ProgressDialog progressDialog_1;
    LinearLayout lay1, lay2,laywarning, laycount;
    Button btnrelease, btncontinue, btn_sel_batch, btnyes, btnno;
    TextView txtpending, txtcounted, txtbatchbo, textselectlay, txtwarning, txttitle;
    RadioGroup radgrp;
    ImageView imgrefresh;
    AppCompatRadioButton radio_online,radio_ofline,radio_immediate,radio_batch;
    String Onstatus="",Costatus="",Bostatus="";

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<BatchList>batchListArrayList;
    BatchListAdapter batchListAdapter;

    private SharedPreferences sharedPrefs;
    Gson gson;
    private String json;
    Type type;

    int  TagCurrentNo,TagEndNo,TagStartNo;
    String batchHDR = "", batchCODE = "", BatchHdID = "";

    BillNoClass billNoClass;
    BeanTagList beanTagObj;
    BeanTag glBeanObject;

    String selectedBatchFlag = "";
    String selectedBatchCode = "", selectedBatchHDRID = "", countedPI = "", uploadPendingPI = "";
    boolean selectedtchRelease = false;
    boolean isBatchSelected;
    int lastTagNo, startTagno, countedTags, curTagNo;
    String keyForBatch = "", WareHouseMasterId = "", warehousecode = "", CounterPersonID = "", AuditorPersonID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        getPendingRecordsCount();

        getcountedTagNumber();

        if (batchListArrayList == null) {
            callBatchListAPI();
        }else {
            //callBatchListAPI();
            if (batchListArrayList.size() > 0) {
                if(isBatchSelected){
                    batchListAdapter = new BatchListAdapter(BatchSelectionActivity.this, batchListArrayList);
                    listbatch.setAdapter(batchListAdapter);
                }else{
                    callBatchListAPI();
                }
            }
        }

        /*getPendingRecordsCount();
        getcountedTagNumber();*/

        setListeners();
    }

    public void init(){
        parent = BatchSelectionActivity.this;

        imgrefresh = findViewById(R.id.imgrefresh);
        listbatch = findViewById(R.id.listbatch);
        progressDialog_1=new ProgressDialog(parent);
        lay1 = findViewById(R.id.lay1);
        lay2 = findViewById(R.id.lay2);
        txtcounted = findViewById(R.id.txtcounted);
        txtcounted.setVisibility(View.VISIBLE);
        txtpending = findViewById(R.id.txtpending);
        btncontinue = findViewById(R.id.btncontinue);
        btn_sel_batch = findViewById(R.id.btn_sel_batch);
        btnrelease = findViewById(R.id.btnrelease);
        radio_online=findViewById(R.id.radio_online);
        textselectlay = findViewById(R.id.textselectlay);
        txtbatchbo = findViewById(R.id.txtbatchbo);
        radio_ofline=findViewById(R.id.radio_ofline);
        radio_immediate=findViewById(R.id.radio_immediate);
        radio_batch=findViewById(R.id.radio_batch);
        laywarning= findViewById(R.id.laywarning);
        laywarning.setVisibility(View.GONE);
        txttitle = findViewById(R.id.txttitle);
        txtwarning = findViewById(R.id.txtwarning);
        btnyes = findViewById(R.id.btnyes);
        btnno = findViewById(R.id.btnno);
        laycount = findViewById(R.id.laycount);

        ut = new Utility();
        cf = new CommonFunction(BatchSelectionActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(BatchSelectionActivity.this);
        String dabasename = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(BatchSelectionActivity.this, dabasename);
        CompanyURL = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(BatchSelectionActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        batchListArrayList = new ArrayList<BatchList>();
        billNoClass = new BillNoClass();

        Intent intent=getIntent();
        Costatus = intent.getStringExtra("Costatus");

        if(Costatus.equalsIgnoreCase("Counter")){
            keyForBatch = "C";
        }else if(Costatus.equalsIgnoreCase("Auditor")){
            keyForBatch = "A";
        }

        if(radio_online.isChecked()){
            Onstatus=radio_online.getText().toString();
        }else {
            Onstatus=radio_ofline.getText().toString();
        }

        if(radio_immediate.isChecked()){
            Bostatus=radio_immediate.getText().toString();
        }else {
            Bostatus=radio_batch.getText().toString();
        }

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("batch", "");
        type = new TypeToken<List<BatchList>>() {}.getType();
        batchListArrayList = gson.fromJson(json, type);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
        isBatchSelected = sharedPrefs.getBoolean("isBatchSelected", false);
        selectedBatchCode = sharedPrefs.getString("selectedBatchCode", "");
        batchCODE = sharedPrefs.getString("selectedBatchCode", "");
        selectedBatchHDRID = sharedPrefs.getString("selectedBatchHDRID", "");
        batchHDR = sharedPrefs.getString("selectedBatchHDRID", "");
        countedPI = sharedPrefs.getString("countedPI", "");
        uploadPendingPI = sharedPrefs.getString("uploadPendingPI", "");
        WareHouseMasterId = sharedPrefs.getString("WareHouseMasterId", "");
        warehousecode = sharedPrefs.getString("warehousecode", "");
        CounterPersonID = sharedPrefs.getString("CounterPersonID","");
        AuditorPersonID = sharedPrefs.getString("AuditorPersonID","");
        TagStartNo = sharedPrefs.getInt("startNo",0);
        TagEndNo = sharedPrefs.getInt("endNo",0);

        /*if(uploadPendingPI.equalsIgnoreCase("")){
            uploadPendingPI = "0";
        }*/

        if (Costatus.equals("Counter")){
            laycount.setVisibility(View.VISIBLE);

        }else if (Costatus.equals("Auditor")) {
            laycount.setVisibility(View.GONE);
        }

        txttitle.setText("You are working as a "+Costatus+" at "+warehousecode+" on");

        //if batch already selected then hide lay1 in not then hide lay2
        if(isBatchSelected){
            //batch selected, hide lay1, display lay2
            if (Costatus.equals("Counter")){
                laycount.setVisibility(View.VISIBLE);
                lay1.setVisibility(View.GONE);
                imgrefresh.setVisibility(View.GONE);
                lay2.setVisibility(View.VISIBLE);
                laywarning.setVisibility(View.GONE);

                if(UserMasterId.equalsIgnoreCase(CounterPersonID)){
                    txtbatchbo.setText(selectedBatchCode);
                }else {
                    txtbatchbo.setHint("Select responsible batch code");
                }

                getcountedTagNumber();

            }else if (Costatus.equals("Auditor")) {
                laycount.setVisibility(View.GONE);
                lay1.setVisibility(View.GONE);
                imgrefresh.setVisibility(View.GONE);
                lay2.setVisibility(View.VISIBLE);
                laywarning.setVisibility(View.GONE);

                if(UserMasterId.equalsIgnoreCase(AuditorPersonID)){
                    txtbatchbo.setText(selectedBatchCode);
                }else {
                    txtbatchbo.setHint("Select responsible batch code");
                }
            }

           // txtcounted.setText(countedPI);
            //txtpending.setText(String.valueOf(getPendingRecordsCount()));
            //getcountedTagNumber();
        }else {
            //batch not selected, hide lay2, display lay1
            lay1.setVisibility(View.VISIBLE);
            imgrefresh.setVisibility(View.VISIBLE);
            lay2.setVisibility(View.GONE);
            laywarning.setVisibility(View.GONE);
        }

        if(uploadPendingPI.equalsIgnoreCase("0")){
            btnrelease.setEnabled(true);
        }else {
           /* btnrelease.setEnabled(false);
            btnrelease.setAlpha(Float.parseFloat("0.5"));*/
        }

        try{
            if (AppCommon.getInstance(this).getBillNo_print().equals("")) {

            } else {
                billNoClass = new Gson().fromJson(AppCommon.getInstance(this).getBillNo_print(), BillNoClass.class);
                beanTagObj = new Gson().fromJson(AppCommon.getInstance(this).getBillNo_print(), BeanTagList.class);
                if(beanTagObj!= null ){
                    if(beanTagObj.getBeanTagArrayList() != null){
                        for(BeanTag beanTag : beanTagObj.getBeanTagArrayList() ){
                            if(batchCODE.equalsIgnoreCase(beanTag.getBatchNo())){
                                glBeanObject = beanTag;
                            //    Toast.makeText(this,"Selected tag is "+glBeanObject.getTagNo(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setListeners(){

        /*textselectlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay1.setVisibility(View.VISIBLE);
                imgrefresh.setVisibility(View.VISIBLE);
            }
        });*/

        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBatchListAPI();
            }
        });

        btn_sel_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay1.setVisibility(View.VISIBLE);
                imgrefresh.setVisibility(View.VISIBLE);
                lay2.setVisibility(View.GONE);
                laywarning.setVisibility(View.GONE);
                callBatchListAPI();
            }
        });

        listbatch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TagCurrentNo = batchListArrayList.get(position).getTagCurrentNo();
                TagEndNo= batchListArrayList.get(position).getTagEndNo();
                TagStartNo= batchListArrayList.get(position).getTagStartNo();
                BatchHdID = batchListArrayList.get(position).getPIHdrId();
                batchHDR = batchListArrayList.get(position).getPIHdrId();
                batchCODE = batchListArrayList.get(position).getCode();
                warehousecode = batchListArrayList.get(position).getWarehouseCode();
                WareHouseMasterId = batchListArrayList.get(position).getWareHouseMasterId();

                selectedBatchCode = batchListArrayList.get(position).getCode();
                selectedBatchHDRID = batchListArrayList.get(position).getPIHdrId();
                CounterPersonID = batchListArrayList.get(position).getCountingResponsibleId();
                AuditorPersonID = batchListArrayList.get(position).getAuditorId();

                batchCODE = selectedBatchCode;
                batchHDR = selectedBatchHDRID;

                //Toast.makeText(parent,"Tag - "+String.valueOf(TagCurrentNo),Toast.LENGTH_SHORT).show();

                lay1.setVisibility(View.GONE);
                imgrefresh.setVisibility(View.GONE);
                lay2.setVisibility(View.VISIBLE);

                txtbatchbo.setText(selectedBatchCode);
             //   txtcounted.setText(countedPI);
             //   txtpending.setText(uploadPendingPI);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("WareHouseMasterId",batchListArrayList.get(position).getWareHouseMasterId());
                editor.putBoolean("isBatchSelected",true);
                editor.putString("selectedBatchCode", selectedBatchCode);
                editor.putString("selectedBatchHDRID", selectedBatchHDRID);
                editor.putString("WareHouseMasterId", WareHouseMasterId);
                editor.putString("warehousecode", warehousecode);
                editor.putString("CounterPersonID",CounterPersonID);
                editor.putString("AuditorPersonID",AuditorPersonID);
                editor.putInt("startNo", TagStartNo);
                editor.putInt("endNo", TagEndNo);
                editor.commit();

               /* int COUNT = TagStartNo - getCountedBillstag();
               Log.e("COUNTED - ",String.valueOf(COUNT));
               txtcounted.setText(String.valueOf(COUNT));*/

                getcountedTagNumber();
            }
        });

        radio_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_online.setChecked(true);
                    Onstatus=radio_online.getText().toString();

                }else {
                }
            }
        });

        radio_ofline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_ofline.setChecked(true);
                    Onstatus=radio_ofline.getText().toString();

                }else {
                }
            }
        });

        radio_immediate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_immediate.setChecked(true);
                    Bostatus=radio_immediate.getText().toString();

                }else {
                }
            }
        });
        radio_batch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_batch.setChecked(true);
                    Bostatus=radio_batch.getText().toString();
                }else {
                }
            }
        });

        btnrelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear batchno, call batchlist API and hide lay 2 disp batch list to select new batch

                //show alert dialogue
                lay1.setVisibility(View.GONE);
                imgrefresh.setVisibility(View.GONE);
                lay2.setVisibility(View.GONE);
                laywarning.setVisibility(View.VISIBLE);

                String msg = "Are you sure you want to release batch no. "+batchCODE+", Once it it released you will not be able to access this batch for further operation";
                txtwarning.setText(msg);
                //showalert();

               /* txtbatchbo.setText("");

                lay2.setVisibility(View.GONE);
                lay1.setVisibility(View.VISIBLE);
                imgrefresh.setVisibility(View.VISIBLE);

                //close this screen and open Counter/Auditor screen
                callReleaseAPI();

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("isBatchSelected",false);
                editor.putString("selectedBatchCode", "");
                editor.putString("selectedBatchHDRID", "");
                editor.commit();*/
            }
        });

        btnyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //close this screen and open Counter/Auditor screen
                callReleaseAPI();

                txtbatchbo.setText("");
                laywarning.setVisibility(View.GONE);
                lay2.setVisibility(View.GONE);
                lay1.setVisibility(View.VISIBLE);
                imgrefresh.setVisibility(View.VISIBLE);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("isBatchSelected",false);
                editor.putString("selectedBatchCode", "");
                editor.putString("selectedBatchHDRID", "");
                editor.commit();
            }
        });

        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laywarning.setVisibility(View.GONE);
                lay1.setVisibility(View.GONE);
                imgrefresh.setVisibility(View.GONE);
                lay2.setVisibility(View.VISIBLE);
            }
        });

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                   // Onstatus=radio_online.getText().toString();
                } else {
                    Onstatus=radio_ofline.getText().toString();
                    Toast.makeText(getApplicationContext(), "No Internet Connection, connecting you in offline mode", Toast.LENGTH_LONG).show();
                }

                 if (Costatus.equals("Counter")){

                //    Toast.makeText(parent,"You have opted for "+Onstatus+", "+Bostatus,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BatchSelectionActivity.this, PIEntryPrintingActivity.class)
                            .putExtra("status",Onstatus)
                            .putExtra("bathchprint",Bostatus)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }else if (Costatus.equals("Auditor")) {
                    startActivity(new Intent(BatchSelectionActivity.this, AudiScreenActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    private void callBatchListAPI() {
        if (isnet()) {
            new StartSession(BatchSelectionActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    if(Costatus.equalsIgnoreCase("Counter")){
                        keyForBatch = "C";
                    }else if(Costatus.equalsIgnoreCase("Auditor")){
                        keyForBatch = "A";
                    }
                    new DownloadBatchlistData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, keyForBatch);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadBatchlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog_1 == null) {
                    progressDialog_1 = new ProgressDialog(parent);
                    progressDialog_1.setMessage("Loading Please wait...");
                    progressDialog_1.setIndeterminate(false);
                    progressDialog_1.setCancelable(false);

                }
                progressDialog_1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            keyForBatch = params[0];
            //String url = CompanyURL + WebUrlClass.api_GetBatchList;
            String url = CompanyURL + WebUrlClass.api_GetBatchList+"?UserMasterId="+UserMasterId+"&TypeKey="+keyForBatch;
            try {
                res = ut.OpenConnection(url, parent);
                if (res != null) {
                    response = res.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog_1.dismiss();
            if (response.contains("[]")) {
                Toast.makeText(parent, "Sorry, No batch available.", Toast.LENGTH_SHORT).show();
                try{
                    batchListArrayList.clear();
                    batchListAdapter = new BatchListAdapter(parent, batchListArrayList);
                    listbatch.setAdapter(batchListAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                parseJson(response);
            }
        }
    }

    private void parseJson(String response) {

        if(batchListArrayList != null){
            batchListArrayList.clear();
        }else {

        }

        batchListArrayList = new ArrayList<BatchList>();

        JSONArray jResults = null;
        try {
            jResults = new JSONArray(response);

            BeanTagList beanList = new BeanTagList();
            ArrayList<BeanTag> beanTagArrayList = new ArrayList<>();

            for (int i = 0; i < jResults.length(); i++) {
                BatchList batchList = new BatchList();
                JSONObject jorder = jResults.getJSONObject(i);

                batchList.setCountingResponsibleId(jorder.getString("CountingResponsibleId"));
                batchList.setAuditorId(jorder.getString("AuditorId"));

                batchList.setPIHdrId(jorder.getString("PIHdrId"));
                batchList.setCode(jorder.getString("Code"));
                //batchList.setUsername(jorder.getString("Username"));
                batchList.setReleaseStatus(jorder.getString("ReleaseStatus"));
                batchList.setTagStartNo(jorder.getInt("TagStartNo"));
                batchList.setTagEndNo(jorder.getInt("TagEndNo"));
                batchList.setTagCurrentNo(jorder.getInt("TagCurrentNo"));
                batchList.setWareHouseMasterId(jorder.getString("WareHouseMasterId"));
                batchList.setCountingResponsibleId(jorder.getString("CountingResponsibleId"));
                batchList.setPlantId(jorder.getString("PlantId"));
                batchList.setCountingResponsible(jorder.getString("CountingResponsible"));
                batchList.setAuditorName(jorder.getString("AuditorName"));
                batchList.setWarehouseCode(jorder.getString("WarehouseCode"));
                batchListArrayList.add(batchList);

                batchHDR = jorder.getString("PIHdrId");
                batchCODE = jorder.getString("Code");

                /*if(UserMasterId.equalsIgnoreCase(jorder.getString("CountingResponsibleId"))){
                    batchListArrayList.add(batchList);

                    batchHDR = jorder.getString("PIHdrId");
                    batchCODE = jorder.getString("Code");

                }else {
                    //do not add batch in list coz he is not authorised for that batch
                }*/

               /* if(Costatus.equalsIgnoreCase("Counter")){
                    if(UserMasterId.equalsIgnoreCase(jorder.getString("CountingResponsibleId"))){
                        batchList.setPIHdrId(jorder.getString("PIHdrId"));
                        batchList.setCode(jorder.getString("Code"));
                        //batchList.setUsername(jorder.getString("Username"));
                        batchList.setReleaseStatus(jorder.getString("ReleaseStatus"));
                        batchList.setTagStartNo(jorder.getInt("TagStartNo"));
                        batchList.setTagEndNo(jorder.getInt("TagEndNo"));
                        batchList.setTagCurrentNo(jorder.getInt("TagCurrentNo"));
                        batchList.setWareHouseMasterId(jorder.getString("WareHouseMasterId"));
                        batchList.setCountingResponsibleId(jorder.getString("CountingResponsibleId"));
                        batchList.setPlantId(jorder.getString("PlantId"));

                        batchList.setCountingResponsible(jorder.getString("CountingResponsible"));

                        batchList.setAuditorName(jorder.getString("AuditorName"));
                        batchListArrayList.add(batchList);

                        batchHDR = jorder.getString("PIHdrId");
                        batchCODE = jorder.getString("Code");

                    }else {
                        //do not add batch in list coz he is not authorised for that batch
                    }
                }else {
                    if(UserMasterId.equalsIgnoreCase(jorder.getString("AuditorId"))){
                        batchList.setPIHdrId(jorder.getString("PIHdrId"));
                        batchList.setCode(jorder.getString("Code"));
                        //batchList.setUsername(jorder.getString("Username"));
                        batchList.setReleaseStatus(jorder.getString("ReleaseStatus"));
                        batchList.setTagStartNo(jorder.getInt("TagStartNo"));
                        batchList.setTagEndNo(jorder.getInt("TagEndNo"));
                        batchList.setTagCurrentNo(jorder.getInt("TagCurrentNo"));
                        batchList.setWareHouseMasterId(jorder.getString("WareHouseMasterId"));
                        batchList.setCountingResponsibleId(jorder.getString("CountingResponsibleId"));
                        batchList.setPlantId(jorder.getString("PlantId"));

                        batchList.setCountingResponsible(jorder.getString("CountingResponsible"));

                        batchList.setAuditorName(jorder.getString("AuditorName"));
                        batchListArrayList.add(batchList);

                        batchHDR = jorder.getString("PIHdrId");
                        batchCODE = jorder.getString("Code");

                    }else {
                        //do not add batch in list coz he is not authorised for that batch
                    }
                }*/

                try {
                    // billNoClass.setBill_no(jorder.getInt("TagCurrentNo"));
                    // Log.e("Tag refresh- ", String.valueOf(billNoClass.getBill_no()));
                    // String billingObj = new Gson().toJson(billNoClass);

                    BeanTag beanTag = new BeanTag(batchCODE, batchHDR, jorder.getInt("TagCurrentNo")/*,jorder.getInt("TagStartNo")*/);
                    beanTagArrayList.add(beanTag);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            beanList.setBeanTagArrayList(beanTagArrayList);
            String billingObj = new Gson().toJson(beanList);
            AppCommon.getInstance(parent).setBillNo_print(billingObj);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(parent);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            Gson gson = new Gson();

            String json = gson.toJson(batchListArrayList);
            editor.putString("batch", json);
            editor.commit();

            batchListAdapter = new BatchListAdapter(parent, batchListArrayList);
            listbatch.setAdapter(batchListAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJson_getCounted(String response) {

        JSONArray jResults = null;
        try {
            jResults = new JSONArray(response);

            for (int i = 0; i < jResults.length(); i++) {
                BatchList batchList = new BatchList();
                JSONObject jorder = jResults.getJSONObject(i);

                if(batchCODE.equalsIgnoreCase(jorder.getString("Code"))){
                    startTagno = jorder.getInt("TagStartNo");
                    lastTagNo = jorder.getInt("TagEndNo");
                    curTagNo = jorder.getInt("TagCurrentNo");

                    countedTags = curTagNo - startTagno;
                    if(countedTags < 0 || countedTags == 0){
                        txtcounted.setText("0");
                    }else {
                        txtcounted.setText(String.valueOf(countedTags));
                    }

                    if(lastTagNo==curTagNo){
                        btncontinue.setEnabled(false);
                        btncontinue.setAlpha((float)0.3);
                        btncontinue.setClickable(false);
                        btncontinue.setText("Your counting for this batch is completed.");
                    }else if(lastTagNo > curTagNo){
                        btncontinue.setEnabled(true);
                        btncontinue.setClickable(true);
                        btncontinue.setAlpha((float)1);
                        btncontinue.setText("Continue");
                    }else if(lastTagNo < curTagNo){
                        btncontinue.setEnabled(false);
                        btncontinue.setAlpha((float)0.3);
                        btncontinue.setClickable(false);
                        btncontinue.setText("Your counting for this batch is completed.");
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callReleaseAPI() {

        if (isnet()) {
            new StartSession(BatchSelectionActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                //   new DownloadReleaseData().execute();
                    new DownloadReleaseData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadReleaseData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog_1 == null) {
                    progressDialog_1 = new ProgressDialog(BatchSelectionActivity.this);
                    progressDialog_1.setMessage("Releasing batch please wait...");
                    progressDialog_1.setIndeterminate(false);
                    progressDialog_1.setCancelable(false);
                }
                progressDialog_1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_UpdateRelaseStatus+"?PIHdrId="+BatchHdID+"&ReleaseStatus=20";

            try {
                res = ut.OpenConnection(url, BatchSelectionActivity.this);
                if (res != null) {
                    response = res.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog_1.dismiss();

            if (response.contains("[]")) {
                Toast.makeText(BatchSelectionActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(BatchSelectionActivity.this,"Batch released successfully",Toast.LENGTH_SHORT);

                //batchListArrayList.clear();

                String remark = "Batch released successfully";
                String url = CompanyURL + WebUrlClass.api_PostPIdetail;

                String op = "true";
                CreateOfflineRelease(url, null, WebUrlClass.GETFlAG, remark, op);

               /* beanTagObj = new Gson().fromJson(AppCommon.getInstance(parent).getBillNo_print(), BeanTagList.class);
                if(beanTagObj!= null ){
                    if(beanTagObj.getBeanTagArrayList() != null){
                        for(int i = 0 ; i < beanTagObj.getBeanTagArrayList().size() ; i++ ){
                            if(batchCODE.equalsIgnoreCase(beanTagObj.getBeanTagArrayList().get(i).getBatchNo())){
                                glBeanObject = beanTagObj.getBeanTagArrayList().get(i);
                                glBeanObject.setReleased(true);
                                beanTagObj.getBeanTagArrayList().get(i).setReleased(false);
                                //beanList.setBeanTagArrayList(beanTagArrayList);
                                String billingObj = new Gson().toJson(beanTagObj);
                                AppCommon.getInstance(parent).setBillNo_print(billingObj);
                               // AppCommon.getInstance(this).setBillNo_print(beanTagObj);

                            }
                        }
                    }
                }*/

                /*selectedtchRelease = true;

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("selectedBatchPosition", selectedBatchPosition);
                editor.putString("selectedBatchFlag", selectedBatchFlag);
                editor.putBoolean("selectedtchRelease", selectedtchRelease);
                editor.commit();*/

                callBatchListAPI();

                finish();
            }
        }
    }

    private void CreateOfflineRelease(final String url, final String parameter,
                                      final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);

       if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);

        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    class Downloadcurtag extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
            /*    if (progressDialog_1 == null) {
                    progressDialog_1 = new ProgressDialog(parent);
                    progressDialog_1.setMessage("Loading Please wait...");
                    progressDialog_1.setIndeterminate(false);
                    progressDialog_1.setCancelable(false);

                }
                progressDialog_1.show();*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {

            keyForBatch = params[0];
            //String url = CompanyURL + WebUrlClass.api_GetBatchList;
            String url = CompanyURL + WebUrlClass.api_GetBatchList+"?UserMasterId="+UserMasterId+"&TypeKey="+keyForBatch;

            try {
                res = ut.OpenConnection(url, parent);
                if (res != null) {
                    response = res.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

         //   progressDialog_1.dismiss();

            if (response.contains("[]")) {
            //    Toast.makeText(parent, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                parseJson_getCounted(response);

            }
        }
    }

    public int getPendingRecordsCount(){
        String url = CompanyURL + WebUrlClass.api_PostPIdetail;
        String qry = "SELECT * FROM  "+db.TABLE_DATA_OFFLINE+" WHERE linkurl='"+url+"'"+" AND isUploaded='"+ WebUrlClass.FlagisUploadedFalse+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                uploadPendingPI = String.valueOf(c.getCount());

                if(c.getCount() < 0 || c.getCount() == 0){
                    uploadPendingPI= "0";
                }else {

                }
                txtpending.setText(uploadPendingPI);
            }while (c.moveToNext());
        }

        return c.getCount();
    }

    private int getCountedBillstag() {
        int count = 0, currentTag = 0;
        String qry = "SELECT TagNo, * FROM "+db.TABLE_PI_GENERATION+" WHERE PIHdrId='"+BatchHdID+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToLast();
            currentTag = Integer.parseInt(c.getString(c.getColumnIndex("TagNo")));
            count = c.getCount();
        }else {

        }
        return count;
    }

    public void getcountedTagNumber(){
        if (isnet()) {
            new StartSession(BatchSelectionActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    if(Costatus.equalsIgnoreCase("Counter")){
                        keyForBatch = "C";
                    }else if(Costatus.equalsIgnoreCase("Auditor")){
                        keyForBatch = "A";
                    }

                   // new Downloadcurtag().execute(keyForBatch);
                    new Downloadcurtag().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,keyForBatch);

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    public void showalert() {
        // TODO Auto-generated method stub

        final Dialog myDialog = new Dialog(BatchSelectionActivity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_message);
        myDialog.setCancelable(true);

        // myDialog.getWindow().setGravity(Gravity.BOTTOM);
        // myDialog.setTitle("Complete Activity");

        String msg = "Are you sure you want to release batch no. "+batchCODE+", Once it it released you will not be able to access this batch further";

        final TextView quest = myDialog.findViewById(R.id.textMsg);
        quest.setText(Html.fromHtml(msg));

        Button btnyes = myDialog.findViewById(R.id.btn_yes);
        Button btnno = myDialog.findViewById(R.id.btn_no);
        btnno.setVisibility(View.VISIBLE);
        btnyes.setBackgroundColor(Color.parseColor("#016a97"));
        btnno.setBackgroundColor(Color.parseColor("#FD8E2A"));

        btnyes.setText("YES");
        btnyes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //procedd further

                txtbatchbo.setText("");
                laywarning.setVisibility(View.GONE);
                lay2.setVisibility(View.GONE);
                lay1.setVisibility(View.VISIBLE);
                imgrefresh.setVisibility(View.VISIBLE);

                //close this screen and open Counter/Auditor screen
                callReleaseAPI();

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(BatchSelectionActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("isBatchSelected",false);
                editor.putString("selectedBatchCode", "");
                editor.putString("selectedBatchHDRID", "");
                editor.commit();

                myDialog.dismiss();
            }
        });

        btnno.setText("NO");
        btnno.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //cancel submition and cleart edittext
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Costatus.equals("Counter")){
            laycount.setVisibility(View.VISIBLE);
            txtpending.setText(String.valueOf(getPendingRecordsCount()));
            getcountedTagNumber();
        }else if (Costatus.equals("Auditor")) {
            laycount.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
