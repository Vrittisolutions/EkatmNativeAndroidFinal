package com.vritti.sales.CounterBilling;

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
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.Reports;
import com.vritti.sales.beans.Reports_SP;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
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
import java.util.Date;

/**
 * Created by sharvari on 8/16/2016.
 */
public class TotalSaleActivity extends AppCompatActivity {
    LinearLayout linearLayoutCordinatorDailysale;
    String itemdata, fromdate, todate;
    Reports reports;
    ArrayList<Reports> reportsArrayList;
    String item_list[];
    String userId, restoredText, json;
    public static String Saledate, S_date;
    SharedPreferences sharedpreferencesUserId;
    ArrayList<Reports_SP> reports_spArrayList;
    Connectiondetector cd;
    TextView txtItem, txtpurchasecost, txtsalecost, txtbroughtqnty, txtsoldqnty, txtprofit, txt_remaining_stock;

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
        setContentView(R.layout.tbuds_activity_dailysale);

        /*getSupportActionBar().setTitle("Total Transaction Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        Intent intent = getIntent();
        itemdata = intent.getStringExtra("Items");
        fromdate = intent.getStringExtra("FromDate");//11-08-2016
        todate = intent.getStringExtra("ToDate");//12-08-2016
        init();
        cd = new Connectiondetector(getApplicationContext());

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(TotalSaleActivity.this);
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

       // sharedpreferencesUserId = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);

        userId = sharedpreferencesUserId.getString("userid", null);
        restoredText = sharedpreferencesUserId.getString("Mobileno", null);
        if (tcf.getReportsCount() > 0) {
            if (itemdata.equalsIgnoreCase("All Items")) {
                reportsArrayList.clear();
                if (tcf.getReportsCount() > 0) {
                    getDataFromDataBase("All Items");
                } else {
                    Toast.makeText(TotalSaleActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (tcf.getReportsCount() > 0) {
                    reportsArrayList.clear();
                    item_list = itemdata.split(",");
                    for (int i = 0; i < item_list.length; i++) {
                        getDataFromDataBase(item_list[i]);
                    }
                } else {
                    Toast.makeText(TotalSaleActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            tcf.deleteReports();
            getDataFromServer();
        }


        //deleteReports()
        //      getReportsCount()
//TABLE_GET_REPORTS
        //addReports
//2016-07-27 17:14:07.000
    }

    private void init() {
        linearLayoutCordinatorDailysale = (LinearLayout) findViewById(R.id.linearLayoutCordinatorDailysale);
     //   databaseHandler = new DatabaseHandler(TotalSaleActivity.this);
        reportsArrayList = new ArrayList<Reports>();
        reports_spArrayList = new ArrayList<Reports_SP>();
    }

    private void getDataFromDataBase(String items) {
        // TODO Auto-generated method stub

        String Item, itemid, P_date;
        float Purchasecost = 0, sallingcost = 0, broughtqty = 0, soldqty = 0, profit = 0, remainingstock = 0;

    //    SQLiteDatabase db = databaseHandler.getWritableDatabase();

        if (items.equalsIgnoreCase("All Items")) {
            Cursor c = sql_db.rawQuery("Select distinct " +
                    "purchaseitemName,purchasemrp ,purchaseitemid, purchaseqty" +
                    " ,purchaseShopname,purchaseVendorid,purchaseAddeddt " +
                    "from  getReports ", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    Purchasecost = 0;
                    sallingcost = 0;
                    broughtqty = 0;
                    soldqty = 0;
                    P_date = get_date(c.getString(c.getColumnIndex("purchaseAddeddt")));
                    Item = c.getString(c.getColumnIndex("purchaseitemName"));
                    itemid = c.getString(c.getColumnIndex("purchaseitemid"));
                    reports = new Reports();

                    Cursor c1 = sql_db.rawQuery("Select distinct " +
                            "purchasemrp , purchaseqty ,purchaseitemid " +
                            " from  getReports where purchaseitemid='" + itemid + "'", null);
                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        do {
                            Purchasecost = Purchasecost + Float.parseFloat(c1.getString(c1.getColumnIndex("purchasemrp")))
                                    * Float.parseFloat(c1.getString(c1.getColumnIndex("purchaseqty")));
                            broughtqty = broughtqty + Float.parseFloat(c1.getString(c1.getColumnIndex("purchaseqty")));

                            Cursor c2 = sql_db.rawQuery("Select distinct  salesheaderid , " +
                                    " salestotal , salesdiscounttotal, salesfinaltotal, salesreceiptamt " +
                                    ",salesbalanceamt " +
                                    ",salesaddeddt , salesvendorid ,salesitemname " +
                                    ", salesqty , salesunit , salesrate ,salesdisc,salesamount," +
                                    "salesitemid,salesaddeddt" +
                                    " from getReports  where salesitemid='" + itemid + "'", null);
                            if (c2.getCount() > 0) {
                                c2.moveToFirst();
                                do {
                                    reports.setItem(c2.getString(c2.getColumnIndex("salesitemname")));
                                    sallingcost = sallingcost + Float.parseFloat(c2.getString(c2.getColumnIndex("salesrate")))
                                            *Float.parseFloat(c2.getString(c2.getColumnIndex("salesqty")));
                                    soldqty = soldqty + Float.parseFloat(c2.getString(c2.getColumnIndex("salesqty")));
                                    S_date = get_date(c2.getString(c2.getColumnIndex("salesaddeddt")));
                                } while (c2.moveToNext());
                            }


                        } while (c1.moveToNext());
                    }
                    if (compare_date(fromdate, todate, S_date) == true) {
                        reports.setPurchasecost(Purchasecost);
                        reports.setSallingcost(sallingcost);
                        reports.setBroughtqty(broughtqty);
                        reports.setSoldqty(soldqty);
                        reports.setProfit(sallingcost - Purchasecost);
                        reports.setRemainingstock(broughtqty - soldqty);
                        reportsArrayList.add(reports);
                    }
                    else {
                        sallingcost = 0;
                        soldqty = 0;

                        reports.setItem(Item);
                        reports.setPurchasecost(Purchasecost);
                        reports.setSallingcost(sallingcost);
                        reports.setBroughtqty(broughtqty);
                        reports.setSoldqty(soldqty);
                        reports.setProfit(sallingcost - Purchasecost);
                        reports.setRemainingstock(broughtqty - soldqty);
                        reportsArrayList.add(reports);
                    }
                } while (c.moveToNext());
            }
        } else {
            Cursor c = sql_db.rawQuery("Select distinct " +
                    "purchaseitemName,purchasemrp ,purchaseitemid, purchaseqty" +
                    " ,purchaseShopname,purchaseVendorid ,purchaseAddeddt " +
                    "from  getReports where purchaseitemid='" + items + "'", null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    Purchasecost = 0;
                    sallingcost = 0;
                    broughtqty = 0;
                    soldqty = 0;
                    P_date = get_date(c.getString(c.getColumnIndex("purchaseAddeddt")));
                    Item = c.getString(c.getColumnIndex("purchaseitemName"));
                    itemid = c.getString(c.getColumnIndex("purchaseitemid"));
                    reports = new Reports();

                    Cursor c1 = sql_db.rawQuery("Select distinct " +
                            "purchasemrp , purchaseqty " +
                            "," +
                            " purchaseitemid " +
                            " from  getReports where purchaseitemid='" + itemid + "'", null);
                    if (c1.getCount() > 0) {
                        c1.moveToFirst();
                        do {
                            Purchasecost = Purchasecost + Float.parseFloat(c1.getString(c1.getColumnIndex("purchasemrp")))
                                    * Float.parseFloat(c1.getString(c1.getColumnIndex("purchaseqty")));
                            broughtqty = broughtqty + Float.parseFloat(c1.getString(c1.getColumnIndex("purchaseqty")));

                            Cursor c2 = sql_db.rawQuery("Select distinct  salesheaderid , " +
                                    " salestotal , salesdiscounttotal, salesfinaltotal, salesreceiptamt" +
                                    " ,salesbalanceamt,salesaddeddt " +
                                    ",salesaddeddt , salesvendorid ,salesitemname " +
                                    ", salesqty , salesunit , salesrate ,salesdisc,salesamount,salesitemid" +
                                    " from getReports  where salesitemid='" + itemid + "'", null);
                            if (c2.getCount() > 0) {
                                c2.moveToFirst();
                                do {
                                    reports.setItem(c2.getString(c2.getColumnIndex("salesitemname")));
                                    sallingcost = sallingcost + Float.parseFloat(c2.getString(c2.getColumnIndex("salesrate")))
                                            *Float.parseFloat(c2.getString(c2.getColumnIndex("salesqty")));
                                    soldqty = soldqty + Float.parseFloat(c2.getString(c2.getColumnIndex("salesqty")));
                                    S_date = get_date(c2.getString(c2.getColumnIndex("salesaddeddt")));
                                } while (c2.moveToNext());
                            }


                        } while (c1.moveToNext());
                    }
                    if (compare_date(fromdate, todate, S_date) == true) {
                        reports.setPurchasecost(Purchasecost);
                        reports.setSallingcost(sallingcost);
                        reports.setBroughtqty(broughtqty);
                        reports.setSoldqty(soldqty);
                        reports.setProfit(sallingcost - Purchasecost);
                        reports.setRemainingstock(broughtqty - soldqty);
                        reportsArrayList.add(reports);
                    }
                    else {
                        sallingcost = 0;
                        soldqty = 0;

                        reports.setItem(Item);
                        reports.setPurchasecost(Purchasecost);
                        reports.setSallingcost(sallingcost);
                        reports.setBroughtqty(broughtqty);
                        reports.setSoldqty(soldqty);
                        reports.setProfit(sallingcost - Purchasecost);
                        reports.setRemainingstock(broughtqty - soldqty);
                        reportsArrayList.add(reports);
                    }
                } while (c.moveToNext());
            }
        }
        linearLayoutCordinatorDailysale.removeAllViews();
        for (int i = 0; i < reportsArrayList.size(); i++) {
            addView(i);
        }

    }

    private void addView(int i) {
        final int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) TotalSaleActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_dailysale,
                null);
        txtItem = (TextView) baseView.findViewById(R.id.txtItem);
        txtpurchasecost = (TextView) baseView.findViewById(R.id.txtpurchasecose);
        txtsalecost = (TextView) baseView.findViewById(R.id.txtsalecose);
        txtbroughtqnty = (TextView) baseView.findViewById(R.id.txtbroughtqnty);
        txtsoldqnty = (TextView) baseView.findViewById(R.id.txtsoldqnty);
        txtprofit = (TextView) baseView.findViewById(R.id.txtprofit);
        txt_remaining_stock = (TextView) baseView.findViewById(R.id.txt_remaining_stock);

        txtItem.setText(reportsArrayList.get(i).getItem());
        txtpurchasecost.setText(reportsArrayList.get(i).getPurchasecost() + "");
        txtsalecost.setText(reportsArrayList.get(i).getSallingcost() + "");
        txtbroughtqnty.setText(reportsArrayList.get(i).getBroughtqty() + "");
        txtsoldqnty.setText(reportsArrayList.get(i).getSoldqty() + "");
        txtprofit.setText(reportsArrayList.get(i).getProfit() + "");
        txt_remaining_stock.setText(reportsArrayList.get(i).getRemainingstock() + "");

        linearLayoutCordinatorDailysale.addView(baseView);
    }

    private void getDataFromServer() {

        if (cd.isConnectingToInternet()) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetReportData().execute();
            } else {
                new StartSession_tbuds(TotalSaleActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetReportData().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet..", Toast.LENGTH_LONG).show();
        }
    }

    public String get_date(String d) {
        String toDate;
        try {
//8\/12\/2016 3:36:42 PM
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

    public static boolean compare_date(String fromdate, String todate, String dt) {
        boolean b = false;
        //12-08-2016
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        Saledate = dt;
        try {
            if ((dfDate.parse(Saledate).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(Saledate).equals(dfDate.parse(fromdate))) &&
                    (dfDate.parse(Saledate).before(dfDate.parse(todate)) ||
                            dfDate.parse(Saledate).equals(dfDate.parse(todate)))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    class GetReportData extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_GetReportData = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressBar();
            //  showProgressDialog();
        }

       @Override
       protected Void doInBackground(Void... params) {
           String url_GetReport = AnyMartData.MAIN_URL + AnyMartData.METHOD_REPORTS +
                   "?handler="+ AnyMartData.HANDLE +
                   "&sessionid="+ AnyMartData.SESSION_ID +
                   "&vendorid="+userId;

           URLConnection urlConnection = null;
           BufferedReader bufferedReader = null;

           try {
               URL urlGetReport = new URL(url_GetReport);
               urlConnection = urlGetReport.openConnection();
               urlConnection.setRequestProperty("Content-Type", "application/json");
               urlConnection.setRequestProperty("Accept", "JSON");
               bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

               StringBuffer stringBuff_GetReport = new StringBuffer();
               String line;
               StringBuilder str_build_GetReport = new StringBuilder();
               while((line = bufferedReader.readLine())!=null){
                   stringBuff_GetReport = stringBuff_GetReport.append(line);
               }
               responseString = stringBuff_GetReport.toString().replaceAll("^\"|\"$", "");
               resp_GetReportData = responseString.replaceAll("\\\\","");

               Log.e("Response", resp_GetReportData);

           } catch (Exception e) {
               resp_GetReportData = "error";
               e.printStackTrace();
           }
           return null;
       }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dismissProgressDialog();
            if (!resp_GetReportData.equalsIgnoreCase("error")) {
                json = resp_GetReportData;
                parseJson(json);
            }
        }
    }

    protected void parseJson(String json) {

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                if (userId.equalsIgnoreCase(jsonArray.getJSONObject(i).getString(
                        "salesvendorid")) && userId.equalsIgnoreCase(jsonArray.getJSONObject(i).getString(
                        "purchaseVendorid"))) {

                    Reports_SP reports_sp = new Reports_SP();
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "customermobno"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "customerusername"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseAddeddt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseShopname"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseVendorid"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseamt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseitemName"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseitemid"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchasemrp"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseqty"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchasetotamt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "purchaseunit"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesaddeddt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesamount"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesbalanceamt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesdisc"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesdiscounttotal"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesfinaltotal"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesheaderid"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesitemname"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesqty"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesrate"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesreceiptamt"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salestotal"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesunit"));
                    reports_sp.setCustomermobno(jsonArray.getJSONObject(i).getString(
                            "salesvendorid"));
                    reports_spArrayList.add(reports_sp);

                    tcf.addReports(jsonArray.getJSONObject(i).getString(
                                    "salesheaderid"),
                            jsonArray.getJSONObject(i).getString(
                                    "customermobno"), jsonArray.getJSONObject(i).getString(
                                    "customerusername"),
                            jsonArray.getJSONObject(i).getString(
                                    "salestotal"), jsonArray.getJSONObject(i).getString(
                                    "salesdiscounttotal"),
                            jsonArray.getJSONObject(i).getString(
                                    "salesfinaltotal"),
                            jsonArray.getJSONObject(i).getString(
                                    "salesreceiptamt"), jsonArray.getJSONObject(i).getString(
                                    "salesbalanceamt"),
                            jsonArray.getJSONObject(i).getString(
                                    "salesaddeddt"), jsonArray.getJSONObject(i).getString(
                                    "salesvendorid"),
                            jsonArray.getJSONObject(i).getString(
                                    "purchaseitemName"), jsonArray.getJSONObject(i).getString(
                                    "purchasemrp"), jsonArray.getJSONObject(i).getString(
                                    "purchaseqty"), jsonArray.getJSONObject(i).getString(
                                    "purchaseunit"),
                            jsonArray.getJSONObject(i).getString(
                                    "purchaseamt"), jsonArray.getJSONObject(i).getString(
                                    "purchaseVendorid"),
                            jsonArray.getJSONObject(i).getString(
                                    "purchaseShopname"), jsonArray.getJSONObject(i).getString(
                                    "purchaseAddeddt"), jsonArray.getJSONObject(i).getString(
                                    "purchasetotamt"), jsonArray.getJSONObject(i).getString(
                                    "purchaseitemid"),
                            jsonArray.getJSONObject(i).getString(
                                    "salesitemname"), jsonArray.getJSONObject(i).getString(
                                    "salesqty"), jsonArray.getJSONObject(i).getString(
                                    "salesunit"),
                            jsonArray.getJSONObject(i).getString(
                                    "salesrate"), jsonArray.getJSONObject(i).getString(
                                    "salesdisc"), jsonArray.getJSONObject(i).getString(
                                    "salesamount"), jsonArray.getJSONObject(i).getString(
                                    "salesitemid"));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (tcf.getReportsCount() > 0) {
            if (itemdata.equalsIgnoreCase("All Items")) {
                reportsArrayList.clear();
                if (tcf.getReportsCount() > 0) {
                    getDataFromDataBase("All Items");
                } else {
                    Toast.makeText(TotalSaleActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (tcf.getReportsCount() > 0) {
                    reportsArrayList.clear();
                    item_list = itemdata.split(",");
                    for (int i = 0; i < item_list.length; i++) {
                        getDataFromDataBase(item_list[i]);
                    }
                } else {
                    Toast.makeText(TotalSaleActivity.this, "No data", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
