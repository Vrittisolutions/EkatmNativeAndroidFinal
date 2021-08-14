package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SupportStaffMaster extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;
    public static ProgressBar mprogress;


    LinearLayout lin_loginid, lin_name, lin_adress, lin_mobile,
            lin_mailid, lin_ifsc, lin_bankname,
            lin_branchname, lin_accnumber, lin_adhar, lin_pan,
            lin_vendor, lin_plantaccess;
    EditText edt_loginid, edt_name, edt_adress, edt_mobile,
            edt_mailid, edt_ifsc, edt_accnumber, edt_adhar, edt_pan;
    TextView edt_bankname,
            edt_branchname;
    SearchableSpinner sp_vendor, sp_plantaccess;
    CheckBox checkBox;
    Button btn_save, btn_cancel, btn_ifsc;

    String IFSCCode = "", string_Vendor = "", string_plant = "", string_Vendorid = "", string_plantid = "";
    List<String> lstSupplier = new ArrayList<String>();
    List<String> lstSupplierPlant = new ArrayList<String>();
    private static Boolean isverified_ifsc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_staff_master);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        mprogress = findViewById(R.id.toolbar_progress_staff);
        init();
        setListner();
        if (cf.getSuppliercount() > 0) {
            getSupplier();
        } else {
            if (ut.isNet(SupportStaffMaster.this)) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadSupplierJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(SupportStaffMaster.this, msg);
                    }
                });
            }
        }

        if (cf.getSupplierPlantcount() > 0) {
            getSupplierPlant();
        } else {
            if (ut.isNet(SupportStaffMaster.this)) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadSupplierPlantJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(SupportStaffMaster.this, msg);
                    }
                });
            }
        }
    }

    private void init() {

        lin_loginid = (LinearLayout) findViewById(R.id.lin_login);
        lin_name = (LinearLayout) findViewById(R.id.lin_name);
        lin_adress = (LinearLayout) findViewById(R.id.lin_address);
        lin_mobile = (LinearLayout) findViewById(R.id.lin_mobile);
        lin_mailid = (LinearLayout) findViewById(R.id.lin_mailid);
        lin_ifsc = (LinearLayout) findViewById(R.id.lin_ifsc);
        lin_bankname = (LinearLayout) findViewById(R.id.lin_Bankname);
        lin_branchname = (LinearLayout) findViewById(R.id.lin_Branchname);
        lin_accnumber = (LinearLayout) findViewById(R.id.lin_AccountName);
        lin_adhar = (LinearLayout) findViewById(R.id.lin_AadharNumber);
        lin_pan = (LinearLayout) findViewById(R.id.lin_pan);
        lin_vendor = (LinearLayout) findViewById(R.id.lin_throughven);
        lin_plantaccess = (LinearLayout) findViewById(R.id.lin_plantaccess);

        edt_loginid = (EditText) findViewById(R.id.edt_login);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_adress = (EditText) findViewById(R.id.edt_address);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_mailid = (EditText) findViewById(R.id.edt_mailid);
        edt_ifsc = (EditText) findViewById(R.id.edt_ifsc);
        edt_bankname = (TextView) findViewById(R.id.edt_bankname);
        edt_branchname = (TextView) findViewById(R.id.edt_Branchname);
        edt_accnumber = (EditText) findViewById(R.id.edt_accountname);
        edt_adhar = (EditText) findViewById(R.id.edt_aadharnumber);
        edt_pan = (EditText) findViewById(R.id.edt_pan);
        sp_vendor = (SearchableSpinner) findViewById(R.id.spinner_throughven);
        sp_plantaccess = (SearchableSpinner) findViewById(R.id.spinner_plantAccess);

        checkBox = (CheckBox) findViewById(R.id.checkBox1);

        btn_ifsc = findViewById(R.id.btn_ifsc_code);
        btn_save = findViewById(R.id.btn_save);
        btn_cancel = findViewById(R.id.btn_cancel);


    }

    private void setListner() {
        btn_ifsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IFSCCode = edt_ifsc.getText().toString().trim();
                if (ut.isNet(SupportStaffMaster.this)) {
                    new checkIFSC().execute(IFSCCode);
                } else {
                    ut.displayToast(SupportStaffMaster.this, "No Internet Connetion");
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (validate()) {
                        JSONObject json = getInputJson();
                        String data = json.toString().replaceAll("\\\\", "");
                        if (ut.isNet(SupportStaffMaster.this)) {
                            sendData(data);
                        }else {
                            String remark = "Add Support User" + edt_loginid.getText().toString();
                            String url = CompanyURL + WebUrlClass.api_upload_Support_User;
                            String op = "False";
                            CreateOffline(url, data, WebUrlClass.POSTFLAG, remark, op);
                        }


                    }

            }
        });

        sp_vendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_Vendor = sp_vendor.getSelectedItem().toString();
                string_Vendorid = getSupplierCode(string_Vendor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_plantaccess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_plant = sp_plantaccess.getSelectedItem().toString();
                string_plantid = getSupplierPlantCode(string_plant);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void sendData(final String data) {

        new StartSession(this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new UploadClaimJSONData().execute(data);
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(SupportStaffMaster.this, msg);
            }
        });
    }

    private JSONObject getInputJson() {
        JSONObject main = new JSONObject();

        JSONObject User = new JSONObject();
        try {
            User.put("UserLoginId", edt_loginid.getText().toString().trim());
            User.put("UserName", edt_name.getText().toString().trim());
            User.put("Email", edt_mailid.getText().toString().trim());
            User.put("Mobile", edt_mobile.getText().toString().trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject Staff = new JSONObject();
        try {
            Staff.put("satffAddress", edt_adress.getText().toString());
            Staff.put("UserMasterId", "");
            Staff.put("Ifscode", edt_ifsc.getText().toString().trim());
            Staff.put("BranchName", edt_branchname.getText().toString().trim());

            Staff.put("BankName", edt_bankname.getText().toString().trim());
            Staff.put("AccountNo", edt_accnumber.getText().toString().trim());
            Staff.put("AdharNo", edt_adhar.getText().toString().trim());
            Staff.put("PaNo", edt_pan.getText().toString().trim());
            Staff.put("VendorName", string_Vendor);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] data = {string_plant};
        JSONArray Plantname = new JSONArray(Arrays.asList(data));
        String[] data1 = {string_plantid};
        JSONArray Plantcode = new JSONArray(Arrays.asList(data1));


        try {
            main.put("User", User);
            main.put("Staff", Staff);
            main.put("Mode", "A");
            main.put("PlantNames", (Object) Plantname);
            main.put("PlantIds", (Object) Plantcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return main;

    }

    public boolean validate() {
        String email = edt_mailid.getEditableText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // TODO Auto-generated method stub
        if ((edt_loginid.getText().toString().equalsIgnoreCase("") ||
                edt_loginid.getText().toString().equalsIgnoreCase(" ") ||
                edt_loginid.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Login Id", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_name.getText().toString().equalsIgnoreCase("") ||
                edt_name.getText().toString().equalsIgnoreCase(" ") ||
                edt_name.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_adress.getText().toString().equalsIgnoreCase("") ||
                edt_adress.getText().toString().equalsIgnoreCase(" ") ||
                edt_adress.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(context, "Please Adress", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_mobile.getText().toString().equalsIgnoreCase("") ||
                edt_mobile.getText().toString().equalsIgnoreCase(" ") ||
                edt_mobile.getText().toString().equalsIgnoreCase(null) ||
                edt_mobile.getText().length() != 10)) {
            Toast.makeText(context, "Please enter mobile or valid mobile no.", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_mailid.getText().toString().equalsIgnoreCase("") ||
                edt_mailid.getText().toString().equalsIgnoreCase(" ") ||
                edt_mailid.getText().toString().equalsIgnoreCase(null) ||
                !(email.matches(emailPattern)))) {
            Toast.makeText(context, "Enter email address or valid email address", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_ifsc.getText().toString().equalsIgnoreCase("") ||
                edt_ifsc.getText().toString().equalsIgnoreCase(" ") ||
                edt_ifsc.getText().toString().equalsIgnoreCase(null)) && isverified_ifsc) {

            Toast.makeText(context, "Please Enter ifsc and Verify it ", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_bankname.getText().toString().equalsIgnoreCase("") ||
                edt_bankname.getText().toString().equalsIgnoreCase(" ") ||
                edt_bankname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Bank name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_branchname.getText().toString().equalsIgnoreCase("") ||
                edt_branchname.getText().toString().equalsIgnoreCase(" ") ||
                edt_branchname.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Branch name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_accnumber.getText().toString().equalsIgnoreCase("") ||
                edt_accnumber.getText().toString().equalsIgnoreCase(" ") ||
                edt_accnumber.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Account Number", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_adhar.getText().toString().equalsIgnoreCase("") ||
                edt_adhar.getText().toString().equalsIgnoreCase(" ") ||
                edt_adhar.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter Aadhar Number", Toast.LENGTH_LONG).show();
            return false;
        } else if ((edt_pan.getText().toString().equalsIgnoreCase("") ||
                edt_pan.getText().toString().equalsIgnoreCase(" ") ||
                edt_pan.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter PAN", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_Vendor.equalsIgnoreCase("") ||
                string_Vendor.equalsIgnoreCase(" ") ||
                string_Vendor.equalsIgnoreCase(null))) {

            Toast.makeText(context, "Please select Vendor", Toast.LENGTH_LONG).show();
            return false;
        } else if ((string_plant.equalsIgnoreCase("") ||
                string_plant.equalsIgnoreCase(" ") ||
                string_plant.equalsIgnoreCase(null))) {

            Toast.makeText(context, "Please select Plant", Toast.LENGTH_LONG).show();
            return false;

        } else {
            return true;
        }

    }

    private void getSupplier() {

        lstSupplier.clear();
        String query = "SELECT * " +
                " FROM " + db.TABLE_SUPPLIER;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstSupplier.add(cur.getString(cur.getColumnIndex("CustVendorName")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstSupplier, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(SupportStaffMaster.this,
                R.layout.crm_custom_spinner_txt, lstSupplier);
        sp_vendor.setAdapter(customDept);
        Collections.sort(lstSupplier, String.CASE_INSENSITIVE_ORDER);

    }

    private String getSupplierCode(String Suppliername) {
        String Data = "";
        String query = "SELECT * " +
                " FROM " + db.TABLE_SUPPLIER + " Where CustVendorName='" + Suppliername + "'";

        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                Data = cur.getString(cur.getColumnIndex("CustVendorMasterId"));

            } while (cur.moveToNext());

        }

        return Data;
    }

    private void getSupplierPlant() {

        lstSupplierPlant.clear();
        String query = "SELECT * " +
                " FROM " + db.TABLE_SUPPLIER_PLANT;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                lstSupplierPlant.add(cur.getString(cur.getColumnIndex("PlantName")));

            } while (cur.moveToNext());

        }
        Collections.sort(lstSupplierPlant, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(SupportStaffMaster.this,
                R.layout.crm_custom_spinner_txt, lstSupplierPlant);
        sp_plantaccess.setAdapter(customDept);

    }

    private String getSupplierPlantCode(String plantname) {

        String Data = "";
        String query = "SELECT * " +
                " FROM " + db.TABLE_SUPPLIER_PLANT + " Where PlantName='" + plantname + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Data = cur.getString(cur.getColumnIndex("PlantMasterId"));

            } while (cur.moveToNext());

        }
        return Data;

    }

    class UploadClaimJSONData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (integer.equalsIgnoreCase("False")) {
                Toast.makeText(SupportStaffMaster.this, "Support User Added  Succesfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SupportStaffMaster.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SupportStaffMaster.this, "Can not add Support User", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_upload_Support_User;
            try {
                res = ut.OpenPostConnection(url, params[0], SupportStaffMaster.this);
                int b = res.toString().getBytes().length;
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
                // "False"
            }
            return response;
        }
    }

    class DownloadSupplierPlantJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showprogress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetSupplierPlant;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_SUPPLIER_PLANT, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUPPLIER_PLANT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SUPPLIER_PLANT, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.contains("PlantMasterId")) {

                getSupplierPlant();
            } else {
                ut.displayToast(SupportStaffMaster.this, "Server Error occurred...Try After some time");
            }


        }

    }

    class DownloadSupplierJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showprogress();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetSupplier;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_SUPPLIER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUPPLIER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SUPPLIER, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.contains("CustVendorMasterId")) {

                getSupplier();
            } else {
                ut.displayToast(SupportStaffMaster.this, "Server Error occurred...Try After some time");
            }


        }

    }


    class checkIFSC extends AsyncTask<String, Void, String> {
        String url;
        Object res;
        String response, Bankaddress, Bankname, Branch;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }


        @Override
        protected String doInBackground(String... params) {
            url = WebUrlClass.api_checkifsc + params[0] + "/";
            try {
                res = ut.OpenConnection(url, SupportStaffMaster.this);
                response = res.toString().replaceAll("\\\\n", "");
                   /* response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);*/
                JSONObject jorder = new JSONObject(response);

                Bankaddress = jorder.getString("address");
                Bankname = jorder.getString("bank");
                Branch = jorder.getString("branch");
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (!(integer.equalsIgnoreCase(WebUrlClass.setError))) {

                isverified_ifsc = true;
                edt_bankname.setText(Bankname);
                edt_branchname.setText(Branch);
                ut.displayToast(SupportStaffMaster.this, "IFSC verified successfully");
            } else {
                isverified_ifsc = false;
                ut.displayToast(SupportStaffMaster.this, "Not valid IFSC");
            }
        }
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

    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);

    }

    private void CreateOffline(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            Intent intent = new Intent(getApplicationContext(), ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                return true;

            case R.id.refresh:
                if (ut.isNet(SupportStaffMaster.this)) {
                    new StartSession(this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadSupplierJSON().execute();
                            new DownloadSupplierPlantJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(SupportStaffMaster.this, msg);
                        }
                    });
                }else {
                    ut.displayToast(SupportStaffMaster.this,"No Internet Connetion");
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
