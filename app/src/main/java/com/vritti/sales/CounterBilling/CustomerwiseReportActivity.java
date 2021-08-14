package com.vritti.sales.CounterBilling;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.CustomerReceivableAdapter;
import com.vritti.sales.beans.PendingBillReport;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sharvari on 8/2/2016.
 */
public class CustomerwiseReportActivity extends AppCompatActivity {
    Button buttonFdate, buttonTdate, buttonCancel, buttonShow;
    GridView gridView;
    static int year, month, day;
    String From_date = null, To_date = null, userid;
    EditText custMob;
   ArrayList<PendingBillReport> billReportArrayList;
    String CustMobno;
    float remaining_amt = 0;
    public static String date;
    private String json;
    SharedPreferences sharedpreferences;
    CustomerReceivableAdapter customerReceivableAdapter;

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
        setContentView(R.layout.tbuds_activity_customerreport);

        /*getSupportActionBar().setTitle("Customer Receivable Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Customer Receivable Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        setListener();
    }

    private void init() {
        buttonFdate = (Button) findViewById(R.id.buttonFdate);
        buttonTdate = (Button) findViewById(R.id.buttonTdate);
        gridView = (GridView) findViewById(R.id.gridView);
        buttonShow= (Button) findViewById(R.id.buttonShow);
        billReportArrayList = new ArrayList<PendingBillReport>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CustomerwiseReportActivity.this);
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

    private void getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable(CustomerwiseReportActivity.this)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetPendingBalance();
            } else {
                new StartSession_tbuds(CustomerwiseReportActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetPendingBalance().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
            Toast.makeText(CustomerwiseReportActivity.this,
                    "The server is taking too long to respond OR something is wrong with your" +
                            " iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
            // callSnackbar();
        }
    }

    public class GetPendingBalance extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;
        String resp_pendingBal = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomerwiseReportActivity.this);
            progressDialog.setMessage("Customer wise balance reports ...");
            progressDialog.show();
        }

       @Override
       protected Void doInBackground(Void... params) {
           String url_PendingBalance = AnyMartData.MAIN_URL + AnyMartData.METHOD_BALANCE +
                   "?sessionid="+ AnyMartData.SESSION_ID + "&handler="+ AnyMartData.HANDLE +
                   "&vendorid="+userid;

           URLConnection urlConnection = null;
           BufferedReader bufferedReader = null;

           try {
               URL urlPendingBalance = new URL(url_PendingBalance);
               urlConnection = urlPendingBalance.openConnection();
               urlConnection.setRequestProperty("Content-Type", "application/json");
               urlConnection.setRequestProperty("Accept", "JSON");
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

               StringBuffer stringBuff_PendingBalance = new StringBuffer();
               String line;
               StringBuilder str_build_prndingBal = new StringBuilder();
               while((line = bufferedReader.readLine())!=null){
                   stringBuff_PendingBalance = stringBuff_PendingBalance.append(line);
               }
               responseString = stringBuff_PendingBalance.toString().replaceAll("^\"|\"$", "");
               resp_pendingBal = responseString.replaceAll("\\\\","");

               Log.e("Response", resp_pendingBal);

           } catch (Exception e) {
               resp_pendingBal = "error";
               e.printStackTrace();
           }
           return null;
       }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (resp_pendingBal.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(CustomerwiseReportActivity.this)) {
                    new StartSession_tbuds(CustomerwiseReportActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetPendingBalance().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (resp_pendingBal.equalsIgnoreCase("error")) {
                Toast.makeText(CustomerwiseReportActivity.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else if (resp_pendingBal.equalsIgnoreCase("No Data present")) {
                Toast.makeText(CustomerwiseReportActivity.this,
                        "No Data present", Toast.LENGTH_LONG)
                        .show();
            } else {
                json = resp_pendingBal;
                parseJson(json);
            }
        }
    }

    protected void parseJson(String json) {

    //    SQLiteDatabase db = dbHandler.getWritableDatabase();
        sql_db.execSQL("DROP TABLE IF EXISTS "
                + dbhandler.TABLE_PENDING_BALANCE);
        final String CREATE_TABLE_PENDING_BALANCE = "CREATE TABLE "
                + dbhandler.TABLE_PENDING_BALANCE
                + "( PbId INTEGER PRIMARY KEY AUTOINCREMENT , BillId TEXT,FinalTotalBill TEXT,Received TEXT" +
                ",Balance TEXT,CustomerName TEXT,Cust_mob TEXT,date TEXT)";
        sql_db.execSQL(CREATE_TABLE_PENDING_BALANCE);

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {

                tcf.addPendingBalance(jsonArray.getJSONObject(i).getString(
                        "orderheaderid"), jsonArray.getJSONObject(i).getString(
                        "finaltotal"), jsonArray.getJSONObject(i).getString(
                        "ReceiptAmt"), jsonArray.getJSONObject(i).getString(
                        "Balanceamt"), jsonArray.getJSONObject(i).getString(
                        "username"), jsonArray.getJSONObject(i).getString(
                        "mobileno"), jsonArray.getJSONObject(i).getString(
                        "addeddt"));
            }

            if (From_date != null && To_date != null) {
                if (tcf.getPendingBalanceCount() > 0) {
                    getDataFromDataBase();
                } else {
                    getDataFromServer();
                }
            } else {

                    Toast.makeText(CustomerwiseReportActivity.this,
                            "select From date and To date", Toast.LENGTH_LONG).show();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        billReportArrayList.clear();
        remaining_amt = 0;
      //  DatabaseHandler db1 = new DatabaseHandler(CustomerwiseReportActivity.this);
      //  SQLiteDatabase db = db1.getWritableDatabase();

     /*   BillId ,FinalTotalBill ,Received , Balance ,CustomerName ,Cust_mob ,date )*/

