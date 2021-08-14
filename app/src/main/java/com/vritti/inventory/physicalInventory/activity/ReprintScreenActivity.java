package com.vritti.inventory.physicalInventory.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beeprt.print.PrintPP_CPCL;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.bean.PrintDataClass_QSPrinter;
import com.vritti.inventory.physicalInventory.adapter.LocationListAdapter;
import com.vritti.inventory.physicalInventory.bean.BeanTag;
import com.vritti.inventory.physicalInventory.bean.BeanTagList;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.inventory.physicalInventory.bean.Utils_print;
import com.vritti.sales.beans.BillNoClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

public class ReprintScreenActivity extends AppCompatActivity {
    private Context parent;
    AutoCompleteTextView edttagno, edttagfrm, edttagto;
    ImageView img_barcode, img_barcode_frm, img_barcode_to;
    TextView txtprevtag;

    LinearLayout layrangeprint;
    RelativeLayout laysingleprint;
    RadioButton radbtnsingle, radbtnrange;
    Button btnproceed;
    RadioGroup radgroup;

    View sheetview;
    RelativeLayout laytag;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="",UserName = "",
            MobileNo = "",Indentamount;
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;
    public static final int REQUEST_ENABLE_BT = 4;
    public static final int REQUEST_CONNECT_DEVICE = 6;
    public static final int REQ_PARTCODE = 7;
    public static final int REQ_PARTNAME = 8;

    double CONV_factor = 1;
    String PIHdrId = "", PIDtlId = "", AuditedItemPlantId = "", AuditedItemCode = "", AuditedItemDesc = "", AuditedLocationCode = "",
            AuditedLocationMasterId = "", AuditedWeight = "", AuditedActualQty = "", AuditedVerifyBy = "", TAGNO = "",
            TAGNOFRM = "", TAGNTO = "", AuditedWareHouseMasterId = "", AuditedWarehouseCode = "";

    String OLDItemPlantId = "", OLDItemCode = "", OLDItemDesc = "", OLDLocationCode = "",OLDLocationMasterId = "",
            OLDWeight = "", OLDActualQty = "", OLDVerifyBy = "", OLDWareHouseMasterId = "", OLDWarehouseCode = "", OLDCountedBy = "",
            OLDTAGDescription = "";

    // int tagNO = 0;

    String dateStr = "", AuditDATE = "";
    JSONObject jMain = null;

    ArrayList<LocationList>locationListArrayList;
    LocationListAdapter locationListAdapter;

    private SharedPreferences sharedPrefs;
    String labelSize = "",selPrinterName="";
    Gson gson;
    private String json;
    Type type;
    String radioFlag = "", barcodeFLAG = "";
    /*int  TagCurrentNo,TagEndNo,TagStartNo;
    int billNO = 0;*/

    String billnumber = "0000";
    static int year, month, day;
    public static String today, todaysDate;
    public static String date = null;
    public static String time = null;
    String DATE = "", TIME = "";

    ArrayList<BeanTag> tagPrintList;
    BillNoClass billNoClass;
    BeanTagList beanTagObj;
    BeanTag glBeanObject;
    String BatchHdID = "", batchCODE = "";

    PIEntryPrintingActivity.PrintLabel printLabel;
    PrintPP_CPCL printPP_cpcl;
    private PrintLabel pl;
    private int interval;
    View viewforprint;
    private boolean isSending = false;
    private boolean isConnected = false;
    private ProgressDialog dialog;
    private BluetoothAdapter mBluetoothAdapter;
    String _countedByPrint = "", _verifyByPrint = "";

    PrintDataClass_QSPrinter dataClass_qsPrinter;
    String UOMVAL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprint_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        dataClass_qsPrinter = new PrintDataClass_QSPrinter();

        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(ReprintScreenActivity.this,
                    "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        *//*Intent serverIntent = new Intent(ReprintScreenActivity.this, Devicelist_LablelPrint.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);*//*
        printPP_cpcl = new PrintPP_CPCL();*/

        _countedByPrint = "Counted By : "+ getPrevCountedByName();
        _verifyByPrint = "Verify By : "+UserName;

        setListeners();
    }

