package com.vritti.AlfaLavaModule.PI;

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
import android.os.Handler;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.AlfaLavaModule.activity.PutAwayScanDetails;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.TicketUpdateDEPLActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateBatchActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;


    Button btn_create_batch;
    Spinner spinner_warehouse;
    private String warehousename;
    private String  warehouseID;
    String PICode="",PIHeaderID="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_batch_lay);
        ButterKnife.bind(this);
        pContext = CreateBatchActivity.this;
        getSupportActionBar().setTitle("Create Batch");


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        sql.delete(db.TABLE_GRN_PACKET, null, null);

        btn_create_batch=findViewById(R.id.btn_create_batch);
        spinner_warehouse=findViewById(R.id.spinner_warehouse);


        btn_create_batch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    ProgressHUD.show(pContext, "Creating batch ...", true, false);
                    new StartSession(pContext, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            PostCreateBatch postCreateBatch = new PostCreateBatch();
                            postCreateBatch.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                } else {
                    Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                 //startActivity(new Intent(CreateBatchActivity.this, LocationScanner.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        if (CheckWarehouse()) {
            ArrayList<String> lstWarehouse = getWarehouse();
            MySpinnerAdapter customDept = new MySpinnerAdapter(CreateBatchActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstWarehouse);
            spinner_warehouse.setAdapter(customDept);

        } else {
            if (isnet()) {
                new StartSession(CreateBatchActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetWarehouse().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
            }
        }


        spinner_warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                warehousename = spinner_warehouse.getSelectedItem().toString();
                warehouseID = getWarehouseID(warehousename);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private class GetWarehouse extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getWarehouse;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_TICKET_UPDATE_WAREHOUSE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);


                    }

                    long a = sql.insert(db.TABLE_TICKET_UPDATE_WAREHOUSE, null, values);
                    Log.e("Warehouse : ", "" + a);

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
            ArrayList<String> lstWarehouse = getWarehouse();
            MySpinnerAdapter customDept = new MySpinnerAdapter(CreateBatchActivity.this,
                    R.layout.vwb_custom_spinner_txt, lstWarehouse);
            spinner_warehouse.setAdapter(customDept);

            }



    }



    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }



    private ArrayList<String> getWarehouse() {

        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data.add(cur.getString(cur.getColumnIndex("WarehouseDescription")));
            } while (cur.moveToNext());

        }
        return data;
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
    private Boolean CheckWarehouse() {
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    private String getWarehouseID(String name) {
        String data = "";
        String query = "SELECT *" +
                " FROM " + db.TABLE_TICKET_UPDATE_WAREHOUSE + " WHERE WarehouseDescription='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("WareHouseMasterId"));
            } while (cur.moveToNext());

        }
        return data;
    }

    public class PostCreateBatch extends AsyncTask<String, Void, String> {
        Object res;
        String response;




        @Override
        protected String doInBackground(String... params) {


            String url = CompanyURL+ WebUrlClass.PostPiDataForAndroid+"?WareHouseId="+warehouseID;


            try {
                res = ut.OpenConnection(url,pContext);
                response = res.toString();
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId
            ProgressHUD.Destroy();

            String s = res;

            if (response.equals("Error")){
                ProgressHUD.Destroy();
            }
            if(s.equals("[]")) {
                Toast.makeText(pContext, "Record not present", Toast.LENGTH_LONG).show();

            }
            else if(s.contains("Location not found in data")){
                Toast.makeText(pContext, "Location not found in data", Toast.LENGTH_LONG).show();

            }else {
                try {




                    String[] namesList = s.split("#");
                    PIHeaderID = namesList [0];
                    PICode = namesList [1];

                    Toast.makeText(CreateBatchActivity.this,"Batch created successfully",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(CreateBatchActivity.this,LocationScanner.class)
                            .putExtra("PICode",PICode).
                                    putExtra("PIHeaderID",PIHeaderID).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }

    }

}
