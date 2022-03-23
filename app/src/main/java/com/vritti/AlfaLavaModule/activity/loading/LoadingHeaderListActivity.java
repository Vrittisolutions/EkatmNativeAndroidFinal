package com.vritti.AlfaLavaModule.activity.loading;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.AlfaLavaModule.activity.CreateSecondaryPackActivity;
import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.cartonlabel.Adp_CartonLabelListData;
import com.vritti.AlfaLavaModule.activity.packing_qc.QCPackingCartonDetailActivity;
import com.vritti.AlfaLavaModule.activity.pick_riversal.PickPacketScanDetails;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.activity.unpacking.UnPackingCartonDetailActivity;
import com.vritti.AlfaLavaModule.bean.CartonData;
import com.vritti.AlfaLavaModule.bean.CartonDetail;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.Utils_print;
import com.vritti.sales.CounterBilling.DeviceListActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

public class LoadingHeaderListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_LoadingCartonListData adapter;
    private ArrayList<CartonData> cartonDataArrayList;
    private static RecyclerView recycler;
    TextView edt_scanPacket;
    ImageView img_search;
    ProgressBar progress;
    private String CartonHeaderId="",PackOrderHeaderId="";
    LinearLayout len_search;
    private AlertDialog b;
    private String finaljson;
    Handler mHandler = new Handler();
    private String CartanCode="",PackOrderNo="";
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;
    private String data;
    private String PacketNo="";
    ImageView img_barcode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(LoadingHeaderListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(LoadingHeaderListActivity.this);
        String dabasename = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(LoadingHeaderListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(LoadingHeaderListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        cartonDataArrayList = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        edt_scanPacket = (TextView) findViewById(R.id.edt_scanPacket);
        edt_scanPacket.setVisibility(View.VISIBLE);
        img_search = (ImageView) findViewById(R.id.img_search);
        len_search = (LinearLayout) findViewById(R.id.len_search);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(LoadingHeaderListActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_LoadingCartonListData(cartonDataArrayList);
        recycler.setAdapter(adapter);
        img_barcode = findViewById(R.id.img_barcode);
        img_barcode.setVisibility(View.VISIBLE);

        len_search.setVisibility(View.VISIBLE);

        PackOrderNo=getIntent().getStringExtra("CODE");
        PackOrderHeaderId=getIntent().getStringExtra("ID");
        getSupportActionBar().setTitle(PackOrderNo);


        mService = new BluetoothService(LoadingHeaderListActivity.this, mHandler);


        if (mService.isAvailable() == false) {
            Toast.makeText(LoadingHeaderListActivity.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }



        edt_scanPacket.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (edt_scanPacket != null) {
                                edt_scanPacket.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay


                    data = edt_scanPacket.getText().toString();
                    filter(data);

                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PacketNo = edt_scanPacket.getText().toString().trim();
                try {
                    ArrayList<CartonData> dummyList = new ArrayList<>();

                    for (int i = 0; i < cartonDataArrayList.size(); i++) {
                        if (cartonDataArrayList.get(i).getCartonCode().equals(PacketNo)) {
                            dummyList.add(cartonDataArrayList.get(i));
                            adapter.update(dummyList);
                        }
                        if (dummyList.size()>0){

                            if (isnet()) {
                                try {
                                    progress.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                new StartSession(LoadingHeaderListActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new GetPacketValidation().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                    }


                                });

                            } else {
                                Toast.makeText(LoadingHeaderListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }


                        }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(LoadingHeaderListActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });


    }

    public void setdonumber(String DONumber,String Pack_OrdHdrId ) {

     //  Packetdeletedialog(DONumber,Pack_OrdHdrId);

       String packorderno=getIntent().getStringExtra("CODE");
       getdialog(DONumber,packorderno);

    }



    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private class GetCartonList extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getCartonHdr +"?Pack_OrdHdrId="+PackOrderHeaderId;
            try {
                res = ut.OpenConnection(url, LoadingHeaderListActivity.this);
                response = res.toString();
                // response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId

            progress.setVisibility(View.GONE);

            cartonDataArrayList.clear();
            if (s.contains("CartonHeaderId")) {
                try {
                    JSONArray jResults = new JSONArray(s);
                    for (int i=0;i<jResults.length();i++){
                        CartonData cartonData=new CartonData();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        cartonData.setCartonHeaderId(jsonObject.getString("CartonHeaderId"));
                        cartonData.setCartonCode(jsonObject.getString("CartonCode"));
                        cartonDataArrayList.add(cartonData);
                    }

                    adapter.update(cartonDataArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(LoadingHeaderListActivity.this, "Carton not found in " + PackOrderNo, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                }else {

                Toast toast = Toast.makeText(LoadingHeaderListActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Carton not found in " + PackOrderNo+ "</big></b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
                progress.setVisibility(View.GONE);
                final MediaPlayer mp = MediaPlayer.create(LoadingHeaderListActivity.this, R.raw.alert);
                mp.start();
            } else {
                Toast.makeText(LoadingHeaderListActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            progress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isnet()) {

            try {
                progress.setVisibility(View.VISIBLE);
                new StartSession(LoadingHeaderListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        GetCartonList getCartonList = new GetCartonList();
                        getCartonList.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(LoadingHeaderListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void getdialog(String DONumber, String packorderno) {


                if(BluetoothClass.isPrinterConnected(getApplicationContext(), LoadingHeaderListActivity.this)) {
                    mService = BluetoothClass.getServiceInstance();



                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(), LoadingHeaderListActivity.this);
                }


                if(BluetoothClass.isPrinterConnected(getApplicationContext(), LoadingHeaderListActivity.this)) {
                    mService = BluetoothClass.getServiceInstance();
                    printReceipt(DONumber,packorderno);
                }


            }




    public void printReceipt(String DONumber, String  Pack_order) {

        //  for(int b =0; b<5; b++){
        final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
        final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
        final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
        final byte[] SMALLFONT = {0x1b, 0x21, 0x01}; //small font
        final byte[] DEFAULT = {0x1b, 0x21, 0x00};
        final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
        byte[] BOLD_NORMAL = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] BOLD_MEDIUM = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] BOLD_LARGE = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text

        byte[] format = {27, 33, 0};
        byte[] arrayOfByte1 = {27, 33, 0};
        // Small
        format[2] = ((byte) (0x1 | arrayOfByte1[2]));

        String msg = "", company = "";
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
        double weight = 0;


        msg = "Carton no : " + DONumber;
        if (msg.length() > 0) {
            mService.write(ALIGN_CENTER);
            mService.write(BOLD_NORMAL);
            mService.sendMessage(msg, "GBK");
        }

        try {
            int MARGIN_NONE = 0;
            int MARGIN_AUTOMATIC = -1;
            int marginSize = MARGIN_NONE;

            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //hintMap.put(EncodeHintType.MARGIN, ErrorCorrectionLevel.forBits(marginSize));
            QRCodeWriter codeWriter;
            codeWriter = new QRCodeWriter(); //CodeWriter
            BitMatrix byteMatrix = codeWriter.encode(DONumber, BarcodeFormat.QR_CODE, 500, 200, hintMap);
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
            mService.write(ALIGN_CENTER);
            byte[] command = Utils_print.decodeBitmap(bitmap);
            mService.write(command);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        String Pack="Pack Order : "+Pack_order;
        msg = "";
        msg = "" + Pack + /*"" + Itemcode + */"\n";
        if (msg.length() > 0) {
            mService.write(ALIGN_CENTER);
            mService.write(BOLD_NORMAL);
            mService.sendMessage(msg + "\n", "GBK");
        }

    }

    public void printReceipt_Twin(String DONumber, String  Pack_order) {

        //  for(int b =0; b<5; b++){
        final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
        final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
        final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
        final byte[] SMALLFONT = {0x1b, 0x21, 0x01}; //small font
        final byte[] DEFAULT = {0x1b, 0x21, 0x00};
        final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
        byte[] BOLD_NORMAL = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] BOLD_MEDIUM = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] BOLD_LARGE = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text

        byte[] format = {27, 33, 0};
        byte[] arrayOfByte1 = {27, 33, 0};
        // Small
        format[2] = ((byte) (0x1 | arrayOfByte1[2]));

        String msg = "", company = "";
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
        double weight = 0;


      /*  msg = "Carton no : " + DONumber;
        if (msg.length() > 0) {
            mService.write(ALIGN_CENTER);
            mService.write(BOLD_NORMAL);
            mService.sendMessage(msg, "GBK");
        }*/

        try {
            int MARGIN_NONE = 0;
            int MARGIN_AUTOMATIC = -1;
            int marginSize = MARGIN_NONE;

            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter codeWriter;
            codeWriter = new QRCodeWriter(); //CodeWriter
            BitMatrix byteMatrix = codeWriter.encode(DONumber, BarcodeFormat.QR_CODE, 500, 200, hintMap);
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

            mService.write(ALIGN_LEFT);
            byte[] command = Utils_print.decodeBitmap(bitmap);
            mService.write(command);

            BitMatrix byteMatrix2 = codeWriter.encode(Pack_order, BarcodeFormat.QR_CODE, 500, 200, hintMap);

            int width2 = byteMatrix2.getWidth();
            int height2 = byteMatrix2.getHeight();
            Log.e("x ", String.valueOf(width2));
            Log.e("y ", String.valueOf(height2));
            Bitmap bitmap2 = Bitmap.createBitmap(width2, height2, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < width1; i++) {
                for (int j = 0; j < height1; j++) {
                    bitmap2.setPixel(i, j, byteMatrix2.get(i, j) ? Color.BLACK : Color.WHITE);

                }
            }

            mService.write(ALIGN_RIGHT);
            byte[] command2 = Utils_print.decodeBitmap(bitmap2);
            mService.write(command2);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

       /* String Pack="Pack Order : "+Pack_order;
        msg = "";
        msg = "" + Pack + *//*"" + Itemcode + *//*"\n";
        if (msg.length() > 0) {
            mService.write(ALIGN_CENTER);
            mService.write(BOLD_NORMAL);
            mService.sendMessage(msg + "\n", "GBK");
        }*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);

        if (requestCode == CreateSecondaryPackActivity.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //bluetooth enabled and request for showing available bluetooth devices
            // Toast.makeText(parent, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            BluetoothClass.pairPrinter(getApplicationContext(), LoadingHeaderListActivity.this);
        }else if (requestCode == CreateSecondaryPackActivity.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            //bluetooth device selected and request pairing with device
            String address = data1.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            String Address = address.substring(address.length() - 17);
            BluetoothClass.pairedPrinterAddress(getApplicationContext(), LoadingHeaderListActivity.this,Address);


        }else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data1);
            if (result != null) {
                if (result.getContents() == null) {
                    Log.e("Scan*******", "Cancelled scan");

                } else {
                    Log.e("Scan", "Scanned");


                    data = result.getContents().toString();
                    filter(data);

                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg1) {
            switch (msg1.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg1.arg1) {
                        case BluetoothService.STATE_CONNECTED: // ÒÑÁ¬½Ó
                            Toast.makeText(LoadingHeaderListActivity.this, "Connect successful",
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
                    Toast.makeText(LoadingHeaderListActivity.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(LoadingHeaderListActivity.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };
    private void filter(String packet) {
        PacketNo=packet;
        try {
            ArrayList<CartonData> dummyList = new ArrayList<>();

            for (int i = 0; i < cartonDataArrayList.size(); i++) {
                if (cartonDataArrayList.get(i).getCartonCode().equals(packet)) {
                    dummyList.add(cartonDataArrayList.get(i));
                    adapter.update(dummyList);
                }
            }
                if (dummyList.size()>0){

                    if (isnet()) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new StartSession(LoadingHeaderListActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetPacketValidation().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }


                        });

                    } else {
                        Toast.makeText(LoadingHeaderListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        Toast toast = Toast.makeText(LoadingHeaderListActivity.this, "Scanned carton not in list", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {

                Toast toast = Toast.makeText(LoadingHeaderListActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Scanned carton not in list"+"</big></b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
                    final MediaPlayer mp = MediaPlayer.create(LoadingHeaderListActivity.this, R.raw.alert);
                    mp.start();
                }



        }catch (Exception e){
            e.printStackTrace();
        }
        edt_scanPacket.setText("");
    }

    private class GetPacketValidation extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_CartonLoading+"?Pack_OrdHdrId="+PackOrderHeaderId+"&CartonCode="+PacketNo;

            try {
                res = ut.OpenConnection(url, LoadingHeaderListActivity.this);
                response = res.toString().replaceAll("\\\\", "");

                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
            try{
                progress.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {

                edt_scanPacket.setText("");

                if (s.contains("Success")){
                                    MediaPlayer mp = MediaPlayer.create(LoadingHeaderListActivity.this, R.raw.ok);
                                    mp.start();
                                    Toast toast = Toast.makeText(LoadingHeaderListActivity.this, "Carton loaded successfully", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.GREEN);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastView.setBackgroundColor(Color.WHITE);
                                    toast.show();
                                    adapter.update(cartonDataArrayList);
                }
                else if (s.contains("Failed")) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String status = jsonObject.getString("ERROR");
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                            Toast toast = Toast.makeText(LoadingHeaderListActivity.this, status, Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                    }else {

                        Toast toast = Toast.makeText(LoadingHeaderListActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + status+"</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }
                        final MediaPlayer mp = MediaPlayer.create(LoadingHeaderListActivity.this, R.raw.alert);
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        Toast toast = Toast.makeText(LoadingHeaderListActivity.this, s, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                }else {

                    Toast toast = Toast.makeText(LoadingHeaderListActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + s+"</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }

                    final MediaPlayer mp = MediaPlayer.create(LoadingHeaderListActivity.this, R.raw.alert);
                    mp.start();


                }

                progress.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

}