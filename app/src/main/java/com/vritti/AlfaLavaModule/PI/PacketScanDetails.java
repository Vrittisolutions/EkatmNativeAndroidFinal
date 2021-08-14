package com.vritti.AlfaLavaModule.PI;

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
import android.os.StrictMode;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.activity.GRNScanner;
import com.vritti.AlfaLavaModule.adapter.AdapterPacketDetail;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PacketScanDetails extends AppCompatActivity {

    ArrayList<PutAwayDetail> putAwayDetail;
    PutAwayDetail putAwayDetails;
    private Context pContext;
    @BindView(R.id.txt_locatn)
    TextView txt_locatn;
    EditText edt_scanPacket;
    String packetno, StockDetailsId="";
    DownloadPutAwayPacketDetails downloadPutAwayDetails;
    private static RecyclerView recycler;
    private static AdapterPacketDetail adapter;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    double TotalQty = 0;
    double qty;

    String  LotNo, OrderNo, WareHouseMasterId, LocationMasterId, ItemPlantId, PacketMasterId, DestWareHouseMasterId, DestLocationMasterId, BalQty, FIFODate,LocationCode;
    String ItemCode="",ItemDescription="";
    private String finaljson;
    String StockID = "";
    private String StockDetailsid = "";
    JSONObject jobjdata, jsonObject1;
    JSONArray ob;
    Button btn_scan;
    private TextToSpeech t1;
    private AlertDialog b;
    boolean doubleBackToExitPressedOnce = false;
    private int backpressCount = 0;
    Handler mHandler = new Handler();
    Button btn_confirm;
    String  PICode="",PIHeaderID="";
    ImageView img_barcode;
    String Location;
    LinearLayout len;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pi_packet_details_lay);
        ButterKnife.bind(this);
        pContext = PacketScanDetails.this;

        getSupportActionBar().setTitle("Physical Inventory");



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        Intent intent=getIntent();
        if (intent.hasExtra("PIHeaderID")){
            PIHeaderID=intent.getStringExtra("PIHeaderID");
            PICode=intent.getStringExtra("PICode");
        }

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
        editor.commit();


        DestLocationMasterId=userpreferences.getString("DestLocationMasterId","");


        recycler = (RecyclerView) findViewById(R.id.list_itemDetailList);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_scan = (Button) findViewById(R.id.btn_scan);
        edt_scanPacket = (EditText) findViewById(R.id.edt_scanPacket_1);
        img_barcode = (ImageView) findViewById(R.id.img_barcode);
        len = (LinearLayout) findViewById(R.id.len);
        recycler.setHasFixedSize(true);
        adapter = new AdapterPacketDetail(putAwayDetail,PacketScanDetails.this);
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
            Location=putAwayDetails.getLocationCode();
            txt_locatn.setText(Location);
            StockDetailsId = putAwayDetails.getStockDetailsId();
            DestLocationMasterId = putAwayDetails.getDestLocationMasterId();
            DestWareHouseMasterId = putAwayDetails.getDestWareHouseMasterId();

            editor = userpreferences.edit();
            editor.putString("DestLocationMasterId",DestLocationMasterId);
            editor.commit();

        }

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PacketScanDetails.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestLocationMasterId=userpreferences.getString("DestLocationMasterId","");
                packetno = edt_scanPacket.getText().toString();
                if (packetno.contains("PN")||packetno.contains("G")) {
                    if (isnet()) {
                        ProgressHUD.show(pContext, "Fetching packet details...", true, false);

                        downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                        downloadPutAwayDetails.execute();

                    } else {

                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    edt_scanPacket.setText("");
                    Toast toast = Toast.makeText(PacketScanDetails.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDetails.this, R.raw.alert);
                    mp.start();
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


                    if (packetno.contains("PN")||packetno.contains("G")) {
                        if (isnet()) {
                            ProgressHUD.show(pContext, "Fetching packet details...", true, false);

                            downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                            downloadPutAwayDetails.execute();

                        } else {

                            Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        edt_scanPacket.setText("");
                        Toast toast = Toast.makeText(PacketScanDetails.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(PacketScanDetails.this, R.raw.alert);
                        mp.start();
                    }
                    return true;
                }
                return false;
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DestLocationMasterId=userpreferences.getString("DestLocationMasterId","");
                startActivity(new Intent(PacketScanDetails.this,AddPacketActivity.class)
                        .putExtra("PICode",PICode).
                        putExtra("PIHeaderID",PIHeaderID).
                        putExtra("code",ItemCode).
                        putExtra("desc",ItemDescription).
                        putExtra("lot",LotNo).
                        putExtra("location",LocationCode).
                        putExtra("locMasterId",DestLocationMasterId).
                        putExtra("fifo",FIFODate).
                        putExtra("StockDetailsId",StockDetailsId).
                        putExtra("ItemPlantId",ItemPlantId).
                        putExtra("Packet",packetno).
                                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });


    }

    public void clickitem() {

        startActivity(new Intent(PacketScanDetails.this, ItemCreateActivity.class)
                .putExtra("PICode",PICode).
                        putExtra("locMasterId",DestLocationMasterId).
                        putExtra("PIHeaderID",PIHeaderID).
                        putExtra("StockDetailsId",StockDetailsId).
                        putExtra("Packet",packetno));



    }


    public class DownloadPutAwayPacketDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.PacketInquiry + "?PacketNo=" + packetno;

            try {

                res = ut.OpenConnection(url, pContext);
                response = res.toString().replaceAll("\\\\", "");
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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId
            ProgressHUD.Destroy();
            edt_scanPacket.setText("");

            String s = res;

            if (res.contains("StockDetailsId")){
                try {
                    edt_scanPacket.setText("");

                    JSONArray jResults = new JSONArray(s);

                    len.setVisibility(View.VISIBLE);
                    putAwayDetail.clear();
                    for (int i = 0; i < jResults.length(); i++) {
                        PutAwayDetail putAwayDetaill = new PutAwayDetail();
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        LotNo = jsonObject.getString("LotNo");
                        putAwayDetaill.setLotNo(LotNo);

                        putAwayDetaill.setItemCode(jsonObject.getString("ItemCode"));
                        putAwayDetaill.setItemDesc(jsonObject.getString("ItemDesc"));
                        LocationMasterId = jsonObject.getString("LocationMasterId");
                        putAwayDetaill.setLocationMasterId(LocationMasterId);
                       // LocationCode = jsonObject.getString("LocationCode");
                        putAwayDetaill.setLocationCode(Location);
                        StockDetailsid = jsonObject.getString("StockDetailsId");
                        putAwayDetaill.setStockDetailsId(StockDetailsid);
                        ItemPlantId = jsonObject.getString("ItemPlantId");
                        putAwayDetaill.setItemPlantId(ItemPlantId);
                        FIFODate = jsonObject.getString("FIFODate");
                        String[] dateSplit = FIFODate.split("T");
                        FIFODate = dateSplit[0];
                        FIFODate = formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", FIFODate);
                        putAwayDetaill.setFIFODate(FIFODate);
                        putAwayDetail.add(0, putAwayDetaill);


                    }


                    adapter.update(putAwayDetail);

                    recycler.post(new Runnable() {
                        @Override
                        public void run() {
                            recycler.smoothScrollToPosition(0);
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (s.equals("[]")) {
                edt_scanPacket.setText("");
                t1.speak("Packet not present", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
                Toast toast = Toast.makeText(PacketScanDetails.this, "Record not present", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PacketScanDetails.this, R.raw.alert);
                mp.start();

                startActivity(new Intent(PacketScanDetails.this, ItemCreateActivity.class)
                        .putExtra("PICode",PICode).
                                putExtra("locMasterId",DestLocationMasterId).
                                putExtra("PIHeaderID",PIHeaderID).
                                putExtra("StockDetailsId","").
                                putExtra("Packet",packetno));

            }
             else {

                t1.speak(res, TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }

                Toast toast = Toast.makeText(PacketScanDetails.this, res, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(PacketScanDetails.this, R.raw.alert);
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

       /* if (backpressCount == 2) {
            finish();
        } else {
            transactiondialog();
        }
*/
        finish();

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


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PacketScanDetails.this);
        LayoutInflater inflater = PacketScanDetails.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.transaction_dialog_lay, null);
        dialogBuilder.setView(myView);

        Button btn_cancel = myView.findViewById(R.id.btn_cancel);
        Button btn_yes = myView.findViewById(R.id.btn_yes);
        Button button_post = myView.findViewById(R.id.button_post);


        button_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    postdata();

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PacketScanDetails.this, GRNScanner.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

  /*  private void postdata() {

        try {
            if (putAwayDetail.size() > 0) {

                for (int j = 0; j < putAwayDetail.size(); j++) {

                    qty = Double.parseDouble(putAwayDetail.get(j).getBalQty());
                    //  TotalQty = qty + TotalQty;
                    Log.d("Qty", String.valueOf(TotalQty));

                    String PackId = putAwayDetail.get(j).getPacketMasterId();

                    String poListString = PackId;
                    String[] parts = poListString.split(",");
                    StringBuilder output = new StringBuilder();

                    for (String part : parts) {
                        if (output.length() > 0) {
                            output.append(",");
                        }

                        output.append("'").append(part).append("'");
                    }

                    System.out.println(output);

                    PacketMasterId = String.valueOf(output);
                    StockDetailsid = putAwayDetail.get(j).getStockDetailsId();
                    ItemPlantId = putAwayDetail.get(j).getItemPlantId();
                    WareHouseMasterId = putAwayDetail.get(j).getWarehouseId();
                    LocationMasterId = putAwayDetail.get(j).getLocationMasterId();
                    LotNo = putAwayDetail.get(j).getLotNo();
                    // DestWareHouseMasterId = putAwayDetail.get(j).getDestWareHouseMasterId();
                    //DestLocationMasterId = putAwayDetail.get(j).getDestLocationMasterId();
                    FIFODate = putAwayDetail.get(j).getFIFODate();

                    try {
                        jsonObject1.put("SrNO", "");
                        jsonObject1.put("SrcItemMasterId", ItemPlantId);


                        jsonObject1.put("SrcTranQty", qty);
                        jsonObject1.put("SrcStockDetailId", StockDetailsid);
                        jsonObject1.put("SrcWareHouseMasterId", WareHouseMasterId);
                        jsonObject1.put("SrcLocationMasterId", LocationMasterId);
                        jsonObject1.put("SrcUOMMasterId", "");
                        jsonObject1.put("SrcLotNo", LotNo);
                        jsonObject1.put("DestWareHouseMasterId", DestWareHouseMasterId);
                        jsonObject1.put("DestLocationMasterId", DestLocationMasterId);
                        jsonObject1.put("Remark", "");
                        jsonObject1.put("FiFo", FIFODate);
                        jsonObject1.put("PacketMasterId", PacketMasterId);


                        try {
                            JSONObject a = new JSONObject(jsonObject1.toString());
                            ob.put(a);
                            jobjdata.put("PutAwayData", ob);
                        } catch (JSONException e) {

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                //Post data

                finaljson = jobjdata.toString();
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
                Toast.makeText(PacketScanDetails.this, "Minimum 1 packet required", Toast.LENGTH_SHORT).show();

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
*/

    @Override
    protected void onStop() {
        super.onStop();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

    }
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

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

                if (packetno.contains("PN")||packetno.contains("G")) {
                    if (isnet()) {
                        ProgressHUD.show(pContext, "Fetching packet details...", true, false);

                        downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                        downloadPutAwayDetails.execute();

                    } else {

                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    edt_scanPacket.setText("");
                    Toast toast = Toast.makeText(PacketScanDetails.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PacketScanDetails.this, R.raw.alert);
                    mp.start();
                }


                }

            }

            super.onActivityResult(requestCode, resultCode, data);
        }

}
