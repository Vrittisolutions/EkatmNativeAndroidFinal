package com.vritti.AlfaLavaModule.fragment;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.AlfaLavaModule.activity.CreateSecondaryPackActivity;
import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.packaging.ReceiptPackagingDOListActivity;
import com.vritti.AlfaLavaModule.adapter.AdapterSecondaryDetail;
import com.vritti.AlfaLavaModule.bean.PacketListDetail;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
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

import java.util.ArrayList;
import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentScanPackingPacket extends Fragment {

    View Creatview;


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

    public FragmentScanPackingPacket() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Creatview = inflater.inflate(R.layout.secondarybox_scan_lay, container, false);

        ButterKnife.bind(getActivity());
        pContext = getActivity();

        userpreferences = getActivity().getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
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
        txt = (TextView)Creatview.findViewById(R.id.txt);
        secondaryBoxArrayList = new ArrayList<>();


        CartanCode=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");

        mService = new BluetoothService(getActivity(), mHandler);


        if (mService.isAvailable() == false) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }



        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
            edt_scanPacket.setHint("Scan AWB No");
        }else {
            if (getActivity().getIntent().hasExtra("dono")){
                PackOrderNo=getActivity().getIntent().getStringExtra("dono");


            }

        }





        // getdialog();


        recycler = (RecyclerView) Creatview.findViewById(R.id.list_itemDetailList);
        img_search = (ImageView) Creatview.findViewById(R.id.img_search);
        btn_finish = (Button) Creatview.findViewById(R.id.btn_finish);

        recycler.setHasFixedSize(true);
        adapter = new AdapterSecondaryDetail(secondaryBoxArrayList);
        recycler.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);


        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
            PackOrderNo=userpreferences.getString("OrdNo","");
            if (!PackOrderNo.equals("")) {
                DOdetailPacket();
            }

        }else {
            if (Check_Db()) {
                DOdetailPacket();
            } else {
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

                    if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                        if (data.contains("PN")){
                            PacketNo=data;
                            //Toast.makeText(DOPackingScanDetails.this,"Packet Scanned",Toast.LENGTH_SHORT).show();
                            if (isnet()) {
                                //   progress.setVisibility(View.VISIBLE);
                                try{
                                    ProgressHUD.show(getActivity(), "Sending data please wait ...", true, false);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                new StartSession(getActivity(), new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new GetPacketValidation().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                    }


                                });

                            } else {
                                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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

                                SharedPreferences.Editor editor = userpreferences.edit();
                                editor.putString("OrdNo", PackOrderNo);
                                editor.commit();
                            }

                            if(PackOrderNo != null && !(PackOrderNo.equals(""))) {
                                PackOrderNo=userpreferences.getString("OrdNo","");
                                DOdetailPacket();
                            }
                        }

                    }else {
                        try {
                            if (Constants.type == Constants.Type.Alfa) {

                                PacketNo = new JSONObject(data).getString("PacketNo");
                                ItemCode = new JSONObject(data).getString("Itemcode");
                                Qty = new JSONObject(data).getString("PacketQty");

                                edt_scanPacket.setText(PacketNo);

                                filter(ItemCode, PacketNo);
                                edt_scanPacket.setText("");

                            } else {
                                PacketNo = data;
                                if (isnet()) {
                                    //   progress.setVisibility(View.VISIBLE);
                                    try{
                                        ProgressHUD.show(getActivity(), "Sending data please wait ...", true, false);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    new StartSession(getActivity(), new CallbackInterface() {
                                        @Override
                                        public void callMethod() {
                                            new GetPacketValidation().execute();
                                        }

                                        @Override
                                        public void callfailMethod(String msg) {
                                        }


                                    });

                                } else {
                                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


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

                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                    if (isnet()) {
                        ProgressHUD.show(getActivity(), "Sending data...", true, false);
                        new StartSession(getActivity(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostShipment().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                            }


                        });

                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                    editor.commit();
                    startActivity(new Intent(getActivity(),
                            ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }




            }
        });




        return Creatview;
    }


    public class DownloadPacketedOrdNoDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response="";


        @Override
        protected String doInBackground(String... params) {
            String url="";
            if (!PackOrderNo.equals("")) {
                if (Constants.type == Constants.Type.Alfa) {
                    url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + PackOrderNo.trim();
                } else {
                    if (EnvMasterId.equalsIgnoreCase("jal") || EnvMasterId.equalsIgnoreCase("jaluat")) {

                        url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + PackOrderNo.trim() + "&RefType=A";

                    } else {
                        url = CompanyURL + WebUrlClass.api_getPackingOrdDetails + "?PackedOrdNo=" + PackOrderNo.trim() + "&RefType=P";
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
            ProgressHUD.Destroy();

            String s = res;
            if (s.equals("[]")) {
                edt_scanPacket.setText("");
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "NO Record Present", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("OrdNo");
                editor.commit();
            } else if (s.equals("Error")) {
                edt_scanPacket.setText("");
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "Server Error....", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("OrdNo");
                editor.commit();
            } else if (s.contains("AWB")) {

                //    cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                Toast.makeText(pContext, "AWB NO not found please update AWB NO", Toast.LENGTH_LONG).show();
                edt_scanPacket.setText("");
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove("OrdNo");
                editor.commit();
            }else {
                if (!(s.equalsIgnoreCase("[]"))) {
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


                            JSONArray pick_listSuggLotId = jsonObject.getJSONArray("CartonHeaderDetail");
                            if (pick_listSuggLotId.length() > 0) {


                            }


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
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    edt_scanPacket.setText("");
                }
                ProgressHUD.Destroy();
            }
        }
    }

    public boolean isnet() {
        Context context = getActivity().getApplicationContext();
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
                    Pack_OrdHdrId=c.getString(c.getColumnIndex("Pack_OrdHdrId"));
                    bean.setPack_OrdHdrId(Pack_OrdHdrId);
                    bean.setPack_OrdDtlId(c.getString(c.getColumnIndex("Pack_OrdDtlId")));
                    bean.setQtyToPack(c.getInt(c.getColumnIndex("QtyToPack")));
                    bean.setQtyPacked(c.getInt(c.getColumnIndex("QtyPacked")));
                    bean.setItemMasterId(c.getString(c.getColumnIndex("ItemMasterId")));
                    bean.setSoScheduleId(c.getString(c.getColumnIndex("SoScheduleId")));
                    bean.setFlag (c.getString(c.getColumnIndex("Flag")));
                    secondaryBoxArrayList.add(bean);
                } while (c.moveToNext());
                adapter.update(secondaryBoxArrayList);

                if (isPresentToLocal_Doneflg()){
                    btn_finish.setVisibility(View.VISIBLE);
                }


                userpreferences = getActivity().getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);

                String CartanHeaderId = userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER, "");

                if (CartanHeaderId.equals("")) {

                    BoxTypeMasterId ="04ea5c65-00aa-4c90-8058-3d77fce50c26";


                    final JSONObject jsonObject = new JSONObject();

                    try {

                        jsonObject.put("RefType", "Pack");
                        jsonObject.put("RefId", Pack_OrdHdrId);
                        jsonObject.put("BoxTypeMasterId",  BoxTypeMasterId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (isnet()) {
                        ProgressHUD.show(getActivity(), "Sending  data...", true, false);
                        new StartSession(getActivity(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new UpLoadData().execute(jsonObject.toString());

                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    } else {
                        Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                }

            }else {
                recycler.setVisibility(View.GONE);
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


        } catch (Exception e) {
            e.printStackTrace();

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
            Toast toast = Toast.makeText(getActivity(), "Wrong item scanned!", Toast.LENGTH_LONG);View toastView = toast.getView();

            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(18);
            toastMessage.setTextColor(Color.RED);
            toastMessage.setGravity(Gravity.CENTER);
            toastMessage.setCompoundDrawablePadding(5);
            toastView.setBackgroundColor(Color.TRANSPARENT);
            toast.show();
            final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.alert);
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
                    try{
                        ProgressHUD.show(getActivity(), "Sending data...", true, false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    new StartSession(getActivity(), new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UpLoadDetails().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    private boolean isPresentToLocal_Doneflg() {

        Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_SECONDARY_BOX + "' WHERE Flag=?", new String[]{String.valueOf(WebUrlClass.DoneFlag_Complete)});
        if (c.getCount() == 0) {
            return true;
        } else {
            return false;
        }
    }
    public void printReceipt(String DONumber, String  qty) {

        //  for(int b =0; b<5; b++){
        final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
        final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
        final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
        final byte[] SMALLFONT = {0x1b, 0x21, 0x01}; //small font
        final byte[] DEFAULT = {0x1b, 0x21, 0x00};
        final byte[] NORMAL = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text

        byte[] format = {27, 33, 0};
        byte[] arrayOfByte1 = {27, 33, 0};
        // Small
        format[2] = ((byte) (0x1 | arrayOfByte1[2]));

        String msg = "", company = "";
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
        double weight = 0;




        try {


            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter codeWriter;
            codeWriter = new QRCodeWriter(); //CodeWriter
            BitMatrix byteMatrix = codeWriter.encode("4368534", BarcodeFormat.QR_CODE, 500, 200, hintMap);
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
            mService.write(ALIGN_RIGHT);
            byte[] command = Utils_print.decodeBitmap(bitmap);
            mService.write(command);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        DONumber="  DO Number:4368534";

        msg += "  " + DONumber + /*"" + Itemcode + */"\n\n";

        // String n="Alfa Laval";

        msg += "" + CartanCode + "\n";

        // msg += "" + Itemdesc + "\n";

        //msg += " Loc/Area : " + LocationCode + "/" + area + "\n";

        // msg += "Qty       : "+qty+"\n";

        //  msg += "" + GRNDate + "";

        if (msg.length() > 0) {
            mService.write(ALIGN_LEFT);
            mService.write(NORMAL);
            mService.sendMessage(msg + "\n", "GBK");
        }

        b.dismiss();



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CreateSecondaryPackActivity.REQUEST_ENABLE_BT && resultCode == getActivity().RESULT_OK) {
            //bluetooth enabled and request for showing available bluetooth devices
            // Toast.makeText(parent, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            BluetoothClass.pairPrinter(getActivity(), getActivity());
        }else if (requestCode == CreateSecondaryPackActivity.REQUEST_CONNECT_DEVICE && resultCode == getActivity().RESULT_OK) {
            //bluetooth device selected and request pairing with device
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            BluetoothClass.pairedPrinterAddress(getActivity(), getActivity(),address);
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
                            Toast.makeText(getActivity(), "Connect successful",
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
                    Toast.makeText(getActivity(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(getActivity(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        PackOrderNo=userpreferences.getString("OrdNo","");
        CartanCode=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");
        if (!PackOrderNo.equals("")) {
            //getSupportActionBar().setTitle(PackOrderNo +" \n" +CartanCode);
          //  getSupportActionBar().setTitle(PackOrderNo);

            if (Check_Db()) {
                DOdetailPacket();
            }
        }


    }


    private class GetPacketValidation extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_CheckPacketValidation+"?Pick_ListHdrId="+Pick_ListHdrId+"&PacketNo="+data;
            try {
                res = ut.OpenConnection(url, getActivity());
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
                Toast toast = Toast.makeText(getActivity(), "Item Not Present", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.alert);
                mp.start();

            }else {

                if (s.contains("PacketMasterId")) {
                    try {
                        JSONArray jResults = new JSONArray(s);
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            Quantity = jsonObject.getInt("BalQty");
                            ItemCode = jsonObject.getString("ItemCode");
                            int BalToPick = jsonObject.getInt("BalToPick");
                            int FiFoQtyBalanceTopick = jsonObject.getInt("FiFoQtyBalanceTopick");
                            FiFoQtyToPick = jsonObject.getInt("FiFoQtyToPick");
                            LotQtyToPick = jsonObject.getInt("LotQtyToPick");
                            // int QtyToPick = jsonObject.getInt("QtyToPick");


                            if (secondaryBoxArrayList.size() > 0) {
                                for (int j = 0; j < secondaryBoxArrayList.size(); j++) {
                                    if (secondaryBoxArrayList.get(j).getItemCode().equals(ItemCode)) {
                                        String Picked = String.valueOf(secondaryBoxArrayList.get(j).getQtyPacked());
                                        String Pick = String.valueOf(secondaryBoxArrayList.get(j).getQtyToPack());
                                        ItemMasterId=secondaryBoxArrayList.get(j).getItemMasterId();

                                        if (Pick.equals(Picked)) {
                                            Toast toast = Toast.makeText(getActivity(), "Packing completed for "+ItemCode, Toast.LENGTH_LONG);View toastView = toast.getView();

                                            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                                            toastMessage.setTextSize(18);
                                            toastMessage.setTextColor(Color.GREEN);
                                            toastMessage.setGravity(Gravity.CENTER);
                                            toastMessage.setCompoundDrawablePadding(5);
                                            toastView.setBackgroundColor(Color.TRANSPARENT);
                                            toast.show();
                                            final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.ok);
                                            mp.start();
                                        } else {
                                            filter(ItemCode, data);
                                        }
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
                res = ut.OpenPostConnection(url,FinalJson, getActivity());
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try{
                ProgressHUD.Destroy();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (s.contains("Success")) {


                Toast toast = Toast.makeText(getActivity(), "Data Send Successfully", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.ok);
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
                            sql.update(db.TABLE_SECONDARY_BOX, values, "ItemMasterId=?", new String[]{String.valueOf(ItemMasterId)});
                            onResume();
                        }
                    }
                }

            } else {

                Toast toast = Toast.makeText(getActivity(), "Record not saved", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.alert);
                mp.start();
            }
        }
    }
    public class PostShipment extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_AWBShipmentCreation + "?Pack_OrdHdrId=" + Pack_OrdHdrId +"&AWBNo="+PackOrderNo;
            try {
                res = ut.OpenConnection(url, getActivity());
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
            ProgressHUD.Destroy();

            String s = String.valueOf(res);
            if (s.contains("false")) {
                Toast.makeText(getActivity(), "Please check stock", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(getActivity(), "Server Error....", Toast.LENGTH_LONG).show();
            } else if (s.contains("true")) {
                Toast.makeText(getActivity(), "Record saved Successfully", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove(WebUrlClass.MyPREFERENCES_CODE);
                editor.remove("OrdNo");
                editor.commit();
                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                    startActivity(new Intent(getActivity(), DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    //  finish();
                }else {
                    startActivity(new Intent(getActivity(), ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                }
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
                res = ut.OpenPostConnection(url, params[0], getActivity());
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();
            if (s.contains(",")) {

                String[] namesList = s.split(",");
                String Cartan_ID = namesList[0].replace("\"", "");
                CartanCode = namesList[1];

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_HEADER, Cartan_ID);
                editor.putString(WebUrlClass.MyPREFERENCES_CODE, CartanCode);
                editor.commit();

                final MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.ok);
                mp.start();



                Toast.makeText(getActivity(),"Carton created successfully",Toast.LENGTH_SHORT).show();
               // getSupportActionBar().setTitle(PackOrderNo);
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
                res = ut.OpenConnection(url, getActivity());
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


}