    private void init() {
        parent = ReprintScreenActivity.this;

        edttagno = findViewById(R.id.edttagno);
        edttagfrm = findViewById(R.id.edttagfrm);
        edttagto = findViewById(R.id.edttagto);
        img_barcode = findViewById(R.id.img_barcode);
        img_barcode_frm = findViewById(R.id.img_barcode_frm);
        img_barcode_to = findViewById(R.id.img_barcode_to);
        radbtnrange = findViewById(R.id.radbtnrange);
        radbtnsingle = findViewById(R.id.radbtnsingle);
        laysingleprint = findViewById(R.id.laysingleprint);
        layrangeprint = findViewById(R.id.layrangeprint);
        btnproceed = findViewById(R.id.btnproceed);
        radgroup = findViewById(R.id.radgroup);
        txtprevtag= findViewById(R.id.txtprevtag);
        btnproceed.setEnabled(true);
        btnproceed.setText("PROCEED");
        btnproceed.setAlpha(1);

        ut = new Utility();
        cf = new CommonFunction(ReprintScreenActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ReprintScreenActivity.this);
        String dabasename = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ReprintScreenActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ReprintScreenActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        Date today = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        AuditDATE = curFormater.format(today);
        Log.e("AuditDATE : ",AuditDATE);

        tagPrintList = new ArrayList<BeanTag>();

        /*sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReprintScreenActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("location", "");
        type = new TypeToken<List<LocationList>>() {}.getType();
        locationListArrayList = gson.fromJson(json, type);*/

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReprintScreenActivity.this);
        labelSize = sharedPrefs.getString("labelSize", "3x2mm");
        selPrinterName = sharedPrefs.getString("PrinterName", "");

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ReprintScreenActivity.this);
        batchCODE = sharedPrefs.getString("selectedBatchCode", "");
        BatchHdID = sharedPrefs.getString("selectedBatchHDRID", "");

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

                                String  prevTag = "0000" + String.valueOf(glBeanObject.getTagNo());
                                //StringUtils.leftPad("129018", 10, "0");   //padding
                                if (prevTag.length() == 5) {
                                    prevTag = prevTag.substring(prevTag.length() - 5, 5);
                                } else if (prevTag.length() == 6) {
                                    prevTag = prevTag.substring(prevTag.length() - 5, 6);
                                } else if (prevTag.length() == 7) {
                                    prevTag = prevTag.substring(prevTag.length() - 5, 7);
                                } else if (prevTag.length() == 8) {
                                    prevTag = prevTag.substring(prevTag.length() - 5, 8);
                                }else if (prevTag.length() == 9) {
                                    prevTag = prevTag.substring(prevTag.length() - 5, 9);
                                }
                                txtprevtag.setText(" Previous Tag No is  "+prevTag);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setListeners() {

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ReprintScreenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        radbtnsingle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                edttagfrm.setText("");
                edttagto.setText("");

                if (isChecked){
                    radioFlag = "SINGLE";
                    radbtnsingle.setChecked(true);
                    radbtnrange.setChecked(false);
                    layrangeprint.setVisibility(View.GONE);
                    laysingleprint.setVisibility(View.VISIBLE);
                    // Onstatus=radio_ofline.getText().toString();

                }else {
                }
            }
        });

        radbtnrange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                edttagno.setText("");

                if (isChecked){
                    radioFlag = "RANGE";
                    radbtnrange.setChecked(true);
                    radbtnsingle.setChecked(false);
                    // Onstatus=radio_ofline.getText().toString();
                    layrangeprint.setVisibility(View.VISIBLE);
                    laysingleprint.setVisibility(View.GONE);

                }else {
                }
            }
        });

        btnproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewforprint = v;

                btnproceed.setEnabled(false);
                btnproceed.setText("Sending...");
                btnproceed.setAlpha((float) 0.5);

                if(radioFlag.equalsIgnoreCase("SINGLE")){

                    TAGNO = edttagno.getText().toString().trim();
                    AuditedVerifyBy = UserName;

                    singlePrint();

                }else if(radioFlag.equalsIgnoreCase("RANGE")){

                    TAGNOFRM = edttagfrm.getText().toString().trim();
                    TAGNTO = edttagto.getText().toString().trim();
                    AuditedVerifyBy = UserName;

                    rangePrint();

                }

            }
        });

        img_barcode_frm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeFLAG = "FROM";

                IntentIntegrator integrator = new IntentIntegrator(ReprintScreenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        img_barcode_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeFLAG = "TO";

                IntentIntegrator integrator = new IntentIntegrator(ReprintScreenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    private void singlePrint() {

        if(TAGNO.equalsIgnoreCase("") || TAGNO.equalsIgnoreCase(null)){
            Toast.makeText(parent,"Please enter tag number",Toast.LENGTH_SHORT).show();
        }else {
            //call API to get Tag details
            if (isnet()) {
                new StartSession(ReprintScreenActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadTagDetails().execute(TAGNO);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }else {
                Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void rangePrint() {

        if(TAGNOFRM.equalsIgnoreCase("") || TAGNTO.equalsIgnoreCase(null)){
            Toast.makeText(parent,"Please enter tag number",Toast.LENGTH_SHORT).show();
        }else {
            //call API to get Tag details
            if (isnet()) {
                new StartSession(ReprintScreenActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DwnldRangeTagDetails().execute(TAGNOFRM, TAGNTO);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }else {
                Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getPrevCountedByName() {
        String qry = "Select CountedBy from "+db.TABLE_PI_GENERATION;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToLast();
            _countedByPrint = c.getString(c.getColumnIndex("CountedBy"));
            // edt_countedby.setText(c.getString(c.getColumnIndex("CountedBy")));
        }else {
            // edt_countedby.setText("");
        }

        return _countedByPrint;
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

    public double getConvFactor(String ItemCode){
        //String qry = " SELECT ConvFactor FROM "+db.TABLE_GetItemList+ " WHERE  ItemCode like '%" + ItemCode + "%'";
        String qry = " SELECT ConvFactor FROM "+db.TABLE_GetItemList+ " WHERE  ItemCode='"+ItemCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            CONV_factor = c.getDouble(c.getColumnIndex("ConvFactor"));
        }else {

        }

        return CONV_factor;
    }

    class DownloadTagDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ReprintScreenActivity.this);
                    progressDialog.setMessage("Searching for Tag details Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            TAGNO = params[0];
            String url = CompanyURL + WebUrlClass.api_TagDetails+"?TagNo="+TAGNO+"&Task=Reprint";  //RQry,from,to

            try {
                res = ut.OpenConnection(url, ReprintScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();

            if(response.equalsIgnoreCase("No Record Found")){
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(parent,"Sorry! Tag "+TAGNO+" not available, it might be deleted.",Toast.LENGTH_SHORT).show();
                        btnproceed.setEnabled(true);
                        btnproceed.setText("PROCEED");
                        btnproceed.setAlpha(1);
                    }
                });

            }else {
                getTagDetails_SinglePrint(response);
            }
        }
    }

    private void getTagDetails_SinglePrint(String response) {

        if(response.equalsIgnoreCase("[]")){
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(parent,"Sorry! Tag not available, it might be deleted.",Toast.LENGTH_LONG).show();
                    btnproceed.setEnabled(true);
                    btnproceed.setText("PROCEED");
                    btnproceed.setAlpha(1);
                }
            });

        }else if (!response.equalsIgnoreCase(null)) {

            tagPrintList.clear();

            JSONArray jResults = null;
            try {
                jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    OLDItemCode = jorder.getString("ItemCode");
                    OLDItemDesc = jorder.getString("ItemDesc");
                    PIDtlId = jorder.getString("PIDtlId");
                    PIHdrId = jorder.getString("PIHdrId");
                    TAGNO = jorder.getString("TagNo");
                    OLDWeight = jorder.getString("weight");
                    OLDActualQty = jorder.getString("ActualQty");
                    OLDLocationMasterId = jorder.getString("LocationMasterId");
                    OLDLocationCode = jorder.getString("LocationCode");     //todisplay
                    OLDWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    OLDWarehouseCode = jorder.getString("WarehouseCode");  //todisplay
                    OLDCountedBy = jorder.getString("CountedBy");  //todisplay
                    OLDTAGDescription = jorder.getString("TAGDescription");  //todisplay

                    AuditedItemPlantId = jorder.getString("AuditedItemPlantId");
                    AuditedLocationMasterId = jorder.getString("AuditedLocationId");
                    AuditedActualQty = jorder.getString("AuditedQty");
                    AuditedWeight = jorder.getString("AuditedWeight");
                    AuditedVerifyBy = jorder.getString("AuditedBy");

                    if(!AuditedItemPlantId.equalsIgnoreCase("") || !AuditedItemPlantId.equalsIgnoreCase(null)){
                       /* String[] data = getAuditedItemcodeDesc(AuditedItemPlantId).split("_");
                        AuditedItemCode = data[0];
                        AuditedItemDesc = data[1];*/
                        getAuditedItemcodeDesc(AuditedItemPlantId);
                    }

                    AuditedVerifyBy = UserName;

                    try{
                        UOMVAL = getUOMVAL(OLDItemCode);
                        //txtuom.setText(UOMVAL);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    CONV_factor = getConvFactor(OLDItemCode);
                    Log.e("CF -",String.valueOf(CONV_factor));

                    BeanTag beanTag = new BeanTag();
                    beanTag.setItemPlantId_print(OLDItemPlantId);
                    beanTag.setItemCode_print(OLDItemCode);
                    beanTag.setItemDesc_print(OLDItemDesc);
                    beanTag.setPIDtlId_print(PIDtlId);
                    beanTag.setPIHdrId_print(PIHdrId);
                    beanTag.setTagNo_print(TAGNO);
                    beanTag.setWeight_print(OLDWeight);
                    beanTag.setActualQty_print(OLDActualQty);
                    beanTag.setLocationMasterId_print(OLDLocationMasterId);
                    beanTag.setLocationCode_print_print(OLDLocationCode);
                    beanTag.setWareHouseMasterId_print(OLDWareHouseMasterId);
                    beanTag.setWarehouseCode_print(OLDWarehouseCode);
                    beanTag.setAuditedItemPlantId_print(AuditedItemPlantId);
                    beanTag.setAuditedLocationId_print(AuditedLocationMasterId);
                    beanTag.setAuditedQty_print(AuditedActualQty);
                    beanTag.setAuditedWeight_print(AuditedWeight);
                    beanTag.setAuditedBy_print(AuditedVerifyBy);
                    beanTag.setAuditedItemCode(AuditedItemCode);
                    beanTag.setAuditedItemDesc(AuditedItemDesc);
                    beanTag.setUomToPrint(UOMVAL);

                    tagPrintList.add(beanTag);

                }

                /*if(BluetoothClass.isPrinterConnected(getApplicationContext(), ReprintScreenActivity.this)) {
                    mService = BluetoothClass.getServiceInstance();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printReceipt(tagPrintList);
                    }

                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(),ReprintScreenActivity.this);
                }*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    // printReceipt(tagPrintList);     //teklogic printer
                    //LTKPrint_single(viewforprint,tagPrintList);      //LTK Print

                    getPrintData(tagPrintList);     //QSPrinter data

                }


            } catch (JSONException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnproceed.setEnabled(true);
                        btnproceed.setText("PROCEED");
                        btnproceed.setAlpha(1);
                    }
                });

            }
        }else if(response.equalsIgnoreCase("error")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(parent,"Server error",Toast.LENGTH_SHORT).show();
                    btnproceed.setEnabled(true);
                    btnproceed.setText("PROCEED");
                    btnproceed.setAlpha(1);
                }
            });
        }
    }

    private void getTagDetails(String response) {

        if(response.equalsIgnoreCase("[]")){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnproceed.setEnabled(true);
                    btnproceed.setText("PROCEED");
                    btnproceed.setAlpha(1);
                    Toast.makeText(parent,"Sorry! Tags not available, it might be deleted.",Toast.LENGTH_LONG).show();
                }
            });

        }else if (!response.equalsIgnoreCase(null)) {

            tagPrintList.clear();

            JSONArray jResults = null;
            try {
                jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    OLDItemCode = jorder.getString("ItemCode");
                    OLDItemDesc = jorder.getString("ItemDesc");
                    PIDtlId = jorder.getString("PIDtlId");
                    PIHdrId = jorder.getString("PIHdrId");
                    TAGNO = jorder.getString("TagNo");
                    OLDWeight = jorder.getString("weight");
                    OLDActualQty = jorder.getString("ActualQty");
                    OLDLocationMasterId = jorder.getString("LocationMasterId");
                    OLDLocationCode = jorder.getString("LocationCode");     //todisplay
                    OLDWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    OLDWarehouseCode = jorder.getString("WarehouseCode");  //todisplay

                    AuditedItemPlantId = jorder.getString("AuditedItemPlantId");
                    AuditedLocationMasterId = jorder.getString("AuditedLocationId");
                    AuditedActualQty = jorder.getString("AuditedQty");
                    AuditedWeight = jorder.getString("AuditedWeight");
                    AuditedVerifyBy = jorder.getString("AuditedBy");

                    if(!AuditedItemPlantId.equalsIgnoreCase("") || !AuditedItemPlantId.equalsIgnoreCase(null)){
                       /* String[] data = getAuditedItemcodeDesc(AuditedItemPlantId).split("_");
                        AuditedItemCode = data[0];
                        AuditedItemDesc = data[1];*/
                        getAuditedItemcodeDesc(AuditedItemPlantId);
                    }

                    AuditedVerifyBy = UserName;

                    try{
                        UOMVAL = getUOMVAL(OLDItemCode);
                        //txtuom.setText(UOMVAL);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    CONV_factor = getConvFactor(OLDItemCode);
                    Log.e("CF -",String.valueOf(CONV_factor));

                    BeanTag beanTag = new BeanTag();
                    beanTag.setItemPlantId_print(OLDItemPlantId);
                    beanTag.setItemCode_print(OLDItemCode);
                    beanTag.setItemDesc_print(OLDItemDesc);
                    beanTag.setPIDtlId_print(PIDtlId);
                    beanTag.setPIHdrId_print(PIHdrId);
                    beanTag.setTagNo_print(TAGNO);
                    beanTag.setWeight_print(OLDWeight);
                    beanTag.setActualQty_print(OLDActualQty);
                    beanTag.setLocationMasterId_print(OLDLocationMasterId);
                    beanTag.setLocationCode_print_print(OLDLocationCode);
                    beanTag.setWareHouseMasterId_print(OLDWareHouseMasterId);
                    beanTag.setWarehouseCode_print(OLDWarehouseCode);
                    beanTag.setAuditedItemPlantId_print(AuditedItemPlantId);
                    beanTag.setAuditedLocationId_print(AuditedLocationMasterId);
                    beanTag.setAuditedQty_print(AuditedActualQty);
                    beanTag.setAuditedWeight_print(AuditedWeight);
                    beanTag.setAuditedBy_print(AuditedVerifyBy);
                    beanTag.setAuditedItemCode(AuditedItemCode);
                    beanTag.setAuditedItemDesc(AuditedItemDesc);
                    beanTag.setUomToPrint(UOMVAL);

                    tagPrintList.add(beanTag);

                    Collections.sort(tagPrintList, new Comparator<BeanTag>() {
                        @Override
                        public int compare(BeanTag lhs, BeanTag rhs) {
                            return lhs.getTagNo_print().compareTo(rhs.getTagNo_print());
                        }
                    });

                }

                /*if(BluetoothClass.isPrinterConnected(getApplicationContext(), ReprintScreenActivity.this)) {
                    mService = BluetoothClass.getServiceInstance();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        printReceipt(tagPrintList);
                    }

                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(),ReprintScreenActivity.this);
                }*/

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    //LTKPrint_range(viewforprint, tagPrintList);

                    getPrintData(tagPrintList);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                btnproceed.setEnabled(true);
                btnproceed.setText("PROCEED");
                btnproceed.setAlpha(1);
            }
        }else if(response.equalsIgnoreCase("error")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnproceed.setEnabled(true);
                    btnproceed.setText("PROCEED");
                    btnproceed.setAlpha(1);
                    Toast.makeText(parent,"Server error",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getAuditedItemcodeDesc(String auditedItemPlantId) {
        String qry = "SELECT ItemCode, ItemDesc FROM "+db.TABLE_GetItemList+" WHERE ItemPlantId='"+auditedItemPlantId+"'";
        Cursor c= sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            AuditedItemCode = c.getString(c.getColumnIndex("ItemCode"));
            AuditedItemDesc = c.getString(c.getColumnIndex("ItemDesc"));
        }
        //return AuditedItemCode+""+AuditedItemDesc;
    }

    class DwnldRangeTagDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ReprintScreenActivity.this);
                    progressDialog.setMessage("Searching for Tag details Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            TAGNO = params[0];
            String url = CompanyURL + WebUrlClass.api_RangePrint+"?FromTagNo="+TAGNOFRM+"&ToTagNo="+TAGNTO;  //RQry,from,to

            try {
                res = ut.OpenConnection(url, ReprintScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.toString().replaceAll("\\\\", "");
                    // response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            getTagDetails(response);

        }
    }

    public void printReceipt(ArrayList<BeanTag> listToPrint){

        //  displayProduct();
      /*  int BILL_NO = 0;
        try{
            BILL_NO =  billNoClass.getBill_no();
        }catch (Exception e){
            e.printStackTrace();
            BILL_NO = 0;
        }*/

        // int billNO = BILL_NO + 1;

        for(int k =0; k<listToPrint.size(); k++) {
            //int billNO = Integer.parseInt(TAGNO);
            String billNO = listToPrint.get(k).getTagNo_print();

            if(billNO.contains(".0")){
                billNO =  billNO.replace(".0","");
            }

            billnumber = "0000" + billNO;
            //StringUtils.leftPad("129018", 10, "0");   //padding
            if (billnumber.length() == 5) {
                billnumber = billnumber.substring(billnumber.length() - 5, 5);
            } else if (billnumber.length() == 6) {
                billnumber = billnumber.substring(billnumber.length() - 5, 6);
            } else if (billnumber.length() == 7) {
                billnumber = billnumber.substring(billnumber.length() - 5, 7);
            } else if (billnumber.length() == 8) {
                billnumber = billnumber.substring(billnumber.length() - 5, 8);
            }else if (billnumber.length() == 9) {
                billnumber = billnumber.substring(billnumber.length() - 5, 9);
            }

            //  for(int b =0; b<5; b++){
            final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
            final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
            final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
            final byte[] SMALLFONT = {0x1b, 0x21, 0x01}; //small font
            final byte[] DEFAULT = {0x1b, 0x21, 0x00};
            final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text

            byte[] format = {27, 33, 0};
            byte[] arrayOfByte1 = {27, 33, 0};
            // Small
            format[2] = ((byte) (0x1 | arrayOfByte1[2]));

            String msg = null, company = "";
            String itemcode = "", itemdesc = "", dateTime = "", username = "", location = "", countedby = "", area = "";
            double weight = 0, qty = 0;

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            String yr = String.valueOf(year);

            if(yr.equals("2020")){
                try{
                    Date today1 = new Date();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today1);

                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.add(Calendar.DATE, -1);

                    Date lastDayOfMonth = calendar.getTime();
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    String a = sdf.format(lastDayOfMonth);

                    date = a;
                    DATE = date;

                }catch (Exception e) {
                    e.printStackTrace();

                    date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                    DATE = date;
                }
            }else {
                date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                DATE = date;
            }

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            time = updateTime(hour, minute);
            TIME = time;

            itemcode = listToPrint.get(k).getItemCode_print().trim();
            itemdesc = listToPrint.get(k).getItemDesc_print().trim();
            location = listToPrint.get(k).getLocationCode_print_print().trim();
            countedby = UserName.trim();
            area = listToPrint.get(k).getWarehouseCode_print().trim();
            weight = Double.parseDouble(listToPrint.get(k).getWeight_print().trim());
            qty = Double.parseDouble(listToPrint.get(k).getActualQty_print().trim());

            //Print text on label
            try {
                String productId = billnumber;

                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                Writer codeWriter;
                codeWriter = new Code128Writer();
                BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 300, 50, hintMap);
                int width1 = byteMatrix.getWidth();
                int height1 = byteMatrix.getHeight();
                Log.e("x ", String.valueOf(width1));
                Log.e("y ", String.valueOf(height1));
                Bitmap bitmap = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < width1; i++) {
                    for (int j = 0; j < height1; j++) {
                        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                mService.write(ALIGN_RIGHT);
                byte[] command = Utils_print.decodeBitmap(bitmap);
                mService.write(command);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            // msg = " No.: " + billnumber + "         " + itemcode + "\n";
            msg = " " + itemcode + "               " + billnumber + "\n";
            //msg += " "+itemcode+"\n";

            if (itemdesc.length() > 40) {
                itemdesc = itemdesc.substring(0, 40);
            } else if (itemdesc.length() <= 40) {
                int diff = 40 - itemdesc.length();
                for (int i = 0; i < diff; i++) {
                    itemdesc += " ";
                }
            }

            msg += " " + itemdesc + "\n";
            // msg += " Loc/Area : " + location + "/" + area + "\n";
            msg += " Area/Loc : " + area + "/" + location + "\n";

       /* if(CONV_factor == 1 || CONV_factor == 1.0){
            //do not show weight
            msg += " Qty  : "+qty+"\n";
        }else if(CONV_factor > 1 || CONV_factor > 1.0){
            msg += " Weight/Qty : "+String.format("%.2f",weight)+" kg"+"/"+qty+"\n";
            // msg += "Qty       : "+qty+"\n";
        }else {
            msg += " Qty       : "+qty+"\n";
        }*/

            if (weight == 0 || weight == 0.0 || weight == 0.00) {
                msg += " Qty : " + String.format("%.2f", qty) + "\n";
            } else {
                msg += " Weight/Qty : " + String.format("%.2f", weight) + " kg" + "/" + String.format("%.2f", qty) + "\n";
            }

            // msg += " Weight/Qty : "+String.format("%.2f",weight)+" kg"+"/"+String.format("%.2f",qty)+"\n";
            msg += " Date Time : " + date + " " + time + "\n";
            // msg += " User      : "+UserName+"\n";
            msg += " Verified By: " + UserName + "\n";

            if (msg.length() > 0) {
                mService.write(ALIGN_LEFT);
                mService.write(SMALLFONT);
                mService.sendMessage(msg + "\n", "GBK");
            }

        }

        edttagno.setText("");
        edttagfrm.setText("");
        edttagto.setText("");
    }

    public void createJSON(){

        PIDtlId = PIDtlId;
        PIHdrId = PIHdrId;
        TAGNO = edttagno.getText().toString().trim();
       /* AuditedItemCode = edt_itemcode.getText().toString().trim();
        AuditedItemDesc = edt_description.getText().toString().trim();
        AuditedItemPlantId = AuditedItemPlantId;   //getnew items itemplantid
       // AuditedLocationCode = edt_location.getText().toString().trim();
        AuditedLocationCode = spinner_location.getSelectedItem().toString().trim();
        AuditedLocationMasterId = "";  //audited locn masterid
        AuditedWarehouseCode = edt_area.getText().toString().trim();
        AuditedWeight = edt_weight.getText().toString().trim();
        AuditedActualQty = edt_qty.getText().toString().trim();
        AuditedVerifyBy = edt_countedby.getText().toString().trim();*/

        /*{"PIDtlId":"DFDB7816-C210-43B8-8FD4-650344D845ED",
        "PIHdrId":"2B54E440-C844-45BA-B9B1-A0594C94484D",
        "ItemPlantId":"07cf072e-47c6-4ea8-9b78-4b11827d0c67",   //selected items ItemplantId
        "Location":"1",     //locationcode
        "Weight":"45",
        "ActualQty":"450.0",
        "AddedBy":"Ravindra Bhapkar",
        "Printed":"",
        "TagNo":00001,
        "Mode":"E",
        "AuditedDt":"2018-06-01"}

        {
  "PIDtlId": "347t53874567834657365093",
  "PIHdrId": "46537465079364879604360",
  "TagNo": "56346503475984375",
  "ItemPlantId": "73598746436436036",
  "Location": "LOC1",
  "Weight": "45.00",
  "ActualQty": "45.00",
  "AddedBy": "Ravindra Bhapkar",
  "Printed": "",
  "Mode": "E",
  "AuditedDt": "2019-12-07",
  "AuditedBy": "Ravindra Bhapkar",
  "AuditedLocation": "LOC2",
  "AuditedWeight": "30.00",
  "AuditedActualQty": "30:00",
  "AuditedItemPlantId": "7460219-019308244758"
}
*/

        JSONObject jAudit = new JSONObject();

        try{
            jAudit.put("PIDtlId",PIDtlId);
            jAudit.put("PIHdrId",PIHdrId);
            jAudit.put("ItemPlantId",OLDItemPlantId);
            jAudit.put("Location",OLDLocationMasterId);        //display LocationCode, Send LocationMasterId
            jAudit.put("Weight",OLDWeight);
            jAudit.put("ActualQty",OLDActualQty);
            jAudit.put("AddedBy",OLDVerifyBy);
            jAudit.put("Printed","");
            jAudit.put("TagNo",TAGNO);
            jAudit.put("Mode","E");
            jAudit.put("AuditedDt",AuditDATE);
            jAudit.put("AuditedItemPlantId",AuditedItemPlantId);
            jAudit.put("AuditedLocation",AuditedLocationMasterId);
            jAudit.put("AuditedWeight",AuditedWeight);
            jAudit.put("AuditedActualQty",AuditedActualQty);
            jAudit.put("AuditedBy",AuditedVerifyBy);

            //Edit
           /* jAudit.put("ItemCode",ItemCode);
            jAudit.put("ItemDesc",ItemDesc);
            jAudit.put("LocationMasterId",LocationMasterId);
            jAudit.put("WareHouseMasterId",WareHouseMasterId);*/

        }catch (Exception e){
            e.printStackTrace();
        }

        jMain = jAudit;

        if (isnet()) {
            new StartSession(ReprintScreenActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    //submit audit details to server
                    //  new PostAuditData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }else {
            Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg1) {
            switch (msg1.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg1.arg1) {
                        case BluetoothService.STATE_CONNECTED: // ÒÑÁ¬½Ó
                            Toast.makeText(ReprintScreenActivity.this, "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            deviceConnected = true;
                            break;
                        case BluetoothService.STATE_CONNECTING: // ÕýÔÚÁ¬½Ó
                            Log.d("À¶ÑÀµ÷ÊÔ", "ÕýÔÚÁ¬½Ó.....");
                            break;
                        case BluetoothService.STATE_LISTEN: // ¼àÌýÁ¬½ÓµÄµ½À´
                        case BluetoothService.STATE_NONE:
                            Log.d("À¶ÑÀµ÷ÊÔ", "µÈ´ýÁ¬½Ó.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST: // À¶ÑÀÒÑ¶Ï¿ªÁ¬½Ó
                    Toast.makeText(ReprintScreenActivity.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(ReprintScreenActivity.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    private void respMessage(String response) {
        JSONObject jobj = null;
        String Result = "";
        try {
            jobj = new JSONObject(response);
            Result = jobj.getString("Result");

            if(Result.equalsIgnoreCase("true")){
                Toast.makeText(parent,"Data submitted successfully",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(parent,"Data not submitted",Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    public String getUOMVAL(String OLDItemCode){
        String Pur_Unit = "", uomval = "";
        String qry = "SELECT PurUnit FROM "+db.TABLE_GetItemList+ " WHERE ItemCode='"+OLDItemCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            Pur_Unit = c.getString(c.getColumnIndex("PurUnit"));

            String query = "SELECT UOMCode from "+db.TABLE_UOM+" WHERE UOMMasterId='"+Pur_Unit+"'";
            Cursor c_ = sql.rawQuery(query,null);
            if(c_.getCount() > 0){
                c_.moveToFirst();
                uomval = c_.getString(c_.getColumnIndex("UOMCode"));
            }else {

            }
        }
        return uomval;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
            } else {
                Log.e("Scan", "Scanned");
                if(radioFlag.equalsIgnoreCase("SINGLE")){
                    edttagno.setText(result.getContents());
                }else if(radioFlag.equalsIgnoreCase("RANGE")){
                    if(barcodeFLAG.equalsIgnoreCase("FROM")){
                        edttagfrm.setText(result.getContents());
                        TAGNOFRM = edttagfrm.getText().toString().trim();

                    }else if(barcodeFLAG.equalsIgnoreCase("TO")){
                        edttagto.setText(result.getContents());
                        TAGNTO = edttagto.getText().toString().trim();
                    }
                }
            }
        }

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            BluetoothClass.pairPrinter(getApplicationContext(), ReprintScreenActivity.this);
        }else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            //String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            // BluetoothClass.pairedPrinterAddress(getApplicationContext(),ReprintScreenActivity.this,address);
            if (isConnected & (printPP_cpcl != null)) {
                printPP_cpcl.disconnect();
                isConnected = false;
            }

            String sdata = data.getExtras().getString(Devicelist_LablelPrint.EXTRA_DEVICE_ADDRESS);
            String address = sdata.substring(sdata.length() - 17);
            String name = sdata.substring(0, (sdata.length() - 17));

            if (!isConnected) {
                if(printPP_cpcl.connect(name,address)) {
                    isConnected = true;
                    //mTitle.setText(R.string.title_connected_to);
                    //mTitle.append(name);
                    Toast.makeText(this,"Connect Successful",Toast.LENGTH_SHORT).show();
                } else {
                    isConnected = false;
                }
            }
        }
    }

    /*label printer print class*/
    public abstract class PrintLabel {
        private Context context;
        private String itemcode;
        private String itemdesc;
        private String area;
        private String Location;
        private String qtyprint;
        private String wtprint;
        private String datetime;
        private String produce_company;
        private int barcode;
        private String desc;
        private String desc1;
        private String desc2;
        private String countedBy, verifyBy;

        public PrintLabel(Context context) {
            this.context = context;
        }

        public abstract void onReceived(boolean successed);


       /* public void label2(PrintPP_CPCL iPrinter, Bitmap rawBitmap) {
            int height = rawBitmap.getHeight();
            iPrinter.pageSetup(576, height + 70);
            iPrinter.drawGraphic2(0, 0, rawBitmap.getWidth(), rawBitmap.getHeight(), rawBitmap);
            iPrinter.print(0, 0);
        }*/

        public void label1(PrintPP_CPCL iPrinter, View view, ArrayList<BeanTag> tagPrintList) {

            for(int k = 0; k < tagPrintList.size() ; k++) {

                int x = 10;
                int y = 10;
                int space =40;
                //  initValue(tagPrintList);
                double weight = 0, qty = 0;

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                String yr = String.valueOf(year);

                if(yr.equals("2020")){
                    try{
                        Date today1 = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(today1);

                        calendar.add(Calendar.MONTH, 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.add(Calendar.DATE, -1);

                        Date lastDayOfMonth = calendar.getTime();
                        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        String a = sdf.format(lastDayOfMonth);

                        date = a;
                        DATE = date;

                    }catch (Exception e) {
                        e.printStackTrace();

                        date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                        DATE = date;
                    }
                }else {
                    date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                    DATE = date;
                }

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                time = updateTime(hour, minute);
                TIME = time;

                String billNO = tagPrintList.get(k).getTagNo_print();

                if(billNO.contains(".0")){
                    billNO =  billNO.replace(".0","");
                }

                billnumber = "0000" + billNO;
                //StringUtils.leftPad("129018", 10, "0");   //padding
                if (billnumber.length() == 5) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 5);
                } else if (billnumber.length() == 6) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 6);
                } else if (billnumber.length() == 7) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 7);
                } else if (billnumber.length() == 8) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 8);
                }else if (billnumber.length() == 9) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 9);
                }

                itemcode = tagPrintList.get(k).getItemCode_print().trim();
                itemdesc = tagPrintList.get(k).getItemDesc_print().trim();
                Location = OLDLocationCode;
                area = tagPrintList.get(k).getWarehouseCode_print().trim();
                weight = Double.parseDouble(tagPrintList.get(k).getWeight_print().trim());
                qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());
                // _countedByPrint = "Counted By : "+_countedByPrint;
                // _verifyByPrint = "Verify By : "+UserName;

                // itemcode = edt_itemcode.getText().toString().trim();
                // itemdesc = edt_description.getText().toString().trim();
                //countedBy = "Counted By : "+edt_countedby.getText().toString().trim();
                //verifyBy = "Verify By : "+edt_verifyby.getText().toString().trim();
                //area = edt_area.getText().toString().trim();
                //area = OLDWarehouseCode;

                if (CONV_factor == 1) {
                    weight = 0;
                } else if (CONV_factor > 1) {
                    weight = Double.parseDouble(tagPrintList.get(k).getWeight_print().trim());
                } else {
                    weight = 0;
                }

                int quantity = 0;
                if (CONV_factor == 1) {
                    qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());

                } else if (CONV_factor > 1) {
                    qty = Double.parseDouble(String.valueOf(weight * CONV_factor));
                } else {
                    qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());
                }

                //itemcode = "V5002891";
                //itemdesc = "(TILT METER) SUPPORT";
                // area= "Pune";
                Location = tagPrintList.get(k).getLocationCode_print_print().trim();
                qtyprint = String.valueOf(qty);
                wtprint = String.valueOf(weight);
                datetime = date + " " + time;
                produce_company = "Vritti";
                // billnumber= String.valueOf(billNO);

                /*if(tagPrintList.get(k).getWeight_print().trim().getText().toString().length() > 36){
                    desc = edt_tag_desc.getText().toString().trim().substring(36);
                    desc1 = edt_tag_desc.getText().toString().trim().substring(37,edt_tag_desc.getText().toString().length());

                }else if(edt_tag_desc.getText().toString().length() < 36){
                    desc = edt_tag_desc.getText().toString().trim();
                }*/


                /*****************************************************************************************/

                iPrinter.pageSetup(576, 480);

                iPrinter.drawLine(2, 0, 0, 574, 0, false);

                //iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.number_label), 2, 0, 0, false, false);
                iPrinter.drawText(x + 10, y, itemcode, 3, 0, 1, false, false);

                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.tag_desc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 2, y, itemdesc, 2, 0, 0, false, false);

                /*if (edt_tag_desc.getText().toString().length() > 36) {
                    iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
                    iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 2, y, desc1, 2, 0, 0, false, false);
                } else {
                    iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
                }*/

                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.area), 2, 0, 0, false, false);
                iPrinter.drawText(x + 80, y, area, 2, 0, 0, false, false);
                iPrinter.drawText(x + 190, y, getSringbyID(view, R.string.loc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 250, y, Location, 2, 0, 0, false, false);

                iPrinter.drawLine(2, 0, y += (space + 10), 574, y, false);

                if (CONV_factor == 1) {
                    //do not show weight
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);
                } else if (CONV_factor > 1) {
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);

                    iPrinter.drawText(x + 150, y, getSringbyID(view, R.string.weight), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 250, y, wtprint, 2, 0, 0, false, false);
                } else {
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);
                }

                // iPrinter.drawText(x, y += space, getSringbyID(view, R.string.produce_date), 2, 0, 0, false, false);
                // iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);

         /*   iPrinter.drawText(x + 280, y, 105, 80, getSringbyID(view, R.string.produce_company), 2, 0, 0, false, false);
            iPrinter.drawText(x + 370, y, 210, 80, produce_company, 2, 0, 0, false, false);
*/
                //  iPrinter.drawText(x, (y += space) + 35, getSringbyID(view, R.string.barcode), 3, 0, 1, false, false);

                iPrinter.drawText(x, (y += space) + 5, _countedByPrint, 2, 0, 0, false, false);
                iPrinter.drawText(x, (y += space) + 10, _verifyByPrint, 2, 0, 0, false, false);


                iPrinter.drawText(x, (y += space) + 10, billnumber, 3, 0, 1, false, false);
                iPrinter.drawBarCode(x + 120, y + 5, billnumber, 1, 0, 3, 56);
                iPrinter.drawBarCode(x + 120, y + 5, billnumber, 1, 0, 3, 56);

                iPrinter.drawText(x, (y += space) + 35, datetime, 2, 0, 0, false, false);

