package com.vritti.crmlib.vcrm7;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AdvanceProvisionalListAdapter;
import com.vritti.crmlib.bean.AdvanceProvisionalData;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharvari on 05-Oct-17.
 */

public class AdvanceProvisionalReceiptDisplayActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;


    TextView txtadd;
    ListView list_material;
    ArrayList<AdvanceProvisionalData>advanceProvisionalDataArrayList;
    AdvanceProvisionalListAdapter userAdapter;
    SharedPreferences userpreferences;
    String  UserType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_advance_pro_list_display);
        getSupportActionBar().setTitle("Adv.Provisional Receipt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();


        if (isnet()) {
            new StartSession(AdvanceProvisionalReceiptDisplayActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadAdvanceProvisionalDetailsData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    private void init() {
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);

        txtadd= (TextView) findViewById(R.id.txtadd);
        list_material= (ListView) findViewById(R.id.list_material);
        advanceProvisionalDataArrayList=new ArrayList<>();

        txtadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AdvanceProvisionalReceiptDisplayActivity.this,AdvanceProvisionalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AdvanceProvisionalReceiptDisplayActivity.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    class DownloadAdvanceProvisionalDetailsData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM hh:mm");
        Date DOJDate = null, DOBDate = null;

         ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AdvanceProvisionalReceiptDisplayActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                // show popup
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_GetAdvance;
                System.out.println("AdvanceProList :"+url);
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
               /* response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);*/
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jsonadvanceprolistdata = jResults.getJSONObject(i);

                    AdvanceProvisionalData advanceProvisionalData = new AdvanceProvisionalData();
                    advanceProvisionalData.setCustomer_name(jsonadvanceprolistdata.getString("CustVendorName"));
                    advanceProvisionalData.setAmount(jsonadvanceprolistdata.getString("Amount"));
                    advanceProvisionalData.setBankName(jsonadvanceprolistdata.getString("BankName"));
                    advanceProvisionalData.setInstrumentNo(jsonadvanceprolistdata.getString("InstrumentNo"));
                    advanceProvisionalData.setTDSAmount(jsonadvanceprolistdata.getString("TDSAmount"));
                    advanceProvisionalData.setPaymentDepBank(jsonadvanceprolistdata.getString("PaymentDepBank"));
                    advanceProvisionalData.setDepositedDt(jsonadvanceprolistdata.getString("DepositedDt"));

                    advanceProvisionalDataArrayList.add(advanceProvisionalData);

                }



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            userAdapter = new AdvanceProvisionalListAdapter(AdvanceProvisionalReceiptDisplayActivity.this, advanceProvisionalDataArrayList);
            list_material.setAdapter(userAdapter);
            userAdapter.notifyDataSetChanged();
            progressDialog.dismiss();





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
}