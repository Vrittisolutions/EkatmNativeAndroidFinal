package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_PacketDisplay;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AdapterCreditApproval;
import com.vritti.vwb.Beans.CreditApproval;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CreditApprovalListActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private  RecyclerView recycler;
    private AdapterCreditApproval adapter;
    private List<CreditApproval> list;
    SQLiteDatabase sql;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;

    Button button_add;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_approval_list_lay);


        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(CreditApprovalListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(CreditApprovalListActivity.this);
        String dabasename = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(CreditApprovalListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(CreditApprovalListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        getSupportActionBar().setTitle(R.string.app_name_toolbar_Vwb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list=new ArrayList<>();
        adapter= new AdapterCreditApproval(list);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreditApprovalListActivity.this);
        recycler.setLayoutManager(layoutManager);
        recycler.setVisibility(View.GONE);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        button_add = (Button) findViewById(R.id.button_add);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditApprovalListActivity.this,CreditSanctionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        if (isnet()) {
            progressbar.setVisibility(View.VISIBLE);

            new StartSession(CreditApprovalListActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    DownloadCreditList creditList = new DownloadCreditList();
                    creditList.execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(CreditApprovalListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }



    public boolean isnet () {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    private class DownloadCreditList extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String url="";

        @Override
        protected String doInBackground(String... params) {

                 url = CompanyURL + WebUrlClass.api_GetDetails;

            try {
                res = Utility.OpenConnection(url, CreditApprovalListActivity.this);
                response = res.toString();
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
            progressbar.setVisibility(View.GONE);
            list.clear();
            if (s.contains("PKCreditId")) {
                try {
                    JSONArray jResults = new JSONArray(s);

                    for (int i = 0; i < jResults.length(); i++) {
                        recycler.setVisibility(View.VISIBLE);
                        CreditApproval creditApproval = new CreditApproval();
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        creditApproval.setCustVendorName(jsonObject.getString("CustVendorName"));
                        creditApproval.setApprovalStatus(jsonObject.getString("ApprovalStatus"));
                        creditApproval.setCreditApprovalAmt(jsonObject.getString("CreditApprovalAmt"));
                        creditApproval.setAddedDt(jsonObject.getString("AddedDt"));
                        creditApproval.setPKCreditId(jsonObject.getString("PKCreditId"));
                        list.add(creditApproval);

                    }

                    adapter.update(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (s.contains("[]")) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(CreditApprovalListActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(CreditApprovalListActivity.this, s, Toast.LENGTH_LONG).show();
            }

            progressbar.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
