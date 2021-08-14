package com.vritti.inventory.physicalInventory.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.RowCnt;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.vritti.chat.activity.SharefunctionActivity.getRealPathFromUri;

public class ItemMasterSyncActivity extends AppCompatActivity {
    private Context parent;
    Button btnsyncall, btn1_fromserver,btn1_fromfolder;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount;
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;
    EditText edt_qty, edt_weight, edt_countedby, edt_area;
    AutoCompleteTextView edt_location;
    TextView edt_itemcode, edt_description;
    ArrayList<String> ItemCodelist;
    ArrayList<String> ItemDesclist;
    int ROWS = 0, lastDownloadedindex;
    String fromIndex = "0", toIndex = "", flag;

    private ProgressBar progBar;
    private TextView textprogrs, txtdwnld;
    LinearLayout laystartfrom, layoptions,laystatus, laymodify;
    RelativeLayout layprogrsbar;
    TextView txtavlitems, txtstartfrom, txtdwnlitems;
    ImageView imgdone;
    private Handler mHandler = new Handler();
    private int mProgressStatus=0;
    int incvar = 100;
    String ROWCNT = "", lstItemCode = "";
    RowCnt rowCntClass;
    String KEYSYNCALL = "SyncAll", KEYRESUME = "StartsFrom";
    static StringRequest stringRequest;
    String KEY = "";
   // AsyncTask asyncTask = null;
   /* DownloadROWCountJSON asyncTask = null;
    DownloadGetItemlistJSON asyncTask2 = null;*/
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private int PICKFILE_REQUEST_CODE=1;
    String filePathUrl = "", readFileDB = "";
    ProgressBar progress;
    Handler mhandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_master_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();



        pDialog=new ProgressDialog(ItemMasterSyncActivity.this);
        progress=findViewById(R.id.progress);

        ROWS = cf.getGetItemcount();

