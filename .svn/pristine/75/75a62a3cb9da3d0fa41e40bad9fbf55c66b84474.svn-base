package com.vritti.AlfaLavaModule.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.adapter.AdapteMRSDetail;
import com.vritti.AlfaLavaModule.adapter.AdapterPicklistDetail;
import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.MRSDetailBean;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PickListDetail;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.AlfaLavaModule.bean.PrinterName;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.UserListAdapter;
import com.vritti.vwb.Beans.UserList;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.AssetTransferActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PickListDetailActivity extends AppCompatActivity {


    private static RecyclerView recycler;
    private static AdapterPicklistDetail adapter;
    private static Paint p = new Paint();
    private  ArrayList<PickListDetail> list;
    static View Creatview;
    private DownloadPicklistItem dowmloaditem;
    MenuItem chk;
    ArrayList<String> barcodecontent;
    static boolean DoNOTShowPopup = false;
    MenuItem menuItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject J_obj;
    String GrnId = "";
    Spinner s_search;
    private EditText s_search1;
    private AlertDialog dialog;



    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private String ItemCode,PacketNo="",PacketMasterId="";
    private double ReqQty;

    private String Pick_ListHdrId="",PickListNo="",
            Pick_ListDtlId="",SoScheduleId="",ItemMasterId="",
            QtyToPick="",QtyPicked="",Pick_listSuggLotId="",
            Pick_listDtlId="",StockDetailsId="",QtyPickPosted="",ItemDesc="",FinalJson="",LocationCode="";

    ImageView img_search;
    private String  barcode;
    TextView txt_do,txt_qty;
    private String DONumber="",HeaderId="";
    private Spinner printername;
    ArrayList<PrinterName>printerNameArrayList;
    private Adapter_PrinterName adapterPrinterName;
    private String printerName;
    private AlertDialog b;

    //4368534

    int currentTotal=0;
    int currentQty=0;
    int Qty=0;
    int PickedQty=0;
    private String data;
    private TextToSpeech t1;
    private ProgressBar progress;

    int QtyTopick,BalToPick,FiFoQtyBalanceTopick,LotQtyBalanceofFiFo,FiFoQtyToPick,LotQtyToPick;
    Button btn_finish;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklist_dodropdown_lay);
        getSupportActionBar().setTitle("Picklist Details");

        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(PickListDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(PickListDetailActivity.this);
        String dabasename = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PickListDetailActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(PickListDetailActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        progress=findViewById(R.id.progress);


        DONumber=getIntent().getStringExtra("dono");
        HeaderId=getIntent().getStringExtra("headerid");

        printerNameArrayList=new ArrayList<>();
        initView();

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });








        if (Check_Db()) {
            detailPacket(DONumber);
        } else {


            if (isnet()) {
                progress.setVisibility(View.VISIBLE);
                new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dowmloaditem = new DownloadPicklistItem();
                        dowmloaditem.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });

            } else {
                Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }



        s_search1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (s_search1 != null) {
                                s_search1.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay

                    data = s_search1.getText().toString();


                    if (EnvMasterId.equals("karldungs")||EnvMasterId.equals("karldungsuat")){
                        try {

                           /* String[] separated = data.split("PDK");
                            Qty = Integer.parseInt(separated[0]);
                            String Item= separated[1];
                            String[] separated_1 = Item.split("S");
                            ItemCode = separated_1[0];
                            filter(ItemCode, data);*/

                            data = s_search1.getText().toString();
                            PacketNo=data;
                            if (data != null && !(data.equals(""))) {
                                String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                                Cursor cursor = sql.rawQuery(searchQuery, null);
                                int count = cursor.getCount();
                                if (count > 0) {
                                    cursor.moveToFirst();
                                    do {
                                        s_search1.setText("");
                                        Toast toast = Toast.makeText(PickListDetailActivity.this, "Already scanned packet", Toast.LENGTH_LONG);
                                        View toastView = toast.getView();
                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(18);
                                        toastMessage.setTextColor(Color.RED);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(5);
                                        toastView.setBackgroundColor(Color.TRANSPARENT);
                                        toast.show();
                                    } while (cursor.moveToNext());

                                }
                            }else {

                                if (isnet()) {
                                    try {
                                        progress.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new GetPacketValidation().execute();
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {
                                        }


                                    });

                                } else {
                                    Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }catch (Exception e){
                        e.printStackTrace();
                        }
                    }else {
                        try {
                            if (Constants.type == Constants.Type.Alfa) {

                                JSONObject jsonObject = new JSONObject(data);

                                ItemCode = jsonObject.getString("Itemcode");
                                PacketNo = jsonObject.getString("PacketNo");
                                Qty = Integer.parseInt(jsonObject.getString("PacketQty"));

                                s_search1.setText(PacketNo);


                                filter(ItemCode, PacketNo);
                            } else {
                                filter("", data);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    // convertToGson(data);

                    return true;
                } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                    String data = s_search1.getText().toString();
                    return true;
                }
                return false;
            }
        });

    }

    private void initView() {

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        s_search1 = findViewById(R.id.s_search1);
        txt_do = findViewById(R.id.txt_do);
        txt_qty = findViewById(R.id.txt_qty);
        img_search = findViewById(R.id.img_search);

        txt_do.setText("DN - " + DONumber);

        s_search1.setHint("Scan Packet No");




        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PickListDetailActivity.this);
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        barcodecontent = new ArrayList<String>();
        btn_finish=findViewById(R.id.btn_finish);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PickListDetailActivity.this,DOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=s_search1.getText().toString();
                if (EnvMasterId.equals("karldungs")||EnvMasterId.equals("karldungsuat")){

                    try {
                       /* packet=s_search1.getText().toString();
                        String[] separated = packet.split("PDK");
                        Qty = Integer.parseInt(separated[0]);
                        String Item= separated[1];
                        String[] separated_1 = Item.split("S");
                        ItemCode = separated_1[0];
                        filter(ItemCode, packet);*/


                        if (data != null && !(data.equals(""))) {
                            String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                            Cursor cursor = sql.rawQuery(searchQuery, null);
                            int count = cursor.getCount();
                            if (count > 0) {
                                cursor.moveToFirst();
                                do {
                                    s_search1.setText("");
                                    Toast.makeText(PickListDetailActivity.this, "Packet already scanned", Toast.LENGTH_SHORT).show();

                                } while (cursor.moveToNext());

                            }
                        }else {

                        if (isnet()) {
                            //   progress.setVisibility(View.VISIBLE);
                            ProgressHUD.show(PickListDetailActivity.this, "Sending data please wait ...", true, false);
                            new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetPacketValidation().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                }


                            });

                        } else {
                            Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if (data != null && !(data.equals(""))) {
                    String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                    Cursor cursor = sql.rawQuery(searchQuery, null);
                    int count = cursor.getCount();
                    if (count > 0) {
                        cursor.moveToFirst();
                        do {
                            s_search1.setText("");
                            Toast.makeText(PickListDetailActivity.this, "Packet already scanned", Toast.LENGTH_SHORT).show();

                        } while (cursor.moveToNext());

                    } else {


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("PacketNo", data);
                            FinalJson = jsonObject.toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    s_search1.setText("");
                                    new UpLoadData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    new UpLoadData().execute();
                                }


                            });

                        } else {
                            Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }
        });
    }


    public void detailPacket(String DO) {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_CARTAN_PICKLIST + "' WHERE DONumber=?", new String[]{String.valueOf(DO)});


            if (c.getCount()>0) {
                c.moveToFirst();
                do {
                    recycler.setVisibility(View.VISIBLE);

                    PickListDetail pickListDetail = new PickListDetail();
                    pickListDetail.setPick_ListDtlId(c.getString(c.getColumnIndex("Pick_ListHdrId")));
                    pickListDetail.setPickListNo(c.getString(c.getColumnIndex("PickListNo")));
                    pickListDetail.setPick_ListDtlId(c.getString(c.getColumnIndex("Pick_ListDtlId")));
                    pickListDetail.setSoScheduleId(c.getString(c.getColumnIndex("SoScheduleId")));
                    pickListDetail.setItemMasterId(c.getString(c.getColumnIndex("ItemMasterId")));
                    pickListDetail.setQtyToPick(c.getString(c.getColumnIndex("QtyToPick")));
                    pickListDetail.setQtyPicked(c.getString(c.getColumnIndex("QtyPicked")));
                    pickListDetail.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    pickListDetail.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    pickListDetail.setStockDetailsId(c.getString(c.getColumnIndex("StockDetailsId")));
                    pickListDetail.setFlag(c.getString(c.getColumnIndex("Flag")));
                    pickListDetail.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    list.add(pickListDetail);


                } while (c.moveToNext());



                adapter = new AdapterPicklistDetail(list, PickListDetailActivity.this);
                recycler.setAdapter(adapter);
                adapter.update(list);

            }else {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                            }


                    });

                } else {
                    Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }



        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (Check_Db()) {

            detailPacket(DONumber);
        }

    }
    public Boolean Check_Db() {
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_CARTAN_PICKLIST, null);
            if (c.getCount() > 0) {

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    public void Qtytotal(double currentTotal) {
        String qty= String.valueOf(currentTotal);
        String TotalPick = String.valueOf(qty).split("\\.")[0];
        txt_qty.setVisibility(View.VISIBLE);
        txt_qty.setText("Total Pick :"+TotalPick);

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
            String url = CompanyURL + WebUrlClass.api_POSTValidatePacket;
            try {
                res = ut.OpenPostConnection(url,FinalJson, PickListDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //   ProgressHUD.Destroy();
            try {
                progress.setVisibility(View.GONE);
                if (s.contains("Success")) {
                    Packet grnpost_1 = new Packet();
                    grnpost_1.setPacketNo(PacketNo);
                    cf.Insert_GRNPACKETNO(grnpost_1);

                    ContentValues values_1 = new ContentValues();
                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getItemCode().equals(ItemCode)) {
                                String current = String.valueOf(list.get(j).getQtyPicked());
                                String[] namesList = current.split(".0");
                                currentQty = Integer.parseInt(namesList[0]);
                                currentTotal = currentQty + Qty;
                                values_1.put("QtyPicked", currentTotal);
                                sql.update(db.TABLE_CARTAN_PICKLIST, values_1, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                                onResume();
                            }
                        }
                    }
                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.ok);
                    mp.start();
                    Toast toast = Toast.makeText(PickListDetailActivity.this, "Data send successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();


                } else if (s.contains("Failed")) {
                    Packet grnpost_1 = new Packet();
                    grnpost_1.setPacketNo(PacketNo);
                    cf.Insert_GRNPACKETNO(grnpost_1);

                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        String Error = jsonObject.getString("ERROR");
                        if (Error.contains("Packet not found")) {
                            Toast.makeText(PickListDetailActivity.this, "Packet not found", Toast.LENGTH_SHORT).show();
                            t1.speak("Packet not found", TextToSpeech.QUEUE_FLUSH, null);
                            if (t1 != null) {
                                t1.stop();
                                t1.shutdown();
                            }
                        } else {
                            String[] separated = Error.split(":");
                            try {
                                ReqQty = Double.parseDouble(separated[1]);
                                getdeletedialog();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(PickListDetailActivity.this, "Technical error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    onResume();


                } else {
                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        String Error = jsonObject.getString("ERROR");
                        Toast.makeText(PickListDetailActivity.this, Error, Toast.LENGTH_LONG).show();
                        onResume();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class DownloadPicklistItem extends AsyncTask<String, String, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {

          //  PackOrderNo="INV/19-20/018";
            String url = CompanyURL + WebUrlClass.api_GetPickingOrdDetails + "?PickedOrdNo=" + DONumber.trim();

            try {

                 res = ut.OpenConnection(url, PickListDetailActivity.this);
                 response=res.toString();
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
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);


            if (!(s.equalsIgnoreCase("[]"))) {
                if (s.contains("Pick_ListHdrId")) {
                    try {
                        list.clear();
                       cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);


                        JSONObject jsonObject=new JSONObject(s);

                        PickListDetail pickListDetail=new PickListDetail();

                        JSONArray pick_ListHdr=jsonObject.getJSONArray("Pick_ListHdr");
                        if (pick_ListHdr.length()>0) {

                            for (int i=0;i<pick_ListHdr.length();i++){
                                JSONObject pickjsonObject=pick_ListHdr.getJSONObject(i);
                                Pick_ListHdrId=pickjsonObject.getString("Pick_ListHdrId");
                                pickListDetail.setPick_ListHdrId(Pick_ListHdrId);
                                PickListNo=pickjsonObject.getString("PickListNo");
                                pickListDetail.setPickListNo(PickListNo);
                              //  pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);

                            }

                        }
                        JSONArray pick_ListDtl=jsonObject.getJSONArray("Pick_ListDtl");
                        if (pick_ListDtl.length()>0) {

                            for (int i=0;i<pick_ListDtl.length();i++){
                                JSONObject pick_ListDtljsonObject=pick_ListDtl.getJSONObject(i);
                                Pick_ListDtlId=pick_ListDtljsonObject.getString("Pick_ListDtlId");
                                pickListDetail.setPick_ListDtlId(Pick_ListDtlId);
                                SoScheduleId=pick_ListDtljsonObject.getString("SoScheduleId");
                                pickListDetail.setSoScheduleId(SoScheduleId);
                                ItemMasterId=pick_ListDtljsonObject.getString("ItemMasterId");
                                pickListDetail.setItemMasterId(ItemMasterId);
                                QtyToPick=pick_ListDtljsonObject.getString("QtyToPick");
                                pickListDetail.setQtyToPick(QtyToPick);
                                QtyPicked=pick_ListDtljsonObject.getString("QtyPicked");
                                pickListDetail.setQtyPicked(QtyPicked);
                                ItemCode=pick_ListDtljsonObject.getString("ItemCode");
                                pickListDetail.setItemCode(ItemCode);
                                ItemDesc=pick_ListDtljsonObject.getString("ItemDesc");
                                pickListDetail.setItemDesc(ItemDesc);
                                LocationCode=pick_ListDtljsonObject.getString("LocationCode");
                                pickListDetail.setLocationCode(LocationCode);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                pickListDetail.setDONumber(DONumber);

                                cf.InsertPickingOrderDetails(pickListDetail);
                               // cf.InsertPickingOrderDetails(pickListDetail);
                            }

                        }
                        JSONArray pick_listSuggLotId=jsonObject.getJSONArray("Pick_ListSuggLot");
                        if (pick_listSuggLotId.length()>0) {

                            for (int i=0;i<pick_listSuggLotId.length();i++){
                                JSONObject Pick_ListSuggLotId=pick_listSuggLotId.getJSONObject(i);
                                Pick_listSuggLotId=Pick_ListSuggLotId.getString("Pick_listSuggLotId");
                                pickListDetail.setPick_listSuggLotId(Pick_listSuggLotId);
                                Pick_listDtlId=Pick_ListSuggLotId.getString("Pick_listDtlId");
                                pickListDetail.setPick_listDtlId(Pick_listDtlId);
                                StockDetailsId=Pick_ListSuggLotId.getString("StockDetailsId");
                                pickListDetail.setStockDetailsId(StockDetailsId);
                                //pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                //cf.InsertPickingOrderDetails(pickListDetail);

                            }

                        }





                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    detailPacket(DONumber);
                    progress.setVisibility(View.GONE);

                } else {
                    cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);
                    detailPacket(DONumber);
                    Toast toast = Toast.makeText(PickListDetailActivity.this, "Record not found", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    progress.setVisibility(View.GONE);

                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                    mp.start();



                }

            } else {
                cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);
                Toast toast = Toast.makeText(PickListDetailActivity.this, "Record not found", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();
                progress.setVisibility(View.GONE);
                final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                mp.start();

            }
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and nameuom
        void onFragmentInteraction(Uri uri);
    }


    private void filter(String data,String packet) {
        try {
            ArrayList<PickListDetail> dummyList = new ArrayList<>();
            if (Constants.type == Constants.Type.Alfa) {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItemCode().equals(data)) {
                        dummyList.add(list.get(i));
                        adapter.update(dummyList);
                    }

                }


                if (dummyList.size() > 0) {

                    Toast.makeText(PickListDetailActivity.this, "Item found!!", Toast.LENGTH_SHORT).show();


                    if (packet != null && !(packet.equals(""))) {
                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + packet + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                s_search1.setText("");
                                Toast.makeText(PickListDetailActivity.this, "Packet already scanned", Toast.LENGTH_SHORT).show();
                                t1.speak("Packet already scanned", TextToSpeech.QUEUE_FLUSH, null);
                                if (t1 != null) {
                                    t1.stop();
                                    t1.shutdown();
                                }
                            } while (cursor.moveToNext());

                        } else {


                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("PacketNo", packet);
                                FinalJson = jsonObject.toString();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (isnet()) {
                                progress.setVisibility(View.VISIBLE);
                                //  ProgressHUD.show(PickListDetailActivity.this, "Sending data please wait ...", true, false);
                                new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new UpLoadData().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        new UpLoadData().execute();
                                    }


                                });

                            } else {
                                Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                } else {
                    Toast.makeText(PickListDetailActivity.this, "Wrong item scanned", Toast.LENGTH_SHORT).show();

                }
            } else {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItemCode().equals(data)) {
                        dummyList.add(list.get(i));
                        adapter.update(dummyList);
                    }

                }


                if (dummyList.size() > 0) {


                    if (packet != null && !(packet.equals(""))) {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("PacketNo", packet);
                            FinalJson = jsonObject.toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new UpLoadData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    // new UpLoadData().execute();
                                }


                            });

                        } else {
                            Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                } else {
                    Toast.makeText(PickListDetailActivity.this, "Wrong item scanned!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    s_search1.setText("");
    }

    public boolean isnet () {
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
        finish();


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_refresh:
                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            dowmloaditem = new DownloadPicklistItem();
                            dowmloaditem.execute();
                        }


                    });

                } else {
                    Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private class UpLoadSplitData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetPacketSplitUpdate+"?PacketNo="+PacketNo+"&RemovedQty="+ReqQty+"&PrinterName="+ URLEncoder.encode(printerName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url,PickListDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.setVisibility(View.GONE);
            if (s.equalsIgnoreCase("ok")) {
                b.dismiss();
                ContentValues values = new ContentValues();
                String aa = WebUrlClass.DoneFlag_Complete;
                values.put("Flag", aa);
                sql.update(db.TABLE_CARTAN_PICKLIST, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                onResume();
                Toast.makeText(PickListDetailActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
                b.dismiss();
            } else {
                Toast.makeText(PickListDetailActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
                onResume();
                b.dismiss();
            }
        }
    }
    class DownloadPrinterData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetPrinterName;

            try {
                res = ut.OpenConnection(url, PickListDetailActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    printerNameArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        PrinterName userList = new PrinterName();
                        JSONObject jorder = jResults.getJSONObject(i);
                        userList.setPrinterName(jorder.getString("PrinterName"));
                        printerNameArrayList.add(userList);


                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            progress.setVisibility(View.GONE);

            if (response.contains("[]")) {

                Toast.makeText(PickListDetailActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PickListDetailActivity.this);
                LayoutInflater inflater = PickListDetailActivity.this.getLayoutInflater();
                final View myView = inflater.inflate(R.layout.printername_lay, null);
                dialogBuilder.setView(myView);
                printername = (Spinner) myView .findViewById(R.id.spinner_printer);

                adapterPrinterName = new Adapter_PrinterName(PickListDetailActivity.this, printerNameArrayList);
                printername.setAdapter(adapterPrinterName);
                printername.setSelection(0,false);


                printername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        printerName=printerNameArrayList.get(position).getPrinterName();

                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new UpLoadSplitData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    new UpLoadSplitData().execute();

                                }


                            });

                        } else {
                            Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                b = dialogBuilder.create();
                b.show();

            }


        }
    }


    private class GetPacketValidation extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_CheckPacketValidationNew+"?Pick_ListHdrId="+HeaderId+"&PacketNo="+data;

            try {
                res = ut.OpenConnection(url, PickListDetailActivity.this);
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

                s_search1.setText("");

                if (s.contains("true")){

                    Packet grnpost_1 = new Packet();
                    grnpost_1.setPacketNo(PacketNo);
                    cf.Insert_GRNPACKETNO(grnpost_1);

                    String[] separated = s.split("@");
                    String Item=separated[1];
                    String[] sep1 = Item.split("#");
                    ItemCode=sep1[0];
                    Qty= Integer.parseInt(sep1[1]);

                    if (list.size() > 0) {
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j).getItemCode().equals(ItemCode)) {
                                String Picked = String.valueOf(list.get(j).getQtyPicked());
                                String Pick = String.valueOf(list.get(j).getQtyToPick());

                                if (Pick.equals(Picked)) {
                                    Toast.makeText(PickListDetailActivity.this, "Picking completed for " + ItemCode, Toast.LENGTH_SHORT).show();
                                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.ok);
                                    mp.start();

                                } else {

                                    filter(ItemCode, data);
                                }
                            }
                        }

                    }
                }else  if (s.contains("PacketMasterId")) {

                    JSONArray jResults = new JSONArray(s);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        Qty = jsonObject.getInt("BalQty");
                        PickedQty = jsonObject.getInt("PickedQty");
                        PacketNo = jsonObject.getString("PacketNo");
                        PacketMasterId = jsonObject.getString("PacketMasterId");

                        t1.speak(PacketNo+" already scanned", TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }

                        Toast toast = Toast.makeText(PickListDetailActivity.this, PacketNo+" already scanned", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.GREEN);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();

                        final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                        mp.start();
                    }
                } else if(s.contains("Picking completed")){
                    t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                    Toast toast = Toast.makeText(PickListDetailActivity.this, s, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.ok);
                    mp.start();
                }

                else {
                    t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                    if (t1 != null) {
                        t1.stop();
                        t1.shutdown();
                    }
                    Toast toast = Toast.makeText(PickListDetailActivity.this, s, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                    mp.start();

                   /* if (s.contains("PacketMasterId")) {
                        try {

                            JSONArray jResults = new JSONArray(s);
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jsonObject = jResults.getJSONObject(i);
                                Qty = jsonObject.getInt("BalQty");
                                ItemCode = jsonObject.getString("ItemCode");
                                int BalToPick = jsonObject.getInt("BalToPick");
                                int FiFoQtyBalanceTopick = jsonObject.getInt("FiFoQtyBalanceTopick");
                                FiFoQtyToPick = jsonObject.getInt("FiFoQtyToPick");
                                LotQtyToPick = jsonObject.getInt("LotQtyToPick");
                                double QtyToPick = jsonObject.getInt("QtyToPick");

                                if (QtyToPick == 0 || QtyToPick == 0.0) {
                                    Toast toast = Toast.makeText(PickListDetailActivity.this, "Item " + ItemCode + " not in picklist", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastView.setBackgroundColor(Color.WHITE);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                                    mp.start();
                                } else if (BalToPick == 0) {
                                    Toast toast = Toast.makeText(PickListDetailActivity.this, "Picking completed for " + ItemCode, Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastView.setBackgroundColor(Color.WHITE);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                                    mp.start();
                                } else if (FiFoQtyBalanceTopick == 0 || FiFoQtyBalanceTopick == 0.0) {
                                    Toast toast = Toast.makeText(PickListDetailActivity.this, "FIFO of " + data + " not in suggested LOT", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastView.setBackgroundColor(Color.WHITE);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.alert);
                                    mp.start();
                                } else {
                                    if (list.size() > 0) {
                                        for (int j = 0; j < list.size(); j++) {
                                            if (list.get(j).getItemCode().equals(ItemCode)) {
                                                String Picked = String.valueOf(list.get(j).getQtyPicked());
                                                String Pick = String.valueOf(list.get(j).getQtyToPick());

                                                if (Pick.equals(Picked)) {
                                                    Toast.makeText(PickListDetailActivity.this, "Picking completed for " + ItemCode, Toast.LENGTH_SHORT).show();
                                                    final MediaPlayer mp = MediaPlayer.create(PickListDetailActivity.this, R.raw.ok);
                                                    mp.start();

                                                } else {

                                                    filter(ItemCode, data);
                                                }
                                            }
                                        }

                                    }
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }*/
                }


                progress.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(PickListDetailActivity.this))
                return true;
            else
                openAndroidPermissionsMenu();
        }
        return false;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    private void getdeletedialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PickListDetailActivity.this);
        LayoutInflater inflater = PickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(PickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadPrinterData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(PickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

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
}

