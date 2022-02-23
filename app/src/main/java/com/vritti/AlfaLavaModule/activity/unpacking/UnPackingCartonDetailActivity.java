package com.vritti.AlfaLavaModule.activity.unpacking;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.AdapterCartonDetail;
import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.bean.CartonDetail;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PrinterName;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class UnPackingCartonDetailActivity extends AppCompatActivity {


    private static RecyclerView recycler;
    private static AdapterCartonDetail adapter;
    private  ArrayList<CartonDetail> list;
    private DownloadPicklistItem dowmloaditem;

    private JSONObject J_obj;
    private EditText s_search1;



    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private String ItemCode,PacketNo="";
    private double ReqQty;


    ImageView img_search;
    private String  barcode;
    TextView txt_do,txt_qty;
    private String DONumber="",HeaderId="";
    private Spinner printername;
    ArrayList<PrinterName>printerNameArrayList;
    private Adapter_PrinterName adapterPrinterName;
    private String printerName="";
    private AlertDialog b;

    private String data;
    private TextToSpeech t1;
    private ProgressBar progress;

    Button btn_finish;
    private String Flag="",PackOrderHeadrId="";
    private String finaljson;
    Handler mHandler = new Handler();
    private  ArrayList<CartonDetail> cartondetailslist;
    CartonDetail cartonDetail=new CartonDetail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklist_dodropdown_lay);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(UnPackingCartonDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(UnPackingCartonDetailActivity.this);
        String dabasename = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(UnPackingCartonDetailActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(UnPackingCartonDetailActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        progress=findViewById(R.id.progress);


        DONumber=getIntent().getStringExtra("dono");
        HeaderId=getIntent().getStringExtra("headerid");
        Flag=getIntent().getStringExtra("Flag");
        PackOrderHeadrId=getIntent().getStringExtra("PackHdrid");


        getSupportActionBar().setTitle(DONumber);


        initView();


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });




            if (isnet()) {
                progress.setVisibility(View.VISIBLE);
                new StartSession(UnPackingCartonDetailActivity.this, new CallbackInterface() {
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
                Toast.makeText(UnPackingCartonDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

                    if (data != null && !(data.equals(""))) {
                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                s_search1.setText("");
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Already scanned packet", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.RED);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                }else {
                                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Already scanned packet" + "</big></b></font>"), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                                final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
                                mp.start();
                            } while (cursor.moveToNext());

                        }
                    }else {

                        filter(data);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UnPackingCartonDetailActivity.this);
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        cartondetailslist = new ArrayList<>();
        btn_finish=findViewById(R.id.btn_finish);

        if (Flag.equalsIgnoreCase("F")){
            btn_finish.setVisibility(View.GONE);

        }else {
           // btn_finish.setVisibility(View.VISIBLE);
        }




        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jobjdata = new JSONObject();
                JSONArray packetarray = new JSONArray();
                JSONObject jsonObjectpacket = new JSONObject();
                for (int i = 0; i < cartondetailslist.size(); i++) {

                    try {
                        jsonObjectpacket.put("CartonDetailId", cartondetailslist.get(i).getCartonDetailId());
                        jsonObjectpacket.put("CartonHeaderId", HeaderId);
                        jsonObjectpacket.put("RefId", PackOrderHeadrId);
                        jsonObjectpacket.put("PacketMasterId", cartondetailslist.get(i).getPacketMasterId());
                        jsonObjectpacket.put("ItemMasterId", cartondetailslist.get(i).getItemMasterId());
                        jsonObjectpacket.put("Qty", cartondetailslist.get(i).getQty());
                        jsonObjectpacket.put("SoScheduleId", cartondetailslist.get(i).getSoScheduleId());
                        try {
                            JSONObject a1 = new JSONObject(jsonObjectpacket.toString());
                            packetarray.put(a1);
                            jobjdata.put("PacketData", packetarray);
                        } catch (JSONException e) {

                        }


                    } catch (JSONException e) {


                    }
                }
                finaljson = "";
                finaljson = jobjdata.toString();
                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(UnPackingCartonDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PacketRemoveData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(UnPackingCartonDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=s_search1.getText().toString();


            }
        });



    }




    @Override
    public void onResume() {
        super.onResume();

    }




    public class DownloadPicklistItem extends AsyncTask<String, String, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getCartonDtl + "?CartonHeaderId=" + HeaderId;

            try {

                 res = ut.POSTOpenConnection(url, UnPackingCartonDetailActivity.this);
                 response=res.toString();


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
                if (s.contains("CartonHeaderId")) {
                    try {
                        list.clear();
                        JSONArray jResults = new JSONArray(s);
                        for (int i=0;i<jResults.length();i++){
                            CartonDetail cartonDetail=new CartonDetail();
                            JSONObject jsonObject=jResults.getJSONObject(i);
                            cartonDetail.setCartonHeaderId(jsonObject.getString("CartonHeaderId"));
                            cartonDetail.setCartonCode(jsonObject.getString("CartonCode"));
                            cartonDetail.setCartonDetailId(jsonObject.getString("CartonDetailId"));
                            cartonDetail.setQty(jsonObject.getString("Qty"));
                            cartonDetail.setItemCode(jsonObject.getString("ItemCode"));
                            cartonDetail.setItemDesc(jsonObject.getString("ItemDesc"));
                            cartonDetail.setPacketNo(jsonObject.getString("PacketNo"));
                            cartonDetail.setRefId(jsonObject.getString("RefId"));
                            cartonDetail.setPacketMasterId(jsonObject.getString("PacketMasterId"));
                            cartonDetail.setItemMasterId(jsonObject.getString("ItemMasterId"));
                            cartonDetail.setSoScheduleId(jsonObject.getString("SoScheduleId"));
                            list.add(cartonDetail);
                        }
                        adapter = new AdapterCartonDetail(list, UnPackingCartonDetailActivity.this);
                        recycler.setAdapter(adapter);
                        //adapter.update(list);


                        if (Flag.equalsIgnoreCase("F")) {
                            JSONObject jobjdata = new JSONObject();
                            JSONArray packetarray = new JSONArray();
                            JSONObject jsonObjectpacket = new JSONObject();
                            for (int i = 0; i < list.size(); i++) {

                                try {
                                    jsonObjectpacket.put("CartonDetailId", list.get(i).getCartonDetailId());
                                    jsonObjectpacket.put("CartonHeaderId", HeaderId);
                                    jsonObjectpacket.put("RefId", PackOrderHeadrId);
                                    jsonObjectpacket.put("PacketMasterId", list.get(i).getPacketMasterId());
                                    jsonObjectpacket.put("ItemMasterId", list.get(i).getItemMasterId());
                                    jsonObjectpacket.put("Qty", list.get(i).getQty());
                                    jsonObjectpacket.put("SoScheduleId", list.get(i).getSoScheduleId());
                                    try {
                                        JSONObject a1 = new JSONObject(jsonObjectpacket.toString());
                                        packetarray.put(a1);
                                        jobjdata.put("PacketData", packetarray);
                                    } catch (JSONException e) {

                                    }


                                } catch (JSONException e) {


                                }
                            }
                            finaljson = "";
                            finaljson = jobjdata.toString();
                            if (isnet()) {
                                progress.setVisibility(View.VISIBLE);
                                new StartSession(UnPackingCartonDetailActivity.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new PacketRemoveData().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }


                                });

                            } else {
                                Toast.makeText(UnPackingCartonDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            btn_finish.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progress.setVisibility(View.GONE);

                } else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Carton details not found", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Carton details not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                    progress.setVisibility(View.GONE);

                    final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
                    mp.start();



                }

            } else {
                cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Carton details not found", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();
            }else{
                Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Carton details not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
                progress.setVisibility(View.GONE);
                final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
                mp.start();

            }
        }
    }



    private void filter(String packet) {
        try {
            ArrayList<CartonDetail> dummyList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPacketNo().equals(packet)) {
                    dummyList.add(list.get(i));
                   // adapter.update(dummyList);
                }

            }


            if (dummyList.size() > 0) {
                for (int i = 0; i < dummyList.size(); i++){
                    if (packet.equalsIgnoreCase(dummyList.get(i).getPacketNo())){
                        cartonDetail.setCartonDetailId(dummyList.get(i).getCartonDetailId());
                        cartonDetail.setCartonHeaderId(HeaderId);
                        cartonDetail.setRefId(PackOrderHeadrId);
                        cartonDetail.setPacketMasterId(dummyList.get(i).getPacketMasterId());
                        cartonDetail.setItemMasterId(dummyList.get(i).getItemMasterId());
                        cartonDetail.setQty(dummyList.get(i).getQty());
                        cartonDetail.setSoScheduleId(dummyList.get(i).getSoScheduleId());
                        cartondetailslist.add(cartonDetail);
                    }
                }

                Packet grnpost_1 = new Packet();
                grnpost_1.setPacketNo(data);
                cf.Insert_GRNPACKETNO(grnpost_1);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Packet scanned" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.ok);
                mp.start();

            }else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Packet not found in carton details", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Packet not found in carton details" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
                mp.start();
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
                    new StartSession(UnPackingCartonDetailActivity.this, new CallbackInterface() {
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
                    Toast.makeText(UnPackingCartonDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    class PacketRemoveData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_UnPacking;

            try {
                res = ut.OpenPostConnection(url, finaljson, UnPackingCartonDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (final Exception e) {
                response = "Error";

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UnPackingCartonDetailActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
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

            if (integer.contains("Success")) {

                cartondetailslist.clear();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, "Record removed successfully", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.GREEN);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Record removed successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
               onBackPressed();

            }
            else if (integer.contains("Failed")) {
                try {
                    integer = integer.substring(1, integer.length() - 1);
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, status, Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(UnPackingCartonDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + status+ "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                    final MediaPlayer mp = MediaPlayer.create(UnPackingCartonDetailActivity.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

