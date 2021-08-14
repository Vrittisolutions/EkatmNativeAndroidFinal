package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.SOHistoryListAdapter;
import com.vritti.sales.beans.SaleItemBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;

public class SOHistoryActivity extends AppCompatActivity {
    private Context parent;
    ProgressBar progressBar;
    Toolbar toolbar;
    ListView listhistory;
    EditText edtSono;
    ImageView imgSearch;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<SaleItemBean> listSO;
    SOHistoryListAdapter soAdapter;
    String SOHeaderId = "", SONo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_pdf);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();
        
        getDataFromDatabase();

        setListeners();
    }

    public void init(){
        parent = SOHistoryActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("SO History");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ut = new Utility();
        cf = new CommonFunction(SOHistoryActivity.this);
        tcf = new Tbuds_commonFunctions(SOHistoryActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SOHistoryActivity.this);
        String dabasename = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SOHistoryActivity.this, dabasename);
        CompanyURL = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SOHistoryActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        
        listhistory = findViewById(R.id.listhistory);
        imgSearch = findViewById(R.id.imgsearch);
        edtSono = findViewById(R.id.edtsono);

        listSO = new ArrayList<SaleItemBean>();

    }

    public void setListeners(){
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSono.getText().toString() != "" || edtSono.getText().toString() != null){

                }
            }
        });

        listhistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SONo = listSO.get(position).getSoNo();
                SOHeaderId = listSO.get(position).getSOHeaderId();

                //call API to check SoStatus
                if (isnet()) {
                    new StartSession(SOHistoryActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                          new CheckSOStatusAPI().execute();
                        }
                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        });
    }
    
    public void getDataFromDatabase(){
        listSO.clear();
        String qry = "Select SOHeaderId,SONo,SODate,ConsigneeName,NetAmt from "+db.TABLE_SOHistory;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                SaleItemBean sbean = new SaleItemBean();
                sbean.setSOHeaderId(c.getString(c.getColumnIndex("SOHeaderId")));
                sbean.setSoNo(c.getString(c.getColumnIndex("SONo")));
                sbean.setSoDate(c.getString(c.getColumnIndex("SODate")));
                sbean.setCustName(c.getString(c.getColumnIndex("ConsigneeName")));
                sbean.setAmt(c.getString(c.getColumnIndex("NetAmt")));

                listSO.add(sbean);
            }while (c.moveToNext());

            soAdapter = new SOHistoryListAdapter(parent,listSO);
            listhistory.setAdapter(soAdapter);
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

    class CheckSOStatusAPI extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
            showProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_checkSOStatus + "?CallId="+SOHeaderId;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                }else {
                    response = "NoData";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            hideProgress();
            if (response.equalsIgnoreCase("Exists")) {
                Toast.makeText(parent,"You can not edit this SO , As This SO is in Under Approval State.",Toast.LENGTH_SHORT).show();
            }else if(response.equalsIgnoreCase("Shipment")){
                Toast.makeText(parent,"Shipment is done..You can not edit this SO !",Toast.LENGTH_SHORT).show();
            }else if(response.equalsIgnoreCase("Under Amend")){
                Toast.makeText(parent,"Under Amendment..You can not edit this SO !",Toast.LENGTH_SHORT).show();
            }else if(response.equalsIgnoreCase("NoData")){
                Toast.makeText(parent,"Incorrect SO Number",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(parent,"SO editable",Toast.LENGTH_SHORT).show();

                //go to NewSalesOrderBooking screen
                //call API to get SOheader data and set values to views
                Intent intent = new Intent(SOHistoryActivity.this, NewSalesOrderBooking.class);
                intent.putExtra("Mode","Edit");
                intent.putExtra("SoHeaderID",SOHeaderId);
                startActivity(intent);
            }
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromDatabase();
    }

}
