package com.vritti.AlfaLavaModule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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

public class PutAwayScanDetails extends AppCompatActivity {

    ArrayList<PutAwayDetail> putAwayDetail;
    PutAwayDetail putAwayDetails;
    private Context pContext;
    @BindView(R.id.txt_locatn)
    TextView txt_locatn;
    EditText edt_scanPacket;
    String packetno, StockDetailsId;
    DownloadPutAwayPacketDetails downloadPutAwayDetails;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_putaway_details);
        ButterKnife.bind(this);
        pContext = PutAwayScanDetails.this;

        getSupportActionBar().setTitle("GRN Put-away");


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

        jobjdata = new JSONObject();
        jsonObject1 = new JSONObject();
        ob = new JSONArray();

        putAwayDetails = new PutAwayDetail();
        putAwayDetail = new ArrayList<PutAwayDetail>();

        putAwayDetail.clear();
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove("stock");
        editor.remove("total");
        editor.remove("api");
        editor.commit();


        recycler = (RecyclerView) findViewById(R.id.list_itemDetailList);
        button_post = (Button) findViewById(R.id.button_post);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        edt_scanPacket = (EditText) findViewById(R.id.edt_scanPacket_1);
        recycler.setHasFixedSize(true);
        adapter = new AdapterPutHandDetail(putAwayDetail);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);

        Location_Transfer=getIntent().getStringExtra("loc_transcode");

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
         //   Location_Transfer=getIntent().getStringExtra("loc_transcode");


        }

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                packetno = edt_scanPacket.getText().toString();
                String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + packetno + "'";
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
                        Toast toast = Toast.makeText(PutAwayScanDetails.this, "You have already scanned this packet", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                        mp.start();
                    } while (cursor.moveToNext());

                } else {
                    if (isnet()) {
                        ProgressHUD.show(pContext, "Fetching Pending Putaway...", true, false);
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


                    packetno = edt_scanPacket.getText().toString().trim();

                    if (Constants.type == Constants.Type.Alfa) {

                        try {
                            packetno = new JSONObject(packetno.replace("Info:", "")).getString("PacketNo");
                            edt_scanPacket.setText("");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i("id", packetno);

                        if (packetno != null && !(packetno.equals(""))) {


                            if (isnet()) {
                                ProgressHUD.show(pContext, "Fetching Pending Putaway...", true, false);

                                downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                downloadPutAwayDetails.execute();


                            } else {
                                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                           /* if (isnet()) {
                                ProgressHUD.show(pContext, "Fetching Pending Putaway...", true, false);
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
                            }*/

                        }
                    } else {


                        if (packetno != null && !(packetno.equals(""))) {

                            String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + packetno + "'";
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
                                    Toast toast = Toast.makeText(PutAwayScanDetails.this, "You have already scanned this packet", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                                    mp.start();
                                } while (cursor.moveToNext());

                            } else {
                                if (isnet()) {
                                    ProgressHUD.show(pContext, "Fetching Pending Putaway...", true, false);

                                    downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                    downloadPutAwayDetails.execute();

                                } else {

                                    Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    }

                    return true;
                }
                return false;
            }
        });

        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jobjdata = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                JSONArray ob = new JSONArray();
                JSONArray packetarray = new JSONArray();
                JSONObject jsonObjectpacket = new JSONObject();


                String q = userpreferences.getString("total", "");


                try {

                    if (putAwayDetail.size() > 0) {

                        if (q.equals("")) {

                            StockDetailsid = putAwayDetail.get(0).getStockDetailsId();
                            ItemPlantId = putAwayDetail.get(0).getItemPlantId();
                            WareHouseMasterId = putAwayDetail.get(0).getWarehouseId();
                            LocationMasterId = putAwayDetail.get(0).getLocationMasterId();
                            LotNo = putAwayDetail.get(0).getLotNo();
                            // DestWareHouseMasterId = putAwayDetail.get(j).getDestWareHouseMasterId();
                            //DestLocationMasterId = putAwayDetail.get(j).getDestLocationMasterId();
                            FIFODate = putAwayDetail.get(0).getFIFODate();

                            jsonObject1.put("SrNO", "");
                            jsonObject1.put("SrcItemMasterId", ItemPlantId);
                            jsonObject1.put("SrcStockDetailId", StockDetailsid);
                            jsonObject1.put("SrcWareHouseMasterId", WareHouseMasterId);
                            jsonObject1.put("SrcLocationMasterId", LocationMasterId);
                            jsonObject1.put("SrcUOMMasterId", "");
                            jsonObject1.put("SrcLotNo", LotNo);
                            jsonObject1.put("DestWareHouseMasterId", DestWareHouseMasterId);
                            jsonObject1.put("DestLocationMasterId", DestLocationMasterId);
                            jsonObject1.put("Remark", "");
                            jsonObject1.put("FiFo", FIFODate);
                            jsonObject1.put("PutAwayType", Location_Transfer);

                            for (int j = 0; j < putAwayDetail.size(); j++) {

                                qty = Double.parseDouble(putAwayDetail.get(j).getBalQty());
                                //mimba change
                                TotalQty = qty + TotalQty;


                                String PackId = putAwayDetail.get(j).getPacketMasterId();

                                /*String poListString = PackId;
                                String[] parts = poListString.split(",");
                                StringBuilder output = new StringBuilder();

                                for (String part : parts) {
                                    if (output.length() > 0) {
                                        output.append(",");
                                    }

                                    output.append("'").append(part).append("'");

                                }

                                System.out.println(output);*/

                                PacketMasterId = String.valueOf(PackId);


                                try {
                                    jsonObject1.put("SrcTranQty", TotalQty);
                                    jsonObjectpacket.put("PacketMasterId", PacketMasterId);
                                    try {
                                        JSONObject a1 = new JSONObject(jsonObjectpacket.toString());
                                        packetarray.put(a1);
                                        jobjdata.put("PacketData", packetarray);
                                    } catch (JSONException e) {

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            JSONObject a = new JSONObject(jsonObject1.toString());
                            ob.put(a);
                            jobjdata.put("PutAwayData", ob);

                            //Post data

                            finaljson = "";
                            finaljson = jobjdata.toString();

                            SharedPreferences.Editor editor = userpreferences.edit();
                            editor.putString("total", "1");
                            editor.putString("api", finaljson);
                            editor.commit();

                            if (isnet()) {
                                ProgressHUD.show(pContext, "Sending data please wait...", true, false);
                                new StartSession(pContext, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new UpLoadData().execute(finaljson);

                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        //   new UpLoadData().execute(finaljson);

                                    }


                                });

                            } else {
                                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            finaljson = userpreferences.getString("api", "");
                            if (isnet()) {
                                ProgressHUD.show(pContext, "Sending data please wait...", true, false);
                                new StartSession(pContext, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new UpLoadData().execute(finaljson);

                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        //   new UpLoadData().execute(finaljson);

                                    }


                                });

                            } else {
                                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(PutAwayScanDetails.this, "Minimum 1 packet required", Toast.LENGTH_SHORT).show();
                        t1.speak("Minimum 1 packet required", TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private class UpLoadData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            //Nimba given
            String url = CompanyURL + WebUrlClass.POSTAndroidUpdated;
            //Nimba given
            //String url = CompanyURL + WebUrlClass.POST_V2;
            try {
                res = ut.OpenPostConnection(url, params[0], pContext);
                response = res.toString().replaceAll("\\\\", "");

            } catch (final Exception e) {
                response = "Error";

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(pContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                        mp.start();
                    }
                });
            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();

            if (s.contains("Success")) {

                Toast.makeText(pContext, "Putaway post successfully", Toast.LENGTH_LONG).show();

                t1.speak("Putaway post Successfully", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.ok);
                mp.start();

                putAwayDetail.clear();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("stock");
                editor.remove("total");
                editor.remove("api");
                editor.commit();
                TotalQty = 0;

                startActivity(new Intent(PutAwayScanDetails.this, GRNScanner.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            } else if (s.contains("Failed")) {
                try {
                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast.makeText(pContext, status, Toast.LENGTH_LONG).show();
                    final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                    mp.start();

                    t1.speak(status, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("False")) {
                s = s.substring(1, s.length() - 1);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast.makeText(pContext, status, Toast.LENGTH_LONG).show();
                    final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                    mp.start();
                    t1.speak(status, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(pContext, s, Toast.LENGTH_LONG).show();
                final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                mp.start();
                t1.speak("Record not saved", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }


            }
        }

    }


    public class DownloadPutAwayPacketDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.GetScanPacket + "?PacketNo=" + packetno;

            try {

                res = ut.OpenConnection(url, pContext);
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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId
            ProgressHUD.Destroy();
            edt_scanPacket.setText("");

            String s = res;

            if (res.contains("StockDetailsId")) {
                try {
                    edt_scanPacket.setText("");

                    JSONArray jResults = new JSONArray(s);
                    String StockDetailsid = "";

                    Packet grnpost_1 = new Packet();
                    grnpost_1.setPacketNo(packetno);
                    cf.Insert_GRNPACKETNO(grnpost_1);

                    for (int i = 0; i < jResults.length(); i++) {
                        PutAwayDetail putAwayDetaill = new PutAwayDetail();
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        PacketNo = jsonObject.getString("PacketNo");
                        putAwayDetaill.setPacketNo(PacketNo);
                        LotNo = jsonObject.getString("LotNo");
                        putAwayDetaill.setLotNo(LotNo);
                        putAwayDetaill.setOrderNo(jsonObject.getString("OrderNo"));
                        putAwayDetaill.setItemCode(jsonObject.getString("ItemCode"));
                        putAwayDetaill.setItemDesc(jsonObject.getString("ItemDesc"));
                        WareHouseMasterId = jsonObject.getString("WareHouseMasterId");
                        putAwayDetaill.setWarehouseId(WareHouseMasterId);
                        LocationMasterId = jsonObject.getString("LocationMasterId");
                        putAwayDetaill.setLocationMasterId(LocationMasterId);
                        StockDetailsid = jsonObject.getString("StockDetailsId");
                        StockID = userpreferences.getString("stock", "");
                        if (StockID.equals("")) {
                            SharedPreferences.Editor editor = userpreferences.edit();
                            editor.putString("stock", StockDetailsid);
                            editor.commit();
                            putAwayDetaill.setStockDetailsId(StockDetailsid);
                        } else {
                            putAwayDetaill.setStockDetailsId(StockDetailsid);
                        }
                        ItemPlantId = jsonObject.getString("ItemPlantId");
                        putAwayDetaill.setItemPlantId(ItemPlantId);
                        PacketMasterId = jsonObject.getString("PacketMasterId");
                        putAwayDetaill.setPacketMasterId(PacketMasterId);
                        BalQty = jsonObject.getString("BalQty");
                        FIFODate = jsonObject.getString("FIFODate");
                        FIFODate = getDateEnd(FIFODate);
                        putAwayDetaill.setFIFODate(FIFODate);
                        putAwayDetaill.setBalQty(BalQty);
                        putAwayDetail.add(0, putAwayDetaill);


                    }

                    button_post.setVisibility(View.VISIBLE);

                    adapter.update(putAwayDetail);

                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(0);
                        }
                    });


                    String stockdetails = "";
                    for (int j = 0; j < putAwayDetail.size(); j++) {
                        stockdetails = putAwayDetail.get(j).getStockDetailsId();

                    }
                    StockID = userpreferences.getString("stock", "");
                    if (StockID.equals(stockdetails)) {

                        //Stack details Id change post data to server and clear arratlist and stockdetailsid and total qty blank

                    } else {
                        try {

                            Toast.makeText(PutAwayScanDetails.this, "SKU/Lot changed, posting transaction, please wait", Toast.LENGTH_SHORT).show();

                            t1.speak("SKU/Lot changed, posting transaction, please wait", TextToSpeech.QUEUE_FLUSH, null);

                            if (t1 != null) {
                                t1.stop();
                                t1.shutdown();
                            }

                            postdata();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.equals("[]")) {
                edt_scanPacket.setText("");
                packetno = "";
                edt_scanPacket.setText("");
                t1.speak("Record not present", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                Toast toast = Toast.makeText(PutAwayScanDetails.this, "Record not present", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                mp.start();

            }
         /*   else if (s.equals("Error")) {
                packetno = "";
                edt_scanPacket.setText("");
                Toast.makeText(pContext, "Technical error....", Toast.LENGTH_LONG).show();

                t1.speak("Technical error....", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }

            }else if (s.contains("zero")) {
                packetno = "";
                edt_scanPacket.setText("");
                Toast.makeText(pContext, "Packet quantity is zero", Toast.LENGTH_LONG).show();

                t1.speak("Packet quantity is zero", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }

            }*/
            else if (s.contains("receiving buffer")) {
                packetno = "";
                edt_scanPacket.setText("");


                t1.speak("Packet not found in receiving buffer", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                Toast toast = Toast.makeText(PutAwayScanDetails.this, "Packet not found in receiving buffer", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
                mp.start();
            } else {

                t1.speak(res, TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }

                Toast toast = Toast.makeText(PutAwayScanDetails.this, res, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PutAwayScanDetails.this, R.raw.alert);
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
    public void onBackPressed() {

        if (backpressCount == 2) {
            finish();
        } else {
            transactiondialog();
        }


    }


    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    private void transactiondialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PutAwayScanDetails.this);
        LayoutInflater inflater = PutAwayScanDetails.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.transaction_dialog_lay, null);
        dialogBuilder.setView(myView);

        Button btn_cancel = myView.findViewById(R.id.btn_cancel);
        Button btn_yes = myView.findViewById(R.id.btn_yes);
        Button button_post = myView.findViewById(R.id.button_post);


        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postdata();

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PutAwayScanDetails.this, GRNScanner.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.dismiss();

            }
        });


        b = dialogBuilder.create();
        b.show();
    }

    private void postdata() {

        JSONObject jobjdata = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONArray ob = new JSONArray();
        JSONArray packetarray = new JSONArray();
        JSONObject jsonObjectpacket = new JSONObject();


        String q = userpreferences.getString("total", "");


        try {

            if (putAwayDetail.size() > 0) {

                if (q.equals("")) {

                    StockDetailsid = putAwayDetail.get(0).getStockDetailsId();
                    ItemPlantId = putAwayDetail.get(0).getItemPlantId();
                    WareHouseMasterId = putAwayDetail.get(0).getWarehouseId();
                    LocationMasterId = putAwayDetail.get(0).getLocationMasterId();
                    LotNo = putAwayDetail.get(0).getLotNo();
                    // DestWareHouseMasterId = putAwayDetail.get(j).getDestWareHouseMasterId();
                    //DestLocationMasterId = putAwayDetail.get(j).getDestLocationMasterId();
                    FIFODate = putAwayDetail.get(0).getFIFODate();

                    jsonObject1.put("SrNO", "");
                    jsonObject1.put("SrcItemMasterId", ItemPlantId);
                    jsonObject1.put("SrcStockDetailId", StockDetailsid);
                    jsonObject1.put("SrcWareHouseMasterId", WareHouseMasterId);
                    jsonObject1.put("SrcLocationMasterId", LocationMasterId);
                    jsonObject1.put("SrcUOMMasterId", "");
                    jsonObject1.put("SrcLotNo", LotNo);
                    jsonObject1.put("DestWareHouseMasterId", DestWareHouseMasterId);
                    jsonObject1.put("DestLocationMasterId", DestLocationMasterId);
                    jsonObject1.put("Remark", "");
                    jsonObject1.put("FiFo", FIFODate);
                    jsonObject1.put("PutAwayType", Location_Transfer);


                    for (int j = 0; j < putAwayDetail.size(); j++) {

                        qty = Double.parseDouble(putAwayDetail.get(j).getBalQty());
                        //mimba change
                        TotalQty = qty + TotalQty;


                        String PackId = putAwayDetail.get(j).getPacketMasterId();

                       /* String poListString = PackId;
                        String[] parts = poListString.split(",");
                        StringBuilder output = new StringBuilder();

                        for (String part : parts) {
                            if (output.length() > 0) {
                                output.append(",");
                            }

                            output.append("'").append(part).append("'");
                        }
*/

                        PacketMasterId = String.valueOf(PackId);


                        try {
                            jsonObject1.put("SrcTranQty", TotalQty);
                            jsonObjectpacket.put("PacketMasterId", PacketMasterId);
                            try {
                                JSONObject a1 = new JSONObject(jsonObjectpacket.toString());
                                packetarray.put(a1);
                                jobjdata.put("PacketData", packetarray);
                            } catch (JSONException e) {

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    JSONObject a = new JSONObject(jsonObject1.toString());
                    ob.put(a);
                    jobjdata.put("PutAwayData", ob);

                    //Post data

                    finaljson = "";
                    finaljson = jobjdata.toString();

                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("total", "1");
                    editor.putString("api", finaljson);
                    editor.commit();

                    if (isnet()) {
                        ProgressHUD.show(pContext, "Sending data please wait...", true, false);
                        new StartSession(pContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new UpLoadData().execute(finaljson);

                            }

                            @Override
                            public void callfailMethod(String msg) {
                                //   new UpLoadData().execute(finaljson);

                            }


                        });

                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    finaljson = userpreferences.getString("api", "");
                    if (isnet()) {
                        ProgressHUD.show(pContext, "Sending data please wait...", true, false);
                        new StartSession(pContext, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new UpLoadData().execute(finaljson);

                            }

                            @Override
                            public void callfailMethod(String msg) {
                                //   new UpLoadData().execute(finaljson);

                            }


                        });

                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {

                Toast.makeText(PutAwayScanDetails.this, "Minimum 1 packet required", Toast.LENGTH_SHORT).show();
                t1.speak("Minimum 1 packet required", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    @Override
    protected void onStop() {
        super.onStop();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

    }
}
