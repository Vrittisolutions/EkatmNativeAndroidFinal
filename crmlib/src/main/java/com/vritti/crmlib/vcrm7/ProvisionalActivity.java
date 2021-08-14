package com.vritti.crmlib.vcrm7;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ProvisionalData;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

/**
 * Created by sharvari on 26-Sep-17.
 */

public class ProvisionalActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    Intent intent;
    String callId, FKCustomerId, FKConsigneeId, InvoiceNo, date, ProvisionalRecieptjson;
    SharedPreferences userpreferences;

    SQLiteDatabase sql;
    EditText edt_bankname, edt_amount, edt_instrument_no, edt_tds_amount, edt_narration, edt_date, edt_deposite_bank;
    TextView txtfirmname, tvcall, txtcityname, txtactiondatetime, txtopportunity_type, tv_latestremark, txt_Save, txt_Close, txt_invoice_no, txt_amount;
    ImageView img_date;
    int pos, Provi_count;
    SimpleDateFormat dfDate;
    LinearLayout lsCall_list;
    ArrayList<ProvisionalData> provisionalDataArrayList;
    String BankMasterId, CustomerId, CallId, Amount, InstrumentNo, BankName, TDSAmount, Narration, ReceiptStatus, AddedBy, AddedDt, PaymentDepBank, DepositedDt;
    Spinner spinner_bankname;
    List<String> listBanknamedata = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_provisional_receipt_lay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Provisional Receipt");

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();

        init();

        provisionalDataArrayList = new ArrayList<>();

        intent = getIntent();
        callId = intent.getStringExtra("Call_id");
        Provi_count = intent.getIntExtra("procount", 0);

        if (isnet()) {
            new StartSession(ProvisionalActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCustomerIdData().execute(callId);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (Provi_count > 0) {

            if (isnet()) {
                new StartSession(ProvisionalActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadProvisionalListData().execute(callId);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        if (cf.getBankdatacount() > 0) {
            getBankname();
        } else {
            if (isnet()) {
                new StartSession(ProvisionalActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadBanknameData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

    }


    private void init() {

        edt_amount = (EditText) findViewById(R.id.edt_amount);
        edt_instrument_no = (EditText) findViewById(R.id.edt_instrument_no);
        spinner_bankname = (Spinner) findViewById(R.id.spinner_bankname);
        edt_tds_amount = (EditText) findViewById(R.id.edt_tds_amount);
        edt_narration = (EditText) findViewById(R.id.edt_narration);
        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_bankname = (EditText) findViewById(R.id.edt_bankname);

        img_date = (ImageView) findViewById(R.id.img_date);
        txt_Save = (TextView) findViewById(R.id.buttonSave);
        txt_Close = (TextView) findViewById(R.id.buttonClose);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);


        txt_Save.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (view.getTag() instanceof Integer) {
                        pos = (Integer) view.getTag();
                    }

                    String Amount = edt_amount.getText().toString();
                    String Instrument = edt_instrument_no.getText().toString();
                    String Bankname = edt_bankname.getText().toString();
                    String TDS_Amount = edt_tds_amount.getText().toString();
                    String Narration = edt_narration.getText().toString();

                    // String Deposite = edt_deposite_bank.getText().toString();

                    JSONObject jsonprovisionalreciptadd = new JSONObject();
                    dfDate = new SimpleDateFormat("dd/MM/yyyy");
                    date = dfDate.format(new Date());

                    try {
                        jsonprovisionalreciptadd.put("CallId", callId);
                        jsonprovisionalreciptadd.put("InvoiceNumber", InvoiceNo);
                        jsonprovisionalreciptadd.put("Amount", Amount);
                        jsonprovisionalreciptadd.put("InstrumentNo", Instrument);
                        jsonprovisionalreciptadd.put("BankName", Bankname);
                        jsonprovisionalreciptadd.put("TDSAmount", TDS_Amount);
                        jsonprovisionalreciptadd.put("Narration", Narration);
                        jsonprovisionalreciptadd.put("PaymentDepBank", BankMasterId);
                        jsonprovisionalreciptadd.put("DepositedDt", date);
                        jsonprovisionalreciptadd.put("CustId", FKCustomerId);
                        jsonprovisionalreciptadd.put("ReceiptId: ", "");
                        jsonprovisionalreciptadd.put("ConsigneeId", FKConsigneeId);

                        ProvisionalRecieptjson = jsonprovisionalreciptadd.toString();

                        System.out.println("Contact list : " + ProvisionalRecieptjson.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProvisionalRecieptjson = ProvisionalRecieptjson.toString();
                    ProvisionalRecieptjson = ProvisionalRecieptjson.replaceAll("\\\\", "");
                    if (isnet()) {
                        new StartSession(ProvisionalActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostSaveReciptJSON().execute(ProvisionalRecieptjson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
            }
        });
        img_date.setOnClickListener(new View.OnClickListener()

        {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProvisionalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        edt_date.setOnClickListener(new View.OnClickListener()

        {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProvisionalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth));
                                edt_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        txt_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        spinner_bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct BankName,BankMasterId" +
                        " FROM " + db.TABLE_BANKNAME +
                        " WHERE BankName='" + spinner_bankname.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        BankMasterId = cur.getString(cur.getColumnIndex("BankMasterId"));

                    } while (cur.moveToNext());

                } else {
                    BankMasterId = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class DownloadCustomerIdData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_getCustCon + "?CallId=" +
                        URLEncoder.encode(callId, "UTF-8");

                System.out.println("Provisional List Data :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    //JSONArray jResults = new JSONArray(response);

                    JSONObject jsoncustomer_id = null;
                    try {
                        jsoncustomer_id = new JSONObject(response);
                        FKCustomerId = jsoncustomer_id.getString("FKCustomerId");

                        FKConsigneeId = jsoncustomer_id.getString("FKConsigneeId");
                        InvoiceNo = jsoncustomer_id.getString("InvoiceNo");


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            progressDialog.dismiss();


        }
    }

    class DownloadProvisionalListData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_GetLoadProvisionalList + "?CallId=" +
                        URLEncoder.encode(callId, "UTF-8");

                System.out.println("Provisional CallList :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_PROVISINALLIST, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROVISINALLIST, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_PROVISINALLIST, null, values);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            UpdateProvisionalListdata();
            progressDialog.dismiss();


        }

    }

    private void UpdateProvisionalListdata() {
        provisionalDataArrayList.clear();
        String query = "SELECT  InstrumentNo,BankName," +
                "DepositedDate,Amount,DepositedBank,Narration,TDSAmount" +
                " FROM " + db.TABLE_PROVISINALLIST + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ProvisionalData provisionalData = new ProvisionalData();

                provisionalData.setInstrumentNo(cur.getString(cur.getColumnIndex("InstrumentNo")));
                provisionalData.setBankName(cur.getString(cur.getColumnIndex("BankName")));
                provisionalData.setDepositedDt(cur.getString(cur.getColumnIndex("DepositedDate")));
                provisionalData.setAmount(cur.getString(cur.getColumnIndex("Amount")));
                provisionalData.setPaymentDepBank(cur.getString(cur.getColumnIndex("DepositedBank")));
                provisionalData.setNarration(cur.getString(cur.getColumnIndex("Narration")));
                provisionalData.setTDSAmount(cur.getString(cur.getColumnIndex("TDSAmount")));
                provisionalDataArrayList.add(provisionalData);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (provisionalDataArrayList.size() > 0) {
                for (int i = 0; i < provisionalDataArrayList.size(); i++) {
                    addView_Provisional(i);
                }
            }
        }

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

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((edt_amount.getText().toString().equalsIgnoreCase("") ||
                edt_amount.getText().toString().equalsIgnoreCase(" ") ||
                edt_amount.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(ProvisionalActivity.this, "Amount is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_instrument_no.getText().toString().equalsIgnoreCase("") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(" ") ||
                edt_instrument_no.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(ProvisionalActivity.this, "Instrument No is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(ProvisionalActivity.this, "Bank name is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(" ") ||
                edt_date.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(ProvisionalActivity.this, "Date is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(" ") ||
                edt_date.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(ProvisionalActivity.this, "Date required", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    class PostSaveReciptJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveCollectionReceipt;
            System.out.println("BusinessAPIURL-1 :" + ProvisionalRecieptjson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();


            Toast.makeText(ProvisionalActivity.this, "Receipt save successfully", Toast.LENGTH_LONG).show();
            onBackPressed();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void addView_Provisional(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) ProvisionalActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_provisional_list, null);

        TextView txt_provisional_name = (TextView) convertView.findViewById(R.id.txt_provisional_name);
        String outputDateStr = null;

        InvoiceNo = provisionalDataArrayList.get(i).getInvoiceNo();
        BankName = provisionalDataArrayList.get(i).getBankName();
        Amount = provisionalDataArrayList.get(i).getAmount();
        PaymentDepBank = provisionalDataArrayList.get(i).getPaymentDepBank();
        Narration = provisionalDataArrayList.get(i).getNarration();
        TDSAmount = provisionalDataArrayList.get(i).getTDSAmount();
        InstrumentNo = provisionalDataArrayList.get(i).getInstrumentNo();

        String amount = Amount.split("\\.", 2)[0];
        String tdsamount = TDSAmount.split("\\.", 2)[0];


        DepositedDt = provisionalDataArrayList.get(i).getDepositedDt();


        if (TDSAmount.equals("0.0")) {
            txt_provisional_name.setText("Instrument No : " + InstrumentNo + " drawn on " + BankName + " bank dated " + DepositedDt + " for amount " + amount + " is deposited in " + PaymentDepBank + " bank." + Narration);

        } else {
            txt_provisional_name.setText("Instrument No : " + InstrumentNo + " drawn on " + BankName + " bank dated " + DepositedDt + " for amount " + amount + " and TDS " + tdsamount + " is deposited in " + PaymentDepBank + " bank." + Narration);
        }


        lsCall_list.addView(convertView);
    }

    class DownloadBanknameData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetBankname;

            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_BANKNAME, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BANKNAME, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_BANKNAME, null, values);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressDialog.dismiss();

            if (response.contains("")) {

            }
            getBankname();
        }

    }

    private void getBankname() {
        listBanknamedata.clear();
        String query = "SELECT distinct BankMasterId,BankName" +
                " FROM " + db.TABLE_BANKNAME;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                listBanknamedata.add(cur.getString(cur.getColumnIndex("BankName")));

            } while (cur.moveToNext());

        }

        MySpinnerAdapter customDept = new MySpinnerAdapter(ProvisionalActivity.this,
                R.layout.crm_custom_spinner_txt, listBanknamedata);
        spinner_bankname.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_bankname.setSelection(0);
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }

}
