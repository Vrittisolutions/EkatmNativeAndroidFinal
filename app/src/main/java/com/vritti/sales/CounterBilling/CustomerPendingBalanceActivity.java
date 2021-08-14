package com.vritti.sales.CounterBilling;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.PendingBillReport;
import com.vritti.sales.beans.Tbuds_commonFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharvari on 8/2/2016.
 */

public class CustomerPendingBalanceActivity extends AppCompatActivity {
    LinearLayout linearLayout_pending;
    ArrayList<PendingBillReport> billReportArrayList;
    String mob;
    String From_date = null, To_date = null, userid;
    public static String date;
    float remaining_amt;
    TextView CustomerName, MobileNumber, Date, Remainingamt, BillNo, textview_total_pending_amount;
    TextView PaidAmt, TotalAmt;

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
        setContentView(R.layout.tbuds_activity_customer_pending_balance);

        /*getSupportActionBar().setTitle("Customer Receivable Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Customer Receivable Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        Intent intent = getIntent();
        mob = intent.getStringExtra("Mobile");
        From_date = intent.getStringExtra("From_date");
        To_date = intent.getStringExtra("To_date");

        billReportArrayList=new ArrayList<PendingBillReport>();
        linearLayout_pending = (LinearLayout) findViewById(R.id.linearLayout_pending);
        textview_total_pending_amount = (TextView) findViewById(R.id.textview_total_pending_amount);
        MobileNumber = (TextView) findViewById(R.id.MobileNumber);
        MobileNumber.setText(mob + "");

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CustomerPendingBalanceActivity.this);
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

        getDataFromDataBase();
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        billReportArrayList.clear();
        remaining_amt = 0;
    //    DatabaseHandler db1 = new DatabaseHandler(CustomerPendingBalanceActivity.this);
    //    SQLiteDatabase db = db1.getWritableDatabase();

        remaining_amt = 0;

        Cursor c11 = sql_db.rawQuery("Select distinct BillId ,FinalTotalBill ,Received ," +
                " Balance ,CustomerName ,Cust_mob ,date from "
                + dbhandler.TABLE_PENDING_BALANCE +
                " where Cust_mob='" + mob + "'", null);
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
                    PendingBillReport billReport = new PendingBillReport();
                    billReport.setCust_mob(mob);
                    billReport.setDate(get_date(c11.getString(c11.getColumnIndex("date"))));
                    billReport.setBalance(Balance);
                    billReport.setBillId(BillId);
                    billReport.setFinalTotalBill(FinalTotalBill);
                    billReport.setReceived(Received);
                    billReport.setCustomerName(Cust_Name);
                    billReport.setTotalPnding(remaining_amt);
                    billReportArrayList.add(billReport);

                } else {
                    PendingBillReport billReport = new PendingBillReport();
                    billReport.setCust_mob(mob);
                    billReport.setDate(date);
                    billReport.setBalance("0");
                    billReport.setBillId("0");
                    billReport.setFinalTotalBill("0");
                    billReport.setReceived("0");
                    billReport.setCustomerName("0");
                    billReport.setTotalPnding(0);
                    billReportArrayList.add(billReport);
                }
            } while (c11.moveToNext());

        } else {
            PendingBillReport billReport = new PendingBillReport();
            billReport.setCust_mob(mob);
            billReport.setDate(date);
            billReport.setBalance("0");
            billReport.setBillId("0");
            billReport.setFinalTotalBill("0");
            billReport.setReceived("0");
            billReport.setCustomerName("0");
            billReport.setTotalPnding(0);
            billReportArrayList.add(billReport);
        }

        if (billReportArrayList.size() > 0) {
            linearLayout_pending.removeAllViews();
            for (int i = 0; i < billReportArrayList.size(); i++) {
                addView(i);
            }
            textview_total_pending_amount.setText(remaining_amt + "");

        } else {
            Toast.makeText(CustomerPendingBalanceActivity.this,
                    "No data for this mobile number", Toast.LENGTH_LONG).show();
        }
    }

    public String get_date(String d) {
        String toDate;
        try {
//8\/12\/2016 3:41:45 PM
            java.util.Date date1 = new Date();
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

    private void addView(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) CustomerPendingBalanceActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_pending_balance,
                null);

        PaidAmt = (TextView) baseView.findViewById(R.id.PaidAmt);
        TotalAmt = (TextView) baseView.findViewById(R.id.TotalAmt);
        Date = (TextView) baseView.findViewById(R.id.Date);
        Remainingamt = (TextView) baseView.findViewById(R.id.Remainingamt);
        BillNo = (TextView) baseView.findViewById(R.id.BillNo);

        PaidAmt.setText(billReportArrayList.get(i).getReceived() + "");
        TotalAmt.setText(billReportArrayList.get(i).getFinalTotalBill() + "");
        Date.setText(billReportArrayList.get(i).getDate() + "");
        Remainingamt.setText(billReportArrayList.get(i).getBalance() + "");
        BillNo.setText(billReportArrayList.get(i).getBillId() + "");
        linearLayout_pending.addView(baseView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                CustomerPendingBalanceActivity.this.finish();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
