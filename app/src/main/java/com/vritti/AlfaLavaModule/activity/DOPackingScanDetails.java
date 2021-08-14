package com.vritti.AlfaLavaModule.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.AlfaLavaModule.activity.cartonlabel.CartonLabelHeaderListActivity;
import com.vritti.AlfaLavaModule.activity.picking.ItemPickListDetailActivity;
import com.vritti.AlfaLavaModule.adapter.AdapterSecondaryDetail;
import com.vritti.AlfaLavaModule.adapter.Adp_Box;
import com.vritti.AlfaLavaModule.bean.BoxBean;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PacketListDetail;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.Devicelist_LablelPrint;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.Utils_print;
import com.vritti.sales.CounterBilling.DeviceListActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DOPackingScanDetails extends AppCompatActivity {

    ArrayList<SecondaryBox> secondaryBoxArrayList;
    PutAwayDetail putAwayDetails;
    private Context pContext;
    @BindView(R.id.edt_scanPacket)
    TextView edt_scanPacket;
    String PackOrderNo="", PacketNo="";
    DownloadPacketedOrdNoDetails downloadPutAwayDetails;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private static RecyclerView recycler;
    private static AdapterSecondaryDetail adapter;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    private String Pack_OrdHdrId = "", Pick_ListHdrId = "",
            Pack_OrdDtlId = "", Pick_ListDtlId = "", ItemMasterId = "",
             ItemCode = "", ItemDesc = "", SoScheduleId = "",CartanCode="",CartanHeaderId="";

    public static final int REQUEST_CONNECT_DEVICE = 6;

    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;
    String Qty="";
    ImageView img_search;
    AlertDialog b;
    private String data="";

    int QtyTopick,BalToPick,FiFoQtyBalanceTopick,LotQtyBalanceofFiFo,FiFoQtyToPick,LotQtyToPick;
    private String FinalJson="";
    int QtyToPack = 0, QtyPacked = 0;
    int Quantity=0;


    Button btn_finish;
    private String BoxTypeMasterId="";
    TextView txt;
    private String PacketMasterId="";
    private int PickedQty=0;
    SharedPreferences.Editor editor;
    private List<BoxBean> list;
    private String Pack="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondarybox_scan_lay);
        ButterKnife.bind(this);
        pContext = DOPackingScanDetails.this;
        getSupportActionBar().setTitle("Packing Details");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
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
        txt = (TextView) findViewById(R.id.txt);
        secondaryBoxArrayList = new ArrayList<>();
        editor= userpreferences.edit();

       // getdialog();

        CartanCode=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");

        mService = new BluetoothService(DOPackingScanDetails.this, mHandler);

        list=new ArrayList<>();

        if (mService.isAvailable() == false) {
            Toast.makeText(DOPackingScanDetails.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }






        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
            edt_scanPacket.setHint("Scan AWB No");
        }else {
            if (getIntent().hasExtra("dono")){


                PackOrderNo=userpreferences.getString("OrdNo","");
                Pack_OrdHdrId=getIntent().getStringExtra("header");
                getSupportActionBar().setTitle(PackOrderNo);

            }

        }
        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {

        }else {

            if (BoxCheck_Db()) {
                Update_BOX();
            } else {
                if (isnet()) {
                    new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            DowmloadBox downloadputlist = new DowmloadBox();
                            downloadputlist.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            DowmloadBox downloadputlist = new DowmloadBox();
                            downloadputlist.execute();
                        }


                    });

                } else {
                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

        }

      // getdialog();


        recycler = (RecyclerView) findViewById(R.id.list_itemDetailList);
        img_search = (ImageView) findViewById(R.id.img_search);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recycler.setHasFixedSize(true);
        adapter = new AdapterSecondaryDetail(secondaryBoxArrayList);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);



        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
            PackOrderNo=userpreferences.getString("OrdNo","");
            if (!PackOrderNo.equals("")) {
                getSupportActionBar().setTitle(PackOrderNo +" \n" +CartanCode);
                    DOdetailPacket();
            }

        }else {
            if (Check_Db()) {
            DOdetailPacket();
        } else {
            if (isnet()) {
                progressBar.setVisibility(View.VISIBLE);
            new StartSession(pContext, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                        downloadPutAwayDetails.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                        downloadPutAwayDetails.execute();
                    }


                });

            } else {
                Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
               }
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


                    data = edt_scanPacket.getText().toString().trim();

                    if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal"))
                    {
                        if (data.contains("PN")||(data.contains("P-21")||(data.contains("G")))) {
                            PacketNo=data;


                            String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                            Cursor cursor = sql.rawQuery(searchQuery, null);
                            int count = cursor.getCount();
                            if (count > 0) {
                                cursor.moveToFirst();
                                do {
                                    edt_scanPacket.setText("");
                                    Toast toast = Toast.makeText(DOPackingScanDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
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
                            else {
                                if (isnet()) {
                                    //   progress.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.VISIBLE);

                                    new GetPacketValidation().execute();


                                } else {
                                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else {
                             PackOrderNo=userpreferences.getString("OrdNo","");

                            if (PackOrderNo.equals("")) {
                                PackOrderNo = edt_scanPacket.getText().toString();
                                if (data.length()==13){
                                    PackOrderNo = data;
                                }else if (data.length()==11) {
                                    PackOrderNo = data;
                                }
                                else {
                                    data = data.substring(data.length() - 12);
                                    PackOrderNo = data;
                                }

                                editor.putString("OrdNo", PackOrderNo);
                                editor.commit();
                            }

                            if(PackOrderNo != null && !(PackOrderNo.equals(""))) {
                                PackOrderNo=userpreferences.getString("OrdNo","");
                                DOdetailPacket();


/*
                          else {
                              if (isnet()) {
                                  ProgressHUD.show(pContext, "Fetching data...", true, false);
                                  new StartSession(pContext, new CallbackInterface() {
                                      @Override
                                      public void callMethod() {
                                          downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                          downloadPutAwayDetails.execute();
                                      }

                                      @Override
                                      public void callfailMethod(String msg) {
                                          downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                          downloadPutAwayDetails.execute();
                                      }


                                  });

                              } else {
                                  Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                              }
                          }
*/
                      }
                      }

                    }else {
                        try {
                            if (Constants.type == Constants.Type.Alfa) {

                                JSONObject jsonObject = new JSONObject(data);

                                ItemCode = jsonObject.getString("Itemcode");
                                data = jsonObject.getString("PacketNo");
                                PacketNo=data;
                                Qty = jsonObject.getString("PacketQty");

                                edt_scanPacket.setText("");

                                if (isnet()) {
                                    //   progress.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.VISIBLE);

                                    new GetPacketValidation().execute();


                                } else {
                                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                PacketNo = data;
                                String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                                Cursor cursor = sql.rawQuery(searchQuery, null);
                                int count = cursor.getCount();
                                if (count > 0) {
                                    cursor.moveToFirst();
                                    do {
                                        edt_scanPacket.setText("");
                                        Toast toast = Toast.makeText(DOPackingScanDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
                                        View toastView = toast.getView();
                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(18);
                                        toastMessage.setTextColor(Color.RED);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(5);
                                        toastView.setBackgroundColor(Color.TRANSPARENT);
                                        toast.show();
                                    } while (cursor.moveToNext());

                                } else {
                                    if (isnet()) {
                                        //   progress.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.VISIBLE);
                                        new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                                            @Override
                                            public void callMethod() {
                                                new GetPacketValidation().execute();
                                            }

                                            @Override
                                            public void callfailMethod(String msg) {
                                            }


                                        });

                                    } else {
                                        Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }


                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                    // callApi id pass
                    //      public string GetScanLocation(string LocationCode)


                  /*  if(PackOrderNo != null && !(PackOrderNo.equals(""))) {


                        if (isnet()) {
                            ProgressHUD.show(pContext, "Fetching data...", true, false);
                            new StartSession(pContext, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                    downloadPutAwayDetails.execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                    downloadPutAwayDetails.execute();
                                }


                            });

                        } else {
                            Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
*/

                    return true;
                }
                return false;
            }
        });


        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(DOPackingScanDetails.this, BoxmasterActivity.class);
                intent.putExtra("packorder", Pack_OrdHdrId);
                intent.putExtra("dono", PackOrderNo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/


                    if (isnet()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostShipment().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }


                        });

                    } else {
                        Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }






            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = edt_scanPacket.getText().toString().trim();

                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
                    if (data.contains("PN") || (data.contains("P-21") || (data.contains("G")))) {
                        PacketNo = data;


                        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET + " where PacketNo='" + data + "'";
                        Cursor cursor = sql.rawQuery(searchQuery, null);
                        int count = cursor.getCount();
                        if (count > 0) {
                            cursor.moveToFirst();
                            do {
                                edt_scanPacket.setText("");
                                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Already scanned packet", Toast.LENGTH_LONG);
                                View toastView = toast.getView();
                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                toastMessage.setTextSize(18);
                                toastMessage.setTextColor(Color.RED);
                                toastMessage.setGravity(Gravity.CENTER);
                                toastMessage.setCompoundDrawablePadding(5);
                                toastView.setBackgroundColor(Color.TRANSPARENT);
                                toast.show();
                            } while (cursor.moveToNext());

                        } else {
                            if (isnet()) {
                                //   progress.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.VISIBLE);

                                new GetPacketValidation().execute();


                            } else {
                                Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }


            }
        });


    }

    public void showpackestscan() {
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");
        if (!CartanHeaderId.equals("")) {

            startActivity(new Intent(DOPackingScanDetails.this, PacketNoDisplayActivity.class)
                    .putExtra("header", CartanHeaderId)
                    .putExtra("flag", "0").
                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }else {
            Toast.makeText(DOPackingScanDetails.this,"Carton not created",Toast.LENGTH_LONG).show();
        }
    }


    public class DownloadPacketedOrdNoDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response="";


        @Override
        protected String doInBackground(String... params) {
            String url="";
            if (!PackOrderNo.equals("")) {
                if (Constants.type == Constants.Type.Alfa) {

                   /* try {
                        url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + URLEncoder.encode(PackOrderNo.trim(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
*/

                    try {
                       // url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + URLEncoder.encode(PackOrderNo.trim(),"UTF-8");
                        url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + URLEncoder.encode(PackOrderNo.trim(),"UTF-8") + "&RefType=P";

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (EnvMasterId.equalsIgnoreCase("jal") || EnvMasterId.equalsIgnoreCase("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {

                        try {
                            url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + URLEncoder.encode(PackOrderNo.trim(),"UTF-8") + "&RefType=A";
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + URLEncoder.encode(PackOrderNo.trim(),"UTF-8") + "&RefType=P";
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    res = ut.OpenConnection(url, pContext);
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                } catch (Exception e) {
                    response = "Error";
                    e.printStackTrace();
                }
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
            if (s.equals("[]")) {
                edt_scanPacket.setText("");
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "NO Record Present", Toast.LENGTH_LONG).show();
                editor.remove("OrdNo");
                editor.commit();
            } else if (s.equals("Error")) {
                edt_scanPacket.setText("");
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "Server Error....", Toast.LENGTH_LONG).show();
                editor.remove("OrdNo");
                editor.commit();
            } else if (s.contains("AWB")) {
                progressBar.setVisibility(View.GONE);
                //    cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "AWB NO not found please update AWB NO", Toast.LENGTH_LONG).show();
                edt_scanPacket.setText("");
                editor.remove("OrdNo");
                editor.commit();
            } else if (s.contains("Failed")) {
                progressBar.setVisibility(View.GONE);
                //    cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "Packing already completed", Toast.LENGTH_LONG).show();
                edt_scanPacket.setText("");
                editor.remove("OrdNo");
                editor.commit();
            } else if (!(s.equalsIgnoreCase("[]"))) {
                if (s.contains("Pack_OrdHdrId")) {
                    edt_scanPacket.setText("");
                    edt_scanPacket.setHint("Scan Packet No");
                    try {

                        cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);

                        JSONObject jsonObject = new JSONObject(s);

                        PacketListDetail pickListDetail = new PacketListDetail();


                        JSONArray pick_ListHdr = jsonObject.getJSONArray("Pack_OrdHdr");
                        if (pick_ListHdr.length() > 0) {

                            for (int i = 0; i < pick_ListHdr.length(); i++) {
                                JSONObject pickjsonObject = pick_ListHdr.getJSONObject(i);
                                Pack_OrdHdrId = pickjsonObject.getString("Pack_OrdHdrId");
                                pickListDetail.setPack_OrdHdrId(Pack_OrdHdrId);
                                Pick_ListHdrId = pickjsonObject.getString("Pick_ListHdrId");
                                pickListDetail.setPick_ListHdrId(Pick_ListHdrId);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);

                            }

                        }
                        JSONArray pick_ListDtl = jsonObject.getJSONArray("Pack_OrdDtl");
                        if (pick_ListDtl.length() > 0) {

                            for (int i = 0; i < pick_ListDtl.length(); i++) {
                                JSONObject pick_ListDtljsonObject = pick_ListDtl.getJSONObject(i);
                                Pack_OrdDtlId = pick_ListDtljsonObject.getString("Pack_OrdDtlId");
                                pickListDetail.setPack_OrdDtlId(Pack_OrdDtlId);
                                Pick_ListDtlId = pick_ListDtljsonObject.getString("Pick_ListDtlId");
                                pickListDetail.setPick_ListDtlId(Pick_ListDtlId);
                                ItemMasterId = pick_ListDtljsonObject.getString("ItemMasterId");
                                pickListDetail.setItemMasterId(ItemMasterId);
                                QtyToPack = pick_ListDtljsonObject.getInt("QtyToPack");
                                pickListDetail.setQtyToPack(QtyToPack);
                                QtyPacked = pick_ListDtljsonObject.getInt("QtyPacked");
                                pickListDetail.setQtyPacked(QtyPacked);
                                ItemCode = pick_ListDtljsonObject.getString("ItemCode");
                                pickListDetail.setItemCode(ItemCode);
                                ItemDesc = pick_ListDtljsonObject.getString("ItemDesc");
                                pickListDetail.setItemDesc(ItemDesc);
                                SoScheduleId = pick_ListDtljsonObject.getString("SoScheduleId");
                                pickListDetail.setSoScheduleId(SoScheduleId);
                                pickListDetail.setFlag(WebUrlClass.DoneFlag_Default);
                                pickListDetail.setPackOrderNo(PackOrderNo);
                                cf.InsertPackOrderDetails(pickListDetail);
                                // cf.InsertPickingOrderDetails(pickListDetail);
                            }

                        }


                            /*JSONArray pick_listSuggLotId = jsonObject.getJSONArray("CartonHeaderDetail");
                            if (pick_listSuggLotId.length() > 0) {


                            }
*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    DOdetailPacket();
                    edt_scanPacket.setText("");
                } else {
                    cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                    edt_scanPacket.setText("");
                }

            } else {
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(DOPackingScanDetails.this, s, Toast.LENGTH_SHORT).show();
                edt_scanPacket.setText("");

                progressBar.setVisibility(View.GONE);
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

    private Boolean Check_Db() {
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_SECONDARY_BOX, null);
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

    public void DOdetailPacket() {

        edt_scanPacket.setText("");
        secondaryBoxArrayList.clear();
        int count = 0;
        try {

            String searchQuery = "SELECT  * FROM " + db.TABLE_SECONDARY_BOX + " where PackOrderNo='" + PackOrderNo + "'";

            Cursor c = sql.rawQuery(searchQuery,null);
        //    Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_SECONDARY_BOX, null);
            if (c.getCount()>0) {
                recycler.setVisibility(View.VISIBLE);
                c.moveToFirst();
                do {
                    SecondaryBox bean = new SecondaryBox();
                    bean.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    Pack_OrdHdrId = c.getString(c.getColumnIndex("Pack_OrdHdrId"));
                    bean.setPack_OrdHdrId(Pack_OrdHdrId);
                    bean.setPack_OrdDtlId(c.getString(c.getColumnIndex("Pack_OrdDtlId")));
                    bean.setQtyToPack(c.getInt(c.getColumnIndex("QtyToPack")));
                    bean.setQtyPacked(c.getInt(c.getColumnIndex("QtyPacked")));
                    bean.setItemMasterId(c.getString(c.getColumnIndex("ItemMasterId")));
                    bean.setSoScheduleId(c.getString(c.getColumnIndex("SoScheduleId")));
                    bean.setFlag(c.getString(c.getColumnIndex("Flag")));
                    secondaryBoxArrayList.add(bean);
                } while (c.moveToNext());
                adapter.update(secondaryBoxArrayList);

                for (int j = 0; j < secondaryBoxArrayList.size(); j++) {

                    if (secondaryBoxArrayList.get(j).getItemCode().equals(ItemCode)) {

                        String Picked = String.valueOf(secondaryBoxArrayList.get(j).getQtyPacked());
                        String Pick = String.valueOf(secondaryBoxArrayList.get(j).getQtyToPack());

                        if (Constants.type == Constants.Type.Alfa) {

                        }else {
                            if (Pick.equals(Picked)) {

                                btn_finish.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }




                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {

                }else {
                    if (secondaryBoxArrayList.size()>1) {
                        for (int j = 0; j < secondaryBoxArrayList.size(); j++) {
                            if (secondaryBoxArrayList.get(j).getPack_OrdHdrId().equals(Pack_OrdHdrId)) {
                                String Picked = String.valueOf(secondaryBoxArrayList.get(j).getQtyPacked());
                                String Pick = String.valueOf(secondaryBoxArrayList.get(j).getQtyToPack());
                                String ItemCode = String.valueOf(secondaryBoxArrayList.get(j).getItemCode());

                                // ItemMasterId = secondaryBoxArrayList.get(j).getItemMasterId();

                                if (Pick.equals(Picked)) {
                                    Toast toast = Toast.makeText(DOPackingScanDetails.this, "Packing completed for " + ItemCode, Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                    toastMessage.setTextSize(18);
                                    toastMessage.setTextColor(Color.GREEN);
                                    toastMessage.setGravity(Gravity.CENTER);
                                    toastMessage.setCompoundDrawablePadding(5);
                                    toastView.setBackgroundColor(Color.TRANSPARENT);
                                    toast.show();
                                    final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                                    mp.start();
                                }


                            }
                        }
                    }

                }


                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {


                    String CartanHeaderId = userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER, "");

                    if (CartanHeaderId.equals("")) {

                        BoxTypeMasterId = "04ea5c65-00aa-4c90-8058-3d77fce50c26";


                        final JSONObject jsonObject = new JSONObject();

                        try {

                            jsonObject.put("RefType", "Pack");
                            jsonObject.put("RefId", Pack_OrdHdrId);
                            jsonObject.put("BoxTypeMasterId", BoxTypeMasterId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (isnet()) {
                            progressBar.setVisibility(View.VISIBLE);
                            new UpLoadData().execute(jsonObject.toString());
                        } else {
                            Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                }


            }else {
                recycler.setVisibility(View.GONE);
                if (isnet()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new StartSession(pContext, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                            downloadPutAwayDetails.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                            downloadPutAwayDetails.execute();
                        }


                    });

                } else {
                    Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.secondarybox_menu, menu);
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
                    progressBar.setVisibility(View.VISIBLE);
                    new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                            downloadPutAwayDetails.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                            downloadPutAwayDetails.execute();
                        }


                    });

                } else {
                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }



               /* if (isnet()) {
                    try {
                        ProgressHUD.show(DOPackingScanDetails.this, "Fetching data...", true, false);
                        new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                UploadDoDump uploadDoDump = new UploadDoDump();
                                uploadDoDump.execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/


                return true;

            case R.id.menu_finished:

                    if (isnet()) {
                        progressBar.setVisibility(View.VISIBLE);
                        new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostShipment().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }


                        });

                    } else {
                        Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                return true;
            case R.id.menu_suspend:

                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove(WebUrlClass.MyPREFERENCES_CODE);
                editor.remove("OrdNo");
                editor.commit();

                startActivity(new Intent(DOPackingScanDetails.this,DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;

            case R.id.menu_create:

                if (Constants.type == Constants.Type.Alfa) {
                    BoxTypeMasterId ="3d106a3f-eb88-4f95-b70f-ab958b4b0ee0";
                }else {
                    BoxTypeMasterId ="04ea5c65-00aa-4c90-8058-3d77fce50c26";
                }
                final JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("RefType", "Pack");
                    jsonObject.put("RefId", Pack_OrdHdrId);
                    jsonObject.put("BoxTypeMasterId",  BoxTypeMasterId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isnet()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UpLoadData().execute(jsonObject.toString());

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void createsecondary(String itemMasterId, String pack_OrdHdrId, String soScheduleId) {



        String CartanHeaderId = userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER, "");

        if (CartanHeaderId.equals("")) {
            Intent intent = new Intent(DOPackingScanDetails.this, BoxmasterActivity.class);
            intent.putExtra("itemid", itemMasterId);
            intent.putExtra("packorder", pack_OrdHdrId);
            intent.putExtra("qty", Qty);
            intent.putExtra("soScheduleId", soScheduleId);
            intent.putExtra("packetno", PacketNo);
            startActivity(intent);

        } else {
            Intent intent = new Intent(DOPackingScanDetails.this, CreateSecondaryPackActivity.class);
            intent.putExtra("itemid", itemMasterId);
            intent.putExtra("packorder", pack_OrdHdrId);
            intent.putExtra("qty", Qty);
            intent.putExtra("packetno", PacketNo);
            intent.putExtra("soScheduleId", soScheduleId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }


    private void filter(String data, String packet) {
        ArrayList<SecondaryBox> dummyList = new ArrayList<>();

        for (int i = 0; i < secondaryBoxArrayList.size(); i++) {
            if (secondaryBoxArrayList.get(i).getItemCode().equals(data)) {
                dummyList.add(secondaryBoxArrayList.get(i));
                adapter.update(dummyList);
            }

        }

        if (dummyList.size() == 0) {
            dummyList.clear();
            Toast toast = Toast.makeText(DOPackingScanDetails.this, "Wrong item scanned!", Toast.LENGTH_LONG);View toastView = toast.getView();

            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(18);
            toastMessage.setTextColor(Color.RED);
            toastMessage.setGravity(Gravity.CENTER);
            toastMessage.setCompoundDrawablePadding(5);
            toastView.setBackgroundColor(Color.TRANSPARENT);
            toast.show();
            final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
            mp.start();

        } else {
           // Toast.makeText(DOPackingScanDetails.this, "Item found!!", Toast.LENGTH_SHORT).show();

            CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");

            if (!CartanHeaderId.equals("")&&!PacketNo.equals("")) {
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("CartonHeaderId", CartanHeaderId);
                    jsonObject.put("ItemMasterId", ItemMasterId);
                    jsonObject.put("Pack_OrdHdrId", Pack_OrdHdrId);
                    jsonObject.put("PacketNo", PacketNo);
                    jsonObject.put("Qty", Quantity);

                    FinalJson = jsonObject.toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isnet()) {
                    progressBar.setVisibility(View.VISIBLE);
                    new UpLoadDetails().execute();

                   /* new StartSession(DOPackingScanDetails.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UpLoadDetails().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });
*/
                } else {
                    Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private boolean isPresentToLocal_Doneflg() {

       // Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_SECONDARY_BOX + "' WHERE Flag=?", new String[]{String.valueOf(WebUrlClass.DoneFlag_Complete)});

        String query = "SELECT  * FROM " + db.TABLE_SECONDARY_BOX + " WHERE  Flag ='" + WebUrlClass.DoneFlag_Complete + "'";
        Cursor c = sql.rawQuery(query,null);
        if (c.getCount() == 0) {
            return true;
        } else {
            return false;
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
        b.dismiss();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CreateSecondaryPackActivity.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //bluetooth enabled and request for showing available bluetooth devices
            // Toast.makeText(parent, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            BluetoothClass.pairPrinter(getApplicationContext(), DOPackingScanDetails.this);
        }else if (requestCode == CreateSecondaryPackActivity.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            //bluetooth device selected and request pairing with device
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            String Address = address.substring(address.length() - 17);
            BluetoothClass.pairedPrinterAddress(getApplicationContext(), DOPackingScanDetails.this,Address);


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
                            Toast.makeText(DOPackingScanDetails.this, "Connect successful",
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
                    Toast.makeText(DOPackingScanDetails.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(DOPackingScanDetails.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();


        PackOrderNo=userpreferences.getString("OrdNo","");
        CartanCode=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");
        if (!PackOrderNo.equals("")) {
            //getSupportActionBar().setTitle(PackOrderNo +" \n" +CartanCode);
            getSupportActionBar().setTitle(PackOrderNo);
            if (Check_Db()) {
                DOdetailPacket();
            }
        }



    }


/*
    private class GetPacketValidation extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_CheckPacketValidation+"?Pick_ListHdrId="+Pick_ListHdrId+"&PacketNo="+data;
            String url = CompanyURL + WebUrlClass.api_CheckPacketPackingValidation+"?Pack_OrdHdrId="+Pack_OrdHdrId+"&PacketNo="+data;

            try {
                res = ut.OpenConnection(url, DOPackingScanDetails.this);
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
                ProgressHUD.Destroy();

            }catch (Exception e){
                e.printStackTrace();
            }

            edt_scanPacket.setText("");


            if (s.contains("Item Not Present")){
                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Item Not Present", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                mp.start();

            }else  if (s.contains("PickedQty")) {
                try {
                    JSONArray jResults = new JSONArray(s);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        Quantity = jsonObject.getInt("BalQty");
                        PickedQty = jsonObject.getInt("PickedQty");
                        PacketNo = jsonObject.getString("PacketNo");
                        PacketMasterId = jsonObject.getString("PacketMasterId");
                        Toast toast = Toast.makeText(DOPackingScanDetails.this, PacketNo + " already scanned", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.GREEN);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();

                        final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                        mp.start();


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {

                if (s.contains("PacketMasterId")) {
                    try {

                        Packet grnpost_1 = new Packet();
                        grnpost_1.setPacketNo(PacketNo);
                        cf.Insert_GRNPACKETNO(grnpost_1);

                        JSONArray jResults = new JSONArray(s);
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            Quantity = jsonObject.getInt("BalQty");
                            ItemCode = jsonObject.getString("ItemCode");
                            int BalToPick = jsonObject.getInt("BalToPick");
                            int FiFoQtyBalanceTopick = jsonObject.getInt("FiFoQtyBalanceTopick");
                            FiFoQtyToPick = jsonObject.getInt("FiFoQtyToPick");
                            LotQtyToPick = jsonObject.getInt("LotQtyToPick");



                                if (secondaryBoxArrayList.size() > 0) {
                                    for (int j = 0; j < secondaryBoxArrayList.size(); j++) {
                                        if (secondaryBoxArrayList.get(j).getItemCode().equals(ItemCode)) {
                                            String Picked = String.valueOf(secondaryBoxArrayList.get(j).getQtyPacked());
                                            String Pick = String.valueOf(secondaryBoxArrayList.get(j).getQtyToPack());
                                            ItemMasterId=secondaryBoxArrayList.get(j).getItemMasterId();

                                            if (Pick.equals(Picked)) {
                                                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Packing completed for "+ItemCode, Toast.LENGTH_LONG);View toastView = toast.getView();
                                                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                                toastMessage.setTextSize(18);
                                                toastMessage.setTextColor(Color.GREEN);
                                                toastMessage.setGravity(Gravity.CENTER);
                                                toastMessage.setCompoundDrawablePadding(5);
                                                toastView.setBackgroundColor(Color.TRANSPARENT);
                                                toast.show();
                                                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                                                mp.start();


                                            } else {
                                                filter(ItemCode, data);
                                            }
                                        }else {
                                           */
/* Toast toast = Toast.makeText(DOPackingScanDetails.this, ItemCode + " not present in packing", Toast.LENGTH_LONG);View toastView = toast.getView();
                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.RED);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastMessage.setCompoundDrawablePadding(5);
                                            toastView.setBackgroundColor(Color.TRANSPARENT);
                                            toast.show();
                                            final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                                            mp.start();
*//*

                                        }
                                    }

                                }
                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            try{
                ProgressHUD.Destroy();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
*/

    private class GetPacketValidation extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_CheckPacketValidation+"?Pick_ListHdrId="+Pick_ListHdrId+"&PacketNo="+data;
            String url = CompanyURL + WebUrlClass.api_CheckPacketPackingValidation+"?Pack_OrdHdrId="+Pack_OrdHdrId+"&PacketNo="+data;

            try {
                res = ut.OpenConnection(url, DOPackingScanDetails.this);
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
            progressBar.setVisibility(View.GONE);

            edt_scanPacket.setText("");


            if (s.contains("already Packed")){
                Toast toast = Toast.makeText(DOPackingScanDetails.this, s, Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                mp.start();

            }
            else  if (s.contains("true")) {
                    try {

                        String[] separated = s.split("#");
                        String Item = separated[1];
                        String[] sep1 = Item.split("Item Code @");
                        double quantity = Double.parseDouble(sep1[0]);
                        Quantity= (int) quantity;
                        ItemCode = sep1[1];


                            if (secondaryBoxArrayList.size() > 0) {
                                for (int j = 0; j < secondaryBoxArrayList.size(); j++) {
                                    if (secondaryBoxArrayList.get(j).getItemCode().equals(ItemCode)) {

                                        Packet grnpost_1 = new Packet();
                                        grnpost_1.setPacketNo(PacketNo);
                                        cf.Insert_GRNPACKETNO(grnpost_1);

                                        String Picked = String.valueOf(secondaryBoxArrayList.get(j).getQtyPacked());
                                        String Pick = String.valueOf(secondaryBoxArrayList.get(j).getQtyToPack());
                                        ItemMasterId=secondaryBoxArrayList.get(j).getItemMasterId();

                                        if (Pick.equals(Picked)) {
                                            Toast toast = Toast.makeText(DOPackingScanDetails.this, "Packing completed for "+ItemCode, Toast.LENGTH_LONG);View toastView = toast.getView();
                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.GREEN);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastMessage.setCompoundDrawablePadding(5);
                                            toastView.setBackgroundColor(Color.TRANSPARENT);
                                            toast.show();
                                            final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                                            mp.start();


                                        } else {
                                            filter(ItemCode, data);
                                        }
                                    }else {
                                        Toast toast = Toast.makeText(DOPackingScanDetails.this, ItemCode +" not found in packing list ", Toast.LENGTH_LONG);View toastView = toast.getView();
                                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                        toastMessage.setTextSize(18);
                                        toastMessage.setTextColor(Color.GREEN);
                                        toastMessage.setGravity(Gravity.CENTER);
                                        toastMessage.setCompoundDrawablePadding(5);
                                        toastView.setBackgroundColor(Color.TRANSPARENT);
                                        toast.show();
                                        }
                                }

                            }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {

                Toast toast = Toast.makeText(DOPackingScanDetails.this, s, Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                mp.start();
            }

            progressBar.setVisibility(View.GONE);
        }
    }



    private class UpLoadDetails extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_POSTInsertPutPacketInCartonDtl;
            try {
                res = ut.OpenPostConnection(url,FinalJson, DOPackingScanDetails.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            progressBar.setVisibility(View.GONE);

            if (s.contains("Success")) {


                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Data Send Successfully", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                mp.start();

                if (secondaryBoxArrayList.size()>0) {
                    for (int j = 0; j < secondaryBoxArrayList.size(); j++) {
                        if (secondaryBoxArrayList.get(j).getItemCode().equals(ItemCode)) {
                            int current = secondaryBoxArrayList.get(j).getQtyPacked();
                            ContentValues values = new ContentValues();
                            int currentTotal = current + Quantity;
                            values.put("QtyPacked", currentTotal);
                            String aa = WebUrlClass.DoneFlag_Complete;
                            values.put("Flag", aa);
                            sql.update(db.TABLE_SECONDARY_BOX, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});
                            onResume();
                        }
                    }
                }

            } else {

                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Record not saved", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                mp.start();
            }
        }
    }
    public class PostShipment extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_AWBShipmentCreation + "?Pack_OrdHdrId=" + Pack_OrdHdrId +"&AWBNo="+ URLEncoder.encode(PackOrderNo,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url, DOPackingScanDetails.this);
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
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);//GRNHeaderId
            progressBar.setVisibility(View.GONE);
            String s = resp;
           /* String s = String.valueOf(res);
            if (s.contains("false")) {
                Toast.makeText(DOPackingScanDetails.this, "Please check stock", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(DOPackingScanDetails.this, "Server Error....", Toast.LENGTH_LONG).show();
            }
            else if (s.contains("true")) {
                Toast.makeText(DOPackingScanDetails.this, "Record saved Successfully", Toast.LENGTH_LONG).show();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove(WebUrlClass.MyPREFERENCES_CODE);
                editor.remove("OrdNo");
                editor.remove("OrdNo");
                editor.commit();
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                secondaryBoxArrayList.clear();
                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
//                      startActivity(new Intent(DOPackingScanDetails.this, DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                      finish();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    DOPackingScanDetails.this.recreate();


                }else {
                    startActivity(new Intent(DOPackingScanDetails.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            }*/

            if (s.contains("Success")) {


                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Shipment created Successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();

                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove(WebUrlClass.MyPREFERENCES_CODE);
                editor.remove("OrdNo");
                editor.remove("OrdNo");
                editor.commit();
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                secondaryBoxArrayList.clear();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                mp.start();

                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
//                      startActivity(new Intent(DOPackingScanDetails.this, DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                      finish();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    DOPackingScanDetails.this.recreate();
                }else {
                    startActivity(new Intent(DOPackingScanDetails.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
            else if (s.contains("Failed")) {
                try {
                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(DOPackingScanDetails.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (s.contains("False"))
            {
                s = s.substring(1, s.length() - 1);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");

                    Toast toast = Toast.makeText(DOPackingScanDetails.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                    mp.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(DOPackingScanDetails.this, s, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.alert);
                mp.start();

            }
        }
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

            String url = CompanyURL + WebUrlClass.api_POSTInsertPutPacketInCartonHdr;
            try {
                res = ut.OpenPostConnection(url, params[0], DOPackingScanDetails.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (s.contains(",")) {

                String[] namesList = s.split(",");
                String Cartan_ID = namesList[0].replace("\"", "");
                CartanCode = namesList[1];


                editor.putString(WebUrlClass.MyPREFERENCES_HEADER, Cartan_ID);
                editor.putString(WebUrlClass.MyPREFERENCES_CODE, CartanCode);
                editor.commit();

                final MediaPlayer mp = MediaPlayer.create(DOPackingScanDetails.this, R.raw.ok);
                mp.start();

                Toast toast = Toast.makeText(DOPackingScanDetails.this, "Carton created successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();

                printlabel(CartanCode.replace("\"", ""),PackOrderNo);

              //  Toast.makeText(DOPackingScanDetails.this,"Carton created successfully",Toast.LENGTH_SHORT).show();

                getSupportActionBar().setTitle(PackOrderNo);
                edt_scanPacket.setHint("Scan Packet No");

            }
        }
    }

    private class UploadDoDump extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_OrderPacking;
            try {
                res = ut.OpenConnection(url, DOPackingScanDetails.this);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
            ProgressHUD.Destroy();
            try {
                if (s.equalsIgnoreCase("true")){

                }

            }

            catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private class DowmloadBox extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getAllData;
            try {
                res = ut.OpenConnection(url, DOPackingScanDetails.this);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId


            if (s.contains("BoxTypeMasterId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    Log.d("test", "jResults :=" + jResults);
                    cf.DeleteAllRecord(db.TABLE_BOX);
                    Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_BOX, null);
                    Log.e("Table values----", "" + cur.getCount());
                    ContentValues Container = new ContentValues();
                    String columnName, columnValue;
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jstring = jResults.getJSONObject(index);
                        for (int j = 0; j < cur.getColumnCount(); j++) {
                            columnName = cur.getColumnName(j);
                            columnValue = jstring.getString(columnName);
                            Container.put(columnName, columnValue);
                            Log.e("Count Location ...",
                                    " count i: " + index + "  j:" + j);
                        }
                        long a = sql.insert(db.TABLE_BOX, null, Container);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Update_BOX();
            } else if (s.contains("[]")) {
                Toast.makeText(DOPackingScanDetails.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(DOPackingScanDetails.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void Update_BOX() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_BOX + "'", null);
            if (c.getCount() == 0) {
                //  addgrn_D(ScanGRN);
            } else {
                c.moveToFirst();
                do {
                    BoxBean bean = new BoxBean();
                    bean.setBoxTypeMasterId(c.getString(c.getColumnIndex("BoxTypeMasterId")));
                    list.add(bean);


                } while (c.moveToNext());

                if (list.size()==1){
                    for (int i=0;i<list.size();i++){
                        BoxTypeMasterId=list.get(i).getBoxTypeMasterId();
                    }

                    if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {


                    }else {
                        String CartanHeaderId = userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER, "");

                        if (CartanHeaderId.equals("")) {

                            //   BoxTypeMasterId ="04ea5c65-00aa-4c90-8058-3d77fce50c26";


                            final JSONObject jsonObject = new JSONObject();

                            try {

                                try{
                                    Pack=getIntent().getStringExtra("header");
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                jsonObject.put("RefType", "Pack");
                                jsonObject.put("RefId",Pack );
                                jsonObject.put("BoxTypeMasterId", BoxTypeMasterId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            if (isnet()) {
                                progressBar.setVisibility(View.VISIBLE);
                                new UpLoadData().execute(jsonObject.toString());
                            } else {
                                Toast.makeText(DOPackingScanDetails.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }else {
                    CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");
                    if (!CartanHeaderId.equals("")) {

                    }else {
                        startActivity(new Intent(DOPackingScanDetails.this,
                                BoxmasterActivity.class).
                                putExtra("packorder", Pack_OrdHdrId).
                                putExtra("dono", PackOrderNo).
                                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Boolean BoxCheck_Db() {

        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_BOX + "'", null);
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


    private void printlabel (final String DONumber, final String packorderno) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DOPackingScanDetails.this);
        LayoutInflater inflater = DOPackingScanDetails.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.split_lay_dialog, null);
        dialogBuilder.setView(myView);

        TextView MO=myView.findViewById(R.id.MO);

        MO.setText("Do you want to print this label?");

        Button btn_cancel=myView.findViewById(R.id.btn_cancel);
        Button btn_yes=myView.findViewById(R.id.btn_yes);


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(BluetoothClass.isPrinterConnected(getApplicationContext(), DOPackingScanDetails.this)) {
                    mService = BluetoothClass.getServiceInstance();



                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(), DOPackingScanDetails.this);
                }


                if(BluetoothClass.isPrinterConnected(getApplicationContext(), DOPackingScanDetails.this)) {
                    mService = BluetoothClass.getServiceInstance();
                    printReceipt(DONumber,packorderno);
                }








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




