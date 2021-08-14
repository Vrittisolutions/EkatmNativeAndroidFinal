package com.vritti.AlfaLavaModule.PI;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPacketActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;

    ArrayList<PutAwayDetail> putAwayDetail;
    String locationCode = "";

    Button btn_create_batch;
    Spinner spinner_warehouse;
    private String warehousename;
    private String  warehouseID;
    EditText txt_packet;
    EditText edt_qty;
    ImageView img_search;

    String PIDtlId="",PIHdrId="",ItemPlantId="",Location="",FIFO="",LOTNo="",PacketNo="",StockDetailsId="",ActualQty="",uuidInString="";
    private String FinalJson;
    private UUID uuid;
    ImageView img_barcode;
    Button btn_ok;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_packet_lay);
        ButterKnife.bind(this);
        pContext = AddPacketActivity.this;
        getSupportActionBar().setTitle("Physical Inventory");

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


        txt_packet=findViewById(R.id.txt_packet);
        edt_qty=findViewById(R.id.edt_qty);
        img_search=findViewById(R.id.img_search);
        img_barcode=findViewById(R.id.img_barcode);
        btn_ok=findViewById(R.id.btn_ok);

        ActualQty=edt_qty.getText().toString();



        PIHdrId=getIntent().getStringExtra("PIHeaderID");
        ItemPlantId=getIntent().getStringExtra("ItemPlantId");
        LOTNo=getIntent().getStringExtra("lot");
        Location=getIntent().getStringExtra("locMasterId");
        FIFO=getIntent().getStringExtra("fifo");
        StockDetailsId=getIntent().getStringExtra("StockDetailsId");

        if (getIntent().hasExtra("Packet")){
            PacketNo=getIntent().getStringExtra("Packet");
            txt_packet.setText(PacketNo);
            getpacket();
        }




        txt_packet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (txt_packet != null) {
                                txt_packet.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay


                    PacketNo = txt_packet.getText().toString().trim();
                    if (PacketNo.contains("PN")||PacketNo.contains("G")) {
                        txt_packet.setText(PacketNo);
                        getpacket();
                    }else {
                        txt_packet.setText("");
                        Toast toast = Toast.makeText(AddPacketActivity.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastView.setBackgroundColor(Color.WHITE);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.alert);
                        mp.start();
                    }

                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PacketNo = txt_packet.getText().toString().trim();
                if (PacketNo.contains("PN")||PacketNo.contains("G")) {
                    txt_packet.setText(PacketNo);
                    getpacket();
                }else {
                    txt_packet.setText("");
                    Toast toast = Toast.makeText(AddPacketActivity.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.alert);
                    mp.start();
                }

            }
        });

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(AddPacketActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }

            });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PacketNo.equals("")) {
                    Toast.makeText(AddPacketActivity.this, "Please scan packet/enter packet", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    ActualQty = edt_qty.getText().toString();
                    PacketNo = txt_packet.getText().toString().trim();
                    txt_packet.setText(PacketNo);
                    getpacket();
                }
            }
        });

       /* PacketNo="G200000001855";


        getpacket();*/








    }

    private void getpacket() {

        UUID uuid = UUID.randomUUID();
        uuidInString = uuid.toString();

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PIDtlId", uuidInString);
            jsonObject.put("PIHdrId", PIHdrId);
            jsonObject.put("ItemPlantId", ItemPlantId);
            jsonObject.put("Location", Location);
            jsonObject.put("FIFO", FIFO);
           // FIFO = formateDateFromstring("dd MMM yyyy", "yyyy-MM-dd", FIFO);
            jsonObject.put("LOTNo", LOTNo);
            jsonObject.put("PacketNo", PacketNo);
            jsonObject.put("StockDetailsId", StockDetailsId);
            jsonObject.put("ActualQty",ActualQty );

            FinalJson = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (isnet()) {
            try{
                ProgressHUD.show(AddPacketActivity.this, "Sending data...", true, false);
            }catch (Exception e){
                e.printStackTrace();
            }



            new StartSession(AddPacketActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpLoadDetails().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(AddPacketActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    private void getpacket_barcode() {

        UUID uuid = UUID.randomUUID();
        uuidInString = uuid.toString();

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("PIDtlId", uuidInString);
            jsonObject.put("PIHdrId", PIHdrId);
            jsonObject.put("ItemPlantId", ItemPlantId);
            jsonObject.put("Location", Location);
            jsonObject.put("FIFO", FIFO);
            // FIFO = formateDateFromstring("dd MMM yyyy", "yyyy-MM-dd", FIFO);
            jsonObject.put("LOTNo", LOTNo);
            jsonObject.put("PacketNo", PacketNo);
            jsonObject.put("StockDetailsId", StockDetailsId);
            jsonObject.put("ActualQty",ActualQty );

            FinalJson = jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (isnet()) {
            try{
                ProgressHUD.show(AddPacketActivity.this, "Sending data...", true, false);
            }catch (Exception e){
                e.printStackTrace();
            }



            new StartSession(AddPacketActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpLoadDetailsBarcode().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(AddPacketActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    private class UpLoadDetails extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.AddPIDtl;
            try {
                res = ut.OpenPostConnection(url,FinalJson, AddPacketActivity.this);
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

            if (s.contains("true")) {

                txt_packet.setText("");

                Toast toast = Toast.makeText(AddPacketActivity.this, "Data Send Successfully", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.ok);
                mp.start();



            } else {
                txt_packet.setText("");
                Toast toast = Toast.makeText(AddPacketActivity.this, s, Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.alert);
                mp.start();
            }
        }
    }


    private class UpLoadDetailsBarcode extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.AddPIDtl;
            try {
                res = ut.OpenPostConnection(url,FinalJson, AddPacketActivity.this);
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

            if (s.contains("true")) {

                txt_packet.setText("");

                Toast toast = Toast.makeText(AddPacketActivity.this, "Data Send Successfully", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.ok);
                mp.start();

                PacketNo=txt_packet.getText().toString();
                IntentIntegrator integrator = new IntentIntegrator(AddPacketActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();


            } else {
                txt_packet.setText("");
                Toast toast = Toast.makeText(AddPacketActivity.this, s, Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.alert);
                mp.start();
            }
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


                PacketNo = result.getContents().toString();
                if (PacketNo.contains("PN")||PacketNo.contains("G")) {
                    txt_packet.setText(PacketNo);
                    getpacket();
                }else {
                    txt_packet.setText("");
                    Toast toast = Toast.makeText(AddPacketActivity.this, "Wrong packet scanned", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(AddPacketActivity.this, R.raw.alert);
                    mp.start();
                }


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
