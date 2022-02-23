package com.vritti.crm.vcrm7;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

/**
 * Created by sharvari on 22-Sep-17.
 */

public class AdvanceProvisionalActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    AutoCompleteTextView spinner_customer, spinner_consignee;
    EditText edt_amount, edt_instrument_no, edt_bankname, edt_tds_amount, edt_narration, edt_date;
    TextView txt_Save, txt_Close;
    SQLiteDatabase sql;
    String BankMasterId, date, Amount, Consignee, instrument_no, bank_name, tds_amount, narration, AdvanceProvisionalRecieptjson, CustVendorMasterId, ShipToMasterId;
    SharedPreferences userpreferences;
    List<String> lstCustomer = new ArrayList<String>();
    List<String> lstConsignee = new ArrayList<String>();
    AutoCompleteTextView spinner_bankname;
    List<String> listBanknamedata = new ArrayList<String>();
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    private boolean isCustomer=false,isBank=false;
    private String CustomerName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_advance_provisional_receipt_lay_v1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        init();


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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

       /* if (cf.getCustomercount() > 0) {
            getcustomer();
        } else {
            if (isnet()) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCustomerJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }*/


        if (cf.getBankdatacount() > 0) {
            getBankname();
        } else {
            if (isnet()) {
                new StartSession(AdvanceProvisionalActivity.this, new CallbackInterface() {
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
        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));

        txt_title.setText("Provisional Receipt");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinner_customer = (AutoCompleteTextView) findViewById(R.id.spinner_customer);
        txt_Save = (TextView) findViewById(R.id.txt_Save);
     //   txt_Close = (TextView) findViewById(R.id.txt_Close);
        spinner_consignee = (AutoCompleteTextView) findViewById(R.id.spinner_consignee);
        edt_instrument_no = (EditText) findViewById(R.id.edt_instrument_no);
        edt_bankname = (EditText) findViewById(R.id.edt_bankname);
        edt_tds_amount = (EditText) findViewById(R.id.edt_tds_amount);
        edt_narration = (EditText) findViewById(R.id.edt_narration);
        edt_amount = (EditText) findViewById(R.id.edt_amount);
        edt_date = (EditText) findViewById(R.id.edt_date);
        spinner_bankname = (AutoCompleteTextView) findViewById(R.id.spinner_bankname);


       // spinner_consignee.setHint("Select Consignee Name");
       // spinner_customer.setHint("Select Customer Name");


        spinner_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvanceProvisionalActivity.this,
                        CountryListActivity.class);
                isCustomer=true;
                isBank=false;

                String url = CompanyURL + WebUrlClass.api_GetFillEntityCustomer;
                intent.putExtra("Table_Name", db.TABLE_CUSTOMER);
                intent.putExtra("Id", "CustVendorMasterId");
                intent.putExtra("DispName", "CustVendorName");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });

       /* spinner_bankname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdvanceProvisionalActivity.this,
                        CountryListActivity.class);
                isCustomer=false;
                isBank=true;

                String url = CompanyURL + WebUrlClass.api_GetBankname;
                intent.putExtra("Table_Name", db.TABLE_BANKNAME);
                intent.putExtra("Id", "BankMasterId");
                intent.putExtra("DispName", "BankName");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });
*/


        img_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (validate()) {

                   instrument_no = edt_instrument_no.getText().toString();
                   bank_name = edt_bankname.getText().toString();
                   tds_amount = edt_tds_amount.getText().toString();
                   narration = edt_narration.getText().toString();
                   Amount = edt_amount.getText().toString();
                   JSONObject jsonprovisionalreciptadd = new JSONObject();

                   SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
                   date = dfDate.format(new Date());

                   try {
                       jsonprovisionalreciptadd.put("CallId", "");
                       jsonprovisionalreciptadd.put("InvoiceNumber", "");
                       jsonprovisionalreciptadd.put("Amount", Amount);
                       jsonprovisionalreciptadd.put("InstrumentNo", instrument_no);
                       jsonprovisionalreciptadd.put("BankName", bank_name);
                       jsonprovisionalreciptadd.put("TDSAmount", tds_amount);
                       jsonprovisionalreciptadd.put("Narration", narration);
                       jsonprovisionalreciptadd.put("PaymentDepBank", BankMasterId);
                       jsonprovisionalreciptadd.put("DepositedDt", date);
                       jsonprovisionalreciptadd.put("CustId", CustVendorMasterId);
                       jsonprovisionalreciptadd.put("ReceiptId", "");
                       jsonprovisionalreciptadd.put("ConsigneeId", ShipToMasterId);

                       AdvanceProvisionalRecieptjson = jsonprovisionalreciptadd.toString();

                       System.out.println("AdvanceProvisional list : " + AdvanceProvisionalRecieptjson.toString());


                   } catch (Exception e) {
                       e.printStackTrace();
                   }


                   AdvanceProvisionalRecieptjson = AdvanceProvisionalRecieptjson.toString();
                   AdvanceProvisionalRecieptjson = AdvanceProvisionalRecieptjson.replaceAll("\\\\", "");
                   if (isnet()) {
                       new StartSession(AdvanceProvisionalActivity.this, new CallbackInterface() {
                           @Override
                           public void callMethod() {
                               new PostSaveReciptJSON().execute(AdvanceProvisionalRecieptjson);
                           }

                           @Override
                           public void callfailMethod(String msg) {

                           }
                       });
                   }


               }
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdvanceProvisionalActivity.this,
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



        spinner_bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct BankName,BankMasterId" +
                        " FROM " + db.TABLE_BANKNAME +
                        " WHERE BankName='" + spinner_bankname.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                int cnt = cur.getCount();

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





        /*spinner_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct CustVendorName,CustVendorMasterId" +
                        " FROM " + db.TABLE_CUSTOMER +
                        " WHERE CustVendorName='" + spinner_customer.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                int cnt = cur.getCount();

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        CustVendorMasterId = cur.getString(cur.getColumnIndex("CustVendorMasterId"));
                        System.out.println("CustMaster :" + CustVendorMasterId);

                    } while (cur.moveToNext());

                } else {
                    CustVendorMasterId = "";
                    Toast.makeText(AdvanceProvisionalActivity.this,"Unable to get data ..Please check internet connection",Toast.LENGTH_SHORT).show();
                }

                if (isnet()) {
                    new StartSession(AdvanceProvisionalActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadConsigneeJSON().execute(CustVendorMasterId);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }else{
                    Toast.makeText(AdvanceProvisionalActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        /*spinner_consignee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct ConsigneeName,ShipToMasterId" +
                        " FROM " + db.TABLE_Consignee +
                        " WHERE ConsigneeName='" + spinner_customer.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        ShipToMasterId = cur.getString(cur.getColumnIndex("ShipToMasterId"));

                    } while (cur.moveToNext());

                } else {
                    ShipToMasterId = "";
                    Toast.makeText(AdvanceProvisionalActivity.this,"No consignee name present" +
                            " to respective customer name",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
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

    class DownloadCustomerJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvanceProvisionalActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_GetFillEntityCustomer;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_CUSTOMER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CUSTOMER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_CUSTOMER, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response != null) {
                progressDialog.dismiss();
                getcustomer();
            }


        }

    }

    private void getcustomer() {

        lstCustomer.clear();
        String query = "SELECT distinct CustVendorMasterId,CustVendorName" +
                " FROM " + db.TABLE_CUSTOMER;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstCustomer.add(cur.getString(cur.getColumnIndex("CustVendorName")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstCustomer, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(AdvanceProvisionalActivity.this,
                R.layout.crm_custom_spinner_txt, lstCustomer);
        spinner_customer.setAdapter(customDept);
        Collections.sort(lstCustomer, String.CASE_INSENSITIVE_ORDER);

    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
       /* Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "font/BOOKOS.TTF");
*/
        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //   view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //   view.setTypeface(font);
            return view;
        }
    }

    class PostSaveReciptJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AdvanceProvisionalActivity.this);
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
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + AdvanceProvisionalRecieptjson);

            try {
                res = ut.OpenPostConnection(url, params[0],AdvanceProvisionalActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                System.out.println("AdvanceProvisionalRecieptjson-1 :" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            System.out.println("AdvanceProvisionalRecieptjson-1 :" + integer);

            if (integer.equals("Success")) {
                Toast.makeText(AdvanceProvisionalActivity.this, "Receipt save successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AdvanceProvisionalActivity.this, AdvanceProvisionalReceiptDisplayActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.provisional_receipt, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_menu);

        return super.onPrepareOptionsMenu(menu);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            if (isnet()) {
                new StartSession(AdvanceProvisionalActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadCustomerJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });


            }
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdvanceProvisionalActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((edt_amount.getText().toString().equalsIgnoreCase("") ||
                edt_amount.getText().toString().equalsIgnoreCase(" ") ||
                edt_amount.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(AdvanceProvisionalActivity.this, "Amount is required", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(AdvanceProvisionalActivity.this, "Bank name is required", Toast.LENGTH_LONG).show();
            return false;
        } else if (edt_tds_amount.getText().toString().equalsIgnoreCase("") ||
                edt_tds_amount.getText().toString().equalsIgnoreCase(" ") ||
                edt_tds_amount.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(AdvanceProvisionalActivity.this, "TDS is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(" ") ||
                edt_date.getText().toString().equalsIgnoreCase(null)) {

            Toast.makeText(AdvanceProvisionalActivity.this, "Date is required", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((spinner_customer.getText().toString().equalsIgnoreCase("") ||
                spinner_customer.getText().toString().toString().equalsIgnoreCase(" ") ||
                spinner_customer.getText().toString().toString().equalsIgnoreCase(null))) {

            Toast.makeText(AdvanceProvisionalActivity.this, "Customer name is Required", Toast.LENGTH_LONG).show();
            return false;

        } else if ((spinner_consignee.getText().toString().equalsIgnoreCase("") ||
                spinner_consignee.getText().toString().toString().equalsIgnoreCase(" ") ||
                spinner_consignee.getText().toString().toString().equalsIgnoreCase(null))) {

            Toast.makeText(AdvanceProvisionalActivity.this, "Consignee name is Required", Toast.LENGTH_LONG).show();
            return false;

        } else {
            return true;
        }
    }

    class DownloadConsigneeJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvanceProvisionalActivity.this);
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
                String url = CompanyURL + WebUrlClass.api_GetFillConsignee +
                        "?CustId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\\\\\/", "");
                // response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();

                /*sql.delete(db.TABLE_Consignee, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Consignee, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_Consignee, null, values);

                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            try {

                JSONArray jResults = new JSONArray(response);
                if (jResults.length()>0) {

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        ShipToMasterId = jsonObject.getString("ShipToMasterId");
                        String ConsigneeName = jsonObject.getString("ConsigneeName");
                        spinner_consignee.setText(ConsigneeName);

                    }

                }else {
                    ShipToMasterId = "";
                    Toast.makeText(AdvanceProvisionalActivity.this,"No consignee name present" +
                            " to respective customer name",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

           // getConsignee();
            progressDialog.dismiss();
        }

    }

    private void getConsignee() {

        String query = "SELECT distinct ConsigneeName,ShipToMasterId" +
                " FROM " + db.TABLE_Consignee +
                " WHERE ConsigneeName='" + CustomerName + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                ShipToMasterId = cur.getString(cur.getColumnIndex("ShipToMasterId"));
                String ConsigneeName = cur.getString(cur.getColumnIndex("ConsigneeName"));
                spinner_consignee.setText(ConsigneeName);


            } while (cur.moveToNext());

        } else {
            ShipToMasterId = "";
            Toast.makeText(AdvanceProvisionalActivity.this,"No consignee name present" +
                    " to respective customer name",Toast.LENGTH_SHORT).show();
        }

        /*String query = "SELECT distinct ShipToMasterId,ConsigneeName" +
                " FROM " + db.TABLE_Consignee ;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Scur.getString(cur.getColumnIndex("ConsigneeName")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstConsignee, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(AdvanceProvisionalActivity.this,
                R.layout.crm_custom_spinner_txt, lstConsignee);
        spinner_consignee.setAdapter(customDept);
        Collections.sort(lstConsignee, String.CASE_INSENSITIVE_ORDER);*/

    }

    class DownloadBanknameData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvanceProvisionalActivity.this);
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

        MySpinnerAdapter customDept = new MySpinnerAdapter(AdvanceProvisionalActivity.this,
                R.layout.crm_custom_spinner_txt, listBanknamedata);
        spinner_bankname.setAdapter(customDept);
        //   customDept.notifyDataSetChanged();
        spinner_bankname.setSelection(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTRY && resultCode == COUNTRY) {
            if (isCustomer == true) {
                CustomerName = data.getStringExtra("Name");
                CustVendorMasterId = data.getStringExtra("ID");

                spinner_customer.setText(CustomerName);
                if (isnet()) {
                    new StartSession(AdvanceProvisionalActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadConsigneeJSON().execute(CustVendorMasterId);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }else{
                    Toast.makeText(AdvanceProvisionalActivity.this,"Please Check Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }else if(isBank == true){
                String BankName = data.getStringExtra("Name");
                BankMasterId = data.getStringExtra("ID");

                spinner_bankname.setText(BankName);
            }

        }
    }

}
