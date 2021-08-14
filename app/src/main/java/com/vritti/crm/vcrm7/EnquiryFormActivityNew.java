package com.vritti.crm.vcrm7;

/**
 * Created by Admin-1 on 4/10/2018.
 */

import android.app.ProgressDialog;
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
import com.vritti.crm.bean.CityBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EnquiryFormActivityNew extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SharedPreferences sharedpreferences;
    List<String> lstPlan;
    List<String> lstbike;
    List<String> lstCity = new ArrayList<String>();
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
    EditText eName, eAddress, eEmail, eContactNo, eAge, eOccupation, remark;
    SQLiteDatabase sql;
    String UserType;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_enquiry_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES,
                Context.MODE_PRIVATE);
        spCity = sharedpreferences.getString("City", "");
        spAddress = sharedpreferences.getString("Address", "");
        spEmail = sharedpreferences.getString("Email", "");
        spOccupation = sharedpreferences.getString("Occupation", "");
        spPurchasemode = sharedpreferences.getString("Purchasemode", "");
        spBuyplan = sharedpreferences.getString("Buyplan", "");
        spProduct = sharedpreferences.getString("Product", "");
        spRemark = sharedpreferences.getString("Remark", "");
        spAge = sharedpreferences.getString("Age", "");
        init();

        if (spCity.equalsIgnoreCase("Checked")) {
            len_city.setVisibility(View.VISIBLE);

            if (cf.check_City() > 0) {
                getCity();
            } else {
                if (isnet()) {
                    new StartSession(EnquiryFormActivityNew.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadCityJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
            }
        } else {
            len_city.setVisibility(View.GONE);
        }
        if (spAddress.equalsIgnoreCase("Checked")) {
            ((EditText) findViewById(R.id.eAddress)).setVisibility(View.VISIBLE);
        } else {
            ((EditText) findViewById(R.id.eAddress)).setVisibility(View.GONE);
        }

        if (spEmail.equalsIgnoreCase("Checked")) {
            ((EditText) findViewById(R.id.eEmail)).setVisibility(View.VISIBLE);
        } else {
            ((EditText) findViewById(R.id.eEmail)).setVisibility(View.GONE);
        }

        if (spAge.equalsIgnoreCase("Checked")) {
            ((EditText) findViewById(R.id.eAge)).setVisibility(View.VISIBLE);
        } else {
            ((EditText) findViewById(R.id.eAge)).setVisibility(View.GONE);
        }

        if (spOccupation.equalsIgnoreCase("Checked")) {
            ((EditText) findViewById(R.id.eOccupation)).setVisibility(View.VISIBLE);
        } else {
            ((EditText) findViewById(R.id.eOccupation)).setVisibility(View.GONE);
        }

        if (spPurchasemode.equalsIgnoreCase("Checked")) {
            layPmode.setVisibility(View.VISIBLE);
        } else {
            layPmode.setVisibility(View.GONE);
        }

        if (spBuyplan.equalsIgnoreCase("Checked")) {
            ((Spinner) findViewById(R.id.sPlan)).setVisibility(View.VISIBLE);
        } else {
            ((Spinner) findViewById(R.id.sPlan)).setVisibility(View.GONE);
        }

        if (spProduct.equalsIgnoreCase("Checked")) {
            llProduct.setVisibility(View.VISIBLE);

            if (cf.getProuctcount() > 0) {
                getproduct();
            } else {
                if (isnet()) {
                    new StartSession(this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadProductJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        } else {
            llProduct.setVisibility(View.GONE);
        }

        if (spRemark.equalsIgnoreCase("Checked")) {
            ((EditText) findViewById(R.id.remark)).setVisibility(View.VISIBLE);
        } else {
            ((EditText) findViewById(R.id.remark)).setVisibility(View.GONE);
        }


        lstPlan = new ArrayList<String>();
        lstPlan.add("--Select buy plan--");
        lstPlan.add("1 Month");
        lstPlan.add("6 Month");
        lstPlan.add("No Plan");

        MySpinnerAdapter_plan customPlan = new MySpinnerAdapter_plan(EnquiryFormActivityNew.this,
                R.layout.crm_view_spinner_item, lstPlan);
        ((Spinner) findViewById(R.id.sPlan)).setAdapter(customPlan);


        lstbike = new ArrayList<String>();
        lstbike.add("--Select--");
        lstbike.add("Replacement");
        lstbike.add("Purchase");


        MySpinnerAdapter_bike custombike = new MySpinnerAdapter_bike(EnquiryFormActivityNew.this,
                R.layout.crm_view_spinner_item, lstbike);
        ((Spinner) findViewById(R.id.spinner_bike)).setAdapter(custombike);

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
        headtitle = ((TextView) findViewById(R.id.headtitle));
        eName = ((EditText) findViewById(R.id.eName));
        eAddress = ((EditText) findViewById(R.id.eAddress));
        eEmail = ((EditText) findViewById(R.id.eEmail));
        eContactNo = ((EditText) findViewById(R.id.eContactNo));
        eAge = ((EditText) findViewById(R.id.eAge));
        eOccupation = ((EditText) findViewById(R.id.eOccupation));
        llProduct = (LinearLayout) findViewById(R.id.llProduct);
        len_city = (LinearLayout) findViewById(R.id.len_city);
        spinner_product = (SearchableSpinner) findViewById(R.id.spinner_product);
        txtbk = ((TextView) findViewById(R.id.txtbk));// remark
        remark = ((EditText) findViewById(R.id.remark));
        eAutoCity = (SearchableSpinner) findViewById(R.id.eAutoCity);
        layPmode = (LinearLayout) findViewById(R.id.layPmode);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        spinner_toWhom = (Spinner) findViewById(R.id.spinner_toWhom);
        spinner_Assigntocall = (SearchableSpinner) findViewById(R.id.spinner_Assigntocall);

        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String query = "SELECT distinct ItemDesc,ItemMasterId" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemDesc='" + (String) spinner_product.getSelectedItem() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        Productid = cur.getString(cur.getColumnIndex("ItemMasterId"));

                    } while (cur.moveToNext());

                } else {
                    Productid = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        eAutoCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct CityName,PKCityID" +
                        " FROM " + db.TABLE_CITY +
                        " WHERE CityName='" + eAutoCity.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Cityid = cur.getString(cur.getColumnIndex("PKCityID"));

                    } while (cur.moveToNext());

                } else {
                    Cityid = "";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=" + URLEncoder.encode("", "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_CITY, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            String jsonAddeddt = jorder.getString("AddedDt");
                            String jsonModifiedDt = jorder.getString("ModifiedDt");
                            if (columnName.equalsIgnoreCase("AddedDt")) {
                                jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonAddeddt);
                                DOBDate = new Date(DOB_date);
                                jsonAddeddt = sdf.format(DOBDate);
                                values.put(columnName, jsonAddeddt);

                            } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                                jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                        + 1, jsonModifiedDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonModifiedDt);
                                DOBDate = new Date(DOB_date);
                                jsonModifiedDt = sdf.format(DOBDate);
                                values.put(columnName, jsonModifiedDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CITY, null, values);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //  dismissProgressDialog();
            if (response.contains("")) {

                //   getInitiatedby();
                getCity();
            }

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

    private static class MySpinnerAdapter_plan extends ArrayAdapter<String> {
        // Initialise custom font, for example:

        private MySpinnerAdapter_plan(Context context, int resource,
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
            plan = view.getText().toString();

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

    private static class MySpinnerAdapter_bike extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter_bike(Context context, int resource,
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
            call_bike = view.getText().toString();

            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);

            // call_bike = view.getText().toString();
            return view;
        }

    }


    public void AddToLocal(String name, String address, String city,
                           String occupation, String email, String mobile, String product,
                           String remark, String age, String Purchasemode, String Buyplan) {
        // TODO Auto-generated method stub


        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("city", city);
        values.put("occupation", occupation);
        values.put("email", email);
        values.put("mobile", mobile);
        values.put("product", product);
        values.put("remark", remark);
        values.put("age", age);
        values.put("Purchasemode", Purchasemode);
        values.put("Buyplan", Buyplan);
        values.put("isUpload", "No");
        // Inserting Row
        Long data = sql.insert("EnquiryForm", null, values);
        Log.d("test", " values " + values);
        Cursor c = sql.rawQuery("select * From EnquiryForm", null);
        int cntNo = c.getCount();

        Toast.makeText(EnquiryFormActivityNew.this, "Data saved successfully..", Toast.LENGTH_LONG).show();
        c.close();
       // sql.close();



    }

    class DownloadProductJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Product;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Product, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }
                        long a = sql.insert(db.TABLE_Product, null, values);
                        Log.e("product", "" + a);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("")) {

            }
            getproduct();
        }

    }

    private void getproduct() {


        lstProduct.clear();
        String query = "SELECT distinct ItemMasterId,ItemDesc" +
                " FROM " + db.TABLE_Product;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                lstProduct.add(cur.getString(cur.getColumnIndex("ItemDesc")));
            } while (cur.moveToNext());
        }
        Collections.sort(lstProduct, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(EnquiryFormActivityNew.this,
                R.layout.crm_custom_spinner_txt, lstProduct);
        spinner_product.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(lstProduct, String.CASE_INSENSITIVE_ORDER);
        //   int a =  lstProduct.indexOf(Product_name);
        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        //spinner_product.setSelection(Productionitems.indexOf(Product_name));
    }

    private void clear() {
        ((EditText) findViewById(R.id.eName)).setText("");
        ((EditText) findViewById(R.id.eAddress)).setText("");
        ((EditText) findViewById(R.id.eEmail)).setText("");
        ((EditText) findViewById(R.id.eContactNo)).setText("");
        ((EditText) findViewById(R.id.eAge)).setText("");
        ((EditText) findViewById(R.id.eOccupation)).setText("");
        //((AutoCompleteTextView) findViewById(R.id.Product)).setHint("--Select Product--");// remark
        ((EditText) findViewById(R.id.remark)).setText("");
        ((Spinner) findViewById(R.id.sPlan)).setSelection(0);
        ((Spinner) findViewById(R.id.spinner_bike)).setSelection(0);
        //spinner_bike

    }

    private boolean isValidName(String pass) {
        if (pass != null || pass.length() != 0) {
            return true;
        }
        return false;
    }

    private boolean isValidMob(String pass) {
        if (pass != null || pass.length() != 0 || pass.length() <= 10) {
            return true;
        }
        return false;
    }

    /*private boolean isValidCity(String pass) {
        if (spCity.equalsIgnoreCase("Checked")) {
            if (pass != null || pass.length() != 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }*/

    //&& pass.length() != 0
   /* private boolean isValidProduct(String pass) {
        if (spProduct.equalsIgnoreCase("Checked")) {
            if (!(pass.equalsIgnoreCase("--Select Product--") || pass == null || pass == "")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }*/

    private boolean isValidPlan(String pass) {
        if (spBuyplan.equalsIgnoreCase("Checked")) {
            if (!(pass.equalsIgnoreCase("--Select buy plan--")
                    || pass == null || pass == "")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPMode(String pass) {
        if (spPurchasemode.equalsIgnoreCase("Checked")) {
            if (!(pass.equalsIgnoreCase("--Select--") || pass == null)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void CancelClick(View v) {
        clear();
        Intent i = new Intent(EnquiryFormActivityNew.this, CallListActivity.class);
        startActivity(i);
        EnquiryFormActivityNew.this.finish();
    }

    public void AddEnquiry(View v) {
        String name = "0", address = "0", city = "0", occupation = "0", Email = "0",
                Mobile = "0", productString = "0", remark = "0", age = "0";
        name = ((EditText) findViewById(R.id.eName)).getText()
                .toString();
        address = ((EditText) findViewById(R.id.eAddress)).getText()
                .toString();

        occupation = ((EditText) findViewById(R.id.eOccupation))
                .getText().toString();
        Email = ((EditText) findViewById(R.id.eEmail)).getText()
                .toString();
        Mobile = ((EditText) findViewById(R.id.eContactNo)).getText()
                .toString();
       /* productString = ((AutoCompleteTextView) findViewById(R.id.Product))
                .getText().toString();
     */
        remark = ((EditText) findViewById(R.id.remark))
                .getText().toString();
        age = ((EditText) findViewById(R.id.eAge))
                .getText().toString();
        int flag = 0;

        if (spProduct.equalsIgnoreCase("Checked")) {
        } else {
            productString = "0";
        }
        if (spCity.equalsIgnoreCase("Checked")) {
        } else {
            city = "0";
        }
        if (spBuyplan.equalsIgnoreCase("Checked")) {

        } else {
            plan = "0";
        }
        if (spPurchasemode.equalsIgnoreCase("Checked")) {

        } else {
            call_bike = "0";
        }
        if (spEmail.equalsIgnoreCase("Checked")) {

        } else {
            Email = "0";
        }
        if (spAddress.equalsIgnoreCase("Checked")) {

        } else {
            address = "0";
        }
        if (spAge.equalsIgnoreCase("Checked")) {

        } else {
            age = "0";
        }
        if (spOccupation.equalsIgnoreCase("Checked")) {

        } else {
            occupation = "0";
        }
        if (!isValidName(name)) {
            ((EditText) findViewById(R.id.eName)).setError("Required");
            name = "0";
            flag = 1;
        } /*else if (!isValidCity(city)) {
            city = "0";
            flag = 1;
        }*/ else if (!isValidMob(Mobile)) {
            ((EditText) findViewById(R.id.eContactNo)).setError("Required");
            Mobile = "0";
            flag = 1;
        } /*else if (!isValidProduct(productString)) {
            productString = "0";
            flag = 1;
            Toast.makeText(EnquiryFormActivityNew.this, "Select product",
                    Toast.LENGTH_LONG).show();
        }*/ else if (!isValidPlan(plan)) {
            plan = "0";
            flag = 1;
            Toast.makeText(EnquiryFormActivityNew.this, "Select buy plan",
                    Toast.LENGTH_LONG).show();
        } else if (!isValidPMode(call_bike)) {
            call_bike = "0";
            Toast.makeText(EnquiryFormActivityNew.this, "Select purchase mode",
                    Toast.LENGTH_LONG).show();
            flag = 1;
        }

        if (flag == 0) {


            try {
                String query = "";
                query += "SELECT * FROM " + db.TABLE_Product;
                query += " WHERE ItemDesc= '" + (String) spinner_product.getSelectedItem() + "'";
                Cursor cur4 = sql.rawQuery(query, null);
                prod = new String[cur4.getCount()];
                if (cur4.getCount() > 0) {

                    cur4.moveToFirst();

                    do {

                        Productid = cur4.getString(cur4.getColumnIndex("ItemMasterId"));


                    } while (cur4.moveToNext());
                } else {
                    Productid = "0";
                }

            } catch (Exception e) {

            }
            JSONObject jsonenquiry = new JSONObject();

            try {
                jsonenquiry.put("FirmName", name);
                jsonenquiry.put("ContactName", name);
                jsonenquiry.put("MobileNo", 789889);
                jsonenquiry.put("Address", address);
                jsonenquiry.put("MailId", Email);
                jsonenquiry.put("Age", 45);
                jsonenquiry.put("Occuption", occupation);
                jsonenquiry.put("PurchaseMode", "1");
                jsonenquiry.put("BuyPlan", "1month");
                jsonenquiry.put("Product", Productid);
                jsonenquiry.put("Remark", remark);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String query = "SELECT * FROM " + db.TABLE_CITY;
            query += " WHERE CityName= '" + (String) eAutoCity.getSelectedItem() + "'";

            Cursor cur3 = sql.rawQuery(query, null);
            if (cur3.getCount() > 0) {

                cur3.moveToFirst();

                do {

                    Cityid = cur3.getString(cur3.getColumnIndex("PKCityID"));
                } while (cur3.moveToNext());
            } else {
                Cityid = "";
            }


            final String jsonenquirystring = jsonenquiry.toString().replaceAll("\\\\", "");
            //finaljson = finaljson.replaceAll(" ", "%20");
            if (isnet()) {
                new StartSession(EnquiryFormActivityNew.this, new CallbackInterface() {
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


        //  clear();
    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EnquiryFormActivityNew.this);
            progressDialog.setMessage("Please wait data sending...");

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Post_Enquiry;
            try {
                res = ut.OpenPostConnection(url, params[0],EnquiryFormActivityNew.this);
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
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("Error")) {
                Toast.makeText(EnquiryFormActivityNew.this, "Data not saved ", Toast.LENGTH_LONG).show();
            } else if (response.equalsIgnoreCase("False")) {//False
                Toast.makeText(EnquiryFormActivityNew.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EnquiryFormActivityNew.this, "Data not saved ", Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }

    private void showProgressDialog() {


        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {

        progressbar.setVisibility(View.GONE);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent i = new Intent(EnquiryFormActivityNew.this, CallListActivity.class);
        startActivity(i);*/
        EnquiryFormActivityNew.this.finish();
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
                new StartSession(EnquiryFormActivityNew.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadProductJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

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

    private void getCity() {

        List<CityBean> lstdb = cf.getCitybean();
        lstCity.clear();
        for (int i = 0; i < lstdb.size(); i++)
            lstCity.add(lstdb.get(i).getCityName());
        MySpinnerAdapter customAdcity = new MySpinnerAdapter(EnquiryFormActivityNew.this,
                R.layout.crm_custom_spinner_txt, lstCity);

        eAutoCity.setAdapter(customAdcity);
        //  int a = lstCity.indexOf(City_name);
        //  eAutoCity.setSelection(lstCity.indexOf(City_name));
    }

    class DownloadSEJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_SE;
            try {
                res = ut.OpenConnection(url);
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
            dismissProgressDialog();
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
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Boe;
            try {
                res = ut.OpenConnection(url);
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
            //   dismissProgressDialog();
            if (response.contains("UserLoginId")) {
                // displayBOE();
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

    private void displayProductDetails() {


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
}

