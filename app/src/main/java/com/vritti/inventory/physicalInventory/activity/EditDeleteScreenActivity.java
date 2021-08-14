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
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beeprt.print.PrintPP_CPCL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.inventory.bean.PrintDataClass_QSPrinter;
import com.vritti.inventory.physicalInventory.adapter.LocationListAdapter;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.Constants_wifi;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.inventory.physicalInventory.bean.Util_Wifi_print;
import com.vritti.inventory.physicalInventory.bean.Utils_print;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class EditDeleteScreenActivity extends AppCompatActivity {
    private Context parent;
    AutoCompleteTextView edttagno;
    ImageView imgserch, img_barcode, imgdelete;
    LinearLayout addressLayout;
    ImageButton imgedtpartno, imgedtpartdesc, imgedtloc, imgedtarea, imgedtweight,imgedtqty, imgedtverifyby,imgedtcountedby;
    View sheetview;
    Button btncancel, btnsave, btnprint;
    LinearLayout dtlslay;
    RelativeLayout laytag;
    ScrollView scrollview;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount;
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    EditText edt_qty, edt_weight, edt_countedby, edt_area,edt_verifyby, edt_tag_desc;
    TextView edt_location, t4,txtuom;
    Spinner spinner_location;
    TextView edt_itemcode, edt_description;
    ArrayList<String> ItemCodelist;
    ArrayList<String> ItemDesclist;

    public static final int REQUEST_ENABLE_BT = 4;
    public static final int REQUEST_CONNECT_DEVICE = 6;
    public static final int REQ_PARTCODE = 7;
    public static final int REQ_PARTNAME = 8;
    public static final int REQ_LOCATION = 9;

    public static String today, todaysDate;
    public static String date = null;
    public static String time = null;
    String DATE = "", TIME = "";
    static int year, month, day;

    double CONV_factor = 1;
    String UOMVAL = "";
    String PIHdrId = "", PIDtlId = "", AuditedItemPlantId = "", AuditedItemCode = "", AuditedItemDesc = "", AuditedLocationCode = "",
            AuditedLocationMasterId = "", AuditedWeight = "", AuditedActualQty = "", AuditedVerifyBy = "", TAGNO = "",
            AuditedCountedBy = "", AuditedWareHouseMasterId = "", AuditedWarehouseCode = "", TAGDescription = "";

    String OLDItemPlantId = "", OLDItemCode = "", OLDItemDesc = "", OLDLocationCode = "",OLDLocationMasterId = "",
            OLDWeight = "", OLDActualQty = "", OLDVerifyBy = "", OLDWareHouseMasterId = "", OLDWarehouseCode = "", OLDCountedBy = "";

    String dateStr = "", AuditDATE = "";
    JSONObject jMain = null;

    ArrayList<LocationList>locationListArrayList;
    LocationListAdapter locationListAdapter;

    private SharedPreferences sharedPrefs;
    String labelSize = "",selPrinterName="";
    Gson gson;
    private String json;
    Type type;

    String callFrom = "", prevTAG = "";
    String Status="",BatchPrint="",uuidInString="";
    String BatchHdID="",LocationID="",LocationCode="";
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;

    private WifiConfiguration mPrinterConfiguration, mOldWifiConfiguration;

    PIEntryPrintingActivity.PrintLabel printLabel;
    PrintPP_CPCL printPP_cpcl;
    private PrintLabel pl;
    private int interval;
    View viewforprint;
    private boolean isSending = false;
    private boolean isConnected = false;
    private ProgressDialog dialog;
    private BluetoothAdapter mBluetoothAdapter;
    String WareHouseMasterId = "", warehousecode = "";

    PrintDataClass_QSPrinter dataClass_qsPrinter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        /*LTK Printer*/
        /*mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //If the Bluetooth adapter is not supported,programmer is over
        if (mBluetoothAdapter == null) {
            Toast.makeText(EditDeleteScreenActivity.this,
                    "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
       *//* Intent serverIntent = new Intent(EditDeleteScreenActivity.this, Devicelist_LablelPrint.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);*//*
        printPP_cpcl = new PrintPP_CPCL();*/

        dataClass_qsPrinter = new PrintDataClass_QSPrinter();

        setListeners();

        //by default addtextchange is called so write here by validating
        if(edt_weight.isEnabled()){
            //add textchange of edtweight
            edt_weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String edt = s.toString();

                    if( edt.equals("")){

                    }else {

                        if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
                            edt_qty.setText(String.format("%.2f",CONV_factor));
                            Log.e("edwt txtchnge- ",edt_qty.getText().toString());
                        }else if(CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
                            double wt = 0, qty = 0;
                            if(edt_weight.getText().toString().trim() == ""){
                                wt = 0;
                            }else {
                                wt = Double.parseDouble(edt_weight.getText().toString().trim());
                            }
                            qty = wt * CONV_factor;
                            edt_qty.setText(String.format("%.2f",qty));
                            Log.e("edwt addtxtchnge1- ",edt_qty.getText().toString());
                        }

                    }
                }
            });
        }
    }

    private void init() {
        parent =EditDeleteScreenActivity.this;

        edttagno = findViewById(R.id.edttagno);
        imgserch = findViewById(R.id.imgserch);
        img_barcode = findViewById(R.id.img_barcode);
        imgdelete = findViewById(R.id.imgdelete);
        dtlslay = findViewById(R.id.dtlslay);
        laytag = findViewById(R.id.laytag);

        btnsave = findViewById(R.id.btnsave);
        btnprint = findViewById(R.id.btnprint);
        btnprint.setVisibility(View.GONE);
        btncancel = findViewById(R.id.btncancel);

        imgedtpartno = findViewById(R.id.imgedtpartno);
        imgedtpartdesc = findViewById(R.id.imgedtpartdesc);
        imgedtloc = findViewById(R.id.imgedtpartloc);
        imgedtarea = findViewById(R.id.imgedtarea);
        imgedtarea.setEnabled(false);
        imgedtweight = findViewById(R.id.imgedtweight);
        imgedtqty = findViewById(R.id.imgedtqty);
        imgedtverifyby = findViewById(R.id.imgedtverifyby);
        imgedtcountedby = findViewById(R.id.imgedtcountedby);

        scrollview = findViewById(R.id.scrollview);
        scrollview.setVisibility(View.GONE);

        addressLayout = findViewById(R.id.addressLayout);
        edt_itemcode=findViewById(R.id.edt_itemcode);
        edt_itemcode.setEnabled(false);
        edt_description=findViewById(R.id.edt_description);
        edt_description.setEnabled(false);
        edt_qty=findViewById(R.id.edt_qty);
        edt_qty.setEnabled(false);
        edt_weight = findViewById(R.id.txt_weight);
        edt_weight.setEnabled(false);
        edt_location = findViewById(R.id.edt_location);
        edt_location.setEnabled(false);
        edt_countedby = findViewById(R.id.edt_countedby);
        edt_countedby.setEnabled(false);
        edt_verifyby = findViewById(R.id.edt_verifyby);
        edt_verifyby.setEnabled(false);
        edt_tag_desc = findViewById(R.id.edt_tag_desc);
        edt_area = findViewById(R.id.edt_area);
        edt_area.setEnabled(false);
        t4 = findViewById(R.id.t4);
        t4.setVisibility(View.GONE);
        txtuom = findViewById(R.id.txtuom);
       // spinner_location = findViewById(R.id.spinner_location);

        ut = new Utility();
        cf = new CommonFunction(EditDeleteScreenActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(EditDeleteScreenActivity.this);
        String dabasename = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(EditDeleteScreenActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(EditDeleteScreenActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        ItemCodelist = new ArrayList<String>();
        ItemDesclist = new ArrayList<String>();

        mService = new BluetoothService(EditDeleteScreenActivity.this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(EditDeleteScreenActivity.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        Date today = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        AuditDATE = curFormater.format(today);
        Log.e("AuditDATE : ",AuditDATE);

        Intent intent = getIntent();
        callFrom = intent.getStringExtra("CallFrom");
        prevTAG = intent.getStringExtra("PrevTAG");
        Status=intent.getStringExtra("status");
        BatchPrint=intent.getStringExtra("bathchprint");

        prevTAG = "0000" + prevTAG;
        //StringUtils.leftPad("129018", 10, "0");   //padding
        if (prevTAG.length() == 5) {
            prevTAG = prevTAG.substring(prevTAG.length() - 5, 5);
        } else if (prevTAG.length() == 6) {
            prevTAG = prevTAG.substring(prevTAG.length() - 5, 6);
        } else if (prevTAG.length() == 7) {
            prevTAG = prevTAG.substring(prevTAG.length() - 5, 7);
        } else if (prevTAG.length() == 8) {
            prevTAG = prevTAG.substring(prevTAG.length() - 5, 8);
        }else if (prevTAG.length() == 9) {
            prevTAG = prevTAG.substring(prevTAG.length() - 5, 9);
        }

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(EditDeleteScreenActivity.this);
        labelSize = sharedPrefs.getString("labelSize", "3x2mm");
        selPrinterName = sharedPrefs.getString("PrinterName", "");

        WareHouseMasterId = sharedPrefs.getString("WareHouseMasterId", "");
        warehousecode = sharedPrefs.getString("warehousecode", "");

        if(callFrom.equalsIgnoreCase("PIENTRY")){
            //directly hit call to api and display list
            TAGNO = prevTAG;
           // fetchFromServer(TAGNO);
            getPrevTagData(TAGNO);      //get data from local

        }else {

        }

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(EditDeleteScreenActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("location", "");
        type = new TypeToken<List<LocationList>>() {}.getType();
        locationListArrayList = gson.fromJson(json, type);

       /* if (locationListArrayList == null) {
            // downloadlocationdata();
        }else {
            if (locationListArrayList.size() > 0) {
                locationListAdapter = new LocationListAdapter(EditDeleteScreenActivity.this, locationListArrayList);
                spinner_location.setAdapter(locationListAdapter);
            }
        }*/
    }

    private void setListeners() {

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(EditDeleteScreenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete record
                callAPIDeleteRecord();
            }
        });

        edt_itemcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDeleteScreenActivity.this,PartCodeActivity.class);
                startActivityForResult(intent,REQ_PARTCODE);
            }
        });

        edt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDeleteScreenActivity.this, PartNameActivity.class);
                startActivityForResult(intent,REQ_PARTNAME);
            }
        });

        edt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDeleteScreenActivity.this, LocationPIActivity.class);
                startActivityForResult(intent,REQ_LOCATION);
            }
        });

        imgserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TAGNO = edttagno.getText().toString().trim();
                AuditedVerifyBy = UserName;

                if(TAGNO.equalsIgnoreCase("") || TAGNO.equalsIgnoreCase(null)){
                    Toast.makeText(parent,"Please enter tag number",Toast.LENGTH_SHORT).show();
                }else {
                    //call API to get Tag details
                    if (isnet()) {
                        new StartSession(EditDeleteScreenActivity.this, new CallbackInterface() {
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

               /* // slide-up animation
                Animation slideUp = AnimationUtils.loadAnimation(parent, R.anim.slide_up);
                if (scrollview.getVisibility() == View.GONE) {
                    scrollview.setVisibility(View.VISIBLE);
                    scrollview.startAnimation(slideUp);
                    scrollview.fullScroll(ScrollView.FOCUS_UP);
                }*/
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewforprint = v;
                createJSON(prevTAG);

            }
        });

        /*btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewforprint = v;

                String  newTag = "";

                mService = BluetoothClass.getServiceInstance();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                     newTag = "0000" + (String.valueOf(prevTAG));
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

                   // printReceipt(newTag);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //  printReceipt(billNO);

                        if (!isSending) {
                            pl = new PrintLabel(EditDeleteScreenActivity.this) {
                                @Override
                                public void onReceived(boolean successed) {
                                }
                            };

                            final String finalNewTag = newTag;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    isSending = true;
                                    if (isConnected) {
                                        pl.label1(printPP_cpcl, viewforprint, finalNewTag);
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

                }

            }
        });*/

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);

                if (scrollview.getVisibility() == View.VISIBLE) {
                    scrollview.setVisibility(View.GONE);
                    scrollview.startAnimation(slideDown);
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }

                edttagno.setText("");
                edt_itemcode.setText("");
                edt_description.setText("");
                edt_location.setText("");
                edt_area.setText("");
                edt_weight.setText("");
                edt_qty.setText("");
                edt_countedby.setText("");
                edt_verifyby.setText("");

                finish();
            }
        });

       /* spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AuditedLocationMasterId = locationListArrayList.get(position).getLocationMasterId();
                AuditedLocationCode = locationListArrayList.get(position).getLocationCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        imgedtpartno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_itemcode.setEnabled(true);
            }
        });

        imgedtpartdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_description.setEnabled(true);
            }
        });

        imgedtloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_location.setEnabled(true);
            }
        });

        imgedtarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_area.setEnabled(true);
            }
        });

        imgedtweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_weight.setEnabled(true);
                edt_weight.setFocusable(true);
                edt_weight.setPressed(true);
            }
        });

        imgedtqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_qty.setEnabled(true);
                edt_qty.setFocusable(true);
                edt_qty.setPressed(true);
            }
        });

        imgedtcountedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_countedby.setEnabled(true);
                edt_countedby.setFocusable(true);
            }
        });

        imgedtverifyby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_verifyby.setEnabled(true);
                edt_verifyby.setFocusable(true);
            }
        });
    }

    public void fetchFromServer(final String TAGNO){

        if(TAGNO.equalsIgnoreCase("") || TAGNO.equalsIgnoreCase(null)){
            Toast.makeText(parent,"Please enter tag number",Toast.LENGTH_SHORT).show();
        }else {
            //call API to get Tag details
            if (isnet()) {
                new StartSession(EditDeleteScreenActivity.this, new CallbackInterface() {
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
                //getPrevioustagdatafrom local
                getPrevTagData(TAGNO);
            }
        }
    }

    public void setDataToViews(String AuditedItemCode,String AuditedItemDesc, String AuditedLocationCode, String AuditedWarehouseCode,
                               String AuditedActualQty, String UserName, String AuditedCountedBy, String TAGDescription,
                               String AuditedWeight){

        edt_itemcode.setText(AuditedItemCode);
        edt_description.setText(AuditedItemDesc);
        edt_location.setText(AuditedLocationCode);
        edt_area.setText(AuditedWarehouseCode);
        edt_qty.setText(AuditedActualQty);
        edt_verifyby.setText(UserName);
        edt_countedby.setText(AuditedCountedBy);     //set countedpersons name
        edt_tag_desc.setText(TAGDescription);

        if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
            addressLayout.setVisibility(View.GONE);
            edt_weight.setText(AuditedWeight);
            t4.setVisibility(View.VISIBLE);
        }else {
            addressLayout.setVisibility(View.VISIBLE);
            edt_weight.setText(AuditedWeight);
            t4.setVisibility(View.GONE);
        }

        if(AuditedWeight.equalsIgnoreCase("0") || AuditedWeight.equalsIgnoreCase("0.0") ||
                AuditedWeight.equalsIgnoreCase("0.00")){
            addressLayout.setVisibility(View.GONE);
            t4.setVisibility(View.VISIBLE);
        }else {
            addressLayout.setVisibility(View.VISIBLE);
            edt_weight.setText(AuditedWeight);
            t4.setVisibility(View.GONE);
        }
        // slide-up animation
        Animation slideUp = AnimationUtils.loadAnimation(parent, R.anim.slide_up);
        if (scrollview.getVisibility() == View.GONE) {
            scrollview.setVisibility(View.VISIBLE);
            scrollview.startAnimation(slideUp);
            scrollview.fullScroll(ScrollView.FOCUS_UP);
        }

    }

    public void getPrevTagData(String prevTagNo){
        String qry = "SELECT * FROM "+db.TABLE_PI_GENERATION+" WHERE TagNo='"+prevTagNo+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                PIHdrId = c.getString(c.getColumnIndex("PIHdrId"));
                PIDtlId = c.getString(c.getColumnIndex("PIDtlId"));
                TAGNO = prevTAG;
                AuditedItemCode = c.getString(c.getColumnIndex("ItemCode"));
                AuditedItemDesc = c.getString(c.getColumnIndex("ItemDesc"));
                OLDItemPlantId = c.getString(c.getColumnIndex("ItemPlantId"));
                AuditedLocationCode = c.getString(c.getColumnIndex("LocationCode"));     //todisplay
                AuditedLocationMasterId = c.getString(c.getColumnIndex("LocationMasterID"));

                /*AuditedWareHouseMasterId =c.getString(c.getColumnIndex("WareHouseMasterId"));
                AuditedWarehouseCode = c.getString(c.getColumnIndex("WarehouseCode"));  //todisplay*/

                AuditedWareHouseMasterId =WareHouseMasterId;
                AuditedWarehouseCode = warehousecode;  //todisplay

                AuditedWeight =c.getString(c.getColumnIndex("Weight"));
                AuditedActualQty = c.getString(c.getColumnIndex("ActualQty"));
                AuditedCountedBy = c.getString(c.getColumnIndex("CountedBy"));
                TAGDescription = c.getString(c.getColumnIndex("TAGDescription"));
                // AuditedVerifyBy = jorder.getString("ActualQty");

                OLDItemCode = c.getString(c.getColumnIndex("ItemCode"));
                OLDItemDesc = c.getString(c.getColumnIndex("ItemDesc"));
                OLDItemPlantId = c.getString(c.getColumnIndex("ItemPlantId"));
                OLDLocationCode = c.getString(c.getColumnIndex("LocationCode"));     //todisplay
                OLDLocationMasterId = c.getString(c.getColumnIndex("LocationMasterID"));

                /*OLDWareHouseMasterId = c.getString(c.getColumnIndex("WareHouseMasterId"));
                OLDWarehouseCode = c.getString(c.getColumnIndex("WarehouseCode"));  //todisplay*/
                OLDWareHouseMasterId =WareHouseMasterId;
                OLDWarehouseCode = warehousecode;  //todisplay

                OLDWeight = c.getString(c.getColumnIndex("Weight"));
                OLDActualQty = c.getString(c.getColumnIndex("ActualQty"));
                OLDVerifyBy = UserName;
                OLDCountedBy = "";  //countedbyname

                try{
                    UOMVAL = getUOMVAL(OLDItemCode);
                    txtuom.setText(UOMVAL);
                }catch (Exception e){
                    e.printStackTrace();
                }

                CONV_factor = getConvFactor(OLDItemCode);
                Log.e("CF -",String.valueOf(CONV_factor));

                setDataToViews(AuditedItemCode,AuditedItemDesc,AuditedLocationCode,AuditedWarehouseCode,AuditedActualQty,
                        UserName,AuditedCountedBy,TAGDescription,AuditedWeight);

            }while (c.moveToNext());
        }else {
            if(prevTagNo.equalsIgnoreCase("00000")){
                Toast.makeText(parent,"No tag counted yet.",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(parent,"No tag available",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void callAPIDeleteRecord() {
        if (isnet()) {
            new StartSession(EditDeleteScreenActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DeleteTagDetails().execute(prevTAG);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }else {
            Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
        }

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

    private void GetItemCode(String itemcode) {
        //String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode like '%" + itemcode + "%'";
        String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode='"+itemcode+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                edt_weight.setText("");
                edt_qty.setText("");

                String itemcode1=cur.getString(cur.getColumnIndex("ItemDesc"));

                edt_description.setText(itemcode1);
                try{
                    String cnv = cur.getString(cur.getColumnIndex("ConvFactor"));
                    if(cnv.equalsIgnoreCase("null")){
                        CONV_factor = 0;
                    }else {
                        CONV_factor = Double.parseDouble(cnv);
                    }

                    if(CONV_factor == 1){
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        t4.setVisibility(View.VISIBLE);

                    }else if(CONV_factor > 1) {
                        edt_qty.setEnabled(false);
                        addressLayout.setVisibility(View.VISIBLE);
                        t4.setVisibility(View.GONE);

                    }else {
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        t4.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            } while (cur.moveToNext());
        }
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
                   /* progressDialog = new ProgressDialog(EditDeleteScreenActivity.this);
                    progressDialog.setMessage("Searching for Tag details Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);*/

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            TAGNO = params[0];
            String url = CompanyURL + WebUrlClass.api_TagDetails+"?TagNo="+TAGNO;  //RQry,from,to

            try {
                res = ut.OpenConnection(url,EditDeleteScreenActivity.this);
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
           // progressDialog.dismiss();
            getTagDetails(response);
        }
    }

    class DeleteTagDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                   /* progressDialog = new ProgressDialog(EditDeleteScreenActivity.this);
                    progressDialog.setMessage("Searching for Tag details Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);*/

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            //TAGNO = params[0];
            String url = null;  //RQry,from,to
            try {
                url = CompanyURL + WebUrlClass.api_DeleteTAG+"?PIDtlId="+PIDtlId+"&UserName="+ URLEncoder.encode(UserName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                res = ut.OpenConnection(url,EditDeleteScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                   // response = response.toString().replaceAll("\\\\", "");
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
            // progressDialog.dismiss();
            getMsgResp(response);
        }
    }

    private void getMsgResp(String response) {
        if(response.equalsIgnoreCase("True")){
            Toast.makeText(parent,"Tag deleted successfully",Toast.LENGTH_SHORT).show();

            //delete from local table to
            sql.delete(db.TABLE_PI_GENERATION, "PIDtlId=?", new String[]{PIDtlId});

            Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);
            if (scrollview.getVisibility() == View.VISIBLE) {
                scrollview.setVisibility(View.GONE);
                scrollview.startAnimation(slideDown);
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }

            edttagno.setText("");
            edt_itemcode.setText("");
            edt_description.setText("");
            edt_location.setText("");
            edt_area.setText("");
            edt_weight.setText("");
            edt_qty.setText("");
            edt_countedby.setText("");
            edt_verifyby.setText("");
            edt_tag_desc.setText("");

            finish();

        }else if(response.equalsIgnoreCase("False")){
            Toast.makeText(parent,"Unable to delete this record",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(parent,"Server Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void getTagDetails(String response) {
        if(response.equalsIgnoreCase("[]")){
            Toast.makeText(parent,"Sorry, No records found",Toast.LENGTH_SHORT).show();
            finish();
        }else if(response.equalsIgnoreCase("Already audited")){
            Toast.makeText(parent,"Sorry, Tag Already audited",Toast.LENGTH_SHORT).show();
            finish();
        }else if(response.equalsIgnoreCase("No Record Found")){
            Toast.makeText(parent,"Sorry, No records found",Toast.LENGTH_SHORT).show();
            finish();
        } else if (!response.equalsIgnoreCase(null)) {

            JSONArray jResults = null;
            try {
                jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    PIHdrId = jorder.getString("PIHdrId");
                    PIDtlId = jorder.getString("PIDtlId");
                    TAGNO = prevTAG;
                    AuditedItemCode = jorder.getString("ItemCode");
                    AuditedItemDesc = jorder.getString("ItemDesc");
                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    AuditedLocationCode = jorder.getString("LocationCode");     //todisplay
                    AuditedLocationMasterId = jorder.getString("LocationMasterId");
                    AuditedWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    AuditedWarehouseCode = jorder.getString("WarehouseCode");  //todisplay
                    AuditedWeight = jorder.getString("weight");
                    AuditedActualQty = jorder.getString("ActualQty");
                    AuditedCountedBy = jorder.getString("CountedBy");
                    TAGDescription = jorder.getString("TAGDescription");
                   // AuditedVerifyBy = jorder.getString("ActualQty");

                    OLDItemCode = jorder.getString("ItemCode");
                    OLDItemDesc = jorder.getString("ItemDesc");
                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    OLDLocationCode = jorder.getString("LocationCode");     //todisplay
                    OLDLocationMasterId = jorder.getString("LocationMasterId");
                    OLDWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    OLDWarehouseCode = jorder.getString("WarehouseCode");  //todisplay
                    OLDWeight = jorder.getString("weight");
                    OLDActualQty = jorder.getString("ActualQty");
                    OLDVerifyBy = UserName;
                    OLDCountedBy = "";  //countedbyname

                    try{
                       UOMVAL = getUOMVAL(OLDItemCode);
                       txtuom.setText(UOMVAL);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    CONV_factor = getConvFactor(OLDItemCode);
                    Log.e("CF -",String.valueOf(CONV_factor));
                }

                setDataToViews(AuditedItemCode,AuditedItemDesc,AuditedLocationCode,AuditedWarehouseCode,AuditedActualQty,
                        UserName,AuditedCountedBy,TAGDescription,AuditedWeight);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(response.equalsIgnoreCase("error")){
            Toast.makeText(parent,"Server error",Toast.LENGTH_SHORT).show();
        }
    }

    public double getConvFactor(String ItemCode){
        String qry = "SELECT ConvFactor FROM "+db.TABLE_GetItemList+ " WHERE ItemCode='"+ItemCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            CONV_factor = c.getDouble(c.getColumnIndex("ConvFactor"));
        }else {

        }

        return CONV_factor;
    }

    public String locationMasterId(){
        OLDLocationMasterId = locationListArrayList.get(spinner_location.getSelectedItemPosition()).getLocationMasterId();
        return OLDLocationMasterId;
    }

    public void createJSON(String prevTAG){

        PIDtlId = PIDtlId;
        PIHdrId = PIHdrId;
        //TAGNO = edttagno.getText().toString().trim();
        TAGNO = prevTAG;
        OLDItemCode = edt_itemcode.getText().toString().trim();
        OLDItemDesc = edt_description.getText().toString().trim();
        OLDItemPlantId = OLDItemPlantId;   //getnew items itemplantid
        //OLDLocationCode = spinner_location.getSelectedItem().toString().trim();
        OLDLocationCode = edt_location.getText().toString().trim();
       // OLDLocationMasterId = locationMasterId();  //audited locn masterid if spinner set
        OLDLocationMasterId = OLDLocationMasterId;
        OLDWarehouseCode = edt_area.getText().toString().trim();
        OLDWeight = edt_weight.getText().toString().trim();
        OLDActualQty = edt_qty.getText().toString().trim();
        OLDVerifyBy = edt_verifyby.getText().toString().trim();
        OLDCountedBy = edt_countedby.getText().toString().trim();
        TAGDescription = edt_tag_desc.getText().toString();

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
            jAudit.put("TagNo",prevTAG);
            jAudit.put("Mode","E");
            jAudit.put("AuditedDt",AuditDATE);
            jAudit.put("AuditedItemPlantId","");
            jAudit.put("AuditedLocation","");
            jAudit.put("AuditedWeight","0.0");
            jAudit.put("AuditedActualQty","0.0");
            jAudit.put("AuditedBy",UserName);
            jAudit.put("CountedBy",AuditedCountedBy);
            jAudit.put("TAGDescription",TAGDescription);

            //Edit
           /* jAudit.put("ItemCode",ItemCode);
            jAudit.put("ItemDesc",ItemDesc);
            jAudit.put("LocationMasterId",LocationMasterId);
            jAudit.put("WareHouseMasterId",WareHouseMasterId);*/

        }catch (Exception e){
            e.printStackTrace();
        }

        jMain = jAudit;

        //update PI data of selected pidtldid, tagno

     //   updatePrevTag(prevTAG);
        sql.delete(db.TABLE_PI_GENERATION, "PIHdrId=?",new String[]{PIHdrId});
        cf.insertPIData(jMain, UserName,edt_itemcode.getText().toString().trim(),
                edt_description.getText().toString().trim(),edt_location.getText().toString().trim());

        //same logic as online offline, check online/offline and update PITable and call API
         if (Status.equalsIgnoreCase("Offline")) {
            setoffline(jMain.toString());
        }else {
             if (isnet()) {
                 new StartSession(EditDeleteScreenActivity.this, new CallbackInterface() {
                     @Override
                     public void callMethod() {
                         //submit edit details to server
                         new PostAuditData().execute();
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

  /*  public void updatePrevTag(String prevTAG){
        sql.delete(db.TABLE_PI_GENERATION, "PIHdrId=?",new String[]{PIHdrId});
        cf.insertPIData(jMain, UserName,edt_itemcode.getText().toString().trim(),
                edt_description.getText().toString().trim(),edt_location.getText().toString().trim());
    }*/

    class PostAuditData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(EditDeleteScreenActivity.this);
                    progressDialog.setMessage("Submitting data please wait...");
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
            String FinalObj = jMain.toString();

            String url = CompanyURL + WebUrlClass.api_PostPIdetail;

            try {
                res = ut.OpenPostConnection(url,FinalObj,EditDeleteScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);

                   /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                   */
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
            respMessage(response);
        }
    }

    private void respMessage(String response) {
        JSONObject jobj = null;
        String Result = "";

        if(response != null){
            try {
                jobj = new JSONObject(response);
                Result = jobj.getString("Result");

                //Result = response.trim();

                if(Result.equalsIgnoreCase("true")){
                    Toast.makeText(parent,"Data submitted successfully",Toast.LENGTH_SHORT).show();

                   /* if(BluetoothClass.isPrinterConnected(getApplicationContext(), EditDeleteScreenActivity.this)) {
                        mService = BluetoothClass.getServiceInstance();*/
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            //printReceipt(String.valueOf(prevTAG));  //teklogic printer
                            //LTKPrint(prevTAG, viewforprint);         //LTK Printer
                            getPrintData(String.valueOf(prevTAG));           //QSPrinter

                        }

                   /* }else {
                        BluetoothClass.connectPrinter(getApplicationContext(), EditDeleteScreenActivity.this);
                    }*/

                    /*btnsave.setVisibility(View.GONE);
                    btnprint.setVisibility(View.GONE);*/

                }else if(Result.equalsIgnoreCase("false")) {
                    Toast.makeText(parent,"Data not submitted",Toast.LENGTH_SHORT).show();
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(parent,"Server Error",Toast.LENGTH_SHORT).show();
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

    private void setoffline(String FinalObj) {

        if (BatchPrint.equalsIgnoreCase("Immediate")||BatchPrint.equalsIgnoreCase("Batch")) {

            //update data to PI table

            String remark = "Record save successfully-" + prevTAG;
            String url = CompanyURL + WebUrlClass.api_PostPIdetail;

            String op = "true";

            CreateOfflinePrintOfflineActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //  printReceipt(billNO);       //teklogic printer
                //LTKPrint(billNO, viewforprint);       //LTKPrinter
                getPrintData(prevTAG);           //QSPrinter
            }


        }else {
            // Toast.makeText(this,"Batchprint key is empty",Toast.LENGTH_SHORT).show();
           /* piEntryData.setPIDtlId(uuidInString);
            piEntryData.setPIHdrId(BatchHdID);
            piEntryData.setItemPlantId(ItemPlantId);
            piEntryData.setLocation(LocationID);
            piEntryData.setWeight(edt_weight.getText().toString());
            piEntryData.setActualQty(edt_qty.getText().toString());
            piEntryData.setAddedBy(UserName);
            piEntryData.setPrinted("N");
            piEntryData.setTagNo(billnumber);
            piEntryData.setMode("A");
            piEntryData.setFlag("No");

            cf.AddPiEntryDetails(piEntryData,uuidInString);*/
        }

    }

    private void CreateOfflinePrintOfflineActivity(final String url, final String parameter,
                                                   final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Successfully, will update after getting network!", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);

        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(EditDeleteScreenActivity.this, "Connect successful",
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
                    Toast.makeText(EditDeleteScreenActivity.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(EditDeleteScreenActivity.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

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

    public void printReceipt(String previoustag){
        //getDataFromLocal();

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
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
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

        itemcode = edt_itemcode.getText().toString().trim();
        itemdesc = edt_description.getText().toString().trim();
        countedby = edt_countedby.getText().toString().trim();
        verifyby = edt_verifyby.getText().toString().trim();
        area = OLDWarehouseCode;

        if (CONV_factor == 1) {
            weight = 0;
        } else if (CONV_factor > 1) {
            weight = Double.parseDouble(edt_weight.getText().toString().trim());
        } else {
            weight = 0;
        }

        int quantity = 0;
        if (CONV_factor == 1) {
            qty = Double.parseDouble(edt_qty.getText().toString().trim());

        } else if (CONV_factor > 1) {
            qty = Double.parseDouble(String.valueOf(weight * CONV_factor));
        } else {
            qty = Double.parseDouble(edt_qty.getText().toString());
        }

        //Print text on label
        try {
            String productId = String.valueOf(previoustag);

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
        msg = " " + itemcode + "               " + previoustag + "\n";
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
        //msg += " Loc/Area : " + LocationCode + "/" + area + "\n";
        msg += " Area/Loc : " + area + "/" + LocationCode + "\n";

        if (CONV_factor == 1) {
            //do not show weight
            msg += " Qty  : " + String.format("%.2f",qty) + "\n";
        } else if (CONV_factor > 1) {
            msg += " Weight/Qty : " + String.format("%.2f", weight) + " kg" + "/" + String.format("%.2f",qty) + "\n";
            // msg += "Qty       : "+qty+"\n";
        } else {
            msg += " Qty       : " + String.format("%.2f",qty) + "\n";
        }

        // msg += "Qty       : "+qty+"\n";
        msg += " Date Time : " + date + " " + time + "\n";
        msg += " Counted By : " + edt_countedby.getText().toString().trim() + "\n";
        msg += " Verified By: " + UserName + "\n";

        try{
            if (msg.length() > 0) {
                mService.write(ALIGN_LEFT);
                mService.write(SMALLFONT);
                mService.sendMessage(msg + "\n", "GBK");
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(parent,"Unable to print",Toast.LENGTH_SHORT).show();
        }

        Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);
        if (scrollview.getVisibility() == View.VISIBLE) {
            scrollview.setVisibility(View.GONE);
            scrollview.startAnimation(slideDown);
            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }

        edttagno.setText("");
        edt_itemcode.setText("");
        edt_description.setText("");
        edt_location.setText("");
        edt_area.setText("");
        edt_weight.setText("");
        edt_qty.setText("");
        edt_countedby.setText("");
        edt_verifyby.setText("");
        edt_tag_desc.setText("");

    }

    public void getPrintData(String previoustag){
        //getDataFromLocal();

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
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
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

        itemcode = edt_itemcode.getText().toString().trim();
        itemdesc = edt_description.getText().toString().trim();
        countedby = edt_countedby.getText().toString().trim();
        verifyby = edt_verifyby.getText().toString().trim();
        area = OLDWarehouseCode;
        LocationCode = OLDLocationCode;

        if (CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000) {
            weight = 0;
        } else if (CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
            weight = Double.parseDouble(edt_weight.getText().toString().trim());
        } else {
            weight = 0;
        }

        int quantity = 0;
        if (CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000) {
            qty = Double.parseDouble(edt_qty.getText().toString().trim());
        } else if (CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
            qty = Double.parseDouble(String.valueOf(weight * CONV_factor));
        } else {
            qty = Double.parseDouble(edt_qty.getText().toString());
           // float q1 = Float.parseFloat(edt_qty.getText().toString().trim());
        }

        //Print text on label
        try {
            String productId = previoustag;

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

        msg = "" + itemcode + "            " + previoustag + "\n";

        if (itemdesc.length() > 40) {
            itemdesc = itemdesc.substring(0, 40);
        } else if (itemdesc.length() <= 40) {
            int diff = 40 - itemdesc.length();
            for (int i = 0; i < diff; i++) {
                itemdesc += " ";
            }
        }

            msg += "" + itemdesc + "\n";
            msg += "Area/Loc   : " + area + "/" + LocationCode + "\n";

        if (CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000) {
            //do not show weight
            msg += "Qty        : " + String.format("%.2f",qty)+" "+UOMVAL+  "\n";
        } else if (CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
            msg += "Weight/Qty : " + String.format("%.2f", weight) + " kg" + "/" + String.format("%.2f",qty) +" "+UOMVAL+ "\n";
        } else {
            msg += "Qty        : " + String.format("%.2f",qty) +" "+UOMVAL+ "\n";
        }

            //msg += "Date Time  : " + date + " " + time + "\n";
            msg += ""+date + " " + time + " "+edt_countedby.getText().toString().trim()+ "\n";
            //msg += "Counted By : " + edt_countedby.getText().toString().trim() + "\n";
            msg += "Verified By: " + UserName + "\n";

        if(!edt_tag_desc.getText().toString().trim().equalsIgnoreCase("") ||
                !edt_tag_desc.getText().toString().trim().equalsIgnoreCase(null) ){
            msg += "Tag Desc   :" + edt_tag_desc.getText().toString().trim() + "\n";
        }else {

        }

       /* try{
            if (msg.length() > 0) {
                mService.write(ALIGN_LEFT);
                mService.write(SMALLFONT);
                mService.sendMessage(msg + "\n", "GBK");
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(parent,"Unable to print",Toast.LENGTH_SHORT).show();
        }*/

        //previoustag = "Tag : "+previoustag;
        String areloc = "Area/Loc: "+area+" / "+LocationCode;

        String cntBy =  "Counted By: "+countedby;
        String vrfyBy = "Verify By: "+UserName;
        String tagdesc = edt_tag_desc.getText().toString().trim();

        try{
            //dataClass_qsPrinter.printLabel_single(msg, previoustag,itemcode);    //data to print
            if(labelSize.equalsIgnoreCase("2x2mm")){
                dataClass_qsPrinter.printLabel_single(msg, previoustag,itemcode);    //data to print
            }else if(labelSize.equalsIgnoreCase("3x2mm")){

                if(selPrinterName.equalsIgnoreCase("")){
                    dataClass_qsPrinter.printLabel_customsize(previoustag, itemcode, itemdesc.trim(),tagdesc, areloc,
                            String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);    //data to print
                }else if(selPrinterName.contains("Qsprinter")){
                    dataClass_qsPrinter.printLabel_customsize(previoustag, itemcode, itemdesc.trim(),tagdesc, areloc,
                            String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);    //data to print
                }else {
                    dataClass_qsPrinter.printLabel_customsize_HMPrinter(previoustag, itemcode, itemdesc.trim(),tagdesc, areloc,
                            String.format("%.2f", weight), String.format("%.2f", qty)+" "+UOMVAL, date+" "+ time, cntBy, vrfyBy);    //data to print
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);
        if (scrollview.getVisibility() == View.VISIBLE) {
            scrollview.setVisibility(View.GONE);
            scrollview.startAnimation(slideDown);
            scrollview.fullScroll(ScrollView.FOCUS_DOWN);
        }

        edttagno.setText("");
        edt_itemcode.setText("");
        edt_description.setText("");
        edt_location.setText("");
        edt_area.setText("");
        edt_weight.setText("");
        edt_qty.setText("");
        edt_countedby.setText("");
        edt_verifyby.setText("");
        edt_tag_desc.setText("");
        txtuom.setText("");

        finish();

    }

    /*private void respMessage(String response) {
        JSONObject jobj = null;
        String Result = "";
        //  try {
        // jobj = new JSONObject(response);
        //  Result = jobj.getString("Result");
        if(response != null){
            Result = response.trim();

            if(Result.equalsIgnoreCase("true")){
                Toast.makeText(parent,"Data submitted successfully",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(parent,"Data not submitted",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(parent,"Server Error",Toast.LENGTH_SHORT).show();
        }

        *//*} catch (JSONException e) {
            e.printStackTrace();
        }*//*

    }*/

    public boolean validate(){
        boolean val = false;

        if(edt_itemcode.getText().toString().equalsIgnoreCase("")
                && edt_description.getText().toString().equalsIgnoreCase("")
                && edt_location.getText().toString().equalsIgnoreCase("")
                && edt_area.getText().toString().equalsIgnoreCase("")
                && edt_weight.getText().toString().equalsIgnoreCase("")
                && edt_qty.getText().toString().equalsIgnoreCase("")
                && edt_countedby.getText().toString().equalsIgnoreCase("")
                && edt_verifyby.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(EditDeleteScreenActivity.this,"Please fill all details",Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(CONV_factor > 1){
            if(edt_weight.getText().toString().equalsIgnoreCase("") ||
                    edt_weight.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(EditDeleteScreenActivity.this,"Please enter weight",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(EditDeleteScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(EditDeleteScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }else if(CONV_factor == 1){
            if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(EditDeleteScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(EditDeleteScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }else {
            if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(EditDeleteScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(EditDeleteScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if (edt_location.getText().toString().equalsIgnoreCase("") ||
                    edt_location.getText().toString().equalsIgnoreCase(null)) {
                Toast.makeText(EditDeleteScreenActivity.this, "Please select location", Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //scan barcode
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
            } else {
                Log.e("Scan", "Scanned");
                 edttagno.setText(result.getContents());
                 TAGNO = result.getContents();
                 //GetItemCode(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            BluetoothClass.pairPrinter(getApplicationContext(),EditDeleteScreenActivity.this);
        }else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
           //String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            //BluetoothClass.pairedPrinterAddress(getApplicationContext(),EditDeleteScreenActivity.this,address);

           /* if (isConnected & (printPP_cpcl != null)) {
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
            }*/

        }else if (requestCode == Constants_wifi.REQUEST_CODE_PRINTER && resultCode == Constants_wifi.RESULT_CODE_PRINTER) {
            mPrinterConfiguration = Util_Wifi_print.getWifiConfiguration(EditDeleteScreenActivity.this, Constants_wifi.CONTROLLER_PRINTER);
           // doPrint();
        }else if(requestCode == REQ_PARTCODE && resultCode == REQ_PARTCODE){
            edt_itemcode.setText(data.getStringExtra("PartCode"));
            edt_description.setText(data.getStringExtra("PartName"));
            OLDItemPlantId = data.getStringExtra("ItemPlantId");
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            LocationID = data.getStringExtra("LocationMasterID");
            LocationCode = data.getStringExtra("LocationCode");
            OLDLocationCode = LocationCode;
            OLDLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
            UOMVAL = data.getStringExtra("uomval");
            txtuom.setText(UOMVAL);
            //Toast.makeText(this, "getting back frm ocr",Toast.LENGTH_SHORT).show();

            if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                addressLayout.setAlpha((float) 0.3);
                edt_weight.setEnabled(false);
                edt_weight.setFocusable(false);
                edt_weight.setClickable(false);
                t4.setVisibility(View.VISIBLE);

            }else if(CONV_factor > 1 ||  CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
                edt_qty.setEnabled(false);
                addressLayout.setVisibility(View.VISIBLE);
                addressLayout.setAlpha(1);
                edt_weight.setEnabled(true);
                edt_weight.setFocusable(true);
                edt_weight.setClickable(true);
                t4.setVisibility(View.GONE);
            }else {
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                t4.setVisibility(View.VISIBLE);
            }

        }else if(requestCode == REQ_PARTNAME && resultCode == REQ_PARTNAME){
            edt_itemcode.setText(data.getStringExtra("PartCode"));
            edt_description.setText(data.getStringExtra("PartName"));
            OLDItemPlantId = data.getStringExtra("ItemPlantId");
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            LocationID = data.getStringExtra("LocationMasterID");
            LocationCode = data.getStringExtra("LocationCode");
            OLDLocationCode = LocationCode;
            OLDLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
            UOMVAL = data.getStringExtra("uomval");
            txtuom.setText(UOMVAL);

            if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                edt_weight.setEnabled(false);
                edt_weight.setFocusable(false);
                edt_weight.setClickable(false);
                t4.setVisibility(View.VISIBLE);
            }else if(CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
                edt_qty.setEnabled(false);
                addressLayout.setVisibility(View.VISIBLE);
                edt_weight.setEnabled(true);
                edt_weight.setFocusable(true);
                edt_weight.setClickable(true);
                t4.setVisibility(View.GONE);
            }
        }else if(requestCode == REQ_LOCATION && resultCode == REQ_LOCATION){
            LocationCode = data.getStringExtra("LocationCode");
            LocationID = data.getStringExtra("LocationMasterID");
            OLDLocationCode = LocationCode;
            OLDLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
        }
    }

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

        public void label1(PrintPP_CPCL iPrinter, View view, String billNO) {
            int x = 10;
            int y = 10;
            int space =40;
            initValue(billNO);

            iPrinter.pageSetup(576, 480);

            iPrinter.drawLine(2, 0, 0, 574, 0, false);

            //iPrinter.drawText(x, y += 10, getSringbyID(view, R.string.number_label), 2, 0, 0, false, false);
            iPrinter.drawText(x + 10, y, itemcode, 3, 0, 1, false, false);

            iPrinter.drawText(x, y += space, getSringbyID(view, R.string.tag_desc), 2, 0, 0, false, false);
            iPrinter.drawText(x + 2, y,  itemdesc, 2, 0, 0, false, false);


            if(edt_tag_desc.getText().toString().length() > 36){
                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 2, y, desc1, 2, 0, 0, false, false);
            }else {
                iPrinter.drawText(x, y += space, getSringbyID(view, R.string.desc), 2, 0, 0, false, false);
                iPrinter.drawText(x + 2, y, desc, 2, 0, 0, false, false);
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

            iPrinter.drawText(x, (y += space) + 5, countedBy, 2, 0, 0, false, false);
            iPrinter.drawText(x, (y += space) + 10, verifyBy, 2, 0, 0, false, false);

            iPrinter.drawText(x, (y += space) + 10, prevTAG, 3, 0, 1, false, false);
            iPrinter.drawBarCode(x + 120, y + 5, prevTAG, 1, 0, 3, 56);
            iPrinter.drawBarCode(x + 120, y + 5,prevTAG, 1, 0, 3, 56);

            iPrinter.drawText(x, (y += space) +35, datetime, 2, 0, 0, false, false);

            //iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);
            //iPrinter.drawText(x + 150, y, produce_date, 2, 0, 0, false, false);

            iPrinter.drawLine(2, 0, y += (space + 50), 574, y, false);
            iPrinter.drawLine(2, 0, 0, 0, y, false);
            iPrinter.drawLine(2, 574, 0, 574, y, false);
            iPrinter.print(0, 0);

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

        private void initValue(String billNO) {

          //  getDataFromLocal();

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

            itemcode = edt_itemcode.getText().toString().trim();
            itemdesc = edt_description.getText().toString().trim();
            countedBy = "Counted By : "+edt_countedby.getText().toString().trim();
            verifyBy = "Verify By : "+edt_verifyby.getText().toString().trim();
            // area = edt_area.getText().toString().trim();
            area = OLDWarehouseCode;

            if (CONV_factor == 1) {
                weight = 0;
            } else if (CONV_factor > 1) {
                weight = Double.parseDouble(edt_weight.getText().toString().trim());
            } else {
                weight = 0;
            }

            int quantity = 0;
            if (CONV_factor == 1) {
                qty = Double.parseDouble(edt_qty.getText().toString().trim());

            } else if (CONV_factor > 1) {
                qty = Double.parseDouble(String.valueOf(weight * CONV_factor));
            } else {
                qty = Double.parseDouble(edt_qty.getText().toString().trim());
            }

            //itemcode = "V5002891";
            //itemdesc = "(TILT METER) SUPPORT";
            // area= "Pune";
            Location = LocationCode;
            qtyprint = String.valueOf(qty);
            wtprint = String.valueOf(weight);
            datetime = date + " " + time;
            produce_company = "Vritti";
            // billnumber= String.valueOf(billNO);

            prevTAG = "0000" + prevTAG;
            //StringUtils.leftPad("129018", 10, "0");   //padding
            if (prevTAG.length() == 5) {
                prevTAG = prevTAG.substring(prevTAG.length() - 5, 5);
            } else if (prevTAG.length() == 6) {
                prevTAG = prevTAG.substring(prevTAG.length() - 5, 6);
            } else if (prevTAG.length() == 7) {
                prevTAG = prevTAG.substring(prevTAG.length() - 5, 7);
            } else if (prevTAG.length() == 8) {
                prevTAG = prevTAG.substring(prevTAG.length() - 5, 8);
            }else if (prevTAG.length() == 9) {
                prevTAG = prevTAG.substring(prevTAG.length() - 5, 9);
            }

            if(edt_tag_desc.getText().toString().length() > 36){
                desc = edt_tag_desc.getText().toString().trim().substring(36);
                desc1 = edt_tag_desc.getText().toString().trim().substring(37,edt_tag_desc.getText().toString().length());

            }else if(edt_tag_desc.getText().toString().length() < 36){
                desc = edt_tag_desc.getText().toString().trim();
            }
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

        *//*if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }*//*
    }*/

    public void LTKPrint(int billNO, final View viewforprint){
        if (!isSending) {
            pl = new PrintLabel(EditDeleteScreenActivity.this) {
                @Override
                public void onReceived(boolean successed) {
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSending = true;
                    if (isConnected) {
                        pl.label1(printPP_cpcl, viewforprint, prevTAG);
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

}
