package com.vritti.sales.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.TransitShipmentsAdapter;
import com.vritti.sales.beans.DeliveryAgentBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TransitShipmentActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ListView list_transit_shipments;
    ProgressBar mprogress;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "",  usertype = "", username = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    ArrayList<DeliveryAgentBean> transitsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_transit_shipment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Transit Shipments");
        toolbar.setTitle("Transit Packaging");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //if merchantid empty callthis api then call pendingdeliveries
        if(AnyMartData.MerchantID.equalsIgnoreCase("")){
            getMerchantId();
        }else {
            //get data from server
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTransitShipmentJSON().execute();
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
        parent = TransitShipmentActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle(R.string.app_name_toolbar_sales);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        list_transit_shipments = (ListView)findViewById(R.id.list_transit_shipments);
        mprogress = (ProgressBar)findViewById(R.id.toolbar_progress_Assgnwork);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(TransitShipmentActivity.this);
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
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
        AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.CITY = sharedpreferences.getString("City","");
        AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.STATE = sharedpreferences.getString("State","");
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        transitsList = new ArrayList<DeliveryAgentBean>();

    }

    public void setListeners(){

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

    class DownloadTransitShipmentJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
            // Toast.makeText(parent,"Searching for transit shipments...", Toast.LENGTH_SHORT).show();
            Toast.makeText(parent,""+getResources().getString(R.string.serchtransit), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getTransitShipments+
                        //"?MerchantId=" + AnyMartData.MerchantID;
                        "?MerchantId=" + "";

                res = Utility.OpenconnectionOrferbilling(url,parent);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = res.toString().replaceAll("^\"+ \"+$","");
                    //response = response.substring(1, response.length()-1);
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
            mprogress.setVisibility(View.GONE);

            try{
                if(jResults != null){
                    parsetransitShipmentsJson(jResults);
                }else {
                    //Toast.makeText(parent,"No transit shipments found.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(parent,""+getResources().getString(R.string.notransit), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void parsetransitShipmentsJson(JSONArray jResults){
        transitsList.clear();

        String ActivityId = "", ActivityName = "", Status = "", AgentName = "", UserLogInId = "", UserMasterId = "", AgentMobNo = "",
                InvoiceNo = "", InvoiceDt = "", SODate = "", ConsigneeName = "", Address = "", PrefDelFrmTime = "", PrefDelToTime = "",
                Mobile = "";

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                ActivityId = jsonObject.getString("ActivityId");
                ActivityName = jsonObject.getString("ActivityName");
                Status = jsonObject.getString("Status");
                AgentName = jsonObject.getString("AgentName");
                UserLogInId = jsonObject.getString("UserLogInId");
                UserMasterId = jsonObject.getString("UserMasterId");
                AgentMobNo = jsonObject.getString("AgentMobNo");

                InvoiceNo = jsonObject.getString("InvoiceNo");
                InvoiceDt = jsonObject.getString("InvoiceDt");
                SODate = jsonObject.getString("SODate");
                ConsigneeName = jsonObject.getString("ConsigneeName");
                Address = jsonObject.getString("Address");
                PrefDelFrmTime = jsonObject.getString("PrefDelFrmTime");
                PrefDelToTime = jsonObject.getString("PrefDelToTime");
                Mobile = jsonObject.getString("Mobile");

                String[] idate = InvoiceDt.split("T");
                InvoiceDt = idate[0] + " " + idate[1];

                String[] sdate = SODate.split("T");
                SODate =sdate[0] + " " + sdate[1];

                DeliveryAgentBean sbean = new DeliveryAgentBean();
                sbean.setActivityId(ActivityId);
                sbean.setActivityName(ActivityName);
                sbean.setStatus(Status);
                sbean.setAgentName(AgentName);
                sbean.setUserLogInId(UserLogInId);
                sbean.setUserMasterId(UserMasterId);
                sbean.setAgentMobNo(AgentMobNo);
                sbean.setInvoiceNo(InvoiceNo);
                sbean.setInvoiceDt(InvoiceDt);
                sbean.setSODate(SODate);
                sbean.setConsigneeName(ConsigneeName);
                sbean.setAddress(Address);
                sbean.setPrefDelFrmTime(PrefDelFrmTime);
                sbean.setPrefDelToTime(PrefDelToTime);
                sbean.setMobile(Mobile);

                transitsList.add(sbean);

                Collections.sort(transitsList, new Comparator<DeliveryAgentBean>() {
                    @Override
                    public int compare(DeliveryAgentBean lhs, DeliveryAgentBean rhs) {
                        return rhs.getInvoiceNo().compareTo(lhs.getInvoiceNo());
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        list_transit_shipments.setAdapter(new TransitShipmentsAdapter(this, transitsList));
    }

    public void getMerchantId(){
        new DownloadReferenceJSON().execute();
    }

    class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Reference + "?LeadWise=V";

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\r\\n","");
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
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

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){
                    for(int i=0; i<=jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            AnyMartData.MerchantName = jsonObject.getString("CustVendorName");
                            AnyMartData.MerchantID = jsonObject.getString("CustVendorMasterId");
                            String Mobile = jsonObject.getString("Mobile");
                            String Email = jsonObject.getString("Email");
                            String Address = jsonObject.getString("Address");

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("MerchantID",  AnyMartData.MerchantID);
                            editor.putString("MerchantName",  AnyMartData.MerchantName);
                            editor.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                }

                if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new DownloadTransitShipmentJSON().execute();
                        }
                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }

            }catch (Exception e){

            }
        }
    }

}
