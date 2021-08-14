package com.vritti.crmlib.vcrm7;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.vritti.crmlib.R;import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;import com.vritti.databaselib.other.Utility;import com.vritti.databaselib.other.WebUrlClass;import com.vritti.sessionlib.CallbackInterface;import com.vritti.sessionlib.StartSession;

public class AddEnquiry extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SharedPreferences sharedpreferences;
    List<String> lstPlan;
    List<String> lstbike;
    List<String> lstCity = new ArrayList<String>();
    String link, LoginUserId, strRes = "";
    ArrayList<String> Productionitems = new ArrayList<String>();
    private PopupWindow pw;
    public static boolean[] checkSelected;
    String productString = "";
    Spinner spinner_bike;
    static String plan = "", call_bike = "";
    String spCity, spAddress, spEmail, spAge,
            spOccupation, spPurchasemode, spBuyplan,
            spProduct, spRemark, Productid = "", Cityid;
    LinearLayout layPmode;
    TextView headtitle, txtbk;
    EditText eCustomerName, eContactName, eContactNo, eEmail, eRemark;
    SQLiteDatabase sql;
    String  UserType;
    SharedPreferences userpreferences;
    List<String> lstProduct = new ArrayList<String>();
    String[] susmaster;
    String[] contact;
    String[] prod;
    ProgressHUD progressHUD;
    ProgressBar progressbar;
    LinearLayout llProduct, len_city;
    SearchableSpinner spinner_product, eAutoCity, spinner_Assigntocall;
    private String Productname;
    Spinner spinner_toWhom;
    List<String> lstSE = new ArrayList<String>();
    List<String> lstBOE = new ArrayList<String>();
    private static String string_assignto = "";
    String seid = "", boeid = "", CurrentCallOwner = "", ProductId = "", OrderTypeMasterId = "";
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_add_enquiry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CRM");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
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
        UserType = userpreferences.getString("UserType", null);
        SharedPreferences prefs = getSharedPreferences("CRM", MODE_PRIVATE);
        LoginUserId = prefs.getString("LoginUserId", null);
        init();

        spinner_toWhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) spinner_toWhom.getSelectedItem();
                if (selection.equalsIgnoreCase("SE")) {
                    if (cf.getSEcount() > 0) {
                        displaySE();
                    } else {
                        if (isnet()) {
                            new StartSession(getApplicationContext(), new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadSEJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                } else if (selection.equalsIgnoreCase("BOE")) {

                    if (cf.getBOEcount() > 0) {
                        displayBOE();
                    } else {
                        if (isnet()) {
                            new StartSession(getApplicationContext(), new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadBOEJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Assigntocall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_assignto = (String) spinner_Assigntocall.getSelectedItem();
                if (string_assignto.equalsIgnoreCase("") ||
                        string_assignto.equalsIgnoreCase(" ")
                        || string_assignto.equalsIgnoreCase(null)) {
                    if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("SE")) {
                        Toast.makeText(getApplicationContext(), "No SE Present", Toast.LENGTH_LONG).show();
                    } else if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("BOE")) {
                        Toast.makeText(getApplicationContext(), "No BOE Present", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        sql = db.getWritableDatabase();
        eCustomerName = ((EditText) findViewById(R.id.eName));
        eContactName = ((EditText) findViewById(R.id.eContactName));
        eContactNo = ((EditText) findViewById(R.id.eContactNo));
        eEmail = ((EditText) findViewById(R.id.eEmail));
        eRemark = ((EditText) findViewById(R.id.remark));
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        spinner_toWhom = (Spinner) findViewById(R.id.spinner_toWhom);
        spinner_Assigntocall = (SearchableSpinner) findViewById(R.id.spinner_Assigntocall);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void ShowProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    private void HideProgress() {
        progressbar.setVisibility(View.GONE);

    }

    public void AddEnquiry(View v) {

        if (Validation()) {
            getDetails();
            String custName = "", cnctName = "", Email = "",
                    enqdetail = "", cntcNo = "";
            custName = eCustomerName.getText()
                    .toString();
            cnctName = eContactName.getText()
                    .toString();

            cntcNo = eContactNo
                    .getText().toString();
            long ContactNo = 0;
            if (!(cntcNo.equalsIgnoreCase(""))) {
                ContactNo = Long.parseLong(cntcNo);
            }
            Email = eEmail.getText()
                    .toString();
            enqdetail = eRemark.getText()
                    .toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String CurrentDate = sdf.format(new Date());

            JSONObject jsonenquiry = new JSONObject();

            try {
                jsonenquiry.put("EnquiryDate", CurrentDate);
                jsonenquiry.put("AssignedToId", CurrentCallOwner);
                jsonenquiry.put("CustomerName", custName);
                jsonenquiry.put("ContactName", cnctName);
                jsonenquiry.put("ContactNumber", ContactNo);
                jsonenquiry.put("Email", Email);
                jsonenquiry.put("EnquiryDetails", enqdetail);
                jsonenquiry.put("ActionTaken", "");
                jsonenquiry.put("ReasonForCancellation", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final String jsonenquirystring = jsonenquiry.toString().replaceAll("\\\\", "");
            //finaljson = finaljson.replaceAll(" ", "%20");
            if (isnet()) {
                new StartSession(AddEnquiry.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostProspectUpdateJSON().execute(jsonenquirystring);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void CancelClick(View v) {
        onBackPressed();

    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Enquiry;//[]
            try {
                res = ut.OpenPostConnection(url, params[0],getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            HideProgress();//"True"
            if (response.equalsIgnoreCase("Error")) {
                Toast.makeText(AddEnquiry.this, "Data not saved ", Toast.LENGTH_LONG).show();
            } else if (response.equalsIgnoreCase("False")) {//False
                Toast.makeText(AddEnquiry.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddEnquiry.this, "Data not saved ", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }

    class DownloadSEJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_SE;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_SE, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SE, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_SE, null, values);
                        Log.e("", "" + a);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            HideProgress();
            if (response.contains("UserLoginId")) {
                // displaySE();
            }
            displaySE();
        }

    }

    class DownloadBOEJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Boe;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_BOE, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BOE, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_BOE, null, values);
                        Log.e("","");

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            HideProgress();
            if (response.contains("UserLoginId")) {

            }
            displayBOE();
        }

    }

    private void displaySE() {
        lstSE.clear();
        String countQuery = "SELECT  UserName FROM "
                + db.TABLE_SE;
        Cursor cursor = sql.rawQuery(countQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstSE.add(cursor.getString(cursor.getColumnIndex("UserName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.crm_custom_spinner_txt, lstSE);
        spinner_Assigntocall
                .setAdapter(adapter);
    }

    private void displayBOE() {
        lstBOE.clear();
        String countQuery = "SELECT  UserName FROM "
                + db.TABLE_BOE;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstBOE.add(cursor.getString(cursor.getColumnIndex("UserName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(getApplicationContext(),
                R.layout.crm_custom_spinner_txt, lstBOE);
        spinner_Assigntocall.setAdapter(adapter);
    }

    private void getDetails() {


        if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("SE")) {
            String countQ = "SELECT  EkatmUserMasterId,UserName FROM "
                    + db.TABLE_SE + " WHERE UserName='" + string_assignto + "'";
            Cursor c = sql.rawQuery(countQ, null);

            if (c.getCount() > 0) {
                c.moveToFirst();

                seid = c.getString(c.getColumnIndex("EkatmUserMasterId"));
                CurrentCallOwner = seid;
            }
        } else if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("BOE")) {
            String kdjL = (String) spinner_Assigntocall.getSelectedItem();
            String countQ = "SELECT  EkatmUserMasterId,UserName FROM "
                    + db.TABLE_BOE + " WHERE UserName='" + string_assignto + "'";
            Cursor c = sql.rawQuery(countQ, null);

            if (c.getCount() > 0) {
                c.moveToFirst();

                boeid = c.getString(c.getColumnIndex("EkatmUserMasterId"));
                CurrentCallOwner = boeid;

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

            // view.setPadding(0, 10, 0, 10);
            // view.setLayoutParams(params);
            //  plan = view.getText().toString();

            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);

            //  plan = view.getText().toString();
            return view;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (isnet()) {
                new StartSession(AddEnquiry.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        ShowProgress();
                        new DownloadSEJSON().execute();
                        new DownloadBOEJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        HideProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean Validation() {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!(eCustomerName.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter customer name", Toast.LENGTH_LONG).show();
            return false;
        } else if (!(eContactName.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter contact name", Toast.LENGTH_LONG).show();
            return false;
        } else if (!(eContactNo.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter contact no", Toast.LENGTH_LONG).show();
            return false;
        } /*else if (!(eEmail.getText().toString().matches(emailPattern)) ||
                !(eEmail.getText().toString().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Enter valid email", Toast.LENGTH_LONG).show();
            return false;
        }*/ else {
            return true;
        }
    }


}
