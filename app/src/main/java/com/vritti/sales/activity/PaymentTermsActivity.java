package com.vritti.sales.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.PaytermAdapter;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class PaymentTermsActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;

    ProgressBar mprogress;
    ImageView imgrefresh,imgsearch;
    ListView listpayterms;
    EditText edtsearch;

    Utility ut;
    static DatabaseHandlers db;
    CommonFunction cf;
    static Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    ArrayList<Customer> customerArrayList;
    String usertype = "";
    String username = "";

    public static final int PAYMENTTERMS = 1;
    PaytermAdapter payadapter;
    ArrayList<ConfigDropDownData> payTermsList;
    ArrayList<String> tempPayTermList;

    String TermsCode = "", TermsDescription = "", PymtSettTermMasterId = "", IsDeleted = "", CreditDays = "", BaseDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();


        if(cf.getPaytermsCount() > 0){
            getPaytermData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadPaytermJSON().execute();
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
        parent = PaymentTermsActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Payment Terms");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        imgrefresh = (ImageView)findViewById(R.id.imgrefresh);
        imgsearch = (ImageView)findViewById(R.id.imgsearch);
        listpayterms = (ListView)findViewById(R.id.listpayterms);
        edtsearch = (EditText)findViewById(R.id.edtsearch);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(PaymentTermsActivity.this);
        cf = new CommonFunction(PaymentTermsActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        payTermsList = new ArrayList<ConfigDropDownData>();
        tempPayTermList = new ArrayList<String>();
    }

    public void setListeners(){

        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    payadapter.filter((edtsearch)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        listpayterms.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                Intent intent = new Intent(PaymentTermsActivity.this, SalesChargeActivity.class);
                //intent.putExtra("CustVendorMasterId", AnyMartData.USER_ID);
                intent.putExtra("username", username);
                intent.putExtra("TermsCode", payTermsList.get(position).getTermsCode());
                intent.putExtra("TermsDescription", payTermsList.get(position).getTermsDescription());
                intent.putExtra("PymtSettTermMasterId", payTermsList.get(position).getPymtSettTermMasterId());
                intent.putExtra("CreditDays", payTermsList.get(position).getCreditDays());
                intent.putExtra("BaseDate", payTermsList.get(position).getBaseDate());
                setResult(PAYMENTTERMS,intent);
                finish();

            }
        });

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    //get payterms list
    class DownloadPaytermJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getPaymentTerms;

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length()-1);
                    ContentValues values = new ContentValues();
                    jResults = new JSONArray(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hideProgress();

            try{
                if(jResults != null){
                    parsePaytermJson(jResults);
                }else {
                }
            }catch (Exception e){

            }
        }
    }

    public void parsePaytermJson(JSONArray jResults){

        //    tcf.clearTable(parent, dbhandler.TABLE_ADD_ITEMS_COUNTERBILL);

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                TermsCode = jsonObject.getString("TermsCode");
                TermsDescription = jsonObject.getString("TermsDescription");
                PymtSettTermMasterId = jsonObject.getString("PymtSettTermMasterId");
                IsDeleted = jsonObject.getString("IsDeleted");
                CreditDays = jsonObject.getString("CreditDays");
                BaseDate = jsonObject.getString("BaseDate");

                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setTermsCode(TermsCode);
                dropdown.setTermsDescription(TermsDescription);
                dropdown.setPymtSettTermMasterId(PymtSettTermMasterId);
                dropdown.setIsDeleted(IsDeleted);
                dropdown.setCreditDays(CreditDays);
                dropdown.setBaseDate(BaseDate);

                cf.insertPaymentTerms(TermsCode,TermsDescription,PymtSettTermMasterId,IsDeleted,CreditDays,BaseDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getPaytermData();
    }

    public void getPaytermData(){
        if(payTermsList.size() > 0){
            payTermsList.clear();
            tempPayTermList.clear();
        }

        String payTerms = "Select * from "+db.TABLE_PAYMENT_TERMS;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setTermsCode(c.getString(c.getColumnIndex("TermsCode")));
                dropdown.setTermsDescription(c.getString(c.getColumnIndex("TermsDescription")));
                dropdown.setPymtSettTermMasterId(c.getString(c.getColumnIndex("PymtSettTermMasterId")));
                dropdown.setIsDeleted(c.getString(c.getColumnIndex("IsDeleted")));
                dropdown.setCreditDays(c.getString(c.getColumnIndex("CreditDays")));
                dropdown.setBaseDate(c.getString(c.getColumnIndex("BaseDate")));


                tempPayTermList.add(c.getString(c.getColumnIndex("TermsDescription")));
                payTermsList.add(dropdown);

            }while (c.moveToNext());

            payadapter = new PaytermAdapter(parent,payTermsList);
            listpayterms.setAdapter(payadapter);

        }else {

        }
    }

}