//            iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);
                //iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);

                iPrinter.drawLine(2, 0, y += (space + 50), 574, y, false);
                iPrinter.drawLine(2, 0, 0, 0, y, false);
                iPrinter.drawLine(2, 574, 0, 574, y, false);
                iPrinter.print(0, 0);

            }

            /*EditDeleteScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        billNoClass.setBill_no(billNO);
                        Log.e("Tag aftr print - ", String.valueOf(billNoClass.getBill_no()));

                        if(beanTagObj!= null ){
                            if(beanTagObj.getBeanTagArrayList() != null){
                                for(int i = 0 ; i<beanTagObj.getBeanTagArrayList().size() ;i++ ){
                                    if(batchCODE.equalsIgnoreCase(beanTagObj.getBeanTagArrayList().get(i).getBatchNo())){
                                        beanTagObj.getBeanTagArrayList().get(i).setTagNo(billNO);
                                    }
                                }
                            }
                        }
                        AppCommon.getInstance(PIEntryPrintingActivity.this).setBillNo_print( new Gson().toJson(beanTagObj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print().equals("")) {

                    } else {
                        billNoClass = new Gson().fromJson(AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print(), BillNoClass.class);
                        beanTagObj = new Gson().fromJson(AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print(), BeanTagList.class);
                        if(beanTagObj!= null ){
                            if(beanTagObj.getBeanTagArrayList() != null){
                                for(BeanTag beanTag : beanTagObj.getBeanTagArrayList() ){
                                    if(batchCODE.equalsIgnoreCase(beanTag.getBatchNo())){
                                        glBeanObject = beanTag;
                                        //    Toast.makeText(this,"Selected tag is "+glBeanObject.getTagNo(), Toast.LENGTH_SHORT).show();

                                        String  newTag = "0000" + String.valueOf(glBeanObject.getTagNo()+1);
                                        //StringUtils.leftPad("129018", 10, "0");   //padding
                                        if (newTag.length() == 5) {
                                            newTag = newTag.substring(newTag.length() - 5, 5);
                                        } else if (newTag.length() == 6) {
                                            newTag = newTag.substring(newTag.length() - 5, 6);
                                        } else if (newTag.length() == 7) {
                                            newTag = newTag.substring(newTag.length() - 5, 7);
                                        } else if (newTag.length() == 8) {
                                            newTag = newTag.substring(newTag.length() - 5, 8);
                                        }else if (newTag.length() == 9) {
                                            newTag = newTag.substring(newTag.length() - 5, 9);
                                        }
                                        txt_runtag.setText(newTag);
                                    }
                                }
                            }
                        }
                    }

                    addressLayout.setVisibility(View.VISIBLE);
                    edt_weight.setText("");
                    edt_weight.setEnabled(true);
                    edt_weight.setClickable(true);
                    edt_weight.setFocusable(true);
                    edt_qty.setText("");
                    edt_itemcode.setText("");
                    edt_description.setText("");
                    edt_area.setText("");
                }
            });*/

        }

        public void label1_singlePrint(PrintPP_CPCL iPrinter, View view, ArrayList<BeanTag> tagPrintList) {
            int x = 10;
            int y = 10;
            int space =40;

            for(int k = 0; k<tagPrintList.size(); k++) {
                //  initValue(tagPrintList);
                double weight = 0, qty = 0;

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                String yr = String.valueOf(year);

                if(yr.equals("2020")){
                    try{
                        Date today1 = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(today1);

                        calendar.add(Calendar.MONTH, 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.add(Calendar.DATE, -1);

                        Date lastDayOfMonth = calendar.getTime();
                        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        String a = sdf.format(lastDayOfMonth);

                        date = a;
                        DATE = date;

                    }catch (Exception e) {
                        e.printStackTrace();

                        date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                        DATE = date;
                    }
                }else {
                    date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                    DATE = date;
                }

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                time = updateTime(hour, minute);
                TIME = time;

                String billNO = tagPrintList.get(k).getTagNo_print();

                if(billNO.contains(".0")){
                    billNO =  billNO.replace(".0","");
                }

                billnumber = "0000" + billNO;
                //StringUtils.leftPad("129018", 10, "0");   //padding
                if (billnumber.length() == 5) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 5);
                } else if (billnumber.length() == 6) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 6);
                } else if (billnumber.length() == 7) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 7);
                } else if (billnumber.length() == 8) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 8);
                }else if (billnumber.length() == 9) {
                    billnumber = billnumber.substring(billnumber.length() - 5, 9);
                }

                itemcode = tagPrintList.get(k).getItemCode_print().trim();
                itemdesc = tagPrintList.get(k).getItemDesc_print().trim();
                Location = OLDLocationCode;
                OLDCountedBy = "Counted By : "+OLDCountedBy;
                UserName = "Verify By : "+UserName;
                area = tagPrintList.get(k).getWarehouseCode_print().trim();
                weight = Double.parseDouble(tagPrintList.get(k).getWeight_print().trim());
                qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());

                // itemcode = edt_itemcode.getText().toString().trim();
                // itemdesc = edt_description.getText().toString().trim();
                //countedBy = "Counted By : "+edt_countedby.getText().toString().trim();
                //verifyBy = "Verify By : "+edt_verifyby.getText().toString().trim();
                //area = edt_area.getText().toString().trim();
                //area = OLDWarehouseCode;

                if (CONV_factor == 1) {
                    weight = 0;
                } else if (CONV_factor > 1) {
                    weight = Double.parseDouble(tagPrintList.get(k).getWeight_print().trim());
                } else {
                    weight = 0;
                }

                int quantity = 0;
                if (CONV_factor == 1) {
                    qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());

                } else if (CONV_factor > 1) {
                    qty = Double.parseDouble(String.valueOf(weight * CONV_factor));
                } else {
                    qty = Double.parseDouble(tagPrintList.get(k).getActualQty_print().trim());
                }

                //itemcode = "V5002891";
                //itemdesc = "(TILT METER) SUPPORT";
                // area= "Pune";
                Location = tagPrintList.get(k).getLocationCode_print_print().trim();
                qtyprint = String.valueOf(qty);
                wtprint = String.valueOf(weight);
                datetime = date + " " + time;
                produce_company = "Vritti";
                // billnumber= String.valueOf(billNO);

                /*if(tagPrintList.get(k).getWeight_print().trim().getText().toString().length() > 36){
                    desc = edt_tag_desc.getText().toString().trim().substring(36);
                    desc1 = edt_tag_desc.getText().toString().trim().substring(37,edt_tag_desc.getText().toString().length());

                }else if(edt_tag_desc.getText().toString().length() < 36){
                    desc = edt_tag_desc.getText().toString().trim();
                }*/


                /*****************************************************************************************/

                iPrinter.pageSetup(576, 480);

                iPrinter.drawLine(2, 0, 0, 574, 0, false);

                //iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.number_label), 2, 0, 0, false, false);
                iPrinter.drawText(x + 10, y, itemcode, 3, 0, 1, false, false);

                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.tag_desc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 2, y, itemdesc, 2, 0, 0, false, false);

                if(!OLDTAGDescription.equalsIgnoreCase("") || !OLDTAGDescription.equalsIgnoreCase(null) ||
                        !OLDTAGDescription.equalsIgnoreCase("null")){
                    if (OLDTAGDescription.length() > 36) {
                        iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                        iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
                        iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                        iPrinter.drawText(x + 2, y, desc1, 2, 0, 0, false, false);
                    } else {
                        iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                        iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
                    }
                }else {
                    //do not print
                }

                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.area), 2, 0, 0, false, false);
                iPrinter.drawText(x + 80, y, area, 2, 0, 0, false, false);
                iPrinter.drawText(x + 190, y, getSringbyID(view, R.string.loc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 250, y, Location, 2, 0, 0, false, false);

                iPrinter.drawLine(2, 0, y += (space + 10), 574, y, false);

                if (CONV_factor == 1) {
                    //do not show weight
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);
                } else if (CONV_factor > 1) {
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);

                    iPrinter.drawText(x + 150, y, getSringbyID(view, R.string.weight), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 250, y, wtprint, 2, 0, 0, false, false);
                } else {
                    iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.qty), 2, 0, 0, false, false);
                    iPrinter.drawText(x + 50, y, qtyprint, 3, 0, 1, false, false);
                }

                // iPrinter.drawText(x, y += space, getSringbyID(view, R.string.produce_date), 2, 0, 0, false, false);
                // iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);

         /*   iPrinter.drawText(x + 280, y, 105, 80, getSringbyID(view, R.string.produce_company), 2, 0, 0, false, false);
            iPrinter.drawText(x + 370, y, 210, 80, produce_company, 2, 0, 0, false, false);
*/
                //  iPrinter.drawText(x, (y += space) + 35, getSringbyID(view, R.string.barcode), 3, 0, 1, false, false);

                iPrinter.drawText(x, (y += space) + 5, OLDCountedBy, 2, 0, 0, false, false);
                iPrinter.drawText(x, (y += space) + 10, UserName, 2, 0, 0, false, false);

                /*for tag no barcode at bottom*/
                iPrinter.drawText(x, (y += space) + 10, billnumber, 3, 0, 1, false, false);
                iPrinter.drawBarCode(x + 120, y + 5, billnumber, 1, 0, 3, 56);
                iPrinter.drawBarCode(x + 120, y + 5, billnumber, 1, 0, 3, 56);

                iPrinter.drawText(x, (y += space) + 35, datetime, 2, 0, 0, false, false);

