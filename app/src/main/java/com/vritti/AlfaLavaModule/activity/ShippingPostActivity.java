package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_PickOrderNo;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ShippingPostActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_PickOrderNo adapter;
    private ArrayList<PicklistNO> picklistNOList;
    private static RecyclerView recycler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);
        getSupportActionBar().setTitle("Delivery Note List");

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(ShippingPostActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ShippingPostActivity.this);
        String dabasename = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ShippingPostActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(ShippingPostActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        picklistNOList = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShippingPostActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_PickOrderNo(picklistNOList);
        recycler.setAdapter(adapter);

        if (isnet()) {
            try {
                ProgressHUD.show(ShippingPostActivity.this, "Fetching data...", true, false);
                new StartSession(ShippingPostActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        UploadDoDump uploadDoDump = new UploadDoDump();
                        uploadDoDump.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ShippingPostActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void setdonumber(String DONumber) {


        startActivity(new Intent(ShippingPostActivity.this,DOPackingScanDetails.class).
                putExtra("dono",DONumber)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


    }
    private class UploadDoDump extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_UploadOfflineALMTAPIController;
            try {
                res = ut.OpenConnection(url, ShippingPostActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                // response = response.substring(1, response.length() - 1);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
            ProgressHUD.Destroy();
            try {
                if (s.equalsIgnoreCase("true")) ;
         /*       if (isnet()) {
                    try {
                        ProgressHUD.show(ShippingPostActivity.this, "Fetching data...", true, false);
                        new StartSession(ShippingPostActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                GetPicklistNO getPicklistNO = new GetPicklistNO();
                                getPicklistNO.execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ShippingPostActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }*/
            }catch (Exception e){
                e.printStackTrace();
            }
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

    private class GetPicklistNO extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetPackOrdNO;
            try {
                res = ut.OpenConnection(url, ShippingPostActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                // response = response.substring(1, response.length() - 1);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
            ProgressHUD.Destroy();

            picklistNOList.clear();
            if (s.contains("Pack_OrdHdrId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    for (int i=0;i<jResults.length();i++){
                        PicklistNO picklistNO=new PicklistNO();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        picklistNO.setPick_listHdrId(jsonObject.getString("Pack_OrdHdrId"));
                        picklistNO.setPicklistNo(jsonObject.getString("Pack_OrdNo"));
                        picklistNOList.add(picklistNO);
                    }

                    adapter.update(picklistNOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {
                Toast.makeText(ShippingPostActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ShippingPostActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            ProgressHUD.Destroy();
        }
    }


}