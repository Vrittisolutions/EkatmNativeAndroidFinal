package com.vritti.sales.activity;

import android.annotation.SuppressLint;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.ShipmentEntryAdapter;
import com.vritti.sales.adapters.ShipmentEntryAdapter_RecyclerView;
import com.vritti.sales.beans.ShipmentEntryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.vwb.Beans.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShipmentEntryActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    LinearLayout lay_list_to_ship, lay_butns, lay_listdata;
    RecyclerView list_shipments;
    TextView txt_deladdr;
    Button btnsave, btncancel;

    List<String> lstReference = new ArrayList<>();
   // SearchableSpinner sp_consignee;
    TextView sp_consignee;
   // Spinner edt_ordtype, edt_sono;
    TextView edt_ordtype, edt_sono;
    List<String> lstOrdertype = new ArrayList<String>();
    List<String> lstSO = new ArrayList<>();
    ArrayList<Customer> customerArrayList;
    ArrayList<ShipmentEntryBean> SOList;
    ArrayList<ShipmentEntryBean> SODetailsList;
    ArrayList<ShipmentEntryBean> temp_SODtlList_toship;
    String usertype = "";
    String username = "";
    String OrderType, ConsigneeName, ConsigneeID, SONO_from_intent,SONO, SOHeaderID, ShipToMasterId,
            deliveryAddress, deliveryDate, DateToStr, prfDelFrmTime, prfDelToTime,CustomerMasterId;

    ShipmentEntryAdapter entryAdapter;
    ShipmentEntryAdapter_RecyclerView shipmentEntryAdapter_recyclerView;
    JSONObject jMain;
    String invoiceNo, OrderTypeMasterId;
    boolean isShipmentSave;
    Float baseAMT = 0.0F;
    Float totalTaxWithNet = 0.0F;
    Float netTOTAL = 0.0F;
    String TotalAmt="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_shipment_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Shipment Entry");
        toolbar.setTitle("Packaging Entry");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //AnyMartData.MerchantID = "fee4b450-174f-4bdc-84d2-ad6eca7d37fc";

        //sonumber selection
        GetSODetailsList();

        setListeners();

    }

    public void init(){
        parent = ShipmentEntryActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle(R.string.app_name_toolbar_sales);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress =  findViewById(R.id.toolbar_progress_Assgnwork);
        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);

        lay_listdata = findViewById(R.id.lay_listdata);
        lay_listdata.setVisibility(View.VISIBLE);
        lay_list_to_ship = findViewById(R.id.lay_list_to_ship);
        list_shipments = findViewById(R.id.list_shipments);
        txt_deladdr = findViewById(R.id.txt_deladdr);

       // sp_consignee = (SearchableSpinner) findViewById(R.id.sp_consignee);
        sp_consignee = (TextView) findViewById(R.id.txt_consignee);

        //edt_ordtype = (Spinner)findViewById(R.id.edt_ordtype);
        edt_ordtype = (TextView) findViewById(R.id.txt_ordtype);

        // edt_sono = (Spinner)findViewById(R.id.edt_sono);
        edt_sono = (TextView) findViewById(R.id.txt_sono);

        btnsave = (Button)findViewById(R.id.btnsaveshipment);
      //  btncancel = (Button)findViewById(R.id.btncancel);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ShipmentEntryActivity.this);
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
        // mprogress=findViewById(R.id.toolbar_progress_App_bar);

        customerArrayList=new ArrayList<>();
        SOList=new ArrayList<>();
        SODetailsList=new ArrayList<>();
        temp_SODtlList_toship = new ArrayList<>();

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        Intent intent = getIntent();
        //OrderType = intent.getStringExtra(AnyMartData.Order_Type);
        SONO_from_intent = intent.getStringExtra("SONO").trim();
        ConsigneeName = intent.getStringExtra("Consignee").trim();
        SOHeaderID = intent.getStringExtra("SOHeaderID");
        ShipToMasterId = intent.getStringExtra("ShipToMasterId");
        deliveryAddress = intent.getStringExtra("DeliveryAddress");
        deliveryDate = intent.getStringExtra("DeliveryDate");
        ConsigneeID = intent.getStringExtra("ConsigneeID");
        DateToStr = Convertdate(deliveryDate);
        prfDelFrmTime = intent.getStringExtra("prfDelFrmTime");
        prfDelToTime = intent.getStringExtra("prfDelToTime");
        CustomerMasterId = intent.getStringExtra("CustomerMasterId");
        TotalAmt = intent.getStringExtra("TotalAmt");
        Log.d("Amount",TotalAmt);

        //edt_ordtype.setText(OrderType);
        edt_sono.setText(SONO_from_intent);
        sp_consignee.setText(ConsigneeName);
        edt_ordtype.setText("");
        txt_deladdr.setText(deliveryAddress);
    }

    public void setListeners(){

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call method to hit API and then call intent
                //temp_SODtlList_toship = ShipmentEntryAdapter.getAllSelectedSOData();
                temp_SODtlList_toship = ShipmentEntryAdapter_RecyclerView.getAllSelectedSOData();
                /*
                for(ShipmentEntryBean shipmentEntryBean : SODetailsList){
                    if(shipmentEntryBean.isChecked()){
                        temp_SODtlList_toship.add(shipmentEntryBean);
                    }
                }*/
                if(temp_SODtlList_toship.isEmpty()){
                    Toast.makeText(parent,"Please select item for packaging",Toast.LENGTH_SHORT).show();
                }else {
                    PostShipmentMethod();
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

    public void GetOrderTypeData(){
        //call API to get order type
        new DownloadOrderTypeJSON().execute();

    }

    public void GetCustomerData(){
        new DownloadReferenceJSON().execute();
    }

    public int getOrderTypecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_OrderType;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private void displayOrderType() {

        lstOrdertype.clear();
        String countQuery = "SELECT  Description FROM "
                + db.TABLE_OrderType;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstOrdertype.add(cursor.getString(cursor.getColumnIndex("Description")));
            } while (cursor.moveToNext());
        }

      /* MySpinnerAdapter adapter = new MySpinnerAdapter(parent,
                R.layout.crm_custom_spinner_txt, lstOrdertype);
        edt_ordtype.setAdapter(adapter);

        for(int i=0; i < lstOrdertype.size();i++){

            if(OrderType.equalsIgnoreCase(lstOrdertype.get(i).toString())){
               edt_ordtype.setSelection(i);
            }else {

            }
        }*/
    }

    public void setChackPos(int position, boolean b) {
        SODetailsList.get(position).setChecked(b);
        //shipmentEntryAdapter_recyclerView.notifyDataSetChanged();

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

    class DownloadOrderTypeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_OrderType, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_OrderType, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_OrderType, null, values);
                    }
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
            if (response.contains("")) {

            }
            displayOrderType();
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private  class MySpinnerAdapter_customer extends ArrayAdapter<Customer> {
        // Initialise custom font, for example:
        ArrayList<Customer> customerArrayList = new ArrayList<>();


        public MySpinnerAdapter_customer(Context context, int textViewResourceId, ArrayList<Customer> customerArrayList) {
            super(context, textViewResourceId, customerArrayList);
            this.customerArrayList=customerArrayList;
        }
        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vwb_spinner_text, null);
            TextView textView = (TextView) v.findViewById(R.id.txt);
            textView.setText(customerArrayList.get(position).getCustomer_name());

            return v;
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

    class DownloadReferenceJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_Reference +
                        "?LeadWise=C";

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
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
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){
                    for(int i=0; i<=jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String CustVendorName = jsonObject.getString("CustVendorName");
                            String CustVendorMasterId = jsonObject.getString("CustVendorMasterId");

                            Customer cust = new Customer();
                            cust.setCustomer_name(CustVendorName);
                            cust.setClient_id(CustVendorMasterId);

                            customerArrayList.add(cust);
                            lstReference.add(CustVendorName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {

                }
            }catch (Exception e){

            }

            /*MySpinnerAdapter strlst = new MySpinnerAdapter(ShipmentEntryActivity.this,
                    R.layout.crm_custom_spinner_txt, lstReference);
            sp_consignee.setAdapter(strlst);
            //  customDept.notifyDataSetChanged();
            sp_consignee.setSelection(0);

            for(int i=0; i < lstReference.size();i++){

                String cust = lstReference.get(i).toString();
                String cust1 = customerArrayList.get(i).getCustomer_name();
                ConsigneeID = customerArrayList.get(i).getClient_id();

                if(ConsigneeName.equalsIgnoreCase(cust)){
                    sp_consignee.setSelection(i);

                }else {

                }
            }

            GetSOList();*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, ShipmentAndInvoicing.class);
      //  i.putExtra("intentFrom", "ShipmentEntry");
        startActivity(i);
        finish();
    }

    public void GetSOList(){

        new DownloadSOList().execute();
    }

    class DownloadSOList extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getSOList_consignee + "?Consignee="+ShipToMasterId;

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
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
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){

                    for(int i=0; i<=jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            SOHeaderID = jsonObject.getString("Soheaderid");
                            SONO = jsonObject.getString("Sono");

                            ShipmentEntryBean entryBean = new ShipmentEntryBean();
                            entryBean.setSOno(SONO);
                            entryBean.setSOHeaderID(SOHeaderID);

                            SOList.add(entryBean);
                            lstSO.add(SONO);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

            /*MySpinnerAdapter strlst = new MySpinnerAdapter(ShipmentEntryActivity.this,
                    R.layout.crm_custom_spinner_txt, lstSO);
            edt_sono.setAdapter(strlst);
            //  customDept.notifyDataSetChanged();
            edt_sono.setSelection(0);

            for(int i=0; i < lstSO.size();i++){

                String sono = SOList.get(i).getSOno();

                if(SONO_from_intent.equalsIgnoreCase(lstSO.get(i).toString())){
                    edt_sono.setSelection(i);
                }else {
                }
            }

            GetSODetailsList();*/
        }
    }

    public void GetSODetailsList(){

        new StartSession_tbuds(parent, new CallbackInterface() {

            @Override
            public void callMethod() {

                new DownloadSODetailsList().execute();
            }

            @Override
            public void callfailMethod(String s) {

            }
        });
    }

    class DownloadSODetailsList extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getItemsList_SO + "?SONO="+SOHeaderID;
                //pass SOHeaderId as SOID in it

                res = Utility.OpenConnection_ekatm(url,ShipmentEntryActivity.this);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
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
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){
                    parseXml(jResults);

                }else {
                    Toast.makeText(parent,""+parent.getResources().getString(R.string.nodatafound), Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void PostShipmentMethod(){
        showProgress();

        btnsave.setAlpha((float)0.5);
        btnsave.setText(getResources().getString(R.string.sndng));

        String so_no = "", soheaderID = "", SOScheduleId, rate,TaxAmtdtl, TotWithtaxdtl,DiscAmountDtl, TotAmntWithDisc;

        float BaseAmt = 0.0F, TotTaxWithNet = 0.0F, NetTotal = 0.0F, TotalTaxAmt = 0.0F, Amt = 0.0F, TotDisc = 0.0F, DiscPC = 0.0F,
                ShipmentQty = 0.0F;

        //temp_SODtlList_toship = ShipmentEntryAdapter.getAllSelectedSOData();
        temp_SODtlList_toship = ShipmentEntryAdapter_RecyclerView.getAllSelectedSOData();

        JSONArray jsonArray = new JSONArray();
        for(int i=0; i< temp_SODtlList_toship.size();i++){
            //array of data
            SOScheduleId = temp_SODtlList_toship.get(i).getSOScheduleId();
            ShipmentQty = temp_SODtlList_toship.get(i).getEdtQty(); //edttext qty
            Amt = temp_SODtlList_toship.get(i).getSubtotal_Amt();
            rate = temp_SODtlList_toship.get(i).getRate();
            TaxAmtdtl = "0";
            TotWithtaxdtl = "0";
            DiscAmountDtl = "0";
            TotAmntWithDisc = "0";

            //fix data
            so_no = temp_SODtlList_toship.get(i).getSOno();
            soheaderID = temp_SODtlList_toship.get(i).getSOHeaderID();
            BaseAmt = BaseAmt + temp_SODtlList_toship.get(i).getSubtotal_Amt();    // BaseAmt = Amt1 +Amt2 +Amt3;
            TotalTaxAmt = 0;
            TotTaxWithNet = BaseAmt + TotalTaxAmt; //Amt1 +Amt2 +Amt3
            TotDisc = 0;
            NetTotal = TotTaxWithNet - TotDisc; //total //Amt1 +Amt2 +Amt3
            DiscPC = 0;

            JSONObject jsonObject = new JSONObject();

            try{
                jsonObject.put("SOScheduleId", SOScheduleId);
                jsonObject.put("ShipmentQty", ShipmentQty);
                jsonObject.put("Amt", String.valueOf(Amt));
                jsonObject.put("rate", rate);
                jsonObject.put("TaxAmtdtl", TaxAmtdtl);
                jsonObject.put("TotWithtaxdtl", TotWithtaxdtl);
                jsonObject.put("DiscAmountDtl", DiscAmountDtl);
                jsonObject.put("TotAmntWithDisc", TotAmntWithDisc);

                jsonArray.put(jsonObject);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        baseAMT = BaseAmt;
        totalTaxWithNet = TotTaxWithNet;
        netTOTAL = NetTotal;

        jMain = new JSONObject();

        try {
            jMain.put("PIckedSchld",jsonArray);
            jMain.put("SONO",SOHeaderID);       //pass SOheaderId as SONO
            jMain.put("BaseAmt",String.valueOf(BaseAmt));
            jMain.put("TotalTaxAmt",String.valueOf(TotalTaxAmt));
            jMain.put("TotTaxWithNet",TotalAmt);
           // jMain.put("TotTaxWithNet",String.valueOf(TotTaxWithNet));
            jMain.put("TotDisc",String.valueOf(TotDisc));
           // jMain.put("NetTotal",String.valueOf(NetTotal));
            jMain.put("NetTotal",TotalAmt);
            jMain.put("DiscPc",String.valueOf(DiscPC));
            jMain.put("AppEnvMasterId",EnvMasterId);
            jMain.put("PlantId",PlantMasterId);
            jMain.put("CustomerMasterId",CustomerMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(isnet()){
            new PostShipmentAPI().execute();  //call API to shipment
        }

    }

    class PostShipmentAPI extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;
        String so_no = "", ItemCode = "", ItemDesc = "", AddedDt = "", scheduledate = "", ScheduleQty = "", ShipmentQty = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            try {
                //String url = CompanyURL + WebUrlClass.api_postShipment + "?OBJ="+jMain;
                String url = CompanyURL + WebUrlClass.api_postShipment;

                res = Utility.OpenPostConnection(url,String.valueOf(jMain),ShipmentEntryActivity.this);

                if (res != null) {
                    res = res.toString().replaceAll("\\\\", "");
                    response = res.toString().replaceAll("^\"+ \"+$","");
                    response = response.substring(1, response.length()-1);    //to remove first and last character from string
                }

            } catch (Exception e) {
                e.printStackTrace();
                btnsave.setAlpha((float)1);
                btnsave.setText(getResources().getString(R.string.procpckg));
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            btnsave.setAlpha((float)1);
            btnsave.setText(getResources().getString(R.string.procpckg));

            try{
                if(response != null){

                    if(response.contains("false")){
                        isShipmentSave = false;
                        //Toast.makeText(parent,"Shipment not saved"+ invoiceNo,Toast.LENGTH_SHORT).show();
                        Toast.makeText(parent,""+getResources().getString(R.string.pckgsav),Toast.LENGTH_SHORT).show();
                    }else {
                        invoiceNo = response.trim();
                        isShipmentSave = true;
                        //shipment process
                        //Toast.makeText(parent,"Shipment saved successfully, your invoice no is  "+ invoiceNo, Toast.LENGTH_SHORT).show();
                        Toast.makeText(parent,""+getResources().getString(R.string.pckgsavesucc)+" "+ invoiceNo, Toast.LENGTH_SHORT).show();

                        //store shipment details
                        insertShipment(invoiceNo);

                        //test purpose
                        Intent intent = new Intent(ShipmentEntryActivity.this,DeliveryAgentSelection.class);
                        intent.putExtra("IntentFrom","ShipmentEntry");
                        intent.putExtra("InvoiceNo",invoiceNo);
                        intent.putExtra("DeliveryDate",DateToStr);
                        intent.putExtra("ConsigneeName",ConsigneeName);
                        intent.putExtra("DelvAddress",deliveryAddress);
                        intent.putExtra("SONO_frmIntent",SONO_from_intent);
                        intent.putExtra("OrderType", OrderType);
                        intent.putExtra("ShipToMasterId",ShipToMasterId);
                        intent.putExtra("SODate",deliveryDate);
                        //intent.putExtra("SODate",DateToStr);
                        intent.putExtra("OrderTypeMasterId",OrderTypeMasterId);
                        intent.putExtra("prfDelFrmTime",prfDelFrmTime);
                        intent.putExtra("prfDelToTime",prfDelToTime);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    //      finish();      //test purpose

                }else {
                    isShipmentSave = false;
                    //Toast.makeText(parent,"Shipment not saved"+ invoiceNo,Toast.LENGTH_SHORT).show();
                    Toast.makeText(parent,""+getResources().getString(R.string.pckgsav)+" "+ invoiceNo,Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void parseXml(JSONArray jResults){
        SODetailsList.clear();

        String SeqNo,so_no, SOScheduleId, SODetailId, CustOrderPONo, SOHeaderStatus, ScheduleDate, SailingDate,TaxClassMasterId, Qty, Rate,
                MOQty,ActualMOQty,ShipmentQty, ItemProcessId, Plant, WareHouse, LineAmt, LineTaxes, LineTotal, DiscPC, DiscAmount, PlantId,
                WareHouseId, ItemMasterId, UOMCode,ItemCode,ItemDesc, ReqQty, SalesUnit, PurUnit, WareHouseId1, LocationMasterId, StockUnit,
                UOMMasterId, OrderTypeMasterId = "",PackOfQty="0",Content="",ContentUOM="",SellingUOM="",Brand="";

        for(int i=0; i<=jResults.length();i++){
            try {
                JSONObject jsonObject = jResults.getJSONObject(i);
                SeqNo = jsonObject.getString("SeqNo");
                so_no = jsonObject.getString("SONo");
                SOScheduleId = jsonObject.getString("SOScheduleId");
                SODetailId = jsonObject.getString("SODetailId");
                CustOrderPONo = jsonObject.getString("CustOrderPONo");
                SOHeaderStatus = jsonObject.getString("SOHeaderStatus");
                ScheduleDate = jsonObject.getString("ScheduleDate");
                SailingDate = jsonObject.getString("SailingDate");
                TaxClassMasterId = jsonObject.getString("TaxClassMasterId");
                Qty = jsonObject.getString("Qty");
                Rate = jsonObject.getString("Rate");
                MOQty = jsonObject.getString("MOQty");
                ActualMOQty = jsonObject.getString("ActualMOQty");
                ShipmentQty = jsonObject.getString("ShipmentQty");
                ItemProcessId = jsonObject.getString("ItemProcessId");
                Plant = jsonObject.getString("Plant");
                WareHouse = jsonObject.getString("WareHouse");
                LineAmt = jsonObject.getString("LineAmt");
                LineTaxes = jsonObject.getString("LineTaxes");
                LineTotal = jsonObject.getString("LineTotal");
                DiscPC = jsonObject.getString("DiscPC");
                DiscAmount = jsonObject.getString("DiscAmount");
                PlantId = jsonObject.getString("PlantId");
                WareHouseId = jsonObject.getString("WareHouseId");
                ItemMasterId = jsonObject.getString("ItemMasterId");
                ItemCode = jsonObject.getString("ItemCode");
                ItemDesc = jsonObject.getString("ItemDesc");
                UOMCode = jsonObject.getString("UOMCode");
                ReqQty = jsonObject.getString("ReqQty");
                SalesUnit = jsonObject.getString("SalesUnit");
                SellingUOM = SalesUnit;
                PurUnit = jsonObject.getString("PurUnit");
                ContentUOM = PurUnit;
                WareHouseId1 = jsonObject.getString("WareHouseId1");
                LocationMasterId = jsonObject.getString("LocationMasterId");
                StockUnit = jsonObject.getString("StockUnit");
                UOMMasterId = jsonObject.getString("UOMMasterId");
                OrderTypeMasterId = jsonObject.getString("OrderTypeMasterId");

                try{
                    PackOfQty = jsonObject.getString("PackOfQty");
                    Brand = jsonObject.getString("Brand");
                    Content = jsonObject.getString("Content");
                }catch (Exception e){
                    e.printStackTrace();
                }

                ShipmentEntryBean entryBean = new ShipmentEntryBean();
                entryBean.setSeqNo(SeqNo);
                entryBean.setSOno(so_no);
                entryBean.setSOHeaderID(SOHeaderID);
                entryBean.setSOScheduleId(SOScheduleId);
                entryBean.setSODetailId(SODetailId);
                entryBean.setCustOrderPONo(CustOrderPONo);
                entryBean.setSOHeaderStatus(SOHeaderStatus);
                entryBean.setScheduleDate(ScheduleDate);
                entryBean.setSailingDate(SailingDate);
                entryBean.setTaxClassMasterId(TaxClassMasterId);
                entryBean.setQty(Qty);
                entryBean.setRate(Rate);
                entryBean.setMOQty(MOQty);
                entryBean.setActualMOQty(ActualMOQty);
                entryBean.setShipmentQty(ShipmentQty);
                entryBean.setItemProcessId(ItemProcessId);
                entryBean.setPlant(Plant);
                entryBean.setWareHouse(WareHouse);
                entryBean.setLineAmt(LineAmt);
                entryBean.setLineTaxes(LineTaxes);
                entryBean.setLineTotal(LineTotal);
                entryBean.setDiscPC(DiscPC);
                entryBean.setDiscAmount(DiscAmount);
                entryBean.setPlantId(PlantId);
                entryBean.setWareHouseId(WareHouseId);
                entryBean.setItemMasterId(ItemMasterId);
                entryBean.setItemCode(ItemCode);
                entryBean.setItemDesc(ItemDesc);
                entryBean.setUOMCode(UOMCode);
                entryBean.setReqQty(ReqQty);
                entryBean.setSalesUnit(SalesUnit);
                entryBean.setPurUnit(PurUnit);
                entryBean.setWareHouseId1(WareHouseId1);
                entryBean.setLocationMasterId(LocationMasterId);
                entryBean.setStockUnit(StockUnit);
                entryBean.setUOMMasterId(UOMMasterId);
                entryBean.setOrderTypeMasterId(OrderTypeMasterId);
                entryBean.setPackOfQty(PackOfQty);
                entryBean.setBrand(Brand);
                entryBean.setContent(Content);

                SODetailsList.add(entryBean);
                //lstSO.add(SONO);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // entryAdapter = new ShipmentEntryAdapter(parent, SODetailsList);
        //list_shipments.setAdapter(entryAdapter);

        String query = "SELECT Description FROM "+ DatabaseHandlers.TABLE_OrderType+ " WHERE OrderTypeMasterId='"+OrderTypeMasterId+"'";

        Cursor cotype = sql.rawQuery(query,null);
        if(cotype.getCount()>0){
            cotype.moveToFirst();
            //  AnyMartData.Order_Type = cotype.getString(cotype.getColumnIndex("Description"));
        }else {

        }

        shipmentEntryAdapter_recyclerView =  new ShipmentEntryAdapter_RecyclerView(parent, SODetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_shipments.setLayoutManager(mLayoutManager);
        list_shipments.setAdapter(shipmentEntryAdapter_recyclerView);

    }

    public String Convertdate(String date){

        if(date.contains("/Date")){
            date = date.replace("/Date(","");
            date = date.replace(")/","");
            Long timestamp = Long.valueOf(date);
            Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
            cal1.setTimeInMillis(timestamp);
            String date1 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal1).toString();

            SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date d1 = null;
            try {
                d1 = format.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateToStr = Format.format(d1);
        }else {
            if(!date.equalsIgnoreCase("")){
                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = null;
                try {
                    d1 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateToStr = Format.format(d1);
                System.out.println(DateToStr);
            }else {
                DateToStr = "";
            }
        }

        return DateToStr;
    }

    public void insertShipment(String invoiceNo){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY, hh:mm a");
        String addedDt = sdf.format(c.getTime());

        for(int i=0; i< temp_SODtlList_toship.size(); i++){

            OrderTypeMasterId = temp_SODtlList_toship.get(i).getOrderTypeMasterId();

            tcf.insertShipment(invoiceNo,
                    temp_SODtlList_toship.get(i).getSOno(),
                    temp_SODtlList_toship.get(i).getSOHeaderID(),
                    temp_SODtlList_toship.get(i).getOrderTypeMasterId(),
                    temp_SODtlList_toship.get(i).getSOScheduleId(),
                    temp_SODtlList_toship.get(i).getShipmentQty(),
                    temp_SODtlList_toship.get(i).getLineAmt(),
                    temp_SODtlList_toship.get(i).getRate(),
                    "0","0","0","0",
                    String.valueOf(baseAMT),"0",
                    String.valueOf(totalTaxWithNet),"0",
                    String.valueOf(netTOTAL),"0",addedDt, deliveryDate, ConsigneeName, ConsigneeID,
                    ShipToMasterId, deliveryAddress, "N");

            //  Toast.makeText(parent,"Shipment data inserted successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
