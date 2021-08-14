package com.vritti.AlfaLavaModule.activity;

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
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.picking.ItemPickListDetailActivity;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.adapter.Adp_Box;
import com.vritti.AlfaLavaModule.adapter.Adp_DONumber;
import com.vritti.AlfaLavaModule.bean.BoxBean;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DOListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_DONumber adapter;
    private ArrayList<PicklistNO> picklistNOList;
    private static RecyclerView recycler;
    private ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);
        getSupportActionBar().setTitle("Picking List");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(DOListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(DOListActivity.this);
        String dabasename = ut.getValue(DOListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(DOListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(DOListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(DOListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(DOListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(DOListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(DOListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(DOListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(DOListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(DOListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        picklistNOList = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DOListActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_DONumber(picklistNOList);
        recycler.setAdapter(adapter);

        sql.delete(db.TABLE_GRN_PACKET, null, null);

        /*if (isnet()) {
            try {
                progress.setVisibility(View.VISIBLE);
                new StartSession(DOListActivity.this, new CallbackInterface() {
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
            Toast.makeText(DOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }*/


       /* if (isnet()) {
            try {
                ProgressHUD.show(DOListActivity.this, "Fetching data...", true, false);
                new StartSession(DOListActivity.this, new CallbackInterface() {
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
            Toast.makeText(DOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }*/



    }

    public void setdonumber(String DONumber,String headerid,String QCStatus) {




       /* if (Constants.type == Constants.Type.Alfa) {

            startActivity(new Intent(DOListActivity.this, PickListDetailActivity.class).
                    putExtra("dono",DONumber)
                    .putExtra("headerid",headerid)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }else {
            startActivity(new Intent(DOListActivity.this, ItemPickListDetailActivity.class).
                    putExtra("dono", DONumber)
                    .putExtra("headerid", headerid)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }*/
        startActivity(new Intent(DOListActivity.this, ItemPickListDetailActivity.class).
                putExtra("dono", DONumber)
                .putExtra("headerid", headerid)
                .putExtra("qcstatus", QCStatus)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


    }
    private class UploadDoDump extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_UploadDoDump;
            try {
                res = ut.OpenConnection(url, DOListActivity.this);
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
                if (isnet()) {
                    try {
                        ProgressHUD.show(DOListActivity.this, "Fetching data...", true, false);
                        new StartSession(DOListActivity.this, new CallbackInterface() {
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
                    Toast.makeText(DOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
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
            String url = CompanyURL + WebUrlClass.api_GetPicklistNO;
            try {
                res = ut.OpenConnection(url, DOListActivity.this);
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
            progress.setVisibility(View.GONE);
          //  cf.DeleteAllRecord(db.TABLE_ITEM_PICKLIST);
         //   cf.DeleteAllRecord(db.TABLE_CARTAN_PICKLIST);

            picklistNOList.clear();
            if (s.contains("Pick_listHdrId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    for (int i=0;i<jResults.length();i++){
                        PicklistNO picklistNO=new PicklistNO();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        picklistNO.setPick_listHdrId(jsonObject.getString("Pick_listHdrId"));
                        picklistNO.setPicklistNo(jsonObject.getString("PicklistNo"));
                        if (Constants.type == Constants.Type.Alfa) {
                            picklistNO.setRemark("");
                        }else {
                            picklistNO.setRemark(jsonObject.getString("Remark"));
                            picklistNO.setQCStatus(jsonObject.getString("QCStatus"));
                        }
                        picklistNOList.add(picklistNO);
                    }

                    adapter.update(picklistNOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {
                Toast.makeText(DOListActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(DOListActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            progress.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isnet()) {
            try {
                progress.setVisibility(View.VISIBLE);
                new StartSession(DOListActivity.this, new CallbackInterface() {
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
            Toast.makeText(DOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }
}