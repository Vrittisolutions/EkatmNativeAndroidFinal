package com.vritti.AlfaLavaModule.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.adapter.Adp_DONumber;
import com.vritti.AlfaLavaModule.adapter.Adp_Packing_Report;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.AlfaLavaModule.bean.PrinterName;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PackingDONOReportActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_Packing_Report adapter;
    private ArrayList<PicklistNO> picklistNOList;
    private static RecyclerView recycler;
    ArrayList<PrinterName>printerNameArrayList;
    private Adapter_PrinterName adapterPrinterName;
    private String printerName;
    private AlertDialog b;
    private Spinner printername;
    private String Pack_OrdHdrId="",Pack_OrdNo="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);
        getSupportActionBar().setTitle("Delivery Note List");
        printerNameArrayList=new ArrayList<>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(PackingDONOReportActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(PackingDONOReportActivity.this);
        String dabasename = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PackingDONOReportActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(PackingDONOReportActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        picklistNOList = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PackingDONOReportActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_Packing_Report(picklistNOList);
        recycler.setAdapter(adapter);


        if (isnet()) {
            try {
             //   ProgressHUD.show(PackingDONOReportActivity.this, "Fetching data...", true, false);
                new StartSession(PackingDONOReportActivity.this, new CallbackInterface() {
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
            Toast.makeText(PackingDONOReportActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }






    }

    public void setdonumber(String picklistNo, String DONumber) {


        Pack_OrdNo=picklistNo;
        Pack_OrdHdrId=DONumber;
        if (isnet()) {
         //   ProgressHUD.show(PackingDONOReportActivity.this, "Sending data ...", true, false);
            new StartSession(PackingDONOReportActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadPrinterData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(PackingDONOReportActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }




    }

    class DownloadPrinterData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   ProgressHUD.show(PackingDONOReportActivity.this, "Sending data ...", true, false);

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetPrinterName;

            try {
                res = ut.OpenConnection(url, PackingDONOReportActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    printerNameArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        PrinterName userList = new PrinterName();
                        JSONObject jorder = jResults.getJSONObject(i);
                        userList.setPrinterName(jorder.getString("PrinterName"));
                        printerNameArrayList.add(userList);


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


        //    ProgressHUD.Destroy();

            if (response.contains("[]")) {
             //   ProgressHUD.Destroy();
                Toast.makeText(PackingDONOReportActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PackingDONOReportActivity.this);
                LayoutInflater inflater = PackingDONOReportActivity.this.getLayoutInflater();
                final View myView = inflater.inflate(R.layout.printername_lay, null);
                dialogBuilder.setView(myView);
                printername = (Spinner) myView .findViewById(R.id.spinner_printer);

                adapterPrinterName = new Adapter_PrinterName(PackingDONOReportActivity.this, printerNameArrayList);
                printername.setAdapter(adapterPrinterName);
                printername.setSelection(0,false);


                printername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        printerName=printerNameArrayList.get(position).getPrinterName();

                        if (isnet()) {
                         //   ProgressHUD.show(PackingDONOReportActivity.this, "Sending data ...", true, false);
                            new StartSession(PackingDONOReportActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new UpLoadSplitData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        } else {
                            Toast.makeText(PackingDONOReportActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                b = dialogBuilder.create();
                b.show();

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
            String url = CompanyURL + WebUrlClass.api_GetPackingList;
            try {
                res = ut.OpenConnection(url, PackingDONOReportActivity.this);
                response = res.toString().replaceAll("\\\\", "");
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
        //    ProgressHUD.Destroy();

            picklistNOList.clear();
            if (s.contains("Pack_OrdHdrId")) {
                try {
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
                Toast.makeText(PackingDONOReportActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(PackingDONOReportActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

         //   ProgressHUD.Destroy();
        }
    }

    private class UpLoadSplitData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_PrintPacketListHHD+"?Pack_OrdHdrId="+Pack_OrdHdrId+"&Pack_OrdNo="+Pack_OrdNo+"&PrinterName="+ URLEncoder.encode(printerName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url,PackingDONOReportActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ProgressHUD.Destroy();
            if (s.equalsIgnoreCase("ok")) {
                b.dismiss();
             //   ProgressHUD.Destroy();
                Toast.makeText(PackingDONOReportActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(PackingDONOReportActivity.this, "Print Successfully", Toast.LENGTH_LONG).show();
                b.dismiss();
               // ProgressHUD.Destroy();
            }
        }
    }

}