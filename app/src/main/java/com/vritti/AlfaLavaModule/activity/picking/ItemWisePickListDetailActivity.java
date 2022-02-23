package com.vritti.AlfaLavaModule.activity.picking;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.activity.PacketNoDisplayActivity;
import com.vritti.AlfaLavaModule.adapter.AdapterPicklistDetail;
import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.bean.FIFOBreakReson;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PickListDetail;
import com.vritti.AlfaLavaModule.bean.PrinterName;
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
import java.util.ArrayList;
import java.util.Locale;

public class ItemWisePickListDetailActivity extends AppCompatActivity {


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
    private String ItemCode,PacketNo="";
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
    private String printerName="";
    private AlertDialog b;

    //4368534

    int currentTotal=0;
    int currentQty=0;
    int Qty=0;
    private String data;
    private TextToSpeech t1;
    private ProgressBar progress;

    int QtyTopick,BalToPick,FiFoQtyBalanceTopick,LotQtyBalanceofFiFo,FiFoQtyToPick,LotQtyToPick;
    Button btn_finish;
    String ItemmasterId="";
    private int PickedQty=0;
    private String PacketMasterId="";
    private String Flag="N";
    String Reason="";
    ArrayList<FIFOBreakReson>fifoBreakResonArrayList;
    ArrayAdapter<FIFOBreakReson> dataAdapter;
    private AutoCompleteTextView edt_reason;
    private String QCStatus="";
    ImageView img_barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picklist_dodropdown_lay);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(ItemWisePickListDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ItemWisePickListDetailActivity.this);
        String dabasename = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ItemWisePickListDetailActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(ItemWisePickListDetailActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        progress=findViewById(R.id.progress);


        DONumber=getIntent().getStringExtra("dono");
        HeaderId=getIntent().getStringExtra("headerid");
        ItemmasterId=getIntent().getStringExtra("itemmaster");
        ItemCode=getIntent().getStringExtra("Itemcode");
        QCStatus=getIntent().getStringExtra("qcstatus");
        if (QCStatus==null||QCStatus.equalsIgnoreCase("")){
            QCStatus="";
        }

        getSupportActionBar().setTitle(DONumber);

        fifoBreakResonArrayList=new ArrayList<>();
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

        /*String s="true@9612636710#10$PV-H2-12!e03c822b-b5ad-41b6-970a-ddf5a741cb36";
        String[] separated = s.split("@");
        String Item = separated[1];
        String[] sep1 = Item.split("#");
        ItemCode = sep1[0];
        String qty= sep1[1];
        String[] parts = qty.split("\\$"); // escape .
        Qty = Integer.parseInt(parts[0]);
        String locationCode = parts[1];
        String[] parts1 = locationCode.split("!"); // escape .
        LocationCode = parts1[0] ;// escape .
        Pick_listSuggLotId = parts1[1]; // escape .*/



    //   BreakFifo("","");

        if (Check_Db()) {
            detailPacket(ItemCode);
        } else {

            if (isnet()) {
                progress.setVisibility(View.VISIBLE);
                new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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



                            data = s_search1.getText().toString();
                            PacketNo=data;
                        try {
                            if (Constants.type == Constants.Type.Alfa) {

                                JSONObject jsonObject = new JSONObject(data);

                                ItemCode = jsonObject.getString("Itemcode");
                                data = jsonObject.getString("PacketNo");
                                PacketNo=data;
                                Qty = Integer.parseInt(jsonObject.getString("PacketQty"));

                                s_search1.setText("");


                                //filter(ItemCode, PacketNo);

                                if (isnet()) {
                                            try {
                                                progress.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                                                @Override
                                                public void callMethod() {
                                                    new GetPacketValidation().execute();
                                                }

                                                @Override
                                                public void callfailMethod(String msg) {
                                                }


                                            });

                                        } else {
                                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        }





                            }
                            else {

                                if (data != null && !(data.equals(""))) {
                                    String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                                    Cursor cursor = sql.rawQuery(searchQuery, null);
                                    int count = cursor.getCount();
                                    if (count > 0) {
                                        cursor.moveToFirst();
                                        do {
                                            s_search1.setText("");
                                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                                                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Already scanned packet", Toast.LENGTH_SHORT);
                                            View toastView = toast.getView();
                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.RED);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastMessage.setCompoundDrawablePadding(5);
                                            toastView.setBackgroundColor(Color.TRANSPARENT);
                                            toast.show();
                                            final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                                            mp.start();
                                        }else {
                                                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Already scanned packet" + "</big></b></font>"), Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        } while (cursor.moveToNext());

                                    } else {

                                        if (isnet()) {
                                            try {
                                                progress.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                                                @Override
                                                public void callMethod() {
                                                    new GetPacketValidation().execute();
                                                }

                                                @Override
                                                public void callfailMethod(String msg) {
                                                }


                                            });

                                        } else {
                                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
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
        img_barcode = findViewById(R.id.img_barcode);

        txt_do.setText("DN - " + DONumber);

        s_search1.setHint("Scan Packet No");




        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ItemWisePickListDetailActivity.this);
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        barcodecontent = new ArrayList<String>();
        btn_finish=findViewById(R.id.btn_finish);
        btn_finish.setVisibility(View.GONE);

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemWisePickListDetailActivity.this, DOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=s_search1.getText().toString();

                PacketNo=data;


                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetPacketValidation().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                }


                            });

                        } else {
                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


               /* if (data != null && !(data.equals(""))) {
                    String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                    Cursor cursor = sql.rawQuery(searchQuery, null);
                    int count = cursor.getCount();
                    if (count > 0) {
                        cursor.moveToFirst();
                        do {
                            s_search1.setText("");
                            Toast.makeText(ItemWisePickListDetailActivity.this, "Packet already scanned", Toast.LENGTH_SHORT).show();

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
                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                }*/
            }
        });

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    IntentIntegrator integrator = new IntentIntegrator(ItemWisePickListDetailActivity.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();

            }
        });


