package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.PI.AddPacketActivity;
import com.vritti.AlfaLavaModule.PI.PacketScanDetails;
import com.vritti.AlfaLavaModule.adapter.AdapterCutOverPAcketDetails;
import com.vritti.AlfaLavaModule.adapter.AdapterPutHandDetail;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PacketScanDataCutoff extends AppCompatActivity {

    ArrayList<PutAwayDetail> putAwayDetail;
    PutAwayDetail putAwayDetails;
    private Context pContext;
    @BindView(R.id.txt_locatn)
    TextView txt_locatn;
    EditText edt_scanPacket;
    String packetno="", StockDetailsId;
    DownloadPutAwayPacketDetails downloadPutAwayDetails;
    private static RecyclerView recycler;
    private static AdapterCutOverPAcketDetails adapter;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    double TotalQty = 0;
    double qty;

    String PacketNo, LotNo, OrderNo, WareHouseMasterId, LocationMasterId, ItemPlantId, PacketMasterId, DestWareHouseMasterId, DestLocationMasterId, BalQty, FIFODate;
    private String finaljson;
    String StockID = "";
    Button button_post;
    private String StockDetailsid = "";
    JSONObject jobjdata, jsonObject1;
    JSONArray ob;
    Button btn_scan;
    private TextToSpeech t1;
    private AlertDialog b;
    boolean doubleBackToExitPressedOnce = false;
    private int backpressCount = 0;
    Handler mHandler = new Handler();
    LinearLayout ln_locatn;
    String LocationCode="";
    ProgressBar progressBar;
    ImageView img_barcode;
    String LOT="",FIFO="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putaway_details);
        ButterKnife.bind(this);
        pContext = PacketScanDataCutoff.this;

        getSupportActionBar().setTitle("Packet Scan");


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        putAwayDetails = new PutAwayDetail();
        putAwayDetail = new ArrayList<PutAwayDetail>();

        putAwayDetail.clear();

        recycler = (RecyclerView) findViewById(R.id.list_itemDetailList);
        recycler.setVisibility(View.VISIBLE);
        button_post = (Button) findViewById(R.id.button_post);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        edt_scanPacket = (EditText) findViewById(R.id.edt_scanPacket_1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ln_locatn = (LinearLayout) findViewById(R.id.ln_locatn);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);
        img_barcode.setVisibility(View.VISIBLE);
        recycler.setHasFixedSize(true);
        adapter = new AdapterCutOverPAcketDetails(putAwayDetail);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);
        ln_locatn.setVisibility(View.VISIBLE);



        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        t1.speak("Start scanning packets when cursor is in text box", TextToSpeech.QUEUE_FLUSH, null);


        LocationCode=getIntent().getStringExtra("locationcode");
        LOT=getIntent().getStringExtra("lot");
        FIFO=getIntent().getStringExtra("fifo");

        txt_locatn.setText(LocationCode);

         btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packetno = edt_scanPacket.getText().toString();

                if (packetno != null && !(packetno.equals(""))) {
                    if (isnet()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new StartSession(pContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                downloadPutAwayDetails.execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                downloadPutAwayDetails.execute();
                            }


                        });

                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                    mp.start();
                }
               /* String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + packetno + "'";
                Cursor cursor = sql.rawQuery(searchQuery, null);
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    do {
                        edt_scanPacket.setText("");

                        t1.speak("You have already scanned this packet", TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                        Toast toast = Toast.makeText(PacketScanDataCutoff.this, "You have already scanned this packet", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                        mp.start();
                    } while (cursor.moveToNext());

                } else {

                }*/
            }
        });


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


                    packetno = edt_scanPacket.getText().toString().trim();

                    if (packetno != null && !(packetno.equals(""))) {


                        if (isnet()) {
                            progressBar.setVisibility(View.VISIBLE);
                            new StartSession(pContext, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                    downloadPutAwayDetails.execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                    downloadPutAwayDetails.execute();
                                }


                            });

                        } else {
                            Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                        }else {
                        Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                        mp.start();
                    }


                    return true;
                }
                return false;
            }
        });

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PacketScanDataCutoff.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }

    public class DownloadPutAwayPacketDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.InsertStockCutOff +
                        "?PacketNo=" + packetno +
                        "&LocationCode="+ URLEncoder.encode(LocationCode,"UTF-8")+
                        "&LotNo="+ URLEncoder.encode(LOT,"UTF-8")+
                         "&Fifo="+ URLEncoder.encode(FIFO,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {

                res = ut.OpenConnection(url, pContext);
                response = res.toString();


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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId
            progressBar.setVisibility(View.GONE);
            edt_scanPacket.setText("");

            String s = res;

            if (res.contains("False")) {
                try {
                    edt_scanPacket.setText("");

                    PutAwayDetail putAwayDetaill = new PutAwayDetail();
                    putAwayDetaill.setPacketNo(packetno);
                    putAwayDetail.add(0, putAwayDetaill);
                    adapter.update(putAwayDetail);

                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(0);
                        }
                    });

                    Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Data send successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.ok);
                    mp.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else  {
                edt_scanPacket.setText("");
                packetno = "";

                t1.speak(res, TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Packet not found", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                mp.start();

            }
        }


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



    @Override
    protected void onStop() {
        super.onStop();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");


                packetno = result.getContents().toString();
                edt_scanPacket.setText("");
                if (!packetno.equals("")) {
                    if (isnet()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new StartSession(pContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                DownloadPutAwayPacketDetails_Barcode  downloadPutAwayDetails =
                                        new DownloadPutAwayPacketDetails_Barcode();
                                downloadPutAwayDetails.execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    edt_scanPacket.setText("");
                    Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                    mp.start();
                }


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class DownloadPutAwayPacketDetails_Barcode extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.InsertStockCutOff +
                        "?PacketNo=" + packetno +
                        "&LocationCode="+ URLEncoder.encode(LocationCode,"UTF-8")+
                        "&LotNo="+ URLEncoder.encode(LOT,"UTF-8")+
                        "&Fifo="+ URLEncoder.encode(FIFO,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {

                res = ut.OpenConnection(url, pContext);
                response = res.toString();


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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId
            progressBar.setVisibility(View.GONE);
            edt_scanPacket.setText("");

            String s = res;

            if (res.contains("False")) {
                try {
                    PutAwayDetail putAwayDetaill = new PutAwayDetail();
                    putAwayDetaill.setPacketNo(packetno);
                    putAwayDetail.add(0, putAwayDetaill);
                    adapter.update(putAwayDetail);

                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        recycler.smoothScrollToPosition(0);
                    }
                });


                edt_scanPacket.setText("");
                    Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Data send successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.ok);
                    mp.start();

                    packetno=edt_scanPacket.getText().toString();
                    IntentIntegrator integrator = new IntentIntegrator(PacketScanDataCutoff.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else  {
                edt_scanPacket.setText("");
                packetno = "";

                t1.speak(res, TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                Toast toast = Toast.makeText(PacketScanDataCutoff.this, "Packet not found", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PacketScanDataCutoff.this, R.raw.alert);
                mp.start();

            }
        }


    }

}
