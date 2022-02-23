package com.vritti.AlfaLavaModule.activity.grn;

import android.app.Activity;
import android.content.ContentValues;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.activity.PacketNoDisplayActivity;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
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

public class GRNPOSTPACKETSacnDetails extends Activity {

    ArrayList<GRNPOST> grnpostArrayList;
    ArrayList<GRNPOST> arrayList;
    GRNPOST grnpost;
    private Context pContext;
    @BindView(R.id.edt_scanPacket)
    TextView edt_scanPacket;
    String packId,StockDetailsId,GRNDetailId="" ;
    DownloadPutAwayPacketDetails downloadPutAwayDetails;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private static RecyclerView recycler;
    private static AdapterPacketScanItemValidation adapter;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    SharedPreferences userpreferences;
    SQLiteDatabase sql;



    String PacketNo,LotNo,OrderNo,WareHouseMasterId,LocationMasterId,
            ItemPlantId,PacketMasterId,DestWareHouseMasterId,DestLocationMasterId,BalQty,FIFODate;
    private String finaljson;
    private Utility pUt;
    private String qtyUOM,ItemCode,packetnum,PartyDCNo,FinalJson="";
    private String UOM,GRNHeaderId="",GRN_NO,SupplierId;
    Button btn_post;
    TextView btn_scan;
    private TextToSpeech t1;
    private AlertDialog b;
    private TextView text;
    private Handler handler;
    int qty=0;
    AppCompatCheckBox btn_reject;
    String Flag;
    String[] separated;


    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    ImageView img_barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grnpostscan_lay);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ButterKnife.bind(this);
        pContext = GRNPOSTPACKETSacnDetails.this;
        grnpost = new GRNPOST();
        grnpostArrayList = new ArrayList<>();
        arrayList = new ArrayList<>();

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




        Intent intent=getIntent();
        GRNHeaderId=intent.getStringExtra("GRN_Header");
        GRN_NO=intent.getStringExtra("GRN_No");
        SupplierId=intent.getStringExtra("supplierId");
        PartyDCNo=intent.getStringExtra("partyDCNo");

        recycler = (RecyclerView) findViewById(R.id.list_itemDetailList);
        btn_post = (Button) findViewById(R.id.btn_post);
        btn_scan = (TextView) findViewById(R.id.btn_scan);
        btn_reject = (AppCompatCheckBox) findViewById(R.id.btn_reject);
        recycler.setHasFixedSize(true);
        adapter = new AdapterPacketScanItemValidation(arrayList, GRNPOSTPACKETSacnDetails.this);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);
        progressBar=findViewById(R.id.progressBar);
        img_barcode = findViewById(R.id.img_barcode);

        if (btn_reject.isChecked()){
            Flag="D";
        }else {
            Flag="G";
        }


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });



       // Update_GRN(GRN_NO);

        LoadItem(GRN_NO);



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
                    if (isnet()) {
                        packetnum = "";

                        try {

                            packId = edt_scanPacket.getText().toString().trim();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (packId != null && !(packId.equals(""))) {

                            String searchQuery = "SELECT  * FROM " + db.TABLE_GRNNO_PACKET + " where PacketNo='" + packId + "'";
                            Cursor cursor = sql.rawQuery(searchQuery, null);
                            int count = cursor.getCount();
                            if (count > 0) {
                                cursor.moveToFirst();
                                do {
                                    edt_scanPacket.setText("");
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                        Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
                                        View toastView = toast.getView();

                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(18);
                                        toastMessage.setTextColor(Color.RED);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(5);
                                        toastView.setBackgroundColor(Color.TRANSPARENT);
                                        toast.show();
                                    }else {
                                        Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Already scanned packet" + "</big></b></font>"), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }

                                } while (cursor.moveToNext());

                            } else {
                                if (isnet()) {
                                    edt_scanPacket.setText("");

                                    if (packId.contains("PDK")||packId.contains("NO")||packId.contains("MTR")||packId.contains("Mtr")) {
                                        qtyUOM = packId;
                                        if (qtyUOM.contains("NO")){
                                            separated= qtyUOM.split("NO");
                                        }else if (qtyUOM.contains("MTR")){
                                            separated= qtyUOM.split("MTR");
                                        }else if (qtyUOM.contains("Mtr")){
                                            separated= qtyUOM.split("Mtr");
                                        }
                                        else {
                                            separated = qtyUOM.split("PDK");
                                        }
                                        qty = Integer.parseInt(separated[0]);
                                        String Item = separated[1];
                                        if (Item.contains("PN")){
                                            String[] separated_1 = Item.split("PN");
                                            ItemCode = separated_1[0];
                                        }else {
                                            String[] separated_1 = Item.split("S");
                                            ItemCode = separated_1[0];
                                        }


                                         progressBar.setVisibility(View.VISIBLE);

                                        try {
                                            new StartSession(GRNPOSTPACKETSacnDetails.this, new CallbackInterface() {
                                                @Override
                                                public void callMethod() {
                                                    downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                                    downloadPutAwayDetails.execute(); }

                                                @Override
                                                public void callfailMethod(String msg) {

                                                }


                                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }



                                    } else {
                                        Toast.makeText(GRNPOSTPACKETSacnDetails.this, "PDK/NO not found in string", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }

                    return true;
                }
                return false;
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {

                    packId=edt_scanPacket.getText().toString();
                    if (packId.contains("PDK")||packId.contains("NO")) {
                        qtyUOM = packId;
                        if (qtyUOM.contains("NO")){
                            separated= qtyUOM.split("NO");
                        }else if (qtyUOM.contains("MTR")){
                            separated= qtyUOM.split("MTR");
                        }else if (qtyUOM.contains("Mtr")){
                            separated= qtyUOM.split("Mtr");
                        }
                        else {
                            separated = qtyUOM.split("PDK");
                        }
                        qty = Integer.parseInt(separated[0]);
                        String Item = separated[1];
                        if (Item.contains("PN")){
                            String[] separated_1 = Item.split("PN");
                            ItemCode = separated_1[0];
                        }else {
                            String[] separated_1 = Item.split("S");
                            ItemCode = separated_1[0];
                        }




                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            new StartSession(GRNPOSTPACKETSacnDetails.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                    downloadPutAwayDetails.execute(); }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    } else {
                        Toast.makeText(GRNPOSTPACKETSacnDetails.this, "PDK/NO not found in string", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }




        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    if (isnet()) {
                        //ProgressHUD.show(pContext, "Sending data please wait...", true, false);

                        progressBar.setVisibility(View.VISIBLE);
                        try {
                            new StartSession(GRNPOSTPACKETSacnDetails.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GRNUpLoadData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

        });

       btn_reject.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   Flag="D";
              }else {
                   Flag="G";
               }
           }
       });
        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(GRNPOSTPACKETSacnDetails.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();

            }
        });


    }

    private void getdeletedialog(final String packetNo) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GRNPOSTPACKETSacnDetails.this);
        LayoutInflater inflater = GRNPOSTPACKETSacnDetails.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.packet_delete_dialog, null);
        dialogBuilder.setView(myView);

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sql.delete(db.TABLE_GRN_POST, "PacketNo=?", new String[]{packetNo});
                LoadItem(GRN_NO);
                b.dismiss();




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

    public void setpacket(String packetNo) {

        getdeletedialog(packetNo);

    }

    public void showpackets(String grnheader) {


        startActivity(new Intent(GRNPOSTPACKETSacnDetails.this,
                PacketNoDisplayActivity.class)
                .putExtra("header",grnheader)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));



    }



    private class GRNUpLoadData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String strRes = null;
            String url =CompanyURL+
                    WebUrlClass.PostToStock+"?GRNHeaderId="+GRNHeaderId;
            try {
                res = ut.OpenConnection(url, GRNPOSTPACKETSacnDetails.this);
               response = res.toString().replaceAll("\\\\", "");

            } catch (final Exception e) {

                handler.post(new Runnable()
                {
                    public void run() {
                        Toast.makeText(GRNPOSTPACKETSacnDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

            if (s.contains("true")) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Data Send Successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();

                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Data Send Successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                sql.delete(db.TABLE_GRN_POST, "GRNHeaderId=?", new String[]{GRNHeaderId});
                btn_post.setVisibility(View.GONE);
                finish();


            }

            else {



              /*  Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Record not save", Toast.LENGTH_LONG);
                View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();*/


            }


        }

    }





    public class DownloadPutAwayPacketDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            //   String url = pUt.getSharedPreference_URL(GRNPOSTPACKETSacnDetails.this) + WebUrlClass.checkValidPacketNo+"?PacketNo="+"S"+packetnum;

       //     String url = pUt.getSharedPreference_URL(GRNPOSTPACKETSacnDetails.this) + WebUrlClass.checkValidPacketNo+"?PacketNo="+packId;

            String url = CompanyURL +
                    WebUrlClass.checkValidPacketNo_Item+"?PacketNo="+packId+
                    "&ItemCode="+ItemCode+"&GRNHeaderId="+GRNHeaderId+"&Flag="+Flag+"&PacketSrNo="+"&PacketQty="+qty;

            try {

                res = ut.POSTOpenConnection(url, GRNPOSTPACKETSacnDetails.this);

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

            String s = res;

            if (s.contains("true")){

                GRNPOST grnpost_1 = new GRNPOST();
                grnpost_1.setPacketNo(packId);
                grnpost_1.setGRNHeaderId(GRNHeaderId);
                cf.Insert_GRNPACKETS(grnpost_1);

                try {

                    grnpost.setGRNNo(GRN_NO);
                    grnpost.setGRNHeaderId(GRNHeaderId);
                    grnpost.setPacketNo(packId);
                    grnpost.setItemCode(ItemCode);
                    grnpost.setQuantity(qty);
                    grnpost.setUOM("NO");
                    cf.Insert_GRNPACKET(grnpost);


                    if (arrayList.size() > 0) {
                        for (int j = 0; j < arrayList.size(); j++) {
                            if (arrayList.get(j).getItemCode().equals(ItemCode)) {
                                if (Flag.contains("D")) {
                                    int current = arrayList.get(j).getRejQty();
                                    ContentValues values = new ContentValues();
                                    int currentTotal = current + qty;
                                    values.put("RejQty", currentTotal);
                                    sql.update(db.TABLE_GRN_POST_ITEM, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                                } else {
                                    int current = arrayList.get(j).getQuantity();
                                    ContentValues values = new ContentValues();
                                    int currentTotal = current + qty;
                                    values.put("Quantity", currentTotal);
                                    sql.update(db.TABLE_GRN_POST_ITEM, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                                }

                                onResume();
                            }
                        }
                    }
                    MediaPlayer mp = MediaPlayer.create(GRNPOSTPACKETSacnDetails.this, R.raw.ok);
                    mp.start();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Packet scan successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Packet scan successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                    t1.speak("Packet scan successfully", TextToSpeech.QUEUE_FLUSH, null);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {

                MediaPlayer mp = MediaPlayer.create(GRNPOSTPACKETSacnDetails.this, R.raw.alert);
                mp.start();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, s, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + s + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
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
        super.onBackPressed();





    }

    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }



    private void LoadItem(String GRN) {
        arrayList.clear();
        try {


            String que = "SELECT * FROM " + db.TABLE_GRN_POST_ITEM + " WHERE  GRNNo ='" + GRN + "'";
            Cursor c = sql.rawQuery(que, null);
            if (c.getCount() == 0) {

                recycler.setVisibility(View.GONE);
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new StartSession(GRNPOSTPACKETSacnDetails.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            GetPacketItemDetails getPacketItemDetails = new GetPacketItemDetails();
                            getPacketItemDetails.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                c.moveToFirst();
                do {
                    recycler.setVisibility(View.VISIBLE);
                    GRNPOST bean = new GRNPOST();
                    bean.setGRNNo(c.getString(c.getColumnIndex("GRNNo")));
                    bean.setGRNHeaderId(c.getString(c.getColumnIndex("GRNHeaderId")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setQuantity(c.getInt(c.getColumnIndex("Quantity")));
                    bean.setChallanQty(c.getInt(c.getColumnIndex("ChallanQty")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setRejQty(c.getInt(c.getColumnIndex("RejQty")));
                    bean.setInvoiceNo(c.getString(c.getColumnIndex("InvoiceNo")));
                    bean.setCustomerName(c.getString(c.getColumnIndex("CustomerName")));
                    arrayList.add(bean);

                } while (c.moveToNext());
                adapter.update(arrayList);

                if (arrayList.size()>0){
                    btn_post.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private class GetPacketItemDetails extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            Object res;
            String response;

            String url = CompanyURL + WebUrlClass.GetGRNItemDetails + "?GRNHeaderId="+GRNHeaderId;
            try {
                res = ut.OpenConnection(url, GRNPOSTPACKETSacnDetails.this);
                response = res.toString();
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayList.clear();
            if (s.contains("ItemCode")) {
                try {
                    progressBar.setVisibility(View.GONE);
                    JSONArray jResults = new JSONArray(s);

                    cf.DeleteAllRecord(db.TABLE_GRN_POST_ITEM);

                    for (int i=0;i<jResults.length();i++){
                        GRNPOST grnpost=new GRNPOST();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        String GRNNo=jsonObject.getString("GRNNo");
                        grnpost.setGRNNo(GRNNo);
                        double challanQty=jsonObject.getDouble("ChallanQty");
                        int ChallanQty= (int) challanQty;
                        grnpost.setChallanQty(ChallanQty);
                        grnpost.setQuantity(jsonObject.getInt("GRNQty"));
                        grnpost.setRejQty(jsonObject.getInt("RejQty"));
                        grnpost.setItemCode(jsonObject.getString("ItemCode"));
                        grnpost.setItemMasterId(jsonObject.getString("ItemMasterId"));
                        grnpost.setGRNHeaderId(jsonObject.getString("GRNHeaderId"));
                        grnpost.setGRNDetailId(jsonObject.getString("GRNDetailId"));
                        grnpost.setInvoiceNo(jsonObject.getString("InvoiceNo"));
                        grnpost.setCustomerName(jsonObject.getString("CustomerName"));
                        cf.Insert_GRNPACKETITEM(grnpost);

                    }

                         LoadItem(GRN_NO);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (s.equals("[]")){
                progressBar.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Record not found", Toast.LENGTH_LONG);
                    View toastView = toast.getView();

                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Record not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                t1.speak("Record not found", TextToSpeech.QUEUE_FLUSH, null);

            }

            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(pContext, s, Toast.LENGTH_LONG).show();
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoadItem(GRN_NO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data1);
        if (result != null) {
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");


                packId = result.getContents().toString();

                if (isnet()) {

                    try {

//                        packId = edt_scanPacket.getText().toString().trim();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (packId != null && !(packId.equals(""))) {

                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRNNO_PACKET + " where PacketNo='" + packId + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                edt_scanPacket.setText("");
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();

                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                }else {
                                    Toast toast = Toast.makeText(GRNPOSTPACKETSacnDetails.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Already scanned packet" + "</big></b></font>"), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }

                            } while (cursor.moveToNext());

                        } else {
                            if (isnet()) {
                                edt_scanPacket.setText("");

                                if (packId.contains("PDK")||packId.contains("NO")||packId.contains("MTR")||packId.contains("Mtr")) {
                                    qtyUOM = packId;
                                    if (qtyUOM.contains("NO")){
                                        separated= qtyUOM.split("NO");
                                    }else if (qtyUOM.contains("MTR")){
                                        separated= qtyUOM.split("MTR");
                                    }else if (qtyUOM.contains("Mtr")){
                                        separated= qtyUOM.split("Mtr");
                                    }
                                    else {
                                        separated = qtyUOM.split("PDK");
                                    }
                                    qty = Integer.parseInt(separated[0]);
                                    String Item = separated[1];
                                    if (Item.contains("PN")){
                                        String[] separated_1 = Item.split("PN");
                                        ItemCode = separated_1[0];
                                    }else {
                                        String[] separated_1 = Item.split("S");
                                        ItemCode = separated_1[0];
                                    }


                                    progressBar.setVisibility(View.VISIBLE);

                                    try {
                                        new StartSession(GRNPOSTPACKETSacnDetails.this, new CallbackInterface() {
                                            @Override
                                            public void callMethod() {
                                                downloadPutAwayDetails = new DownloadPutAwayPacketDetails();
                                                downloadPutAwayDetails.execute(); }

                                            @Override
                                            public void callfailMethod(String msg) {

                                            }


                                        });
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }



                                } else {
                                    Toast.makeText(GRNPOSTPACKETSacnDetails.this, "PDK/NO not found in string", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        }
    }

}