//            iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);
                //iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);

                iPrinter.drawLine(2, 0, y += (space + 50), 574, y, false);
                iPrinter.drawLine(2, 0, 0, 0, y, false);
                iPrinter.drawLine(2, 574, 0, 574, y, false);
                iPrinter.print(0, 0);

            }

            /*EditDeleteScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        billNoClass.setBill_no(billNO);
                        Log.e("Tag aftr print - ", String.valueOf(billNoClass.getBill_no()));

                        if(beanTagObj!= null ){
                            if(beanTagObj.getBeanTagArrayList() != null){
                                for(int i = 0 ; i<beanTagObj.getBeanTagArrayList().size() ;i++ ){
                                    if(batchCODE.equalsIgnoreCase(beanTagObj.getBeanTagArrayList().get(i).getBatchNo())){
                                        beanTagObj.getBeanTagArrayList().get(i).setTagNo(billNO);
                                    }
                                }
                            }
                        }
                        AppCommon.getInstance(PIEntryPrintingActivity.this).setBillNo_print( new Gson().toJson(beanTagObj));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print().equals("")) {

                    } else {
                        billNoClass = new Gson().fromJson(AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print(), BillNoClass.class);
                        beanTagObj = new Gson().fromJson(AppCommon.getInstance(PIEntryPrintingActivity.this).getBillNo_print(), BeanTagList.class);
                        if(beanTagObj!= null ){
                            if(beanTagObj.getBeanTagArrayList() != null){
                                for(BeanTag beanTag : beanTagObj.getBeanTagArrayList() ){
                                    if(batchCODE.equalsIgnoreCase(beanTag.getBatchNo())){
                                        glBeanObject = beanTag;
                                        //    Toast.makeText(this,"Selected tag is "+glBeanObject.getTagNo(), Toast.LENGTH_SHORT).show();

                                        String  newTag = "0000" + String.valueOf(glBeanObject.getTagNo()+1);
                                        //StringUtils.leftPad("129018", 10, "0");   //padding
                                        if (newTag.length() == 5) {
                                            newTag = newTag.substring(newTag.length() - 5, 5);
                                        } else if (newTag.length() == 6) {
                                            newTag = newTag.substring(newTag.length() - 5, 6);
                                        } else if (newTag.length() == 7) {
                                            newTag = newTag.substring(newTag.length() - 5, 7);
                                        } else if (newTag.length() == 8) {
                                            newTag = newTag.substring(newTag.length() - 5, 8);
                                        }else if (newTag.length() == 9) {
                                            newTag = newTag.substring(newTag.length() - 5, 9);
                                        }
                                        txt_runtag.setText(newTag);
                                    }
                                }
                            }
                        }
                    }

                    addressLayout.setVisibility(View.VISIBLE);
                    edt_weight.setText("");
                    edt_weight.setEnabled(true);
                    edt_weight.setClickable(true);
                    edt_weight.setFocusable(true);
                    edt_qty.setText("");
                    edt_itemcode.setText("");
                    edt_description.setText("");
                    edt_area.setText("");
                }
            });*/

        }

        public void readThread(final PrintPP_CPCL iPrinter) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int times = 0;
                    boolean flag = true;
                    while (flag) {
                        byte[] temp = iPrinter.readPrintResult();
                        times++;
                        if (times >= 50) {
                            flag = false;
                            onReceived(false);
                            break;
                        }
                        if (temp != null) {
                            Log.e("get" + String.valueOf(times), String.valueOf(ByteArrToHex(temp)));
                            if (temp[0] == 0x4F) {
                                flag = false;
                                onReceived(true);
                                break;
                            }
                            if (temp[0] == 0x45) {
                                flag = false;
                                onReceived(false);
                                break;
                            }

                        }

                    }

                }
            }).start();
        }

        private void initValue(ArrayList<BeanTag> listToPrint) {

            //  getDataFromLocal();


            //  desc = edt_tag_desc.getText().toString().trim().substring(36);

        }

        private String getSringbyID(View view, int id) {
            return view.getContext().getResources().getString(id);
        }

        private String Byte2Hex(Byte inByte) {
            return String.format("%02x", inByte).toUpperCase();
        }

        private String ByteArrToHex(byte[] inBytArr) {
            StringBuilder strBuilder = new StringBuilder();
            int j = inBytArr.length;
            for (int i = 0; i < j; i++) {
                strBuilder.append(Byte2Hex(inBytArr[i]));
                strBuilder.append(" ");
            }
            return strBuilder.toString();
        }
    }

   /* @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled
        // setupChat() will then be called during onActivityRe//sultsetupChat

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }*/

    public void LTKPrint_single(final View viewforprint, final ArrayList<BeanTag>tagPrintList){
        if (!isSending) {
            pl = new PrintLabel(ReprintScreenActivity.this) {
                @Override
                public void onReceived(boolean successed) {
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSending = true;
                    if (isConnected) {
                        pl.label1_singlePrint(printPP_cpcl, viewforprint, tagPrintList);
                        pl.readThread(printPP_cpcl);
                    }

                    try {
                        interval = 0;
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isSending = false;
                }
            }).start();
        }
    }

    public void LTKPrint_range(final View viewforprint, final ArrayList<BeanTag>tagPrintList){
        if (!isSending) {
            pl = new PrintLabel(ReprintScreenActivity.this) {
                @Override
                public void onReceived(boolean successed) {
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSending = true;
                    if (isConnected) {
                        pl.label1(printPP_cpcl, viewforprint, tagPrintList);
                        pl.readThread(printPP_cpcl);
                    }

                    try {
                        interval = 0;
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isSending = false;
                }
            }).start();
        }
    }

    public void getPrintData(ArrayList<BeanTag> listToPrint){

        //  displayProduct();
      /*  int BILL_NO = 0;
        try{
            BILL_NO =  billNoClass.getBill_no();
        }catch (Exception e){
            e.printStackTrace();
            BILL_NO = 0;
        }*/

        // int billNO = BILL_NO + 1;

        for(int k =0; k<listToPrint.size(); k++) {
            //int billNO = Integer.parseInt(TAGNO);
            String billNO = listToPrint.get(k).getTagNo_print();

            if(billNO.contains(".0")){
                billNO =  billNO.replace(".0","");
            }

            billnumber = "0000" + billNO;
            //StringUtils.leftPad("129018", 10, "0");   //padding
            if (billnumber.length() == 5) {
                billnumber = billnumber.substring(billnumber.length() - 5, 5);
            } else if (billnumber.length() == 6) {
                billnumber = billnumber.substring(billnumber.length() - 5, 6);
            } else if (billnumber.length() == 7) {
                billnumber = billnumber.substring(billnumber.length() - 5, 7);
            } else if (billnumber.length() == 8) {
                billnumber = billnumber.substring(billnumber.length() - 5, 8);
            }else if (billnumber.length() == 9) {
                billnumber = billnumber.substring(billnumber.length() - 5, 9);
            }

            final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
            final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
            final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
            final byte[] SMALLFONT = {0x1b, 0x21, 0x01}; //small font
            final byte[] DEFAULT = {0x1b, 0x21, 0x00};
            final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text

            byte[] format = {27, 33, 0};
            byte[] arrayOfByte1 = {27, 33, 0};
            // Small
            format[2] = ((byte) (0x1 | arrayOfByte1[2]));

            String msg = null, company = "";
            String itemcode = "", itemdesc = "", dateTime = "", username = "", location = "", countedby = "", area = "";
            double weight = 0, qty = 0;

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            String yr = String.valueOf(year);

            if(yr.equals("2020")){
                try{
                    Date today1 = new Date();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today1);

                    calendar.add(Calendar.MONTH, 1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.add(Calendar.DATE, -1);

                    Date lastDayOfMonth = calendar.getTime();
                    DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                    String a = sdf.format(lastDayOfMonth);

                    date = a;
                    DATE = date;

                }catch (Exception e) {
                    e.printStackTrace();

                    date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                    DATE = date;
                }
            }else {
                date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                DATE = date;
            }

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            time = updateTime(hour, minute);
            TIME = time;

            itemcode = listToPrint.get(k).getItemCode_print().trim();
            itemdesc = listToPrint.get(k).getItemDesc_print().trim();
            location = listToPrint.get(k).getLocationCode_print_print().trim();
            area = listToPrint.get(k).getWarehouseCode_print().trim();
            weight = Double.parseDouble(listToPrint.get(k).getWeight_print().trim());
            qty = Double.parseDouble(listToPrint.get(k).getActualQty_print().trim());
            UOMVAL = listToPrint.get(k).getUomToPrint();

            if(radioFlag.equalsIgnoreCase("SINGLE")){
                countedby = OLDCountedBy;
            }else if(radioFlag.equalsIgnoreCase("RANGE")){
                countedby = getPrevCountedByName();
            }

            //Print text on label
            try {
                String productId = billnumber;

                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                Writer codeWriter;
                codeWriter = new Code128Writer();
                BitMatrix byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 300, 50, hintMap);
                int width1 = byteMatrix.getWidth();
                int height1 = byteMatrix.getHeight();
                Log.e("x ", String.valueOf(width1));
                Log.e("y ", String.valueOf(height1));
                Bitmap bitmap = Bitmap.createBitmap(width1, height1, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < width1; i++) {
                    for (int j = 0; j < height1; j++) {
                        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                /*mService.write(ALIGN_RIGHT);
                byte[] command = Utils_print.decodeBitmap(bitmap);
                mService.write(command);*/

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            msg = "" + itemcode + "            " + billnumber + "\n";

            if (itemdesc.length() > 40) {
                itemdesc = itemdesc.substring(0, 40);
            } else if (itemdesc.length() <= 40) {
                int diff = 40 - itemdesc.length();
                for (int i = 0; i < diff; i++) {
                    itemdesc += " ";
                }
            }

            msg += "" + itemdesc + "\n";
            // msg += " Loc/Area : " + location + "/" + area + "\n";
            msg += "Area/Loc   : " + area + "/" + location + "\n";

            if (weight == 0 || weight == 0.0 || weight == 0.00) {
                msg += "Qty        : " + String.format("%.2f", qty)+" "+UOMVAL+ "\n";
            } else {
                msg += "Weight/Qty : " + String.format("%.2f", weight) + " kg" + "/" + String.format("%.2f", qty) +" "+UOMVAL+ "\n";
            }

            // msg += " Weight/Qty : "+String.format("%.2f",weight)+" kg"+"/"+String.format("%.2f",qty)+"\n";
            //msg += "Date Time  : " + date + " " + time + "\n";
            msg += ""+date + " " + time + " "+countedby+ "\n";
            //msg += "Counted By : "+countedby+"\n";
            msg += "Verified By: " + UserName + "\n";

            /*if (msg.length() > 0) {
                mService.write(ALIGN_LEFT);
                mService.write(SMALLFONT);
                mService.sendMessage(msg + "\n", "GBK");
            }*/

            String wt = String.format("%.2f", weight) + " kg";

            //billnumber = "Tag:"+billnumber;
            String areloc = "Area/Loc: "+area+" / "+location;

            String cntBy =  "Counted By: "+countedby;
            String vrfyBy = "Verify By: "+UserName;
            String tagdesc = ""/*edt_tag_desc.getText().toString().trim()*/;

            //  dataClass_qsPrinter.printLabel_single(msg, billnumber,itemcode);    //data to print

            /*dataClass_qsPrinter.printLabel_customsize(billnumber, itemcode, itemdesc, area+"/"+location,
                    wt, String.format("%.2f", qty), date+" "+ time, countedby, UserName);    //data to print*/

            try{
                if(labelSize.equalsIgnoreCase("2x2mm")){
                        dataClass_qsPrinter.printLabel_single(msg, billnumber,itemcode);    //data to print
                }else if(labelSize.equalsIgnoreCase("3x2mm")){
                    if(selPrinterName.equalsIgnoreCase("")){
                        dataClass_qsPrinter.printLabel_customsize(billnumber, itemcode, itemdesc.trim(),tagdesc, areloc,
                                String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);
                    }else if(selPrinterName.contains("Qsprinter")){
                        dataClass_qsPrinter.printLabel_customsize(billnumber, itemcode, itemdesc.trim(),tagdesc, areloc,
                                String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);    //data to print

                    }else {
                        dataClass_qsPrinter.printLabel_customsize_HMPrinter(billnumber, itemcode, itemdesc.trim(),tagdesc, areloc,
                                String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);
                    }
                    //data to print
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        btnproceed.setEnabled(true);
        btnproceed.setText("PROCEED");
        btnproceed.setAlpha(1);

        edttagno.setText("");
        edttagfrm.setText("");
        edttagto.setText("");
    }

}
