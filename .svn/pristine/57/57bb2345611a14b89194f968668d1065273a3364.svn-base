package com.vritti.AlfaLavaModule.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vritti.AlfaLavaModule.adapter.AdapterSecondaryDetail;
import com.vritti.AlfaLavaModule.bean.PacketListDetail;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
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

public class ShipingInspectionSheetActivity extends AppCompatActivity {
    private Context pContext;
    @BindView(R.id.edt_scanPacket)
    TextView edt_scanPacket;
    String PackOrderNo, PacketNo;
    DownloadPacketedOrdNoDetails downloadPutAwayDetails;
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
            QtyToPack = "", QtyPacked = "", ItemCode = "", ItemDesc = "", SoScheduleId = "",CartanCode="";

    String Qty="",Quantity="";
    JSONObject jsonObject;
    private String data;
    AlertDialog b;
    private String HeatNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondarybox_scan_lay);
        ButterKnife.bind(this);
        pContext = ShipingInspectionSheetActivity.this;
        getSupportActionBar().setTitle("Shipping Inspection Sheet");

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

                    data="";
                    data = edt_scanPacket.getText().toString().trim();
                    try {
                        PacketNo = new JSONObject(data).getString("PacketNo");
                        ItemCode = new JSONObject(data).getString("Itemcode");
                        Qty = new JSONObject(data).getString("PacketQty");
                        HeatNo = new JSONObject(data).getString("HeatNo");


                        edt_scanPacket.setText("");

                       
                        //filter(ItemCode, PacketNo);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                      if(PacketNo != null && !(PacketNo.equals(""))) {

                          getdialog();



                    }


                    return true;
                }
                return false;
            }
        });


    }


    public class DownloadPacketedOrdNoDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTInsertShippingInspection;
            try {
                res = ut.OpenPostConnection(url,jsonObject.toString(),pContext);
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
            b.dismiss();
            if (res.contains("Success")){
                Toast.makeText(ShipingInspectionSheetActivity.this,"Record save successfully",Toast.LENGTH_SHORT).show();
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
    private void getdialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShipingInspectionSheetActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alfa_shipping_qty_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText textotp = (EditText) dialogView.findViewById(R.id.edt_qty);
        Button button = (Button) dialogView.findViewById(R.id.txt_submit);
        Button txt_cancel = (Button) dialogView.findViewById(R.id.txt_cancel);
        dialogBuilder.setCancelable(false);

        b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(true);
        b.show();

        // if button is clicked, close the custom dialog

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textotp.getText().toString().equals("null")||textotp.getText().toString().equals("")||textotp.getText().toString()==null) {
                    Toast.makeText(ShipingInspectionSheetActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                }else {
                    Quantity = textotp.getText().toString();


                    try {
                        jsonObject=new JSONObject();
                        jsonObject.put("HeatNo",HeatNo);
                        jsonObject.put("PacketNo",PacketNo);
                        jsonObject.put("ItemCode",ItemCode);
                        jsonObject.put("Qty",Quantity);
                        jsonObject.put("UserMasterId",UserMasterId);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    
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
            }
        });



    }


}




