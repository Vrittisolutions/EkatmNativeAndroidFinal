package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketActivityServiceReport extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;
    private Toolbar toolbar;

    SharedPreferences userpreferences;
    ProgressBar progressBar;
    Button btnOpen, btnClosed, btnOntime;
    TextView txtCustName;
    static String Custmername, CustmerId, SelectedYear;
    SearchableSpinner mSpinnerYear;
    TextView txtjan,
            txtFeb,
            txtMar,
            txtApr,
            txtMay,
            txtJune,
            txtJuly,
            txtAug,
            txtSep,
            txtOct,
            txtNov,
            txtDec;

    String value,month;
    private Intent intent;
    LinearLayout len_jan,len_feb,len_mar,len_apr,len_may,len_june,len_july,len_aug,len_sep,len_oct,len_nov,len_dec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_ticket_report);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_support);
        toolbar.setTitle(R.string.app_name_toolbar_service);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);


        intent = getIntent();
        Custmername = intent.getStringExtra("CustName");
        CustmerId = intent.getStringExtra("CustVenderMasterId");
        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmerrpt);
        txtCustName = (TextView) findViewById(R.id.cust_name);
        btnOpen = (Button) findViewById(R.id.OpenCount);
        btnClosed = (Button) findViewById(R.id.completecount);
        btnOntime = (Button) findViewById(R.id.ontimecount);
        txtjan = (TextView) findViewById(R.id.jancnt);
        txtFeb = (TextView) findViewById(R.id.febcnt);
        txtMar = (TextView) findViewById(R.id.cntmarch);
        txtApr = (TextView) findViewById(R.id.cntapr);
        txtMay = (TextView) findViewById(R.id.cntmay);
        txtJune = (TextView) findViewById(R.id.cntjun);
        txtJuly = (TextView) findViewById(R.id.cntjuly);
        txtAug = (TextView) findViewById(R.id.cntaug);
        txtSep = (TextView) findViewById(R.id.cntspt);
        txtOct = (TextView) findViewById(R.id.cntoct);
        txtNov = (TextView) findViewById(R.id.cntnov);
        txtDec = (TextView) findViewById(R.id.cntdec);


        len_jan = (LinearLayout) findViewById(R.id.len_jan);
        len_feb = (LinearLayout) findViewById(R.id.len_feb);
        len_mar = (LinearLayout) findViewById(R.id.len_mar);
        len_apr = (LinearLayout) findViewById(R.id.len_apr);
        len_may = (LinearLayout) findViewById(R.id.len_may);
        len_june = (LinearLayout) findViewById(R.id.len_june);
        len_july = (LinearLayout) findViewById(R.id.len_july);
        len_aug = (LinearLayout) findViewById(R.id.len_aug);
        len_sep = (LinearLayout) findViewById(R.id.len_sep);
        len_oct = (LinearLayout) findViewById(R.id.len_oct);
        len_nov = (LinearLayout) findViewById(R.id.len_nov);
        len_dec = (LinearLayout) findViewById(R.id.len_dec);

        txtCustName.setText(Custmername);

        mSpinnerYear = (SearchableSpinner) findViewById(R.id.spinner_year);
        String[] countries = getResources().getStringArray(R.array.year);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        mSpinnerYear.setAdapter(dataAdapter);
        mSpinnerYear.setTitle("Select Year");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String currentYear = sdf.format(new Date());
        int pos = 0;
        for (int i = 0; i < countries.length; i++) {
            String dum = countries[i];
            if (countries[i].equalsIgnoreCase(currentYear)) {
                pos = i;
            }
        }
        mSpinnerYear.setSelection(pos);


        if (isTicketCount(CustmerId)) {
            updateTicketCount(CustmerId);
        } else {
            RefreshTicketCount(CustmerId);
        }


        mSpinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedYear = (String) mSpinnerYear.getSelectedItem();


                    RefreshTicketCountMonthwise(CustmerId, SelectedYear);

                // Toast.makeText(getApplicationContext(),""+SelectedYear,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=btnOpen.getText().toString();
                if (value.equals("0")){

                }else {
                    Intent i = new Intent(getApplicationContext(), TicketActivityServiceReportDetail.class);
                    i.putExtra("CustName", Custmername);
                    i.putExtra("CustVenderMasterId", CustmerId);
                    i.putExtra("TypeFlag", "A");
                    startActivity(i);
                }
            }
        });
        btnClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=btnClosed.getText().toString();
                if (value.equals("0")){

                }else {
                    Intent i = new Intent(getApplicationContext(), TicketActivityServiceReportDetail.class);
                    i.putExtra("CustName", Custmername);
                    i.putExtra("CustVenderMasterId", CustmerId);
                    i.putExtra("TypeFlag", "C");
                    startActivity(i);
                }
            }
        });
        btnOntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=btnOntime.getText().toString();
                if (value.equals("0")){

                }else {
                    Intent i = new Intent(getApplicationContext(), TicketActivityServiceReportDetail.class);
                    i.putExtra("CustName", Custmername);
                    i.putExtra("CustVenderMasterId", CustmerId);
                    i.putExtra("TypeFlag", "CL");
                    startActivity(i);
                }
            }
        });
        len_jan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtjan.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Jan");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtFeb.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Feb");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtMar.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Mar");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_apr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtApr.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Apr");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_may.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtMay.getText().toString();
          if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "May");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_june.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtJune.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "June");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        len_july.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtJuly.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "July");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtAug.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Aug");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtSep.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Sep");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtOct.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Oct");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtNov.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Nov");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        len_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value=txtDec.getText().toString();
                if (value.equals("0")){

                }else {
                    intent = new Intent(TicketActivityServiceReport.this, TicketActivityServiceReportDetail.class);
                    intent.putExtra("month", "Dec");
                    intent.putExtra("year", SelectedYear);
                    intent.putExtra("CustVenderMasterId", CustmerId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

    }

    void RefreshTicketCount(final String CustID) {
        if (isnet()) {
            new StartSession(TicketActivityServiceReport.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTicketCount().execute(CustID);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void RefreshTicketCountMonthwise(final String CustID, final String Year1) {
        if (isnet()) {
            new StartSession(TicketActivityServiceReport.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTicketCountMonthwise().execute(CustID, Year1);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    class DownloadTicketCount extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        String custID = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            custID = params[0];
            try {
                url = CompanyURL + WebUrlClass.api_TicketCountForAndroid + "?ShipToMasterId=" + URLEncoder.encode(params[0], "UTF-8");
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (response.equalsIgnoreCase("error")) {
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                try {
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_SUPPORTTICKET_COUNT, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUPPORTTICKET_COUNT, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("ConsigneeName");
                        //    if (CheckifRecordPresent(db.TABLE_TICKET_CUSTOMER_LIST, "CustVendorMasterId", Id)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_SUPPORTTICKET_COUNT, null, values);
                        Log.e("cnt", "" + a);
                        //    }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateTicketCount(custID);
            } else if (response.contains("No Record Found")) {
                //   Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Server Error..", Toast.LENGTH_LONG).show();
            }
            //Please Customize Your Search...
            hideProgress();
        }

    }

    class DownloadTicketCountMonthwise extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        String custID = "";
        String Year = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            custID = params[0];
            Year = params[1];
            try {
                url = CompanyURL + WebUrlClass.api_TicketCountMonthWiseForAndroid + "?ShipToMasterId=" + URLEncoder.encode(params[0], "UTF-8")
                        + "&Year=" + URLEncoder.encode(params[1]);
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //No Record Found...

            if (response.equalsIgnoreCase("error")) {
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            } else if (response.contains("ConsigneeName")) {
                try {
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_TICKET_COUNT_MONTHWISE, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_COUNT_MONTHWISE, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        String Id = jorder.getString("ConsigneeName");
                        //    if (CheckifRecordPresent(db.TABLE_TICKET_CUSTOMER_LIST, "CustVendorMasterId", Id)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("monthYear")) {
                                columnValue = Year;
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_TICKET_COUNT_MONTHWISE, null, values);
                        Log.e("cnt", "" + a);
                        //    }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateTicketCountMonthwise(custID, Year);
            } else if (response.contains("No Record Found")) {
                Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Server Error...", Toast.LENGTH_LONG).show();

            }
            //Please Customize Your Search...
            hideProgress();
        }

    }

    private boolean isTicketCount(String CustId) {

        String query = "SELECT *" +
                "FROM " + db.TABLE_SUPPORTTICKET_COUNT + " WHERE SourceId='" + CustId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    private boolean isTicketCountmonth(String CustId, String Year) {

        String query = "SELECT * FROM " + db.TABLE_TICKET_COUNT_MONTHWISE + " WHERE SourceId='"
                + CustId + "' AND monthYear='" + Year + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        }
        return false;
    }

    private void updateTicketCount(String CustId) {
        String query = "SELECT *" +
                " FROM " + db.TABLE_SUPPORTTICKET_COUNT + " WHERE SourceId='" + CustId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                String Open = cur.getString(cur.getColumnIndex("OpenticketCount"));
                String Closed = cur.getString(cur.getColumnIndex("CompletedTkt"));
                String Ontime = cur.getString(cur.getColumnIndex("ClosedTicketCount"));
                btnOpen.setText(Open + "");
                btnClosed.setText(Closed + "");
                btnOntime.setText(Ontime + "");

            } while (cur.moveToNext());

        } else {
            btnOpen.setText(0 + "");
            btnClosed.setText(0 + "");
            btnOntime.setText(0 + "");
        }
    }

    private void updateTicketCountMonthwise(String CustId, String Year) {
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_COUNT_MONTHWISE + " WHERE SourceId='" + CustId +
                "' AND monthYear='" + Year + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                String ConsingeeName = cur.getString(cur.getColumnIndex("ConsigneeName"));
                String CustID = cur.getString(cur.getColumnIndex("CustomerId"));
                String City = cur.getString(cur.getColumnIndex("City"));
                String SourceID = cur.getString(cur.getColumnIndex("SourceId"));
                String CntJan = cur.getString(cur.getColumnIndex("Jan"));
                String CntFeb = cur.getString(cur.getColumnIndex("Feb"));
                String CntMar = cur.getString(cur.getColumnIndex("Mar"));
                String CntApr = cur.getString(cur.getColumnIndex("Apr"));
                String CntMay = cur.getString(cur.getColumnIndex("May"));
                String CntJune = cur.getString(cur.getColumnIndex("June"));
                String CntJuly = cur.getString(cur.getColumnIndex("July"));
                String CntAug = cur.getString(cur.getColumnIndex("Aug"));
                String CntSep = cur.getString(cur.getColumnIndex("Sep"));
                String CntOct = cur.getString(cur.getColumnIndex("Oct"));
                String CntNov = cur.getString(cur.getColumnIndex("Nov"));
                String CntDec = cur.getString(cur.getColumnIndex("Dec"));
                String yeardata = cur.getString(cur.getColumnIndex("monthYear"));

                txtjan.setText(CntJan + "");
                txtFeb.setText(CntFeb + "");
                txtMar.setText(CntMar + "");
                txtApr.setText(CntApr + "");
                txtMay.setText(CntMay + "");
                txtJune.setText(CntJune + "");
                txtJuly.setText(CntJuly + "");
                txtAug.setText(CntAug + "");
                txtSep.setText(CntSep + "");
                txtOct.setText(CntOct + "");
                txtNov.setText(CntNov + "");
                txtDec.setText(CntDec + "");
            } while (cur.moveToNext());

        } else {
            txtjan.setText(0 + "");
            txtFeb.setText(0 + "");
            txtMar.setText(0 + "");
            txtApr.setText(0 + "");
            txtMay.setText(0 + "");
            txtJune.setText(0 + "");
            txtJuly.setText(0 + "");
            txtAug.setText(0 + "");
            txtSep.setText(0 + "");
            txtOct.setText(0 + "");
            txtNov.setText(0 + "");
            txtDec.setText(0 + "");

        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TicketActivityServiceReport.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        if (id == R.id.refresh) {

            RefreshTicketCount(CustmerId);

            RefreshTicketCountMonthwise(CustmerId, SelectedYear);

            return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
