package com.vritti.AlfaLavaModule.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.AlfaLavaModule.activity.packaging.ReceiptPackagingDOListActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

public class CreateSecondaryPackActivity extends AppCompatActivity {


    private static String ScanGRN = null;
    EditText s_search;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private String  boxTypeMasterId;
    String ItemMasterId="",Pack_OrdHdrId="",DOnumber="",SoScheduleId="",PacketNo="",PackOrderNo="";

    double Quantity;

    public static final int REQUEST_ENABLE_BT = 4;
    public static final int REQUEST_CONNECT_DEVICE = 6;

    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;
    Button btn_print,btn_suspend,btn_finish;
    private String CartanHeaderId;
    private String FinalJson="";
    AlertDialog b;
    private String Weight;
    private String Flag="No";
    private String Cartan_code="";
    int currentTotal=0;
    int currentQty=0;
    int Qty=0;
    int Packed=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_cartan);




        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(CreateSecondaryPackActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(CreateSecondaryPackActivity.this);
        String dabasename = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(CreateSecondaryPackActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(CreateSecondaryPackActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        mService = new BluetoothService(CreateSecondaryPackActivity.this, mHandler);

        if (mService.isAvailable() == false) {
            Toast.makeText(CreateSecondaryPackActivity.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
        if (mService.isAvailable() == false) {
            Toast.makeText(CreateSecondaryPackActivity.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        init();


    }

    private void init() {

        btn_print=findViewById(R.id.btn_cprint);
        btn_finish=findViewById(R.id.btn_finish);
        btn_suspend=findViewById(R.id.btn_suspend);


        ItemMasterId= getIntent().getStringExtra("itemid");
        Pack_OrdHdrId=getIntent().getStringExtra("packorder");
        if (getIntent().hasExtra("boxTypeMasterId")){
            boxTypeMasterId=getIntent().getStringExtra("boxTypeMasterId");
        }
        SoScheduleId=getIntent().getStringExtra("soScheduleId");
        Qty= getIntent().getIntExtra("qty",0);
        PacketNo= getIntent().getStringExtra("packetno");
        Packed= getIntent().getIntExtra("Packed",0);
        if (getIntent().hasExtra("dono")){
            PackOrderNo=getIntent().getStringExtra("dono");
        }


        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");

        if (CartanHeaderId.equals("")){

            final JSONObject jsonObject=new JSONObject();

            // SoScheduleId - 790F73BF-E734-4184-A376-FC406F9965F9
            try {
                jsonObject.put("RefType","Pack");
                jsonObject.put("RefId",Pack_OrdHdrId);
                jsonObject.put("BoxTypeMasterId",boxTypeMasterId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            btn_print.setVisibility(View.VISIBLE);

            if (isnet()) {
                ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending  data...", true, false);
                new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new UpLoadData().execute(jsonObject.toString());;
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        new UpLoadData().execute();;

                    }


                });

            } else {
                Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }else {
         //   CartanHeaderId = CartanHeaderId.substring(1, CartanHeaderId.length() - 1);


            final JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("CartonHeaderId",CartanHeaderId);
                jsonObject.put("ItemMasterId",ItemMasterId);
                jsonObject.put("Pack_OrdHdrId",Pack_OrdHdrId);
                jsonObject.put("PacketNo",PacketNo);
                Quantity= Qty;
                jsonObject.put("Qty",Quantity);
                
                FinalJson=jsonObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isnet()) {
                ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending data...", true, false);
                new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new UpLoadDetails().execute();;
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        new UpLoadDetails().execute();;

                    }


                });

            } else {
                Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(CreateSecondaryPackActivity.this, "Data Send Successfully", Toast.LENGTH_LONG).show();

        }

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Constants.type == Constants.Type.Alfa) {
                    getdialog();
                }else {

                    if (isnet()) {
                        ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending data...", true, false);
                        new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostShipment().execute();
                                ;
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                new PostShipment().execute();
                                ;

                            }


                        });

                    } else {
                        Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Cartan_code=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
                if(BluetoothClass.isPrinterConnected(getApplicationContext(),CreateSecondaryPackActivity.this)) {
                    mService = BluetoothClass.getServiceInstance();
                    printReceipt(Cartan_code);
                }

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




    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
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
                res = ut.OpenPostConnection(url, params[0], CreateSecondaryPackActivity.this);
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
                String Cartan_ID = namesList [0].replace("\"","");
                Cartan_code = namesList [1];

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_HEADER, Cartan_ID);
                editor.putString(WebUrlClass.MyPREFERENCES_CODE, Cartan_code);
                editor.commit();

                final MediaPlayer mp = MediaPlayer.create(CreateSecondaryPackActivity.this, R.raw.ok);
                mp.start();

/*
                final JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("CartonHeaderId",Cartan_ID);
                    jsonObject.put("ItemMasterId",ItemMasterId);
                    jsonObject.put("Pack_OrdHdrId",Pack_OrdHdrId);
                    jsonObject.put("PacketNo",PacketNo);
                    Quantity= Qty;
                    jsonObject.put("Qty",Quantity);
                    FinalJson=jsonObject.toString();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isnet()) {
                    ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending data...", true, false);
                    new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UpLoadDetails().execute(jsonObject.toString());;
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            new UpLoadDetails().execute();;

                        }


                    });

                } else {
                    Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/


              //  Toast.makeText(CreateSecondaryPackActivity.this, "Data Send Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreateSecondaryPackActivity.this, "Data Not Send", Toast.LENGTH_LONG).show();
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
                res = ut.OpenPostConnection(url,FinalJson, CreateSecondaryPackActivity.this);
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

            if (s.contains("Success")) {



                Toast toast = Toast.makeText(CreateSecondaryPackActivity.this, "Data Send Successfully", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(CreateSecondaryPackActivity.this, R.raw.ok);
                mp.start();

                try {
                    ContentValues values = new ContentValues();
                    currentTotal = Packed + Qty;
                    values.put("QtyPacked", currentTotal);
                    sql.update(db.TABLE_SECONDARY_BOX, values, "ItemMasterId=?", new String[]{String.valueOf(ItemMasterId)});
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                    startActivity(new Intent(CreateSecondaryPackActivity.this,
                            DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }else {
                    startActivity(new Intent(CreateSecondaryPackActivity.this,
                            DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            } else {

                Toast toast = Toast.makeText(CreateSecondaryPackActivity.this, "Record not saved", Toast.LENGTH_LONG);View toastView = toast.getView();

                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(CreateSecondaryPackActivity.this, R.raw.alert);
                mp.start();

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProgressHUD.Destroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CreateSecondaryPackActivity.REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            //bluetooth enabled and request for showing available bluetooth devices
            // Toast.makeText(parent, "Bluetooth open successful", Toast.LENGTH_LONG).show();
            BluetoothClass.pairPrinter(getApplicationContext(),CreateSecondaryPackActivity.this);
        }else if (requestCode == CreateSecondaryPackActivity.REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            //bluetooth device selected and request pairing with device
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            BluetoothClass.pairedPrinterAddress(getApplicationContext(),CreateSecondaryPackActivity.this,address);
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
                            Toast.makeText(CreateSecondaryPackActivity.this, "Connect successful",
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
                    Toast.makeText(CreateSecondaryPackActivity.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(CreateSecondaryPackActivity.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    public void printReceipt(String DONumber) {

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

        String msg = null, company = "";
        String itemcode = "", itemdesc = "", dateTime = "", username = "", countedby = "", area = "", verifyby = "";
        double weight = 0;




            try {


                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
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
                mService.write(ALIGN_RIGHT);
                byte[] command = Utils_print.decodeBitmap(bitmap);
                mService.write(command);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

          /*  if (PacketNo.length() > 21) {
                PacketNo = PacketNo.substring(0, 21);
            } else if (PacketNo.length() <= 21) {
                int diff = 21 - PacketNo.length();
                for (int i = 0; i < diff; i++) {
                    PacketNo = " " + PacketNo;
                }
            }

            // msg = " No.: " + billnumber + "         " + itemcode + "\n";
            //msg = "\n " + PacketNo + "   " + PacketQty + "\n";
            msg = "\n " + PacketQty + "   " + PacketNo + "\n";
            //msg += " "+itemcode+"\n";

            if (Itemdesc.length() > 32) {
                Itemdesc = Itemdesc.substring(0, 32);
            } else if (Itemdesc.length() <= 32) {
                int diff = 32 - Itemdesc.length();
                for (int i = 0; i < diff; i++) {
                    Itemdesc += " ";
                }
            }

            if (Itemcode.length() > 30) {
                Itemcode = Itemdesc.substring(0, 30);
            } else if (Itemcode.length() <= 30) {
                int diff = 30 - Itemcode.length();
                for (int i = 0; i < diff; i++) {
                    Itemcode += " ";
                }
            }
*/
            msg += "" + Qty + /*"" + Itemcode + */"\n";

            String n="Alfa Laval";

            msg += "" + n + "\n";

           // msg += "" + Itemdesc + "\n";

            //msg += " Loc/Area : " + LocationCode + "/" + area + "\n";

            // msg += "Qty       : "+qty+"\n";

        //  msg += "" + GRNDate + "";

            if (msg.length() > 0) {
                mService.write(ALIGN_LEFT);
                mService.write(NORMAL);
                mService.sendMessage(msg + "\n", "GBK");
            }



        btn_print.setVisibility(View.GONE);

    }
    private void getdialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateSecondaryPackActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alfa_weight_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
        Button button = (Button) dialogView.findViewById(R.id.txt_submit);
        dialogBuilder.setCancelable(false);

        b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(true);
        b.show();

        // if button is clicked, close the custom dialog

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Weight=textotp.getText().toString();
                b.dismiss();
                if (isnet()) {
                    ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending  data...", true, false);
                    new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            PostWeight postWeight = new PostWeight();
                            postWeight.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


                // b.dismiss();

                /*if(BluetoothClass.isPrinterConnected(getApplicationContext(), DOPackingScanDetails.this)) {
                    mService = BluetoothClass.getServiceInstance();



                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(), DOPackingScanDetails.this);
                }


                if(BluetoothClass.isPrinterConnected(getApplicationContext(), DOPackingScanDetails.this)) {
                    mService = BluetoothClass.getServiceInstance();
                    printReceipt(PackOrderNo,"15");
                }
*/

            }
        });



    }
    public class PostWeight extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_UpdateComputedWt + "?CartonHeaderId=" + CartanHeaderId +"&ComputedWt="+Weight+"&Flag="+Flag;
            try {
                res = ut.OpenConnection(url, CreateSecondaryPackActivity.this);
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

            String s = res;
            if (s.equals("[]")) {
                     Toast.makeText(CreateSecondaryPackActivity.this, "NO Record Present", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(CreateSecondaryPackActivity.this, "Server Error....", Toast.LENGTH_LONG).show();
            }  else if (s.contains("Success")) {
                Toast.makeText(CreateSecondaryPackActivity.this, "Record updated Successfully", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.commit();
                startActivity(new Intent(CreateSecondaryPackActivity.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                 finish();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSecondaryPackActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing but close the dialog
                        if (isnet()) {

                            Flag="Yes";
                            ProgressHUD.show(CreateSecondaryPackActivity.this, "Sending  data...", true, false);
                            new StartSession(CreateSecondaryPackActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    PostWeight postWeight = new PostWeight();
                                    postWeight.execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        } else {
                            Toast.makeText(CreateSecondaryPackActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show(); }
        }
    }


    public class PostShipment extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_AWBShipmentCreation + "?Pack_OrdHdrId=" + Pack_OrdHdrId +"&AWBNo="+PackOrderNo;
            try {
                res = ut.OpenConnection(url, CreateSecondaryPackActivity.this);
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
                Toast.makeText(CreateSecondaryPackActivity.this, "Please check stock", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(CreateSecondaryPackActivity.this, "Server Error....", Toast.LENGTH_LONG).show();
            } else if (s.contains("true")) {
                Toast.makeText(CreateSecondaryPackActivity.this, "Record updated Successfully", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove("OrdNo");
                editor.commit();
                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                    startActivity(new Intent(CreateSecondaryPackActivity.this, DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }else {
                    startActivity(new Intent(CreateSecondaryPackActivity.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        }
    }


}