        Cursor c1 = sql_db.rawQuery("Select distinct " +
                " Cust_mob from "
                + dbhandler.TABLE_PENDING_BALANCE +
                " order by BillId ", null);
        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                remaining_amt = 0;
                String Cust_mob = c1.getString(c1.getColumnIndex("Cust_mob"));

                PendingBillReport billReport = new PendingBillReport();
                billReport.setCust_mob(Cust_mob);

                Cursor c11 = sql_db.rawQuery("Select distinct BillId ,FinalTotalBill ,Received ," +
                        " Balance ,CustomerName ,Cust_mob ,date from "
                        + dbhandler.TABLE_PENDING_BALANCE +
                        " where Cust_mob='" + Cust_mob + "'", null);
                if (c11.getCount() > 0) {
                    c11.moveToFirst();
                    do {

                        String Cust_Name = c11.getString(c11.getColumnIndex("CustomerName"));

                     //   date = get_date(c11.getString(c11.getColumnIndex("date")));
                        String BillId = c11.getString(c11.getColumnIndex("BillId"));
                        String FinalTotalBill = c11.getString(c11.getColumnIndex("FinalTotalBill"));
                        String Received = c11.getString(c11.getColumnIndex("Received"));
                        String Balance = c11.getString(c11.getColumnIndex("Balance"));

                        if (compare_date(From_date, To_date) == true) {

                            remaining_amt = remaining_amt + Float.parseFloat(Balance);

                            billReport.setDate(get_date(c11.getString(c11.getColumnIndex("date"))));
                            billReport.setBalance(Balance);
                            billReport.setBillId(BillId);
                            billReport.setFinalTotalBill(FinalTotalBill);
                            billReport.setReceived(Received);
                            billReport.setCustomerName(Cust_Name);
                            billReport.setTotalPnding(remaining_amt);


                        } else {
                            billReport.setDate(date);
                            billReport.setBalance("0");
                            billReport.setBillId("0");
                            billReport.setFinalTotalBill("0");
                            billReport.setReceived("0");
                            billReport.setCustomerName("0");
                            billReport.setTotalPnding(0);
                        }
                    } while (c11.moveToNext());

                } else {
                    billReport.setDate(date);
                    billReport.setBalance("0");
                    billReport.setBillId("0");
                    billReport.setFinalTotalBill("0");
                    billReport.setReceived("0");
                    billReport.setCustomerName("0");
                    billReport.setTotalPnding(0);
                }
                billReportArrayList.add(billReport);
            } while (c1.moveToNext());

        } else {
            Toast.makeText(CustomerwiseReportActivity.this,
                    "No data for this mobile number", Toast.LENGTH_LONG).show();
        }

        if (billReportArrayList.size() > 0) {
           /* Intent intent = new Intent(CustomerwiseReportActivity.this, CustomerPendingBalanceActivity.class);

            intent.putExtra("remaining_amt", remaining_amt);
            startActivity(intent);*/
            customerReceivableAdapter=new CustomerReceivableAdapter(CustomerwiseReportActivity.this,billReportArrayList);
            gridView.setAdapter(customerReceivableAdapter);

        } else {
            Toast.makeText(CustomerwiseReportActivity.this,
                    "No data for this mobile number", Toast.LENGTH_LONG).show();
        }
    }

    public String get_date(String d) {
        String toDate;
        try {
//8\/12\/2016 3:41:45 PM
            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");//Feb 23 2016 12:16PM

            SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
            String date = d;
            try {
                date1 = formatter.parse(date);
            } catch (Exception e) {
                Log.e("Date", "" + e);
            }
            toDate = toFormat.format(date1);
        } catch (Exception e) {
            toDate = "";
        }
        return toDate;
    }

    public static boolean compare_date(String fromdate, String todate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        date = dfDate.format(new Date());
        try {
            if ((dfDate.parse(date).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(date).equals(dfDate.parse(fromdate))) &&
                    (dfDate.parse(date).before(dfDate.parse(todate)) ||
                            dfDate.parse(date).equals(dfDate.parse(todate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private void setListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerwiseReportActivity.this, CustomerPendingBalanceActivity.class);
                intent.putExtra("Mobile", billReportArrayList.get(position).getCust_mob());
                intent.putExtra("From_date", From_date);
                intent.putExtra("To_date", To_date);

                startActivity(intent);
            }
        });

        buttonFdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerwiseReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                buttonFdate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                From_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                /*if (From_date != null && To_date != null) {
                                    if (dbHandler.getPendingBalanceCount() > 0) {
                                        getDataFromDataBase();
                                    } else {
                                        getDataFromServer();
                                    }
                                } else {
                                    if (From_date == null) {
                                        Toast.makeText(CustomerwiseReportActivity.this,
                                                "select From date", Toast.LENGTH_LONG).show();
                                    } else if (To_date == null) {
                                        Toast.makeText(CustomerwiseReportActivity.this,
                                                "select To date", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(CustomerwiseReportActivity.this,
                                                "select From date and To date", Toast.LENGTH_LONG).show();
                                    }
                                }*/
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        buttonTdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                //  c.add(Calendar.DAY_OF_MONTH, 30);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerwiseReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                buttonTdate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                To_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });

        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (From_date != null && To_date != null) {
                    if (tcf.getPendingBalanceCount() > 0) {
                        getDataFromDataBase();
                    } else {
                        getDataFromServer();
                    }
                } else {
                    if (From_date == null) {
                        Toast.makeText(CustomerwiseReportActivity.this,
                                "select From date", Toast.LENGTH_LONG).show();
                    } else if (To_date == null) {
                        Toast.makeText(CustomerwiseReportActivity.this,
                                "select To date", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerwiseReportActivity.this,
                                "select From date and To date", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tbuds_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                CustomerwiseReportActivity.this.finish();

                break;
            case R.id.refresh:
                getDataFromServer();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