        try{
            if (AppCommon.getInstance(this).getRowCnt().equals("")) {

            } else {
                rowCntClass = new Gson().fromJson(AppCommon.getInstance(this).getRowCnt(), RowCnt.class);
                ROWS = Integer.parseInt(rowCntClass.getRowCnt());
                lstItemCode = rowCntClass.getLastitemCode();
                Log.e("appcommonROW - ", ROWCNT);
            }

            txtavlitems.setText(String.valueOf(ROWS));
            txtdwnlitems.setText(String.valueOf(cf.getLstInsertedCount()));
            txtstartfrom.setText(lstItemCode);

            if(cf.getLstInsertedCount() == ROWS){
                imgdone.setVisibility(View.GONE);
                txtdwnld.setVisibility(View.GONE);
                textprogrs.setVisibility(View.GONE);
                mProgressStatus = cf.getLstInsertedCount();
                progBar.setProgress( cf.getLstInsertedCount());
                textprogrs.setText(""+ cf.getLstInsertedCount()+"");
            }else {
                imgdone.setVisibility(View.GONE);
                txtdwnld.setVisibility(View.VISIBLE);
                textprogrs.setVisibility(View.VISIBLE);
                mProgressStatus = cf.getLstInsertedCount();
                progBar.setProgress( cf.getLstInsertedCount());
                textprogrs.setText(""+ cf.getLstInsertedCount()+"");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        setListeners();
    }

    public void init(){
        parent = ItemMasterSyncActivity.this;

        btn1_fromserver = findViewById(R.id.btn1_fromserver);
        btn1_fromfolder = findViewById(R.id.btn1_fromfolder);
        btnsyncall = findViewById(R.id.btnsyncall);
        progBar= (ProgressBar)findViewById(R.id.progressBar);
        laystartfrom = findViewById(R.id.laystartfrom);
        laystartfrom.setVisibility(View.VISIBLE);
        textprogrs = findViewById(R.id.textprogrs);
        txtdwnld = findViewById(R.id.txtdwnld);
        txtavlitems = findViewById(R.id.txtavlitems);
        txtstartfrom = findViewById(R.id.txtstartfrom);
        txtdwnlitems = findViewById(R.id.txtdwnlitems);
        layoptions = findViewById(R.id.layoptions);
        laystatus = findViewById(R.id.laystatus);
        layprogrsbar = findViewById(R.id.layprogrsbar);
        laymodify = findViewById(R.id.laymodify);
        imgdone = findViewById(R.id.imgdone);
        imgdone.setVisibility(View.GONE);
        textprogrs.setVisibility(View.VISIBLE);
        txtdwnld.setVisibility(View.GONE);

        layoptions.setVisibility(View.VISIBLE);
        layprogrsbar.setVisibility(View.GONE);

        ut = new Utility();
        cf = new CommonFunction(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        rowCntClass = new RowCnt();

    }

    public void setListeners(){

        btn1_fromserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    new StartSession(ItemMasterSyncActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //String url = CompanyURL+"/Attachments/View%20Attachment/ItemMaster.json";
                            KEY = KEYSYNCALL;
                            new DownloadROWONLY().execute(KEY);

                            String url = CompanyURL+"/Attachments/"+EnvMasterId+"/ItemMaster.json";
                           // new DownloadFileFromURL().execute("http://hyvatest.ekatm.com/Attachments/View%20Attachment/ItemMaster.json");
                            new DownloadFileFromURL().execute(url);
                            //new DownloadFileFromURL_new().execute(url);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }
                    });
                }
            }
        });

        btn1_fromfolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_REQUEST_CODE);
            }
        });

        btnsyncall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoptions.setVisibility(View.GONE);
                layprogrsbar.setVisibility(View.VISIBLE);

                if (isnet()) {
                    new StartSession(ItemMasterSyncActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            @SuppressLint("InvalidWakeLockTag")
                            PowerManager.WakeLock screenLock = ((PowerManager)getSystemService(POWER_SERVICE)).newWakeLock(
                                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
                            screenLock.acquire();

                            KEY = KEYSYNCALL;
                           //new DownloadROWCountJSON().execute(KEY);
                            new DownloadROWCountJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
                            /*asyncTask = new DownloadROWCountJSON(); //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);*/
                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }
                    });
                }
            }
        });

        laystartfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ROWS
                layoptions.setVisibility(View.GONE);
                layprogrsbar.setVisibility(View.VISIBLE);

                KEY = KEYRESUME;

                toIndex = String.valueOf(cf.getLstInsertedCount());
                fromIndex = String.valueOf(cf.getLstInsertedCount());
                int sum = Integer.parseInt(toIndex) + incvar;

                if(sum > ROWS){
                    toIndex = String.valueOf(ROWS);
                }else {
                    toIndex = String.valueOf(sum);
                }

                if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //new DownloadROWCountJSON().execute(KEY);
                             new DownloadROWCountJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
                            /*asyncTask = new DownloadROWCountJSON();
                            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);*/

                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }
                    });
                }
            }
        });

        laymodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoptions.setVisibility(View.GONE);
                layprogrsbar.setVisibility(View.VISIBLE);

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

    public void dosomething() {

        new Thread(new Runnable() {
            public void run() {
                final int percentage = 0;
                while (mProgressStatus < ROWS) {
                    mProgressStatus += 1;
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            //progBar.setProgress(mProgressStatus);
                            //textprogrs.setText(""+mProgressStatus+"");
                            textprogrs.setText(""+toIndex+"");
                            txtdwnlitems.setText(toIndex);
                        }
                    });
                   /* try {

                        Thread.sleep(50);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }).start();
    }

    class DownloadROWONLY extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String rowsCnt = "0";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(parent);
                    progressDialog.setMessage("Checking for row count Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";
            flag = "Y";
            KEY = params[0];

            // String url = CompanyURL + WebUrlClass.api_ItemList;
            url = CompanyURL + WebUrlClass.api_ItemListAndroid+"?RQry=Y";  //RQry,from,to

            try {
                res = ut.OpenConnection(url,parent);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\r\\\\n","");
                    response = response.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);

                    JSONObject jsonObject = new JSONObject(response);
                    rowsCnt = jsonObject.getString("Rows");
                    ROWS = Integer.parseInt(rowsCnt);
                    Log.e("Rows ", String.valueOf(ROWS));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            // dismissProgressDialog();
            if(ROWS > 0){

                txtavlitems.setText(String.valueOf(ROWS));

                try{
                    rowCntClass.setRowCnt(String.valueOf(ROWS));
                    String rowcntObj = new Gson().toJson(rowCntClass);
                    AppCommon.getInstance(parent).setRowcnt(rowcntObj);
                    Log.e("appcommonROW - ", ROWCNT);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {

            }
        }
    }

    class DownloadROWCountJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String rowsCnt = "0";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(parent);
                    progressDialog.setMessage("Checking for row count Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";
            flag = "Y";
            KEY = params[0];

            // String url = CompanyURL + WebUrlClass.api_ItemList;
            url = CompanyURL + WebUrlClass.api_ItemListAndroid+"?RQry=Y";  //RQry,from,to

            try {
                res = ut.OpenConnection(url,parent);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\r\\\\n","");
                    response = response.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);

                    JSONObject jsonObject = new JSONObject(response);
                    rowsCnt = jsonObject.getString("Rows");
                    ROWS = Integer.parseInt(rowsCnt);
                    Log.e("Rows ", String.valueOf(ROWS));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            // dismissProgressDialog();
            if(ROWS > 0){

                try{
                    rowCntClass.setRowCnt(String.valueOf(ROWS));
                    String rowcntObj = new Gson().toJson(rowCntClass);
                    AppCommon.getInstance(parent).setRowcnt(rowcntObj);
                    Log.e("appcommonROW - ", ROWCNT);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(KEY.equalsIgnoreCase(KEYSYNCALL)){
                    txtavlitems.setText(String.valueOf(ROWS));
                    fromIndex = "0";
                    toIndex = String.valueOf(incvar);
                    Log.e("toIndex ", toIndex);

                    sql.delete(db.TABLE_GetItemList, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GetItemList, null);
                    int count = c.getCount();
                    Log.e("count ", String.valueOf(count));

                    //call API again with from to parameter, N flag
                    new DownloadGetItemlistJSON().execute(fromIndex, toIndex);
                  /* asyncTask2 =  new DownloadGetItemlistJSON();
                   asyncTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,fromIndex,toIndex);*/

                }else if(KEY.equalsIgnoreCase(KEYRESUME)){
                    if (isnet()) {
                        new StartSession(ItemMasterSyncActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //new DownloadGetItemlistJSON().execute(fromIndex, toIndex);
                                new DownloadGetItemlistJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,fromIndex,toIndex);
                                Log.e("CallAPI - ",fromIndex+","+toIndex);

                                /*Thread thread = new Thread(new Runnable(){
                                    @Override
                                    public void run(){
                                        //code to do the HTTP request
                                        asyncTask2 =  new DownloadGetItemlistJSON();
                                        asyncTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,fromIndex,toIndex);
                                        Log.e("CallAPI - ",fromIndex+","+toIndex);
                                        *//*asyncTask = new DownloadGetItemlistJSON();
                                        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,fromIndex,toIndex);
                                        Log.e("CallAPI - ",fromIndex+","+toIndex);*//*
                                    }
                                });
                                thread.start();*/
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                Log.e("sesstion_issue - ",msg.toString());
                            }
                        });
                    }
                }

            }else {
                //call API again with Y flag
                KEY = KEYSYNCALL;
                //new DownloadROWCountJSON().execute(KEY);
               new DownloadROWCountJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
               // asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
            }
        }
    }

    class DownloadGetItemlistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        String rowsCnt = "0";
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                    progBar.setMax(ROWS);
                    dosomething();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = "";
            String from = params[0];
            String to = params[1];
            int fromIndex = Integer.parseInt(from);
            int toIndex = Integer.parseInt(to);

                //call API again with from to parameter, N flag
                url = CompanyURL + WebUrlClass.api_ItemListAndroid+"?RQry=N&from="+fromIndex+"&to="+toIndex;  //RQry,from,to
            Log.e("url - ",url);

            try {
                res = ut.OpenConnection(url,parent);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);
                    Log.e("url - ",response);

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("url - ",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if(response.contains("ExceptionMessage")){
                Log.e("error - ", "error no response found");

                KEY = KEYRESUME;
                if (isnet()) {
                    new StartSession(ItemMasterSyncActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadROWCountJSON().execute(KEY);
                           //asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,KEY);
                            Log.e("activate row session - ","");
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Log.e("sesstion_issue - ",msg.toString());
                        }
                    });
                }

            }else if(response.equalsIgnoreCase("[]")) {
                Toast.makeText(parent,"No more items",Toast.LENGTH_SHORT).show();

                if(cf.getLstInsertedCount() == ROWS){
                    imgdone.setVisibility(View.VISIBLE);
                    txtdwnld.setVisibility(View.GONE);
                    textprogrs.setVisibility(View.GONE);
                    mProgressStatus = cf.getLstInsertedCount();
                    progBar.setProgress( cf.getLstInsertedCount());
                    textprogrs.setText(""+ cf.getLstInsertedCount()+"");
                }else {
                    imgdone.setVisibility(View.GONE);
                    txtdwnld.setVisibility(View.VISIBLE);
                    textprogrs.setVisibility(View.VISIBLE);
                    mProgressStatus = cf.getLstInsertedCount();
                    progBar.setProgress( cf.getLstInsertedCount());
                    textprogrs.setText(""+ cf.getLstInsertedCount()+"");
                }

            }else {
                parseJson(response, toIndex);
            }
        }

    }

    public void parseJson(String response,String Index){
        toIndex = Index;
        String ItemCode = "", ItemDesc = "", ItemMasterId = "", ItemPlantId = "",
                PurUnit = "", SalesUnit = "", ConvFactor = "", StockUnit = "", WareHouseMasterId = "", LocationMasterId = "",
                WarehouseCode = "",LocationCode = "";

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);

            for(int i=0; i<jsonArray.length();i++){

                try{
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    ItemCode = jobj.getString("ItemCode");
                    ItemDesc = jobj.getString("ItemDesc");
                    ItemMasterId = jobj.getString("ItemMasterId");
                    ItemPlantId = jobj.getString("ItemPlantId");
                    PurUnit = jobj.getString("PurUnit");
                    SalesUnit = jobj.getString("SalesUnit");
                    ConvFactor = jobj.getString("ConvFactor");

                    try{
                        StockUnit = jobj.getString("StockUnit");
                        WareHouseMasterId = jobj.getString("WareHouseMasterId");
                        LocationMasterId = jobj.getString("LocationMasterId");
                        WarehouseCode = jobj.getString("WarehouseCode");
                        LocationCode = jobj.getString("LocationCode");
                    }catch (Exception e){

                    }

                    cf.insertItemMasterData(this,ItemCode, ItemDesc, ItemMasterId, ItemPlantId, PurUnit, SalesUnit, ConvFactor,
                            StockUnit,WareHouseMasterId,LocationMasterId,WarehouseCode,LocationCode);

                    toIndex = String.valueOf(cf.getLstInsertedCount());
                    mProgressStatus = Integer.parseInt(toIndex);
                    //progBar.setProgress(Integer.parseInt(toIndex));
                    txtdwnlitems.setText(toIndex);
                    Log.e("lastInsertedIndex - ", toIndex);
                    progBar.setProgress(Integer.parseInt(toIndex));
                    textprogrs.setText(""+toIndex+"");
                    txtstartfrom.setText(ItemCode);
                    //  Toast.makeText(parent," "+toIndex+" records downloaded",Toast.LENGTH_SHORT).show();

                   /* if(mProgressStatus == ROWS){
                        imgdone.setVisibility(View.VISIBLE);
                        textprogrs.setVisibility(View.GONE);
                    }else {
                        imgdone.setVisibility(View.GONE);
                        textprogrs.setVisibility(View.VISIBLE);
                    }*/

                    if(cf.getLstInsertedCount() == ROWS){
                        imgdone.setVisibility(View.VISIBLE);
                        txtdwnld.setVisibility(View.GONE);
                        textprogrs.setVisibility(View.GONE);
                        mProgressStatus = cf.getLstInsertedCount();
                        progBar.setProgress( cf.getLstInsertedCount());
                        textprogrs.setText(""+ cf.getLstInsertedCount()+"");
                    }else {
                        imgdone.setVisibility(View.GONE);
                        txtdwnld.setVisibility(View.VISIBLE);
                        textprogrs.setVisibility(View.VISIBLE);
                        mProgressStatus = cf.getLstInsertedCount();
                        progBar.setProgress( cf.getLstInsertedCount());
                        textprogrs.setText(""+ cf.getLstInsertedCount()+"");
                    }

                    try{
                        rowCntClass.setLastitemCode(ItemCode);
                        String itemcodeObj = new Gson().toJson(rowCntClass);
                        AppCommon.getInstance(parent).setRowcnt(itemcodeObj);
                        Log.e("itemcodeObj - ", itemcodeObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    toIndex = String.valueOf(cf.getLstInsertedCount());
                    Log.e("exception index - ", toIndex+","+ e.getMessage());
                    Toast.makeText(parent,"task paused at "+toIndex+" records ",Toast.LENGTH_SHORT).show();
                    txtstartfrom.setText(ItemCode);

                    try{
                        rowCntClass.setLastitemCode(ItemCode);
                        String itemcodeObj = new Gson().toJson(rowCntClass);
                        AppCommon.getInstance(parent).setRowcnt(itemcodeObj);
                        Log.e("itemcodeObj - ", itemcodeObj);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            toIndex = String.valueOf(cf.getLstInsertedCount());
            // Log.e("lastInsertedIndex - ", toIndex);
            Log.e("jsnparse exc - ", toIndex+","+ e.getMessage());
            Toast.makeText(parent,"unable to parse json response",Toast.LENGTH_SHORT).show();
        }

        //fromIndex = String.valueOf(Integer.parseInt(toIndex) + 1);
        fromIndex = toIndex;
        int sum = Integer.parseInt(toIndex) + incvar;
        //int diff = ROWS - Integer.parseInt(toIndex);

        if(sum > ROWS){
            toIndex = String.valueOf(ROWS);
        }else {
            toIndex = String.valueOf(sum);
        }

        if (isnet()) {
            new StartSession(ItemMasterSyncActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                  //  new DownloadGetItemlistJSON().execute(fromIndex, toIndex);
                    new DownloadGetItemlistJSON().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,fromIndex,toIndex);
                    Log.e("CallAPI - ",fromIndex+","+toIndex);
                }

                @Override
                public void callfailMethod(String msg) {
                    Log.e("sesstion_issue - ",msg.toString());
                }
            });
        }
}

    class DownloadFileFromURL_new extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // showDialog(progress_bar_type);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Downloading file...",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            int count;

            try {
                URL url = new URL(params[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

            /*    res = ut.OpenConnection(url,parent);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);
                    Log.e("url - ",response);

                }*/

                int lenghtOfFile = conection.getContentLength();
                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + "/ItemMaster.json");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                 //   publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();

                try{
                    filePathUrl = Environment.getExternalStorageDirectory().toString() + "/ItemMaster.json";
                    Log.e("file_url",filePathUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }

                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //dismissDialog(progress_bar_type);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "File downloaded successfully, storing items in file...",
                            Toast.LENGTH_LONG).show();
                }
            });


            Thread thread = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        readFileFromAssets(parent,filePathUrl);

                    }
                }
            };
            thread.start();

        }

    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
              //  InputStream input = new BufferedInputStream(url.openStream(), 8192);
                InputStream input = new BufferedInputStream(url.openStream(), lenghtOfFile);

                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + "/ItemMaster.json");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();

                try{
                   filePathUrl = Environment.getExternalStorageDirectory().toString() + "/ItemMaster.json";
                    Log.e("file_url",filePathUrl);
                }catch (Exception e){
                    e.printStackTrace();
                }

                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
           // Log.e("file_url",String.valueOf(file_url));
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            /*Thread thread = new Thread() {
                public void run() {
                    while (true) {
                        *//*try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }*//*
                        readFileFromAssets(parent,filePathUrl);
                    }
                }
            };
            thread.start();*/

         //   new LoadData().execute();
            new DownloadFileTask(ItemMasterSyncActivity.this,"Downloading items...").execute();

        }

    }

    private void readFile_json(String readFileDB) {

        sql.delete(db.TABLE_GetItemList,null,null);

        parseFileData(readFileDB);

    }

    private void parseFileData(String readFileDB) {

        String ItemCode = "", ItemDesc = "", ItemMasterId = "", ItemPlantId = "",
                PurUnit = "", SalesUnit = "", ConvFactor = "",StockUnit = "", WareHouseMasterId = "", LocationMasterId = "",
                WarehouseCode = "",LocationCode = "";

        JSONArray jsonArray = null;
        View view = null;

        try {
            jsonArray = new JSONArray(readFileDB);

            for(int i=0; i<jsonArray.length();i++){

                try{
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    ItemCode = jobj.getString("ItemCode");
                    ItemDesc = jobj.getString("ItemDesc");
                    ItemMasterId = jobj.getString("ItemMasterId");
                    ItemPlantId = jobj.getString("ItemPlantId");
                    PurUnit = jobj.getString("PurUnit");
                    SalesUnit = jobj.getString("SalesUnit");
                    ConvFactor = jobj.getString("ConvFactor");

                    try{
                        StockUnit = jobj.getString("StockUnit");
                        WareHouseMasterId = jobj.getString("WareHouseMasterId");
                        LocationMasterId = jobj.getString("LocationMasterId");
                        WarehouseCode = jobj.getString("WarehouseCode");
                        LocationCode = jobj.getString("LocationCode");
                    }catch (Exception e){

                    }


                    final String finalItemCode = ItemCode;
                    final String finalItemCode1 = ItemCode;

                    cf.insertItemMasterData(this,ItemCode, ItemDesc, ItemMasterId, ItemPlantId, PurUnit, SalesUnit, ConvFactor,
                            StockUnit,WareHouseMasterId,LocationMasterId,WarehouseCode,LocationCode);

                    try{
                        rowCntClass.setLastitemCode(finalItemCode);
                        String itemcodeObj = new Gson().toJson(rowCntClass);
                        AppCommon.getInstance(parent).setRowcnt(itemcodeObj);
                        // Log.e("itemcodeObj - ", itemcodeObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }catch (Exception e){
                    e.printStackTrace();

                }

                //txtdwnlitems.setText(String.valueOf(cf.getGetItemcount()));

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ItemMasterSyncActivity.this,
                                "Storing items..."+String.valueOf(cf.getGetItemcount()),Toast.LENGTH_SHORT).show();
                        Log.e("Count2 : ",String.valueOf(cf.getGetItemcount()));
                        ItemMasterSyncActivity.this.txtdwnlitems.setText(String.valueOf(cf.getGetItemcount()));
                    }
                });*/
            }

         /*   runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                }
            });*/

        } catch (JSONException e) {
            e.printStackTrace();
            toIndex = String.valueOf(cf.getLstInsertedCount());
            // Log.e("lastInsertedIndex - ", toIndex);
            Log.e("jsnparse exc - ", String.valueOf(cf.getGetItemcount())+","+ e.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ItemMasterSyncActivity.this,"unable to parse json response",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    public void readFileFromAssets(Context context, String fileName) {
        String data = "";

       /* runOnUiThread(new Runnable() {

            @Override
            public void run() {
                progress.setVisibility(View.VISIBLE);
            }
        });*/

         File file = new File(fileName);

        //Read text from file
        final StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            data = line;
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            //You'll need to add proper error handling here
        }

        readFile_json(text.toString().trim());

     //   return line;
    }

    public String getLastItemCOde(){
        String iCode = "";
        String qry = "Select ItemCode from "+db.TABLE_GetItemList;//+" LIMIT 1";
        Cursor c = sql.rawQuery(qry, null);
        if(c.getCount() > 0){
            c.moveToLast();
            iCode = c.getString(c.getColumnIndex("ItemCode"));
        }else {

        }

        return iCode;
    }

    public class LoadData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        //declare other objects as per your need
        @Override
        protected void onPreExecute()
        {
            progressDialog= ProgressDialog.show(ItemMasterSyncActivity.this, "Item Master Sync",
                    "Storing items please wait...", true);
            progressDialog.setCanceledOnTouchOutside(true);

            //do initialization of required objects objects here
        };
        @Override
        protected Void doInBackground(Void... params)
        {
            readFileFromAssets(parent,filePathUrl);
            //do loading operation here
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            progressDialog.dismiss();
        };

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            progressDialog= ProgressDialog.show(ItemMasterSyncActivity.this, "Item Master Sync",
                    "Storing items please wait...", true);
            progressDialog.setCanceledOnTouchOutside(true);
            //txtdwnlitems.setText();
        }
    }

    public class DownloadFileTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog mPDialog;
        private Context mContext;
        private PowerManager.WakeLock mWakeLock;
        //Constructor parameters :
        // @context (current Activity)
        // @targetFile (File object to write,it will be overwritten if exist)
        // @dialogMessage (message of the ProgresDialog)

        public DownloadFileTask(Context context,String dialogMessage) {
            this.mContext = context;
            mPDialog = new ProgressDialog(context);

            mPDialog.setMessage(dialogMessage);
            mPDialog.setIndeterminate(true);
            mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mPDialog.setCancelable(false);
            mPDialog.setCanceledOnTouchOutside(false);
            // reference to instance to use inside listener
            final DownloadFileTask me = this;
            mPDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    me.cancel(false);
                }
            });
            Log.i("DownloadTask","Constructor done");
        }

        @Override
        protected String doInBackground(String... sUrl) {

            // readFileFromAssets(HomeScreenActvity.this,file_url_1);

            try {
                readFileFromAssets(ItemMasterSyncActivity.this,filePathUrl);
            }
            catch (Exception e) {
                e.printStackTrace();
                //You'll need to add proper error handling here
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();

            mPDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mPDialog.setIndeterminate(false);
            //mPDialog.setMax(100);
            mPDialog.setMax(ROWS);
           // mPDialog.setProgress(progress[0]);
            mPDialog.setProgress(cf.getGetItemcount());

        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("DownloadTask", "Work Done! PostExecute");
            mWakeLock.release();
            mPDialog.dismiss();
            if (result != null)
                Toast.makeText(mContext,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
                txtdwnlitems.setText(String.valueOf(cf.getGetItemcount()));
                Toast.makeText(mContext,"Items Downloaded successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                File file = new File(getRealPathFromUri(ItemMasterSyncActivity.this,uri));//create path from uri
                final String path = file.toString();
                String Imagefilename = file.getName();
                filePathUrl = path;

               // new LoadData().execute();
                new DownloadFileTask(ItemMasterSyncActivity.this,"Downloading items...").execute();

               /* Thread thread = new Thread() {
                    public void run() {
                        while (true) {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            readFileFromAssets(parent, path);
                        }
                    }
                };
                thread.start();*/
            }
        }
    }

    @Override
    public void onBackPressed() {
       /* Log.i("clickMe" , "Yes");
        if (asyncTask2 != null)
            asyncTask2.cancel(true);

        super.onBackPressed();*/

       finish();


     /*   if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            layoptions.setVisibility(View.VISIBLE);
            layprogrsbar.setVisibility(View.GONE);

            if(layprogrsbar.getVisibility() == View.VISIBLE){
                layoptions.setVisibility(View.VISIBLE);
                layprogrsbar.setVisibility(View.GONE);
            }else if(layoptions.getVisibility() == View.VISIBLE){
                //finish();
                super.onBackPressed();
            }
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
*/
      /* try{
            if (asyncTask2 != null) {
                //asyncTask.cancel(true);
                //show dialogue box and then calcel task
                asyncTask2.cancel(true);
                Toast.makeText(parent,"task is running", Toast.LENGTH_SHORT).show();
            }else if(asyncTask2 == null){
                Toast.makeText(parent,"task is not running", Toast.LENGTH_SHORT).show();
                layoptions.setVisibility(View.VISIBLE);
                layprogrsbar.setVisibility(View.GONE);

                if(layprogrsbar.getVisibility() == View.VISIBLE){
                    layoptions.setVisibility(View.VISIBLE);
                    layprogrsbar.setVisibility(View.GONE);
                }else if(layoptions.getVisibility() == View.VISIBLE){
                   super.onBackPressed();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
           super.onBackPressed();
        }*/
    }

}
