package com.vritti.sales.activity;

import android.content.ContentValues;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.PriceListAdapter;
import com.vritti.sales.beans.PriceListBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;
import java.util.Locale;

public class PriceListActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText edtsearch;
    ListView listprice;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;
    static DatabaseHandlers dbhandler;

    PriceListAdapter pAdapter;
    ArrayList<PriceListBean> price_List;
    ArrayList<String> listString;

    String CallFrom = "", Key = "";
    public static final int PRICELISTDATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getPListcount() > 0){
            //new DownloadPriceListJSON().execute();
            getPriceListData();
        }else {
            if (isnet()) {
                new StartSession(PriceListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadPriceListJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        setListeners();
    }

    public void init(){
        parent = PriceListActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Sales Family");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        edtsearch = findViewById(R.id.edtsearch);
        listprice = findViewById(R.id.listprice);

        Intent intent = getIntent();
        CallFrom = intent.getStringExtra("CallFrom");
        Key = intent.getStringExtra("key");

        if(Key == "PriceListDesc"){
            edtsearch.setHint("Search price list description");
        }else {
            edtsearch.setHint("Search price list code");
        }
        
        ut = new Utility();
        tcf = new Tbuds_commonFunctions(PriceListActivity.this);
        cf = new CommonFunction(PriceListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(parent, dabasename);
        sql = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        price_List = new ArrayList<PriceListBean>();
        listString = new ArrayList<String>();

    }

    public void setListeners(){
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    pAdapter.filter((edtsearch).getText().toString().trim().toLowerCase(Locale.getDefault()),Key);
                    //getPriceListData();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listprice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pListDesc = price_List.get(position).getpListDesc();
                String pListCode = price_List.get(position).getpListCode();
                String pListHDRId = price_List.get(position).getpListHdrId();

                if(CallFrom.equalsIgnoreCase("SalesFamily")){
                    Intent intent = new Intent(PriceListActivity.this,SalesFamilyDtlActivity.class);
                    intent.putExtra("PListDesc",pListDesc);
                    intent.putExtra("PListCode",pListCode);
                    intent.putExtra("PListHDRID",pListHDRId);
                    setResult(PRICELISTDATA,intent);
                    finish();
                }else if(CallFrom.equalsIgnoreCase("PriceListTab")){
                    Intent intent = new Intent(PriceListActivity.this,PriceListTabActivity.class);
                    intent.putExtra("PListDesc",pListDesc);
                    intent.putExtra("PListCode",pListCode);
                    intent.putExtra("PListHDRID",pListHDRId);
                    setResult(PRICELISTDATA,intent);
                    finish();
                }
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

    class DownloadPriceListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getPricelistDesc+"?SearchText=";
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressBar.setVisibility(View.GONE);
            if (response != "" || response != null || response != "error") {

                try{
                    ContentValues values = new ContentValues();
                   // JSONArray jResults = new JSONArray(response);
                    String[] pdata = response.split(",");
                    sql.delete(dbhandler.TABLE_PRICELIST, null, null);

                    for (int i = 0; i < pdata.length; i++) {
                        //JSONObject jObj = jResults.getJSONObject(i);

                        String[] data = pdata[i].toString().split("@");

                        String desc = data[0].split("\"")[1].toString();
                        String code = data[2].split("\"")[0].toString();
                        String hdrId = data[1];

                        PriceListBean pBean = new PriceListBean();
                        pBean.setpListDesc(desc);
                        pBean.setpListHdrId(hdrId);
                        pBean.setpListCode(code);

                        tcf.insertPricelist(hdrId,code,desc);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                getPriceListData();
            }else {
                Toast.makeText(parent,"No data found",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void getPriceListData(){
        //call API pass it keyname
        if(price_List.size() > 0){
            price_List.clear();
        }

        String qry = "Select * from "+dbhandler.TABLE_PRICELIST;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                PriceListBean pBean = new PriceListBean();
                pBean.setpListDesc(c.getString(c.getColumnIndex("PListDesc")));
                pBean.setpListHdrId(c.getString(c.getColumnIndex("PListHDRId")));
                pBean.setpListCode(c.getString(c.getColumnIndex("PListCode")));

                price_List.add(pBean);
            }while (c.moveToNext());

            if(Key == "PriceListDesc"){
                pAdapter = new PriceListAdapter(parent,price_List,"PriceListDesc");
            }else {
                pAdapter = new PriceListAdapter(parent,price_List,"PriceListCode");
            }
            listprice.setAdapter(pAdapter);
            listprice.deferNotifyDataSetChanged();

        }
    }
}