/*
        s_search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                PackOrderNo = parent.getSelectedItem().toString();
                if (PackOrderNo.equals("Select Delivery Note")) {


                } else {



                    if (isnet()) {
                        ProgressHUD.show(PickListDetailActivity.this, "Fetching picklist Detail ...", true, false);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


    }


    public void detailPacket(String DO) {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_CARTAN_PICKLIST + "' WHERE ItemCode=?", new String[]{String.valueOf(ItemCode)});


            if (c.getCount()>0) {
                c.moveToFirst();
                do {
                    recycler.setVisibility(View.VISIBLE);

                    PickListDetail pickListDetail = new PickListDetail();
                    pickListDetail.setPick_ListDtlId(c.getString(c.getColumnIndex("Pick_ListHdrId")));
                    pickListDetail.setPickListNo(c.getString(c.getColumnIndex("PickListNo")));
                    Pick_ListDtlId=c.getString(c.getColumnIndex("Pick_ListDtlId"));
                    pickListDetail.setPick_listDtlId(Pick_ListDtlId);
                  //  pickListDetail.setSoScheduleId(c.getString(c.getColumnIndex("SoScheduleId")));
                    String ItemmasterId=c.getString(c.getColumnIndex("ItemMasterId"));
                    pickListDetail.setItemMasterId(ItemmasterId);
                    pickListDetail.setQtyToPick(c.getString(c.getColumnIndex("QtyToPick")));
                    pickListDetail.setQtyPicked(c.getString(c.getColumnIndex("QtyPicked")));
                    ItemCode=c.getString(c.getColumnIndex("ItemCode"));
                    pickListDetail.setItemCode(ItemCode);
                    pickListDetail.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    pickListDetail.setStockDetailsId(c.getString(c.getColumnIndex("StockDetailsId")));
                    pickListDetail.setFlag(c.getString(c.getColumnIndex("Flag")));
                    pickListDetail.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    pickListDetail.setPick_listSuggLotId(c.getString(c.getColumnIndex("Pick_listSuggLotId")));
                    list.add(pickListDetail);


                } while (c.moveToNext());



                adapter = new AdapterPicklistDetail(list, ItemWisePickListDetailActivity.this);
                recycler.setAdapter(adapter);
                adapter.update(list);

            }else {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

              /*  Toast.makeText(PickListDetailActivity.this,"Record not found",Toast.LENGTH_SHORT).show();
                recycler.setVisibility(View.GONE);

               if (isnet()) {
                    ProgressHUD.show(PickListDetailActivity.this, "Fetching picklist Detail ...", true, false);
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
                }*/

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

            detailPacket(ItemCode);
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

    public void showpacket(String picklistDtl) {

        startActivity(new Intent(ItemWisePickListDetailActivity.this, PacketNoDisplayActivity.class)
                .putExtra("header",picklistDtl)
                .putExtra("flag","1").
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                res = ut.OpenPostConnection(url,FinalJson, ItemWisePickListDetailActivity.this);
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
                                sql.update(db.TABLE_ITEM_PICKLIST, values_1, "ItemMasterId=?", new String[]{String.valueOf(ItemmasterId)});

                                onResume();
                            }
                        }
                    }
                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                    mp.start();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Data send successfully", Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.GREEN);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                    }
                    else {
                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Data send successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }


                } else if (s.contains("Failed")) {
                   /* Packet grnpost_1 = new Packet();
                    grnpost_1.setPacketNo(PacketNo);
                    cf.Insert_GRNPACKETNO(grnpost_1);*/

                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(s);
                        String Error = jsonObject.getString("ERROR");
                        if (Error.contains("Packet not found")) {
                            Toast.makeText(ItemWisePickListDetailActivity.this, "Packet not found", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(ItemWisePickListDetailActivity.this, "Technical error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ItemWisePickListDetailActivity.this, Error, Toast.LENGTH_SHORT).show();
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
            String url = CompanyURL + WebUrlClass.api_getPicklistDtl + "?Pick_ListHdrId=" + HeaderId +"&ItemMasterId="+ItemmasterId;

            try {

                 res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
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
                if (s.contains("Pick_ListDtlId")) {
                    try {
                        list.clear();
                       cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);


                        JSONObject jsonObject=new JSONObject(s);

                        PickListDetail pickListDetail=new PickListDetail();

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
                                pickListDetail.setItemMasterId("");
                                /* ItemMasterId=Pick_ListSuggLotId.getString("ItemMasterId");
                                pickListDetail.setItemMasterId(ItemMasterId);*/
                                QtyToPick=Pick_ListSuggLotId.getString("QtyToPick");
                                pickListDetail.setQtyToPick(QtyToPick);
                                QtyPicked=Pick_ListSuggLotId.getString("QtyPicked");
                                pickListDetail.setQtyPicked(QtyPicked);
                                ItemCode=Pick_ListSuggLotId.getString("ItemCode");
                                pickListDetail.setItemCode(ItemCode);
                                ItemDesc=Pick_ListSuggLotId.getString("ItemDesc");
                                pickListDetail.setItemDesc(ItemDesc);
                                LocationCode=Pick_ListSuggLotId.getString("LocationCode");
                                pickListDetail.setLocationCode(LocationCode);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                pickListDetail.setDONumber(DONumber);

                                cf.InsertPickingOrderDetails(pickListDetail);

                            }

                        }else {

                        JSONArray pick_ListDtl=jsonObject.getJSONArray("Pick_ListDtl");
                        if (pick_ListDtl.length()>0) {

                            for (int i = 0; i < pick_ListDtl.length(); i++) {
                                JSONObject pick_ListDtljsonObject = pick_ListDtl.getJSONObject(i);
                                Pick_ListDtlId = pick_ListDtljsonObject.getString("Pick_ListDtlId");
                                pickListDetail.setPick_listDtlId(Pick_ListDtlId);
                                SoScheduleId = pick_ListDtljsonObject.getString("SoScheduleId");
                                pickListDetail.setSoScheduleId(SoScheduleId);
                                ItemMasterId = pick_ListDtljsonObject.getString("ItemMasterId");
                                pickListDetail.setItemMasterId(ItemMasterId);
                                QtyToPick = pick_ListDtljsonObject.getString("QtyToPick");
                                pickListDetail.setQtyToPick(QtyToPick);
                                QtyPicked = pick_ListDtljsonObject.getString("QtyPicked");
                                pickListDetail.setQtyPicked(QtyPicked);
                                ItemCode = pick_ListDtljsonObject.getString("ItemCode");
                                pickListDetail.setItemCode(ItemCode);
                                ItemDesc = pick_ListDtljsonObject.getString("ItemDesc");
                                pickListDetail.setItemDesc(ItemDesc);
                                LocationCode = pick_ListDtljsonObject.getString("LocationCode");
                                pickListDetail.setLocationCode(LocationCode);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                pickListDetail.setDONumber(DONumber);

                                cf.InsertPickingOrderDetails(pickListDetail);
                                // cf.InsertPickingOrderDetails(pickListDetail);
                            }
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
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Record not found", Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    progress.setVisibility(View.GONE);

                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                    mp.start();
                }else{
                    Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Record not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                }

            } else {
                cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                    Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Record not found", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();
                progress.setVisibility(View.GONE);
                final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                mp.start();
            }else {
                    Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Record not found" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

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

                    Toast.makeText(ItemWisePickListDetailActivity.this, "Item found!!", Toast.LENGTH_SHORT).show();


                    if (packet != null && !(packet.equals(""))) {
                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + packet + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                s_search1.setText("");
                                Toast.makeText(ItemWisePickListDetailActivity.this, "Packet already scanned", Toast.LENGTH_SHORT).show();
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
                                new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                                Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                } else {
                    Toast.makeText(ItemWisePickListDetailActivity.this, "Wrong item scanned", Toast.LENGTH_SHORT).show();

                }
            } else {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItemCode().equals(data)) {
                        dummyList.add(list.get(i));
                        adapter.update(dummyList);
                    }

                }


                if (dummyList.size() > 0) {


                   /* if (packet != null && !(packet.equals(""))) {

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("PacketNo", packet);
                            FinalJson = jsonObject.toString();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (isnet()) {
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }*/
                } else {
                    Toast.makeText(ItemWisePickListDetailActivity.this, "Wrong item scanned!", Toast.LENGTH_SHORT).show();
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
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
                res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
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
              //  b.dismiss();
                /*ContentValues values = new ContentValues();
                String aa = WebUrlClass.DoneFlag_Complete;
                values.put("Flag", aa);
                sql.update(db.TABLE_CARTAN_PICKLIST, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});
*/

                onResume();
                Toast.makeText(ItemWisePickListDetailActivity.this, "Packet split Successfully", Toast.LENGTH_SHORT).show();
                //b.dismiss();
            } else {
                //    Toast.makeText(ItemWisePickListDetailActivity.this, "Packet split Successfully", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Packet split Successfully", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();

            }else{
                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Packet split Successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
                s=s.substring(1, s.length() - 1);

                getpacketdialog(s);

             //   onResume();
               // b.dismiss();
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
                res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
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

                Toast.makeText(ItemWisePickListDetailActivity.this, "Printer not found", Toast.LENGTH_SHORT).show();
            } if (response.equals("")) {
                printerName="";
             //   Toast.makeText(ItemWisePickListDetailActivity.this, "Printer not found", Toast.LENGTH_SHORT).show();

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemWisePickListDetailActivity.this);
                LayoutInflater inflater = ItemWisePickListDetailActivity.this.getLayoutInflater();
                final View myView = inflater.inflate(R.layout.printername_lay, null);
                dialogBuilder.setView(myView);
                printername = (Spinner) myView .findViewById(R.id.spinner_printer);

                adapterPrinterName = new Adapter_PrinterName(ItemWisePickListDetailActivity.this, printerNameArrayList);
                printername.setAdapter(adapterPrinterName);
                printername.setSelection(0,false);


                printername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        printerName=printerNameArrayList.get(position).getPrinterName();


                        if (isnet()) {
                            b.dismiss();
                            progress.setVisibility(View.VISIBLE);
                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

        @Override
        protected String doInBackground(String... params) {
           // String url = CompanyURL + WebUrlClass.api_CheckPacketValidationNew+"?Pick_ListHdrId="+HeaderId+"&PacketNo="+data;


            String url = null;
            try {
                if (Constants.type == Constants.Type.Alfa) {
                    url = CompanyURL + WebUrlClass.api_CheckPacketValidationAlfaLaval+"?Pick_ListHdrId="+HeaderId+"&PacketNo="+data+"&QCStatus="+URLEncoder.encode(QCStatus,"UTF-8")+"&FifoBreak="+Flag+"&FifoBreakReason="+URLEncoder.encode(Reason,"UTF-8");

                }else {
                    url = CompanyURL + WebUrlClass.api_CheckPacketValidationNew+"?Pick_ListHdrId="+HeaderId+"&PacketNo="+data+"&QCStatus="+URLEncoder.encode(QCStatus,"UTF-8")+"&FifoBreak="+Flag+"&FifoBreakReason="+URLEncoder.encode(Reason,"UTF-8");

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
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

                if (Constants.type == Constants.Type.Alfa) {
                    if (s.contains("true"))
                    {


                        Packet grnpost_1 = new Packet();
                        grnpost_1.setPacketNo(PacketNo);
                        cf.Insert_GRNPACKETNO(grnpost_1);

                        String[] separated = s.split("@");
                        String Item = separated[1];
                        String[] sep1 = Item.split("#");
                        ItemCode = sep1[0];
                        String qty= sep1[1];
                        String[] parts = qty.split("\\$"); // escape .
                        Qty = Integer.parseInt(parts[0]);
                        String locationCode = parts[1];
                        String[] parts1 = locationCode.split("!"); // escape .
                        LocationCode = parts1[0] ;// escape .
                        String Pick_listSuggLotId = parts1[1]; // escape



                        ContentValues values_1 = new ContentValues();
                        if (list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getPick_listSuggLotId().equals(Pick_listSuggLotId)) {
                                    String current = String.valueOf(list.get(j).getQtyPicked());
                                    String Pick = String.valueOf(list.get(j).getQtyToPick());

                                    if (Pick.equals(current)) {
                                    /*Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Picking completed for "+ItemCode, Toast.LENGTH_SHORT);View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.GREEN);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                                    mp.start();*/


                                    } else {


                                        String[] namesList = current.split("\\.0");
                                        currentQty = Integer.parseInt(namesList[0]);
                                        currentTotal = currentQty + Qty;
                                        values_1.put("QtyPicked", currentTotal);
                                        sql.update(db.TABLE_CARTAN_PICKLIST, values_1, "Pick_listSuggLotId=?", new String[]{String.valueOf(Pick_listSuggLotId)});


                                       /* String[] args = new String[]{ItemCode, LocationCode};
                                        sql.update(db.TABLE_CARTAN_PICKLIST, values_1, "ItemCode=? AND LocationCode=?", args);
*/

/*
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getItemCode().equals(ItemCode)) {
                                        String current1 = String.valueOf(list.get(i).getQtyPicked());
                                        String[] namesList1 = current1.split(".0");
                                        currentQty = Integer.parseInt(namesList1[0]);
                                        currentTotal = currentQty + Qty;
                                        values_1.put("QtyPicked", currentTotal);
                                        sql.update(db.TABLE_ITEM_PICKLIST, values_1, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                                    }
                                }*/

                                /*int qtypick= Integer.parseInt(list.get(j).getQtyToPick());
                                int qtypicked= Integer.parseInt(list.get(j).getQtyPicked());

                                if (qtypick-qtypicked<Qty){

                                    Toast.makeText(ItemWisePickListDetailActivity.this,"",Toast.LENGTH_SHORT).show();
                                }*/

                                        onResume();

                                        MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                                        mp.start();
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Data send successfully", Toast.LENGTH_SHORT);
                                            View toastView = toast.getView();
                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.GREEN);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastView.setBackgroundColor(Color.WHITE);
                                            toast.show();
                                        }else {
                                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Data send successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                    }
                                }
                            }
                        }




                    }
                    else  if (s.contains("PickedQty")) {

                        JSONArray jResults = new JSONArray(s);
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            Qty = jsonObject.getInt("BalQty");
                            PickedQty = jsonObject.getInt("PickedQty");
                            PacketNo = jsonObject.getString("PacketNo");
                            PacketMasterId = jsonObject.getString("PacketMasterId");

                            t1.speak(PacketNo + " already scanned", TextToSpeech.QUEUE_FLUSH, null);
                            if (t1 != null) {
                                t1.stop();
                                t1.shutdown();
                            }
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, PacketNo + " already scanned", Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextSize(18);
                            toastMessage.setTextColor(Color.GREEN);
                            toastMessage.setGravity(Gravity.CENTER);
                            toastView.setBackgroundColor(Color.WHITE);
                            toast.show();

                            final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                            mp.start();
                        }else{
                                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B'><b><big>" + PacketNo + " already scanned" + "</big></b></font>"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                            }

                            Packetdeletedialog();
                        }
                    }
                    else if(s.contains("Picking completed")){
                        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, s, Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextSize(18);
                            toastMessage.setTextColor(Color.GREEN);
                            toastMessage.setGravity(Gravity.CENTER);
                            toastView.setBackgroundColor(Color.WHITE);
                            toast.show();
                        } else{
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + s + "</big></b></font>"), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                        mp.start();
                    }
                    else  if(s.contains("Split")){
                        JSONObject jsonObject = new JSONObject(s);
                        String Error = jsonObject.getString("ERROR");
                        String[] separated = Error.split(":");
                        try {
                            ReqQty = Double.parseDouble(separated[1]);
                            getdeletedialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ItemWisePickListDetailActivity.this, "Technical error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, s, Toast.LENGTH_SHORT);
                            View toastView = toast.getView();
                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                            toastMessage.setTextSize(18);
                            toastMessage.setTextColor(Color.RED);
                            toastMessage.setGravity(Gravity.CENTER);
                            toastView.setBackgroundColor(Color.WHITE);
                            toast.show();
                        }else {
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + s + "</big></b></font>"), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                        mp.start();

                        if (s.contains("FIFO")){
                            BreakFifo(s,data);
                        }


                    }

                    progress.setVisibility(View.GONE);
                }

                else {
                    if (s.contains("true"))
                    {


                        Packet grnpost_1 = new Packet();
                        grnpost_1.setPacketNo(PacketNo);
                        cf.Insert_GRNPACKETNO(grnpost_1);

                        String[] separated = s.split("@");
                        String Item = separated[1];
                        String[] sep1 = Item.split("#");
                        ItemCode = sep1[0];
                        String qty= sep1[1];
                        String[] parts = qty.split("\\$"); // escape .
                        Qty = Integer.parseInt(parts[0]);
                        LocationCode = parts[1];



                        ContentValues values_1 = new ContentValues();
                        if (list.size() > 0) {
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getItemCode().equals(ItemCode)&&list.get(j).getLocationCode().equals(LocationCode))
                                {
                                    String current = String.valueOf(list.get(j).getQtyPicked());
                                    String Pick = String.valueOf(list.get(j).getQtyToPick());

                                    if (Pick.equals(current)) {
                                    /*Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Picking completed for "+ItemCode, Toast.LENGTH_SHORT);View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.GREEN);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                                    mp.start();*/


                                    } else {


                                        String[] namesList = current.split("\\.0");
                                        currentQty = Integer.parseInt(namesList[0]);
                                        currentTotal = currentQty + Qty;
                                        values_1.put("QtyPicked", currentTotal);


                                        String[] args = new String[]{ItemCode, LocationCode};
                                        sql.update(db.TABLE_CARTAN_PICKLIST, values_1, "ItemCode=? AND LocationCode=?", args);

/*
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getItemCode().equals(ItemCode)) {
                                        String current1 = String.valueOf(list.get(i).getQtyPicked());
                                        String[] namesList1 = current1.split(".0");
                                        currentQty = Integer.parseInt(namesList1[0]);
                                        currentTotal = currentQty + Qty;
                                        values_1.put("QtyPicked", currentTotal);
                                        sql.update(db.TABLE_ITEM_PICKLIST, values_1, "ItemCode=?", new String[]{String.valueOf(ItemCode)});

                                    }
                                }*/

                                /*int qtypick= Integer.parseInt(list.get(j).getQtyToPick());
                                int qtypicked= Integer.parseInt(list.get(j).getQtyPicked());

                                if (qtypick-qtypicked<Qty){

                                    Toast.makeText(ItemWisePickListDetailActivity.this,"",Toast.LENGTH_SHORT).show();
                                }*/

                                        onResume();

                                        MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                                        mp.start();
                                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Data send successfully", Toast.LENGTH_SHORT);
                                            View toastView = toast.getView();
                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.GREEN);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastView.setBackgroundColor(Color.WHITE);
                                            toast.show();
                                        }else {
                                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Data send successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                    }
                                }
                            }
                        }




                    }
                    else  if (s.contains("PickedQty")) {

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
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, PacketNo + " already scanned", Toast.LENGTH_SHORT);
                                View toastView = toast.getView();
                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(18);
                                toastMessage.setTextColor(Color.GREEN);
                                toastMessage.setGravity(Gravity.CENTER);
                                toastView.setBackgroundColor(Color.WHITE);
                                toast.show();

                            }else {
                                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B'><b><big>" + PacketNo + " already scanned" + "</big></b></font>"), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                            final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                            mp.start();

                            Packetdeletedialog();
                        }
                    }
                    else if(s.contains("Picking completed")) {
                        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, s, Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.GREEN);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B'><b><big>" + s + "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                        final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                        mp.start();
                    }
                    else  if(s.contains("Split")){
                        JSONObject jsonObject = new JSONObject(s);
                        String Error = jsonObject.getString("ERROR");
                        String[] separated = Error.split(":");
                        try {
                            ReqQty = Double.parseDouble(separated[1]);
                            getdeletedialog();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ItemWisePickListDetailActivity.this, "Technical error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);
                        if (t1 != null) {
                            t1.stop();
                            t1.shutdown();
                        }
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                            Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, s, Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F'><b><big>" + s + "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                        final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                        mp.start();

                        if (s.contains("FIFO")){
                            BreakFifo(s,data);
                        }


                    }

                    progress.setVisibility(View.GONE);
                }



            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    private boolean checkSystemWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.System.canWrite(ItemWisePickListDetailActivity.this))
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


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemWisePickListDetailActivity.this);
        LayoutInflater inflater = ItemWisePickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadPrinterData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    private void getpacketdialog(String packet) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemWisePickListDetailActivity.this);
        LayoutInflater inflater = ItemWisePickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);
        TextView Txt_wait_reshuffle=myView.findViewById(R.id.MO);

        btn_yes.setText("OK");
        Txt_wait_reshuffle.setText("New generated packet after spliting " + packet);



        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        b.setCanceledOnTouchOutside(false);
        b.show();
    }


    private void Packetdeletedialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemWisePickListDetailActivity.this);
        LayoutInflater inflater = ItemWisePickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        TextView MO=myView.findViewById(R.id.MO);

        MO.setText("This packet is already scanned in this picklist.Do you want to remove it?");

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PacketRemoveData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    class PacketRemoveData extends AsyncTask<String, Void, String> {
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

            String url = CompanyURL + WebUrlClass.api_RemovedPickedPacket +"?PacketMasterId="+PacketMasterId+"&PickedQty="+PickedQty+"&PickListDtlId="+Pick_ListDtlId;

            try {
                res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            progress.setVisibility(View.GONE);

            if (integer.contains("Success")) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Packet removed successfully", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
            }else {
                Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#26C14B' ><b><big>" + "Packet removed successfully" + "</big></b></font>"), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }

                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
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
                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
            else if (integer.contains("Failed")) {
                try {
                    integer = integer.substring(1, integer.length() - 1);
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, status, Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                    }else {
                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + status + "</big></b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    }

                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void BreakFifo (final String message, final String packet) {

        data=packet;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemWisePickListDetailActivity.this);
        LayoutInflater inflater = ItemWisePickListDetailActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.fifo_break_dialog_lay, null);
        dialogBuilder.setView(myView);

        TextView MO=myView.findViewById(R.id.MO);

        MO.setText("Scanned packet not in suggested LOT.Do you want to break FIFO?");

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);
        final LinearLayout len_reason=myView.findViewById(R.id.len_reason);
        edt_reason=myView.findViewById(R.id.edt_reason);
        Button btn_submit=myView.findViewById(R.id.btn_submit);
        btn_cancel.setText("No");

        edt_reason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });


        if (isnet()) {
            try {
                progress.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetFifoReason().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                }


            });

        } else {
            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                len_reason.setVisibility(View.VISIBLE);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                len_reason.setVisibility(View.GONE);

                b.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Flag = "Y";
                Reason = edt_reason.getText().toString();

                if (Reason.equalsIgnoreCase("")) {
                    final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.ok);
                    mp.start();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Please enter reason", Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Please enter reason" + "</big></b></font>"), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                } else {
                    b.dismiss();
                    if (isnet()) {
                        try {
                            progress.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetPacketValidation().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }


                        });

                    } else {
                        Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        b = dialogBuilder.create();
        b.show();
    }

    class GetFifoReason extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progress.setVisibility(View.GONE);
             if (!res.equalsIgnoreCase("")) {

                fifoBreakResonArrayList.clear();

                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int i=0;i<jResults.length();i++){
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        FIFOBreakReson fifoBreakReson = new FIFOBreakReson();
                        fifoBreakReson.setConfiguration(jsonObject.getString("Configuration"));//UserLoginId
                        fifoBreakReson.setConfigurationDetailId(jsonObject.getString("ConfigurationDetailId"));
                        fifoBreakResonArrayList.add(fifoBreakReson);
                    }

                    dataAdapter = new ArrayAdapter<FIFOBreakReson>(ItemWisePickListDetailActivity.this, android.R.layout.simple_spinner_item, fifoBreakResonArrayList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edt_reason.setAdapter(dataAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }


            }else{
                if(ut.isNet(ItemWisePickListDetailActivity.this)){
                    Toast.makeText(ItemWisePickListDetailActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ItemWisePickListDetailActivity.this,"Record not found",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            url =  CompanyURL + WebUrlClass.GetFifoBreakReason;

            try {
                res = ut.OpenConnection(url, ItemWisePickListDetailActivity.this);
                //res = res.replaceAll("\\\\", "");
                //res = res.substring(1, res.length() - 1);



            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
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


                data = result.getContents().toString();
                PacketNo = data;
                try {
                    if (Constants.type == Constants.Type.Alfa) {

                        JSONObject jsonObject = new JSONObject(data);

                        ItemCode = jsonObject.getString("Itemcode");
                        data = jsonObject.getString("PacketNo");
                        PacketNo = data;
                        Qty = Integer.parseInt(jsonObject.getString("PacketQty"));

                        s_search1.setText("");


                        //filter(ItemCode, PacketNo);

                        if (isnet()) {
                            try {
                                progress.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetPacketValidation().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                }


                            });

                        } else {
                            Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else {

                        if (data != null && !(data.equals(""))) {
                            String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                            Cursor cursor = sql.rawQuery(searchQuery, null);
                            int count = cursor.getCount();
                            if (count > 0) {
                                cursor.moveToFirst();
                                do {
                                    s_search1.setText("");
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, "Already scanned packet", Toast.LENGTH_SHORT);
                                        View toastView = toast.getView();
                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(18);
                                        toastMessage.setTextColor(Color.RED);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(5);
                                        toastView.setBackgroundColor(Color.TRANSPARENT);
                                        toast.show();
                                        final MediaPlayer mp = MediaPlayer.create(ItemWisePickListDetailActivity.this, R.raw.alert);
                                        mp.start();
                                    } else {
                                        Toast toast = Toast.makeText(ItemWisePickListDetailActivity.this, Html.fromHtml("<font color='#EF4F4F' ><b><big>" + "Already scanned packet" + "</big></b></font>"), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                } while (cursor.moveToNext());

                            } else {

                                if (isnet()) {
                                    try {
                                        progress.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    new StartSession(ItemWisePickListDetailActivity.this, new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new GetPacketValidation().execute();
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {
                                        }


                                    });

                                } else {
                                    Toast.makeText(ItemWisePickListDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}

