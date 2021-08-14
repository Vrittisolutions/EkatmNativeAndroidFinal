package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.CounterAuditorSelectActvity;
import com.vritti.inventory.physicalInventory.activity.ItemMasterSyncActivity;
import com.vritti.inventory.physicalInventory.activity.LocationPIActivity;
import com.vritti.inventory.physicalInventory.activity.PIEntryPrintingActivity;
import com.vritti.inventory.physicalInventory.activity.PartCodeActivity;
import com.vritti.inventory.physicalInventory.activity.PartNameActivity;
import com.vritti.inventory.physicalInventory.activity.ReprintScreenActivity;
import com.vritti.inventory.physicalInventory.activity.SelectLableSize;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.Constants_wifi;
import com.vritti.inventory.physicalInventory.bean.Util_Wifi_print;
import com.vritti.sales.CounterBilling.DeviceListActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class LabelPrintData extends AppCompatActivity {


    TextInputEditText etItemId,etItemDes,etheatno,etmono,etpacketqty,etunit,etgrade,etFacility,etWereHouse,etlocation,etpackno;
    Button save,clear;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    public static final int REQ_PARTCODE = 7;
    public static final int REQ_PARTNAME = 8;
    public static final int REQ_LOCATION = 9;
    double CONV_factor = 1;



    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_add_label_data_lay);


        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(LabelPrintData.this);
        String settingKey = ut.getSharedPreference_SettingKey(LabelPrintData.this);
        String dabasename = ut.getValue(LabelPrintData.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(LabelPrintData.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(LabelPrintData.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(LabelPrintData.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(LabelPrintData.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(LabelPrintData.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(LabelPrintData.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(LabelPrintData.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(LabelPrintData.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(LabelPrintData.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        etItemId=findViewById(R.id.etItemId);
        etItemDes=findViewById(R.id.etItemDes);
        etheatno=findViewById(R.id.etheatno);
        etmono=findViewById(R.id.etmono);
        etpacketqty=findViewById(R.id.etpacketqty);
        etunit=findViewById(R.id.etunit);
        etgrade=findViewById(R.id.etgrade);
        etFacility=findViewById(R.id.etFacility);
        etWereHouse=findViewById(R.id.etWereHouse);
        etlocation=findViewById(R.id.etlocation);
        etpackno=findViewById(R.id.etpackno);
        save=findViewById(R.id.save);
        clear=findViewById(R.id.clear);


        etItemId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (etItemId != null) {
                                etItemId.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                    String data = etItemId.getText().toString();
                    Log.d("JSon",data);

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;



                    qrgEncoder = new QRGEncoder(
                            data, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                    } catch (WriterException t) {
                        t.printStackTrace();
                    }


                    return true;
                } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                    String data = etItemId.getText().toString();
                    return true;
                }
                return false;
            }
        });




        etItemId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabelPrintData.this, PartCodeActivity.class);
                startActivityForResult(intent,REQ_PARTCODE);
            }
        });

        etItemDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabelPrintData.this, PartNameActivity.class);
                startActivityForResult(intent,REQ_PARTNAME);
            }
        });

        etlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LabelPrintData.this, LocationPIActivity.class);
                startActivityForResult(intent,REQ_LOCATION);
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                JSONObject jsonObject1=new JSONObject();

                JSONArray jsonArray=new JSONArray();


                try {
                    jsonObject1.put( "Itemcode",etItemId.getText().toString());
                    jsonObject1.put("ItemDesc",etItemDes.getText().toString());
                    jsonObject1.put("HeatNo",etheatno.getText().toString());
                    jsonObject1.put("MoNo",etmono.getText().toString());
                    jsonObject1.put("PacketQty",etpacketqty.getText().toString());
                    jsonObject1.put("Unit",etunit.getText().toString());
                    jsonObject1.put("MaterialGrade",etgrade.getText().toString());
                    jsonObject1.put("PacketNo",etpackno.getText().toString());



                    JSONObject jobjdata = new JSONObject();
                    JSONArray ob = new JSONArray();
                    try {
                        JSONObject a = new JSONObject(jsonObject1.toString());
                        ob.put(a);
                        jobjdata.put("Data11", ob);
                    } catch (JSONException e) {

                    }

                    jobjdata.put("PrinterName","HP LaserJet Pro MFP M125-M126 PCLmS");

                   String finaljson = jobjdata.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");





                } catch (JSONException e) {
                    e.printStackTrace();
                }





                if (isnet()) {
                    ProgressHUD.show(LabelPrintData.this, "Sending data ...", true, false);
                    new StartSession(LabelPrintData.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new  UpLoadData().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {
                            new  UpLoadData().execute();

                        }


                    });

                } else {
                   Toast.makeText(LabelPrintData.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }




            }
        });

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
            String strRes = null;
            String url = CompanyURL + WebUrlClass.api_PrintLabelarray;

            try {
                res =ut.OpenPostConnection(url, params[0],LabelPrintData.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.contains("true")) {

            } else {
                Toast.makeText(LabelPrintData.this, "Data Not Send", Toast.LENGTH_LONG).show();
            }

            ProgressHUD.Destroy();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.itemsync){
            Intent intent = new Intent(this, AlfaItemMasterSyncActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

                etItemId.setText(result.getContents());

                GetItemCode(result.getContents());


                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == REQ_PARTCODE && resultCode == REQ_PARTCODE){
            etItemId.setText(data.getStringExtra("PartCode"));
            etItemDes.setText(data.getStringExtra("PartName"));
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            etlocation.setText(data.getStringExtra("LocationCode"));
            etunit.setText(data.getStringExtra("uomval"));
            //Toast.makeText(this, "getting back frm ocr",Toast.LENGTH_SHORT).show();


        }else if(requestCode == REQ_PARTNAME && resultCode == REQ_PARTNAME){
            etItemId.setText(data.getStringExtra("PartCode"));
            etItemDes.setText(data.getStringExtra("PartName"));
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            etlocation.setText(data.getStringExtra("LocationCode"));
            etunit.setText(data.getStringExtra("uomval"));


        }else if(requestCode == REQ_LOCATION && resultCode == REQ_LOCATION){
            etlocation.setText(data.getStringExtra("LocationCode"));

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void GetItemCode(String itemcode) {
        //String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode like '%" + itemcode + "%'";
        String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode='"+itemcode+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                String itemcode1=cur.getString(cur.getColumnIndex("ItemDesc"));

                etItemDes.setText(itemcode1);
                try{
                    String cnv = cur.getString(cur.getColumnIndex("ConvFactor"));
                    if(cnv.equalsIgnoreCase("null")){
                        CONV_factor = 0;
                    }else {
                        CONV_factor = Double.parseDouble(cnv);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            } while (cur.moveToNext());
        }
    }





}
