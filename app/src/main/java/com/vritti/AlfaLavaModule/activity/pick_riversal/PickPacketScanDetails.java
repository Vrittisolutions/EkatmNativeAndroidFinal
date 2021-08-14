package com.vritti.AlfaLavaModule.activity.pick_riversal;

import android.app.MediaRouteButton;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.AlfaLavaModule.activity.GRNScanner;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.activity.unpacking.UnPackingCartonDetailActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickPacketScanDetails extends AppCompatActivity {

    ArrayList<PutAwayDetail> putAwayDetail;
    PutAwayDetail putAwayDetails;
    private Context pContext;
    @BindView(R.id.txt_locatn)
    TextView txt_locatn;
    EditText edt_scanPacket;
    String packetno, StockDetailsId;
    private static RecyclerView recycler;
    private static AdapterPutHandDetail adapter;


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
    private String Location_Transfer="";
    private ProgressBar progress;
    private String Pick_ListHdrId="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putaway_details);
        ButterKnife.bind(this);
        pContext = PickPacketScanDetails.this;

        getSupportActionBar().setTitle("Picking Reversal");


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
        button_post = (Button) findViewById(R.id.button_post);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        edt_scanPacket = (EditText) findViewById(R.id.edt_scanPacket_1);
        edt_scanPacket.setHint("Scan packet");
        progress = (ProgressBar) findViewById(R.id.progressBar);
        recycler.setHasFixedSize(true);
        adapter = new AdapterPutHandDetail(putAwayDetail);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);


        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        t1.speak("Start scanning packets when cursor is in text box", TextToSpeech.QUEUE_FLUSH, null);


        if (getIntent() != null) {
            putAwayDetails = new Gson().fromJson(getIntent().getStringExtra("putArayObject"), PutAwayDetail.class);
        }

        if (putAwayDetail != null) {
            txt_locatn.setText(putAwayDetails.getLocationCode());
            StockDetailsId = putAwayDetails.getStockDetailsId();
            DestLocationMasterId = putAwayDetails.getDestLocationMasterId();
            DestWareHouseMasterId = putAwayDetails.getDestWareHouseMasterId();
            Pick_ListHdrId=getIntent().getStringExtra("headerid");
         //   Location_Transfer=getIntent().getStringExtra("loc_transcode");


        }

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PacketNo=edt_scanPacket.getText().toString();
                    if (isnet()) {
                        progress.setVisibility(View.VISIBLE);
                    new StartSession(pContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                PacketReversalData  downloadPutAwayDetails = new PacketReversalData();
                                downloadPutAwayDetails.execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
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


                    PacketNo = edt_scanPacket.getText().toString().trim();

                    if (PacketNo != null && !(PacketNo.equals(""))) {
                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + PacketNo + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                edt_scanPacket.setText("");
                                Toast toast = Toast.makeText(PickPacketScanDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
                                View toastView = toast.getView();
                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(18);
                                toastMessage.setTextColor(Color.RED);
                                toastMessage.setGravity(Gravity.CENTER);
                                toastMessage.setCompoundDrawablePadding(5);
                                toastView.setBackgroundColor(Color.TRANSPARENT);
                                toast.show();
                                final MediaPlayer mp = MediaPlayer.create(PickPacketScanDetails.this, R.raw.alert);
                                mp.start();
                            } while (cursor.moveToNext());

                        }
                    }else {


                            if (isnet()) {
                                progress.setVisibility(View.VISIBLE);
                                PacketReversalData downloadPutAwayDetails = new PacketReversalData();
                                downloadPutAwayDetails.execute();

                            } else {

                                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }


                        }


                    return true;
                }
                return false;
            }
        });


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
    public void onBackPressed() {

        finish();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

    }
    class PacketReversalData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.PickingReverse + "?Pick_ListHdrId=" + Pick_ListHdrId+"&LocationMasterId="+DestLocationMasterId+
                    "&WarehosueId="+DestWareHouseMasterId+"&PacketNo="+PacketNo;

            try {

                res = ut.OpenConnection(url, pContext);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);


            } catch (final Exception e) {
                response = "Error";

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PickPacketScanDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        final MediaPlayer mp = MediaPlayer.create(PickPacketScanDetails.this, R.raw.alert);
                        mp.start();
                    }
                });
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            progress.setVisibility(View.GONE);
            edt_scanPacket.setText("");

            if (integer.contains("Success")) {

                Packet grnpost_1 = new Packet();
                grnpost_1.setPacketNo(PacketNo);
                cf.Insert_GRNPACKETNO(grnpost_1);

                Toast toast = Toast.makeText(PickPacketScanDetails.this, "Packet removed successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();



                //onBackPressed();

            }
            else if (integer.contains("Failed")) {
                try {
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(PickPacketScanDetails.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(PickPacketScanDetails.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (integer.contains("False")||integer.contains("false")) {
                try {
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(PickPacketScanDetails.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(PickPacketScanDetails.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
