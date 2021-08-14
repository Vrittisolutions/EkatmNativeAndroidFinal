package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.sales.CounterBilling.BillsHistory;
import com.vritti.sales.CounterBilling.ItemListCB;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.vwb.vworkbench.ActivityMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Sales_HomeSActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    LinearLayout cntrbilng, ordbkng, shipment_invoice, pending_deliveries, transit_shipments, order_history,
            bills_history,soaapr, lay_delivery_module, all_orders_history;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "",  usertype = "", username = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_sales__home_s);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(AnyMartData.AppCode.equalsIgnoreCase("")){
            new DownloadGetEnvJSON().execute();
        }else {

        }

        setListeners();
    }

    public void init(){
        parent = Sales_HomeSActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle(R.string.app_name_toolbar_sales);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ordbkng = (LinearLayout)findViewById(R.id.ordbkng);
        cntrbilng = (LinearLayout)findViewById(R.id.cntrbilng);
        shipment_invoice = (LinearLayout)findViewById(R.id.shipment_invoice);
        pending_deliveries = (LinearLayout)findViewById(R.id.pending_deliveries);
        transit_shipments = (LinearLayout)findViewById(R.id.transit_shipments);
        transit_shipments.setVisibility(View.VISIBLE);
        order_history = (LinearLayout)findViewById(R.id.order_history);
        order_history.setVisibility(View.VISIBLE);
        bills_history = (LinearLayout)findViewById(R.id.bills_history);
        soaapr = (LinearLayout)findViewById(R.id.soappr);
        lay_delivery_module = findViewById(R.id.lay_delivery_module);
        all_orders_history = findViewById(R.id.all_orders_history);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);

        ut = new Utility();
        cf = new Tbuds_commonFunctions(Sales_HomeSActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.AppCode = sharedpreferences.getString("AppCode","");
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

    }

    public void setListeners(){

        ordbkng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //test
               /* int year;
                int month;
                int day;
                String today, todaysDate;
                String date = null;
                String time = null;
                String DATE = "", TIME = "";

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                String yr = String.valueOf(year);

                if(yr.equals("2020")){
                    try{
                        Date today1 = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(today1);

                        calendar.add(Calendar.MONTH, 1);
                        calendar.set(Calendar.DAY_OF_MONTH, 1);
                        calendar.add(Calendar.DATE, -1);

                        Date lastDayOfMonth = calendar.getTime();
                        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        String a = sdf.format(lastDayOfMonth);

                        date = a;
                        DATE = date;
                    }catch (Exception e) {
                        e.printStackTrace();

                        date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                        DATE = date;
                    }
                }else {
                    date = day + "-" + String.format("%02d", (month + 1)) + "-" + year;
                    DATE = date;
                }

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                time = updateTime(hour, minute);
                TIME = time;*/

                Intent intent1 = new Intent(Sales_HomeSActivity.this, Sales_OrderTypeSelectionActivity.class);
                //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                // finish();
            }
        });

        soaapr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(Sales_HomeSActivity.this, SOApproveActivity.class);
                // intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                // finish();
            }
        });

        cntrbilng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent1 = new Intent(Sales_HomeSActivity.this, CounterBillingMainActivity.class);
                // intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();*/

                Intent intent = new Intent(Sales_HomeSActivity.this, ItemListCB.class);
                intent.putExtra("intentFrom", "Sales_HomeActivity");
                startActivity(intent);
            }
        });

        shipment_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Mobileno", AnyMartData.MOBILE);
                editor.putString("usertype", usertype);
                editor.putString("username", username);
                editor.putString("CustVendorMasterId", AnyMartData.USER_ID);
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.commit();

                Intent intent = new Intent(Sales_HomeSActivity.this, ShipmentAndInvoicing.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });

        pending_deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity not assigned to delivery boy
                    Intent intent = new Intent(parent, PendingDeliveries.class);
                    startActivity(intent);
            }
        });

        transit_shipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity assigned to delivery boy but not completed yet
                Intent intent = new Intent(parent, TransitShipmentActivity.class);
                startActivity(intent);
               /* if(getTransitShipments()){
                    Toast.makeText(parent,"transit shipments available",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(parent,"No transit shipments",Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,SOHistoryActivity.class);
                startActivity(intent);
            }
        });

        bills_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, BillsHistory.class);
                startActivity(intent);

            }
        });

        lay_delivery_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(parent, ReceiptActivity.class);
                 startActivity(intent);
            }
        });

        all_orders_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyOrderHistory_Tabactivity.class);
                intent.putExtra("appName","");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean getPendingDeliveries(){
        sql = db.getReadableDatabase();

        String invoice;

        String query = "Select * from  "+ db.TABLE_SHIPMENT_INVOICE + " WHERE isActivityAssigned ='N'";
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() == 0){
            Toast.makeText(parent,"No pending deliveries",Toast.LENGTH_SHORT).show();

            return false;
        }else {
            c.moveToFirst();
            do{
                invoice = c.getString(c.getColumnIndex("InvoiceNo"));

                // Toast.makeText(parent,"Invoice : "+invoice, Toast.LENGTH_SHORT).show();
                // Toast.makeText(parent,"Invoice : "+invoice + "Count - "+c.getCount(), Toast.LENGTH_SHORT).show();

            }while (c.moveToNext());

            return true;
        }
    }

    public boolean getTransitShipments(){
        sql = db.getReadableDatabase();

        String invoice, ActDesc;

        String query = "Select * from  "+ db.TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS + " WHERE isActivityCompleted ='N'";
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() == 0){
            Toast.makeText(parent,"No transit shipments",Toast.LENGTH_SHORT).show();

            return false;
        }else {
            c.moveToFirst();
            do{
                invoice = c.getString(c.getColumnIndex("InvoiceNo"));
                ActDesc = c.getString(c.getColumnIndex("AssignActivityDesc"));

                Toast.makeText(parent,"ActDesc : "+ActDesc + "Count -  "+c.getCount(), Toast.LENGTH_SHORT).show();

            }while (c.moveToNext());

            return true;
        }
    }

    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, String> {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\\\\\\\"", "");
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res.contains("AppEnvMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        String data = jorder.getString("AppEnvMasterId");
                       String AppCode = jorder.getString("AppCode");

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("AppCode",AppCode);
                        editor.commit();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            } else {
                //Toast.makeText(getApplicationContext(), "Server not responding", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

}
