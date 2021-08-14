package com.vritti.crm.vcrm7;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ReassignCallsActivity extends AppCompatActivity {
    TextView txtcall;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SQLiteDatabase sql;
    String callid, firmname, Usermasterid;
    Spinner spinnerReassignto;
    Button btnReassign, btncancel;
    List category;
    ProgressHUD progressHUD;
    String finaljson;
    SharedPreferences userpreferences;
    String AssignToUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_reassign_calls);
        init();


        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
        firmname = intent.getStringExtra("firmname");
        Usermasterid = intent.getStringExtra("Usermasterid");
        txtcall.setText(firmname);

        if (cf.getExecutivecount() > 0) {
            getCategory();
        } else {
            if (isnet()) {
                new StartSession(ReassignCallsActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        setListener();
    }

    private void setListener() {

        btnReassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinnerReassignto.getSelectedItem().toString().equalsIgnoreCase("Select")) {

                } else {
                    String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                            " FROM " + db.TABLE_Executive + " WHERE " +
                            " UserName='" + spinnerReassignto.getSelectedItem().toString() + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();

                        AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));


                    }


                    if (isnet()) {
                        new StartSession(ReassignCallsActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostReassignCallJSON().execute(callid, Usermasterid, AssignToUserId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }


                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    class PostReassignCallJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Reassign_call
                        + "?CallId=" + URLEncoder.encode(params[0], "UTF-8")
                        + "&FromUserId=" + URLEncoder.encode(params[1], "UTF-8")
                        + "&AssignToUserId=" + URLEncoder.encode(params[2], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (integer.equalsIgnoreCase("Success")) {
                Toast.makeText(ReassignCallsActivity.this, "Call Reassigned Successfully !", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ReassignCallsActivity.this, "Call Not Reassigned !", Toast.LENGTH_LONG).show();
            }


            ReassignCallsActivity.this.finish();
            //onBackPressed();
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

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        txtcall = (TextView) findViewById(R.id.txtcall);
        spinnerReassignto = (Spinner) findViewById(R.id.spinnerReassignto);
        btnReassign = (Button) findViewById(R.id.btnReassign);
        btncancel = (Button) findViewById(R.id.btncancel);

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
    }

    private void getCategory() {

        category = new ArrayList();
        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                " FROM " + db.TABLE_Executive + " WHERE CRMCategory='1' OR CRMCategory='2'";
        Cursor cur = sql.rawQuery(query, null);
        category.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                category.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (ReassignCallsActivity.this, android.R.layout.simple_spinner_item, category);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReassignto.setAdapter(dataAdapter);


    }

    private void showProgressDialog(String txt) {


        progressHUD = ProgressHUD.show(ReassignCallsActivity.this, "" + txt, false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    class DownloadCategoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            // progressHUD7 = ProgressHUD.show(ReassignCallsActivity.this, " ", false, false, null);
            showProgressDialog("");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Category;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_Executive, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Executive, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Executive, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();

            if (response != null) {

            }

            getCategory();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ReassignCallsActivity.this.finish();
    }
}
