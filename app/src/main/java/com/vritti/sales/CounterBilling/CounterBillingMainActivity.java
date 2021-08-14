package com.vritti.sales.CounterBilling;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.AdapterCounterBilling;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.zj.btsdk.BluetoothService;

import java.net.DatagramSocket;
import java.util.ArrayList;

/**
 * Created by 300151 on 7/19/2016.
 */
public class CounterBillingMainActivity extends AppCompatActivity {
    GridView cdView;
    Dialog dialog;
    CounterbillingBean counterbillingBean;
    ArrayList<CounterbillingBean> counterbillingBeanArrayList;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_counter_billing);

      /*  getSupportActionBar().setTitle("Counter Billing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        init();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Counter Billing");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        //dbhandler = new DatabaseHandler(this);
        if (tcf.getAllCustomerCount() > 0) {
            getDataFromDataBase();
        }

     //   connectDevice();
       /* mService = new BluetoothService(CounterBillingMainActivity.this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(CounterBillingMainActivity.this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
        }*/
       /* dbhandler.deleteCart();*/
        cdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CounterBillingMainActivity.this, ItemListCB.class);
                intent.putExtra("CustomerName", counterbillingBeanArrayList.get(position).getCustName());
                intent.putExtra("CustomerMobno", counterbillingBeanArrayList.get(position).getMobileNo());
                intent.putExtra("intentFrom", "CounterBillingActivity");
                startActivity(intent);
                //finish();
            }
        });
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        counterbillingBeanArrayList.clear();
       // SQLiteDatabase db = dbhandler.getWritableDatabase();
        String que = "Select  * from " + dbhandler.TABLE_CUSTOMER_CB;
        Cursor c = sql_db.rawQuery(que, null);
        Log.d("test", "" + c.getCount());
        if (c.getCount() > 0) {
            c.moveToFirst();
            try {
                do {
                    String cust_name = c.getString(c.getColumnIndex("FullName"));
                    String cust_mob = c.getString(c.getColumnIndex("Mobile"));

                    counterbillingBean = new CounterbillingBean();
                    counterbillingBean.setCustName(cust_name);
                    counterbillingBean.setMobileNo(cust_mob);
                    counterbillingBeanArrayList.add(counterbillingBean);
                } while (c.moveToNext());
            } finally {

                /*db.close();
                c.close();*/
            }
        }

        cdView.setAdapter(new AdapterCounterBilling(CounterBillingMainActivity.this,
                counterbillingBeanArrayList));
    }

    private void init() {

        cdView = (GridView) findViewById(R.id.listview_customer_list);
        counterbillingBeanArrayList = new ArrayList<CounterbillingBean>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CounterBillingMainActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tbuds_add_counter_billing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
              //  Toast.makeText(CounterBillingMainActivity.this, "Add new Items", Toast.LENGTH_LONG).show();
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.tbuds_dialog_add_customer);
                dialog.setTitle("Add new customer");
                final EditText editText_customerName = (EditText) dialog.findViewById(R.id.editText_customerName);
                final EditText editText_MobNo = (EditText) dialog.findViewById(R.id.editText_MobNo);
                final Button ok = (Button) dialog.findViewById(R.id.buttonOk);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText_customerName.getText().toString().equalsIgnoreCase("") &&
                                editText_customerName.getText().toString().equalsIgnoreCase(null)) {
                            /*if(editText_customerName.getText().toString().isEmpty() || )
                            {
                                editText_customerName.setError("This field should not be blank");
                            }else {

                            }*/
                            //editText_customerName.setError("Name should not be blank");

                        } else {
                            tcf.addCustomer(editText_customerName.getText().toString(), editText_MobNo.getText().toString());
                        }
                        dialog.dismiss();
                        getDataFromDataBase();
                    }
                });

                dialog.show();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectDevice() {
        // TODO
        String address = getBluetoothAddress(CounterBillingMainActivity.this);
        if (address != null) {
            con_dev = mService.getDevByMac(address);
            mService.connect(con_dev);
            Log.e("Auto connected", "state : " + mService.getState());
        } else {
            scanBluetooth();
        }
    }
    private void scanBluetooth() {

        startActivityForResult(new Intent(CounterBillingMainActivity.this, DeviceListActivity.class),
                AnyMartData.REQUEST_CONNECT_DEVICE);
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg1) {
            switch (msg1.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg1.arg1) {
                        case BluetoothService.STATE_CONNECTED: // ÒÑÁ¬½Ó
                            Toast.makeText(CounterBillingMainActivity.this, "Connect successful",
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
                    Toast.makeText(CounterBillingMainActivity.this, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(CounterBillingMainActivity.this, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AnyMartData.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(CounterBillingMainActivity.this, "Bluetooth open successful",
                            Toast.LENGTH_LONG).show();
                } else {
                    // finish();
                }
                break;
            case AnyMartData.REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    clearTable(CounterBillingMainActivity.this, "Bluetooth_Address");
                    tcf.AddBluetooth(address);
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                    Log.e("bluetooth state", "state : " + mService.getState());
                }

                break;


        }
    }
    public static void clearTable(Context parent, String tablename) {
     //   DatabaseHandler db = new DatabaseHandler(parent);
     //   SQLiteDatabase sql = db.getWritableDatabase();
        sql_db.delete(tablename, null, null);

      /*  sql.close();
        db.close();*/
    }

    public static String getBluetoothAddress(Context parent) {
       // DatabaseHandler db1 = new DatabaseHandler(parent);
       // SQLiteDatabase sql = db1.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery("Select * from Bluetooth_Address", null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String str = cursor.getString(0);
         /*   cursor.close();
            sql.close();
            db1.close();*/
            return str;

        } else {

          /*  cursor.close();
            sql.close();
            db1.close();*/
            return null;
        }
    }

}
