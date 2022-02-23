package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.SOApprDetailsAdapter;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SOApproveDetailActivity extends AppCompatActivity {
    private Context parent;
    ListView listitems;
    TextView delchrge;
    static TextView total_amount,TotTaxAmt,txt_tot_amount;
    ProgressBar progressBar;
    static ProgressHUD progress;
    SOApprDetailsAdapter dtlAdapter;
    static ArrayList<OrderHistoryBean> listSO;
    Button btnapprove,btndisapprove;
    JSONObject jsonMain;
    JSONArray jsonArray;

    SharedPreferences sharedpreferences;
    public String restoredText, restoredusername, restoredownername, usertype, domainname;
    String CustVendorMasterId, CustomerID, consigneeName, consigneeId,SONO;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private static DatabaseHandlers db;
    static SQLiteDatabase sql_db;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "", OrderType = "",DeliveryMode="",
            MerchName="";

    String soHeaderid = "";
    static float amt = 0, totOrdValue = 0;

    ArrayList<OrderHistoryBean> temp_SODtlList_toship;
    Dialog dialog;
    String reason="";
    static float deliveryCharge = 0,FreeAboveAmt = 0, Distance = 0,ValPerKm = 0,MaxFreeDelDist=0;
    LinearLayout len_tax,len_total;
    String tax="",TotalAmount="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soapprove_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales Order Details");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        //get data from bundle of selected SO
        getDataFromDatabase();

        if(FreeAboveAmt == 0 || FreeAboveAmt == 0.0 || FreeAboveAmt == 0.00){
            delchrge.setVisibility(View.GONE);
        }else {
            deliveryCharge = getDelChargeVal();

            if(DeliveryMode.equalsIgnoreCase("Door Step")){
                delchrge.setVisibility(View.VISIBLE);
                delchrge.setText(getResources().getString(R.string.incl)+" "+String.format("%.2f",deliveryCharge)+" ₹");
            }else {
                delchrge.setVisibility(View.GONE);
            }
        }

        setListeners();

    }

    public void init(){
        parent = SOApproveDetailActivity.this;

        listitems = findViewById(R.id.listitems);
        btnapprove = findViewById(R.id.btnapprove);
        btndisapprove = findViewById(R.id.btndisapprove);
        total_amount = findViewById(R.id.total_amount);
        TotTaxAmt = findViewById(R.id.TotTaxAmt);
        txt_tot_amount = findViewById(R.id.txt_tot_amount);
        //progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        progressBar = findViewById(R.id.tbuds_tbar_prgrs_bar);
        delchrge = findViewById(R.id.delchrge);
        len_tax = findViewById(R.id.len_tax);
        len_total = findViewById(R.id.len_total);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        restoredText = sharedpreferences.getString("Mobileno", null);
        //restoredownername = sharedpreferences.getString("OwnerName", null);
        restoredusername = sharedpreferences.getString("username", null);
        usertype = sharedpreferences.getString("usertype", null);
        domainname = sharedpreferences.getString("companyURL_LOGO",null);
        restoredownername = sharedpreferences.getString("companyURL_LOGO",null);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        CustVendorMasterId = sharedpreferences.getString("CustVendorMasterId",null);
        CustomerID = sharedpreferences.getString("CustVendorMasterId",null);
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(SOApproveDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql_db = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //logged in's mobile number.
        usertype = "C";

        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        listSO = new ArrayList<OrderHistoryBean>();
        temp_SODtlList_toship = new ArrayList<>();

        Intent intent = getIntent();
        soHeaderid = intent.getStringExtra("SoHeaderId");
        consigneeName = intent.getStringExtra("ConsigneeName");
        consigneeId = intent.getStringExtra("ConsigneeId");
        SONO = intent.getStringExtra("SONO");
        totOrdValue = Float.parseFloat(intent.getStringExtra("TotOrdValue"));
        DeliveryMode = intent.getStringExtra("DeliveryMode");

        try{
            FreeAboveAmt = Float.parseFloat(intent.getStringExtra("FreeAboveAmt"));
            ValPerKm = Float.parseFloat(intent.getStringExtra("ValPerKm"));
            Distance = Float.parseFloat(intent.getStringExtra("Distance"));
            MaxFreeDelDist = Float.parseFloat(intent.getStringExtra("MaxFreeDelDist"));
        }catch (Exception e){
            e.printStackTrace();
            FreeAboveAmt = 0;
            ValPerKm = 0;
            Distance = 0;
        }
    }

    public void setListeners(){

        btndisapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(parent);
                dialog.setContentView(R.layout.dialog_message);
                TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
                Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
                Button btnno = (Button) dialog.findViewById(R.id.btn_no);
                final AutoCompleteTextView edtreason =  dialog.findViewById(R.id.edtreason);
                edtreason.setVisibility(View.VISIBLE);
                txtMsg.setText("Do you really want to disapprove this order?");
                dialog.show();

                edtreason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        reason=parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String reason = edtreason.getText().toString().trim();

                        if(reason.equalsIgnoreCase("") || reason.equalsIgnoreCase(null)){
                            Toast.makeText(parent,"Enter the reason of disapprove.",Toast.LENGTH_LONG).show();
                        }else {
                            orderNonAcceptanceMethod(reason);
                        }
                    }
                });

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        btnapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temp_SODtlList_toship = SOApprDetailsAdapter.getAllSelectedSOData();

                if(temp_SODtlList_toship.isEmpty()){
                    Toast.makeText(parent,"Please select item for acceptance",Toast.LENGTH_SHORT).show();
                }else {
                    orderAcceptanceMethod();
                }
            }
        });
    }

    public void getDataFromDatabase(){
        listSO.clear();

        String itemDesc = "", itemMasterId = "", SODetailId = "", apprQty = "", appr_TotOrdVal = "",Range = "",DeliveryTerms = "",
                MRP ="",UOMDigit = "", content = "", uomcode = "",PackOfQty="0",mrpInRangeCase = "0";

        float orgQty = 0.0f, rate = 0.0f,apprLineAmt = 0.0f,LineAmt = 0.0f, rateInRangeCase = 0.0f;

        String qry = "Select * from "+DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE+" WHERE SOHeaderId='"+soHeaderid+"'";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                itemDesc = c.getString(c.getColumnIndex("ItemDesc"));
                itemMasterId = c.getString(c.getColumnIndex("ItemMasterId"));
                rate = Float.parseFloat(c.getString(c.getColumnIndex("Rate")));
                rateInRangeCase = Float.parseFloat(c.getString(c.getColumnIndex("Rate")));
                orgQty = Float.parseFloat(c.getString(c.getColumnIndex("OrgQty")));
                LineAmt = Float.parseFloat(c.getString(c.getColumnIndex("LineAmt")));
                SODetailId = c.getString(c.getColumnIndex("SODetailId"));
                Range = c.getString(c.getColumnIndex("Range"));
                MRP = c.getString(c.getColumnIndex("MRP"));
                mrpInRangeCase = c.getString(c.getColumnIndex("MRP"));
                DeliveryTerms = c.getString(c.getColumnIndex("DeliveryTerms"));
                UOMDigit = c.getString(c.getColumnIndex("UOMDigit"));
                content = c.getString(c.getColumnIndex("Content"));
                uomcode = c.getString(c.getColumnIndex("UOMCode"));
                PackOfQty = c.getString(c.getColumnIndex("PackOfQty"));

                OrderHistoryBean ordBean = new OrderHistoryBean();
                ordBean.setItemDesc(itemDesc);
                ordBean.setItemMasterId(itemMasterId);
                ordBean.setRate(rate);
                ordBean.setOrgQty(String.valueOf(orgQty));
                ordBean.setApprQty(String.valueOf(orgQty));
                ordBean.setLineAmt(LineAmt);
                ordBean.setSODetailID(SODetailId);
                ordBean.setRange(Range);
                ordBean.setMrp(Float.parseFloat(MRP));
                ordBean.setDeliveryTerms(DeliveryTerms);
                ordBean.setUOMDigit(UOMDigit);
                ordBean.setUOMCode(uomcode);
                ordBean.setContent(content);
                ordBean.setPackOfQty(PackOfQty);
                ordBean.setRateinrangecase(rate);
                ordBean.setMrpinrangecase(Float.parseFloat(MRP));

                listSO.add(ordBean);

            }while (c.moveToNext());

            dtlAdapter = new SOApprDetailsAdapter(this,listSO);
            listitems.setAdapter(dtlAdapter);

            calculate_total();

        }
    }

    public float getDelChargeVal(){
        sql_db = db.getWritableDatabase();
        float totVal = totOrdValue;
        float sumTot = 0, mrp =0, rate = 0, qty = 0;
        float delcharge = 0;
        String range="";
        //String qry = "Select SUM(LineAmt) as NetAMT from "+AnyMartDatabaseConstants.TABLE_MY_ORDER_ACCEPTANCE+" WHERE SOHeaderId='"+soHeaderid+"'";
        String qry = "Select Rate,Qty,MRP,Range from "+DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE+" WHERE SOHeaderId='"+soHeaderid+"'";
        Cursor c=sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                //sumTot = Float.parseFloat(c.getString(c.getColumnIndex("NetAMT")));
                range = c.getString(c.getColumnIndex("Range"));
                qty = Float.parseFloat(c.getString(c.getColumnIndex("Qty")));
                rate = Float.parseFloat(c.getString(c.getColumnIndex("Rate")));
                mrp = Float.parseFloat(c.getString(c.getColumnIndex("MRP")));

                if(range.equalsIgnoreCase("true")){
                    sumTot = sumTot + (qty * mrp);
                }else {
                    sumTot = sumTot + (qty * rate);
                }

            }while (c.moveToNext());
        }

        delcharge = totVal - sumTot;

        return delcharge;
    }

    public void calculate_total(){
        amt = 0;
        for(int i=0; i<listSO.size(); i++){
            amt = amt + listSO.get(i).getLineAmt();
        }

        if(DeliveryMode.equalsIgnoreCase("Door Step")){
            float delcharge = 0, amttot = 0, perkmcharge = 0;

            if(AnyMartData.MerchantName.equalsIgnoreCase("Madam Home")){
                //madam home logic

                if(Distance > MaxFreeDelDist){
                    //no delivery service
                    amt = amt;
                    delchrge.setVisibility(View.GONE);

                }else if(Distance <= MaxFreeDelDist){
                    if(amt >= FreeAboveAmt){
                        //free delivery

                        amt = amt;
                        delchrge.setVisibility(View.GONE);

                    }else if(amt < FreeAboveAmt){
                        //apply fix delivery charge

                        delcharge = 15;
                        delcharge = math(delcharge);
                        deliveryCharge = delcharge;

                        amt = amt + delcharge;
                        delchrge.setVisibility(View.VISIBLE);

                        delchrge.setText(getResources().getString(R.string.incl)+" : "+String.format("%.2f",deliveryCharge)+" ₹");
                    }
                }else {
                    amt = amt;
                    delchrge.setVisibility(View.GONE);
                }

            }else {
                //old regular logic
                if(amt < FreeAboveAmt){
                    //   amt = amt + deliveryCharge;
                    if(deliveryCharge == 0 || deliveryCharge == 0.0 || deliveryCharge == 0.00){
                        delcharge = Distance * ValPerKm;
                        delcharge = math(delcharge);
                        deliveryCharge = delcharge;
                        amt = amt + delcharge;
                        delchrge.setVisibility(View.VISIBLE);

                        delchrge.setText(getResources().getString(R.string.incl)+" : "+String.format("%.2f",deliveryCharge)+" ₹");
                    }else {
                        amt = amt + deliveryCharge;
                        delchrge.setVisibility(View.VISIBLE);

                        delchrge.setText(getResources().getString(R.string.incl)+" : "+String.format("%.2f",deliveryCharge)+" ₹");
                    }
                }else {
                    if(Distance <= MaxFreeDelDist){
                        amt = amt;
                        delchrge.setVisibility(View.GONE);
                    }else {
                        delcharge = ((Distance - MaxFreeDelDist) * ValPerKm);
                        delcharge = math(delcharge);
                        deliveryCharge = delcharge;

                        amt = amt + delcharge;
                        delchrge.setVisibility(View.VISIBLE);

                        delchrge.setText(getResources().getString(R.string.incl)+" : "+String.format("%.2f",deliveryCharge)+" ₹");
                    }
                }
            }


        }else {
            amt = amt;
        }

        String aAmt = String.format("%.2f",amt);
        double amount = Double.parseDouble(aAmt);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);

        //total_amount.setText(String.format("%.2f",amt));
        tax = getIntent().getStringExtra("Tax");
        if (tax.equalsIgnoreCase("0")||tax.equalsIgnoreCase("")||tax.equalsIgnoreCase("0.0")||tax.equalsIgnoreCase("0.0000")){
            len_tax.setVisibility(View.GONE);
            len_total.setVisibility(View.GONE);
        }else {
            len_tax.setVisibility(View.VISIBLE);
            len_total.setVisibility(View.VISIBLE);
            double Tottax=Double.parseDouble(tax);
            TotTaxAmt.setText(String.format("%.2f", Tottax));

            double TotalAmount= amount+Tottax;
            txt_tot_amount.setText(String.format("%.2f", TotalAmount));


        }
        total_amount.setText(aAmt);
    }

    public void orderAcceptanceMethod(){

        btnapprove.setAlpha((float)0.5);
        btnapprove.setText(getResources().getString(R.string.sndng));

        float totAmt = 0.0f;
        for(int j=0; j<temp_SODtlList_toship.size();j++){
            totAmt = totAmt + temp_SODtlList_toship.get(j).getLineAmt();
        }

        if(DeliveryMode.equalsIgnoreCase("Door Step")){
            if(totAmt < FreeAboveAmt){
                totAmt = totAmt + deliveryCharge;
            }else {
                totAmt = totAmt;
            }
        }else {
            totAmt = totAmt;
        }

        //Partacceptance, Nonacceptance,Fullacceptance
        //create jsonobject
        try{
            jsonMain = new JSONObject();

            if(totAmt < totOrdValue){
                jsonMain.put("StatusKey","Partacceptance");
            }else if(totAmt == totOrdValue) {
                jsonMain.put("StatusKey","Fullacceptance");
            }

            jsonMain.put("Sono",SONO);
            jsonMain.put("SOHeaderId",soHeaderid);

            if (tax.equalsIgnoreCase("0")||tax.equalsIgnoreCase("")||tax.equalsIgnoreCase("0.0")||tax.equalsIgnoreCase("0.0000")) {
                jsonMain.put("ApprTotalOrdValue",totAmt);
            }else {
                jsonMain.put("ApprTotalOrdValue",txt_tot_amount.getText().toString());
            }

            jsonMain.put("ConsigneeName",consigneeName);
            jsonMain.put("CustomerMasterId",consigneeId);
            jsonMain.put("Reason","");
            /*jsonMain.put("VendorId",AnyMartData.MerchantID);
            jsonMain.put("VendorName",AnyMartData.MerchantName);*/
            jsonMain.put("VendorId","");
            jsonMain.put("VendorName","");

            jsonArray = new JSONArray();

            //for(int i=0; i<listSO.size();i++){
            for(int i=0; i<temp_SODtlList_toship.size();i++){
                JSONObject jobj = new JSONObject();
                jobj.put("SODetailId",temp_SODtlList_toship.get(i).getSODetailID());
                jobj.put("ItemMasterId",temp_SODtlList_toship.get(i).getItemMasterId());
                jobj.put("OrgQty",temp_SODtlList_toship.get(i).getOrgQty());      //put customer requested qty in it
                jobj.put("ApprQty",temp_SODtlList_toship.get(i).getApprQty());      //put approved qty in it
                jobj.put("Rate",temp_SODtlList_toship.get(i).getRate());
                jobj.put("ApprLineAmt",temp_SODtlList_toship.get(i).getLineAmt());      //put new calculated amount in it rate * qty

                jsonArray.put(jobj);
            }

            jsonMain.put("SODtlData",jsonArray);

            if (NetworkUtils.isNetworkAvailable(SOApproveDetailActivity.this)) {
                new StartSession_tbuds(SOApproveDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostOrdAcceptanceAPI().execute("approved");
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }

            //call API to post data

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void orderNonAcceptanceMethod(String reason){

        btndisapprove.setAlpha((float)0.5);
        btndisapprove.setText(getResources().getString(R.string.sndng));

        //create jsonobject
        try{
            jsonMain = new JSONObject();
            jsonMain.put("StatusKey","Nonacceptance");
            jsonMain.put("Sono",SONO);
            jsonMain.put("SOHeaderId",soHeaderid);
            //jsonMain.put("ApprTotalOrdValue",totAmt);
            jsonMain.put("ConsigneeName",consigneeName);
            jsonMain.put("CustomerMasterId",consigneeId);
            jsonMain.put("Reason",reason);
           /* jsonMain.put("VendorId",AnyMartData.MerchantID);
            jsonMain.put("VendorName",AnyMartData.MerchantName);*/
            jsonMain.put("VendorId","");
            jsonMain.put("VendorName","");

            //call API to post data

            if (NetworkUtils.isNetworkAvailable(SOApproveDetailActivity.this)) {
                new StartSession_tbuds(SOApproveDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new PostOrdAcceptanceAPI().execute("disapproved");
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class PostOrdAcceptanceAPI extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;
        String ItemDesc = "", scheduledate = "", status = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                progressBar.setVisibility(View.VISIBLE);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            status = params[0];
            try {
                //PostOrderAcceptance
                String url = AnyMartData.MAIN_URL + AnyMartData.api_postOrderAcceptance;

                res = Utility.OpenPostConnection(url, String.valueOf(jsonMain),SOApproveDetailActivity.this);

                if (res != null) {
                    res = res.toString().replaceAll("\\\\", "");
                    response = res.toString().replaceAll("^\"+ \"+$","");
                    response = response.substring(1, response.length()-1);    //to remove first and last character from string
                }

            } catch (Exception e) {
                e.printStackTrace();
                btndisapprove.setAlpha((float)1);
                btndisapprove.setText(getResources().getString(R.string.decline));
                btnapprove.setAlpha((float)1);
                btnapprove.setText(getResources().getString(R.string.accept));
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);

            try{
                progressBar.setVisibility(View.GONE);
            }catch (Exception e){
                e.printStackTrace();
            }

            btndisapprove.setAlpha((float)1);
            btndisapprove.setText(getResources().getString(R.string.decline));
            btnapprove.setAlpha((float)1);
            btnapprove.setText(getResources().getString(R.string.accept));

            try{
                if(response.equalsIgnoreCase("true")){
                    Toast.makeText(parent,"Order "+SONO+" has been "+status+" successfully.", Toast.LENGTH_SHORT).show();

                }else {
                }

                finish();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

}
